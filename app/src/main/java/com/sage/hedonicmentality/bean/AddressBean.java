package com.sage.hedonicmentality.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2015/11/4.
 */
public class AddressBean implements Serializable {
    private ArrayList<Country> data  =  new ArrayList<Country>();

    public AddressBean() {
    }

    public AddressBean(ArrayList<Country> data) {
        this.data = data;
    }

    public ArrayList<Country> getData() {
        return data;
    }

    public void setData(ArrayList<Country> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "AddressBean{" +
                "data=" + data +
                '}';
    }
}
