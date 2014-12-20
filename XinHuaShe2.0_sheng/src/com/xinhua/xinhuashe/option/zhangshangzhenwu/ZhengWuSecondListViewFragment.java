package com.xinhua.xinhuashe.option.zhangshangzhenwu;

import java.util.LinkedList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.android.threadpool.IActivity;
import com.android.threadpool.Task;
import com.google.gson.reflect.TypeToken;
import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.domain.Article;
import com.xinhua.xinhuashe.domain.Category;
import com.xinhua.xinhuashe.domain.ViewPagerItemInfo;
import com.xinhua.xinhuashe.option.SlidingMenuControlActivity;
import com.xinhua.xinhuashe.option.news.SecondNewsItemFragment;
import com.xinhua.xinhuashe.option.xinwendongtai.service.ColumnService;
import com.xinhua.xinhuashe.option.zhangshangzhenwu.adapter.SecondListViewAdapter;
import com.xinhua.xinhuashe.option.zhangshangzhenwu.adapter.ZhangShangZhengWuListAdapter_New;
import com.xinhua.xinhuashe.parentclass.ParentFragment;
import com.xinhua.xinhuashe.request.RequestURL;
import com.xinhua.xinhuashe.service.MobileApplication;
import com.xinhua.xinhuashe.service.ParentHandlerService;
import com.xinhua.xinhuashe.service.TaskID;
import com.xinhua.xinhuashe.view.ListViewInScrollView;

public class ZhengWuSecondListViewFragment extends ParentFragment implements IActivity{

	private String columnId;	//-- 栏目ID
	private ListViewInScrollView listView;
	private List<Category> categories = new LinkedList<Category>();
	private List<Article> articles;
	private ZhangShangZhengWuListAdapter_New adapter;
	private List<Category> list;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobileApplication.allIActivity.add(this);
		
		columnId = (String) params[0];
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
		
	}

	@Override
	public void refresh(int arg0, Object... arg1) {
		
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.secondnews_listview;
	}

	@Override
	protected void setupViews(View parentView) {
		
		String articles_cache1 = MobileApplication.cacheUtils.getAsString(ColumnService.GF_Column_News1);
		String articles_cache2 = MobileApplication.cacheUtils.getAsString(ColumnService.GF_Column_News2);
		String articles_cache3 = MobileApplication.cacheUtils.getAsString(ColumnService.GF_Column_News3);
		String articles_cache4 = MobileApplication.cacheUtils.getAsString(ColumnService.GF_Column_News4);
		
		articles = new LinkedList<Article>();
		articles.clear();
		if(articles_cache1 != null && !"".equals(articles_cache1)) {
			System.out.println("又进来这了？");
			List<Article> article1 = ParentHandlerService.gson.fromJson(articles_cache1,new TypeToken<LinkedList<Article>>() {}.getType());
			articles.add(article1.get(0));
		}else {
			Article a = new Article();
			a.setId(0L);
			a.setTitle("");
			a.setDescription("");
			a.setHits("");
			a.setImage("assets/img/list_default_icon.png");
			articles.add(a);
			System.out.println("添加新闻没？");
	
			Task task_column_news1 = new Task(TaskID.TASK_COLUNMN_NEWS_GF1,
					RequestURL.getGFColumnNews1(), this.getClass().getName(),
					"-官方发布栏目下的新闻1-");
			MobileApplication.poolManager.addTask(task_column_news1);
			
		}
		if(articles_cache2 != null && !"".equals(articles_cache2)) {
			System.out.println("又进来这了？");
			List<Article> article2 = ParentHandlerService.gson.fromJson(articles_cache2,new TypeToken<LinkedList<Article>>() {}.getType());
			articles.add(article2.get(0));
		}else {
			Article a = new Article();
			a.setId(0L);
			a.setTitle("");
			a.setDescription("");
			a.setHits("");
			a.setImage("assets/img/list_default_icon.png");
			articles.add(a);
			System.out.println("添加新闻没？");
			
			Task task_column_news2 = new Task(TaskID.TASK_COLUNMN_NEWS_GF2,
					RequestURL.getGFColumnNews2(), this.getClass().getName(),
					"-官方发布栏目下的新闻2-");
			MobileApplication.poolManager.addTask(task_column_news2);
			
		}
		if(articles_cache3 != null && !"".equals(articles_cache3)) {
			System.out.println("又进来这了？");
			List<Article> article3 = ParentHandlerService.gson.fromJson(articles_cache3,new TypeToken<LinkedList<Article>>() {}.getType());
			articles.add(article3.get(0));
		}else {
			Article a = new Article();
			a.setId(0L);
			a.setTitle("");
			a.setDescription("");
			a.setHits("");
			a.setImage("assets/img/list_default_icon.png");
			articles.add(a);
			System.out.println("添加新闻没？");
			
			Task task_column_news3 = new Task(TaskID.TASK_COLUNMN_NEWS_GF3,
					RequestURL.getGFColumnNews3(), this.getClass().getName(),
					"-官方发布栏目下的新闻3-");
			MobileApplication.poolManager.addTask(task_column_news3);
			
		}
		if(articles_cache4 != null && !"".equals(articles_cache4)) {
			System.out.println("又进来这了？");
			List<Article> article4 = ParentHandlerService.gson.fromJson(articles_cache4,new TypeToken<LinkedList<Article>>() {}.getType());
			articles.add(article4.get(0));
		}else {
			Article a = new Article();
			a.setId(0L);
			a.setTitle("");
			a.setDescription("");
			a.setHits("");
			a.setImage("assets/img/list_default_icon.png");
			articles.add(a);
			System.out.println("添加新闻没？");
			
			Task task_column_news4 = new Task(TaskID.TASK_COLUNMN_NEWS_GF4,
					RequestURL.getGFColumnNews4(), this.getClass().getName(),
					"-官方发布栏目下的新闻4-");
			MobileApplication.poolManager.addTask(task_column_news4);
			
		}
		list = null;
		for (int i = 0; i < categories.size(); i++) {
			Category category = categories.get(i);
			if (columnId.equals(category.getId() + "")) {
				list = category.getChildList();
			}
		}
		listView = (ListViewInScrollView) parentView.findViewById(R.id.secondnews_listview);
		adapter = new ZhangShangZhengWuListAdapter_New(getActivity(), this.getFragmentManager(), list, articles);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, final int position,
					long arg3) {
				ParentFragment fragment = new ZhengWuSecondNewsItemFragment();
				ViewPagerItemInfo pageInfo = new ViewPagerItemInfo(
						list.get(position).getName(), list.get(position).getId().toString(),
						position);
				pageInfo.setModule(list.get(position).getModule());
				switchFragment(fragment, fragment.getClass().getSimpleName(), pageInfo);
			}
		});
	}

	@Override
	protected void initialized() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void threadTask() {
		
		
	}



}
