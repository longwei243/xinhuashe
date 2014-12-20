package com.xinhua.xinhuashe.option.news;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.xinhua.xinhuashe.domain.Article;
import com.xinhua.xinhuashe.option.SlidingMenuControlActivity;
import com.xinhua.xinhuashe.option.news.adapter.PushNewsListViewAdapter;
import com.xinhua.xinhuashe.parentclass.ParentFragment;
import com.xinhua.xinhuashe.service.MobileApplication;
import com.xinhuanews.sheng.R;

public class PushNewsItemFragment extends ParentFragment{

	ListView push_news_item_ListView;
	PushNewsListViewAdapter adapter;
	List<Article> articles = new ArrayList<Article>();
	private int pushCount;
	@Override
	protected int getLayoutId() {
		
		return R.layout.push_news;
	}

	@Override
	protected void setupViews(View parentView) {
		articles.clear();
		if(MobileApplication.cacheUtils.getAsString("pushCount") != null) {
			pushCount = Integer.parseInt(MobileApplication.cacheUtils.getAsString("pushCount"));
			System.out.println("======================"+pushCount);
			if(pushCount != 0){
				for (int i = 1; i <= pushCount; i++) {
					
					String obj = MobileApplication.cacheUtils.getAsString("push"+i);
					try {
						JSONObject j = new JSONObject(obj);
						String newsId = j.getString("newsId");
						System.out.println("newsId是："+newsId);
						String title = j.getString("title");
						System.out.println("title是："+title);
						String content = j.getString("content");
						System.out.println("content是："+content);
						Article a = new Article();
						a.setId(Long.parseLong(newsId));
						a.setTitle(title);
						a.setDescription(content);
						a.setHits("");
						a.setImage("");
						articles.add(a);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				push_news_item_ListView = (ListView) parentView.findViewById(R.id.push_news_item_ListView);
				adapter = new PushNewsListViewAdapter(SlidingMenuControlActivity.activity, articles);
				push_news_item_ListView.setAdapter(adapter);
				push_news_item_ListView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						ParentFragment fragment = new NewsDetailFragment();
						switchFragment(fragment, fragment.getClass()
								.getSimpleName(), articles.get(position).getId(), articles.get(position).getTitle());
					
					}
				});
				adapter.notifyDataSetChanged();
			}
			
		}
		
	}

	@Override
	protected void initialized() {
		
	}

	@Override
	protected void threadTask() {
		// TODO Auto-generated method stub
		
	}

}
