package com.sage.hedonicmentality.bean;

/**
 * Created by Administrator on 2016/5/31.
 */
public class MUserInfo {
    String userid ;
    String username ;
    String realname;
    String mobile ;
    String type ;

    public MUserInfo(String userid, String username, String realname, String mobile, String type) {
        this.userid = userid;
        this.username = username;
        this.realname = realname;
        this.mobile = mobile;
        this.type = type;
    }

    @Override
    public String toString() {
        return "MUserInfo{" +
                "userid='" + userid + '\'' +
                ", username='" + username + '\'' +
                ", realname='" + realname + '\'' +
                ", mobile='" + mobile + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
