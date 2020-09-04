package com.common.base.bean;

import java.util.List;
import java.util.Map;

/**
 * Created by HSK° on 2018/9/10.
 * --function:与后台通讯实体类(调用sql)
 */
public class PubDataList {
    private Page page;
    private String code;
    private List<Map<String, Object>> data;
    private Map<String, Object> sendMap;
    private String msg;
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public Map<String, Object> getSendMap() {
        return sendMap;
    }
    public void setSendMap(Map<String, Object> sendMap) {
        this.sendMap = sendMap;
    }
    public Page getPage() {
        return page;
    }
    public void setPage(Page page) {
        this.page = page;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public List<Map<String, Object>> getData() {
        return data;
    }
    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }


}
