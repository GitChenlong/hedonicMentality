package com.sage.hedonicmentality.fragment.My;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.adapter.ConsultAdapter;
import com.sage.hedonicmentality.adapter.FragAdapter;
import com.sage.hedonicmentality.utils.SPHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/6/4.
 * 我的咨询
 */
public class MyOrderFragment extends FragmentActivity {
    @Bind(R.id.rb_all)
    RadioButton rb_all;
    @Bind(R.id.rb_wait_consult)
    RadioButton rb_wait_consult;
    @Bind(R.id.rb_wait_evaluate)
    RadioButton rb_wait_evaluate;
    @Bind(R.id.rb_accomplish)
    RadioButton rb_accomplish;
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    @Bind(R.id.iv_line)
    ImageView iv_line;
    @Bind(R.id.rg)
    RadioGroup rg;
    @Bind(R.id.btn_left)
    ImageView btn_left;
    @Bind(R.id.tv_title)
    TextView tv_title;
    private int mCurrentCheckedRadioLeft;
    private List<Fragment> fragments;
    private allFragment allfragment;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myorderfragment);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        addFragments();
        selecteViewPager();
        tv_title.setText("我的咨询");
        tv_title.setTextColor(getResources().getColor(R.color.white_color));
    }

    @OnClick({R.id.ll_left})
    public void myorderOnclick(View v) {
        if (v.getId()==R.id.ll_left) {
            finish();
            int version = Integer.valueOf(android.os.Build.VERSION.SDK);
            if(version  >= 5) {
               overridePendingTransition(R.anim.anim_out_ac, R.anim.anim_finsh_ac);
            }
        }
    }
    public void addFragments(){
        fragments =  getFrList();
        Log.e("size",fragments.size()+"");
        FragAdapter adapter = new FragAdapter(getSupportFragmentManager(), fragments);
        viewpager.setAdapter(adapter);
        viewpager.setOffscreenPageLimit(4);
        viewpager.setCurrentItem(0);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonId = group.getCheckedRadioButtonId();
                //根据ID获取RadioButton的实例
                switch (radioButtonId) {
                    case 0:
                        setAnimation(radioButtonId, rb_all);
                        break;
                    case 1:
                        setAnimation(radioButtonId, rb_wait_consult);
                        break;
                    case 2:
                        setAnimation(radioButtonId, rb_wait_evaluate);
                        break;
                    case 3:
                        setAnimation(radioButtonId, rb_accomplish);
                        break;
                }
            }
        });
    }
    private List<Fragment> getFrList(){
        //构造适配器
        List<Fragment> fragments=new ArrayList<Fragment>();
        fragments.add(new allFragment());
        fragments.add(new waitConsultFragment());
        fragments.add(new waitEvaluateFragment());
        fragments.add(new offTheStocksFragment());
        for (int i=0;i<fragments.size();i++) {
            switch (i){
                case 0:
                    rb_all.setId(i);
                    rb_all.setText("全部咨询");
                    break;
                case 1:
                    rb_wait_consult.setId(i);
                    rb_wait_consult.setText("待咨询");
                    break;
                case 2:
                    rb_wait_evaluate.setText("待评价");
                    rb_wait_evaluate.setId(i);
                    break;
                case 3:
                    rb_accomplish.setId(i);
                    rb_accomplish.setText("已完成");
                    break;

            }
        }
        return  fragments;
    }

    private void setAnimation(int radioButtonId,RadioButton rb){
        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation translateAnimation;

        translateAnimation = new TranslateAnimation(mCurrentCheckedRadioLeft, rb.getLeft(), 0f, 0f);
        animationSet.addAnimation(translateAnimation);
        animationSet.setFillBefore(true);
        animationSet.setFillAfter(true);
        animationSet.setDuration(300);
        iv_line.startAnimation(animationSet);//开始上面蓝色横条图片的动画切换
        viewpager.setCurrentItem(radioButtonId);//让下方ViewPager跟随上面的HorizontalScrollView切换
        mCurrentCheckedRadioLeft  = rb.getLeft();//更新当前蓝色横条距离左边的距离
    }
    private void selecteViewPager(){
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                RadioButton radioButton = (RadioButton)findViewById(position);
                radioButton.performClick();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    public void setTitles(int postion){
        switch (postion){
            case 0:

                break;
            case 1:

                break;
            case 2:
                break;
            case 3:

                break;

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    /**
     * 全部咨询
     * */
    class allFragment extends Fragment{
        @Bind(R.id.lv_forall)
        ListView listView;
        private ConsultAdapter adapter;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = View.inflate(getActivity(),R.layout.allfragment,null);
            ButterKnife.bind(this,view);
            return view;

        }
        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            final List<String> list = new ArrayList<>();
            for (int i=0;i<10;i++) {
                list.add("待评价"+i);
            }
            adapter = new ConsultAdapter(getActivity(),list);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getActivity(), list.get(position), Toast.LENGTH_SHORT).show();
                }
            });
        }
        @Override
        public void onDestroy() {
            super.onDestroy();
            ButterKnife.unbind(this);
        }
    }
    /**
     * 待咨询
     * */
    class waitConsultFragment extends Fragment{
        @Bind(R.id.lv_forall)
        ListView listView;
        private ConsultAdapter adapter;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = View.inflate(getActivity(),R.layout.allfragment,null);
            ButterKnife.bind(this,view);
            final List<String> list = new ArrayList<>();
            for (int i=0;i<10;i++) {
                list.add("待评价"+i);
            }
            adapter = new ConsultAdapter(getActivity(),list);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getActivity(), list.get(position), Toast.LENGTH_SHORT).show();
                }
            });
            return view;

        }
        @Override
        public void onDestroy() {
            super.onDestroy();
            ButterKnife.unbind(this);
        }
    }
    /**
     * 待评价
     * */
    class waitEvaluateFragment extends Fragment{
        @Bind(R.id.lv_forall)
        ListView listView;
        private ConsultAdapter adapter;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = View.inflate(getActivity(),R.layout.allfragment,null);
            ButterKnife.bind(this, view);

            return view;

        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            final List<String> list = new ArrayList<>();
            for (int i=0;i<10;i++) {
                list.add("待评价"+i);
            }
            adapter = new ConsultAdapter(getActivity(),list);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getActivity(), list.get(position), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            ButterKnife.unbind(this);
        }
    }
    /**
     * 已完成
     * */
    class offTheStocksFragment extends Fragment{
        @Bind(R.id.lv_forall)
        ListView listView;
        private ConsultAdapter adapter;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = View.inflate(getActivity(),R.layout.allfragment,null);
            ButterKnife.bind(this, view);
            return view;

        }
        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            final List<String> list = new ArrayList<>();
            for (int i=0;i<10;i++) {
                list.add("待评价"+i);
            }
            adapter = new ConsultAdapter(getActivity(),list);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getActivity(), list.get(position), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            ButterKnife.unbind(this);
        }
    }
}
