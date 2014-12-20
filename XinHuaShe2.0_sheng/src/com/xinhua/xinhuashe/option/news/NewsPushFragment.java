package com.xinhua.xinhuashe.option.news;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.task.AbTaskQueue;
import com.ab.view.listener.AbOnListViewListener;
import com.ab.view.pullview.AbPullListView;
import com.google.gson.reflect.TypeToken;
import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.domain.Article;
import com.xinhua.xinhuashe.domain.JsonPageModel;
import com.xinhua.xinhuashe.option.SlidingMenuControlActivity;
import com.xinhua.xinhuashe.option.news.adapter.NewsListViewAdapter;
import com.xinhua.xinhuashe.parentclass.ParentFragment;
import com.xinhua.xinhuashe.request.RequestURL;
import com.xinhua.xinhuashe.service.ParentHandlerService;
import com.xinhua.xinhuashe.util.NetUtils;

/**
 * 我的收藏
 * 
 * @author azuryleaves
 * @since 2014-3-13 上午10:27:22
 * @version 1.0
 * 
 */
public class NewsPushFragment extends ParentFragment {

	private AbPullListView news_item_ListView;
	private AbTaskQueue mAbTaskQueue = null;
	private NewsListViewAdapter adapter;
	private String resultData = "";
	private boolean isLastPage = false;
	private LinkedList<Article> articles;
	private List<Article> newArticles;
	private JsonPageModel<Article> jsonPageModel;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contextView = super.onCreateView(inflater, container,
				savedInstanceState);
		return contextView;
	}

	@Override
	public void onStart() {
		super.onStart();
		final AbTaskItem refreshItem = new AbTaskItem();
		refreshItem.listener = new AbTaskListener() {

			@Override
			public void update() {
				articles.clear();
				if (newArticles != null && newArticles.size() > 0) {
					articles.addAll(newArticles);
					refreshView();
					newArticles.clear();
				}
				news_item_ListView.stopRefresh();
			}

			@Override
			public void get() {
//				System.out.println("---GET请求方式URL---"
//						+ RequestURL.getMyAttention("1"));
				Map<String, Object> resultMap = NetUtils.doHttpGetSetCookie(
						RequestURL.getMyAttention("1"), "UTF-8");
				String result = "";
				if (resultMap != null) {
					result = (String) resultMap.get(NetUtils.Result);
				}
				if (result != null && !"".equals(result)) {
					jsonPageModel = ParentHandlerService.gson.fromJson(result,
							new TypeToken<JsonPageModel<Article>>() {
							}.getType());
					newArticles = jsonPageModel.getContent();
				}
			};
		};

		final AbTaskItem loadMoreItem = new AbTaskItem();
		loadMoreItem.listener = new AbTaskListener() {

			@Override
			public void update() {
				if (newArticles != null && newArticles.size() > 0) {
					articles.addAll(newArticles);
					refreshView();
					newArticles.clear();
				}
				news_item_ListView.stopLoadMore();
			}

			@Override
			public void get() {
				newArticles = new LinkedList<Article>();
				Map<String, Object> resultMap = NetUtils
						.doHttpGetSetCookie(
								RequestURL.getMyAttention(jsonPageModel
										.getNext() + ""), "UTF-8");
				String result = "";
				if (resultMap != null) {
					result = (String) resultMap.get(NetUtils.Result);
				}
				if (result != null && !"".equals(result)) {
					jsonPageModel = ParentHandlerService.gson.fromJson(result,
							new TypeToken<JsonPageModel<Article>>() {
							}.getType());
					newArticles = jsonPageModel.getContent();
					isLastPage = jsonPageModel.isLastPage();
				}
			};
		};

		news_item_ListView.setAbOnListViewListener(new AbOnListViewListener() {

			@Override
			public void onRefresh() {
				mAbTaskQueue.execute(refreshItem);
			}

			@Override
			public void onLoadMore() {
				if (isLastPage) {
					news_item_ListView.stopLoadMore();
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

	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapterView, View view,
				int position, long l) {
			if (--position >= 0) {
				Article article = (Article) adapter.getItem(position);
				ParentFragment fragment = new NewsDetailFragment();
				switchFragment(fragment, fragment.getClass().getSimpleName(),
						article.getId());
			}
		}
	};

	/**
	 * 保存数据（可序列化对象）
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState, this, resultData);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.news_item;
	}

	@Override
	protected void setupViews(View parentView) {
		news_item_ListView = (AbPullListView) parentView
				.findViewById(R.id.news_item_ListView);
		// 设置进度条的样式
		news_item_ListView.getHeaderView().setHeaderProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.loading_data_icon));
		news_item_ListView.getFooterView().setFooterProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.loading_data_icon));

		news_item_ListView.setOnItemClickListener(itemClickListener);
	}

	@Override
	protected void initialized() {
		articles = new LinkedList<Article>();
		adapter = new NewsListViewAdapter(SlidingMenuControlActivity.activity,
				articles);
		news_item_ListView.setAdapter(adapter);
		mAbTaskQueue = AbTaskQueue.getInstance();
		SlidingMenuControlActivity.main_header_title_TextView.setText("消息记录");
	}

	@Override
	protected void threadTask() {

	}

	private void refreshView() {

	}

}
