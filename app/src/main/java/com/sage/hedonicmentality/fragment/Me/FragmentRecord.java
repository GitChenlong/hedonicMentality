package com.sage.hedonicmentality.fragment.Me;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.adapter.ExpRecordAdapter;
import com.sage.hedonicmentality.app.Http;
import com.sage.hedonicmentality.bean.HRVData;
import com.sage.hedonicmentality.bean.HRVDataBase;
import com.sage.hedonicmentality.bean.ParentHrv;
import com.sage.hedonicmentality.fragment.BaseFragment;
import com.sage.hedonicmentality.utils.Contact;
import com.sage.hedonicmentality.utils.SPHelper;
import com.sage.hedonicmentality.utils.SharedPreferencesHelper;
import com.sage.hedonicmentality.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Sage on 2015/7/29.
 */
public class FragmentRecord extends BaseFragment {
    @Bind(R.id.exp_lv)
    ExpandableListView exp_lv;
    @Bind(R.id.iv_empty)
    ImageView iv_empty;
    @Bind(R.id.layout_actionbar)
    RelativeLayout layout_actionbar;
     DbUtils dbUtils;
    private boolean isGuest=true;
    @Override
    public int getLayout() {
        return R.layout.fragment_record;
    }

    @Override
    public void initActionbar() {
        tv_title.setText(R.string.title_breath_record);
        tv_title.setTextColor(getResources().getColor(R.color.wh));
        layout_actionbar.setBackgroundResource(R.color.bg_title);
        btn_left.setImageResource(R.mipmap.back_01);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dbUtils=Util.getDbUtils(getActivity());

        exp_lv.setEmptyView(iv_empty);
        exp_lv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);/**使虚线生效*/
        try {
            getData();
        } catch (DbException e) {
            e.printStackTrace();
        }

        SharedPreferencesHelper.getInstance().Builder(getActivity());
        String ID = SharedPreferencesHelper.getInstance().getString(Contact.SH_ID);
        isGuest=TextUtils.isEmpty(ID);

