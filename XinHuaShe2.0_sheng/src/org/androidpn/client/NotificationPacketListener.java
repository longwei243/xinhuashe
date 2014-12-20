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

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;

import android.util.Log;

import com.mdcl.message.MessageCallBack;
import com.mdcl.message.MessageUtil;

/**
 * This class notifies the receiver of incoming notifcation packets
 * asynchronously.
 * 
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class NotificationPacketListener implements PacketListener {

	private static final String LOGTAG = LogUtil.makeLogTag(NotificationPacketListener.class);

	private final XmppManager xmppManager;

	public NotificationPacketListener(XmppManager xmppManager) {
		this.xmppManager = xmppManager;
	}

	@Override
	public void processPacket(Packet packet) {
		Log.d(LOGTAG, "NotificationPacketListener.processPacket()...");
		Log.d(LOGTAG, "packet.toXML()=" + packet.toXML());

		if (packet instanceof NotificationIQ) {
			NotificationIQ notification = (NotificationIQ) packet;

			if (notification.getChildElementXML().contains("androidpn:iq:notification")) {
				String notificationId = notification.getId();
				String notificationApiKey = notification.getApiKey();
				String notificationTitle = notification.getTitle();
				String notificationMessage = notification.getMessage();
				// String notificationTicker = notification.getTicker();
				String notificationUri = notification.getUri();
				String notificationFrom = notification.getFrom();
				String packetId = notification.getPacketID();
				String titleId = notification.getTitleId();
				String additional = notification.getAdditional();

				// TODO FIXME 发送收到通知回执
				IQ result = NotificationIQ.createResultIQ(notification);

				try {
					xmppManager.getConnection().sendPacket(result);
				} catch (Exception e) {
				}

				MessageCallBack callback = MessageUtil.getInstance(xmppManager.getContext(),"").getMessageCallBack();
				if (callback != null) {
					callback.receiveMessage(notificationId, notificationApiKey, notificationTitle, notificationMessage, notificationUri, titleId, additional, notificationFrom, packetId);
				}

				// xmppManager.getContext().sendBroadcast(intent);
			}
		}

	}
}
