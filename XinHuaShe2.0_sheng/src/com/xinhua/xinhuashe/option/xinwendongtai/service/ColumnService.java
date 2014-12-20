package com.xinhua.xinhuashe.option.xinwendongtai.service;

import org.apache.http.HttpStatus;

import android.os.Bundle;
import android.os.Message;

import com.android.threadpool.Task;
import com.xinhua.xinhuashe.service.MobileApplication;
import com.xinhua.xinhuashe.service.ParentHandlerService;
import com.xinhua.xinhuashe.service.TaskID;
import com.xinhua.xinhuashe.util.CacheUtils;
import com.xinhua.xinhuashe.util.NetUtils;

public class ColumnService {

	public static final String ColumnInfo_HuiMingShengHuo = "ColumnInfo_HuiMingShengHuo";
	public static final String ColumnInfo_XinWenDongTai = "ColumnInfo_XinWenDongTai";
	public static final String ColumnInfo_ZhangShangZhengWu = "ColumnInfo_ZhangShangZhengWu";
	public static final String ColumnInfo_HuDongFenXiang = "ColumnInfo_HuDongFenXiang";
	public static final String ColumnInfo_BianMinXinXi = "ColumnInfo_BianMinXinXi";
	public static final String ColumnInfo_YuQingKuaiBao = "ColumnInfo_YuQingKuaiBao";
	
	public static String XinWenDongTai_Column_News1 = "XinWenDongTai_Column_News1";
	public static String XinWenDongTai_Column_News2 = "XinWenDongTai_Column_News2";
	public static String XinWenDongTai_Column_News3 = "XinWenDongTai_Column_News3";
	public static String XinWenDongTai_Column_News4 = "XinWenDongTai_Column_News4";
	public static String GF_Column_News1 = "GF_Column_News1";
	public static String GF_Column_News2 = "GF_Column_News2";
	public static String GF_Column_News3 = "GF_Column_News3";
	public static String GF_Column_News4 = "GF_Column_News4";
	public static String HuiMingShengHuo_Column_News = "HuiMingShengHuo_Column_News";
	public static String ZhangShangZhengWu_Column_News = "ZhangShangZhengWu_Column_News";

	/**
	 * 获取栏目列表 - get
	 * 
	 * @param task
	 * @param message
	 */
	public static void getColumnList(Task task, Message message, String code) {
			String result = MobileApplication.cacheUtils.getAsString(code);
			if (result == null) {
				result = ParentHandlerService.get(task, message);;
				MobileApplication.cacheUtils.put(code, result, CacheUtils.TIME_DAY);
			} else {
				Bundle data = message.getData();
				data.putInt(NetUtils.StatusCode, HttpStatus.SC_OK);
			}
			if (result != null && result.startsWith("[{")) {
				message.obj = result;
			} else {
				message.obj = null;
				MobileApplication.cacheUtils.remove(code);
			}
	}
	public static void getColumnNews(Task task, Message message) {
		String fileName = "";
		switch (task.getTaskId()) {
		case TaskID.TASK_COLUNMN_NEWS1:
			fileName = XinWenDongTai_Column_News1;
			break;
		case TaskID.TASK_COLUNMN_NEWS2:
			fileName = XinWenDongTai_Column_News2;
			break;
		case TaskID.TASK_COLUNMN_NEWS3:
			fileName = XinWenDongTai_Column_News3;
			break;
		case TaskID.TASK_COLUNMN_NEWS4:
			fileName = XinWenDongTai_Column_News4;
			break;
		case TaskID.TASK_COLUNMN_NEWS_GF1:
			fileName = GF_Column_News1;
			break;
		case TaskID.TASK_COLUNMN_NEWS_GF2:
			fileName = GF_Column_News2;
			break;
		case TaskID.TASK_COLUNMN_NEWS_GF3:
			fileName = GF_Column_News3;
			break;
		case TaskID.TASK_COLUNMN_NEWS_GF4:
			fileName = GF_Column_News4;
			break;
		case TaskID.TASK_COLUNMN_NEWS_HUIMING:
			fileName = HuiMingShengHuo_Column_News;
			break;
		case TaskID.TASK_COLUNMN_NEWS_ZHENGWU:
			fileName = ZhangShangZhengWu_Column_News;
			break;
		
		default:
			break;
		}
		String result = MobileApplication.cacheUtils.getAsString(fileName);
		if (result == null) {
			result = ParentHandlerService.get(task, message);;
			MobileApplication.cacheUtils.put(fileName, result, 60 * 5);
		} else {
			Bundle data = message.getData();
			data.putInt(NetUtils.StatusCode, HttpStatus.SC_OK);
		}
		if (result != null && result.startsWith("[{")) {
			message.obj = result;
		} else {
			message.obj = null;
			MobileApplication.cacheUtils.remove(fileName);
		}
	}
	
	
}
