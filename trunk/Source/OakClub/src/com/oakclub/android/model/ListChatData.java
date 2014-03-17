package com.oakclub.android.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.oakclub.android.util.Constants;

public class ListChatData {
    private String name="";
    private String avatar="";
    private String profile_id="";
    private int status = 0;
    private boolean is_vip = false;
    private String last_message="";
    private String last_message_time="";
    private boolean matches = false;
    private String match_time  ="";
    private String fid="";
    private int  unread_count = 0;
    private String last_active_time="";
    
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
		this.match_time = convertStringToDate(match_time);
	}
	public String getLast_message_time() {
		return last_message_time;
	}
	public void setLast_message_time(String last_message_time) {
		this.last_message_time = convertStringToDate(last_message_time);
	}
	public String getLast_active_time() {
		return last_active_time;
	}
	public void setLast_active_time(String last_active_time) {
		this.last_active_time = convertStringToDate(last_active_time);
	}
	

    private String convertStringToDate(String str){
    	if(str.equals(""))
    		return str;
		SimpleDateFormat dateFormatServer = new SimpleDateFormat(Constants.CHAT_SERVER_FORMAT);
		SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.CHAT_CLIENT_FORMAT);
		Date date= new Date();
		try {
			date = dateFormatServer.parse(str);
		} catch (ParseException e) {
			//e.printStackTrace();
			return str; 
		}
		str = dateFormat.format(date);
		return str; 
    }
}
