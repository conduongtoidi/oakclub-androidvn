package com.oakclub.android.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ProfileInfoData implements Serializable {
    private String fb_id = "";
	private String about_me = "";
	private String avatar = "";
	private String birthday_date = "";
	private BirthdayObject created_at;
	private int gender = 1;
	private int interested  = 0 ;
	private boolean is_vip = false ;
	private ArrayList<Integer> language;
	private LocationObject location;
	private String name = "";
	private String profile_id = "";
	private int relationship_status = 0;
	private String username = "";
	private String weight = "";
	private String school = "";
	private int work = 59;
	private int ethnicity = 0;
	private String height = "";
	private String video_link = "";
	private ArrayList<HangoutProfileInfoFavor> fav;
	private String xmpp_username = "";
	private String xmpp_password = "";
	private String email ="";
	ArrayList<ListPhotoReturnDataItemObject> photos;

	public ArrayList<ListPhotoReturnDataItemObject> getPhotos() {
		return photos;
	}

	public void setPhotos(ArrayList<ListPhotoReturnDataItemObject> photos) {
		this.photos = photos;
	}

	public String getAbout_me() {
		return about_me;
	}

	public void setAbout_me(String about_me) {
		this.about_me = about_me;
	}
	
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getBirthday_date() {
		return birthday_date;
	}

	public void setBirthday_date(String birthday_date) {
		this.birthday_date = birthday_date;
	}

	public BirthdayObject getCreated_at() {
		return created_at;
	}

	public void setCreated_at(BirthdayObject created_at) {
		this.created_at = created_at;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public int getInterested() {
		return interested;
	}

	public void setInterested(int interested) {
		this.interested = interested;
	}

	public boolean isIs_vip() {
		return is_vip;
	}

	public void setIs_vip(boolean is_vip) {
		this.is_vip = is_vip;
	}


	public ArrayList<Integer> getLanguage() {
		return language;
	}

	public void setLanguage(ArrayList<Integer> language) {
		this.language = language;
	}

	public int getWork() {
		return work;
	}

	public void setWork(int work) {
		this.work = work;
	}

	public int getEthnicity() {
		return ethnicity;
	}

	public void setEthnicity(int ethnicity) {
		this.ethnicity = ethnicity;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}


	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getVideo_link() {
		return video_link;
	}

	public void setVideo_link(String video_link) {
		this.video_link = video_link;
	}

	public ArrayList<HangoutProfileInfoFavor> getFav() {
		return fav;
	}

	public void setFav(ArrayList<HangoutProfileInfoFavor> fav) {
		this.fav = fav;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

    public String getFb_id() {
        return fb_id;
    }

    public void setFb_id(String fb_id) {
        this.fb_id = fb_id;
    }
}
