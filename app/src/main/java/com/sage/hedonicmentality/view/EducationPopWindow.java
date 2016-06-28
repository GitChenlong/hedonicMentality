package com.sage.hedonicmentality.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.bean.Indicate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/30.
 */
public class EducationPopWindow extends PopupWindow {

    private final View mMenuView;
    private final ViewFlipper viewfipper;
    private final MyListAdapter adapter;
    private final ListView listview;
    private final int mType;
    private Context mcontext;
    private Handler mHandler;
    private String[] mdateList;
    public EducationPopWindow(Context context, Handler handler, final String[]dateList,int type
            ) {
        super(context);
//        setAnimationStyle(R.style.cnsultAnimation);
        this.mcontext = context;
        this.mHandler = handler;
        this.mdateList = dateList;
        this.mType = type;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(mcontext.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.educationpopwindow, null);
        viewfipper = new ViewFlipper(context);
        viewfipper.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        listview = (ListView)mMenuView.findViewById(R.id.listview);
        adapter = new MyListAdapter(mcontext,mdateList);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Message message = new Message();
                message.what=mType;
                Bundle bundle = new Bundle();
                bundle.putString("data", dateList[position]);
                message.setData(bundle);
                mHandler.sendMessage(message);
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
        this.update();
    }


    class MyListAdapter extends BaseAdapter{
        private Context mContext;
        private String[] mList ;

        public MyListAdapter(Context context,String[] list) {
            this.mContext = context;
            this.mList = list;
        }

        @Override
        public int getCount() {

            return mList==null? 0 :mList.length;
        }

        @Override
        public String getItem(int position) {

            return mList[position];
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
                name.setText(mList[position]);

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
