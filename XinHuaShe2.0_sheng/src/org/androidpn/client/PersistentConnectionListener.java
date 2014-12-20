package org.androidpn.client;

import org.jivesoftware.smack.ConnectionListener;

import android.util.Log;

import com.mdcl.message.AndroidpnListener;
import com.mdcl.message.MessageUtil;

public class PersistentConnectionListener implements ConnectionListener {

	private static final String LOGTAG = LogUtil.makeLogTag(PersistentConnectionListener.class);

	private final XmppManager xmppManager;
	AndroidpnListener androidpnListener;

	public PersistentConnectionListener(XmppManager xmppManager) {
		this.xmppManager = xmppManager;
		this.androidpnListener = MessageUtil.getInstance(xmppManager.getContext(),"").getAndroidpnListener();
	}

	@Override
	public void connectionClosed() {
		androidpnListener.connectedFail();
		Log.d(LOGTAG, "connectionClosed()...");
	}

	@Override
	public void connectionClosedOnError(Exception e) {
		Log.d(LOGTAG, "connectionClosedOnError()...");
		if (xmppManager.getConnection() != null && xmppManager.getConnection().isConnected()) {
			xmppManager.getConnection().disconnect();
		}
		xmppManager.startReconnectionThread();
		androidpnListener.connectedFail();
	}

	@Override
	public void reconnectingIn(int seconds) {
		Log.d(LOGTAG, "reconnectingIn()...");
	}

	@Override
	public void reconnectionFailed(Exception e) {
		androidpnListener.connectedFail();
		Log.d(LOGTAG, "reconnectionFailed()...");
	}

	@Override
	public void reconnectionSuccessful() {
		androidpnListener.connectedSuccess();
		Log.d(LOGTAG, "reconnectionSuccessful()...");
	}

}
