package com.xinhua.xinhuashe.option.say.adapter;

import java.util.List;

import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.domain.SayReply;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 有话要说回复列表适配器
 *
 * @author azuryleaves
 * @since 2014-4-15 上午11:58:13
 * @version 1.0
 *
 */
public class SayReplyListViewAdapter extends BaseAdapter {

	private List<SayReply> data;
	private LayoutInflater inflater;
	
	public SayReplyListViewAdapter(Context context, List<SayReply> data) {
		super();
		this.inflater = LayoutInflater.from(context);
		this.data = data;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.say_listview_item, null);
			holder = new ViewHolder();
			holder.time = (TextView) convertView
					.findViewById(R.id.say_listview_item_time_TextView);
			holder.content = (TextView) convertView
					.findViewById(R.id.say_listview_item_content_TextView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		SayReply reply = data.get(position);
		holder.time.setText(reply.getCreateDate());
		System.out.println("---reply.getContent()---" + reply.getReContent());
		holder.content.setText(Html.fromHtml(reply.getReContent()));
		return convertView;
	}
	
	public class ViewHolder {
		public TextView time, content;
	}
}
