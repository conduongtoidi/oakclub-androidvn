
package com.oakclub.android.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.oakclub.android.util.Constants;

public class ChatHistoryData {
    private String body = "" ;
    private String time_string = "";
    private String from = "";
    private String to = "";
    
    private String profile_id = "";
    private int status = 0;
    
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public String getTime_string() {
        return time_string;
    }
    public void setTime_string(String time_string) {
        this.time_string = convertStringToDate(time_string);
    }
    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public String getTo() {
        return to;
    }
    public void setTo(String to) {
        this.to = to;
    }

    private String convertStringToDate(String str){
    	if(str.equals(""))
    		return str;
		SimpleDateFormat dateFormatServer = new SimpleDateFormat(Constants.CHAT_SERVER_FORMAT);
		SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.CHAT_CLIENT_FORMAT);
		Date date= new Date();
		try {
			date = dateFormat.parse(str);
		} catch (ParseException e) {
			return str; 
		}
		str = dateFormatServer.format(date);
		return str; 
    }
    
	public String getProfile_id() {
		return profile_id;
	}
	public void setProfile_id(String profile_id) {
		this.profile_id = profile_id;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		if(status!=-1){
			this.status = status;
		}
		else{
			if(getProfile_id().equals(getFrom()))
				setStatus(Constants.STATUS_CHAT_FROM_SENT);
			else setStatus(Constants.STATUS_CHAT_FROM_RECEIVED);
		}
	}
}
