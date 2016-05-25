package com.sage.hedonicmentality.bean;

/**
 * Created by Administrator on 2015/8/17.
 */
public class Userinfo {
    private  String mobile_phone;
    private  String password;
    private  String nick_name;
    private  int sex;
    private  String birthday;
    private  Double height;
    private  Double weight;

    public Userinfo() {

    }

    public Userinfo(String mobile_phone,String nick_name, int sex, String birthday, Double height, Double weight) {
        this.mobile_phone = mobile_phone;
        this.nick_name = nick_name;
        this.sex = sex;
        this.birthday = birthday;
        this.height = height;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Userinfo{" +
                "mobile_phone='" + mobile_phone + '\'' +
                ", password='" + password + '\'' +
                ", nick_name='" + nick_name + '\'' +
                ", sex=" + sex +
                ", birthday='" + birthday + '\'' +
                ", height=" + height +
                ", weight=" + weight +
                '}';
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getMobile_phone() {
        return mobile_phone;
    }

    public void setMobile_phone(String mobile_phone) {
        this.mobile_phone = mobile_phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }
}
