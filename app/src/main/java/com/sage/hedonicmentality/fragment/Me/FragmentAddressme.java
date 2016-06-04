package com.sage.hedonicmentality.fragment.Me;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.app.Http;
import com.sage.hedonicmentality.bean.AddressBean;
import com.sage.hedonicmentality.fragment.BaseFragment;
import com.sage.hedonicmentality.ui.ActivityMe;
import com.sage.hedonicmentality.ui.simple.BreathSetting;
import com.sage.hedonicmentality.utils.Contact;
import com.sage.hedonicmentality.utils.GsonTools;
import com.sage.hedonicmentality.utils.SharedPreferencesHelper;
import com.sage.hedonicmentality.utils.Util;
import com.sage.hedonicmentality.view.SelectAdressPopupWindow;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/11/11.
 */
public class FragmentAddressme extends BaseFragment{
    @Bind(R.id.tv_right)
    public TextView tv_right;
    @Bind(R.id.tv_dwdress)
    public TextView tv_dwdress;
    @Bind(R.id.tv_address)
    public TextView tv_address;
    @Bind(R.id.iv_dw)
    public ImageView iv_dw;
    @Bind(R.id.iv_dr)
    public ImageView iv_dr;
    @Bind(R.id.layout_adress)
    public RelativeLayout layout_adress;
    @Bind(R.id.layout_actionbar)
    RelativeLayout layout_actionbar;

    private String sex;
    public AddressBean address = null;
    private String myaddress = "";
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case 8:
                    myaddress = msg.getData().getString("address");
                    tv_address.setText(myaddress);
                    iv_dw.setImageResource(R.mipmap.gray_wrong);
                    iv_dr.setImageResource(R.mipmap.green_right);
                    break;
            }
        }
    };

    @Override
    public int getLayout() {
        return R.layout.fragmnetaddress;
    }

    @Override
    public void initActionbar() {
        tv_right.setTextColor(getResources().getColor(R.color.white_color));
        layout_actionbar.setBackgroundResource(R.color.bg_title);
        tv_title.setTextColor(getResources().getColor(R.color.whi));
        btn_left.setImageResource(R.mipmap.back_01);
        tv_right.setText(R.string.baocun);
        tv_title.setText(getString(R.string.adresst));
        getAddress();
        dwaddress();
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!myaddress.equals("")&&!myaddress.equals(null)) {
                    SharedPreferencesHelper.getInstance().Builder(getActivity());
                    SharedPreferencesHelper.getInstance().putString(Contact.SH_Address, myaddress);
                    FragmentMeSetting.addressme = myaddress;
                    ActivityMe ac = (ActivityMe)getActivity();
                    ac.setaddress();
                    back();
                }
            }
        });
    }

    @OnClick({R.id.layout_adress})
    void registerClick(View view){
        SharedPreferencesHelper.getInstance().Builder(getActivity());

        switch(view.getId()){
            case R.id.layout_adress:
                //弹窗
                if(address != null){
                    chooseAddress();
                }
                break;
        }
    }
    private void chooseAddress() {
        SelectAdressPopupWindow selectBirthdayPopupWindow=new SelectAdressPopupWindow(getActivity(),address,mHandler);
        selectBirthdayPopupWindow.showAtLocation(getView().findViewById(R.id.layout_root), Gravity.BOTTOM, 0, 0);
    }
    public void  getAddress(){

        Util.showProgressFor(getActivity(), getResources().getString(R.string.longdings));
        Http.GetAddress(getActivity(), new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Util.cancelProgressFor(getActivity());
                String v = responseInfo.result;
                byte[] bytes = v.getBytes();
                try {
                    v = new String(bytes, "UTF-8");
                    address = GsonTools.changeGsonToBean(v, AddressBean.class);
                    LogUtils.e("-----" + address.getData().toString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Util.cancelProgressFor(getActivity());
            }
        });
    }
    private void nextPage(){
        getActivity().startActivity(new Intent(getActivity(), BreathSetting.class));
        getActivity().overridePendingTransition(R.anim.main_alpha, R.anim.login_scale_alpha);
        getActivity().finish();
    }

    //定位获取地址
    private void dwaddress(){
        LocationManager locationManager;
        String serviceName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager)getActivity().getSystemService(serviceName);
//        String provider = LocationManager.GPS_PROVIDER;
        String provider = LocationManager.NETWORK_PROVIDER;

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
//        String provider = locationManager.getBestProvider(criteria, true);

        Location location = locationManager.getLastKnownLocation(provider);
        updateWithNewLocation(location);
        locationManager.requestLocationUpdates(provider, 2000, 10,
                locationListener);
    }
    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            updateWithNewLocation(location);
        }
        public void onProviderDisabled(String provider){
            updateWithNewLocation(null);
        }
        public void onProviderEnabled(String provider){ }
        public void onStatusChanged(String provider, int status,
                                    Bundle extras){ }
    };
    private void updateWithNewLocation(Location location) {
        String latLongString = null;
        if (location != null) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            List<Address> addList = null;
            if(null == getActivity()){
                return;
            }
            Geocoder ge = new Geocoder(getActivity());
            try {
                addList = ge.getFromLocation(lat, lng, 1);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if(addList!=null && addList.size()>0){
                for(int i=0; i<addList.size(); i++){
                    Address ad = addList.get(i);
//                    latLongString += "\n";
//                    latLongString = ad.getCountryName() + " " + ad.getLocality()+" "+ad.getFeatureName();
                    latLongString = ad.getCountryName() + " " + ad.getLocality();
                }
            }
            if(latLongString!=null){
                tv_dwdress.setText(latLongString);
                myaddress = latLongString;
                LogUtils.e("您当前的位置是:\n" + latLongString);
                iv_dw.setImageResource(R.mipmap.green_right);
                iv_dr.setImageResource(R.mipmap.gray_wrong);
            }
        } else {
            latLongString = "无法获取地理信息";
        }
    }
}
