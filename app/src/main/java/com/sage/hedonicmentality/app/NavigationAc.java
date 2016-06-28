package com.sage.hedonicmentality.app;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.fragment.My.ConsultFragment;
import com.sage.hedonicmentality.fragment.My.FindFragment;
import com.sage.hedonicmentality.fragment.My.IncomingCallActivity;
import com.sage.hedonicmentality.fragment.My.UserFragment;
import com.sage.hedonicmentality.fragment.My.VideoCallFragment;
import com.sage.hedonicmentality.utils.Contact;
import com.sage.hedonicmentality.utils.SPHelper;
import com.sage.hedonicmentality.utils.Util;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECInitParams;
import com.yuntongxun.ecsdk.ECVoIPCallManager;
import com.yuntongxun.ecsdk.SdkErrorCode;
import com.yuntongxun.ecsdk.VideoRatio;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/17.
 * 首页
 */
public class NavigationAc extends FragmentActivity{
    @Bind(R.id.fl)
    FrameLayout fl;
    @Bind(R.id.iv_find)
    ImageView iv_find;
    @Bind(R.id.iv_consult)
    ImageView iv_consult;
    @Bind(R.id.iv_user)
    ImageView iv_user;
    @Bind(R.id.tv_find)
    TextView tv_find;
    @Bind(R.id.tv_consult)
    TextView tv_consult;
    @Bind(R.id.tv_user)
    TextView tv_user;
    public static FrameLayout maxfl;
    private FragmentManager fragmentManager;
    private FindFragment findFragment;
    private ConsultFragment consultfragment;
    private UserFragment userfragment;
    private FragmentTransaction transaction;
    private long exitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigationac);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        ButterKnife.bind(this);
        SPHelper.putDefaultString(NavigationAc.this,SPHelper.USER_NUMBER,"18684642028");
        initSDK();
        initView();
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            Rect frame = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            int statusBarHeight = frame.top;
            SPHelper.putDefaultInt(NavigationAc.this, SPHelper.WINDOW_TOP_HEIGHT, statusBarHeight);
        }
    }
    private void initView() {
        fragmentManager = getSupportFragmentManager();
        setTab(1);
        changeState(1);
        maxfl = (FrameLayout)findViewById(R.id.maxfl);
    }
    @OnClick({R.id.ll_find,R.id.ll_zixun,R.id.ll_user})
    public void NaAcAOnclick(View view){
        switch (view.getId()){
            case R.id.ll_find:
                findFragment.setScrollviewPosition();
                setTab(1);
                changeState(1);
                break;
            case R.id.ll_zixun:
                setTab(2);
                changeState(2);
                break;
            case R.id.ll_user:
                changeState(3);
                setTab(3);
                break;
        }

    }
    /**添加Fr*/
    public static void addFr(Fragment fragment,String name,FragmentManager fragmentManager,int animType){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (animType==1) {
            transaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_in, R.anim.back_left_in, R.anim.back_right_out);
        }else if(animType==2) {
            transaction.setCustomAnimations(R.anim.anim_ins, R.anim.anim_ins, R.anim.y_anim_in, R.anim.y_anim_out);
        }
        transaction.add(R.id.maxfl, fragment, name);
        transaction.addToBackStack(null);
        transaction.show(fragment);
        transaction.commit();
    }
    public void changeState(int type){
        switch (type){
            case 1:
                tv_find.setTextColor(getResources().getColor(R.color.green_essential_colour));
                tv_consult.setTextColor(getResources().getColor(R.color.grays));
                tv_user.setTextColor(getResources().getColor(R.color.grays));
                iv_find.setImageResource(R.mipmap.table_find_higlighted);
                iv_consult.setImageResource(R.mipmap.talk_normal);
                iv_user.setImageResource(R.mipmap.table_mine_normal);
                break;
            case 2:
                tv_find.setTextColor(getResources().getColor(R.color.grays));
                tv_consult.setTextColor(getResources().getColor(R.color.green_essential_colour));
                tv_user.setTextColor(getResources().getColor(R.color.grays));
                iv_find.setImageResource(R.mipmap.table_find_normal);
                iv_consult.setImageResource(R.mipmap.talk_selected);
                iv_user.setImageResource(R.mipmap.table_mine_normal);
                break;
            case 3:
                tv_find.setTextColor(getResources().getColor(R.color.grays));
                tv_consult.setTextColor(getResources().getColor(R.color.grays));
                tv_user.setTextColor(getResources().getColor(R.color.green_essential_colour));
                iv_find.setImageResource(R.mipmap.table_find_normal);
                iv_consult.setImageResource(R.mipmap.talk_normal);
                iv_user.setImageResource(R.mipmap.table_mine_highlighted);
                break;
        }

    }
    public void setTab(int type){
        transaction = fragmentManager.beginTransaction();
        switch (type){
            case 1:
                if (findFragment!=null) {
                    transaction.show(findFragment);
                }else{
                    findFragment = new FindFragment();
                    transaction.add(R.id.fl, findFragment);
                }
                if (consultfragment!=null) {
                    transaction.hide(consultfragment);
                }
                if (userfragment!=null) {
                    transaction.hide(userfragment);
                }
                break;
            case 2:
                if (consultfragment!=null) {
                    transaction.show(consultfragment);
                }else{
                    consultfragment = new ConsultFragment();
                    transaction.add(R.id.fl, consultfragment);
                }
                if (findFragment!=null) {
                    transaction.hide(findFragment);
                }
                if (userfragment!=null) {
                    transaction.hide(userfragment);
                }
                break;
            case 3:
                if (userfragment!=null) {
                    transaction.show(userfragment);
                }else{
                    userfragment = new UserFragment();
                    transaction.add(R.id.fl, userfragment);
                }
                if (consultfragment!=null) {
                    transaction.hide(consultfragment);
                }
                if (findFragment!=null) {
                    transaction.hide(findFragment);
                }
                break;
        }
        transaction.commit();
    }

    public void initSDK(){
        // 通过init初始化ECDeviceKit类,userId为当前用户账号，跟登录时传入ECAuthParameters中设置的userId值保持一直;
        if(!ECDevice.isInitialized()) {
            ECDevice.initial(this, new ECDevice.InitListener() {
                @Override
                public void onInitialized() {
                    // SDK已经初始化成功
                    Log.e("EC-login", "初始化成功");
                    Intent intent = new Intent(NavigationAc.this, IncomingCallActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(NavigationAc.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    ECDevice.setPendingIntent(pendingIntent);
                    sdk();
                }
                @Override
                public void onError(Exception exception) {
                    Log.e("EC-login", "初始化失败" );
                    Log.e("EC-Exception", exception.toString());
                }
            });
        }
    }

    public void sdk(){
//        第二步：设置注册参数、设置通知回调监听
        ECInitParams params = ECInitParams.createParams();
        //自定义登录方式：
        String user_number = SPHelper.getDefaultString(this,SPHelper.USER_NUMBER,"");
        params.setUserid(user_number);
        params.setAppKey(Contact.SH_KEY);
        params.setToken(Contact.SH_TOKEN);
        params.setAuthType(ECInitParams.LoginAuthType.NORMAL_AUTH);
        // 1代表用户名+密码登陆（可以强制上线，踢掉已经在线的设备）
        // 2代表自动重连注册（如果账号已经在其他设备登录则会提示异地登陆）
        // 3 LoginMode（强制上线：FORCE_LOGIN  默认登录：AUTO）
        params.setMode(ECInitParams.LoginMode.FORCE_LOGIN);
        params.setOnDeviceConnectListener(new ECDevice.OnECDeviceConnectListener() {
            public void onConnect() {
                // 兼容4.0，5.0可不必处理
            }

            @Override
            public void onDisconnect(ECError error) {
                // 兼容4.0，5.0可不必处理
            }
            @Override
            public void onConnectState(ECDevice.ECConnectState state, ECError error) {
                if(state == ECDevice.ECConnectState.CONNECT_FAILED ){
                    if(error.errorCode == SdkErrorCode.SDK_KICKED_OFF) {
                        //账号异地登陆
                        Log.e("EC-login", "异地账号登录" );
                    }
                    else{
                        //连接状态失败
                        Log.e("EC-login", "登录失败" );
                    }
                    return ;
                }
                else if(state == ECDevice.ECConnectState.CONNECT_SUCCESS) {
                    // 登陆成功
                    Log.e("EC-login", "登录成功");
                }
            }
        });

        // 获得SDKVoIP呼叫接口
        // 注册VoIP呼叫事件回调监听
        ECVoIPCallManager callInterface = ECDevice.getECVoIPCallManager();
        if(callInterface != null) {
            callInterface.setOnVoIPCallListener(new ECVoIPCallManager.OnVoIPListener() {
                @Override
                public void onVideoRatioChanged(VideoRatio videoRatio) {
                    Log.e("EC-Message", "videoRatio：" + videoRatio.toString());
                }

                @Override
                public void onSwitchCallMediaTypeRequest(String s, ECVoIPCallManager.CallType callType) {
                    Log.e("EC-Message", "TypeRequest-callType：" + callType);
                }

                @Override
                public void onSwitchCallMediaTypeResponse(String s, ECVoIPCallManager.CallType callType) {
                    Log.e("EC-Message", "TypeResponse-callType：" + callType);
                }

                @Override
                public void onDtmfReceived(String s, char c) {
                    Log.e("EC-Message", "onDtmfReceived：" + c);
                }

                @Override
                public void onCallEvents(ECVoIPCallManager.VoIPCall voipCall) {
                    // 处理呼叫事件回调
                    int reason =voipCall.reason;
                    Log.e("EC-call",reason+"");
                    if (voipCall == null) {
                        Log.e("SDKCoreHelper", "handle call event error , voipCall null");
                        return;
                    }

                    // 根据不同的事件通知类型来处理不同的业务
                    ECVoIPCallManager.ECCallState callState = voipCall.callState;
                    switch (callState) {
                        case ECCALL_PROCEEDING:
                            Log.e("EC-call", "正在连接服务器处理呼叫请求" );
                            break;
                        case ECCALL_ALERTING:
                            Log.e("EC-call", "呼叫到达对方客户端，对方正在振铃" );
                            break;
                        case ECCALL_ANSWERED:
                            Log.e("EC-call", "对方接听本次呼叫" );
                            if(Util.videoCallList!=null &&!Util.videoCallList.isEmpty()){
                               VideoCallFragment videoCall = (VideoCallFragment)Util.videoCallList.get(0);
                                videoCall.dialog.dismiss();
                            }
                            break;
                        case ECCALL_FAILED:
                            Log.e("EC-call", "本次呼叫失败" );
                            if(Util.incomingList!=null &&!Util.incomingList.isEmpty()){
                                for (int i=0;i<Util.incomingList.size();i++){
                                    Util.incomingList.get(i).finish();
                                    Util.incomingList.clear();
                                }
                            }else if(Util.videoCallList!=null &&!Util.videoCallList.isEmpty()){
                                for (int i=0;i<Util.videoCallList.size();i++){
                                    Util.videoCallList.get(i).finish();
                                    Util.videoCallList.clear();
                                }
                            }
                            break;
                        case ECCALL_RELEASED:
                            if(Util.incomingList!=null &&!Util.incomingList.isEmpty()){
                                for (int i=0;i<Util.incomingList.size();i++){
                                    Util.incomingList.get(i).finish();
                                    Util.incomingList.clear();
                                }
                            }else if(Util.videoCallList!=null &&!Util.videoCallList.isEmpty()){
                                for (int i=0;i<Util.videoCallList.size();i++){
                                    Util.videoCallList.get(i).finish();
                                    Util.videoCallList.clear();
                                }
                            }
                            Log.e("EC-call", "通话释放[完成一次呼叫]" );
                            break;
                        default:
                            Log.e("SDKCoreHelper", "handle call event error , callState " + callState);
                            break;
                    }

                    Log.e("EC-call",reason+"");
                }
            });
        }

//        第三步：验证参数是否正确，注册SDK
        if(params.validate()) {
            // 判断注册参数是否正确
            ECDevice.login(params);
        }
    }
    /**退出**/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if(Util.incomingList!=null &&!Util.incomingList.isEmpty()){
                for (int i=0;i<Util.incomingList.size();i++){
                    Util.incomingList.get(i).finish();
                    Util.incomingList.clear();
                }
            }else if(Util.videoCallList!=null &&!Util.videoCallList.isEmpty()){
                for (int i=0;i<Util.videoCallList.size();i++){
                    Util.videoCallList.get(i).finish();
                    Util.videoCallList.clear();
                }
            }else {
                if (fragmentManager.getBackStackEntryCount()==0) {
                    if ((System.currentTimeMillis() - exitTime) > 2000) {
                        Toast.makeText(getApplicationContext(), "再按一次退出应用!",
                                Toast.LENGTH_SHORT).show();
                        exitTime = System.currentTimeMillis();
                    } else {
                        finish();
                        System.exit(0);
                    }
                }else if(fragmentManager.getBackStackEntryCount()>0){
                    fragmentManager.popBackStack();
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
