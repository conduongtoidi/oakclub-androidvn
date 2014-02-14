package com.oakclub.android.model;

import org.xbill.DNS.tests.primary;

public class ListChatData {
    private String fid;
    private String profile_id;
    private boolean is_blocked;
    private boolean is_blocked_by_user;
    private boolean is_deleted;
    private boolean is_deleted_by_user;
    private boolean is_new_contact;
    private double order;
    private int  unread_count;
    private boolean matches;
    private String name;
    private String avatar;
    private int status;
    private String time;
    private String last_message;
    private String last_message_time;
    private boolean is_vip;
    private String match_time;
    private String time_string;
    
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
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getLast_message() {
        return last_message;
    }
    public void setLast_message(String last_message) {
        this.last_message = last_message;
    }
    public String getFid() {
        return fid;
    }
    public void setFid(String fid) {
        this.fid = fid;
    }
    public String getProfile_id() {
        return profile_id;
    }
    public void setProfile_id(String profile_id) {
        this.profile_id = profile_id;
    }
    public boolean isIs_blocked() {
        return is_blocked;
    }
    public void setIs_blocked(boolean is_blocked) {
        this.is_blocked = is_blocked;
    }
    public boolean isIs_blocked_by_user() {
        return is_blocked_by_user;
    }
    public void setIs_blocked_by_user(boolean is_blocked_by_user) {
        this.is_blocked_by_user = is_blocked_by_user;
    }
    public boolean isIs_deleted() {
        return is_deleted;
    }
    public void setIs_deleted(boolean is_deleted) {
        this.is_deleted = is_deleted;
    }
    public boolean isIs_deleted_by_user() {
        return is_deleted_by_user;
    }
    public void setIs_deleted_by_user(boolean is_deleted_by_user) {
        this.is_deleted_by_user = is_deleted_by_user;
    }
    public boolean isIs_new_contact() {
        return is_new_contact;
    }
    public void setIs_new_contact(boolean is_new_contact) {
        this.is_new_contact = is_new_contact;
    }
    public double getOrder() {
        return order;
    }
    public void setOrder(double order) {
        this.order = order;
    }
    public int getUnread_count() {
        return unread_count;
    }
    public void setUnread_count(int unread_count) {
        this.unread_count = unread_count;
    }
    public boolean isMatches() {
        return matches;
    }
    public void setMatches(boolean matches) {
        this.matches = matches;
    }
	public boolean isIs_vip() {
		return is_vip;
	}
	public void setIs_vip(boolean is_vip) {
		this.is_vip = is_vip;
	}
	public String getMatch_time() {
		return match_time;
	}
	public void setMatch_time(String match_time) {
		this.match_time = match_time;
	}
	public String getTime_string() {
		return time_string;
	}
	public void setTime_string(String time_string) {
		this.time_string = time_string;
	}
	public String getLast_message_time() {
		return last_message_time;
	}
	public void setLast_message_time(String last_message_time) {
		this.last_message_time = last_message_time;
	}
}
