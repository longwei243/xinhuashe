package com.xinhua.xinhuashe.option.login;
/**
 * 用户登录
 * 
 * @author fanxiaole
 * @since  2014/7/13
 * 
 */
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.threadpool.IActivity;
import com.android.threadpool.Task;
import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.domain.UserInfo;
import com.xinhua.xinhuashe.option.LeftMenuFragment;
import com.xinhua.xinhuashe.option.SlidingMenuControlActivity;
import com.xinhua.xinhuashe.option.homepage.HomepageFragment;
import com.xinhua.xinhuashe.parentclass.ParentFragment;
import com.xinhua.xinhuashe.request.RequestURL;
import com.xinhua.xinhuashe.service.MobileApplication;
import com.xinhua.xinhuashe.service.ParentHandlerService;
import com.xinhua.xinhuashe.service.TaskID;
import com.xinhua.xinhuashe.util.UserBehaviorInfo;

public class LoginFragment extends ParentFragment implements IActivity {
	
	public static SharedPreferences preferences;
	private EditText login_username_ediText,login_password_ediText;
	private TextView forget_password_textView,immediately_register_textView;
	private Button login_login_button;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		preferences = getActivity().getSharedPreferences(MobileApplication.TAG,
				Context.MODE_PRIVATE);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		MobileApplication.allIActivity.add(this);
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	

	@Override
	protected void setupViews(View parentView) {
		login_username_ediText = (EditText) parentView.findViewById(R.id.login_username);
		login_password_ediText = (EditText) parentView.findViewById(R.id.login_password);
		forget_password_textView = (TextView) parentView.findViewById(R.id.forget_password_textView);
		immediately_register_textView = (TextView) parentView.findViewById(R.id.immediately_register_textView);
		login_login_button = (Button) parentView.findViewById(R.id.login_logining_Button);
		forget_password_textView.setOnClickListener(onClickListener);
		immediately_register_textView.setOnClickListener(onClickListener);
		login_login_button.setOnClickListener(onClickListener);
	}
	
	
	private OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			ParentFragment fragment = null;
			switch (v.getId()) {
			case R.id.forget_password_textView:
				//忘记密码
				 fragment = new ForgetPasswordFragment();
				 System.out.println("点击忘记密码");
				break;
			case R.id.immediately_register_textView:
				//马上注册
				 System.out.println("点击注册");
				fragment  = new RegisterFragment();
				break;
			case R.id.login_logining_Button:
				//登录
				if(checkEmpty()){
					Map<String, String>  mapLogin  = new HashMap<String, String>();
					mapLogin.put("name", login_username_ediText.getText().toString().trim());
					mapLogin.put("password", login_password_ediText.getText().toString().trim());
					mapLogin.put(ParentHandlerService.URL, RequestURL.getLogin());
					loadingDialog.show();
					Task taskLogin = new Task(TaskID.TASK_LOGIN, mapLogin, this.getClass().getName(), "用户登录");
					MobileApplication.poolManager.addTask(taskLogin);
				}
				break;

			default:
				break;
			}
			if(v.getId()!=R.id.login_logining_Button){
			switchFragment(fragment, fragment.getClass().getSimpleName(),params);
			}
		}
	};
	@Override
	public void closeLoadingView() {
		
	}

	@Override
	public void init() {
		
	}

	@Override
	public void refresh(int taskID, Object... params) {
		loadingDialog.cancel();
		if (params != null) {
			if (params[0] != null) {
				switch (taskID) {
				case TaskID.TASK_LOGIN:
					try {
						JSONObject jsonObject = new JSONObject(
								params[0].toString());
						JSONObject result = new JSONObject(
								jsonObject.getString("message"));
						String loginState = result.getString("result");
						if ("error".equals(loginState)) {
							Toast.makeText(SlidingMenuControlActivity.activity,
									result.getString("message"),
									Toast.LENGTH_SHORT).show();
						} else if ("success".equals(loginState)) {
							//登陆之后再次登陆在线  不能写在下面
//							MobileApplication.cacheUtils.clear();
							Toast.makeText(SlidingMenuControlActivity.activity,
									R.string.login_success, Toast.LENGTH_SHORT)
									.show();
							SlidingMenuControlActivity.activity.getSupportFragmentManager().popBackStackImmediate();
							JSONObject userinfo = jsonObject
									.getJSONObject("vo");
							UserInfo.userId = userinfo.getString("id");
							preferences.edit().putString("id", UserInfo.userId)
									.commit();
							UserInfo.groupIdss=userinfo.getString("groupIdss");
							UserInfo.userName = userinfo.getString("name");
							MobileApplication.cacheUtils.put("id", UserInfo.userId);
							MobileApplication.cacheUtils.put("username", UserInfo.userName);
							MobileApplication.cacheUtils.put("groupIdss", UserInfo.groupIdss);
							// UserInfoService.jsonObject =
							// jsonObject.getJSONObject("users");
							LeftMenuFragment.leftmenu_username_TextView
							.setText(UserInfo.userName);
							//--用户行为
							Map<String, String> map1 =UserBehaviorInfo.sendUserOpenAppInfo();
							map1.put("operateType", "015");
							map1.put("operateObjID", "");
							map1.put(ParentHandlerService.URL, RequestURL.postUserbehaviorPhoneInfo());
							Task task1 = new Task(TaskID.TASK_POST_USERBEHAVIORINFO, map1, this
									.getClass().getName(), "用户登录行为");
							MobileApplication.poolManager.addTask(task1);
							//
							/*// -- 刷新栏目列表
							Map<String, String> map = new HashMap<String, String>();
							map.put(ParentHandlerService.URL,
									RequestURL.getAllColumnsList());
							map.put("areaCode", RequestURL.areaCode);
							map.put("userId", UserInfo.userId);
							Task task = new Task(TaskID.TASK_COLUMN_INIT, map,
									this.getClass().getName(), "-获取栏目信息-");
							//loadingDialog.show();
//							MobileApplication.poolManager.addTask(task);
*/						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
				default:
					break;
				}
			} else {
				loadingDialog.showTimeOut();
			}
		} else {
			loadingDialog.showTimeOut();
		}}

	@Override
	protected int getLayoutId() {
		return R.layout.login;
	}
	@Override
	protected void initialized() {
		SlidingMenuControlActivity.main_header_title_TextView.setText("登录");
	}

	@Override
	protected void threadTask() {

	}
	private boolean checkEmpty() {
		if ("".equals(login_username_ediText.getText().toString().trim())) {
			Toast.makeText(getActivity(), R.string.account, Toast.LENGTH_SHORT)
					.show();
			//login_username_ediText.startAnimation(shake);
		} else {
			if ("".equals(login_password_ediText.getText().toString().trim())) {
				Toast.makeText(getActivity(), R.string.empty_password,
						Toast.LENGTH_SHORT).show();
				//login_password_ediText.startAnimation(shake);
			} else {
				return true;
			}
		}
		return false;
	}

}
