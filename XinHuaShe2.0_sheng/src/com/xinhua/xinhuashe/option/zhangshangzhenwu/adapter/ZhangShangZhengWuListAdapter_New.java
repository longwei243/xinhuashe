package com.xinhua.xinhuashe.option.zhangshangzhenwu.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.core.BitmapSize;
import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.domain.Article;
import com.xinhua.xinhuashe.domain.Category;
import com.xinhua.xinhuashe.option.news.NewsItemFragment;
import com.xinhua.xinhuashe.option.zhangshangzhenwu.SecondListViewFragment;
import com.xinhua.xinhuashe.parentclass.ParentFragment;
import com.xinhua.xinhuashe.request.RequestURL;

public class ZhangShangZhengWuListAdapter_New extends BaseAdapter{



	private List<Article> articles;
	private Context context;
	private LayoutInflater inflater;
	private FragmentManager manager;
	List<Category> categories;
	private BitmapUtils bitmapUtil;
	public ZhangShangZhengWuListAdapter_New(Context context, FragmentManager manager, List<Category> categories, List<Article> articles) {
		super();
		this.categories = categories;
		this.articles = articles;
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.manager = manager;
		bitmapUtil = new BitmapUtils(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return categories.size();
	}

	@Override
	public Object getItem(int location) {
		// TODO Auto-generated method stub
		return categories.get(location);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View arg1, ViewGroup arg2) {
		View view = inflater.inflate(R.layout.xinwen_first_news_listview_item, null);
		
		TextView text = (TextView) view.findViewById(R.id.first_news_listview_item_top_textview);
		text.setText(categories.get(position).getName());
		
		TextView first_news_listview_item_title_TextView = (TextView) view.findViewById(R.id.first_news_listview_item_title_TextView);
		first_news_listview_item_title_TextView.setText(Html.fromHtml(articles.get(position).getTitle()));
		
		BitmapDisplayConfig displayConfig = new BitmapDisplayConfig();
		BitmapSize bitmapMaxSize = new BitmapSize(96, 72);
		displayConfig.setBitmapMaxSize(bitmapMaxSize);
		
		ImageView first_news_listview_item_icon_ImageView = (ImageView) view.findViewById(R.id.first_news_listview_item_icon_ImageView);
		String defaultIcon = "assets/img/list_default_icon.png";
		String imageUrl = articles.get(position).getImage();
		if ("".equals(imageUrl)) {
			imageUrl = defaultIcon;
		} else {
			if (!imageUrl.contains("http://")) {
				imageUrl = RequestURL.http + imageUrl;
			}
		}
		bitmapUtil.display(first_news_listview_item_icon_ImageView, imageUrl);
		
		TextView first_news_listview_item_content_TextView = (TextView) view.findViewById(R.id.first_news_listview_item_content_TextView);
		first_news_listview_item_content_TextView.setText(articles.get(position).getKeywords());
		
		TextView first_news_listview_item_attention_TextView = (TextView) view.findViewById(R.id.first_news_listview_item_attention_TextView);
		first_news_listview_item_attention_TextView.setText(articles.get(position).getHits());
		return view;
	}


	

}
