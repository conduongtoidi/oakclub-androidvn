package com.oakclub.android.util;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.oakclub.android.model.DataConfig;
import com.oakclub.android.model.SettingObject;

public class Constants {
	
	public static String TAG = "OakClub";
	
	public static String ACTION = "";
	public static String ACTION_LIKE = "1";
	public static String ACTION_NOPE = "0";
	public static final int DISTANCE_MIN_TO_INTO_PROFILE = 15;
	/*
	 * Query
	 */
	public static final String GET_SNAPSHOT = "getSnapshot";
	public static final String GET_HANGOUT_PROFILE = "getProfileInfo";
	public static final String SET_FAVORITE = "setLikedSnapshot";
	public static final String GET_SNAPSHOT_SETTINGS = "getAccountSetting";
	public static final String SET_VIEW_MUTUAL_MATCH = "setViewedMutualMatch";
	public static final String SET_READ_MESSAGES = "setReadMessages";
	public static final String VERIFY_USER = "verifyUser";
	public static final String SKIP_VERIFIED = "skipVerifyUser";
	public static final String UPDATE_LANGUAGE = "updateLanguage";
	public static final String GETCONFIG = "getConfig";
	public static final String VIP_REGISTER = "verifyGoogleReceipt";
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
	public static String HEADER_ACCESS_EXPIRES = "AccessExpires";
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
    public static int LENGTH_NAME = 10;
	
	public static int SPLASH_SCREEN_DELAYED_TIME = 2000;
	public static String BUNDLE_PROFILE_ID = "profile_id";
	public static String BUNDLE_NAME = "name";
	public static String BUNDLE_AVATAR = "avatar";
	public static String BUNDLE_BODY = "body";
	public static String BUNDLE_STATUS = "status";
	public static String BUNDLE_MATCH_TIME = "match_time";
	public static String BUNDLE_NOTI = "notification";
	public static String XMPP_SERVICE_NAME = "oakclub.com";
	public static int XMPP_SERVER_PORT = 5222;
	public static String CHAT_CLIENT_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static String CHAT_SERVER_FORMAT = "MM/dd/yyyy HH:mm:ss";
	
	public static int MIN_SEARCH_DISTANCE = 0;
	
	public static boolean isChangedSetting = false;
	public static SettingObject settingObject= null;
	public static DataConfig dataConfig;
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
    public static String IS_LOAD_CHAT_AGAIN = "isLoadChatAgain";
    public static String isLoadListMutualMatch = "isLoadListMutualMatch";

    public static final String PHOTO_EXTENSION = "jpg";
    public static final String VIDEO_EXTENSION = "flv"; 
    
    public static final int  TUTORIAL = 1111;
    public static final int VERIFIED = 1112;
    public static final int GETVIP = 1113;
    public static final String KEY_IS_SKIP = "is_skip";
    public static final int MEN = 1;
    public static final String VERIFIED_PERMISSIONS  = "publish_stream";
    public static final String START_LOGIN = "start_login";
   // public static final String FORCE_VERIFIED = "force_verified";
    public static final String COMEBACK_SNAPSHOT = "comeBackSnapshot";
    public static final String VERIFIED_SUCCESS = "verified_success";
    public static final String VERIFIED_FAILED = "verified_failed";
    public static final String[] FACEBOOK_PERMISSION = new String[] {
		"friends_photos", "friends_relationship_details",
		"friends_relationships", "friends_birthday",
		"friends_location", "friends_education_history",
		"friends_work_history", "friends_likes",
		"friends_religion_politics", "friends_status",
		"friends_about_me", "friends_interests",
		"user_relationship_details", "user_relationships",
		"user_birthday", "user_location", "user_education_history",
		"user_work_history", "user_photos", "user_likes",
		"user_religion_politics", "user_status", "user_about_me",
		"user_interests", "email", "user_hometown",
		"friends_hometown", "friends_events", "friends_checkins",
		"user_checkins", "user_events", "offline_access" };
    public static final String[] PUSHLISH_KEY = new String[]{
    	"MHKAMoGIJnazdfgH)f\"QUDSQYXOBCR<DKNAKIlGNO^UPj~`w&#V]CLkPRhNT9GNdfe]GTrv xvE\\~Sd5k;|*^u@",
		"!yeP4t`j;xP!RxmSBy`GhdBIblG07R1hRhdRUmy$$QJXC~b_zb=kc2Or+rHfU@vg/R$khz)D}^8NC8[H3n@T)J/",
		"iZtHOX_gR{`Y7oAhr;3:gX]]E}Z1hjxyhvX^oRF6h-ZKU=r Dwitg0Jfx<a;'v&ZQL4\\e3\\fuJ}OwQaLJ9L5zH`n'A",
		")~_Ny_>tp$#^v[^g`O~CCPCX[oG2pjC\\ozk,CUIqlrE2Ofqe|DK2dAUkZGIFz&}(AeOKKMYy&'mrKeR6R[PheEPG.tY",
		"gxZAV1AdaU @!!g{xs\\[j]yM#[I{CLGUCB"};
    public static final String[] PRODUCT_IDS =  new String[]{"com.oakclub.iap.01month","locpham.test.package2","com.oakclub.iap.6month","com.oakclub.iap.12month"};
    public static final String PURCHASE_PRICE = "PURCHASE_PRICE";
    public static final String TRANSACTION_ID= "TRANSACTION_ID";

	public static final String SENDER_ID = "105826272497";//"377367791749";//"857951122835";
	public static final int[] SnapShotCounter = {50, 250, 500, 1000};
	public static final String[] LANGUAGE_LOCALE = {"en", "vi", "de", "id", "th",
													   "ru", "es", "fr", "tr", "da",
													   "nb", "sv", "ro", "pl", "bg",
													   "hu", "hr", "el", "nl", "ar",
													   "ko", "sw", "ph", "hi"};

	public static final int STATUS_CHAT_FROM_SENT = 0;
	public static final int STATUS_CHAT_FROM_RECEIVED = 1;
    
}
