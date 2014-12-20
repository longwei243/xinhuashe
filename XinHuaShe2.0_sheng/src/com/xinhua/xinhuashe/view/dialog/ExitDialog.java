package com.xinhua.xinhuashe.view.dialog;

import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.android.threadpool.Task;
import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.request.RequestURL;
import com.xinhua.xinhuashe.service.MobileApplication;
import com.xinhua.xinhuashe.service.ParentHandlerService;
import com.xinhua.xinhuashe.service.TaskID;
import com.xinhua.xinhuashe.util.UserBehaviorInfo;

/**
 * 退出对话框
 * 
 * @author azuryleaves
 * 
 */
public class ExitDialog extends ParentDialog {

	private Context context;
	private static ExitDialog dialog;

	public ExitDialog(Context context) {
		super(context, onComfirmClickListener);
		this.context = context;
		setContentView();
		dialog = this;
	}

	public void setContentView() {
		View setting_netaddress = LayoutInflater.from(context).inflate(
				R.layout.exit, null);
		super.setContentView(setting_netaddress);
	}
	
	
	private static android.view.View.OnClickListener onComfirmClickListener = new android.view.View.OnClickListener() {
		
		@Override
		public void onClick(View view) {
			//退出应用发送用户行为
			Map<String, String> map = UserBehaviorInfo.sendUserOpenAppInfo();
			map.put("operateType", "025");
			map.put("operateObjID", "");
			map.put(ParentHandlerService.URL, RequestURL.postUserbehaviorPhoneInfo());
			Task task = new Task(TaskID.TASK_POST_USERBEHAVIORINFO, map, MobileApplication.mobileApplication
					.getClass().getName(), "用户退出行为");
			MobileApplication.poolManager.addTask(task);
			MobileApplication.exitApp(view.getContext());
			dialog.cancel();
		}
	};

}
