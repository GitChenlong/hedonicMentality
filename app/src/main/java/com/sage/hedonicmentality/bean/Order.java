package com.sage.hedonicmentality.bean;

/**
 * Created by Administrator on 2016/5/31.
 */
public class Order {
//    starttime：开始时间（时间戳）
//    endtime：结束时间（时间戳）
//    orderid：订单id
//    ordernum：订单号
//    m_userid：用户id
//    m_username: 用户名
//    m_password：用户密码
//    m_subAccountSid 用户云通讯账号id
//    m_subToken:用户云通讯口令
//    m_voipAccount：用户云通讯账号
//    m_voipPwd：用户云通讯密码
//    z_userid：咨询师id
//    z_username: 咨询师名
//    z_password：咨询师密码
//    z_subAccountSid 咨询师云通讯账号id
//    z_subToken:咨询师云通讯口令
//    z_voipAccount：咨询师云通讯账号
//    z_voipPwd：咨询师云通讯密码
//    price：价格
//    order_status：0 未确认 1已确认 2已取消 3无效 4退款
//    shipping_status：0 未咨询 1 咨询中 2已咨询
//    pay_status：支付状态 0 未付款 1 已付款
    private String starttime = "";
    private String endtime = "";
    private String orderid = "";
    private String ordernum = "";
    private String m_userid = "";
    private String m_username = "";
    private String m_password = "";
    private String m_subAccountSid = "";
    private String m_subToken = "";
    private String m_voipAccount = "";
    private String m_voipPwd = "";
    private String z_userid = "";
    private String z_username = "";
    private String z_password = "";
    private String z_subAccountSid = "";
    private String z_subToken = "";
    private String z_voipAccount = "";
    private String z_voipPwd = "";
    private String price = "";
    private String order_status = "";
    private String shipping_status = "";
    private String pay_status = "";

    public Order(String starttime, String endtime, String orderid, String ordernum, String m_userid, String m_username, String m_password, String m_subAccountSid, String m_subToken, String m_voipAccount, String m_voipPwd, String z_userid, String z_username, String z_password, String z_subAccountSid, String z_subToken, String z_voipAccount, String z_voipPwd, String price, String order_status, String shipping_status, String pay_status) {
        this.starttime = starttime;
        this.endtime = endtime;
        this.orderid = orderid;
        this.ordernum = ordernum;
        this.m_userid = m_userid;
        this.m_username = m_username;
        this.m_password = m_password;
        this.m_subAccountSid = m_subAccountSid;
        this.m_subToken = m_subToken;
        this.m_voipAccount = m_voipAccount;
        this.m_voipPwd = m_voipPwd;
        this.z_userid = z_userid;
        this.z_username = z_username;
        this.z_password = z_password;
        this.z_subAccountSid = z_subAccountSid;
        this.z_subToken = z_subToken;
        this.z_voipAccount = z_voipAccount;
        this.z_voipPwd = z_voipPwd;
        this.price = price;
        this.order_status = order_status;
        this.shipping_status = shipping_status;
        this.pay_status = pay_status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "starttime='" + starttime + '\'' +
                ", endtime='" + endtime + '\'' +
                ", orderid='" + orderid + '\'' +
                ", ordernum='" + ordernum + '\'' +
                ", m_userid='" + m_userid + '\'' +
                ", m_username='" + m_username + '\'' +
                ", m_password='" + m_password + '\'' +
                ", m_subAccountSid='" + m_subAccountSid + '\'' +
                ", m_subToken='" + m_subToken + '\'' +
                ", m_voipAccount='" + m_voipAccount + '\'' +
                ", m_voipPwd='" + m_voipPwd + '\'' +
                ", z_userid='" + z_userid + '\'' +
                ", z_username='" + z_username + '\'' +
                ", z_password='" + z_password + '\'' +
                ", z_subAccountSid='" + z_subAccountSid + '\'' +
                ", z_subToken='" + z_subToken + '\'' +
                ", z_voipAccount='" + z_voipAccount + '\'' +
                ", z_voipPwd='" + z_voipPwd + '\'' +
                ", price='" + price + '\'' +
                ", order_status='" + order_status + '\'' +
                ", shipping_status='" + shipping_status + '\'' +
                ", pay_status='" + pay_status + '\'' +
                '}';
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getOrdernum() {
        return ordernum;
    }

    public void setOrdernum(String ordernum) {
        this.ordernum = ordernum;
    }

    public String getM_userid() {
        return m_userid;
    }

    public void setM_userid(String m_userid) {
        this.m_userid = m_userid;
    }

    public String getM_username() {
        return m_username;
    }

    public void setM_username(String m_username) {
        this.m_username = m_username;
    }

    public String getM_password() {
        return m_password;
    }

    public void setM_password(String m_password) {
        this.m_password = m_password;
    }

    public String getM_subAccountSid() {
        return m_subAccountSid;
    }

    public void setM_subAccountSid(String m_subAccountSid) {
        this.m_subAccountSid = m_subAccountSid;
    }

    public String getM_subToken() {
        return m_subToken;
    }

    public void setM_subToken(String m_subToken) {
        this.m_subToken = m_subToken;
    }

    public String getM_voipAccount() {
        return m_voipAccount;
    }

    public void setM_voipAccount(String m_voipAccount) {
        this.m_voipAccount = m_voipAccount;
    }

    public String getM_voipPwd() {
        return m_voipPwd;
    }

    public void setM_voipPwd(String m_voipPwd) {
        this.m_voipPwd = m_voipPwd;
    }

    public String getZ_userid() {
        return z_userid;
    }

    public void setZ_userid(String z_userid) {
        this.z_userid = z_userid;
    }

    public String getZ_username() {
        return z_username;
    }

    public void setZ_username(String z_username) {
        this.z_username = z_username;
    }

    public String getZ_password() {
        return z_password;
    }

    public void setZ_password(String z_password) {
        this.z_password = z_password;
    }

    public String getZ_subAccountSid() {
        return z_subAccountSid;
    }

    public void setZ_subAccountSid(String z_subAccountSid) {
        this.z_subAccountSid = z_subAccountSid;
    }

    public String getZ_subToken() {
        return z_subToken;
    }

    public void setZ_subToken(String z_subToken) {
        this.z_subToken = z_subToken;
    }

    public String getZ_voipAccount() {
        return z_voipAccount;
    }

    public void setZ_voipAccount(String z_voipAccount) {
        this.z_voipAccount = z_voipAccount;
    }

    public String getZ_voipPwd() {
        return z_voipPwd;
    }

    public void setZ_voipPwd(String z_voipPwd) {
        this.z_voipPwd = z_voipPwd;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getShipping_status() {
        return shipping_status;
    }

    public void setShipping_status(String shipping_status) {
        this.shipping_status = shipping_status;
    }

    public String getPay_status() {
        return pay_status;
    }

    public void setPay_status(String pay_status) {
        this.pay_status = pay_status;
    }
}
