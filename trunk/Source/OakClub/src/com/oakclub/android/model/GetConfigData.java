package com.oakclub.android.model;

import java.util.ArrayList;

public class GetConfigData {
	private boolean status;
	private String msg;
	private String error_code;
	private DataConfig data;
	
	public boolean getStatus() {
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
	public DataConfig getData() {
		return data;
	}
	public void setData(DataConfig data) {
		this.data = data;
	}
	

}
