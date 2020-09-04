package com.rxkj.wananpovertyaid.ui.base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.rxkj.wananpovertyaid.receiver.NetworkConnectChangedReceiver;
import com.rxkj.wananpovertyaid.utils.NetUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * Created by chiachi 2020/6/22 4:37 PM
 * Desc: 抽象基类activity
 *
 * @author chiachi
 */

public abstract class BaseActivity extends AppCompatActivity implements NetworkConnectChangedReceiver.NetChangeListener {

    private int mNetMobile;
    private Toast mToast;
    private PromptDialog mPromptDialog;
    private Unbinder mUnbinder;
    //网络状态变化的广播接收器
    private NetworkConnectChangedReceiver mNetWorkChangReceiver;
    //网络是否可用
    protected boolean isNetAvailable = false;
    private static final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initQMUIBarStyle();
        mPromptDialog = new PromptDialog(this);
        setContentView(provideContentViewId());
        initNetBroadcastReceiver();
        ButterKnife.bind(this);
        mUnbinder = ButterKnife.bind(this);
        initData();
        initView();
    }

    /**
     * 在setContentView()调用之前调用，可以设置WindowFeature
     * (如：this.requestWindowFeature(Window.FEATURE_NO_TITLE);)
     */
    public void initQMUIBarStyle() {
        // 沉浸式状态栏
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
    }

    //注册Evenbus
    public void initEvenbusData(Bundle bundle) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEvenBusData(Bundle bundle) {
        initEvenbusData(bundle);
    }


    public void initView() {

    }

    public void initData() {

    }

    /**
     * 得到当前界面的布局文件id(由子类实现)
     *
     * @return
     */
    protected abstract int provideContentViewId();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destoryEvenbus();
        mUnbinder.unbind();
        mHandler.removeCallbacksAndMessages(this);
        unregisterReceiver(mNetWorkChangReceiver);
    }

    private void destoryEvenbus() {
        //取消EventBus注册
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    /**
     * 注册网络监听广播
     */
    private void initNetBroadcastReceiver() {
        mNetWorkChangReceiver = new NetworkConnectChangedReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetWorkChangReceiver, filter);
    }

    /**
     * 延迟一段时间执行
     */
    protected final void postDelayed(Runnable r, long delayMillis) {
        if (delayMillis < 0) {
            delayMillis = 0;
        }
        postAtTime(r, SystemClock.uptimeMillis() + delayMillis);
    }

    /**
     * 在指定的时间执行
     */
    private boolean postAtTime(Runnable r, long uptimeMillis) {
        return mHandler.postAtTime(r, this, uptimeMillis);
    }

    /**
     * 初始化时判断有没有网络
     */
    public boolean inspectNet() {
        this.mNetMobile = NetUtils.getNetWorkState(BaseActivity.this);
        return isNetConnect();
    }

    /**
     * 网络变化之后的类型
     */
    public void onNetChange(int netMobile) {
        this.mNetMobile = netMobile;
        isNetConnect();
    }

    /**
     * 判断有无网络
     *
     * @return true 有网, false 没有网络.
     */
    public boolean isNetConnect() {
        if (mNetMobile == NetUtils.NETWORK_WIFI) {
            return true;
        } else if (mNetMobile == NetUtils.NETWORK_MOBILE) {
            return true;
        } else if (mNetMobile == NetUtils.NETWORK_NONE) {
            return false;
        }
        return false;
    }

    public void showPromptDialog(String msg) {
        mPromptDialog.showLoading(msg);
    }

    public void dismissPromptDialog() {
        mPromptDialog.dismiss();
    }

    /**
     * 弹出Toast（防止连续弹出 ）
     *
     * @param msg
     */
    protected void showToast(String msg, int time) {
        if (mToast == null) {
            mToast = Toast.makeText(this, msg, time);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    /**
     * 单个选项提示弹窗
     *
     * @param title
     * @param msg
     * @param listener
     */
    protected void showSingleBtnDialog(String title, String msg, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton("确定", listener)
                .create();
        builder.show();
    }

    /**
     * 复选提示弹窗
     *
     * @param title
     * @param msg
     * @param positiveListener
     */
    protected void showDoubleBtnDialog(String title, String msg, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener nagetiveListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("确定", positiveListener)
                .setNegativeButton("取消", nagetiveListener)
                .create();
        builder.show();
    }

    @Override
    public void finish() {
        hideSoftKeyboard();
        super.finish();
    }

    /**
     * 隐藏软键盘
     */
    private void hideSoftKeyboard() {
        // 隐藏软键盘，避免软键盘引发的内存泄露
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (manager != null) {
                manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

}
