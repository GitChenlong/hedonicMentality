package com.sage.hedonicmentality.fragment.My;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.app.Http;
import com.sage.hedonicmentality.app.NavigationAc;
import com.sage.hedonicmentality.fragment.BaseFragment;
import com.sage.hedonicmentality.view.PullToRefreshLayout;
import com.sage.hedonicmentality.view.PullableGridView;
import com.sage.hedonicmentality.view.PullableScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/17
 * 发现
 */
public class FindFragment extends BaseFragment {
    @Bind(R.id.refresh_view)
    PullToRefreshLayout refresh_view;
    @Bind(R.id.layout_actionbar)
    RelativeLayout layout_actionbar;
    @Bind(R.id.gd)
    PullableGridView gridView;
    @Bind(R.id.ll_point_group)
    LinearLayout llPointGroup;
    @Bind(R.id.tv_image_description)
    TextView tvDescription;
    @Bind(R.id.viewpagers)
    ViewPager mViewPager;
    @Bind(R.id.scrollview)
    PullableScrollView scrollview;
    @Bind(R.id.inotification_alert)
    ImageView inotification_alert;
    private List<String > list=new ArrayList<>();
    private GridAdapter adapter;

    private List<ImageView> mImageList;
    /** 广告条正下方的标语 */
    private String[] imageDescriptionArray = {
            "广告------1", //
            "广告------2", //
            "广告------3", //
            "广告------4", //
            "广告------5" };
    /** 记录上一次点的位置，默认为0 */
    private int previousPointEnale = 0;

    /** 记录是否停止循环播放 */
    private boolean isStop = false;
    private CountDownTimer countDownTimer;
    private Thread th;

    @Override
    public int getLayout() {
        return R.layout.findfragment;
    }

    @Override
    public void initActionbar() {
        tv_title.setText(R.string.find);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText(R.string.Officina);
        refresh_view.setOnRefreshListener(new MyListener());
        setViewPager();
        scrollview.scrollTo(0, 0);
        addList();
        adapter = new GridAdapter(getActivity(),list);
        gridView.setAdapter(adapter);
        setScrollviewPosition();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TeacherDetailsFragment tf =(TeacherDetailsFragment)getActivity().getSupportFragmentManager().findFragmentByTag("TeacherDetailsFragment");
                if (tf!=null) {
                    NavigationAc.addFr(tf,"TeacherDetailsFragment",getFragmentManager(),1);
                }else{
                    NavigationAc.addFr(new TeacherDetailsFragment(),"TeacherDetailsFragment",getFragmentManager(),1);
                }
            }
        });

        //设置Scrollview 初始位置
        //设置textDrawble
