package com.common.base.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.common.base.R;

import java.util.List;

/**
 * Created by HSK° on 2018/9/11.
 * --function:  activity管理类
 */
public class ActivityUtils {


    /**
     * 关闭所有activity
     */
    public static void finishAllActivities() {
        finishAllActivities(false);
    }


    /**
     * finish所有activity
     * @param isLoadAnim W是否开启动画
     */
    public static void finishAllActivities(final boolean isLoadAnim) {
        List<Activity> activityList = LifeCircleUtils.activityList;
        for (int i = activityList.size() - 1; i >= 0; --i) {// remove from top
            Activity activity = activityList.get(i);
            // sActivityList remove the index activity at onActivityDestroyed
            activity.finish();
            if (!isLoadAnim) {
                activity.overridePendingTransition(0, 0);
            }
        }
    }

    /**
     * 退出应用
     */
    public static void exitApp() {
        List<Activity> activityList = LifeCircleUtils.activityList;
        for (int i = activityList.size() - 1; i >= 0; --i) {// remove from top
            Activity activity = activityList.get(i);
            // sActivityList remove the index activity at onActivityDestroyed
            activity.finish();
        }
        System.exit(0);
    }

    /**
     * 重启应用
     * @param packageName 包名
     * @param className   完整的mainactivity路径
     */
    public static void restartApp(String packageName, String className) {
        Intent intent = new Intent();
        intent.setClassName(packageName, className);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Application application = LifeCircleUtils.getApp();

        PendingIntent restartIntent = PendingIntent.getActivity(application, 0, intent, 0);
        AlarmManager manager = (AlarmManager) application.getSystemService(Context.ALARM_SERVICE);
        if (manager == null) return;
        manager.set(AlarmManager.RTC, System.currentTimeMillis() + 1, restartIntent);
        ActivityUtils.finishAllActivities();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }


    /**
     * @param thisActivity 当前Activity
     * @param toClass      跳转的activity
     * @param isLoadAnim   是否开启动画
     */

    public static void finishAllToActivity(@NonNull Activity thisActivity,
                                           @NonNull final Class<?> toClass,
                                           final boolean isLoadAnim) {
        List<Activity> activities = LifeCircleUtils.activityList;
        for (int i = activities.size() - 1; i >= 0; --i) {
            Activity aActivity = activities.get(i);
            finishActivity(aActivity, isLoadAnim);
        }
        thisActivity.startActivity(new Intent(thisActivity, toClass));
        if (isLoadAnim) {
            thisActivity.overridePendingTransition(R.anim.fade_in_600, R.anim.fade_out_600);
        }
    }


    /**
     * 关闭activity
     * @param activity   The activity.
     * @param isLoadAnim 是否开启动画
     */
    public static void finishActivity(@NonNull final Activity activity, final boolean isLoadAnim) {
        activity.finish();
        if (isLoadAnim) {
            activity.overridePendingTransition(R.anim.fade_in_600, R.anim.fade_out_600);
        }
    }

    /**
     * 开启activity
     * @param isLoadAnimation 是否开启动画
     */

    public static void startActivity(@NonNull Activity thisActivity,
                                     @NonNull Class<?> toActivity,
                                     boolean isLoadAnimation) {

        thisActivity.startActivity(new Intent(thisActivity, toActivity));
        if (isLoadAnimation) {
            thisActivity.overridePendingTransition(R.anim.fade_in_600, R.anim.fade_out_600);
        }

    }

    /**
     * 获取对应的activity是不是为null；
     */
    public static boolean getTargetActivityIsNotNull(String name) {
        List<Activity> activityList = LifeCircleUtils.activityList;

        if (activityList == null)
            return false;

        for (int i = 0; i < activityList.size(); i++) {
            Activity activity = activityList.get(i);
            String name2 = activity.getClass().getName();
            int pos = name2.lastIndexOf(".");
            if (name.equals(name2.substring(pos+1))) {
                return true;
            }
        }

        return false;
    }



}
