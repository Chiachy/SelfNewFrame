package com.common.base.bean;

import org.litepal.crud.LitePalSupport;

/**
 * Created by HSK° on 2018/9/21.
 * --function:用户信息表
 */
public class UserInfo extends LitePalSupport {

    private String userId;
    private String compId;
    private String telphone;
    private String userpwd;
    private String roletype;
    private String Qdeptname;
    private String deptId;
    private String duty;
    private String loginName;

    private String ziplevel;
    private String loctype;
    private String sessionId;
    private String userName;

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getUserpwd() {
        return userpwd;
    }

    public void setUserpwd(String userpwd) {
        this.userpwd = userpwd;
    }

    public String getRoletype() {
        return roletype;
    }

    public void setRoletype(String roletype) {
        this.roletype = roletype;
    }

    public String getQdeptname() {
        return Qdeptname;
    }

    public void setQdeptname(String qdeptname) {
        Qdeptname = qdeptname;
    }

    public String getZiplevel() {
        return ziplevel;
    }

    public void setZiplevel(String ziplevel) {
        this.ziplevel = ziplevel;
    }

    public String getLoctype() {
        return loctype;
    }

    public void setLoctype(String loctype) {
        this.loctype = loctype;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}
