package com.sage.hedonicmentality.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.sage.hedonicmentality.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sage on 2015/7/28.
 */
public class CatalogPopupWindow extends PopupWindow {

    public CatalogPopupWindow(Context context) {
        super(context);
        View view= LayoutInflater.from(context).inflate(R.layout.pw_catalog,null);
        ButterKnife.bind(this,view);
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable());
        setOutsideTouchable(true);
    }

    public interface CatalogClickListener{
        void whichClick(int position);
    }

    public void setCatalogClickListener(CatalogClickListener catalogClickListener) {
        this.catalogClickListener = catalogClickListener;
    }

    private CatalogClickListener catalogClickListener;

    @OnClick({R.id.tv_wreak,R.id.tv_worry,R.id.tv_meditation,R.id.tv_chat,R.id.tv_me})
    void catalogClick(View view){
        switch (view.getId()){
            case R.id.tv_wreak:
                handle(0);
                break;
            case R.id.tv_worry:
                handle(1);
                break;
            case R.id.tv_meditation:
                handle(2);
                break;
            case R.id.tv_chat:
                handle(3);
                break;
            case R.id.tv_me:
                handle(4);
                break;
        }
        dismiss();

    }

    private void handle(int position){
        if(catalogClickListener!=null){
            catalogClickListener.whichClick(position);
        }
    }
}
