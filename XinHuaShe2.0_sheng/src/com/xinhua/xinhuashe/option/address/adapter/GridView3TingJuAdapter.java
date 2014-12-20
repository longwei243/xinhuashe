package com.xinhua.xinhuashe.option.address.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.domain.Category;

public class GridView3TingJuAdapter extends BaseAdapter{
	LayoutInflater inflater;
	List<Category> categories;
	
	private int clickTemp = -1;
	    //标识选择的Item
	public void setSeclection(int position) {
		clickTemp = position;
	}
	public GridView3TingJuAdapter(Context context, List<Category> categories) {
		inflater = LayoutInflater.from(context);
		this.categories = categories;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return categories.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return categories.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View contentView, ViewGroup arg2) {
		ViewHolder holder;
		if(contentView == null) {
			contentView = inflater.inflate(R.layout.gridview3_item, null);
			holder = new ViewHolder();
			holder.gridview3_item_text = (TextView) contentView.findViewById(R.id.gridview3_item_text);
			contentView.setTag(holder);
		}else {
			holder = (ViewHolder) contentView.getTag();
		}
		
		
		holder.gridview3_item_text.setText(categories.get(position).getName());
		if (clickTemp == position) {
			holder.gridview3_item_text.setBackgroundResource(R.drawable.grid3_input_bg);
		} else {
			holder.gridview3_item_text.setBackgroundResource(R.drawable.grid3_button_bg_normal);
		}
		return contentView;
	}

	public class ViewHolder {
		public TextView gridview3_item_text;
	}

}
