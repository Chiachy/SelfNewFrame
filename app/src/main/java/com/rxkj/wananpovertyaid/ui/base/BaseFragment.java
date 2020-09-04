package com.rxkj.wananpovertyaid.ui.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.rxkj.wananpovertyaid.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * Created by Chiachi on 2019/6/24
 * Code'n'learn,ç'est la vie
 */
public abstract class BaseFragment extends Fragment {

    private Unbinder mUnbinder;
    private PromptDialog mPromptDialog;
    private Toast mToast;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPromptDialog = new PromptDialog(Objects.requireNonNull(getActivity()));

        //注册Evenbus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(provideContentViewId(), container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        initView(rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {

    }

    private void initView(View rootView) {

    }

    //得到当前界面的布局文件id(由子类实现)
    abstract int provideContentViewId();

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
            mToast = Toast.makeText(getActivity(), msg, time);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftKeyboard() {
        // 隐藏软键盘，避免软键盘引发的内存泄露
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (manager != null) {
                manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //取消EventBus注册
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        //此处unbind会导致 java.lang.IllegalStateException: Bindings already cleared.
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            mUnbinder.unbind();
        } catch (Exception e) {
            ToastUtils.show(getContext(), e.getMessage());
        }
    }
}
