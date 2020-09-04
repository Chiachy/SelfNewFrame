package com.common.base.utils;

import androidx.annotation.NonNull;

import com.common.base.bean.PubData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author HSK
 * @date 2019/5/5
 * --------------获取List<Map<String,Object>工具类
 **/
@SuppressWarnings({"unchecked", "MismatchedQueryAndUpdateOfCollection","SpellCheckingInspection"})
public class DataUtils {


    /**
     * @param listName list名称(Key值)
     * @param pubData  pubData
     */
    public static List<Map<String, Object>> convertData(String listName,@NonNull PubData pubData) {
        return pubData.getData().get(listName) == null ? new ArrayList<Map<String, Object>>()
                : (List<Map<String, Object>>) pubData.getData().get(listName);
    }


    /**
     * @param name    key值
     * @param pubData 数据
     */
    public static String convertDaata2String(String name,@NonNull PubData pubData) {
        return pubData.getData().get(name) == null ? "" : String.valueOf(pubData.getData().get(name));
    }

    /**
     * @param name    key值
     * @param defult  默认值
     * @param pubData 数据
     * @return 字符串
     */
    public static String convertDaata2String(String name, String defult,@NonNull PubData pubData) {
        return pubData.getData().get(name) == null ? defult : String.valueOf(pubData.getData().get(name));
    }



}
