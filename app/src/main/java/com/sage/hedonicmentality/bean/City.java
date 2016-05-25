package com.sage.hedonicmentality.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/11/4.
 *
 */
public class City implements Serializable {
    private String region_id;
    private String parent_id;
    private String region_name;

    public City() {
    }

    public City(String parent_id, String region_id, String region_name) {
        this.parent_id = parent_id;
        this.region_id = region_id;
        this.region_name = region_name;
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

    @Override
    public String toString() {
        return "City{" +
                "parent_id='" + parent_id + '\'' +
                ", region_id='" + region_id + '\'' +
                ", region_name='" + region_name + '\'' +
                '}';
    }
}
