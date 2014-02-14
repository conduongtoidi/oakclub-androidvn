package com.oakclub.android.util;

import java.util.ArrayList;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.oakclub.android.model.SettingObject;

public class Constants {
	
	public static String TAG = "OakClub";
	
	public static String ACTION = "";
	public static String ACTION_LIKE = "1";
	public static String ACTION_NOPE = "0";
	/*
	 * Query
	 */
	public static final String GET_SNAPSHOT = "getSnapshot";
	public static final String GET_HANGOUT_PROFILE = "getProfileInfo";
	public static final String SET_FAVORITE = "setLikedSnapshot";
	public static final String GET_SNAPSHOT_SETTINGS = "getAccountSetting";
	public static final String SET_VIEW_MUTUAL_MATCH = "setViewedMutualMatch";
	public static final String SET_READ_MESSAGES = "setReadMessages";
	/*
	 * Key config request
	 */
	public static String PREFERENCE_NAME = "oakclub_pref";
	public static String PREFERENCE_FIRST_JOINED = "first_join";
    public static String PREFERENCE_SHOW_TUTORIAL = "show_tutorial";
    public static String PREFERENCE_SHOW_LIKE_DIALOG = "show_like_dialog";
    public static String PREFERENCE_SHOW_NOPE_DIALOG = "show_nope_dialog";
	public static String PREFERENCE_LOGGINED = "authen";
	public static String PREFERENCE_LANGUAGE = "language";
	public static String PREFERENCE_USER_ID = "facebook_user_id";
	public static String HEADER_USERNAME = "Username";
	public static String HEADER_ACCESS_TOKEN = "AccessToken";
	public static String HEADER_NONCE = "Nonce";
	public static String HEADER_CREATED = "Created";
	public static String HEADER_X_WSSE ="X-WSSE";
	public static String HEADER_ACCEPT = "Accept";
	public static String PROFILE_SETTING_FIELD_EMAIL = "email";
	public static String PROFILE_SETTING_FIELD_NAME = "name";
	public static String PROFILE_SETTING_FIELD_BIRTHDATE = "birthday";
	public static String PROFILE_SETTING_FIELD_GENDER = "gender";
	public static String PROFILE_SETTING_FIELD_INTERESTED = "interested";
	public static String PROFILE_SETTING_FIELD_RELATION_STATUS = "relationship_status";
	public static String PROFILE_SETTING_FIELD_HEIGHT = "height";
	public static String PROFILE_SETTING_FIELD_WEIGHT = "weight";
	public static String PROFILE_SETTING_FIELD_LOCATION = "location";
	public static String PROFILE_SETTING_FIELD_SCHOOL = "school";
	public static String PROFILE_SETTING_FIELD_ETHNICITY = "ethnicity";
	public static String PROFILE_SETTING_FIELD_WORK = "work";
	public static String PROFILE_SETTING_FIELD_LANGUAGE = "language";
	public static String PROFILE_SETTING_FIELD_ABOUT_ME = "about_me";
	public static int MAX_SEARCH_AGE = 80;
	public static int MIN_SEARCH_AGE = 17;
	
	public static int SPLASH_SCREEN_DELAYED_TIME = 2000;
	public static String BUNDLE_PROFILE_ID = "profile_id";
	public static String BUNDLE_NAME = "name";
	public static String BUNDLE_AVATAR = "avatar";
	public static String BUNDLE_BODY = "body";
	public static String XMPP_SERVICE_NAME = "oakclub.com";
	public static int XMPP_SERVER_PORT = 5222;
	public static String CHAT_TIME_FORMAT = "MM-dd-yyyy HH:mm:ss";
	
	public static int MIN_SEARCH_DISTANCE = 0;
	
	public static boolean isChangedSetting = false;
	public static SettingObject settingObject= null;
	public static String urlAvatar = "";
	public static String country="en";
    public static final int DEFAULT_HEIGHT_MAX = 300;
    public static final int DEFAULT_HEIGHT_MIN = 100;
    public static final int DEFAULT_HEIGHT = 150;
    public static final int DEFAULT_WEIGHT_MAX = 160;
    public static final int DEFAULT_WEIGHT_MIN = 40;
    public static final int DEFAULT_WEIGHT = 60;
    public static ImageLoader imageLoader = null;
    public static DisplayImageOptions options = null;
    public static int widthImage = 0;
    public static int heightImage = 0;
    public static String isLoadChat = "isLoadChat";
    
	public static final String SENDER_ID = "105826272497";//"377367791749";//"857951122835";
    
}
