package com.sage.hedonicmentality.fragment.account;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.fragment.BaseFragment;

/**
 * Created by Administrator on 2015/12/31.
 */
public class FragmentAgreement extends BaseFragment {
    @Override
    public int getLayout() {
        return R.layout.fragment_agreement;
    }
    @Override
    public void initActionbar() {
        tv_title.setText(getString(R.string.title_agreement));
    }
}
