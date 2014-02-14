package com.oakclub.android.model;

import java.util.ArrayList;

public class ChatHistoryReturnObject {
    private boolean status;
    String username;
    String avatar;
    private ArrayList<ChatHistoryData> data;

    public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public ArrayList<ChatHistoryData> getData() {
        return data;
    }
    public void setData(ArrayList<ChatHistoryData> data) {
        this.data = data;
    }
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
    
}
