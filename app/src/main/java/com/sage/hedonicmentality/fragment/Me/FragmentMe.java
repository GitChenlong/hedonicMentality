package com.sage.hedonicmentality.fragment.Me;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.util.LogUtils;
import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.fragment.BaseFragment;
import com.sage.hedonicmentality.ui.ActivityLogin;
import com.sage.hedonicmentality.ui.ActivityMe;
import com.sage.hedonicmentality.utils.Contact;
import com.sage.hedonicmentality.utils.SharedPreferencesHelper;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Sage on 2015/7/17.
 */
public class FragmentMe extends BaseFragment {
    public static final String TAG=FragmentMe.class.getSimpleName();
    @Bind(R.id.iv_head)
    ImageView iv_head;
    @Bind(R.id.tv_name)
    TextView tv_name;
    @Bind(R.id.tv_hedonicId)
    TextView tv_hedonicId;
    @Bind(R.id.tv_hp)
    TextView tv_hp;
    @Bind(R.id.layout_actionbar)
    RelativeLayout layout_actionbar;
//    @Bind(R.id.tv_wool)
//    TextView tv_wool;
//    @Bind(R.id.tv_record)
//    TextView tv_record;
//    @Bind(R.id.tv_secret)
//    TextView tv_secret;
//    @Bind(R.id.tv_msg)
//    TextView tv_msg;
    @Override
    public int getLayout() {
        return R.layout.fragment_me;
    }

    @Override
    public void initActionbar() {
        layout_actionbar.setBackgroundResource(R.color.bg_title);
        tv_title.setText(R.string.title_me);
        tv_title.setTextColor(getResources().getColor(R.color.edit_focus));
        btn_left.setImageResource(R.mipmap.back_01);
        SharedPreferencesHelper.getInstance().Builder(getActivity());
        String phone = SharedPreferencesHelper.getInstance().getString(Contact.SH_PHONE);
        String ID = SharedPreferencesHelper.getInstance().getString(Contact.SH_ID);
        int sumhp = SharedPreferencesHelper.getInstance().getInt(Contact.SH_SUMHP);
        LogUtils.e("++++++++++++++initActionbar+++SH_SUMHP:"+sumhp);
        tv_hp.setText(sumhp+"");
        if(!TextUtils.isEmpty(ID)){
            String avatar = SharedPreferencesHelper.getInstance().getString(Contact.SH_Avatar);
            String name = SharedPreferencesHelper.getInstance().getString(Contact.SH_USERNAME);
            if(avatar.length()>0){
                try {
                    Picasso.with(getActivity()).load(avatar).into(iv_head);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            tv_name.setText(name);
        }
        if(!TextUtils.isEmpty(ID)){
            StringBuffer str = new StringBuffer(phone);
            String qin = str.substring(0, 3);
            String hou = str.substring(7, 11);
            String ph = qin + "****" + hou;
            tv_hedonicId.setText(getString(R.string.phone)+ph);
        }
    }

    @Override
    public void onResume() {
        SharedPreferencesHelper.getInstance().Builder(getActivity());
        String ID = SharedPreferencesHelper.getInstance().getString(Contact.SH_ID);
        String phone = SharedPreferencesHelper.getInstance().getString(Contact.SH_PHONE);
        int sumhp = SharedPreferencesHelper.getInstance().getInt(Contact.SH_SUMHP);
        LogUtils.e("++++++++++++onResume+++++SH_SUMHP:"+sumhp);
        tv_hp.setText(sumhp + "");
        if(!TextUtils.isEmpty(ID)){
            String avatar = SharedPreferencesHelper.getInstance().getString(Contact.SH_Avatar);
            String name = SharedPreferencesHelper.getInstance().getString(Contact.SH_USERNAME);
            if(avatar.length()>0){
                try {
                    Picasso.with(getActivity()).load(avatar).into(iv_head);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            tv_name.setText(name);
        }
        if(!TextUtils.isEmpty(ID)){
            StringBuffer str = new StringBuffer(phone);
            String qin = str.substring(0, 3);
            String hou = str.substring(7, 11);
            String ph = qin + "****" + hou;
            tv_hedonicId.setText(getString(R.string.phone)+ph);
        }
        super.onResume();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @OnClick({R.id.tv_advice, R.id.tv_system, R.id.iv_record, R.id.iv_meSetting,R.id.iv_myhealth,R.id.iv_hp})
    public void meClick(View view){
        switch (view.getId()){
            case R.id.iv_meSetting:
                SharedPreferencesHelper.getInstance().Builder(getActivity());
                String ID =  SharedPreferencesHelper.getInstance().getString(Contact.SH_ID);
                if(!TextUtils.isEmpty(ID)){
                    which(1);
                }else{
                    getActivity().startActivity(new Intent(getActivity(), ActivityLogin.class));
                }
                break;
            case R.id.iv_record:
                which(2);
                break;
//            case R.id.iv_secret:
//                which(3);
//                break;
//            case R.id.iv_msg:
//                which(4);
//                break;
            case R.id.tv_advice:
                which(5);
                break;
            case R.id.tv_system:
                which(6);
                break;
            case R.id.iv_myhealth:
                SharedPreferencesHelper.getInstance().Builder(getActivity());
                String ID1 =  SharedPreferencesHelper.getInstance().getString(Contact.SH_ID);
                if(!TextUtils.isEmpty(ID1)){
                    which(9);
                }else{
                    getActivity().startActivity(new Intent(getActivity(), ActivityLogin.class));
                }
                break;
            case R.id.iv_hp:
                which(10);
                break;

        }
//        UtilSnackbar.showSimple(tv_name, "test");
    }

    private void which(int i){
        if(getActivity()==null){
            return;
        }
        ((ActivityMe)getActivity()).changePage(i);
    }
    public void setNameP(){
        SharedPreferencesHelper.getInstance().Builder(getActivity());
        String avatar =  SharedPreferencesHelper.getInstance().getString(Contact.SH_Avatar);
        String name =  SharedPreferencesHelper.getInstance().getString(Contact.SH_USERNAME);
        tv_name.setText(name);
        try {
            Picasso.with(getActivity()).load(avatar).into(iv_head);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
