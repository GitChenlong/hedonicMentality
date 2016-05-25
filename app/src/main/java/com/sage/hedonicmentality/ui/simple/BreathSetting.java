package com.sage.hedonicmentality.ui.simple;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.app.Common;
import com.sage.hedonicmentality.app.Http;
import com.sage.hedonicmentality.bean.HRVData;
import com.sage.hedonicmentality.bean.SceneBean;
import com.sage.hedonicmentality.fragment.breath.FragmentBlueDiaPrompt;
import com.sage.hedonicmentality.fragment.breath.FragmentDiaPrompt;
import com.sage.hedonicmentality.fragment.breath.FragmentaPromptdio;
import com.sage.hedonicmentality.fragment.breath.Fragmentdowloadshow;
import com.sage.hedonicmentality.fragment.breath.Fragmentislogin;
import com.sage.hedonicmentality.service.BluetoothLeService;
import com.sage.hedonicmentality.service.ServiceBlueTooth;
import com.sage.hedonicmentality.ui.ActivityAbdominalR;
import com.sage.hedonicmentality.ui.ActivityBreath;
import com.sage.hedonicmentality.ui.ActivityHuxituna;
import com.sage.hedonicmentality.ui.ActivityLogin;
import com.sage.hedonicmentality.ui.ActivityMe;
import com.sage.hedonicmentality.utils.Contact;
import com.sage.hedonicmentality.utils.GsonTools;
import com.sage.hedonicmentality.utils.SPHelper;
import com.sage.hedonicmentality.utils.SharedPreferencesHelper;
import com.sage.hedonicmentality.utils.Util;
import com.sage.hedonicmentality.utils.ZipUtils;
import com.sage.hedonicmentality.view.TuneWheelGF;
import com.sage.hedonicmentality.view.TuneWheelGO;
import com.sage.hedonicmentality.view.WiperSwitch;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sage on 2015/8/6.
 */
public class BreathSetting extends AppCompatActivity {

    @Bind(R.id.tv_bpm)
    TextView tv_bpm;
    @Bind(R.id.tv_time)
    TextView tv_time;
    @Bind(R.id.tv_type)
    TextView tv_type;
    @Bind(R.id.tv_Scene)
    TextView tv_Scene;
    @Bind(R.id.btn_left)
    ImageView btn_left;
    @Bind(R.id.tv_title)
    TextView tv_title;
    @Bind(R.id.btn_right)
    ImageView btn_right;
    @Bind(R.id.view_ws)
    WiperSwitch view_ws;
    @Bind(R.id.view_tunew_go)
    TuneWheelGO view_tunew_go;
    @Bind(R.id.view_tunew_gf)
    TuneWheelGF view_tunew_gf;
    @Bind(R.id.btn_start)
    Button btn_start;
//    @Bind(R.id.iv_next)
//    LinearLayout iv_next;
    private int bpm = 2;//呼吸频率位置
    private int duration;//呼吸时间位置
    private int device;
    private String[] devices;
    private String[] numbers;
    private int[] times;
    public AlertDialog.Builder builder;

    public HttpHandler<File> handler;
    private List<SceneBean> scenebeans;
    private HttpUtils http = new HttpUtils();
    public static final String ACTION = "com.tona.data";
//    private TunaReceivers tunareceiver = new TunaReceivers();

    private  FragmentaPromptdio pridio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.breath_setting);
//        btn_right.setVisibility(View.GONE);
        ButterKnife.bind(this);
        initView();
        initData();
            receiver=new LoginOutBroadcast();
            IntentFilter filter=new IntentFilter(Common.ACTION_LOGIN_OUT);
            registerReceiver(receiver, filter);
    //第一版暂时默认一个场景
        SPHelper.putDefaultString(this, SPHelper.KEY_SCENE_HTML, "index.html");
        SPHelper.putDefaultString(this, SPHelper.KEY_SCENE_NAME, "荷塘禅心");
        SPHelper.putDefaultString(this, SPHelper.KEY_SCENE_JS, "startScene");
        SPHelper.putDefaultString(this, SPHelper.KEY_SCENE_JSHX, "breathingRate");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        stopService(new Intent(this,ServiceBlueTooth.class));
        if(receiver!=null){
            unregisterReceiver(receiver);
        }
