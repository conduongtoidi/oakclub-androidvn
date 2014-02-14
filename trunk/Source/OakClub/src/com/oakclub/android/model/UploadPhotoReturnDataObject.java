package com.oakclub.android.model;

public class UploadPhotoReturnDataObject {
	String id;
	String tweet_image_link;
	boolean is_avatar;
	public String getTweet_image_link() {
		return tweet_image_link;
	}
	public void setTweet_image_link(String tweet_image_link) {
		this.tweet_image_link = tweet_image_link;
	}
	public boolean isIs_avatar() {
		return is_avatar;
	}
	public void setIs_avatar(boolean is_avatar) {
		this.is_avatar = is_avatar;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
