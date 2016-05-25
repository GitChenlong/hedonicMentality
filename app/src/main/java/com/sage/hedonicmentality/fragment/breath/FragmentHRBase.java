package com.sage.hedonicmentality.fragment.breath;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;
import com.lidroid.xutils.util.LogUtils;
import com.sage.hedonicmentality.fragment.BaseFragment;
import com.sage.hedonicmentality.service.BluetoothLeService;
import com.sage.hedonicmentality.service.ServiceBlueTooth;
import com.sage.hedonicmentality.utils.AsyncTaskListener;
import com.sage.hedonicmentality.utils.AsyncTaskRunner;
import com.sage.hedonicmentality.utils.ImageProcess;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Created by Sage on 2015/7/22.
 */
public abstract class FragmentHRBase extends BaseFragment implements AsyncTaskListener{

    public String TAG=FragmentHRBase.class.getSimpleName();
    public Handler mhandler;
    public Camera camera;
    public SurfaceHolder previewHolder;
    public int pace;//每分钟呼吸数bpm
    public int duration;//设定的测试时间，分钟，这两个记得初始化
    public int myCoherence;//心意合一程度
    public boolean finish=false;/**测试是否结束了*/
    public long intervalTime=0;/**时间差，就是中间断开的情况,如果断了2次，*/
    public long deltTime;/**单独一次断开的时间*/
    public long lastTime;/**最后一条数据获取的时间*/
    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseCamera();
        EventBus.getDefault().unregister(this);
        getActivity().stopService(new Intent(getActivity(), BluetoothLeService.class));
    }

    @Override
    public void onDestroyView() {
        if(mhandler!=null) {
            mhandler.removeCallbacksAndMessages(null);
        }
        super.onDestroyView();
    }

    public SurfaceView preview;
    /**如果调用相机的话，初始化相机的数据*/
    public void initCamera(SurfaceView preview){
        initHandler();
        this.preview=preview;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            Toast.makeText(getActivity(),"请开启相机权限",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        preview.setVisibility(View.VISIBLE);
         previewHolder = preview.getHolder();
        previewHolder.addCallback(surfaceCallback);
        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
    //退出的时候释放camera；
    public void releaseCamera(){
        //getActivity().stopService(new Intent(getActivity(), ServiceBlueTooth.class));
        if(camera!=null){
            try {
                camera.setPreviewCallback(null);
                camera.stopPreview();
                camera.release();
                camera = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
//    /**如果调用蓝牙的话，启动服务*/
//    public void initBlueTooth(SurfaceView preview){
//        preview.setVisibility(View.GONE);
//        initHandler();
//        ServiceBlueTooth.mHandler = mhandler;
//        //getActivity().stopService(new Intent(getActivity(), ServiceBlueTooth.class));
//        getActivity().startService(new Intent(getActivity(), ServiceBlueTooth.class));
//    }
    /**如果调用蓝牙的话，启动服务*/
    public void initBlueTooth(SurfaceView preview){
        preview.setVisibility(View.GONE);
        initHandler();
        EventBus.getDefault().register(this);
        getActivity().startService(new Intent(getActivity(), BluetoothLeService.class));
        //ServiceBlueTooth.mHandler = mhandler;
        //getActivity().stopService(new Intent(getActivity(), ServiceBlueTooth.class));
        //getActivity().startService(new Intent(getActivity(), ServiceBlueTooth.class));
    }

    public SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            LogUtils.i("surfaceCreated==");
            try {
//                if(previewHolder==null){
//                    previewHolder=preview.getHolder();
//                }
                camera.setPreviewDisplay(previewHolder);
                camera.setPreviewCallback(previewCallback);
            } catch (Exception e) {
                Toast.makeText(getActivity(),"请开启相机权限",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            LogUtils.i( "surfaceChanged==");
            try {
                startCamera();
            } catch (Exception e) {
                Toast.makeText(getActivity(),"请开启相机权限",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            LogUtils.i("surfaceDestroyed==");
            previewHolder=null;
        }
    };
    /***启动相机开始获取数据*/
    private void startCamera(){
        Camera.Parameters parameters = camera.getParameters();
        Camera.Size size = getSmallestPreviewSize(200, 200, parameters);
        if (size != null) {
            parameters.setPreviewSize(size.width, size.height);
            LogUtils.i( "Using width=" + size.width + " height=" + size.height);
        }
        List<String> flashModes = parameters.getSupportedFlashModes();
        String flashMode = parameters.getFlashMode();
        if (!Camera.Parameters.FLASH_MODE_TORCH.equals(flashMode)) {

            if (flashModes.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            } else {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
            }
        }

        String manuName = android.os.Build.MANUFACTURER.toLowerCase();
        if (manuName.contains("huawei")) {
            camera.startPreview();
            camera.setParameters(parameters);
        } else {
            camera.setParameters(parameters);
            camera.startPreview();
        }

    }
    /***获取相机支持的最低分辨率*/
    private Camera.Size getSmallestPreviewSize(int width, int height, Camera.Parameters parameters) {
//        Camera.Size result = null;
//
//        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
//            LogUtils.i( "PreviewSizes width= " + size.width + " height=" + size.height);
//            if (size.width <= width && size.height <= height) {
//                if (result == null) {
//                    result = size;
//                } else {
//                    int resultArea = result.width * result.height;
//                    int newArea = size.width * size.height;
//
//                    if (newArea < resultArea) result = size;
//                }
//            }
//        }
//
        List<Camera.Size> list=parameters.getSupportedPreviewSizes();
        if(list.size()>1){
            Camera.Size size1=list.get(0);
            Camera.Size size2=list.get(list.size()-1);
            if(size1.width*size1.height>size2.width*size2.height){
                return size2;
            }else{
                return size1;
            }
        }else{
            return list.get(0);
        }
    }

    //private AtomicBoolean processing = new AtomicBoolean();
    private static final int averageArraySize = 4;
    private static final int[] averageArray = new int[averageArraySize];
    private int averageIndex;
    private int beats;/**每10秒钟的心跳数*/


    private static final int averageBpmArraySize = 4;
    private static final float[] averageBpmArray = new float[averageBpmArraySize];
    private static int averageBpmIndex = 0;
    private int beatsIndex;

    private static final int beatsArraySize = 3;
    private static final int[] beatsArray = new int[beatsArraySize];

    private boolean state=false;

    private int beatsAvg;//心跳平均值
    private long previousBeatTime;//上一次心跳时间
    //private long beatTime;//当前心跳时间
    private boolean  guideStarted=false;
    private long preHeartTime;//上一次心跳时间

    private long noFingerTime=0;
    private long lastDataTime=0;
    private static final AtomicBoolean processing = new AtomicBoolean(false);
    private boolean finger=false;/**根据红色值的大小来判断手指是否按在摄像头上*/
    private long timerTime;//计时
    private boolean timer=false;
    private int count=0;
    private Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {

        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {

            //synchronized (camera) {
                if (data == null) {
                    LogUtils.i( "onPreviewFrame data ====null");
                    return;
                }
                Camera.Size size = camera.getParameters().getPreviewSize();
                if (size == null) {
                    LogUtils.i("onPreviewFrame size ====null");
                    return;
                }
//            if(!timer){
//                count++;
//                if(timerTime==0){
//                    timerTime=System.currentTimeMillis();
//
//                }else{
//                    if(System.currentTimeMillis()-timerTime>5000){
//                        timer=true;
//                        LogUtils.i(Build.MODEL+"******="+count);
//                    }
//                }
//            }


           // if (!processing.compareAndSet(false, true)) return;
                int imgAvg = ImageProcess.decodeYUV420SPtoRedAvg(data, size.width, size.height, 2);
                LogUtils.i("imgAvg...==" +imgAvg );

                mhandler.obtainMessage(What_CameraDATA,imgAvg,0,System.currentTimeMillis()).sendToTarget();
            }

       // }
    };
    /**数据暂停  界面变成等待状态*/
    public void DataSuspend(boolean ishow){

    }
    public static final int What_CameraDATA=0x555;
;    private void handleCameraData(int imgAvg,long currentTime){
        LogUtils.i("receive data...="+imgAvg);
        if (imgAvg < 2000) {
            if (sessionStartTime != 0) {
                if (noFingerTime == 0) {
                    noFingerTime = currentTime;
                }
            }
            processing.set(false);
            DataSuspend(true);
            return;
        }
            DataSuspend(false);
        if (noFingerTime != 0) {
            deltTime += (currentTime - noFingerTime);
            intervalTime += (currentTime - noFingerTime);
//                Log.i("test...","interval="+intervalTime+" delt="+deltTime+" nofinger="+noFingerTime);
            noFingerTime = 0;
        }
        int averageArraySum = 0;
        int averageArrayCount = 0;
        for (int i = 0; i < averageArray.length; i++) {
            if (averageArray[i] > 0) {
                averageArraySum += averageArray[i];
                averageArrayCount++;
            }
        }

        int rollingAverage = (averageArrayCount > 0) ? (averageArraySum / averageArrayCount) : 0;
        //LogUtils.i("imgAvg==" + imgAvg + " rollingAverage==" + rollingAverage);
        final float durationTime;

        if (sessionStartTime == 0) {
            sessionStartTime = previousBeatTime = currentTime;
        }
        if (imgAvg < rollingAverage) {
            if (!state) {
                try {
                    beats++;
                    if (preHeartTime == 0) {
                        preHeartTime = currentTime;
                    }
//                        else{
                    durationTime = (float) (currentTime - sessionStartTime - intervalTime) / 1000;
                    //add average logic for last three plus itself, if its value <40 or >150
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showTime((long) (durationTime * 1000));
                        }
                    });

                    if (durationTime > duration * 60) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                finishTest();
                            }
                        });

                        return;
                    }
                     //LogUtils.i("abc=" + durationTime+ " beatsAvg==" + beatsAvg + " previousBeatTime=" + previousBeatTime);
                    float bpmval = (float) 60 / ((float) (currentTime - previousBeatTime) / (1000.0f));
                    previousBeatTime = currentTime;
                    //only start curve after heart beat was detected.s
                    if (beatsAvg >= 0) {
                        //private static int averageBpmIndex = 0;
                        //private static final int averageBpmArraySize = 4;
                        //private static final float[] averageBpmArray = new float[averageBpmArraySize];
                        if (bpmval < 35 || bpmval > 100) {
                            bpmval = beatsAvg == 0 ? 70 : beatsAvg;
                        }

                        //if (averageBpmIndex == averageBpmArraySize) averageBpmIndex = 0;
                        averageBpmIndex = averageBpmIndex % averageBpmArraySize;
                        averageBpmArray[averageBpmIndex] = bpmval;
                        averageBpmIndex++;

                        int beatsBpmArraySum = 0;
                        int beatsBpmArrayCount = 0;
                        for (int i = 0; i < averageBpmArray.length; i++) {
                            if (averageBpmArray[i] > 0) {
                                beatsBpmArraySum += averageBpmArray[i];
                                beatsBpmArrayCount++;
                            }
                        }
                        final float bpmAvg = (beatsBpmArraySum / (beatsBpmArrayCount > 0 ? beatsBpmArrayCount : 1));
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                addBeat(durationTime, bpmAvg);
                                updateScoreSession(durationTime);
                            }
                        });
                        bgCalculate(durationTime,bpmAvg);
                        if (!guideStarted) {
                            //LogUtils.i("+++++++"+"startAnim++++++++++++");
                            guideStarted = true;
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    startShow();
                                }
                            });

                        }
                    }
                    //  }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            state = true;
        } else if (imgAvg > rollingAverage) {
            state = false;
        }

        try {
            //if (averageIndex == averageArraySize) averageIndex = 0;
            averageIndex = averageIndex % averageArraySize;
            averageArray[averageIndex] = imgAvg;
            averageIndex++;

            double totalTimeInSecs = (currentTime - preHeartTime - deltTime) / 1000d;
            if (totalTimeInSecs >= 10) {
                deltTime = 0;
                preHeartTime = currentTime;
                int bpm = (int) (beats * 60 / totalTimeInSecs);
                //LogUtils.i( "totalTimeInSecs=" + totalTimeInSecs + " beats=" + beats);
                if (bpm < 30 || bpm > 180) {
                    beats = 0;
                    processing.set(false);
                    return;
                }

                if (beatsIndex == beatsArraySize) beatsIndex = 0;
                beatsArray[beatsIndex] = bpm;
                beatsIndex++;

                int beatsArraySum = 0;
                int beatsArrayCount = 0;
                for (int i = 0; i < beatsArray.length; i++) {
                    if (beatsArray[i] > 0) {
                        beatsArraySum += beatsArray[i];
                        beatsArrayCount++;
                    }
                }
                beatsAvg = (beatsArraySum / (beatsArrayCount > 0 ? beatsArrayCount : 1));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showBeatsAvg(beatsAvg);
                    }
                });

                beats = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        processing.set(false);
    }

    private void initHandler(){
        HandlerThread thread=new HandlerThread("handleData");
        thread.start();
        mhandler = new Handler(thread.getLooper()) {

            @Override
            public void handleMessage(Message msg) {

                super.handleMessage(msg);
                switch (msg.what) {
                    case What_CameraDATA:
                        LogUtils.i("cameraData....==" +msg.arg1 );
                        handleCameraData(msg.arg1, (Long) msg.obj);
                        break;
                    case ServiceBlueTooth.WhatException:
                        LogUtils.i("WhatException----"+"++++");
                        handleException();
                        break;
                    case ServiceBlueTooth.WhatBlueToothClose:
                        LogUtils.i("WhatBlueToothClose----------"+"++++");
                        handleBlueToothClose();
                        break;
                    case ServiceBlueTooth.WhatNotFindBlueTooth:
                        LogUtils.i("WhatNotFindBlueTooth--"+"+++");
                        handleNotFindBlueTooth();
                        break;
                    case ServiceBlueTooth.WhatConnectedFailed:
                        LogUtils.i("WhatConnectedFailed---"+"+++");
                        handConnectedFailed();
                        break;
                    case ServiceBlueTooth.WhatConnectSucess:
                        LogUtils.i("WhatConnectSuccess---"+"+++");

                        break;
                    case ServiceBlueTooth.READ_DATA:
                        int hr = msg.arg1;
                        //int so2 = msg.arg2;
                        LogUtils.i("bluetooth,READ_DATA---------"+"------------"+hr);
                        if(finish){
                            System.err.println("finish.....");
                            return;
                        }
                        if(sessionStartTime==0){
                            sessionStartTime=System.currentTimeMillis();
                            lastTime=System.currentTimeMillis();
                        }else {
                            //intervalTime+=System.currentTimeMillis()-lastTime;
                        }
                        if(System.currentTimeMillis()-lastTime>2*1000){
                            intervalTime+=(System.currentTimeMillis()-lastTime);
                        }

                        try {
                            lastTime=System.currentTimeMillis();
                            final float durationTime = (float) (System.currentTimeMillis() - sessionStartTime-intervalTime) / 1000;
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showTime((long) (durationTime * 1000));
                                }
                            });

                            if (durationTime > duration * 60) {
                                finish=true;
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        finishTest();
                                    }
                                });

                                //return;
                            }
                            if (hr < 35 || hr > 100) {
                                hr = (beatsAvg==0?70:beatsAvg);
                            }
                            final float now_hr=hr;
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    addBeat(durationTime, now_hr);
                                }
                            });
                            bgCalculate(durationTime,hr);

                            if (beatsIndex == beatsArraySize) beatsIndex = 0;
                            beatsArray[beatsIndex] = hr;
                            beatsIndex++;
                            int beatsArraySum = 0;
                            int beatsArrayCount = 0;
                            for (int i = 0; i < beatsArray.length; i++) {
                                if (beatsArray[i] > 0) {
                                    beatsArraySum += beatsArray[i];
                                    beatsArrayCount++;
                                }
                            }
                            if(beatsArrayCount>0)
                                beatsAvg = (beatsArraySum / beatsArrayCount);

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showBeatsAvg(beatsAvg);
                                    updateScoreSession(durationTime);
                                }
                            });
                            if (!guideStarted) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        startShow();
                                    }
                                });

                                guideStarted = true;
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        break;
                }
            }
        };
    }


