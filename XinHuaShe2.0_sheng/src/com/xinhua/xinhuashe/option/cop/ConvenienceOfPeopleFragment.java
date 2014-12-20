package com.xinhua.xinhuashe.option.cop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.android.threadpool.IActivity;
import com.android.threadpool.Task;
import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.domain.ViewPagerItemInfo;
import com.xinhua.xinhuashe.option.cop.adapter.ConvenienceOfPeopleAdapter;
import com.xinhua.xinhuashe.option.news.BSZNNewsItemFragment;
import com.xinhua.xinhuashe.option.news.NewsFragment;
import com.xinhua.xinhuashe.option.news.NewsItemFragment;
import com.xinhua.xinhuashe.option.news.SecondNewsItemFragment;
import com.xinhua.xinhuashe.parentclass.ParentFragment;
import com.xinhua.xinhuashe.request.RequestURL;
import com.xinhua.xinhuashe.service.MobileApplication;
import com.xinhua.xinhuashe.service.TaskID;

/**
 * 便民工具
 * 
 * @author azuryleaves
 * @since 2014-2-25 上午10:29:27
 * @version 1.0
 * 
 */
public class ConvenienceOfPeopleFragment extends ParentFragment implements IActivity{

	private View cop_ImageView;
	private GridView gridView;
	public static final String BIANMINXINXI_CODE = "" + 55;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		MobileApplication.allIActivity.add(this);
		View contextView = super.onCreateView(inflater, container,
				savedInstanceState);
		threadTask();
		return contextView;
	}

	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View view) {
			ParentFragment fragment = new ConvenienceOfPeopleDetailFragment();
			switchFragment(fragment, fragment.getClass().getSimpleName(), 0);
		}
	};
	
	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapterView, View view,
				int position, long l) {
			ParentFragment fragment;
			switch (position) {
			case 0:
				String id = "55";
				fragment = new COPFragment();
				if (fragment != null) {
					switchFragment(fragment, fragment.getClass().getSimpleName(), id);
				}
				break;
			case 1://56
				fragment = new BSZNNewsItemFragment();
				ViewPagerItemInfo pageInfo = new ViewPagerItemInfo(
						"办事指南", "56", 0);
				pageInfo.setModule("article"); 
				
				if (fragment != null) {
					switchFragment(fragment, fragment.getClass().getSimpleName(), pageInfo);
				}
				break;
			default:
				fragment = new ConvenienceOfPeopleDetailFragment();
				if (fragment != null) {
					switchFragment(fragment, fragment.getClass().getSimpleName(), position);
				}
				break;
			}
		}
	};

	@Override
	protected int getLayoutId() {
		return R.layout.cop;
	}

	@Override
	protected void setupViews(View parentView) {
		cop_ImageView = parentView.findViewById(R.id.cop_ImageView);
		cop_ImageView.setOnClickListener(clickListener);
		gridView = (GridView) parentView.findViewById(R.id.general_gridview_GridView);
		gridView.setNumColumns(3);
	}

	@Override
	protected void initialized() {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		String[] titles = getResources().getStringArray(R.array.convenience_of_people_list);
		for (int i = 0; i < COPData.urls.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("icon", COPData.icons[i]);
			map.put("title", titles[i]);
			data.add(map);
		}
		SimpleAdapter adapter = new ConvenienceOfPeopleAdapter(
				getActivity(), data, R.layout.cop_gridview_item,
				new String[] { "icon", "title" }, new int[] {
						R.id.cop_item_icon_ImageView, R.id.cop_item_TextView });
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(itemClickListener);
	}

	@Override
	protected void threadTask() {
		Task task = new Task(TaskID.TASK_COLUMN_BIANMINXINXI,
				RequestURL.getColumnsList(BIANMINXINXI_CODE), this.getClass().getName(),
				"-获取便民信息下的栏目信息-");
		MobileApplication.poolManager.addTask(task);
	}

	@Override
	public void closeLoadingView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refresh(int arg0, Object... arg1) {
		// TODO Auto-generated method stub
		
	}

}
