package com.sage.hedonicmentality.fragment.Me;

import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.fragment.BaseFragment;
import com.sage.hedonicmentality.ui.ActivityMe;
import com.sage.hedonicmentality.utils.Contact;
import com.sage.hedonicmentality.utils.SharedPreferencesHelper;
import com.sage.libwheelview.widget.wheel.OnWheelChangedListener;
import com.sage.libwheelview.widget.wheel.WheelView;
import com.sage.libwheelview.widget.wheel.adapter.ArrayWheelAdapter;

import butterknife.Bind;

/**
 * Created by Administrator on 2015/11/10.
 */
public class FragmentEducation extends BaseFragment {
    @Bind(R.id.wh_education)
    public WheelView wh_education;
    @Bind(R.id.tv_right)
    public TextView tv_right;
    private String[] dateType;
    @Bind(R.id.layout_actionbar)
    RelativeLayout layout_actionbar;

    String educa;

    @Override
    public int getLayout() {
        return R.layout.fragment_education;
    }

    @Override
    public void initActionbar() {
        tv_right.setTextColor(getResources().getColor(R.color.white_color));
        layout_actionbar.setBackgroundResource(R.color.bg_title);
        tv_title.setTextColor(getResources().getColor(R.color.whi));
        btn_left.setImageResource(R.mipmap.back_01);
        tv_right.setText(R.string.baocun);
        tv_title.setText(getString(R.string.jiaoyu));
        dateType = getActivity().getResources().getStringArray(com.sage.libwheelview.R.array.eddata);
        OnWheelChangedListener listener = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {

            }
        };
        ArrayWheelAdapter<String> educaAdapter = new ArrayWheelAdapter<String>(
                getActivity(), dateType);
        wh_education.addChangingListener(listener);
        wh_education.setViewAdapter(educaAdapter);
        wh_education.setCurrentItem(0);

        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                educa  = dateType[wh_education.getCurrentItem()];
                SharedPreferencesHelper.getInstance().Builder(getActivity());
                SharedPreferencesHelper.getInstance().putString(Contact.SH_Education,educa);
                if(TextUtils.isEmpty(educa)){
                    return;
                }
                FragmentMeSetting.education = educa;
                ActivityMe ac = (ActivityMe)getActivity();
                ac.seteducation();
                back();
            }
        });
    }
}
