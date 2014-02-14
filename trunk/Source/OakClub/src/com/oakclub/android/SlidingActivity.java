package com.oakclub.android;

import java.io.IOException;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;

import com.oakclub.android.base.SlidingMenuActivity;
import com.oakclub.android.fragment.ProfileSettingFragment;
import com.oakclub.android.fragment.SnapshotFragment;
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
        
        snapshot = new SnapshotFragment(this);
        snapshot.initSnapshot();
        this.setMenu(MenuOakclub.SNAPSHOT);
        
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
            default: 
                break;
        }
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
        INVITE_FRIEND(3);
        
        private final int menuChose;
        
        private MenuOakclub(int menu){
            this.menuChose = menu;
        }
        
        public int getMenuOakclub(){
            return menuChose;
        }
    };
}
