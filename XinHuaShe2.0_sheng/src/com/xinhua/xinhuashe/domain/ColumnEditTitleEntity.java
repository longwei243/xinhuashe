package com.xinhua.xinhuashe.domain;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class ColumnEditTitleEntity implements Comparable<ColumnEditTitleEntity> {

	private String titleid;
	private String titleName;
	private String titleType;
	private Integer titleIndex;
	private String titleBgColor;

	public ArrayList<ColumnEditTitleEntity> getListEntity(String json) throws Exception {
		ArrayList<ColumnEditTitleEntity> list = new ArrayList<ColumnEditTitleEntity>();
		JSONObject obj = new JSONObject(json);
		JSONArray array1 = obj.getJSONArray("data");
		for (int i = 0; i < array1.length(); i++) {
			JSONObject obj1 = (JSONObject) array1.get(i);
			ColumnEditTitleEntity entity = new ColumnEditTitleEntity();
			entity.setTitleName(obj1.optString("columnname"));
			entity.setTitleid(obj1.optString("id"));
			entity.setTitleIndex(obj1.optInt("index"));
			list.add(entity);
		}
		return list;

	}

	public String getTitleid() {
		return titleid;
	}

	public void setTitleid(String titleid) {
		this.titleid = titleid;
	}

	public String getTitleName() {
		return titleName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}

	public String getTitleType() {
		return titleType;
	}

	public void setTitleType(String titleType) {
		this.titleType = titleType;
	}

	public Integer getTitleIndex() {
		return titleIndex;
	}

	public void setTitleIndex(Integer titleIndex) {
		this.titleIndex = titleIndex;
	}

	public String getTitleBgColor() {
		return titleBgColor;
	}

	public void setTitleBgColor(String titleBgColor) {
		this.titleBgColor = titleBgColor;
	}

	@Override
	public int compareTo(ColumnEditTitleEntity another) {
		return this.getTitleIndex().compareTo(another.getTitleIndex());
	}

}
