package com.common.base.permission;

import java.util.List;

/**
 * Created by HSK° on 2018/1/30.
 * --function:
 */

public interface PermissionCallBack{
    void OnPermissionSuccess(List<String> permissions);
    void OnPermissionFail(List<String> permissions);
}