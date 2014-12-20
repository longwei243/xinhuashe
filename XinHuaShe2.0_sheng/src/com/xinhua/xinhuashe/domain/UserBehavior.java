package com.xinhua.xinhuashe.domain;

public class UserBehavior {
	public String deviceID;
	public String appVersion;
	public String deviceIMSI;
	public String deviceBrand;
	public String deviceModel;
	public String deviceOSVersion;
	public String longitude;
	public String latitude;
	public String netWorkType;
	public String userIP;
	public static String height="";
	public static String width="";
	public UserBehavior(){};
	
	public String getUserIP() {
		return userIP;
	}

	public void setUserIP(String userIP) {
		this.userIP = userIP;
	}

	public String getDeviceID() {
		return deviceID;
	}
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	public String getAppVersion() {
		return appVersion;
	}
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	public String getDeviceIMSI() {
		return deviceIMSI;
	}
	public void setDeviceIMSI(String deviceIMSI) {
		this.deviceIMSI = deviceIMSI;
	}
	public String getDeviceBrand() {
		return deviceBrand;
	}
	public void setDeviceBrand(String deviceBrand) {
		this.deviceBrand = deviceBrand;
	}
	public String getDeviceModel() {
		return deviceModel;
	}
	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}
	public String getDeviceOSVersion() {
		return deviceOSVersion;
	}
	public void setDeviceOSVersion(String deviceOSVersion) {
		this.deviceOSVersion = deviceOSVersion;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getNetWorkType() {
		return netWorkType;
	}
	public void setNetWorkType(String netWorkType) {
		this.netWorkType = netWorkType;
	}
	
}
