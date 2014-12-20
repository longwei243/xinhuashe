package com.xinhua.xinhuashe.option.loding;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.Toast;

import com.android.net.update.Config;
import com.android.threadpool.IActivity;
import com.android.threadpool.Task;
import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.domain.UserInfo;
import com.xinhua.xinhuashe.option.SlidingMenuControlActivity;
import com.xinhua.xinhuashe.option.guide.GuideActivity;
import com.xinhua.xinhuashe.parentclass.ParentActivity;
import com.xinhua.xinhuashe.request.RequestURL;
import com.xinhua.xinhuashe.service.MobileApplication;
import com.xinhua.xinhuashe.service.ParentHandlerService;
import com.xinhua.xinhuashe.service.TaskID;
import com.xinhua.xinhuashe.util.DeviceUtils;
import com.xinhua.xinhuashe.util.NetWorkHelper;
import com.xinhua.xinhuashe.util.TelManager;
import com.xinhua.xinhuashe.util.UserBehaviorInfo;

public class LoadingActivity extends ParentActivity implements IActivity {

	private SharedPreferences preferences;
	private boolean isFinish = false;
	private String state ,gps,wifi;
	public static final String XINWEN_CODE = "" + 2;
	public static final String ZHENGWU_CODE = "" + 3;
	public static final String HUIMING_CODE = "" + 4;
	public static final String HUDONG_CODE = "" + 5;
	public static final String YUQING_CODE = "" + 26;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		MobileApplication.allActivity.add(this);
		MobileApplication.allIActivity.add(this);
		super.onCreate(savedInstanceState);
		preferences = getSharedPreferences(MobileApplication.TAG, MODE_PRIVATE);
		
		if(!NetWorkHelper.checkNetState(LoadingActivity.this)) {
			Toast.makeText(LoadingActivity.this, "当前没有网络连接，请设置网络后使用", Toast.LENGTH_LONG).show();
			this.finish();
			return;
		}
		
