package com.sage.hedonicmentality.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/11/6.
 */
public class HMRecord implements Serializable{
    private String id = "0";
    private String user_id = "";
    private String sleep = "0";//睡眠
    private String appetite = "0";//胃口
    private String systolic = "0";//收缩血压
    private String diastolic = "0";//舒张血压
    private int editabled = 2;//1 = 可修改  0 = 不可修改

    private Long createtime;

    public HMRecord() {
    }

    public HMRecord(String appetite, Long createtime, String diastolic, int editabled, String id, String sleep, String systolic, String user_id) {
        this.appetite = appetite;
        this.createtime = createtime;
        this.diastolic = diastolic;
        this.editabled = editabled;
        this.id = id;
        this.sleep = sleep;
        this.systolic = systolic;
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "HMRecord{" +
                "appetite='" + appetite + '\'' +
                ", id='" + id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", sleep='" + sleep + '\'' +
                ", systolic='" + systolic + '\'' +
                ", diastolic='" + diastolic + '\'' +
                ", editabled=" + editabled +
                ", createtime=" + createtime +
                '}';
    }

    public String getAppetite() {
        return appetite;
    }

    public void setAppetite(String appetite) {
        this.appetite = appetite;
    }

    public Long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Long createtime) {
        this.createtime = createtime;
    }

    public String getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(String diastolic) {
        this.diastolic = diastolic;
    }

    public int getEditabled() {
        return editabled;
    }

    public void setEditabled(int editabled) {
        this.editabled = editabled;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSleep() {
        return sleep;
    }

    public void setSleep(String sleep) {
        this.sleep = sleep;
    }

    public String getSystolic() {
        return systolic;
    }

    public void setSystolic(String systolic) {
        this.systolic = systolic;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
