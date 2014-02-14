package com.oakclub.android.model;

public class UploadPhotoReturnObject {
	boolean status;
	String msg;
	String error_code;
	UploadPhotoReturnDataObject data;
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
	public String getError_code() {
		return error_code;
	}
	public void setError_code(String error_code) {
		this.error_code = error_code;
	}
	public UploadPhotoReturnDataObject getData() {
		return data;
	}
	public void setData(UploadPhotoReturnDataObject data) {
		this.data = data;
	}
	
}
