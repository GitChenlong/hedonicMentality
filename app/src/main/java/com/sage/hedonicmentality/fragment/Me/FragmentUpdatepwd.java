package com.sage.hedonicmentality.fragment.Me;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
 * Created by Administrator on 2015/8/13.
 */
public class FragmentUpdatepwd extends BaseFragment {

    @Bind(R.id.btn_right)
    ImageView btn_right;
    @Bind(R.id.ed_oldpwd)
    EditText ed_oldpwd;
    @Bind(R.id.ed_newpwd)
    EditText ed_newpwd;
    @Bind(R.id.ed_qrpwd)
    EditText ed_qrpwd;
    @Bind(R.id.tv_right)
    TextView tv_right;
    @Bind(R.id.layout_actionbar)
    RelativeLayout layout_actionbar;
    @Override
    public int getLayout() {
        return R.layout.fragment_udatep;
    }
    @Override
    public void initActionbar() {
        tv_title.setText(R.string.title_me_udpter);
//        btn_right.setImageResource(R.mipmap.queding);
        tv_right.setText(R.string.baocun);
        tv_right.setTextColor(getResources().getColor(R.color.white_color));
        tv_title.setTextColor(getResources().getColor(R.color.whi));
        btn_left.setImageResource(R.mipmap.back_01);
        layout_actionbar.setBackgroundResource(R.color.bg_title);
    }
    @OnClick(R.id.tv_right)
    void adviceClick(){
        String oldpwd=ed_oldpwd.getText().toString().trim();
        String newpwd=ed_newpwd.getText().toString().trim();
        String qrpwd =ed_qrpwd.getText().toString().trim();
        if(TextUtils.isEmpty(oldpwd)||TextUtils.isEmpty(newpwd)||TextUtils.isEmpty(qrpwd)){
            UtilSnackbar.showSimple(ed_qrpwd,"密码不能为空");
        }else{
            if(oldpwd.length()<6||newpwd.length()<6||qrpwd.length()<6){
                UtilSnackbar.showSimple(ed_qrpwd,"密码长度最少为6位");
            }else{
                if(!newpwd.equals(qrpwd)){
                    UtilSnackbar.showSimple(ed_qrpwd,"两次密码不一致");
                }else {
                    upPwd();
                }
            }
        }
//        Toast.makeText(getActivity(), content, Toast.LENGTH_SHORT).show();
    }

    private void upPwd(){
        SharedPreferencesHelper.getInstance().Builder(getActivity());
        String phone =  SharedPreferencesHelper.getInstance().getString(Contact.SH_PHONE);
        String oldpwd=ed_oldpwd.getText().toString().trim();
        final String newpwd=ed_newpwd.getText().toString().trim();
        Util.showProgressFor(getActivity(), getResources().getString(R.string.longdings));
        Http.Resetpwd(phone, oldpwd,newpwd, new RequestCallBack<String>() {
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
                        UtilSnackbar.showSimple(ed_newpwd, object.getString("tip"));
                        return;
                    }
                    //接口调用成功后
                    Toast.makeText(Http.getCotext(),"密码修改成功！",Toast.LENGTH_LONG).show();
                    SharedPreferencesHelper.getInstance().putString(Contact.SH_PWD, newpwd);
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
                UtilSnackbar.showSimple(ed_newpwd, s);
            }
        });
    }
}
