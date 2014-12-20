package com.xinhua.xinhuashe.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import org.androidpn.client.Constants;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager.WakeLock;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.android.threadpool.IActivity;
import com.android.threadpool.ThreadPoolManager;
import com.mdcl.message.AndroidpnListener;
import com.mdcl.message.MessageCallBack;
import com.mdcl.message.MessageUtil;
import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.util.CacheUtils;


/**
 * 
 * 
 */
public class MobileApplication extends Application implements AMapLocationListener, Runnable{

	private LocationManagerProxy mAMapLocManager = null;
	private AMapLocation aMapLocation;// 用于判断定位超时
	private Handler handler = new Handler();
	
	public static String TAG = "XinhuaNews";
	public static MobileApplication mobileApplication;
	public static ArrayList<IActivity> allIActivity = new ArrayList<IActivity>();
	public static ArrayList<Activity> allActivity = new ArrayList<Activity>();

	public static ThreadPoolManager poolManager;
	public Receiver rev = new Receiver(); // 监控网络状态（是否在网）
	public static Properties http_properties = new Properties(),
			filepath_properties = new Properties();
	public static SharedPreferences preferences;
	//栏目信息缓存
	public static CacheUtils cacheUtils;
	
	private WakeLock wakeLock;
	private static final String GROUP_TAG ="group";
    private static final String USER_TAG = "users";
	private static final String AREA_CODE = "AREA_CODE";
    
    private String groupName = "0001";//后台获取
    private String username = "dllen";//后台获取
    
    public static double[] db= new double[2];
    public static String desc="";
    public static String addressDistric="未知";
	@Override
	public void onCreate() {
		super.onCreate();
		mobileApplication = this;
		CrashHandler crashHandler = CrashHandler.getInstance();
//		crashHandler.init(this);
		cacheUtils = CacheUtils.get(this);
		
		if(mobileApplication.cacheUtils.getAsString("pushCount") == null){
			
			mobileApplication.cacheUtils.put("pushCount", "0");
		}
	
		poolManager = ThreadPoolManager.getInstance(new ClientTask());
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		registerReceiver(rev, filter); // 注册监控网络状态的广播
		try {
			http_properties.load(getResources().openRawResource(R.raw.http));
			filepath_properties.load(getResources().openRawResource(R.raw.filepath));
		} catch (Exception e) {
			e.printStackTrace();
		}
		preferences = getSharedPreferences(TAG, MODE_PRIVATE);
		if (preferences.getBoolean("FirstRun", true)) {
			Iterator<Entry<Object, Object>> iterator = http_properties
					.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Object, Object> entry = iterator.next();
				String key = (String) entry.getKey();
				String value = (String) entry.getValue();
				preferences.edit().putString(key, value).commit();
			}
			preferences.edit().putBoolean("FirstRun", false).commit();
		}

		File file = new File(Environment.getExternalStorageDirectory()
				.getPath() + File.separator + TAG);
		if (!file.exists()) {
			file.mkdir();
		}
		File logFile = new File(Environment.getExternalStorageDirectory()
				.getPath()
				+ File.separator
				+ TAG
				+ File.separator
				+ CrashHandler.FileName);
		if (!logFile.exists()) {
			logFile.mkdir();
		}
		File updateFile = new File(Environment.getExternalStorageDirectory()
				.getPath() + File.separator + TAG + File.separator + "update");
		if (!updateFile.exists()) {
			updateFile.mkdir();
		}
		File configFile = new File(Environment.getExternalStorageDirectory()
				.getPath() + File.separator + TAG + File.separator + "config");
		if (!configFile.exists()) {
			configFile.mkdir();
		}
		File cacheFile = new File(Environment.getExternalStorageDirectory()
				.getPath() + File.separator + TAG + File.separator + "cache");
		if (!cacheFile.exists()) {
			cacheFile.mkdir();
		}
		File cachePicFile = new File(Environment.getExternalStorageDirectory()
				.getPath()
				+ File.separator
				+ TAG
				+ File.separator
				+ "cache"
				+ File.separator + "pic");
		if (!cachePicFile.exists()) {
			cachePicFile.mkdir();
		}
		File cacheDataFile = new File(Environment.getExternalStorageDirectory()
				.getPath()
				+ File.separator
				+ TAG
				+ File.separator
				+ "cache"
				+ File.separator + "data");
		if (!cacheDataFile.exists()) {
			cacheDataFile.mkdir();
		}
		
