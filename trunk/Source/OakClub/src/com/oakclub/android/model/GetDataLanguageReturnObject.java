package com.oakclub.android.model;

public class GetDataLanguageReturnObject {
	boolean status;
	String msg;
	int error_code;
	GetDataLanguageReturnDataObject data;
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
	public int getError_code() {
		return error_code;
	}
	public void setError_code(int error_code) {
		this.error_code = error_code;
	}
	public GetDataLanguageReturnDataObject getData() {
		return data;
	}
	public void setData(GetDataLanguageReturnDataObject data) {
		this.data = data;
	}
	
}
