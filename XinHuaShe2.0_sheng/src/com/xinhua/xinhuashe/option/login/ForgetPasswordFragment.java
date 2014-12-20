package com.xinhua.xinhuashe.option.login;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

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
import com.xinhua.xinhuashe.option.SlidingMenuControlActivity;
import com.xinhua.xinhuashe.parentclass.ParentFragment;
import com.xinhua.xinhuashe.request.RequestURL;
import com.xinhua.xinhuashe.service.MobileApplication;
import com.xinhua.xinhuashe.service.ParentHandlerService;
import com.xinhua.xinhuashe.service.TaskID;
import com.xinhua.xinhuashe.view.ValidateCode;

/**
 * 忘记密码
 * @author fanxiaole
 *
 */
public class ForgetPasswordFragment extends ParentFragment implements IActivity {
	
	private String code;
	private ValidateCode vCode;
	private TextView find_password_change_verification_code_tv;
	private EditText find_password_email_et,find_password_verification_code_et,find_password_username_et;
	private ImageView find_password_verification_code_iv;
	private Button find_password_button;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		MobileApplication.allIActivity.add(this);
		return super.onCreateView(inflater, container, savedInstanceState);
		
	}
	@Override
	protected int getLayoutId() {
		return R.layout.password_find;
	}

	@Override
	protected void setupViews(View parentView) {
		find_password_username_et  = (EditText) parentView.findViewById(R.id.find_password_username_et);
		find_password_change_verification_code_tv =	(TextView) parentView.findViewById(R.id.find_password_change_verification_code_tv);
		find_password_email_et=(EditText) parentView.findViewById(R.id.find_password_email_et);
		find_password_verification_code_et=(EditText) parentView.findViewById(R.id.find_password_verification_code_et);
		find_password_verification_code_iv=(ImageView) parentView.findViewById(R.id.find_password_verification_code_iv);
		find_password_button = (Button) parentView.findViewById(R.id.find_password_button);
		find_password_change_verification_code_tv.setOnClickListener(onclickListener);
		find_password_button.setOnClickListener(onclickListener);
	}
	private OnClickListener onclickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.find_password_change_verification_code_tv:
				vCode = new ValidateCode(80, 40, 4, 5);
				Bitmap bitmap = vCode.getBitmap();
				find_password_verification_code_iv.setImageBitmap(bitmap);
				break;
			case R.id.find_password_button:
				code = vCode.getCode();
				if(checkFindPassword()){
					loadingDialog.show();
					//发送请求
					Map<String, String> map = new HashMap<String, String>();
					map.put("frontUserName", find_password_username_et.getText().toString().trim());
					map.put("email", find_password_email_et.getText().toString().trim());
					map.put(ParentHandlerService.URL, RequestURL.findPassword());
					Task task = new Task(TaskID.TASK_FIND_PASSWORD, map, this.getClass().getName(), "忘记密码");
					MobileApplication.poolManager.addTask(task);
				}
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
		find_password_verification_code_iv.setImageBitmap(bitmap);
		SlidingMenuControlActivity.main_header_title_TextView.setText("忘记密码");
	}

	@Override
	protected void threadTask() {

	}

	@Override
	public void closeLoadingView() {
		
	}

	@Override
	public void init() {
		
	}

	@Override
	public void refresh(int taskId, Object... params) {
		loadingDialog.cancel();
		if(params!=null){
			if (params[0]!=null) {
				try {
					JSONObject jb = new JSONObject(params[0].toString());
					Toast.makeText(SlidingMenuControlActivity.activity, jb.getString("message"), Toast.LENGTH_SHORT).show();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else{
			Toast.makeText(SlidingMenuControlActivity.activity, R.string.request_timeout, Toast.LENGTH_SHORT).show();

		}
	}
	private boolean checkFindPassword(){
		if(!find_password_email_et.getText().toString().trim().equals("")){
			if(ChangeCommitPersonalDataFragment.emailFormat(find_password_email_et.getText().toString().trim())){
				if(!find_password_username_et.getText().toString().trim().equals("")){
					if(find_password_verification_code_et.getText().toString().trim().equals(code)){
						return true;
					}else{
						Toast.makeText(SlidingMenuControlActivity.activity, "验证码输入错误", Toast.LENGTH_SHORT).show();
					}
				}
			}else{
				Toast.makeText(SlidingMenuControlActivity.activity, "邮箱格式不正确", Toast.LENGTH_SHORT).show();
				return false;
			}
		}else{
			Toast.makeText(SlidingMenuControlActivity.activity, "邮箱不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		return false;
	}

}