        upLoadData();
    }

    private int count=0;
    private int size=0;
    private void upLoadData(){
        try {
            List<HRVData> dbModels = dbUtils.findAll(Selector.from(HRVData.class).where("upload","=","0"));
            if(dbModels!=null&&dbModels.size()>0){
                size=dbModels.size();
                for(int i=0;i<size;i++){
                    uploadHRV(dbModels.get(i),getActivity());
                }
            }else{
                //本地没有需要上传的   判断是否和本地MD5相同，相同则直接不搜索
                Util.showProgressFor(getActivity(),getResources().getString(R.string.jiaoyan));
                String actionIDs="";
                try {
                    List<DbModel> dbModels1 = dbUtils.findDbModelAll(Selector.from(HRVData.class).select("actionid"));
                    if(dbModels!=null&&dbModels.size()>0){
                        for(DbModel dbModel:dbModels1){
                            actionIDs+=dbModel.getLong("actionid")+",";
                        }
                        actionIDs=actionIDs.substring(0,actionIDs.length()-1);
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }
                if(isGuest){
                    Http.getHrvListForGuest(getActivity(), actionIDs, isMD5CallBack,true);
                }else {
                    Http.getHrvList(getActivity(),actionIDs, isMD5CallBack,true);
                }
//                uploadEnd();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        System.err.println("size="+size);
    }

    /**上传数据*/
    private void uploadHRV(final HRVData lastData, final Context context) {

        if(!isGuest){
            Http.saveHrv(getActivity(), lastData, new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            count++;
                            LogUtils.i("saveHrv="+"onSuccess="+responseInfo.result);

                            lastData.upload=1;
                            try {
                                dbUtils.update(lastData,"upload");
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                            uploadEnd();
                        }
                        @Override
                        public void onFailure(HttpException e, String s) {
                            count++;
                            LogUtils.i("saveHrv="+"onFailure==="+s);
                            uploadEnd();
                        }
                    });
        }else{
            Http.saveHrvForGuest(getActivity(), lastData, new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            count++;
                            LogUtils.i("saveHrv="+ "onSuccess=" + responseInfo.result);

                            lastData.upload = 1;
                            try {
                                dbUtils.update(lastData, "upload");
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                            uploadEnd();
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            count++;
                            LogUtils.i("saveHrv="+ "onFailure===" + s);
                            uploadEnd();
                        }
                    });
        }


    }


    /**本地数据上传结束，不轮成功与失败*/
    private void uploadEnd(){
        if(count==size){
            downLoadHRV();
        }
    }

    private void downLoadHRV(){
        Util.showProgressFor(getActivity(),getResources().getString(R.string.longdings));
        String actionIDs="";
        try {
            List<DbModel> dbModels = dbUtils.findDbModelAll(Selector.from(HRVData.class).select("actionid"));
            if(dbModels!=null&&dbModels.size()>0){
                for(DbModel dbModel:dbModels){
                    actionIDs+=dbModel.getLong("actionid")+",";
                }
                actionIDs=actionIDs.substring(0,actionIDs.length()-1);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        if(isGuest){
            Http.getHrvListForGuest(getActivity(), actionIDs, downCallBack,false);
        }else {
            Http.getHrvList(getActivity(),actionIDs, downCallBack,false);
        }

    }
    private RequestCallBack<String> isMD5CallBack=new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            Util.cancelProgressFor(getActivity());
            LogUtils.i("getHrvList=="+"onSuccess="+responseInfo.result);
            try {
//                HRVDataBase root=new Gson().fromJson(responseInfo.result,HRVDataBase.class);
                JSONObject  jo = new JSONObject(responseInfo.result);
                if(jo.getInt("info")==1){
                    String md5 = SharedPreferencesHelper.getInstance().getString(Contact.SH_JLMD5);
                    if(!md5.equals(jo.getString("md5"))){
                        SharedPreferencesHelper.getInstance().putString(Contact.SH_JLMD5, jo.getString("md5"));
                        uploadEnd();
                    }
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
            Util.cancelProgressFor(getActivity());
        }
    };

    private RequestCallBack<String> downCallBack=new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            Util.cancelProgressFor(getActivity());
            LogUtils.i("getHrvList=="+"onSuccess="+responseInfo.result);
            try {
                HRVDataBase root=new Gson().fromJson(responseInfo.result,HRVDataBase.class);
                if(root.info==0){
                    LogUtils.i("getHrvList=="+"info0="+root.tip);
                }
                if(root!=null){
                    if(root.data!=null){
                        for (HRVData hrvData:root.data){
                            try {
                                hrvData.upload=1;
                                hrvData.setActionId(hrvData.actionid);
                                if(hrvData.getTerminal().startsWith("ios")){
                                    hrvData.setActionId(hrvData.actionid*1000);
                                }
                                dbUtils.save(hrvData);
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }
                        //更新数据
                        try {
                            getData();
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
            Util.cancelProgressFor(getActivity());
            LogUtils.i("getHrvList==" + "onFailure=" + s);
        }
    };

    private void getData() throws DbException {
         List<ParentHrv> parent=new ArrayList<>();
        DbUtils dbUtils= Util.getDbUtils(getActivity());
        List<DbModel> list=dbUtils.findDbModelAll(Selector.from(HRVData.class).groupBy("day").orderBy("actionid",true)
                .select("month","day","count(day) as count"));
    if(list!=null){
        String lastMonth="";
        for(int i=0;i<list.size();i++){
            DbModel dbModel=list.get(i);
            ParentHrv parentHrv=new ParentHrv();
            parentHrv.month=dbModel.getString("month");
            parentHrv.day=dbModel.getString("day");
            parentHrv.size=dbModel.getInt("count");
            List<HRVData> list1 = dbUtils.findAll(Selector.from(HRVData.class).orderBy("actionid", true)
                    .where("day", "=", parentHrv.day));
            int sumtime = 0;
            int sumhp = 0;
            float sumhrv = 0;
            if(list1.size()>0){
                //添加当天总数
                for (int j=0; j< list1.size();j++){
                    sumtime = sumtime+list1.get(j).interval;
                    sumhp =  sumhp+list1.get(j).score;
                    sumhrv = sumhrv+list1.get(j).hr;
                }
                HRVData hd = new HRVData();
                hd.setActionId(sumtime+list1.get(0).getActionid());
                hd.setHr((int)sumhrv/list1.size());
                hd.setInterval(sumtime);
                hd.setScore(sumhp);
                parentHrv.list.add(hd);
            }

            parentHrv.list.addAll(list1);
            if(lastMonth.equals(dbModel.getString("month"))){

            }else{
                parentHrv.showHeader=true;
                lastMonth=dbModel.getString("month");
            }
            if(i==0){
                if(dbModel.getInt("count")<=3){
                    parentHrv.type=0;
                }else{
                    parentHrv.type=1;
                }
                parentHrv.isFirst=true;
            }else{
                if(dbModel.getInt("count")>0){
                    parentHrv.type=1;
                }else {
                    parentHrv.type=0;
                }
            }
            parent.add(parentHrv);
        }

    }else{
        LogUtils.i("-------" + "list null");
    }
        ExpRecordAdapter adapter=new ExpRecordAdapter(getActivity());
        adapter.setParentHrvs(parent);
        exp_lv.setAdapter(adapter);
        for(int i=0;i<parent.size();i++){
            exp_lv.expandGroup(i);
        }
    }

}
