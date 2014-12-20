package com.xinhua.xinhuashe.option.login;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
/**
 * 
 * @author fanxiaole
 *
 */
public class ChangePersonalDataFragment extends ParentFragment implements IActivity{

	private static EditText change_personal_data_age_et,
	change_personal_data_jiguan_et,change_personal_data_zhengmao_et,
	change_personal_data_juzhudi_et,change_personal_data_xueli_et,
	change_personal_data_xingzuo_et,change_personal_data_nation_et,
	change_personal_data_marriage_et,change_personal_data_hobby_et;
	private Button change_personal_data_yes_bt,change_personal_data_no_bt;
	private String[] sbNew;
	private String[] sbOld;
	private JSONObject jb;
	
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
	private String profession;//职业
	private String section;//部门
	private String duty;//职务
	private String title;//职称
	private String career;//专业
	private String officeNumber;//机构等级
	private String nature;//性格
	private String school;//毕业院校
	private String family;//家族
	private String lifeArae;//活动区域
	private Integer chileNumber;//子女人数

	
	private Handler handler = new Handler(new Handler.Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_PERSON_DATA:
				if(age.equals("-1")){
					change_personal_data_age_et.setText("");	
				}else{
					change_personal_data_age_et.setText(age);
				}
				change_personal_data_jiguan_et.setText(nativePlace);
				change_personal_data_zhengmao_et.setText(politicalLandscape);
				change_personal_data_juzhudi_et.setText(homePlace);
				change_personal_data_xueli_et.setText(studentRecord);
				change_personal_data_xingzuo_et.setText(constellation);
				change_personal_data_nation_et.setText(nationality);
				change_personal_data_hobby_et.setText(interest);
				break;
			case CHANGE_PERSON_DATA:
				if (checkDataChange()) {
					Map<String, String> mapChange = new HashMap<String, String>();
					mapChange.put(ParentHandlerService.URL, RequestURL.getmodifyUser());
					if(change_personal_data_age_et.getText().toString().trim().equals("")){
						mapChange.put("age", "-1");
					}else{
					mapChange.put("age", change_personal_data_age_et.getText().toString().trim());
					}
					mapChange.put("nativePlace", change_personal_data_jiguan_et.getText().toString().trim());
					mapChange.put("politicalLandscape", change_personal_data_zhengmao_et.getText().toString().trim());
					mapChange.put("homePlace", change_personal_data_juzhudi_et.getText().toString().trim());
					mapChange.put("studentRecord", change_personal_data_xueli_et.getText().toString().trim());
					mapChange.put("constellation", change_personal_data_xingzuo_et.getText().toString().trim());
					mapChange.put("nationality", change_personal_data_nation_et.getText().toString().trim());
					mapChange.put("interest", change_personal_data_hobby_et.getText().toString().trim());
					mapChange.put("marriage", "1");
					mapChange.put("frontUserId", UserInfo.userId);
					System.out.println("----------------"+mapChange.size());
					Task taskchange = new Task(TaskID.TASK_CHANGE_PERSON_DATA, mapChange, this.getClass().getName(), "自然属性修改");
					MobileApplication.poolManager.addTask(taskchange);
					
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		MobileApplication.allIActivity.add(this);
		threadTask();
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	@Override
	protected int getLayoutId() {
		return R.layout.change_my_data_layout;
	}

	@Override
	protected void setupViews(View parentView) {
		change_personal_data_age_et =(EditText) parentView.findViewById(R.id.change_personal_data_age_et);
		change_personal_data_jiguan_et =(EditText) parentView.findViewById(R.id.change_personal_data_jiguan_et);
		change_personal_data_zhengmao_et =(EditText) parentView.findViewById(R.id.change_personal_data_zhengmao_et);
		change_personal_data_juzhudi_et =(EditText) parentView.findViewById(R.id.change_personal_data_juzhudi_et);
		change_personal_data_xueli_et =(EditText) parentView.findViewById(R.id.change_personal_data_xueli_et);
		change_personal_data_xingzuo_et =(EditText) parentView.findViewById(R.id.change_personal_data_xingzuo_et);
		change_personal_data_nation_et =(EditText) parentView.findViewById(R.id.change_personal_data_nation_et);
		change_personal_data_marriage_et =(EditText) parentView.findViewById(R.id.change_personal_data_marriage_et);
		change_personal_data_hobby_et =(EditText) parentView.findViewById(R.id.change_personal_data_hobby_et);
		change_personal_data_yes_bt=(Button) parentView.findViewById(R.id.change_personal_data_yes_bt);
		change_personal_data_no_bt=(Button) parentView.findViewById(R.id.change_personal_data_no_bt);
		change_personal_data_yes_bt.setOnClickListener(onclickListener);
		change_personal_data_no_bt.setOnClickListener(onclickListener);
	}
	private OnClickListener onclickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			ParentFragment fragment = null;
			switch (v.getId()) {
			case R.id.change_personal_data_yes_bt:
				//确认修改个人资料
				Message msg = new Message();
				msg.what = CHANGE_PERSON_DATA;
				handler.sendMessage(msg);
				break;
			case R.id.change_personal_data_no_bt:
				SlidingMenuControlActivity.activity.getSupportFragmentManager().popBackStackImmediate();
				break;
			default:
				break;
			}
			
		}
	};

