package com.oakclub.android.model;

public class GetSnapshotSettingsReturnDataObject {
	private boolean include_friend;
	private String gender_of_search;
	private int range;
	int age_from;
	private int age_to;
	
	public String getGender_of_search() {
		return gender_of_search;
	}
	public void setGender_of_search(String gender_of_search) {
		this.gender_of_search = gender_of_search;
	}
	public boolean isInclude_friend() {
		return include_friend;
	}
	public void setInclude_friend(boolean include_friend) {
		this.include_friend = include_friend;
	}
	public int getRange() {
		return range;
	}
	public void setRange(int range) {
		this.range = range;
	}
	public int getAge_from() {
		return age_from;
	}
	public void setAge_from(int age_from) {
		this.age_from = age_from;
	}
	public int getAge_to() {
		return age_to;
	}
	public void setAge_to(int age_to) {
		this.age_to = age_to;
	}
	
}
