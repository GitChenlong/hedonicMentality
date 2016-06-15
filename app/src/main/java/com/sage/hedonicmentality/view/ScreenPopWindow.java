package com.sage.hedonicmentality.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.bean.Indicate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/30.
 */
public class ScreenPopWindow extends PopupWindow  {
    private final View mMenuView;
    private final ViewFlipper viewfipper;
    private final RadioGroup rg_price;
    private final RadioGroup rg_age_one;
    private final RadioGroup rg_age_two;
    private final RadioGroup rg_sex;
    private final RadioGroup rg_oneline;
    private final RadioGroup rg_price_two;
    private final RadioGroup rg_age_three;
    private final RadioButton rb_price_one;
    private Context mcontext;
    private Handler mHandler;
    private List<String> mlist = new ArrayList<>();

    public ScreenPopWindow(Context context,Handler handler) {
        super(context);
//        setAnimationStyle(R.style.cnsultAnimation);
        this.mcontext = context;
        this.mHandler = handler;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(mcontext.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.screenpopwindow, null);
        viewfipper = new ViewFlipper(context);
        viewfipper.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        rg_price = (RadioGroup)mMenuView.findViewById(R.id.rg_price);
        rb_price_one = (RadioButton)mMenuView.findViewById(R.id.rb_price_one);
        rg_price_two = (RadioGroup)mMenuView.findViewById(R.id.rg_price_two);
        rg_age_one = (RadioGroup)mMenuView.findViewById(R.id.rg_age_one);
        rg_age_two = (RadioGroup)mMenuView.findViewById(R.id.rg_age_two);
        rg_age_three = (RadioGroup)mMenuView.findViewById(R.id.rg_age_three);
        rg_sex = (RadioGroup)mMenuView.findViewById(R.id.rg_sex);
        rg_oneline = (RadioGroup)mMenuView.findViewById(R.id.rg_oneline);
        rg_price.setOnCheckedChangeListener(MyCheckedChangeListener);
        rg_price_two.setOnCheckedChangeListener(MyCheckedChangeListener);
        rg_age_one.setOnCheckedChangeListener(MyCheckedChangeListener);
        rg_age_two.setOnCheckedChangeListener(MyCheckedChangeListener);
        rg_age_three.setOnCheckedChangeListener(MyCheckedChangeListener);
        rg_sex.setOnCheckedChangeListener(MyCheckedChangeListener);
        rg_oneline.setOnCheckedChangeListener(MyCheckedChangeListener);
        rb_price_one.setChecked(true);
        viewfipper.addView(mMenuView);
        viewfipper.setFlipInterval(6000000);

        this.setContentView(viewfipper);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);
//        this.setOutsideTouchable(true);
        this.update();

    }
    RadioGroup.OnCheckedChangeListener MyCheckedChangeListener =new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (group.getId()) {
                case R.id.rg_price:
                    switch (checkedId){
                        case R.id.rb_price_one:
                            Toast.makeText(mcontext,"价格-1",Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.rb_price_two:
                            Toast.makeText(mcontext,"价格-2",Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.rb_price_three:
                            Toast.makeText(mcontext,"价格-3",Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                case R.id.rg_price_two:
                    if (checkedId==R.id.rb_price_four) {
                        Toast.makeText(mcontext,"价格-4",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.rg_age_one:
                    switch (checkedId){
                        case R.id.rb_age_one:
                            Toast.makeText(mcontext,"年龄-1",Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.rb_age_two:
                            Toast.makeText(mcontext,"年龄-2",Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.rb_age_three:
                            Toast.makeText(mcontext,"年龄-3",Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                case R.id.rg_age_two:
                    switch (checkedId){
                        case R.id.rb_age_four:
                            Toast.makeText(mcontext,"年龄-4",Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.rb_age_five:
                            Toast.makeText(mcontext,"年龄-5",Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.rb_age_six:
                            Toast.makeText(mcontext,"年龄-6",Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                case R.id.rg_age_three:
                    switch (checkedId) {
                        case R.id.rb_age_seven:
                            Toast.makeText(mcontext, "年龄-7", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                case R.id.rg_sex:
                    switch (checkedId){
                        case R.id.rb_sex_one:
                            Toast.makeText(mcontext,"性别-4",Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.rb_sex_two:
                            Toast.makeText(mcontext,"性别-2",Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.rb_sex_three:
                            Toast.makeText(mcontext,"性别-3",Toast.LENGTH_SHORT).show();
                            break;
                    }

                    break;
                case R.id.rg_oneline:
                    switch (checkedId){
                        case R.id.rb_oneline_one:
                            Toast.makeText(mcontext,"状态-1",Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.rb_oneline_two:
                            Toast.makeText(mcontext,"状态-2",Toast.LENGTH_SHORT).show();
                            break;
                    }

                    break;
            }
        }
    };
    class MyListAdapter extends BaseAdapter {
        private Context mContext;
        private Indicate indicate;
        private List<String> mList = new ArrayList<>();

        public MyListAdapter(Context context,Indicate indicates,List<String> list) {
            this.mContext = context;
            this.indicate = indicates;
            this.mList = list;
        }
        @Override
        public int getCount() {

            return mList==null? 0 :mList.size();
        }

        @Override
        public String getItem(int position) {

            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.btgoodatitem,parent,false);
            }
            TextView name = (TextView)convertView.findViewById(R.id.tv_name);
            ImageView iv = (ImageView)convertView.findViewById(R.id.iv_yes);
            name.setText(mList.get(position));
            if (indicate.getPostion()==position) {
                name.setTextColor(mContext.getResources().getColor(R.color.green_essential_colour));
                iv.setVisibility(View.VISIBLE);
            }else{
                name.setTextColor(mContext.getResources().getColor(R.color.grays));
                iv.setVisibility(View.INVISIBLE);
            }
            return convertView;
        }
    }
    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        viewfipper.startFlipping();
    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
        viewfipper.startFlipping();
    }
}
