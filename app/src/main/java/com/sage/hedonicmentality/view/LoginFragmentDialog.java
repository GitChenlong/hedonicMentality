package com.sage.hedonicmentality.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.utils.AnimationUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/31.
 */
public class LoginFragmentDialog extends DialogFragment {
    private static Handler mHandler;
    private FrameLayout fl;
    @Bind(R.id.fl_register)
    LinearLayout fl_register;
    @Bind(R.id.fl_login)
    LinearLayout fl_login;
    @Bind(R.id.fl_forget)
    LinearLayout fl_forget;
    @Bind(R.id.login)
    TextView login;
    @Bind(R.id.register)
    TextView register;
    @Bind(R.id.tv_forget)
    TextView forget;
    @Bind(R.id.et_login_user)
    EditText et_login_user;
    @Bind(R.id.et_login_ps)
    EditText et_login_ps;
    @Bind(R.id.et_register_name)
    EditText et_register_name;
    @Bind(R.id.et_register_user)
    EditText et_register_user;
    @Bind(R.id.et_register_ps)
    EditText et_register_ps;
    @Bind(R.id.et_register_ems)
    EditText et_register_ems;
    @Bind(R.id.et_forget_user)
    EditText et_forget_user;
    @Bind(R.id.et_forget_ems)
    EditText et_forget_ems;
    @Bind(R.id.et_forget_ps)
    EditText et_forget_ps;
    @Bind(R.id.btn_forget_ems)
    Button btn_forget_ems;
    @Bind(R.id.btn_register_ems)
    Button btn_register_ems;
    @Bind(R.id.scrollview)
    ScrollView scrollview;
    @Bind(R.id.ll)
    LinearLayout ll;
    @Bind(R.id.iv_eye)
    ImageView iv_eye;
    @Bind(R.id.iv_register_eye)
    ImageView iv_register_eye;
    @Bind(R.id.iv_forget_eye)
    ImageView iv_forget_eye;
    private CountDownTimer countDownTimer;
    private boolean isLoginPs =false,isRegisterPs=false,isForgetPs=false;
    public static LoginFragmentDialog create(Handler handler){
        mHandler = handler;
        LoginFragmentDialog fragment=new LoginFragmentDialog();
        return fragment;
    }
    public LoginFragmentDialog(){
        setStyle(STYLE_NO_TITLE, R.style.DiaScaleAnimationTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.loginfragmentdialog, container, false);
        ButterKnife.bind(this, view);
        init(view);
        return view;
    }

