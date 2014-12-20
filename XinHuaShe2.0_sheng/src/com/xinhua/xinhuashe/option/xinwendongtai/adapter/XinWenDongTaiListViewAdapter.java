package com.xinhua.xinhuashe.option.xinwendongtai.adapter;

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
import com.xinhua.xinhuashe.option.news.NewsFragment;
import com.xinhua.xinhuashe.option.xinwendongtai.XinWenDongTaiNewsFragment;
import com.xinhua.xinhuashe.parentclass.ParentFragment;
import com.xinhua.xinhuashe.request.RequestURL;
/**
 * 新闻动态页面Listview适配器
 * @author LongWei
 *
 */
public class XinWenDongTaiListViewAdapter extends BaseAdapter{

	private List<Article> articles = new ArrayList<Article>();
	private Context context;
	private LayoutInflater inflater;
	private FragmentManager manager;
	List<Category> categories;
	private BitmapUtils bitmapUtil;
	public XinWenDongTaiListViewAdapter(Context context, FragmentManager manager, List<Category> categories, List<Article> articles) {
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
//		RelativeLayout first_news_listview_item_top_layout = (RelativeLayout) view.findViewById(R.id.first_news_listview_item_top_layout);
		
		/*first_news_listview_item_top_layout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ParentFragment fragment_china = new XinWenDongTaiNewsFragment();
					Bundle bundle = new Bundle();
					bundle.putString("columnId", categories.get(position).getId()+"");
					fragment_china.setArguments(bundle);
					manager.beginTransaction()
					.replace(R.id.slidingmenu_control_FrameLayout, fragment_china)
					.addToBackStack(fragment_china.getClass().getSimpleName())
					.commit();
					
				}
		});*/
		
		
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
