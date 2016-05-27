package com.sage.hedonicmentality.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

public class FragAdapter extends FragmentPagerAdapter {

	/*private List<Fragment> fragments;
	private FragmentManager manager;

	public FragAdapter(FragmentManager manager, List<Fragment> fragments) {
		super();
		this.fragments = fragments;
		this.manager = manager;
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(fragments.get(position).getView());
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		Fragment fragment = fragments.get(position);
		if (!fragment.isAdded()) {
			FragmentTransaction transaction = manager.beginTransaction();
			transaction.add(fragment, fragment.getClass().getSimpleName());
			transaction.commitAllowingStateLoss();
			manager.executePendingTransactions();
		}
		if (fragment.getView().getParent() == null) {
			container.addView(fragment.getView());
		}
		return fragment.getView();
	}*/
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
//		container.removeView(mFragments.get(position).getView());
	}
}
