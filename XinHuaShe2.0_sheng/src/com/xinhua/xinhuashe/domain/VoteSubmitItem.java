package com.xinhua.xinhuashe.domain;

import java.util.List;

public class VoteSubmitItem {
	
	private long id;
    private String title;    // 标题
    private String content;// 内容
    protected String createDate;// 创建日期
    private String delFlag; // 删除标记（0：开启；1：删除；2：未开启）
    private String path;//图片路径
    private Integer stype; // 类型
    private int hits;
    public int getHits() {
		return hits;
	}
	public void setHits(int hits) {
		this.hits = hits;
	}
	private List<PostChoose> postChooseList ;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Integer getStype() {
		return stype;
	}
	public void setStype(Integer stype) {
		this.stype = stype;
	}
	public List<PostChoose> getPostChooseList() {
		return postChooseList;
	}
	public void setPostChooseList(List<PostChoose> postChooseList) {
		this.postChooseList = postChooseList;
	}
}
