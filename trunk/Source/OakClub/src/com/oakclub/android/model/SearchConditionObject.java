package com.oakclub.android.model;

public class SearchConditionObject {
	int age_from;
	int age_to;
	String fb_id;
	String gender;
	String gender_of_search;
	LocationObject location;
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
	public String getFb_id() {
		return fb_id;
	}
	public void setFb_id(String fb_id) {
		this.fb_id = fb_id;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getGender_of_search() {
		return gender_of_search;
	}
	public void setGender_of_search(String gender_of_search) {
		this.gender_of_search = gender_of_search;
	}
	public LocationObject getLocation() {
		return location;
	}
	public void setLocation(LocationObject location) {
		this.location = location;
	}
}
