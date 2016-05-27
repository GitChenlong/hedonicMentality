package com.sage.hedonicmentality.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;

import com.sage.hedonicmentality.bean.AddressBean;
import com.sage.hedonicmentality.bean.City;
import com.sage.hedonicmentality.bean.Country;
import com.sage.hedonicmentality.bean.Province;
import com.sage.libwheelview.R;
import com.sage.libwheelview.widget.wheel.OnWheelChangedListener;
import com.sage.libwheelview.widget.wheel.WheelView;
import com.sage.libwheelview.widget.wheel.adapter.ArrayWheelAdapter;
import com.sage.libwheelview.widget.wheel.adapter.MArrayWheelAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/11/3.
 */
public class SelectTimePopWindow extends PopupWindow implements View.OnClickListener {


    private  RelativeLayout rl_ascertain;
    private Activity mContext;
    private View mMenuView;
    private ViewFlipper viewfipper;
    private WheelView wl_dates, wl_times;
    private Handler mHandler;
    private String dates[] ={"今天","明天","11"};
    private String times[] ={"9:00-9:50","10:00-10:50","11:00-11:50"};
    private AddressBean myaddress;
    // 使用自定义的Log机制

    public SelectTimePopWindow(Activity context,
                               Handler handler) {
        super(context);
        setAnimationStyle(R.style.PopupAnimation);
        mContext = context;
        mHandler = handler;
        OnWheelChangedListener listener = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                Log.e("onChanged",wheel.getCurrentItem()+"/"+oldValue+"/"+newValue);
                setadapter(wl_dates, wl_times);
            }
        };
//
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popupwindow_times, null);
        viewfipper = new ViewFlipper(context);
        viewfipper.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        wl_dates = (WheelView) mMenuView
                .findViewById(R.id.wl_dates);
        wl_times = (WheelView) mMenuView
                .findViewById(R.id.wl_times);
        rl_ascertain = (RelativeLayout) mMenuView
                .findViewById(R.id.rl_ascertain);

        rl_ascertain.setOnClickListener(this);
        wl_dates.addChangingListener(listener);
        wl_times.addChangingListener(listener);

        ArrayWheelAdapter<String> dateAdapter = new ArrayWheelAdapter<String>(
                context, dates);
        dateAdapter.setItemResource(R.layout.float_item);
        dateAdapter.setItemTextResource(R.id.tv_float_item);
        dateAdapter.setEmptyItemResource(R.layout.float_item);
        wl_dates.setViewAdapter(dateAdapter);
        wl_dates.setCurrentItem(0);

        ArrayWheelAdapter<String> timeAdapeter = new ArrayWheelAdapter<String>(
                context, times);
        timeAdapeter.setItemResource(R.layout.float_item);
        timeAdapeter.setItemTextResource(R.id.tv_float_item);
        timeAdapeter.setEmptyItemResource(R.layout.float_item);
        wl_times.setViewAdapter(timeAdapeter);
        wl_times.setCurrentItem(0);

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

    public void setadapter(WheelView date, WheelView time){
        ArrayWheelAdapter<String> dateAdapter = new ArrayWheelAdapter<String>(
                mContext, dates);
                dateAdapter.setItemResource(R.layout.float_item);
                dateAdapter.setItemTextResource(R.id.tv_float_item);
                dateAdapter.setEmptyItemResource(R.layout.float_item);
        date.setViewAdapter(dateAdapter);


        ArrayWheelAdapter<String> timeAdapter = new ArrayWheelAdapter<String>(
                mContext, times);
        timeAdapter.setItemResource(R.layout.float_item);
        timeAdapter.setItemTextResource(R.id.tv_float_item);
        timeAdapter.setEmptyItemResource(R.layout.float_item);
        time.setViewAdapter(timeAdapter);
    }
    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        viewfipper.startFlipping();
    }

    /**
     * Adapter for numeric wheels. Highlights the current value.
     */
    public void onClick(View v) {
        if(v.getId()== R.id.rl_ascertain){
            Message message = Message.obtain();
            message.what = 8;
            Bundle bundle = new Bundle();
            String datename = dates[wl_dates.getCurrentItem()];
            String timename = times[wl_times.getCurrentItem()];
            bundle.putString("select",datename+"/"+timename);
            message.setData(bundle);
            mHandler.sendMessage(message);
        }
        dismiss();
    }
}
