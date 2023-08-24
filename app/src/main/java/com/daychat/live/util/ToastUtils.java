package com.daychat.live.util;

import android.view.Gravity;
import android.widget.Toast;

import com.daychat.live.base.MyApp;

public class ToastUtils {


    public static void showT(String text) {
        Toast mToast = new Toast(MyApp.getInstance());

        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setText(text);
        mToast.show();
    }
}
