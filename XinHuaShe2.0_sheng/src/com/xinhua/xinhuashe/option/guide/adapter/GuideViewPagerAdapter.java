package com.xinhua.xinhuashe.option.guide.adapter;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class GuideViewPagerAdapter extends PagerAdapter{

	private ArrayList<View> arrayView;

	public GuideViewPagerAdapter(ArrayList<View> arrayView) {

		this.arrayView = arrayView;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public int getCount() {
		return arrayView.size();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView(arrayView.get(position));
	}


	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		((ViewPager) container).addView(arrayView.get(position));
		return arrayView.get(position);
	}
}
