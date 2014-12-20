package com.xinhua.xinhuashe.option.login;

import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.android.threadpool.Task;
import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.domain.UserInfo;
import com.xinhua.xinhuashe.option.LeftMenuFragment;
import com.xinhua.xinhuashe.option.SlidingMenuControlActivity;
import com.xinhua.xinhuashe.request.RequestURL;
import com.xinhua.xinhuashe.service.MobileApplication;
import com.xinhua.xinhuashe.service.ParentHandlerService;
import com.xinhua.xinhuashe.service.TaskID;
import com.xinhua.xinhuashe.util.UserBehaviorInfo;
import com.xinhua.xinhuashe.view.dialog.LoadingDialog;

/**
 * 注销
 * 
 * @author azuryleaves
 * @since 2014-1-3 下午12:14:29
 * @version 1.0
 * 
 */
public class LogoutActivity extends Activity {

	private Button parentdialog_cancel_Button, parentdialog_confirm_Button;
	private LoadingDialog loadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MobileApplication.allActivity.add(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logout);
		parentdialog_cancel_Button = (Button) findViewById(R.id.parentdialog_cancel_Button);
		parentdialog_cancel_Button.setOnClickListener(clickListener);
		parentdialog_confirm_Button = (Button) findViewById(R.id.parentdialog_confirm_Button);
		parentdialog_confirm_Button.setOnClickListener(clickListener);
		loadingDialog = new LoadingDialog(this, clickListener);
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.parentdialog_cancel_Button:
				finish();
				break;
			case R.id.parentdialog_confirm_Button:
				
				if(UserInfo.userId==""){
					Toast.makeText(SlidingMenuControlActivity.activity, R.string.nologin, Toast.LENGTH_SHORT).show();
					finish();
					return;
				}
				Map<String, String> map =UserBehaviorInfo.sendUserOpenAppInfo();
				map.put("operateType", "016");
				map.put("operateObjID", "");
				map.put(ParentHandlerService.URL, RequestURL.postUserbehaviorPhoneInfo());
				Task task = new Task(TaskID.TASK_POST_USERBEHAVIORINFO, map, this
						.getClass().getName(), "用户注销行为");
				MobileApplication.poolManager.addTask(task);
				UserInfo.userId = "";
				UserInfo.userName = "";
				LeftMenuFragment.leftmenu_username_TextView.setText(R.string.leftmenu_login);
				MobileApplication.cacheUtils.remove("id");
				MobileApplication.cacheUtils.remove("username");
				Toast.makeText(LogoutActivity.this, "注销成功", Toast.LENGTH_SHORT).show();
				finish();
				
				break;
			case R.id.loadingdialog_cancel_ImageView:
				loadingDialog.cancel();
				break;
			default:
				break;
			}
		}
	};

}
