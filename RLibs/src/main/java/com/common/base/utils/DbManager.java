package com.common.base.utils;

import android.text.TextUtils;

import com.common.base.bean.UserInfo;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

/**
 * Created by HSK° on 2018/10/8.
 * --function:表管理类
 */
public class DbManager {


    /**
     * @return 查找id为1的用户信息表
     */
    public static UserInfo getUserinfo() {
        return LitePal.find(UserInfo.class, 1);
    }

    /**
     * 清空用户信息表;
     */
    public static void clearUserInfo() {
        UserInfo userInfo = LitePal.find(UserInfo.class, 1);
        if (userInfo != null) {
            userInfo.setUserId("");
            userInfo.setCompId("");
            userInfo.setUserName("");
            userInfo.setTelphone("");
            userInfo.setUserpwd("");
            userInfo.setRoletype("");
            userInfo.setZiplevel("");
            userInfo.setLoctype("");
            userInfo.saveOrUpdate("id =?", "1");
        }

    }


    /**
     * @return true 表示用户已经登入
     */
    public static boolean hadLogin() {
        UserInfo userInfo = LitePal.find(UserInfo.class, 1);
        return userInfo != null && !TextUtils.isEmpty(userInfo.getUserId());

    }

}
