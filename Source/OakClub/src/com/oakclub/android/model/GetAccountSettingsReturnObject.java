package com.oakclub.android.model;

public class GetAccountSettingsReturnObject {
	boolean status;
	GetAccountSettingsReturnDataObject data;
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public GetAccountSettingsReturnDataObject getData() {
		return data;
	}
	public void setData(GetAccountSettingsReturnDataObject data) {
		this.data = data;
	}
}
