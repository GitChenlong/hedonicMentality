package com.sage.hedonicmentality.app;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.man.MANService;
import com.lidroid.xutils.util.LogUtils;
import com.sage.hedonicmentality.utils.DeviceUUIDFactory;
import com.sage.hedonicmentality.utils.SPHelper;

/**
 * Created by Sage on 2015/8/18.
 */
public class MyApplication extends Application {

    public static Context getContext(){

       return  app.getApplicationContext();
    }
    public static MyApplication app;
    private static final String TAG = "Init";

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化Appcontext
        Http.InitContext(this);
        LogUtils.allowI=true;
        app=this;
        setUpUUID();
//        AlibabaSDK.asyncInit(getApplicationContext(), new InitResultCallback() {
//
//            @Override
//            public void onSuccess() {
//                Log.d(TAG, "init onesdk success");
//            }
//
//            @Override
//            public void onFailure(int code, String message) {
//                Log.e(TAG, "init onesdk failed : " + message);
//            }
//        });
        // 获取MAN服务
//        MANService manService = AlibabaSDK.getService(MANService.class);

        /* 【注意】建议您在Application中初始化OneSDK，以保证正常获取MANService*/

        // OneSDK的初始化方法之一
        AlibabaSDK.asyncInit(getApplicationContext());

        // 获取MAN服务
        MANService manService = AlibabaSDK.getService(MANService.class);

        // 若需要关闭 SDK 的自动异常捕获功能可进行如下操作,详见文档5.4
        // manService.getMANAnalytics().turnOffCrashHandler();

        // 通过此接口关闭页面自动打点功能，详见文档4.2
        // manService.getMANAnalytics().turnOffAutoPageTrack();

        // 打开调试日志
        manService.getMANAnalytics().turnOnDebug();

        // 设置渠道（用以标记该app的分发渠道名称），如果不关心可以不设置即不调用该接口，渠道设置将影响控制台【渠道分析】栏目的报表展现。如果文档3.3章节更能满足您渠道配置的需求，就不要调用此方法，按照3.3进行配置即可
        // manService.getMANAnalytics().setChannel("某渠道");

        // 若AndroidManifest.xml 中的 android:versionName 不能满足需求，可在此指定
        // 若在上述两个地方均没有设置appversion，上报的字段默认为null
        // manService.getMANAnalytics().setAppVersion("3.1.1");
    }
    private void setUpUUID() {
        String uuid = SPHelper.getDefaultString(this,SPHelper.KEY_UUID,"");
        if (TextUtils.isEmpty(uuid)) {
            uuid = new DeviceUUIDFactory(this).getDeviceUuid().toString();
            SPHelper.putDefaultString(this, SPHelper.KEY_UUID, uuid);
        }
        //LogUtils.i("uuid======"+ uuid);
    }
}
