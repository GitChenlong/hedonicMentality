package com.sage.hedonicmentality.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sage on 2015/8/11.
 */
@Table(name = "hrvData")
public class HRVData implements Serializable{
    @Column(column = "interval")
    public int interval;/**练习时长*/
    @Id
    @NoAutoIncrement
    public long actionid;/**开始练习的时间，用来区分不同的记录*/
    @Column(column = "score")
    public int score;/**得分*/
    @Column(column = "coherence")
    public int coherence;/**心意合一程度*/
    @Column(column = "bpm")
    public int bpm;/**每分钟呼吸数*/
    @Column(column = "hr")
    public float hr;
    @Column(column = "hrmax")
    public float hrmax;
    @Column(column = "hrmean")
    public float hrmean;
    @Column(column = "nnmax")
    public float nnmax;
    @Column(column = "nnmean")
    public float nnmean;
    @Column(column = "sdnn")
    public float sdnn;
    @Column(column = "sumsdnn")
    public float sumsdnn;
    @Column(column = "rmssd")
    public float rmssd;
    @Column(column = "lf_hf")
    public float lf_hf;
    @Column(column = "lf")
    public  float lf;
    @Column(column = "hf")
    public float hf;
    @Column(column = "totalpower")
    public float totalpower;
    @Column(column = "month")
    public String month;
    @Column(column = "day")
    public String day;
    @Column(column = "hour")
    public String hour;
    @Column(column = "upload")
    public int upload;
    /**0表示未上传，1表示上传成功的*/
    @Column(column = "device")
    public String device="camera";/**是bluetooth蓝牙或者摄像头camera*/
    @Column(column = "terminal")
    public String terminal="android";
    @Transient //忽略这个列，不存入数据库
    public float weekaveragehrv;
    @Transient
    public int color=1;

    public HRVData(){}

    public HRVData(int interval, long actionID,float hr, float HRmax,
               float HRmean, float NNmax, float NNmean, float SDNN, float RMSSD, float LF_HF, float LF, float HF, float totalPower,float sumsdnn) {
        this.interval = interval;
        this.actionid = actionID;
        this.hr=convert(hr);
        this.hrmax = convert(HRmax);
        this.hrmean = convert(HRmean);
        this.nnmax = convert(NNmax);
        this.nnmean = convert(NNmean);
        this.sdnn = convert(SDNN);
        this.rmssd = convert(RMSSD);
        this.lf = convert(LF);
        this.hf = convert(HF);
        this.lf_hf = convert(LF_HF);
        this.totalpower = convert(totalPower);
        this.sumsdnn = convert(sumsdnn);
        this.terminal = terminal;
        initDate(actionID);
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    private void initDate(long actionID) {
//        month=new SimpleDateFormat("yyyy/MM").format(new Date(actionID));
        month=new SimpleDateFormat("MM").format(new Date(actionID));
        day=new SimpleDateFormat("yyyy/MM/dd").format(new Date(actionID));
        hour=new SimpleDateFormat("HH:mm").format(new Date(actionID));
    }

    private float convert(float original){
        int temp= Math.round(original*100);
        return temp/100.0f;
    }

    public void setActionId(long actionId){
        this.actionid=actionId;
        initDate(actionid);
    }

    @Override
    public String toString() {
        return "\n[hf="+hf+"] [lf="+lf+"] [lf/hf="+lf_hf+"] [tp="+totalpower+"] [sdnn = +"+sdnn+"] [sumsdnn = +"+sumsdnn+"]\n";
    }

    public float getHf() {
        return hf;
    }

    public void setHf(float hf) {
        this.hf = hf;
    }

    public float getLf() {
        return lf;
    }

    public void setLf(float lf) {
        this.lf = lf;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public long getActionid() {
        return actionid;
    }

    public void setActionid(long actionid) {
        this.actionid = actionid;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getCoherence() {
        return coherence;
    }

    public void setCoherence(int coherence) {
        this.coherence = coherence;
    }

    public int getBpm() {
        return bpm;
    }

    public void setBpm(int bpm) {
        this.bpm = bpm;
    }

    public float getHr() {
        return hr;
    }

    public void setHr(float hr) {
        this.hr = hr;
    }

    public float getHrmax() {
        return hrmax;
    }

    public void setHrmax(float hrmax) {
        this.hrmax = hrmax;
    }

    public float getHrmean() {
        return hrmean;
    }

    public void setHrmean(float hrmean) {
        this.hrmean = hrmean;
    }

    public float getNnmax() {
        return nnmax;
    }

    public void setNnmax(float nnmax) {
        this.nnmax = nnmax;
    }

    public float getNnmean() {
        return nnmean;
    }

    public void setNnmean(float nnmean) {
        this.nnmean = nnmean;
    }

    public float getSdnn() {
        return sdnn;
    }

    public void setSdnn(float sdnn) {
        this.sdnn = sdnn;
    }

    public float getSumsdnn() {
        return sumsdnn;
    }

    public void setSumsdnn(float sumsdnn) {
        this.sumsdnn = sumsdnn;
    }

    public float getRmssd() {
        return rmssd;
    }

    public void setRmssd(float rmssd) {
        this.rmssd = rmssd;
    }

    public float getLf_hf() {
        return lf_hf;
    }

    public void setLf_hf(float lf_hf) {
        this.lf_hf = lf_hf;
    }

    public float getTotalpower() {
        return totalpower;
    }

    public void setTotalpower(float totalpower) {
        this.totalpower = totalpower;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public int getUpload() {
        return upload;
    }

    public void setUpload(int upload) {
        this.upload = upload;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public float getWeekaveragehrv() {
        return weekaveragehrv;
    }

    public void setWeekaveragehrv(float weekaveragehrv) {
        this.weekaveragehrv = weekaveragehrv;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
