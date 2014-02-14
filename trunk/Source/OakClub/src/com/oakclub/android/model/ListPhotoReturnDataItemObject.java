package com.oakclub.android.model;

import java.io.Serializable;

public class ListPhotoReturnDataItemObject implements Serializable {

	String id;
	String tweet_image_link;
	boolean is_profile_picture;
	
	public String getTweet_image_link() {
		return tweet_image_link;
	}
	public void setTweet_image_link(String tweet_image_link) {
		this.tweet_image_link = tweet_image_link;
	}
	public boolean isIs_profile_picture() {
		return is_profile_picture;
	}
	public void setIs_profile_picture(boolean is_profile_picture) {
		this.is_profile_picture = is_profile_picture;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
