package com.rxkj.wananpovertyaid.utils;

import android.content.Context;
import android.text.TextUtils;

import com.common.base.utils.SPUtils;

/**
 * created by 黄思凯 on 2020/5/25
 *
 *         UserInfo userInfo = new UserInfo(getActivity());
 *
 *
 */
public class UserInfoSaver {

    private final SPUtils spUtils;

    public UserInfoSaver(Context context) {
        spUtils = SPUtils.getInstance("user", context);
    }

    public void saveUserInfo(String userName, String passWord, String loginName, String userId, String areaCode, String areaName) {
        spUtils.put("userName", userName);
        spUtils.put("password", passWord);
        spUtils.put("loginName", loginName);
        spUtils.put("userId", userId);
        spUtils.put("areaCode", areaCode);
        spUtils.put("areaName", areaName);

    }

    public void savePwd(String pwd) {
        spUtils.put("password", pwd);
    }

    /**
     * 是否登入
     */
    public boolean isLogin() {
        return !TextUtils.isEmpty(getLoginName()) && !TextUtils.isEmpty(getPassWord());
    }

    public void clearUser() {
        spUtils.clear();
    }

    public String getUserName() {
        return spUtils.getString("userName");
    }

    public String getLoginName() {
        return spUtils.getString("loginName");
    }

    public String getUserId() {
        return spUtils.getString("userId");
    }

    public String getAreaCode() {
        return spUtils.getString("areaCode");
    }

    public String getPassWord() {
        return spUtils.getString("password");
    }

    public String getAreaName() {
        return spUtils.getString("areaName");
    }
}
