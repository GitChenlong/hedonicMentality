package com.sage.hedonicmentality.fragment.Me;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.man.MANService;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.app.Common;
import com.sage.hedonicmentality.app.Http;
import com.sage.hedonicmentality.app.MyApplication;
import com.sage.hedonicmentality.fragment.BaseFragment;
import com.sage.hedonicmentality.fragment.FragmentPrompt;
import com.sage.hedonicmentality.ui.ActivityLogin;
import com.sage.hedonicmentality.utils.Contact;
import com.sage.hedonicmentality.utils.DataCleanManager;
import com.sage.hedonicmentality.utils.SharedPreferencesHelper;
import com.sage.hedonicmentality.utils.Util;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Sage on 2015/7/29.
 */
public class FragmentSystem extends BaseFragment implements FragmentPrompt.PromptClickListener{
    @Bind(R.id.tv_version)
    TextView tv_version;
    @Bind(R.id.tv_cache)
    TextView tv_cache;
    @Bind(R.id.iv_new_version)
    ImageView iv_new_version;
    @Bind(R.id.layout_actionbar)
    RelativeLayout layout_actionbar;
    @Bind(R.id.iv_headsys)
    ImageView iv_headsys;
    @Bind(R.id.btn_login_out)
    Button btn_login_out;
    @Override
    public int getLayout() {
        return R.layout.fragment_system;
    }

    @Override
    public void initActionbar() {
        layout_actionbar.setBackgroundResource(R.color.bg_title);
        tv_title.setText(R.string.title_system);
        tv_title.setTextColor(getResources().getColor(R.color.whi));
        btn_left.setImageResource(R.mipmap.back_01);
        try {
            tv_cache.setText(DataCleanManager.getCacheSize(getActivity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        SharedPreferencesHelper.getInstance().Builder(getActivity());
        String ID = SharedPreferencesHelper.getInstance().getString(Contact.SH_ID);
        if(!TextUtils.isEmpty(ID)){
            String avatar = SharedPreferencesHelper.getInstance().getString(Contact.SH_Avatar);
            if(avatar.length()>0){
                try {
                    Picasso.with(getActivity()).load(avatar).into(iv_headsys);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        checkVersion();

        SharedPreferencesHelper.getInstance().Builder(getActivity());
        String ID1 =  SharedPreferencesHelper.getInstance().getString(Contact.SH_ID);
        if(!TextUtils.isEmpty(ID1)){

        }else{
            btn_login_out.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.tv_check_version, R.id.tv_clear,R.id.btn_login_out})
    void systemClick(View view){
        switch(view.getId()){
            case R.id.tv_clear:
                    showClear();
                break;
            case R.id.tv_check_version:
                if(url!=null){
                    showCheck();
                }else{
                    Util.showToast(getActivity(),"已是最新版本");
                }

                break;
            case R.id.btn_login_out:/**退出登陆*/
                loginOut();
                break;
        }
    }
    private void loginOut() {
        SharedPreferencesHelper.getInstance().Builder(getActivity());
         String name  = SharedPreferencesHelper.getInstance().getString(Contact.SH_USERNAME,"");
         String id =  SharedPreferencesHelper.getInstance().getString(Contact.SH_ID,"");
        MANService manService = AlibabaSDK.getService(MANService.class);
        // 用户注销埋点
        manService.getMANAnalytics().updateUserAccount(name, id);
        // SharedPreferencesHelper.getInstance().putString(Contact.SH_ID, "");
        SharedPreferencesHelper.getInstance().clear();
        startActivity(new Intent(getActivity(), ActivityLogin.class));
        getActivity().sendBroadcast(new Intent(Common.ACTION_LOGIN_OUT));
    }

    private FragmentPrompt clearFragment;
    private FragmentPrompt checkFragment;

    private void  showClear(){
        if(clearFragment==null){
            clearFragment= FragmentPrompt.create(0);
            clearFragment.setPromptClickListener(this);
        }
        clearFragment.show(getChildFragmentManager(), "clear");

    }

    private void showCheck(){
        if(checkFragment==null){
            checkFragment= FragmentPrompt.create(1);
            checkFragment.setPromptClickListener(this);
        }
        checkFragment.show(getActivity().getSupportFragmentManager(), "check");
    }
    @Override
    public void okClick(int tag) {
        switch(tag){
            case 0:/**清除数据*/
                clearCache();
                break;
            case 1:/**更新*/
               downloading(url);
                break;
        }
    }
    private String url;
    private void checkVersion(){
        Http.versionCheck(getActivity(), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    LogUtils.i("versionCheck=="+"onSuccess==" + responseInfo.result);
                    JSONObject object=new JSONObject(responseInfo.result);
                    if(object.getInt("info")==0){
                        LogUtils.i("getHrvList=="+"tip="+object.getString("tip"));
                    }else {
                        String version=object.getString("version");
                        if(Http.version.compareTo(version)<0){
                            url=object.getString("data");
                            iv_new_version.setVisibility(View.VISIBLE);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                LogUtils.i("versionCheck=="+"onFailure=="+s);
            }
        });
    }
    ProgressDialog progressDialog;
    private void downloading(String url) {
        String apk_dir= Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+ Common.ROOT_DIR+"/"+Common.ROOT_APK_DIR+"/";
        if(!new File(apk_dir).exists()){
            new File(apk_dir).mkdirs();
        }
        final String downPath=apk_dir+url.substring(url.lastIndexOf("/")+1,url.length());
        HttpUtils http = new HttpUtils();
        HttpHandler handler = http.download(url,downPath,
                true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
                false, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
                new RequestCallBack<File>() {
                    @Override
                    public void onStart() {
                        LogUtils.i("download"+ "onStart=");
                         progressDialog=new ProgressDialog(getActivity());
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progressDialog.setMessage("下载中...");
                        progressDialog.show();
                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        progressDialog.setMax((int) (total/1024));
                        progressDialog.setProgress((int) (current/1024));
                        //tv_cache.setText(current+"/"+total+"/"+isUploading);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<File> responseInfo) {
                        try {
                            progressDialog.dismiss();
                            LogUtils.i("download="+"onSuccess="+responseInfo.result.getPath());
                            Util.install(new File(responseInfo.result.getPath()), MyApplication.getContext());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(HttpException error, String msg) {
                        progressDialog.dismiss();
                        LogUtils.i("download="+"onFailure="+msg);
                        if("maybe the file has downloaded completely".equals(msg)){
                            Util.install(new File(downPath), MyApplication.getContext());
                        }
                    }
                });

    }

    private void clearCache() {
        DataCleanManager.cleanInternalCache(getActivity());
        tv_cache.setText("");
    }

    @Override
    public void cancelClick(int tag) {

    }

}
