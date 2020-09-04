package com.common.base;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import com.common.base.net.HttpUtil;
import com.common.base.net.HttpsConfig;
import com.common.base.net.IpConfig;
import com.common.base.utils.CrashHandler;
import com.common.base.utils.LifeCircleUtils;
import com.common.base.utils.LogUtil;
import com.common.base.utils.album.MyLoader;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import org.litepal.LitePal;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;


/**
 * Created by HSK° on 2018/9/25.
 * --function:  基类 application  使用此module必须继承此类
 * 并且在mainfest文件application中声明
 */
public abstract class RApplication extends Application {

    private static Application application;

    @Override
    public void onCreate() {
        super.onCreate();

        initHttp(getUrl());
//        CrashHandler.getsInstance().init(this);
        LogUtil.isDebug = isDebug();
        LifeCircleUtils.init(this);
        LitePal.initialize(this);
        initHeaderFooter();
        initPostDelay();
        initOthers();

        application = this;

    }

    /**
     * 空方法，重写实现自己需要的其他初始化
     */
    protected  void initOthers(){

    }


    public static Application getApplication() {
        return application;
    }

    /**
     * 日志调控开关
     * true: 日志可见
     */
    protected abstract boolean isDebug();

    /**
     * 域名
     */
    protected abstract String getUrl();

    /**
     * 低版本兼容矢量图
     */
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    /**
     * 初始化网络框架
     */
    private void initHttp(String url) {

        if (TextUtils.isEmpty(url))
            throw new IllegalArgumentException("域名不能为空...详见--RApplication.class");

        HttpsConfig.SSLParams sslParams = HttpsConfig.getSslSocketFactory(null, null, null);
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(new LoggerInterceptor("TAG"))
                .retryOnConnectionFailure(false) //Okhttp在网络请示出现错误时会重新发送请求，最终会不断执行，false就不会（okhttp3.4.2已处理该bug）
                .build();
                IpConfig.httpBase = url;
        HttpUtil.initClient(okHttpClient, this, url);


        OkHttpUtils.initClient(okHttpClient);
    }

    /**
     * 延迟加载 提升启动速度
     */
    private void initPostDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initAlbum();
            }
        }, 1000);
    }


    /**
     * 初始化相册 拍照 图片选择...
     */
    private void initAlbum() {
        Album.initialize(
                AlbumConfig.newBuilder(this)
                        .setAlbumLoader(new MyLoader())
                        .setLocale(Locale.getDefault())
                        .build());
    }

    /**
     * 初始化刷新控件的头布局和地布局的全局样式
     */

    private void initHeaderFooter() {
        //设置smartRefreshLayout全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                MaterialHeader materialHeader = new MaterialHeader(context);
                materialHeader.setColorSchemeColors(Color.parseColor("#1875f0"));//蓝色
                return materialHeader;
            }
        });



        //设置smartRefreshLayout全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                ClassicsFooter classicsFooter = new ClassicsFooter(context);
                classicsFooter.setTextSizeTitle(14);
                return classicsFooter;
            }
        });
    }
}
