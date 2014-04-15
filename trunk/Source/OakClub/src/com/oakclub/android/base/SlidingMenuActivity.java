package com.oakclub.android.base;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jivesoftware.smack.XMPPConnection;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.oakclub.android.ChatActivity;
import com.oakclub.android.R;
import com.oakclub.android.base.OakClubBaseActivity.PingActivitiesLoader;
import com.oakclub.android.core.IRequestQueue;
import com.oakclub.android.core.RequestQueue;
import com.oakclub.android.core.RequestUI;
import com.oakclub.android.fragment.LeftMenuListFragment;
import com.oakclub.android.fragment.ListChatFragment;
import com.oakclub.android.fragment.ProfileSettingFragment;
import com.oakclub.android.helper.operations.ListChatOperation;
import com.oakclub.android.model.GetDataLanguageReturnDataObject;
import com.oakclub.android.net.IOakClubApi;
import com.oakclub.android.net.OakClubApi;
import com.oakclub.android.util.BitmapScaler;
import com.oakclub.android.util.Constants;
import com.oakclub.android.util.OakClubUtil;

public class SlidingMenuActivity extends SlidingFragmentActivity {
	public static XMPPConnection xmpp;

	public static String user_id;
	public IOakClubApi oakClubApi;
	public IOakClubApi oakClubApiTemp;
	public static String facebook_user_id;
	public static String access_token;
	
	LinearLayout linearLayout;
	protected RelativeLayout titleBar;
	
	ActionBarDrawerToggle mDrawerToggle;
	ImageView mMenuImv;
	ImageView mChatImv;
	ImageView mEditImv;
	ImageView smallLogo;
	protected LeftMenuListFragment mFrag;
	TextView titleTv;
	public static TextView mNotificationTv;
	public static int totalUnreadMessage = 0;
//	public static List<String> listProfileSendMessage = new ArrayList<String>();   
	public static GetDataLanguageReturnDataObject mDataLanguageObj;

    private LayoutInflater inflater;
    private View v;
    protected Intent intent;
    public boolean isChangedAvatar = false;
    public boolean isLoadListMutualMatch = false;
    public String profileIdMultualMatch = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(android.R.style.Theme_Holo_NoActionBar);
		super.onCreate(savedInstanceState);

		
		if(Constants.imageLoader==null){
		    Constants.imageLoader = ImageLoader.getInstance();
		    Constants.imageLoader.init(ImageLoaderConfiguration.createDefault(this
    				.getApplicationContext()));
    		setConfigImage();
		}
		if (Constants.options == null) {
		    Constants.options = new DisplayImageOptions.Builder()
					.showStubImage(R.drawable.logo_splashscreen)
					.showImageForEmptyUri(R.drawable.logo_splashscreen)
					//.cacheInMemory().cacheOnDisc()
					.build();
		}
		oakClubApi = OakClubApi.createInstance(this.getApplicationContext(),
				getString(R.string.default_server_address));
		
		oakClubApiTemp = OakClubApi.createInstance(this.getApplicationContext(),
				getString(R.string.default_server_address_temp));
		
		setContentView(R.layout.activity_oakclub_base);
		
		titleTv = (TextView) findViewById(R.id.app_title);
		linearLayout = (LinearLayout) findViewById(R.id.main_linear_layout);
		titleBar = (RelativeLayout) findViewById(R.id.top_title_bar);
		mMenuImv = (ImageView) findViewById(R.id.main_icon_menu);
		mChatImv = (ImageView) findViewById(R.id.main_icon_chat);
		mEditImv = (ImageView) findViewById(R.id.main_icon_edit);
		smallLogo = (ImageView) findViewById(R.id.small_logo);
		mNotificationTv = (TextView) findViewById(R.id.main_icon_noti);
		mNotificationTv.setGravity(Gravity.CENTER);
		mMenuImv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getSlidingMenu().toggle();
				if(isChangedAvatar){
				    mFrag.reloadAvatar();
				    isChangedAvatar = false;
				}
			}
		});
		mChatImv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getSlidingMenu().showSecondaryMenu();
				SharedPreferences pref = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE);
				boolean isNewMessage = pref.getBoolean(Constants.isLoadChat, false);
				if (isNewMessage) {
					getSupportFragmentManager().beginTransaction()
					.replace(R.id.menu_frame_right, new ListChatFragment())
					.commit();
					
					SharedPreferences.Editor editor = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit();
					editor.putBoolean(Constants.isLoadChat, false);
					editor.commit();
				}
			}
		});
		
		getSlidingMenu().setMode(SlidingMenu.LEFT_RIGHT);
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		setBehindContentView(R.layout.menu_frame);

		if (savedInstanceState == null) {
			FragmentTransaction t = this.getSupportFragmentManager()
					.beginTransaction();
			mFrag = new LeftMenuListFragment();
			t.replace(R.id.menu_frame_left, mFrag);
			t.commit();
		} else {
			mFrag = (LeftMenuListFragment) this.getSupportFragmentManager()
					.findFragmentById(R.id.menu_frame_left);
		}

		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.drawer_shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		
		getSlidingMenu().setSecondaryMenu(R.layout.menu_frame_two);
		getSlidingMenu().setSecondaryShadowDrawable(R.drawable.drawer_shadow);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame_right, new ListChatFragment())
				.commit();

		inflater = LayoutInflater.from(getApplicationContext());

