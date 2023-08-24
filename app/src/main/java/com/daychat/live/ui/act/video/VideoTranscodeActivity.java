package com.daychat.live.ui.act.video;

import static com.daychat.live.ui.act.video.VideoDubActivity.MP4_PATH;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.daychat.live.R;
import com.daychat.live.ui.act.ShortVideoPublishActivity;
import com.daychat.live.util.Config;
import com.daychat.live.util.GetPathFromUri;
import com.daychat.live.util.MediaStoreUtils;
import com.daychat.live.util.RecordSettings;
import com.daychat.live.util.VideoUtils;
import com.daychat.live.view.CustomProgressDialog;
import com.daychat.live.widget.ToastUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.qiniu.android.utils.StringUtils;
import com.qiniu.pili.droid.shortvideo.PLMediaFile;
import com.qiniu.pili.droid.shortvideo.PLShortVideoTranscoder;
import com.qiniu.pili.droid.shortvideo.PLVideoSaveListener;
import com.qiniu.pili.droid.shortvideo.PLWatermarkSetting;

import java.io.File;
import java.util.List;

public class VideoTranscodeActivity extends AppCompatActivity {
    private static final String TAG = "VideoTranscodeActivity";

    private static final int REQUEST_MIX_AUDIO = 100;

    private CustomProgressDialog mProcessingDialog;

    private PLShortVideoTranscoder mShortVideoTranscoder;
    private PLMediaFile mMediaFile;
    private TextView mVideoFilePathText;
    private TextView mVideoSizeText;
    private TextView mVideoRotationText;
    private TextView mVideoSizeRotatedText;
    private TextView mVideoBitrateText;

    private EditText mTranscodingWidthEditText;
    private EditText mTranscodingHeightEditText;

    private EditText mTranscodingClipXText;
    private EditText mTranscodingClipYText;
    private EditText mTranscodingClipWidthText;
    private EditText mTranscodingClipHeightText;

    private EditText mTranscodingBitrateText;
    private Spinner mTranscodingRotationSpinner;
    private EditText mTranscodingMaxFPSEditText;

