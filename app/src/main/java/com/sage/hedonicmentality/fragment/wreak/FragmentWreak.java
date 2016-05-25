package com.sage.hedonicmentality.fragment.wreak;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.fragment.BaseFragment;

import butterknife.OnClick;

/**
 * Created by Sage on 2015/8/4.
 */
public class FragmentWreak extends BaseFragment {
    @Override
    public int getLayout() {
        return R.layout.fragment_wreak;
    }

    @Override
    public void initActionbar() {
        tv_title.setText("");
        btn_rigth.setImageResource(R.drawable.circle_trans_solid);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @OnClick(R.id.btn_right)
     void wreak_click(){

    }
}
