package com.daychat.live.ui.act.video;

import static com.daychat.live.ui.act.video.VideoDubActivity.MP4_PATH;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Movie;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.daychat.live.R;
import com.daychat.live.ui.act.ShortVideoPublishActivity;
import com.daychat.live.ui.act.video.adapter.ComposeItemListAdapter;
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
import com.qiniu.pili.droid.shortvideo.PLComposeItem;
import com.qiniu.pili.droid.shortvideo.PLShortVideoComposer;
import com.qiniu.pili.droid.shortvideo.PLVideoEncodeSetting;
import com.qiniu.pili.droid.shortvideo.PLVideoSaveListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class MultipleComposeActivity extends AppCompatActivity {

    private static final String TAG = "MultipleComposeActivity";
    public static final int REQUEST_AUDIO_CODE = 101;
    public static final int REQUEST_VIDEO_CODE = 102;
    public static final int REQUEST_IMAGE_CODE = 103;

    private CustomProgressDialog mProcessingDialog;
    private PLShortVideoComposer mShortVideoComposer;

    private ComposeItemListAdapter mItemListAdapter;
    private ListView mVideoListView;

    private Spinner mEncodingSizeLevelSpinner;
    private Spinner mEncodingBitrateLevelSpinner;

    private String mMixFilePath;
    private int mDeletePosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_compose);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }

        setTitle(R.string.title_multiple_compose);

        mVideoListView = findViewById(R.id.VideoListView);
        mItemListAdapter = new ComposeItemListAdapter(this);
        mVideoListView.setAdapter(mItemListAdapter);

        mEncodingSizeLevelSpinner = findViewById(R.id.EncodingSizeLevelSpinner);
        mEncodingBitrateLevelSpinner = findViewById(R.id.EncodingBitrateLevelSpinner);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, RecordSettings.ENCODING_SIZE_LEVEL_TIPS_ARRAY);
        mEncodingSizeLevelSpinner.setAdapter(adapter1);
        mEncodingSizeLevelSpinner.setSelection(7);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, RecordSettings.ENCODING_BITRATE_LEVEL_TIPS_ARRAY);
        mEncodingBitrateLevelSpinner.setAdapter(adapter2);
        mEncodingBitrateLevelSpinner.setSelection(2);

        mShortVideoComposer = new PLShortVideoComposer(this);

        mProcessingDialog = new CustomProgressDialog(this);
        mProcessingDialog.setOnCancelListener(dialog -> mShortVideoComposer.cancelComposeItems());

        mVideoListView.setOnCreateContextMenuListener((menu, v, menuInfo) -> menu.add(0, 0, 0, "删除"));

        mVideoListView.setOnItemLongClickListener((parent, view, position, id) -> {
            mDeletePosition = position;
            return false;
        });
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
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                mItemListAdapter.removeItem(mDeletePosition);
                mItemListAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
        return true;
    }

    public void onClickAddVideo(View v) {
        chooseVideoFile();
    }

    public void onClickAddImage(View v) {
        chooseImageFile();
    }

    public void onClickCompose(View v) {
        List<PLComposeItem> itemList = mItemListAdapter.getItemList();
        PLVideoEncodeSetting setting = new PLVideoEncodeSetting(this);
        setting.setEncodingSizeLevel(getEncodingSizeLevel(mEncodingSizeLevelSpinner.getSelectedItemPosition()));
        setting.setEncodingBitrate(getEncodingBitrateLevel(mEncodingBitrateLevelSpinner.getSelectedItemPosition()));
        setting.setEncodingFps(25);
        if (mShortVideoComposer.composeItems(itemList, Config.COMPOSE_FILE_PATH, setting, mMixFilePath, 1, 1, mVideoSaveListener)) {
            mProcessingDialog.show();
            mProcessingDialog.setProgress(0);
        } else {
            ToastUtils.showShortToast("开始拼接失败！");
        }
    }

    private final PLVideoSaveListener mVideoSaveListener = new PLVideoSaveListener() {
        @Override
        public void onSaveVideoSuccess(String filepath) {
            MediaStoreUtils.storeVideo(MultipleComposeActivity.this, new File(filepath), "video/mp4");
            mProcessingDialog.dismiss();
//            PlaybackActivity.start(MultipleComposeActivity.this, filepath);
            long duration = VideoUtils.getDuration(filepath);
            Intent intent = new Intent(MultipleComposeActivity.this, ShortVideoPublishActivity.class);
            intent.putExtra(MP4_PATH,filepath);
            intent.putExtra("video_duration",duration);
            startActivity(intent);
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
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK ) {

            switch (requestCode) {
                case REQUEST_AUDIO_CODE: {
                    String selectedFilepath = GetPathFromUri.getRealPathFromURI(this, data.getData());
                    if (selectedFilepath == null || "".equals(selectedFilepath)) {
                        Log.i(TAG, "Select file error : can't be null or empty !");
                        return;
                    }
                    Log.i(TAG, "Select file: " + selectedFilepath);
                    if (data.getData() != null) {
                        mMixFilePath = selectedFilepath;
                    }
                    break;
                }
                case REQUEST_VIDEO_CODE: {
                    List<LocalMedia> medias = PictureSelector.obtainMultipleResult(data);
                    if (medias!=null && !medias.isEmpty()){
                        String selectedFilepath = GetPathFromUri.getRealPathFromURI(this, Uri.parse(medias.get(0).getPath()));
                        PLComposeItem item = createVideoItem(selectedFilepath);
                        mItemListAdapter.addItem(item);
                        mItemListAdapter.notifyDataSetChanged();
                    }

                    break;
                }
                case REQUEST_IMAGE_CODE: {
                    List<LocalMedia> medias = PictureSelector.obtainMultipleResult(data);
                    if (medias!=null && !medias.isEmpty()) {
                        String selectedFilepath = medias.get(0).getPath();//GetPathFromUri.getRealPathFromURI(this, Uri.parse(medias.get(0).getPath()));
                        PLComposeItem item = createImageItem(selectedFilepath);
                        mItemListAdapter.addItem(item);
                        mItemListAdapter.notifyDataSetChanged();
                    }
                    break;
                }
                default:
                    break;
            }
        }
    }

    private void chooseImageFile() {
        PictureSelector.create(this).openGallery(PictureConfig.TYPE_IMAGE).selectionMode(PictureConfig.MULTIPLE).forResult(REQUEST_IMAGE_CODE);
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
//        intent.setType("image/*");
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        startActivityForResult(intent, REQUEST_IMAGE_CODE);
    }

    private void chooseVideoFile() {
        PictureSelector.create(this).openGallery(PictureConfig.TYPE_VIDEO).selectionMode(PictureConfig.MULTIPLE).forResult(REQUEST_VIDEO_CODE);
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
//        intent.setType("video/*");
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        startActivityForResult(intent, REQUEST_VIDEO_CODE);
    }


    public void onClickAddMusic(View v) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("audio/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_AUDIO_CODE);
    }

    private PLComposeItem createImageItem(String path) {
        Log.i(TAG, "createImageItem path : " + path);
        PLComposeItem item;
        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(path);
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
        Log.i(TAG, "createImageItem fileExtension : " + fileExtension);
        Log.i(TAG, "createImageItem mimeType : " + mimeType);

        if (mimeType != null && mimeType.contains("gif")) {
            item = createGIFImageItem(path);
        } else {
            item = createNormalImageItem(path);
        }
        return item;
    }

    private PLComposeItem createNormalImageItem(String path) {
        PLComposeItem item = new PLComposeItem(path);
        item.setItemType(PLComposeItem.ItemType.IMAGE);
        item.setDurationMs(5000);
        item.setTransitionTimeMs(1000);
        return item;
    }

    private PLComposeItem createGIFImageItem(String path) {
        PLComposeItem item = new PLComposeItem(path);
        item.setItemType(PLComposeItem.ItemType.GIF);
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(item.getFilePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        Movie movie = Movie.decodeStream(fileInputStream);
        item.setDurationMs(movie.duration());
        long transitionTimeMs = movie.duration() / 2;
        transitionTimeMs = transitionTimeMs > 1000 ? 1000 : transitionTimeMs;
        item.setTransitionTimeMs(transitionTimeMs);
        return item;
    }

    private PLComposeItem createVideoItem(String path) {
        PLComposeItem item = new PLComposeItem(path);
        item.setItemType(PLComposeItem.ItemType.VIDEO);
        item.setTransitionTimeMs(1000);
        return item;
    }

    private PLVideoEncodeSetting.VIDEO_ENCODING_SIZE_LEVEL getEncodingSizeLevel(int position) {
        return RecordSettings.ENCODING_SIZE_LEVEL_ARRAY[position];
    }

    private int getEncodingBitrateLevel(int position) {
        return RecordSettings.ENCODING_BITRATE_LEVEL_ARRAY[position];
    }
}