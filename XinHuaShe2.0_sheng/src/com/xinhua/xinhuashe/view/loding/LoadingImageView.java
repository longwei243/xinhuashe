package com.xinhua.xinhuashe.view.loding;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 
 *
 * @author azuryleaves
 * @since 2013-12-31 上午11:05:28
 * @version 1.0
 *
 */
public class LoadingImageView extends ImageView {

	public LoadingImageView(Context context) {
		super(context);
	}

	public LoadingImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LoadingImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (getBackground() == null) {
			new RuntimeException("LoadingImageView没有添加背景");
		}
		int w = getBackground().getIntrinsicWidth();
		int h = getBackground().getIntrinsicHeight();
		super.onMeasure(w, h);
	}
}
