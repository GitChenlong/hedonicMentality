package com.sage.hedonicmentality.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.widget.Toast;
import com.lidroid.xutils.util.LogUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Administrator on 2015/7/7.
 */
public class ServiceBlueTooth extends Service {
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private String TAG=getClass().getSimpleName();
    private BluetoothAdapter mBluetoothAdapter;
    private String macAddress;
    private boolean founded=false;//是否找到我们需要的设备
    private boolean isConnected=false;//蓝牙链接状态
    private Handler getDataHandler;
    public static  Handler mHandler;

    public static final int WhatNotFindBlueTooth=0x12;//搜索一次没搜索到设备，给予提示，然后继续搜索
    public static final int READ_DATA=0x123;//获取到数据以后传递给UI更新
    public static final int WhatException=0x1234;//获取数据的时候发生异常
    public static final int WhatConnectedFailed=0x12345;//获取到蓝牙，连接的时候失败了。
    public static final int WhatBlueToothClose=0x123456;//蓝牙被关闭了。
    public static final int WhatConnectSucess=0x1234567;//连接成功，获取输入流，开始读取蓝牙数据，通知开始计时
    @Override
    public void onCreate() {
        super.onCreate();
        initHandler();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.i("========"+"onStartCommand");
        if(!founded)
            initBlueTooth();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(getDataHandler!=null){
            getDataHandler.removeCallbacksAndMessages(null);
            getDataHandler=null;
        }
        if(mHandler!=null){
            mHandler.removeCallbacksAndMessages(null);
            mHandler=null;
        }
        unregisterBluetoothStateReceiver();
        disConnected();
        LogUtils.i("onDestroy.."+"stop service");
    }

    private void initHandler(){
        if(getDataHandler==null){
            HandlerThread thread=new HandlerThread("getData");
            thread.start();
            getDataHandler=new Handler(thread.getLooper());
        }
    }
    private void initBlueTooth(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //  If the adapter is null, then Bluetooth is not supported

        if (mBluetoothAdapter == null) {
            //Bluetooth needs to be available on this device, and also enabled.
            Toast.makeText(this, "Bluetooth is not available or not enabled", Toast.LENGTH_LONG).show();

        } else {
            registerBluetoothStateReceiver();
            // Everything should be good to go so let's try to connect to the HxM

            if (!mBluetoothAdapter.isEnabled()) {
 //               LogUtils.i("==", "蓝牙没有打开,跳到蓝牙打开界面");
//                        Intent intent=new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
//                        startActivity(intent);
                //我们可以直接打开蓝牙不用询问用户，需要相应的权限android.permission.BLUETOOTH_ADMIN
                    boolean result=mBluetoothAdapter.enable();//打开蓝牙
                LogUtils.i("=="+"蓝牙打开结果=="+result);
                // mBluetoothAdapter.disable();//关闭蓝牙
            } else {
                // mStatus.setText(R.string.connecting);
                macAddress = mBluetoothAdapter.getAddress();
                LogUtils.i("macAddress=="+macAddress);

                //先获取已经配对过的设备列表，看有没有我们需要的设备
                Set<BluetoothDevice> set= mBluetoothAdapter.getBondedDevices();

                if(set!=null&&set.size()>0){
                    for(BluetoothDevice device:set){
                        String address=device.getAddress();
                        String name=device.getName();
                        int state=device.getBondState();
                        LogUtils.i("device======="+name+"=="+address+"=="+state);
                        //我们可以选择一个连接，这里就选择第一个好了
                        if(name!=null&&name.startsWith("Berry")){
                            founded=true;
                            connetBluetooth(device);
                            break;
                        }
                    }
                }
                if(!founded){//没有找到我们需要的设备，开启搜索蓝牙设备
                    mBluetoothAdapter.startDiscovery();
                }
            }
        }
    }
    private BluetoothDevice Connectdevice;
    private BluetoothSocket mSocket;
    /**获取蓝牙设备以后的操作*/
    private void connetBluetooth(BluetoothDevice device) {
        LogUtils.i("try connect device...."+"device=="+device.getName());
        Connectdevice=device;
       // getDataHandler.post(connecteRunnable);
        try {
            LogUtils.i("getSocket"+"start=======");
            mSocket=  Connectdevice.createRfcommSocketToServiceRecord(MY_UUID);
            mSocket.connect();
        } catch (IOException e) {
            LogUtils.i("Get socket exception.=====connected failed, try again startDiscovery");
            e.printStackTrace();
            sendHandle(WhatConnectedFailed);
            founded=false;
            mBluetoothAdapter.startDiscovery();
            return;
        }
        LogUtils.i("getSocket"+"connect success=======");
        getDataHandler.post(runnable);
    }
    private static int averageBpmIndex = 0;
    private static final int averageBpmArraySize = 3;
    private static final float[] averageBpmArray = new float[averageBpmArraySize];
    private boolean go=true;
    InputStream inputStream=null;
    private Runnable runnable=new Runnable() {
        @Override
        public void run() {
            try {
                 inputStream=mSocket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
                LogUtils.i("inputStream....null");
            }
            if(inputStream==null){
                LogUtils.i("inputStream"+ "==========null");
                ExceptionHandle();
                founded=false;
                mBluetoothAdapter.startDiscovery();
                return;
            }
            isConnected=true;
            int b;
            int bufferIndex=0;
            byte[] buffer=new byte[1024];
            int preWaveValue=-1;
            int updown=0;
            long peakTime;
            long prePeakTime=-1;
            double hr;
            int so2;
            LogUtils.i("start while..."+"==================");
            sendHandle(WhatConnectSucess);
            int count=0;
            go=true;
            while (go){
                try {
                    b=inputStream.read();
                    if(b>128){
                        bufferIndex = 0;
                    }
                    buffer[bufferIndex]= (byte) b;
                    if(bufferIndex==2){
                        if(preWaveValue<0){
                            preWaveValue=b;
                        }
                        if(b>preWaveValue){
                            updown=1;
                        }else if(b<preWaveValue){
                            if(updown==1){//previous is up and now it is down, peak value detected
                                peakTime=System.currentTimeMillis();
                                if(prePeakTime<0){
                                    prePeakTime=peakTime;
                                }else{
                                    hr=(1000*60.00)/(peakTime-prePeakTime);
                                    averageBpmIndex=averageBpmIndex%averageBpmArraySize;
                                    averageBpmArray[averageBpmIndex]= (float) hr;
                                    averageBpmIndex++;
                                    float beatsBpmArraySum = 0;
                                    int beatsBpmArrayCount = 0;
                                    for (int i = 0; i < averageBpmArray.length; i++) {
                                        if (averageBpmArray[i] > 0) {
                                            beatsBpmArraySum += averageBpmArray[i];
                                            beatsBpmArrayCount++;
                                        }
                                    }
                                    hr = (beatsBpmArraySum / beatsBpmArrayCount);
                                    so2 = buffer[4];

                                    if(mHandler!=null){
                                        mHandler.obtainMessage(READ_DATA,(int)hr,so2,peakTime).sendToTarget();
                                        LogUtils.i("bluetooth"+ "hr==" + hr + " so2==" + so2);
                                    }else{
                                        LogUtils.i( "mHandler null............");
                                    }
                                        prePeakTime = peakTime;

                                }
                            }
                            updown=0;
                        }
                        preWaveValue=b;
                    }

//                    if(bufferIndex==1){
//                        if(preBarValue<0)preBarValue=b;
//                    }
                    bufferIndex++;

                } catch (IOException e) {
                    e.printStackTrace();
                    count++;
                    if(count>0){
                        if(go){
                            sendHandle(WhatException);
                            founded=false;
                            mBluetoothAdapter.startDiscovery();
                        }else{
                           disConnected();
                        }
                        break;
                    }
                }
            }
        }
    };

