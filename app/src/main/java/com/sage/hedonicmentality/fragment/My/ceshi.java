package com.sage.hedonicmentality.fragment.My;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/31.
 */
public class ceshi extends Fragment {
    @Bind(R.id.tv_order)
    TextView tv_order;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.ceshi,null);
        ButterKnife.bind(this,view);

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
        Http.getOrder("18684642028", "123456", new RequestCallBack<String>() {
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
                    tv_order.setText(orders.get(0).toString());
                    UtilSnackbar.showSimple(getView().findViewById(R.id.ll_ceshi), object.getString("tip"));

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
}
