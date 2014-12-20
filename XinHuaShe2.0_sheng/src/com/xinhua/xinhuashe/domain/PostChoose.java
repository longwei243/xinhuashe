package com.xinhua.xinhuashe.domain;

public class PostChoose {
	private Long id;
	private String content;// 内容

	protected String createDate;// 创建日期

	private String delFlag; // 删除标记（0：正常；1：删除；）

	private Integer hits = 0;// 选择次数

	private String choose;// 选项（比如ABCD)

	public String getChoose() {
		return choose;
	}

	public String getContent() {
		return content;
	}

	public String getCreateDate() {
		return createDate;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public Integer getHits() {
		return hits;
	}

	public Long getId() {
		return id;
	}

	public void setChoose(String choose) {
		this.choose = choose;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public void setHits(Integer hits) {
		this.hits = hits;
	}
	public void setId(Long id) {
		this.id = id;
	}
}
