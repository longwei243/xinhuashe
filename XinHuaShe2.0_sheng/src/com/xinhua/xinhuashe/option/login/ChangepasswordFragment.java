package com.xinhua.xinhuashe.option.login;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.threadpool.IActivity;
import com.android.threadpool.Task;
import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.domain.UserInfo;
import com.xinhua.xinhuashe.option.SlidingMenuControlActivity;
import com.xinhua.xinhuashe.parentclass.ParentFragment;
import com.xinhua.xinhuashe.request.RequestURL;
import com.xinhua.xinhuashe.service.MobileApplication;
import com.xinhua.xinhuashe.service.ParentHandlerService;
import com.xinhua.xinhuashe.service.TaskID;
import com.xinhua.xinhuashe.view.ValidateCode;

public class ChangepasswordFragment extends ParentFragment implements IActivity{

	private EditText change_password_original_et,change_password_new_et,
	change_password_new_again_et,change_password_validateCode_et;
	private ImageView change_password_validateCode_iv;
	private TextView change_password_ChangevalidateCode_tv;
	private Button change_password_button;
	private String code;
	private static boolean isfirst=true;
	private ValidateCode vCode;
	
	private Activity activity;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		MobileApplication.allIActivity.add(this);
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	@Override
	protected int getLayoutId() {
		return R.layout.password_set;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity=activity;
	}
	@Override
	protected void setupViews(View parentView) {
		change_password_original_et=(EditText) parentView.findViewById(R.id.change_password_original_et);
		change_password_new_et=(EditText) parentView.findViewById(R.id.change_password_new_et);
		change_password_new_again_et=(EditText) parentView.findViewById(R.id.change_password_new_again_et);
		change_password_validateCode_et=(EditText) parentView.findViewById(R.id.change_password_validateCode_et);
		change_password_validateCode_iv= (ImageView) parentView.findViewById(R.id.change_password_validateCode_iv);
		change_password_ChangevalidateCode_tv= (TextView) parentView.findViewById(R.id.change_password_ChangevalidateCode_tv);
		change_password_button= (Button) parentView.findViewById(R.id.change_password_button);
//		change_password_button.setEnabled(false);
		change_password_ChangevalidateCode_tv.setOnClickListener(onclicklistener);
//		change_password_button.setBackgroundColor(getResources().getColor(R.color.gray));
		change_password_button.setOnClickListener(onclicklistener);
//		change_password_new_again_et.setOnClickListener(onclicklistener);
//		change_password_new_et.setOnClickListener(onclicklistener);
//		change_password_validateCode_et.setOnClickListener(onclicklistener);
	}
    private   OnClickListener  onclicklistener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.change_password_button:
				code = vCode.getCode();
				if(checkpassword(change_password_new_et.getText().toString().trim(), 
						change_password_new_again_et.getText().toString().trim())){
					if (checkCode(change_password_validateCode_et.getText().toString().trim())) {
						Map<String, String> mapNewPassword = new HashMap<String, String>();
						mapNewPassword.put("oldPassword",change_password_original_et.getText().toString().trim());
						mapNewPassword.put("newPassword", change_password_new_et.getText().toString().trim());
						mapNewPassword.put("frontUserId", UserInfo.userId);
						mapNewPassword.put(ParentHandlerService.URL, RequestURL.setPassword());
						Task tasknewPassword = new Task(TaskID.TASK_CHANGEPASSWORD_NEWPASSWORD, mapNewPassword, this.getClass().getName(), "设置新密码");
						MobileApplication.poolManager.addTask(tasknewPassword);
					}
				}
				break;
			case R.id.change_password_ChangevalidateCode_tv:
				vCode = new ValidateCode(80, 40, 4, 5);
				Bitmap bitmap = vCode.getBitmap();
				change_password_validateCode_iv.setImageBitmap(bitmap);
				break;
			default:
				break;
			}
		}
	};
	@Override
	protected void initialized() {
		vCode = new ValidateCode(80, 40, 4, 5);
		Bitmap bitmap = vCode.getBitmap();
		change_password_validateCode_iv.setImageBitmap(bitmap);
		SlidingMenuControlActivity.main_header_title_TextView.setText("修改密码");
		
	}
	public Boolean checkCode(String etCode){
		if(!code.equals(etCode)||etCode.equals("")){
			Toast.makeText(SlidingMenuControlActivity.activity, "验证码输入不正确", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	public Boolean checkpassword(String oldp,String newp){
	   if(!oldp.equals("")){
		   if(!newp.equals("")){
			  if(oldp.equals(newp)){
				  return true;
			  } else{
				  Toast.makeText(SlidingMenuControlActivity.activity, "两次密码不一致", Toast.LENGTH_SHORT).show();
				  return false;
			  }
		   }else{
			   Toast.makeText(SlidingMenuControlActivity.activity, "请再次输入密码", Toast.LENGTH_SHORT).show();
			   return false;
		   }
	   }else{
		   Toast.makeText(SlidingMenuControlActivity.activity, "请输入新密码", Toast.LENGTH_SHORT).show();
		   return false;
	   }
	}
	@Override
	protected void threadTask() {
		// TODO Auto-generated method stub

	}

	@Override
	public void closeLoadingView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refresh(int TaskId, Object... params) {
		ParentFragment fragment =null;
		if(params!=null){
			if(params[0]!=null){
				switch (TaskId) {
				case TaskID.TASK_CHANGEPASSWORD_NEWPASSWORD:

					try {
						JSONObject jb = new JSONObject(params[0].toString());
						String message = jb.getString("message");
						Toast.makeText(SlidingMenuControlActivity.activity, 
								message, Toast.LENGTH_SHORT).show();
						if(jb.getString("result").equals("success")){
							SlidingMenuControlActivity.activity.getSupportFragmentManager().popBackStackImmediate();
						}
						change_password_button.setEnabled(true);
						if(isAdded()){
						change_password_button.setBackgroundColor(getResources().getColor(R.color.news_blue));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;

				default:
					break;
				}
			}
			else{
				loadingDialog.showTimeOut();
			}
		}else{
			loadingDialog.showTimeOut();
		}
	}
}
