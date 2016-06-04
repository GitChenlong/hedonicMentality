package com.sage.hedonicmentality.fragment.My;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.app.Http;
import com.sage.hedonicmentality.app.NavigationAc;
import com.sage.hedonicmentality.bean.MUserInfo;
import com.sage.hedonicmentality.bean.SubAccount;
import com.sage.hedonicmentality.utils.UtilSnackbar;
import com.sage.hedonicmentality.view.LoginPopWindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/17.
 */
public class UserFragment extends Fragment {

    private LoginPopWindow popwindow;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(),R.layout.userfragment,null);
        ButterKnife.bind(this,view);

        return view;
    }

    @OnClick({R.id.ll_photo,R.id.ll_myorder,R.id.ll_health,R.id.ll_discount_coupon,R.id.ll_attention,R.id.ll_message
            ,R.id.ll_setting,R.id.ll_feedback})
    public void userFrOnclick(View view) {
        switch (view.getId()) {
            case R.id.ll_photo:
                //头像
                showLoginPop();
                break;
            case R.id.ll_myorder:
                //我的预约

                break;
            case R.id.ll_health:
                //健康贴士

                break;
            case R.id.ll_discount_coupon:
                //优惠卡券

                break;
            case R.id.ll_attention:
                //我的关注

                break;
            case R.id.ll_message:
                //消息中心

                break;
            case R.id.ll_setting:
                //设置

                break;
            case R.id.ll_feedback:
                //意见反馈

                break;
        }
    }
    //登录POP
    public void showLoginPop(){
         popwindow = new LoginPopWindow(getActivity(),mHandler);
        popwindow.showAtLocation(getView().findViewById(R.id.ll_user), Gravity.CENTER,0,0);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case 1:
                    UtilSnackbar.showSimple(getView().findViewById(R.id.ll_user), "账户不能为空");
                    break;
                case 2:
                    UtilSnackbar.showSimple(getView().findViewById(R.id.ll_user), "密码不能为空");
                    break;
                case 3:
                    //Login
                    String user = msg.getData().getString("user");
                    String ps = msg.getData().getString("ps");
                    Http.login(user, ps, new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            String v = responseInfo.result;
                            byte[] bytes = v.getBytes();
                            try {
                                v = new String(bytes, "UTF-8");
                                JSONObject object = new JSONObject(v);
                                int info = object.getInt("info");
                                if (info != 1) {
                                    UtilSnackbar.showSimple(getView().findViewById(R.id.ll_user), object.getString("tip"));
                                    return;
                                }
                                JSONObject data = object.getJSONObject("data");
                                JSONObject sub = data.getJSONObject("SubAccount");
                                String subAccountSid = sub.getString("subAccountSid");
                                String subToken = sub.getString("subToken");
                                String dateCreated = sub.getString("dateCreated");
                                String voipAccount = sub.getString("voipAccount");
                                String voipPwd = sub.getString("voipPwd");
                                JSONObject userInfo = data.getJSONObject("userInfo");
                                String userid = userInfo.getString("userid");
                                String username = userInfo.getString("username");
                                String realname = userInfo.getString("realname");
                                String mobile = userInfo.getString("mobile");
                                String type = userInfo.getString("type");
                                SubAccount subAccount = new SubAccount(subAccountSid,subToken,dateCreated,voipAccount,voipPwd);
                                MUserInfo muserinfo = new MUserInfo(userid,username,realname,mobile,type);
                                Toast.makeText(getActivity(),subAccount.toString()+"  "+muserinfo.toString(),Toast.LENGTH_SHORT).show();
                                nextPage();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(HttpException error, String msg) {
                            UtilSnackbar.showSimple(getView().findViewById(R.id.ll_user), "网络异常");
                        }
                    });
                    popwindow.dismiss();
                    break;
            }
        }
    };

    public void nextPage(){
        NavigationAc.addFr(new ceshi(),"ceshi",getActivity().getSupportFragmentManager(),1);
    }

}
