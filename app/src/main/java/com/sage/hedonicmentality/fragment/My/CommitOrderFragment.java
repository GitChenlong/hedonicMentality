package com.sage.hedonicmentality.fragment.My;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.app.Http;
import com.sage.hedonicmentality.utils.Util;
import com.sage.hedonicmentality.view.SubmittedSuccessfullyDiaLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/6/15.
 */
public class CommitOrderFragment extends Fragment {
    @Bind(R.id.tv_title)
    TextView title;
    @Bind(R.id.residue_times)
    TextView residue_times;//订单守护时间
    @Bind(R.id.iv_checked)
    ImageView iv_checked;
    @Bind(R.id.iv_zhifubao_checked)
    ImageView iv_zhifubao_checked;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.commitorderfragment,null);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        title.setText(R.string.commit_order);
        getData();
    }

    @OnClick({R.id.ll_left,R.id.ll_weixin,R.id.ll_zhifubao,R.id.btn_commit})
    public void healthOnclick(View v) {
        if (v.getId()==R.id.ll_left) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
        if (v.getId()==R.id.ll_weixin) {
            iv_checked.setVisibility(View.VISIBLE);
            iv_zhifubao_checked.setVisibility(View.INVISIBLE);
        }
        if (v.getId()==R.id.ll_zhifubao) {
            iv_checked.setVisibility(View.INVISIBLE);
            iv_zhifubao_checked.setVisibility(View.VISIBLE);

        }
        if (v.getId()==R.id.btn_commit) {
            Util.showCommitSuccess(getActivity(),getString(R.string.paysuccess));
        }

    }    @Override
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
