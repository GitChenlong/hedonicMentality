package com.sage.hedonicmentality.utils;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import java.util.LinkedList;

/**
 * Created by Sage on 2015/8/5.
 */
public class AudioListener  {

    static final int frequency = 44100;
    static final int channelConfiguration = AudioFormat.CHANNEL_OUT_DEFAULT;
    static final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
    private final int socketVol = 2000;
    private final int cycle = 8;
    protected AudioRecord audioRecord;
    protected int bufferSize;
    protected byte[] buffer;
    protected boolean m_keep_running;
    protected LinkedList<byte[]> m_in_q;
    private int volTime = 0;
    private boolean flagVol = false;


    private Handler handler;
    private Handler mHandler;

    public AudioListener(Handler handler) {
        mHandler=handler;
        bufferSize = AudioRecord.getMinBufferSize(frequency,
                channelConfiguration, audioEncoding) * 2;

        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                8000, channelConfiguration, audioEncoding,
                bufferSize);

        buffer = new byte[bufferSize];

        m_keep_running = true;
        m_in_q = new LinkedList<>();
    }

    public void start() {

        if(handler==null){
            HandlerThread thread=new HandlerThread("audio");
            thread.start();
            handler=new Handler(thread.getLooper());
        }
        audioRecord.startRecording();
        handler.post(runnable);
    }
    byte[] bytes_pkg;

    private Runnable runnable=new Runnable() {
        @Override
        public void run() {
            try {
                if (m_keep_running) {
                    //从内存获取数据
                    int r = audioRecord.read(buffer, 0, bufferSize);
                    bytes_pkg = buffer.clone();

                    //发送socket数据
                    if (m_in_q.size() >= 2) {
                        byte[] buff = m_in_q.removeFirst();
                        // ByteString socketStr = ByteString.copyFrom(buff);
                    }

                    //音量
                    int maxVol = getVolumeMax(r, bytes_pkg);
                    int v = getVolume(r, bytes_pkg);
                    Log.i("+++++++++",maxVol+"=="+v+"=="+r);
                    //如果音量大于最小值 打开开关
                    if (maxVol > socketVol) {
                        flagVol = true;
                    }
                    //如果开关为打开状态，开始计时，计时大约1个周期的时候 ，关闭开关，停止计时，停止发送数据
                    if (flagVol) {
                        m_in_q.add(bytes_pkg);
                        volTime++;
                        if ((volTime / cycle) > 0) {
                            flagVol = false;
                            volTime = 0;
                        }
                    }
                    handler.postDelayed(this,1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    private int getVolume(int r, byte[] bytes_pkg) {
        //way 1
        int v = 0;
//      将 buffer 内容取出，进行平方和运算
        for (byte aBytes_pkg : bytes_pkg) {
            // 这里没有做运算的优化，为了更加清晰的展示代码
            v += aBytes_pkg * aBytes_pkg;
        }
//      平方和除以数据总长度，得到音量大小。可以获取白噪声值，然后对实际采样进行标准化。
//      如果想利用这个数值进行操作，建议用 sendMessage 将其抛出，在 Handler 里进行处理。
        return (int) (v / (float) r);
    }

    private int getVolumeMax(int r, byte[] bytes_pkg) {

        //way 2
        int mShortArrayLenght = r / 2;
        short[] short_buffer = byteArray2ShortArray(bytes_pkg, mShortArrayLenght);
        int max = 0;
        if (r > 0) {
            for (int i = 0; i < mShortArrayLenght; i++) {
                if (Math.abs(short_buffer[i]) > max) {
                    max = Math.abs(short_buffer[i]);
                }
            }
        }
        return max;
    }

    private short[] byteArray2ShortArray(byte[] data, int items) {
        short[] retVal = new short[items];
        for (int i = 0; i < retVal.length; i++)
            retVal[i] = (short) ((data[i * 2] & 0xff) | (data[i * 2 + 1] & 0xff) << 8);

        return retVal;
    }

    public void stop() {
        m_keep_running = false;
        audioRecord.release();
        audioRecord = null;
        buffer = null;
    }

}