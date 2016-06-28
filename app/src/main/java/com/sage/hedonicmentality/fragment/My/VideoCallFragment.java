package com.sage.hedonicmentality.fragment.My;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.utils.Contact;
import com.sage.hedonicmentality.utils.SPHelper;
import com.sage.hedonicmentality.utils.Util;
import com.sage.hedonicmentality.view.VoipCallDialog;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECInitParams;
import com.yuntongxun.ecsdk.ECVoIPCallManager;
import com.yuntongxun.ecsdk.ECVoIPSetupManager;
import com.yuntongxun.ecsdk.SdkErrorCode;
import com.yuntongxun.ecsdk.VideoRatio;
import com.yuntongxun.ecsdk.voip.video.ECCaptureView;

import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/31.
 */
//public class VideoCallFragment extends Fragment {
public class VideoCallFragment extends FragmentActivity {
    @Bind(R.id.sv_teacher)
    ECCaptureView sv_teacher;
    @Bind(R.id.sv_user)
    ECCaptureView sv_user;
    public static LinearLayout ll_voip;
    public static RelativeLayout rl_request;
    private String mCallId;
    public  static int CAMERA_PREPOSITION = 1;//  前置摄像头
    public  static int CAMERA_POSTPOSITION = 0;// 后置
    public  static int CAPABILITYINDEX = 5;//摄像头支持的分辨率index
    private boolean isSelectCamera=true;
    private boolean isSelectSound=true;
    private String mCurrentCallId;
    public VoipCallDialog dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videocallfragment);
        ButterKnife.bind(this);
        mCallId = SPHelper.getDefaultString(this,SPHelper.TEACHER_NUMBER,"");
        Util.videoCallList.add(VideoCallFragment.this);
        showDialog();
        call(mCallId);

    }
    public void showDialog(){
        dialog = VoipCallDialog.create(mHandler);
        dialog.show(getSupportFragmentManager(),"VoipCallDialog");
    }
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = View.inflate(getActivity(), R.layout.videocallfragment, null);
//        ButterKnife.bind(this, view);
//        mCallId = SPHelper.getDefaultString(getActivity(),SPHelper.TEACHER_NUMBER,"");
//        call(mCallId);
//        return view;
//    }
    private Handler mHandler=new Handler(){
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch(msg.what){
            case 1:
                break;
            case 8:
                ECDevice.getECVoIPCallManager().releaseCall(mCurrentCallId);
                finish();
                break;
        }
    }
};
    @OnClick({R.id.ll_close,R.id.iv_camera,R.id.iv_sound})
    public void videocallOnclick(View view) {
        if (view.getId()==R.id.ll_close) {
            finish();
            ECDevice.getECVoIPCallManager().releaseCall(mCurrentCallId);
        }
        if (view.getId()==R.id.iv_camera) {
            if (isSelectCamera) {
                changeCamera(0);
                isSelectCamera = false;
            } else {
                changeCamera(1);
                isSelectCamera = true;
            }
        }
        if (view.getId()==R.id.iv_sound) {
            if (ECDevice. getECVoIPSetupManager().getLoudSpeakerStatus()) {
                ECDevice. getECVoIPSetupManager().enableLoudSpeaker(false);
                Log.e("isSelectSound", ECDevice.getECVoIPSetupManager().getLoudSpeakerStatus()+ "");
            }else{
                ECDevice. getECVoIPSetupManager().enableLoudSpeaker(true);
                Log.e("isSelectSound", ECDevice.getECVoIPSetupManager().getLoudSpeakerStatus() + "");

            }
        }
    }

    public void call(String id){
        ECDevice.getECVoIPSetupManager().getCameraInfos();
        ECVoIPSetupManager setupManager = ECDevice.getECVoIPSetupManager();
        setupManager.setCodecEnabled(ECVoIPSetupManager.Codec.Codec_G729, true);
        setupManager.setAudioConfigEnabled(ECVoIPSetupManager.AudioType.AUDIO_EC, true, ECVoIPSetupManager.AudioMode.EC_Conference);
        ECDevice.getECVoIPSetupManager().selectCamera(CAMERA_POSTPOSITION, CAPABILITYINDEX, Util.getFps(VideoCallFragment.this), null, true);
        ECDevice.getECVoIPSetupManager().setVideoView(sv_teacher, sv_user);
        mCurrentCallId = ECDevice.getECVoIPCallManager().makeCall(ECVoIPCallManager.CallType.VIDEO,id);
        Log.e("VideoCallFragment",mCurrentCallId);
    }

    public void changeCamera(int type){
        if (type==CAMERA_POSTPOSITION){
            sv_user.setVisibility(View.GONE);
            ECDevice.getECVoIPSetupManager().selectCamera(CAMERA_POSTPOSITION, CAPABILITYINDEX, Util.getFps(VideoCallFragment.this), null, true);
        }else{
            sv_user.setVisibility(View.VISIBLE);
            ECDevice.getECVoIPSetupManager().selectCamera(CAMERA_PREPOSITION, CAPABILITYINDEX, Util.getFps(VideoCallFragment.this), null, true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("voip-call", "video-onDestroy-" + mCurrentCallId);
        Log.e("finish", "destroy videofr finish");
        ECDevice.getECVoIPCallManager().releaseCall(mCurrentCallId);
        if (dialog!=null) {
            dialog.dismiss();
        }

    }
}
