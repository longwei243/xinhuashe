package com.xinhua.xinhuashe.option.xinwendongtai;


import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.android.threadpool.IActivity;
import com.android.threadpool.Task;
import com.google.gson.reflect.TypeToken;
import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.domain.Article;
import com.xinhua.xinhuashe.domain.Category;
import com.xinhua.xinhuashe.option.SlidingMenuControlActivity;
import com.xinhua.xinhuashe.option.china.ChinaFragment;
import com.xinhua.xinhuashe.option.news.NewsDetailFragment;
import com.xinhua.xinhuashe.option.xinwendongtai.adapter.XinWenDongTaiListViewAdapter;
import com.xinhua.xinhuashe.option.xinwendongtai.service.ColumnService;
import com.xinhua.xinhuashe.parentclass.ParentFragment;
import com.xinhua.xinhuashe.request.RequestURL;
import com.xinhua.xinhuashe.service.MobileApplication;
import com.xinhua.xinhuashe.service.ParentHandlerService;
import com.xinhua.xinhuashe.service.TaskID;
import com.xinhua.xinhuashe.view.ListViewInScrollView;

/**
 * 新闻动态页面
 * @author LongWei
 *
 */
public class XinWenDongTaiFragment extends ParentFragment implements IActivity{
	
	private static ListViewInScrollView listView;
	private List<Category> categories;
	private List<Article> articles;
	private XinWenDongTaiListViewAdapter adapter;

	private static FragmentManager manager;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobileApplication.allIActivity.add(this);
		try{
			String result = MobileApplication.cacheUtils.getAsString(ColumnService.ColumnInfo_XinWenDongTai);
			categories = ParentHandlerService.gson
					.fromJson(result,
							new TypeToken<LinkedList<Category>>() {
					}.getType());
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		String articles_cache1 = MobileApplication.cacheUtils.getAsString(ColumnService.XinWenDongTai_Column_News1);
		String articles_cache2 = MobileApplication.cacheUtils.getAsString(ColumnService.XinWenDongTai_Column_News2);
		String articles_cache3 = MobileApplication.cacheUtils.getAsString(ColumnService.XinWenDongTai_Column_News3);
		String articles_cache4 = MobileApplication.cacheUtils.getAsString(ColumnService.XinWenDongTai_Column_News4);
		
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
	
			Task task_column_news1 = new Task(TaskID.TASK_COLUNMN_NEWS1,
					RequestURL.getXinWenColumnNews1(), this.getClass().getName(),
					"-新闻动态栏目下的新闻1-");
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
			
			Task task_column_news2 = new Task(TaskID.TASK_COLUNMN_NEWS2,
					RequestURL.getXinWenColumnNews2(), this.getClass().getName(),
					"-新闻动态栏目下的新闻2-");
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
			
			Task task_column_news3 = new Task(TaskID.TASK_COLUNMN_NEWS3,
					RequestURL.getXinWenColumnNews3(), this.getClass().getName(),
					"-新闻动态栏目下的新闻3-");
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
			
			Task task_column_news4 = new Task(TaskID.TASK_COLUNMN_NEWS4,
					RequestURL.getXinWenColumnNews4(), this.getClass().getName(),
					"-新闻动态栏目下的新闻4-");
			MobileApplication.poolManager.addTask(task_column_news4);
			
		}
		
		RequestURL.xianarea = RequestURL.area;
		RequestURL.xianCode = RequestURL.areaCode;

		SlidingMenuControlActivity.main_header_title_TextView.setText("新闻动态");
		SlidingMenuControlActivity.main_header_text.setVisibility(View.INVISIBLE);
		SlidingMenuControlActivity.main_header_spinner_ImageView.setVisibility(View.INVISIBLE);
		
		manager = SlidingMenuControlActivity.activity.getSupportFragmentManager();
		
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
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.firstnews_listview;
	}

	@Override
	protected void setupViews(View parentView) {
		listView = (ListViewInScrollView) parentView.findViewById(R.id.firstnews_listview);
		
		System.out.println("新闻动态下的栏目4条新闻："+categories.size());
		System.out.println("新闻动态下的栏目文章："+articles.size());
		adapter = new XinWenDongTaiListViewAdapter(SlidingMenuControlActivity.activity, manager, categories, articles);
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, final int position,
					long arg3) {
				if(position == 0) {
					ParentFragment fragment = new ChinaFragment();
					switchFragment(fragment, fragment.getClass()
							.getSimpleName(), params);
				}else {
					ParentFragment fragment = new XinWenDongTaiNewsFragment();
					switchFragment(fragment, fragment.getClass()
							.getSimpleName(), categories.get(position).getId()+"");
				}
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
	
	@Override
	public void refresh(int taskId, Object... arg1) {
		
		
		
	}

}
