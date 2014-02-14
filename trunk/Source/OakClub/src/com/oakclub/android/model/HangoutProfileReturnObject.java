package com.oakclub.android.model;

public class HangoutProfileReturnObject {
	boolean status;
	HangoutProfileItemObject data;
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public HangoutProfileItemObject getData() {
		return data;
	}
	public void setData(HangoutProfileItemObject data) {
		this.data = data;
	}
}
