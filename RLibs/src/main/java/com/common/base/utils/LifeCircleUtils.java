package com.common.base.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by HSK° on 2018/9/11.
 * --function:关联整个app生命周期,只在application、在application初始化
 */
public class LifeCircleUtils {

    private static Application sApplication;

    static WeakReference<Activity> weakReference;
    static List<Activity> activityList = new LinkedList<>();


    private LifeCircleUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }


    /**
     * *
     * 在 application 初始化
     */
    public static void init(@NonNull final Context context) {
        LifeCircleUtils.sApplication = ((Application) context.getApplicationContext());
        LifeCircleUtils.sApplication.registerActivityLifecycleCallbacks(lifecycleCallbacks);
    }

    /**
     * 关联activity生命之气周期的回调
     */
    private static Application.ActivityLifecycleCallbacks lifecycleCallbacks =new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
            activityList.add(activity);
            setTopActivityWeakRef(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            activityList.remove(activity);
        }
    };



    /**
     *
     * @return 返回application实例
     */
    public static Application getApp() {
        if (sApplication != null) return sApplication;
        throw new NullPointerException("u should init first");
    }

    private static void setTopActivityWeakRef(final Activity activity) {
        if (weakReference == null || !activity.equals(weakReference.get())) {
            weakReference = new WeakReference<>(activity);
        }
    }



}
