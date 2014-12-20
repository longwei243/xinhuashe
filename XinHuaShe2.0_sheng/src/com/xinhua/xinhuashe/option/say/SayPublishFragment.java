package com.xinhua.xinhuashe.option.say;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.threadpool.IActivity;
import com.android.threadpool.Task;
import com.android.view.utils.SelecterUtil;
import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.domain.UserInfo;
import com.xinhua.xinhuashe.option.SlidingMenuControlActivity;
import com.xinhua.xinhuashe.option.login.SelectMydataPicActivity;
import com.xinhua.xinhuashe.parentclass.ParentFragment;
import com.xinhua.xinhuashe.request.RequestURL;
import com.xinhua.xinhuashe.service.MobileApplication;
import com.xinhua.xinhuashe.service.ParentHandlerService;
import com.xinhua.xinhuashe.service.TaskID;
import com.xinhua.xinhuashe.util.Data;
import com.xinhua.xinhuashe.util.UploadUtil;
import com.xinhua.xinhuashe.util.UploadUtil.OnUploadProcessListener;
import com.xinhua.xinhuashe.util.UserBehaviorInfo;

/**
 * 有话要说-发表
 * 
 * @author azuryleaves
 * @since 2014-4-12 下午1:58:41
 * @version 1.0
 * 
 */
public class SayPublishFragment extends ParentFragment implements IActivity {

	private LayoutInflater inflater;
	private static RadioGroup type_RadioGroup;
	private int[] radioButtonIds = { R.id.say_type_report_RadioButton,
			R.id.say_type_suggest_RadioButton, R.id.say_type_share_RadioButton,
			R.id.say_type_praise_RadioButton };
	private TextView target_TextView, attachment_select_TextView;
	private EditText content_EditText;
	private Button attachment_select_Button, uploadImage;
	private static final int TARGET = 0, ATTACHMENT = 1;
	private String type = "0";
	public static String picPath = "";
	private ProgressDialog progressDialog;
	private RadioButton radio;
	private Map<String, String> params;
	// 去上传文件
	protected static final int TO_UPLOAD_FILE = 1;
	// 上传文件响应
	protected static final int UPLOAD_FILE_DONE = 2;
	// 选择文件
	public static final int TO_SELECT_PHOTO = 3;
	// 上传初始化
	private static final int UPLOAD_INIT_PROCESS = 4;
	// 上传中
	private static final int UPLOAD_IN_PROCESS = 5;
	//无文件上传
	private static final int TO_UPLOAD_NULL_FILE = 6;
	private String serverPath = "";

	private Handler handler = new Handler(new Handler.Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case TO_UPLOAD_FILE:
				toUploadFile();
				Map<String, String> map =UserBehaviorInfo.sendUserOpenAppInfo();
				map.put("operateType", "019");
				map.put("operateObjID", "");
				map.put(ParentHandlerService.URL, RequestURL.postUserbehaviorPhoneInfo());
				Task task1 = new Task(TaskID.TASK_POST_USERBEHAVIORINFO, map, this
						.getClass().getName(), "用户爆料行为");
				MobileApplication.poolManager.addTask(task1);	
				break;
			case UPLOAD_FILE_DONE:
				// String result = "响应码：" + msg.arg1 + "\n响应信息：" + msg.obj
				// + "\n耗时：" + UploadUtil.getRequestTime() + "秒";
				String message = "";
				String result = "";
				try {
						JSONObject jsonObject = new JSONObject(msg.obj.toString());
//						serverPath = jsonObject.getString("path");
						String resultStr = jsonObject.getString("result");
						if ("success".equals(resultStr)) {
							ParentFragment fragment = new SayPublishResultFragment();
							switchFragment(fragment, fragment.getClass()
									.getSimpleName());
						} else if ("error".equals(resultStr)) {
							
						message = jsonObject.getString("message");
						System.out.println("---serverPath---" + serverPath);
					}
				} catch (JSONException e) {
					message = msg.obj.toString();
					e.printStackTrace();
				}
				result = "结果：" + message + "\n耗时："
						+ UploadUtil.getRequestTime() + "秒";
				System.out.println("---result---" + result);
				break;
			case TO_UPLOAD_NULL_FILE:
				System.out.println("-----------------TO_UPLOAD_NULL_FILE");
				String target = target_TextView.getTag(R.id.say_target_TextView)
						.toString();
				String content = content_EditText.getText().toString();
				params = new HashMap<String, String>();
				params.put("frontUserId", UserInfo.userId);
				params.put("stype", type);
				params.put("content", content);
				params.put("goalUserId", target);
				params.put("code", RequestURL.areaCode);
				params.put("url", RequestURL.getSayAdd());
				
				Task task = new Task(TaskID.TASK_SAY_COMMENT_NULL_FILE,params,this.getClass().getName(),"无文件");
				MobileApplication.poolManager.addTask(task);
				
				Map<String, String> map1 =UserBehaviorInfo.sendUserOpenAppInfo();
				map1.put("operateType", "019");
				map1.put("operateObjID", "");
				map1.put(ParentHandlerService.URL, RequestURL.postUserbehaviorPhoneInfo());
				Task task2 = new Task(TaskID.TASK_POST_USERBEHAVIORINFO, map1, this
						.getClass().getName(), "用户爆料行为");
				MobileApplication.poolManager.addTask(task2);		
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
		this.inflater = inflater;
		MobileApplication.allIActivity.add(this);
		SlidingMenuControlActivity.main_header_title_TextView.setText("我要说");
		View contextView = super.onCreateView(inflater, container,
				savedInstanceState);
		View view = SlidingMenuControlActivity.activity
				.getMain_header_right_ImageView();
		SlidingMenuControlActivity.activity.setHeaderRightView(view);
		threadTask();
		return contextView;
	}

