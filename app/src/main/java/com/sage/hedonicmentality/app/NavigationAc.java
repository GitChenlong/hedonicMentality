package com.sage.hedonicmentality.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.fragment.BaseFragment;
import com.sage.hedonicmentality.fragment.My.ConsultFragment;
import com.sage.hedonicmentality.fragment.My.FindFragment;
import com.sage.hedonicmentality.fragment.My.UserFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/17.
 */
public class NavigationAc extends FragmentActivity{
    @Bind(R.id.fl)
    FrameLayout fl;
    public static FrameLayout maxfl;
    private FragmentManager fragmentManager;
    private FindFragment findFragment;
    private ConsultFragment consultfragment;
    private UserFragment userfragment;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigationac);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        fragmentManager = getSupportFragmentManager();
        setTab(1);
        maxfl = (FrameLayout)findViewById(R.id.maxfl);
    }
    @OnClick({R.id.ll_find,R.id.ll_zixun,R.id.ll_user})
    public void NaAcAOnclick(View view){
        switch (view.getId()){
            case R.id.ll_find:
                setTab(1);
                break;
            case R.id.ll_zixun:
                setTab(2);
                break;
            case R.id.ll_user:
                setTab(3);
                break;
        }

    }
    /**添加Fr*/
    public static void addFr(Fragment fragment,String name,FragmentManager fragmentManager,int animType){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (animType==1) {
            transaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_in, R.anim.back_left_in, R.anim.back_right_out);
        }else if(animType==2) {
            transaction.setCustomAnimations(R.anim.anim_ins, R.anim.anim_ins, R.anim.y_anim_in, R.anim.y_anim_out);
        }
        transaction.add(R.id.maxfl, fragment, name);
        transaction.addToBackStack(null);
        transaction.show(fragment);
        transaction.commit();
    }

    public void setTab(int type){
        transaction = fragmentManager.beginTransaction();
        switch (type){
            case 1:
                if (findFragment!=null) {
                    transaction.show(findFragment);
                }else{
                    findFragment = new FindFragment();
                    transaction.add(R.id.fl, findFragment);
                }
                if (consultfragment!=null) {
                    transaction.hide(consultfragment);
                }
                if (userfragment!=null) {
                    transaction.hide(userfragment);
                }
                break;
            case 2:
                if (consultfragment!=null) {
                    transaction.show(consultfragment);
                }else{
                    consultfragment = new ConsultFragment();
                    transaction.add(R.id.fl, consultfragment);
                }
                if (findFragment!=null) {
                    transaction.hide(findFragment);
                }
                if (userfragment!=null) {
                    transaction.hide(userfragment);
                }
                break;
            case 3:
                if (userfragment!=null) {
                    transaction.show(userfragment);
                }else{
                    userfragment = new UserFragment();
                    transaction.add(R.id.fl, userfragment);
                }
                if (consultfragment!=null) {
                    transaction.hide(consultfragment);
                }
                if (findFragment!=null) {
                    transaction.hide(findFragment);
                }
                break;
        }
        transaction.commit();

    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
