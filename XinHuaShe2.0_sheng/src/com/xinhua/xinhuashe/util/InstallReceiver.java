package com.xinhua.xinhuashe.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class InstallReceiver extends BroadcastReceiver {

	private InstallReceiverListener listener;

	@Override
	public void onReceive(Context c, Intent intent) {
		boolean state = false;
		if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
			state = true;
		} else if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
			state = false;
		}
		String packageName = intent.getDataString();

		if (packageName != null && packageName.length() != 0 && packageName.startsWith("package")) {
			packageName = packageName.substring(8);
			if (packageName.equalsIgnoreCase(IPOSBase.IPOS_FULL_PACK_NAME)) {
				if (this.listener != null) {
					this.listener.notifyInstallState(state);
				}
			}
		}
	}

	public void setIPOSUtilsListener(InstallReceiverListener listener) {
		this.listener = listener;
	}

}
