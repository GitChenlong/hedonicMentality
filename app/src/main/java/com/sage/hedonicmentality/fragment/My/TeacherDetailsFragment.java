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
import android.widget.Toast;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.view.OrderPopWindow;
import com.sage.hedonicmentality.view.SelectAdressPopupWindow;
import com.sage.hedonicmentality.view.SelectTimePopWindow;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/18.
 */
public class TeacherDetailsFragment extends Fragment {

    private FragmentManager fm;

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

                break;
            case R.id.ll_attention:
                //关注

                break;
            case R.id.ll_order:
                //预约咨询
                chooseAddress();
                break;

        }
    }
    private void chooseAddress() {
        String dates[] ={"周1","周2","周3","周4","周5","周6","周7","周8","周9","周10","周11","周12"};
        String times[] ={"9:00-10:00-1","9:00-10:00-2","9:00-10:00-3","9:00-10:00-4",
                "9:00-10:00-5","9:00-10:00-6","9:00-10:00-7","9:00-10:00-8","9:00-10:00-9","9:00-10:00-10","9:00-10:00-11"};
        OrderPopWindow orderpopwindow=new OrderPopWindow(getActivity(),dates,times,mHandler);
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
                    break;
            }
        }
    };
    public void setTab(int type){
        FragmentTransaction bt = fm.beginTransaction();
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
        bt.commit();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
