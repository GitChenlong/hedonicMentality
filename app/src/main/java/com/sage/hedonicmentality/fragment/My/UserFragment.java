package com.sage.hedonicmentality.fragment.My;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.fragment.BaseFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/17.
 */
public class UserFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(),R.layout.userfragment,null);
        ButterKnife.bind(this,view);

        return view;
    }

    @OnClick({R.id.ll_photo,R.id.ll_myorder,R.id.ll_health,R.id.ll_discount_coupon,R.id.ll_attention,R.id.ll_message
            ,R.id.ll_setting,R.id.ll_feedback})
    public void userFrOnclick(View view) {
        switch (view.getId()) {
            case R.id.ll_photo:
                //头像

                break;
            case R.id.ll_myorder:
                //我的预约

                break;
            case R.id.ll_health:
                //健康贴士

                break;
            case R.id.ll_discount_coupon:
                //优惠卡券

                break;
            case R.id.ll_attention:
                //我的关注

                break;
            case R.id.ll_message:
                //消息中心

                break;
            case R.id.ll_setting:
                //设置

                break;
            case R.id.ll_feedback:
                //意见反馈

                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}
