package com.xinhua.xinhuashe.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 引用字体TextView
 *
 * @author azuryleaves
 * @since 2013-12-11 下午5:26:50
 * @version 1.0
 *
 */
public class NewFontsTextView extends TextView {

	public NewFontsTextView(Context context) {
		super(context);
		Typeface typeface = Typeface.createFromAsset(context.getResources().getAssets(), "fonts/yhxxt.otf");
		this.setTypeface(typeface);
	}

	public NewFontsTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Typeface typeface = Typeface.createFromAsset(context.getResources().getAssets(), "fonts/yhxxt.otf");
		this.setTypeface(typeface);
	}

	public NewFontsTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		Typeface typeface = Typeface.createFromAsset(context.getResources().getAssets(), "fonts/yhxxt.otf");
		this.setTypeface(typeface);
	}

}
