package com.sage.hedonicmentality.fragment.My;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.utils.Util;
import com.sage.hedonicmentality.utils.UtilSnackbar;
import com.sage.hedonicmentality.view.IncomingDialog;
import com.yuntongxun.ecsdk.CameraInfo;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECUserState;
import com.yuntongxun.ecsdk.ECVoIPCallManager;
import com.yuntongxun.ecsdk.ECVoIPSetupManager;
import com.yuntongxun.ecsdk.voip.video.ECCaptureView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/20.
 */
public class IncomingCallActivity extends FragmentActivity {
    /**昵称*/
    public static final String EXTRA_CALL_NAME = "con.yuntongxun.ecdemo.VoIP_CALL_NAME";
    /**通话号码*/
    public static final String EXTRA_CALL_NUMBER = "con.yuntongxun.ecdemo.VoIP_CALL_NUMBER";
    /**呼入方或者呼出方*/
    public static final String EXTRA_OUTGOING_CALL = "con.yuntongxun.ecdemo.VoIP_OUTGOING_CALL";
    /**VoIP呼叫*/
    public static final String ACTION_VOICE_CALL = "con.yuntongxun.ecdemo.intent.ACTION_VOICE_CALL";
    /**Video呼叫*/
    public static final String ACTION_VIDEO_CALL = "con.yuntongxun.ecdemo.intent.ACTION_VIDEO_CALL";
    public static final String ACTION_CALLBACK_CALL = "con.yuntongxun.ecdemo.intent.ACTION_VIDEO_CALLBACK";
    private static final int CAPABILITYINDEX = 5;

    /**通话昵称*/
    protected String mCallName;
    /**通话号码*/
    protected String mCallNumber;
    protected String mPhoneNumber;
    /**是否来电*/
    protected boolean mIncomingCall = false;
    /**呼叫唯一标识号*/
    protected String mCallId;
    /**VoIP呼叫类型（音视频）*/
    protected ECVoIPCallManager.CallType mCallType;
    /**透传号码参数*/
    private static final String KEY_TEL = "tel";
    /**透传名称参数*/
    private static final String KEY_NAME = "nickname";
    private static final int CAMERA_PREPOSITION =1;//前置
    private static final int CAMERA_POSTPOSITION=0;//后置摄像头
    private Intent sIntent;
    @Bind(R.id.sv_user)
    ECCaptureView sv_user;
    @Bind(R.id.sv_teacher)
    ECCaptureView sv_teacher;
    @Bind(R.id.ll_voip)
    LinearLayout ll_voip;
    private boolean isSelect= true;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.incomingcall);
        ButterKnife.bind(this);
        this.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        Util.incomingList.add(IncomingCallActivity.this);
        mIncomingCall = !(getIntent().getBooleanExtra(EXTRA_OUTGOING_CALL, false));
        //获取是否是音频还是视频
        mCallType = (ECVoIPCallManager.CallType) getIntent().getSerializableExtra(ECDevice.CALLTYPE);
        //获取当前的callid
        mCallId = getIntent().getStringExtra(ECDevice.CALLID);
        Toast.makeText(IncomingCallActivity.this,mCallId,Toast.LENGTH_LONG).show();
        //获取对方的号码
        mCallNumber = getIntent().getStringExtra(ECDevice.CALLER);

        CameraInfo[] ss = ECDevice.getECVoIPSetupManager().getCameraInfos();
        Log.e("CameraInfo",ss.toString());
        ECVoIPSetupManager setupManager = ECDevice.getECVoIPSetupManager();
        // 比如设置开启回音消除模式
        setupManager.setAudioConfigEnabled(ECVoIPSetupManager.AudioType.AUDIO_EC, true, ECVoIPSetupManager.AudioMode.EC_Conference);
        // 比如：设置当前通话使用 G729编码传输
        setupManager.setCodecEnabled(ECVoIPSetupManager.Codec.Codec_G729, true);
        // 比如是否启用回音消除
        boolean a = setupManager.getAudioConfig(ECVoIPSetupManager.AudioType.AUDIO_EC);
        // 查询回音消除模式
        ECVoIPSetupManager.AudioMode b = setupManager.getAudioConfigMode(ECVoIPSetupManager.AudioType.AUDIO_EC);
        // 查询制定编解码是否支持
        boolean c = setupManager.getCodecEnabled(ECVoIPSetupManager.Codec.Codec_G729);
        //如果视频呼叫，则在接受呼叫之前，需要先设置视频通话显示的view
        ECDevice.getECVoIPSetupManager().selectCamera(CAMERA_POSTPOSITION, CAPABILITYINDEX, Util.getFps(IncomingCallActivity.this), null, true);
        ECDevice.getECVoIPSetupManager().setVideoView(sv_teacher, sv_user);
        Log.e("callin", a + "/" + c + "/" + b.toString());

        ECDevice.getUserState(mCallNumber, new ECDevice.OnGetUserStateListener() {
            @Override
            public void onGetUserState(ECError ecError, ECUserState ecUserState) {
                //处理获取对方状态的回调结果
                //通过userState就能知道对方的终端类型、网络及在线状态等等
            }
        });
        showDialog();
    }
    public void changeCamera(int type){
        if (type==CAMERA_POSTPOSITION){
            sv_user.setVisibility(View.GONE);
//            ECDevice.getECVoIPSetupManager().selectCamera(CAMERA_POSTPOSITION, CAPABILITYINDEX, Util.getFps(this), null, true);
        }else{
            sv_user.setVisibility(View.VISIBLE);
            ECDevice.getECVoIPSetupManager().selectCamera(CAMERA_PREPOSITION, CAPABILITYINDEX, Util.getFps(this), null, true);
        }
    }
    public void showDialog(){
        IncomingDialog incomingDialog = IncomingDialog.create(mHandler);
        incomingDialog.show(getSupportFragmentManager(),"incomingdialog");
    }
    @OnClick({R.id.ll_close,R.id.iv_camera,R.id.iv_sound})
    public void videocallOnclick(View view) {
        if (view.getId()==R.id.ll_close) {
            ECDevice.getECVoIPCallManager().releaseCall(mCallId);
            finish();
            Log.e("finish", "onclick incomingac finish");

        }
        if (view.getId()==R.id.iv_camera) {
            if (isSelect) {
                changeCamera(0);
                isSelect=false;
            }else{
                changeCamera(1);
                isSelect=true;
            }
        }
        if (view.getId()==R.id.iv_sound) {
            if (ECDevice. getECVoIPSetupManager().getLoudSpeakerStatus()) {
                ECDevice. getECVoIPSetupManager().enableLoudSpeaker(false);
                Log.e("isSelectSound", ECDevice.getECVoIPSetupManager().getLoudSpeakerStatus() + "");
            }else{
                ECDevice. getECVoIPSetupManager().enableLoudSpeaker(true);
                Log.e("isSelectSound", ECDevice.getECVoIPSetupManager().getLoudSpeakerStatus() + "");

            }
        }
    }
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case 1:
                    getWindow().getDecorView().setBackgroundColor(Color.WHITE);
                    ll_voip.setVisibility(View.VISIBLE);
                    ECDevice.getECVoIPCallManager().acceptCall(mCallId);
                    break;
                case 2:
                    ECDevice.getECVoIPCallManager().rejectCall(mCallId,0);
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("voip-call", "onDestroy-" + mCallId);
        ECDevice.getECVoIPCallManager().releaseCall(mCallId);
        Log.e("finish", "incomingac finish");

    }
}
