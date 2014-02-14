package com.oakclub.android.model;

import java.util.ArrayList;

public class HangoutProfileOtherData {
	private String fb_id;
    private String name;
    private String birthday_date;
    private String gender;
    private String relationship_status;
    private int interested;
    private String profile_id;
    private String access_token;
    private LocationObject location;
    private String ethnicity;
    private ArrayList<Integer> language;
    private String status_message;
    private boolean is_private;
    private boolean is_enabled;
    private boolean is_star;
    private boolean approved;
    private BirthdayObject created_at;
    private String username;
    private String email;
    private String is_online;
    private String about_me;
    private boolean is_vip;
    private String height;
    private String weight;
    private String last_login;
    private String video_link;
    private String xmpp_username;
    private String xmpp_password;
    private ArrayList<SchoolsObject> schools;
    private ArrayList<WorkCategoryObject> works;
    private ArrayList<HangoutProfileInfoFavor> favs;
    private String school;
    private int work;
    private ArrayList<HangoutProfileInfoFavor> fav;
    private String avatar;
    private ArrayList<ListPhotoReturnDataItemObject> photos;
    private String viewed;
    private String like;
    private int active;
    
    
    private String birthday_year;
    private String count_photos;

    private boolean is_new_user;
    
    
    
    private int rebuild_data;
    
    private int roster_count;
    private int roster_count_per_day;
    private BirthdayObject roster_modified_at;
    
    private ArrayList<FacebookInfoObject> mutual_friends;
    private ArrayList<FacebookInfoObject> share_interests;
    
    private int age;
    private float distance;
    private String hometown_name;
    private boolean is_like;
    private String location_name;
    
    private String snapshot_id;
    
    
	
    
    private String meet_type;
    private int popularity;
    
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
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getStatus_message() {
        return status_message;
    }
    public void setStatus_message(String status_message) {
        this.status_message = status_message;
    }
    public String getAbout_me() {
        return about_me;
    }
    public void setAbout_me(String about_me) {
        this.about_me = about_me;
    }
    public boolean getApproved() {
        return approved;
    }
    public void setApproved(boolean approved) {
        this.approved = approved;
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
    public String getBirthday_year() {
        return birthday_year;
    }
    public void setBirthday_year(String birthday_year) {
        this.birthday_year = birthday_year;
    }
    public String getCount_photos() {
        return count_photos;
    }
    public void setCount_photos(String count_photos) {
        this.count_photos = count_photos;
    }
    public BirthdayObject getCreated_at() {
        return created_at;
    }
    public void setCreated_at(BirthdayObject created_at) {
        this.created_at = created_at;
    }
    public ArrayList<HangoutProfileInfoFavor> getFav() {
        return fav;
    }
    public void setFav(ArrayList<HangoutProfileInfoFavor> fav) {
        this.fav = fav;
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
    public int getInterested() {
        return interested;
    }
    public void setInterested(int interested) {
        this.interested = interested;
    }
    public boolean isIs_enabled() {
        return is_enabled;
    }
    public void setIs_enabled(boolean is_enabled) {
        this.is_enabled = is_enabled;
    }
    public boolean isIs_new_user() {
        return is_new_user;
    }
    public void setIs_new_user(boolean is_new_user) {
        this.is_new_user = is_new_user;
    }
    public boolean isIs_private() {
        return is_private;
    }
    public void setIs_private(boolean is_private) {
        this.is_private = is_private;
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
    public LocationObject getLocation() {
        return location;
    }
    public void setLocation(LocationObject location) {
        this.location = location;
    }
    public int getRebuild_data() {
        return rebuild_data;
    }
    public void setRebuild_data(int rebuild_data) {
        this.rebuild_data = rebuild_data;
    }
    public String getRelationship_status() {
        return relationship_status;
    }
    public void setRelationship_status(String relationship_status) {
        this.relationship_status = relationship_status;
    }
    public int getRoster_count() {
        return roster_count;
    }
    public void setRoster_count(int roster_count) {
        this.roster_count = roster_count;
    }
    public int getRoster_count_per_day() {
        return roster_count_per_day;
    }
    public void setRoster_count_per_day(int roster_count_per_day) {
        this.roster_count_per_day = roster_count_per_day;
    }
    public BirthdayObject getRoster_modified_at() {
        return roster_modified_at;
    }
    public void setRoster_modified_at(BirthdayObject roster_modified_at) {
        this.roster_modified_at = roster_modified_at;
    }
    public String getWeight() {
        return weight;
    }
    public void setWeight(String weight) {
        this.weight = weight;
    }
    public String getSchool() {
        return school;
    }
    public void setSchool(String school) {
        this.school = school;
    }
    public int getWork() {
        return work;
    }
    public void setWork(int work) {
        this.work = work;
    }
    public String getHeight() {
        return height;
    }
    public void setHeight(String height) {
        this.height = height;
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
    public String getLike() {
        return like;
    }
    public void setLike(String like) {
        this.like = like;
    }
    public String getViewed() {
        return viewed;
    }
    public void setViewed(String viewed) {
        this.viewed = viewed;
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
    public int getActive() {
		return active;
	}
	public void setActive(int active) {
		this.active = active;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public float getDistance() {
		return distance;
	}
	public void setDistance(float distance) {
		this.distance = distance;
	}
	public String getHometown_name() {
		return hometown_name;
	}
	public void setHometown_name(String hometown_name) {
		this.hometown_name = hometown_name;
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

	public ArrayList<ListPhotoReturnDataItemObject> getPhotos() {
		return photos;
	}
	public void setPhotos(ArrayList<ListPhotoReturnDataItemObject> photos) {
		this.photos = photos;
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

	public ArrayList<SchoolsObject> getSchools() {
		return schools;
	}
	public void setSchools(ArrayList<SchoolsObject> schools) {
		this.schools = schools;
	}

	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getEthnicity() {
		return ethnicity;
	}
	public void setEthnicity(String ethnicity) {
		this.ethnicity = ethnicity;
	}

	public boolean getIs_star() {
		return is_star;
	}
	public void setIs_star(boolean is_star) {
		this.is_star = is_star;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getIs_online() {
		return is_online;
	}
	public void setIs_online(String is_online) {
		this.is_online = is_online;
	}
	public String getLast_login() {
		return last_login;
	}
	public void setLast_login(String last_login) {
		this.last_login = last_login;
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
	public ArrayList<WorkCategoryObject> getWorks() {
		return works;
	}
	public void setWorks(ArrayList<WorkCategoryObject> works) {
		this.works = works;
	}
	public ArrayList<HangoutProfileInfoFavor> getFavs() {
		return favs;
	}
	public void setFavs(ArrayList<HangoutProfileInfoFavor> favs) {
		this.favs = favs;
	}    
}
