package com.xinhua.xinhuashe.option.login.service;

import java.io.File;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Message;

import com.android.threadpool.Task;
import com.xinhua.xinhuashe.service.FilePath;
import com.xinhua.xinhuashe.service.MobileApplication;
import com.xinhua.xinhuashe.service.ParentHandlerService;
import com.xinhua.xinhuashe.util.FileUtils;
import com.xinhua.xinhuashe.util.NetUtils;

/**
 * 
 * 
 * @author azuryleaves
 * @since 2014-3-15 上午3:56:59
 * @version 1.0
 * 
 */
public class LoginService {

	public static String ColumnInfo = "ColumnInfo";
	public static String UserInfoFileName = "UserInfo.dat";

	/**
	 * 用户登录 - get
	 * 
	 * @param task
	 * @param message
	 */
	public static void getLogin(Task task, Message message) {
		if (FileUtils.isFileExist(FilePath.CacheDataPath + File.separator
				+ UserInfoFileName)) {
			try {
				String result = FileUtils.readFileAsStr(FilePath.CacheDataPath
						+ File.separator + UserInfoFileName);
				if (result != null && !"".equals(result)) {
					JSONObject jsonObject = new JSONObject(result);
					JSONObject userinfo = jsonObject.getJSONObject("vo");
					String name = userinfo.getString("name");
					@SuppressWarnings("unchecked")
					Map<String, Object> map = (Map<String, Object>) task
							.getMap();
					if (map.get("name").toString().equals(name)) {
						Bundle data = message.getData();
						data.putInt(NetUtils.StatusCode, HttpStatus.SC_OK);
						message.obj = result;
					} else {
						result = ParentHandlerService.post(task, message);
						if (result != null && !"".equals(result)) {
							FileUtils.deleteFile(FilePath.CacheDataPath
									+ File.separator + UserInfoFileName);
							FileUtils.saveFile(FilePath.ConfigPath,
									UserInfoFileName, result.getBytes());
							message.obj = result;
						} else {
							message.obj = null;
						}
					}
				} else {
					message.obj = null;
				}
			} catch (Exception e) {
				message.obj = null;
				e.printStackTrace();
			}
		} else {
			String result = ParentHandlerService.post(task, message);
			if (result != null && !"".equals(result)) {
				FileUtils.saveFile(FilePath.ConfigPath, UserInfoFileName,
						result.getBytes());
				message.obj = result;
			} else {
				message.obj = null;
			}
		}
	}

	/**
	 * 获取栏目列表 - get
	 * 
	 * @param task
	 * @param message
	 */
	public static void getColumnList(Task task, Message message) {
		String result = MobileApplication.cacheUtils.getAsString(ColumnInfo);
		result = null;
		if (result == null) {
			result = ParentHandlerService.post(task, message);
			FileUtils.saveFile(FilePath.ConfigPath,
					ColumnInfo, result.getBytes());
			MobileApplication.cacheUtils.put(ColumnInfo, result);
		} else {
			Bundle data = message.getData();
			data.putInt(NetUtils.StatusCode, HttpStatus.SC_OK);
		}
		if (result != null && !"".equals(result)) {
			message.obj = result;
		} else {
			message.obj = null;
		}
	}

}
