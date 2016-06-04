package com.sage.hedonicmentality.fragment.account;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.app.Http;
import com.sage.hedonicmentality.entity.RegisterParams;
import com.sage.hedonicmentality.fragment.BaseFragment;
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
public class FragmentForget extends BaseFragment {
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
        return R.layout.fragment_forget;
    }

    @Override
    public void initActionbar() {
        tv_title.setText(getString(R.string.title_forget_psw));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        et_phone.addTextChangedListener(listener);
        et_code.addTextChangedListener(listener);
        et_psw.addTextChangedListener(listener);
    }

    @OnClick({R.id.btn_commit,R.id.tv_code})
    void click(View view){
        Util.hiddenInputMethod(getActivity(),et_phone);
        switch(view.getId()){
            case R.id.btn_commit:
                check();
                break;
            case R.id.tv_code:
               checkPhone();
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
        timer=Util.timer(tv_code,60);
        //调用接口获取验证码
        Http.Sendsms(number,"2",new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String v = responseInfo.result;
                byte[] bytes = v.getBytes();
                try {
                    v = new String(bytes, "UTF-8");
                    JSONObject object = new JSONObject(v);
                    int info = object.getInt("info");
                    if (info != 1) {
                        UtilSnackbar.showSimple(tv_code,object.getString("tip"));
                        timer.cancel();
                        timer.onFinish();
                        return;
                    }
                    Util.showToast(getActivity(),"验证码已发送，请稍等");
                } catch (Exception e) {
                    e.printStackTrace();
                    timer.cancel();
                    timer.onFinish();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                timer.cancel();
                timer.onFinish();
                UtilSnackbar.showSimple(tv_code,s);
            }
        });
    }
    private void check(){
        RegisterParams params=new RegisterParams();
        params.number=et_phone.getText().toString().trim();
        params.code=et_code.getText().toString().trim();
        params.psw=et_psw.getText().toString().trim();
        String check=params.check_forget(getActivity());
        if(check!=null){
            UtilSnackbar.showSimple(tv_code,check);
            return;
        }
        Http.Forget(params.number, params.psw, params.code, new RequestCallBack<String>() {
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
                        UtilSnackbar.showSimple(tv_code, object.getString("tip"));
//                                mBtnNext.setEnabled(true);
                        return;
                    }
                    //判断成功了
                    if(getActivity()!=null){
                        Util.showToast(getActivity(), "新密码已生效");
                        getActivity().onBackPressed();
                    }
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                UtilSnackbar.showSimple(tv_code, "失败");
            }

        });
    }


}
