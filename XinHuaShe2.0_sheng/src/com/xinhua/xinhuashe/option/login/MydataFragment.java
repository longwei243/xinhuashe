package com.xinhua.xinhuashe.option.login;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.threadpool.IActivity;
import com.android.threadpool.Task;
import com.lidroid.xutils.BitmapUtils;
import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.domain.UserInfo;
import com.xinhua.xinhuashe.option.SlidingMenuControlActivity;
import com.xinhua.xinhuashe.parentclass.ParentFragment;
import com.xinhua.xinhuashe.request.RequestURL;
import com.xinhua.xinhuashe.service.MobileApplication;
import com.xinhua.xinhuashe.service.TaskID;
import com.xinhua.xinhuashe.util.UploadUtil;
import com.xinhua.xinhuashe.util.UploadUtil.OnUploadProcessListener;

/**
 * 
 * @author fanxiaole
 *	
 */
public class MydataFragment extends ParentFragment implements IActivity{

	private ProgressDialog progressDialog;
	private static TextView mydata_rezheng_tv,mydata_username_tv,mydata_real_name_tv,
	mydata_phone_tv,mydata_email_tv,mydata_space_tv,mydata_changePassword_tv,
	mydata_change_personaldata_tv,mydata_commit_changedata_tv;
	private static ImageView mydata_portrait_iv;
	private RelativeLayout mydata_15;
	private JSONObject userInfo;
	private String picPath=null;
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
	
