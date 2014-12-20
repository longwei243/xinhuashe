package com.xinhua.xinhuashe.option.vote.view;

import java.util.Timer;
import java.util.TimerTask;
import com.xinhua.xinhuashe.util.VoteSubmitTools;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TextView;

public class VoteSubmitPercentView extends TextView {

	Timer timer;
	int numPercent=0;

	public VoteSubmitPercentView(Activity context) {
		super(context);
		setTextColor(Color.BLACK);
	}

	public VoteSubmitPercentView(Activity context, AttributeSet attrs) {
		super(context, attrs);
		setTextColor(Color.BLACK);
	}

	/**
	 * @param percent
	 *            根据参数percent的大小决定百分数增长
	 */
	public void setDrawUpdate(final int percent, boolean isNeedDrawing, final VoteSubmitTools convertTools) {
		if (isNeedDrawing) {
			timer = new Timer();
			TimerTask task = new TimerTask() {

				@SuppressLint("NewApi") @Override
				public void run() {
					
					if (numPercent == percent) {
						if(numPercent==0){
							setText(numPercent + "%");
							setLeft(convertTools.dip2px(numPercent * 3 ));
						}
						timer.cancel();
						return;
					}
					post(new Runnable() {

						@SuppressLint("NewApi") 
						@Override
						public void run() {
							setText(numPercent + "%");
							setLeft(convertTools.dip2px(numPercent * 3)-10);
						}
					});
					numPercent += 1;
				}
			};
			timer.schedule(task, 10, 50);
		} else {
			numPercent = percent;
			setText(numPercent + "%");
		}
	}

}
