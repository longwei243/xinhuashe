package com.xinhua.xinhuashe.option.staggeredgridview.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.threadpool.IActivity;
import com.android.threadpool.Task;
import com.android.view.utils.SelecterUtil;
import com.xinhua.xinhuashe.domain.UserInfo;
import com.xinhua.xinhuashe.option.SlidingMenuControlActivity;
import com.xinhua.xinhuashe.option.login.SelectMydataPicActivity;
import com.xinhua.xinhuashe.option.staggeredgridview.test.adapter.SGVAdapter;
import com.xinhua.xinhuashe.parentclass.ParentFragment;
import com.xinhua.xinhuashe.request.RequestURL;
import com.xinhua.xinhuashe.service.MobileApplication;
import com.xinhua.xinhuashe.service.TaskID;
import com.xinhua.xinhuashe.util.Data;
import com.xinhua.xinhuashe.view.staggeredgridview.StaggeredGridView;
import com.xinhuanews.sheng.R;

public class SGVFragment extends ParentFragment implements IActivity {

	private static StaggeredGridView mGridView;
	private Button sgv_takephoto_button;
	public static final int TARGET = 0, ATTACHMENT = 1;
	public static String picPath = "";
	private ArrayList<Map<String, String>> urlsList;
	private JSONArray result;
	private JSONObject urlsSGVPics;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		MobileApplication.allIActivity.add(this);
		threadTask();
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_sgv;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void setupViews(View parentView) {
		urlsList = new ArrayList<Map<String, String>>();
		mGridView = (StaggeredGridView) parentView.findViewById(R.id.grid_view);

		sgv_takephoto_button = (Button) parentView
				.findViewById(R.id.sgv_takephoto_button);
		sgv_takephoto_button.setBackgroundDrawable(SelecterUtil.setSelector(
				SlidingMenuControlActivity.activity, R.color.main_header_blue,
				R.color.gray, -1, -1));
		sgv_takephoto_button.setOnClickListener(btOnClickListener);

	}

	@SuppressLint("NewApi")
	@Override
	protected void initialized() {
		
	}

	private OnClickListener btOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.sgv_takephoto_button:
				if ("".equals(UserInfo.userId)) {
					Toast.makeText(SlidingMenuControlActivity.activity,
							R.string.nologin, Toast.LENGTH_SHORT).show();
					return;
				}
				Data.SelectPic_Title = "随手拍";
				Intent intent = new Intent(SlidingMenuControlActivity.activity,
						SelectMydataPicActivity.class);
				getParentFragment().startActivityForResult(intent, ATTACHMENT);
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void threadTask() {
		Task task = new Task(TaskID.TASK_SHOW_SGV_PICS,
				RequestURL.getShowSGVPics(),this.getClass().getName(), "获取随手拍展示图片");
		MobileApplication.poolManager.addTask(task);
	}

	@Override
	public void closeLoadingView() {

	}

	@Override
	public void init() {
	}

	@SuppressLint("NewApi")
	@Override
	public void refresh(int taskId, Object... params) {
		if (params != null) {
			if (params[0] != null) {
				switch (taskId) {
				case TaskID.TASK_SHOW_SGV_PICS:
					try {
						urlsList.clear();
						result = new JSONArray(params[0].toString());
						for(int i=0;i<result.length();i++){
							urlsSGVPics=result.getJSONObject(i);
							Map<String,String> map=new HashMap<String, String>();
							map.put("SGVPicsUrl", RequestURL.http
									+ urlsSGVPics.getString("path").toString());
							map.put("SGVPicsDistrict", urlsSGVPics.getString("address").toString());
							urlsList.add(map);
						}
						SGVAdapter adapter = new SGVAdapter(SlidingMenuControlActivity.activity, urlsList);
						mGridView.setAdapter(adapter);
						adapter.notifyDataSetChanged();
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
				default:
					break;
				}
			}
		}
	}

}
