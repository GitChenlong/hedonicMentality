package com.sage.hedonicmentality.fragment;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.util.LogUtils;
import com.sage.hedonicmentality.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Sage on 2015/7/16.
 */
public abstract class BaseFragment extends Fragment {
    @Bind(R.id.btn_left)/**左侧后退按钮*/
    public ImageView btn_left;
    @Bind( R.id.btn_right)/**右侧功能按钮*/
    public ImageView btn_rigth;
    @Bind( R.id.tv_right)/**右侧标题*/
    public TextView tv_right;
    @Bind(R.id.tv_title)/**标题*/
    public TextView tv_title;
    @Bind(R.id.layout_actionbar)/**actionbar整体*/
    public RelativeLayout layout_acionbar;
    public boolean isBack=true;
    public boolean isRight=true;
    public abstract int getLayout();
    public   void initActionbar(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(getLayout(),container, false);
        LogUtils.e("++++onCreateView;+");
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initActionbar();
        LogUtils.e("++++onActivityCreated;+");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setHasOptionsMenu(true);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null)
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);

    }

    @OnClick({R.id.btn_left,R.id.tv_right})
    public void baseClick(View v){
        switch (v.getId())
        {
            case R.id.btn_left:
                if(isBack){
                    back();
                }else{
                    navigation();
                }
                break;
            case R.id.tv_right:
                navigation();
                break;
        }

    }

    public void back(){
        getActivity().onBackPressed();
    }
    /**左侧按钮不是后退的话重写这个方法，实现点击事件*/
    public void navigation(){

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
