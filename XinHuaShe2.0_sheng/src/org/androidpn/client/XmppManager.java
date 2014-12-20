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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smack.provider.ProviderManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.util.Log;

import com.mdcl.message.AndroidpnListener;
import com.mdcl.message.MessageUtil;

/**
 * This class is to manage the XMPP connection between client and server.
 * 
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class XmppManager {

	private static final String LOGTAG = LogUtil.makeLogTag(XmppManager.class);

	private static final String XMPP_RESOURCE_NAME = "AndroidpnClient";

	private Context context;

	private NotificationService.TaskSubmitter taskSubmitter;

	private NotificationService.TaskTracker taskTracker;

	private SharedPreferences sharedPrefs;

	private String xmppHost;

	private int xmppPort;

	private XMPPConnection connection;

	private String username;

	private String password;

	private ConnectionListener connectionListener;

	private PacketListener notificationPacketListener;

	private Handler handler;

	private List<Runnable> taskList;

	private boolean running = false;

	private Future<?> futureTask;

	private Thread reconnection;

	private String packageName;
	
	private String name;

	private String mobileType;

	private String deviceId;

	private static XmppManager instance;

	public static XmppManager getXmppManager(NotificationService notificationService) {
		if (instance == null) {
			instance = new XmppManager(notificationService);
		}
		return instance;
	}

	public static XmppManager getXmppManager() {
		return instance;
	}

	private XmppManager(NotificationService notificationService) {
		context = notificationService;
		taskSubmitter = notificationService.getTaskSubmitter();
		taskTracker = notificationService.getTaskTracker();
		sharedPrefs = notificationService.getSharedPreferences();

		xmppHost = sharedPrefs.getString(Constants.XMPP_HOST, "localhost");
		xmppPort = sharedPrefs.getInt(Constants.XMPP_PORT, 5222);
		username = sharedPrefs.getString(Constants.XMPP_USERNAME, "");
		password = sharedPrefs.getString(Constants.XMPP_PASSWORD, "");
		name = sharedPrefs.getString(Constants.XMPP_NAME, "");
		packageName = sharedPrefs.getString(Constants.PACKAGE_NAME, "");
		mobileType = sharedPrefs.getString(Constants.BRAND, "");
		deviceId = sharedPrefs.getString(Constants.DEVICE_ID, "");

		connectionListener = new PersistentConnectionListener(this);
		notificationPacketListener = new NotificationPacketListener(this);

		handler = new Handler();
		taskList = new ArrayList<Runnable>();
		reconnection = new ReconnectionThread(this);
	}

	public Context getContext() {
		return context;
	}

	public void connect() {
		Log.d(LOGTAG, "connect()...");
		submitLoginTask();
	}

	public void openApp() {
		Log.d(LOGTAG, "openApp()...");
		submitOpenAppTask();
	}

	public void disconnect() {
		Log.d(LOGTAG, "disconnect()...");
		terminatePersistentConnection();
	}

	public void terminatePersistentConnection() {
		Log.d(LOGTAG, "terminatePersistentConnection()...");
		Runnable runnable = new Runnable() {

			final XmppManager xmppManager = XmppManager.this;

			public void run() {
				if (xmppManager.isConnected()) {
					Log.d(LOGTAG, "terminatePersistentConnection()... run()");
					xmppManager.getConnection().removePacketListener(xmppManager.getNotificationPacketListener());
					xmppManager.getConnection().disconnect();
				}
				xmppManager.runTask();
			}

		};
		addTask(runnable);
	}

	public XMPPConnection getConnection() {
		return connection;
	}

	public void setConnection(XMPPConnection connection) {
		this.connection = connection;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ConnectionListener getConnectionListener() {
		return connectionListener;
	}

	public PacketListener getNotificationPacketListener() {
		return notificationPacketListener;
	}

	/*
	 * public void startReconnectionThread() { synchronized (reconnection) { if
	 * (!reconnection.isAlive()) {
	 * reconnection.setName("Xmpp Reconnection Thread"); reconnection.start(); }
	 * } }
	 */
	public void startReconnectionThread() {
			synchronized (reconnection) {
				if (!reconnection.isAlive()) {
					reconnection.setName("Xmpp Reconnection Thread");
					reconnection.start();
				} else {
					reconnection.notifyAll();// 通知连接服务器线程启动
				}
			}
	}

	public Handler getHandler() {
		return handler;
	}

	public void reregisterAccount() {
		removeAccount();
		submitLoginTask();
		runTask();
	}

	public List<Runnable> getTaskList() {
		return taskList;
	}

	public Future<?> getFutureTask() {
		return futureTask;
	}

	public void runTask() {
		Log.d(LOGTAG, "runTask()...");
		synchronized (taskList) {
			running = false;
			futureTask = null;
			if (!taskList.isEmpty()) {
				Runnable runnable = (Runnable) taskList.get(0);
				taskList.remove(0);
				Log.d(LOGTAG, "task list size: " + taskList.size());
				running = true;
				futureTask = taskSubmitter.submit(runnable);
				if (futureTask == null) {
					taskTracker.decrease();
				}
			}
		}
		taskTracker.decrease();
		Log.d(LOGTAG, "runTask()...done");
	}

	/*
	 * private boolean isConnected() { return connection != null &&
	 * connection.isConnected(); }
	 */
	
	private boolean isConnected() {
		return connection != null && connection.isConnected();
	}

	public boolean isAuthenticated() {
		return connection != null && connection.isConnected() && connection.isAuthenticated();
	}

	private boolean isRegistered() {
		boolean isRegistered = sharedPrefs.getBoolean(Constants.REGISTERED, true);
		return isRegistered;
		// return sharedPrefs.contains(Constants.XMPP_USERNAME)
		// && sharedPrefs.contains(Constants.XMPP_PASSWORD);
	}

	private void submitConnectTask() {
		Log.d(LOGTAG, "submitConnectTask()...");
		addTask(new ConnectTask());
	}

	private void submitRegisterTask() {
		Log.d(LOGTAG, "submitRegisterTask()...");
		addTask(new RegisterTask());
	}

	private void submitLoginTask() {
		Log.d(LOGTAG, "submitLoginTask()...");
		if (!isConnected()) {
			submitConnectTask();
		}
		// 获取是否需要注册
		boolean isRegedit = sharedPrefs.getBoolean(Constants.REGISTERED, true);
		if (isRegedit) {
			Log.d(LOGTAG, "begin regedit");
			submitRegisterTask();
		}

		if (!isAuthenticated()) {
			addTask(new LoginTask());
		}
	}

	private void submitOpenAppTask() {
		Log.d(LOGTAG, "submitOpenAppTask()...");
		if (!isConnected()) {
			submitConnectTask();
		}
		boolean isRegedit = sharedPrefs.getBoolean(Constants.REGISTERED, true);
		if (isRegedit) {
			Log.d(LOGTAG, "begin regedit");
			submitRegisterTask();
		}
		if (!isAuthenticated()) {
			addTask(new LoginTask());
		}

		addTask(new openAppTask());
	}

	private void addTask(Runnable runnable) {
		Log.d(LOGTAG, "addTask(runnable)..." + runnable.getClass());
		taskTracker.increase();
		synchronized (taskList) {
			if (taskList.isEmpty() && !running) {
				running = true;
				futureTask = taskSubmitter.submit(runnable);
				if (futureTask == null) {
					taskTracker.decrease();
				}
			} else {
				taskList.add(runnable);
			}
		}
		Log.d(LOGTAG, "addTask(runnable)... done");
	}

	private void removeAccount() {
		Editor editor = sharedPrefs.edit();
		editor.remove(Constants.XMPP_USERNAME);
		editor.remove(Constants.XMPP_PASSWORD);
		editor.remove(Constants.XMPP_NAME);
		//true 注册
		editor.putBoolean(Constants.REGISTERED, true);
		editor.commit();
	}

	/**
	 * A runnable task to connect the server.
	 */
	private class ConnectTask implements Runnable {

		final XmppManager xmppManager;

		private ConnectTask() {
			this.xmppManager = XmppManager.this;
		}

		public void run() {
			Log.i(LOGTAG, "ConnectTask.run()...");

			if (!xmppManager.isConnected()) {
				// Create the configuration for this new connection
				ConnectionConfiguration connConfig = new ConnectionConfiguration(xmppHost, xmppPort);
				// connConfig.setSecurityMode(SecurityMode.disabled);
				connConfig.setSecurityMode(SecurityMode.required);
				connConfig.setSASLAuthenticationEnabled(false);
				connConfig.setCompressionEnabled(false);

				XMPPConnection connection = new XMPPConnection(connConfig);
				xmppManager.setConnection(connection);
				AndroidpnListener androidpnListener = MessageUtil.getInstance(xmppManager.getContext(),"").getAndroidpnListener();
				try {
					// Connect to the server
					connection.connect();
					Log.i(LOGTAG, "XMPP connected successfully");
					androidpnListener.connectedSuccess();
					// packet provider
					ProviderManager.getInstance().addIQProvider("notification", "androidpn:iq:notification", new NotificationIQProvider());

				} catch (XMPPException e) {
					androidpnListener.connectedFail();
					Log.e(LOGTAG, "XMPP connection failed", e);
				}

				xmppManager.runTask();

			} else {
				Log.i(LOGTAG, "XMPP connected already");
				xmppManager.runTask();
			}
		}
	}

	/**
	 * A runnable task to register a new user onto the server.
	 */
	private class RegisterTask implements Runnable {

		final XmppManager xmppManager;

		private RegisterTask() {
			xmppManager = XmppManager.this;
		}

		public void run() {
			Log.i(LOGTAG, "RegisterTask.run()...");
			final AndroidpnListener androidpnListener = MessageUtil.getInstance(xmppManager.getContext(),"").getAndroidpnListener();
			if (xmppManager.isRegistered()) {
				final String newUsername = sharedPrefs.getString(Constants.XMPP_USERNAME, packageName + deviceId);

				Registration registration = new Registration();

				PacketFilter packetFilter = new AndFilter(new PacketIDFilter(registration.getPacketID()), new PacketTypeFilter(IQ.class));

				PacketListener packetListener = new PacketListener() {

					public void processPacket(Packet packet) {
						Log.d("RegisterTask.PacketListener", "processPacket().....");
						Log.d("RegisterTask.PacketListener", "packet=" + packet.toXML());

						if (packet instanceof IQ) {
							Registration response = (Registration) packet;
							Log.d(LOGTAG, packet.toXML());
							if (response.getType() == IQ.Type.ERROR) {
								if (!response.getError().toString().contains("409")) {
									Log.e(LOGTAG, "Unknown error while registering XMPP account! " + response.getError().getCondition());
								}
								androidpnListener.connectedFail();
							} else if (response.getType() == IQ.Type.RESULT) {
								xmppManager.setUsername(newUsername);
								String newPassword = response.getAttributes().get("password");
								xmppManager.setPassword(newPassword);
								Log.d(LOGTAG, "username=" + newUsername);
								Log.d(LOGTAG, "password=" + newPassword);

								Editor editor = sharedPrefs.edit();
								editor.putString(Constants.XMPP_USERNAME, newUsername);
								editor.putString(Constants.XMPP_PASSWORD, newPassword);

								editor.putBoolean(Constants.REGISTERED, false);
								editor.commit();
								androidpnListener.connectedSuccess();
								Log.i(LOGTAG, "Account registered successfully");
								xmppManager.runTask();
							}
						}
					}
				};

				connection.addPacketListener(packetListener, packetFilter);

				registration.setType(IQ.Type.SET);
				registration.addAttribute("username", packageName + deviceId);
				registration.addAttribute("password", null);
				registration.addAttribute("name", name);
				registration.addAttribute("appPackage", packageName);
				registration.addAttribute("mobileType", mobileType);
				registration.addAttribute("deviceId", deviceId);
				connection.sendPacket(registration);

			} else {
				Log.i(LOGTAG, "Account registered already");
				xmppManager.runTask();
			}
		}
	}

	/**
	 * A runnable task to log into the server.
	 */
	private class LoginTask implements Runnable {

		final XmppManager xmppManager;

		private LoginTask() {
			this.xmppManager = XmppManager.this;
		}

		public void run() {
			Log.i(LOGTAG, "LoginTask.run()...");
			AndroidpnListener androidpnListener = MessageUtil.getInstance(xmppManager.getContext(),"").getAndroidpnListener();
			if (!xmppManager.isAuthenticated()) {
			/*	Log.d(LOGTAG, "username=" + username);
				Log.d(LOGTAG, "password=" + password);*/

				try {
					xmppManager.getConnection().login(username, password, XMPP_RESOURCE_NAME);
					Log.d(LOGTAG, "Loggedn in successfully");
					// connection listener
					if (xmppManager.getConnectionListener() != null) {
						xmppManager.getConnection().addConnectionListener(xmppManager.getConnectionListener());
					}

					// packet filter
					PacketFilter packetFilter = new PacketTypeFilter(NotificationIQ.class);
					// packet listener
					PacketListener packetListener = xmppManager.getNotificationPacketListener();
					connection.addPacketListener(packetListener, packetFilter);

					getConnection().startKeepAliveThread(xmppManager);
					androidpnListener.loginSuccess();

				} catch (XMPPException e) {
					e.printStackTrace();
					androidpnListener.loginFail();
					Log.e(LOGTAG, "LoginTask.run()... xmpp error");
					Log.e(LOGTAG, "Failed to login to xmpp server. Caused by: " + e.getMessage());
					String INVALID_CREDENTIALS_ERROR_CODE = "401";
					String errorMessage = e.getMessage();
					if (errorMessage != null && errorMessage.contains(INVALID_CREDENTIALS_ERROR_CODE)) {
						xmppManager.reregisterAccount();
						return;
					}
					xmppManager.startReconnectionThread();
					
				} catch (Exception e) {
					Log.e(LOGTAG, "LoginTask.run()... other error");
					Log.e(LOGTAG, "Failed to login to xmpp server. Caused by: " + e.getMessage());
					xmppManager.startReconnectionThread();
					androidpnListener.loginFail();
				}
				xmppManager.runTask();
			} else {
				Log.i(LOGTAG, "Logged in already");
				xmppManager.runTask();
			}

		}
	}

	/**
     *
     */
	private class openAppTask implements Runnable {

		final XmppManager xmppManager;

		private openAppTask() {
			this.xmppManager = XmppManager.this;
		}

		public void run() {
			Log.i(LOGTAG, "OpenAppTask.run()...");

			if (xmppManager.isAuthenticated()) {
				Registration registration = new Registration();

				PacketFilter packetFilter = new AndFilter(new PacketIDFilter(registration.getPacketID()), new PacketTypeFilter(IQ.class));

				PacketListener packetListener = new PacketListener() {

					public void processPacket(Packet packet) {
						Log.d("openApp.PacketListener", "processPacket().....");
						Log.d("openApp.PacketListener", "packet=" + packet.toXML());

						if (packet instanceof IQ) {
							Registration response = (Registration) packet;
							Log.d(LOGTAG, packet.toXML());
							if (response.getType() == IQ.Type.ERROR) {
								if (!response.getError().toString().contains("409")) {
									Log.e(LOGTAG, "Unknown error while registering XMPP account! " + response.getError().getCondition());
								}
							} else if (response.getType() == IQ.Type.RESULT) {
								Log.i(LOGTAG, "openApp successfully");
								xmppManager.runTask();
							}
						}
					}
				};

				connection.addPacketListener(packetListener, packetFilter);

				registration.setType(IQ.Type.SET);
				registration.addAttribute("username", packageName + deviceId);
				registration.addAttribute("password", null);
				registration.addAttribute("appPackage", packageName);
				registration.addAttribute("mobileType", mobileType);
				registration.addAttribute("deviceId", deviceId);
				registration.addAttribute("openApp", packageName);

				Log.d("openApp", "openApp=" + registration.toXML());
				connection.sendPacket(registration);
			} else {
				Log.i(LOGTAG, "Logged in already");
				xmppManager.runTask();
			}

		}
	}

}
