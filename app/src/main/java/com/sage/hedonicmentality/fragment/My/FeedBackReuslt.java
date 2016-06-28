package com.sage.hedonicmentality.fragment.My;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.app.Http;
import com.sage.hedonicmentality.view.SubmittedSuccessfullyDiaLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/6/16.
 */
public class FeedBackReuslt extends Fragment {
    @Bind(R.id.tv_title)
    TextView title;
    @Bind(R.id.ll_success)
    LinearLayout ll_success;
    @Bind(R.id.ll_failure)
    LinearLayout ll_failure;
    private int type=1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.feedbackreuslt,null);
        ButterKnife.bind(this, view);

        if (type==1) { // 反馈结果类型
            ll_success.setVisibility(View.VISIBLE);
            ll_failure.setVisibility(View.GONE);
        }else{
            ll_success.setVisibility(View.GONE);
            ll_failure.setVisibility(View.VISIBLE);

        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        title.setText(R.string.feedbackreuslt);
        getData();
    }

    @OnClick({R.id.ll_left})
    public void healthOnclick(View v) {
        if (v.getId()==R.id.ll_left) {
            getActivity().getSupportFragmentManager().popBackStack();
        }

    }

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
