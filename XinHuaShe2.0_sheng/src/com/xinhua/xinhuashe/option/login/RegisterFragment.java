package com.xinhua.xinhuashe.option.login;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.threadpool.IActivity;
import com.android.threadpool.Task;
import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.option.SlidingMenuControlActivity;
import com.xinhua.xinhuashe.option.login.city_cn.DBManager;
import com.xinhua.xinhuashe.option.login.city_cn.MyAdapter;
import com.xinhua.xinhuashe.option.login.city_cn.MyListItem;
import com.xinhua.xinhuashe.parentclass.ParentFragment;
import com.xinhua.xinhuashe.request.RequestURL;
import com.xinhua.xinhuashe.service.MobileApplication;
import com.xinhua.xinhuashe.service.ParentHandlerService;
import com.xinhua.xinhuashe.service.TaskID;
import com.xinhua.xinhuashe.util.TelManager;
import com.xinhua.xinhuashe.util.UserBehaviorInfo;

/**
 * 注册
 * @author fanxiaole
 */
public class RegisterFragment extends ParentFragment implements IActivity {
	
	private TextView register_common_textView,register_real_textView ;
	private LinearLayout register_layout_putong,register_layout_shiming;
	private EditText register_common_username_et,register_common_password_et,
	register_common_againpassword_et,register_common_real_name_et,register_common_phonenumber_et,
	register_common_email_et;
	private TextView register_common_username_prompt_tv,register_common_password_prompt_tv,
	register_common_againpassword_prompt_tv;
	private Button register_common_button,register_real_immediately_register_bt;
	private EditText register_real_username_et,register_real_password_et,register_real_again_password_et,
	register_real_name_et,register_real_job_et,register_real_workspace_et,register_real_phonenumber_et,
	register_real_email_et,register_real_personal_number_et;
	private TextView register_real_username_prompt_tv,register_real_password_prompt_tv,register_real_again_password_prompt_tv,
	register_real_name_prompt_tv,register_real_job_prompt_tv,register_real_phonenumber_prompt_tv,
	register_real_email_prompt_tv,register_real_personal_number_prompt_tv;
	private RadioGroup register_real_gender_RadioGroup;
	private RadioButton register_real_male_RadioButton,register_real_female_RadioButton;
	private Spinner register_real_space_sheng_spinner,register_real_space_shi_spinner,register_real_space_xian_spinner;
	private Animation shake;
	private String genderType = "0";
	private DBManager dbm;
	private SQLiteDatabase db;
	private String province=null;
	private String city=null;
	private String district=null;
	private Activity activity;
private Handler handler = new Handler(new Handler.Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (isAdded()){
				register_real_immediately_register_bt.setBackgroundColor(getResources().getColor(R.color.news_blue));
				}
			    register_real_immediately_register_bt.setEnabled(true);
			    register_common_button.setBackgroundColor(SlidingMenuControlActivity.activity.getResources().getColor(R.color.news_blue));
			    register_common_button.setEnabled(true);
			    SlidingMenuControlActivity.activity.getSupportFragmentManager().popBackStackImmediate();
			    Map<String, String> map1 =UserBehaviorInfo.sendUserOpenAppInfo();
				map1.put("operateType", "023");
				map1.put("operateObjID", "");
				map1.put(ParentHandlerService.URL, RequestURL.postUserbehaviorPhoneInfo());
				Task task1 = new Task(TaskID.TASK_POST_USERBEHAVIORINFO, map1, this
						.getClass().getName(), "用户注册行为");
				MobileApplication.poolManager.addTask(task1);
			    break;
			case 2:
				if (isAdded()){
				register_real_immediately_register_bt.setBackgroundColor(SlidingMenuControlActivity.activity.getResources().getColor(R.color.news_blue));
				}
			    register_real_immediately_register_bt.setEnabled(true);
			    register_common_button.setBackgroundColor(SlidingMenuControlActivity.activity.getResources().getColor(R.color.news_blue));
			    register_common_button.setEnabled(true);
				break;
			case 3:
				if (isAdded()){
				register_common_button.setBackgroundColor(SlidingMenuControlActivity.activity.getResources().getColor(R.color.news_blue));
				}
			    register_common_button.setEnabled(true);
				break;
			default:
				break;
			}
			return false;
		}
	});

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
		return R.layout.register;
	}

	@Override
	protected void setupViews(View parentView) {
		//linearlayout
		register_layout_putong=(LinearLayout)parentView.findViewById(R.id.register_layout_putong);
		register_layout_shiming=(LinearLayout)parentView.findViewById(R.id.register_layout_shiming);
		//实名  普通注册按钮
		register_common_textView = (TextView) parentView.findViewById(R.id.register_common_textView);
		register_real_textView = (TextView) parentView.findViewById(R.id.register_real_textView);
		//普通注册  
		register_common_username_et =(EditText) parentView.findViewById(R.id.register_common_username_et);
		register_common_password_et =(EditText) parentView.findViewById(R.id.register_common_password_et);
		register_common_againpassword_et =(EditText) parentView.findViewById(R.id.register_common_againpassword_et);
		register_common_real_name_et =(EditText) parentView.findViewById(R.id.register_common_real_name_et);
		register_common_phonenumber_et =(EditText) parentView.findViewById(R.id.register_common_phonenumber_et);
		register_common_email_et =(EditText) parentView.findViewById(R.id.register_common_email_et);
		register_common_username_prompt_tv=(TextView) parentView.findViewById(R.id.register_common_username_prompt_tv);
		register_common_password_prompt_tv=(TextView) parentView.findViewById(R.id.register_common_password_prompt_tv);
		register_common_againpassword_prompt_tv=(TextView) parentView.findViewById(R.id.register_common_againpassword_prompt_tv);
		
		//实名注册
		register_real_username_et=(EditText) parentView.findViewById(R.id.register_real_username_et);
		register_real_password_et=(EditText) parentView.findViewById(R.id.register_real_password_et);
		register_real_again_password_et=(EditText) parentView.findViewById(R.id.register_real_again_password_et);
		register_real_name_et=(EditText) parentView.findViewById(R.id.register_real_name_et);
		register_real_job_et=(EditText) parentView.findViewById(R.id.register_real_job_et);
		register_real_workspace_et=(EditText) parentView.findViewById(R.id.register_real_workspace_et);
		register_real_email_et=(EditText) parentView.findViewById(R.id.register_real_email_et);
		register_real_phonenumber_et=(EditText) parentView.findViewById(R.id.register_real_phonenumber_et);
		register_real_personal_number_et=(EditText) parentView.findViewById(R.id.register_real_personal_number_et);
		register_real_gender_RadioGroup=(RadioGroup) parentView.findViewById(R.id.register_real_gender_RadioGroup);
		register_real_male_RadioButton=(RadioButton) parentView.findViewById(R.id.register_real_male_RadioButton);
		register_real_female_RadioButton=(RadioButton) parentView.findViewById(R.id.register_real_female_RadioButton);
		register_real_space_sheng_spinner=(Spinner) parentView.findViewById(R.id.register_real_space_sheng_spinner);
		register_real_space_shi_spinner=(Spinner) parentView.findViewById(R.id.register_real_space_shi_spinner);
		register_real_space_xian_spinner=(Spinner) parentView.findViewById(R.id.register_real_space_xian_spinner);
		
		register_real_username_prompt_tv=(TextView) parentView.findViewById(R.id.register_real_username_prompt_tv);
		register_real_password_prompt_tv=(TextView) parentView.findViewById(R.id.register_real_password_prompt_tv);
		register_real_again_password_prompt_tv=(TextView) parentView.findViewById(R.id.register_real_again_password_prompt_tv);
		register_real_name_prompt_tv=(TextView) parentView.findViewById(R.id.register_real_name_prompt_tv);
		register_real_job_prompt_tv=(TextView) parentView.findViewById(R.id.register_real_job_prompt_tv);
		register_real_phonenumber_prompt_tv=(TextView) parentView.findViewById(R.id.register_real_phonenumber_prompt_tv);
		register_real_email_prompt_tv=(TextView) parentView.findViewById(R.id.register_real_email_prompt_tv);
		register_real_personal_number_prompt_tv=(TextView) parentView.findViewById(R.id.register_real_personal_number_prompt_tv);
		//普通  实名注册按钮
		register_common_button = (Button) parentView.findViewById(R.id.register_common_button);
		register_real_immediately_register_bt = (Button) parentView.findViewById(R.id.register_real_immediately_register_bt);
		//省市县
		register_real_space_sheng_spinner.setPrompt("省");
		register_real_space_shi_spinner.setPrompt("地区");
		register_real_space_xian_spinner.setPrompt("县区");
		register_common_textView.setOnClickListener(onClickListener);
		register_real_textView.setOnClickListener(onClickListener);
		register_common_button.setOnClickListener(onClickListener);
		register_real_immediately_register_bt.setOnClickListener(onClickListener);
		register_layout_putong.setVisibility(View.VISIBLE);
		register_layout_shiming.setVisibility(View.GONE);
		
		register_real_immediately_register_bt.setBackgroundColor(SlidingMenuControlActivity.activity.getResources().getColor(R.color.news_blue));
		register_common_button.setBackgroundDrawable(SlidingMenuControlActivity.activity.getResources().getDrawable(R.color.news_blue));

		register_real_textView.setBackgroundDrawable(SlidingMenuControlActivity.activity.getResources().getDrawable(R.drawable.register_textview_noonclick));
		register_common_textView.setBackgroundDrawable(SlidingMenuControlActivity.activity.getResources().getDrawable(R.drawable.register_text_bg));
		initSpinner1();

	}
	
	private OnClickListener onClickListener  = new  OnClickListener() {
		
		@Override
		public void onClick(View v) {
			double[] db = MobileApplication.db;
			switch (v.getId()) {
			case R.id.register_common_textView:
				register_layout_putong.setVisibility(View.VISIBLE);
				register_layout_shiming.setVisibility(View.GONE);
				register_real_textView.setBackgroundDrawable(SlidingMenuControlActivity.activity.getResources().getDrawable(R.drawable.register_textview_noonclick));
				register_common_textView.setBackgroundDrawable(SlidingMenuControlActivity.activity.getResources().getDrawable(R.drawable.register_text_bg));
				break;
			case R.id.register_real_textView:
				register_layout_putong.setVisibility(View.GONE);
				register_layout_shiming.setVisibility(View.VISIBLE);
				register_common_textView.setBackgroundDrawable(SlidingMenuControlActivity.activity.getResources().getDrawable(R.drawable.register_textview_noonclick));
				register_real_textView.setBackgroundDrawable(SlidingMenuControlActivity.activity.getResources().getDrawable(R.drawable.register_text_bg));
				break;
				//普通注册
			case R.id.register_common_button:
				
				if(checkCommonEmpty()){
					register_common_button.setBackgroundColor(getResources().getColor(R.color.gray));
					register_common_button.setEnabled(false);
					Map<String, String> mapCommon= new HashMap<String, String>();
					mapCommon.put(ParentHandlerService.URL, RequestURL.getDefaultRegister());
					mapCommon.put("name", register_common_username_et.getText().toString().trim());
					mapCommon.put("password", register_common_password_et.getText().toString().trim());
					mapCommon.put("nickName", register_common_real_name_et.getText().toString().trim());
					mapCommon.put("phone", register_common_phonenumber_et.getText().toString().trim());
					mapCommon.put("email", register_common_email_et.getText().toString().trim());
					//mapCommon.put("code", RequestURL.areaCode);
					mapCommon.put("userRegIP",TelManager.getLocalIpAddressV4());
					mapCommon.put("longitude", db[0]+"");
					mapCommon.put("latitude", db[1]+"");
					System.out.println("-------userRegIP-----"+TelManager.getLocalIpAddressV4());
					System.out.println("-------longitude-----"+db[0]+"");
					System.out.println("-------latitude-----"+db[1]+"");
					
					
					Task taskCommon = new Task(TaskID.TASK_REGISTER, mapCommon, this.getClass().getName(), "普通注册");
					MobileApplication.poolManager.addTask(taskCommon);
				}
				break;
				//实名注册
			case R.id.register_real_immediately_register_bt:
				
				if(checkRealEmpty()){
				    register_real_immediately_register_bt.setBackgroundColor(getResources().getColor(R.color.gray));
				    register_real_immediately_register_bt.setEnabled(false);
					Map<String, String> mapReal = new HashMap<String, String>();
					mapReal.put(ParentHandlerService.URL,RequestURL.getAuthRegister());
					mapReal.put("name", register_real_username_et.getText().toString().trim());
					mapReal.put("password", register_real_password_et.getText().toString().trim());
					mapReal.put("nickName", register_real_name_et.getText().toString().trim());
					mapReal.put("workUnit", register_real_job_et.getText().toString().trim());
					mapReal.put("job", register_real_workspace_et.getText().toString().trim());
					mapReal.put("phone", register_real_phonenumber_et.getText().toString().trim());
					mapReal.put("cardId", register_real_personal_number_et.getText().toString().trim());
					mapReal.put("gender", genderType);
					mapReal.put("userRegIP",TelManager.getLocalIpAddressV4());
					mapReal.put("longitude", db[0]+"");
					mapReal.put("latitude", db[1]+"");
					mapReal.put("email", register_real_email_et.getText().toString().trim());
					//省市县
					mapReal.put("adress",province+city+district);
					Task taskReal = new  Task(TaskID.TASK_REGISTER, mapReal, this.getClass().getName(), "实名注册");
					MobileApplication.poolManager.addTask(taskReal);
				}
				System.out.println("省市县-------------"+province+city+district);
				break;
			default:
				break;
			}
		}
	};
	private Boolean checkCommonEmpty(){
		if ("".equals(register_common_username_et.getText().toString().trim())) {
			Toast.makeText(SlidingMenuControlActivity.activity, "用户名不能为空", Toast.LENGTH_SHORT).show();
		}else{
			if("".equals(register_common_password_et.getText().toString().trim())){
			Toast.makeText(SlidingMenuControlActivity.activity, "密码不能为空", Toast.LENGTH_SHORT).show();
			}else{
				if("".equals(register_common_againpassword_et.getText().toString().trim())){
					Toast.makeText(SlidingMenuControlActivity.activity, "请再次输入密码", Toast.LENGTH_SHORT).show();
				}else{
					if(!register_common_againpassword_et.getText().toString().trim().equals(register_common_password_et.getText().toString().trim())){
					Toast.makeText(SlidingMenuControlActivity.activity, "两次密码不一致", Toast.LENGTH_SHORT).show();
					}else{
						if(register_common_email_et.getText().toString().trim().equals("")){
							Toast.makeText(SlidingMenuControlActivity.activity, "请输入邮箱", Toast.LENGTH_SHORT).show();
						}else{
							if(emailFormat(register_common_email_et.getText().toString().trim())){
								return true;
							}else{
								Toast.makeText(SlidingMenuControlActivity.activity, "邮箱格式不正确", Toast.LENGTH_SHORT).show();
							}
						}
					}
				}
			}
		}
		return false;
		
	}
	private Boolean checkRealEmpty(){
		if("".equals(register_real_username_et.getText().toString().trim())){
			Toast.makeText(SlidingMenuControlActivity.activity, "用户名不能为空", Toast.LENGTH_SHORT).show();
			register_common_username_et.setAnimation(shake);
		}else{
			if("".equals(register_real_password_et.getText().toString().trim())){
			Toast.makeText(SlidingMenuControlActivity.activity, "密码不能为空", Toast.LENGTH_SHORT).show();
			register_real_password_et.setAnimation(shake);
			}else{
				if("".equals(register_real_again_password_et.getText().toString().trim())){
					Toast.makeText(SlidingMenuControlActivity.activity, "请输入密码", Toast.LENGTH_SHORT).show();
					register_real_again_password_et.setAnimation(shake);
				}else{
					if(!register_common_againpassword_et.getText().toString().trim().equals(register_common_password_et.getText().toString().trim())){
						Toast.makeText(SlidingMenuControlActivity.activity, "两次密码不一致", Toast.LENGTH_SHORT).show();
					}else{
						if("".equals(register_real_name_et.getText().toString().trim())){
							Toast.makeText(SlidingMenuControlActivity.activity, "请输入您的姓名", Toast.LENGTH_SHORT).show();
							register_real_name_et.setAnimation(shake);
							}
							else{
								if("".equals(register_real_job_et.getText().toString().trim())){
									Toast.makeText(SlidingMenuControlActivity.activity, "请输入您的工作单位", Toast.LENGTH_SHORT).show();
								}else{
									if("".equals(register_real_phonenumber_et.getText().toString().trim())){
										Toast.makeText(SlidingMenuControlActivity.activity, "请输入您的手机号", Toast.LENGTH_SHORT).show();
									}else{
										if("".equals(register_real_email_et.getText().toString().trim())){
											Toast.makeText(SlidingMenuControlActivity.activity, "请输入您的邮箱", Toast.LENGTH_SHORT).show();
										}else{
											if("".equals(register_real_personal_number_et.getText().toString().trim())){
												Toast.makeText(SlidingMenuControlActivity.activity, "请输入您的身份证号", Toast.LENGTH_SHORT).show();
											}else{
													if(!emailFormat(register_real_email_et.getText().toString().trim())){
														Toast.makeText(SlidingMenuControlActivity.activity, "邮箱格式不正确", Toast.LENGTH_SHORT).show();
													}else{
														return true;
													}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		
		return false;
	}

	@Override
	protected void initialized() {
		register_real_gender_RadioGroup.setOnCheckedChangeListener(changeListener);
		((RadioButton) register_real_gender_RadioGroup.getChildAt(0)).setChecked(true);
		SlidingMenuControlActivity.main_header_title_TextView.setText("用户注册");
		shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
	}
	private OnCheckedChangeListener changeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
			switch (checkedId) {
			case R.id.register_real_male_RadioButton:
				genderType = "0";
				break;
			case R.id.register_real_female_RadioButton:
				genderType = "1";
				break;
			default:
				break;
			}
		}
	};
	public void initSpinner1(){
		dbm = new DBManager(getActivity().getApplicationContext());
	 	dbm.openDatabase();
	 	db = dbm.getDatabase();
	 	List<MyListItem> list = new ArrayList<MyListItem>();
		
	 	try {    
	        String sql = "select * from province";  
	        Cursor cursor = db.rawQuery(sql,null);  
	        cursor.moveToFirst();
	        while (!cursor.isLast()){ 
		        String code=cursor.getString(cursor.getColumnIndex("code")); 
		        byte bytes[]=cursor.getBlob(2); 
		        String name=new String(bytes,"gbk");
		        MyListItem myListItem=new MyListItem();
		        myListItem.setName(name);
		        myListItem.setPcode(code);
		        list.add(myListItem);
		        cursor.moveToNext();
	        }
	        String code=cursor.getString(cursor.getColumnIndex("code")); 
	        byte bytes[]=cursor.getBlob(2); 
	        String name=new String(bytes,"gbk");
	        MyListItem myListItem=new MyListItem();
	        myListItem.setName(name);
	        myListItem.setPcode(code);
	        list.add(myListItem);
	        
	    } catch (Exception e) {  
	    } 
	 	
	 	if(dbm!=null){
	 		dbm.closeDatabase();
	 	}
	 	if(db!=null){
	 		db.close();	
	 	}
	 	
	 	MyAdapter myAdapter = new MyAdapter(getActivity().getApplicationContext(),list);
	 	
	 	register_real_space_sheng_spinner.setAdapter(myAdapter);
	 	register_real_space_sheng_spinner.setOnItemSelectedListener(new SpinnerOnSelectedListener1());
	 	
	}
    public void initSpinner2(String pcode){
		dbm = new DBManager(getActivity().getApplicationContext());
	 	dbm.openDatabase();
	 	db = dbm.getDatabase();
	 	List<MyListItem> list = new ArrayList<MyListItem>();
		
	 	try {    
	        String sql = "select * from city where pcode='"+pcode+"'";  
	        Cursor cursor = db.rawQuery(sql,null);  
	        cursor.moveToFirst();
	        while (!cursor.isLast()){ 
		        String code=cursor.getString(cursor.getColumnIndex("code")); 
		        byte bytes[]=cursor.getBlob(2); 
		        String name=new String(bytes,"gbk");
		        MyListItem myListItem=new MyListItem();
		        myListItem.setName(name);
		        myListItem.setPcode(code);
		        list.add(myListItem);
		        cursor.moveToNext();
	        }
	        String code=cursor.getString(cursor.getColumnIndex("code")); 
	        byte bytes[]=cursor.getBlob(2); 
	        String name=new String(bytes,"gbk");
	        MyListItem myListItem=new MyListItem();
	        myListItem.setName(name);
	        myListItem.setPcode(code);
	        list.add(myListItem);
	        
	    } catch (Exception e) {  
	    } 
	 	if(dbm!=null){
	 		dbm.closeDatabase();
	 	}
	 	if(db!=null){
	 		db.close();	
	 	}	
	 	
	 	MyAdapter myAdapter = new MyAdapter(getActivity().getApplicationContext(),list);
	 	register_real_space_shi_spinner.setAdapter(myAdapter);
	 	register_real_space_shi_spinner.setOnItemSelectedListener(new SpinnerOnSelectedListener2());
	}
    public void initSpinner3(String pcode){
		dbm = new DBManager(getActivity().getApplicationContext());
	 	dbm.openDatabase();
	 	db = dbm.getDatabase();
	 	List<MyListItem> list = new ArrayList<MyListItem>();
		
	 	try {    
	        String sql = "select * from district where pcode='"+pcode+"'";  
	        Cursor cursor = db.rawQuery(sql,null);  
	        cursor.moveToFirst();
	        while (!cursor.isLast()){ 
		        String code=cursor.getString(cursor.getColumnIndex("code")); 
		        byte bytes[]=cursor.getBlob(2); 
		        String name=new String(bytes,"gbk");
		        MyListItem myListItem=new MyListItem();
		        myListItem.setName(name);
		        myListItem.setPcode(code);
		        list.add(myListItem);
		        cursor.moveToNext();
	        }
	        String code=cursor.getString(cursor.getColumnIndex("code")); 
	        byte bytes[]=cursor.getBlob(2); 
	        String name=new String(bytes,"gbk");
	        MyListItem myListItem=new MyListItem();
	        myListItem.setName(name);
	        myListItem.setPcode(code);
	        list.add(myListItem);
	        
	    } catch (Exception e) {  
	    } 
	 	if(dbm!=null){
	 		dbm.closeDatabase();
	 	}
	 	if(db!=null){
	 		db.close();	
	 	}
	 	
	 	MyAdapter myAdapter = new MyAdapter(getActivity().getApplicationContext(),list);
	 	register_real_space_xian_spinner.setAdapter(myAdapter);
	 	register_real_space_xian_spinner.setOnItemSelectedListener(new SpinnerOnSelectedListener3());
	}
    
	class SpinnerOnSelectedListener1 implements OnItemSelectedListener{
		
		public void onItemSelected(AdapterView<?> adapterView, View view, int position,
				long id) {
			province=((MyListItem) adapterView.getItemAtPosition(position)).getName().trim();
			String pcode =((MyListItem) adapterView.getItemAtPosition(position)).getPcode();
			
			initSpinner2(pcode);
			initSpinner3(pcode);
		}

		public void onNothingSelected(AdapterView<?> adapterView) {
			// TODO Auto-generated method stub
		}		
	}
	class SpinnerOnSelectedListener2 implements OnItemSelectedListener{
		
		public void onItemSelected(AdapterView<?> adapterView, View view, int position,
				long id) {
			city=((MyListItem) adapterView.getItemAtPosition(position)).getName().trim();
			String pcode =((MyListItem) adapterView.getItemAtPosition(position)).getPcode();

			initSpinner3(pcode);
		}

		public void onNothingSelected(AdapterView<?> adapterView) {
			// TODO Auto-generated method stub
		}		
	}
	
	class SpinnerOnSelectedListener3 implements OnItemSelectedListener{
		
		public void onItemSelected(AdapterView<?> adapterView, View view, int position,
				long id) {
			district=((MyListItem) adapterView.getItemAtPosition(position)).getName().trim();
			//Toast.makeText(getActivity(), province+","+city+","+district, Toast.LENGTH_LONG).show();
		}

		public void onNothingSelected(AdapterView<?> adapterView) {
			// TODO Auto-generated method stub
		}		
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
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}
	@Override
	public void refresh(int taskID, Object... params) {
		Message msg =new Message();
		if (params != null) {
			if (params[0] != null) {
				try {
					JSONObject jsonObject = (JSONObject) params[0];
					String result = jsonObject.getString("result");
					System.out.println("---success---" + result);
					switch (taskID) {
					case TaskID.TASK_REGISTER:
						Toast.makeText(SlidingMenuControlActivity.activity,
								jsonObject.getString("message"), Toast.LENGTH_LONG)
								.show();
						if ("success".equals(result)) {
							msg.what=1;
							handler.sendEmptyMessage(1);
						}
						else{
							msg.what=2;
							handler.sendEmptyMessage(2);
						}
						break;

					default:
						break;
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				loadingDialog.showTimeOut();
			}
		} else {
			msg.what=3;
			handler.sendEmptyMessage(3);
			loadingDialog.showTimeOut();
		}
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
