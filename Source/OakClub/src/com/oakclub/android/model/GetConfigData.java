package com.oakclub.android.model;

public class GetConfigData {
	private boolean status;
	private String msg;
	private String error_code;
	private DataConfig data2;
	
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
	public DataConfig getData2() {
		return data2;
	}
	public void setData2(DataConfig data2) {
		this.data2 = data2;
	}
	

}
