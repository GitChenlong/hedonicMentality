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

import butterknife.Bind;

/**
 * Created by Administrator on 2015/11/20.
 */
public class FragmentHeight extends BaseFragment {
    @Bind(R.id.tuneWheel)
    public TuneWheel tuneWheel;
    @Bind(R.id.tv_right)
    public TextView tv_right;
    @Bind(R.id.up_hegit)
    public TextView up_hegit;
    @Bind(R.id.layout_actionbar)
    RelativeLayout layout_actionbar;
    @Override
    public int getLayout() {
        return R.layout.fragment_height;
    }
    private Double height = 170.0;
    @Override
    public void initActionbar() {

        tv_right.setTextColor(getResources().getColor(R.color.white_color));
        layout_actionbar.setBackgroundResource(R.color.bg_title);
        tv_title.setTextColor(getResources().getColor(R.color.whi));
        btn_left.setImageResource(R.mipmap.back_01);
        tv_right.setText(R.string.baocun);
        tv_title.setText(getString(R.string.height_title));
//        SharedPreferencesHelper.getInstance().Builder(getActivity());
//        String Height = SharedPreferencesHelper.getInstance().getString(Contact.SH_Height);
        String Height = FragmentMeSetting.height;
        height = Double.parseDouble(Height);
        int he = (int)Double.parseDouble(Height);
        up_hegit.setText(getString(R.string.uphegit_txt, he + ""));
        tuneWheel.mValue = he;
        tuneWheel.setValueChangeListener(new TuneWheel.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                height = Double.parseDouble(tuneWheel.getValue()+"");
                up_hegit.setText(getString(R.string.uphegit_txt, height + ""));
            }
        });
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentMeSetting.height = height+"";
                ActivityMe ac = (ActivityMe)getActivity();
                ac.setHegit();
                back();
            }
        });
    }
}
