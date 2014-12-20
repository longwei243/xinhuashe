package com.xinhua.xinhuashe.option.say.adapter;

import java.util.List;

import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.domain.Suggestion;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 有话要说列表适配器
 * 
 * @author azuryleaves
 * @since 2014-4-15 上午11:58:13
 * @version 1.0
 * 
 */
public class SayListViewAdapter extends BaseAdapter {

	private List<Suggestion> data;
	private LayoutInflater inflater;

	public SayListViewAdapter(Context context, List<Suggestion> data) {
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
			holder.sayType = (TextView) convertView
					.findViewById(R.id.say_listview_item_saytype_TextView);
			holder.time = (TextView) convertView
					.findViewById(R.id.say_listview_item_time_TextView);
			holder.content = (TextView) convertView
					.findViewById(R.id.say_listview_item_content_TextView);
			holder.rep = (TextView) convertView
					.findViewById(R.id.say_target_item_rep);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Suggestion suggestion = data.get(position);
		String sayType = "";
		switch (suggestion.getStype()) {// 0--举报 1--建议 2--分享 3--表扬
		case 0:
			sayType = "【举报】";
			break;
		case 1:
			sayType = "【建议】";
			break;
		case 2:
			sayType = "【分享】";
			break;
		case 3:
			sayType = "【表扬】";
			break;
		default:
			break;
		}
		holder.sayType.setText(sayType);
		holder.time.setText(suggestion.getCreateDate());
		holder.content.setText(Html.fromHtml(suggestion.getContent()));
		if ("1".equals(suggestion.getIsReply())) {
			holder.rep.setText("已回复");
		} else {
			holder.rep.setText("未回复");
		}

		return convertView;
	}

	public class ViewHolder {
		public TextView sayType,time, content, rep;
	}
}
