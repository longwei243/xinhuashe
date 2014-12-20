package com.xinhua.xinhuashe.option.news.adapter;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.core.BitmapSize;
import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.domain.Article;
import com.xinhua.xinhuashe.request.RequestURL;

/**
 * 新闻列表适配器
 * 
 * @author azuryleaves
 * @since 2014-3-13 下午5:23:24
 * @version 1.0
 * 
 */
public class NewsListViewAdapter extends BaseAdapter {

	private List<Article> data;
	private LayoutInflater inflater;
	private BitmapUtils bitmapUtil;

	public NewsListViewAdapter(Context context, List<Article> data) {
		super();
		inflater = LayoutInflater.from(context);
		bitmapUtil = new BitmapUtils(context);
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.news_listview_item, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView
					.findViewById(R.id.news_listview_item_title_TextView);
			holder.content = (TextView) convertView
					.findViewById(R.id.news_listview_item_content_TextView);
			holder.attention = (TextView) convertView
					.findViewById(R.id.news_listview_item_attention_TextView);
			holder.icon = (ImageView) convertView
					.findViewById(R.id.news_listview_item_icon_ImageView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		BitmapDisplayConfig displayConfig = new BitmapDisplayConfig();
		BitmapSize bitmapMaxSize = new BitmapSize(96, 72);
		displayConfig.setBitmapMaxSize(bitmapMaxSize);
		
		Article article = data.get(position);
		holder.title.setText(Html.fromHtml(article.getTitle()));
		holder.content.setText(Html.fromHtml(article.getDescription()));
		holder.attention.setText(article.getHits());
		String defaultIcon = "assets/img/list_default_icon.png";
		String imageUrl = article.getImage();
		if ("".equals(imageUrl)) {
			imageUrl = defaultIcon;
		} else {
			if (!imageUrl.contains("http://")) {
				imageUrl = RequestURL.http + imageUrl;
			}
		}
//		System.out.println("---imageURL---" + imageUrl);
		bitmapUtil.display(holder.icon, imageUrl);
		return convertView;
	}

	public class ViewHolder {
		public TextView title, content, attention;
		public ImageView icon;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}
