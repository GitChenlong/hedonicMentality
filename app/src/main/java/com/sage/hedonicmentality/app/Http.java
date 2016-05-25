package com.sage.hedonicmentality.app;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.bean.HMRecord;
import com.sage.hedonicmentality.bean.HRVData;
import com.sage.hedonicmentality.utils.Contact;
import com.sage.hedonicmentality.utils.SPHelper;
import com.sage.hedonicmentality.utils.SharedPreferencesHelper;

import java.io.File;
import java.io.UnsupportedEncodingException;

/**
 * Created by Sage on 2015/8/14.
 */
public class Http {
    public static String HTTP_URL = "http://api.kuailexinli.com/";
//    public static String HTTP_URL = "http://test1.kuailexinli.cn/";
    public static String HTTP_UrlLogin="user/login";
    public static String HTTP_Register="user/register";
    public static String HTTP_Sendsms="user/sendsms";
    public static String HTTP_Forget="user/misspwd";
    public static String HTTP_Avatar="user/avatar";
    public static String HTTP_SelfSetting="user/selfSetting";
    public static String HTTP_GetInfo="user/getInfo";
    public static String HTTP_Fankui="feedback/saveFeedback";
    public static String HTTP_Resetpwd="user/resetpwd";
    public static String HTTP_SaveHrv="hrv/saveHrv";
    public static String HTTP_GetHrvList="hrv/getHrvList1";
    public static String HTTP_VersionCheck="app/update";
    public static String HTTP_Address="user/getcity";
    public static String HTTP_Index="healthrecords/index";
    public static String HTTP_Savehealthrecords="healthrecords/savehealthrecords";
    public static String HTTP_Scene="scene/index";
    private static Context app = null;
    public static String version = "v1.1";
    public static String channel = "官网";
    public static void InitContext(Context context){
        if(app == null){
            app = context.getApplicationContext();
//            try {
//                ApplicationInfo appInfo = app.getPackageManager()
//                    .getApplicationInfo(app.getPackageName(),
//                            PackageManager.GET_META_DATA);
//                 channel=appInfo.metaData.getString("CHANNEL");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

        }
    }
    public static Context getCotext(){
        return app;
    }

    /**登陆*/
    public static void Login(String phone,String password,RequestCallBack<String> callBack){
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("mobile_phone", phone);
        params.addQueryStringParameter("password", password);
        params.addQueryStringParameter("version",version);
        params.addQueryStringParameter("channel",channel);
        HttpUtils http = new HttpUtils();
        http.configTimeout(10*1000);
        http.send(HttpRequest.HttpMethod.POST,HTTP_URL+Http.HTTP_UrlLogin,params,callBack);
    }

    /**获取验证码*/
    public static void Sendsms(String phone,String type,RequestCallBack<String> callBack){
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("mobile_phone", phone);
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("version",version);
        params.addQueryStringParameter("channel",channel);
        HttpUtils http = new HttpUtils();
        http.configTimeout(10*1000);
        http.send(HttpRequest.HttpMethod.POST,Http.HTTP_URL+Http.HTTP_Sendsms,params,callBack);
    }

