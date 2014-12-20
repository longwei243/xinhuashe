package com.xinhua.xinhuashe.view.dialog;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.xinhuanews.sheng.R;

/**
 * 加载对话框
 * 
 * @author azuryleaves
 * 
 */
public class LoadingDialog {

	public Context context;
	private static Dialog loadingDialog;
	private View.OnClickListener cancelClickListener;
	private View view;

	public LoadingDialog(Context context) {
		this.context = context;
	}
	
	public LoadingDialog(Context context, View.OnClickListener cancelClickListener) {
		this.context = context;
		this.cancelClickListener = cancelClickListener;
	}

	private void initLoadingDialog() {
		loadingDialog = new Dialog(context, R.style.loadingDialog);
		view = LayoutInflater.from(context).inflate(R.layout.loadingdialog, null);
		ImageView loadingdialog_refresh_ImageView = (ImageView) view.findViewById(R.id.loadingdialog_refresh_ImageView);
		ImageView loadingdialog_cancel_ImageView = (ImageView) view.findViewById(R.id.loadingdialog_cancel_ImageView);
		RotateAnimation rotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(context, R.anim.loadingdialog_refresh);
		loadingdialog_refresh_ImageView.setAnimation(rotateAnimation);
		loadingDialog.setContentView(view);
		loadingDialog.setCancelable(false);
		if (cancelClickListener == null) {
			loadingdialog_cancel_ImageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View view) {
					loadingDialog.cancel();
				}
			});
		} else {
			loadingdialog_cancel_ImageView.setOnClickListener(cancelClickListener);
		}
	}
	
	public void setTag(Object tag) {
		view.setTag(tag);
	}
	
	public void setTag(int key, Object tag) {
		view.setTag(key, tag);
	}
	
	public Object getTag() {
		return view.getTag();
	}
	
	public Object getTag(int key) {
		return view.getTag(key);
	}

	public void show() {
		initLoadingDialog();
		loadingDialog.show();
	}
	
	public void dismiss() {
		if (loadingDialog != null) {
			if (loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
		}
	}
	
	public void cancel() {
		if (loadingDialog != null) {
			loadingDialog.cancel();
		}
	}

	public void showTimeOut() {
//		Toast.makeText(context, R.string.request_timeout, Toast.LENGTH_SHORT).show();
	}
	
}
