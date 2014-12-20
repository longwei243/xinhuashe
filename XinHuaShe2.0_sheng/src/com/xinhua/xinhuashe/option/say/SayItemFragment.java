package com.xinhua.xinhuashe.option.say;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.task.AbTaskQueue;
import com.ab.view.listener.AbOnListViewListener;
import com.ab.view.pullview.AbPullListView;
import com.android.view.utils.SelecterUtil;
import com.google.gson.reflect.TypeToken;
import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.domain.JsonPageModel;
import com.xinhua.xinhuashe.domain.Suggestion;
import com.xinhua.xinhuashe.domain.UserInfo;
import com.xinhua.xinhuashe.domain.ViewPagerItemInfo;
import com.xinhua.xinhuashe.option.SlidingMenuControlActivity;
import com.xinhua.xinhuashe.option.say.adapter.SayListViewAdapter;
import com.xinhua.xinhuashe.parentclass.ParentFragment;
import com.xinhua.xinhuashe.request.RequestURL;
import com.xinhua.xinhuashe.service.MobileApplication;
import com.xinhua.xinhuashe.service.ParentHandlerService;
import com.xinhua.xinhuashe.util.NetUtils;

/**
 * 有话要说-列表
 * 
 * @author azuryleaves
 * @since 2014-4-12 下午1:58:41
 * @version 1.0
 * 
 */
public class SayItemFragment extends ParentFragment {

	private ViewPagerItemInfo pageInfo;
	private AbPullListView listView;
	private Button say_publish_button;
	private AbTaskQueue mAbTaskQueue = null;
	private boolean isLastPage = false;
	private LinkedList<Suggestion> suggestions;
	private List<Suggestion> newSuggestions;
	private SayListViewAdapter adapter;
	private JsonPageModel<Suggestion> jsonPageModel;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		pageInfo = (ViewPagerItemInfo) params[0];
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View contextView = super.onCreateView(inflater, container,
				savedInstanceState);
		threadTask();
		return contextView;
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.say_publish_button:
				if ("".equals(UserInfo.userId)) {
					Toast.makeText(SlidingMenuControlActivity.activity,
							R.string.nologin, Toast.LENGTH_SHORT).show();
					return;
				}
				ParentFragment fragment = new SayPublishFragment();
				FragmentTransaction f = SlidingMenuControlActivity.activity
						.getSupportFragmentManager().beginTransaction();
				f.setCustomAnimations(R.anim.translate_up,
						R.anim.translate_down);
				f.replace(R.id.slidingmenu_control_FrameLayout, fragment)
				.addToBackStack(fragment.getClass().getSimpleName());
				f.commit();
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onStart() {
		super.onStart();
		final AbTaskItem refreshItem = new AbTaskItem();
		refreshItem.listener = new AbTaskListener() {

			@Override
			public void update() {
				suggestions.clear();
				if (newSuggestions != null && newSuggestions.size() > 0) {
					suggestions.addAll(newSuggestions);
					newSuggestions.clear();
					adapter.notifyDataSetChanged();
				}
				listView.stopRefresh();
				isLastPage=false;
			}

			@Override
			public void get() {
				System.out.println("---GET请求方式URL---"
						+ RequestURL.getSayList("1"));
				Map<String, Object> resultMap = NetUtils.doHttpGetSetCookie(
						RequestURL.getSayList("1"), "UTF-8");
				String result = "";
				if (resultMap != null) {
					result = (String) resultMap.get(NetUtils.Result);
				}
				System.out.println("---GET请求方式返回结果---" + result);
				if (result != null && !"".equals(result)) {
					jsonPageModel = ParentHandlerService.gson.fromJson(result,
							new TypeToken<JsonPageModel<Suggestion>>() {
					}.getType());
					newSuggestions = jsonPageModel.getContent();
				}
			};
		};

		final AbTaskItem loadMoreItem = new AbTaskItem();
		loadMoreItem.listener = new AbTaskListener() {

			@Override
			public void update() {
				Log.i("dlsjlkfjl;", "caonima de zenm, e;l e");
				if (newSuggestions != null && newSuggestions.size() > 0) {
					suggestions.addAll(newSuggestions);
					newSuggestions.clear();
					adapter.notifyDataSetChanged();
				}
				listView.stopLoadMore();
			}

			@Override
			public void get() {
				newSuggestions = new LinkedList<Suggestion>();
				Map<String, Object> resultMap = NetUtils.doHttpGetSetCookie(
						RequestURL.getSayList(jsonPageModel.getNext() + ""),
						"UTF-8");
				String result = "";
				if (resultMap != null) {
					result = (String) resultMap.get(NetUtils.Result);
				}
				if (result != null && !"".equals(result)) {
					jsonPageModel = ParentHandlerService.gson.fromJson(result,
							new TypeToken<JsonPageModel<Suggestion>>() {
					}.getType());
					newSuggestions = jsonPageModel.getContent();
					isLastPage = jsonPageModel.isLastPage();
				}
			};
		};

		listView.setAbOnListViewListener(new AbOnListViewListener() {

			@Override
			public void onRefresh() {
				mAbTaskQueue.execute(refreshItem);
			}

			@Override
			public void onLoadMore() {
//				listView.stopLoadMore();
				if (isLastPage) {
					listView.stopLoadMore();
//					Toast.makeText(SlidingMenuControlActivity.activity,
//							"已经是最后一页了", Toast.LENGTH_SHORT).show();
				} else {
					mAbTaskQueue.execute(loadMoreItem);
				}
			}

		});

		// 第一次下载数据
		mAbTaskQueue.execute(refreshItem);
	}

	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapterView, View view,
				int position, long l) {
			if (--position >= 0) {
				Suggestion suggestion = (Suggestion) adapter.getItem(position);
				ParentFragment fragment = new SayDetailFragment();
				switchFragment(fragment, fragment.getClass().getSimpleName(),
						suggestion.getId() + "");
			}
		}
	};

	@Override
	protected int getLayoutId() {
		return R.layout.say;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void setupViews(View parentView) {
		listView = (AbPullListView) parentView.findViewById(R.id.say_ListView);
		say_publish_button = (Button) parentView
				.findViewById(R.id.say_publish_button);
		say_publish_button.setBackgroundDrawable(SelecterUtil.setSelector(
				SlidingMenuControlActivity.activity, R.color.main_header_blue,
				R.color.gray, -1, -1));
		// 设置进度条的样式
		listView.getHeaderView().setHeaderProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.loading_data_icon));
		listView.getFooterView().setFooterProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.loading_data_icon));
		listView.setOnItemClickListener(itemClickListener);
		say_publish_button.setOnClickListener(clickListener);
	}

	@Override
	protected void initialized() {
		suggestions = new LinkedList<Suggestion>();
		adapter = new SayListViewAdapter(SlidingMenuControlActivity.activity,
				suggestions);
		listView.setAdapter(adapter);
		mAbTaskQueue = AbTaskQueue.getInstance();
	}

	@Override
	protected void threadTask() {

	}

}