    /**注册*/
    public static void Register(String phone,String password,String verify_code,RequestCallBack<String> callBack){
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("mobile_phone", phone);
        params.addQueryStringParameter("password", password);
        params.addQueryStringParameter("verify_code", verify_code);
        params.addQueryStringParameter("version",version);
        params.addQueryStringParameter("channel", channel);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,Http.HTTP_URL+Http.HTTP_Register,params,callBack);
    }

    /**忘记密码*/
    public static void Forget(String phone,String password,String verify_code,RequestCallBack<String> callBack){
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("mobile_phone", phone);
        params.addQueryStringParameter("password", password);
        params.addQueryStringParameter("verify_code", verify_code);
        params.addQueryStringParameter("version",version);
        params.addQueryStringParameter("channel",channel);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,Http.HTTP_URL+Http.HTTP_Forget,params,callBack);
    }

    /**修改个人信息*/
    public static void SelfSetting(String phone,String password,String nick_name,String sex,String birthday,String height,String weight,String address,String education,RequestCallBack<String> callBack){
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("mobile_phone", phone);
        params.addQueryStringParameter("password", password);
        params.addBodyParameter("nick_name", nick_name);
        params.addQueryStringParameter("sex", sex);
        params.addQueryStringParameter("birthday", birthday);
        params.addQueryStringParameter("height", height);
        params.addQueryStringParameter("weight", weight);
        params.addQueryStringParameter("version",version);
        params.addQueryStringParameter("channel",channel);
        params.addQueryStringParameter("education",education);
        params.addQueryStringParameter("area",address);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,Http.HTTP_URL+Http.HTTP_SelfSetting,params,callBack);
    }

    /**上传头像*/
    public static void Avatar(File file,String phone,String pwd,RequestCallBack<String> callBack){
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("mobile_phone", phone);
        params.addQueryStringParameter("password", pwd);
        params.addBodyParameter("avatar", file);
        params.addQueryStringParameter("version", version);
        params.addQueryStringParameter("channel",channel);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,Http.HTTP_URL+Http.HTTP_Avatar,params,callBack);
    }

    /**获取个人信息*/
    public static void GetInfo(String phone,String pwd,RequestCallBack<String> callBack){
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("mobile_phone", phone);
        params.addQueryStringParameter("password", pwd);
        params.addQueryStringParameter("version",version);
        params.addQueryStringParameter("channel",channel);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,Http.HTTP_URL+Http.HTTP_GetInfo,params,callBack);
    }

    /**修改密码*/
    public static void Resetpwd(String phone,String pwd,String new_password,RequestCallBack<String> callBack){
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("mobile_phone", phone);
        params.addQueryStringParameter("old_password", pwd);
        params.addQueryStringParameter("new_password", new_password);
        params.addQueryStringParameter("version",version);
        params.addQueryStringParameter("channel",channel);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,Http.HTTP_URL+Http.HTTP_Resetpwd,params,callBack);
    }

    /**意见反馈*/
    public static void Fankui(String phone,String pwd,String content,RequestCallBack<String> callBack){
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("version",version);
        params.addQueryStringParameter("mobile_phone", phone);
        params.addQueryStringParameter("password", pwd);
        params.addQueryStringParameter("channel",channel);
        String mem;
        try {
            mem = new String(content.getBytes(),"UTF-8");
            params.addBodyParameter("msg_content", mem);
            HttpUtils http = new HttpUtils();
            http.send(HttpRequest.HttpMethod.POST,Http.HTTP_URL+Http.HTTP_Fankui,params,callBack);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

//    /**上传hrv数据*/
//    public static void saveHrv(Context context,String interval,String actionid,String score,String coherence, String bpm,String hr,String hrmax,String hrmean,String nnmax,
//                               String nnmean,String sdnn,String rmssd,String lf_hf,String lf,String hf,String totalpower,RequestCallBack<String> callBack){
//        SharedPreferencesHelper.getInstance().Builder(context);
//        String phone =  SharedPreferencesHelper.getInstance().getString(Contact.SH_PHONE);
//        String pwd =  SharedPreferencesHelper.getInstance().getString(Contact.SH_PWD);
//        RequestParams params = new RequestParams();
//        params.addQueryStringParameter("version",version);
//        params.addQueryStringParameter("channel",channel);
//        params.addQueryStringParameter("terminal","android");
//        //params.addQueryStringParameter("device",0);
//        params.addQueryStringParameter("mobile_phone", phone);
//        params.addQueryStringParameter("password", pwd);
//        params.addQueryStringParameter("interval", interval);
//        params.addQueryStringParameter("actionid", actionid);
//        params.addQueryStringParameter("score", score);
//        params.addQueryStringParameter("coherence", coherence);
//        params.addQueryStringParameter("bpm", bpm);
//        params.addQueryStringParameter("hr", hr);
//        params.addQueryStringParameter("hrmax", hrmax);
//        params.addQueryStringParameter("hrmean", hrmean);
//        params.addQueryStringParameter("nnmax", nnmax);
//        params.addQueryStringParameter("nnmean", nnmean);
//        params.addQueryStringParameter("sdnn", sdnn);
//        params.addQueryStringParameter("rmssd", rmssd);
//        params.addQueryStringParameter("lf_hf", lf_hf);
//        params.addQueryStringParameter("lf", lf);
//        params.addQueryStringParameter("hf", hf);
//        params.addQueryStringParameter("totalpower", totalpower);
//        HttpUtils http = new HttpUtils();
//        http.send(HttpRequest.HttpMethod.POST,Http.HTTP_URL+Http.HTTP_SaveHrv,params,callBack);
//    }
    /**上传hrv数据*/
    public static void saveHrv(Context context,HRVData hrvData,RequestCallBack<String> callBack){
        SharedPreferencesHelper.getInstance().Builder(context);
        String phone =  SharedPreferencesHelper.getInstance().getString(Contact.SH_PHONE);
        String pwd =  SharedPreferencesHelper.getInstance().getString(Contact.SH_PWD);
        int sumhp =  SharedPreferencesHelper.getInstance().getInt(Contact.SH_SUMHP);

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("health_points",sumhp+"");
        params.addQueryStringParameter("version",version);
        params.addQueryStringParameter("channel",channel);
        params.addQueryStringParameter("mobile_phone", phone);
        params.addQueryStringParameter("password", pwd);
        params.addQueryStringParameter("terminal","android");
        params.addQueryStringParameter("device",""+hrvData.device);
        params.addQueryStringParameter("interval", ""+hrvData.interval);
        params.addQueryStringParameter("actionid", ""+hrvData.actionid);
        params.addQueryStringParameter("score", ""+hrvData.score);
        params.addQueryStringParameter("coherence", ""+hrvData.coherence);
        params.addQueryStringParameter("bpm", ""+hrvData.bpm);
        params.addQueryStringParameter("hr", ""+hrvData.hr);
        params.addQueryStringParameter("hrmax", ""+hrvData.hrmax);
        params.addQueryStringParameter("hrmean", ""+hrvData.hrmean);
        params.addQueryStringParameter("nnmax", ""+hrvData.nnmax);
        params.addQueryStringParameter("nnmean", ""+hrvData.nnmean);
        params.addQueryStringParameter("sdnn", ""+hrvData.sdnn);
        params.addQueryStringParameter("rmssd", ""+hrvData.rmssd);
        params.addQueryStringParameter("lf_hf", ""+hrvData.lf_hf);
        params.addQueryStringParameter("lf", ""+hrvData.lf);
        params.addQueryStringParameter("hf", ""+hrvData.hf);
        params.addQueryStringParameter("totalpower", ""+hrvData.totalpower);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,Http.HTTP_URL+Http.HTTP_SaveHrv,params,callBack);
    }
    /**上传游客hrv数据*/
    public static void saveHrvForGuest(Context context,HRVData hrvData,RequestCallBack<String> callBack){
        String uuid = SPHelper.getDefaultString(context, SPHelper.KEY_UUID, "");
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("version",version);
        params.addQueryStringParameter("channel",channel);
        params.addQueryStringParameter("tourist", uuid);
        params.addQueryStringParameter("terminal","android");
        params.addQueryStringParameter("device",""+hrvData.device);
        params.addQueryStringParameter("interval", ""+hrvData.interval);
        params.addQueryStringParameter("actionid", ""+hrvData.actionid);
        params.addQueryStringParameter("score", ""+hrvData.score);
        params.addQueryStringParameter("coherence", ""+hrvData.coherence);
        params.addQueryStringParameter("bpm", ""+hrvData.bpm);
        params.addQueryStringParameter("hr", ""+hrvData.hr);
        params.addQueryStringParameter("hrmax", ""+hrvData.hrmax);
        params.addQueryStringParameter("hrmean", ""+hrvData.hrmean);
        params.addQueryStringParameter("nnmax", ""+hrvData.nnmax);
        params.addQueryStringParameter("nnmean", ""+hrvData.nnmean);
        params.addQueryStringParameter("sdnn", ""+hrvData.sdnn);
        params.addQueryStringParameter("rmssd", ""+hrvData.rmssd);
        params.addQueryStringParameter("lf_hf", ""+hrvData.lf_hf);
        params.addQueryStringParameter("lf", ""+hrvData.lf);
        params.addQueryStringParameter("hf", ""+hrvData.hf);
        params.addQueryStringParameter("totalpower", ""+hrvData.totalpower);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,Http.HTTP_URL+Http.HTTP_SaveHrv,params,callBack);
    }

