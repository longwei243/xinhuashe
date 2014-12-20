package com.xinhua.xinhuashe.parentclass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.service.MobileApplication;
import com.xinhua.xinhuashe.view.dialog.LoadingDialog;

/**
 * Activity父类
 * 
 * 
 */
public abstract class ParentActivity extends Activity {

	/**
	 * LOG打印标签
	 */
	// private static final String TAG = ParentActivity.class.getSimpleName();

	/**
	 * 全局的Context {@link #mContext = this.getApplicationContext();}
	 */
	protected Context mContext;
	public static LoadingDialog loadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int layoutId = getLayoutId();
		if (layoutId == 0) {
			Log.e(MobileApplication.TAG, "请添加ContentView布局文件");
		} else {
			setContentView(layoutId);
			// 删除窗口背景
			getWindow().setBackgroundDrawable(null);
		}

		mContext = this.getApplicationContext();

		if (loadingDialog == null) {
			loadingDialog = new LoadingDialog(this, clickListener);
		}
		// 向用户展示信息前的准备工作在这个方法里处理
		preliminary();
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.loadingdialog_cancel_ImageView:
				loadingDialog.cancel();
				break;

			default:
				break;
			}

		}
	};
	
	/**
	 * 向用户展示信息前的准备工作在这个方法里处理
	 */
	protected void preliminary() {
		// 初始化组件
		setupViews();

		// 初始化数据
		initialized();
	}

	/**
	 * 获取全局的Context
	 * 
	 * @return {@link #mContext = this.getApplicationContext();}
	 */
	public Context getContext() {
		return mContext;
	}

	/**
	 * 布局文件ID
	 * 
	 * @return
	 */
	protected abstract int getLayoutId();

	/**
	 * 初始化组件
	 */
	protected abstract void setupViews();

	/**
	 * 初始化数据
	 */
	protected abstract void initialized();

	/**
	 * 需要线程池操作的任务
	 * 
	 */
	protected abstract void threadTask();
	
	/**
	 * 长时间显示Toast提示(来自String)
	 * 
	 * @param message
	 */
	protected void showToast(String message) {
		Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
	}

	/**
	 * 长时间显示Toast提示(来自res)
	 * 
	 * @param resId
	 */
	protected void showToast(int resId) {
		Toast.makeText(mContext, getString(resId), Toast.LENGTH_LONG).show();
	}

	/**
	 * 短暂显示Toast提示(来自res)
	 * 
	 * @param resId
	 */
	protected void showShortToast(int resId) {
		Toast.makeText(mContext, getString(resId), Toast.LENGTH_SHORT).show();
	}

	/**
	 * 短暂显示Toast提示(来自String)
	 * 
	 * @param text
	 *            .
	 */
	protected void showShortToast(String text) {
		Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 通过Class跳转界面
	 **/
	public void startActivity(Class<?> cls) {
		startActivity(cls, null);
	}

	/**
	 * 含有Bundle通过Class跳转界面
	 **/
	public void startActivity(Class<?> cls, Bundle bundle) {
		Intent intent = new Intent();
		intent.setClass(this, cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
		// overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	/**
	 * 通过Action跳转界面
	 **/
	public void startActivity(String action) {
		startActivity(action, null);
	}

	/**
	 * 含有Bundle通过Action跳转界面
	 **/
	public void startActivity(String action, Bundle bundle) {
		Intent intent = new Intent();
		intent.setAction(action);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
		// overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	/**
	 * 含有Bundle通过Class打开编辑界面
	 **/
	public void startActivityForResult(Class<?> cls, Bundle bundle,
			int requestCode) {
		Intent intent = new Intent();
		intent.setClass(this, cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivityForResult(intent, requestCode);
		// overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	/**
	 * 带有右进右出动画的退出
	 */
	@Override
	public void finish() {
		super.finish();
		// overridePendingTransition(R.anim.push_right_in,
		// R.anim.push_right_out);
	}

	/**
	 * 默认退出
	 */
	public void defaultFinish() {
		super.finish();
	}

}
