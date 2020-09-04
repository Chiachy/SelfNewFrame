package com.common.base.permission;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;

import java.util.List;

/**
 * Created by HSK° on 2018/1/30.
 * --function:权限申请工具类
 */

public class PermissionUtils {

    private Context context;
    private Activity activity;

    private Rationale mRationale;
    private PermissionSetting mSetting;

    private PermissionUtils() {
    }

    private static class Holder {
        private static PermissionUtils INSTANCE = new PermissionUtils();
    }

    public static PermissionUtils getInstance() {
        return Holder.INSTANCE;
    }

    public PermissionUtils init(@NonNull Context context) {
        this.context = context;
        mRationale =new DefultRational();
        mSetting =new PermissionSetting(activity==null?context:activity);
        return this;
    }

    public PermissionUtils init(@NonNull Activity activity) {
        this.activity = activity;
        mRationale =new DefultRational();
        mSetting =new PermissionSetting(context==null?activity:context);
        return this;
    }

    /**
     *
     * @param callBack       成功的 接口回调
     * @param permissions   请求单个或多个权限.
     *                      失败的接口回调不用用户去处理，这里已经处理好了。
     * @return
     */
    public PermissionUtils Permission(final PermissionCallBack callBack, String... permissions) {

        AndPermission.with(context == null ? activity : context)
                .permission(permissions)
                .rationale(mRationale)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        if(callBack!=null){
                            callBack.OnPermissionSuccess(permissions);
                        }
                    }
                })
                .onDenied(new Action() {
                    @Override
                    public void onAction(@NonNull List<String> permissions) {
                        if(callBack!=null) callBack.OnPermissionFail(permissions);

                        if (AndPermission.hasAlwaysDeniedPermission(context == null ? activity : context, permissions)) {
                            mSetting.showSetting(permissions);
                        }
                    }
                })
                .start();

        return this;
    }

//    /**
//     *
//     * @param callBack
//     * @param permissions 请求单个或多个权限组
//     *                    todo:有个bug ，获取Phone权限组时候，已经给了拨打电话权限，会被提示没有给！后续待跟进，或者直接用多个权限写出来就行。
//     *                    用一个权限申请的方法
//     * @return
//     */
//    public PermissionUtils Permission(final PermissionCallBack callBack, String[]... permissions) {
//
//        AndPermission.with(context == null ? activity : context)
//                .permission(permissions)
//                .rationale(mRationale)
//                .onGranted(new Action() {
//                    @Override
//                    public void onAction(List<String> permissions) {
//                        if (callBack != null) {
//                            callBack.OnPermissionSuccess(permissions);
//                        }
//                    }
//                })
//                .onDenied(new Action() {
//                    @Override
//                    public void onAction(@NonNull List<String> permissions) {
//
//                        if(callBack!=null) callBack.OnPermissionFail(permissions);
//                        /**
//                         * 以下为 权限总是被用户拒绝弹出提示，
//                         */
//                        if (AndPermission.hasAlwaysDeniedPermission(context == null ? activity : context, permissions)) {
//                            mSetting.showSetting(permissions);
//                        }
//                    }
//                })
//                .start();
//
//        return this;
//    }




}
