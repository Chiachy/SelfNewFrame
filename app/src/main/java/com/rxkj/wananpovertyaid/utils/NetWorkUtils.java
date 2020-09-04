package com.rxkj.wananpovertyaid.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;


import com.rxkj.wananpovertyaid.configs.BaseApp;

import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by HSK° on 2018/2/26.
 * --function:校验查看网络工具类
 */

public class NetWorkUtils {

        /**
         * 检查互联网地址是否可以访问-使用get请求
         * @param urlStr   要检查的url
         * @param callback 检查结果回调（是否可以get请求成功）{@see java.lang.Comparable<T>}
         */
        public static void isNetWorkAvailableOfGet(final String urlStr, final Comparable<Boolean> callback) {

            /**
             *   加上Handler.Callback 不会有黄色警报 和内存泄露风险
             */
            final Handler handler =new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    if (callback != null) {
                        callback.compareTo(message.arg1 == 0);
                    }
                    return true;
                }
            });
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message msg = new Message();
                    try {
                        Connection conn = new Connection(urlStr);
                        Thread thread = new Thread(conn);
                        thread.start();
                        thread.join(5 * 1000); // 设置等待DNS解析线程响应时间为5秒
                        int resCode = conn.get(); // 获取get请求responseCode
                        msg.arg1 = resCode == 200 ? 0 : -1;
                    } catch (Exception e) {
                        msg.arg1 = -1;
                        e.printStackTrace();
                    } finally {
                        handler.sendMessage(msg);
                    }
                }

            }).start();
        }


    /**
     * 判断网络是否连接
     * <p>需添加权限
     * {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />}</p>
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isConnected() {
        NetworkInfo info = getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    /**
     * 判断网络是否是移动数据
     * <p>需添加权限
     * {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />}</p>
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    @SuppressLint("MissingPermission")
    public static boolean isMobileData() {
        NetworkInfo info = getActiveNetworkInfo();
        return null != info
                && info.isAvailable()
                && info.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    /**
     * 获取活动网络信息
     * <p>需添加权限
     * {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />}</p>
     *
     * @return NetworkInfo
     */
    @SuppressLint("MissingPermission")
    private static NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager manager =
                (ConnectivityManager) BaseApp.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return null;
        }
        return manager.getActiveNetworkInfo();
    }


    /**
     * HttpURLConnection请求线程
     */
    private static class Connection implements Runnable {
        private String urlStr;
        private int responseCode;

        private Connection(String urlStr) {
            this.urlStr = urlStr;
        }

        @Override
        public void run() {
            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();
                set(conn.getResponseCode());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public synchronized void set(int responseCode) {
            this.responseCode = responseCode;
        }

        public synchronized int get() {
            return responseCode;
        }
    }

}
