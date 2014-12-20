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

public class SlidingMenuGridViewAdapter extends SimpleAdapter {

	private Context context;
	private List<Map<String, Object>> data;
	private int resource;
	private String[] from;
	private int[] to;

	@SuppressWarnings("unchecked")
	public SlidingMenuGridViewAdapter(Context context,
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
			holder.icon = (ImageView) convertView.findViewById(to[0]);
			holder.state = (TextView) convertView.findViewById(to[1]);
			holder.title1 = (TextView) convertView.findViewById(to[2]);
			holder.title2 = (TextView) convertView.findViewById(to[3]);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.icon.setImageDrawable((Drawable) data.get(position).get(from[0]));
		holder.state.setText((String) data.get(position).get(from[1]));
		holder.title1.setText((String) data.get(position).get(from[2]));
		holder.title2.setText((String) data.get(position).get(from[3]));
		return convertView;
	}

	private class Holder {
		public TextView state,title1, title2;
		public ImageView icon;
	}
}
