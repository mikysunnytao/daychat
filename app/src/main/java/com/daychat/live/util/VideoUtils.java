package com.daychat.live.util;

import android.media.MediaMetadataRetriever;

import java.io.IOException;

public class VideoUtils {
    /**
     * 获取 视频 或 音频 时长
     * @param path 视频 或 音频 文件路径
     * @return 时长 毫秒值
     */
    public static long getDuration(String path){
        android.media.MediaMetadataRetriever mmr = new android.media.MediaMetadataRetriever();
        long duration=0;
        try {
            if (path!= null) {
                mmr.setDataSource(path);
            }
            String time = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            duration= Long.parseLong(time);
        } catch (Exception ex) {
        } finally {
            try {
                mmr.release();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return duration;
    }
}
