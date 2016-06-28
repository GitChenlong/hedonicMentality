package com.sage.hedonicmentality.fragment.My;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.app.NavigationAc;
import com.sage.hedonicmentality.utils.Util;
import com.sage.hedonicmentality.view.OrderPopWindow;
import com.sage.hedonicmentality.widget.MyScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/18.
 * 咨询师详情
 */
public class TeacherDetailsAc extends FragmentActivity implements MyScrollView.OnScrollListener {
    // 主页缩放动画
    private Animation mScalInAnimation1;
    // 主页缩放完毕小幅回弹动画
    private Animation mScalInAnimation2;
    // 主页回弹正常状态动画
    private Animation mScalOutAnimation;
    private FragmentManager fm;
    @Bind(R.id.ll_techerdetails)
    RelativeLayout ll_techerdetails;
    @Bind(R.id.ll_title_one)
    LinearLayout ll_title_one;
    @Bind(R.id.ll_title_two)
    LinearLayout ll_title_two;
    @Bind(R.id.ll_title)
    LinearLayout ll_title;
    @Bind(R.id.ll_title_three)
    LinearLayout ll_title_three;
    @Bind(R.id.ev_line)
    ImageView ev_line;
    @Bind(R.id.de_line)
    ImageView de_line;
    @Bind(R.id.myscrollview)
    MyScrollView myscrollview;
    private int searchLayoutTop;
    private detailsFr detailsfr;
    private evaluateFr evaluatefr;

