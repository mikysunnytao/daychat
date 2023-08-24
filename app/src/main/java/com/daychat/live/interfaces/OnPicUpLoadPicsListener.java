package com.daychat.live.interfaces;

/**
 *
 */
public interface OnPicUpLoadPicsListener {
    void onPicUrls(String result, String blurResult);
    void onError(Throwable exception);
}