//        Drawable nav_up=getResources().getDrawable(R.drawable.arrow_left_select);
//        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
//        tv_right.setCompoundDrawables(null, null, nav_up, null);
    }
    public void setScrollviewPosition(){
        scrollview.smoothScrollTo(0,20);
    }
    private void setViewPager() {
        mImageList = new ArrayList<ImageView>();
        int[] imageIds = new int[] { R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher };
        ImageView mImageView;
        LinearLayout.LayoutParams params;
        // 初始化广告条资源
        for (int id : imageIds) {
            mImageView = new ImageView(getActivity());
            mImageView.setBackgroundResource(id);
            mImageList.add(mImageView);

            // 初始化广告条正下方的"点"
            View dot = new View(getActivity());
            dot.setBackgroundResource(R.drawable.point_background);
            params = new LinearLayout.LayoutParams(5, 5);
            params.leftMargin = 10;
            dot.setLayoutParams(params);
            dot.setEnabled(false);
            llPointGroup.addView(dot);
        }

        mViewPager.setAdapter(new MyAdapter());

        // 设置广告标语和底部“点”选择状态
        tvDescription.setText(imageDescriptionArray[0]);
        llPointGroup.getChildAt(0).setEnabled(true);

        // 设置广告条跳转时，广告语和状态语的变化
        mViewPager.setOnPageChangeListener(new MyViewPagerListener());
        // 初始化广告条，当前索引Integer.MAX_VALUE的一半
        int index = (Integer.MAX_VALUE / 2) - (Integer.MAX_VALUE / 2 % mImageList.size());
        mViewPager.setCurrentItem(index); // 设置当前选中的Page，会触发onPageChangListener.onPageSelected方法
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isStop) {
                    SystemClock.sleep(2000);
                    th = new Thread(runnable);
                    th.start();
                }
            }
        }).start();
    }

    @OnClick({R.id.inotification_alert})
    public void findOnclick(View v){
        switch (v.getId()){
            case R.id.inotification_alert:
                NavigationAc.addFr(new InotificationAlertFragment(),"InotificationAlertFragment",getFragmentManager(),1);
                break;
        }
    }

    public void addList() {
        for (int i=0;i<9;i++) {
            list.add("LOOK-ME->"+i);
        }
    }

    private int UPDATAPAGERVIEW =1111;
    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1111:
                    if (mViewPager!=null) {
                        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                    }
                    break;
            }
            }
        };

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Message msg = new Message();
                msg.what = UPDATAPAGERVIEW;
                myHandler.sendMessage(msg);
            }
        };

        class MyListener implements PullToRefreshLayout.OnRefreshListener {
            @Override
            public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
                // 下拉刷新操作
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
//                        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                    }
                }.sendEmptyMessageDelayed(0, 1000);
            }

            @Override
            public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
                // 加载操作
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
//                      pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL); //加载失败提示语
                        addList();
                        adapter.notifyDataSetChanged();
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);//加载成功提示语

                    }
                }.sendEmptyMessageDelayed(0, 1000);
            }

        }

        @Override
        public void onResume() {
            super.onResume();
            btn_left.setVisibility(View.INVISIBLE);
        }

        @Override
        public void navigation() {
            // 跳HRV
        }


        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
        }

        public void getData() {
            Http.getFind("", new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {

                }

                @Override
                public void onFailure(HttpException error, String msg) {

                }
            });
        }

        public class GridAdapter extends BaseAdapter {
            Context mContext;
            List<String> mlist = new ArrayList<>();

            public GridAdapter(Context context, List<String> list) {
                mContext = context;
                mlist = list;
            }

            @Override
            public int getCount() {
                return mlist == null ? 0 : mlist.size();
            }

            @Override
            public Object getItem(int position) {
                return mlist.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = View.inflate(mContext, R.layout.grid_item, null);
                }
                return convertView;
            }
        }

        private class MyViewPagerListener implements OnPageChangeListener {

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageSelected(int arg0) {
                // 获取新的位置
                int newPosition = arg0 % imageDescriptionArray.length;
                // 设置广告标语
                tvDescription.setText(imageDescriptionArray[newPosition]);
                // 消除上一次的状态点
                llPointGroup.getChildAt(previousPointEnale).setEnabled(false);
                // 设置当前的状态点“点”
                llPointGroup.getChildAt(newPosition).setEnabled(true);
                // 记录位置
                previousPointEnale = newPosition;
            }
        }

        /**
         * ViewPager数据适配器
         */
        private class MyAdapter extends PagerAdapter {

            @Override
            public int getCount() {
                // 将viewpager页数设置成Integer.MAX_VALUE，可以模拟无限循环
                return Integer.MAX_VALUE;
            }

            /**
             * 复用对象 true 复用view false 复用的是Object
             */
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0 == arg1;
            }

            /**
             * 销毁对象
             *
             * @param position 被销毁对象的索引位置
             */
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(mImageList.get(position % mImageList.size()));
            }

            /**
             * 初始化一个对象
             *
             * @param position 初始化对象的索引位置
             */
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(mImageList.get(position % mImageList.size()));
                return mImageList.get(position % mImageList.size());
            }

        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            isStop = true;
        }
}
