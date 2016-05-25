package com.sage.hedonicmentality.fragment.account;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.fragment.BaseFragment;
import com.sage.hedonicmentality.ui.ActivityLogin;
import com.sage.hedonicmentality.utils.Contact;
import com.sage.hedonicmentality.utils.SharedPreferencesHelper;
import com.sage.hedonicmentality.utils.Util;
import com.sage.hedonicmentality.utils.UtilSnackbar;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/11/2.
 */
public class FragmentSex extends BaseFragment {
    @Bind(R.id.iv_man)
    ImageView iv_man;
    @Bind(R.id.iv_woman)
    ImageView iv_woman;
    @Bind(R.id.tv_right)
    public TextView tv_right;
    public static int  sex = 0;

    @Override
    public int getLayout() {
        return R.layout.fragment_sex;
    }

    @Override
    public void navigation() {
//        UtilSnackbar.showSimple(tv_right, "跳转");
//            next();
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
        btn_left.setVisibility(View.GONE);
        tv_title.setText(getString(R.string.sextitle));
//        btn_left.setVisibility(View.GONE);
        tv_right.setText(R.string.baocun);
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });
    }
    @OnClick({R.id.iv_man,R.id.iv_woman})
    void registerClick(View view){
        SharedPreferencesHelper.getInstance().Builder(getActivity());

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
