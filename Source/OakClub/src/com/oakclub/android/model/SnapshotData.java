package com.oakclub.android.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.xbill.DNS.tests.primary;

public class SnapshotData implements Serializable {
    private boolean isFromChat = false;
    private int age = 0;
    private ArrayList<ListPhotoReturnDataItemObject> photos;
    private String video_link = "";
    private String profile_id = "";
    private String name = "";
    private String avatar = "";
    private ArrayList<FacebookInfoObject> mutual_friends;
    private ArrayList<FacebookInfoObject> share_interests;
    private String like = "";
    private String viewed = "";
    private float distance =0;
    private int active = 0;
    private String about_me = "";
    private boolean is_like = false;
    private String like_time;
    
    private String location_name = "";
    private String hometown_name = ""; 
    private String birthday_date = "";
    //private String schools = "";
    private String school;
    private int work = 59 ;
    private int interested = 0;
    private boolean is_verify;
    private boolean is_vip=false;
    
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public ArrayList<ListPhotoReturnDataItemObject> getPhotos() {
        return photos;
    }
    public void setPhotos(ArrayList<ListPhotoReturnDataItemObject> photos) {
        this.photos = photos;
    }
    public String getVideo_link() {
        return video_link;
    }
    public void setVideo_link(String video_link) {
        this.video_link = video_link;
    }
    public String getProfile_id() {
        return profile_id;
    }
    public void setProfile_id(String profile_id) {
        this.profile_id = profile_id;
    }
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
    public int getLike() {
        return Integer.parseInt(like);
    }
    public void setLike(String i) {
        this.like = ""+ i;
    }
    public int getViewed() {
        return Integer.parseInt(viewed);
    }
    public void setViewed(String viewed) {
        this.viewed = ""+viewed;
    }
    public float getDistance() {
        return distance;
    }
    public void setDistance(float distance) {
        this.distance = distance;
    }
    public int getActive() {
        return active;
    }
    public void setActive(int active) {
        this.active = active;
    }
    public String getAbout_me() {
        return about_me;
    }
    public void setAbout_me(String about_me) {
        this.about_me = about_me;
    }
	public boolean getIs_like() {
		return is_like;
	}
	public void setIs_like(boolean is_like) {
		this.is_like = is_like;
	}
	public String getLocation_name() {
		return location_name;
	}
	public void setLocation_name(String location_name) {
		this.location_name = location_name;
	}
	public int getWork() {
		return work;
	}
	public void setWork(int work) {
		this.work = work;
	}
	public int getInterested() {
		return interested;
	}
	public void setInterested(int interested) {
		this.interested = interested;
	}
	public String getBirthday_date() {
		return birthday_date;
	}
	public void setBirthday_date(String birthday_date) {
		this.birthday_date = birthday_date;
	}
	public String getHometown_name() {
		return hometown_name;
	}
	public void setHometown_name(String hometown_name) {
		this.hometown_name = hometown_name;
	}
//	public String getSchools() {
//		return schools;
//	}
//	public void setSchools(String schools) {
//		this.schools = schools;
//	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
    public boolean isFromChat() {
        return isFromChat;
    }
    public void setFromChat(boolean isFromChat) {
        this.isFromChat = isFromChat;
    }
	public String getLike_time() {
		return like_time;
	}
	public void setLike_time(String like_time) {
		this.like_time = like_time;
	}
	public boolean getIs_verify() {
		return is_verify;
	}
	public void setIs_verify(boolean is_verify) {
		this.is_verify = is_verify;
	}
    public boolean isIs_vip() {
        return is_vip;
    }
    public void setIs_vip(boolean is_vip) {
        this.is_vip = is_vip;
    }
}
