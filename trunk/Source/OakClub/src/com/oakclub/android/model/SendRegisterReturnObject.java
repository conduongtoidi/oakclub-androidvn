package com.oakclub.android.model;



public class SendRegisterReturnObject {
	private boolean status;
	private String msg;
	private String error_status;
	private ProfileInfoData data;
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getError_status() {
		return error_status;
	}
	public void setError_status(String error_status) {
		this.error_status = error_status;
	}
	public ProfileInfoData getData() {
		return data;
	}
	public void setData(ProfileInfoData data) {
		this.data = data;
	}
	
}
