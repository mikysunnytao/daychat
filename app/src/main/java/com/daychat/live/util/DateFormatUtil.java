package com.daychat.live.util;

import java.text.SimpleDateFormat;
import java.util.Date;



public class DateFormatUtil {

    private static final SimpleDateFormat sFormat;
    private static final SimpleDateFormat sFormat2;

    static {
        sFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        sFormat2 = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");
    }


    public static String getCurTimeString() {
        return sFormat.format(new Date());
    }

    public static String getVideoCurTimeString() {
        return sFormat2.format(new Date());
    }
}
