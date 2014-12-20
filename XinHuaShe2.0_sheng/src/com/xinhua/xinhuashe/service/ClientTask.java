package com.xinhua.xinhuashe.service;

import org.apache.http.HttpStatus;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.android.threadpool.IActivity;
import com.android.threadpool.Task;
import com.android.threadpool.TaskOperate;
import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.option.address.service.TingJuService;
import com.xinhua.xinhuashe.option.homepage.service.HomePageService;
import com.xinhua.xinhuashe.option.login.service.LoginService;
import com.xinhua.xinhuashe.option.update.service.UpdateService;
import com.xinhua.xinhuashe.option.xinwendongtai.service.ColumnService;
import com.xinhua.xinhuashe.util.NetUtils;

public class ClientTask implements TaskOperate {

	private static final String KEY = "ClassName";
	/**
	 * 布局相同页面发起的同一TaskId请求区分数据返回页面
	 */
	private static final String POSITION = "Position";

	@Override
	public void operate(Task task) {
		Message message = handler.obtainMessage();
		message.what = task.getTaskId();
		Bundle bundle = new Bundle();
		bundle.putString(KEY, task.getClassName());
		bundle.putInt(POSITION, task.getPosition());
		message.setData(bundle);
		switch (task.getTaskId()) {
		// 新闻动态下的栏目
		case TaskID.TASK_COLUMN_XINWENDONGTAI:
			ColumnService.getColumnList(task, message,
					ColumnService.ColumnInfo_XinWenDongTai);
			break;
		// 惠民生活下的栏目
		case TaskID.TASK_COLUMN_HUIMINGSHENGHUO:
			ColumnService.getColumnList(task, message,
					ColumnService.ColumnInfo_HuiMingShengHuo);
			break;
		// 掌上政务下的栏目
		case TaskID.TASK_COLUMN_ZHANGSHANGZHENGWU:
			ColumnService.getColumnList(task, message,
					ColumnService.ColumnInfo_ZhangShangZhengWu);

			break;
		// 互动分享下的栏目
		case TaskID.TASK_COLUMN_HUDONGFENXIANG:
			ColumnService.getColumnList(task, message,
					ColumnService.ColumnInfo_HuDongFenXiang);
			break;
			// 舆情快报下的栏目
		case TaskID.TASK_COLUMN_YUQING:
			ColumnService.getColumnList(task, message,
					ColumnService.ColumnInfo_YuQingKuaiBao);
			break;
		case TaskID.TASK_HOMEPAGE_HOT_PIC:
		case TaskID.TASK_HOMEPAGE_NEWS1:
		case TaskID.TASK_HOMEPAGE_NEWS2:
		case TaskID.TASK_ZHONGGUOWANGSHI:
			HomePageService.getHomePage(task, message);
			break;
		case TaskID.TASK_COLUNMN_NEWS1:
		case TaskID.TASK_COLUNMN_NEWS2:
		case TaskID.TASK_COLUNMN_NEWS3:
		case TaskID.TASK_COLUNMN_NEWS4:
		case TaskID.TASK_COLUNMN_NEWS_GF1:
		case TaskID.TASK_COLUNMN_NEWS_GF2:
		case TaskID.TASK_COLUNMN_NEWS_GF3:
		case TaskID.TASK_COLUNMN_NEWS_GF4:
		case TaskID.TASK_COLUNMN_NEWS_HUIMING:
		case TaskID.TASK_COLUNMN_NEWS_ZHENGWU:
			ColumnService.getColumnNews(task, message);
			break;
		case TaskID.TASK_MYDATA_INFO:
		case TaskID.TASK_VOTE:
			ParentHandlerService.getGeneralMethod(task, message);
			break;

		case TaskID.TASK_COLUMN_TINGJU:
			TingJuService.getTingJuColumn(task, message);
			break;
		case TaskID.TASK_LOGIN:
			LoginService.getLogin(task, message);
			break;
		case TaskID.TASK_UPDATE_CHECK:
			UpdateService.getServerVersionInfo(task, message);
			break;
		case TaskID.TASK_UPDATE_DOWNLOAD:
			UpdateService.getDownLoadFile(task, message);
			break;

		/*
		 * case TaskID.TASK_CHANGEPASSWORD_OLDPASSWORD:
		 * ParentHandlerService.postGeneralMethod(task, message); break;
		 */
		case TaskID.TASK_FIND_PASSWORD:
		case TaskID.TASK_POST_VOTE:
		case TaskID.TASK_CHANGEPASSWORD_NEWPASSWORD:
			ParentHandlerService.postGeneralMethod(task, message);
			break;
		// 天气信息请求
		case TaskID.TASK_WEATHER_INFO:
			ParentHandlerService.getGeneralMethod(task, message);
			break;
		case TaskID.TASK_NEWS_DETAIL:
		case TaskID.TASK_NEWS_DETAIL_PRAISE:
		case TaskID.TASK_NEWS_DETAIL_ATTENTION:
		case TaskID.TASK_SAY_TARGET:
			// 有话要说-列表
		case TaskID.TASK_SAY_COMMIT:
			ParentHandlerService.getGeneralMethod(task, message);
			break;
		case TaskID.TASK_NEWS_DETAIL_COMMENT:
			ParentHandlerService.postGeneralMethod(task, message);
			break;
		case TaskID.TASK_SAY_TYPE:
			ParentHandlerService.getGeneralMethod(task, message);
			break;
		case TaskID.TASK_SAY_COMMENT_NULL_FILE:
			ParentHandlerService.postGeneralMethod(task, message);
			break;

		case TaskID.TASK_GETQRCODE:
			ParentHandlerService.postGeneralMethod(task, message);
			break;
		case TaskID.FEED_BACK_SEND:
			ParentHandlerService.postGeneralMethod(task, message);
			break;
		case TaskID.TASK_REGISTER:
			ParentHandlerService.postGeneralMethod(task, message);
			break;
		case TaskID.TASK_CHANGE_PERSON_DATA:
			ParentHandlerService.postGeneralMethod(task, message);
			break;
		case TaskID.TASK_SHOW_PERSON_DATA:
			ParentHandlerService.postGeneralMethod(task, message);
			break;
		case TaskID.TASK_MODIFYUSERAPPLY_PERSON_DATA:
			ParentHandlerService.postGeneralMethod(task, message);
			break;
		case TaskID.TASK_SHOW_SGV_PICS:
			ParentHandlerService.getGeneralMethod(task, message);
			break;
		// 便民信息下的栏目
		case TaskID.TASK_COLUMN_BIANMINXINXI:
			ColumnService.getColumnList(task, message,
					ColumnService.ColumnInfo_BianMinXinXi);
			break;
		case TaskID.TASK_POST_USERBEHAVIORINFO:
			ParentHandlerService.postGeneralMethod(task, message);
			break;
		case TaskID.TASK_PHONE_INFO:
			ParentHandlerService.postGeneralMethod(task, message);
		default:
			break;
		}
		handler.sendMessage(message);
	}

