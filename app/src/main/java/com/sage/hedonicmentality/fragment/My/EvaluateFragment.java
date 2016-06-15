package com.sage.hedonicmentality.fragment.My;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.app.Http;
import com.sage.hedonicmentality.app.NavigationAc;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/6/15.
 */
public class EvaluateFragment extends Fragment {
    @Bind(R.id.tv_title)
    TextView title;
    @Bind(R.id.rg_manner)
    LinearLayout rg_manner;
    @Bind(R.id.rg_result)
    LinearLayout rg_result;
    @Bind(R.id.rg_flower)
    LinearLayout rg_flower;
    @Bind(R.id.rb_manner_one)
    ImageView rb_manner_one;
    @Bind(R.id.rb_manner_two)
    ImageView rb_manner_two;
    @Bind(R.id.rb_manner_three)
    ImageView rb_manner_three;
    @Bind(R.id.rb_manner_four)
    ImageView rb_manner_four;
    @Bind(R.id.rb_manner_five)
    ImageView rb_manner_five;
    @Bind(R.id.rb_result_one)
    ImageView rb_result_one;
    @Bind(R.id.rb_result_two)
    ImageView rb_result_two;
    @Bind(R.id.rb_result_three)
    ImageView rb_result_three;
    @Bind(R.id.rb_result_four)
    ImageView rb_result_four;
    @Bind(R.id.rb_result_five)
    ImageView rb_result_five;
    @Bind(R.id.rb_flower_one)
    ImageView rb_flower_one;
    @Bind(R.id.rb_flower_two)
    ImageView rb_flower_two;
    @Bind(R.id.rb_flower_three)
    ImageView rb_flower_three;
    @Bind(R.id.rb_flower_four)
    ImageView rb_flower_four;
    @Bind(R.id.rb_flower_five)
    ImageView rb_flower_five;
    @Bind(R.id.rb_yes)
    ImageView rb_yes;
    @Bind(R.id.rb_no)
    ImageView rb_no;
    private static int isPunctuality = 0;//是否准时咨询 0：是 、1：否
    private static int mannerCount = 1;//咨询态度星级
    private static int resultCount = 1;//咨询效果星级
    private static int flowerCount = 0;//赠送鲜花数量
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.evaluatefragment,null);
        ButterKnife.bind(this, view);
        return view;
    }
    @OnClick({R.id.ll_left,R.id.rb_no,R.id.rb_yes,R.id.rb_manner_one,R.id.rb_manner_two,R.id.rb_manner_three,R.id.rb_manner_four,R.id.rb_manner_five,
            R.id.rb_result_one,R.id.rb_result_two,R.id.rb_result_three,R.id.rb_result_four,R.id.rb_result_five,
            R.id.rb_flower_one,R.id.rb_flower_two,R.id.rb_flower_three,R.id.rb_flower_four,R.id.rb_flower_five,
            R.id.btn_commit})

    public void evaluateOnclick(View view) {
        switch (view.getId()){
            case R.id.ll_left:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.rb_yes:
                rb_yes.setImageResource(R.mipmap.choice_higlighted);
                rb_no.setImageResource(R.mipmap.choice_normal);
                isPunctuality=0;
                break;
            case R.id.rb_no:
                rb_yes.setImageResource(R.mipmap.choice_normal);
                rb_no.setImageResource(R.mipmap.choice_higlighted);
                isPunctuality=1;
                break;
            case  R.id.btn_commit:
                Toast.makeText(getActivity(),"准时咨询:"+isPunctuality+" 态度星级："+mannerCount+" 效果星级:"+resultCount+" 赠送鲜花:"+flowerCount,Toast.LENGTH_SHORT).show();
                backToNavigationAc();
                break;
            case R.id.rb_manner_one:
                setMannerRaidoChecked(1,R.id.rg_manner);
                break;
            case R.id.rb_manner_two:
                setMannerRaidoChecked(2,R.id.rg_manner);
                break;
            case R.id.rb_manner_three:
                setMannerRaidoChecked(3,R.id.rg_manner);
                break;
            case R.id.rb_manner_four:
                setMannerRaidoChecked(4,R.id.rg_manner);
                break;
            case R.id.rb_manner_five:
                setMannerRaidoChecked(5,R.id.rg_manner);
                break;
            case R.id.rb_result_one:
                setMannerRaidoChecked(1,R.id.rg_result);
                break;
            case R.id.rb_result_two:
                setMannerRaidoChecked(2,R.id.rg_result);
                break;
            case R.id.rb_result_three:
                setMannerRaidoChecked(3,R.id.rg_result);
                break;
            case R.id.rb_result_four:
                setMannerRaidoChecked(4,R.id.rg_result);
                break;
            case R.id.rb_result_five:
                setMannerRaidoChecked(5,R.id.rg_result);
                break;
            case R.id.rb_flower_one:
                setMannerRaidoChecked(1,R.id.rg_flower);
                break;
            case R.id.rb_flower_two:
                setMannerRaidoChecked(2,R.id.rg_flower);
                break;
            case R.id.rb_flower_three:
                setMannerRaidoChecked(3,R.id.rg_flower);
                break;
            case R.id.rb_flower_four:
                setMannerRaidoChecked(4,R.id.rg_flower);
                break;
            case R.id.rb_flower_five:
                setMannerRaidoChecked(5,R.id.rg_flower);
                break;
        }

    }
    public void backToNavigationAc(){
        FragmentManager fm =getActivity().getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {//fm.getBackStackEntryCount()得到栈的大小
            fm.popBackStack(fm.getBackStackEntryAt(0).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
//            NavigationAc navigationac  = (NavigationAc)getActivity();
//            navigationac.viewpager.setCurrentItem(0);
//            navigationac.bottom.setVisibility(View.VISIBLE);
        }
    }
    public void setMannerRaidoChecked(int type,int group) {
        if (group==R.id.rg_manner) {
            switch (type) {
                case 1:
                    mannerCount=1;
                    rb_manner_one.setImageResource(R.mipmap.star_higlighted);
                    rb_manner_two.setImageResource(R.mipmap.star_normal);
                    rb_manner_three.setImageResource(R.mipmap.star_normal);
                    rb_manner_four.setImageResource(R.mipmap.star_normal);
                    rb_manner_five.setImageResource(R.mipmap.star_normal);
                    break;
                case 2:
                    mannerCount=2;
                    rb_manner_one.setImageResource(R.mipmap.star_higlighted);
                    rb_manner_two.setImageResource(R.mipmap.star_higlighted);
                    rb_manner_three.setImageResource(R.mipmap.star_normal);
                    rb_manner_four.setImageResource(R.mipmap.star_normal);
                    rb_manner_five.setImageResource(R.mipmap.star_normal);
                    break;
                case 3:
                    mannerCount=3;
                    rb_manner_one.setImageResource(R.mipmap.star_higlighted);
                    rb_manner_two.setImageResource(R.mipmap.star_higlighted);
                    rb_manner_three.setImageResource(R.mipmap.star_higlighted);
                    rb_manner_four.setImageResource(R.mipmap.star_normal);
                    rb_manner_five.setImageResource(R.mipmap.star_normal);
                    break;
                case 4:
                    mannerCount=4;
                    rb_manner_one.setImageResource(R.mipmap.star_higlighted);
                    rb_manner_two.setImageResource(R.mipmap.star_higlighted);
                    rb_manner_three.setImageResource(R.mipmap.star_higlighted);
                    rb_manner_four.setImageResource(R.mipmap.star_higlighted);
                    rb_manner_five.setImageResource(R.mipmap.star_normal);
                    break;
                case 5:
                    mannerCount=5;
                    rb_manner_one.setImageResource(R.mipmap.star_higlighted);
                    rb_manner_two.setImageResource(R.mipmap.star_higlighted);
                    rb_manner_three.setImageResource(R.mipmap.star_higlighted);
                    rb_manner_four.setImageResource(R.mipmap.star_higlighted);
                    rb_manner_five.setImageResource(R.mipmap.star_higlighted);
                    break;
            }
        }
        if (group==R.id.rg_result) {
            switch (type) {
                case 1:
                    resultCount=1;
                    rb_result_one.setImageResource(R.mipmap.star_higlighted);
                    rb_result_two.setImageResource(R.mipmap.star_normal);
                    rb_result_three.setImageResource(R.mipmap.star_normal);
                    rb_result_four.setImageResource(R.mipmap.star_normal);
                    rb_result_five.setImageResource(R.mipmap.star_normal);
                    break;
                case 2:
                    resultCount=2;
                    rb_result_one.setImageResource(R.mipmap.star_higlighted);
                    rb_result_two.setImageResource(R.mipmap.star_higlighted);
                    rb_result_three.setImageResource(R.mipmap.star_normal);
                    rb_result_four.setImageResource(R.mipmap.star_normal);
                    rb_result_five.setImageResource(R.mipmap.star_normal);
                    break;
                case 3:
                    resultCount=3;
                    rb_result_one.setImageResource(R.mipmap.star_higlighted);
                    rb_result_two.setImageResource(R.mipmap.star_higlighted);
                    rb_result_three.setImageResource(R.mipmap.star_higlighted);
                    rb_result_four.setImageResource(R.mipmap.star_normal);
                    rb_result_five.setImageResource(R.mipmap.star_normal);
                    break;
                case 4:
                    resultCount=4;
                    rb_result_one.setImageResource(R.mipmap.star_higlighted);
                    rb_result_two.setImageResource(R.mipmap.star_higlighted);
                    rb_result_three.setImageResource(R.mipmap.star_higlighted);
                    rb_result_four.setImageResource(R.mipmap.star_higlighted);
                    rb_result_five.setImageResource(R.mipmap.star_normal);
                    break;
                case 5:
                    resultCount=5;
                    rb_result_one.setImageResource(R.mipmap.star_higlighted);
                    rb_result_two.setImageResource(R.mipmap.star_higlighted);
                    rb_result_three.setImageResource(R.mipmap.star_higlighted);
                    rb_result_four.setImageResource(R.mipmap.star_higlighted);
                    rb_result_five.setImageResource(R.mipmap.star_higlighted);
                    break;
            }
        }
        if (group==R.id.rg_flower) {
            switch (type) {
                case 1:
                    flowerCount=1;
                    rb_flower_one.setImageResource(R.mipmap.flower_higlighted);
                    rb_flower_two.setImageResource(R.mipmap.flower_normal);
                    rb_flower_three.setImageResource(R.mipmap.flower_normal);
                    rb_flower_four.setImageResource(R.mipmap.flower_normal);
                    rb_flower_five.setImageResource(R.mipmap.flower_normal);
                    break;
                case 2:
                    flowerCount=2;
                    rb_flower_one.setImageResource(R.mipmap.flower_higlighted);
                    rb_flower_two.setImageResource(R.mipmap.flower_higlighted);
                    rb_flower_three.setImageResource(R.mipmap.flower_normal);
                    rb_flower_four.setImageResource(R.mipmap.flower_normal);
                    rb_flower_five.setImageResource(R.mipmap.flower_normal);
                    break;
                case 3:
                    flowerCount=3;
                    rb_flower_one.setImageResource(R.mipmap.flower_higlighted);
                    rb_flower_two.setImageResource(R.mipmap.flower_higlighted);
                    rb_flower_three.setImageResource(R.mipmap.flower_higlighted);
                    rb_flower_four.setImageResource(R.mipmap.flower_normal);
                    rb_flower_five.setImageResource(R.mipmap.flower_normal);
                    break;
                case 4:
                    flowerCount=4;
                    rb_flower_one.setImageResource(R.mipmap.flower_higlighted);
                    rb_flower_two.setImageResource(R.mipmap.flower_higlighted);
                    rb_flower_three.setImageResource(R.mipmap.flower_higlighted);
                    rb_flower_four.setImageResource(R.mipmap.flower_higlighted);
                    rb_flower_five.setImageResource(R.mipmap.flower_normal);
                    break;
                case 5:
                    flowerCount=5;
                    rb_flower_one.setImageResource(R.mipmap.flower_higlighted);
                    rb_flower_two.setImageResource(R.mipmap.flower_higlighted);
                    rb_flower_three.setImageResource(R.mipmap.flower_higlighted);
                    rb_flower_four.setImageResource(R.mipmap.flower_higlighted);
                    rb_flower_five.setImageResource(R.mipmap.flower_higlighted);
                    break;
            }
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        title.setText(R.string.evaluate);
        getData();
    }
    @OnClick({R.id.ll_left})
    public void healthOnclick(View v) {

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
