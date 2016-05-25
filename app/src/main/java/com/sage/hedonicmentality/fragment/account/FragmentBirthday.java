package com.sage.hedonicmentality.fragment.account;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.fragment.BaseFragment;
import com.sage.hedonicmentality.ui.ActivityLogin;
import com.sage.hedonicmentality.utils.Contact;
import com.sage.hedonicmentality.utils.SharedPreferencesHelper;
import com.sage.hedonicmentality.utils.UtilSnackbar;
import com.sage.libwheelview.widget.wheel.OnWheelChangedListener;
import com.sage.libwheelview.widget.wheel.WheelView;
import com.sage.libwheelview.widget.wheel.adapter.NumericWheelAdapter;

import java.util.Calendar;

import butterknife.Bind;

/**
 * Created by Administrator on 2015/11/2.
 */
public class FragmentBirthday extends BaseFragment {
    @Bind(R.id.year_birthday)
    public WheelView year;
    @Bind(R.id.month_birthday)
    public WheelView month;
    @Bind(R.id.day_birthday)
    public WheelView day;
    @Bind(R.id.tv_right)
    public TextView tv_right;
    private View mMenuView;
    private ViewFlipper viewfipper;
    private Button btn_submit, btn_cancel;
    public static String age = "1993-10-10";
    private DateNumericAdapter monthAdapter, dayAdapter, yearAdapter;
    private int mCurYear = 80, mCurMonth = 5, mCurDay = 14;
    private String[] dateType;
//    private Handler mHandler;
    @Override
    public int getLayout() {
        return R.layout.fragment_birthday;
    }
    @Override
    public void back() {
        getActivity().onBackPressed();
    }

    private void next() {
            ((ActivityLogin)getActivity()).GoAddress();
    }

    @Override
    public void initActionbar() {
        btn_left.setVisibility(View.GONE);
        tv_right.setText(R.string.next);
        tv_title.setText(getString(R.string.brtitle));
        LayoutInflater inflater = (LayoutInflater)getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(com.sage.libwheelview.R.layout.popupwindow_birthday, null);
        viewfipper = new ViewFlipper(getActivity());
        viewfipper.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

//        btn_submit.setOnClickListener(this);
//        btn_cancel.setOnClickListener(this);
        Calendar calendar = Calendar.getInstance();
        OnWheelChangedListener listener = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateDays(year, month, day);

            }
        };
        int curYear = calendar.get(Calendar.YEAR);
        if (age != null && age.contains("-")) {
            String str[] = age.split("-");
            mCurYear = 100 - (curYear - Integer.parseInt(str[0]));
            mCurMonth = Integer.parseInt(str[1]) - 1;
            mCurDay = Integer.parseInt(str[2]) - 1;
        }
        dateType = getActivity().getResources().getStringArray(com.sage.libwheelview.R.array.date);
        monthAdapter = new DateNumericAdapter(getActivity(), 1, 12, 5);
        monthAdapter.setTextType(dateType[1]);
        month.setViewAdapter(monthAdapter);
        month.setCurrentItem(mCurMonth);
        month.addChangingListener(listener);
        // year

        yearAdapter = new DateNumericAdapter(getActivity(), curYear - 100, curYear+100,
                100 - 20);
        yearAdapter.setTextType(dateType[0]);
        year.setViewAdapter(yearAdapter);
        year.setCurrentItem(mCurYear);
        year.addChangingListener(listener);
        // day

        updateDays(year, month, day);
        day.setCurrentItem(mCurDay);
        updateDays(year, month, day);
        day.addChangingListener(listener);

        viewfipper.addView(mMenuView);
        viewfipper.setFlipInterval(6000000);

        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesHelper.getInstance().Builder(getActivity());
                SharedPreferencesHelper.getInstance().putString(Contact.SH_Birthday, age);
                next();
            }
        });
    }
    private void updateDays(WheelView year, WheelView month, WheelView day) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,
                calendar.get(Calendar.YEAR) + year.getCurrentItem());
        calendar.set(Calendar.MONTH, month.getCurrentItem());

        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        dayAdapter = new DateNumericAdapter(getActivity(), 1, maxDays,
                calendar.get(Calendar.DAY_OF_MONTH) - 1);
        dayAdapter.setTextType(dateType[2]);
        day.setViewAdapter(dayAdapter);
        int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
        day.setCurrentItem(curDay - 1, true);
        int years = calendar.get(Calendar.YEAR) - 100;
        age = years + "-" + (month.getCurrentItem() + 1) + "-"
                + (day.getCurrentItem() + 1);
    }

//    public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.btn_submit_select_birthday:
//			Message message = Message.obtain();
//			message.what=8;
//		    Bundle bundle = new Bundle();
//		    bundle.putString("birthday", age);
//		    message.setData(bundle);
//			mHandler.sendMessage(message);
//			this.dismiss();
//			break;
//        case R.id.btn_cancel_select_birthday:
//        	this.dismiss();
//			break;
//		default:
//			break;
//		}

//        if(v.getId()== com.sage.libwheelview.R.id.btn_submit_select_birthday){
//            Message message = Message.obtain();
//            message.what=8;
//            Bundle bundle = new Bundle();
//            bundle.putString("birthday", age);
//            message.setData(bundle);
//            mHandler.sendMessage(message);
//        }else if(v.getId()== com.sage.libwheelview.R.id.btn_cancel_select_birthday){
//
//        }
//
//    }

    /**
     * Adapter for numeric wheels. Highlights the current value.
     */
    private class DateNumericAdapter extends NumericWheelAdapter {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;

        /**
         * Constructor
         */
        public DateNumericAdapter(Context context, int minValue, int maxValue,
                                  int current) {
            super(context, minValue, maxValue);
            this.currentValue = current;
            setTextSize(18);

        }

        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            view.setTypeface(Typeface.SANS_SERIF);
        }

        public CharSequence getItemText(int index) {
            currentItem = index;
            return super.getItemText(index);
        }

    }
}
