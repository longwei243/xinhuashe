package com.xinhua.xinhuashe.option.say;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.android.threadpool.IActivity;
import com.android.threadpool.Task;
import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.option.say.adapter.SingleSelectAdapter;
import com.xinhua.xinhuashe.parentclass.ParentActivity;
import com.xinhua.xinhuashe.request.RequestURL;
import com.xinhua.xinhuashe.service.MobileApplication;
import com.xinhua.xinhuashe.service.TaskID;

/**
 * 有话要说-目标用户
 *
 * @author azuryleaves
 * @since 2014-4-15 上午10:36:41
 * @version 1.0
 *
 */
public class SayTargetActivity extends ParentActivity implements IActivity {

	public static final String KEY_TARGET_NAME = "target_name", KEY_TARGET_ID = "target_id";
	private static View say_target_loading_TextView;
	private static ListView listView;
	private static JSONArray jsonArray;
	private SingleSelectAdapter adapter;
	private Button parentdialog_cancel_Button, parentdialog_confirm_Button;
	private Intent lastIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MobileApplication.allActivity.add(this);
		MobileApplication.allIActivity.add(this);
		super.onCreate(savedInstanceState);
		lastIntent = getIntent();
		threadTask();
	}
	
	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.parentdialog_cancel_Button:
				finish();
				break;
			case R.id.parentdialog_confirm_Button:
				JSONObject jsonObject;
				try {
					jsonObject = jsonArray.getJSONObject(adapter.getSelectItem());
					lastIntent.putExtra(KEY_TARGET_NAME, jsonObject.getString("name"));
					lastIntent.putExtra(KEY_TARGET_ID, jsonObject.getString("id"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				setResult(Activity.RESULT_OK, lastIntent);
				finish();
				break;
			case R.id.loadingdialog_cancel_ImageView:
				loadingDialog.cancel();
				break;
			default:
				break;
			}
		}
	};
	
	@Override
	protected int getLayoutId() {
		return R.layout.say_target;
	}

	@Override
	protected void setupViews() {
		say_target_loading_TextView = findViewById(R.id.say_target_loading_TextView);
		listView = (ListView) findViewById(R.id.general_listview_ListView);
		listView.setVisibility(View.GONE);
		parentdialog_cancel_Button = (Button) findViewById(R.id.parentdialog_cancel_Button);
		parentdialog_cancel_Button.setOnClickListener(clickListener);
		parentdialog_confirm_Button = (Button) findViewById(R.id.parentdialog_confirm_Button);
		parentdialog_confirm_Button.setOnClickListener(clickListener);
	}

	@Override
	protected void initialized() {
		jsonArray = new JSONArray();
		adapter = new SingleSelectAdapter(this, jsonArray);
		listView.setAdapter(adapter);
	}

	@Override
	protected void threadTask() {
		Task task = new Task(TaskID.TASK_SAY_TARGET, RequestURL.getSayTarget(),
				this.getClass().getName(), "-获取发送目标用户-");
		MobileApplication.poolManager.addTask(task);
	}
	
	@Override
	public void closeLoadingView() {
		
	}

	@Override
	public void init() {
		
	}

	@Override
	public void refresh(int taskId, Object... params) {
		if (params != null) {
			if (params[0] != null) {
				try {
					JSONArray newJSONArray = new JSONArray(params[0].toString());
					for (int i = 0; i < newJSONArray.length(); i++) {
						jsonArray.put(i, newJSONArray.get(i));
					}
					Log.i(MobileApplication.TAG, params[0].toString());
					adapter.notifyDataSetChanged();
					say_target_loading_TextView.setVisibility(View.GONE);
					listView.setVisibility(View.VISIBLE);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				loadingDialog.showTimeOut();
			}
		} else {
			loadingDialog.showTimeOut();
		}
	}

}
