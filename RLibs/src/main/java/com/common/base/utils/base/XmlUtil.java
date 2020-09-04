package com.common.base.utils.base;

import com.google.gson.Gson;

import java.util.Map;

/**
 * Created by HSK° on 2018/9/10.
 * --function:  对象与json转换
 */
public class XmlUtil {

    /**
     * 对象转json
     */
    public static String obj2JSON(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> json2map(String a){
        Gson h=new Gson();
        return  h.fromJson(a,Map.class);
    }
}
