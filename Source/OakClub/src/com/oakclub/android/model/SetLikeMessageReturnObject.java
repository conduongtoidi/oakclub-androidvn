package com.oakclub.android.model;

public class SetLikeMessageReturnObject {
    private boolean status;
    private String msg;
    private String type;
    private DataLikeSnapShot data;
    
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public DataLikeSnapShot getData() {
		return data;
	}
	public void setData(DataLikeSnapShot data) {
		this.data = data;
	}
}