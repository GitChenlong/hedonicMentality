package com.sage.hedonicmentality.fragment.Me;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.fragment.BaseFragment;
import com.sage.hedonicmentality.ui.ActivityLogin;
import com.sage.hedonicmentality.ui.ActivityMe;
import com.sage.hedonicmentality.utils.Contact;
import com.sage.hedonicmentality.utils.SharedPreferencesHelper;
import com.sage.hedonicmentality.utils.UtilSnackbar;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/11/11.
 */
public class FragmentSexme extends BaseFragment{
    @Bind(R.id.iv_man)
    ImageView iv_man;
    @Bind(R.id.iv_woman)
    ImageView iv_woman;
    @Bind(R.id.tv_right)
    public TextView tv_right;
    @Bind(R.id.layout_actionbar)
    RelativeLayout layout_actionbar;
    public static int  sex = 0;

    @Override
    public int getLayout() {
        return R.layout.fragment_sex;
    }

    private void next() {
        if(sex != 0 ){
            ((ActivityLogin)getActivity()).GoBirthday();
        }else {
            UtilSnackbar.showSimple(iv_man, getResources().getString(R.string.qxzxb));
        }
    }

    @Override
    public void initActionbar() {
        SharedPreferencesHelper.getInstance().Builder(getActivity());
        String strsex =  SharedPreferencesHelper.getInstance().getString(Contact.SH_Sex);
        if(null != strsex &&!"".equals(strsex)){
            sex = Integer.parseInt(strsex);
            if(sex == 1){
                iv_man.setBackgroundResource(R.mipmap.xz_man);
            }else
            if(sex == 2){
                iv_woman.setBackgroundResource(R.mipmap.xz_woman);
            }
        }
        tv_right.setTextColor(getResources().getColor(R.color.white_color));
        layout_actionbar.setBackgroundResource(R.color.bg_title);
        tv_title.setTextColor(getResources().getColor(R.color.whi));
        btn_left.setImageResource(R.mipmap.back_01);
        tv_title.setText(getString(R.string.sextitle));
        tv_right.setText(R.string.baocun);
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sexs = "";
                if (sex == 1) {
                    sexs = getResources().getString(R.string.man);
                } else
                if (sex == 2) {
                    sexs = getResources().getString(R.string.woman);
                }
                FragmentMeSetting.sexme = sexs;
                ActivityMe ac = (ActivityMe)getActivity();
                ac.setSex();
               back();
            }
        });
    }
    @OnClick({R.id.iv_man,R.id.iv_woman})
    void registerClick(View view){

        switch(view.getId()){
            case R.id.iv_man:
                sex = 1;
                SharedPreferencesHelper.getInstance().putString(Contact.SH_Sex, sex + "");
                iv_man.setBackgroundResource(R.mipmap.xz_man);
                iv_woman.setBackgroundResource(R.mipmap.women);
                break;
            case R.id.iv_woman:
                sex = 2;
                SharedPreferencesHelper.getInstance().putString(Contact.SH_Sex, sex + "");
                iv_man.setBackgroundResource(R.mipmap.man);
                iv_woman.setBackgroundResource(R.mipmap.xz_woman);
                break;
        }
    }
}
