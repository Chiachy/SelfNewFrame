package com.common.base.permission;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import androidx.appcompat.app.AlertDialog;

import com.common.base.R;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;

import java.util.List;

/**
 * Created by HSK° on 2018/1/30.
 * --function: 在权限请求中，被用户取消授予权限后，
 * 弹出来的提示对话框 (默认)
 */

public class DefultRational implements Rationale {
    @Override
    public void showRationale(Context context, List<String> permissions, final RequestExecutor executor) {
        List<String> permissionNames = Permission.transformText(context, permissions);
        String message = context.getString(R.string.message_permission_rationale, TextUtils.join("\n", permissionNames));

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true)
                .setTitle(R.string.title_dialog)
                .setMessage(message)
                .setPositiveButton(R.string.resume, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        executor.execute();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        executor.cancel();
                    }
                })
                .show();
    }
}
