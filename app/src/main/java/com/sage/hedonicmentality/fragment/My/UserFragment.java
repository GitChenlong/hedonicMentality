package com.sage.hedonicmentality.fragment.My;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.sage.hedonicmentality.fragment.account.FragmentDiaChoose;
import com.sage.hedonicmentality.utils.Contact;
import com.sage.hedonicmentality.utils.SharedPreferencesHelper;
import com.sage.hedonicmentality.utils.Util;
import com.sage.hedonicmentality.utils.UtilSnackbar;
import com.sage.hedonicmentality.view.LoginPopWindow;
import com.sage.libimagechoose.api.ChooserType;
import com.sage.libimagechoose.api.ChosenImage;
import com.sage.libimagechoose.api.ImageChooserListener;
import com.sage.libimagechoose.api.ImageChooserManager;
import com.sage.libimagechoose.api.utils.UtilPicCut;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/17.
 */
public class UserFragment extends Fragment implements ImageChooserListener {

    private LoginPopWindow popwindow;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(),R.layout.userfragment,null);
        ButterKnife.bind(this,view);

        return view;
    }

    @OnClick({R.id.ll_photo,R.id.ll_myorder,R.id.ll_health,R.id.ll_discount_coupon,R.id.ll_attention,R.id.ll_message
            ,R.id.ll_setting,R.id.ll_feedback,R.id.ll_not_logged_in})
    public void userFrOnclick(View view) {
        switch (view.getId()) {
            case R.id.ll_not_logged_in:
                //未登录头像
                NavigationAc.addFr(new VideoCallFragment(), "VideoCallFragment", getActivity().getSupportFragmentManager(), 1);
//                showAlert();
                break;
            case R.id.ll_photo:
                //已登录头像
                showLoginPop();
                break;
            case R.id.ll_myorder:
                //我的预约
               Intent intent = new Intent(getActivity(),MyOrderFragment.class);
                startActivity(intent);
                int version = Integer.valueOf(android.os.Build.VERSION.SDK);
                if(version  >= 5) {
                    getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.anim_out_ac);  //此为自定义的动画效果，下面两个为系统的动画效果
                }

                break;
            case R.id.ll_health:
                //健康贴士
            NavigationAc.addFr(new HealthFragment(),"HealthFragment",getActivity().getSupportFragmentManager(),1);
                break;
            case R.id.ll_discount_coupon:
                //优惠卡券
            NavigationAc.addFr(new DiscountCouponFragment(),"DiscountCouponFragment",getActivity().getSupportFragmentManager(),1);
                break;
            case R.id.ll_attention:
                //我的关注
            NavigationAc.addFr(new MyAttentionFragment(),"MyAttentionFragment",getActivity().getSupportFragmentManager(),1);
                break;
            case R.id.ll_message:
                //消息中心
            NavigationAc.addFr(new MessageFragment(),"MessageFragment",getActivity().getSupportFragmentManager(),1);
                break;
            case R.id.ll_setting:
                //设置
            NavigationAc.addFr(new SettingFragment(),"SettingFragment",getActivity().getSupportFragmentManager(),1);
                break;
            case R.id.ll_feedback:
                //意见反馈
            NavigationAc.addFr(new FeedBackFragment(),"FeedBackFragment",getActivity().getSupportFragmentManager(),1);
                break;
        }
    }

    //登录POP
    public void showLoginPop(){
        popwindow = new LoginPopWindow(getActivity(),mHandler);
        popwindow.showAtLocation(getView().findViewById(R.id.ll_user), Gravity.CENTER, 0, 0);
    }
    private FragmentDiaChoose fragmentChoosePic;
    private ImageChooserManager manager;
    private File outCropFile;
    /**
     * 裁剪后的头像地址
     */
    public void showAlert() {
        if (fragmentChoosePic != null) {
            fragmentChoosePic.show(getChildFragmentManager(), "choose pics");
            return;
        }
        fragmentChoosePic = FragmentDiaChoose.create(0);
        fragmentChoosePic.setmChooseListener(mChooseListener);

        fragmentChoosePic.show(getChildFragmentManager(), "choose pics");
    }
    private FragmentDiaChoose.ChooseClickListener mChooseListener = new FragmentDiaChoose.ChooseClickListener() {
        @Override
        public void click(int index) {
            switch (index) {
                case 0:/**camera*/
                    getPicture(ChooserType.REQUEST_CAPTURE_PICTURE);
                    break;
                case 1:/**photo*/
                    getPicture(ChooserType.REQUEST_PICK_PICTURE);
                    break;
            }
        }
    };
    public void getPicture(int type) {
        manager = new ImageChooserManager(this, type);
        manager.setImageChooserListener(this);
        try {
            manager.choose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onImageChosen(ChosenImage image) {
        if (image == null) {
            return;
        }
        outCropFile = UtilPicCut.doCropAction(this,
                new File(image.getFilePathOriginal()), true);
    }

    @Override
    public void onError(String reason) {

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ChooserType.REQUEST_CAPTURE_PICTURE:
                case ChooserType.REQUEST_PICK_PICTURE:
                    if (manager == null) {

                    } else {
                        manager.submit(requestCode, data);
                    }
                    break;
                case UtilPicCut.REQUEST_DO_CROP:
                    if (outCropFile != null) {
                       try {
                           Log.e("outCropFile",outCropFile.toString());
                           update();
                            //上传头像
                            /* SharedPreferencesHelper.getInstance().Builder(getActivity());
                            String phone = SharedPreferencesHelper.getInstance().getString(Contact.SH_PHONE);
                            String password = SharedPreferencesHelper.getInstance().getString(Contact.SH_PWD);
                            Util.showProgressFor(getActivity(), getResources().getString(R.string.longdings));
                            Http.Avatar(outCropFile, phone, password, new RequestCallBack<String>() {
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
                                            UtilSnackbar.showSimple(getView().findViewById(R.id.ll_user), object.getString("tip"));
                                            return;
                                        }
                                        Toast.makeText(Http.getCotext(), getResources().getString(R.string.postphone), Toast.LENGTH_SHORT).show();
                                        SharedPreferencesHelper.getInstance().putString(Contact.SH_Avatar, object.getString("avatar"));
//                                        Picasso.with(getActivity()).load(object.getString("avatar")).into(iv_header);
//                                        updata();
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(HttpException e, String s) {
                                    Util.cancelProgressFor(getActivity());
                                    UtilSnackbar.showSimple(getView().findViewById(R.id.ll_user), "上传头像失败！");
                                }
                            });*/
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        showToast("裁剪图片失败");
                    }
                    break;
                default:
                    break;
            }
        }
    }
    public void showToast(String msg) {
        Toast.makeText(Http.getCotext(), msg, Toast.LENGTH_SHORT).show();
    }
    public void update(){
        getView().findViewById(R.id.ll_not_logged_in).setVisibility(View.GONE);
        getView().findViewById(R.id.ll_photo).setVisibility(View.VISIBLE);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    public static SubAccount subAccount;
    public static MUserInfo muserinfo;
    public static String user;
    public static String ps;
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
                     user = msg.getData().getString("user");
                     ps = msg.getData().getString("ps");
                    nextPage();
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
                                 subAccount = new SubAccount(subAccountSid,subToken,dateCreated,voipAccount,voipPwd);
                                 muserinfo = new MUserInfo(userid,username,realname,mobile,type);
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
