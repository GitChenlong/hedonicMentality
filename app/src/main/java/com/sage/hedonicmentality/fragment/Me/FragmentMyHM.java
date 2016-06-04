package com.sage.hedonicmentality.fragment.Me;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.app.Http;
import com.sage.hedonicmentality.bean.HMRecord;
import com.sage.hedonicmentality.fragment.BaseFragment;
import com.sage.hedonicmentality.ui.ActivityMe;
import com.sage.hedonicmentality.utils.GsonTools;
import com.sage.hedonicmentality.utils.TimeUtil;
import com.sage.hedonicmentality.utils.Util;
import com.sage.hedonicmentality.utils.UtilSnackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/11/6.
 */
public class FragmentMyHM extends BaseFragment {
    @Bind(R.id.layout_actionbar)
    RelativeLayout layout_actionbar;
    @Bind(R.id.list_jk)
    ListView list_jk;

    public static List<HMRecord> myhmdata = new ArrayList<HMRecord>();

    public MyHMAdapter myWrongAdapter;
    @Override
    public int getLayout() {
        return R.layout.fragment_myhm;
    }

    @Override
    public void initActionbar() {
        layout_actionbar.setBackgroundResource(R.color.bg_title);
        tv_title.setText(R.string.myjkgl);
        tv_title.setTextColor(getResources().getColor(R.color.whi));
        btn_left.setImageResource(R.mipmap.back_01);
        btn_rigth.setImageResource(R.mipmap.curve);
        btn_rigth.setVisibility(View.VISIBLE);
        myhmdata.clear();
        Getindex();
    }
    @OnClick({R.id.hm_zhuyi,R.id.btn_right})
    public void meClick(View view){
        switch (view.getId()){
            case R.id.hm_zhuyi:
                if (getActivity() == null) {
                    return;
                }
                ((ActivityMe) getActivity()).changePage(11);
                break;
            case R.id.btn_right:
                if (getActivity() == null) {
                    return;
                }
                if(myhmdata.size() == 0){

                    return;
                }
                ((ActivityMe) getActivity()).changePage(16);
                break;

        }
    }


