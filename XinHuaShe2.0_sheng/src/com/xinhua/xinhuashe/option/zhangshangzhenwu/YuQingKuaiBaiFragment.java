package com.xinhua.xinhuashe.option.zhangshangzhenwu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.threadpool.IActivity;
import com.google.gson.reflect.TypeToken;
import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.domain.Category;
import com.xinhua.xinhuashe.domain.ViewPagerItemInfo;
import com.xinhua.xinhuashe.option.SlidingMenuControlActivity;
import com.xinhua.xinhuashe.option.address.AddressSelectActivity;
import com.xinhua.xinhuashe.option.address.ShengShiAddressSelectActivity;
import com.xinhua.xinhuashe.option.address.TingJuSelectActivity;
import com.xinhua.xinhuashe.option.news.NewsItemFragment;
import com.xinhua.xinhuashe.option.news.adapter.TabPagerAdapter;
import com.xinhua.xinhuashe.option.xinwendongtai.DiShiReDianNewsItemFragment;
import com.xinhua.xinhuashe.option.xinwendongtai.TingJuZiXunNewsItemFragment;
import com.xinhua.xinhuashe.option.xinwendongtai.XianQuKuaiBaoNewsItemFragment;
import com.xinhua.xinhuashe.option.xinwendongtai.service.ColumnService;
import com.xinhua.xinhuashe.parentclass.ParentFragment;
import com.xinhua.xinhuashe.request.RequestURL;
import com.xinhua.xinhuashe.service.MobileApplication;
import com.xinhua.xinhuashe.service.ParentHandlerService;
import com.xinhua.xinhuashe.view.viewpager.PagerSlidingTabStrip;
/**
 * 舆情快报页面
 * @author LongWei
 *
 */
public class YuQingKuaiBaiFragment extends ParentFragment implements IActivity,PagerSlidingTabStrip.CallBack {



	private String columnId = "26";	//-- 栏目ID
	private PagerSlidingTabStrip pagerSlidingTabStrip;
	private ImageView spinner_ImageView;
	private ViewPager public_ViewPager;
	private TabPagerAdapter adapter;

	private List<ParentFragment> fragments;
	private List<Category> categories;
	private List<String> tabTitles;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobileApplication.allIActivity.add(this);
//		try {
//			String result = MobileApplication.cacheUtils.getAsString(ColumnService.ColumnInfo_YuQingKuaiBao);
//			categories = ParentHandlerService.gson
//					.fromJson(result,
//							new TypeToken<LinkedList<Category>>() {
//					}.getType());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contextView = super.onCreateView(inflater, container,
				savedInstanceState);
		//		threadTask();
		SlidingMenuControlActivity.main_header_title_TextView.setText("舆情快报");
		SlidingMenuControlActivity.main_header_text.setVisibility(View.GONE);
		SlidingMenuControlActivity.main_header_spinner_ImageView.setVisibility(View.GONE);
		return contextView;
	}
	
	
	
	@Override
	public void onStart() {
		super.onStart();
		PagerSlidingTabStrip.callBack = this;
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {}
		}
	};

	@Override
	protected int getLayoutId() {
		return R.layout.public_viewpager;
	}

	@Override
	protected void setupViews(View parentView) {
		pagerSlidingTabStrip = (PagerSlidingTabStrip) parentView
				.findViewById(R.id.public_viewpager_PagerSlidingTabStrip);
		spinner_ImageView = (ImageView) parentView
				.findViewById(R.id.public_viewpager_spinner_ImageView);
		spinner_ImageView.setOnClickListener(clickListener);
		public_ViewPager = (ViewPager) parentView
				.findViewById(R.id.public_ViewPager);
		public_ViewPager.setOffscreenPageLimit(2);
		pagerSlidingTabStrip.setUnderlineColorResource(R.color.transparent);
		pagerSlidingTabStrip.setIndicatorHeight(5);
		pagerSlidingTabStrip
		.setIndicatorColorResource(R.color.viewpager_tab_bg_selected);
		pagerSlidingTabStrip
		.setTextColorResource(R.drawable.viewpager_tab_text_selector);
		
		doYuQingKuaiBao();
	}
	/**
	 * 处理舆情快报
	 */
	private void doYuQingKuaiBao() {
		
		
		initYuQingKuaiBaoFragmentList();
	}
	/**
	 * 初始化舆情快报栏目页面
	 */
	private void initYuQingKuaiBaoFragmentList() {
		tabTitles = new ArrayList<String>();
		fragments = new ArrayList<ParentFragment>();
		tabTitles.clear();
		fragments.clear();
		int position = 0;
		
		tabTitles.add("舆情快报");
		tabTitles.add("半月舆情动态");
		tabTitles.add("分析报告");
		
		ParentFragment fragment1 = new YuQingKuaiBaoNewsItemFragment();
		ViewPagerItemInfo pageInfo = new ViewPagerItemInfo(
				"舆情快报", "38",position++);
		pageInfo.setModule("article");
		pageInfo.setColumnId(columnId);
		setArguments(fragment1, pageInfo);
		fragments.add(fragment1);
		ParentFragment fragment2 = new YuQingKuaiBaoNewsItemFragment();
		ViewPagerItemInfo pageInfo2 = new ViewPagerItemInfo(
				"半月舆情动态", "39",position++);
		pageInfo2.setModule("article");
		pageInfo2.setColumnId(columnId);
		setArguments(fragment2, pageInfo2);
		fragments.add(fragment2);
		ParentFragment fragment3 = new FenXiBaoGaoFragment();
		fragments.add(fragment3);
		
		adapter = new TabPagerAdapter(this.getChildFragmentManager(),
				fragments, tabTitles);
		// 设置Adapter
		public_ViewPager.setAdapter(adapter);
		pagerSlidingTabStrip.setViewPager(public_ViewPager);
		OnPageChange(0);
	}

	
	@Override
	protected void initialized() {
	
	}

	
	@Override
	protected void threadTask() {

	}

	@Override
	public void closeLoadingView() {
		loadingDialog.cancel();
	}

	@Override
	public void init() {

	}

	@SuppressWarnings("unchecked")
	@Override
	public void refresh(int taskId, Object... params) {
		
	}

	@Override
	public void OnPageChange(int currentPosition) {
		// TODO Auto-generated method stub
		
	}

	



}
