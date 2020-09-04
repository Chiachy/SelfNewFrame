package com.common.base.ui.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatDialog;

import com.common.base.R;

import cn.fanrunqi.waveprogress.WaveProgressView;

/**
 * Created by HSK° on 2018/9/10.
 * --function:  带进度的 圆形 进度条
 * 可以是任何drawable
 */
public class CircleProgressView {
    private Context context;
    private AppCompatDialog mDialog;

    private WaveProgressView waveProgressbar;


    public CircleProgressView(Context context) {
        this.context = context;
        init();
    }

    //
    public void init() {
        mDialog = new AppCompatDialog(context, R.style.ActionSheetDialogStyle);
        View view = LayoutInflater.from(context).inflate(R.layout.circle_progress_view, null);
        waveProgressbar = view.findViewById(R.id.waveProgressbar);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);

        waveProgressbar.setWaveColor("#3AC42C");
        waveProgressbar.setMaxProgress(100);
    }

    public void canCancellable(boolean toachOutSiteAble) {
        mDialog.setCanceledOnTouchOutside(toachOutSiteAble);
    }

    public void setProgress(int progress, String text) {
        waveProgressbar.setCurrent(progress, text);
    }


    public void show() {
        mDialog.show();
    }

    public void dissmiss() {
        mDialog.dismiss();
    }

    public boolean isShowing() {
        return mDialog.isShowing();
    }
}

