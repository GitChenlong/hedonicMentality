package com.sage.hedonicmentality.fragment.breath;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.ui.ActivityBreath;
import com.sage.hedonicmentality.ui.simple.ActivityBuyDevice;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sage on 2015/7/27.
 */
public class FragmentDiaHelp extends DialogFragment {

    @Bind(R.id.tv_title)
    TextView tv_title;

    public static FragmentDiaHelp create(){
        FragmentDiaHelp fragment=new FragmentDiaHelp();
        return fragment;
    }

    public FragmentDiaHelp(){
        setStyle(STYLE_NO_TITLE, R.style.DiaScaleAnimationTheme);//取消标题
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_breath_info, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
    private void initData(){
        tv_title.setText(R.string.title_breath_info);
    }
    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        initData();
        Window window=getDialog().getWindow();
        //window.setBackgroundDrawable(new ColorDrawable());
        WindowManager.LayoutParams wl = window.getAttributes();
//        wl.x = 0;//这里的坐标是相对默认重心 center而言的。
//        wl.y = getResources().getDisplayMetrics().heightPixels;
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.MATCH_PARENT;
//        wl.dimAmount=0.5f;
//        wl.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;

        // 设置显示位置
        getDialog().onWindowAttributesChanged(wl);
        //window.setAttributes(wl);
        // 设置点击外围取消
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().setCancelable(true);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.btn_left})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                dismiss();
                break;
            default:
                break;
        }

    }


}