		threadTask();
		intentToMain();
	}

	private void intentToMain() {
		if (!isFinish) {
			System.out.println("---intentToMain---");
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					Intent mainIntent = new Intent(LoadingActivity.this,
							SlidingMenuControlActivity.class);
					if (preferences.getBoolean("isReinstall", true)) {
						phoneInfo();
						firstSendUserBehavior();
						mainIntent = new Intent(LoadingActivity.this,
								GuideActivity.class);
					}
					mainIntent.putExtras(getIntent());
					startActivityForResult(mainIntent, 10);
					overridePendingTransition(R.anim.anim_fromright_toup6,
							R.anim.anim_down_toleft6);
					finish();
				}
			}, 2000);
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_DOWN
					&& event.getRepeatCount() == 0) {
				isFinish = true;
				finish();
				return true;
			} else {
				return false;
			}
		} else {
			return super.dispatchKeyEvent(event);
		}
	}

	@Override
	protected int getLayoutId() {
		return R.layout.loading;
	}

	@Override
	protected void setupViews() {

	}

	@Override
	protected void initialized() {
		
	}

	@Override
	public void closeLoadingView() {

	}

	@Override
	public void init() {

	}

	@Override
	public void refresh(int taskId, Object... params) {
		switch(taskId) {
		case TaskID.TASK_COLUMN_XINWENDONGTAI:
			if(params[0] != null) {
//				intentToMain();
			}
		}
		
		
		
	}

	@Override
	protected void threadTask() {
		
		String id = MobileApplication.cacheUtils.getAsString("id");
		String username = MobileApplication.cacheUtils.getAsString("username");
		String groupIdss = MobileApplication.cacheUtils.getAsString("groupIdss");
		if (id == null) {
			id = "";
			System.out.println("---用户ID为空---");
		}
		if (username == null) {
			username = "";
			System.out.println("---用户名为空---");
		}
		UserInfo.userId = id;
		UserInfo.userName = username;
		UserInfo.groupIdss = groupIdss;
		
		Task task = new Task(TaskID.TASK_COLUMN_XINWENDONGTAI,
				RequestURL.getColumnsList(XINWEN_CODE), this.getClass().getName(),
				"-获取新闻动态下的栏目信息-");
		MobileApplication.poolManager.addTask(task);
		
		Task task_huiming = new Task(TaskID.TASK_COLUMN_HUIMINGSHENGHUO,
				RequestURL.getHuiMingColumnsList(), this.getClass().getName(),
				"-获取惠民生活下的栏目信息-");
		MobileApplication.poolManager.addTask(task_huiming);
		
		Task task_zhengwu = new Task(TaskID.TASK_COLUMN_ZHANGSHANGZHENGWU,
				RequestURL.getColumnsList(ZHENGWU_CODE), this.getClass().getName(),
				"-获取掌上政务下的栏目信息-");
		MobileApplication.poolManager.addTask(task_zhengwu);
		Task task_hudong = new Task(TaskID.TASK_COLUMN_HUDONGFENXIANG,
				RequestURL.getColumnsList(HUDONG_CODE), this.getClass().getName(),
				"-互动分享下的栏目信息-");
		MobileApplication.poolManager.addTask(task_hudong);
//			Task task_tingju = new Task(TaskID.TASK_COLUMN_TINGJU,
//				RequestURL.getTingJu(), this.getClass().getName(),
//				"-获取厅局下的栏目信息-");
//		MobileApplication.poolManager.addTask(task_tingju);
//		
//		Task task_yuqing = new Task(TaskID.TASK_COLUMN_YUQING,
//				RequestURL.getColumnsList(YUQING_CODE), this.getClass().getName(),
//				"-获取舆情快报下的栏目信息-");
//		MobileApplication.poolManager.addTask(task_yuqing);


		Task task_column_news1 = new Task(TaskID.TASK_COLUNMN_NEWS1,
				RequestURL.getXinWenColumnNews1(), this.getClass().getName(),
				"-新闻动态栏目下的新闻1-");
		MobileApplication.poolManager.addTask(task_column_news1);
		
		Task task_column_news2 = new Task(TaskID.TASK_COLUNMN_NEWS2,
				RequestURL.getXinWenColumnNews2(), this.getClass().getName(),
				"-新闻动态栏目下的新闻2-");
		MobileApplication.poolManager.addTask(task_column_news2);
		
		Task task_column_news3 = new Task(TaskID.TASK_COLUNMN_NEWS3,
				RequestURL.getXinWenColumnNews3(), this.getClass().getName(),
				"-新闻动态栏目下的新闻3-");
		MobileApplication.poolManager.addTask(task_column_news3);
		
		Task task_column_news4 = new Task(TaskID.TASK_COLUNMN_NEWS4,
				RequestURL.getXinWenColumnNews4(), this.getClass().getName(),
				"-新闻动态栏目下的新闻4-");
		MobileApplication.poolManager.addTask(task_column_news4);

		Task task_column_news_huiming = new Task(TaskID.TASK_COLUNMN_NEWS_HUIMING,
				RequestURL.getHuiMingColumnNews(), this.getClass().getName(),
				"-惠民生活栏目下的新闻-");
		MobileApplication.poolManager.addTask(task_column_news_huiming);
		
		Task task_column_news_zhengwu = new Task(TaskID.TASK_COLUNMN_NEWS_ZHENGWU,
				RequestURL.getZhengWuColumnNews(), this.getClass().getName(),
				"-掌上政务栏目下的新闻-");
		MobileApplication.poolManager.addTask(task_column_news_zhengwu);
	
	}
	private void phoneInfo() {
		/*
		 * post方式上传手机信息
		 */
		
			double[] db = MobileApplication.db;
			TelManager telmanager = new TelManager(MobileApplication.mobileApplication);
			DeviceUtils deviceUtils = new DeviceUtils();
			Map<String, String> map = new HashMap<String, String>();
			map.put(ParentHandlerService.URL, RequestURL.getPhoneInfo());
			map.put("code", RequestURL.areaCode);
			map.put("width", deviceUtils.getScreenWidth(LoadingActivity.this)+"");
			map.put("height", deviceUtils.getScreenHeight(LoadingActivity.this)+"");
			if (DeviceUtils.isExternalStorageWriteable()==true) {
				 state ="0";
			} else {
				state = "1";
			}
			map.put("writableyes", state);
			if (DeviceUtils.isGpsEnabled(MobileApplication.mobileApplication)==true) {
				gps="0";
			}else{
				gps="1";
			}
			map.put("gps", gps);
			if (DeviceUtils.isWifiEnabled(MobileApplication.mobileApplication)==true) {
				wifi="0";
			} else {
				wifi="1";
			}
			map.put("wifi", wifi);
			map.put("sim", telmanager.getSIMState());
			map.put("name",telmanager.getSIMOperatorName() );
			map.put("number", telmanager.getSIMOperatorCode());
			map.put("iccid",telmanager.getSimSerialNumber());
			map.put("headingCode", telmanager.getIMSI());
			map.put("equipmentId", telmanager.getDeviceId());
			map.put("OSVersion", deviceUtils.getDeviceVersionNumber());
			map.put("APIVersion",String.valueOf(deviceUtils.getApi()));
			map.put("phoneBand", deviceUtils.getDeviceModels());
			map.put("phoneModel", deviceUtils.getBrand());
			map.put("versionNumber", Config.getVerName(MobileApplication.mobileApplication, getResources()
					.getString(R.string.package_name)));
			map.put("versionName", getString(R.string.package_name));
			map.put("serialNumber", deviceUtils.getserial());
			map.put("networkType",String.valueOf(telmanager.getNetworkType()));
			map.put("IP", telmanager.getLocalIpAddressV4());
			map.put("mobileOS", "02");
			map.put("appStoreID", "015");
			map.put("longitude", db[0]+"");
			map.put("latitude", db[1]+"");
			map.put("mobilePhoneNumber", telmanager.getPhoneNumber());
			Task task = new Task(TaskID.TASK_PHONE_INFO, map, this
					.getClass().getName(), "手机信息");
			MobileApplication.poolManager.addTask(task);
	}
	
	public void firstSendUserBehavior(){
		Map<String, String> map =UserBehaviorInfo.sendUserOpenAppInfo();
		map.put("operateType", "014");
		map.put("operateObjID", "");
		map.put(ParentHandlerService.URL, RequestURL.postUserbehaviorPhoneInfo());
		Task task = new Task(TaskID.TASK_POST_USERBEHAVIORINFO, map, this
				.getClass().getName(), "用户安装应用行为");
		MobileApplication.poolManager.addTask(task);
	}
}
