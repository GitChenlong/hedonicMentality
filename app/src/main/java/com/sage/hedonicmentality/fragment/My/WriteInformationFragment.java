package com.sage.hedonicmentality.fragment.My;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.app.Http;
import com.sage.hedonicmentality.app.NavigationAc;
import com.sage.hedonicmentality.view.CnsultTimePopWindow;
import com.sage.hedonicmentality.view.EducationPopWindow;
import com.sage.hedonicmentality.view.SubmittedSuccessfullyDiaLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/6/16.
 */
public class WriteInformationFragment extends Fragment {
    @Bind(R.id.tv_title)
    TextView title;
    @Bind(R.id.txt_education)
    TextView txt_education;
    @Bind(R.id.txt_territory)
    TextView txt_territory;
    private String[] educationList ={"博士","硕士","大学","高中","初中","小学"};
    private String[] territoryList ={"不限","婚恋情感","情绪压力","亲子教育","职场发展","自我成长"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.writeinformationfragment, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        title.setText(R.string.writeinfomantion);
        getData();
    }

    @OnClick({R.id.ll_left, R.id.rl_territory, R.id.rl_education, R.id.txt_xiyi,R.id.btn_commit})
    public void healthOnclick(View v) {
        if (v.getId() == R.id.ll_left) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
        if (v.getId() == R.id.rl_territory) {//最高学历
            showPop(territoryList,2,getView().findViewById(R.id.iv_territory));
        }
        if (v.getId() == R.id.rl_education) {//咨询领域
            showPop(educationList, 1,getView().findViewById(R.id.iv_education));
        }
        if (v.getId() == R.id.txt_xiyi) {//协议

        }
        if (v.getId() == R.id.btn_commit) {
            NavigationAc.addFr(new CommitOrderFragment(), "CommitOrderFragment", getFragmentManager(), 1);
        }

    }

    public void showPop(String[] data, int type,View view) {
        EducationPopWindow popWindow = new EducationPopWindow(getActivity(), mHandler, data, type);
        popWindow.showAsDropDown(view);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    txt_education.setText(msg.getData().getString("data"));
                    break;
                case 2:
                    txt_territory.setText(msg.getData().getString("data"));
                    break;
            }
        }
    };
    @Override
         public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    public void getData(){
        List<String> list = new ArrayList<>();
        list.add("20");
        list.add("100");
        list.add("99");
        Http.getHealth("", "", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });
    }
}
