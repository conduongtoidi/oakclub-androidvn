package com.oakclub.android.model;

public class ProfileInfoObject {
	boolean status;
	ProfileInfoData data;
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public ProfileInfoData getData() {
		return data;
	}
	public void setData(ProfileInfoData data) {
		this.data = data;
	}
}
