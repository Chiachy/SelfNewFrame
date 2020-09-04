package com.common.base.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;

/**
 * Created by HSK° on 2018/9/10.
 * --function:  获取当前app配置项
 */
public class Config {

    /**
     * * 获取本应用程序res中string配置文件信息
     * 配置资源文件目录名
     */
    public static String getConfigInfo(Context c, String name) {
        ApplicationInfo appInfo = c.getApplicationInfo();
        return c.getString(c.getResources().getIdentifier(name, "string", appInfo.packageName));
    }

    /**
     *
     * @return      * 获取设备型号
     */
    public static String getDeviceType() {
        return android.os.Build.BRAND + " " + android.os.Build.MODEL;
    }

    /**
     *
     * @return      * 获取OS版本
     */
    public static String getOsVersion() {
        return android.os.Build.VERSION.RELEASE;
    }
}
