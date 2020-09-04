package com.common.base.bean;

import java.util.Map;

/**
 * Created by HSK° on 2018/9/10.
 * --function: 与后台通讯实体类（调用存储过程）
 */
public class PubData {
    private Page page;
    private String sessionId;
    private String code;
    private Map<String, Object> data;
    private Map<String, Object> sendMap;
    private String msg;
    public Map<String, Object> getSendMap() {
        return sendMap;
    }
    public void setSendMap(Map<String, Object> sendMap) {
        this.sendMap = sendMap;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
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
    public Map<String, Object> getData() {
        return data;
    }
    public void setData(Map<String, Object> data) {
        this.data = data;
    }
    public String getSessionId() {
        return sessionId;
    }
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }


}

