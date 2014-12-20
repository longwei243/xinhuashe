package com.xinhua.xinhuashe.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.apache.http.conn.util.InetAddressUtils;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
/**
 * SIM卡信息获取
 * @author azuryleaves
 *
 */
public class TelManager {
	
	private TelephonyManager telephonyManager;
	private double latitude = 0, longitude = 0;
	private LocationManager locationManager;
	private Context context;

	public TelManager(Context context) {
		telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE); 
	}

	/**
	 * SIM卡状态
	 * 
	 * @return
	 */
	public String getSIMState() {
		String ms_simstate;
		int mi_simstate = telephonyManager.getSimState();
		if (TelephonyManager.SIM_STATE_READY == mi_simstate) {
//			ms_simstate = "SIM卡良好";
			ms_simstate = "0";
		} else if (TelephonyManager.SIM_STATE_ABSENT == mi_simstate) {
//			ms_simstate = "SIM卡缺失";
			ms_simstate = "1";
		} else if (TelephonyManager.SIM_STATE_NETWORK_LOCKED == mi_simstate) {
//			ms_simstate = "SIM卡网络锁定";
			ms_simstate = "2";
		} else if (TelephonyManager.SIM_STATE_PIN_REQUIRED == mi_simstate) {
//			ms_simstate = "SIM-PIN锁定";
			ms_simstate = "3";
		} else if (TelephonyManager.SIM_STATE_PUK_REQUIRED == mi_simstate) {
//			ms_simstate = "SIM-PUK锁定";
			ms_simstate = "4";
		} else {
//			ms_simstate = "未知";
			ms_simstate = "5";
		}
		return ms_simstate;
	}

	/**
	 * 运营商名称
	 * @return
	 */
	public String getSIMOperatorName() {
		String operatorName = telephonyManager.getSimOperatorName();
		if (null == operatorName)
			operatorName = "";
		return operatorName;
	}

	/**
	 * 运营商代码
	 * @return
	 */
	public String getSIMOperatorCode() {
		String operatorCode = telephonyManager.getSimOperator();
		if (null == operatorCode)
			operatorCode = "";
		return operatorCode;
	}

	/**
	 * SIM卡卡号
	 * ICCID：Integrate circuit card identity 集成电路卡识别码（固化在手机SIM卡中）
	 * ICCID为IC卡的唯一识别号码，共有20位数字组成，其编码格式为：
	 */
	public String getSimSerialNumber() {
		String simSerialNumber = telephonyManager.getSimSerialNumber();
		if (null == simSerialNumber)
			simSerialNumber = "";
		return simSerialNumber;
	}

	/**
	 * 用户手机号码，很可能为空
	 * MSISDN:Mobile Subscriber International ISDN/PSTN
	 * number(ISDN即是综合业务数字网，是Integrated Service Digital Network 的简称)
	 * MSISDN是指主叫用户为呼叫GSM PLMN中的一个移动用户所需拨的号码 ，作用同于固定网PSTN号码
	 */
	public String getPhoneNumber() {
		String phoneNumber = telephonyManager.getLine1Number();
		if (null == phoneNumber)
			phoneNumber = "";
		return phoneNumber;
	}

	/**
	 * 国际移动用户识别码（IMSI：International Mobile SubscriberIdentification Number）
	 * @return
	 */
	public String getIMSI() {
		String imsi = telephonyManager.getSubscriberId();
		if (null == imsi)
			imsi = "";
		return imsi;
	}

	/**
	 * 唯一的设备ID
	 * GSM手机的 IMEI 和 CDMA手机的 MEID
	 * IMEI(International Mobile Equipment Identity)也称手机串号，是国际移动设备身份码的缩写，国际移动装备辨识码，是由15位数字组成的"电子串号"，它与每台手机一...共38次编辑
	 */
	public String getDeviceId() {
		String deviceId = telephonyManager.getDeviceId();
		if (null == deviceId)
			deviceId = "";
		return deviceId;
		
	}

	/**
	 * 电话方位
	 * 
	 * @return
	 */
	public CellLocation getCellLocation() {
		return telephonyManager.getCellLocation();
	}
	/**
	 * 网络制式
	 */
	public int getNetworkType(){
		int networkType = telephonyManager.getNetworkType();
		return networkType;
		
	}
	/**
	 * wifi ip 
	 */
	public static String getLocalIpAddressV4()
    {
        String ip ="";
        try
        {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
            {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
                {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(inetAddress.getHostAddress()))  //这里做了一步IPv4的判定
                    {
                        ip = inetAddress.getHostAddress().toString();
                        return ip;
                    }
                }
            }
        } catch (SocketException e)
        {
//            Log.i("SocketException--->", ""+e.getLocalizedMessage());
            return "ip is error";
        }
        return ip;
    }
	//获取设备经纬度
	 /*public static double[] getLoc(Context mContext) {
	        double[] mLoc = new double[3];
	        LocationManager mLocationManager = (LocationManager)mContext
	                .getSystemService(Context.LOCATION_SERVICE);
	 
	        Criteria mCriteria = new Criteria();
	        mCriteria.setAccuracy(Criteria.ACCURACY_FINE);
	        mCriteria.setAltitudeRequired(true);
	        mCriteria.setBearingRequired(false);
	        mCriteria.setCostAllowed(true);
	        mCriteria.setPowerRequirement(Criteria.POWER_LOW);
	 
	        String mProvider = mLocationManager.getBestProvider(mCriteria, true);
	        System.out.println("mProvider"+mProvider);
	        Location mLocation = mLocationManager.getLastKnownLocation(mProvider);
	        if (mLocation != null) {
	            mLoc[0] = mLocation.getLongitude();
	            mLoc[1] = mLocation.getLatitude();
	            mLoc[2] = mLocation.getAltitude();
	        }
	       
	        return mLoc;
	    }*/
	/*public double[] InitLocation(){
		  System.out.println("locationmanager"+LocationManager.GPS_PROVIDER);
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Criteria criteria = new Criteria();     
			criteria.setAccuracy(Criteria.ACCURACY_FINE);	//-- 高精度
			criteria.setAltitudeRequired(false);	//-- 不要求海拔 
			criteria.setBearingRequired(false);		//-- 不要求方位  
			criteria.setCostAllowed(true);		//-- 允许有花费
			criteria.setPowerRequirement(Criteria.POWER_LOW);	//-- 低功耗
			
			//-- 从可用的位置提供器中，匹配以上标准的最佳提供器
			String provider = locationManager.getBestProvider(criteria, true);  
			System.out.println("-----provider--------"+provider);
			//-- 获得最后一次变化的位置
			Location location = locationManager.getLastKnownLocation(provider);   
			
			   
			return updataGpsWidthLocation(location);
			//首次进去获取GPS信息     
			//每隔一定的时间通知server去更新一下GPS信息     
			//这个时候屏幕上面应该会出现一个锅了     
			//监听位置变化，1秒一次，距离10米以上
		} else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);  
            return updataGpsWidthLocation(location);
		}
	}   
	private double[] updataGpsWidthLocation(Location location) { 
		double[] db= null;
		if (location != null) {
			longitude = location.getLongitude();	//-- 经度
			latitude = location.getLatitude();	//-- 纬度
			 db = new double[2];
			db[0]=longitude;
			db[1]=latitude;
			return db;
		} else {
			longitude = 0;
			latitude = 0;
			System.out.println("-----location为空-----经度-----"+longitude+"-----纬度-----"+latitude);
			return db;
		}
	}   */
}
