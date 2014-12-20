package com.mdcl.message;

/**
 * 消息回调接口类
 * 
 * @author ningk
 *
 */
public interface MessageCallBack {

	/**
	 * 接受消息回调
	 */
	public void receiveMessage(String messageId, String apiKey, String title, String message, 
			String messageUri, String titleId, String additional, String messageFrom, String packetId);
}
