package com.sage.hedonicmentality.fragment.My;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.app.Http;
import com.sage.hedonicmentality.app.NavigationAc;
import com.sage.hedonicmentality.bean.Indicate;
import com.sage.hedonicmentality.bean.ScreenBean;
import com.sage.hedonicmentality.view.BeGoodAtPopwindow;
import com.sage.hedonicmentality.view.CnsultTimePopWindow;
import com.sage.hedonicmentality.view.ScreenPopWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/17.
 * 咨询
 */
public class ConsultFragment extends Fragment {
    @Bind(R.id.iv_two_dimension_code)
    ImageView two_dimension_code;//二维码
    @Bind(R.id.tv_time)
    TextView tv_time;//咨询时间
    @Bind(R.id.tv_be_adept_at)
    TextView tv_be_adept_at;//擅长
    @Bind(R.id.tv_screen)
    TextView tv_screen;//筛选
    @Bind(R.id.lv_zixun)
    ListView lv_zixun;//筛选
    private Indicate dateIn;
    private Indicate timeIn;
    private Indicate begoodatIn;
    private boolean srceenAnimation = true,begoodatAnimation=true,cnsultAnimation=true;
    private ScreenPopWindow screenpopwindow;
    private BeGoodAtPopwindow begoodatpopwindow;
    private CnsultTimePopWindow cnsulttimepopwindow;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(),R.layout.consultfragment,null);
        ButterKnife.bind(this, view);
        getData();
        return view;
    }
    public void setPopDismiss() {
        if (screenpopwindow!=null) {
        screenpopwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                changeArrows(getView().findViewById(R.id.iv_screen_fold), srceenAnimation);
                srceenAnimation = srceenAnimation ? false : true;
            }
        });
        }
        if (begoodatpopwindow!=null) {
            begoodatpopwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                public void onDismiss() {
                    changeArrows(getView().findViewById(R.id.iv_be_adept_at_fold), begoodatAnimation);
                    begoodatAnimation = begoodatAnimation ? false : true;
                }
            });
        }
        if (cnsulttimepopwindow!=null) {
            cnsulttimepopwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                public void onDismiss() {
                    changeArrows(getView().findViewById(R.id.iv_time_fold), cnsultAnimation);
                    cnsultAnimation = cnsultAnimation ? false : true;
                }
            });
        }
    }
    @OnClick({R.id.ll_time,R.id.ll_be_adept_at,R.id.ll_screen,R.id.iv_two_dimension_code,R.id.ll_search})
    public void consultOnclick(View view){
        switch (view.getId()){
            case R.id.iv_two_dimension_code: //二维码
                break;
            case R.id.ll_time://咨询时间
                showPopConsult();
                changeArrows(getView().findViewById(R.id.iv_time_fold), cnsultAnimation);
                cnsultAnimation = cnsultAnimation ? false : true;
                setPopDismiss();
                break;
            case R.id.ll_be_adept_at://擅长
                showPopBeGoodAt();
                changeArrows(getView().findViewById(R.id.iv_be_adept_at_fold), begoodatAnimation);
                begoodatAnimation = begoodatAnimation ? false : true;
                setPopDismiss();
                break;
            case R.id.ll_screen://筛选
                showScreenPop();
                changeArrows(getView().findViewById(R.id.iv_screen_fold), srceenAnimation);
                srceenAnimation = srceenAnimation ? false : true;
                setPopDismiss();
                break;
            case R.id.ll_search://搜索
            NavigationAc.addFr(new SearchFragment(),"SearchFragment",getFragmentManager(),1);
                break;
        }
    }
    /**
     * 箭头旋转动画
     * */
    public void changeArrows(View view,boolean type) {
        Animation rotate  =AnimationUtils.loadAnimation(getActivity(), R.anim.arrow_anim);
        rotate.setInterpolator(new AccelerateInterpolator());
        rotate.setFillAfter(type);
        view.startAnimation(rotate);

    }
    public void showPopBeGoodAt(){
         begoodatpopwindow  = new BeGoodAtPopwindow(getActivity(),mHandler,begoodatIn);
        begoodatpopwindow.showAsDropDown(getView().findViewById(R.id.tv_be_adept_at));
    }
    public void showScreenPop(){
        screenpopwindow = new ScreenPopWindow(getActivity(),mHandler);
        screenpopwindow.showAsDropDown(getView().findViewById(R.id.tv_be_adept_at));
    }
    public void showPopConsult(){
        List<String> date = new ArrayList<>();
        for (int i=0;i<20;i++) {
            date.add("日期"+i);
        }
        List<String> time = new ArrayList<>();
        for (int i=0;i<20;i++) {
            time.add("时间"+i);
        }
         cnsulttimepopwindow = new CnsultTimePopWindow(getActivity(),mHandler,date,time,dateIn,timeIn);
        cnsulttimepopwindow.showAsDropDown(getView().findViewById(R.id.tv_time));

    }
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case 3:
                    ScreenBean screen = (ScreenBean)msg.getData().getSerializable("screen");
                    Log.e("screen",screen.toString());
                    changeArrows(getView().findViewById(R.id.iv_screen_fold), false);
                    break;
                case 2:
                    String selct = msg.getData().getString("select");
                    Log.e("result","what=2"+selct);
                    changeArrows(getView().findViewById(R.id.iv_be_adept_at_fold), false);
                    break;
                case 1:
                    //按咨询时间搜索
                    String timename = msg.getData().getString("timename");
                    String datename = msg.getData().getString("datename");
                    Log.e("result","what=1"+datename+"/"+timename+"/"+dateIn.getPostion());
                    changeArrows(getView().findViewById(R.id.iv_time_fold), false);
                    break;
            }
        }
    };
    private void getData(){

        Http.getZixun("", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });
         dateIn = new Indicate(0);
         timeIn = new Indicate(0);
         begoodatIn =  new Indicate(0);
        List<String> datas = new ArrayList<>();
        for (int i=0;i<10;i++) {
            datas.add("10"+i);
        }
        MyListAdapter myListAdapter = new MyListAdapter(getActivity(),datas);
        lv_zixun.setAdapter(myListAdapter);

        lv_zixun.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),TeacherDetailsAc.class);
                startActivity(intent);
                int version = Integer.valueOf(android.os.Build.VERSION.SDK);
                if(version  >= 5) {
                    getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.anim_out_ac);  //此为自定义的动画效果，下面两个为系统的动画效果
                }
//                NavigationAc.addFr(new TeacherDetailsFragment(),"TeacherDetailsFragment",getFragmentManager(),1);
            }
        });
    }

    private void setView(){

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
                convertView = Inflater.inflate(R.layout.consult_listitem, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
                String content = mlist.get(position);

                holder.tv_abstract.setText(content);
//
//            ImageLoader.getInstance().displayImage(music.getIcon_url(),holder.iv_book);
//            holder.txt_book_content.setText(music.getDescription());
//            holder.txt_book_name.setText(music.getTitle());
//            holder.music_update_year.setText(music.getUptime());
//            holder.music_num.setText(music.getHit()+"");
            return convertView;
        }

        public final class ViewHolder {
//            @Bind(R.id.iv_book.
//            @Bind(R.id.txt_book_name)
//            TextView txt_book_name;
//            @Bind(R.id.txt_book_content)
//            TextView txt_book_content;
//            @Bind(R.id.music_update_year)
//            TextView music_update_year;
            @Bind(R.id.tv_abstract)
            TextView tv_abstract;

            public ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
