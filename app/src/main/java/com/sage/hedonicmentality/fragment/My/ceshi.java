package com.sage.hedonicmentality.fragment.My;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.alipay.AlipayFragment;
import com.sage.hedonicmentality.app.Http;
import com.sage.hedonicmentality.app.NavigationAc;
import com.sage.hedonicmentality.bean.Order;
import com.sage.hedonicmentality.bean.OrderListBean;
import com.sage.hedonicmentality.utils.GsonTools;
import com.sage.hedonicmentality.utils.UtilSnackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.zip.Inflater;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/31.
 */
public class ceshi extends Fragment {
    @Bind(R.id.tv_order)
    TextView tv_order;
    @Bind(R.id.lv_order)
    ListView lv_order;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.ceshi, null);
        ButterKnife.bind(this,view);
        getOrder();
        return view;

    }
    @OnClick({R.id.getorder,R.id.pay,R.id.bt_vedio})
    public void ceshiO(View view) {
        if (view.getId()==R.id.getorder) {
            getOrder();
        }
        if (view.getId()==R.id.pay) {
            NavigationAc.addFr(new AlipayFragment(), "AlipayFragment", getActivity().getSupportFragmentManager(), 1);
        }
        if (view.getId()==R.id.bt_vedio) {
            NavigationAc.addFr(new VideoCallFragment(),"VideoCallFragment",getActivity().getSupportFragmentManager(),1);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
    public void getOrder(){
        UserFragment userFragment = (UserFragment)getActivity().getSupportFragmentManager().findFragmentByTag("UserFragment");
        Http.getOrder( userFragment.user,  userFragment.ps, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String v = responseInfo.result;
                byte[] bytes = v.getBytes();
                try {
                    v = new String(bytes, "UTF-8");
                    JSONObject object = new JSONObject(v);
                    int info = object.getInt("info");
                    if (info != 1) {
                        UtilSnackbar.showSimple(getView().findViewById(R.id.ll_ceshi), object.getString("tip"));
                        return;
                    }
                    JSONObject data = object.getJSONObject("data");
                    OrderListBean bean = GsonTools.changeGsonToBean(data.toString(), OrderListBean.class);
                    ArrayList<Order> orders = new ArrayList<Order>();
                    orders.addAll(bean.getOder());
                    OrderListAdapter adapter = new OrderListAdapter(orders,getActivity());
                    lv_order.setAdapter(adapter);
                    lv_order.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            NavigationAc.addFr(new VideoCallFragment(),"VideoCallFragment",getActivity().getSupportFragmentManager(),1);
                        }
                    });
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });
    }
    class OrderListAdapter extends BaseAdapter{
        private  LayoutInflater Inflater;
        private ArrayList<Order> orders ;
        private Context mContext;

        public OrderListAdapter(ArrayList<Order> orders, Context mContext) {
            this.orders = orders;
            this.mContext = mContext;
            this.Inflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return this.orders == null ? 0:this.orders.size();
        }

        @Override
        public Object getItem(int position) {
            return this.orders.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final ViewHolder holder;
            if (convertView == null) {
                convertView = Inflater.inflate(R.layout.orderlist_item, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Order order = this.orders.get(position);
            holder.one.setText(order.getOrdernum());
            holder.two.setText(order.getPrice());
            holder.three.setText(order.getOrder_status());
            holder.four.setText(order.getPay_status());
//
            return convertView;
        }

        public final class ViewHolder {
            @Bind(R.id.one)
            TextView one;
            @Bind(R.id.two)
            TextView two;
            @Bind(R.id.three)
            TextView three;
            @Bind(R.id.four)
            TextView four;

            public ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

    public class A
    {
        private int level;

        public A(int level){
            this.level = level;
        }

        public int getLevel()
        {
            return level;
        }

        public void setLevel(int level)
        {
            this.level = level;
        }
    }
    public class SortComparator implements Comparator {
        @Override
        public int compare(Object lhs, Object rhs) {
            A a = (A) lhs;
            A b = (A) rhs;

            return (b.getLevel() - a.getLevel());
        }
    }
}
