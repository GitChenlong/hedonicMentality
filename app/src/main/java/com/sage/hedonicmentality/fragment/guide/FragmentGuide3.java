package com.sage.hedonicmentality.fragment.guide;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.fragment.BaseFragment;
import com.sage.hedonicmentality.ui.ActivityGuide;
import com.sage.hedonicmentality.ui.ActivityLogin;
import com.sage.hedonicmentality.ui.simple.BreathSetting;
import com.sage.hedonicmentality.utils.Contact;
import com.sage.hedonicmentality.utils.SPHelper;
import com.sage.hedonicmentality.utils.SharedPreferencesHelper;

import butterknife.OnClick;

/**
 * Created by Sage on 2015/8/6.
 */
public class FragmentGuide3 extends GuideBase {


    @Override
    public int getLayout() {
        return R.layout.fragment_guide3;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @OnClick({R.id.tv_next,R.id.iv_guide3})
    public void skipWhich(){
        SharedPreferencesHelper.getInstance().Builder(getActivity());
        if(!TextUtils.isEmpty(SharedPreferencesHelper.getInstance().getString(Contact.SH_ID, ""))){
            //跳入主页面
            startActivity(new Intent(getActivity(),BreathSetting.class));
            getActivity().finish();
        }else{
            //跳入登陆页
            startActivity(new Intent(getActivity(),ActivityLogin.class));
            getActivity().finish();
        }
    }
}
