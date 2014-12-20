package com.xinhua.xinhuashe.view.slidingmenu.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class SlidingMenuListViewAdapter extends SimpleAdapter {

	private Context context;
	private List<Map<String, Object>> data;
	private int resource;
	private String[] from;
	private int[] to;

	@SuppressWarnings("unchecked")
	public SlidingMenuListViewAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		this.context = context;
		this.data = (List<Map<String, Object>>) data;
		this.resource = resource;
		this.from = from;
		this.to = to;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = LayoutInflater.from(context).inflate(resource, null);
			holder.title = (TextView) convertView.findViewById(to[0]);
			holder.icon = (ImageView) convertView.findViewById(to[1]);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.id = position + "";
		holder.title.setText((String) data.get(position).get(from[0]));
		holder.icon.setImageDrawable((Drawable) data.get(position).get(from[1]));
		return convertView;
	}

	@SuppressWarnings("unused")
	private class Holder {
		public String id;
		public TextView title;
		public ImageView icon;
	}
}
