package com.xinhua.xinhuashe.option.setting;

import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.android.threadpool.IActivity;
import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.option.SlidingMenuControlActivity;
import com.xinhua.xinhuashe.option.address.AddressSelectActivity;
import com.xinhua.xinhuashe.option.login.LogoutActivity;
import com.xinhua.xinhuashe.option.setting.adapter.SettingAdapter;
import com.xinhua.xinhuashe.parentclass.ParentFragment;
import com.xinhua.xinhuashe.service.MobileApplication;
import com.xinhua.xinhuashe.view.dialog.NetAddressDialog;

/**
 * 设置
 */
public class SettingFragment extends ParentFragment implements IActivity {

	private ListView listView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		MobileApplication.allIActivity.add(this);
		View contextView = super.onCreateView(inflater, container,
				savedInstanceState);
		return contextView;
	}

	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapterView, View view,
				int position, long id) {
			switch (position) {
			case 0:
				String pushCount=MobileApplication.cacheUtils.getAsString("pushCount");
				int count = Integer.parseInt(pushCount);
				for (int i = 1; i <= count; i++) {
					MobileApplication.cacheUtils.remove("push"+i);
				}
				MobileApplication.cacheUtils.put("pushCount","0");
				Toast.makeText(getActivity(), "缓存清除成功", Toast.LENGTH_LONG).show();
				
				break;
			case 1:
				Intent intent = new Intent(SlidingMenuControlActivity.activity,
						LogoutActivity.class);
				startActivity(intent);
				break;
			/*case 3:
				Intent intent1 = new Intent(getActivity(), AddressSelectActivity.class);
				getActivity().startActivity(intent1); 
				break;*/
			default:
				break;
			}
		}
	};

	@Override
	protected int getLayoutId() {
		return R.layout.general_listview;
	}

	@Override
	protected void setupViews(View parentView) {
		listView = (ListView) parentView
				.findViewById(R.id.general_listview_ListView);
	}

	@Override
	protected void initialized() {
		String[] data = getResources().getStringArray(R.array.setting_list);
		SettingAdapter adapter = new SettingAdapter(getActivity(), data);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(itemClickListener);
		SlidingMenuControlActivity.main_header_title_TextView.setText("设置");
	}

	@Override
	protected void threadTask() {

	}

	@Override
	public void closeLoadingView() {

	}

	@Override
	public void init() {

	}

	@Override
	public void refresh(int taskId, Object... params) {
		Toast.makeText(SlidingMenuControlActivity.activity, "缓存清除成功",
				Toast.LENGTH_SHORT).show();
	}

}
