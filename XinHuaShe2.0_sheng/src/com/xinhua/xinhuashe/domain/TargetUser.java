package com.xinhua.xinhuashe.domain;

/**
 * 有话要说-目标用户
 * 
 * @author azuryleaves
 * @since 2014-4-15 下午4:30:49
 * @version 1.0
 * 
 */
public class TargetUser {

	private String id;
	private String name;

	public TargetUser(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
