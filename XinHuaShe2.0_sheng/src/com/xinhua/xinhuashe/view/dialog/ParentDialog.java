package com.xinhua.xinhuashe.view.dialog;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.xinhuanews.sheng.R;

/**
 * 父对话框-所有对话框继承此对话框
 * @author azuryleaves
 *
 */
public class ParentDialog extends Dialog {

	private Context context;
	private View parentView;
	private android.view.View.OnClickListener onComfirmClickListener;
	
	public ParentDialog(Context context, android.view.View.OnClickListener onComfirmClickListener) {
		super(context, R.style.parentDialog);
		this.setCanceledOnTouchOutside(false);
		parentView = LayoutInflater.from(context).inflate(R.layout.parentdialog, null);
		this.context = context;
		this.onComfirmClickListener = onComfirmClickListener;
		setTitle(R.string.parentdialog_title);
	}
	
	@Override
	public void setContentView(View view) {
		FrameLayout parentdialog_content_FrameLayout = (FrameLayout) parentView.findViewById(R.id.parentdialog_content_FrameLayout);
		parentdialog_content_FrameLayout.addView(view);
		Button cancel = (Button) parentView.findViewById(R.id.parentdialog_cancel_Button);
		Button confirm = (Button) parentView.findViewById(R.id.parentdialog_confirm_Button);
		cancel.setOnClickListener(onCancelClickListener);
		if (onComfirmClickListener == null) {
			confirm.setOnClickListener(onCancelClickListener);
		} else {
			confirm.setOnClickListener(onComfirmClickListener);
		}
		super.setContentView(parentView);
	}

	@Override
	public void setTitle(CharSequence title) {
		TextView textView = (TextView) parentView.findViewById(R.id.parentdialog_title_TextView);
		textView.setText(title);
	}

	@Override
	public void setTitle(int titleId) {
		TextView textView = (TextView) parentView.findViewById(R.id.parentdialog_title_TextView);
		textView.setText(titleId);
	}
	
	public void setIcon(int iconId) {
		TextView textView = (TextView) parentView.findViewById(R.id.parentdialog_title_TextView);
		textView.setCompoundDrawables(context.getResources().getDrawable(iconId), null, null, null);
	}
	
	private android.view.View.OnClickListener onCancelClickListener = new android.view.View.OnClickListener() {
		
		@Override
		public void onClick(View view) {
			ParentDialog.this.cancel();
		}
	};

}
