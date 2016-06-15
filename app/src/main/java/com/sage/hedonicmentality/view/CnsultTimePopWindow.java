package com.sage.hedonicmentality.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.bean.Indicate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/30.
 */
public class CnsultTimePopWindow extends PopupWindow {

    private final View mMenuView;
    private final ViewFlipper viewfipper;
    private final ListView lv_date;
    private final ListView lv_time;
    private final MyListAdapter dateAdapter;
    private final MyListAdapter timeAdapter;
    private final Indicate dateIndicate;
    private final Indicate timeIndicate;
    private Context mcontext;
    private Handler mHandler;
    private List<String> mdateList;
    private List<String> mtimelist;

    public CnsultTimePopWindow(Context context,Handler handler, final List<String> dateList, final List<String> timelist
            ,Indicate dateIn ,Indicate timeIn) {
        super(context);
//        setAnimationStyle(R.style.cnsultAnimation);
        this.mcontext = context;
        this.mHandler = handler;
        this.mdateList = dateList;
        this.mtimelist = timelist;
        this.dateIndicate =dateIn;
        this.timeIndicate =timeIn;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(mcontext.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.cnsulttimepopwindow, null);
        viewfipper = new ViewFlipper(context);
        viewfipper.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        lv_date = (ListView)mMenuView.findViewById(R.id.lv_date);
        lv_time = (ListView)mMenuView.findViewById(R.id.lv_time);
        lv_date.setDividerHeight(0);
        lv_time.setDividerHeight(0);
        dateAdapter = new MyListAdapter(mcontext,dateIndicate,mdateList);
        timeAdapter = new MyListAdapter(mcontext,timeIndicate,mtimelist);
        lv_date.setAdapter(dateAdapter);
        lv_time.setAdapter(timeAdapter);
        lv_date.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dateIndicate.setPostion(position);
                dateAdapter.notifyDataSetChanged();
            }
        });

        lv_time.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                timeIndicate.setPostion(position);
                timeAdapter.notifyDataSetChanged();
                dismiss();
            }
        });
        viewfipper.addView(mMenuView);
        viewfipper.setFlipInterval(6000000);
        this.setContentView(viewfipper);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);
//        this.setOutsideTouchable(true);
        this.update();
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                String datename = mdateList.get(dateIndicate.getPostion());
                String timename = mtimelist.get(timeIndicate.getPostion());
                Message message = new Message();
                message.what=1;
                Bundle bundle = new Bundle();
                bundle.putString("datename",datename);
                bundle.putString("timename",timename);
                message.setData(bundle);
                mHandler.sendMessage(message);
            }
        });
    }


    class MyListAdapter extends BaseAdapter{
        private Context mContext;
        private Indicate indicate;
        private List<String> mList = new ArrayList<>();

        public MyListAdapter(Context context,Indicate indicates,List<String> list) {
            this.mContext = context;
            this.indicate = indicates;
            this.mList = list;
        }

        @Override
        public int getCount() {

            return mList==null? 0 :mList.size();
        }

        @Override
        public String getItem(int position) {

            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.consultitem,parent,false);
            }
                TextView name = (TextView)convertView.findViewById(R.id.tv_name);
                name.setText(mList.get(position));
                if (indicate.getPostion()==position) {
                    name.setTextColor(mContext.getResources().getColor(R.color.green_essential_colour));
                }else{
                    name.setTextColor(mContext.getResources().getColor(R.color.grays));
                }
            return convertView;
        }
    }
    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        viewfipper.startFlipping();
    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
        viewfipper.startFlipping();
    }
}
