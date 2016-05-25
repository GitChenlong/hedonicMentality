package com.sage.hedonicmentality.fragment.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.man.MANService;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.app.Http;
import com.sage.hedonicmentality.ui.ActivityLogin;
import com.sage.hedonicmentality.ui.simple.BreathSetting;
import com.sage.hedonicmentality.utils.Contact;
import com.sage.hedonicmentality.utils.SPHelper;
import com.sage.hedonicmentality.utils.SharedPreferencesHelper;
import com.sage.hedonicmentality.utils.Util;
import com.sage.hedonicmentality.utils.UtilSnackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sage on 2015/7/16.
 */
public class FragmentLogin extends Fragment {

    @Bind(R.id.et_phone)
    EditText et_phone;
    @Bind(R.id.et_pwd)
    EditText et_pwd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_login,container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        et_phone.setText("15238369929");
//        et_pwd.setText("123456");
    }

    @OnClick({R.id.btn_login,R.id.btn_register,R.id.btn_skip,R.id.tv_forget})
    public void loginClick(View view){
        switch(view.getId()){
            case R.id.btn_login:
                Util.hiddenInputMethod(getActivity(),et_phone);
                 login();
                break;
            case R.id.btn_register:
                ((ActivityLogin)getActivity()).GoRegister();
                break;
            case R.id.btn_skip:
                guest();
                break;
            case R.id.tv_forget:
                ((ActivityLogin)getActivity()).GoForgetPsw();
                break;
        }

    }


    private void login(){

        String number=et_phone.getText().toString().trim();
        if(TextUtils.isEmpty(number)){
            toast(getString(R.string.phoneNull));
            return ;
        }
        final String psw=et_pwd.getText().toString().trim();
        if(TextUtils.isEmpty(psw)||psw.length()<6){
            toast(getString(R.string.pswLengthWrong));
            return ;
        }
        //登陆
        Util.showProgressFor(getActivity(),getResources().getString(R.string.longdings));
        Http.Login(number, psw, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Util.cancelProgressFor(getActivity());
                String v = responseInfo.result;
                byte[] bytes = v.getBytes();
                try {
                    v = new String(bytes, "UTF-8");
                    JSONObject object = new JSONObject(v);

                    int info = object.getInt("info");

                    if (info != 1) {
                        UtilSnackbar.showSimple(et_pwd, object.getString("tip"));
                        return;
                    }
                    JSONObject obj = object.getJSONObject("data");
                    SharedPreferencesHelper.getInstance().Builder(getActivity());
                    SharedPreferencesHelper.getInstance().putString(Contact.SH_PHONE, obj.getString("mobile_phone"));
                    SharedPreferencesHelper.getInstance().putString(Contact.SH_USERNAME,obj.getString("nick_name"));
                    SharedPreferencesHelper.getInstance().putString(Contact.SH_PWD,psw);
                    SharedPreferencesHelper.getInstance().putString(Contact.SH_Avatar,obj.getString("avatar"));
                    SharedPreferencesHelper.getInstance().putString(Contact.SH_ID, obj.getString("user_id"));
                    SharedPreferencesHelper.getInstance().putString(Contact.SH_Birthday, obj.getString("birthday"));
                    SharedPreferencesHelper.getInstance().putString(Contact.SH_Sex, obj.getString("sex"));
                    SharedPreferencesHelper.getInstance().putString(Contact.SH_Weight, obj.getString("weight"));
                    SharedPreferencesHelper.getInstance().putString(Contact.SH_Height, obj.getString("height"));
                    SharedPreferencesHelper.getInstance().putString(Contact.SH_Address, obj.getString("area"));
                    SharedPreferencesHelper.getInstance().putString(Contact.SH_Education, obj.getString("education"));
                    SharedPreferencesHelper.getInstance().putInt(Contact.SH_SUMHP, Integer.parseInt(obj.getString("health_points")));

                    MANService manService = AlibabaSDK.getService(MANService.class);
                    // 用户登录埋点
                    manService.getMANAnalytics().updateUserAccount(obj.getString("nick_name"), obj.getString("user_id"));
                    nextPage();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(HttpException e, String s) {
//                Toast.makeText(getActivity(),"登陆失败",Toast.LENGTH_SHORT).show();
                Util.cancelProgressFor(getActivity());
            }

        });

    }

    private void nextPage(){
        SPHelper.putDefaultBoolean(getActivity(), SPHelper.KEY_HAVE_LOGIN, true);
        guest();
    }

    public void guest(){
        getActivity().startActivity(new Intent(getActivity(), BreathSetting.class));
        getActivity().overridePendingTransition(R.anim.main_alpha, R.anim.login_scale_alpha);
        getActivity().finish();
    }
    private void toast(String msg){
        UtilSnackbar.showSimple(et_phone,msg);
    }



}
