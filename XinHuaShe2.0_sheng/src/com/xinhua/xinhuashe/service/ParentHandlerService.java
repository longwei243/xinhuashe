package com.xinhua.xinhuashe.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.cookie.Cookie;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Message;

import com.android.threadpool.Task;
import com.google.gson.Gson;
import com.xinhua.xinhuashe.util.NetUtils;

public class ParentHandlerService {

	public static String URL = "url";
	public static Cookie sessionCookie = null;
	public static Gson gson = new Gson();

	public static String get(Task task, Message message) {
		String url = task.getParams();
//		 System.out.println("---Get请求URL---" + url);
		try {
			Map<String, Object> resultMap = NetUtils.doHttpGetSetCookie(url,
					"UTF-8");
			Bundle data = message.getData();
			String result = "";
			if (resultMap != null) {
				result = (String) resultMap.get(NetUtils.Result);
				data.putInt(NetUtils.StatusCode,
						(Integer) resultMap.get(NetUtils.StatusCode));
			} else {
				data.putInt(NetUtils.StatusCode, NetUtils.TimeOut);
			}
			message.setData(data);
			System.out.println("---Get请求返回结果---" + result);
			return result;
		} catch (Exception e) {
			System.out.println("ParentHandlerService-get请求失败");
			e.printStackTrace();
		}
		return null;
	}

	public static String post(Task task, Message message) {
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) task.getMap();
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			Iterator<Entry<String, String>> iterator = map.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Entry<String, String> entry = (Entry<String, String>) iterator
						.next();
				String key = entry.getKey();
				String value = entry.getValue();
				params.add(new BasicNameValuePair(key, value));
			}
			String url = map.get(URL);
			System.out.println("---Post请求URL---" + url);
			Map<String, Object> resultMap = NetUtils.doHttpPostSetCookie(url,
					params);
			Bundle data = message.getData();
			String result = "";
			if (resultMap != null) {
				result = (String) resultMap.get(NetUtils.Result);
				data.putInt(NetUtils.StatusCode,
						(Integer) resultMap.get(NetUtils.StatusCode));
			} else {
				data.putInt(NetUtils.StatusCode, NetUtils.TimeOut);
			}
			message.setData(data);
			 System.out.println("---Post请求返回结果---" + result + "---result为空---" + (result == null));
			return result;
		} catch (Exception e) {
			// System.out.println("ParentHandlerService-post请求失败");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 通用方法 - get
	 * 
	 * @param task
	 * @param message
	 */
	public static void getGeneralMethod(Task task, Message message) {
		String result = ParentHandlerService.get(task, message);
		if (result != null && !"".equals(result)) {
//			try {
//				JSONObject jsonObject = new JSONObject(result);
//				message.obj = jsonObject;
//			} catch (JSONException e) {
//				message.obj = null;
//				e.printStackTrace();
//			}
			message.obj = result;
		} else {
			message.obj = null;
		}
	}

	/**
	 * 通用方法 - post
	 * 
	 * @param task
	 * @param message
	 */
	public static void postGeneralMethod(Task task, Message message) {
		String result = ParentHandlerService.post(task, message);
		if (result != null && !"".equals(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				message.obj = jsonObject;
			} catch (JSONException e) {
				message.obj = null;
				e.printStackTrace();
			}
		} else {
			message.obj = null;
		}
	}

}
