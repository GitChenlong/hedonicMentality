package com.sage.hedonicmentality.fragment.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.fragment.BaseFragment;

import butterknife.OnClick;

/**
 * Created by Sage on 2015/8/4.
 */
public class FragmentChatMain extends BaseFragment {
    @Override
    public int getLayout() {
        return R.layout.fragment_chat_main;
    }

    @Override
    public void initActionbar() {
        tv_title.setText("快乐心理001");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @OnClick(R.id.btn_right)
    void right_click(){

    }


}
