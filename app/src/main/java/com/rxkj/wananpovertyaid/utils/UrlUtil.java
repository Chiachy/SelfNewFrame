package com.rxkj.wananpovertyaid.utils;

import android.util.Patterns;

import com.alibaba.android.arouter.utils.TextUtils;

/**
 * Created by Chiachi on 2019/7/5
 * Code'n'learn,ç'est la vie
 */

public class UrlUtil {

    public static String ToUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return url;
        } else if (!Patterns.WEB_URL.matcher(url.toString()).matches()) {
            // TODO: 2020/9/3 加上域名信息
//            url = ApiConst.BASE_URL + url;
            return url;
        } else {
            return url;
        }
    }
}