    /**处理连接异常*/
    private void ExceptionHandle(){
        disConnected();
        sendHandle(WhatException);
    }
    private void disConnected(){
        founded=false;
        go=false;
        if(inputStream!=null){
            try {
                inputStream.close();
                inputStream=null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(mSocket!=null){
            try {
                mSocket.close();
                mSocket=null;
                LogUtils.i("++++++++"+"socket close..");
            } catch (IOException e) {
                e.printStackTrace();
                LogUtils.i("++++++++"+ "socket close..failed");
            }
        }
    }

    private BluetoothStateReceiver bluetoothStateReceiver;
    private void registerBluetoothStateReceiver(){
        if(bluetoothStateReceiver==null){
            bluetoothStateReceiver=new BluetoothStateReceiver();
        }
        IntentFilter filter=new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED );
        filter.addAction(BluetoothDevice.ACTION_FOUND);
       registerReceiver(bluetoothStateReceiver, filter);
    }

    private void unregisterBluetoothStateReceiver(){
        if(bluetoothStateReceiver!=null){
            unregisterReceiver(bluetoothStateReceiver);
            bluetoothStateReceiver=null;
        }
    }
    /**监听蓝牙的打开关闭*/
    public  class BluetoothStateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.i("founded==="+founded+"action="+intent.getAction());
            if(intent.getAction().equals(BluetoothAdapter.ACTION_STATE_CHANGED)){
                if(mBluetoothAdapter!=null&&mBluetoothAdapter.isEnabled()){
                    if(!founded){
                        mBluetoothAdapter.startDiscovery();
                    }
                }

            }
//            else if(intent.getAction().equals(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)){
//
//            }
            else if(intent.getAction().equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)){
                sendHandle(WhatBlueToothClose);
            }else if(intent.getAction().equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)){
                if(!founded){
                    sendHandle(WhatNotFindBlueTooth);
                    mBluetoothAdapter.startDiscovery();
                }

            }else if(intent.getAction().equals(BluetoothDevice.ACTION_FOUND)){//发现新的蓝牙设备
                BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                LogUtils.i("founded...."+""+device.getName());
                //正常情况这里判断下是否是我们需要的设备，是的话就建立连接
                if(device.getName()!=null&&device.getName().startsWith("Berry")){
                    founded=true;
                    mBluetoothAdapter.cancelDiscovery();//找到自己需要的设备后应该停止搜索设备，
                    connetBluetooth(device);
                }

            }
        }
    }


    private  void sendHandle(int what){
        if(mHandler!=null){
            mHandler.sendEmptyMessage(what);
        }
    }
}
