package com.sage.hedonicmentality.fragment.Me;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.fragment.BaseFragment;
import com.sage.hedonicmentality.ui.ActivityMe;
import com.sage.hedonicmentality.utils.Contact;
import com.sage.hedonicmentality.utils.SharedPreferencesHelper;
import com.sage.hedonicmentality.view.TuneWheel;
import com.sage.hedonicmentality.view.TuneWheels;

import butterknife.Bind;

/**
 * Created by Administrator on 2015/12/16.
 */
public class FragmentWeight extends BaseFragment{
    @Bind(R.id.we_tuneWheel)
    public TuneWheels we_tuneWheel;
    @Bind(R.id.tv_right)
    public TextView tv_right;
    @Bind(R.id.up_weight)
    public TextView up_weight;
    @Bind(R.id.layout_actionbar)
    RelativeLayout layout_actionbar;
    private Double weight;

    @Override
    public int getLayout() {
        return R.layout.fragment_weight;
    }
    @Override
    public void initActionbar() {

        tv_right.setTextColor(getResources().getColor(R.color.white_color));
        layout_actionbar.setBackgroundResource(R.color.bg_title);
        tv_title.setTextColor(getResources().getColor(R.color.whi));
        btn_left.setImageResource(R.mipmap.back_01);
        tv_right.setText(R.string.baocun);
        tv_title.setText(getString(R.string.wegit_title));
//        SharedPreferencesHelper.getInstance().Builder(getActivity());
//        String weight1 = SharedPreferencesHelper.getInstance().getString(Contact.SH_Weight);
        String weight1 = FragmentMeSetting.weight;
        weight = Double.parseDouble(weight1);
        int we = (int)Double.parseDouble(weight1);
        we_tuneWheel.mValue = we;
        up_weight.setText(getString(R.string.upweight_txt,weight1+""));
        we_tuneWheel.setValueChangeListener(new TuneWheels.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                weight = Double.parseDouble(we_tuneWheel.getValue()+"");
                up_weight.setText(getString(R.string.upweight_txt,weight+""));
            }
        });
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentMeSetting.weight = weight+"";
                ActivityMe ac = (ActivityMe)getActivity();
                ac.setWeight();
                back();
            }
        });
    }
}
