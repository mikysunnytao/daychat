package com.daychat.live.base;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import com.daychat.live.R;
import com.daychat.live.util.Config;
import com.daychat.live.util.TxPicturePushUtils;
import com.daychat.live.widget.ToastUtils;
import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.style.MaterialStyle;
import com.meiyinzb.nasinet.utils.AppManager;
import com.orhanobut.hawk.Hawk;
import com.qiniu.pili.droid.shortvideo.PLShortVideoEnv;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.live2.V2TXLivePremier;
import com.tencent.qcloud.tuicore.TUIThemeManager;
import com.tencent.rtmp.TXLiveBase;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.ugc.TXUGCBase;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;


public class MyApp extends Application{
    private static Context mContext;
    //腾讯短视频KEY
    String ugcLicenceUrl = "https://license.vod2.myqcloud.com/license/v2/1300134639_1/v_cube.license"; //您从控制台申请的 licence url
    String ugcKey = "1edf6ba2c08907203aa44c1805126d99";

    public static Context sApplication;

    public static long UNREAD = 0;
    private boolean login_mode = false;
    public static int mopiLevel = 8;
    public static int meibaiLevel = 5;
    public static int hongrunLevel = 3;

    public boolean isLogin_mode() {
        return login_mode;
    }

    public void setLogin_mode(boolean login_mode) {
        this.login_mode = login_mode;
    }

    public boolean license = false;



    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        sApplication = getApplicationContext();
        TUIThemeManager.addLightTheme(R.style.DemoLightTheme);
        TUIThemeManager.addLivelyTheme(R.style.DemoLivelyTheme);
        TUIThemeManager.addSeriousTheme(R.style.DemoSeriousTheme);
        DialogX.init(this);
        PLShortVideoEnv.init(this);
        DialogX.globalStyle = MaterialStyle.style();
        DialogX.globalTheme = DialogX.THEME.DARK;
        //初始化X5内核
        QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
            }

            @Override
            public void onViewInitFinished(boolean b) {

                Log.e("@@", "加载内核是否成功:" + b);
            }
        });
        AppManager.getAppManager().setmApplication(this);
        Hawk.init(this).build();
        mContext = getApplicationContext();
//        Fresco.initialize(this);
        TCConfigManager.init(this);
        V2TXLivePremier.setLicence(this, ugcLicenceUrl, ugcKey);
        V2TXLivePremier.setObserver(new V2TXLivePremier.V2TXLivePremierObserver() {
            @Override
            public void onLicenceLoaded(int result, String reason) {
                Log.i("initLivePre", "onLicenceLoaded: result:" + result + ", reason:" + reason);
            }

            @Override
            public void onLog(int level, String log) {
                super.onLog(level, log);
            }
        });


        TXUGCBase.getInstance().setLicence(this, ugcLicenceUrl, ugcKey);

        TxPicturePushUtils.getInstance().setmContext(mContext);
        //移动分享SDK
//        MobSDK.submitPolicyGrantResult(false, null);
//        MobSDK.init(this);

        initSDK();

        // Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler());

        initUGCKit();
    }

    private void initUGCKit() {
        //初始化腾讯短视频
        TCConfigManager.init(this);
        // 短视频licence设置
        TXUGCBase.getInstance().setLicence(this, ugcLicenceUrl, ugcKey);
        PLShortVideoEnv.init(this);
//        TCUserMgr.getInstance().initContext(getApplicationContext());
//        UGCKit.init(this);
    }

    public static Context getInstance() {
        return mContext;
    }

    public Application getApplication() {
        return this;
    }


    /**
     * 初始化SDK，包括Bugly，LiteAVSDK等
     */
    public void initSDK() {
        //启动bugly组件，bugly组件为腾讯提供的用于crash上报和分析的开放组件，如果您不需要该组件，可以自行移除
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
        strategy.setAppVersion(TXLiveBase.getSDKVersionStr());
//        CrashReport.initCrashReport(getApplicationContext(), "9f09d9a052", true, strategy);
        CrashReport.initCrashReport(this,"9c542e9d97",true,strategy);
        PLShortVideoEnv.init(getApplicationContext());
        Config.init(this);
        ToastUtils.init(this);
        // TCUserMgr.getInstance().initContext(getApplicationContext());

    }



    public class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            final Writer result = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(result);
            //如果异常时在AsyncTask里面的后台线程抛出的
            //那么实际的异常仍然可以通过getCause获得
            Throwable cause = ex;
            while (null != cause) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            //stacktraceAsString就是获取的carsh堆栈信息
            final String stacktraceAsString = result.toString();
            printWriter.close();
            AppManager.getAppManager().AppExit(getApplicationContext());
        }
    }


    @Override
    public void startActivity(Intent intent) {
        if (checkDoubleClick(intent)) {
            super.startActivity(intent);
        }

    }

    @Override
    public void startActivity(Intent intent, Bundle options) {
        if (checkDoubleClick(intent)) {
            super.startActivity(intent, options);
        }
    }

    private String mActivityJumpTag;        //activity跳转tag
    private long mClickTime;                //activity跳转时间

    /**
     * 检查是否重复跳转，不需要则重写方法并返回true
     */
    protected boolean checkDoubleClick(Intent intent) {

        // 默认检查通过
        boolean result = true;
        // 标记对象
        String tag;
        if (intent.getComponent() != null) { // 显式跳转
            tag = intent.getComponent().getClassName();
        } else if (intent.getAction() != null) { // 隐式跳转
            tag = intent.getAction();
        } else {
            return true;
        }

        if (tag.equals(mActivityJumpTag) && mClickTime >= SystemClock.uptimeMillis() - 500) {
            // 检查不通过
            result = false;
        }

        // 记录启动标记和时间
        mActivityJumpTag = tag;
        mClickTime = SystemClock.uptimeMillis();
        return result;
    }

    private static MyApp instance;

    public static MyApp instance() {
        return instance;
    }

}


