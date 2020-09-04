package com.common.base.update;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.vector.update_app.HttpManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by HSK° on 2018/9/14.
 * --function:  app版本更新辅助类
 */
class UpdateAppHttpUtil implements HttpManager {
    /**
     * 异步get
     *
     * @param url      get请求地址
     * @param params   get参数
     * @param callBack 回调
     */
    @Override
    public void asyncGet(@NonNull String url, @NonNull Map<String, String> params, @NonNull final Callback callBack) {
        OkHttpUtils.get()
                .url(url)
                .params(params)
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        callBack.onError(validateError(e));

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        callBack.onResponse(response);
                    }
                });
    }

    /**
     * 异步post
     *
     * @param url      post请求地址
     * @param params   post请求参数
     * @param callBack 回调
     */
    @Override
    public void asyncPost(@NonNull String url, @NonNull Map<String, String> params, @NonNull final Callback callBack) {
        OkHttpUtils.post()
                .url(url)
                .params(params)
                .build()
                .execute(new StringCallback() {



                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        callBack.onError(validateError(e));

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        callBack.onResponse(response);
                    }
                });

    }

    /**
     * 下载
     *
     * @param url      下载地址
     * @param path     文件保存路径
     * @param fileName 文件名称
     * @param callback 回调
     */
    @Override
    public void download(@NonNull String url, @NonNull String path, @NonNull String fileName, @NonNull final FileCallback callback) {
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new FileCallBack(path, fileName) {
                    @Override
                    public void inProgress(float progress, long total, int id) {
                        callback.onProgress(progress, total);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callback.onError(validateError(e));

                    }


                    @Override
                    public void onResponse(File response, int id) {
                        callback.onResponse(response);

                    }

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        callback.onBefore();
                    }
                });

    }

    @SuppressLint("DefaultLocale")
    private String validateError(Exception error) {

        if (error != null) {
            if (error instanceof NoNetWorkException) {
                return "无网络，请联网重试";
            } else if (error instanceof SocketTimeoutException) {
                return "网络连接超时，请稍候重试";
            } else if (error instanceof JSONException) {
                return "json转化异常";
            } else if (error instanceof ConnectException) {
                return "服务器网络异常或宕机，请稍候重试";
            }
        }else {
            return "服务器异常，请稍候重试";
        }

        return "未知异常，请稍候重试";
    }

    public class NoNetWorkException extends IOException {
        public NoNetWorkException() {
        }

        public NoNetWorkException(String message) {
            super(message);
        }
    }
}
