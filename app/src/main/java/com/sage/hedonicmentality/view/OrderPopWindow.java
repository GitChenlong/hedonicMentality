package com.sage.hedonicmentality.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.bean.AddressBean;

/**
 * Created by Administrator on 2016/5/27.
 */
public class OrderPopWindow extends PopupWindow implements View.OnClickListener  {

    private final Activity mcontext;
    private final DateView wl_dates;
    private final TimesView wl_times;
    private final Handler mHandler;
    private View mMenuView;
    private ViewFlipper viewfipper;
    private String dateList[];
    private String timeList[];
    private DateView dateView;
    private TimesView timesView;
    public  OrderPopWindow(Activity context,String[] date,String time[],Handler Handler){
        super(context);
        setAnimationStyle(R.style.orderAnimation);
        mcontext =context;
        mHandler= Handler;
        this.dateList=date;
        this.timeList=time;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(mcontext.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.orderpopwindow, null);
        viewfipper = new ViewFlipper(context);
        viewfipper.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        wl_dates = (DateView)mMenuView.findViewById(R.id.wl_dates);
        wl_times = (TimesView)mMenuView.findViewById(R.id.wl_times);
        dateView.setDates(dateList);
        timesView.setDates(timeList);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_ascertain:
                Message message = Message.obtain();
                message.what = 8;
                Bundle bundle = new Bundle();
                String date = dateList[dateView.getValue()];
                String time = timeList[timesView.getValue()];
                bundle.putString("date",date+"/"+time);
                message.setData(bundle);
                mHandler.sendMessage(message);
                dismiss();
                break;
        }
    }
    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        viewfipper.startFlipping();
    }
}
