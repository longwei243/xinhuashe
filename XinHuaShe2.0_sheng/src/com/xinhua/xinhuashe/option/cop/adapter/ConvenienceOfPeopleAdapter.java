package com.xinhua.xinhuashe.option.cop.adapter;

import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.xinhuanews.sheng.R;

/**
 * 便民工具适配器
 * 
 * @author azuryleaves
 * @since 2014-2-25 下午5:25:04
 * @version 1.0
 * 
 */
public class ConvenienceOfPeopleAdapter extends SimpleAdapter {

	private TypedArray bgs;
	private List<? extends Map<String, ?>> data;
	
	@SuppressLint("Recycle")
	public ConvenienceOfPeopleAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		this.data = data;
		bgs = context.getResources().obtainTypedArray(
				R.array.convenience_of_people_bg_list);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		ImageView icon = (ImageView) view
				.findViewById(R.id.cop_item_icon_ImageView);
		view.setBackgroundColor(bgs.getColor(position % bgs.length(), R.color.cop_bg_1));
		icon.setImageResource((Integer)data.get(position).get("icon"));
		return view;
	}

}
