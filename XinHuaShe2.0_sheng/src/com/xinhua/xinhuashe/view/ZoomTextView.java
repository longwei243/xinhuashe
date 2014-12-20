package com.xinhua.xinhuashe.view;

import android.widget.TextView;

/**
 * 手势缩放TextView
 * 
 * @author azuryleaves
 * @since 2014-4-9 下午4:18:04
 * @version 1.0
 * 
 */
public class ZoomTextView extends ZoomView<TextView> {

	/** 最小字体 */
	public static final float MIN_TEXT_SIZE = 18f;

	/** 最大字体 */
	public static final float MAX_TEXT_SIZE = 20.0f;

	/** 缩放比例 */
	float scale;

	/** 设置字体大小 */
	float textSize;

	public ZoomTextView(TextView view, float scale) {
		super(view);
		this.scale = scale;
		textSize = view.getTextSize();
	}

	/**
	 * 放大
	 */
	protected void zoomOut() {
		textSize += scale;
		if (textSize > MAX_TEXT_SIZE) {
			textSize = MAX_TEXT_SIZE;
		}
		view.setTextSize(textSize);
	}

	/**
	 * 缩小
	 */
	protected void zoomIn() {
		textSize -= scale;
		if (textSize < MIN_TEXT_SIZE) {
			textSize = MIN_TEXT_SIZE;
		}
		view.setTextSize(textSize);
	}

}
