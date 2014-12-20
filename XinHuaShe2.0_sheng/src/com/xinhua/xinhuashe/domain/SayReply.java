package com.xinhua.xinhuashe.domain;

/**
 * 有话要说-回复实体类
 * 
 * @author azuryleaves
 * @since 2014-4-15 下午5:15:41
 * @version 1.0
 * 
 */
public class SayReply {

	private String id;
	private String createDate;
	private String reContent;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getReContent() {
		return reContent;
	}

	public void setReContent(String reContent) {
		this.reContent = reContent;
	}

}
