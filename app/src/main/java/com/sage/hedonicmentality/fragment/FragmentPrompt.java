package com.sage.hedonicmentality.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.sage.hedonicmentality.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sage on 2015/7/29.
 */
public class FragmentPrompt extends DialogFragment {

    @Bind(R.id.tv_title)
    TextView tv_title;
    @Bind(R.id.tv_cancel)
    TextView tv_cancel;
    @Bind(R.id.tv_ok)
    TextView tv_ok;
    private int tag;/**tag=0,清除缓存的，tag=1 检查更新的*/
    private String title;
    public static FragmentPrompt create(int tag) {
        FragmentPrompt fragmentPrompt = new FragmentPrompt();
        Bundle bundle = new Bundle();
        bundle.putInt("tag", tag);
        fragmentPrompt.setArguments(bundle);
        return fragmentPrompt;
    }

    public FragmentPrompt(){
        setStyle(STYLE_NO_TITLE, R.style.MyCustomTheme);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prompt, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments() != null) {
            tag = getArguments().getInt("tag");
            switch(tag){
                case 0:
                    tv_title.setText(getString(R.string.dialog_clear));
                    break;
                case 1:
                    tv_title.setText(getString(R.string.dialog_update));
                    break;
            }
        }
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;//这里的坐标是相对默认重心 center而言的。
        wl.y = getResources().getDisplayMetrics().heightPixels;
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        wl.dimAmount = 0.6f;
        wl.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        // 设置显示位置
        getDialog().onWindowAttributesChanged(wl);
        //window.setBackgroundDrawable(new ColorDrawable());//背景透明可以用我们自己的背景
        //window.setAttributes(wl);
        // 设置点击外围解散
        getDialog().setCanceledOnTouchOutside(true);
        ButterKnife.bind(this, getView());
    }

    @OnClick({R.id.tv_cancel, R.id.tv_ok})
    void promptClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                if (promptClickListener != null) {
                    promptClickListener.cancelClick(tag);
                }
                break;
            case R.id.tv_ok:
                if (promptClickListener != null) {
                    promptClickListener.okClick(tag);
                }
                break;
        }
        dismiss();
    }


    public interface PromptClickListener {
        void okClick(int tag);

        void cancelClick(int tag);
    }

    public void setPromptClickListener(PromptClickListener promptClickListener) {
        this.promptClickListener = promptClickListener;
    }

    private PromptClickListener promptClickListener;
}
