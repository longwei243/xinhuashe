package com.xinhua.xinhuashe.option.say.adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.xinhuanews.sheng.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;

/**
 * 有话要说-目标用户单选适配器
 * 
 * @author azuryleaves
 * @since 2014-4-15 上午10:56:06
 * @version 1.0
 * 
 */
public class SingleSelectAdapter extends BaseAdapter {

	private Activity activity;
	private LayoutInflater inflater;
	private JSONArray jsonArray;
	private int selectItem = -1;

	public SingleSelectAdapter(Activity context, JSONArray jsonArray) {
		super();
		this.activity = context;
		this.inflater = LayoutInflater.from(context);
		this.jsonArray = jsonArray;
	}

	@Override
	public int getCount() {
		return jsonArray.length();
	}

	@Override
	public Object getItem(int position) {
		Object object = null;
		try {
			object = jsonArray.get(position);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.say_target_item, null);
			holder.radioButton = (RadioButton) convertView
					.findViewById(R.id.say_target_item_RadioButton);
			holder.radioButton.setChecked(false);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		try {
			JSONObject jsonObject = jsonArray.getJSONObject(position);
			String name = jsonObject.getString("name");
			holder.radioButton.setText(name);
			holder.radioButton.setTag(R.id.say_target_item_RadioButton,
					jsonObject.getString("id"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		holder.radioButton.setId(position);
		holder.radioButton
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							if (selectItem != -1) {
								RadioButton tempButton = (RadioButton) activity
										.findViewById(selectItem);
								if (tempButton != null) {
									tempButton.setChecked(false);
								}
							}
							selectItem = buttonView.getId();
						}
					}
				});
		if (position == selectItem) {
			holder.radioButton.setChecked(true);
		} else {
			holder.radioButton.setChecked(false);
		}
		return convertView;
	}

	private class ViewHolder {
		public RadioButton radioButton;
	}

	public int getSelectItem() {
		return selectItem;
	}

}
