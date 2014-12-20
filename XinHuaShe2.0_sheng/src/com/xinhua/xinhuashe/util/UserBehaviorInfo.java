package com.xinhua.xinhuashe.util;

import java.util.HashMap;
import java.util.Map;

import com.android.net.update.Config;
import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.domain.UserBehavior;
import com.xinhua.xinhuashe.domain.UserInfo;
import com.xinhua.xinhuashe.service.MobileApplication;


public class UserBehaviorInfo {
	public static UserBehavior ub;
	public Map<String, String> userBehaviorInfo(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("appID", "d0045");
		//map.put("deviceID", telmanager.getDeviceId());
		//map.put("appVersion", Config.getVerName(context, context.getResources().getString(R.string.package_name)));
		map.put("appStoreID", "004");
		//map.put("deviceIMSI", telmanager.getSIMState());
		map.put("mobileOS", "02");
		//map.put("deviceBrand", deviceUtils.getDeviceModels());
		//map.put("deviceModel",deviceUtils.getBrand());
		//map.put("deviceOSVersion", deviceUtils.getDeviceVersionNumber());
		//map.put("width", String.valueOf(DeviceUtils.getScreenWidth((Activity) context)));
		//map.put("height", String.valueOf(DeviceUtils.getScreenHeight((Activity) context)));
		//map.put("longitude", db[0]+"");
		//map.put("latitude", db[1]+"");
		//map.put("netWorkType", String.valueOf(telmanager.getNetworkType()));
		map.put("sessionID", "");
		map.put("serviceParm", "");
		//map.put("userIP", telmanager.getLocalIpAddressV4());
		return map;
	}
	public static UserBehavior UserBehaviorInfoOpenApp() {
		ub = new UserBehavior();
		double[] db = MobileApplication.db;
		TelManager telmanager = new TelManager(MobileApplication.mobileApplication);
		DeviceUtils deviceUtils = new DeviceUtils();
		ub.setDeviceID(telmanager.getDeviceId());
		ub.setDeviceIMSI(telmanager.getIMSI());
		ub.setAppVersion( Config.getVerName(MobileApplication.mobileApplication,MobileApplication.mobileApplication.getResources().getString(R.string.package_name)));
		ub.setDeviceBrand(deviceUtils.getDeviceModels());
		ub.setDeviceModel(deviceUtils.getBrand());
		ub.setDeviceOSVersion(deviceUtils.getDeviceVersionNumber());
		ub.setLatitude(db[1]+"");
		ub.setLongitude(db[0]+"");
		ub.setNetWorkType(String.valueOf(telmanager.getNetworkType()));
		ub.setUserIP(telmanager.getLocalIpAddressV4());
		return ub;
	}
	public static  Map<String, String> sendUserOpenAppInfo(){
		UserBehaviorInfo ubi = new UserBehaviorInfo();
		Map<String, String> map  = ubi.userBehaviorInfo();
		UserBehavior ub = UserBehaviorInfoOpenApp();
		map.put("deviceID", ub.getDeviceID());
		map.put("appVersion", ub.getAppVersion());
		map.put("deviceIMSI", ub.getDeviceIMSI());
		map.put("deviceBrand", ub.getDeviceBrand());
		map.put("deviceModel", ub.getDeviceModel());
		map.put("deviceOSVersion", ub.getDeviceOSVersion());
		map.put("longitude", ub.getLongitude());
		map.put("latitude", ub.getLatitude());
		map.put("netWorkType", ub.getNetWorkType());
		map.put("height", UserBehavior.height);
		map.put("width", UserBehavior.width);
		map.put("userIP",ub.getUserIP());
		map.put("userID", UserInfo.userId);
		return map;
	}
}