    private void Getindex(){
        Util.showProgressFor(getActivity(), getResources().getString(R.string.longdings));
        Http.Getindex(getActivity(), new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Util.cancelProgressFor(getActivity());
                try {
                    LogUtils.i("versionCheck==" + "onSuccess==" + responseInfo.result);
                    JSONObject object = new JSONObject(responseInfo.result);
                    int sertime = object.getInt("systemtime");
                    if (object.getInt("info") == 2) {
                        List<HMRecord> hmdata1 =  new  ArrayList<HMRecord>();
                        HMRecord hm = new HMRecord();
                        hm.setCreatetime(Long.parseLong(sertime + ""));
                        hmdata1.add(hm);
                        myWrongAdapter = new MyHMAdapter(getActivity(), hmdata1);
                        list_jk.setAdapter(myWrongAdapter);
                    } else {
                        JSONArray data = object.getJSONArray("data");
                        List<HMRecord> hmdata = GsonTools.fromJsonArray(data.toString(), HMRecord.class);
                        myhmdata.clear();
                        myhmdata.addAll(hmdata);
//                        for(int i = 0; i <hmdata.size(); i++){
//                            HMRecord h =  hmdata.get((hmdata.size() - 1) - i);
//                            myhmdata.add(h);
//                        }
                        List<HMRecord> hmdata1 =  new  ArrayList<HMRecord>();
                        if (hmdata.size() > 0) {
                            int cmd =  isNewMin(sertime+"" , hmdata.get(0));
                            if (cmd == 1) {
                                HMRecord hm = new HMRecord();
                                hm.setCreatetime(Long.parseLong(sertime + ""));
                                hm.setId("0");
                                hmdata1.add(hm);
                            }
                            hmdata1.addAll(hmdata);
                            myWrongAdapter = new MyHMAdapter(getActivity(), hmdata1);
                            list_jk.setAdapter(myWrongAdapter);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Util.cancelProgressFor(getActivity());
                LogUtils.i("versionCheck==" + "onFailure==" + s);
            }
        });
    }
    public int isNewMin(String nowmin ,HMRecord hmRecord){
        String min = TimeUtil.getStringNowMin(hmRecord.getCreatetime());

        //先判断最新数据，年份是否小于当先年份
        if(Integer.parseInt(TimeUtil.getStringNowYear(hmRecord.getCreatetime())) < Integer.parseInt(TimeUtil.getStringNowYear(Long.parseLong(nowmin)))){
            return 1;
        }else {//比较月份
            if (Integer.parseInt(TimeUtil.getStringNowMin(hmRecord.getCreatetime())) < Integer.parseInt(TimeUtil.getStringNowMin(Long.parseLong(nowmin)))) {
                return 1;
            } else {
                return -1;
            }
        }
    }
    public class MyHMAdapter extends BaseAdapter {
        private List<HMRecord> mlist;
        private Context mcontext;
        private LayoutInflater Inflater;

        public MyHMAdapter(Context context, List<HMRecord> list) {
            // TODO Auto-generated constructor stub
            this.Inflater = LayoutInflater.from(context);
            this.mlist = list;
            this.mcontext = context;
        }


        @Override
        public int getCount() {

            return this.mlist == null ? 0 : this.mlist.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return mlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup arg2) {
            // TODO Auto-generated method stub
            final TextView tv_hmdate;
            final TextView tv_hmtime;
            final TextView tv_hmsubmit;
            final TextView hm_date;
            final TextView tv_sleepshow;
            final TextView tv_weishow;
            final TextView tv_sdpshow;
            final TextView tv_ddpshow;
            final ImageView iv_editing;
            final ImageView iv_sleep1;
            final ImageView iv_sleep2;
            final ImageView iv_sleep3;
            final ImageView iv_sleep4;
            final ImageView iv_sleep5;
            final ImageView iv_wei1;
            final ImageView iv_wei2;
            final ImageView iv_wei3;
            final ImageView iv_wei4;
            final ImageView iv_wei5;
             final EditText ed_hmsdp;
             final EditText ed_hmddp;
            switch (mlist.get(position).getEditabled()) {
                case 2:
                    convertView = Inflater.inflate(R.layout.hmlist_item,null);
                    tv_hmdate = (TextView)convertView.findViewById(R.id.tv_hmdate);
                    tv_hmtime = (TextView)convertView.findViewById(R.id.tv_hmtime);
                    iv_sleep1 = (ImageView)convertView.findViewById(R.id.iv_sleep1);
                    iv_sleep2 = (ImageView)convertView.findViewById(R.id.iv_sleep2);
                    iv_sleep3 = (ImageView)convertView.findViewById(R.id.iv_sleep3);
                    iv_sleep4 = (ImageView)convertView.findViewById(R.id.iv_sleep4);
                    iv_sleep5 = (ImageView)convertView.findViewById(R.id.iv_sleep5);
                    iv_wei1 = (ImageView)convertView.findViewById(R.id.iv_wei1);
                    iv_wei2 = (ImageView)convertView.findViewById(R.id.iv_wei2);
                    iv_wei3 = (ImageView)convertView.findViewById(R.id.iv_wei3);
                    iv_wei4 = (ImageView)convertView.findViewById(R.id.iv_wei4);
                    iv_wei5 = (ImageView)convertView.findViewById(R.id.iv_wei5);
                    ed_hmsdp = (EditText)convertView.findViewById(R.id.ed_hmsdp);
                    ed_hmddp = (EditText)convertView.findViewById(R.id.ed_hmddp);
                    tv_hmsubmit = (TextView)convertView.findViewById(R.id.tv_hmsubmit);
                    tv_hmdate.setText(TimeUtil.getStringNowYear(mlist.get(position).getCreatetime()));//
                    tv_hmtime.setText(TimeUtil.getStringNowMin(mlist.get(position).getCreatetime()));//

                    ed_hmsdp.setInputType(EditorInfo.TYPE_CLASS_PHONE);
                    ed_hmddp.setInputType(EditorInfo.TYPE_CLASS_PHONE);
                    ed_hmsdp.setText(mlist.get(position).getSystolic());
                    ed_hmddp.setText(mlist.get(position).getDiastolic());

                    ed_hmsdp.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            mlist.get(position).setSystolic(ed_hmsdp.getText().toString());
                        }
                    });

                    ed_hmddp.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            mlist.get(position).setDiastolic(ed_hmddp.getText().toString());
                        }
                    });

                    switch (Integer.parseInt(mlist.get(position).getSleep())){
                        case 0:

                            break;
                        case 1:
                            iv_sleep1.setBackgroundResource(R.mipmap.star);
                            iv_sleep2.setBackgroundResource(R.mipmap.star02);
                            iv_sleep3.setBackgroundResource(R.mipmap.star02);
                            iv_sleep4.setBackgroundResource(R.mipmap.star02);
                            iv_sleep5.setBackgroundResource(R.mipmap.star02);
                            break;
                        case 2:
                            iv_sleep1.setBackgroundResource(R.mipmap.star);
                            iv_sleep2.setBackgroundResource(R.mipmap.star);
                            iv_sleep3.setBackgroundResource(R.mipmap.star02);
                            iv_sleep4.setBackgroundResource(R.mipmap.star02);
                            iv_sleep5.setBackgroundResource(R.mipmap.star02);
                            break;
                        case 3:
                            iv_sleep1.setBackgroundResource(R.mipmap.star);
                            iv_sleep2.setBackgroundResource(R.mipmap.star);
                            iv_sleep3.setBackgroundResource(R.mipmap.star);
                            iv_sleep4.setBackgroundResource(R.mipmap.star02);
                            iv_sleep5.setBackgroundResource(R.mipmap.star02);
                            break;
                        case 4:
                            iv_sleep1.setBackgroundResource(R.mipmap.star);
                            iv_sleep2.setBackgroundResource(R.mipmap.star);
                            iv_sleep3.setBackgroundResource(R.mipmap.star);
                            iv_sleep4.setBackgroundResource(R.mipmap.star);
                            iv_sleep5.setBackgroundResource(R.mipmap.star02);
                            break;
                        case 5:
                            iv_sleep1.setBackgroundResource(R.mipmap.star);
                            iv_sleep2.setBackgroundResource(R.mipmap.star);
                            iv_sleep3.setBackgroundResource(R.mipmap.star);
                            iv_sleep4.setBackgroundResource(R.mipmap.star);
                            iv_sleep5.setBackgroundResource(R.mipmap.star);
                            break;
                    }

                    switch (Integer.parseInt(mlist.get(position).getAppetite())){
                        case 0:

                            break;
                        case 1:
                            iv_wei1.setBackgroundResource(R.mipmap.star);
                            iv_wei2.setBackgroundResource(R.mipmap.star02);
                            iv_wei3.setBackgroundResource(R.mipmap.star02);
                            iv_wei4.setBackgroundResource(R.mipmap.star02);
                            iv_wei5.setBackgroundResource(R.mipmap.star02);
                            break;
                        case 2:
                            iv_wei1.setBackgroundResource(R.mipmap.star);
                            iv_wei2.setBackgroundResource(R.mipmap.star);
                            iv_wei3.setBackgroundResource(R.mipmap.star02);
                            iv_wei4.setBackgroundResource(R.mipmap.star02);
                            iv_wei5.setBackgroundResource(R.mipmap.star02);
                            break;
                        case 3:
                            iv_wei1.setBackgroundResource(R.mipmap.star);
                            iv_wei2.setBackgroundResource(R.mipmap.star);
                            iv_wei3.setBackgroundResource(R.mipmap.star);
                            iv_wei4.setBackgroundResource(R.mipmap.star02);
                            iv_wei5.setBackgroundResource(R.mipmap.star02);
                            break;
                        case 4:
                            iv_wei1.setBackgroundResource(R.mipmap.star);
                            iv_wei2.setBackgroundResource(R.mipmap.star);
                            iv_wei3.setBackgroundResource(R.mipmap.star);
                            iv_wei4.setBackgroundResource(R.mipmap.star);
                            iv_wei5.setBackgroundResource(R.mipmap.star02);
                            break;
                        case 5:
                            iv_wei1.setBackgroundResource(R.mipmap.star);
                            iv_wei2.setBackgroundResource(R.mipmap.star);
                            iv_wei3.setBackgroundResource(R.mipmap.star);
                            iv_wei4.setBackgroundResource(R.mipmap.star);
                            iv_wei5.setBackgroundResource(R.mipmap.star);
                            break;
                    }

                    tv_hmsubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if("".equals(ed_hmsdp.getText().toString())||"".equals(ed_hmddp.getText().toString())||"0".equals(mlist.get(position).getSleep())||
                            "0".equals(mlist.get(position).getAppetite())){
                                UtilSnackbar.showSimple(list_jk,getResources().getString(R.string.errshow));
                                    return;
                            }
                            int sdp = Integer.parseInt(ed_hmsdp.getText().toString());
                            int ddp = Integer.parseInt(ed_hmddp.getText().toString());

                            if(sdp < 1 || sdp > 300){
                                UtilSnackbar.showSimple(list_jk,getResources().getString(R.string.hmtosatsdp));
                            }else if(ddp < 0 || ddp > 250){
                                UtilSnackbar.showSimple(list_jk,getResources().getString(R.string.hmtosatddp));
                            }else {
                                mlist.get(position).setSystolic(ed_hmsdp.getText().toString());
                                mlist.get(position).setDiastolic(ed_hmddp.getText().toString());
                                Http.PostSavehealthrecords(getActivity(), mlist.get(position), new RequestCallBack<String>() {
                                    @Override
                                    public void onSuccess(ResponseInfo<String> responseInfo) {
                                        Util.cancelProgressFor(getActivity());
                                        try {
                                            JSONObject object = new JSONObject(responseInfo.result);
                                            if (object.getInt("info") == 0) {
                                                UtilSnackbar.showSimple(list_jk, object.getString("tip"));
                                            } else {
                                                Getindex();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onFailure(HttpException e, String s) {
                                        Util.cancelProgressFor(getActivity());
                                    }
                                });
                            }
                        }
                    });
                    iv_sleep1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mlist.get(position).setSleep("1");
                            iv_sleep1.setBackgroundResource(R.mipmap.star);
                            iv_sleep2.setBackgroundResource(R.mipmap.star02);
                            iv_sleep3.setBackgroundResource(R.mipmap.star02);
                            iv_sleep4.setBackgroundResource(R.mipmap.star02);
                            iv_sleep5.setBackgroundResource(R.mipmap.star02);
                        }
                    });
                    iv_sleep2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mlist.get(position).setSleep("2");
                            iv_sleep1.setBackgroundResource(R.mipmap.star);
                            iv_sleep2.setBackgroundResource(R.mipmap.star);
                            iv_sleep3.setBackgroundResource(R.mipmap.star02);
                            iv_sleep4.setBackgroundResource(R.mipmap.star02);
                            iv_sleep5.setBackgroundResource(R.mipmap.star02);
                        }
                    });
                    iv_sleep3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mlist.get(position).setSleep("3");
                            iv_sleep1.setBackgroundResource(R.mipmap.star);
                            iv_sleep2.setBackgroundResource(R.mipmap.star);
                            iv_sleep3.setBackgroundResource(R.mipmap.star);
                            iv_sleep4.setBackgroundResource(R.mipmap.star02);
                            iv_sleep5.setBackgroundResource(R.mipmap.star02);
                        }
                    });
                    iv_sleep4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mlist.get(position).setSleep("4");
                            iv_sleep1.setBackgroundResource(R.mipmap.star);
                            iv_sleep2.setBackgroundResource(R.mipmap.star);
                            iv_sleep3.setBackgroundResource(R.mipmap.star);
                            iv_sleep4.setBackgroundResource(R.mipmap.star);
                            iv_sleep5.setBackgroundResource(R.mipmap.star02);
                        }
                    });
                    iv_sleep5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mlist.get(position).setSleep("5");
                            iv_sleep1.setBackgroundResource(R.mipmap.star);
                            iv_sleep2.setBackgroundResource(R.mipmap.star);
                            iv_sleep3.setBackgroundResource(R.mipmap.star);
                            iv_sleep4.setBackgroundResource(R.mipmap.star);
                            iv_sleep5.setBackgroundResource(R.mipmap.star);
                        }
                    });
                    iv_wei1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //����˵��
                            mlist.get(position).setAppetite("1");
                            iv_wei1.setBackgroundResource(R.mipmap.star);
                            iv_wei2.setBackgroundResource(R.mipmap.star02);
                            iv_wei3.setBackgroundResource(R.mipmap.star02);
                            iv_wei4.setBackgroundResource(R.mipmap.star02);
                            iv_wei5.setBackgroundResource(R.mipmap.star02);
                        }
                    });
                    iv_wei2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mlist.get(position).setAppetite("2");
                            iv_wei1.setBackgroundResource(R.mipmap.star);
                            iv_wei2.setBackgroundResource(R.mipmap.star);
                            iv_wei3.setBackgroundResource(R.mipmap.star02);
                            iv_wei4.setBackgroundResource(R.mipmap.star02);
                            iv_wei5.setBackgroundResource(R.mipmap.star02);
                        }
                    });
                    iv_wei3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mlist.get(position).setAppetite("3");
                            iv_wei1.setBackgroundResource(R.mipmap.star);
                            iv_wei2.setBackgroundResource(R.mipmap.star);
                            iv_wei3.setBackgroundResource(R.mipmap.star);
                            iv_wei4.setBackgroundResource(R.mipmap.star02);
                            iv_wei5.setBackgroundResource(R.mipmap.star02);
                        }
                    });
                    iv_wei4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mlist.get(position).setAppetite("4");
                            iv_wei1.setBackgroundResource(R.mipmap.star);
                            iv_wei2.setBackgroundResource(R.mipmap.star);
                            iv_wei3.setBackgroundResource(R.mipmap.star);
                            iv_wei4.setBackgroundResource(R.mipmap.star);
                            iv_wei5.setBackgroundResource(R.mipmap.star02);
                        }
                    });
                    iv_wei5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mlist.get(position).setAppetite("5");
                            iv_wei1.setBackgroundResource(R.mipmap.star);
                            iv_wei2.setBackgroundResource(R.mipmap.star);
                            iv_wei3.setBackgroundResource(R.mipmap.star);
                            iv_wei4.setBackgroundResource(R.mipmap.star);
                            iv_wei5.setBackgroundResource(R.mipmap.star);
                        }
                    });
                    break;
                case 1:
                    convertView = Inflater.inflate(R.layout.hmlistxs_item,null);
                    tv_hmdate = (TextView)convertView.findViewById(R.id.tv_hmdate);
                    tv_hmtime = (TextView)convertView.findViewById(R.id.tv_hmtime);
                    hm_date = (TextView)convertView.findViewById(R.id.hm_date);
                    tv_sleepshow = (TextView)convertView.findViewById(R.id.tv_sleepshow);
                    tv_weishow = (TextView)convertView.findViewById(R.id.tv_weishow);
                    tv_sdpshow = (TextView)convertView.findViewById(R.id.tv_sdpshow);
                    tv_ddpshow = (TextView)convertView.findViewById(R.id.tv_ddpshow);
                    iv_editing = (ImageView)convertView.findViewById(R.id.iv_editing);

                    iv_editing.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mlist.get(position).setEditabled(2);
                            notifyDataSetChanged();
                        }
                    });
                    hm_date.setText(TimeUtil.getStringNowDate(mlist.get(position).getCreatetime()));//��һ�е�ʱ��
                    tv_hmdate.setText(TimeUtil.getStringNowYear(mlist.get(position).getCreatetime()));//���
                    tv_hmtime.setText(TimeUtil.getStringNowMin(mlist.get(position).getCreatetime()));//�·�
                    tv_sdpshow.setText(mlist.get(position).getSystolic()+getResources().getString(R.string.mmhg));
                    tv_ddpshow.setText(mlist.get(position).getDiastolic()+getResources().getString(R.string.mmhg));
                    switch (Integer.parseInt(mlist.get(position).getSleep())){
                        case 0:

                            break;
                        case 1:
                            tv_sleepshow.setText(getResources().getString(R.string.sleep_1));
                            break;
                        case 2:
                            tv_sleepshow.setText(getResources().getString(R.string.sleep_2));
                            break;
                        case 3:
                            tv_sleepshow.setText(getResources().getString(R.string.sleep_3));
                            break;
                        case 4:
                            tv_sleepshow.setText(getResources().getString(R.string.sleep_4));
                            break;
                        case 5:
                            tv_sleepshow.setText(getResources().getString(R.string.sleep_5));
                            break;
                    }

                    switch (Integer.parseInt(mlist.get(position).getAppetite())){
                        case 0:

                            break;
                        case 1:
                            tv_weishow.setText(getResources().getString(R.string.wei_1));
                            break;
                        case 2:
                            tv_weishow.setText(getResources().getString(R.string.wei_2));
                            break;
                        case 3:
                            tv_weishow.setText(getResources().getString(R.string.wei_3));
                            break;
                        case 4:
                            tv_weishow.setText(getResources().getString(R.string.wei_4));
                            break;
                        case 5:
                            tv_weishow.setText(getResources().getString(R.string.wei_5));
                            break;
                    }
                    break;
                case 0:
                    convertView = Inflater.inflate(R.layout.hmlistxs_item,null);
                    tv_hmdate = (TextView)convertView.findViewById(R.id.tv_hmdate);
                    tv_hmtime = (TextView)convertView.findViewById(R.id.tv_hmtime);
                    hm_date = (TextView)convertView.findViewById(R.id.hm_date);
                    tv_sleepshow = (TextView)convertView.findViewById(R.id.tv_sleepshow);
                    tv_weishow = (TextView)convertView.findViewById(R.id.tv_weishow);
                    tv_sdpshow = (TextView)convertView.findViewById(R.id.tv_sdpshow);
                    tv_ddpshow = (TextView)convertView.findViewById(R.id.tv_ddpshow);
                    iv_editing = (ImageView)convertView.findViewById(R.id.iv_editing);

                    iv_editing.setVisibility(View.GONE);
                    hm_date.setText(TimeUtil.getStringNowDate(mlist.get(position).getCreatetime()));//
                    tv_hmdate.setText(TimeUtil.getStringNowYear(mlist.get(position).getCreatetime()));//
                    tv_hmtime.setText(TimeUtil.getStringNowMin(mlist.get(position).getCreatetime()));//
                    tv_sdpshow.setText(mlist.get(position).getSystolic()+getResources().getString(R.string.mmhg));
                    tv_ddpshow.setText(mlist.get(position).getDiastolic()+getResources().getString(R.string.mmhg));
                    switch (Integer.parseInt(mlist.get(position).getSleep())){
                        case 0:

                            break;
                        case 1:
                            tv_sleepshow.setText(getResources().getString(R.string.sleep_1));
                            break;
                        case 2:
                            tv_sleepshow.setText(getResources().getString(R.string.sleep_2));
                            break;
                        case 3:
                            tv_sleepshow.setText(getResources().getString(R.string.sleep_3));
                            break;
                        case 4:
                            tv_sleepshow.setText(getResources().getString(R.string.sleep_4));
                            break;
                        case 5:
                            tv_sleepshow.setText(getResources().getString(R.string.sleep_5));
                            break;
                    }

                    switch (Integer.parseInt(mlist.get(position).getAppetite())){
                        case 0:

                            break;
                        case 1:
                            tv_weishow.setText(getResources().getString(R.string.wei_1));
                            break;
                        case 2:
                            tv_weishow.setText(getResources().getString(R.string.wei_2));
                            break;
                        case 3:
                            tv_weishow.setText(getResources().getString(R.string.wei_3));
                            break;
                        case 4:
                            tv_weishow.setText(getResources().getString(R.string.wei_4));
                            break;
                        case 5:
                            tv_weishow.setText(getResources().getString(R.string.wei_5));
                            break;
                    }
                    break;
            }


            return convertView;

        }

    }
}
