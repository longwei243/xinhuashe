package com.xinhua.xinhuashe.view.loding;

import android.os.Handler;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class LoadingAnimations {

	public static void startAnimations(ViewGroup viewgroup,
			int durationMillis) {
		for (int i = 0; i < viewgroup.getChildCount(); i++) {
			final LoadingImageView imageView = (LoadingImageView) viewgroup.getChildAt(i);
			final TranslateAnimation animation = new TranslateAnimation(
					0, -300.0f, 0, 0);
			animation.setFillAfter(false);
			animation.setDuration(durationMillis);
			animation.setStartTime(0);
//			animation.setStartOffset(((viewgroup.getChildCount() - i) * 300)
//					/ (-1 + viewgroup.getChildCount()));// 下一个动画的偏移时间
			animation.setRepeatCount(Integer.MAX_VALUE);
			animation.setRepeatMode(Animation.REVERSE);
			if (i == 1) {
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						imageView.startAnimation(animation);
					}
					
				}, 1000);
			} else {
				imageView.startAnimation(animation);
			}
		}
	}
	
}