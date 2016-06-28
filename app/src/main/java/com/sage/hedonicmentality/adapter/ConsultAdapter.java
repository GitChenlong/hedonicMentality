package com.sage.hedonicmentality.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.sage.hedonicmentality.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/6/6.
 */
public class ConsultAdapter extends BaseAdapter {

    private final LayoutInflater Inflater;
    private final Context mcontext;
    private final List<String> mlist;

    public ConsultAdapter(Context context, List<String> list) {

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
            convertView = Inflater.inflate(R.layout.consult_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String content = mlist.get(position);

        holder.txt_username.setText(content);
//
        return convertView;
    }

    public final class ViewHolder {
        @Bind(R.id.txt_username)
        TextView txt_username;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
