package com.sage.hedonicmentality.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.sage.hedonicmentality.R;

/**
 * Created by Administrator on 2016/5/31.
 */
public class LoginPopWindow extends PopupWindow implements View.OnClickListener{

    private final Activity mcontext;
    private final EditText et_user;
    private final EditText et_ps;
    private final Handler mHandler;
    private final TextView tv_login;
    private View mMenuView;
    private ViewFlipper viewfipper;
    public  LoginPopWindow(Activity context,Handler Handler){
        super(context);
        mcontext =context;
        mHandler= Handler;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(mcontext.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.loginpopwindow, null);
        viewfipper = new ViewFlipper(context);
        viewfipper.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        et_user = (EditText)mMenuView.findViewById(R.id.et_user);
        et_ps = (EditText)mMenuView.findViewById(R.id.et_ps);
        et_user.setText("18684642028");
        et_ps.setText("123456");
        tv_login = (TextView)mMenuView.findViewById(R.id.tv_login);
        tv_login.setOnClickListener(this);
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
            case R.id.tv_login:
                Message message = Message.obtain();
                if (TextUtils.isEmpty(et_user.getText().toString())) {
                    message.what = 1;
                    mHandler.sendMessage(message);
                    return;
                }
                if (TextUtils.isEmpty(et_ps.getText().toString())) {
                    message.what = 2;
                    mHandler.sendMessage(message);
                    return;
                }
                message.what = 3;
                Bundle bundle = new Bundle();
                String user = et_user.getText().toString();
                String ps = et_ps.getText().toString();
                bundle.putString("user",user);
                bundle.putString("ps", ps);
                message.setData(bundle);
                mHandler.sendMessage(message);
                break;
        }
    }
    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        viewfipper.startFlipping();
    }

}
