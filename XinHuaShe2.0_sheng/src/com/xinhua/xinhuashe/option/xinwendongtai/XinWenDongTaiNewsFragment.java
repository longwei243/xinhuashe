package com.xinhua.xinhuashe.option.xinwendongtai;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.xinhua.xinhuashe.option.news.adapter.TabPagerAdapter;
import com.xinhua.xinhuashe.option.xinwendongtai.service.ColumnService;
import com.xinhua.xinhuashe.parentclass.ParentFragment;
import com.xinhua.xinhuashe.request.RequestURL;
import com.xinhua.xinhuashe.service.MobileApplication;
import com.xinhua.xinhuashe.service.ParentHandlerService;
import com.xinhua.xinhuashe.view.viewpager.PagerSlidingTabStrip;
/**
 * 新闻动态下的栏目页
 * @author LongWei
 *
 */
public class XinWenDongTaiNewsFragment extends ParentFragment implements IActivity,PagerSlidingTabStrip.CallBack {


	private String columnId;	//-- 栏目ID
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
//		Bundle b = getArguments();
//		columnId = (String) b.get("columnId");
//		System.out.println("传过来的栏目id是："+columnId);
		columnId = (String) params[0];
		try {
			String result = MobileApplication.cacheUtils.getAsString(ColumnService.ColumnInfo_XinWenDongTai);
			categories = ParentHandlerService.gson
					.fromJson(result,
							new TypeToken<LinkedList<Category>>() {
					}.getType());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contextView = super.onCreateView(inflater, container,
				savedInstanceState);
		//		threadTask();
		
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
		
		if("6".equals(columnId)) {
			//处理山西新闻
			doShanXiXinWen();
		}
		if("7".equals(columnId)) {
			//处理地市热点
			doDiShiReDian();
		}
		if("8".equals(columnId)) {
			//处理县区快报
			doXianQuKuaiBao();
		}
		if("9".equals(columnId)) {
			//处理厅局资讯
			doTingJuZiXun();
		}
	}
	/**
	 * 处理山西新闻
	 */
	private void doShanXiXinWen() {
		SlidingMenuControlActivity.main_header_title_TextView.setText("山西新闻");
		SlidingMenuControlActivity.main_header_text.setVisibility(View.GONE);
		SlidingMenuControlActivity.main_header_spinner_ImageView.setVisibility(View.GONE);
		
		initFragmentList();
	}
	/**
	 * 处理县区快报
	 */
	private void doXianQuKuaiBao() {
		SlidingMenuControlActivity.main_header_title_TextView.setText("县区快报");
		SlidingMenuControlActivity.main_header_text.setVisibility(View.VISIBLE);
		SlidingMenuControlActivity.main_header_text.setText(RequestURL.area);
		SlidingMenuControlActivity.main_header_spinner_ImageView.setVisibility(View.VISIBLE);
		SlidingMenuControlActivity.main_header_spinner_ImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), AddressSelectActivity.class);
				getActivity().startActivity(intent);

			}
		});
		
		initXianQuKuaiBaoFragmentList();
	}
	/**
	 * 初始化县区快报栏目页面
	 */
	private void initXianQuKuaiBaoFragmentList() {
		tabTitles = new ArrayList<String>();
		fragments = new ArrayList<ParentFragment>();
		tabTitles.clear();
		fragments.clear();
		int position = 0;
		for (int i = 0; i < categories.size(); i++) {
			Category category = categories.get(i);
			if (columnId.equals(category.getId() + "")) {
				List<Category> list = category.getChildList();
				if (list != null) {
					for (int j = 0; j < list.size(); j++) {
						Category childCategory = list.get(j);
						tabTitles.add(childCategory.getName());
						ParentFragment fragment = new XianQuKuaiBaoNewsItemFragment();
						ViewPagerItemInfo pageInfo = new ViewPagerItemInfo(
								childCategory.getName(), childCategory.getId().toString(),
								position++);
						pageInfo.setModule(childCategory.getModule());
						pageInfo.setColumnId(columnId);
						setArguments(fragment, pageInfo);
						fragments.add(fragment);
					}
				} else {}
			}
		}
		adapter = new TabPagerAdapter(this.getChildFragmentManager(),
				fragments, tabTitles);
		// 设置Adapter
		public_ViewPager.setAdapter(adapter);
		pagerSlidingTabStrip.setViewPager(public_ViewPager);
		OnPageChange(0);
	}

	/**
	 * 处理地市热点
	 */
	private void doDiShiReDian() {
		SlidingMenuControlActivity.main_header_title_TextView.setText("地市热点");
		SlidingMenuControlActivity.main_header_text.setVisibility(View.VISIBLE);
		SlidingMenuControlActivity.main_header_text.setText(RequestURL.shi);
		SlidingMenuControlActivity.main_header_spinner_ImageView.setVisibility(View.VISIBLE);
		SlidingMenuControlActivity.main_header_spinner_ImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), ShengShiAddressSelectActivity.class);
				getActivity().startActivity(intent);
			}
		});
		
		initDiShiReDianFragmentList();
	}
	/**
	 * 地市热点栏目页面
	 */
	private void initDiShiReDianFragmentList() {

		tabTitles = new ArrayList<String>();
		fragments = new ArrayList<ParentFragment>();
		tabTitles.clear();
		fragments.clear();
		int position = 0;
		for (int i = 0; i < categories.size(); i++) {
			Category category = categories.get(i);
			if (columnId.equals(category.getId() + "")) {
				List<Category> list = category.getChildList();
				if (list != null) {
					for (int j = 0; j < list.size(); j++) {
						Category childCategory = list.get(j);
						tabTitles.add(childCategory.getName());
						ParentFragment fragment = new DiShiReDianNewsItemFragment();
						ViewPagerItemInfo pageInfo = new ViewPagerItemInfo(
								childCategory.getName(), childCategory.getId().toString(),
								position++);
						pageInfo.setModule(childCategory.getModule());
						pageInfo.setColumnId(columnId);
						setArguments(fragment, pageInfo);
						fragments.add(fragment);
					}
				} else {}
			}
		}
		adapter = new TabPagerAdapter(this.getChildFragmentManager(),
				fragments, tabTitles);
		// 设置Adapter
		public_ViewPager.setAdapter(adapter);
		pagerSlidingTabStrip.setViewPager(public_ViewPager);
		OnPageChange(0);
	
	}

	/**
	 * 处理厅局资讯
	 */
	private void doTingJuZiXun() {
		SlidingMenuControlActivity.main_header_title_TextView.setText("厅局资讯");
		SlidingMenuControlActivity.main_header_text.setVisibility(View.VISIBLE);
		SlidingMenuControlActivity.main_header_text.setText(RequestURL.tingJu);
		SlidingMenuControlActivity.main_header_spinner_ImageView.setVisibility(View.VISIBLE);
		SlidingMenuControlActivity.main_header_spinner_ImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), TingJuSelectActivity.class);
				getActivity().startActivity(intent);
			
			}
		});
		initTingJuZiXunFragmentList();
	}

	private void initTingJuZiXunFragmentList() {


		tabTitles = new ArrayList<String>();
		fragments = new ArrayList<ParentFragment>();
		tabTitles.clear();
		fragments.clear();
		int position = 0;
		for (int i = 0; i < categories.size(); i++) {
			Category category = categories.get(i);
			if (columnId.equals(category.getId() + "")) {
				List<Category> list = category.getChildList();
				if (list != null) {
					for (int j = 0; j < list.size(); j++) {
						Category childCategory = list.get(j);
						tabTitles.add(childCategory.getName());
						ParentFragment fragment = new TingJuZiXunNewsItemFragment();
						ViewPagerItemInfo pageInfo = new ViewPagerItemInfo(
								childCategory.getName(), childCategory.getId().toString(),
								position++);
						pageInfo.setModule(childCategory.getModule());
						pageInfo.setColumnId(columnId);
						setArguments(fragment, pageInfo);
						fragments.add(fragment);
					}
				} else {}
			}
		}
		adapter = new TabPagerAdapter(this.getChildFragmentManager(),
				fragments, tabTitles);
		// 设置Adapter
		public_ViewPager.setAdapter(adapter);
		pagerSlidingTabStrip.setViewPager(public_ViewPager);
		OnPageChange(0);
	
	
	}

	@Override
	protected void initialized() {
//		tabTitles = new ArrayList<String>();
//		fragments = new ArrayList<ParentFragment>();
//		initFragmentList();
	}

	private void initFragmentList() {
		tabTitles = new ArrayList<String>();
		fragments = new ArrayList<ParentFragment>();
		tabTitles.clear();
		fragments.clear();
		int position = 0;
		for (int i = 0; i < categories.size(); i++) {
			Category category = categories.get(i);
			if (columnId.equals(category.getId() + "")) {
				List<Category> list = category.getChildList();
				if (list != null) {
					for (int j = 0; j < list.size(); j++) {
						Category childCategory = list.get(j);
						tabTitles.add(childCategory.getName());
						ParentFragment fragment = new ShanXiNewsItemFragment();
						ViewPagerItemInfo pageInfo = new ViewPagerItemInfo(
								childCategory.getName(), childCategory.getId().toString(),
								position++);
						pageInfo.setModule(childCategory.getModule());
						pageInfo.setColumnId(columnId);
						setArguments(fragment, pageInfo);
						fragments.add(fragment);
					}
				} else {}
			}
		}
		adapter = new TabPagerAdapter(this.getChildFragmentManager(),
				fragments, tabTitles);
		// 设置Adapter
		public_ViewPager.setAdapter(adapter);
		pagerSlidingTabStrip.setViewPager(public_ViewPager);
		OnPageChange(0);
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
		loadingDialog.cancel();
		if (params != null) {
			if (params[0] != null) {
			} else {
				loadingDialog.showTimeOut();
			}
		} else {
			loadingDialog.showTimeOut();
		}
	}

	@Override
	public void OnPageChange(int currentPosition) {
		// TODO Auto-generated method stub
		
	}

	


}
