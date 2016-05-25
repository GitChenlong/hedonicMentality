package com.sage.hedonicmentality.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/11/17.
 */
public class SceneBean implements Serializable{
    private String id;
    private String name;//场景名字
    private String letter;//下载的zip名字
    private String index_html;//动画的首页
    private String rate_function;//设置一分钟呼吸多少次的函数
    private String start_function;//开的调用动画的函数
    private String range;//动画的层级
    private String createtime;//创建的时间
    private String downloadurl;//场景下载的地址
    private int filesize = 0;//下载文件的大小
    public SceneBean() {

    }

    public SceneBean(String id, String name, String letter, String index_html, String rate_function, String start_function, String range, String createtime, String downloadurl,int filesize) {
        this.id = id;
        this.name = name;
        this.letter = letter;
        this.index_html = index_html;
        this.rate_function = rate_function;
        this.start_function = start_function;
        this.range = range;
        this.createtime = createtime;
        this.downloadurl = downloadurl;
        this.filesize = filesize;
    }

    public int getFilesize() {
        return filesize;
    }

    public void setFilesize(int filesize) {
        this.filesize = filesize;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getDownloadurl() {
        return downloadurl;
    }

    public void setDownloadurl(String downloadurl) {
        this.downloadurl = downloadurl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIndex_html() {
        return index_html;
    }

    public void setIndex_html(String index_html) {
        this.index_html = index_html;
    }

    public String getRate_function() {
        return rate_function;
    }

    public void setRate_function(String rate_function) {
        this.rate_function = rate_function;
    }

    public String getStart_function() {
        return start_function;
    }

    public void setStart_function(String start_function) {
        this.start_function = start_function;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    @Override
    public String toString() {
        return "SceneBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", letter='" + letter + '\'' +
                ", index_html='" + index_html + '\'' +
                ", rate_function='" + rate_function + '\'' +
                ", start_function='" + start_function + '\'' +
                ", range='" + range + '\'' +
                ", createtime='" + createtime + '\'' +
                ", downloadurl='" + downloadurl + '\'' +
                '}';
    }
}
