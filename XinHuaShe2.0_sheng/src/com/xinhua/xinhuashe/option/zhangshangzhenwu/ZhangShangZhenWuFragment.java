package com.xinhua.xinhuashe.option.zhangshangzhenwu;

import java.util.LinkedList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.android.threadpool.IActivity;
import com.android.threadpool.Task;
import com.google.gson.reflect.TypeToken;
import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.domain.Article;
import com.xinhua.xinhuashe.domain.Category;
import com.xinhua.xinhuashe.domain.UserInfo;
import com.xinhua.xinhuashe.domain.ViewPagerItemInfo;
import com.xinhua.xinhuashe.option.SlidingMenuControlActivity;
import com.xinhua.xinhuashe.option.news.NewsDetailFragment;
import com.xinhua.xinhuashe.option.xinwendongtai.adapter.XinWenDongTaiListViewAdapter;
import com.xinhua.xinhuashe.option.xinwendongtai.service.ColumnService;
import com.xinhua.xinhuashe.option.zhangshangzhenwu.adapter.ZhangShangZhengWuListAdapter;
import com.xinhua.xinhuashe.option.zhangshangzhenwu.adapter.ZhangShangZhengWuListAdapter_New;
import com.xinhua.xinhuashe.parentclass.ParentFragment;
import com.xinhua.xinhuashe.request.RequestURL;
import com.xinhua.xinhuashe.service.MobileApplication;
import com.xinhua.xinhuashe.service.ParentHandlerService;
import com.xinhua.xinhuashe.service.TaskID;
import com.xinhua.xinhuashe.view.ListViewInScrollView;
/**
 * 掌上政务页面
 * @author LongWei
 *
 */
public class ZhangShangZhenWuFragment extends ParentFragment implements IActivity{

	private ListViewInScrollView listView;
	private RelativeLayout fragment_zszw_yuqingkuaibao_layout,
	fragment_zszw_vip, fragment_zszw_qq, fragment_zszw_work, fragment_zszw_news;
	private List<Category> categories = new LinkedList<Category>();
	private ZhangShangZhengWuListAdapter_New adapter;
	private List<Article> articles;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MobileApplication.allIActivity.add(this);
		try{
			String result = MobileApplication.cacheUtils.getAsString(ColumnService.ColumnInfo_ZhangShangZhengWu);
			categories = ParentHandlerService.gson
					.fromJson(result,
							new TypeToken<LinkedList<Category>>() {
					}.getType());
//			String articles_cache = MobileApplication.cacheUtils.getAsString(ColumnService.ZhangShangZhengWu_Column_News);
//			articles = ParentHandlerService.gson.fromJson(articles_cache,new TypeToken<LinkedList<Article>>() {}.getType());

		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		articles = new LinkedList<Article>();
		articles.clear();
		String articles_cache = MobileApplication.cacheUtils.getAsString(ColumnService.ZhangShangZhengWu_Column_News);
		if(articles_cache != null && !"".equals(articles_cache)) {
			articles = ParentHandlerService.gson.fromJson(articles_cache,new TypeToken<LinkedList<Article>>() {}.getType());
		}else {
			articles = new LinkedList<Article>();
			for (int i = 0; i < categories.size(); i++) {
				Article a = new Article();
				a.setId(0L);
				a.setTitle("");
				a.setDescription("");
				a.setHits("");
				a.setImage("assets/img/list_default_icon.png");
				articles.add(a);
			}
			Task task_column_news_zhengwu = new Task(TaskID.TASK_COLUNMN_NEWS_ZHENGWU,
					RequestURL.getZhengWuColumnNews(), this.getClass().getName(),
					"-掌上政务栏目下的新闻-");
			MobileApplication.poolManager.addTask(task_column_news_zhengwu);

		}
		
		SlidingMenuControlActivity.main_header_title_TextView.setText("掌上政务");
		SlidingMenuControlActivity.main_header_text.setVisibility(View.INVISIBLE);
		SlidingMenuControlActivity.main_header_spinner_ImageView.setVisibility(View.INVISIBLE);
		threadTask();
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void closeLoadingView() {
		loadingDialog.cancel();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refresh(int arg0, Object... arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_zszw;
	}

	@Override
	protected void setupViews(View parentView) {
		
		
		listView = (ListViewInScrollView) parentView.findViewById(R.id.fragment_zszw_listview);
		final List<Category> templist = new LinkedList<Category>();
		for (int i = 0; i < categories.size(); i++) {
			templist.add(categories.get(i));
		}
		adapter = new ZhangShangZhengWuListAdapter_New(getActivity(), getFragmentManager(), templist, articles);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, final int position,
					long arg3) {
				if(position == 3) {
					ParentFragment fragment_second = new ZhengWuSecondListViewFragment();
					switchFragment(fragment_second, fragment_second.getClass().getSimpleName(), templist.get(position).getId()+"");
					SlidingMenuControlActivity.main_header_title_TextView.setText(templist.get(position).getName());
				}else {
					ParentFragment fragment = new ZhengWuNewsItemFragment();
					ViewPagerItemInfo pageInfo = new ViewPagerItemInfo(
							templist.get(position).getName(), templist.get(position).getId().toString(),
							position);
					pageInfo.setModule(templist.get(position).getModule());
					switchFragment(fragment, fragment.getClass().getSimpleName(), pageInfo);

				}
			}
		});
	}
	
	public OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId()) {
			
			}
		}
	};

	@Override
	protected void initialized() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void threadTask() {
		Task task_column_news1 = new Task(TaskID.TASK_COLUNMN_NEWS_GF1,
				RequestURL.getGFColumnNews1(), this.getClass().getName(),
				"-官方发布栏目下的新闻1-");
		MobileApplication.poolManager.addTask(task_column_news1);
		
		Task task_column_news2 = new Task(TaskID.TASK_COLUNMN_NEWS_GF2,
				RequestURL.getGFColumnNews2(), this.getClass().getName(),
				"-官方发布栏目下的新闻2-");
		MobileApplication.poolManager.addTask(task_column_news2);
		
		Task task_column_news3 = new Task(TaskID.TASK_COLUNMN_NEWS_GF3,
				RequestURL.getGFColumnNews3(), this.getClass().getName(),
				"-官方发布栏目下的新闻3-");
		MobileApplication.poolManager.addTask(task_column_news3);
		
		Task task_column_news4 = new Task(TaskID.TASK_COLUNMN_NEWS_GF4,
				RequestURL.getGFColumnNews4(), this.getClass().getName(),
				"-官方发布栏目下的新闻4-");
		MobileApplication.poolManager.addTask(task_column_news4);

	}

}