//		ListChatOperation listChatDb = new ListChatOperation(this);
//		totalUnreadMessage = listChatDb.getTotalNotification();
//		getTotalNotification(totalUnreadMessage);

		
	}
	

	class PingActivitiesLoader extends RequestUI {
		public PingActivitiesLoader(Object key, Activity activity) {
			super(key, activity);
		}

		@Override
		public void execute() throws Exception {
			oakClubApi.pingActivities();
		}

		@Override
		public void executeUI(Exception ex) {
			
		}
	}

	
	public static void getTotalNotification(int totalUnreadMessage){
		if (totalUnreadMessage > 0) {
			mNotificationTv.setText("" + totalUnreadMessage);
			mNotificationTv.setVisibility(View.VISIBLE);
		} else {
			mNotificationTv.setVisibility(View.GONE);
		}
	}

    private void setConfigImage() {
        if(Constants.widthImage ==0 && Constants.heightImage ==0){
    		int t = (int) OakClubUtil.getWidthScreen(this);
    		if (t < 320) {
    		    Constants.heightImage = 100;
    			Constants.widthImage = 100;
    		} else if (t >= 320 && t < 1024) {
    		    Constants.heightImage = 250;
    			Constants.widthImage = 250;
//    		} else if (t >= 480 && t < 720) {
//    		    Constants.heightImage = 250;
//    			Constants.widthImage = 250;
//    		}
//    		if (t >= 720 && t < 1024) {
//    		    Constants.heightImage = 250;
//    			Constants.widthImage = 250;
    		} else {
    		    Constants.heightImage = 320;
    			Constants.widthImage = 320;
    		}
        }
	}

	public RequestQueue getRequestQueue() {
		return ((IRequestQueue) getApplication()).getRequestQueue();
	}

    protected void setContentViewX(int id) {
		linearLayout.removeAllViews();
        v =  inflater.inflate(id, null);
		linearLayout.addView(v, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
	}
    
    public void setContentViewX(View view) {
        linearLayout.removeAllViews();
        linearLayout.addView(view, LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
    }

	public TextView getAppTitle() {
		return titleTv;
	}

	@Override
	public void onBackPressed() {
		SlidingMenu sm = getSlidingMenu();
		if (sm.isMenuShowing()) {
			getSlidingMenu().toggle();
		} else {
			super.onBackPressed();
		}

	}

	
	@Override
    protected void onPause() {
	    System.gc();
        super.onPause();
    }

	@Override
	protected void onStop() {
		super.onStop();
	}
	
    @Override
    protected void onDestroy() {
        Runtime.getRuntime().gc();
        linearLayout.removeAllViews();   
        try {
            OakClubUtil.trimCache(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }    
    
    @Override
    protected void onResume() {
    	if(!OakClubUtil.isInternetAccess(SlidingMenuActivity.this)){
            OakClubUtil.enableDialogWarning(this, 
                    this.getString(R.string.txt_warning), 
                    this.getString(R.string.txt_internet_message));
            //return;
        } else {
	        com.facebook.AppEventsLogger.activateApp(this, this.getString(R.string.app_id));
	        SharedPreferences pref = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE);
	        boolean isBlockUser = pref.getBoolean(Constants.IS_LOAD_CHAT_AGAIN, false);
	        if (isBlockUser) {
				ChatBaseActivity.updateListChat(SlidingMenuActivity.this);
	            
	            SharedPreferences.Editor editor = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit();
	            editor.putBoolean(Constants.IS_LOAD_CHAT_AGAIN, false);
	            editor.commit();
	        }
        }
        super.onResume();
    }    
}
