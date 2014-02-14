package com.oakclub.android.model;

import java.io.Serializable;

public class BirthdayObject implements Serializable {
	public long sec;
	public long usec;
	private String date;
	private String timezone_type;
	private String timezone;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTimezone_type() {
		return timezone_type;
	}
	public void setTimezone_type(String timezone_type) {
		this.timezone_type = timezone_type;
	}
	public String getTimezone() {
		return timezone;
	}
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
}
