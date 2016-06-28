package com.sage.hedonicmentality.fragment.My;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.app.Http;
import com.sage.hedonicmentality.view.SubmittedSuccessfullyDiaLog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/6/13.
 */
public class FeedBackFragment extends Fragment {
    @Bind(R.id.rg_title)
    RadioGroup rg_title;
    @Bind(R.id.fl_feedback)
    FrameLayout fl_feedback;
    @Bind(R.id. rb_feedback)
    RadioButton rb_feedback;
    @Bind(R.id. rb_question)
    RadioButton rb_question;
    private questionFragment questionFr;
    private feedbackFragment feedbackFr;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.feedbackfragment,null);
        ButterKnife.bind(this, view);
        rg_title.setOnCheckedChangeListener(MyCheckedChangeListener);
        init(view);
        return view;
    }
    public void init(View view){
        rb_feedback.setChecked(true);
    }

    RadioGroup.OnCheckedChangeListener MyCheckedChangeListener =new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId) {
                case R.id.rb_question:
                    rb_question.setBackground(getResources().getDrawable(R.drawable.btn_white));
                    rb_feedback.setBackground(getResources().getDrawable(R.drawable.btn_lucency_green));
                    rb_question.setTextColor(getResources().getColor(R.color.green_essential_colour));
                    rb_feedback.setTextColor(getResources().getColor(R.color.selector_white));
                    setTab(1);
                    break;
                case R.id.rb_feedback:
                    rb_question.setTextColor(getResources().getColor(R.color.selector_white));
                    rb_feedback.setTextColor(getResources().getColor(R.color.green_essential_colour));
                    rb_question.setBackground(getResources().getDrawable(R.drawable.btn_lucency_green));
                    rb_feedback.setBackground(getResources().getDrawable(R.drawable.btn_white));
                    setTab(2);
                    break;
            }
        }
        };
    public void setTab(int type){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction bt = fm.beginTransaction();
        switch (type){
            case 1:
                if (questionFr==null) {
                    questionFr =new questionFragment();
                    bt.add(R.id.fl_feedback,questionFr);
                }else{
                    bt.show(questionFr);
                }
                if (feedbackFr!=null) {
                    bt.hide(feedbackFr);
                }

                break;

            case 2:
                if (feedbackFr==null) {
                    feedbackFr =new feedbackFragment();
                    bt.add(R.id.fl_feedback,feedbackFr);
                }else{
                    bt.show(feedbackFr);
                }
                if (questionFr!=null) {
                    bt.hide(questionFr);
                }
                break;

        }
        bt.commit();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @OnClick({R.id.ll_left})
    public void healthOnclick(View v) {
        if (v.getId()==R.id.ll_left) {
            getActivity().getSupportFragmentManager().popBackStack();
        }

    }    @Override
         public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    public void getData() {
        Http.getHealth("", "", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

            }
            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });
    }

    public class questionFragment extends Fragment{
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = View.inflate(getActivity(), R.layout.questionfragment,null);
            ButterKnife.bind(this, view);
            return view;
        }
    }
    public class feedbackFragment extends Fragment{
        @Bind(R.id.et_feedback)
        EditText et_feedback;
        @Bind(R.id.btn_commit)
        Button btn_commit;
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = View.inflate(getActivity(), R.layout.feedbackchildfragment,null);
            ButterKnife.bind(this, view);
            return view;
        }
        @OnClick(R.id.btn_commit)
        public void feedbackOnclick(View v) {
            if (v.getId()==R.id.btn_commit) {
                et_feedback.getText();
                showCommitSuccess();
                getFragmentManager().popBackStack();
            }
        }
        public void showCommitSuccess(){
            SubmittedSuccessfullyDiaLog diaLog = new SubmittedSuccessfullyDiaLog(getActivity());
            diaLog.show();
        }
    }
}
