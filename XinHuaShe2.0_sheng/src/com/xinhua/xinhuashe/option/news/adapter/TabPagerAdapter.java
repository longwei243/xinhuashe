package com.xinhua.xinhuashe.option.news.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.xinhua.xinhuashe.parentclass.ParentFragment;

public class TabPagerAdapter extends FragmentStatePagerAdapter {
	
	private List<ParentFragment> list;
	private List<String> tabTitles;
	
	public TabPagerAdapter(FragmentManager fm, List<ParentFragment> list, List<String> tabTitles) {
		super(fm);
		this.list = list;
		this.tabTitles = tabTitles;
	}
	
	@Override
	public Fragment getItem(int position) {
		return list.get(position);
	}

	@Override
	public int getCount() {
		return list.size();
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return tabTitles.get(position);
	}
	
//	@Override
//	public void destroyItem(ViewGroup container, int position, Object object) {
//		container.removeView(list.get(position).getView());
//	}

//	@Override
//	public Object instantiateItem(ViewGroup viewGroup, int position) {
//		return list.get(position);
//	}
	
//	@Override
//	public boolean isViewFromObject(View view, Object object) {
//		return view == object;
//	}
	
//	@Override
//	public int getItemPosition(Object object) {
//		return POSITION_NONE;
//	}
//	
//	@Override
//	public Parcelable saveState() {
//		return null;
//	}
	
}
