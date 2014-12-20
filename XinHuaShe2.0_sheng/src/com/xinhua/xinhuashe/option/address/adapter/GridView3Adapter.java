package com.xinhua.xinhuashe.option.address.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xinhuanews.sheng.R;

public class GridView3Adapter extends BaseAdapter{

	LayoutInflater inflater;
	List<String> dataList;
	
	private int clickTemp = -1;
	    //标识选择的Item
	public void setSeclection(int position) {
		clickTemp = position;
	}

	
	public GridView3Adapter(Context context) {
		inflater = LayoutInflater.from(context);
		dataList = new ArrayList<String>();
		dataList.add("山西");
	}
	
	public GridView3Adapter(Context context, List<String> dataList) {
		inflater = LayoutInflater.from(context);
		this.dataList = dataList;
	}
	
	public GridView3Adapter(Context context, String flag) {
		inflater = LayoutInflater.from(context);
		dataList = new ArrayList<String>();
		if("山西".equals(flag)) {
			String[] shanxi_province_item = context.getResources().getStringArray(R.array.shanxi_province_item);
			for (int i = 0; i < shanxi_province_item.length; i++) {
				dataList.add(shanxi_province_item[i]);
			}
		}else if("太原".equals(flag)){
			String[] taiyuan_city_item = context.getResources().getStringArray(R.array.taiyuan_city_item);
			for (int i = 0; i < taiyuan_city_item.length; i++) {
				dataList.add(taiyuan_city_item[i]);
			}
		}else if("大同".equals(flag)) {
			String[] datong_city_item = context.getResources().getStringArray(R.array.datong_city_item);
			for (int i = 0; i < datong_city_item.length; i++) {
				dataList.add(datong_city_item[i]);
			}
		}else if("阳泉".equals(flag)) {
			String[] yangquan_city_item = context.getResources().getStringArray(R.array.yangquan_city_item);
			for (int i = 0; i < yangquan_city_item.length; i++) {
			dataList.add(yangquan_city_item[i]);
			}
		}else if("长治".equals(flag)) {
			String[] changzhi_city_item = context.getResources().getStringArray(R.array.changzhi_city_item);
			for (int i = 0; i < changzhi_city_item.length; i++) {
				dataList.add(changzhi_city_item[i]);
			}
		}else if("晋城".equals(flag)) {
			String[] jincheng_city_item = context.getResources().getStringArray(R.array.jincheng_city_item);
			for (int i = 0; i < jincheng_city_item.length; i++) {
				dataList.add(jincheng_city_item[i]);
			}
		}else if("朔州".equals(flag)) {
			String[] shuozhou_city_item = context.getResources().getStringArray(R.array.shuozhou_city_item);
			for (int i = 0; i < shuozhou_city_item.length; i++) {
				dataList.add(shuozhou_city_item[i]);
			}
		}else if("晋中".equals(flag)) {
			String[] jinzhong_city_item = context.getResources().getStringArray(R.array.jinzhong_city_item);
			for (int i = 0; i < jinzhong_city_item.length; i++) {
				dataList.add(jinzhong_city_item[i]);
			}
		}else if("运城".equals(flag)) {
			String[] yuncheng_city_item = context.getResources().getStringArray(R.array.yuncheng_city_item);
			for (int i = 0; i < yuncheng_city_item.length; i++) {
				dataList.add(yuncheng_city_item[i]);
			}
		}else if("忻州".equals(flag)) {
			String[] xinzhou_city_item = context.getResources().getStringArray(R.array.xinzhou_city_item);
			for (int i = 0; i < xinzhou_city_item.length; i++) {
				dataList.add(xinzhou_city_item[i]);
			}
		}else if("临汾".equals(flag)) {
			String[] linfen_city_item = context.getResources().getStringArray(R.array.linfen_city_item);
			for (int i = 0; i < linfen_city_item.length; i++) {
				dataList.add(linfen_city_item[i]);
			}
		}else if("吕梁".equals(flag)) {
			String[] lvliang_city_item = context.getResources().getStringArray(R.array.lvliang_city_item);
			for (int i = 0; i < lvliang_city_item.length; i++) {
				dataList.add(lvliang_city_item[i]);
			}
		}
	}
	
	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
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
		
		
		holder.gridview3_item_text.setText(dataList.get(position));
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
