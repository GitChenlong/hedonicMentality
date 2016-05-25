package com.sage.hedonicmentality.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import com.sage.hedonicmentality.fragment.guide.FragmentGuide1;
import com.sage.hedonicmentality.fragment.guide.FragmentGuide2;
import com.sage.hedonicmentality.fragment.guide.FragmentGuide3;
import com.sage.hedonicmentality.myviewpager.FragmentPagerAdapter;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

	public MyFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
	}


	@Override
	public Fragment getItem(int position) {
		switch(position){
			case 0:
//				return new FragmentGuide1();
//			case 1:
//				return new FragmentGuide2();
//			case 2:
				return new FragmentGuide3();
		}
		return new Fragment();
	}

	@Override
	public int getCount() {
		return 1;
	}
	@Override
	public float getPageWidth(int position) {
		return super.getPageWidth(position);
	}

    /**
     * Returns the proportional size (width or height depending on orientation)
     * of a given page as a percentage of the ViewPager's measured size from (0.f-1.f).
     *
     * @param position The position of the page requested
     * @return Proportional size for the given page position
     */
    public float getPageSize(int position) {
        return getPageWidth(position);
    }
    
    
}
