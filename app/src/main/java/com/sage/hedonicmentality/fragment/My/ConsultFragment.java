package com.sage.hedonicmentality.fragment.My;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.app.Http;
import com.sage.hedonicmentality.app.NavigationAc;
import com.sage.hedonicmentality.bean.Indicate;
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
 */
public class ConsultFragment extends Fragment {
    @Bind(R.id.iv_two_dimension_code)
    ImageView two_dimension_code;//二维码
    @Bind(R.id.tv_time)
    TextView tv_time;//咨询时间
    @Bind(R.id.tv_be_adept_at)
    TextView tv_be_adept_at;//擅长
    @Bind(R.id.tv_screen)
    TextView ll_time;//筛选
    @Bind(R.id.lv_zixun)
    ListView lv_zixun;//筛选
    private Indicate dateIn;
    private Indicate timeIn;
    private Indicate begoodatIn;

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

    @OnClick({R.id.tv_time,R.id.tv_be_adept_at,R.id.tv_screen,R.id.iv_two_dimension_code})
    public void consultOnclick(View view){
        switch (view.getId()){
            case R.id.iv_two_dimension_code: //二维码

                break;
            case R.id.tv_time://咨询时间
                showPopConsult();
                break;
            case R.id.tv_be_adept_at://擅长
                showPopBeGoodAt();

                break;
            case R.id.tv_screen://筛选
                showScreenPop();
                break;
        }
    }
    public void showPopBeGoodAt(){
        BeGoodAtPopwindow popwindow = new BeGoodAtPopwindow(getActivity(),mHandler,begoodatIn);
        popwindow.showAsDropDown(getView().findViewById(R.id.tv_be_adept_at));
    }
    public void showScreenPop(){
        ScreenPopWindow popwindow = new ScreenPopWindow(getActivity(),mHandler);
        popwindow.showAsDropDown(getView().findViewById(R.id.tv_be_adept_at));
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
        CnsultTimePopWindow popWindow = new CnsultTimePopWindow(getActivity(),mHandler,date,time,dateIn,timeIn);
        popWindow.showAsDropDown(getView().findViewById(R.id.tv_time));

    }
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
//                case 1:
//                    String date = msg.getData().getString("date");
//                    Toast.makeText(getActivity(), date, Toast.LENGTH_SHORT).show();
//                    break;
                case 2:
                    String selct = msg.getData().getString("select");
                    Log.e("result","what=2"+selct);
                    break;
                case 1:
                    //按咨询时间搜索
                    String timename = msg.getData().getString("timename");
                    String datename = msg.getData().getString("datename");
                    Log.e("result","what=1"+datename+"/"+timename+"/"+dateIn.getPostion());
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
                NavigationAc.addFr(new TeacherDetailsFragment(),"TeacherDetailsFragment",getFragmentManager(),1);
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
