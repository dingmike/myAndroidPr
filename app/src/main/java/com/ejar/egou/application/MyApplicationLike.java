package com.ejar.egou.application;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.ejar.egou.Constants_num;
import com.ejar.chapp.baselibrary.utils.TU;
import com.mob.MobSDK;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tinker.loader.app.DefaultApplicationLike;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2017\12\28 0028.
 */

public class MyApplicationLike extends DefaultApplicationLike {
    public static final String TAG = "Tinker.SampleApplicationLike";
    public static IWXAPI api;
    private static Context app;
    private static String id;

    public MyApplicationLike(Application application, int tinkerFlags,
                             boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime,
                             long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }

    public static Context getAppContext() {
        return app;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        app = this.getApplication();
        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId
        // 调试时，将第三个参数改为true
        api = WXAPIFactory.createWXAPI(getApplication(), Constants_num.APP_ID, false);
        api.registerApp(Constants_num.APP_ID);
        TU.register(getApplication());
        Bugly.init(getApplication(), "99790746d8", true);
        MobSDK.init(getApplication());
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this.getApplication());
        JPushInterface.getAlias(this.getApplication(), 0);
        String id = JPushInterface.getRegistrationID(this.getApplication());
        Log.e("RegistrationID", id);
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);

        // 安装tinker
        // TinkerManager.installTinker(this); 替换成下面Bugly提供的方法
        Beta.installTinker(this);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallback(Application.ActivityLifecycleCallbacks callbacks) {
        getApplication().registerActivityLifecycleCallbacks(callbacks);
    }
}