//    /**上传游客hrv数据*/
//    public static void saveHrvForGuest(Context context,String interval,String actionid,String score,String coherence, String bpm,String hr,String hrmax,String hrmean,String nnmax,
//                               String nnmean,String sdnn,String rmssd,String lf_hf,String lf,String hf,String totalpower,RequestCallBack<String> callBack){
//        String uuid = SPHelper.getDefaultString(context, SPHelper.KEY_UUID, "");
//        RequestParams params = new RequestParams();
//        params.addQueryStringParameter("version",version);
//        params.addQueryStringParameter("channel",channel);
//        params.addQueryStringParameter("tourist", uuid);
//        params.addQueryStringParameter("interval", interval);
//        params.addQueryStringParameter("actionid", actionid);
//        params.addQueryStringParameter("score", score);
//        params.addQueryStringParameter("coherence", coherence);
//        params.addQueryStringParameter("bpm", bpm);
//        params.addQueryStringParameter("hr", hr);
//        params.addQueryStringParameter("hrmax", hrmax);
//        params.addQueryStringParameter("hrmean", hrmean);
//        params.addQueryStringParameter("nnmax", nnmax);
//        params.addQueryStringParameter("nnmean", nnmean);
//        params.addQueryStringParameter("sdnn", sdnn);
//        params.addQueryStringParameter("rmssd", rmssd);
//        params.addQueryStringParameter("lf_hf", lf_hf);
//        params.addQueryStringParameter("lf", lf);
//        params.addQueryStringParameter("hf", hf);
//        params.addQueryStringParameter("totalpower", totalpower);
//        HttpUtils http = new HttpUtils();
//        http.send(HttpRequest.HttpMethod.POST,Http.HTTP_URL+Http.HTTP_SaveHrv,params,callBack);
//    }

    /**获取hrv数据*/
    public static void getHrvList(Context context,String actionids,RequestCallBack<String> callBack,boolean bl){
        SharedPreferencesHelper.getInstance().Builder(context);
        String phone =  SharedPreferencesHelper.getInstance().getString(Contact.SH_PHONE);
        String pwd =  SharedPreferencesHelper.getInstance().getString(Contact.SH_PWD);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("version",version);
        params.addQueryStringParameter("channel",channel);
        params.addQueryStringParameter("mobile_phone", phone);
        params.addQueryStringParameter("password", pwd);
        params.addQueryStringParameter("actionids", actionids);
        if(bl){
            params.addQueryStringParameter("md5", "1");
        }
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,Http.HTTP_URL+Http.HTTP_GetHrvList,params,callBack);
    }
    /**获取游客hrv数据*/
    public static void getHrvListForGuest(Context context,String actionids,RequestCallBack<String> callBack,boolean bl){
        String uuid = SPHelper.getDefaultString(context, SPHelper.KEY_UUID, "");
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("version",version);
        params.addQueryStringParameter("channel",channel);
        params.addQueryStringParameter("tourist", uuid);
        params.addQueryStringParameter("actionids", actionids);
        if(bl){
            params.addQueryStringParameter("md5", "1");
        }
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,Http.HTTP_URL+Http.HTTP_GetHrvList,params,callBack);
    }

    public static void versionCheck(Context context,RequestCallBack<String> callBack){
        SharedPreferencesHelper.getInstance().Builder(context);
        String phone =  SharedPreferencesHelper.getInstance().getString(Contact.SH_PHONE);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("version",version);
        params.addQueryStringParameter("channel",channel);
        params.addQueryStringParameter("mobile_phone", phone);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,Http.HTTP_URL+Http.HTTP_VersionCheck,params,callBack);
    }

    /**获取地址数据*/
    public static void GetAddress(Context context,RequestCallBack<String> callBack){
        SharedPreferencesHelper.getInstance().Builder(context);
        String phone =  SharedPreferencesHelper.getInstance().getString(Contact.SH_PHONE);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("version",version);
        params.addQueryStringParameter("channel",channel);
        params.addQueryStringParameter("mobile_phone", phone);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,Http.HTTP_URL+Http.HTTP_Address,params,callBack);
    }
    /**获取健康档案*/
    public static void Getindex(Context context,RequestCallBack<String> callBack){
        SharedPreferencesHelper.getInstance().Builder(context);
        String phone =  SharedPreferencesHelper.getInstance().getString(Contact.SH_PHONE);
        String pwd =  SharedPreferencesHelper.getInstance().getString(Contact.SH_PWD);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("version",version);
        params.addQueryStringParameter("channel",channel);
        params.addQueryStringParameter("mobile_phone", phone);
        params.addQueryStringParameter("password", pwd);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,Http.HTTP_URL+Http.HTTP_Index,params,callBack);
    }
    /**提交健康档案*/
    public static void PostSavehealthrecords(Context context,HMRecord hmRecord,RequestCallBack<String> callBack){
        SharedPreferencesHelper.getInstance().Builder(context);
        String phone =  SharedPreferencesHelper.getInstance().getString(Contact.SH_PHONE);
        String pwd =  SharedPreferencesHelper.getInstance().getString(Contact.SH_PWD);
        String id =  SharedPreferencesHelper.getInstance().getString(Contact.SH_ID);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("version",version);
        params.addQueryStringParameter("channel",channel);
        params.addQueryStringParameter("mobile_phone", phone);
        params.addQueryStringParameter("password", pwd);
        params.addQueryStringParameter("id", hmRecord.getId());
        params.addQueryStringParameter("sleep", hmRecord.getSleep());
        params.addQueryStringParameter("appetite", hmRecord.getAppetite());
        params.addQueryStringParameter("systolic", hmRecord.getSystolic());
        params.addQueryStringParameter("diastolic", hmRecord.getDiastolic());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,Http.HTTP_URL+Http.HTTP_Savehealthrecords,params,callBack);
    }

    /**获取场景列表*/
    public static void getScene(Context context,RequestCallBack<String> callBack){
        String uuid = SPHelper.getDefaultString(context, SPHelper.KEY_UUID, "");
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("version",version);
        params.addQueryStringParameter("channel",channel);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,Http.HTTP_URL+Http.HTTP_Scene,params,callBack);
    }

    /**获取发现列表*/
    public static void getFind(String xixi,RequestCallBack<String> callBack){
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("version",version);
        params.addQueryStringParameter("channel",channel);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,"",params,callBack);
    }
    /**获取咨询列表*/
    public static void getZixun(String xixi,RequestCallBack<String> callBack){
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("version",version);
        params.addQueryStringParameter("channel",channel);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,"",params,callBack);
    }
}
