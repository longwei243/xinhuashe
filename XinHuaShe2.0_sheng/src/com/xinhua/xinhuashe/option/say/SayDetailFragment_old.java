package com.xinhua.xinhuashe.option.say;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.task.AbTaskQueue;
import com.ab.view.listener.AbOnListViewListener;
import com.ab.view.pullview.AbPullListView;
import com.google.gson.reflect.TypeToken;
import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.domain.JsonPageModel;
import com.xinhua.xinhuashe.domain.SayReply;
import com.xinhua.xinhuashe.option.SlidingMenuControlActivity;
import com.xinhua.xinhuashe.option.say.adapter.SayReplyListViewAdapter;
import com.xinhua.xinhuashe.parentclass.ParentFragment;
import com.xinhua.xinhuashe.request.RequestURL;
import com.xinhua.xinhuashe.service.ParentHandlerService;
import com.xinhua.xinhuashe.util.NetUtils;

/**
 * 有话要说-回复信息列表
 * 
 * @author azuryleaves
 * @since 2014-4-12 下午1:58:41
 * @version 1.0
 * 
 */
public class SayDetailFragment_old extends ParentFragment {

	private Button say_publish_button;
	private AbPullListView listView;
	private AbTaskQueue mAbTaskQueue = null;
	private boolean isLastPage = false;
	private LinkedList<SayReply> suggestions;
	private List<SayReply> newSuggestions;
	private SayReplyListViewAdapter adapter;
	private JsonPageModel<SayReply> jsonPageModel;
	private String suggestionId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		suggestionId = (String) params[0];
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contextView = super.onCreateView(inflater, container,
				savedInstanceState);
		threadTask();
		return contextView;
	}

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
				}
				listView.stopRefresh();
			}

			@Override
			public void get() {
				System.out.println("---GET请求方式URL---"
						+ RequestURL.getSayReplyList(suggestionId,"1"));
				Map<String, Object> resultMap = NetUtils.doHttpGetSetCookie(
						RequestURL.getSayReplyList(suggestionId, "1"), "UTF-8");
				String result = "";
				if (resultMap != null) {
					result = (String) resultMap.get(NetUtils.Result);
				}
				System.out.println("---GET请求方式返回结果---" + result);
				if (result != null && !"".equals(result)) {
					jsonPageModel = ParentHandlerService.gson.fromJson(result,
							new TypeToken<JsonPageModel<SayReply>>() {
							}.getType());
					newSuggestions = jsonPageModel.getContent();
				}
			};
		};

		final AbTaskItem loadMoreItem = new AbTaskItem();
		loadMoreItem.listener = new AbTaskListener() {

			@Override
			public void update() {
				if (newSuggestions != null && newSuggestions.size() > 0) {
					suggestions.addAll(newSuggestions);
					newSuggestions.clear();
				}
				listView.stopLoadMore();
			}

			@Override
			public void get() {
				newSuggestions = new LinkedList<SayReply>();
				Map<String, Object> resultMap = NetUtils.doHttpGetSetCookie(
						RequestURL.getSayReplyList(suggestionId,
								jsonPageModel.getNext() + ""), "UTF-8");
				String result = "";
				if (resultMap != null) {
					result = (String) resultMap.get(NetUtils.Result);
				}
				if (result != null && !"".equals(result)) {
					jsonPageModel = ParentHandlerService.gson.fromJson(result,
							new TypeToken<JsonPageModel<SayReply>>() {
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
				if (isLastPage) {
					listView.stopLoadMore();
					Toast.makeText(SlidingMenuControlActivity.activity,
							"已经是最后一页了", Toast.LENGTH_SHORT).show();
				} else {
					mAbTaskQueue.execute(loadMoreItem);
				}
			}

		});

		// 第一次下载数据
		mAbTaskQueue.execute(refreshItem);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.say;
	}

	@Override
	protected void setupViews(View parentView) {
		say_publish_button = (Button) parentView
				.findViewById(R.id.say_publish_button);
		say_publish_button.setVisibility(View.GONE);
		listView = (AbPullListView) parentView.findViewById(R.id.say_ListView);
		// 设置进度条的样式
		listView.getHeaderView().setHeaderProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.loading_data_icon));
		listView.getFooterView().setFooterProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.loading_data_icon));
	}

	@Override
	protected void initialized() {
		suggestions = new LinkedList<SayReply>();
		adapter = new SayReplyListViewAdapter(
				SlidingMenuControlActivity.activity, suggestions);
		listView.setAdapter(adapter);
		mAbTaskQueue = AbTaskQueue.getInstance();
	}

	@Override
	protected void threadTask() {

	}

}
