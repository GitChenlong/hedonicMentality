package com.sage.hedonicmentality.fragment;

import android.content.Intent;
import android.os.Bundle;
import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.ui.ActivityBreath;
import com.sage.hedonicmentality.ui.ActivityChat;
import com.sage.hedonicmentality.ui.ActivityMe;
import com.sage.hedonicmentality.ui.ActivityWorry;
import com.sage.hedonicmentality.ui.ActivityWreak;
import com.sage.hedonicmentality.widget.CatalogPopupWindow;


/**
 * Created by Sage on 2015/7/17.
 */
public class FragmentMain extends BaseFragment {
    public static String TAG = FragmentMain.class.getSimpleName();
    private CatalogPopupWindow catalogPopupWindow;

    @Override
    public int getLayout() {
        return R.layout.fragment_main;
    }

    @Override
    public void initActionbar() {
        tv_title.setText(R.string.app_name);
        isBack=false;

    }

    @Override
    public void onResume() {
        super.onResume();
        btn_left.setBackgroundResource(R.drawable.u26);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void navigation() {

        if(catalogPopupWindow==null){
            catalogPopupWindow=new CatalogPopupWindow(getActivity());
            catalogPopupWindow.setAnimationStyle(R.style.MyPWAnimation);
            catalogPopupWindow.setCatalogClickListener(new CatalogPopupWindow.CatalogClickListener() {
                @Override
                public void whichClick(int position) {
                    nextPage(position);
                }
            });
        }
        catalogPopupWindow.showAsDropDown(btn_left);
    }


    private void nextPage(int position){
        switch(position){
            case 0:
                getActivity().startActivity(new Intent(getActivity(), ActivityWreak.class));
                break;
            case 1:
                getActivity().startActivity(new Intent(getActivity(), ActivityWorry.class));
                break;
            case 2:
                getActivity().startActivity(new Intent(getActivity(), ActivityBreath.class));
                break;
            case 3:
                getActivity().startActivity(new Intent(getActivity(), ActivityChat.class));
                break;
            case 4:
                Intent intent=new Intent(getActivity(), ActivityMe.class);
                getActivity().startActivity(intent);
                break;
        }
    }


}
