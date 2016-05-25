package com.sage.hedonicmentality.fragment.account;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.man.MANService;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.app.Http;
import com.sage.hedonicmentality.entity.RegisterParams;
import com.sage.hedonicmentality.fragment.BaseFragment;
import com.sage.hedonicmentality.ui.ActivityAgreement;
import com.sage.hedonicmentality.ui.ActivityLogin;
import com.sage.hedonicmentality.utils.Contact;
import com.sage.hedonicmentality.utils.SharedPreferencesHelper;
import com.sage.hedonicmentality.utils.Util;
import com.sage.hedonicmentality.utils.UtilSnackbar;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Sage on 2015/7/16.
 */
public class FragmentRegister extends BaseFragment {
    @Bind(R.id.et_phone)
    EditText et_phone;
    @Bind(R.id.et_code)
    EditText et_code;
    @Bind(R.id.et_psw)
    EditText et_psw;
    @Bind(R.id.tv_code)
    TextView tv_code;
    @Bind(R.id.btn_commit)
    Button btn_commit;
    @Bind(R.id.ck_boxs)
    CheckBox ck_boxs;
    private TextWatcher listener=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(TextUtils.isEmpty(et_phone.getText().toString())||TextUtils.isEmpty(et_code.getText().toString())||
                    TextUtils.isEmpty(et_psw.getText().toString().trim())){
                btn_commit.setEnabled(false);
            }else{
                btn_commit.setEnabled(true);
            }
        }
    };

    @Override
    public int getLayout() {
        return R.layout.fragment_register;
    }
    @Override
    public void initActionbar() {
        tv_title.setText(getString(R.string.title_register));
        et_phone.addTextChangedListener(listener);
        et_code.addTextChangedListener(listener);
        et_psw.addTextChangedListener(listener);
//        btn_commit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(ck_boxs.isChecked()){
//                    check();
//                }else {
//                    UtilSnackbar.showSimple(tv_code,getActivity().getString(R.string.title_agreement_err));
//                }
//            }
//        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @OnClick({R.id.btn_commit,R.id.tv_code,R.id.tv_agreement})
    void registerClick(View view){
        Util.hiddenInputMethod(getActivity(), et_phone);
        switch(view.getId()){
            case R.id.btn_commit:
                if(ck_boxs.isChecked()){
                    check();
                }else {
                    UtilSnackbar.showSimple(tv_code,getActivity().getString(R.string.title_agreement_err));
                }
                break;
            case R.id.tv_code:
                checkPhone();
//                next();
//                SharedPreferencesHelper.getInstance().Builder(getActivity());
//                SharedPreferencesHelper.getInstance().putString(Contact.SH_PHONE, "15238369929");
//                SharedPreferencesHelper.getInstance().putString(Contact.SH_PWD, "123456");
                break;
            case R.id.tv_agreement:
//                ((ActivityLogin)getActivity()).GoAgreement();
                Intent intent = new Intent(getActivity(), ActivityAgreement.class);
                startActivity(intent);
                break;

        }
    }

    private CountDownTimer timer;
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(timer!=null){
            timer.cancel();
            timer=null;
        }
    }

    private void checkPhone(){
        String number=et_phone.getText().toString().trim();
        if(TextUtils.isEmpty(number)){
            UtilSnackbar.showSimple(tv_code,getActivity().getString(R.string.phoneNull));
            return ;
        }
        if(!Util.isMobileNO(number)){
            UtilSnackbar.showSimple(tv_code,getActivity().getString(R.string.phoneWrong));
            return ;
        }
        //调用接口获取验证码
        timer=Util.timer(tv_code,60);
        Util.showProgressFor(getActivity(),getResources().getString(R.string.longdings));
        Http.Sendsms(number,"1",new RequestCallBack<String>() {
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
//                      Toast.makeText(getActivity(), object.getString("msg"), Toast.LENGTH_SHORT).show();
                        UtilSnackbar.showSimple(tv_code,object.getString("tip"));
                        timer.cancel();
                        timer.onFinish();
                        return;
                    }
                    Util.showToast(getActivity(),"验证码已发送，请稍等");
                }  catch (Exception e) {
                    e.printStackTrace();
                    timer.cancel();
                    timer.onFinish();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Util.cancelProgressFor(getActivity());
                UtilSnackbar.showSimple(tv_code, s);
                timer.cancel();
                timer.onFinish();
            }
        });
    }

    private void check(){
        final RegisterParams params=new RegisterParams();
        params.number=et_phone.getText().toString().trim();
        params.code=et_code.getText().toString().trim();
        params.psw=et_psw.getText().toString().trim();
        String check=params.check(getActivity());
        if(check!=null){
            UtilSnackbar.showSimple(tv_code,check);
            return;
        }
        //提交数据
//        final String number=et_phone.getText().toString().trim();
//        String code = et_code.getText().toString().trim();
//        final String psw = et_psw.getText().toString().trim();

        Http.Register(params.number, params.psw, params.code,new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String v = responseInfo.result;
                byte[] bytes = v.getBytes();
                try {
                    v = new String(bytes, "UTF-8");
                    JSONObject object = new JSONObject(v);
                    int info = object.getInt("info");
                    if (info != 1) {
//                        Toast.makeText(getActivity(), object.getString("tip"), Toast.LENGTH_SHORT).show();
                          UtilSnackbar.showSimple(tv_code,object.getString("tip"));
//                                mBtnNext.setEnabled(true);
                        return;
                    }
                    LogUtils.i("register+++" + v);
                    SharedPreferencesHelper.getInstance().Builder(getActivity());
                    SharedPreferencesHelper.getInstance().putString(Contact.SH_PHONE, params.number);
                    SharedPreferencesHelper.getInstance().putString(Contact.SH_PWD,params.psw);
                    JSONObject obj = object.getJSONObject("data");
                    SharedPreferencesHelper.getInstance().putString(Contact.SH_ID,obj.getString("user_id"));

                    MANService manService = AlibabaSDK.getService(MANService.class);
                    // 注册用户埋点
                    manService.getMANAnalytics().userRegister(obj.getString("user_id"));
                    //判断成功了
                    next();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                UtilSnackbar.showSimple(tv_code, s);
            }

        });
    }

    private void next(){
//        UtilSnackbar.showSimple(tv_code, "跳转");
        ((ActivityLogin)getActivity()).GoSex();
    }

}
