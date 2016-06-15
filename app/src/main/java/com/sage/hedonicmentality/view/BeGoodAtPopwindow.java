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
import android.widget.ImageView;
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
public class BeGoodAtPopwindow extends PopupWindow {
    private final View mMenuView;
    private final ViewFlipper viewfipper;
    private final ListView lv_begoodat;
    private final MyListAdapter dateAdapter;
    private final Indicate dateIndicate;
    private Context mcontext;
    private Handler mHandler;
    private List<String> mlist = new ArrayList<>();
    public BeGoodAtPopwindow(Context context,Handler handler,Indicate indicate) {
        super(context);
//        setAnimationStyle(R.style.cnsultAnimation);
        this.mcontext = context;
        this.mHandler = handler;
        this.dateIndicate =indicate;
        mlist.add("不限");
        mlist.add("婚恋情感");
        mlist.add("情绪压力");
        mlist.add("亲子教育");
        mlist.add("职场发展");
        mlist.add("自我成长");
        mlist.add("人际关系");
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(mcontext.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.begoodatpopwindow, null);
        viewfipper = new ViewFlipper(context);
        viewfipper.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        lv_begoodat = (ListView)mMenuView.findViewById(R.id.lv_begoodat);
        dateAdapter = new MyListAdapter(mcontext,dateIndicate,mlist);
        lv_begoodat.setAdapter(dateAdapter);
        lv_begoodat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dateIndicate.setPostion(position);
                dateAdapter.notifyDataSetChanged();
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
        this.setOutsideTouchable(true);
        this.update();

        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                String select = mlist.get(dateIndicate.getPostion());
                Message message = new Message();
                message.what=2;
                Bundle bundle = new Bundle();
                bundle.putString("select",select);
                message.setData(bundle);
                mHandler.sendMessage(message);
            }
        });
    }

    class MyListAdapter extends BaseAdapter {
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
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.btgoodatitem,parent,false);
            }
            TextView name = (TextView)convertView.findViewById(R.id.tv_name);
            ImageView iv = (ImageView)convertView.findViewById(R.id.iv_yes);
            name.setText(mList.get(position));
            if (indicate.getPostion()==position) {
                name.setTextColor(mContext.getResources().getColor(R.color.green_essential_colour));
                iv.setVisibility(View.VISIBLE);
            }else{
                name.setTextColor(mContext.getResources().getColor(R.color.grays));
                iv.setVisibility(View.INVISIBLE);
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