//    private void handleException() {
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                showAlert();
//            }
//        });
//
//    }
//    @Subscriber(tag = BluetoothLeService.TAG_DISCONNECTED)
    private void handleException() {
        LogUtils.e("++++++++++++onConnectionStatsssss++++++++++++");
        DataSuspend(true);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showAlert();
            }
        });

    }
    private void handleBlueToothClose() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showAlert();
            }
        });

    }

    private void handConnectedFailed() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showAlert();
            }
        });
    }

    private void handleNotFindBlueTooth() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showAlert();
            }
        });


    }
    @Subscriber(tag = BluetoothLeService.TAG_DISCONNECTEDS)
    private void handleDisconnected(int i){
        LogUtils.e("++++++++++++onConnectionStatsssss++++++++++++");
        DataSuspend(true);
//        showAlert();
    }


    @Subscriber(tag = BluetoothLeService.TAG_SPO2_DATA_AVAILABLE)
    private void handleData(int hr){
        LogUtils.i("bluetooth,READ_DATA---------" + "------------" + hr);
        DataSuspend(false);
        if(finish){
            System.err.println("finish.....");
            return;
        }
        if(sessionStartTime==0){
            sessionStartTime=System.currentTimeMillis();
            lastTime=System.currentTimeMillis();
        }else {
            //intervalTime+=System.currentTimeMillis()-lastTime;
        }
        if(System.currentTimeMillis()-lastTime>2*1000){
            intervalTime+=(System.currentTimeMillis()-lastTime);
        }

        try {
            lastTime=System.currentTimeMillis();
            final float durationTime = (float) (System.currentTimeMillis() - sessionStartTime-intervalTime) / 1000;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showTime((long) (durationTime * 1000));
                }
            });

            if (durationTime > duration * 60) {
                finish=true;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finishTest();
                    }
                });

                //return;
            }
            if (hr < 35 || hr > 100) {
                hr = (beatsAvg==0?70:beatsAvg);
            }
            final float now_hr=hr;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    addBeat(durationTime, now_hr);
                }
            });
            bgCalculate(durationTime,hr);

            if (beatsIndex == beatsArraySize) beatsIndex = 0;
            beatsArray[beatsIndex] = hr;
            beatsIndex++;
            int beatsArraySum = 0;
            int beatsArrayCount = 0;
            for (int i = 0; i < beatsArray.length; i++) {
                if (beatsArray[i] > 0) {
                    beatsArraySum += beatsArray[i];
                    beatsArrayCount++;
                }
            }
            if(beatsArrayCount>0)
                beatsAvg = (beatsArraySum / beatsArrayCount);

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showBeatsAvg(beatsAvg);
                    updateScoreSession(durationTime);
                }
            });


            if (!guideStarted) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startShow();
                    }
                });

                guideStarted = true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**蓝牙异常的处理*/
    public void showAlert(){

    }
    private int mindex = 0;
    private int hrSize = 800;
    private float[][] hrData = new float[hrSize][2];/**保留每次心跳以及对应的时间戳*/
    public  long sessionStartTime;//获取第一条数据的时间，也就是我们开始计时的时间

    public long lastFiveMinTime;/**上一次计算的时间戳，每5秒计算一次*/