//        if(tunareceiver != null){
//            unregisterReceiver(tunareceiver);
//        }
//        unregisterBluetoothStateReceiver();
    }

    private LoginOutBroadcast receiver;
    public class LoginOutBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Common.ACTION_LOGIN_OUT)){
                finish();
            }
        }
    }
    private void initView() {
        btn_left.setVisibility(View.INVISIBLE);
        tv_title.setText("吐纳呼吸");
        btn_right.setImageResource(R.mipmap.me);
    }
    private void initData(){
        devices = getResources().getStringArray(R.array.device_names);
        numbers = getResources().getStringArray(R.array.number);
        times = getResources().getIntArray(R.array.times);
        SharedPreferencesHelper.getInstance().Builder(BreathSetting.this);
        String ID = SharedPreferencesHelper.getInstance().getString(Contact.SH_ID);
        if(TextUtils.isEmpty(ID)){
            SPHelper.putDefaultInt(BreathSetting.this, SPHelper.KEY_BREATH_PER_MIN, Integer.parseInt(numbers[3]));
            SPHelper.putDefaultInt(BreathSetting.this, SPHelper.KEY_BREATH_PER_MIN_ITEM,3);
            SPHelper.putDefaultInt(BreathSetting.this, SPHelper.KEY_DURATION_item, 0);
            SPHelper.putDefaultInt(BreathSetting.this, SPHelper.KEY_DURATION,times[0]);
        }
//         bpm= SPHelper.getDefaultInt(this, SPHelper.KEY_BREATH_PER_MIN_ITEM, 2);
//        tv_bpm.setText(getString(R.string.setting_bmp_format,numbers[bpm]));
         duration=SPHelper.getDefaultInt(this,SPHelper.KEY_DURATION_item,0);
        tv_time.setText(getString(R.string.setting_duration_format, times[duration]));
        SPHelper.putDefaultInt(BreathSetting.this, SPHelper.KEY_DEVICE, 0);
        device=SPHelper.getDefaultInt(this,SPHelper.KEY_DEVICE,0);
        tv_type.setText(devices[device]);
        view_ws.setChecked(false);
        view_ws.setOnChangedListener(new WiperSwitch.OnChangedListener() {
            @Override
            public void OnChanged(WiperSwitch wiperSwitch, boolean checkState) {
                if (!checkState) {
                    SPHelper.putDefaultInt(BreathSetting.this, SPHelper.KEY_DEVICE, 0);
                    device = 0;
                } else {
                    SPHelper.putDefaultInt(BreathSetting.this, SPHelper.KEY_DEVICE, 1);
                    device = 1;
                }
            }
        });
        view_tunew_go.setValue(SPHelper.getDefaultInt(this, SPHelper.KEY_BREATH_PER_MIN, 3));
        view_tunew_go.setValueChangeListener(new TuneWheelGO.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                SPHelper.putDefaultInt(BreathSetting.this, SPHelper.KEY_BREATH_PER_MIN, (int) value);
            }
        });
        int a = SPHelper.getDefaultInt(this, SPHelper.KEY_DURATION, 5);
        if(a == 30){
            a = 25;
        }else if(a == 60){
            a = 30;
        }
        view_tunew_gf.setValue(a);
        view_tunew_gf.setValueChangeListener(new TuneWheelGF.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                if (value == 25) {
                    value = 30;
                } else if (value == 30) {
                    value = 60;
                }
                SPHelper.putDefaultInt(BreathSetting.this, SPHelper.KEY_DURATION, (int) value);
            }
        });

    }
    public Fragmentislogin fragmentislogin;

    @OnClick({R.id.btn_start, R.id.btn_right, R.id.iv_left_bpm, R.id.iv_right_bpm, R.id.iv_left_duration, R.id.iv_right_duration,
            R.id.iv_left_device, R.id.iv_right_device,R.id.iv_next,R.id.iv_syhead,R.id.tv_prompt_dio})
    void set_click(View v) {
        String ID = SharedPreferencesHelper.getInstance().getString(Contact.SH_ID);
        switch (v.getId()) {
            case R.id.btn_start://开始
                if(isdowload){
                    Fragmentdowloadshow diaPrompt = new Fragmentdowloadshow();
                    diaPrompt.show(getSupportFragmentManager(),"promptdowload");
                    return;
                }
                File szip = new File(Environment.getExternalStorageDirectory() + "/HRVHTML/htcx.zip");
                int total = SPHelper.getDefaultInt(BreathSetting.this, "荷塘禅心total",0);

                //判断第一个场景的zip是否下载过
                if (!szip.exists()) {
                        updatedCJ();
                }else
                if(szip.length()<total){
                        updatedCJ();
                }else {
                    if(TextUtils.isEmpty(ID)){
                        int a = SPHelper.getDefaultInt(this, SPHelper.KEY_DURATION, 5);
                        int b = SPHelper.getDefaultInt(this, SPHelper.KEY_BREATH_PER_MIN, 3);
                         if(a == 30|| a ==60){
                             fragmentislogin = Fragmentislogin.create(2,BreathSetting.this);
                             fragmentislogin.show(getSupportFragmentManager(),"promptdsssio");
                             return;
                         }
                         if(b == 3|| b == 4){
                             fragmentislogin = Fragmentislogin.create(1,BreathSetting.this);
                             fragmentislogin.show(getSupportFragmentManager(),"promptdsssio");
                             return;
                         }
                    }
                    start();
                }
                break;
            case R.id.btn_right://个人中心
                startActivity(new Intent(this, ActivityMe.class));
                break;
            case R.id.iv_right_bpm://呼吸频率
                bpm++;
                if(!TextUtils.isEmpty(ID)){
                    if(bpm > 6){
                        bpm = 0;
                    }
                }else{
                    if(bpm > 6){
                        bpm = 3;
                    }
                }
                SPHelper.putDefaultInt(BreathSetting.this, SPHelper.KEY_BREATH_PER_MIN, Integer.parseInt(numbers[bpm]));
                SPHelper.putDefaultInt(BreathSetting.this, SPHelper.KEY_BREATH_PER_MIN_ITEM,bpm);
                tv_bpm.setText(getString(R.string.setting_bmp_format, numbers[bpm]));
//                 builder = new AlertDialog.Builder(
//                        BreathSetting.this);
//                builder.setTitle("呼吸频率");
//                builder.setItems(numbers, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        bpm = Integer.parseInt(numbers[which]);
//                        SPHelper.putDefaultInt(BreathSetting.this, SPHelper.KEY_BREATH_PER_MIN, bpm);
//                        tv_bpm.setText(getString(R.string.setting_bmp_format, numbers[which]));
//                    }
//                });
//                builder.show();
                break;
            case R.id.iv_left_bpm://呼吸频率
                bpm--;
                if(!TextUtils.isEmpty(ID)){
                    if(bpm < 0){
                        bpm = 6;
                    }
                }else{
                    if(bpm < 3){
                        bpm = 6;
                    }
                }
                SPHelper.putDefaultInt(BreathSetting.this, SPHelper.KEY_BREATH_PER_MIN, Integer.parseInt(numbers[bpm]));
                SPHelper.putDefaultInt(BreathSetting.this, SPHelper.KEY_BREATH_PER_MIN_ITEM,bpm);
                tv_bpm.setText(getString(R.string.setting_bmp_format, numbers[bpm]));
                break;
            case R.id.iv_right_duration:
                duration++;
                if(!TextUtils.isEmpty(ID)){
                    if(duration > 5){
                        duration = 0;
                    }
                }else{
                    if(duration > 1){
                        duration = 0;
                    }
                }
                SPHelper.putDefaultInt(BreathSetting.this, SPHelper.KEY_DURATION_item, duration);
                SPHelper.putDefaultInt(BreathSetting.this, SPHelper.KEY_DURATION,times[duration]);
                LogUtils.e("-----------------timesss" + times[duration]);
               tv_time.setText(getString(R.string.setting_duration_format, times[duration]));
//                builder = new AlertDialog.Builder(
//                        BreathSetting.this);
//                builder.setTitle("练习时间");
//                builder.setItems(numbers, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        duration = Integer.parseInt(times[which]);
//                        SPHelper.putDefaultInt(BreathSetting.this, SPHelper.KEY_DURATION, duration);
//                        tv_time.setText(getString(R.string.setting_duration_format, times[which]));
//                    }
//                });
//                builder.show();
                break;
            case R.id.iv_left_duration:
                duration--;
                if(!TextUtils.isEmpty(ID)){
                    if(duration < 0){
                        duration = 5;
                    }
                }else{
                    if(duration < 0){
                        duration = 1;
                    }
                }
                SPHelper.putDefaultInt(BreathSetting.this, SPHelper.KEY_DURATION, duration);
                SPHelper.putDefaultInt(BreathSetting.this, SPHelper.KEY_DURATION,times[duration]);
                LogUtils.e("-----------------timesaa" + times[duration]);
               tv_time.setText(getString(R.string.setting_duration_format, times[duration]));
                break;
            case R.id.iv_right_device:
                if(device == 0){
                    device = 1;
                }else {
                    device = 0;
                }
                SPHelper.putDefaultInt(BreathSetting.this, SPHelper.KEY_DEVICE, device);
                tv_type.setText(devices[device]);
//                builder = new AlertDialog.Builder(
//                        BreathSetting.this);
//                builder.setTitle("设备类型");
//                builder.setItems(devices, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        device = which;
//                        SPHelper.putDefaultInt(BreathSetting.this, SPHelper.KEY_DEVICE, device);
//                        tv_type.setText(devices[which]);
//                    }
//                });
//                builder.show();
                break;
            case R.id.iv_left_device:
                if(device == 0){
                    device = 1;
                }else {
                    device = 0;
                }
                SPHelper.putDefaultInt(BreathSetting.this, SPHelper.KEY_DEVICE, device);
                tv_type.setText(devices[device]);
                break;
            case R.id.layout_Scene://场景切换


                break;
            case R.id.iv_next:
                Intent intent = new Intent(BreathSetting.this, ActivityAbdominalR.class);
                startActivity(intent);
                break;
            case R.id.iv_syhead:

                Intent intent1 = new Intent(BreathSetting.this, ActivityHuxituna.class);
                startActivityForResult(intent1, RESULT_FIRST_USER);
                break;
            case R.id.tv_prompt_dio:
                pridio =  FragmentaPromptdio.create();
                pridio.show(getSupportFragmentManager(),"promptdio");
                break;
        }
    }



    private void start(){
        if(device==0){
            if(mBluetoothAdapter!=null){
                founded=true;
                mBluetoothAdapter.cancelDiscovery();
            }
            startActivity(new Intent(this, ActivityBreath.class));
        }else
        if(device==1){
            Util.showProgressFor(this, "正在查找蓝牙设备");
            initBlueTooth();
        }
    }

    private BluetoothAdapter mBluetoothAdapter;
    private boolean founded=false;
    private void initBlueTooth(){
//        founded=false;
//        unregisterBluetoothStateReceiver();
//        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        //  If the adapter is null, then Bluetooth is not supported
//        if (mBluetoothAdapter == null) {
//            //Bluetooth needs to be available on this device, and also enabled.
//            Toast.makeText(this, "此设备不支持蓝牙", Toast.LENGTH_LONG).show();
//            Util.cancelProgressFor(BreathSetting.this);
//        } else {
//            registerBluetoothStateReceiver();
//            // Everything should be good to go so let's try to connect to the HxM
//            if (!mBluetoothAdapter.isEnabled()) {
//                boolean result=mBluetoothAdapter.enable();//打开蓝牙
//                LogUtils.i("=="+"蓝牙打开结果=="+result);
//            } else {
//                LogUtils.i("=="+"startDiscovery==");
//                mBluetoothAdapter.startDiscovery();
//            }
//        }
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            return;
        }
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
        if(mBluetoothAdapter.isEnabled()){
            mBluetoothAdapter.disable();
        }
