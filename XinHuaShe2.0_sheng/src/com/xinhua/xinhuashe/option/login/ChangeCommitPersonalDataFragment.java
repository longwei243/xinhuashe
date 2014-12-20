package com.xinhua.xinhuashe.option.login;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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

public class ChangeCommitPersonalDataFragment extends ParentFragment implements IActivity{

	private final int SHOW_PERSON_DATA = 0;
	private final int CHANGE_PERSON_DATA = 1;
	
	private static String age;		// 年龄;
	private static String nationality;// 名族;
	private static String nativePlace;// 籍贯;
	private static String politicalLandscape;// 政治面貌;
	private static String homePlace;// 居住地;
	private static String studentStatus;// 学籍
	private static String studentRecord;// 学历;
	private String specialty;//特长
	private String height;//身高
	private String weight;//体重
	private static String constellation;//星座;
	private Date born;//出生
	private static Integer marriage;//婚姻
	private String  birth;//生育
	
	private static String interest;//爱好;
	private static String profession=" ";//职业
	private static String workUnit=" ";//工作单位
	private static String section=" ";//部门
	private static String duty=" ";//职务
	private static String title=" ";//职称
	private static String career= " ";//专业
	private static String officeNumber=" ";//机构等级
	private static String nature=" ";//性格
	private static String school=" ";//毕业院校
	private static String family=" ";//家族
	private static String lifeArae=" ";//活动区域
	private static String chileNumber=" ";//子女人数
	private static String adress=" ";//地区
	private static String email=" ";//邮箱
	private static String phone=" ";//手机号
	
