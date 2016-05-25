package com.sage.hedonicmentality.fragment.worry;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.fragment.BaseFragment;

import butterknife.OnClick;

/**
 * Created by Sage on 2015/8/4.
 */
public class FragmentWorry extends BaseFragment {
    @Override
    public int getLayout() {
        return R.layout.fragment_worry;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @OnClick(R.id.btn_right)
    void right_click(){

    }

}
