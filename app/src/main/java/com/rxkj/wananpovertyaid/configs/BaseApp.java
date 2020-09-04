package com.rxkj.wananpovertyaid.configs;

import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

import com.alibaba.android.arouter.launcher.ARouter;
import com.common.base.BuildConfig;
import com.common.base.RApplication;

/**
 * @author chiachi
 */
public class BaseApp extends RApplication {

    @Override
    protected void initOthers() {
        // android 7.0系统解决拍照的问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

        //配置阿里路由
        if (BuildConfig.DEBUG) {
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this);

    }

    @Override
    protected boolean isDebug() {
        return false;
    }

    @Override
    protected String getUrl() {
        return null;
    }
}
