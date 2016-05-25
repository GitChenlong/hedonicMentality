package com.sage.hedonicmentality.bean;

import java.util.ArrayList;

/**
 * Created by Sage on 2015/8/19.
 */
public class HRVDataBase {
    public int info;
    public String tip;
    public ArrayList<HRVData> data;

    public int getInfo() {
        return info;
    }

    public void setInfo(int info) {
        this.info = info;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public ArrayList<HRVData> getData() {
        return data;
    }

    public void setData(ArrayList<HRVData> data) {
        this.data = data;
    }
}