//        mBluetoothAdapter.enable();
//        if (mBluetoothAdapter.isEnabled()) {
//            scanLeDevice(true);
//        }
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, 999);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.e("lllll+++++onPause:");
//        unregisterBluetoothStateReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.e("lllll+++++onResume:");
//        registerBluetoothStateReceiver();
    }

//    private BluetoothStateReceiver bluetoothStateReceiver;
//    private void registerBluetoothStateReceiver(){
//        if(bluetoothStateReceiver==null){
//            bluetoothStateReceiver=new BluetoothStateReceiver();
//        }
//        IntentFilter filter=new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
//        filter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
//        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
//        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
//        filter.addAction(BluetoothDevice.ACTION_FOUND);
//        registerReceiver(bluetoothStateReceiver, filter);
//    }

//    private void unregisterBluetoothStateReceiver(){
//        if(bluetoothStateReceiver!=null){
//            unregisterReceiver(bluetoothStateReceiver);
//            bluetoothStateReceiver=null;
//        }
//    }
    /**监听蓝牙的打开关闭*/
//    public  class BluetoothStateReceiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            LogUtils.i("founded===" + founded+" action===" +intent.getAction());
//            if(intent.getAction().equals(BluetoothAdapter.ACTION_STATE_CHANGED)){
//                if(mBluetoothAdapter!=null&&mBluetoothAdapter.isEnabled()){
//                    if(!founded){
//                        if(bluetoothStateReceiver!=null){
//                            mBluetoothAdapter.startDiscovery();
//                        }
//                    }
//                }
//
//            }else if(intent.getAction().equals(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)){
//
//            }else if(intent.getAction().equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)){
////                mHandler.sendEmptyMessage(WhatBlueToothClose);
//            }else if(intent.getAction().equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)){
//                if(!founded){
//                   //mHandler.sendEmptyMessage(WhatNotFindBlueTooth);
//                    if(bluetoothStateReceiver!=null){
//                        unregisterBluetoothStateReceiver();
//                        handleNotFindBlueTooth();
//                    }
//                    //mBluetoothAdapter.startDiscovery();
//                }
//
//            }else if(intent.getAction().equals(BluetoothDevice.ACTION_FOUND)){//发现新的蓝牙设备
//                BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                LogUtils.i("founded...."+ "" + device.getName());
//                //正常情况这里判断下是否是我们需要的设备，是的话就建立连接
//                if(device!=null&&device.getName()!=null&&device.getName().startsWith("Berry")){
//                    founded=true;
//                    if(bluetoothStateReceiver!=null){
//                        try {
//                            Util.cancelProgressFor(BreathSetting.this);
//                            unregisterBluetoothStateReceiver();
//                            mBluetoothAdapter.cancelDiscovery();//找到自己需要的设备后应该停止搜索设备，
//                            if(prompt!=null){
//                                prompt.dismiss();
//                                prompt=null;
//                            }
//                            startActivity(new Intent(BreathSetting.this, ActivityBreath.class));
//                        }catch (Exception e){
//                            unregisterBluetoothStateReceiver();
//                        }
//                    }
//                }
//
//            }
//        }
//    }


    private FragmentDiaPrompt prompt;
    private FragmentBlueDiaPrompt fragmentBlueDiaPrompt;
    private void handleNotFindBlueTooth() {
        if(founded){
            return;
        }
        Util.cancelProgressFor(BreathSetting.this);
        if(prompt!=null&&prompt.isAdded()){
            return;
        }
        prompt= FragmentDiaPrompt.create(1);
        prompt.show(getSupportFragmentManager(), "bluetooth");
    }
    private  boolean isdowload = false;
    //更新场景
    public void updatedCJ(){
//        btn_start.setEnabled(false);
        Util.showProgressFor(this,getResources().getString(R.string.csscene));
        Http.getScene(this, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String v = responseInfo.result;
                        byte[] bytes = v.getBytes();
                        try {
                            v = new String(bytes, "UTF-8");
                            JSONObject object = new JSONObject(v);



                            int info = object.getInt("info");

                            if (info != 1) {
                                return;
                            }
                            scenebeans = GsonTools.fromJsonArray(object.getJSONArray("data").toString(), SceneBean.class);
                            //当前是只有一个场景的做法
                            final SceneBean sceneBean = scenebeans.get(0);
                            SPHelper.putDefaultInt(BreathSetting.this, "荷塘禅心total", sceneBean.getFilesize());
                            isdowload = true;//判断正在下载  用来判断弹窗
                            handler = http.download(sceneBean.getDownloadurl(), Environment.getExternalStorageDirectory() + "/HRVHTML/" + sceneBean.getLetter(),
                                    false, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
                                    false, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
                                    new RequestCallBack<File>() {
                                @Override
                                public void onSuccess(ResponseInfo<File> responseInfo) {
                                    isdowload = false;
                                    ImageView btn_left;

                                    tv_Scene.setText("荷塘禅心");
                                    Util.cancelProgressFor(BreathSetting.this);
                                    start();
                                    try {
                                        ZipUtils.upZipFile(new File(Environment.getExternalStorageDirectory() + "/HRVHTML/htcx.zip"), Environment.getExternalStorageDirectory() + "/HRVHTML");
//                                        //切换场景的时候切换对应数据，当前暂时放这
//                                        SPHelper.putDefaultString(getApplicationContext(), SPHelper.KEY_SCENE_HTML, sceneBean.getLetter());
//                                        SPHelper.putDefaultString(getApplicationContext(), SPHelper.KEY_SCENE_NAME, sceneBean.getName());
//                                        SPHelper.putDefaultString(getApplicationContext(), SPHelper.KEY_SCENE_JS, sceneBean.getStart_function());
//                                        SPHelper.putDefaultString(getApplicationContext(), SPHelper.KEY_SCENE_JSHX, sceneBean.getRate_function());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
//                                    btn_start.setEnabled(true);
                                }

                                @Override
                                public void onLoading(long total, long current, boolean isUploading) {
//                                    LogUtils.e("total:" + total + "------current：" + current);
//                                    SPHelper.putDefaultInt(BreathSetting.this, "荷塘禅心total", Integer.parseInt(total + ""));
                                    SPHelper.putDefaultInt(BreathSetting.this, "荷塘禅心current", Integer.parseInt(current+""));
//                                    Long l = current*100/total;
                                    int i = Integer.parseInt(current+"")*100;
                                    int j = Integer.parseInt(total + "");
//                                    LogUtils.e("chuchuchu+++"+Integer.parseInt(current+"")*100+"total++"+j+"totalss"+total);
                                    tv_Scene.setText("更新场景"+i/sceneBean.getFilesize()+"%");
                                    super.onLoading(total, current, isUploading);
                                }

                                @Override
                                public void onFailure(HttpException e, String s) {
                                    isdowload = false;
                                    Util.cancelProgressFor(BreathSetting.this);
//                                    btn_start.setEnabled(true);
                                    tv_Scene.setText("荷塘禅心");
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        Util.cancelProgressFor(BreathSetting.this);
                    }
                }
        );
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 999:
                if (resultCode == RESULT_OK) {
                    scanLeDevice(true);
                }else if (resultCode == RESULT_CANCELED) {
                    Util.cancelProgressFor(BreathSetting.this);
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
                break;
        }
        if(requestCode == RESULT_FIRST_USER){
            if(resultCode == RESULT_OK){
                if(isdowload){
                    Fragmentdowloadshow diaPrompt = new Fragmentdowloadshow();
                    diaPrompt.show(getSupportFragmentManager(),"promptdowload");
                    return;
                }
                File szip = new File(Environment.getExternalStorageDirectory() + "/HRVHTML/htcx.zip");
//                File szip = new File(Environment.getExternalStorageDirectory() + "/HRVHTML/htcx.zip");
                int total = SPHelper.getDefaultInt(BreathSetting.this, "荷塘禅心total",0);
                //判断第一个场景的zip是否下载过
                if (!szip.exists()) {
                    updatedCJ();
                }else
                if(szip.length()<total){
                    updatedCJ();
                }else{
                    start();
                }
            }
        }
    }
    private int SCAN_PERIOD = 3000;
    private Handler mHandler = new Handler();
    /**
     * 扫描是否结束
     */
    @SuppressWarnings("deprecation")
    public void scanLeDevice(final boolean enable) {
        if (mBluetoothAdapter == null) {
            return;
        }
        if (enable) {
            Util.showProgressFor(this, "设备查找中..");
            // Stops scanning after a pre-defined scan period.
            fragmentBlueDiaPrompt = FragmentBlueDiaPrompt.create();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Util.cancelProgressFor(getApplicationContext());
//                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    if (!mIsScanFinished) {
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                        handleNotFindBlueTooth();
                    }
                }
            }, SCAN_PERIOD);
            bledevice.clear();
            mBluetoothAdapter.startLeScan(mLeScanCallback);
            mIsScanFinished = false;
        } else {
            mIsScanFinished = true;
            Util.cancelProgressFor(getApplicationContext());
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            if (prompt != null) {
                prompt.dismiss();
                prompt = null;
            }
        }

    }
    /**
     * 扫描蓝牙设备的时间
     */
    private boolean mIsScanFinished;
    private int a = 0;
