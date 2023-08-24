package com.daychat.live.ui.act.video;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;

import com.daychat.live.R;
import com.daychat.live.util.Config;
import com.daychat.live.util.GetPathFromUri;
import com.daychat.live.util.MediaStoreUtils;
import com.daychat.live.view.CustomProgressDialog;
import com.daychat.live.widget.ToastUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.qiniu.pili.droid.shortvideo.PLMediaFile;
import com.qiniu.pili.droid.shortvideo.PLShortVideoTrimmer;
import com.qiniu.pili.droid.shortvideo.PLVideoFrame;
import com.qiniu.pili.droid.shortvideo.PLVideoSaveListener;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class VideoTrimActivity extends Activity {
    private static final String TAG = "VideoTrimActivity";

    private static final int SLICE_COUNT = 8;

    private PLShortVideoTrimmer mShortVideoTrimmer;
    private PLMediaFile mMediaFile;

    private LinearLayout mFrameListView;
    private View mHandlerLeft;
    private View mHandlerRight;

    private CustomProgressDialog mProcessingDialog;
    private VideoView mPreview;

    private long mSelectedBeginMs;
    private long mSelectedEndMs;
    private long mDurationMs;

    private int mVideoFrameCount;
    private int mSlicesTotalLength;

    private final Handler mHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mProcessingDialog = new CustomProgressDialog(this);
        mProcessingDialog.setOnCancelListener(dialog -> mShortVideoTrimmer.cancelTrim());

//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
//        intent.setType("video/*");
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        startActivityForResult(intent, 0);
        PictureSelector.create(this).openGallery(PictureConfig.TYPE_VIDEO).selectionMode(PictureConfig.SINGLE).forResult(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        play();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTrackPlayProgress();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mShortVideoTrimmer != null) {
            mShortVideoTrimmer.destroy();
        }
        if (mMediaFile != null) {
            mMediaFile.release();
        }
    }

    private void startTrackPlayProgress() {
        stopTrackPlayProgress();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mPreview.getCurrentPosition() >= mSelectedEndMs) {
                    mPreview.seekTo((int) mSelectedBeginMs);
                }
                mHandler.postDelayed(this, 100);
            }
        }, 100);
    }

    private void stopTrackPlayProgress() {
        mHandler.removeCallbacksAndMessages(null);
    }

    private void play() {
        if (mPreview != null) {
            mPreview.seekTo((int) mSelectedBeginMs);
            mPreview.start();
            startTrackPlayProgress();
        }
    }

    private void init(String videoPath) {
        setContentView(R.layout.activity_trim);
        TextView duration = findViewById(R.id.duration);
        mPreview = findViewById(R.id.preview);

        mShortVideoTrimmer = new PLShortVideoTrimmer(this, videoPath, Config.TRIM_FILE_PATH);
        mMediaFile = new PLMediaFile(videoPath);

        mSelectedEndMs = mDurationMs = mMediaFile.getDurationMs();
        duration.setText("时长: " + formatTime(mDurationMs));
        Log.i(TAG, "video duration: " + mDurationMs);

        mVideoFrameCount = mMediaFile.getVideoFrameCount(false);
        Log.i(TAG, "video frame count: " + mVideoFrameCount);

        adjustSurfaceSize();
        mPreview.setVideoPath(videoPath);
        mPreview.setOnCompletionListener(mediaPlayer -> play());

        initVideoFrameList();
    }

    private void adjustSurfaceSize() {
        WindowManager windowManager = (WindowManager) VideoTrimActivity.this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);

        int screenWidth = outMetrics.widthPixels;
        int videoWidth, videoHeight;
        if (mMediaFile.getVideoRotation() / 90 % 2 == 1) {
            videoWidth = mMediaFile.getVideoHeight();
            videoHeight = mMediaFile.getVideoWidth();
        } else {
            videoWidth = mMediaFile.getVideoWidth();
            videoHeight = mMediaFile.getVideoHeight();
        }
        float videoAspectRatio = (float) videoWidth / videoHeight;
        int displayWidth = 0, displayHeight = 0;
        if (videoAspectRatio < 1) {
            // 竖屏视频
            displayWidth = (int) (screenWidth * videoAspectRatio);
            displayHeight = screenWidth;
        } else if (videoAspectRatio > 1) {
            // 横屏视频
            displayWidth = screenWidth;
            displayHeight = (int) (screenWidth / videoAspectRatio);
        } else {
            displayWidth = screenWidth;
            displayHeight = screenWidth;
        }
        ViewGroup.LayoutParams layoutParams = mPreview.getLayoutParams();
        layoutParams.height = displayHeight;
        layoutParams.width = displayWidth;
        mPreview.setLayoutParams(layoutParams);
    }

    private void initVideoFrameList() {
        mFrameListView = findViewById(R.id.video_frame_list);
        mHandlerLeft = findViewById(R.id.handler_left);
        mHandlerRight = findViewById(R.id.handler_right);

        mHandlerLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                float viewX = v.getX();
                float movedX = event.getX();
                float finalX = viewX + movedX;
                updateHandlerLeftPosition(finalX);

                if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                    calculateRange();
                }
                return true;
            }
        });

        mHandlerRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                float viewX = v.getX();
                float movedX = event.getX();
                float finalX = viewX + movedX;
                updateHandlerRightPosition(finalX);

                if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                    calculateRange();
                }
                return true;
            }
        });

        mFrameListView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mFrameListView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                final int sliceEdge = mFrameListView.getWidth() / SLICE_COUNT;
                mSlicesTotalLength = sliceEdge * SLICE_COUNT;
                Log.i(TAG, "slice edge: " + sliceEdge);
                final float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());

                new AsyncTask<Void, PLVideoFrame, Void>() {
                    @Override
                    protected Void doInBackground(Void... v) {
                        for (int i = 0; i < SLICE_COUNT; ++i) {
                            PLVideoFrame frame = mMediaFile.getVideoFrameByTime((long) ((1.0f * i / SLICE_COUNT) * mDurationMs), true, sliceEdge, sliceEdge);
                            runOnUiThread(() -> onProgressUpdate(frame));
                        }
                        return null;
                    }

                    @Override
                    protected void onProgressUpdate(PLVideoFrame... values) {
                        super.onProgressUpdate(values);
                        PLVideoFrame frame = values[0];
                        if (frame != null) {
                            View root = LayoutInflater.from(VideoTrimActivity.this).inflate(R.layout.frame_item, null);

                            int rotation = frame.getRotation();
                            ImageView thumbnail = root.findViewById(R.id.thumbnail);
                            thumbnail.setImageBitmap(frame.toBitmap());
                            thumbnail.setRotation(rotation);
                            FrameLayout.LayoutParams thumbnailLP = (FrameLayout.LayoutParams) thumbnail.getLayoutParams();
                            if (rotation == 90 || rotation == 270) {
                                thumbnailLP.leftMargin = thumbnailLP.rightMargin = (int) px;
                            } else {
                                thumbnailLP.topMargin = thumbnailLP.bottomMargin = (int) px;
                            }
                            thumbnail.setLayoutParams(thumbnailLP);

                            LinearLayout.LayoutParams rootLP = new LinearLayout.LayoutParams(sliceEdge, sliceEdge);
                            mFrameListView.addView(root, rootLP);
                        }
                    }
                }.execute();
            }
        });
    }

    private void updateHandlerLeftPosition(float movedPosition) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mHandlerLeft.getLayoutParams();
        if ((movedPosition + mHandlerLeft.getWidth()) > mHandlerRight.getX()) {
            lp.leftMargin = (int) (mHandlerRight.getX() - mHandlerLeft.getWidth());
        } else if (movedPosition < 0) {
            lp.leftMargin = 0;
        } else {
            lp.leftMargin = (int) movedPosition;
        }
        mHandlerLeft.setLayoutParams(lp);
    }

    private void updateHandlerRightPosition(float movedPosition) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mHandlerRight.getLayoutParams();
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        if (movedPosition < (mHandlerLeft.getX() + mHandlerLeft.getWidth())) {
            lp.leftMargin = (int) (mHandlerLeft.getX() + mHandlerLeft.getWidth());
        } else if ((movedPosition + (mHandlerRight.getWidth() / 2)) > (mFrameListView.getX() + mSlicesTotalLength)) {
            lp.leftMargin = (int) ((mFrameListView.getX() + mSlicesTotalLength) - (mHandlerRight.getWidth() / 2));
        } else {
            lp.leftMargin = (int) movedPosition;
        }
        mHandlerRight.setLayoutParams(lp);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK ) {
            List<LocalMedia> medias = PictureSelector.obtainMultipleResult(data);
            if (medias!=null && !medias.isEmpty()){
                String selectedFilepath = GetPathFromUri.getRealPathFromURI(this, Uri.parse(medias.get(0).getPath()));
                if (selectedFilepath != null && !"".equals(selectedFilepath)) {
                    init(selectedFilepath);
                }
            }else {
                finish();
            }
//            String selectedFilepath = GetPathFromUri.getRealPathFromURI(this, data.getData());
//            Log.i(TAG, "Select file: " + selectedFilepath);

        } else {
            finish();
        }
    }

    private float clamp(float origin) {
        if (origin < 0) {
            return 0;
        }
        if (origin > 1) {
            return 1;
        }
        return origin;
    }

    private void calculateRange() {
        float beginPercent = 1.0f * ((mHandlerLeft.getX() + mHandlerLeft.getWidth() / 2) - mFrameListView.getX()) / mSlicesTotalLength;
        float endPercent = 1.0f * ((mHandlerRight.getX() + mHandlerRight.getWidth() / 2) - mFrameListView.getX()) / mSlicesTotalLength;
        beginPercent = clamp(beginPercent);
        endPercent = clamp(endPercent);

        Log.i(TAG, "begin percent: " + beginPercent + " end percent: " + endPercent);

        mSelectedBeginMs = (long) (beginPercent * mDurationMs);
        mSelectedEndMs = (long) (endPercent * mDurationMs);

        Log.i(TAG, "new range: " + mSelectedBeginMs + "-" + mSelectedEndMs);
        updateRangeText();
        play();
    }

    public void onDone(View v) {
        Log.i(TAG, "trim to file path: " + Config.TRIM_FILE_PATH + " range: " + mSelectedBeginMs + " - " + mSelectedEndMs);
        mProcessingDialog.show();
        mProcessingDialog.setProgress(0);

        PLShortVideoTrimmer.TRIM_MODE mode = ((RadioButton) findViewById(R.id.mode_fast)).isChecked() ? PLShortVideoTrimmer.TRIM_MODE.FAST : PLShortVideoTrimmer.TRIM_MODE.ACCURATE;
        mShortVideoTrimmer.trim(mSelectedBeginMs, mSelectedEndMs, mode, new PLVideoSaveListener() {
            @Override
            public void onSaveVideoSuccess(String path) {
                MediaStoreUtils.storeVideo(VideoTrimActivity.this, new File(path), "video/mp4");
                mProcessingDialog.dismiss();
                VideoEditActivity.start(VideoTrimActivity.this, path);
            }

            @Override
            public void onSaveVideoFailed(final int errorCode) {
                runOnUiThread(() -> {
                    mProcessingDialog.dismiss();
                    ToastUtils.toastErrorCode(errorCode);
                });
            }

            @Override
            public void onSaveVideoCanceled() {
                mProcessingDialog.dismiss();
            }

            @Override
            public void onProgressUpdate(final float percentage) {
                runOnUiThread(() -> mProcessingDialog.setProgress((int) (100 * percentage)));
            }
        });
    }

    public void onBack(View v) {
        finish();
    }

    private String formatTime(long timeMs) {
        return String.format(Locale.CHINA, "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(timeMs),
                TimeUnit.MILLISECONDS.toSeconds(timeMs) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeMs))
        );
    }

    private void updateRangeText() {
        TextView range = findViewById(R.id.range);
        range.setText("剪裁范围: " + formatTime(mSelectedBeginMs) + " - " + formatTime(mSelectedEndMs));
    }
}
