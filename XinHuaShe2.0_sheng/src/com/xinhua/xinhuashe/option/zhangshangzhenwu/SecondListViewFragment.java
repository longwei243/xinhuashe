package com.xinhua.xinhuashe.option.zhangshangzhenwu;

import java.util.LinkedList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.android.threadpool.IActivity;
import com.google.gson.reflect.TypeToken;
import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.domain.Article;
import com.xinhua.xinhuashe.domain.Category;
import com.xinhua.xinhuashe.option.SlidingMenuControlActivity;
import com.xinhua.xinhuashe.option.news.NewsDetailFragment;
import com.xinhua.xinhuashe.option.xinwendongtai.adapter.XinWenDongTaiListViewAdapter;
import com.xinhua.xinhuashe.option.xinwendongtai.service.ColumnService;
import com.xinhua.xinhuashe.option.zhangshangzhenwu.adapter.SecondListViewAdapter;
import com.xinhua.xinhuashe.parentclass.ParentFragment;
import com.xinhua.xinhuashe.service.MobileApplication;
import com.xinhua.xinhuashe.service.ParentHandlerService;
import com.xinhua.xinhuashe.view.ListViewInScrollView;
/**
 * 掌上政务第2层页面
 * @author LongWei
 *
 */
public class SecondListViewFragment extends ParentFragment implements IActivity{
	private String columnId;	//-- 栏目ID
	private ListViewInScrollView listView;
	private List<Category> categories = new LinkedList<Category>();
	private List<Article> articles;
	private SecondListViewAdapter adapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobileApplication.allIActivity.add(this);
		Bundle b = getArguments();
		columnId = (String) b.get("columnId");
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
		SlidingMenuControlActivity.main_header_title_TextView.setText("新闻动态");
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
		List<Category> list = null;
		for (int i = 0; i < categories.size(); i++) {
			Category category = categories.get(i);
			if (columnId.equals(category.getId() + "")) {
				list = category.getChildList();
			}
		}
		String articles_cache = MobileApplication.cacheUtils.getAsString(ColumnService.ZhangShangZhengWu_Column_News);
		if(articles_cache != null && !"".equals(articles_cache)) {
			articles = ParentHandlerService.gson.fromJson(articles_cache,new TypeToken<LinkedList<Article>>() {}.getType());
		}else {
			articles = new LinkedList<Article>();
			for (int i = 0; i < list.size(); i++) {
				Article a = new Article();
				a.setId(0L);
				a.setTitle("");
				a.setDescription("");
				a.setHits("");
				a.setImage("assets/img/list_default_icon.png");
				articles.add(a);
			}
		}
		listView = (ListViewInScrollView) parentView.findViewById(R.id.secondnews_listview);
		adapter = new SecondListViewAdapter(getActivity(), this.getFragmentManager(), list, articles);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, final int position,
					long arg3) {
				Article article = articles.get(position);
				ParentFragment fragment = new NewsDetailFragment();
				switchFragment(fragment, fragment.getClass()
						.getSimpleName(), article.getId(), article.getTitle());			}
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
