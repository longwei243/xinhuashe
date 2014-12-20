package com.xinhua.xinhuashe.option.xinwendongtai;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.task.AbTaskQueue;
import com.ab.view.listener.AbOnListViewListener;
import com.ab.view.pullview.AbPullListView;
import com.android.threadpool.IActivity;
import com.google.gson.reflect.TypeToken;
import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.domain.Article;
import com.xinhua.xinhuashe.domain.JsonPageModel;
import com.xinhua.xinhuashe.domain.ViewPagerItemInfo;
import com.xinhua.xinhuashe.option.SlidingMenuControlActivity;
import com.xinhua.xinhuashe.option.news.NewsDetailFragment;
import com.xinhua.xinhuashe.option.news.adapter.NewsListViewAdapter;
import com.xinhua.xinhuashe.parentclass.ParentFragment;
import com.xinhua.xinhuashe.request.RequestURL;
import com.xinhua.xinhuashe.service.ParentHandlerService;
import com.xinhua.xinhuashe.util.NetUtils;
/**
 * 厅局资讯新闻列表页
 * @author LongWei
 *
 */
public class TingJuZiXunNewsItemFragment extends ParentFragment implements IActivity{


	private ViewPagerItemInfo pageInfo;
	private AbPullListView news_item_ListView;
	private AbTaskQueue mAbTaskQueue = null;
	private NewsListViewAdapter adapter;
	private String resultData = "";
	private boolean isLastPage = false;
	private LinkedList<Article> articles;
	private List<Article> newArticles;
	private JsonPageModel<Article> jsonPageModel;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pageInfo = (ViewPagerItemInfo) params[0];
//		if (saveData != null) {
//			resultData = (String) saveData[0];
//		}
	}

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
		System.out.println("-------------onstrart()-----------------");
	}

	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapterView, View view,
				int position, long l) {
			if (--position >= 0) { // -- 下拉刷新bug，少了第一条数据
				Article article = (Article) adapter.getItem(position);
				if ("article".equals(pageInfo.getModule())) {
					ParentFragment fragment = new NewsDetailFragment();
					switchFragment(fragment, fragment.getClass()
							.getSimpleName(), article.getId(), article.getTitle());
					
				} else if ("link".equals(pageInfo.getModule())) {
					String url = RequestURL.http + article.getLink();
					System.out.println("---url---" + url);
					Intent intent = new Intent(Intent.ACTION_VIEW);
					String type = "video/*";
					Uri uri = Uri.parse(url);
					intent.setDataAndType(uri, type);
					SlidingMenuControlActivity.activity.startActivity(intent);
				}
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
		adapter.notifyDataSetChanged();
		mAbTaskQueue = AbTaskQueue.getInstance();
	}

	@Override
	protected void threadTask() {

	}

	@Override
	public void closeLoadingView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refresh(int arg0, Object... params) {
		if (params != null) {
			if (params[0] != null) {
				try {
					JSONObject jsonObject = (JSONObject) params[0];
					String result = jsonObject.getString("result");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				loadingDialog.showTimeOut();
			}
		} else {
			loadingDialog.showTimeOut();
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		System.out.println("-----------pause()----------------");
		SlidingMenuControlActivity.main_header_text.setText(RequestURL.tingJu);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		SlidingMenuControlActivity.main_header_text.setText(RequestURL.tingJu);

		System.out.println("-----------onResume()----------------");
		final AbTaskItem refreshItem = new AbTaskItem();
		refreshItem.listener = new AbTaskListener() {

			@Override
			public void update() {
				articles.clear();
				if (newArticles != null && newArticles.size() > 0) {
					articles.addAll(newArticles);
					newArticles.clear();
				}
				news_item_ListView.stopRefresh();
			}

			@Override
			public void get() {
				System.out.println("---GET请求方式URL---"
						+ RequestURL.getTingJuNewsList(pageInfo.getId(), "1"));
				Map<String, Object> resultMap = NetUtils.doHttpGetSetCookie(
						RequestURL.getTingJuNewsList(pageInfo.getId(), "1"), "UTF-8");
				String result = "";
				if (resultMap != null) {
					result = (String) resultMap.get(NetUtils.Result);
				}
				System.out.println("---GET请求方式返回结果---" + result);
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
					newArticles.clear();
				}
				news_item_ListView.stopLoadMore();
			}

			@Override
			public void get() {
				newArticles = new LinkedList<Article>();
				Map<String, Object> resultMap = NetUtils.doHttpGetSetCookie(
						RequestURL.getTingJuNewsList(pageInfo.getId(),
								jsonPageModel.getNext() + ""), "UTF-8");
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
//					Toast.makeText(SlidingMenuControlActivity.activity,
//							"已经是最后一页了", Toast.LENGTH_SHORT).show();
				} else {
					mAbTaskQueue.execute(loadMoreItem);
				}
			}

		});

		// 第一次下载数据
		mAbTaskQueue.execute(refreshItem);
		initialized();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		System.out.println("-----------onStop()----------------");

	}
	
	

}
