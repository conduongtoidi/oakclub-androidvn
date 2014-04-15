package com.oakclub.android.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gcm.GCMRegistrar;
import com.oakclub.android.base.SlidingMenuActivity;
import com.oakclub.android.image.SmartImageView;
import com.oakclub.android.image.WebImage;
import com.oakclub.android.util.Constants;
import com.oakclub.android.util.OakClubUtil;
import com.oakclub.android.view.CircleImageView;
import com.oakclub.android.view.ImageLoader;

import com.oakclub.android.ChooseLanguageActivity;
import com.oakclub.android.InfoProfileOtherActivity;
import com.oakclub.android.MainActivity;
import com.oakclub.android.VerifiedActivity;
import com.oakclub.android.R;
import com.oakclub.android.SlidingActivity;
import com.oakclub.android.SlidingActivity.MenuOakclub;
import com.oakclub.android.ForceVerifiedActivity;

public class LeftMenuListFragment extends Fragment {
	LinearLayout menuProfile;
	LinearLayout menuSnapshot;
	LinearLayout menuSetting;
	LinearLayout menuIncludeFriends;
	LinearLayout logoutBtn;
	LinearLayout menuVerified;
	LinearLayout menuVIPRoom;

	CircleImageView avatarProfile;
	TextView tvProfile;
	TextView tvSnapshot;
	TextView tvVipRoom;
	TextView tvSettings;
	TextView tvVerified;
	TextView tvIncludeFriends;
	String[] menuTextList;
	ImageView ivVerified;
	FrameLayout iconVIPRoom;
	private FrameLayout fltAvaProfile;
	private LinearLayout lltMenu;
	boolean isVerified = true;
	private SlidingActivity activity = (SlidingActivity) getActivity();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.layout_left_menu, container,
				false);
		menuProfile = (LinearLayout) rootView
				.findViewById(R.id.menu_item_llt_profile);
		menuSnapshot = (LinearLayout) rootView
				.findViewById(R.id.menu_item_llt_snapshot);
		menuSetting = (LinearLayout) rootView
				.findViewById(R.id.menu_item_llt_setting);
		menuIncludeFriends = (LinearLayout) rootView
				.findViewById(R.id.menu_item_llt_include_friends);
		menuVerified = (LinearLayout) rootView
				.findViewById(R.id.menu_item_llt_get_verified);
		menuVIPRoom = (LinearLayout)  rootView
				.findViewById(R.id.menu_item_llt_vip_room);
		iconVIPRoom = (FrameLayout) rootView.findViewById(R.id.menu_item_icon_vip_room);
		logoutBtn = (LinearLayout) rootView.findViewById(R.id.linear_logout);

		ivVerified = (ImageView) rootView
				.findViewById(R.id.verified_profile_icon);

		tvProfile = (TextView) rootView
				.findViewById(R.id.menu_item_text_profile);
		tvSnapshot = (TextView) rootView
				.findViewById(R.id.menu_item_text_snapshot);
		tvVipRoom = (TextView) rootView
				.findViewById(R.id.menu_item_text_vip_room);
		tvSettings = (TextView) rootView
				.findViewById(R.id.menu_item_text_setting);
		tvVerified = (TextView) rootView
				.findViewById(R.id.menu_item_text_getverified);
		tvIncludeFriends = (TextView) rootView
				.findViewById(R.id.menu_item_text_include_friends);
		
		fltAvaProfile = (FrameLayout) rootView.findViewById(R.id.menu_item_icon_flt_avaProfile);
		int widthPhotoItem = (int) OakClubUtil.getWidthScreen(this.getActivity()) / 8;
		LinearLayout.LayoutParams layoutView = new LinearLayout.LayoutParams(
				widthPhotoItem, widthPhotoItem);
		layoutView.gravity = Gravity.CENTER;
		fltAvaProfile.setLayoutParams(layoutView);
		avatarProfile = (CircleImageView) fltAvaProfile
				.findViewById(R.id.menu_item_icon_avaProfile);
		
		menuTextList = getResources().getStringArray(R.array.menu_text_list);
		lltMenu = (LinearLayout) rootView
				.findViewById(R.id.layout_left_menu_lltlistmenu);
		
		if (OakClubUtil.isInternetAccess(getActivity())) {
			init();
		}
		return rootView;
	}

	public void reloadAvatar() {
		if (ProfileSettingFragment.profileInfoObj != null) {
			String imageUrl = OakClubUtil.getFullLink(this.getActivity(),
					ProfileSettingFragment.profileInfoObj.getAvatar());
			OakClubUtil.loadImageFromUrl(this.getActivity(), imageUrl,
					avatarProfile, "");
		} else {
			Intent intent = new Intent(this.getActivity(), MainActivity.class);
			startActivity(intent);
			//finish();
		}
	}

	private OnClickListener layoutClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			activity = (SlidingActivity) getActivity();
			Intent intent;
			if (OakClubUtil.isInternetAccess(activity)) {
				switch (v.getId()) {
				case R.id.menu_item_llt_profile:
					if (activity.getMenu() != MenuOakclub.PROFILE) {
						activity.setMenu(MenuOakclub.PROFILE);
						ProfileSettingFragment profileSetting = new ProfileSettingFragment(
								activity);
						profileSetting.initProfile();
					}
					
					break;
				case R.id.menu_item_llt_snapshot:
					if (activity.getMenu() != MenuOakclub.SNAPSHOT) {
						activity.setMenu(MenuOakclub.SNAPSHOT);
						ImageLoader imgLoader = new ImageLoader(activity, "");
						imgLoader = new ImageLoader(activity, Constants.SNAPSHOT_FOLDER);
				        imgLoader.clearCache();
				        imgLoader = new ImageLoader(activity, Constants.OTHER_PROFILE_FOLDER);
				        imgLoader.clearCache();
						SnapshotFragment snapshot = new SnapshotFragment(
								activity);
						snapshot.initSnapshot();
						activity.snapshot = snapshot;
					}
					break;
				case R.id.menu_item_llt_setting:
					if (activity.getMenu() != MenuOakclub.SETTING) {
						activity.setMenu(MenuOakclub.SETTING);
						SettingFragment setting = new SettingFragment(activity);
						setting.initSetting();
					}
					break;
				case R.id.menu_item_llt_include_friends:
					activity.setMenu(MenuOakclub.INVITE_FRIEND);
					intent = new Intent();
					intent.setAction(Intent.ACTION_SEND);
					intent.putExtra(Intent.EXTRA_TEXT,
							getString(R.string.txt_share_title) + "\n"
									+ getString(R.string.txt_share_url));
					intent.setType("text/plain");
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					break;

				case R.id.menu_item_llt_get_verified:
					if (activity.getMenu() != MenuOakclub.VERIFIED) {
						activity.setMenu(MenuOakclub.VERIFIED);
						VerifiedActivity verifiedActivity = new VerifiedActivity(activity,false);
						verifiedActivity.initVerified();
					}
					break;

				case R.id.linear_logout:
					
					ImageLoader imgLoader = new ImageLoader(activity, "");
			        imgLoader.clearCache();
			        imgLoader = new ImageLoader(activity, Constants.SNAPSHOT_FOLDER);
			        imgLoader.clearCache();
			        imgLoader = new ImageLoader(activity, Constants.OTHER_PROFILE_FOLDER);
			        imgLoader.clearCache();
			        imgLoader = new ImageLoader(activity, "Chat");
			        imgLoader.clearCache();
			        
					SmartImageView.cancelAllTasks();
					WebImage.clear();
					
					Editor editor = activity.getSharedPreferences(
							Constants.PREFERENCE_NAME, 0).edit();
					editor.putBoolean(Constants.PREFERENCE_LOGGINED, false);
					editor.putString(Constants.PREFERENCE_USER_ID, null);
					editor.putString(Constants.HEADER_ACCESS_TOKEN, null);
					editor.putString("username", null);
					editor.putString("password", null);
					GCMRegistrar.setRegisteredOnServer(activity, false);
					editor.commit();
					intent = new Intent(activity, MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_CLEAR_TOP
							| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
					startActivity(intent);

					NotificationManager nMgr = (NotificationManager) activity
							.getSystemService(activity.NOTIFICATION_SERVICE);
					nMgr.cancelAll();
					activity.finish();
					break;
					case R.id.menu_item_llt_vip_room:
						if (activity.getMenu() != MenuOakclub.VIPROOM) {
							activity.setMenu(MenuOakclub.VIPROOM);
							if(ProfileSettingFragment.profileInfoObj != null && ProfileSettingFragment.profileInfoObj.isIs_vip()){
								VIPRoomFragment vipRoom = new VIPRoomFragment(activity);
								vipRoom.initVIPRoom();	
							}else{
								GetVIPFragment getVIP = new GetVIPFragment(
										activity);
								getVIP.initGetVIP();
							}							
						}
						break;
				}
				activity.getSlidingMenu().toggle();
			} else {
				AlertDialog.Builder builder;
				builder = new AlertDialog.Builder(activity);
				final AlertDialog dialog = builder.create();
				LayoutInflater inflater = LayoutInflater.from(activity);
				View layout = inflater
						.inflate(R.layout.dialog_warning_ok, null);
				dialog.setView(layout, 0, 0, 0, 0);
				Button btnOK = (Button) layout
						.findViewById(R.id.dialog_internet_access_lltfooter_btOK);
				btnOK.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						activity.finish();
						System.exit(0);
					}
				});
				dialog.setCancelable(false);
				dialog.show();
			}
		}
	};

	@SuppressWarnings("deprecation")
	private void init() {
		if (ProfileSettingFragment.profileInfoObj != null) {
			int heightScreen = (int) OakClubUtil.getHeightScreen(getActivity());
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
					FrameLayout.LayoutParams.FILL_PARENT,
					FrameLayout.LayoutParams.WRAP_CONTENT);
			params.leftMargin = (int) OakClubUtil.convertDpToPixel(30,
					getActivity().getBaseContext());
			params.topMargin = heightScreen / 5;
			if (ProfileSettingFragment.profileInfoObj.getIs_verify()) {
				menuVerified.setVisibility(View.GONE);
				ivVerified.setVisibility(View.VISIBLE);
			}
			if(ProfileSettingFragment.profileInfoObj.isIs_vip()){
				tvVipRoom.setTextColor(getResources().getColor(R.color.Gold));
				iconVIPRoom.setBackgroundResource(R.drawable.viproom_icon_complete);
			}
			lltMenu.setLayoutParams(params);
			menuProfile.setOnClickListener(layoutClick);
			menuSnapshot.setOnClickListener(layoutClick);
			menuSetting.setOnClickListener(layoutClick);
			menuIncludeFriends.setOnClickListener(layoutClick);
			menuVerified.setOnClickListener(layoutClick);
			menuVIPRoom.setOnClickListener(layoutClick);
			logoutBtn.setOnClickListener(layoutClick);

			tvProfile.setText(menuTextList[0]);
			tvSnapshot.setText(menuTextList[1]);
			tvSettings.setText(menuTextList[2]);
			tvIncludeFriends.setText(menuTextList[3]);
			tvVerified.setText(menuTextList[4]);
			tvVipRoom.setText(menuTextList[5]);
			
			String imageUrl = OakClubUtil.getFullLink(this.getActivity(), ProfileSettingFragment.profileInfoObj.getAvatar());
			OakClubUtil.loadImageFromUrl(this.getActivity(), imageUrl, avatarProfile, "");
            
		} else {
			Intent intent = new Intent(this.getActivity(), MainActivity.class);
			startActivity(intent);
			//finish();
		}
	}
	@Override
	public void onResume() {
		super.onResume();
		if(ProfileSettingFragment.profileInfoObj != null && ProfileSettingFragment.profileInfoObj.getIs_verify()){
			menuVerified.setVisibility(View.GONE);
			ivVerified.setVisibility(View.VISIBLE);
		}	
		if(ProfileSettingFragment.profileInfoObj != null && ProfileSettingFragment.profileInfoObj.isIs_vip()){
			tvVipRoom.setTextColor(getResources().getColor(R.color.Gold));
			iconVIPRoom.setBackgroundResource(R.drawable.viproom_icon_complete);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

}
