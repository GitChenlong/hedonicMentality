package com.sage.hedonicmentality.fragment.breath;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.app.Http;
import com.sage.hedonicmentality.bean.HRVData;
import com.sage.hedonicmentality.bean.SoundPoolData;
import com.sage.hedonicmentality.ui.ActivityBreath;
import com.sage.hedonicmentality.utils.Contact;
import com.sage.hedonicmentality.utils.SPHelper;
import com.sage.hedonicmentality.utils.SharedPreferencesHelper;
import com.sage.hedonicmentality.utils.Util;
import com.sage.hedonicmentality.view.MyWebView;
import com.sage.hedonicmentality.widget.DrawerTopLayout;
import com.sage.hedonicmentality.widget.SurfacePanel;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Sage on 2015/8/10.
 */
public class FragmentBreath2 extends FragmentHRBase {
    public static final String AUDIO_SERVICE = Context.AUDIO_SERVICE;
    ////    @Bind(R.id.btn_left)/**左侧后退按钮*/
//    public ImageView btn_left;
////    @Bind( R.id.btn_right)/**右侧功能按钮*/
//    public ImageView btn_rigth;
////    @Bind(R.id.tv_title)/**标题*/
//    public TextView tv_title;
////    @Bind(R.id.layout_actionbar)/**actionbar整体*/
//    public RelativeLayout layout_acionbar;
    @Bind(R.id.tv_start)
    TextView tv_start;
    @Bind(R.id.tv_prompt)
    TextView tv_prompt;
    @Bind(R.id.tv_hr)
    TextView tv_hr;
    @Bind(R.id.tv_coherence)
    TextView tv_coherence;
    @Bind(R.id.tv_bpm)
    TextView tv_bpm;
    @Bind(R.id.tv_score)
    TextView tv_score;
    @Bind(R.id.tv_hp2)
    TextView tv_hp;
    @Bind(R.id.tv_time)
    TextView tv_time;
    @Bind(R.id.tv_showjia)
    TextView tv_showjia;
    @Bind(R.id.bpmView)
    SurfacePanel bpmView;
    @Bind(R.id.preview)
    SurfaceView preview;
    @Bind(R.id.iv_show)
    ImageView iv_show;
    @Bind(R.id.iv_show1)
    ImageView iv_show1;
    @Bind(R.id.iv_show2)
    ImageView iv_show2;
    @Bind(R.id.layout_curve)
    LinearLayout layout_curve;
    @Bind(R.id.layout_root)
    LinearLayout layout_root;
    @Bind(R.id.layout_showjia)
    LinearLayout layout_showjia;
    @Bind(R.id.layout_actionbar)
    RelativeLayout layout_actionbar;
    @Bind(R.id.parentRe)
    RelativeLayout parentRe;
    @Bind(R.id.scene_webview)
    MyWebView scene_webview;
    @Bind(R.id.drawerLayout)
    DrawerTopLayout drawerTopLayout;
    private int device;
    private Animation animation;/**呼吸的收缩动画*/
    public int interval;/**已经训练的时间,分钟*/
    public int hr;/***/
    private String html;
    private String jshx;
    private String name;

    //计算区间的总分值
    private int sumhp = 88;
    //等级
    private int level = 0;

    private int currentlevel = 0;
    private int Hp = 0;//生命值

    private int userhp = 0;
    /**设备，0表示摄像头，1表示外接蓝牙设备*/
    @Override
    public int getLayout() {
        return R.layout.breath_main;
    }
//    public static int ONE_OR_TWO = 1;

