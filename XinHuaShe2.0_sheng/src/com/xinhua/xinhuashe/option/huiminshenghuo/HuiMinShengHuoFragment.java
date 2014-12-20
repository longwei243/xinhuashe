package com.xinhua.xinhuashe.option.huiminshenghuo;


import java.util.LinkedList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

import com.android.threadpool.IActivity;
import com.android.threadpool.Task;
import com.google.gson.reflect.TypeToken;
import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.domain.Article;
import com.xinhua.xinhuashe.domain.Category;
import com.xinhua.xinhuashe.domain.ViewPagerItemInfo;
import com.xinhua.xinhuashe.option.SlidingMenuControlActivity;
import com.xinhua.xinhuashe.option.address.AddressSelectActivity;
import com.xinhua.xinhuashe.option.address.adapter.AddressToCode;
import com.xinhua.xinhuashe.option.cop.ConvenienceOfPeopleFragment;
import com.xinhua.xinhuashe.option.huiminshenghuo.adapter.HuiMingShengHuoListViewAdapter;
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
 * 惠民生活页面
 * @author LongWei
 *
 */
public class HuiMinShengHuoFragment extends ParentFragment implements IActivity{

	private RelativeLayout fragment_hmsh_layout_sht;
	private static ListViewInScrollView listView;
	private List<Category> categories = new LinkedList<Category>();
	private HuiMingShengHuoListViewAdapter adapter;
	private static FragmentManager manager;
	private List<Article> articles;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobileApplication.allIActivity.add(this);
		try{
			String result = MobileApplication.cacheUtils.getAsString(ColumnService.ColumnInfo_HuiMingShengHuo);
			categories = ParentHandlerService.gson
					.fromJson(result,
							new TypeToken<LinkedList<Category>>() {
					}.getType());
			/*for (int i = 0; i < categories.size(); i++) {
				categories.get(i).setName(categories.get(i).getName().replace("县区", RequestURL.area));
			}*/
//			String article_chche = MobileApplication.cacheUtils.getAsString(ColumnService.HuiMingShengHuo_Column_News);
//			articles = ParentHandlerService.gson.fromJson(article_chche,new TypeToken<LinkedList<Article>>() {}.getType());
//			
//			System.out.println("惠民生活下栏目新闻category："+categories.size() + "articles:"+articles.size());
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		String articles_cache = MobileApplication.cacheUtils.getAsString(ColumnService.HuiMingShengHuo_Column_News);
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
			Task task_column_news_huiming = new Task(TaskID.TASK_COLUNMN_NEWS_HUIMING,
					RequestURL.getHuiMingColumnNews(), this.getClass().getName(),
					"-惠民生活栏目下的新闻-");
			MobileApplication.poolManager.addTask(task_column_news_huiming);

		}
		
		RequestURL.xianarea = RequestURL.area;
		RequestURL.xianCode = RequestURL.areaCode;
		
		SlidingMenuControlActivity.main_header_title_TextView.setText("惠民生活");
		SlidingMenuControlActivity.main_header_text.setVisibility(View.INVISIBLE);
		SlidingMenuControlActivity.main_header_spinner_ImageView.setVisibility(View.INVISIBLE);

		manager = SlidingMenuControlActivity.activity.getSupportFragmentManager();
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
	protected int getLayoutId() {
		return R.layout.fragment_hmsh;
	}

	@Override
	protected void setupViews(View parentView) {
		fragment_hmsh_layout_sht = (RelativeLayout) parentView.findViewById(R.id.fragment_hmsh_layout_sht);
		fragment_hmsh_layout_sht.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ParentFragment fragment_cop = new ConvenienceOfPeopleFragment();
				switchFragment(fragment_cop, fragment_cop.getClass().getSimpleName(), params);
				
			}
		});
		
		listView = (ListViewInScrollView) parentView.findViewById(R.id.fragment_hmsh_listview);
		adapter = new HuiMingShengHuoListViewAdapter(SlidingMenuControlActivity.activity, manager, categories, articles);
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, final int position,
					long arg3) {
				ParentFragment fragment_huiming = new HuiMingShengHuoNewsItemFragment();
				ViewPagerItemInfo pageInfo = new ViewPagerItemInfo(
						categories.get(position).getName(), categories.get(position).getId().toString(),
						position);
				pageInfo.setModule(categories.get(position).getModule());
				switchFragment(fragment_huiming, fragment_huiming.getClass().getSimpleName(), pageInfo);
				
				SlidingMenuControlActivity.main_header_title_TextView.setText(categories.get(position).getName());
				SlidingMenuControlActivity.main_header_text.setVisibility(View.VISIBLE);
				SlidingMenuControlActivity.main_header_text.setText(RequestURL.xianarea);
				SlidingMenuControlActivity.main_header_spinner_ImageView.setVisibility(View.VISIBLE);
				SlidingMenuControlActivity.main_header_spinner_ImageView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(), AddressSelectActivity.class);
						getActivity().startActivity(intent);

					}
				});
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
	public void refresh(int arg0, Object... arg1) {
		
	}
}
