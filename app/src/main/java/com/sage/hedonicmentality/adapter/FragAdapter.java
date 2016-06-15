package com.sage.hedonicmentality.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class FragAdapter extends FragmentPagerAdapter {

	private List<Fragment> mFragments;

	public FragAdapter(FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		// TODO Auto-generated constructor stub
		mFragments=fragments;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		Log.e("dasd",arg0+"");
		return mFragments.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mFragments.size();
	}
	@Override
	public int getItemPosition(Object object) {
		Log.e("POSITION_NONE",POSITION_NONE+"");
		return POSITION_NONE;
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(mFragments.get(position).getView());
	}
}
