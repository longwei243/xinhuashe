package com.xinhua.xinhuashe.option.setting.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xinhuanews.sheng.R;

/**
 * 设置适配器
 *
 */
public class SettingAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private String[] data;
	
	public SettingAdapter(Context context, String[] data) {
		super();
		inflater = LayoutInflater.from(context);
		this.data = data;
	}
	
	@Override
	public int getCount() {
		return data.length;
	}

	@Override
	public Object getItem(int position) {
		return data[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = inflater.inflate(R.layout.setting_item, null);
			holder.title = (TextView) convertView.findViewById(R.id.setting_item_title_TextView);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.title.setText(data[position]);
		return convertView;
	}
	
	public class Holder {
		public TextView title;
	}

}
