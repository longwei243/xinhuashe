package com.xinhua.xinhuashe.option.homepage.adapter;

import java.io.File;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapCommonUtils;
import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.request.RequestURL;
import com.xinhua.xinhuashe.service.MobileApplication;

public class GalleryAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<Map<String, String>> data;
	private BitmapUtils bitmapUtil;

//	 private BitmapDisplayConfig displayConfig;

	public GalleryAdapter(Context context, List<Map<String, String>> data) {
		this.data = data;
		this.inflater = LayoutInflater.from(context);
		bitmapUtil = new BitmapUtils(context, Environment.getExternalStorageDirectory()
				.getPath() + File.separator + MobileApplication.TAG + File.separator + "cache");
		bitmapUtil.configDiskCacheEnabled(true);
		bitmapUtil.configDefaultLoadingImage(R.drawable.base_detail_default_pic);
		bitmapUtil.configDefaultLoadFailedImage(R.drawable.base_detail_default_pic);
		bitmapUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
		bitmapUtil.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(
				context).scaleDown(1));
//		 displayConfig = new BitmapDisplayConfig();
		// BitmapSize bitmapMaxSize = new BitmapSize(DisplayUtil.dip2px(400,
		// context.getResources().getDisplayMetrics().density),
		// DisplayUtil.dip2px(200, context.getResources()
		// .getDisplayMetrics().density));
		// displayConfig.setBitmapMaxSize(bitmapMaxSize);
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
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater
					.inflate(R.layout.homepage_gallery_item, null);
			holder.imageView = (ImageView) convertView.findViewById(R.id.homepage_gallery_item_ImageView);
			convertView.setTag(holder); 
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String defaultIcon = "assets/img/default_big.png";
		String url = data.get(position).get("image");
		if ("".equals(url)) {
			url = defaultIcon;
		} else {
			if (!url.contains("http://") && !url.contains(defaultIcon)) {
				url = RequestURL.http + url;
			}
		}
		bitmapUtil.display(holder.imageView, url);
		return convertView;
	}

	public class ViewHolder {
		ImageView imageView;
	}

}
