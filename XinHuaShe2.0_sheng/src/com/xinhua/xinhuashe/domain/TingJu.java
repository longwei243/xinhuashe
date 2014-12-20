package com.xinhua.xinhuashe.domain;

import java.io.Serializable;
/**
 * 厅局
 * @author LongWei
 *
 */
public class TingJu implements Serializable{

	private static final long serialVersionUID = 1L;
	private Long id; // 编号
	private String code; // 编号
	private String remarks; // 编号
	private String name; // 栏目名称
	private String image; // 栏目图片
	private String type; // 链接
	private String grade; // 目标（ _blank、_self、_parent、_top）
	private String zipCode; // 描述，填写有助于搜索引擎优化
	private String master; // 关键字，填写有助于搜索引擎优化
	private Integer phone; // 排序（升序）
	private String fax; // 是否需要审核
	private String createDate; // 是否需要审核
	private String updateDate; // 是否需要审核
	private String address; // 是否需要审核
	private String email; // 是否需要审核
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Long getId() {
		return id;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getMaster() {
		return master;
	}
	public void setMaster(String master) {
		this.master = master;
	}
	public Integer getPhone() {
		return phone;
	}
	public void setPhone(Integer phone) {
		this.phone = phone;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	
}
