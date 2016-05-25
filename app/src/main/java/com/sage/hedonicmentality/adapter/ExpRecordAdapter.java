package com.sage.hedonicmentality.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.bean.HRVData;
import com.sage.hedonicmentality.bean.ParentHrv;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Sage on 2015/8/17.
 */
public class ExpRecordAdapter extends BaseExpandableListAdapter {

    public void setParentHrvs(List<ParentHrv> parentHrvs) {
        this.parentHrvs = parentHrvs;
        //notifyDataSetChanged();
    }

    public ExpRecordAdapter(Context context) {
        mContext=context;
    }
    private Context mContext;
    List<ParentHrv> parentHrvs=new ArrayList<>();


    @Override
    public int getGroupCount() {
        return parentHrvs==null?0:parentHrvs.size();
    }

    private int minShow=1;
    @Override
    public int getChildrenCount(int groupPosition) {
        List<HRVData> childList=getGroup(groupPosition).list;
        switch(getGroup(groupPosition).type){
            case 0:
                return childList==null?0:childList.size();
            case 1:
                return childList==null?0:(getGroup(groupPosition).isFirst?5:(minShow+1));
            case 2:
                return childList==null?0:(childList.size()+1);
        }
        return 0;
    }

    @Override
    public ParentHrv getGroup(int groupPosition) {
        return parentHrvs.get(groupPosition);
    }

    @Override
    public HRVData getChild(int groupPosition, int childPosition) {
        List<HRVData> childList=getGroup(groupPosition).list;
        return childList==null?null:childList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if(convertView==null){
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record_parent,parent,false);
        }
        LinearLayout layout_divider= (LinearLayout) convertView.findViewById(R.id.layout_divider);
        LinearLayout layout_parent= (LinearLayout) convertView.findViewById(R.id.layout_parent);
        View line=convertView.findViewById(R.id.line_header_top);
        layout_divider.setVisibility(groupPosition==0?View.GONE:View.VISIBLE);
        layout_parent.setVisibility(getGroup(groupPosition).showHeader?View.VISIBLE:View.GONE);
        line.setVisibility(getGroup(groupPosition).showHeader?View.INVISIBLE:View.VISIBLE);
        TextView tv_date= (TextView) convertView.findViewById(R.id.tv_date);
        TextView tv_duration= (TextView) convertView.findViewById(R.id.tv_duration);
        TextView tv_percent= (TextView) convertView.findViewById(R.id.tv_percent);
        TextView tv_score= (TextView) convertView.findViewById(R.id.tv_score);
//        String  mo  = new SimpleDateFormat("dd").format(new Date(getGroup(groupPosition).month))+"月";
        tv_date.setText(getGroup(groupPosition).month+"月");
        tv_duration.setText("时长");
        tv_percent.setText("生命值");
        tv_score.setText("心率");

        convertView.setOnClickListener(null);
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if(getGroup(groupPosition).type==1&&isLastChild){
            convertView=LayoutInflater.from(mContext).inflate(R.layout.item_launch_arrow,parent,false);
            ImageView iv_show= (ImageView) convertView.findViewById(R.id.iv_show);
            iv_show.setImageResource(R.mipmap.jiantou_down);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getGroup(groupPosition).type=2;
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }
        if(getGroup(groupPosition).type==2&&isLastChild){
            convertView=LayoutInflater.from(mContext).inflate(R.layout.item_launch_arrow,parent,false);
            ImageView iv_show= (ImageView) convertView.findViewById(R.id.iv_show);
            iv_show.setImageResource(R.mipmap.jiantou_up);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getGroup(groupPosition).type=1;
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }


        convertView=LayoutInflater.from(mContext).inflate(R.layout.item_record_child,parent,false);
        TextView tv_date= (TextView) convertView.findViewById(R.id.tv_date);
        TextView tv_duration= (TextView) convertView.findViewById(R.id.tv_duration);
        TextView tv_percent= (TextView) convertView.findViewById(R.id.tv_percent);
        TextView tv_score= (TextView) convertView.findViewById(R.id.tv_score);
        LinearLayout layout_parent= (LinearLayout) convertView.findViewById(R.id.layout_parent);
        View line=convertView.findViewById(R.id.line_divider);
        HRVData hrvData=getChild(groupPosition,childPosition);
        if(hrvData!=null){
            String day="";
            if(childPosition==0){
                day= "Day "+new SimpleDateFormat("dd").format(new Date(hrvData.actionid));
//                layout_parent.setBackgroundResource(R.color.huis);
                tv_date.setText(day);
            }else {
                tv_date.setText(hrvData.hour);
            }
//            tv_date.setText(day+hrvData.hour);
            tv_duration.setText(""+hrvData.interval+"分");
            tv_score.setText("" + (int)hrvData.hr);
            tv_percent.setText("+" + hrvData.score);
        }
        if((getChildrenCount(groupPosition)>(groupPosition==0?4:1)&&childPosition==getChildrenCount(groupPosition)-2)||getChildrenCount(groupPosition)==1){
           line.setVisibility(View.INVISIBLE);
        }else {
            line.setVisibility(View.VISIBLE);
        }
        convertView.setOnClickListener(null);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }





}
