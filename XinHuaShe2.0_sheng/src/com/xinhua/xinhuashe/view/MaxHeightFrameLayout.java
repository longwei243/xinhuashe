package com.xinhua.xinhuashe.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * 
 * 
 * @author azuryleaves
 * @since 2014-4-8 上午10:22:16
 * @version 1.0
 * 
 */
public class MaxHeightFrameLayout extends FrameLayout {

	private Context context;
	private double scale = 0.4427;	//-- 比例

	public MaxHeightFrameLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	public MaxHeightFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public MaxHeightFrameLayout(Context context) {
		super(context);
		this.context = context;
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metrics);
		int pixelsHeight = metrics.heightPixels;
		int maxHeight = (int) (pixelsHeight * scale);
		final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		if (heightMode == MeasureSpec.UNSPECIFIED) {
			return;
		}
		int height = getMeasuredHeight();
		int specWidthSize = MeasureSpec.getSize(widthMeasureSpec);
		if (height > maxHeight) {
			setMeasuredDimension(specWidthSize, maxHeight);
		} else {
			setMeasuredDimension(specWidthSize, height);
		}
	}

}
