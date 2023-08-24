package com.tencent.qcloud.tuikit.tuichat.minimalistui.component.camera.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.VideoView;

import com.tencent.qcloud.tuicore.util.FileUtil;
import com.tencent.qcloud.tuicore.util.ScreenUtil;
import com.tencent.qcloud.tuikit.tuichat.R;
import com.tencent.qcloud.tuikit.tuichat.TUIChatService;
import com.tencent.qcloud.tuikit.tuichat.config.TUIChatConfigs;
import com.tencent.qcloud.tuikit.tuichat.minimalistui.component.camera.listener.CaptureListener;
import com.tencent.qcloud.tuikit.tuichat.minimalistui.component.camera.listener.ClickListener;
import com.tencent.qcloud.tuikit.tuichat.minimalistui.component.camera.listener.ErrorListener;
import com.tencent.qcloud.tuikit.tuichat.minimalistui.component.camera.listener.CameraListener;
import com.tencent.qcloud.tuikit.tuichat.minimalistui.component.camera.listener.TypeListener;
import com.tencent.qcloud.tuikit.tuichat.minimalistui.component.camera.state.CameraMachine;
import com.tencent.qcloud.tuikit.tuichat.util.TUIChatLog;

import java.io.IOException;

public class CameraView extends FrameLayout implements CameraManager.CameraOpenOverCallback, SurfaceHolder.Callback, ICameraView {

    // 拍照浏览时候的类型
    // camera mode
    public static final int TYPE_PICTURE = 0x001;
    public static final int TYPE_VIDEO = 0x002;
    public static final int TYPE_SHORT = 0x003;
    public static final int TYPE_DEFAULT = 0x004;
    // 录制视频比特率
    // Recording video bit rate
    public static final int MEDIA_QUALITY_HIGH = 20 * 100000;
    public static final int MEDIA_QUALITY_MIDDLE = 16 * 100000;
    public static final int MEDIA_QUALITY_LOW = 12 * 100000;
    public static final int MEDIA_QUALITY_POOR = 8 * 100000;
    public static final int MEDIA_QUALITY_FUNNY = 4 * 100000;
    public static final int MEDIA_QUALITY_DESPAIR = 2 * 100000;
    public static final int MEDIA_QUALITY_SORRY = 80000;
    public static final int BUTTON_STATE_ONLY_CAPTURE = 0x101;      //只能拍照
    public static final int BUTTON_STATE_ONLY_RECORDER = 0x102;     //只能录像
    public static final int BUTTON_STATE_BOTH = 0x103;              //两者都可以
    private static final String TAG = CameraView.class.getSimpleName();
    // 闪关灯状态
    // Flash status
    private static final int TYPE_FLASH_AUTO = 0x021;
    private static final int TYPE_FLASH_ON = 0x022;
    private static final int TYPE_FLASH_OFF = 0x023;
    // Camera 状态机
    // Camera state machine
    private CameraMachine machine;
    private final int type_flash = TYPE_FLASH_OFF;
    private CameraListener jCameraLisenter;
    private ClickListener leftClickListener;
    private ClickListener rightClickListener;

    private final Context mContext;
    private VideoView mVideoView;
    private ImageView mPhoto;
    private ImageView mSwitchCamera;
    private CaptureLayout mCaptureLayout;
    private FoucsView mFoucsView;
    private MediaPlayer mMediaPlayer;

    private int layout_width;
    private float screenProp = 0f;

    private Bitmap captureBitmap;   //捕获的图片 captured picture
    private Bitmap firstFrame;      //第一帧图片 first frame picture
    private String videoUrl;        //视频URL Video URL


    // 切换摄像头按钮的参数
    // Switch camera button parameters
    private int iconSize = 0;
    private int iconMargin = 0;
    private int iconSrc = 0;
    private int iconLeft = 0;
    private int iconRight = 0;
    private int duration = 0;
    private long recordTime;

    // 缩放梯度
    // scale gradient
    private int zoomGradient = 0;

    private boolean firstTouch = true;
    private float firstTouchLength = 0;
    private ErrorListener errorLisenter;

    public CameraView(Context context) {
        this(context, null);
    }

