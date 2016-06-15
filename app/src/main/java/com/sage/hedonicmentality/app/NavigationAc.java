package com.sage.hedonicmentality.app;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.fragment.My.ConsultFragment;
import com.sage.hedonicmentality.fragment.My.FindFragment;
import com.sage.hedonicmentality.fragment.My.UserFragment;
import com.sage.hedonicmentality.utils.SPHelper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/17.
 * 首页
 */
public class NavigationAc extends FragmentActivity{
    @Bind(R.id.fl)
    FrameLayout fl;
    @Bind(R.id.iv_find)
    ImageView iv_find;
    @Bind(R.id.iv_consult)
    ImageView iv_consult;
    @Bind(R.id.iv_user)
    ImageView iv_user;
    @Bind(R.id.tv_find)
    TextView tv_find;
    @Bind(R.id.tv_consult)
    TextView tv_consult;
    @Bind(R.id.tv_user)
    TextView tv_user;
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        ButterKnife.bind(this);
        initView();
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            Rect frame = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            int statusBarHeight = frame.top;
            SPHelper.putDefaultInt(NavigationAc.this, SPHelper.WINDOW_TOP_HEIGHT, statusBarHeight);
        }
    }
    private void initView() {
        fragmentManager = getSupportFragmentManager();
        setTab(1);
        changeState(1);
        maxfl = (FrameLayout)findViewById(R.id.maxfl);
    }
    @OnClick({R.id.ll_find,R.id.ll_zixun,R.id.ll_user})
    public void NaAcAOnclick(View view){
        switch (view.getId()){
            case R.id.ll_find:
                findFragment.setScrollviewPosition();
                setTab(1);
                changeState(1);
                break;
            case R.id.ll_zixun:
                setTab(2);
                changeState(2);
                break;
            case R.id.ll_user:
                changeState(3);
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
    public void changeState(int type){
        switch (type){
            case 1:
                tv_find.setTextColor(getResources().getColor(R.color.green_essential_colour));
                tv_consult.setTextColor(getResources().getColor(R.color.grays));
                tv_user.setTextColor(getResources().getColor(R.color.grays));
                iv_find.setImageResource(R.mipmap.table_find_higlighted);
                iv_consult.setImageResource(R.mipmap.talk_normal);
                iv_user.setImageResource(R.mipmap.table_mine_normal);
                break;
            case 2:
                tv_find.setTextColor(getResources().getColor(R.color.grays));
                tv_consult.setTextColor(getResources().getColor(R.color.green_essential_colour));
                tv_user.setTextColor(getResources().getColor(R.color.grays));
                iv_find.setImageResource(R.mipmap.table_find_normal);
                iv_consult.setImageResource(R.mipmap.talk_selected);
                iv_user.setImageResource(R.mipmap.table_mine_normal);
                break;
            case 3:
                tv_find.setTextColor(getResources().getColor(R.color.grays));
                tv_consult.setTextColor(getResources().getColor(R.color.grays));
                tv_user.setTextColor(getResources().getColor(R.color.green_essential_colour));
                iv_find.setImageResource(R.mipmap.table_find_normal);
                iv_consult.setImageResource(R.mipmap.talk_normal);
                iv_user.setImageResource(R.mipmap.table_mine_highlighted);
                break;
        }

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
