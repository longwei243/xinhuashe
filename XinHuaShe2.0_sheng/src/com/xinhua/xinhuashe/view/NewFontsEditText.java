package com.xinhua.xinhuashe.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * 引用字体EditText
 *
 * @author azuryleaves
 * @since 2013-12-25 下午3:44:06
 * @version 1.0
 *
 */
public class NewFontsEditText extends EditText {

	public NewFontsEditText(Context context) {
		super(context);
		Typeface typeface = Typeface.createFromAsset(context.getResources().getAssets(), "fonts/yhxxt.otf");
		this.setTypeface(typeface);
	}

	public NewFontsEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		Typeface typeface = Typeface.createFromAsset(context.getResources().getAssets(), "fonts/yhxxt.otf");
		this.setTypeface(typeface);
	}
	
	public NewFontsEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		Typeface typeface = Typeface.createFromAsset(context.getResources().getAssets(), "fonts/yhxxt.otf");
		this.setTypeface(typeface);
	}
	
}
