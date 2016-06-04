package com.sage.hedonicmentality.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/31.
 */
public class OrderListBean {

    private ArrayList<Order> orderList;

    public OrderListBean() {
    }

    public OrderListBean(ArrayList orderList) {
        super();
        this.orderList = orderList;
    }

    public List<Order> getOder() {
        return orderList;
    }

    public void setOder(ArrayList<Order> orderList) {
        this.orderList = orderList;
    }
}
