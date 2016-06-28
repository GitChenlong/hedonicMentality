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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ViewFlipper;

import com.sage.hedonicmentality.R;

/**
 * Created by Administrator on 2015/11/3.
 */
public class ExceptionalPayPop extends PopupWindow implements View.OnClickListener {
    private  ImageView iv_zhifubao_checked;
    private  ImageView iv_checked;
    private  LinearLayout wechatPay;
    private  LinearLayout alipay;
    private  Button btn_price;
    private Activity mContext;
    private View mMenuView;
    private ViewFlipper viewfipper;
    private Handler mHandler;
    // 使用自定义的Log机制
    private int payType=1;
    private int price;
    public ExceptionalPayPop(Activity context, Handler handler, int prices) {
        super(context);
        setAnimationStyle(R.style.PopupAnimation);
        mContext = context;
        mHandler = handler;
        price=prices;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.paypopwindow, null);
        viewfipper = new ViewFlipper(context);
        viewfipper.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        wechatPay = (LinearLayout)mMenuView.findViewById(R.id.ll_weixin);
        alipay = (LinearLayout)mMenuView.findViewById(R.id.ll_zhifubao);
        btn_price = (Button)mMenuView.findViewById(R.id.btn_price);
        iv_checked = (ImageView)mMenuView.findViewById(R.id.iv_checked);
        iv_zhifubao_checked = (ImageView)mMenuView.findViewById(R.id.iv_zhifubao_checked);
        wechatPay.setOnClickListener(this);
        alipay.setOnClickListener(this);
        btn_price.setOnClickListener(this);
        btn_price.setText("确认付款:(￥" + price + ")");
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
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        viewfipper.startFlipping();
    }

    /**
     * Adapter for numeric wheels. Highlights the current value.
     */
    public void onClick(View v) {
        if(v.getId()== R.id.btn_price){
            Message message = Message.obtain();
            message.what = 8;
            Bundle bundle = new Bundle();
            bundle.putInt("payType",payType);
            message.setData(bundle);
            mHandler.sendMessage(message);
            dismiss();
        }
        if(v.getId()== R.id.ll_weixin){
            payType=1;
            iv_checked.setVisibility(View.VISIBLE);
            iv_zhifubao_checked.setVisibility(View.GONE);

        }
        if(v.getId()== R.id.ll_zhifubao){
            payType=2;
            iv_checked.setVisibility(View.GONE);
            iv_zhifubao_checked.setVisibility(View.VISIBLE);
        }

    }
}
