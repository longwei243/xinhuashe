package com.xinhua.xinhuashe.util;

import java.io.IOException;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

/**
 * @author  https://cmpay.10086.cn/
 * @version 1.0.0
 */
public abstract class IPOSBase
{
	public static final String PROGRESS_DIALOG_TITLE = "提示";
	public static final String PROGRESS_DIALOG_INIT_CONTENT = "正在初始化网络电话组件, 如果是第一次使用,该过程可能比较长,请耐心等待...";
	
	public static final String IPOS_FULL_PACK_NAME = "dodocall.efg";
	public static final String IPOS_RELEASE_FILE_NAME = "huihua";
	
	public static final String INSTALL_TEMP_APK_NAME = "/temp.apk";

	public static final int WHAT_SHOW_PROGRESS = 401;

	public static final int WHAT_CLOSE_PROGRESS = 402;

	public static final int WHAT_SHOW_INSTALL = 403;

	public static final int WHAT_SHOW_PROGRESS_CAN_CANCEL = 404;

	private Context context;

	private ProgressDialog mProgress;

	protected String tagName;
	
	protected IPOSUtils iposUtils;

	public IPOSBase(Context context)
	{
		this.context = context;
	}

	protected abstract String getTag();

	private Handler handler = new Handler()
	{

		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			if (msg.what == WHAT_SHOW_PROGRESS)
			{
				String[] infos = (String[]) msg.obj;
				mProgress = showProgress(context, infos[0],
						infos[1], true, false);
			}
			else if (msg.what == WHAT_CLOSE_PROGRESS)
			{
				closeProgress();
			}
			else if (msg.what == WHAT_SHOW_INSTALL)
			{
				closeProgress();
				String path = (String) msg.obj;
				showInstallConfirmDialog(path);
			} else if (msg.what == WHAT_SHOW_PROGRESS_CAN_CANCEL)
			{
				Object[] infos = (Object[]) msg.obj;
				mProgress = showProgress(context, (String)infos[0],
						(String)infos[1], true, true);
			}
		}
	};

	protected String getIPosReleaseName()
	{
		return IPOS_RELEASE_FILE_NAME + ".apk";
	}

	private void chmod(String path)
	{
		try
		{
			String command = "chmod 777 " + path;
			Runtime runtime = Runtime.getRuntime();
			runtime.exec(command);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void showInstallConfirmDialog(final String cachePath)
	{
		AlertDialog.Builder tDialog = new AlertDialog.Builder(getContext());
		tDialog.setTitle("安装提示").setMessage("为了保证您的正常使用，需要您安装网络电话组件，才能拨打电话。\n点击确定，立即安装。");
		tDialog.setPositiveButton("确定", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				chmod(cachePath);
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setDataAndType(Uri.parse("file://" + cachePath),
						"application/vnd.android.package-archive");
				getContext().startActivity(intent);
			}
		});

		tDialog.setNegativeButton("取消", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				iposUtils.cancel();
			}
		});

		tDialog.setCancelable(false);
		tDialog.show();
	}

	protected void sendMessage(int what)
	{
		sendMessage(what, null, handler);
	}

	protected void sendMessage(int what, Object obj)
	{
		sendMessage(what, obj, handler);
	}

	protected void sendMessage(int what, Object obj, Handler handler)
	{
		Message msg = new Message();
		msg.what = what;
		msg.obj = obj;
		handler.sendMessage(msg);
	}

	protected void closeProgress()
	{
		try
		{
			if (mProgress != null)
			{
				mProgress.dismiss();
				mProgress = null;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @return the context
	 */
	public Context getContext()
	{
		return context;
	}
	
	public ProgressDialog showProgress(Context context,
			CharSequence title, CharSequence message, boolean indeterminate,
			boolean cancelable )
	{
		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setIndeterminate(indeterminate);
		dialog.setCancelable(cancelable);
		dialog.show();
		return dialog;
	}
	
	public ProgressDialog showProgress(Context context,
			CharSequence title, CharSequence message, boolean indeterminate,
			boolean cancelable, OnCancelListener onCancelListener )
	{
		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setIndeterminate(indeterminate);
		dialog.setCancelable(true);
		dialog.setOnCancelListener(onCancelListener);
		dialog.show();
		return dialog;
	}
}
