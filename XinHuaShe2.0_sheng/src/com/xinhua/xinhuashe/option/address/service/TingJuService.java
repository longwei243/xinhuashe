package com.xinhua.xinhuashe.option.address.service;

import org.apache.http.HttpStatus;

import android.os.Bundle;
import android.os.Message;

import com.android.threadpool.Task;
import com.xinhua.xinhuashe.service.MobileApplication;
import com.xinhua.xinhuashe.service.ParentHandlerService;
import com.xinhua.xinhuashe.service.TaskID;
import com.xinhua.xinhuashe.util.NetUtils;

public class TingJuService {

	public static final String ColumnInfo_TingJu = "ColumnInfo_TingJu";
	

	/**
	 * 获取厅局栏目列表 - get
	 * 
	 * @param task
	 * @param message
	 */
	public static void getTingJuColumn(Task task, Message message) {
			String result = MobileApplication.cacheUtils.getAsString(ColumnInfo_TingJu);
			if (result == null) {
				result = ParentHandlerService.get(task, message);
				System.out.println("厅局信息为："+result);
				MobileApplication.cacheUtils.put(ColumnInfo_TingJu, result);
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
