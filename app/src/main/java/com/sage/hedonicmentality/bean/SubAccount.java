package com.sage.hedonicmentality.bean;

/**
 * Created by Administrator on 2016/5/31.
 */
public class SubAccount {

    String subAccountSid;
    String subToken;
    String dateCreated ;
    String voipAccount ;
    String voipPwd ;

    public SubAccount(String subAccountSid, String subToken, String dateCreated, String voipAccount, String voipPwd) {
        this.subAccountSid = subAccountSid;
        this.subToken = subToken;
        this.dateCreated = dateCreated;
        this.voipAccount = voipAccount;
        this.voipPwd = voipPwd;
    }

    @Override
    public String toString() {
        return "SubAccount{" +
                "subAccountSid='" + subAccountSid + '\'' +
                ", subToken='" + subToken + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", voipAccount='" + voipAccount + '\'' +
                ", voipPwd='" + voipPwd + '\'' +
                '}';
    }

    public String getSubAccountSid() {
        return subAccountSid;
    }

    public void setSubAccountSid(String subAccountSid) {
        this.subAccountSid = subAccountSid;
    }

    public String getSubToken() {
        return subToken;
    }

    public void setSubToken(String subToken) {
        this.subToken = subToken;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getVoipAccount() {
        return voipAccount;
    }

    public void setVoipAccount(String voipAccount) {
        this.voipAccount = voipAccount;
    }

    public String getVoipPwd() {
        return voipPwd;
    }

    public void setVoipPwd(String voipPwd) {
        this.voipPwd = voipPwd;
    }
}
