package com.oakclub.android.model;

import java.util.ArrayList;

public class HangoutProfileItemObject {
	public ArrayList<HangoutProfileInfoFavor> fav;
	// public ArrayList<> "mutual_friends": [ ],
	String avatar;
	BirthdayObject birthday;
	String birthday_date;
	private String birthday_year;
	private String distance;
	private String viewed;
	private String like;
	int count_photos;
	BirthdayObject created_at;
	String ethnicity;
	String fb_id;
	int gender;
	boolean is_new_user;
	LocationObject location;
	String name;
	String email;

	String profile_id;
	int relationship_status;
	String meet_type;
	int popularity;
	String school;
	String about_me;
	String height;
	String weight;
	String xmpp_username;
	String xmpp_password;
	boolean xmpp_registered;
	int interested;
	String work;
	private String active;
	private int age;
	private String hometown_name;   
	private String location_name;
	private ArrayList<FacebookInfoObject> mutual_friends;
	private ArrayList<FacebookInfoObject> share_interests;
	private String snapshot_id;
	private String video_link;
	
	ArrayList<ListPhotoReturnDataItemObject> photos;

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public BirthdayObject getBirthday() {
		return birthday;
	}

	public void setBirthday(BirthdayObject birthday) {
		this.birthday = birthday;
	}

	public String getBirthday_date() {
		return birthday_date;
	}

	public void setBirthday_date(String birthday_date) {
		this.birthday_date = birthday_date;
	}

	public int getCount_photos() {
		return count_photos;
	}

	public void setCount_photos(int count_photos) {
		this.count_photos = count_photos;
	}

	public BirthdayObject getCreated_at() {
		return created_at;
	}

	public void setCreated_at(BirthdayObject created_at) {
		this.created_at = created_at;
	}

	public String getEthnicity() {
		return ethnicity;
	}

	public void setEthnicity(String ethnicity) {
		this.ethnicity = ethnicity;
	}

	public String getFb_id() {
		return fb_id;
	}

	public void setFb_id(String fb_id) {
		this.fb_id = fb_id;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public boolean isIs_new_user() {
		return is_new_user;
	}

	public void setIs_new_user(boolean is_new_user) {
		this.is_new_user = is_new_user;
	}

	public LocationObject getLocation() {
		return location;
	}

	public void setLocation(LocationObject location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProfile_id() {
		return profile_id;
	}

	public void setProfile_id(String profile_id) {
		this.profile_id = profile_id;
	}

	public int getRelationship_status() {
		return relationship_status;
	}

	public void setRelationship_status(int relationship_status) {
		this.relationship_status = relationship_status;
	}

	public String getMeet_type() {
		return meet_type;
	}

	public void setMeet_type(String meet_type) {
		this.meet_type = meet_type;
	}

	public int getPopularity() {
		return popularity;
	}

	public void setPopularity(int popularity) {
		this.popularity = popularity;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getAbout_me() {
		return about_me;
	}

	public void setAbout_me(String about_me) {
		this.about_me = about_me;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getXmpp_username() {
		return xmpp_username;
	}

	public void setXmpp_username(String xmpp_username) {
		this.xmpp_username = xmpp_username;
	}

	public String getXmpp_password() {
		return xmpp_password;
	}

	public void setXmpp_password(String xmpp_password) {
		this.xmpp_password = xmpp_password;
	}

	public boolean isXmpp_registered() {
		return xmpp_registered;
	}

	public void setXmpp_registered(boolean xmpp_registered) {
		this.xmpp_registered = xmpp_registered;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public int getInterested() {
		return interested;
	}

	public void setInterested(int interested) {
		this.interested = interested;
	}

	public String getBirthday_year() {
		return birthday_year;
	}

	public void setBirthday_year(String birthday_year) {
		this.birthday_year = birthday_year;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getViewed() {
		return viewed;
	}

	public void setViewed(String viewed) {
		this.viewed = viewed;
	}

	public String getLike() {
		return like;
	}

	public void setLike(String like) {
		this.like = like;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public ArrayList<HangoutProfileInfoFavor> getFav() {
		return fav;
	}

	public void setFav(ArrayList<HangoutProfileInfoFavor> fav) {
		this.fav = fav;
	}

	public ArrayList<ListPhotoReturnDataItemObject> getPhotos() {
		return photos;
	}

	public void setPhotos(ArrayList<ListPhotoReturnDataItemObject> photos) {
		this.photos = photos;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getHometown_name() {
		return hometown_name;
	}

	public void setHometown_name(String hometown_name) {
		this.hometown_name = hometown_name;
	}

	public String getLocation_name() {
		return location_name;
	}

	public void setLocation_name(String location_name) {
		this.location_name = location_name;
	}

	public ArrayList<FacebookInfoObject> getMutual_friends() {
		return mutual_friends;
	}

	public void setMutual_friends(ArrayList<FacebookInfoObject> mutual_friends) {
		this.mutual_friends = mutual_friends;
	}

	public ArrayList<FacebookInfoObject> getShare_interests() {
		return share_interests;
	}

	public void setShare_interests(ArrayList<FacebookInfoObject> share_interests) {
		this.share_interests = share_interests;
	}

	public String getSnapshot_id() {
		return snapshot_id;
	}

	public void setSnapshot_id(String snapshot_id) {
		this.snapshot_id = snapshot_id;
	}

	public String getVideo_link() {
		return video_link;
	}

	public void setVideo_link(String video_link) {
		this.video_link = video_link;
	}



}