	public Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			String className = msg.getData().getString(KEY);
			// int position = msg.getData().getInt(POSITION, 0);
			// System.out.println("---position---" + position);
			// System.out.println("---className---" + className);
			IActivity iActivity = MobileApplication
					.getIActivityByName(className);
			// List<IActivity> iActivities = MobileApplication
			// .getIActivitysByName(className);
			// IActivity iActivity = iActivities.get(position);
			if (iActivity != null) {
				int statusCode = msg.getData().getInt(NetUtils.StatusCode);
				// System.out.println("---handler-statusCode---" + statusCode);
				switch (statusCode) {
				case HttpStatus.SC_OK:
					iActivity.refresh(msg.what, msg.obj);
					break;
				case HttpStatus.SC_NOT_FOUND:
				case NetUtils.TimeOut:
					iActivity.closeLoadingView();
					iActivity.refresh(msg.what, msg.obj);
					Toast.makeText(MobileApplication.mobileApplication,
							R.string.request_timeout, Toast.LENGTH_SHORT)
							.show();
					break;
				default:
					iActivity.closeLoadingView();
					// Toast.makeText(MobileApplication.mobileApplication,
					// R.string.request_timeout, Toast.LENGTH_SHORT)
					// .show();
					break;
				}
			}
			return false;
		}
	});

}
