package com.sage.hedonicmentality.fragment.breath;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.ui.ActivityLogin;
import com.sage.hedonicmentality.ui.simple.BreathSetting;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/3/16.
 */
public class Fragmentdowloadshow extends DialogFragment implements View.OnClickListener{
    public  static BreathSetting contexts;
    private TextView tv_dow;
    public Fragmentdowloadshow(){
        setStyle(STYLE_NO_TITLE, R.style.DiaScaleAnimationTheme);//取消标题
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_dowloadshow, container, false);
        tv_dow =  (TextView)view.findViewById(R.id.tv_dow);
        tv_dow.setOnClickListener(this);
        ButterKnife.bind(this, view);
        return view;
    }

    private int tag;
    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);

        Window window=getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable());
        WindowManager.LayoutParams wl = window.getAttributes();
//        wl.x = 0;//这里的坐标是相对默认重心 center而言的。
//        wl.y = getResources().getDisplayMetrics().heightPixels;
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = getResources().getDisplayMetrics().widthPixels;
        wl.height = getResources().getDisplayMetrics().heightPixels;
        wl.dimAmount=0.5f;
        wl.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;

        // 设置显示位置
        getDialog().onWindowAttributesChanged(wl);
        //window.setAttributes(wl);
        // 设置点击外围解散
        getDialog().setCanceledOnTouchOutside(true);
    }

    public void showToast(String str) {
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_dow:
                dismiss();
                break;

            default:
                break;
        }

    }
}