    private int i = 0;//测试层级用
    private  View breathview;
    private static int duang = 0;//第一界面声音
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        LogUtils.e("6666++++++++++onAttach:");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.e("6666++++++++++onCreate:");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtils.e("6666++++++++++onCreateView:");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.e("6666++++++++++onStart:");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.e("6666++++++++++onResume:");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.e("6666++++++++++onPause:");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.e("6666++++++++++onStop:");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.e("6666++++++++++onDestroy:");
        getActivity().unregisterReceiver(mBatInfoReceiver);
    }

    private  int isquan = 0;//是否全景模式
    ActivityBreath activityBreath;
    private boolean isinit = false;
    @Override
    public void initActionbar() {
        if(!isinit) {
            regiScreen();
            activityBreath = (ActivityBreath) getActivity();
            isBack = false;
//        btn_left.setImageResource(R.mipmap.breath_back);
//        btn_rigth.setImageResource(R.mipmap.breath_help);
            LogUtils.e("++++activityBreath.diaoduang()1111;+");
            btn_rigth.setVisibility(View.VISIBLE);
            scene_webview.getSettings().setJavaScriptEnabled(true);
            html = SPHelper.getDefaultString(getActivity(), SPHelper.KEY_SCENE_HTML, "");
            jshx = SPHelper.getDefaultString(getActivity(), SPHelper.KEY_SCENE_JSHX, "");//呼吸频率的JS函数
            name = SPHelper.getDefaultString(getActivity(), SPHelper.KEY_SCENE_NAME, "");
            if (!html.equals("")) {
                scene_webview.loadUrl("www.baidu.com");
//                scene_webview.loadUrl("file:///storage/emulated/0/HRVHTML/" + html);
//                /storage/sdcard0/HRVHTML/htcx.zip
                String htm =  "file://"+Environment.getExternalStorageDirectory()+"/HRVHTML/"+ html;
                scene_webview.loadUrl(htm);
//            scene_webview.loadUrl("javascript:breathingRate(30)");
                scene_webview.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        if (!html.equals("")) {
                            scene_webview.loadUrl("javascript:breathingRate(" + pace + ")");
                            activityBreath.diaoduang();
                            LogUtils.e("++++activityBreath.diaoduang();+");
                        }
                    }
                });
            }

            tv_title.setTextColor(getResources().getColor(R.color.bg_title));
            tv_title.setText(name);
            device = SPHelper.getDefaultInt(getActivity(), SPHelper.KEY_DEVICE, 1);
            if (!"".equals(device)) {
                if (0 == device) {
                    tv_showjia.setText(getString(R.string.ca_xiangji));
                    iv_show1.setImageResource(R.mipmap.camr1);
                    iv_show2.setImageResource(R.mipmap.camr2);
                } else if (1 == device) {
                    tv_showjia.setText(getString(R.string.bl_shouzhijia));
                    iv_show1.setImageResource(R.mipmap.bljia1);
                    iv_show2.setImageResource(R.mipmap.bljia2);
                }
            }
            isinit = true;
        }
    }

    private int cshp = 0;
    private int g = 0;
    @OnClick({R.id.btn_right,R.id.iv_show,R.id.parentRe})
    void breathClick(View view){
        switch(view.getId()){
//            case R.id.btn_right:
////               FragmentDiaHelp.create().show(getChildFragmentManager(),"hello");
//
//                if(i == 10){
//                    i=0;
//                }
//                g +=10;
//                String jsname = SPHelper.getDefaultString(getActivity(),SPHelper.KEY_SCENE_JS,"");
////                scene_webview.loadUrl("javascript:"+jsname+"(" + i + ")");startScene
//                int j = 300;
//                scene_webview.loadUrl("javascript:breathingRate("+g+")");
//                scene_webview.loadUrl("javascript:startScene(" + i + ")");
//                i++;
////                cshp++;
////                tv_hp.setText(cshp+"");
////                getLevel(cshp);
//                break;
            case R.id.iv_show:
                //drawerTopLayout.show_hidden_click();
                break;
            case R.id.parentRe:
                if(isquan == 0){
                    drawerTopLayout.setVisibility(View.GONE);
                    layout_actionbar.setVisibility(View.GONE);
                    isquan = 1;
                }else
                if(isquan == 1){
                    drawerTopLayout.setVisibility(View.VISIBLE);
                    layout_actionbar.setVisibility(View.VISIBLE);
                    isquan = 0;
                }
                break;
        }
    }

    /**后退按钮的处理*/
    public void back(){
        if(finish){/**已经测试结束的话，后退直接退出*/
            getActivity().finish();
        }else{
            show_dia(1);
        }
    }

