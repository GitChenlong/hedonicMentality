package com.sage.hedonicmentality.fragment.breath;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.service.BluetoothLeService;
import com.sage.hedonicmentality.ui.ActivityBreath;
import com.sage.hedonicmentality.ui.simple.BreathSetting;
import com.sage.hedonicmentality.view.MyViewpager;

import java.io.InputStream;
import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/1/21.
 */
public class FragmentaPromptdio extends DialogFragment{
    private MyViewpager view_pro;
    private ArrayList<View> pageViews = new ArrayList<View>();
    private ImageView iv_guanbi_dio;
    private TextView bu_pro;
    private ViewGroup group;
    private Bitmap btp2;
    private Bitmap btp;

    public static FragmentaPromptdio create(){
        FragmentaPromptdio fragment=new FragmentaPromptdio();
        return fragment;
    }
    public FragmentaPromptdio(){
        setStyle(STYLE_NO_TITLE, R.style.DiaScaleAnimationTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_promptdio, container, false);
        view_pro = (MyViewpager)view.findViewById(R.id.view_pro);
        bu_pro = (TextView)view.findViewById(R.id.bu_pro);
        bu_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        group = (ViewGroup) view.findViewById(R.id.viewGroup);
        init();
        ButterKnife.bind(this, view);
        return view;
    }

    private void init() {
        pageViews.clear();
        View v = View.inflate(getActivity(), R.layout.viewpager_item, null);
        getResources().getDrawable(R.mipmap.back_01);
        InputStream is = this.getResources().openRawResource(R.raw.tishi1);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 2;   // width��hight��Ϊԭ����ʮ��һ
        btp = BitmapFactory.decodeStream(is, null, options);
        ImageView iv_li1 = (ImageView)v.findViewById(R.id.iv_li1);
        iv_li1.setImageBitmap(btp);

        View v2 = View.inflate(getActivity(), R.layout.viewpager_item2, null);
        InputStream is2 = this.getResources().openRawResource(R.raw.tishi2);
        btp2 = BitmapFactory.decodeStream(is2, null, options);
        ImageView iv_li2 = (ImageView)v2.findViewById(R.id.iv_lis2);
        iv_li2.setImageBitmap(btp2);
        pageViews.add(v);
        pageViews.add(v2);
        view_pro.setAdapter(new GuidePageAdapter());
        view_pro.addOnPageChangeListener(new GuidePageChangeListener());
        getviewpage();
    }

    private ImageView[] imageViews;
    private ImageView imageView;
    public void getviewpage() {
        try {
            imageViews = new ImageView[pageViews.size()];
        } catch (Exception e) {
            // TODO: handle exception
        }
        group.removeAllViews();

        for (int j = 0; j < pageViews.size(); j++) {
            LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            margin.setMargins(10, 0, 0, 0);
            imageView = new ImageView(getActivity().getApplicationContext());

            imageView.setLayoutParams(new ViewGroup.LayoutParams(20, 20));
            imageViews[j] = imageView;
            if (j == 0) {

                imageViews[j].setBackgroundResource(R.mipmap.page_black);
            } else {

                imageViews[j].setBackgroundResource(R.mipmap.page_cover);
            }
            group.addView(imageViews[j], margin);
        }
    }
    private int tag;
    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);

        Window window=getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable());
        WindowManager.LayoutParams wl = window.getAttributes();
//        wl.x = 0;
//        wl.y = getResources().getDisplayMetrics().heightPixels;

        wl.width = getResources().getDisplayMetrics().widthPixels;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        wl.dimAmount=0.5f;
        wl.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;


        getDialog().onWindowAttributesChanged(wl);
        //window.setAttributes(wl);

        getDialog().setCanceledOnTouchOutside(true);

    }

    public void showToast(String str) {
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {


        if(btp != null && !btp.isRecycled()){

            btp.recycle();
            btp = null;
        }
        if(btp2 != null && !btp2.isRecycled()){

            btp2.recycle();
            btp2 = null;
        }
        System.gc();
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    class GuidePageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pageViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(pageViews.get(arg1));
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(pageViews.get(arg1));
            return pageViews.get(arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }

        @Override
        public void finishUpdate(View arg0) {

        }
    }

    class GuidePageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {

            for (int i = 0; i < imageViews.length; i++) {
                imageViews[arg0].setBackgroundResource(R.mipmap.page_black);

                if (arg0 != i) {
                    imageViews[i].setBackgroundResource(R.mipmap.page_cover);
                }
            }
        }
    }
}
