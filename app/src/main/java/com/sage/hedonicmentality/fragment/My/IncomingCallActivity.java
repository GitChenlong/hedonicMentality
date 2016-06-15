package com.sage.hedonicmentality.fragment.My;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.utils.Util;
import com.yuntongxun.ecsdk.CameraInfo;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECUserState;
import com.yuntongxun.ecsdk.ECVoIPCallManager;
import com.yuntongxun.ecsdk.ECVoIPSetupManager;
import com.yuntongxun.ecsdk.voip.video.ECCaptureView;

/**
 * Created by Administrator on 2016/5/20.
 */
public class IncomingCallActivity extends Activity {
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

    private Intent sIntent;
    private ECCaptureView sv_teacher;
    private ECCaptureView sv_user;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.incomingcall);

        TextView tv_state = (TextView)findViewById(R.id.tv_state);
         sv_teacher = (ECCaptureView)findViewById(R.id.sv_teacher);
          sv_user = (ECCaptureView)findViewById(R.id.sv_user);
        mIncomingCall = !(getIntent().getBooleanExtra(EXTRA_OUTGOING_CALL, false));
        //获取是否是音频还是视频
        mCallType = (ECVoIPCallManager.CallType) getIntent().getSerializableExtra(ECDevice.CALLTYPE);
        //获取当前的callid
        mCallId = getIntent().getStringExtra(ECDevice.CALLID);
        //获取对方的号码
        mCallNumber = getIntent().getStringExtra(ECDevice.CALLER);
        tv_state.setText(mCallNumber + "/" + mCallType + "/" + mCallId+"/"+mIncomingCall);

        CameraInfo[] ss = ECDevice.getECVoIPSetupManager().getCameraInfos();
        Log.e("CameraInfo",ss.toString());
        ECVoIPSetupManager setupManager = ECDevice.getECVoIPSetupManager();
        // 比如：将视频通话码流设置成150
//        setupManager.setVideoBitRates(2048);
        // 比如设置开启回音消除模式
        setupManager.setAudioConfigEnabled(ECVoIPSetupManager.AudioType.AUDIO_EC,
                true, ECVoIPSetupManager.AudioMode.EC_Conference);
        // 比如：设置当前通话使用 G729编码传输
        setupManager.setCodecEnabled(ECVoIPSetupManager.Codec.Codec_G729, true);
        // 比如是否启用回音消除
        boolean a = setupManager.getAudioConfig(ECVoIPSetupManager.AudioType.AUDIO_EC);
        // 查询回音消除模式
        ECVoIPSetupManager.AudioMode b = setupManager.getAudioConfigMode(ECVoIPSetupManager.AudioType.AUDIO_EC);
        // 查询制定编解码是否支持
        boolean c = setupManager.getCodecEnabled(ECVoIPSetupManager.Codec.Codec_G729);
        //如果视频呼叫，则在接受呼叫之前，需要先设置视频通话显示的view
        ECDevice.getECVoIPSetupManager().selectCamera(0, 7, Util.getFps(IncomingCallActivity.this), null, true);
        ECDevice.getECVoIPSetupManager().setVideoView(sv_teacher,sv_user);
        Log.e("callin", a + "/" + c + "/" + b.toString());

        findViewById(R.id.bt_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ECDevice.getECVoIPCallManager().acceptCall(mCallId);

            }
        });
        findViewById(R.id.bt_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ECDevice.getECVoIPCallManager().releaseCall(mCallId);
                finish();

            }
        });

        ECDevice.getUserState(mCallNumber, new ECDevice.OnGetUserStateListener() {
            @Override
            public void onGetUserState(ECError ecError, ECUserState ecUserState) {
                //处理获取对方状态的回调结果
                //通过userState就能知道对方的终端类型、网络及在线状态等等
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ECDevice.getECVoIPCallManager().releaseCall(mCallId);
    }
}