//    public long getSessionStartTime() {
//        return sessionStartTime;
//    }
//
//    public void setSessionStartTime(long sessionStartTime) {
//        this.sessionStartTime = sessionStartTime;
//    }

    /**已经测试的时间，调用这个方法实时更新时间的显示*/
    public  void showTime(long time){
       // elapsetime.setText(new SimpleDateFormat("mm:ss").format(new Date(time)));
    }
    /**@param durationTime  时间，单位秒
     * @param bpmval 每分钟呼吸数*/
    public void addBeat(float durationTime,float bpmval){
//        bpmview.addBeat(durationTime, bpmval);
//        bpmview.postInvalidate();
//        score.setText(Integer.toString(bpmview.getScore()));

    }

    private void bgCalculate(float durationTime,float bpmval){
        hrData[mindex][0] = bpmval;
        hrData[mindex][1] = System.currentTimeMillis() - sessionStartTime;
        LogUtils.i("time=" + hrData[mindex][1] + "start time=" + new SimpleDateFormat("mm:ss").format(new Date(sessionStartTime)));
        mindex = (mindex + 1) % hrSize;
        LogUtils.e("-----"+mindex+"----System.currentTimeMillis():"+System.currentTimeMillis()+"----bpmval:"+bpmval);
        if((System.currentTimeMillis() - sessionStartTime) > 60000){
            if (lastFiveMinTime == 0 || (System.currentTimeMillis() - lastFiveMinTime) > 5 * 1000) {
                //计算所有时间的SDNN
                float SumSDNN = SumSDNN(hrData,mindex);
                LogUtils.e("+++++++++++++++++++++++++++SunSDNN:"+SumSDNN);

                calculate(findStartIndex(durationTime, bpmval, 1, mindex), (mindex + hrSize - 1) % hrSize, bpmval, SumSDNN);
                lastFiveMinTime = System.currentTimeMillis();
            }
        }

    }
    private void calculate(int startIndex, int endIndex, float hr,float sumsdnn) {
        LogUtils.i("index..........."+"start=" + startIndex + " endIndex=" + endIndex);
        try {
            AsyncTaskRunner thread = new AsyncTaskRunner(this, startIndex, endIndex, hr, hrData, hrSize,sumsdnn);
            thread.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /***设定的测试时间到了，要做的处理，比如关闭页面，弹出对话框等等*/
    public void finishTest(){

    }
    /**@param  timeSessionSecond second
     * 练习的时候用来更新分数和心意合一程度*/
   public void  updateScoreSession(double timeSessionSecond){
//       if (mode == 2) {
//           double percentage = (double) (bpmview.getScore()) / ((timeSessionSecond / 60.0) * (pace / 1.0) * 2);
//           if (percentage > 1) percentage = 1;
//           myCoherence = (int) (percentage * 100);
//           coherence.setText(String.format("%.0f", percentage * 100) + "%");
//       }
    }
    /**获取第一条数据，开启动画或者别的操作*/
    public void startShow(){
        //fragmentBreathAnim.startAnim();
    }
    /**算出每分钟心跳平均值后用来显示用的*/
    public void showBeatsAvg(int beatsAvg){
       // heartrateValue.setText(String.valueOf(beatsAvg));
    }




    private int findStartIndex(float durationTime, float bpmval, int minutes, int eIndex) {

        int sIndex = 0;
        long currentTime = System.currentTimeMillis() - sessionStartTime;
        int backCount = (int) (0.7 * minutes * 60 * 60 / bpmval); //use 70% of possible number of heart beat for last minutes
        //
        int guessStartCnt;//=(eIndex-backCount-1)>0?(eIndex-backCount-1):(eIndex-1);
        if (eIndex - backCount - 1 >= 0)
            guessStartCnt = eIndex - backCount - 1;
        else
            guessStartCnt = eIndex - backCount - 1 + hrSize;
        try {
            if (currentTime - hrData[guessStartCnt][1] > minutes * 60000) {
                //go forward from guessStartCnt search for start point
                while ((currentTime - hrData[guessStartCnt][1]) > 60000)
                    guessStartCnt = (guessStartCnt + 1) % hrSize;
                guessStartCnt = (hrSize + guessStartCnt - 1) % hrSize; //go back 1
                sIndex = guessStartCnt;
            } else {
                //going backward to search for start point
                while (guessStartCnt >= 0 && (currentTime - hrData[guessStartCnt][1]) < minutes * 60000)
                    guessStartCnt = (hrSize + guessStartCnt - 1) % hrSize;
                guessStartCnt = (guessStartCnt + 1) % hrSize; //go forward 1
                sIndex = guessStartCnt;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtils.i("-------"+sIndex);
        return sIndex;
    }
    //计算总时间的SDNN
    public float SumSDNN(float [][] data,int recordCount){

        float SDNN = 0;
        float NNMean=0;
        float HRMean=0;
        float hrsum=0;
        float nn=0;
        float nnsum=0;
        float hr=0;
        float HRMax=0;
        float NNMax=0;

        //hrsum等于所有的hr值的和
        for(int k=0; k<recordCount;k++){
            hrsum +=data[k][0];
            nn=(float)60.0/data[k][0];
            nnsum +=nn;
            if(nn>NNMax)NNMax=nn;
            if(data[k][0]>HRMax)HRMax=data[k][0];
        }
        HRMean = hrsum/recordCount;
        NNMean = 60/HRMean;
        float sdnnsum=0;

        for(int k=0; k<recordCount;k++){
            sdnnsum += (60/data[k][0]-NNMean)*(60/data[k][0]-NNMean);
        }
        SDNN =1000*(float)Math.sqrt(sdnnsum/recordCount);
        return SDNN;
    }
}
