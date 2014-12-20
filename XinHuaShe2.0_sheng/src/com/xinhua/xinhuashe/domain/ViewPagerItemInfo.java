package com.xinhua.xinhuashe.domain;

import java.io.Serializable;

/**
 * ViewPager子页面ParentFragment信息 页面属性，包括标题、ID等信息
 * 
 * @author azuryleaves
 * @since 2014-3-14 上午10:18:00
 * @version 1.0
 * 
 */
public class ViewPagerItemInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String columnId;
	private String title;
	private String module; // -- 文章(article)或视频(link)
	private String id;	//-- 文章ID
	/**
	 * 主要用于区分布局相同的Fragment发起相同TaskId请求返回的数据
	 */
	private int position;

	public ViewPagerItemInfo(String title, String id, int position) {
		super();
		this.title = title;
		this.id = id;
		this.position = position;
	}

	public ViewPagerItemInfo(String title, String module, String id,
			int position) {
		super();
		this.title = title;
		this.module = module;
		this.id = id;
		this.position = position;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getColumnId() {
		return columnId;
	}

	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}

}
