package com.xinhua.xinhuashe.option.staggeredgridview.test;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.view.utils.SelecterUtil;
import com.lidroid.xutils.BitmapUtils;
import com.xinhua.xinhuashe.domain.UserInfo;
import com.xinhua.xinhuashe.option.SlidingMenuControlActivity;
import com.xinhua.xinhuashe.option.say.SayPublishResultFragment;
import com.xinhua.xinhuashe.parentclass.ParentFragment;
import com.xinhua.xinhuashe.request.RequestURL;
import com.xinhua.xinhuashe.service.MobileApplication;
import com.xinhua.xinhuashe.util.UploadUtil;
import com.xinhua.xinhuashe.util.UploadUtil.OnUploadProcessListener;
import com.xinhuanews.sheng.R;

/**
 * @author jiayou
 * @version 2014年7月22日 下午3:44:43 类说明
 */
public class UploadSelectedPicFragment extends ParentFragment implements
		OnClickListener {

	private ImageView display_selectedpic_imageview;
	private TextView display_mylocation;
	private Button sgv_upload_selectedpic;
	private EditText selectedpic_content_EditText;
	private BitmapUtils bitmapUtils;
	private ProgressDialog progressDialog;
	private Map<String, String> map;
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
	private String picPath = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		bitmapUtils = new BitmapUtils(MobileApplication.mobileApplication);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_upload_selectedpic;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void setupViews(View parentView) {
		selectedpic_content_EditText = (EditText) parentView
				.findViewById(R.id.selectedpic_content_EditText);
		display_selectedpic_imageview = (ImageView) parentView
				.findViewById(R.id.display_selectedpic_imageview);
		display_mylocation = (TextView) parentView
				.findViewById(R.id.display_mylocation);
		sgv_upload_selectedpic = (Button) parentView
				.findViewById(R.id.sgv_upload_selectedpic);
		sgv_upload_selectedpic.setBackgroundDrawable(SelecterUtil.setSelector(
				SlidingMenuControlActivity.activity, R.color.main_header_blue,
				R.color.gray, -1, -1));
		sgv_upload_selectedpic.setOnClickListener(this);
		progressDialog = new ProgressDialog(getActivity());
		SlidingMenuControlActivity.main_header_title_TextView.setText("随手拍");
	}

	@Override
	protected void initialized() {
		Map<String, Object> map = (Map) params[0];
		picPath = map.get("picPath").toString();
		bitmapUtils.display(display_selectedpic_imageview, picPath);
		display_mylocation.setText(MobileApplication.desc.replace(" ", ""));

	}

	@Override
	protected void threadTask() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sgv_upload_selectedpic:
			handler.sendEmptyMessage(TO_UPLOAD_FILE);
			break;
		default:
			break;
		}

	}

	private void toUploadFile() {
		// uploadImageResult.setText("正在上传中...");
		System.out.println("正在上传文件...");
		progressDialog.setMessage("正在上传文件...");
		progressDialog.show();
		String fileKey = "img";
		String content = selectedpic_content_EditText.getText().toString();
		String address = MobileApplication.addressDistric;
		Log.i(MobileApplication.TAG, "我草怎么是null-------"+address);
		UploadUtil uploadUtil = UploadUtil.getInstance();
		uploadUtil.setOnUploadProcessListener(uploadProcessListener);
		map = new HashMap<String, String>();
		map.put("frontUserId", UserInfo.userId);
		map.put("address", address);
		map.put("content", content);
		map.put("code", RequestURL.areaCode);
		uploadUtil.uploadFile(picPath, fileKey, RequestURL.getUploadSGVPics(),
				map);
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

	private Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case TO_UPLOAD_FILE:
				toUploadFile();
				break;
			case UPLOAD_FILE_DONE:
				// String result = "响应码：" + msg.arg1 + "\n响应信息：" + msg.obj
				// + "\n耗时：" + UploadUtil.getRequestTime() + "秒";
				String message = "";
				String result = "";
				try {
					JSONObject jsonObject = new JSONObject(msg.obj.toString());
					// serverPath = jsonObject.getString("path");
					String resultStr = jsonObject.getString("result");
					if ("success".equals(resultStr)) {
						ParentFragment fragment = new SayPublishResultFragment();
						switchFragment(fragment, fragment.getClass()
								.getSimpleName());
					} else if ("error".equals(resultStr)) {
						message = jsonObject.getString("message");
						Toast.makeText(SlidingMenuControlActivity.activity, "上传"+message, Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					message = msg.obj.toString();
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
}
