package com.common.base.ui.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.common.base.R;
import com.gyf.immersionbar.ImmersionBar;

/**
 * Created by HSK° on 2018/9/10.
 * --function:dialogfragemnt基类
 */
@SuppressWarnings("ConstantConditions")
public abstract class BaseDialogFragment extends DialogFragment {

    protected Activity mActivity;
    protected View mRootView;

    protected ImmersionBar mImmersionBar;
    protected Window mWindow;
    protected int mWidth;  //屏幕宽度
    protected int mHeight;  //屏幕高度


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialogFragmennt);
        getData();
    }

    protected void getData(){}

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        dialog.setCanceledOnTouchOutside(true);  //点击外部消失
        mWindow = dialog.getWindow();
        //测量宽高
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getRealMetrics(dm);
            mWidth = dm.widthPixels;
            mHeight = dm.heightPixels;
        } else {
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            mWidth = metrics.widthPixels;
            mHeight = metrics.heightPixels;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(setLayoutId(), container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return mRootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isImmersionBarEnabled())
            initImmersionBar();
        initView();
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    protected abstract int setLayoutId();

    /**
     * 是否在Fragment使用沉浸式
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    /**
     * 初始化沉浸式
     */
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(getActivity(), getDialog());
        mImmersionBar.fitsSystemWindows(true, R.color.update_white).statusBarDarkFont(true, 0.2f).init();

    }

    /**
     * view与数据绑定
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();



    //dip和px转换
    protected int dip2px(float dpValue) {
        final float scale = mActivity.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
