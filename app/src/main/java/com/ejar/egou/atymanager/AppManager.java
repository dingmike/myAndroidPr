package com.ejar.egou.atymanager;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;

import java.util.Stack;

/**
 * Created by Administrator on 2017\12\9 0009.
 */

public class AppManager {
    private static Stack<Activity> activityStruct = new Stack<>();

    public static void addActivity(Activity activity) {
        activityStruct.push(activity);
    }

    public static void removeActivity(Activity activity) {
        if (activity != null) {
            activityStruct.remove(activity);
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }

    }

    public static void removeAllAty() {
        for (Activity activity : activityStruct) {
            if (activity != null) {
                activity.finish();
            }
        }
        activityStruct.clear();
    }

    public static void backToHome() {
        for (Activity activity : activityStruct) {
            if (activity != null) {
                activity.finish();
            }
        }
        activityStruct.clear();
    }


    /**
     * 退出应用程序
     */
    public static void AppExit(Context context) {
        try {
            removeAllAty();
            ActivityManager manager = (ActivityManager)
                    context.getSystemService(Context.ACTIVITY_SERVICE);
            manager.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到软件显示的版本信息&
     * @param context
     * @return
     */
    public String getVersionName(Context context) {
        String packageName = context.getPackageName();
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }
}