//    private ArrayList<String> blename = new ArrayList<String>();
    private ArrayList<BluetoothDevice> bledevice = new ArrayList<BluetoothDevice>();
    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    System.err.println("onLeScan====" + device.getName());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            blename.add(device.getName());
                            if (device.getName() != null && device.getName().startsWith("Berry")) {
                                boolean iscz = false;
                                for(int i=0;i<bledevice.size();i++){
                                    if(device.getAddress().equals(bledevice.get(i).getAddress())){
                                        iscz = true;
                                        break;
                                    }
                                }
                                if(iscz == false){
                                    bledevice.add(device);
                                }else {
                                    return;
                                }
//                            if(a == 0){
                                if(bledevice.size()==1){
                                    fragmentBlueDiaPrompt.show(getSupportFragmentManager(), "bluetodio");
                                }
                                fragmentBlueDiaPrompt.setList(BreathSetting.this, bledevice);
                                mIsScanFinished = true;
//                                if(mIsScanFinished){
//                                    return;
//                                }
//                                scanLeDevice(false);
//                                if (prompt != null) {
//                                    prompt.dismiss();
//                                    prompt = null;
//                                }
//                                BluetoothLeService.device_connect = device;
//                                startActivity(new Intent(BreathSetting.this, ActivityBreath.class));
                            }
                        }
                    });
                }
            };

    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), getString(R.string.exit),
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
