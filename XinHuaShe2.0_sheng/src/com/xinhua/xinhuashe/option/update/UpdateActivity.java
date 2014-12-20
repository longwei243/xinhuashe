package com.xinhua.xinhuashe.option.update;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.file.ApkUtils;
import com.android.net.update.Config;
import com.android.threadpool.IActivity;
import com.android.threadpool.Task;
import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.parentclass.ParentActivity;
import com.xinhua.xinhuashe.request.RequestURL;
import com.xinhua.xinhuashe.service.MobileApplication;
import com.xinhua.xinhuashe.service.ParentHandlerService;
import com.xinhua.xinhuashe.service.TaskID;
import com.xinhua.xinhuashe.util.UpdateUtil;
import com.xinhua.xinhuashe.view.loding.LoadingView;

/**
 * 版本检测
 * 
 */
public class UpdateActivity extends ParentActivity implements IActivity {

	private static LoadingView loadingView;
	private static TextView textView;
	private static ProgressBar progressBar;
	private Thread initLoadingThread;
	private static Button parentdialog_cancel_Button,
			parentdialog_confirm_Button;
	private static JSONObject serverVersionInfo;
	private String filePath = "";

	private static String VersionName = "versionName",
			VersionCode = "versionCode", ApkName = "apkName";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MobileApplication.allActivity.add(this);
		MobileApplication.allIActivity.add(this);
		super.onCreate(savedInstanceState);
		threadTask();
	}

	private void initLoadingImages() {
		int[] imageIds = new int[8];
		imageIds[0] = R.drawable.ic_spinner1;
		imageIds[1] = R.drawable.ic_spinner2;
		imageIds[2] = R.drawable.ic_spinner3;
		imageIds[3] = R.drawable.ic_spinner4;
		imageIds[4] = R.drawable.ic_spinner5;
		imageIds[5] = R.drawable.ic_spinner6;
		imageIds[6] = R.drawable.ic_spinner7;
		imageIds[7] = R.drawable.ic_spinner8;
		loadingView.setImageIds(imageIds);
		initLoadingThread = new Thread() {
			@Override
			public void run() {
				loadingView.startAnim();
			}
		};
		initLoadingThread.start();
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			initLoadingThread.interrupt();
			switch (v.getId()) {
			case R.id.parentdialog_cancel_Button:
				finish();
				break;
			case R.id.parentdialog_confirm_Button:
				switch ((Integer) v.getTag(v.getId())) {
				case UpdateUtil.HasUpdate:
					Map<String, Object> map = new HashMap<String, Object>();
					try {
						map.put(ParentHandlerService.URL, RequestURL.http
								+ serverVersionInfo.getString("path"));
						map.put("apkname", serverVersionInfo.getString(ApkName));
					} catch (JSONException e) {
						e.printStackTrace();
					}
					map.put("handler", handler);
					Task task = new Task(TaskID.TASK_UPDATE_DOWNLOAD, map, this
							.getClass().getName(), "-下载更新文件-");
					MobileApplication.poolManager.addTask(task);
					Message message = handler.obtainMessage();
					message.what = UpdateUtil.ProgessBar_Visible;
					handler.sendMessage(message);
					break;
				case UpdateUtil.DownLoad_Finish:
					ApkUtils.installAPK(UpdateActivity.this, filePath);
					break;
				case UpdateUtil.DownLoad_Error:
					finish();
					break;
				default:
					break;
				}
				break;
			default:
				break;
			}
		}
	};

	private Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case UpdateUtil.HasUpdate:
				loadingView.setVisibility(View.GONE);
				try {
					textView.setText("发现新版本："
							+ serverVersionInfo.getString(VersionName)
							+ "， 确定更新？");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				parentdialog_confirm_Button.setVisibility(View.VISIBLE);
				parentdialog_confirm_Button.setTag(
						R.id.parentdialog_confirm_Button, UpdateUtil.HasUpdate);
				break;
			case UpdateUtil.LatestUpdate:
				loadingView.setVisibility(View.GONE);
				textView.setText(R.string.update_latest);
				break;
			case UpdateUtil.ProgessBar_Visible:
				parentdialog_confirm_Button.setClickable(false);
				progressBar.setVisibility(View.VISIBLE);
				textView.setText(R.string.update_download);
				break;
			case UpdateUtil.ProgessBar_Max:
				parentdialog_confirm_Button.setVisibility(View.GONE);
				progressBar.setMax(msg.arg1);
				break;
			case UpdateUtil.ProgessBar_Progress:
				progressBar.setProgress(msg.arg1);
				break;
			case UpdateUtil.DownLoad_Finish:
				textView.setText(R.string.update_finish);
				filePath = msg.obj.toString();
				progressBar.setVisibility(View.GONE);
				parentdialog_confirm_Button.setClickable(true);
				parentdialog_confirm_Button.setVisibility(View.VISIBLE);
				parentdialog_confirm_Button.setTag(
						R.id.parentdialog_confirm_Button,
						UpdateUtil.DownLoad_Finish);
				break;
			case UpdateUtil.DownLoad_Error:
				textView.setText(R.string.update_download_error);
				parentdialog_cancel_Button.setVisibility(View.GONE);
				parentdialog_confirm_Button.setClickable(true);
				parentdialog_confirm_Button.setVisibility(View.VISIBLE);
				parentdialog_confirm_Button.setTag(
						R.id.parentdialog_confirm_Button,
						UpdateUtil.DownLoad_Error);
				break;
			default:
				break;
			}
			return false;
		}
	});

	@Override
	public void closeLoadingView() {
		System.out.println("---closeLoadingView---");
	}

	@Override
	public void init() {

	}

	@Override
	public void refresh(int taskId, Object... parmas) {
		if (parmas != null) {
			switch (taskId) {
			case TaskID.TASK_UPDATE_CHECK:
				serverVersionInfo = (JSONObject) parmas[0];
				try {
					if (UpdateUtil.hasUpdate(
							serverVersionInfo.getInt(VersionCode),
							Config.getVerCode(
									this,
									getResources().getString(
											R.string.package_name)))) {
						Message message = handler.obtainMessage();
						message.what = UpdateUtil.HasUpdate;
						handler.sendMessage(message);
					} else {
						Message message = handler.obtainMessage();
						message.what = UpdateUtil.LatestUpdate;
						handler.sendMessage(message);
					}
				} catch (JSONException e) {
					loadingView.setVisibility(View.GONE);
					textView.setText(R.string.update_getinfo_error);
					e.printStackTrace();
				}
				break;
			case TaskID.TASK_UPDATE_DOWNLOAD:

				break;
			default:
				break;
			}
		}else{
			loadingView.setVisibility(View.GONE);
			textView.setText("已经是最新版本");
		}
	}

	@Override
	protected int getLayoutId() {
		return R.layout.parentdialog;
	}

	@Override
	protected void setupViews() {
		TextView title = (TextView) findViewById(R.id.parentdialog_title_TextView);
		title.setText(R.string.wait);
		parentdialog_cancel_Button = (Button) findViewById(R.id.parentdialog_cancel_Button);
		parentdialog_cancel_Button.setOnClickListener(clickListener);
		parentdialog_confirm_Button = (Button) findViewById(R.id.parentdialog_confirm_Button);
		parentdialog_confirm_Button.setVisibility(View.GONE);
		parentdialog_confirm_Button.setOnClickListener(clickListener);
		View view = LayoutInflater.from(this).inflate(
				R.layout.update_versioncheck, null);
		FrameLayout parentdialog_content_FrameLayout = (FrameLayout) findViewById(R.id.parentdialog_content_FrameLayout);
		parentdialog_content_FrameLayout.addView(view);
		loadingView = (LoadingView) view
				.findViewById(R.id.update_versioncheck_LoadingView);
		textView = (TextView) view
				.findViewById(R.id.update_versioncheck_TextView);
		progressBar = (ProgressBar) view
				.findViewById(R.id.update_versioncheck_ProgressBar);
		initLoadingImages();
	}

	@Override
	protected void initialized() {

	}

	@Override
	protected void threadTask() {
		Task task = new Task(TaskID.TASK_UPDATE_CHECK,
				RequestURL.getUpdateVersionCheck(), this.getClass().getName(),
				"-版本检测-");
		MobileApplication.poolManager.addTask(task);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN
				&& isOutOfBounds(this, event)) {
			return true;
		}
		return super.onTouchEvent(event);
	}

	private boolean isOutOfBounds(Activity context, MotionEvent event) {
		final int x = (int) event.getX();
		final int y = (int) event.getY();
		final int slop = ViewConfiguration.get(context)
				.getScaledWindowTouchSlop();
		final View decorView = context.getWindow().getDecorView();
		return (x < -slop) || (y < -slop)
				|| (x > (decorView.getWidth() + slop))
				|| (y > (decorView.getHeight() + slop));
	}

}
