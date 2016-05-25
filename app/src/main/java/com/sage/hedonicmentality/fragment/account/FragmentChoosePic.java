package com.sage.hedonicmentality.fragment.account;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.sage.hedonicmentality.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sage on 2015/7/27.
 */
public class FragmentChoosePic extends DialogFragment {
    @Bind(R.id.tv_camera)
    TextView tv_camera;
    @Bind(R.id.tv_photos)
    TextView tv_photos;

    /**0，默认的文字是相机相册，*/
    public FragmentChoosePic create(int tag){
        FragmentChoosePic fragment=new FragmentChoosePic();
        Bundle bundle=new Bundle();
        bundle.putInt("tag",tag);
        fragment.setArguments(bundle);
        return fragment;
    }

    public FragmentChoosePic(){
        setStyle(STYLE_NO_TITLE, R.style.MyCustomTheme);//取消标题
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_choose_pic, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        Window window=getDialog().getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;//这里的坐标是相对默认重心 center而言的。
        wl.y = getResources().getDisplayMetrics().heightPixels;
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        wl.dimAmount=0.5f;
        wl.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        // 设置显示位置
        getDialog().onWindowAttributesChanged(wl);
        //window.setAttributes(wl);
        // 设置点击外围隐藏
        getDialog().setCanceledOnTouchOutside(true);

    }

    public void showToast(String str) {
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.tv_camera,R.id.tv_photos,R.id.tv_cancel})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_camera:
               which(0);
                break;
            case R.id.tv_photos:
               which(1);
                break;
            case R.id.tv_cancel:

                break;
            default:
                break;
        }
        dismiss();
    }

    private void which(int index){
        if(mChooseListener!=null){
            mChooseListener.click(index);
        }
    }

    public interface ChooseClickListener{
           void click(int index);
    }

    public void setmChooseListener(ChooseClickListener mChooseListener) {
        this.mChooseListener = mChooseListener;
    }

    private ChooseClickListener mChooseListener;

}
