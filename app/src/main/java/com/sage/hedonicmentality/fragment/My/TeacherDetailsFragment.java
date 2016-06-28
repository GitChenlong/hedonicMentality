package com.sage.hedonicmentality.fragment.My;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.app.NavigationAc;
import com.sage.hedonicmentality.utils.Util;
import com.sage.hedonicmentality.view.OrderPopWindow;
import com.sage.hedonicmentality.view.SelectAdressPopupWindow;
import com.sage.hedonicmentality.view.SelectTimePopWindow;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/18.
 * 咨询师详情
 */
public class TeacherDetailsFragment extends Fragment {
    // 主页缩放动画
    private Animation mScalInAnimation1;
    // 主页缩放完毕小幅回弹动画
    private Animation mScalInAnimation2;
    // 主页回弹正常状态动画
    private Animation mScalOutAnimation;
    private FragmentManager fm;
    @Bind(R.id.ll_techerdetails)
    RelativeLayout ll_techerdetails;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.teacherdetailsfragment,null);
        ButterKnife.bind(this,view);
        init(view);
        return view;

    }
    private void init(View view) {
        fm = getFragmentManager();
        inits();
    }
    /**
     * 初始化
     */
    private void inits() {
        // 动画初始化
        mScalInAnimation1 = AnimationUtils.loadAnimation(getActivity(),
                R.anim.root_in);
        mScalInAnimation2 = AnimationUtils.loadAnimation(getActivity(),
                R.anim.root_in2);
        mScalOutAnimation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.root_out);
        // 注册事件回调
        mScalInAnimation1.setAnimationListener(new ScalInAnimation1());
    }

    /**
     * popupwindow消失的回调
     */
    private class OnPopupDismissListener implements
            android.widget.PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            // 标题和主页开始播放动画
            ll_techerdetails.startAnimation(mScalOutAnimation);
        }
    }

    /**
     * 缩小动画的回调
     */
    public class ScalInAnimation1 implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            ll_techerdetails.startAnimation(mScalInAnimation2);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    @OnClick({R.id.iv_back,R.id.iv_intent,R.id.ll_indent,R.id.ll_love,R.id.ll_flower
            ,R.id.ll_details,R.id.ll_evaluate,R.id.ll_share,R.id.ll_attention,R.id.ll_order})
    public void teacherOnclick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                fm.popBackStack();
                break;
            case R.id.iv_intent:
                //跳转视屏播放
                break;
            case R.id.ll_indent:
                //订单

                break;
            case R.id.ll_love:
                //喜欢

                break;
            case R.id.ll_flower:
                //鲜花

                break;
            case R.id.ll_details:
                //详情

                break;
            case R.id.ll_evaluate:
                //评价

                break;
            case R.id.ll_share:
                //分享
                Util.showShare(getActivity());
                break;
            case R.id.ll_attention:
                //关注

                break;
            case R.id.ll_order:
                //预约咨询
                chooseAddress();
                ll_techerdetails.startAnimation(mScalInAnimation1);
                break;

        }
    }
    private void chooseAddress() {
        String dates[] ={"周1","周2","周3","周4","周5","周6","周7","周8","周9","周10","周11","周12"};
        String times[] ={"9:00-10:00-1","9:00-10:00-2","9:00-10:00-3","9:00-10:00-4",
                "9:00-10:00-5","9:00-10:00-6","9:00-10:00-7","9:00-10:00-8","9:00-10:00-9","9:00-10:00-10","9:00-10:00-11"};
        OrderPopWindow orderpopwindow=new OrderPopWindow(getActivity(),dates,times,mHandler);
        orderpopwindow.setOnDismissListener(new OnPopupDismissListener());
        orderpopwindow.showAtLocation(getView().findViewById(R.id.ll_techerdetails), Gravity.BOTTOM, 0, 0);
    }
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case 8:
                    String select = msg.getData().getString("date");
                    Toast.makeText(getActivity(),select,Toast.LENGTH_SHORT).show();
                    NavigationAc.addFr(new WriteInformationFragment(), "WriteInformationFragment", getFragmentManager(), 1);
                    break;
            }
        }
    };
    public void setTab(int type){
//        FragmentTransaction bt = fm.beginTransaction();
        switch (type){
            case 1:
//                if (findFragment!=null) {
//                    transaction.show(findFragment);
//                }else{
//                    findFragment = new FindFragment();
//                    transaction.add(R.id.fl_details, findFragment);
//                }
//                if (consultfragment!=null) {
//                    transaction.hide(consultfragment);
//                }
                break;
            case 2:
//                if (findFragment!=null) {
//                    transaction.show(findFragment);
//                }else{
//                    findFragment = new FindFragment();
//                    transaction.add(R.id.fl_details, findFragment);
//                }
//                if (consultfragment!=null) {
//                    transaction.hide(consultfragment);
//                }

                break;
        }
//        bt.commit();
    }

    public class detailsFr extends  Fragment{
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            View view = View.inflate(getActivity(),R.layout.detailsfr,null);
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
