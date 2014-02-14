package com.oakclub.android.model;

public class GetAccountSettingsReturnDataObject {
	String name;
	String avatar;
	String gender;
	int points;
	int unread_messages;
	int count_unlock;
	String profile_status;
	SearchConditionObject search_condition;
	String purpose_of_search;
	int range;
	int age_from;
	int age_to;
	StatusInterestedInObject status_interested_in;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	public int getUnread_messages() {
		return unread_messages;
	}
	public void setUnread_messages(int unread_messages) {
		this.unread_messages = unread_messages;
	}
	public int getCount_unlock() {
		return count_unlock;
	}
	public void setCount_unlock(int count_unlock) {
		this.count_unlock = count_unlock;
	}
	public String getProfile_status() {
		return profile_status;
	}
	public void setProfile_status(String profile_status) {
		this.profile_status = profile_status;
	}
	public SearchConditionObject getSearch_condition() {
		return search_condition;
	}
	public void setSearch_condition(SearchConditionObject search_condition) {
		this.search_condition = search_condition;
	}
	public String getPurpose_of_search() {
		return purpose_of_search;
	}
	public void setPurpose_of_search(String purpose_of_search) {
		this.purpose_of_search = purpose_of_search;
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
	public StatusInterestedInObject getStatus_interested_in() {
		return status_interested_in;
	}
	public void setStatus_interested_in(
			StatusInterestedInObject status_interested_in) {
		this.status_interested_in = status_interested_in;
	}

	
}