    private TextView mMixAudioFileText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("VideoTranscode");
        setContentView(R.layout.activity_transcode);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }

        setTitle(R.string.title_transcode);

        mMixAudioFileText = findViewById(R.id.tv_mix_audio_file);
        mVideoFilePathText = findViewById(R.id.tv_src_video_path);
        mVideoSizeText = findViewById(R.id.tv_src_video_size);
        mVideoRotationText = findViewById(R.id.tv_src_video_rotation);
        mVideoSizeRotatedText = findViewById(R.id.tv_src_video_size_rotation);
        mVideoBitrateText = findViewById(R.id.tv_src_video_bitrate);

        mTranscodingWidthEditText = findViewById(R.id.et_width);
        mTranscodingHeightEditText = findViewById(R.id.et_height);
        mTranscodingMaxFPSEditText = findViewById(R.id.et_max_fps);

        mTranscodingClipXText = findViewById(R.id.et_clip_x);
        mTranscodingClipYText = findViewById(R.id.et_clip_y);
        mTranscodingClipWidthText = findViewById(R.id.et_clip_width);
        mTranscodingClipHeightText = findViewById(R.id.et_clip_height);

        mTranscodingBitrateText = findViewById(R.id.et_bitrate);
        mTranscodingRotationSpinner = findViewById(R.id.spinner_rotation);

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, RecordSettings.ROTATION_LEVEL_TIPS_ARRAY);
        mTranscodingRotationSpinner.setAdapter(adapter);
        mTranscodingRotationSpinner.setSelection(0);

        mProcessingDialog = new CustomProgressDialog(this);
        mProcessingDialog.setOnCancelListener(dialog -> mShortVideoTranscoder.cancelTranscode());

        ((AppCompatCheckBox) findViewById(R.id.cb_add_watermark)).setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mShortVideoTranscoder.setWatermark(createWatermarkSetting());
            } else {
                mShortVideoTranscoder.setWatermark(null);
            }
        });

        ((AppCompatCheckBox) findViewById(R.id.cb_hw_encode)).setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mShortVideoTranscoder.setHWEncodeEnable(true);
            } else {
                mShortVideoTranscoder.setHWEncodeEnable(false);
            }
        });
        PictureSelector.create(this).openGallery(PictureConfig.TYPE_VIDEO).selectionMode(PictureConfig.SINGLE).forResult(0);
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
//        intent.setType("video/*");
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        startActivityForResult(intent, 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK ) {
            if (requestCode == REQUEST_MIX_AUDIO && data.getData() != null) {
                //增加混音文件
                String selectedFilepath = GetPathFromUri.getRealPathFromURI(this, data.getData());
                Log.i(TAG, "Select mix audio file: " + selectedFilepath);
                if (!StringUtils.isNullOrEmpty(selectedFilepath)) {
                    onMixAudioFileSelected(selectedFilepath);
                    return;
                }
            } else {
                List<LocalMedia> medias = PictureSelector.obtainMultipleResult(data);
                if (medias!=null && !medias.isEmpty()){
                    String selectedFilepath = GetPathFromUri.getRealPathFromURI(this, Uri.parse(medias.get(0).getPath()));
                    if (selectedFilepath != null && !"".equals(selectedFilepath)) {
                        if (!StringUtils.isNullOrEmpty(selectedFilepath)) {
                            onVideoFileSelected(selectedFilepath);
                            return;
                        }
                    }
                }else {
                    finish();
                }
//                String selectedFilepath = GetPathFromUri.getRealPathFromURI(this, data.getData());
//                Log.i(TAG, "Select file: " + selectedFilepath);

            }
        }

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaFile != null) {
            mMediaFile.release();
        }
    }

    private void onMixAudioFileSelected(String filepath) {
        mMixAudioFileText.setText(filepath);
        PLMediaFile mediaFile = new PLMediaFile(filepath);
        mShortVideoTranscoder.setMixAudioFile(filepath, 0, mediaFile.getDurationMs(), true);
    }

    private void onVideoFileSelected(String filepath) {
        mShortVideoTranscoder = new PLShortVideoTranscoder(this, filepath, Config.TRANSCODE_FILE_PATH);
        mMediaFile = new PLMediaFile(filepath);
        int bitrateInKbps = mMediaFile.getVideoBitrate() / 1000;
        int videoWidthRaw = mMediaFile.getVideoWidth();
        int videoHeightRaw = mMediaFile.getVideoHeight();
        int videoRotation = mMediaFile.getVideoRotation();
        int videoWidthRotated = videoRotation == 0 || videoRotation == 180 ? mMediaFile.getVideoWidth() : mMediaFile.getVideoHeight();
        int videoHeightRotated = videoRotation == 0 || videoRotation == 180 ? mMediaFile.getVideoHeight() : mMediaFile.getVideoWidth();
        int videoFrameRate = mMediaFile.getVideoFrameRate();

        mVideoFilePathText.setText(new File(filepath).getName());
        mVideoSizeText.setText(videoWidthRaw + " x " + videoHeightRaw);
        mVideoRotationText.setText("" + videoRotation);
        mVideoSizeRotatedText.setText(videoWidthRotated + " x " + videoHeightRotated);
        mVideoBitrateText.setText(bitrateInKbps + " kbps");

        mTranscodingWidthEditText.setText(String.valueOf(videoWidthRaw));
        mTranscodingHeightEditText.setText(String.valueOf(videoHeightRaw));
        mTranscodingMaxFPSEditText.setText(String.valueOf(videoFrameRate));
        mTranscodingClipWidthText.setText(String.valueOf(videoWidthRotated));
        mTranscodingClipHeightText.setText(String.valueOf(videoHeightRotated));
        mTranscodingBitrateText.setText(String.valueOf(bitrateInKbps));
    }

    public void onClickAddMixAudio(View c) {
        chooseMixAudioFile();
    }

    public void onClickTranscode(View v) {
        doTranscode(false);
    }

    public void onClickReverse(View v) {
        doTranscode(true);
    }

    private void doTranscode(boolean isReverse) {
        if (mShortVideoTranscoder == null) {
            ToastUtils.showShortToast("请先选择转码文件！");
            return;
        }

        int transcodingBitrate = Integer.parseInt(mTranscodingBitrateText.getText().toString()) * 1000;
        int transcodingRotationLevel = mTranscodingRotationSpinner.getSelectedItemPosition();
        int transcodingWidth = Integer.parseInt(mTranscodingWidthEditText.getText().toString());
        int transcodingHeight = Integer.parseInt(mTranscodingHeightEditText.getText().toString());
        int transcodingMaxFPS = Integer.parseInt(mTranscodingMaxFPSEditText.getText().toString());
        if (transcodingMaxFPS > 0) {
            mShortVideoTranscoder.setMaxFrameRate(transcodingMaxFPS);
        }

        int clipWidth = Integer.parseInt(mTranscodingClipWidthText.getText().toString());
        int clipHeight = Integer.parseInt(mTranscodingClipHeightText.getText().toString());
        if (clipWidth > 0 && clipHeight > 0) {
            int clipX = Integer.parseInt(mTranscodingClipXText.getText().toString());
            int clipY = Integer.parseInt(mTranscodingClipYText.getText().toString());
            mShortVideoTranscoder.setClipArea(clipX, clipY, clipWidth, clipHeight);
        }

        boolean startResult = mShortVideoTranscoder.transcode(
                transcodingWidth, transcodingHeight, transcodingBitrate,
                RecordSettings.ROTATION_LEVEL_ARRAY[transcodingRotationLevel],
                isReverse, new PLVideoSaveListener() {
                    @Override
                    public void onSaveVideoSuccess(final String filePath) {
                        Log.i(TAG, "save success: " + filePath);
                        MediaStoreUtils.storeVideo(VideoTranscodeActivity.this, new File(filePath), "video/mp4");
                        runOnUiThread(() -> {
                            mProcessingDialog.dismiss();
                            showChooseDialog(filePath);
                        });
                    }

                    @Override
                    public void onSaveVideoFailed(final int errorCode) {
                        Log.i(TAG, "save failed: " + errorCode);
                        runOnUiThread(() -> {
                            mProcessingDialog.dismiss();
                            ToastUtils.toastErrorCode(errorCode);
                        });
                    }

                    @Override
                    public void onSaveVideoCanceled() {
                        runOnUiThread(() -> mProcessingDialog.dismiss());
                    }

                    @Override
                    public void onProgressUpdate(final float percentage) {
                        runOnUiThread(() -> mProcessingDialog.setProgress((int) (100 * percentage)));
                    }
                });

        if (startResult) {
            mProcessingDialog.show();
            mProcessingDialog.setProgress(0);
        } else {
            ToastUtils.showShortToast("开始转码失败！");
        }
    }

    private void showChooseDialog(final String filePath) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.if_edit_video));
        builder.setPositiveButton(getString(R.string.dlg_yes), (dialog, which) -> VideoEditActivity.start(VideoTranscodeActivity.this, filePath));
        builder.setNegativeButton(getString(R.string.dlg_no), (dialog, which) -> {
            long duration = VideoUtils.getDuration(filePath);
            Intent intent = new Intent(VideoTranscodeActivity.this, ShortVideoPublishActivity.class);
            intent.putExtra(MP4_PATH,filePath);
            intent.putExtra("video_duration",duration);
            startActivity(intent);
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    private PLWatermarkSetting createWatermarkSetting() {
        PLWatermarkSetting watermarkSetting = new PLWatermarkSetting();
        watermarkSetting.setResourceId(R.mipmap.ic_logo);
        watermarkSetting.setPosition(0.01f, 0.01f);
        watermarkSetting.setAlpha(128);
        return watermarkSetting;
    }

    private void chooseMixAudioFile() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("audio/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_MIX_AUDIO);
    }
}
