package com.sage.hedonicmentality.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2015/11/4.
 *
 */
public class Country implements Serializable {
    private String region_id;
    private String parent_id;
    private String region_name;
    private ArrayList<Province> children = new ArrayList<Province>();


    public Country(ArrayList<Province> children, String parent_id, String region_id, String region_name) {
        this.children = children;
        this.parent_id = parent_id;
        this.region_id = region_id;
        this.region_name = region_name;
    }

    public Country() {

    }

    @Override
    public String toString() {
        return "Country{" +
                "children=" + children +
                ", region_id='" + region_id + '\'' +
                ", parent_id='" + parent_id + '\'' +
                ", region_name='" + region_name + '\'' +
                '}';
    }

    public ArrayList<Province> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Province> children) {
        this.children = children;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getRegion_id() {
        return region_id;
    }

    public void setRegion_id(String region_id) {
        this.region_id = region_id;
    }

    public String getRegion_name() {
        return region_name;
    }

    public void setRegion_name(String region_name) {
        this.region_name = region_name;
    }
}
