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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.ui.simple.ActivityBuyDevice;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Sage on 2015/7/27.
 */
public class FragmentDiaPrompt extends DialogFragment implements View.OnClickListener{

    @Bind(R.id.tv_content)
    TextView tv_content;
    @Bind(R.id.layout_bluetooth)
    LinearLayout layout_bluetooth;
    @Bind(R.id.layout_camera)
    LinearLayout layout_camera;
    /**tag=0 摄像头的提示，tag=1,蓝牙设备找不到的提示*/
    public static FragmentDiaPrompt create(int tag){
        FragmentDiaPrompt fragment=new FragmentDiaPrompt();
        Bundle bundle=new Bundle();
        bundle.putInt("tag",tag);
        fragment.setArguments(bundle);
        return fragment;
    }

    public FragmentDiaPrompt(){
        setStyle(STYLE_NO_TITLE, R.style.DiaScaleAnimationTheme);//取消标题
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_dia_prompt, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    private int tag;
    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        if(getArguments()!=null){
            tag=getArguments().getInt("tag");

            switch(tag){
                case 0:
                    layout_bluetooth.setVisibility(View.GONE);
                    layout_camera.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    tv_content.setText(Html.fromHtml(getString(R.string.dia_bluetooth_not_find)));
                    layout_bluetooth.setVisibility(View.VISIBLE);
                    layout_camera.setVisibility(View.GONE);
                    break;
            }
        }

        try {
            getView().findViewById(R.id.tv_close).setOnClickListener(this);
            getView().findViewById(R.id.iv_pic).setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Window window=getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable());
        WindowManager.LayoutParams wl = window.getAttributes();
//        wl.x = 0;//这里的坐标是相对默认重心 center而言的。
//        wl.y = getResources().getDisplayMetrics().heightPixels;
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = getResources().getDisplayMetrics().widthPixels*9/10;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
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
            case R.id.tv_close:
                dismiss();
                break;
            case R.id.iv_pic:
                FragmentDiaBuyDevice.create().show(getChildFragmentManager(), "buyDevice");
                break;

            default:
                break;
        }

    }


}