    public CameraView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        //get AttributeSet
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.JCameraView, defStyleAttr, 0);
        iconSize = a.getDimensionPixelSize(R.styleable.JCameraView_iconSize, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 35, getResources().getDisplayMetrics()));
        iconMargin = a.getDimensionPixelSize(R.styleable.JCameraView_iconMargin, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 15, getResources().getDisplayMetrics()));
        iconSrc = a.getResourceId(R.styleable.JCameraView_iconSrc, R.drawable.chat_camera_switchcamera);
        iconLeft = a.getResourceId(R.styleable.JCameraView_iconLeft, 0);
        iconRight = a.getResourceId(R.styleable.JCameraView_iconRight, 0);
        duration = TUIChatConfigs.getConfigs().getGeneralConfig().getVideoRecordMaxTime() * 1000;
        a.recycle();
        initData();
        initView();
    }

    private void initData() {
        layout_width = ScreenUtil.getScreenWidth(mContext);
        zoomGradient = (int) (layout_width / 16f);
        TUIChatLog.i(TAG, "zoom = " + zoomGradient);
        machine = new CameraMachine(getContext(), this);
    }

    private void initView() {
        setWillNotDraw(false);
        View view = LayoutInflater.from(mContext).inflate(R.layout.chat_minimalist_input_camera_view, this);
        mVideoView = view.findViewById(R.id.video_preview);
        mPhoto = view.findViewById(R.id.image_photo);
        mSwitchCamera = view.findViewById(R.id.image_switch);
        mSwitchCamera.setImageResource(iconSrc);
        setFlashRes();
        mCaptureLayout = view.findViewById(R.id.capture_layout);
        mCaptureLayout.setDuration(duration);
        mCaptureLayout.setIconSrc(iconLeft, iconRight);
        mFoucsView = view.findViewById(R.id.fouce_view);
        mVideoView.getHolder().addCallback(this);

        mSwitchCamera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                machine.switchCamera(mVideoView.getHolder(), screenProp);
            }
        });

        mCaptureLayout.setCaptureLisenter(new CaptureListener() {
            @Override
            public void takePictures() {
                TUIChatLog.i(TAG, "takePictures");
                mSwitchCamera.setVisibility(INVISIBLE);
                machine.capture();
            }

            @Override
            public void recordStart() {
                TUIChatLog.i(TAG, "recordStart");
                mSwitchCamera.setVisibility(INVISIBLE);
                machine.record(mVideoView.getHolder().getSurface(), screenProp);
            }

            @Override
            public void recordShort(final long time) {
                TUIChatLog.i(TAG, "recordShort");
                mCaptureLayout.setTextWithAnimation(TUIChatService.getAppContext().getString(R.string.record_time_tip));
                mSwitchCamera.setVisibility(VISIBLE);
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        machine.stopRecord(true, time);
                    }
                }, 1500 - time);
            }

            @Override
            public void recordEnd(long time) {
                TUIChatLog.i(TAG, "recordEnd");
                machine.stopRecord(false, time);
                recordTime = time;
            }

            @Override
            public void recordZoom(float zoom) {
                TUIChatLog.i(TAG, "recordZoom");
                machine.zoom(zoom, CameraManager.TYPE_RECORDER);
            }

            @Override
            public void recordError() {
                TUIChatLog.i(TAG, "recordError");
                if (errorLisenter != null) {
                    errorLisenter.AudioPermissionError();
                }
            }
        });

        mCaptureLayout.setTypeLisenter(new TypeListener() {
            @Override
            public void cancel() {
                TUIChatLog.i(TAG, "capture cancel");
                machine.cancel(mVideoView.getHolder(), screenProp);
            }

            @Override
            public void confirm() {
                TUIChatLog.i(TAG, "capture confirm");
                machine.confirm();
            }
        });

        mCaptureLayout.setLeftClickListener(new ClickListener() {
            @Override
            public void onClick() {
                if (leftClickListener != null) {
                    leftClickListener.onClick();
                }
            }
        });
        mCaptureLayout.setRightClickListener(new ClickListener() {
            @Override
            public void onClick() {
                if (rightClickListener != null) {
                    rightClickListener.onClick();
                }
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float widthSize = mVideoView.getMeasuredWidth();
        float heightSize = mVideoView.getMeasuredHeight();
        if (screenProp == 0) {
            screenProp = heightSize / widthSize;
        }
    }

    @Override
    public void cameraHasOpened() {
        CameraManager.getInstance().doStartPreview(mVideoView.getHolder(), screenProp);
    }

    public void onResume() {
        TUIChatLog.i(TAG, "CameraView onResume");
        resetState(TYPE_DEFAULT);
        CameraManager.getInstance().registerSensorManager(mContext);
        machine.start(mVideoView.getHolder(), screenProp);
    }

    public void onPause() {
        TUIChatLog.i(TAG, "CameraView onPause");
        machine.stop();
        CameraManager.getInstance().unregisterSensorManager(mContext);
    }

    public void onDestroy() {
        stopVideo();
        resetState(TYPE_PICTURE);
        CameraManager.getInstance().isPreview(false);
        CameraManager.getInstance().unregisterSensorManager(mContext);
        CameraManager.destroyCameraInterface();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        TUIChatLog.i(TAG, "CameraView SurfaceCreated");
        new Thread() {
            @Override
            public void run() {
                CameraManager.getInstance().doOpenCamera(CameraView.this);
            }
        }.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        TUIChatLog.i(TAG, "CameraView SurfaceDestroyed");
        CameraManager.getInstance().doDestroyCamera();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getPointerCount() == 1) {
                    setFocusViewWidthAnimation(event.getX(), event.getY());
                }
                if (event.getPointerCount() == 2) {
                    TUIChatLog.i(TAG, "ACTION_DOWN = " + 2);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() == 1) {
                    firstTouch = true;
                }
                if (event.getPointerCount() == 2) {
                    float point_1_X = event.getX(0);
                    float point_1_Y = event.getY(0);
                    float point_2_X = event.getX(1);
                    float point_2_Y = event.getY(1);

                    float result = (float) Math.sqrt(Math.pow(point_1_X - point_2_X, 2) + Math.pow(point_1_Y -
                            point_2_Y, 2));

                    if (firstTouch) {
                        firstTouchLength = result;
                        firstTouch = false;
                    }
                    if ((int) (result - firstTouchLength) / zoomGradient != 0) {
                        firstTouch = true;
                        machine.zoom(result - firstTouchLength, CameraManager.TYPE_CAPTURE);
                    }
//                    TUIChatLog.i(TAG, "result = " + (result - firstTouchLength));
                }
                break;
            case MotionEvent.ACTION_UP:
                firstTouch = true;
                break;
        }
        return true;
    }

    private void setFocusViewWidthAnimation(float x, float y) {
        machine.focus(x, y, new CameraManager.FocusCallback() {
            @Override
            public void focusSuccess() {
                mFoucsView.setVisibility(INVISIBLE);
            }
        });
    }

    private void updateVideoViewSize(float videoWidth, float videoHeight) {
        if (videoWidth > videoHeight) {
            LayoutParams videoViewParam;
            int height = (int) ((videoHeight / videoWidth) * getWidth());
            videoViewParam = new LayoutParams(LayoutParams.MATCH_PARENT, height);
            videoViewParam.gravity = Gravity.CENTER;
            mVideoView.setLayoutParams(videoViewParam);
        }
    }

    public void setJCameraLisenter(CameraListener jCameraLisenter) {
        this.jCameraLisenter = jCameraLisenter;
    }

    public void setErrorLisenter(ErrorListener errorLisenter) {
        this.errorLisenter = errorLisenter;
        CameraManager.getInstance().setErrorLinsenter(errorLisenter);
    }

    public void setFeatures(int state) {
        this.mCaptureLayout.setButtonFeatures(state);
    }

    public void setMediaQuality(int quality) {
        CameraManager.getInstance().setMediaQuality(quality);
    }

    @Override
    public void resetState(int type) {
        switch (type) {
            case TYPE_VIDEO:
                stopVideo();
                FileUtil.deleteFile(videoUrl);
                mVideoView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                machine.start(mVideoView.getHolder(), screenProp);
                break;
            case TYPE_PICTURE:
                mPhoto.setVisibility(INVISIBLE);
                break;
            case TYPE_SHORT:
                break;
            case TYPE_DEFAULT:
                mVideoView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                break;
        }
        mSwitchCamera.setVisibility(VISIBLE);
        //mFlashLamp.setVisibility(VISIBLE);
        mCaptureLayout.resetCaptureLayout();
    }

    @Override
    public void confirmState(int type) {
        switch (type) {
            case TYPE_VIDEO:
                stopVideo();
                mVideoView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                machine.start(mVideoView.getHolder(), screenProp);
                if (jCameraLisenter != null) {
                    jCameraLisenter.recordSuccess(videoUrl, firstFrame, recordTime);
                }
                break;
            case TYPE_PICTURE:
                mPhoto.setVisibility(INVISIBLE);
                if (jCameraLisenter != null) {
                    jCameraLisenter.captureSuccess(captureBitmap);
                }
                break;
            case TYPE_SHORT:
                break;
            case TYPE_DEFAULT:
                break;
        }
        mCaptureLayout.resetCaptureLayout();
    }

    @Override
    public void showPicture(Bitmap bitmap, boolean isVertical) {
        if (isVertical) {
            mPhoto.setScaleType(ImageView.ScaleType.FIT_XY);
        } else {
            mPhoto.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
        captureBitmap = bitmap;
        mPhoto.setImageBitmap(bitmap);
        mPhoto.setVisibility(VISIBLE);
        mCaptureLayout.startAlphaAnimation();
        mCaptureLayout.startTypeBtnAnimator();
    }

    @Override
    public void playVideo(Bitmap firstFrame, final String url) {
        videoUrl = url;
        CameraView.this.firstFrame = firstFrame;
        try {
            if (mMediaPlayer == null) {
                mMediaPlayer = new MediaPlayer();
            } else {
                mMediaPlayer.reset();
            }
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.setSurface(mVideoView.getHolder().getSurface());
            mMediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer
                    .OnVideoSizeChangedListener() {
                @Override
                public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                    updateVideoViewSize(mMediaPlayer.getVideoWidth(), mMediaPlayer.getVideoHeight());
                }
            });
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.start();
                }
            });
            mMediaPlayer.setLooping(true);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stopVideo() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void setTip(String tip) {
        mCaptureLayout.setTip(tip);
    }

    @Override
    public void startPreviewCallback() {
        TUIChatLog.i(TAG, "startPreviewCallback");
        handlerFoucs(mFoucsView.getWidth() / 2, mFoucsView.getHeight() / 2);
    }

    @Override
    public boolean handlerFoucs(float x, float y) {
        if (y > mCaptureLayout.getTop()) {
            return false;
        }
        mFoucsView.setVisibility(VISIBLE);
        if (x < mFoucsView.getWidth() / 2) {
            x = mFoucsView.getWidth() / 2;
        }
        if (x > layout_width - mFoucsView.getWidth() / 2) {
            x = layout_width - mFoucsView.getWidth() / 2;
        }
        if (y < mFoucsView.getWidth() / 2) {
            y = mFoucsView.getWidth() / 2;
        }
        if (y > mCaptureLayout.getTop() - mFoucsView.getWidth() / 2) {
            y = mCaptureLayout.getTop() - mFoucsView.getWidth() / 2;
        }
        mFoucsView.setX(x - mFoucsView.getWidth() / 2);
        mFoucsView.setY(y - mFoucsView.getHeight() / 2);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mFoucsView, "scaleX", 1, 0.6f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mFoucsView, "scaleY", 1, 0.6f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(mFoucsView, "alpha", 1f, 0.4f, 1f, 0.4f, 1f, 0.4f, 1f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(scaleX).with(scaleY).before(alpha);
        animSet.setDuration(400);
        animSet.start();
        return true;
    }

    public void setLeftClickListener(ClickListener clickListener) {
        this.leftClickListener = clickListener;
    }

    public void setRightClickListener(ClickListener clickListener) {
        this.rightClickListener = clickListener;
    }

    private void setFlashRes() {
        switch (type_flash) {
            case TYPE_FLASH_AUTO:
                machine.flash(Camera.Parameters.FLASH_MODE_AUTO);
                break;
            case TYPE_FLASH_ON:
                machine.flash(Camera.Parameters.FLASH_MODE_ON);
                break;
            case TYPE_FLASH_OFF:
                machine.flash(Camera.Parameters.FLASH_MODE_OFF);
                break;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
