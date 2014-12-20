package com.xinhua.xinhuashe.option.homepage.service;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Message;

import com.android.threadpool.Task;
import com.xinhua.xinhuashe.service.MobileApplication;
import com.xinhua.xinhuashe.service.ParentHandlerService;
import com.xinhua.xinhuashe.service.TaskID;
import com.xinhua.xinhuashe.util.CacheUtils;
import com.xinhua.xinhuashe.util.NetUtils;

/**
 * 
 * 
 * @author azuryleaves
 * @since 2014-1-8 上午11:49:52
 * @version 1.0
 * 
 */
public class HomePageService {

	public static String HomepageNews1 = "HomepageNews1";
	public static String HomepageNews2 = "HomepageNews2";
	public static String HomepageHotPic = "HomepageHotPic";

	/**
	 * 
	 * 
	 * @param task
	 * @param message
	 */
	public static void getHomePage(Task task, Message message) {
		String fileName = "";
		switch (task.getTaskId()) {
		case TaskID.TASK_HOMEPAGE_NEWS1:
			fileName = HomepageNews1;
			break;
		case TaskID.TASK_HOMEPAGE_NEWS2:
			fileName = HomepageNews2;
			break;
		case TaskID.TASK_HOMEPAGE_HOT_PIC:
			fileName = HomepageHotPic;
			break;
		default:
			break;
		}
		String result = MobileApplication.cacheUtils.getAsString(fileName);
//		String result ="";
		if (result == null) {
			result = ParentHandlerService.get(task, message);
			MobileApplication.cacheUtils.put(fileName, result, 20);
		} else {
			Bundle data = message.getData();
			data.putInt(NetUtils.StatusCode, HttpStatus.SC_OK);
		}
		if (result != null && !"".equals(result)) {
			switch(task.getTaskId()) {
			case TaskID.TASK_HOMEPAGE_NEWS1:
			case TaskID.TASK_HOMEPAGE_NEWS2:
				try {
					JSONObject obj = new JSONObject(result);
					JSONArray jsonArray = obj.getJSONArray("content");
					message.obj = jsonArray;
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			default :
				try {
					JSONArray jsonArray = new JSONArray(result);
					message.obj = jsonArray;
				} catch (JSONException e) {
					message.obj = null;
					e.printStackTrace();
				}
				break;
			}
			
		} else {
			message.obj = null;
			MobileApplication.cacheUtils.remove(fileName);
		}
		
	}

}
