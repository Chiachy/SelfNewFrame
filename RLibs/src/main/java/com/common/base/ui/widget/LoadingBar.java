package com.common.base.ui.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatTextView;

import com.common.base.R;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * Created by HSK° on 2018/1/26.
 * --function:  转圈圈的进度条
 */

public class LoadingBar {

    private Context context;
    private AppCompatDialog mDialog;
//    private AVLoadingIndicatorView loading;
    private AppCompatTextView tvLoading;

    private boolean toachOutSiteAble = true;


    public LoadingBar(Context context) {
        this.context = context;
        init();
    }

    public void init() {
        mDialog = new AppCompatDialog(context, R.style.LoadingDialog);
        View view = LayoutInflater.from(context).inflate(R.layout.loading, null);
        view.findViewById(R.id.view);
//        loading = view.findViewById(R.id.loading);
        tvLoading = view.findViewById(R.id.tvLoading);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(toachOutSiteAble);
    }

    public void canCancellable(boolean toachOutSiteAble) {
        this.toachOutSiteAble = toachOutSiteAble;
    }


    public void show() {
        try {
            mDialog.show();
//            loading.show();
        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

    }

    public void show(String hint) {
        try {
            mDialog.show();
            tvLoading.setText(hint);
//            loading.show();
        } catch (WindowManager.BadTokenException e) {
            //
        } catch (IllegalArgumentException e) {
            //
        }


    }


    public void dissmiss() {
        try {
            mDialog.dismiss();
        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public boolean isShowing() {
        return mDialog.isShowing();
    }

}