	private static EditText change_commit_personal_zhiye_et,change_commit_personal_gongwei_et,
							change_commit_personal_bumeng_et,change_commit_personal_zhiwu_et,
							change_commit_personal_zhicheng_et,change_commit_personal_zhuangye_et,
							change_commit_personal_jigoudengji_et,change_commit_personal_xingge_et,
							change_commit_personal_bixiao_et,change_commit_personal_jiazu_et,
							change_commit_personal_huoqu_et,change_commit_personal_childnum_et,
							change_commit_personal_phone_et,change_commit_personal_adress_et,
							change_commit_personal_email_et
							;
	private Button change_commit_personal_yes_bt,change_commit_personal_no_bt;
	private Handler handler = new Handler(new Handler.Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_PERSON_DATA:
				change_commit_personal_adress_et.setText(adress);
				change_commit_personal_phone_et.setText(phone);
				change_commit_personal_email_et.setText(email);
				change_commit_personal_zhiye_et.setText(profession);
				change_commit_personal_gongwei_et.setText(workUnit);
				change_commit_personal_bumeng_et.setText(section);
				change_commit_personal_zhiwu_et.setText(duty);
				change_commit_personal_zhicheng_et.setText(title);
				change_commit_personal_zhuangye_et.setText(career);
				change_commit_personal_jigoudengji_et.setText(officeNumber);
				change_commit_personal_xingge_et.setText(nature);
				change_commit_personal_bixiao_et.setText(school);
				change_commit_personal_jiazu_et.setText(family);
				change_commit_personal_huoqu_et.setText(lifeArae);
				if(chileNumber.equals("-1")){
					chileNumber=null;
				}
				change_commit_personal_childnum_et.setText(chileNumber);
				break;
			case CHANGE_PERSON_DATA:
			
				if(checkDataChange()){
					if(checkEmail()){
						if(emailFormat(change_commit_personal_email_et.getText().toString().trim())){
					Map<String, String> map = new HashMap<String, String>();
					map.put("profession", change_commit_personal_zhiye_et.getText().toString().trim());
					map.put("workUnit", change_commit_personal_gongwei_et.getText().toString().trim());
					map.put("section", change_commit_personal_bumeng_et.getText().toString().trim());
					map.put("duty", change_commit_personal_zhiwu_et.getText().toString().trim());
					map.put("career", change_commit_personal_zhuangye_et.getText().toString().trim());
					map.put("nature", change_commit_personal_xingge_et.getText().toString().trim());
					if(change_commit_personal_childnum_et.getText().toString().trim().equals("")){
						chileNumber="0";
					}
					map.put("chileNumber", chileNumber);
					map.put("school", change_commit_personal_bixiao_et.getText().toString().trim());
					map.put("title", "公务员");
					map.put("officeNumber", "一级部门");
					map.put("family", "family");
					map.put("lifeArae", "寿阳");
					map.put("code", RequestURL.areaCode);
					map.put("frontUserId", UserInfo.userId);
					map.put("adress",change_commit_personal_adress_et.getText().toString().trim());
					map.put("email",change_commit_personal_email_et.getText().toString().trim());
					map.put("phone",change_commit_personal_phone_et.getText().toString().trim());
					map.put(ParentHandlerService.URL,RequestURL.getmodifyUserApply());
					Task task = new Task(TaskID.TASK_MODIFYUSERAPPLY_PERSON_DATA, map, this.getClass().getName(), "修改个人资料申请");
					MobileApplication.poolManager.addTask(task);
						}else{
							Toast.makeText(SlidingMenuControlActivity.activity, "邮箱格式不正确", Toast.LENGTH_SHORT).show();
						}
					}else{
						Toast.makeText(SlidingMenuControlActivity.activity, "邮箱不能为空", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(SlidingMenuControlActivity.activity, "您没有修改任何信息", Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
			}
			return false;
		}
	});
	
	@Override
	protected void setupViews(View parentView) {
		change_commit_personal_phone_et = (EditText) parentView.findViewById(R.id.change_commit_personal_phone_et);
		change_commit_personal_adress_et = (EditText) parentView.findViewById(R.id.change_commit_personal_adress_et);
		change_commit_personal_email_et = (EditText) parentView.findViewById(R.id.change_commit_personal_email_et);
		change_commit_personal_zhiye_et = (EditText) parentView.findViewById(R.id.change_commit_personal_zhiye_et);
		change_commit_personal_gongwei_et = (EditText) parentView.findViewById(R.id.change_commit_personal_gongwei_et);
		change_commit_personal_bumeng_et = (EditText) parentView.findViewById(R.id.change_commit_personal_bumeng_et);
		change_commit_personal_zhiwu_et = (EditText) parentView.findViewById(R.id.change_commit_personal_zhiwu_et);
		change_commit_personal_zhicheng_et = (EditText) parentView.findViewById(R.id.change_commit_personal_zhicheng_et);
		change_commit_personal_zhuangye_et = (EditText) parentView.findViewById(R.id.change_commit_personal_zhuangye_et);
		change_commit_personal_jigoudengji_et = (EditText) parentView.findViewById(R.id.change_commit_personal_jigoudengji_et);
		change_commit_personal_xingge_et = (EditText) parentView.findViewById(R.id.change_commit_personal_xingge_et);
		change_commit_personal_bixiao_et = (EditText) parentView.findViewById(R.id.change_commit_personal_bixiao_et);
		change_commit_personal_jiazu_et = (EditText) parentView.findViewById(R.id.change_commit_personal_jiazu_et);
		change_commit_personal_huoqu_et = (EditText) parentView.findViewById(R.id.change_commit_personal_huoqu_et);
		change_commit_personal_childnum_et = (EditText) parentView.findViewById(R.id.change_commit_personal_childnum_et);
		change_commit_personal_yes_bt = (Button) parentView.findViewById(R.id.change_commit_personal_yes_bt);
		change_commit_personal_no_bt = (Button) parentView.findViewById(R.id.change_commit_personal_no_bt);
		change_commit_personal_yes_bt.setOnClickListener(onClickListener);
		change_commit_personal_no_bt.setOnClickListener(onClickListener);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		MobileApplication.allIActivity.add(this);
		threadTask();
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	private OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.change_commit_personal_yes_bt:
				Message msg = new Message();
				msg.what = CHANGE_PERSON_DATA;
				handler.sendMessage(msg);
				break;
			case R.id.change_commit_personal_no_bt:
				SlidingMenuControlActivity.activity.getSupportFragmentManager().popBackStackImmediate();
				break;

			default:
				break;
			}
		}
	};
	@Override
	public void init() {
		
	}

	@Override
	public void refresh(int taskID, Object... params) {
		loadingDialog.cancel();
		if(params!=null){
			if(params[0]!=null){
				Message msg  =  new Message();
				switch (taskID) {
				case TaskID.TASK_SHOW_PERSON_DATA:
					try {
						JSONObject jb = new JSONObject(params[0].toString());
						adress=jb.getString("adress");
						email=jb.getString("email");
						phone=jb.getString("phone");
						profession=jb.getString("profession");
						workUnit = jb.getString("workUnit");
						section=jb.getString("section");
						duty=jb.getString("duty");
						title=jb.getString("title");
						career=jb.getString("career");
						officeNumber=jb.getString("officeNumber");
						nature=jb.getString("nature");
						school=jb.getString("school");
						family=jb.getString("family");
						lifeArae=jb.getString("lifeArae");
						chileNumber=jb.getInt("chileNumber")+"";
						msg.what = SHOW_PERSON_DATA;
						handler.sendMessage(msg);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
				case TaskID.TASK_MODIFYUSERAPPLY_PERSON_DATA:
					JSONObject jb;
					try {
						jb = new JSONObject(params[0].toString());
						Toast.makeText(SlidingMenuControlActivity.activity, jb.getString("message"), Toast.LENGTH_SHORT).show();
						if(jb.getString("result").equals("success")){
							threadTask();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
					break;
				default:
					break;
				}
				
			}else{
				Toast.makeText(SlidingMenuControlActivity.activity, "连接服务器失败", Toast.LENGTH_SHORT).show();
			}
		}else{
			Toast.makeText(SlidingMenuControlActivity.activity, "连接服务器失败", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected int getLayoutId() {
		return R.layout.change_my_data_commit_layout;
	}

	

	@Override
	protected void initialized() {
		
	}

	@Override
	protected void threadTask() {
		loadingDialog.show();
		Map<String, String> mapShow = new HashMap<String, String>();
		mapShow.put(ParentHandlerService.URL, RequestURL.getShowUser());
		mapShow.put("frontUserId", UserInfo.userId);
		Task taskShowCommit = new Task(TaskID.TASK_SHOW_PERSON_DATA, mapShow, this.getClass().getName(), "用户自然信息展示");
		MobileApplication.poolManager.addTask(taskShowCommit);
	}
	@Override
	public void closeLoadingView() {
		
	}
	private boolean checkDataChange(){
		String [] dataold = new String[]{
				change_commit_personal_zhiye_et.getText().toString(),
				change_commit_personal_gongwei_et.getText().toString(),
				change_commit_personal_bumeng_et.getText().toString(),
				change_commit_personal_zhiwu_et.getText().toString(),
				change_commit_personal_zhuangye_et.getText().toString(),
				change_commit_personal_xingge_et.getText().toString(),
				change_commit_personal_bixiao_et.getText().toString(),
				change_commit_personal_adress_et.getText().toString(),
				change_commit_personal_email_et.getText().toString(),
				change_commit_personal_phone_et.getText().toString(),
		};
		String [] dataNew = new String[]{
				profession,workUnit,
				section,duty,
				career,nature,
				school,adress,
				email,phone
		};
		for (int i = 0; i < dataNew.length; i++) {
			if(!dataold[i].equals(dataNew[i])){
				System.out.println("-------i-------"+i);
				i=dataNew.length+1;
				return true;
			}
		}
		return false;
	}
	private boolean checkEmail(){
		if(change_commit_personal_email_et.getText().toString().trim().equals("")){
			
			return false;
		}
		return true;
	}
	/**  
	 * 验证输入的邮箱格式是否符合  
	 * @param email  
	 * @return 是否合法  
	 */  
	public static boolean emailFormat(String email)   
	{   
		boolean tag = true;   
		final String pattern1 = "^([a-z0-9A-Z]+[_-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";   
		final Pattern pattern = Pattern.compile(pattern1);   
		final Matcher mat = pattern.matcher(email);   
		if (!mat.find()) {   
			tag = false;   
		}   
		return tag;   
	}
}
