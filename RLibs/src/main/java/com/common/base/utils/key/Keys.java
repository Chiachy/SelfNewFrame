package com.common.base.utils.key;

import android.content.Context;
import android.text.TextUtils;

import com.common.base.global.Constants;
import com.common.base.utils.Config;
import com.common.base.utils.DateStr;
import com.common.base.utils.SPUtils;

/**
 * Created by HSK° on 2018/9/10.
 * --function:  秘钥的读取
 */
public class Keys {


    public static void setSid(String sid, Context context) {
        SPUtils ssid = SPUtils.getInstance(Constants.SSID,context);
        ssid.put(Constants.SID, sid);
    }

    public static String getSid(Context context) {
        SPUtils instance = SPUtils.getInstance(Constants.SSID,context);
        return instance.getString(Constants.SID);

    }

    public static void setTKey(String key,Context context) {
        try {
            SPUtils instance = SPUtils.getInstance(Constants.SSID,context);
            instance.put(Constants.TKEY, AESUtil.encrypt(key, getDKey(context)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String getTKey(Context context) {

        SPUtils spUtils = SPUtils.getInstance(Constants.SSID,context);
        String key = spUtils.getString(Constants.TKEY, "");
        try {
            if (!TextUtils.isEmpty(key)) {
                key = AESUtil.decrypt(key, getDKey(context));
            }
            return key;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * @return 原始秘钥
     */
    public static String getDKey(Context context) {
        String cryptogram = Config.getConfigInfo(
                context, "cryptogram") + DateStr.yyyymmddStr();
        cryptogram = cryptogram + (cryptogram.length() < 16 ?
                String.format("%1$0" + (16 - cryptogram.trim().length()) + "d", 0) : "");
        return cryptogram;
    }



}
