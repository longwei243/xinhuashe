package com.xinhua.xinhuashe.domain;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * 栏目Entity
 * 
 * @author ThinkGem
 * @version 2013-05-15
 */
public class Category implements Serializable, Comparable<Category> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	private Long id; // 编号
	private String parentIds;// 所有父级编号
	private String module; // 栏目模型（article：文章；picture：图片；download：下载；link：链接；special：专题）
	private String name; // 栏目名称
	private String image; // 栏目图片
	private String href; // 链接
	private String target; // 目标（ _blank、_self、_parent、_top）
	private String description; // 描述，填写有助于搜索引擎优化
	private String keywords; // 关键字，填写有助于搜索引擎优化
	private Integer sort; // 排序（升序）
	private String isAudit; // 是否需要审核
	private String createDate;
	private String updateDate;

	private LinkedList<Category> childList; // 拥有子分类列表

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getIsAudit() {
		return isAudit;
	}

	public void setIsAudit(String isAudit) {
		this.isAudit = isAudit;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public LinkedList<Category> getChildList() {
		return childList;
	}

	public void setChildList(LinkedList<Category> childList) {
		this.childList = childList;
	}

	@Override
	public int compareTo(Category another) {
		return this.getId().compareTo(another.getId());
	}

}