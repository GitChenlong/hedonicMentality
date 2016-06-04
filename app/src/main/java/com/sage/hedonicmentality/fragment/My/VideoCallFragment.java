package com.sage.hedonicmentality.fragment.My;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.sage.hedonicmentality.R;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECInitParams;
import com.yuntongxun.ecsdk.ECVoIPCallManager;
import com.yuntongxun.ecsdk.ECVoIPSetupManager;
import com.yuntongxun.ecsdk.SdkErrorCode;
import com.yuntongxun.ecsdk.VideoRatio;
import com.yuntongxun.ecsdk.voip.video.ECCaptureView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/31.
 */
public class VideoCallFragment extends Fragment {
    @Bind(R.id.sv_teacher)
    ECCaptureView sv_teacher;
    @Bind(R.id.sv_user)
    ECCaptureView sv_user;
    @Bind(R.id.phone)
    EditText phone;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.videocallfragment,null);
        ButterKnife.bind(this, view);
        initSDK();

        return view;

    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.videocallfragment);
//        ButterKnife.bind(this);
//        initSDK();
//    }

    @OnClick({R.id.callvideo})
    public void videoCall(View view) {
        if (view.getId()==R.id.callvideo&&!TextUtils.isEmpty(phone.getText().toString())) {
            ECDevice.getECVoIPSetupManager().getCameraInfos();
            ECVoIPSetupManager setupManager = ECDevice.getECVoIPSetupManager();
            setupManager.setVideoBitRates(2048);
            setupManager.setCodecEnabled(ECVoIPSetupManager.Codec.Codec_G729, true);
            ECDevice.getECVoIPSetupManager().setVideoView(sv_teacher, sv_user);
            String meesage = ECDevice.getECVoIPCallManager().makeCall(ECVoIPCallManager.CallType.VIDEO,phone.getText().toString());
        }
    }
    public void initSDK(){
        // 通过init初始化ECDeviceKit类,userId为当前用户账号，跟登录时传入ECAuthParameters中设置的userId值保持一直;
        if(!ECDevice.isInitialized()) {
            ECDevice.initial(getActivity(), new ECDevice.InitListener() {
                @Override
                public void onInitialized() {
                    // SDK已经初始化成功
                    Log.e("EC-login", "初始化成功" );
//                    Intent intent = new Intent(MainActivity.this, IncomingCallActivity.class);
//                    PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                    ECDevice.setPendingIntent(pendingIntent);
                    sdk();
                }

                @Override
                public void onError(Exception exception) {
                    // SDK 初始化失败,可能有如下原因造成
                    // 1、可能SDK已经处于初始化状态
                    // 2、SDK所声明必要的权限未在清单文件（AndroidManifest.xml）里配置、
                    //    或者未配置服务属性android:exported="false";
                    // 3、当前手机设备系统版本低于ECSDK所支持的最低版本（当前ECSDK支持
                    //    Android Build.VERSION.SDK_INT 以及以上版本）
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
        params.setUserid("147258369");
//        params.setAppKey("8a48b5515493a1b70154c278d9c92e9b");
//        params.setToken("30ef0d9cf8532f9431be1c01dad7e639");
        params.setAppKey("8a48b5515427d27601542d0afa4106f7");
        params.setToken("8f2833ff1378d5be6b630a398e18d1af");
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
                    Log.e("EC-login", "登录成功" );
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
                            break;
                        case ECCALL_FAILED:
                            Log.e("EC-call", "本次呼叫失败" );
                            break;
                        case ECCALL_RELEASED:
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
}
