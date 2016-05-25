package com.sage.hedonicmentality.fragment.Me;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.fragment.BaseFragment;
import com.sage.hedonicmentality.ui.ActivityMe;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/8/14.
 */
public class FragmentUpName extends BaseFragment {
    public int getLayout() {
        return R.layout.fragment_upname;
    }
    @Bind(R.id.btn_right)
    ImageView btn_right;
    @Bind(R.id.ed_upname)
    EditText ed_upname;
    @Bind(R.id.layout_actionbar)
    RelativeLayout layout_actionbar;
    @Bind(R.id.tv_right)
    TextView tv_right;
    @Override
    public void initActionbar() {
        tv_title.setText(R.string.title_me_udname);
//        btn_right.setImageResource(R.mipmap.queding);
        tv_right.setText(R.string.baocun);
        tv_right.setTextColor(getResources().getColor(R.color.white_color));
        tv_title.setTextColor(getResources().getColor(R.color.whi));
        btn_left.setImageResource(R.mipmap.back_01);
        layout_actionbar.setBackgroundResource(R.color.bg_title);
        if(!"".equals(FragmentMeSetting.name)){
            ed_upname.setText(FragmentMeSetting.name);
        }
    }

    @OnClick(R.id.tv_right)
    void adviceClick(){
        String upname=ed_upname.getText().toString().trim();
        if(TextUtils.isEmpty(upname)){
            return;
        }
        FragmentMeSetting.name = upname;
        ActivityMe ac = (ActivityMe)getActivity();
        ac.setmeName();
        back();
    }
}
