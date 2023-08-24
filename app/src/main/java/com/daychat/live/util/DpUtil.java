package com.daychat.live.util;

import com.daychat.live.base.MyApp;



public class DpUtil {

    private static final float scale;

    static {
        scale = MyApp.getInstance().getResources().getDisplayMetrics().density;
    }

    public static int dp2px(int dpVal) {
        return (int) (scale * dpVal + 0.5f);
    }

    public static int sp2px( float spValue) {

        return (int) (spValue * scale + 0.5f);
    }

}
