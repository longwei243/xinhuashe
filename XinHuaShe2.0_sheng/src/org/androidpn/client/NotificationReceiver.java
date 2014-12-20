/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidpn.client;


import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Broadcast receiver that handles push notification messages from the server.
 * This should be registered as receiver in AndroidManifest.xml.
 * 
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public final class NotificationReceiver extends BroadcastReceiver {

	private static final String LOGTAG = LogUtil.makeLogTag(NotificationReceiver.class);

	// private NotificationService notificationService;

	public NotificationReceiver() {
	}

	// public NotificationReceiver(NotificationService notificationService) {
	// this.notificationService = notificationService;
	// }

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(LOGTAG, "NotificationReceiver.onReceive()...");
		String action = intent.getAction();
		Log.d(LOGTAG, "action=" + action);
		if ((Constants.ACTION_SHOW_NOTIFICATION + context.getPackageName()).equals(action)) {
			String notificationId = intent.getStringExtra(Constants.NOTIFICATION_ID);
			String notificationApiKey = intent.getStringExtra(Constants.NOTIFICATION_API_KEY);
			String notificationTitle = intent.getStringExtra(Constants.NOTIFICATION_TITLE);
			String notificationMessage = intent.getStringExtra(Constants.NOTIFICATION_MESSAGE);
			String notificationUri = intent.getStringExtra(Constants.NOTIFICATION_URI);
			String notificationFrom = intent.getStringExtra(Constants.NOTIFICATION_FROM);
			String packetId = intent.getStringExtra(Constants.PACKET_ID);
			String notificationTitleId = intent.getStringExtra(Constants.NOTIFICATION_TITLEID);
			String notificationAdditional = intent.getStringExtra(Constants.NOTIFICATION_ADDITIONAL);

			Log.d(LOGTAG, "notificationId=" + notificationId);
			Log.d(LOGTAG, "notificationApiKey=" + notificationApiKey);
			Log.d(LOGTAG, "notificationTitle=" + notificationTitle);
			Log.d(LOGTAG, "notificationMessage=" + notificationMessage);
			Log.d(LOGTAG, "notificationUri=" + notificationUri);
			Log.d(LOGTAG, "notificationTitleId=" + notificationTitleId);
			Log.d(LOGTAG, "notificationAdditional=" + notificationAdditional);

			try {
				JSONObject json = new JSONObject(notificationMessage);
				String message = (String) json.get("content");
				String messageType = (String) json.get("messageType");
				String newsId = (String) json.get("newsId");
				System.out.println("messageType is:"+messageType);
				Notifier notifier = new Notifier(context);
				notifier.notify(notificationId, notificationApiKey, notificationTitle, message, notificationUri, notificationFrom, packetId, newsId, messageType);
			
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			/*Notifier notifier = new Notifier(context);
			notifier.notify(notificationId, notificationApiKey, notificationTitle, message, notificationUri, notificationFrom, packetId);
		*/}

	}

}
