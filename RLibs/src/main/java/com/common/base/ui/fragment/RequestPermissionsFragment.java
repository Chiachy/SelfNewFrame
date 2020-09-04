package com.common.base.ui.fragment;


import android.text.Html;
import android.view.Gravity;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.common.base.R;
import com.common.base.ui.base.BaseDialogFragment;

/**
 * Created by HSK° on 2018/4/23.
 * --function:第一次使用app，弹出的要求权限提示框(默认)
 */
public class RequestPermissionsFragment extends BaseDialogFragment implements View.OnClickListener {

    private AppCompatButton btn_open;
    private AppCompatTextView tv_info;

    private OpenPermissionListener openPermissionListener;

    public static RequestPermissionsFragment newInstance() {
        return new RequestPermissionsFragment();
    }

    public void setOnOpenPermissionListener(OpenPermissionListener openPermissionListener) {
        this.openPermissionListener = openPermissionListener;
    }

    public interface OpenPermissionListener {
        void open();
    }

    @Override
    public void onStart() {
        super.onStart();
        mWindow.setGravity(Gravity.CENTER);
        mWindow.setWindowAnimations(R.style.SignDialog);
        mWindow.setLayout(3 * mWidth / 4, dip2px(208));
        setCancelable(false);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_request_permission;
    }


    @Override
    protected void initView() {
        btn_open = mRootView.findViewById(R.id.open);
        tv_info = mRootView.findViewById(R.id.tv_info);
        tv_info.setText(Html.fromHtml(getPermissionInfo()));

        btn_open.setOnClickListener(this);
    }


    private String getPermissionInfo() {
        String str = "在使用App前,需要开启:" + "<font color='#1875f0' > " + "手机存储读写权限" + "</font>" +
                "以确保正常使用软件完整功能";
        return str;

    }

    @Override
    protected void initData() {

    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.open) {
            if (openPermissionListener != null) {
                openPermissionListener.open();
            }

            dismiss();

        }
    }
}
