package com.sage.hedonicmentality.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.sage.hedonicmentality.bean.AddressBean;
import com.sage.hedonicmentality.bean.City;
import com.sage.hedonicmentality.bean.Country;
import com.sage.hedonicmentality.bean.Province;
import com.sage.libwheelview.R;
import com.sage.libwheelview.widget.wheel.OnWheelChangedListener;
import com.sage.libwheelview.widget.wheel.WheelView;
import com.sage.libwheelview.widget.wheel.adapter.ArrayWheelAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/11/3.
 */
public class SelectAdressPopupWindow extends PopupWindow implements View.OnClickListener {

    private Activity mContext;
    private View mMenuView;
    private ViewFlipper viewfipper;
    private Button btn_submit_select_address, btn_cancel_select_address;
    private int weight_number;
    private String weight;
    private int float_number;
    private WheelView guo_address, sheng_address,shi_address;
    private Handler mHandler;
    //    private final String guojia[] = new String[] { "中国", "美国"};//国家
//    private final String shengfen[][] = {{"上海","湖南"},{ "纽约", "旧金山"}};//省份
//    private final String chengshi[][][]= { {{"上海"},{"",""}}
//                                    };//国家
    private AddressBean myaddress;
    // 使用自定义的Log机制

    public SelectAdressPopupWindow(Activity context, AddressBean address,
                                   Handler handler) {
        super(context);
        setAnimationStyle(R.style.PopupAnimation);
        myaddress = address;
        mContext = context;
        mHandler = handler;
        // "体重值：70.5"
//        this.weight = weight;
//        int i = weight.length();
//        if (i <= 2) {
//            weight_number = Integer.parseInt(weight);
//        } else {
//            weight_number = Integer.parseInt(weight.substring(0, i - 2));
//        }
        OnWheelChangedListener listener = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                setadapter(guo_address, sheng_address, shi_address);
            }
        };
//        float_number = Integer.parseInt(weight.substring(i - 1, i));
//
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popupwindow_address, null);
        viewfipper = new ViewFlipper(context);
        viewfipper.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        guo_address = (WheelView) mMenuView
                .findViewById(R.id.guo_address);
        sheng_address = (WheelView) mMenuView
                .findViewById(R.id.sheng_address);
        shi_address = (WheelView) mMenuView
                .findViewById(R.id.shi_address);
        btn_submit_select_address = (Button) mMenuView
                .findViewById(R.id.btn_submit_select_address);
        btn_cancel_select_address = (Button) mMenuView
                .findViewById(R.id.btn_cancel_select_address);
        btn_submit_select_address.setOnClickListener(this);
        btn_cancel_select_address.setOnClickListener(this);
        guo_address.addChangingListener(listener);
        sheng_address.addChangingListener(listener);
//        shi_address.addChangingListener(listener);
//        weightAdapter = new DateNumericAdapter(context, 30, 200, weight_number);
//        select_weight.setViewAdapter(weightAdapter);
//        select_weight.setCurrentItem(weight_number - 30);

