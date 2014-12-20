package com.xinhua.xinhuashe.option;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.view.utils.DisplayUtil;
import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.domain.UserInfo;
import com.xinhua.xinhuashe.parentclass.ParentFragment;
import com.xinhua.xinhuashe.view.slidingmenu.adapter.SlidingMenuListViewAdapter;

/**
 * 左侧菜单控制类
 *
 * 
 */
public class LeftMenuFragment extends ParentFragment {

	private Callbacks callbacks = defaultCallbacks;
	private View leftmenu_userinfo_LinearLayout;
	private ImageView leftmenu_usericon_ImageView;
	public static TextView leftmenu_username_TextView;
	private ListView leftmenu_ListView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contextView = super.onCreateView(inflater, container,
				savedInstanceState);
		return contextView;
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			callbacks.onUserInfoClick(view);
		}
	};

	private OnItemClickListener menuListViewListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int i,
				long l) {
			switch (adapterView.getId()) {
			case R.id.leftmenu_ListView:
				callbacks.onLeftMenuClick(adapterView, view, i,
						adapterView.getItemAtPosition(i));
				break;
			default:
				break;
			}
		}

	};

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}
		callbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		callbacks = defaultCallbacks;
	}

	private static Callbacks defaultCallbacks = new Callbacks() {

		@Override
		public void onLeftMenuClick(AdapterView<?> adapterView, View view,
				int i, Object data) {
		}

		@Override
		public void onUserInfoClick(View v) {
			
		}

	};

	public interface Callbacks {

		/**
		 * CommonMenu列表项点击事件回掉接口
		 * 
		 * @param adapterView
		 * @param view
		 * @param i
		 */
		public void onLeftMenuClick(AdapterView<?> adapterView, View view,
				int i, Object data);
		
		/**
		 * 用户登录
		 * 
		 * @param v
		 */
		public void onUserInfoClick(View view);

	}

	@Override
	protected int getLayoutId() {
		return R.layout.leftmenu;
	}

	@Override
	protected void setupViews(View parentView) {
		leftmenu_userinfo_LinearLayout = parentView
				.findViewById(R.id.leftmenu_userinfo_LinearLayout);
		leftmenu_usericon_ImageView = (ImageView) parentView
				.findViewById(R.id.leftmenu_usericon_ImageView);
		leftmenu_username_TextView = (TextView) parentView
				.findViewById(R.id.leftmenu_username_TextView);
		leftmenu_ListView = (ListView) parentView
				.findViewById(R.id.leftmenu_ListView);
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("Recycle")
	@Override
	protected void initialized() {
		if (!"".equals(UserInfo.userName)) {
			LeftMenuFragment.leftmenu_username_TextView
					.setText(UserInfo.userName);
		}
		WindowManager windowManager = (WindowManager) this.getActivity()
				.getSystemService(Context.WINDOW_SERVICE);
		leftmenu_userinfo_LinearLayout
				.setLayoutParams(new LinearLayout.LayoutParams(windowManager
						.getDefaultDisplay().getWidth() / 4 * 3, DisplayUtil
						.dip2px(70, getResources().getDisplayMetrics().density)));
		leftmenu_userinfo_LinearLayout.setOnClickListener(clickListener);
		List<Map<String, Object>> commonData = new ArrayList<Map<String, Object>>();
		String[] titles = getResources().getStringArray(
				R.array.leftmenu_title_list);
		TypedArray icons = getResources().obtainTypedArray(
				R.array.leftmenu_icon_list);
		for (int i = 0; i < titles.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("title", titles[i]);
			map.put("icon", icons.getDrawable(i));
			commonData.add(map);
		}
		SlidingMenuListViewAdapter commonAdapter = new SlidingMenuListViewAdapter(
				this.getActivity(), commonData, R.layout.leftmenu_item,
				new String[] { "title", "icon" }, new int[] {
						R.id.leftmenu_item_title_TextView,
						R.id.leftmenu_item_icon_ImageView });
		leftmenu_ListView.setAdapter(commonAdapter);
		leftmenu_ListView.setOnItemClickListener(menuListViewListener);
	}

	@Override
	protected void threadTask() {

	}

}
