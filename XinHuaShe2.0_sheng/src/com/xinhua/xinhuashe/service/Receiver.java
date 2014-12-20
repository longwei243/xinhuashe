package com.xinhua.xinhuashe.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class Receiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		if (info != null && info.isConnected()) {
			// 判断当前网络是否已经连接
			if (info.getState() == NetworkInfo.State.CONNECTED) {
				
			} else {
				Toast.makeText(context, "网络连接错误，请检查网络", Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(context, "网络连接错误，请检查网络", Toast.LENGTH_SHORT).show();
		}
	}
	
}
