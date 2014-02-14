package com.oakclub.android.model;

public class GetSnapshotSettingsReturnObject {
	boolean status;
	GetSnapshotSettingsReturnDataObject data;
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public GetSnapshotSettingsReturnDataObject getData() {
		return data;
	}
	public void setData(GetSnapshotSettingsReturnDataObject data) {
		this.data = data;
	}
}
