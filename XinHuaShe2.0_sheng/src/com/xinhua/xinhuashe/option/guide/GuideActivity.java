package com.xinhua.xinhuashe.option.guide;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.option.SlidingMenuControlActivity;
import com.xinhua.xinhuashe.option.guide.adapter.GuideViewPagerAdapter;
import com.xinhua.xinhuashe.parentclass.ParentActivity;
import com.xinhua.xinhuashe.service.MobileApplication;

/**
 * 引导页
 * 
 */
public class GuideActivity extends ParentActivity {

	private SharedPreferences preferences;
	private ViewPager guide_viewpager;
	private Timer timer;
	private TimerTask timerTask;
	private boolean timerIsRun = false;
	private GuideViewPagerAdapter adapter;
	private Button guide_btn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		preferences = getSharedPreferences(MobileApplication.TAG, MODE_PRIVATE);
		
		LayoutInflater inflater = LayoutInflater.from(GuideActivity.this);
		View view1 = inflater.inflate(R.layout.guide_view1, null);
		View view2 = inflater.inflate(R.layout.guide_view2, null);
		View view3 = inflater.inflate(R.layout.guide_view3, null);
		
		ArrayList<View> arrayView = new ArrayList<View>();
		arrayView.add(view1);
		arrayView.add(view2);
		arrayView.add(view3);
		
		guide_btn = (Button) view3.findViewById(R.id.guide_btn);
		guide_btn.setOnClickListener(clickListener);
		
		adapter = new GuideViewPagerAdapter(arrayView);
		guide_viewpager.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		guide_viewpager.setOnPageChangeListener(listener);
		
	}
	
	OnPageChangeListener listener = new OnPageChangeListener() {
		
		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onPageScrolled(int position, float arg1, int arg2) {
			if (position == 2 && !timerIsRun) {
				timer.scheduleAtFixedRate(timerTask, 3000, 500);
				timerIsRun = true;
			}
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}
	};


	private Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			if(preferences.getBoolean("isReinstall", true)){
				Intent i = new Intent(GuideActivity.this,
						SlidingMenuControlActivity.class);
				GuideActivity.this.startActivity(i);
				GuideActivity.this.finish();
				preferences.edit().putBoolean("isReinstall", false).commit();
			}else{
				GuideActivity.this.finish();
			}
			return true;
		}
	});

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
//			Intent i = new Intent(GuideActivity.this,
//			SlidingMenuControlActivity.class);
//			GuideActivity.this.startActivity(i);
//			GuideActivity.this.finish();
//			preferences.edit().putBoolean("isReinstall", false).commit();
			handler.sendEmptyMessage(0);
			timerTask.cancel();
			timer.cancel();
		}

	};

	/*@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 按下键盘上返回按钮
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			new AlertDialog.Builder(GuideActivity.this)
					.setMessage(R.string.guide_dialog)
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}
							})
					.setPositiveButton("确认",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Intent i = new Intent(GuideActivity.this,
											SlidingMenuControlActivity.class);
									GuideActivity.this.startActivity(i);
									dialog.cancel();
									GuideActivity.this.finish();
									preferences.edit()
											.putBoolean("isReinstall", false)
											.commit();
								}
							}).create().show();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}*/

	@Override
	protected int getLayoutId() {
		return R.layout.guide;
	}


	@Override
	protected void setupViews() {
		
		guide_viewpager = (ViewPager) findViewById(R.id.guide_viewpager);
		
	}

	@Override
	protected void initialized() {
		timer = new Timer();
		timerTask = new TimerTask() {

			@Override
			public void run() {
				handler.sendEmptyMessage(0);
				timerTask.cancel();
				timer.cancel();
			}
		};
	}

	@Override
	protected void threadTask() {

	}

}
