package com.oakclub.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.facebook.Session;
import com.oakclub.android.base.SlidingMenuActivity;
import com.oakclub.android.fragment.GetVIPFragment;
import com.oakclub.android.fragment.ProfileSettingFragment;
import com.oakclub.android.fragment.SnapshotFragment;
import com.oakclub.android.fragment.VIPRoomFragment;
import com.oakclub.android.util.Constants;
import com.oakclub.android.util.OakClubUtil;

public class SlidingActivity extends SlidingMenuActivity {

    private MenuOakclub menu;
    public SnapshotFragment snapshot;

    private LayoutInflater inflater;
    public View view;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(android.R.style.Theme_Holo_NoActionBar);
        super.onCreate(savedInstanceState);
        
        if (OakClubUtil.isInternetAccess(SlidingActivity.this)) {
        	Bundle bundleListChatData = getIntent().getExtras();
			if (bundleListChatData != null) {
				isLoadListMutualMatch = bundleListChatData.getBoolean(Constants.isLoadListMutualMatch);
				profileIdMultualMatch = bundleListChatData.getString(Constants.BUNDLE_PROFILE_ID);
			}
	        snapshot = new SnapshotFragment(this);
	        snapshot.initSnapshot();
	        this.setMenu(MenuOakclub.SNAPSHOT);
	        
        }
    }
    
    public void init(int id){
        inflater = LayoutInflater.from(this);
        view =  inflater.inflate(id, null);
        this.setContentViewX(view);
    }
    
    public void releaseView(){
        switch(getMenu()){
            case PROFILE :
                break;
            case SNAPSHOT:
                break;
            case SETTING:
                break;
            case INVITE_FRIEND:
                break;
            case VIPROOM:
                break;
            case VERIFIED:
                break;
            default: 
                break;
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (getMenu()) {
            case PROFILE :
                if (resultCode == RESULT_OK) {
                    ProfileSettingFragment profileSetting = new ProfileSettingFragment(this);
                    profileSetting.initProfile();
                    profileSetting.mImageCaptureUri = data.getData();
                    switch (requestCode) {
                    case ProfileSettingFragment.PICK_FROM_CAMERA:
                        profileSetting.doUploadPhoto(false);
                        break;
                    case ProfileSettingFragment.PICK_AVATAR_FROM_CAMERA:
                        profileSetting.doUploadPhoto(true);
                        
                        break;
                    case ProfileSettingFragment.SELECT_VIDEO:
                        profileSetting.doUploadVideo();
                        break;
                    }
                }
                break;
            case SNAPSHOT:
                snapshot.activityResult(requestCode, resultCode, data);
                break;
            case SETTING:
                break;
            case INVITE_FRIEND:
                break;
            case VIPROOM: 
            	if(GetVIPFragment.btn_get_vip_package != null)
            		GetVIPFragment.btn_get_vip_package.setEnabled(true);
            	if(ProfileSettingFragment.profileInfoObj != null && ProfileSettingFragment.profileInfoObj.isIs_vip()){
					VIPRoomFragment vipRoom = new VIPRoomFragment(this);
					vipRoom.initVIPRoom();				
            	}
                break;
            case VERIFIED:
            	Session.getActiveSession().onActivityResult(this, requestCode,
        				resultCode, data);
            	if(resultCode == RESULT_OK && data != null){
            		if(data.getBooleanExtra(Constants.COMEBACK_SNAPSHOT, false)){
                		if (this.getMenu() != MenuOakclub.SNAPSHOT) {
                			this.setMenu(MenuOakclub.SNAPSHOT);
                			SnapshotFragment snapshot = new SnapshotFragment(
                					this);
                			snapshot.initSnapshot();
                			this.snapshot = snapshot;
                		}
                	}
            	}
            	
                break;
            default: 
                break;
        }
    }

	@Override
	protected void onResume() {		
		if(!OakClubUtil.isInternetAccess(this)){			
			VerifiedActivity.firstOpenSessonCall = false;
			VerifiedActivity.firstRequestCall = false;
			if(!VerifiedActivity.start_login)
				finish();
		}
		super.onResume();
		if(((Button) findViewById(R.id.btn_continue_verified)) != null)
			((Button) findViewById(R.id.btn_continue_verified)).setEnabled(true);
		if(((Button) findViewById(R.id.btn_skip_verified)) != null)
			((Button) findViewById(R.id.btn_skip_verified)).setEnabled(true);
	}
    @Override
    protected void onPause(){
        System.gc();
        super.onPause();
    }
    
    @Override
    protected void onDestroy(){
        //OakClubUtil.recycleImagesFromView(view);
        System.gc();
        super.onDestroy();
    }
    
    public MenuOakclub getMenu() {
        return menu;
    }
    
    public void setMenu(MenuOakclub menu) {
        this.menu = menu;
    }
    
    public static enum MenuOakclub{
        PROFILE(0), 
        SNAPSHOT(1), 
        SETTING(2), 
        INVITE_FRIEND(3),
        VIPROOM(4),
        VERIFIED(5);
        
        private final int menuChose;
        
        private MenuOakclub(int menu){
            this.menuChose = menu;
        }
        
        public int getMenuOakclub(){
            return menuChose;
        }
    };
}
