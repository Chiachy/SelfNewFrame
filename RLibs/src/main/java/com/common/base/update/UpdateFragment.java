package com.common.base.update;

import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatTextView;

import com.common.base.R;
import com.common.base.net.IpConfig;
import com.common.base.ui.base.BaseDialogFragment;
import com.common.base.ui.widget.NumberProgressBar;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.service.DownloadService;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by HSK° on 2018/5/23.
 * --function: 检测更新的fragment 下载安装都在此运行
 */
@SuppressWarnings("ConstantConditions")
public class UpdateFragment extends BaseDialogFragment implements View.OnClickListener {

    private AppCompatTextView tv_ok;
    private AppCompatTextView tv_upinfo;
    private String qupdate_flag;      //是否强制升级标志  1强制 0普通
    private String upDateInfo; //升级信息
    private String apkUrl;//url
    private LinearLayout ll_update, ll_bottom;
    private NumberProgressBar numberProgressBar;
    private DissmissListener dissmissListener;

    public static UpdateFragment getInstance(List<Map<String, Object>> list) {
        UpdateFragment f = new UpdateFragment();
        Bundle args = new Bundle();
        args.putSerializable("DATA", (Serializable) list);
        f.setArguments(args);
        return f;
    }


    public interface  DissmissListener{
        void diss();
    }

    public void setDissmissListener(DissmissListener dissmissListener) {
        this.dissmissListener = dissmissListener;
    }

    @Override
    public void onStart() {
        super.onStart();
        mWindow.setGravity(Gravity.CENTER);
        mWindow.setWindowAnimations(R.style.SignDialog);
        mWindow.setLayout(mWidth / 2 + dip2px(52), mHeight / 2 - dip2px(20));
        setCancelable(false);
    }

    @SuppressWarnings({"ConstantConditions", "unchecked"})
    @Override
    protected void getData() {
        List<Map<String, Object>> list = (List<Map<String, Object>>) getArguments().getSerializable("DATA");
        apkUrl = list.get(0).get("QFILEPATH") == null ? "" : String.valueOf(list.get(0).get("QFILEPATH"));//域名
        qupdate_flag = String.valueOf(list.get(0).get("QUPDATE_FLAG"));//是否强制更新
        upDateInfo = list.get(0).get("QVERSIONDESC") == null ? "0" : String.valueOf(list.get(0).get("QVERSIONDESC"));//更新说明
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_update;
    }

    @Override
    protected void initView() {
        tv_ok = mRootView.findViewById(R.id.tv_ok);
        AppCompatTextView tv_no = mRootView.findViewById(R.id.tv_no);
        tv_upinfo = mRootView.findViewById(R.id.tv_upinfo);
        ll_update = mRootView.findViewById(R.id.ll_update);
        numberProgressBar = mRootView.findViewById(R.id.numberProgressBar);
        ll_bottom = mRootView.findViewById(R.id.ll_bottom);

        tv_no.setVisibility("1".equals(qupdate_flag) ? View.GONE : View.VISIBLE);
        tv_upinfo.setText(upDateInfo);

        tv_ok.setOnClickListener(this);
        tv_no.setOnClickListener(this);

    }

    @Override
    protected void initData() {

    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.tv_ok) {
            showProgressbarVisible();
            startDownloadAndInstall(apkUrl);
        } else if (i == R.id.tv_no) {
            if(dissmissListener!=null)
                dissmissListener.diss();
            dismiss();
        }
    }


    private void showProgressbarVisible() {
        ll_update.setVisibility(View.GONE);
        ll_bottom.setVisibility(View.VISIBLE);
    }

    /**
     * 下载并自动安装
     *
     * @param url
     */

    public void startDownloadAndInstall(String url) {
        UpdateAppBean updateAppBean = new UpdateAppBean();

        //设置 apk 的下载地址
        updateAppBean.setApkFileUrl(IpConfig.httpBase + url);

        String path = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) || !Environment.isExternalStorageRemovable()) {
            try {
                path = getContext().getExternalCacheDir().getAbsolutePath();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (TextUtils.isEmpty(path)) {
                path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
            }
        } else {
            path = getContext().getCacheDir().getAbsolutePath();
        }

        //设置apk 的保存路径
        updateAppBean.setTargetPath(path);

        updateAppBean.setHttpManager(new UpdateAppHttpUtil());

        UpdateAppManager.download(getContext(), updateAppBean, new DownloadService.DownloadCallback() {
            @Override
            public void onStart() {
            }

            @Override
            public void onProgress(float progress, long totalSize) {
                numberProgressBar.setProgress(Math.round(progress * 100));
            }

            @Override
            public void setMax(long totalSize) {
            }

            @Override
            public boolean onFinish(File file) {
                dismiss();
                return true;//true，下载完自动安装
            }

            @Override
            public void onError(String msg) {
            }

            @Override
            public boolean onInstallAppAndAppOnForeground(File file) {
                return false;
            }
        });
    }

}
