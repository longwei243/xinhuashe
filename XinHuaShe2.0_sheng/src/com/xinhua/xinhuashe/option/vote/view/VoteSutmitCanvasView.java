package com.xinhua.xinhuashe.option.vote.view;

import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class VoteSutmitCanvasView extends View {

	private int width = 0;
	private int height = 30;
	private int bgColor;
	private Paint paint;
	private Timer timer;

	public VoteSutmitCanvasView(Activity context) {
		super(context);
	}

	public VoteSutmitCanvasView(Activity context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return false;
	}

	/**
	 * @param color
	 *            设置绘制view的背景色
	 */
	public void setBackgroundColor(int color) {
		bgColor = color;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		paint = new Paint();
		paint.setColor(bgColor);
		canvas.drawRect(0, 5, width-10, height, paint);
	}

	/**
	 * @param percent
	 *            根据参数percent的大小决定要绘制的view的最大宽度
	 */
	public void setDrawUpdate(final int percent, boolean isNeedDrawing) {
		if (isNeedDrawing) {
			timer = new Timer();
			TimerTask task = new TimerTask() {

				@Override
				public void run() {
					width += 1;
					if (width == percent) {
						timer.cancel();
						return;
					}
					postInvalidate();
				}
			};
			timer.schedule(task, 10, 10);
		} else {
			width = percent;
		}

	}

}
