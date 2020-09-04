package com.common.base.update;

import androidx.fragment.app.FragmentActivity;

import com.common.base.bean.PubData;
import com.common.base.net.HttpResponseCallBack;
import com.common.base.net.HttpUtil;
import com.common.base.utils.DataUtils;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

/**
 * Created by HSK° on 2018/9/14.
 * --function: app更新助手
 */
@SuppressWarnings("unchecked")
public class AppUpdateHelper {

    private FragmentActivity activity;

    private static final int APPUPDATE_CODE = 6;

    private AppUpdateHelper(FragmentActivity activity) {
        WeakReference<FragmentActivity> weakReference = new WeakReference<>(activity);
        this.activity = weakReference.get();
    }


    public AppUpdateHelper getInstance(FragmentActivity activity) {
        if (activity == null)
            throw new IllegalArgumentException("activity can‘t be null");
        return new AppUpdateHelper(activity);
    }


    /**
     * 检测是否有新版本
     *
     * @param map 存储过程
     */
    public void check(Map<String, Object> map) {
        HttpUtil.getInstance().loadData(map, APPUPDATE_CODE, new HttpResponseCallBack() {
            @Override
            public void onResponse(Object data, int what) {
                PubData pubData = (PubData) data;
                if (pubData != null && "00".equals(pubData.getCode())) {

                    List<Map<String, Object>> list = DataUtils.convertData("LIST", pubData);
                    isHaveNewVersion(list);
                }
            }
        });
    }


    /**
     * 更新   QCODE "01"表示有新版本  (接口返回的参数需要与服务端约定好)
     */

    private void isHaveNewVersion(List<Map<String, Object>> list) {
        if (list.size() > 0) {
            String isHaveNewVersion = list.get(0).get("QCODE") == null ? "00" : String.valueOf(list.get(0).get("QCODE"));
            if ("01".equals(isHaveNewVersion)) {
                UpdateFragment.getInstance(list).show(activity.getSupportFragmentManager(), "UPDATEFRAGMENT");
            }
        }
    }

}
