package com.sage.hedonicmentality.fragment.Me;

import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.fragment.BaseFragment;

import butterknife.Bind;

/**
 * Created by Administrator on 2015/11/9.
 */
public class FragmentMNA extends BaseFragment {
    @Bind(R.id.layout_actionbar)
    RelativeLayout layout_actionbar;
    @Bind(R.id.tv_con2)
    TextView tv_con2;
    @Override
    public int getLayout() {
        return R.layout.fragment_mna;
    }

    @Override
    public void initActionbar() {
        layout_actionbar.setBackgroundResource(R.color.bg_title);
        tv_title.setText(R.string.zhuyi);
        tv_title.setTextColor(getResources().getColor(R.color.edit_focus));
        btn_left.setImageResource(R.mipmap.back_01);
        layout_actionbar.setBackgroundResource(R.color.bg_title);
    }
}