//    public void distroyWebview(){
//        scene_webview.removeAllViews();
//        scene_webview.destroy();
//    }
    /**后退按钮的处理*/
    @Override
    public void navigation() {
        getActivity().onBackPressed();
    }
    private FragmentDiaFinish diaFinish;
    public void show_dia(int tag){
//        sedbrad();
        if(diaFinish!=null){
            diaFinish.dismiss();
            diaFinish=null;
        }
         int times =   SPHelper.getDefaultInt(getActivity(), SPHelper.KEY_DURATION, 5);
        LogUtils.e("-----------------time"+times);
        int ario = 0;
        if(Hp < HP9){
            ario = (Hp*100)/HP9;
        }else
        if(Hp == 0){
            ario = 0;
        }else {
            ario = 100;
        }
         diaFinish=FragmentDiaFinish.create(tag,tag==0?duration:interval,hr,myCoherence,pace,Integer.parseInt(tv_hp.getText().toString()),times,ario,level);
         diaFinish.show(getChildFragmentManager(), "tag" + tag);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        duration= SPHelper.getDefaultInt(getActivity(), SPHelper.KEY_DURATION, 5);
       // duration=1;
        pace=SPHelper.getDefaultInt(getActivity(),SPHelper.KEY_BREATH_PER_MIN,5);
        device=SPHelper.getDefaultInt(getActivity(), SPHelper.KEY_DEVICE, 0);
        tv_bpm.setText(getString(R.string.breath_bpm, pace));
//        initSound(pace);
        resetData();
    }

    private void resetData(){
        if(device==0){
            initCamera(preview);
            boolean first_camera=SPHelper.getDefaultBoolean(getActivity(),SPHelper.KEY_FIRST_CAMERA,true);
//            if(first_camera){
//                FragmentDiaPrompt prompt=FragmentDiaPrompt.create(0);
//                prompt.show(getChildFragmentManager(), "camera");
//                SPHelper.putDefaultBoolean(getActivity(),SPHelper.KEY_FIRST_CAMERA,false);
//            }
        }else if(device==1){
            initBlueTooth(preview);
        }
        showTime(0);
        tv_score.setText("0");
        tv_coherence.setText("0%");
        tv_hr.setText("0");
        tv_hp.setText("0");
        Hp = 0;
        sumhp = 88;
        level = 0;
    }
    FragmentDiaPrompt prompt;/**蓝牙设备连接失败的提示框*/
    @Override
    public void showAlert() {
        super.showAlert();

        if(finish){
            if(prompt!=null){
                prompt.dismiss();
                prompt=null;
            }
            return;
        }
        if(prompt!=null&&prompt.isAdded()){
           return;
        }
        try {
            prompt=FragmentDiaPrompt.create(1);
            prompt.show(getChildFragmentManager(), "bluetooth");
        }catch (Exception e){
                getActivity().finish();
        }
    }

    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if(layout_root == null){
                        return;
                    }
                    layout_root.setVisibility(View.GONE);
                    layout_showjia.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    if(layout_root == null){
                        return;
                    }
                    layout_root.setVisibility(View.VISIBLE);
                    layout_showjia.setVisibility(View.GONE);
                    break;
            }
            super.handleMessage(msg);
        }
    };
    /**数据暂停  界面变成等待状态*/
    public void DataSuspend(boolean ishow){
        Message message = new Message();
        if(ishow){
            message.what = 1;
            handler.sendMessage(message);
        }else {
            message.what = 2;
            handler.sendMessage(message);
        }
    }

    /**已经测试的时间，调用这个方法实时更新时间的显示*/
    public  void showTime(long time){
        if(prompt!=null){
            prompt.dismiss();
        }
        interval= (int) (time/1000/60);
        if(time>duration*60*1000-9*1000){
            if(!activityBreath.guide_end_start){
                activityBreath.guide_end_start=true;
                activityBreath.playEnd();
            }

        }
        long myt = 0 ;
        if(duration*60*1000 >= time ){
             myt = duration*60*1000 - time;
        }
        if(tv_time!=null)
            tv_time.setText(new SimpleDateFormat("mm:ss").format(new Date(myt)));

        //提示第二界面更新时间
        Intent intent = new Intent();
        intent.setAction(FragmentRainbowCircle.ACTION);
        intent.putExtra("cmd", 5);
        intent.putExtra("time",new SimpleDateFormat("mm:ss").format(new Date(myt)));
        getActivity().sendBroadcast(intent);
    }
    /**@param durationTime  时间，单位秒
     * @param bpmval 每分钟呼吸数*/
    public void addBeat(final float durationTime, final float bpmval){
        if(bpmView!=null)
        bpmView.addBeat(durationTime, bpmval);
        LogUtils.i("============addBeat" + "durationtime=" + durationTime + " hr=" + bpmval);
         //bpmView.postInvalidate();
        if(tv_score!=null)
        tv_score.setText("" + bpmView.getScore());
        super.addBeat(durationTime, bpmval);
    }
    /***设定的测试时间到了，要做的处理，比如关闭页面，弹出对话框等等*/
    public void finishTest(){
        if(animation!=null){
            animation.cancel();
            animation=null;
        }
        releaseCamera();
        activityBreath.releaseMP();
        sedbrad();
        if(SCREEN == 1){
            show_dia(0);
        }else
        if(SCREEN == 2){
            getActivity().finish();
        }
    }
    /**@param  timeSessionSecond second
     * 练习的时候用来更新分数和心意合一程度*/
    public void  updateScoreSession(double timeSessionSecond){
           double percentage = (double) (bpmView.getScore()) / ((timeSessionSecond / 60.0) * (pace / 1.0) * 2);
           if (percentage > 1) percentage = 1;
           myCoherence = (int) (percentage * 100);
        if(tv_coherence!=null)
           tv_coherence.setText(getString(R.string.breath_coherence, myCoherence));
    }