		//=====================push start==========================
		initpush();
		//=====================push end============================
		mAMapLocManager = LocationManagerProxy.getInstance(mobileApplication);
		mAMapLocManager.requestLocationUpdates(
				LocationProviderProxy.AMapNetwork, 5000, 10, this);
		handler.postDelayed(this, 12000);// 设置超过12秒还没有定位到就停止定位
	}
	
	private void initpush() {

		MessageCallBack callBack = new MessageCallBack() {

			@Override
			public void receiveMessage(String messageId, String apiKey, String title, String message, String messageUri, String titleId, String additional, String messageFrom, String packetId) {
				System.out.println("messageId是："+messageId);
				System.out.println("apiKey是："+apiKey);
				System.out.println("title是："+title);
				System.out.println("message是："+message);
				System.out.println("messageUri是："+messageUri);
				System.out.println("titleid是："+titleId);
				System.out.println("messageFrom是："+messageFrom);
				System.out.println("packetId是："+packetId);
				System.out.println("additional是："+additional);
				
				try {
					
					
					JSONObject j = new JSONObject(message);
					String content = j.getString("content");
					String newsId = j.getString("newsId");
					String pushCount = mobileApplication.cacheUtils.getAsString("pushCount");
					int pushCountNew = Integer.parseInt(pushCount)+1;
					mobileApplication.cacheUtils.put("pushCount", pushCountNew+"");
					String pushNews = "{\"newsId\":\""+newsId+"\", \"title\":\""+title.replaceAll("\"", "")+"\", \"content\":\""+content+"\"}";
					System.out.println("-----------pushNews:"+pushNews);
					System.out.println("-----------pushCountNew:"+"push"+pushCountNew);
					
					mobileApplication.cacheUtils.put("push"+pushCountNew, pushNews);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				try {
					JSONObject jsonObject = new JSONObject(additional);
					String otherInfo = (String)jsonObject.get("otherInfo");
					String endPoints = (String)jsonObject.get("pushEndpoints");
					System.out.println("otherInfo : "+otherInfo+" endpoints : "+endPoints);
					if(!"null".equals(endPoints)) {
						if(endPoints.startsWith(GROUP_TAG)){
							System.out.println("进入了群组");
							if(!additional.contains(groupName)){
								return;
							}
						}
						if(endPoints.startsWith(USER_TAG)){
							System.out.println("进入了用户");
							if(!additional.contains(username)){
								System.out.println("没有该用户");
								return;
							}
						}
						
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
            	
				Intent intent = new Intent(Constants.ACTION_SHOW_NOTIFICATION + getApplicationContext().getPackageName());
				intent.putExtra(Constants.NOTIFICATION_ID, messageId);
				intent.putExtra(Constants.NOTIFICATION_API_KEY, apiKey);
				intent.putExtra(Constants.NOTIFICATION_TITLE, title);
				intent.putExtra(Constants.NOTIFICATION_MESSAGE, message);
				intent.putExtra(Constants.NOTIFICATION_URI, messageUri);
				intent.putExtra(Constants.NOTIFICATION_FROM, messageFrom);
				intent.putExtra(Constants.PACKET_ID, packetId);
				intent.putExtra(Constants.NOTIFICATION_TITLEID, titleId);
				intent.putExtra(Constants.NOTIFICATION_ADDITIONAL, additional);

				getApplicationContext().sendBroadcast(intent);

			}
		};

		final MessageUtil messageUtil = MessageUtil.getInstance(getApplicationContext(),"名称");
		messageUtil.setMessageCallBack(callBack);

		AndroidpnListener androidpnListener = new AndroidpnListener() {

			@Override
			public void connectedSuccess() {
				System.out.println("=================connectedSuccess");
				Log.d("DemoAppActivity", "connectedSuccess...");

			}

			@Override
			public void connectedFail() {
				System.out.println("=================connectedFail");
				Log.d("DemoAppActivity", "connectedFail...");
			}

			@Override
			public void loginSuccess() {
				System.out.println("=================loginSuccess");
				Log.d("DemoAppActivity", "loginSuccess...");
			}

			@Override
			public void loginFail() {
				System.out.println("=================loginFail");
				Log.d("DemoAppActivity", "loginFail...");
			}

			@Override
			public void networkconnectedSuccess() {
				System.out.println("=================networkconnectedSuccess");
				Log.d("DemoAppActivity", "networkconnectedSuccess...");
			}

			@Override
			public void networkconnectedFail() {
				System.out.println("=================networkconnectedFail");
				Log.d("DemoAppActivity", "networkconnectedFail...");
			}

		};
		messageUtil.setAndroidpnListener(androidpnListener);
//		messageUtil.openApp();
//		acquireWakeLock();
	
	}
	
	


	public static IActivity getIActivityByName(String name) {
		IActivity ia = null;
		for (IActivity ac : allIActivity) {
			if (name.startsWith(ac.getClass().getName())) {
				ia = ac;
				break;
			}
		}
		return ia;
	}

	public static List<IActivity> getIActivitysByName(String name) {
		List<IActivity> iActivities = new ArrayList<IActivity>();
		for (IActivity ac : allIActivity) {
			if (name.startsWith(ac.getClass().getName())) {
				iActivities.add(ac);
			}
		}
		return iActivities;
	}

	public static void exitApp(Context context) {
		for (int i = 0; i < allActivity.size(); i++) {
			allActivity.get(i).finish();
		}
		allIActivity.clear();
		allActivity.clear();
		if (poolManager != null) {
			poolManager.destory();
		}
		try {
			MobileApplication.mobileApplication
					.unregisterReceiver(MobileApplication.mobileApplication.rev);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		String packName = context.getPackageName();
		ActivityManager activityMgr = (ActivityManager) context
				.getSystemService(ACTIVITY_SERVICE);
		activityMgr.restartPackage(packName);
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	private void stopLocation() {
		if (mAMapLocManager != null) {
			mAMapLocManager.removeUpdates(this);
			mAMapLocManager.destory();
		}
		mAMapLocManager = null;
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLocationChanged(AMapLocation location) {

		if (location != null) {
			this.aMapLocation = location;// 判断超时机制
			Double geoLat = location.getLatitude();
			Double geoLng = location.getLongitude();
			
			Log.i("定位经度", geoLat+"");
			Log.i("定位纬度", geoLng+"");
			System.out.println("定位经度"+geoLat+";定位纬度"+geoLng);
			db[1]=geoLat;//纬度
			db[0]=geoLng;//经度
			stopLocation();
			String cityCode = "";
//			String desc = "";
			Bundle locBundle = location.getExtras();
			if (locBundle != null) {
				cityCode = locBundle.getString("citycode");
				desc = locBundle.getString("desc");
				addressDistric=location.getDistrict();
				System.out.println("定位结果为："+addressDistric);
			}
//			String str = (location.getProvince() + location.getCity()
//					+ location.getDistrict());
//			System.out.println("定位当前位置"+desc.replace(" ", ""));
		}
	}
	public void run() {
		if (aMapLocation == null) {
//			Toast.makeText(this, "12秒内还没有定位成功，停止定位", Toast.LENGTH_SHORT).show();
			stopLocation();// 销毁掉定位
		}
	}
	

}
