package com.mdcl.message;

import java.lang.reflect.Field;

import org.androidpn.client.Constants;
import org.androidpn.client.LogUtil;
import org.androidpn.client.ServiceManager;
import org.androidpn.client.XmppManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.xinhuanews.sheng.R;

public class MessageUtil {

	private static final String LOGTAG = LogUtil.makeLogTag(MessageUtil.class);

	private static MessageUtil messageUtil;

	private static SharedPreferences sharedPrefs;
	
	private static TelephonyManager telephonyManager;
	
	private MessageCallBack callBack;
	
	private AndroidpnListener androidpnListener;

	private MessageUtil() {
	}

	/**
	 * ��ȡ������Ϣ������
	 * 
	 * @param c android�е������Ķ���
	 * @param packageName ����Ӧ�õİ���
	 * @param mobileType �ֻ��ͺ�
	 * @param userName �û���¼��
	 * @param name �û����
	 * @return ������Ϣ���������
	 */
	public static MessageUtil getInstance(Context c, String packageName,
			String mobileType, String userName,String name) {
		if (messageUtil == null) {
			return messageUtil;
		}
		
		messageUtil = new MessageUtil();
		
		telephonyManager = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
		// Get deviceId
		String deviceId = telephonyManager.getDeviceId();
		// Log.d(LOGTAG, "deviceId=" + deviceId);
		Editor editor = sharedPrefs.edit();
		editor.putString(Constants.DEVICE_ID, deviceId);
		editor.putString(Constants.PACKAGE_NAME, packageName);
		editor.putString(Constants.BRAND, mobileType);
		editor.putString(Constants.XMPP_NAME, name);
		
		String oldUserName = sharedPrefs.getString(Constants.XMPP_USERNAME, null);
		
		if(oldUserName != null && userName != null && oldUserName.equals(userName)){
			editor.putBoolean(Constants.REGISTERED, true);
		}else{
			editor.putBoolean(Constants.REGISTERED, false);
		}
		
		editor.commit();

		// ����service
		messageUtil.startService(c);

		return messageUtil;
	}

	/**
	 * ��ȡ������Ϣ�����࣬����İ����ֻ��ͺ��Զ���ȡ���û���¼�Ŵ��ֻ�洢�л�ȡ�������ڣ�ϵͳ�Զ����
	 * 
	 * @param c android�е������Ķ���
	 * @param name �û����
	 * @return ������Ϣ���������
	 */
	public static MessageUtil getInstance(Context c,String name) {
		if (messageUtil != null) {
			return messageUtil;
		}

		messageUtil = new MessageUtil();
		
		// ��ȡ��������
		String packageName = null;
		try {
			packageName = c.getApplicationContext().getPackageName();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String mobileType = null;

		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				if (field.getName().equals(Constants.BRAND)) {
					mobileType = field.get(null).toString();
					Log.d(LOGTAG, field.getName() + " : " + field.get(null));
				}
			} catch (Exception e) {
				Log.e(LOGTAG, "an error occured when collect crash info", e);
			}
		}

		sharedPrefs = c.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME,
				Context.MODE_PRIVATE);

		telephonyManager = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
		// Get deviceId
		String deviceId = telephonyManager.getDeviceId();
		// Log.d(LOGTAG, "deviceId=" + deviceId);
		Editor editor = sharedPrefs.edit();
		editor.putString(Constants.DEVICE_ID, deviceId);
		editor.putString(Constants.PACKAGE_NAME, packageName);
		editor.putString(Constants.BRAND, mobileType);
		editor.putString(Constants.XMPP_NAME, name);

		String userName = sharedPrefs.getString(Constants.XMPP_USERNAME, null);
		
		if(userName != null){
			editor.putBoolean(Constants.REGISTERED, false);
		}else{
			editor.putBoolean(Constants.REGISTERED, true);
		}
		
		editor.commit();

		// ����service
		messageUtil.startService(c);

		return messageUtil;
	}

	private void startService(Context c) {
		// Start the service
        ServiceManager serviceManager = new ServiceManager(c);
        serviceManager.setNotificationIcon(R.drawable.notification);
        serviceManager.startService();
	}

	public void openApp(){
		XmppManager manager = XmppManager.getXmppManager(); 
		manager.openApp();
	}
	
	public void setMessageCallBack(MessageCallBack callBack){
		this.callBack = callBack;
	}
	
	public MessageCallBack getMessageCallBack(){
		return this.callBack;
	}

	public AndroidpnListener getAndroidpnListener() {
		return this.androidpnListener;
	}

	public void setAndroidpnListener(AndroidpnListener androidpnListener) {
		this.androidpnListener = androidpnListener;
	}
	
}
