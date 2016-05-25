package com.sage.hedonicmentality.fragment.My;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.app.Http;
import com.sage.hedonicmentality.fragment.BaseFragment;
import com.sage.hedonicmentality.widget.CatalogPopupWindow;

/**
 * Created by Administrator on 2016/5/17.
 */
public class FindFragment extends BaseFragment {
    @Override
    public int getLayout() {
        return R.layout.findfragment;
    }

    @Override
    public void initActionbar() {
        tv_title.setText(R.string.find);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText(R.string.Officina);
        //设置textDrawble
//        Drawable nav_up=getResources().getDrawable(R.drawable.arrow_left_select);
//        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
//        tv_right.setCompoundDrawables(null, null, nav_up, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        btn_left.setVisibility(View.INVISIBLE);
    }

    @Override
    public void navigation() {
        // 跳HRV
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void getData() {
        Http.getFind("", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });
    }

}
