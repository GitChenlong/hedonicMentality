package com.sage.hedonicmentality.fragment.Me;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.app.Http;
import com.sage.hedonicmentality.fragment.BaseFragment;
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
 * Created by Sage on 2015/7/29.
 */
public class FragmentAdvice extends BaseFragment {
    @Bind(R.id.et_advice)
    EditText et_advice;
    @Bind(R.id.btn_right)
    ImageView btn_right;
    @Bind(R.id.layout_actionbar)
    RelativeLayout layout_actionbar;
    @Override
    public int getLayout() {
        return R.layout.fragment_advice;
    }

    @Override
    public void initActionbar() {
        tv_title.setText(R.string.title_advice);
        btn_right.setImageResource(R.mipmap.queding);
        tv_title.setTextColor(getResources().getColor(R.color.edit_focus));
        btn_left.setImageResource(R.mipmap.back_01);
        layout_actionbar.setBackgroundResource(R.color.bg_title);
    }

    @OnClick(R.id.btn_right)
    void adviceClick(){
        String content=et_advice.getText().toString().trim();
        if(TextUtils.isEmpty(content)){
            UtilSnackbar.showSimple(btn_right, getString(R.string.advice_null));
            return;
        }
        SharedPreferencesHelper.getInstance().Builder(getActivity());
        String phone =  SharedPreferencesHelper.getInstance().getString(Contact.SH_PHONE);
        String pwd =  SharedPreferencesHelper.getInstance().getString(Contact.SH_PWD);
//        if(TextUtils.isEmpty(phone)||TextUtils.isEmpty(pwd)){
//            Toast.makeText(Http.getCotext(),"请先登陆!",Toast.LENGTH_SHORT).show();
//            return;
//        }
        Util.showProgressFor(getActivity(),getResources().getString(R.string.longdings));
        Http.Fankui(phone,pwd, content, new RequestCallBack<String>() {
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
                        UtilSnackbar.showSimple(btn_right, object.getString("tip"));
                        return;
                    }
                   
                    Toast.makeText(Http.getCotext(),getResources().getString(R.string.advic),Toast.LENGTH_SHORT).show();
                    back();
                } catch (UnsupportedEncodingException e) {
                    
                    e.printStackTrace();
                } catch (JSONException e) {
                    
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Util.cancelProgressFor(getActivity());
                UtilSnackbar.showSimple(btn_right, s);
            }

        });
//        Toast.makeText(getActivity(),content,Toast.LENGTH_SHORT).show();
    }

}
