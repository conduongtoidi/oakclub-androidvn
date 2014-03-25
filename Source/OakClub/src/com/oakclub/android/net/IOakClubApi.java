package com.oakclub.android.net;

import java.io.File;
import java.util.HashMap;

import android.content.Context;

import com.oakclub.android.model.ChatHistoryReturnObject;
import com.oakclub.android.model.GetAccountSettingsReturnObject;
import com.oakclub.android.model.GetConfigData;
import com.oakclub.android.model.GetDataLanguageReturnObject;
import com.oakclub.android.model.GetSnapShot;
import com.oakclub.android.model.GetSnapshotSettingsReturnObject;
import com.oakclub.android.model.HangoutProfileOtherReturnObject;
import com.oakclub.android.model.HangoutProfileReturnObject;
import com.oakclub.android.model.ListChatReturnObject;
import com.oakclub.android.model.ListPhotoReturnObject;
import com.oakclub.android.model.PostMethodReturnObject;
import com.oakclub.android.model.ProfileUpdateFirstTimeObject;
import com.oakclub.android.model.SenVIPRegisterReturnObject;
import com.oakclub.android.model.SendChatReturnObject;
import com.oakclub.android.model.SendMessageReturnObject;
import com.oakclub.android.model.SendRegisterReturnObject;
import com.oakclub.android.model.SetLikeMessageReturnObject;
import com.oakclub.android.model.SetLocationReturnObject;
import com.oakclub.android.model.SetViewMutualReturnObject;
import com.oakclub.android.model.SettingReturnObject;
import com.oakclub.android.model.UploadPhotoReturnObject;
import com.oakclub.android.model.UploadVideoObject;
import com.oakclub.android.model.VerifiedReturnObject;

public interface IOakClubApi {
	/**
	 * Send user register infomation
	 * 
	 * @param user_id
	 *            user facebook id
	 * @param access_token
	 *            user access token
	 * @param platform
	 * 			  user platform
	 * @return
	 */
	public SendRegisterReturnObject sendRegister(String user_id,
			String access_token, String platform, String os_version, String device_name, String androidToken);

	/**
	 * Get list of person every day
	 * 
	 * @param userId
	 *            : facebookId
	 * @return GetSnapShot
	 */
	public GetSnapShot getSnapShot(String userId, int startIndex, int limitIndex);

	/**
	 * Get info person
	 * 
	 * @param profileId
	 *            :profile_id
	 * @return HangoutProfile
	 */
	// public ListChatReturnObject getListChat();

	/**
	 * Get Photos list of user
	 * 
	 * @param profile_id
	 *            profile id, if get my profile, it should be empty
	 * @return
	 */
	public ListPhotoReturnObject getListPhotos(String profile_id);

	/**
	 * Get hangout profile of user
	 * 
	 * @param profile_id
	 *            profile id, if get my profile, it should be empty
	 * @return
	 */
	public HangoutProfileReturnObject getHangoutProfile(String profile_id);

	
	/**
	 * Get hangout profile of user
	 * 
	 * @param profile_id
	 *            profile id, if get my profile, it should be empty
	 * @return
	 */
	public HangoutProfileOtherReturnObject getHangoutProfileOther(String profile_id);
	
	/**
	 * get account settings
	 * 
	 * @return
	 */
	public GetAccountSettingsReturnObject getAccountSetting();

	public GetSnapshotSettingsReturnObject getSnapshotSetting();

	public SettingReturnObject setSnapshotSetting(String age_from,
			String age_to, String filter_female, String filter_male,
			String fof, String friends, String new_people,
			String purpose_of_search, String range);

	/**
	 * Set Favorite
	 * 
	 * @param profile_id
	 * @param numberSet
	 * @note: YES :1 NO:2
	 */
	public SetLikeMessageReturnObject SetFavoriteInSnapshot(String snapshot_id, String numberSet);

	public ListChatReturnObject getListChat();

	public ChatHistoryReturnObject getHistoryMessages(String profile_id);

	public SendMessageReturnObject sendMessage(String to, String msg);

	public SettingReturnObject setProfileByField(String field, String value);
	
	public UploadPhotoReturnObject UploadProfilePhoto(boolean is_Avatar, File file);

    public UploadVideoObject UploadProfileVideo(File file);
	
	public SetLocationReturnObject SetUserLocation(String latitude, String longitude);
	
	public SendChatReturnObject SendChatMessage(String toUser, String content);

    public PostMethodReturnObject DeleteUserPhoto(String photo_id);
    
    public PostMethodReturnObject DeleteUserVideo();
	
	public GetDataLanguageReturnObject GetDataLanguage();
	
	public SetViewMutualReturnObject SetViewedMutualMatch(String profileId);
	
	public ProfileUpdateFirstTimeObject updateProfileFirstTime(String name, 
	        int gender, String birthday, String interested, String email, double longitude, double latitude);
	
	public void BlockUserById(String profile_id);
	
    public void ReportUserById(String profile_id, String content);
    
	public void SetReadMessages(String profile_id);
	
	public VerifiedReturnObject VerifiedUser();
	
	public VerifiedReturnObject SkipVerified();

	public void UpdateLanguage(String key_language);
	
	public GetConfigData GetConfig();
	
	public SenVIPRegisterReturnObject SendVIPRegister(String productID, String purchaseToken);

	public HashMap<String, Object> getChatList();

	public HashMap<String, Object> getChatHistory(String profileId);
	
	public HashMap<String, Object> sendChatMessage(String profileId, String message);
	
	public HashMap<String, Object> readChatMessage(String profileId);

	public HashMap<String, Object> addRoster(String profileId);
}
