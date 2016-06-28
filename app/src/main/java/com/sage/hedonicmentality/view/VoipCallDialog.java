package com.sage.hedonicmentality.view;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.sage.hedonicmentality.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/6/23.
 */
public class VoipCallDialog extends DialogFragment {
    private static Handler mHandler;
    @Bind(R.id.ll_cancel)
    LinearLayout ll_cancel;
    public static VoipCallDialog create(Handler handler){
        mHandler = handler;
        VoipCallDialog fragment=new VoipCallDialog();
        return fragment;
    }
    public VoipCallDialog(){
        setStyle(STYLE_NO_TITLE, R.style.DiaScaleAnimationTheme);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.voipcalldialog, container, false);
        ButterKnife.bind(this, view);
        this.getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK
//                        && event.getAction() == KeyEvent.ACTION_DOWN) {
//                }
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    Message message = new Message();
                    message.what = 8;
                    mHandler.sendMessage(message);
                }
                return true;
            }
        });

        return view;
    }
    @OnClick({R.id.ll_cancel})
    public void voipcallOnclik(View view) {
        if (view.getId()==R.id.ll_cancel) {
            Message message = new Message();
            message.what = 8;
            mHandler.sendMessage(message);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window=getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable());
        WindowManager.LayoutParams wl = window.getAttributes();
//        wl.x = 0;//这里的坐标是相对默认重心 center而言的。
//        wl.y = getResources().getDisplayMetrics().heightPixels;
        // 以下这两句是为了保证按钮可以水平满屏
//        wl.width = getResources().getDisplayMetrics().widthPixels*9/10;
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.dimAmount=0.5f;
        wl.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        // 设置显示位置
        getDialog().onWindowAttributesChanged(wl);
        //window.setAttributes(wl);
        // 设置点击外围取消
        getDialog().setCanceledOnTouchOutside(false);
    }
}
