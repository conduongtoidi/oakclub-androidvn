package com.oakclub.android.model;

public class ListPhotoReturnObject {
	boolean status;
	ListPhotoReturnDataObject data;
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public ListPhotoReturnDataObject getData() {
		return data;
	}
	public void setData(ListPhotoReturnDataObject data) {
		this.data = data;
	}
}