//        select_float = (WheelView) mMenuView
//                .findViewById(R.id.float_select_weight);
        ArrayWheelAdapter<String> guoAdapter = new ArrayWheelAdapter<String>(
                context, getGUO(myaddress.getData()));
        guoAdapter.setItemResource(R.layout.float_item);
        guoAdapter.setItemTextResource(R.id.tv_float_item);
        guoAdapter.setEmptyItemResource(R.layout.float_item);
        guo_address.setViewAdapter(guoAdapter);
        guo_address.setCurrentItem(0);

        ArrayWheelAdapter<String> shengAdapter = new ArrayWheelAdapter<String>(
                context, getSheng(myaddress.getData().get(0).getChildren()));
        shengAdapter.setItemResource(R.layout.float_item);
        shengAdapter.setItemTextResource(R.id.tv_float_item);
        shengAdapter.setEmptyItemResource(R.layout.float_item);
        sheng_address.setViewAdapter(shengAdapter);
        sheng_address.setCurrentItem(0);

        ArrayWheelAdapter<String> shiAdapter = new ArrayWheelAdapter<String>(
                context,getShi(myaddress.getData().get(0).getChildren().get(0).getChildren()));
        shiAdapter.setItemResource(R.layout.float_item);
        shiAdapter.setItemTextResource(R.id.tv_float_item);
        shiAdapter.setEmptyItemResource(R.layout.float_item);
        shi_address.setViewAdapter(shiAdapter);
        shi_address.setCurrentItem(0);

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

    public String [] getGUO(ArrayList<Country> data){
        String [] cuntry = new String [data.size()];
        for (int i = 0;i<data.size();i++){
            cuntry[i]  = data.get(i).getRegion_name();
        }
        return cuntry;
    }
    public String [] getSheng(ArrayList<Province> data){
        String [] cuntry = new String [data.size()];
        for (int i = 0;i<data.size();i++){
            cuntry[i]  = data.get(i).getRegion_name();
        }
        return cuntry;
    }
    public String [] getShi(ArrayList<City> data){
        String [] cuntry = new String [data.size()];
        for (int i = 0;i<data.size();i++){
            cuntry[i]  = data.get(i).getRegion_name();
        }
        return cuntry;
    }
    public void setadapter(WheelView guo, WheelView sheng, WheelView shi){
        ArrayWheelAdapter<String> shengAdapter = new ArrayWheelAdapter<String>(
                mContext, getSheng(myaddress.getData().get(guo.getCurrentItem()).getChildren()));
        shengAdapter.setItemResource(R.layout.float_item);
        shengAdapter.setItemTextResource(R.id.tv_float_item);
        shengAdapter.setEmptyItemResource(R.layout.float_item);
        sheng.setViewAdapter(shengAdapter);

        ArrayWheelAdapter<String> shiAdapter = new ArrayWheelAdapter<String>(
                mContext,getShi(myaddress.getData().get(guo.getCurrentItem()).getChildren().get(sheng.getCurrentItem()).getChildren()));
        shiAdapter.setItemResource(R.layout.float_item);
        shiAdapter.setItemTextResource(R.id.tv_float_item);
        shiAdapter.setEmptyItemResource(R.layout.float_item);
        shi.setViewAdapter(shiAdapter);
        shi_address.setCurrentItem(0);
    }
    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        viewfipper.startFlipping();
    }

    /**
     * Adapter for numeric wheels. Highlights the current value.
     */
//    private class DateNumericAdapter extends NumericWheelAdapter {
//        // Index of current item
//        int currentItem;
//        // Index of item to be highlighted
//        int currentValue;
//
//        /**
//         * Constructor
//         */
//        public DateNumericAdapter(Context context, int minValue, int maxValue,
//                                  int current) {
//            super(context, minValue, maxValue);
//            this.currentValue = current;
//            setTextSize(18);
//        }
//
//        protected void configureTextView(TextView view) {
//            super.configureTextView(view);
//            view.setTypeface(Typeface.SANS_SERIF);
//        }
//
//        public CharSequence getItemText(int index) {
//            currentItem = index;
//            return super.getItemText(index);
//        }
//
//    }
    public void onClick(View v) {
        if(v.getId()== R.id.btn_submit_select_address){
            Message message = Message.obtain();
            message.what = 8;
            Bundle bundle = new Bundle();
            String guoname = myaddress.getData().get(guo_address.getCurrentItem()).getRegion_name();
            String shengname = myaddress.getData().get(guo_address.getCurrentItem()).getChildren().get(sheng_address.getCurrentItem()).getRegion_name();
            String shiname = myaddress.getData().get(guo_address.getCurrentItem()).getChildren().get(sheng_address.getCurrentItem()).getChildren().get(shi_address.getCurrentItem()).getRegion_name();
            bundle.putString("address",guoname+" "+shengname+" "+shiname);
            message.setData(bundle);
            mHandler.sendMessage(message);
        }else if(v.getId()==R.id.btn_cancel_select_address){

        }
        dismiss();
//		switch (v.getId()) {
//		case R.id.btn_submit_select_weight:
//			Message message = Message.obtain();
//			message.what = 7;
//			Bundle bundle = new Bundle();
//			weight_number = select_weight.getCurrentItem() + 30;
//			float_number = select_float.getCurrentItem();
//			if (float_number == 0) {
//				bundle.putString("weight", weight_number + ".0");
//			} else {
//				bundle.putString("weight", weight_number + ".5");
//			}
//			message.setData(bundle);
//			mHandler.sendMessage(message);
//			this.dismiss();
//			break;
//		case R.id.btn_cancel_select_weight:
//			this.dismiss();
//			break;
//		default:
//			break;
//		}
        // this.dismiss();
    }
}
