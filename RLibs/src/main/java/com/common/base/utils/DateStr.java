package com.common.base.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by HSK° on 2018/9/10.
 * --function: 时间工具类
 */
@SuppressLint("SimpleDateFormat")
public class DateStr {


    /**
     *
     * @return 获取系统时间，转换成 YYYYMMDD 格式的日期字符串
     */
    public static String yyyymmddStr() {
        Date myDate = new Date(System.currentTimeMillis()); // 获取系统时间
         SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        return formatter.format(myDate);
    }


    /**
     *
     * @return 获取系统时间，转换成 YYMMDD 格式的日期字符串
     */
    public static String yymmddStr() {
        Date myDate = new Date(System.currentTimeMillis()); // 获取系统时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");
        return formatter.format(myDate);
    }





    public static String yyyyMMDD(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public static String yyyy(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        return format.format(date);
    }

    public static String yyyyMM(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        return format.format(date);
    }
}