    public  void changeFr(int type) {
        fl_login.setVisibility(type==1?View.VISIBLE:View.GONE);
        fl_register.setVisibility(type == 2 ? View.VISIBLE : View.GONE);
        fl_forget.setVisibility(type == 3 ? View.VISIBLE : View.GONE);
        if (type==1) {
            fl_register.setAnimation(AnimationUtil.moveToViewBottom());
            fl_forget.setAnimation(AnimationUtil.moveToViewBottom());
            fl_login.setAnimation(AnimationUtil.moveToViewLocation());
        }
        if (type==2) {
            fl_forget.setAnimation(AnimationUtil.moveToViewBottom());
            fl_login.setAnimation(AnimationUtil.moveToViewBottom());
            fl_register.setAnimation(AnimationUtil.moveToViewLocation());
        }
        if (type==3) {
            fl_login.setAnimation(AnimationUtil.moveToViewBottom());
            fl_register.setAnimation(AnimationUtil.moveToViewBottom());
            fl_forget.setAnimation(AnimationUtil.moveToViewLocation());
//            // 向左边移入
//            fl_login.setAnimation(AnimationUtils.makeInAnimation(getActivity(), false));
//            // 向右边边移出
//            fl_forget.setAnimation(AnimationUtils.makeOutAnimation(getActivity(), true));

        }
    }
    public void init(View view){

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        changeFr(1);
        Window window=getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable());
        WindowManager.LayoutParams wl = window.getAttributes();
//        wl.x = 0;//这里的坐标是相对默认重心 center而言的。
//        wl.y = getResources().getDisplayMetrics().heightPixels;
        // 以下这两句是为了保证按钮可以水平满屏
//        wl.width = getResources().getDisplayMetrics().widthPixels*9/10;
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.dimAmount=0.5f;
        wl.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        // 设置显示位置
        getDialog().onWindowAttributesChanged(wl);
        //window.setAttributes(wl);
        // 设置点击外围取消
        getDialog().setCanceledOnTouchOutside(false);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @OnClick({R.id.login,R.id.register,R.id.tv_forget,R.id.iv_cancel,R.id.btn_forget_ems,R.id.btn_register_ems,
    R.id.tv_register_forget,R.id.tv_register_login,R.id.tv_forget_login,R.id.txt_forget_register,R.id.tv_login_forget,
    R.id.tv_login_register,R.id.iv_eye,R.id.iv_register_eye,R.id.iv_forget_eye})
    public void loginonclick(View v){
        switch (v.getId()){
            case R.id.login:
                sendLoginMessage(1,"account_number",et_login_user.getText().toString(),"password",et_login_ps.getText().toString());
                break;
            case R.id.register:
                sendRegisterMessage(2, "register_name", "register_phone", "register_ps", "register_ems", et_register_name.getText().toString(), et_register_user.getText().toString(),
                        et_register_ps.getText().toString(), et_register_ems.getText().toString());
                break;
            case R.id.tv_forget:
                sendForgetPsMessage(3, "forget_phone", "forget_ems", "forget_ps", et_forget_user.getText().toString(), et_forget_ems.getText().toString(), et_forget_ps.getText().toString());
                break;
            case R.id.iv_cancel:
                dismiss();
                hideInputFromwindow();
                break;
            case R.id.btn_forget_ems:
                sendEmsMessage(4, 60, btn_forget_ems);
                break;
            case R.id.btn_register_ems:
                sendEmsMessage(5, 60, btn_register_ems);
                break;
            case R.id.tv_register_forget:
                changeFr(3);
                break;
            case R.id.tv_register_login:
                changeFr(1);
                break;
            case R.id.tv_forget_login:
                changeFr(1);
                break;
            case R.id.txt_forget_register:
                changeFr(2);
                break;
            case R.id.tv_login_forget:
                changeFr(3);
                break;
            case R.id.tv_login_register:
                changeFr(2);
                break;
            case R.id.iv_eye://登录 密码是否明文显示
                int loginEye = isLoginPs ? R.mipmap.eye_higligted:R.mipmap.gray_eye_normal;
                iv_eye.setImageResource(loginEye);
                TransformationMethod method = isLoginPs ? PasswordTransformationMethod
                        .getInstance() : HideReturnsTransformationMethod
                        .getInstance();
                et_login_ps.setTransformationMethod(method);
                isLoginPs = isLoginPs ? false : true;
                break;
            case R.id.iv_register_eye://注册 密码是否明文显示
                int registerD = isRegisterPs ? R.mipmap.eye_higligted:R.mipmap.gray_eye_normal;
                iv_register_eye.setImageResource(registerD);
                TransformationMethod registerM = isRegisterPs ? PasswordTransformationMethod
                        .getInstance() : HideReturnsTransformationMethod
                        .getInstance();
                et_register_ps.setTransformationMethod(registerM);
                isRegisterPs = isRegisterPs ? false : true;
                break;
            case R.id.iv_forget_eye://忘记 密码是否明文显示
                int forgetD = isRegisterPs ?R.mipmap.eye_higligted: R.mipmap.gray_eye_normal;
                iv_forget_eye.setImageResource(forgetD);
                TransformationMethod forgetM = isRegisterPs ? PasswordTransformationMethod
                        .getInstance() : HideReturnsTransformationMethod
                        .getInstance();
                et_forget_ps.setTransformationMethod(forgetM);
                isRegisterPs = isRegisterPs ? false : true;
                break;
        }
    }
    public void hideInputFromwindow(){
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0); //强制隐藏键盘
    }
    public void sendLoginMessage(int what,String key_phone ,String phone,String key_ps,String ps){
        Message message=new Message();
        message.what=what;
        Bundle bundle = new Bundle();
        bundle.putString(key_phone,phone);
        bundle.putString(key_ps,ps);
        message.setData(bundle);
        mHandler.sendMessage(message);
    }
    public void sendRegisterMessage(int what,String key_name ,String key_phone,String key_ps,String key_ems
                                    ,String name ,String phone,String ps,String ems){
        Message message=new Message();
        message.what=what;
        Bundle bundle = new Bundle();
        bundle.putString(key_name,name);
        bundle.putString(key_phone,phone);
        bundle.putString(key_ps,ps);
        bundle.putString(key_ems,ems);
        message.setData(bundle);
        mHandler.sendMessage(message);
    }
    public void sendForgetPsMessage(int what,String key_phone,String key_ps,String key_ems
                                    ,String phone,String ps,String ems){
        Message message=new Message();
        message.what=what;
        Bundle bundle = new Bundle();
        bundle.putString(key_phone,phone);
        bundle.putString(key_ems,ems);
        bundle.putString(key_ps,ps);
        message.setData(bundle);
        mHandler.sendMessage(message);
    }
    public void sendEmsMessage(int what,int timer,Button view){
        Message message=new Message();
        message.what=what;
        mHandler.sendMessage(message);
        timer(timer,view);
    }
    private void timer(int time, final Button view){
        countDownTimer=new CountDownTimer(time*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                view.setText(millisUntilFinished/1000 + "秒");
                view.setClickable(false);
            }
            @Override
            public void onFinish() {
                view.setText("获取验证码");
                view.setClickable(true);
            }
        };
        countDownTimer.start();
    }


}