	private OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkId) {
			switch (checkId) {
			case R.id.say_type_report_RadioButton:
				type = "0";
				break;
			case R.id.say_type_suggest_RadioButton:
				type = "1";
				break;
			case R.id.say_type_share_RadioButton:
				type = "2";
				break;
			case R.id.say_type_praise_RadioButton:
				type = "3";
				break;
			default:
				break;
			}
		}
	};

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			Intent intent = null;
			switch (view.getId()) {
			case R.id.say_target_TextView:
				intent = new Intent(SlidingMenuControlActivity.activity,
						SayTargetActivity.class);
				SayPublishFragment.this.startActivityForResult(intent, TARGET);
				break;
			case R.id.say_attachment_select_Button:
				Data.SelectPic_Title="我要说";
				intent = new Intent(SlidingMenuControlActivity.activity,
						SelectMydataPicActivity.class);
				SayPublishFragment.this.startActivityForResult(intent,
						ATTACHMENT);
				
				break;
			case R.id.uploadImage:
				String target = target_TextView.getTag(R.id.say_target_TextView)
				.toString();
				if ("".equals(target)) {
					Toast.makeText(SlidingMenuControlActivity.activity, "请选择对谁说", Toast.LENGTH_SHORT).show();
					return;
				}
				String content = content_EditText.getText().toString();
				if ("".equals(content)) {
					Toast.makeText(SlidingMenuControlActivity.activity,
							R.string.say_content_error, Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (picPath != null&& !picPath.equals("")) {
					handler.sendEmptyMessage(TO_UPLOAD_FILE);
				} else {
					/*Toast.makeText(getActivity(), "上传的文件路径出错",
							Toast.LENGTH_LONG).show();*/
					handler.sendEmptyMessage(TO_UPLOAD_NULL_FILE);
				}
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == TARGET) {
				String targetId = data
						.getStringExtra(SayTargetActivity.KEY_TARGET_ID);
				String targetName = data
						.getStringExtra(SayTargetActivity.KEY_TARGET_NAME);
				target_TextView.setText(targetName);
				target_TextView.setTag(R.id.say_target_TextView, targetId);
			} else if (requestCode == ATTACHMENT) {
				picPath = data.getStringExtra(SelectMydataPicActivity.KEY_PHOTO_PATH);
				Log.i(MobileApplication.TAG, "最终选择的图片：" + picPath);
				attachment_select_TextView.setText("图片选择成功");
				attachment_select_Button.setText("选择图片成功");
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.say_publish;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void setupViews(View parentView) {
		type_RadioGroup = (RadioGroup) parentView
				.findViewById(R.id.say_type_RadioGroup);
		type_RadioGroup.setOnCheckedChangeListener(checkedChangeListener);
		target_TextView = (TextView) parentView
				.findViewById(R.id.say_target_TextView);
		target_TextView.setTag(R.id.say_target_TextView, "");
		content_EditText = (EditText) parentView
				.findViewById(R.id.say_content_EditText);
		attachment_select_TextView = (TextView) parentView
				.findViewById(R.id.say_attachment_select_TextView);
		attachment_select_Button = (Button) parentView
				.findViewById(R.id.say_attachment_select_Button);
		uploadImage = (Button) parentView.findViewById(R.id.uploadImage);
		uploadImage.setBackgroundDrawable(SelecterUtil.setSelector(
				SlidingMenuControlActivity.activity, R.color.main_header_blue,
				R.color.gray, -1, -1));
		attachment_select_Button.setBackgroundDrawable(SelecterUtil
				.setSelector(SlidingMenuControlActivity.activity,
						R.color.main_header_blue, R.color.gray, -1, -1));
		uploadImage.setOnClickListener(clickListener);
		target_TextView.setOnClickListener(clickListener);
		attachment_select_Button.setOnClickListener(clickListener);
		progressDialog = new ProgressDialog(getActivity());
	}

	@Override
	protected void initialized() {

	}

	@Override
	protected void threadTask() {
		Task task = new Task(TaskID.TASK_SAY_TYPE, RequestURL.getSayType(),
				this.getClass().getName(), "-获取类型-");
		MobileApplication.poolManager.addTask(task);
	}

	@Override
	public void closeLoadingView() {
		loadingDialog.cancel();
	}

	@Override
	public void init() {

	}

	private void toUploadFile() {
		// uploadImageResult.setText("正在上传中...");
		System.out.println("正在上传文件...");
		progressDialog.setMessage("正在上传文件...");
		progressDialog.show();
		String fileKey = "img";
		String target = target_TextView.getTag(R.id.say_target_TextView)
				.toString();
		String content = content_EditText.getText().toString();
		UploadUtil uploadUtil = UploadUtil.getInstance();
		uploadUtil.setOnUploadProcessListener(uploadProcessListener);
		params = new HashMap<String, String>();
		params.put("frontUserId", UserInfo.userId);
		params.put("stype", type);
		params.put("content", content);
		params.put("goalUserId", target);
		params.put("code", RequestURL.areaCode);
		uploadUtil.uploadFile(picPath, fileKey, RequestURL.getSayAdd(), params);
	}

	private OnUploadProcessListener uploadProcessListener = new OnUploadProcessListener() {

		/**
		 * 上传服务器响应回调
		 */
		public void onUploadDone(int responseCode, String message) {
			progressDialog.dismiss();
			Message msg = Message.obtain();
			msg.what = UPLOAD_FILE_DONE;
			msg.arg1 = responseCode;
			msg.obj = message;
			handler.sendMessage(msg);
		}

		public void onUploadProcess(int uploadSize) {
			Message msg = Message.obtain();
			msg.what = UPLOAD_IN_PROCESS;
			msg.arg1 = uploadSize;
			handler.sendMessage(msg);
		}

		public void initUpload(int fileSize) {
			Message msg = Message.obtain();
			msg.what = UPLOAD_INIT_PROCESS;
			msg.arg1 = fileSize;
			handler.sendMessage(msg);
		}
	};

	@Override
	public void refresh(int taskId, Object... params) {
		loadingDialog.cancel();
		for (int i = 0; i < params.length; i++) {
			System.out.println("params[" + i + "]-----" + params[i]);
		}
		if (params != null) {
			if (params[0] != null) {
				
			switch (taskId) {
			case TaskID.TASK_SAY_TYPE:
				addRadioButton(params);
				break;
			case TaskID.TASK_SAY_COMMENT_NULL_FILE:
				String message = "";
				try {
						JSONObject jsonObject = new JSONObject(params[0].toString());
						String resultStr = jsonObject.getString("result");
						if ("success".equals(resultStr)) {
							ParentFragment fragment = new SayPublishResultFragment();
							switchFragment(fragment, fragment.getClass()
									.getSimpleName());
						} else if ("error".equals(resultStr)) {
							
						message = jsonObject.getString("message");
					}
				} catch (JSONException e) {
					message = params[0].toString();
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
			}
			
		}
	}

	/**
	 * 自动添加type radiobutton
	 */

	private void addRadioButton(Object... params) {
		try {
			JSONArray jsonarray = new JSONArray(params[0].toString());
			JSONObject jsonob = new JSONObject();
			for (int i = 0; i < jsonarray.length(); i++) {
				jsonob = (JSONObject) jsonarray.get(i);
				radio = (RadioButton) inflater.inflate(
						R.layout.say_publish_item, null);
				radio.setId(radioButtonIds[i % radioButtonIds.length]);
				String label = jsonob.getString("label");
				radio.setText(label);
				System.out.println("-----------" + jsonob.getString("label"));
				type_RadioGroup.addView(radio);
				((RadioButton) type_RadioGroup.getChildAt(0)).setChecked(true);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
