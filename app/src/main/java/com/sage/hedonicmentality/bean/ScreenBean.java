package com.sage.hedonicmentality.bean;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/24.
 */
public class ScreenBean implements Serializable {
    private String price;
    private String age;
    private String sex;
    private String isOnLine;

    @Override
    public String toString() {
        return "ScreenBean{" +
                "price='" + price + '\'' +
                ", age='" + age + '\'' +
                ", sex='" + sex + '\'' +
                ", isOnLine='" + isOnLine + '\'' +
                '}';
    }

    public ScreenBean() {
    }

    public ScreenBean(String price, String age, String sex, String isOnLine) {
        this.price = price;
        this.age = age;
        this.sex = sex;
        this.isOnLine = isOnLine;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIsOnLine() {
        return isOnLine;
    }

    public void setIsOnLine(String isOnLine) {
        this.isOnLine = isOnLine;
    }
}
