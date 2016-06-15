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
import android.widget.EditText;
import android.widget.ImageView;
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
 * Created by Administrator on 2016/6/12.
 * 优惠卡券
 */
public class DiscountCouponFragment extends Fragment {
    @Bind(R.id.lv_discount)
    ListView listView;
    @Bind(R.id.tv_title)
    TextView title;
    @Bind(R.id.cdkey)
    EditText cdkey;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.discountcouponfragment,null);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        title.setText(R.string.discountstring);
        getData();
    }

    @OnClick({R.id.ll_left,R.id.exchange})
    public void healthOnclick(View v) {
        if (v.getId()==R.id.ll_left) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
        if (v.getId()==R.id.exchange) {
            //兑换
            cdkey.getText();
        }
    }    @Override
         public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    public void getData(){
        List<String> list = new ArrayList<>();
        list.add("20");
        list.add("100");
        list.add("99");
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
                convertView = Inflater.inflate(R.layout.discount_item, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String content = mlist.get(position);
            holder.tv_price.setText(content);
            return convertView;
        }

        public final class ViewHolder {
            @Bind(R.id.btn_discount)
            Button btn_discount;
            @Bind(R.id.tv_price)
            TextView tv_price;
            @Bind(R.id.tv_yuan)
            TextView tv_yuan;
            @Bind(R.id.tv_time)
            TextView tv_time;
            @Bind(R.id.iv_type)
            ImageView iv_type;

            public ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
