package com.xinhua.xinhuashe.util;

import java.io.File;
import java.io.FileFilter;
import java.util.UUID;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

/**
 * 设备工具
 * 
 * @author azuryleaves
 * @since 2014-5-15 下午4:51:03
 * @version 1.0
 * 
 */
public class DeviceUtils {

	/**
	 * 拨打电话
	 * 
	 * @param context
	 * @param paramString
	 */
	public static void call(Context context, String paramString) {
		Uri localUri = Uri.parse("tel:" + paramString);
		Intent localIntent = new Intent(Intent.ACTION_CALL, localUri);
		context.startActivity(localIntent);
	}

	/**
	 * 电话状态
	 * 
	 * @param context
	 * @return 1.tm.CALL_STATE_IDLE = 0 无活动 2.tm.CALL_STATE_RINGING = 1 响铃
	 *         3.tm.CALL_STATE_OFFHOOK = 2 摘机
	 */
	public static int getCallState(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getCallState();
	}

	/**
	 * 获取屏幕高度
	 * 
	 * @param paramActivity
	 * @return 屏幕高度，单位px
	 */
	public static int getScreenHeight(Activity paramActivity) {
		DisplayMetrics localDisplayMetrics = new DisplayMetrics();
		
		paramActivity.getWindowManager().getDefaultDisplay()
				.getMetrics(localDisplayMetrics);
		return localDisplayMetrics.heightPixels;
	}

	/**
	 * 获取屏幕宽度
	 * 
	 * @param paramActivity
	 * @return 屏幕宽度，单位px
	 */
	public static int getScreenWidth(Activity paramActivity) {
		DisplayMetrics localDisplayMetrics = new DisplayMetrics();
		paramActivity.getWindowManager().getDefaultDisplay()
				.getMetrics(localDisplayMetrics);
		return localDisplayMetrics.widthPixels;
	}

	public static String getUUID() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 获取外部存储设备是否可写
	 * 
	 * @return
	 */
	public static boolean isExternalStorageWriteable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return false;
		}
		return false;
	}

	/**
	 * 屏幕是否唤醒
	 * 
	 * @param paramContext
	 * @return
	 */
	public static boolean isScreenOn(Context paramContext) {
		return ((PowerManager) paramContext
				.getSystemService(Context.POWER_SERVICE)).isScreenOn();
	}

	/**
	 * 获取该设备中有效的CPU核数 需要查看系统文件夹 "/sys/devices/system/cpu" 的权限
	 * 
	 * @return 设备CPU核数
	 */
	public static int getCPUNumCores() {
		try {
			// -- 获取CPU信息目录
			File dir = new File("/sys/devices/system/cpu/");
			// -- 过滤需要的信息
			File[] files = dir.listFiles(new FileFilter() {

				@Override
				public boolean accept(File pathname) {
					// Check if filename is "cpu", followed by a single digit
					// number
					if (Pattern.matches("cpu[0-9]", pathname.getName())) {
						return true;
					}
					return false;
				}

			});
			// -- 返回CPU核数
			return files.length;
		} catch (Exception e) {
			// -- 默认返回CPU核数为1
			return 1;
		}
	}

	/**
	 * Gps是否打开 需要<uses-permission
	 * android:name="android.permission.ACCESS_FINE_LOCATION" />权限
	 * 
	 * @param context
	 *            上下文
	 * @return 如果可用返回true
	 */
	public static boolean isGpsEnabled(Context context) {
		LocationManager lm = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	/**
	 * Wifi是否打开.
	 * 
	 * @param context
	 *            上下文
	 * @return 如果可用返回true
	 */
	public static boolean isWifiEnabled(Context context) {
		ConnectivityManager mgrConn = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager mgrTel = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return ((mgrConn.getActiveNetworkInfo() != null && mgrConn
				.getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel
				.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
	}
	/**
	 * 获取终端型号
	 * @return
	 */
	public static String getDeviceModels(){
		String models = Build.MODEL;
		return models;
	}
	/**
	 * 获取终端系统版本号
	 * @return
	 */
	public String getDeviceVersionNumber(){
		String release = Build.VERSION.RELEASE;
		return release;
	}
	/**
	 * api版本
	 */
	public int getApi(){
		int api = Build.VERSION.SDK_INT;
		return api;
	}
	/**
	 * 设备序列号
	 */
	@SuppressLint("NewApi")
	public String getserial(){
		String serial = Build.SERIAL;
		return serial;
	}
	/**
	 * 手机品牌
	 */
	public  String getBrand(){
		String brand = Build.BOARD;
		return brand;
	}
}
