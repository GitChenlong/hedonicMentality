package com.sage.hedonicmentality.fragment.Me;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.fragment.BaseFragment;

/**
 * Created by Sage on 2015/7/29.
 */
public class FragmentSecret extends BaseFragment {
    @Override
    public int getLayout() {
        return R.layout.fragment_secret;
    }

    @Override
    public void initActionbar() {
        tv_title.setText(R.string.title_secret);
    }
}
