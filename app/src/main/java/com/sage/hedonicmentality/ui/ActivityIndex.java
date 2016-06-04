package com.sage.hedonicmentality.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.app.Http;
import com.sage.hedonicmentality.bean.SceneBean;
import com.sage.hedonicmentality.ui.simple.BreathSetting;
import com.sage.hedonicmentality.utils.Contact;
import com.sage.hedonicmentality.utils.DeleteFileUtil;
import com.sage.hedonicmentality.utils.GsonTools;
import com.sage.hedonicmentality.utils.SPHelper;
import com.sage.hedonicmentality.utils.SharedPreferencesHelper;
import com.sage.hedonicmentality.utils.Util;
import com.sage.hedonicmentality.utils.ZipUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Sage on 2015/8/20.
 */
public class ActivityIndex extends AppCompatActivity {
    private CountDownTimer countDownTimer;
    public HttpHandler<File> handler;
    private List<SceneBean> scenebeans;
    private HttpUtils http = new HttpUtils();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        timer();
        updatedCJ();
        Util.createDirectory();
        SharedPreferencesHelper.getInstance().Builder(ActivityIndex.this);
    }

    //更新场景
    public void updatedCJ(){
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
                            final SceneBean sceneBean =  scenebeans.get(0);
                            File szip = new File(Environment.getExternalStorageDirectory() + "/HRVHTML/" + sceneBean.getLetter());
                            //判断当前场景是否为最新场景
                            String ctime = SPHelper.getDefaultString(ActivityIndex.this, sceneBean.getName(),"0");
                            if(!ctime.equals(sceneBean.getCreatetime())){
                                File file = new File(Environment.getExternalStorageDirectory() + "/HRVHTML");
                                if  (!file .exists()  && !file .isDirectory())
                                {
                                    file .mkdir();
                                } else
                                {
                                    DeleteFileUtil.deleteDirectory(Environment.getExternalStorageDirectory() + "/HRVHTML");
                                    file .mkdir();
                                }
                            }
                            //判断第一个场景的zip是否下载过
                            if(!szip.exists()){
                                handler = http.download(sceneBean.getDownloadurl(), Environment.getExternalStorageDirectory() + "/HRVHTML/" + sceneBean.getLetter(),
                                        true,// 如果目标文件存在，接着未完成的部分继续下载
                                        false,// 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
                                        new RequestCallBack<File>() {
                                    @Override
                                    public void onSuccess(ResponseInfo<File> responseInfo) {
                                        try {
                                            LogUtils.e("下载成功===========================================");
                                            ZipUtils.upZipFile(new File(Environment.getExternalStorageDirectory() + "/HRVHTML/" + sceneBean.getLetter()), Environment.getExternalStorageDirectory() + "/HRVHTML");
                                            //添加更新完成后的场景标识
                                            SPHelper.putDefaultString(ActivityIndex.this, sceneBean.getName(), sceneBean.getCreatetime());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onFailure(HttpException e, String s) {
                                        LogUtils.e("下载失败===========================================");
                                    }

                                    @Override
                                    public void onLoading(long total, long current, boolean isUploading) {
                                        super.onLoading(total, current, isUploading);
                                        LogUtils.e("正在下载===========================================" + current);
                                    }
                                });
                            }
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

                    }
                }
          );
        }

        @Override
    protected void onDestroy() {
        super.onDestroy();
        if(countDownTimer!=null){
            countDownTimer.cancel();
            countDownTimer=null;
        }
    }

    private void timer(){
        countDownTimer=new CountDownTimer(2*1000,2*1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
               // skipToGuide();
                skip();
            }
        };
        countDownTimer.start();
    }


    private void skip(){
        /**判断下，是否是第一次进入，是否保存的版本号比当前版本号低，是的话显示引导页，不是的话判断是否登陆，跳转到登陆或者主页面*/

        boolean isFirst= SPHelper.getDefaultBoolean(this, SPHelper.KEY_IS_FIRST_LOGIN, true);
        if(isFirst){
            SPHelper.putDefaultBoolean(this, SPHelper.KEY_IS_FIRST_LOGIN, false);
            skipToGuide();
        }else{
            if(SPHelper.getDefaultString(this,SPHelper.KEY_LAST_VERSION,"").compareTo(getString(R.string.version))<0){
                //跳入引导页
                skipToGuide();
            }else{
                skipWhich();
            }
        }
    }

    private void skipToGuide(){
        startActivity(new Intent(this,ActivityGuide.class));
        finish();
    }

    public void skipWhich(){
        SharedPreferencesHelper.getInstance().Builder(this);
        if(!TextUtils.isEmpty(SharedPreferencesHelper.getInstance().getString(Contact.SH_ID, ""))){
            //跳入主页面
            startActivity(new Intent(this,BreathSetting.class));
            finish();
        }else{
            //跳入登陆页
            startActivity(new Intent(this,ActivityLogin.class));
            finish();
        }
    }
}
