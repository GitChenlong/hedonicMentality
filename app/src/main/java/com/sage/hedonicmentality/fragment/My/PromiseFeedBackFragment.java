package com.sage.hedonicmentality.fragment.My;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.app.Http;
import com.sage.hedonicmentality.utils.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/6/14.
 */
public class PromiseFeedBackFragment extends Fragment {
    @Bind(R.id.tv_title)
    TextView title;
    @Bind(R.id.ll_teacher)
    LinearLayout ll_teacher;
    @Bind(R.id.ll_user)
    LinearLayout ll_user;
    @Bind(R.id.et_feedback)
    EditText et_feedback;
    private int select = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.promisefeedbackfragment,null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        title.setText(R.string.promisefeedback);
        getData();
    }
    @OnClick({R.id.ll_left,R.id.ll_teacher,R.id.ll_user,R.id.txt_commit})
    public void healthOnclick(View v) {
        if (v.getId()==R.id.ll_left) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
        if (v.getId()==R.id.ll_teacher) {
            select=1;
            ll_teacher.setBackground(getResources().getDrawable(R.drawable.btn_green_check));
            ll_user.setBackground(getResources().getDrawable(R.drawable.bg_white_gray_outer));
        }
        if (v.getId()==R.id.ll_user) {
            select=2;
            ll_teacher.setBackground(getResources().getDrawable(R.drawable.bg_white_gray_outer));
            ll_user.setBackground(getResources().getDrawable(R.drawable.btn_green_check));
        }
        if (v.getId()==R.id.txt_commit) {
            Toast.makeText(getActivity(), et_feedback.getText().toString() +" /" +select,Toast.LENGTH_SHORT).show();
            Util.showCommitSuccess(getActivity(),"提交成功");
        }
    }
    @Override
         public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    public void getData(){
        List<String> list = new ArrayList<>();
        list.add("健康贴士一");
        list.add("健康贴士二");
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
