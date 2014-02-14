package com.oakclub.android.model;

public class HangoutProfileOtherReturnObject {
    boolean status;
    HangoutProfileOtherData data;
    private String msg;
    private int error_code;
    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
    public HangoutProfileOtherData getData() {
        return data;
    }
    public void setData(HangoutProfileOtherData data) {
        this.data = data;
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
}
