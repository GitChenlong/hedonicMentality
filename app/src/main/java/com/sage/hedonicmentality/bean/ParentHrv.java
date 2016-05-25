package com.sage.hedonicmentality.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sage on 2015/8/17.
 */
public class ParentHrv {
    public String month;
    public String day;
    public int type;/**0表示只有一条数据，不显示箭头，1，表示向下箭头，2表示向上箭头*/
    public boolean isFirst=false;/**第一条数据默认显示4条的，其他默认都是1条，所以这里区分下*/
    public List<HRVData> list = new ArrayList<HRVData>();
    public int  size;/**当天有几条数据，其实就是上边list的大小*/
    public boolean showHeader=false;
}
