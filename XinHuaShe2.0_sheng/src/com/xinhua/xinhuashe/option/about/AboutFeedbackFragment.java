package com.xinhua.xinhuashe.option.about;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.net.update.Config;
import com.android.threadpool.IActivity;
import com.android.threadpool.Task;
import com.android.view.utils.SelecterUtil;
import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.domain.UserInfo;
import com.xinhua.xinhuashe.option.SlidingMenuControlActivity;
import com.xinhua.xinhuashe.parentclass.ParentFragment;
import com.xinhua.xinhuashe.request.RequestURL;
import com.xinhua.xinhuashe.service.MobileApplication;
import com.xinhua.xinhuashe.service.ParentHandlerService;
import com.xinhua.xinhuashe.service.TaskID;
import com.xinhua.xinhuashe.util.UserBehaviorInfo;

public class AboutFeedbackFragment extends ParentFragment implements IActivity {
	private Button about_feedback_button;
	private EditText about_feedback_EditText;
	protected final int FEED_BACK_SEND = 1;
	private Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case FEED_BACK_SEND:
				String feedback = about_feedback_EditText.getText().toString();
				Map<String, String> map = new HashMap<String, String>();
				map.put(ParentHandlerService.URL, RequestURL.getOpition());
				String versionName = Config.getVerName(getActivity(),
						getResources().getString(R.string.package_name));
				map.put("opinion", feedback);
				map.put("versionName", versionName);
				map.put("frontUserId", UserInfo.userId);
				map.put("code", RequestURL.areaCode);
				Task task = new Task(TaskID.FEED_BACK_SEND, map, this
						.getClass().getName(), "意见反馈");
				loadingDialog.show();
				MobileApplication.poolManager.addTask(task);
				Map<String, String> map1 =UserBehaviorInfo.sendUserOpenAppInfo();
				map1.put("operateType", "021");
				map1.put("operateObjID", "");
				map1.put(ParentHandlerService.URL, RequestURL.postUserbehaviorPhoneInfo());
				Task task1 = new Task(TaskID.TASK_POST_USERBEHAVIORINFO, map1, this
						.getClass().getName(), "用户意见反馈行为");
				MobileApplication.poolManager.addTask(task1);
				break;

			default:
				break;
			}
			return false;
		}
	});

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		MobileApplication.allIActivity.add(this);
		View contextView = super.onCreateView(inflater, container,
				savedInstanceState);
		View view = SlidingMenuControlActivity.activity
				.getMain_header_right_ImageView();
		SlidingMenuControlActivity.activity.setHeaderRightView(view);
		return contextView;
	}

	@Override
	protected int getLayoutId() {
		return R.layout.about_feedback;
	}

	@Override
	protected void setupViews(View parentView) {
		about_feedback_button = (Button) parentView
				.findViewById(R.id.about_feedback_button);
		about_feedback_EditText = (EditText) parentView
				.findViewById(R.id.about_feedback_EditText);
		about_feedback_button.setOnClickListener(clickListener);
		about_feedback_button.setBackgroundDrawable(SelecterUtil.setSelector(
				SlidingMenuControlActivity.activity, R.color.main_header_blue,
				R.color.gray, -1, -1));
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.about_feedback_button:
				if(UserInfo.userId.equals("")){
					Toast.makeText(SlidingMenuControlActivity.activity, R.string.nologin, Toast.LENGTH_SHORT).show();
					return;
				}
				String feedback = about_feedback_EditText.getText().toString();
				if ("".equals(feedback)) {
					Toast.makeText(SlidingMenuControlActivity.activity,
							"内容不能为空", Toast.LENGTH_SHORT).show();
					return;
					}
				handler.sendEmptyMessage(FEED_BACK_SEND);
				
				break;

			default:
				break;
			}
		}
	};

	@Override
	protected void initialized() {
		SlidingMenuControlActivity.main_header_title_TextView.setText("意见反馈");
	}
	
	@Override
	protected void threadTask() {

	}

	@Override
	public void closeLoadingView() {
		loadingDialog.cancel();
	}

	@Override
	public void init() {

	}

	@Override
	public void refresh(int TaskId, Object... params) {
		loadingDialog.cancel();
		if (params != null) {
			if (params[0] != null) {
				String message = "";
				try {
					JSONObject jsonObject = new JSONObject(params[0].toString());
					String resultStr = jsonObject.getString("result");
					
					Toast.makeText(SlidingMenuControlActivity.activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
					if ("success".equals(resultStr)) {
						Toast.makeText(SlidingMenuControlActivity.activity, "提交成功",
								Toast.LENGTH_SHORT).show();
						SlidingMenuControlActivity.activity.getSupportFragmentManager().popBackStackImmediate();			
					} else if ("error".equals(resultStr)) {
						Toast.makeText(SlidingMenuControlActivity.activity, "提交失败",
								Toast.LENGTH_SHORT).show();
						message = jsonObject.getString("message");
					}
				} catch (JSONException e) {
					message = params[0].toString();
					e.printStackTrace();
				}
			}
		}

	}

}
