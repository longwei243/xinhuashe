package com.mdcl.message;

/**
 * ��Ϣ�ص��ӿ���
 * 
 * @author ningk
 *
 */
public interface MessageCallBack {

	/**
	 * ������Ϣ�ص�
	 */
	public void receiveMessage(String messageId, String apiKey, String title, String message, 
			String messageUri, String titleId, String additional, String messageFrom, String packetId);
}