	@Override
	protected void initialized() {

	}

	@Override
	protected void threadTask() {
		loadingDialog.show();
		Map<String, String> mapShow = new HashMap<String, String>();
		mapShow.put(ParentHandlerService.URL, RequestURL.getShowUser());
		mapShow.put("frontUserId", UserInfo.userId);
		Task taskShow = new Task(TaskID.TASK_SHOW_PERSON_DATA, mapShow, this.getClass().getName(), "用户自然信息展示");
		MobileApplication.poolManager.addTask(taskShow);
	}

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
			if (params[0]!=null) {
				Message msg = new Message();
				
				switch (taskID) {
				case TaskID.TASK_SHOW_PERSON_DATA:
					try {
						jb = new JSONObject(params[0].toString());
						age = jb.getInt("age")+"";
						if(age==0+""){
							age="";
						}
						nationality = jb.getString("nationality");
						nativePlace = jb.getString("nativePlace");
						politicalLandscape = jb.getString("politicalLandscape");
						homePlace  = jb.getString("homePlace");
						studentRecord = jb.getString("studentRecord");
						constellation = jb.getString("constellation");
						interest  = jb.getString("interest");
						msg.what =  SHOW_PERSON_DATA;
						handler.sendMessage(msg);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
				case TaskID.TASK_CHANGE_PERSON_DATA:
					try {
						jb = new JSONObject(params[0].toString());
						Toast.makeText(SlidingMenuControlActivity.activity, jb.getString("message"), Toast.LENGTH_SHORT).show();
						if(jb.getString("result").equals("success")){
							threadTask();
						}
					} catch (JSONException e) {
						Toast.makeText(SlidingMenuControlActivity.activity, "服务器连接失败", Toast.LENGTH_SHORT).show();
					}
					break;
				default:
					break;
				}
			} else {
				Toast.makeText(SlidingMenuControlActivity.activity, "连接服务器失败", Toast.LENGTH_SHORT).show();
			}
		} else {
				Toast.makeText(SlidingMenuControlActivity.activity, "连接服务器失败", Toast.LENGTH_SHORT).show();
		}
	}

	private boolean checkDataChange(){
		//获得用户修改数据
		sbOld = new String[]{
				change_personal_data_age_et.getText().toString()
				,change_personal_data_jiguan_et.getText().toString()
				,change_personal_data_zhengmao_et.getText().toString()
				,change_personal_data_juzhudi_et.getText().toString()
				,change_personal_data_xueli_et.getText().toString()
				,change_personal_data_xingzuo_et.getText().toString()
				,change_personal_data_nation_et.getText().toString()
				//+change_personal_data_marriage_et.getText().toString()
				,change_personal_data_hobby_et.getText().toString()
		};
		//服务器返回数据
		sbNew=new String[]{age,nativePlace,politicalLandscape,homePlace,studentRecord,constellation,nationality,interest};
		for (int i = 0; i < sbOld.length; i++) {
			if(!sbOld[i].equals(sbNew[i])){
				i=sbOld.length+1;
				return true;
			}
		}
		return false;
	}
	private String subStr(String str){
		return str.substring(0, str.length()-1);
	}
}
