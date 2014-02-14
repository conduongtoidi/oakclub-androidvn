package com.oakclub.android.model;

import java.util.ArrayList;

public class ProfileInfoOtherData {
    private String about_me;
    private int approved;
    private String avatar;
    private String birthday_date;
    private int count_photos;
    private BirthdayObject created_at;
    private int ethnicity;
    private int gender;
    private int interested;
    private boolean is_vip;
    private ArrayList<String> language;
    private LocationObject location;
    private String name;
    private String profile_id;
    private int relationship_status;
    private String username;
    private String weight;
    private String school;
    private int work;
    private int height;
    private String video_link;
    private int distance;
    private int active;
    private ArrayList<FacebookInfoObject> mutual_friends;
    private ArrayList<FacebookInfoObject> share_interests;
    private int like;
    private int viewed;
    //private ArrayList<HangoutProfileInfoFavor> fav;
    public String getAbout_me() {
        return about_me;
    }
    public void setAbout_me(String about_me) {
        this.about_me = about_me;
    }
    public int getApproved() {
        return approved;
    }
    public void setApproved(int approved) {
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
    public int getEthnicity() {
        return ethnicity;
    }
    public void setEthnicity(int ethnicity) {
        this.ethnicity = ethnicity;
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
    public ArrayList<String> getLanguage() {
        return language;
    }
    public void setLanguage(ArrayList<String> language) {
        this.language = language;
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
    public String getFirstName(){
    	return name.length()>10?name.substring(0, 7)+"...":name;
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
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public String getVideo_link() {
        return video_link;
    }
    public void setVideo_link(String video_link) {
        this.video_link = video_link;
    }
    public int getDistance() {
        return distance;
    }
    public void setDistance(int distance) {
        this.distance = distance;
    }
    public int getActive() {
        return active;
    }
    public void setActive(int active) {
        this.active = active;
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
        return like;
    }
    public void setLike(int like) {
        this.like = like;
    }
    public int getViewed() {
        return viewed;
    }
    public void setViewed(int viewed) {
        this.viewed = viewed;
    }
    
}
