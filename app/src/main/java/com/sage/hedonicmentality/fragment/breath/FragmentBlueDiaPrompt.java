package com.sage.hedonicmentality.fragment.breath;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.service.BluetoothLeService;
import com.sage.hedonicmentality.ui.ActivityBreath;
import com.sage.hedonicmentality.ui.simple.BreathSetting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/1/19.
 */
public class FragmentBlueDiaPrompt extends DialogFragment implements View.OnClickListener{
//    @Bind(R.id.list_device)
    ListView list_device;
    public ArrayList<BluetoothDevice> bledevice;
    public NewsListAdapter adapter;
    public BreathSetting contexts;
    public static FragmentBlueDiaPrompt create(){
        FragmentBlueDiaPrompt fragment=new FragmentBlueDiaPrompt();
        return fragment;
    }
    public void setList(BreathSetting context, ArrayList<BluetoothDevice> device){
        bledevice = device;
        contexts = context;
        if(adapter!=null){

            adapter = new NewsListAdapter(contexts ,bledevice);
            list_device.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
    public FragmentBlueDiaPrompt(){
        setStyle(STYLE_NO_TITLE, R.style.DiaScaleAnimationTheme);//
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_bledio, container, false);
        adapter = new NewsListAdapter(contexts ,bledevice);
        list_device =  (ListView)view.findViewById(R.id.list_device);
        list_device.setAdapter(adapter);
        list_device.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothLeService.device_connect = bledevice.get(position);
                BluetoothLeService.deviecname = bledevice.get(position).getName();
                contexts.startActivity(new Intent(contexts, ActivityBreath.class));
                contexts.scanLeDevice(false);
                dismiss();
            }
        });
        ButterKnife.bind(this, view);
        return view;
    }

    private int tag;
    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);

        try {
            getView().findViewById(R.id.tv_close).setOnClickListener(this);
            getView().findViewById(R.id.iv_pic).setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Window window=getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable());
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.width = getResources().getDisplayMetrics().widthPixels*9/10;
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
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_close:
                dismiss();
                break;
            case R.id.iv_pic:
                FragmentDiaBuyDevice.create().show(getChildFragmentManager(), "buyDevice");
                break;

              default:
                break;
        }

    }
    public class NewsListAdapter extends BaseAdapter {
        private List<BluetoothDevice> bledevice;
        private Context ctx; // ������

        public NewsListAdapter(Context context, ArrayList<BluetoothDevice> bledevice) {
            this.bledevice = bledevice;
            this.ctx = context;

        }

        @Override
        public int getCount() {
            return bledevice == null ? 0 : bledevice.size();
        }

        @Override
        public Object getItem(int position) {
            return bledevice == null ? null : bledevice.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(ctx).inflate(R.layout.fragment_device_item, null);
                holder = new ViewHolder();
                holder.device_name_item = (TextView) convertView.findViewById(R.id.device_name_item);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.device_name_item.setText(bledevice.get(position).getName());
            return convertView;
        }

        private  class ViewHolder {
            TextView device_name_item;
        }
    }
}
