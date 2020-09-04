package com.common.base.utils.base;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by HSK° on 2018/9/10.
 * --function: json转化为object
 */
public class JsonTool {



    @SuppressWarnings("unchecked")
    public static Object getObject4JsonString(String jsonString, Class pojoCalss) {
        Object pojo;
        Gson gson = new Gson();
        pojo = gson.fromJson(jsonString, pojoCalss);
        return pojo;

    }





    public static Map<String, Object> getMap4Json(String jsonString) {
        Gson gson = new Gson();
        Map valueMap ;
        valueMap = gson.fromJson(jsonString, Map.class);
        return valueMap;
    }
    /**
     * 将对象转换为json字符串
     */
    public static String obj2JSON(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    /**
     * map 转换成jsonString
     *
     */
    public static Map<String, Object> Stringtomap(String[] mapStr) {
        Map<String, Object> map = null;
        if (mapStr != null && mapStr.length > 0) {
            map = new HashMap<>();
            for (String aMapStr : mapStr) {
                String[] temp = aMapStr.split(":");
                map.put(temp[0], temp[1]);
            }
        }
        return map;

    }

    /**
     * map 转换成jsonString
     *
     * @param map
     * @return
     */
    public static String maptojson(Map<String, Object> map) {
        String jsonsstring = "{";
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (value == null) {
                value = "";
            }
            jsonsstring += "\"" + key.toString() + "\":\""
                    + BaseDataUtil.filterEmoji(BaseDataUtil.GBK2Unicode(value.toString())).replace("\n", "\\n") + "\",";

        }
        jsonsstring = jsonsstring.substring(0, jsonsstring.length() - 1) + "}";
        System.out.println("jsonsstring=" + jsonsstring);
        return jsonsstring;

    }

}