    //    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = View.inflate(getActivity(), R.layout.teacherdetailsfragment,null);
//        ButterKnife.bind(this,view);
//        init(view);
//        return view;
//
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacherdetailsac);
        ButterKnife.bind(this);
        init();
        myscrollview.setOnScrollListener(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            searchLayoutTop = ll_title_three.getBottom();//获取searchLayout的顶部位置
        }
    }

    //监听滚动Y值变化，通过addView和removeView来实现悬停效果
    @Override
    public void onScroll(int scrollY) {
        if(scrollY >= searchLayoutTop){
            if (ll_title.getParent()!=ll_title_two) {
                ll_title_one.removeView(ll_title);
                ll_title_two.addView(ll_title);
            }
        }else{
            if (ll_title.getParent()!=ll_title_one) {
                ll_title_two.removeView(ll_title);
                ll_title_one.addView(ll_title);
            }
        }
    }
    private void init() {
        fm = getSupportFragmentManager();
        inits();
        setTab(1);
    }
    /**
     * 初始化
     */
    private void inits() {
        // 动画初始化
        mScalInAnimation1 = AnimationUtils.loadAnimation(this,
                R.anim.root_in);
        mScalInAnimation2 = AnimationUtils.loadAnimation(this,
                R.anim.root_in2);
        mScalOutAnimation = AnimationUtils.loadAnimation(this,
                R.anim.root_out);
        // 注册事件回调
        mScalInAnimation1.setAnimationListener(new ScalInAnimation1());
    }

    /**
     * popupwindow消失的回调
     */
    private class OnPopupDismissListener implements
            android.widget.PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            // 标题和主页开始播放动画
            ll_techerdetails.startAnimation(mScalOutAnimation);
        }
    }

    /**
     * 缩小动画的回调
     */
    public class ScalInAnimation1 implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            ll_techerdetails.startAnimation(mScalInAnimation2);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    @OnClick({R.id.iv_back,R.id.iv_intent,R.id.ll_indent,R.id.ll_love,R.id.ll_flower
            ,R.id.ll_details,R.id.ll_evaluate,R.id.ll_share,R.id.ll_attention,R.id.ll_order})
    public void teacherOnclick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_intent:
                //跳转视屏播放

                break;
            case R.id.ll_indent:
                //订单

                break;
            case R.id.ll_love:
                //喜欢

                break;
            case R.id.ll_flower:
                //鲜花

                break;
            case R.id.ll_details:
                //详情
                setTab(1);
                break;
            case R.id.ll_evaluate:
                //评价
                setTab(2);
                break;
            case R.id.ll_share:
                //分享
                Util.showShare(this);
                break;
            case R.id.ll_attention:
                //关注

                break;
            case R.id.ll_order:
                //预约咨询
                chooseAddress();
                ll_techerdetails.startAnimation(mScalInAnimation1);
                break;

        }
    }
    private void chooseAddress() {
        String dates[] ={"周1","周2","周3","周4","周5","周6","周7","周8","周9","周10","周11","周12"};
        String times[] ={"9:00-10:00-1","9:00-10:00-2","9:00-10:00-3","9:00-10:00-4",
                "9:00-10:00-5","9:00-10:00-6","9:00-10:00-7","9:00-10:00-8","9:00-10:00-9","9:00-10:00-10","9:00-10:00-11"};
        OrderPopWindow orderpopwindow=new OrderPopWindow(this,dates,times,mHandler);
        orderpopwindow.setOnDismissListener(new OnPopupDismissListener());
        orderpopwindow.showAtLocation(this.findViewById(R.id.ll_techerdetails), Gravity.BOTTOM, 0, 0);
    }
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case 8:
                    String select = msg.getData().getString("date");
                    Toast.makeText(TeacherDetailsAc.this,select,Toast.LENGTH_SHORT).show();
                    NavigationAc.addFr(new WriteInformationFragment(), "WriteInformationFragment", getSupportFragmentManager(), 1);
                    break;
            }
        }
    };
    public void setTab(int type){
        FragmentTransaction bt = fm.beginTransaction();

        switch (type){
            case 1:

                if (detailsfr!=null) {
                    bt.show(detailsfr);
                }else{
                    detailsfr = new detailsFr();
                    bt.add(R.id.fl_details, detailsfr);
                }
                if (evaluatefr!=null) {
                    bt.hide(evaluatefr);
                }
                de_line.setVisibility(View.VISIBLE);
                ev_line.setVisibility(View.INVISIBLE);
                break;
            case 2:
                if (evaluatefr!=null) {
                    bt.show(evaluatefr);
                }else{
                    evaluatefr = new evaluateFr();
                    bt.add(R.id.fl_details, evaluatefr);
                }
                if (detailsfr!=null) {
                    bt.hide(detailsfr);
                }
                ev_line.setVisibility(View.VISIBLE);
                de_line.setVisibility(View.INVISIBLE);
                break;
        }
        bt.commit();
    }

    public class detailsFr extends  Fragment{
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            View view = View.inflate(getActivity(),R.layout.detailsfr,null);
            return view;
        }
    }

    public class evaluateFr extends  Fragment{
        @Bind(R.id.evaluate_listview)
        ListView evaluate_listview;
        @Bind(R.id.tv_evaluate_label_one)
        TextView tv_evaluate_label_one;
        @Bind(R.id.tv_evaluate_label_two)
        TextView tv_evaluate_label_two;
        @Bind(R.id.tv_evaluate_label_three)
        TextView tv_evaluate_label_three;
        @Bind(R.id.tv_evaluate_label_four)
        TextView tv_evaluate_label_four;
        private MyListAdapter adapter;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = View.inflate(getActivity(),R.layout.evaluatefr,null);
            ButterKnife.bind(this,view);
            List<String> list = new ArrayList<>();
            list.add("1");list.add("1");list.add("1");list.add("1");list.add("1");
            adapter  = new MyListAdapter(getActivity(),list);
            evaluate_listview.setAdapter(adapter);
            return view;

        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            ButterKnife.unbind(this);
        }

        class MyListAdapter extends BaseAdapter {
            private List<String> mlist;
            private Context mcontext;
            private LayoutInflater Inflater;
            public MyListAdapter(Context context, List<String> list) {
                this.Inflater = LayoutInflater.from(context);
                this.mcontext = context;
                this.mlist=list;
            }

            @Override
            public int getCount() {
                return this.mlist == null ? 0 : this.mlist.size();
            }

            @Override
            public Object getItem(int position) {
                // TODO Auto-generated method stub
                return mlist.get(position);
            }

            @Override
            public long getItemId(int position) {
                // TODO Auto-generated method stub
                return position;
            }
            @Override
            public View getView( int position, View convertView, ViewGroup arg2) {
                // TODO Auto-generated method stub
                final ViewHolder holder;
                if (convertView == null) {
                    convertView = Inflater.inflate(R.layout.evaluate_item, null);
                    holder = new ViewHolder(convertView);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                String content = mlist.get(position);
                holder.tv_username.setText(content);
//
//            ImageLoader.getInstance().displayImage(music.getIcon_url(),holder.iv_book);
//            holder.txt_book_content.setText(music.getDescription());
//            holder.txt_book_name.setText(music.getTitle());
//            holder.music_update_year.setText(music.getUptime());
//            holder.music_num.setText(music.getHit()+"");
                return convertView;
            }

            public final class ViewHolder {
                @Bind(R.id.tv_username)
                TextView tv_username;

                public ViewHolder(View view) {
                    ButterKnife.bind(this, view);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
