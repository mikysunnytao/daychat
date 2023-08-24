package com.daychat.live.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class Config {
    // 如何生成 TOKEN：https://developer.qiniu.com/kodo/1208/upload-token
    public static final String TOKEN = "请自行替换您的 token";
    public static final String DOMAIN = "https://image.daydaychat.com/";

    public static String VIDEO_STORAGE_DIR;
    public static String RECORD_FILE_PATH;
    public static String DUB_FILE_PATH;
    public static String AUDIO_RECORD_FILE_PATH;
    public static String EDITED_FILE_PATH;
    public static String TRIM_FILE_PATH;
    public static String TRANSCODE_FILE_PATH;
    public static String CAPTURED_FRAME_FILE_PATH;
    public static String GIF_SAVE_PATH;
    public static String SCREEN_RECORD_FILE_PATH;
    public static String COMPOSE_FILE_PATH;
    public static String IMAGE_COMPOSE_FILE_PATH;
    public static String VIDEO_DIVIDE_FILE_PATH;
    public static String MIX_RECORD_FILE_PATH;
    public static String CAMERA_RECORD_CACHE_PATH;
    public static String VIDEO_MIX_PATH;
    public static String EXTERNAL_RECORD_FILE_PATH;
    public static String COMPOSE_WITH_TRANSITION_FILE_PATH;

    public static String GIF_STICKER_DIR;
    public static String MV_DIR;

    public static String SP_NAME = "CONFIG";
    public static String SP_RESOURCE_PREPARED_MV = "MV_PREPARED";
    public static String SP_RESOURCE_PREPARED_GIF = "GIF_PREPARED";

    public static void init(Context context) {
        VIDEO_STORAGE_DIR = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES) + "/ShortVideo/";
        RECORD_FILE_PATH = VIDEO_STORAGE_DIR + "record.mp4";
        DUB_FILE_PATH = VIDEO_STORAGE_DIR + "dub.mp4";
        AUDIO_RECORD_FILE_PATH = VIDEO_STORAGE_DIR + "audio_record.m4a";
        EDITED_FILE_PATH = VIDEO_STORAGE_DIR + "edited.mp4";
        TRIM_FILE_PATH = VIDEO_STORAGE_DIR + "trimmed.mp4";
        TRANSCODE_FILE_PATH = VIDEO_STORAGE_DIR + "transcoded.mp4";
        CAPTURED_FRAME_FILE_PATH = VIDEO_STORAGE_DIR + "captured_frame.jpg";
        GIF_SAVE_PATH = VIDEO_STORAGE_DIR + "generated.gif";
        SCREEN_RECORD_FILE_PATH = VIDEO_STORAGE_DIR + "screen_record.mp4";
        COMPOSE_FILE_PATH = VIDEO_STORAGE_DIR + "composed.mp4";
        IMAGE_COMPOSE_FILE_PATH = VIDEO_STORAGE_DIR + "image_composed.mp4";
        VIDEO_DIVIDE_FILE_PATH = VIDEO_STORAGE_DIR + "divide_composed.mp4";
        MIX_RECORD_FILE_PATH = VIDEO_STORAGE_DIR + "mix_record.mp4";
        CAMERA_RECORD_CACHE_PATH = VIDEO_STORAGE_DIR + "mix_camera_cache.mp4";
        VIDEO_MIX_PATH = VIDEO_STORAGE_DIR + "video_mix.mp4";
        EXTERNAL_RECORD_FILE_PATH = VIDEO_STORAGE_DIR + "external_record.mp4";
        COMPOSE_WITH_TRANSITION_FILE_PATH = VIDEO_STORAGE_DIR + "image_composition.mp4";

        GIF_STICKER_DIR = VIDEO_STORAGE_DIR + "gif/";
        MV_DIR = VIDEO_STORAGE_DIR + "mvs/";

        createDirIfNotExists(VIDEO_STORAGE_DIR);
        createDirIfNotExists(GIF_STICKER_DIR);
        createDirIfNotExists(MV_DIR);
    }

    private static void createDirIfNotExists(String dir) {
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
    }
}