package com.daychat.live.util;

import android.content.res.Resources;

import com.daychat.live.base.MyApp;




public class WordUtil {

    private static final Resources sResources;

    static {
        sResources = MyApp.getInstance().getResources();
    }

    public static String getString(int res) {
        return sResources.getString(res);
    }
}
