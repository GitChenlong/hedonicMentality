package com.sage.hedonicmentality.fragment.My;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.app.Http;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/6/13.
 */
public class MessageFragment extends Fragment {
    @Bind(R.id.tv_title)
    TextView title;
    @Bind(R.id.lv_health)
    ListView listView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.myattentionfragment,null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        title.setText(R.string.meesagetiltlename);
        getData();
    }
    @OnClick({R.id.ll_left})
    public void healthOnclick(View v) {
        if (v.getId()==R.id.ll_left) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }
    @Override
         public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    public void getData(){
        List<String> list = new ArrayList<>();
        list.add("我的消息一");
        list.add("我的消息二");
        list.add("我的消息三");
        HealthAdapter adapter = new HealthAdapter(getActivity(),list);
        listView.setAdapter(adapter);
        Http.getHealth("", "", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });
    }
    class HealthAdapter extends BaseAdapter {

        private final LayoutInflater Inflater;
        private final Context mcontext;
        private final List<String> mlist;

        public HealthAdapter(Context context, List<String> list) {

            this.Inflater = LayoutInflater.from(context);
            this.mcontext = context;
            this.mlist = list;
        }
        public void addBtn(Context context,String btnContent ,LinearLayout layout){
            Button button = new Button(context);
            button.setText(btnContent);
            button.setTextColor(getResources().getColor(R.color.txt_content_color));
            button.setTextSize(R.dimen.txt_12sp);
            button.setBackgroundColor(getResources().getColor(R.color.selector_white));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layout.addView(button,params);
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
        public View getView(int position, View convertView, ViewGroup arg2) {
            // TODO Auto-generated method stub
            final ViewHolder holder;
            if (convertView == null) {
                convertView = Inflater.inflate(R.layout.message_item, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String content = mlist.get(position);
            holder.txt_content.setText(content);
            holder.txt_time.setText("今天");
            return convertView;
        }

        public final class ViewHolder {
            @Bind(R.id.txt_content)
            TextView txt_content;
            @Bind(R.id.txt_time)
            TextView txt_time;

            public ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
