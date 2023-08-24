package com.daychat.live.util;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import com.daychat.live.base.MyApp;
import com.daychat.live.push.HandleOfflinePushCallBack;
import com.daychat.live.push.OfflineMessageDispatcher;
import com.daychat.live.ui.act.GuideActivity;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.qcloud.tuicore.TUIConstants;
import com.tencent.qcloud.tuicore.TUICore;
import com.tencent.qcloud.tuicore.util.TUIBuild;
import com.tencent.qcloud.tuikit.tuichat.bean.OfflineMessageBean;

import java.util.Locale;


public class TUIUtils {
    public static final String TAG = TUIUtils.class.getSimpleName();

    public static void startActivity(String activityName, Bundle param) {
        TUICore.startActivity(activityName, param);
    }

    public static void startChat(String chatId, String chatName, int chatType) {
        Bundle bundle = new Bundle();
        bundle.putString(TUIConstants.TUIChat.CHAT_ID, chatId);
        bundle.putString(TUIConstants.TUIChat.CHAT_NAME, chatName);
        bundle.putInt(TUIConstants.TUIChat.CHAT_TYPE, chatType);
        if (chatType == V2TIMConversation.V2TIM_C2C) {
            TUICore.startActivity(TUIConstants.TUIChat.C2C_CHAT_ACTIVITY_NAME, bundle);
        } else if (chatType == V2TIMConversation.V2TIM_GROUP) {
            TUICore.startActivity(TUIConstants.TUIChat.GROUP_CHAT_ACTIVITY_NAME, bundle);
        }
    }

    public static boolean isZh(Context context) {
        Locale locale;
        if (TUIBuild.getVersionInt() < Build.VERSION_CODES.N) {
            locale = context.getResources().getConfiguration().locale;
        } else {
            locale = context.getResources().getConfiguration().getLocales().get(0);
        }
        String language = locale.getLanguage();
        return language.endsWith("zh");
    }

    public static int getCurrentVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException ignored) {
            DemoLog.e(TAG, "getCurrentVersionCode exception= " + ignored);
        }
        return 0;
    }
//
    public static void handleOfflinePush(Intent intent, HandleOfflinePushCallBack callBack) {
        Context context = MyApp.instance().getApplicationContext();
        if (V2TIMManager.getInstance().getLoginStatus() == V2TIMManager.V2TIM_STATUS_LOGOUT) {
            Intent intentAction = new Intent(context, GuideActivity.class);
            if (intent != null) {
                intentAction.putExtras(intent);
            }
            intentAction.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentAction);
            if (callBack != null) {
                callBack.onHandleOfflinePush(false);
            }
            return;
        }

        final OfflineMessageBean bean = OfflineMessageDispatcher.parseOfflineMessage(intent);
        if (bean != null) {
            if (callBack != null) {
                callBack.onHandleOfflinePush(true);
            }
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.cancelAll();
            }

            if (bean.action == OfflineMessageBean.REDIRECT_ACTION_CHAT) {
                if (TextUtils.isEmpty(bean.sender)) {
                    return;
                }
                TUIUtils.startChat(bean.sender, bean.nickname, bean.chatType);
            }
        }
    }
//
    public static void handleOfflinePush(String ext, HandleOfflinePushCallBack callBack) {
        Context context = MyApp.instance().getApplicationContext();
        if (V2TIMManager.getInstance().getLoginStatus() == V2TIMManager.V2TIM_STATUS_LOGOUT) {
            Intent intentAction = new Intent(context, GuideActivity.class);
            if (!TextUtils.isEmpty(ext)) {
                intentAction.putExtra(TUIConstants.TUIOfflinePush.NOTIFICATION_EXT_KEY, ext);
            }
            intentAction.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentAction);
            if (callBack != null) {
                callBack.onHandleOfflinePush(false);
            }
            return;
        }

        final OfflineMessageBean bean = OfflineMessageDispatcher.getOfflineMessageBeanFromContainer(ext);
        if (bean != null) {
            if (callBack != null) {
                callBack.onHandleOfflinePush(true);
            }
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.cancelAll();
            }

            if (bean.action == OfflineMessageBean.REDIRECT_ACTION_CHAT) {
                if (TextUtils.isEmpty(bean.sender)) {
                    return;
                }
                TUIUtils.startChat(bean.sender, bean.nickname, bean.chatType);
            }
        }
    }

//    public static TUILoginConfig getLoginConfig() {
//        TUILoginConfig config = new TUILoginConfig();
//        if (BuildConfig.DEBUG) {
//            config.setLogLevel(TUILoginConfig.TUI_LOG_DEBUG);
//        }
//        return config;
//    }

}
