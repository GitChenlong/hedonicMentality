package com.sage.hedonicmentality.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.sage.hedonicmentality.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/6/22.
 */
public class IncomingDialog extends DialogFragment{
    private static Handler mHandler;
    public static IncomingDialog create(Handler handler){
        mHandler = handler;
        IncomingDialog fragment=new IncomingDialog();
        return fragment;
    }
    public IncomingDialog(){
        setStyle(STYLE_NO_TITLE, R.style.DiaScaleAnimationTheme);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.incomingdialog, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
    @OnClick({R.id.btn_accept,R.id.btn_refuse})
    public void incomingOnclick(View view) {
        if (view.getId()== R.id.btn_accept) {
            Message message=new Message();
            message.what=1;
            mHandler.sendMessage(message);

        }
        if (view.getId()== R.id.btn_refuse) {
            Message message=new Message();
            message.what=2;
            mHandler.sendMessage(message);
        }
        dismiss();
    }
}
