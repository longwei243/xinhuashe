package com.xinhua.xinhuashe.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

public class IPOSUtils extends IPOSBase implements InstallReceiverListener {

	/** 手机支付插件是否已经安装 */
	private boolean iposIsInstalled = false;

	private boolean isCanceled = false;

	private InstallReceiver receiver;

	public IPOSUtils(Context context) {
		super(context);
		tagName = IPOSUtils.class.getName();
		this.iposUtils = this;

		IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addDataScheme("package");
		receiver = new InstallReceiver();
		receiver.setIPOSUtilsListener(this);
		context.registerReceiver(receiver, filter);
	}

	@Override
	protected String getTag() {
		return tagName;
	}

	private boolean retrieveApkSuccessFlag = false;
	private File cacheDir;
	private String cachePath;
	private String installedVersion;

	/**
	 * Activity在自己的OnCretae方法里必须首先调用该方法完成初始化过程,
	 * 
	 * 该方法在IPOSUtils被创建后,应立即调用
	 */
	private synchronized void init() {
		PackageInfo packageInfo = getPackageInfo();
		cacheDir = getContext().getCacheDir();
		cachePath = cacheDir.getAbsolutePath() + INSTALL_TEMP_APK_NAME;
		File file = null;
		if (cachePath != null && (file = new File(cachePath)).exists()) {
			file.delete();
		}

		if (packageInfo == null) { // 如果没有安装
			iposIsInstalled = false;
			retrieveApkSuccessFlag = retrieveApkFromAssets(getIPosReleaseName(), cachePath);
			if (retrieveApkSuccessFlag == false) { // 解压失败，只能去服务器下载版本
				installedVersion = "0.0.0.1";
			} else {
				PackageInfo apkInfo = getApkInfo(getContext(), cachePath);
				installedVersion = apkInfo.versionName + ".1";
			}
		} else { // 如果已经安装
			iposIsInstalled = true;
			installedVersion = packageInfo.versionName + ".0";
		}
	}

	/**
	 * 所有耗时操作接口,必须启动进度条
	 * 
	 * @param order
	 */
	public void start() {

		this.init();

//		sendMessage(WHAT_SHOW_PROGRESS_CAN_CANCEL, new Object[] { IPOSBase.PROGRESS_DIALOG_TITLE, IPOSBase.PROGRESS_DIALOG_INIT_CONTENT });

		Thread t = new Thread(checkVersionRunnable);
		t.setDaemon(true);
		t.setPriority(Thread.NORM_PRIORITY);
		t.start();

	}

	public void iPay() throws Exception {
		if (!iposIsInstalled) {
			return;
		}
		//方法一
		Intent intent = new Intent();
		//包名 包名+类名（全路径）
		intent.setClassName("dodocall.efg", "dodocall.efg.nnnview.New_Open");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		getContext().startActivity(intent);
		//方法二
//		Intent intent = new Intent();
//		ComponentName comp = new ComponentName("dodocall.efg","dodocall.efg.nnnview.New_Open");
//		intent.setComponent(comp);
//		intent.setAction("android.intent.action.MAIN");
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		getContext().startActivity(intent);  

	}

	/**
	 * 判断是否已经安装了手机支付插件
	 * 
	 * @return
	 */
	// private boolean hasInstalled() {
	// return getPackageInfo() == null ? false : true;
	// }

	/**
	 * 获取手机支付插件的安装信息
	 * 
	 * @return
	 */
	private PackageInfo getPackageInfo() {
		try {
			PackageManager manager = getContext().getPackageManager();
			List<PackageInfo> pkgList = manager.getInstalledPackages(0);
			for (int i = 0; i < pkgList.size(); i++) {
				PackageInfo pI = pkgList.get(i);
				if (pI.packageName.equalsIgnoreCase(IPOSBase.IPOS_FULL_PACK_NAME)) {
					return pI;
				}
			}
		} catch (Exception e) {
			Log.e(getTag(), e.toString());
		}
		return null;
	}

	public void notifyInstallState(boolean state) {
		iposIsInstalled = state;
		try {
			if (state) {
				iPay();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isAPKFileExist() {
		File file = new File(getIPosReleaseName());
		return file.exists();
	}

	private Runnable checkVersionRunnable = new Runnable() {

		public void run() {

			try {
				if (isCanceled) {
					return;
				}
				if (iposIsInstalled) { // 如果本地安装过
					iPay();
					return;
				} else if (new File(cachePath).exists()) { // 本地未安装，如果解压成功，调起安装
					sendMessage(WHAT_SHOW_INSTALL, cachePath);
				} else {
					sendMessage(WHAT_CLOSE_PROGRESS);
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				closeProgress();
			}
		}

	};

	/**
	 * 获取未安装的APK信息
	 * 
	 * @param context
	 * @param archiveFilePath
	 *            APK文件的路径。如：/sdcard/download/XX.apk
	 */
	private PackageInfo getApkInfo(Context context, String archiveFilePath) {
		PackageManager pm = context.getPackageManager();
		PackageInfo apkInfo = pm.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_META_DATA);
		return apkInfo;
	}

	private boolean retrieveApkFromAssets(String fileName, String path) {
		boolean bRet = false;
		InputStream is = null;
		try {
			is = getContext().getAssets().open(fileName);

			File file = new File(path);
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			byte[] temp = new byte[1024];
			int i = 0;
			@SuppressWarnings("unused")
			int fileLength = 0;
			while ((i = is.read(temp)) > 0) {
				fos.write(temp);
				fileLength += i;
			}
			fos.close();
			is.close();

			bRet = true;
		} catch (IOException e) {
			e.printStackTrace();
			bRet = false;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}

		return bRet;
	}

	public void onDestroy() {
		try {
			getContext().unregisterReceiver(receiver);
		} catch (Exception e) {
			Log.e(getTag(), e.toString());
		}
	}

	public void cancel() {
		// if (this.handler != null) {
		// this.handler.sendEmptyMessage(IPOSID.PAY_CANCEL);
		// }
	}
}
