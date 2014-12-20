package com.xinhua.xinhuashe.option.say;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.option.SlidingMenuControlActivity;
import com.xinhua.xinhuashe.parentclass.ParentFragment;

public class SayPublishResultFragment extends ParentFragment {

	private static Timer timer;
	private static TimerTask timerTask;

	@Override
	protected int getLayoutId() {
		return R.layout.say_publish_result;
	}

	@Override
	protected void setupViews(View parentView) {

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
		timer.scheduleAtFixedRate(timerTask, 5000, 500);
	}

	private Handler handler = new Handler(new Handler.Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			SlidingMenuControlActivity.activity.getSupportFragmentManager().popBackStackImmediate();			
			return true;
		}
	});
	
	@Override
	protected void threadTask() {

	}

}
