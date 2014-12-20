package com.xinhua.xinhuashe.option.update.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.threadpool.Task;
import com.android.variable.StaticVariable;
import com.xinhua.xinhuashe.service.FilePath;
import com.xinhua.xinhuashe.service.ParentHandlerService;
import com.xinhua.xinhuashe.util.NetUtils;
import com.xinhua.xinhuashe.util.UpdateUtil;

/**
 * 版本更新
 * 
 */
public class UpdateService {

	/**
	 * 服务器客户端版本信息
	 * 
	 * @param task
	 * @param message
	 */
	public static void getServerVersionInfo(Task task, Message message) {
		String result = ParentHandlerService.get(task, message);
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

	public static void getDownLoadFile(Task task, Message message) {
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) task.getMap();
		downLoadFile(map.get(ParentHandlerService.URL).toString(), FilePath.UpdatePath, map.get("apkname")
				.toString(), (Handler) map.get("handler"));
		Bundle data = message.getData();
		data.putInt(NetUtils.StatusCode, HttpStatus.SC_OK);
	}

	public static void downLoadFile(String urlPath, String filePath,
			String fileName, Handler handler) {

		int fileSize = 0, downloadSize = 0;
		Message msg = null;

		try {
			URL url = new URL(urlPath);
			URLConnection urlConnection = url.openConnection();
			urlConnection.connect();
			InputStream inputStream = urlConnection.getInputStream();
			fileSize = urlConnection.getContentLength(); // -- 获取文件大小
			if (fileSize <= 0) {
				throw new RuntimeException("无法获知文件大小");
			}
			if (inputStream == null) {
				throw new RuntimeException("无返回数据");
			}
			FileOutputStream fileOutputStream = new FileOutputStream(filePath
					+ File.separator + fileName); // -- 把数据存入路径+文件名
			byte b[] = new byte[1024];
			msg = handler.obtainMessage();
			msg.what = UpdateUtil.ProgessBar_Max;
			msg.arg1 = fileSize; // -- 将文件大小传入handler
			handler.sendMessage(msg); // -- 设置进度条最大值
			do {
				// 循环读取
				int numread = inputStream.read(b);
				if (numread == -1) {
					fileOutputStream.close();
					break;
				}
				fileOutputStream.write(b, 0, numread);
				downloadSize += numread; // -- 已经下载大小
				msg = handler.obtainMessage();
				msg.what = UpdateUtil.ProgessBar_Progress;
				msg.arg1 = downloadSize; // -- 将已下载大小传入handler
				handler.sendMessage(msg); // -- 更新进度条
			} while (true);
			msg = handler.obtainMessage();
			msg.what = UpdateUtil.DownLoad_Finish;
			msg.obj = FilePath.UpdatePath
					+ File.separator + fileName;
			handler.sendMessage(msg); // -- 通知下载完成
			inputStream.close();
		} catch (MalformedURLException e) {
			Log.e(StaticVariable.TAG, e.getMessage(), e);
			e.printStackTrace();
			msg = handler.obtainMessage();
			msg.what = UpdateUtil.DownLoad_Error;
			msg.obj = e.getMessage();
			handler.sendMessage(msg); // -- 通知下载出错
		} catch (IOException e) {
			Log.e(StaticVariable.TAG, e.getMessage(), e);
			e.printStackTrace();
			msg = handler.obtainMessage();
			msg.what = UpdateUtil.DownLoad_Error;
			msg.obj = e.getMessage();
			handler.sendMessage(msg); // -- 通知下载出错
		}

	}

}
