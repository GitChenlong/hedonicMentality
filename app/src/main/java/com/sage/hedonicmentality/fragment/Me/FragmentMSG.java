package com.sage.hedonicmentality.fragment.Me;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.fragment.BaseFragment;

/**
 * Created by Sage on 2015/7/29.
 */
public class FragmentMSG extends BaseFragment {
    @Override
    public int getLayout() {
        return R.layout.fragment_msg;
    }

    @Override
    public void initActionbar() {
        tv_title.setText(R.string.title_msg);
    }


}