	private Handler handler = new Handler(new Handler.Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case UPLOAD_FILE_DONE:
				progressDialog.dismiss();
				// String result = "响应码：" + msg.arg1 + "\n响应信息：" + msg.obj
				// + "\n耗时：" + UploadUtil.getRequestTime() + "秒";
				String message = "";
				String result = "";
				try {
						System.out.println("---------"+msg.obj.toString());
						JSONObject jsonObject = new JSONObject(msg.obj.toString());
//						serverPath = jsonObject.getString("path");
						String resultStr = jsonObject.getString("result");
						message= jsonObject.getString("message");
						Toast.makeText(SlidingMenuControlActivity.activity,message, Toast.LENGTH_SHORT).show();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				result = "结果：" + message + "\n耗时："
						+ UploadUtil.getRequestTime() + "秒";
				System.out.println("---result---" + result);
				break;

			default:
				break;
			}
			return false;
		}
	});
	@Override
	public void onCreate(Bundle savedInstanceState) {
	loadingDialog.show();
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		MobileApplication.allIActivity.add(this);
		threadTask();
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	@Override
	protected int getLayoutId() {
		return R.layout.mydata;
	}

	@Override
	protected void setupViews(View parentView) {
		mydata_username_tv=(TextView) parentView.findViewById(R.id.mydata_username_tv);
		mydata_rezheng_tv=(TextView) parentView.findViewById(R.id.mydata_rezheng_tv);
		mydata_real_name_tv=(TextView) parentView.findViewById(R.id.mydata_real_name_tv);
		mydata_phone_tv=(TextView) parentView.findViewById(R.id.mydata_phone_tv);
		mydata_email_tv=(TextView) parentView.findViewById(R.id.mydata_email_tv);
		mydata_space_tv=(TextView) parentView.findViewById(R.id.mydata_space_tv);
		mydata_changePassword_tv=(TextView) parentView.findViewById(R.id.mydata_changePassword_tv);
		mydata_change_personaldata_tv=(TextView) parentView.findViewById(R.id.mydata_change_personaldata_tv);
		mydata_portrait_iv=(ImageView) parentView.findViewById(R.id.mydata_portrait_iv);
		mydata_15 = (RelativeLayout) parentView.findViewById(R.id.mydata_15);
		mydata_commit_changedata_tv=(TextView) parentView.findViewById(R.id.mydata_commit_changedata_tv);
		mydata_portrait_iv.setOnClickListener(onClickListener);
		mydata_15.setOnClickListener(onClickListener);
		mydata_change_personaldata_tv.setOnClickListener(onClickListener);
		mydata_commit_changedata_tv.setOnClickListener(onClickListener);
		progressDialog = new ProgressDialog(SlidingMenuControlActivity.activity);
	}
	private OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			ParentFragment fragment = null;
			switch (v.getId()) {
			case R.id.mydata_15:
				fragment = new ChangepasswordFragment();
				switchFragment(fragment, this.getClass().getName());
				break;
			case R.id.mydata_change_personaldata_tv:
				fragment = new ChangePersonalDataFragment();
				switchFragment(fragment, this.getClass().getName());
				break;
			case R.id.mydata_commit_changedata_tv:
				fragment  = new ChangeCommitPersonalDataFragment();
				switchFragment(fragment, this.getClass().getName());
				break;
			case R.id.mydata_portrait_iv:
				Intent intent = new Intent(SlidingMenuControlActivity.activity, SelectMydataPicActivity.class);
				MydataFragment.this.startActivityForResult(intent, 1);
				break;
			default:
				break;
			}
			
		}
	};

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==Activity.RESULT_OK){
				picPath = data.getStringExtra(SelectMydataPicActivity.KEY_PHOTO_PATH);
				Log.i(MobileApplication.TAG, "最终选择的图片：" + picPath);
				toUploadFile();
				Bitmap bitmap = BitmapFactory.decodeFile(picPath);
				mydata_portrait_iv.setImageBitmap(bitmap);
		}
		super.onActivityResult(requestCode, resultCode, data);
	};
	@Override
	protected void initialized() {
		SlidingMenuControlActivity.main_header_title_TextView.setText("个人资料");
	}

	@Override
	protected void threadTask() {
		if(!"".equals(UserInfo.userId)){
			Task  taskMydata =  new Task(TaskID.TASK_MYDATA_INFO, 
					RequestURL.getMydataInfo(), 
					this.getClass().getName(), 
					"获取用户个人信息");
			MobileApplication.poolManager.addTask(taskMydata);
		}
	}

	@Override
	public void closeLoadingView() {
		
	}

	@Override
	public  void init() {
		try {
			mydata_username_tv.setText(userInfo.getString("name"));
			mydata_real_name_tv.setText(userInfo.getString("nickName"));
			mydata_phone_tv.setText(userInfo.getString("phone"));
			mydata_email_tv.setText(userInfo.getString("email"));
			
			if(userInfo.getString("authStatus").equals("1")){
				mydata_rezheng_tv.setText("（认证用户）");
			}
			mydata_space_tv.setText(userInfo.getString("adress"));
			
		} catch (JSONException e) {
			
		}try {
			BitmapUtils btu = new BitmapUtils(MobileApplication.mobileApplication);
			btu.display(mydata_portrait_iv, RequestURL.http+userInfo.getString("imagePath"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void refresh(int taskID, Object... params) {
		loadingDialog.cancel();
		if(params!=null){
			if(params[0]!=null){
				try {
					switch (taskID) {
					case TaskID.TASK_MYDATA_INFO:
						userInfo =  new JSONObject(params[0].toString());
						init();
						loadingDialog.cancel();
						break;

					default:
						break;
					}
					
				 } catch (JSONException e) {
					e.printStackTrace();
				 }
			}
			else{
				loadingDialog.showTimeOut();
			}
		}else{
			loadingDialog.showTimeOut();
		}
	}
	private void toUploadFile() {
		// uploadImageResult.setText("正在上传中...");
		System.out.println("正在上传文件...");
		progressDialog.setMessage("正在上传文件...");
		progressDialog.show();
		String fileKey = "img";
		Map<String, String> map = new  HashMap<String, String>();
		map.put("frontUserId", UserInfo.userId);
		UploadUtil uploadUtil = UploadUtil.getInstance();
		uploadUtil.setOnUploadProcessListener(uploadProcessListener);
		uploadUtil.uploadFile(picPath, fileKey, RequestURL.setPersonalPic(), map);
	}
	private OnUploadProcessListener uploadProcessListener = new OnUploadProcessListener() {

		/**
		 * 上传服务器响应回调
		 */
		public void onUploadDone(int responseCode, String message) {
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

}
