package com.sage.hedonicmentality.utils;

import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import com.lidroid.xutils.util.LogUtils;
import com.sage.hedonicmentality.app.Common;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sage on 2015/8/5.
 */
public class VolumeListener {

    /**更新话筒状态 分贝是也就是相对响度 分贝的计算公式K=20lg(Vo/Vi) Vo当前振幅值 Vi基准值为600：
     * Math.log10(mMediaRecorder.getMaxAmplitude() / Vi)==0的时候vi就是我所需要的基准值
     * 当我不对着麦克风说任何话的时候，测试获得的mMediaRecorder.getMaxAmplitude()值即为基准值。
     */
    public static final int WHAT_VOLUME=0x600;
    private int BASE=600;/**声音的基准值*/
    private int listenThreshold=500;/**声音监听的间隔时间*/
    private MediaRecorder mMediaRecorder;
    private boolean isStartRecord=true;
    private int mRecordVolume;
    private Handler handler;
    private Handler mHandler;
    public static final String PATH= Environment.getExternalStorageDirectory().getAbsolutePath()+"/"
            + Common.ROOT_DIR+"/"+Common.ROOT_AUDIO_DIR;
    public File recordFile;



    public VolumeListener(Handler handler) {
        mHandler=handler;
    }

    private Runnable listenRunnable=new Runnable() {
        @Override
        public void run() {
            if (mMediaRecorder != null && isStartRecord) {

                int amplitude = mMediaRecorder.getMaxAmplitude();
                int ratio = amplitude / BASE;
                int db = 0;// 分贝
                if (ratio > 1) {
                    db = (int) (20 * Math.log10(ratio));
                }
                LogUtils.i("updateMicStatus : amplitude = " + amplitude
                        + ", ratio = " + ratio + ", db = "
                        + db);
               if(mHandler!=null){
                   mHandler.obtainMessage(WHAT_VOLUME,db).sendToTarget();
               }
                if(handler!=null)
                    handler.postDelayed(this,listenThreshold);
            }
        }
    };
    public void startListen(){
        if(mMediaRecorder==null){
            mMediaRecorder=new MediaRecorder();
            createFile();
            if(recordFile==null){
                return;
            }
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            mMediaRecorder.setOutputFile(recordFile.getAbsolutePath());
            try {
                mMediaRecorder.prepare();
                mMediaRecorder.start();
            } catch (IOException e) {
                e.printStackTrace();
                mMediaRecorder.release();
                mMediaRecorder=null;
                return;
            }
        }
        HandlerThread thread=new HandlerThread("volume");
        thread.start();
        handler=new Handler(thread.getLooper());
        handler.removeCallbacksAndMessages(null);
        isStartRecord=true;
        handler.post(listenRunnable);
    }

    public void stopListen(){
        try {
            isStartRecord=false;
            if(handler!=null)
                handler.removeCallbacksAndMessages(null);
            if(mMediaRecorder!=null){
                mMediaRecorder.release();
                mMediaRecorder=null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(recordFile!=null){
            recordFile.deleteOnExit();
        }

    }

    private void createFile(){
        File file=new File(PATH);
        if(!file.exists()){
            boolean result=file.mkdirs();
            if(!result){
                return;
            }
        }
         String name=new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss_sss").format(new Date());
        try {
           recordFile=  File.createTempFile(name,".mp3",file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