//    private boolean breath=true;

    /**获取第一条数据，开启动画或者别的操作*/
    public void startShow(){
//        if(!html.equals("")){
//////            scene_webview.loadUrl("file:///storage/emulated/0/HRVHTML/" + html);
////            LogUtils.e("-------------pace====:" + pace);
//            scene_webview.loadUrl("javascript:breathingRate("+pace+")");
//            activityBreath.diaoduang();
//        }
        layout_root.setVisibility(View.VISIBLE);
        layout_showjia.setVisibility(View.GONE);
//        playBG();
        activityBreath.playStart();
//        layout_curve.setVisibility(View.VISIBLE);//开始不显示曲线图
//        iv_show.setVisibility(View.VISIBLE);
        activityBreath.breath=true;

        //第二界面开始动画
        Intent intent = new Intent();
        intent.setAction(FragmentRainbowCircle.ACTION);
        intent.putExtra("cmd", 2);
        intent.putExtra("pace",pace);
        getActivity().sendBroadcast(intent);
    }


    /**算出每分钟心跳平均值后用来显示用的*/
    public void showBeatsAvg(int beatsAvg){
        hr=beatsAvg;
        if(tv_hr!=null)
        tv_hr.setText(beatsAvg+"");
        Intent intent = new Intent();
        intent.setAction(FragmentRainbowCircle.ACTION);
        intent.putExtra("cmd", 4);
        intent.putExtra("hr",beatsAvg);
        getActivity().sendBroadcast(intent);
    }
    /**第二界面刷新数据*/
    public void sedbrad(){
        Intent intent = new Intent();
        intent.setAction(FragmentRainbowCircle.ACTION);
        intent.putExtra("cmd",3);
        getActivity().sendBroadcast(intent);
    }

    private HRVData lastData;
    @Override
    public void onTaskComplete(HRVData data) {
        if(data==null){
            return;
        }
        float a  = data.getHf()/data.getLf();
    LogUtils.e("data.getHf()+"+data.getHf()+"++data.getLf()"+data.getLf());
        if(a >= 0 && a < 0.5){
            setText(3);
        }else
        if(a >=0.5 && a < 1){
            setText(2);
            Hp = Hp+1;
        }else
        if(a > 1){
            setText(1);
            Hp = Hp+2;
        }
        tv_hp.setText(Hp+"");//测试时暂停
        getLevel(Hp);
        LogUtils.e("++++++++++++++++++onTaskComplete");
        setHRVData(data.toString());
        Log.e("hrvdata", data.toString());
        lastData=data;
        Intent intent = new Intent();
        intent.setAction(FragmentRainbowCircle.ACTION);
        intent.putExtra("data",data);
        intent.putExtra("cmd",1);
        getActivity().sendBroadcast(intent);
    }
    public void setText(int id){
        if(id == 1){
            tv_prompt.setText(R.string.reminder1);
            tv_prompt.setTextColor(getResources().getColor(R.color.gre));
        }
        if(id == 2){
            tv_prompt.setText(R.string.reminder2);
            tv_prompt.setTextColor(getResources().getColor(R.color.ye));
        }
        if(id == 3){
            tv_prompt.setText(R.string.reminder3);
            tv_prompt.setTextColor(getResources().getColor(R.color.red));
        }
    }

    /**等级9的分数线*/
    private int HP9 = 0;
    /**通过HP 计算等级*/
    public void getLevel(int hp){
        int  time  = SPHelper.getDefaultInt(getActivity(), SPHelper.KEY_DURATION, 5);
        LogUtils.e("-----------------timeccc" + time);
        if(5 == time){
            sumhp = 88;
        }
        if(10 == time){
            sumhp = 198;
        }
        if(15 == time){
            sumhp = 308;
        }
        if(20 == time){
            sumhp = 418;
        }
        if(30 == time){
            sumhp = 638;
        }
        if(60 == time){
            sumhp = 1298;
        }
        int le1,le2,le3,le4,le5,le6,le7,le8,le9;
        le1 = sumhp/10; le2 = sumhp*2/10; le3 = sumhp*3/10; le4 = sumhp*4/10;  le5 = sumhp*5/10;
        le6 = sumhp*58/100; le7 = sumhp*66/100; le8 = sumhp*74/100; le9 = sumhp*82/100;

        if(hp <le1) {
            level = 0;
        }else
        if (hp > (le1 - 1) && hp < le2) {
            level = 1;
        } else if (hp > (le2 - 1) && hp < le3) {
            level = 2;
        } else if (hp > (le3 - 1) && hp < le4) {
            level = 3;
        } else if (hp > (le4 - 1) && hp < le5) {
            level = 4;
        } else if (hp > (le5 - 1) && hp < le6) {
            level = 5;
        } else if (hp > (le6 - 1) && hp < le7) {
            level = 6;
        } else if (hp > (le7 - 1) && hp < le8) {
            level = 7;
        } else if (hp > (le8 - 1) && hp < le9) {
            level = 8;
        } else
        if (hp > (le9 - 1) && hp < (sumhp + 1)){
            level = 9;
        }
        String jsname = SPHelper.getDefaultString(getActivity(),SPHelper.KEY_SCENE_JS,"");
        if(!jsname.equals("")){
            if(level>currentlevel){
                scene_webview.loadUrl("javascript:"+jsname+"(" + level + ")");
                currentlevel = level;
            }
        }
        HP9 = le9;
    }


    @Override
    public void onDestroyView() {
        /**关闭的时候保存数据*/
        LogUtils.e("onDestroyView666666++++");
        if(lastData!=null){
            LogUtils.e("onDestroyView77777++++");
            lastData.interval=interval;
            lastData.setActionId(sessionStartTime);
//            lastData.score=bpmView.getScore();//把原有得分替换为生命值
            String hp = tv_hp.getText().toString();
            if(null != hp){
                lastData.score = Integer.parseInt(hp);
            }
            lastData.bpm=pace;
            lastData.coherence=Integer.parseInt(tv_hr.getText().toString().trim());
            lastData.device=(device==0?"camera":"bluetooth");
            lastData.hr = Float.parseFloat(tv_hr.getText().toString().trim());
            DbUtils dbUtils= Util.getDbUtils(getActivity());
            try {
                dbUtils.save(lastData);
                /**用户登陆的话，调用接口上传数据*/
                if(!TextUtils.isEmpty(SharedPreferencesHelper.getInstance().getString(Contact.SH_ID))){
                  int  uhp =  SharedPreferencesHelper.getInstance().getInt(Contact.SH_SUMHP);//添加总的hp
                    userhp = uhp + lastData.score;
                    SharedPreferencesHelper.getInstance().putInt(Contact.SH_SUMHP,userhp);
                    uploadHRV(lastData, getActivity());
                }else{
                    uploadHRVForGuest(lastData,getActivity());
                }

            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        activityBreath.releaseMP();
        scene_webview.removeAllViews();
        scene_webview.destroy();
        System.gc();
        super.onDestroyView();
    }


    /**上传数据*/
    private void uploadHRV(final HRVData lastData, final Context context) {
        Http.saveHrv(getActivity(),lastData, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                    LogUtils.i("saveHrv="+"onSuccess="+responseInfo.result);
                        JSONObject object= null;
                        try {
                            object = new JSONObject(responseInfo.result);
                            if(object.getInt("info")==0){
                                LogUtils.i("uploadHRV==info=0"+"tip="+object.getString("tip"));
                            }else {
                                LogUtils.i("uploadHRV==info=1"+"tip="+object.getString("tip"));
                                DbUtils dbUtils= Util.getDbUtils(context);
                                lastData.upload=1;
                                try {
                                    dbUtils.update(lastData,"upload");
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(HttpException e, String s) {
                        LogUtils.i("saveHrv="+"onFailure==="+s);
                    }
                });
    }
    /**上传数据*/
    private void uploadHRVForGuest(final HRVData lastData, final Context context) {
        Http.saveHrvForGuest(getActivity(), lastData, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.i("saveHrv="+"onSuccess="+responseInfo.result);
                        JSONObject object= null;
                        try {
                            object = new JSONObject(responseInfo.result);
                            if(object.getInt("info")==0){
                                LogUtils.i("uploadHRVForGuest==info=0"+"tip="+object.getString("tip"));
                            }else {
                                LogUtils.i("uploadHRVForGuest==info=1"+"tip="+object.getString("tip"));
                                DbUtils dbUtils= Util.getDbUtils(context);
                                lastData.upload=1;
                                try {
                                    dbUtils.update(lastData,"upload");
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                    @Override
                    public void onFailure(HttpException e, String s) {
                        LogUtils.i("saveHrv="+"onFailure==="+s);
                    }
                });
    }
    public boolean setHRVData(String str){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存在
        if   (sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
//        return sdDir.toString();

        File file = new File(sdDir.getPath()+"/HRV");
        try {
            //按照指定的路径创建文件夹
            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        File dir = new File(file.getPath()+"/hrvdata.txt");
        if (!dir.exists()) {
            try {
                //在指定的文件夹中创建文件
                dir.createNewFile();
            } catch (Exception e) {

            }
        }
        FileWriter fw = null;
        BufferedWriter bw = null;
        String datetime = "";
        try {
            SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd" + " "
                    + "hh:mm:ss");
            datetime = tempDate.format(new java.util.Date()).toString();
            fw = new FileWriter(dir.getPath(), true);//
            // 创建FileWriter对象，用来写入字符流
            bw = new BufferedWriter(fw); // 将缓冲对文件的输出
            String myreadline = datetime + ":" + str;

            bw.write(myreadline + "\n"); // 写入文件
            bw.newLine();
            bw.flush(); // 刷新该流的缓冲
            bw.close();
            fw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            try {
                bw.close();
                fw.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
            }
        }
    return true;
    }
    public  Thread thread;
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        scene_webview.loadUrl("javascript:breathingRate(" + pace + ")");
                    }catch (Exception e){

                    }
                break;
            }
            super.handleMessage(msg);
        }
    };
    public void starlodurlp(){
               Message message = new Message();
               message.what = 1;
               myHandler.sendMessage(message);
    }

    public int  SCREEN  = 1;
    BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            Log.d(TAG, "onReceive");
            String action = intent.getAction();

            if (Intent.ACTION_SCREEN_ON.equals(action)) {
                SCREEN = 1;
                Log.d(TAG, "screen on");
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                SCREEN = 2;
                Log.d(TAG, "screen off");
            } else if (Intent.ACTION_USER_PRESENT.equals(action)) {

            } else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction())) {

            }
        }
    };
    //监听屏幕是否关闭
    public void regiScreen(){
        final IntentFilter filter = new IntentFilter();
        // 屏幕灭屏广播
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        // 屏幕亮屏广播
        filter.addAction(Intent.ACTION_SCREEN_ON);
        // 屏幕解锁广播
        filter.addAction(Intent.ACTION_USER_PRESENT);
        // 当长按电源键弹出“关机”对话或者锁屏时系统会发出这个广播
        // example：有时候会用到系统对话框，权限可能很高，会覆盖在锁屏界面或者“关机”对话框之上，
        // 所以监听这个广播，当收到时就隐藏自己的对话，如点击pad右下角部分弹出的对话框
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);

        Log.d(TAG, "registerReceiver");
        getActivity().registerReceiver(mBatInfoReceiver, filter);
    }
}
