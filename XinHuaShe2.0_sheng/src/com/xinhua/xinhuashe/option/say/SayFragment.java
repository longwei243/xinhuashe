package com.xinhua.xinhuashe.option.say;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.threadpool.IActivity;
import com.google.gson.reflect.TypeToken;
import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.domain.Category;
import com.xinhua.xinhuashe.domain.ViewPagerItemInfo;
import com.xinhua.xinhuashe.option.SlidingMenuControlActivity;
import com.xinhua.xinhuashe.option.login.SelectMydataPicActivity;
import com.xinhua.xinhuashe.option.news.NewsItemFragment;
import com.xinhua.xinhuashe.option.news.adapter.TabPagerAdapter;
import com.xinhua.xinhuashe.option.staggeredgridview.test.SGVFragment;
import com.xinhua.xinhuashe.option.staggeredgridview.test.UploadSelectedPicFragment;
import com.xinhua.xinhuashe.option.xinwendongtai.service.ColumnService;
import com.xinhua.xinhuashe.parentclass.ParentFragment;
import com.xinhua.xinhuashe.service.MobileApplication;
import com.xinhua.xinhuashe.service.ParentHandlerService;
import com.xinhua.xinhuashe.view.viewpager.PagerSlidingTabStrip;

public class SayFragment extends ParentFragment implements IActivity {

	private PagerSlidingTabStrip pagerSlidingTabStrip;
	private ViewPager public_ViewPager;
	private TabPagerAdapter adapter;
	public static String picPath = "";
	private List<ParentFragment> fragments;
	private List<String> tabTitles, tabIds;
	private List<Category> categories;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobileApplication.allIActivity.add(this);
		try {
			String result = MobileApplication.cacheUtils
					.getAsString(ColumnService.ColumnInfo_HuDongFenXiang);
			categories = ParentHandlerService.gson.fromJson(result,
					new TypeToken<LinkedList<Category>>() {
					}.getType());
			Log.i(MobileApplication.TAG, result);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contextView = super.onCreateView(inflater, container,
				savedInstanceState);
		threadTask();
		initFragmentList();
		SlidingMenuControlActivity.main_header_title_TextView.setText("互动分享");
		return contextView;
	}

	@Override
	protected int getLayoutId() {
		return R.layout.public_viewpager;
	}

	@Override
	protected void setupViews(View parentView) {
		// System.out.println("---fragments---" + fragments.size());
		pagerSlidingTabStrip = (PagerSlidingTabStrip) parentView
				.findViewById(R.id.public_viewpager_PagerSlidingTabStrip);
		public_ViewPager = (ViewPager) parentView
				.findViewById(R.id.public_ViewPager);
		public_ViewPager.setOffscreenPageLimit(2);
		pagerSlidingTabStrip.setUnderlineColorResource(R.color.transparent);
		pagerSlidingTabStrip.setIndicatorHeight(5);
		pagerSlidingTabStrip
				.setIndicatorColorResource(R.color.viewpager_tab_bg_selected);
		pagerSlidingTabStrip
				.setTextColorResource(R.drawable.viewpager_tab_text_selector);
	}

	private void initFragmentList() {
		int position = 0;
		for (int i = 0; i < tabTitles.size(); i++) {
			ParentFragment fragment = null;
			if ("50".endsWith(tabIds.get(i))) {
				fragment = new SayItemFragment();
			} else if ("51".endsWith(tabIds.get(i))) {
				fragment = new SGVFragment();
			} else {
				fragment = new NewsItemFragment();
			}
			ViewPagerItemInfo pageInfo = new ViewPagerItemInfo(
					tabTitles.get(i), tabIds.get(i), position++);
			setArguments(fragment, pageInfo);
			fragments.add(fragment);
		}

		adapter = new TabPagerAdapter(this.getChildFragmentManager(),
				fragments, tabTitles);
		// 设置Adapter
		public_ViewPager.setAdapter(adapter);
		pagerSlidingTabStrip.setViewPager(public_ViewPager);
	}

	@Override
	protected void initialized() {
		tabTitles = new ArrayList<String>();
		// tabTitles.add("有话要说");
		// tabTitles.add("随手拍");
		// tabTitles.add("E言堂");
		tabIds = new ArrayList<String>();
		// tabIds.add("0");
		// tabIds.add("1");
		// tabIds.add("2");
		fragments = new ArrayList<ParentFragment>();
		if (categories != null && !"".equals(categories)) {
			for (int i = 0; i < categories.size(); i++) {
				Category category = categories.get(i);
				tabTitles.add(category.getName());
				tabIds.add(category.getId() + "");
			}
		} else {
			Toast.makeText(SlidingMenuControlActivity.activity, "没有数据",
					Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	protected void threadTask() {
	}

	@Override
	public void closeLoadingView() {

	}

	@Override
	public void init() {

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == SGVFragment.ATTACHMENT) {
				picPath = data
						.getStringExtra(SelectMydataPicActivity.KEY_PHOTO_PATH);
				Log.i(MobileApplication.TAG, "最终选择的图片：" + picPath);
				ParentFragment fragment = new UploadSelectedPicFragment();
				Map<String, String> map = new HashMap<String, String>();
				map.put("picPath", picPath);
				switchFragment(fragment, fragment.getClass().getSimpleName(),
						map);
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void refresh(int TaskId, Object... params) {
		loadingDialog.cancel();
		if (params != null) {
			if (params[0] != null) {
				try {
					tabIds.clear();
					tabTitles.clear();
					JSONArray jsonaArray = new JSONArray(params[0].toString());
					for (int i = 0; i < jsonaArray.length(); i++) {
						JSONObject jb = jsonaArray.getJSONObject(i);
						tabIds.add(jb.getString("value"));
						tabTitles.add(jb.getString("label"));
						Log.i(MobileApplication.TAG,
								jb.getString("value") + jb.getString("label"));
					}
					initFragmentList();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				loadingDialog.showTimeOut();
			}
		} else {
			loadingDialog.showTimeOut();
		}
	}

}
