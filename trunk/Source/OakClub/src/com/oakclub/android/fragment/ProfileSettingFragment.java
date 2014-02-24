package com.oakclub.android.fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import android.widget.FrameLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;
import net.simonvt.numberpicker.NumberPicker;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netcompss.loader.LoadJNI;
import com.oakclub.android.R;
import com.oakclub.android.SlidingActivity;
import com.oakclub.android.SlidingActivity.MenuOakclub;
import com.oakclub.android.VideoViewActivity;
import com.oakclub.android.base.SlidingMenuActivity;
import com.oakclub.android.core.RequestUI;
import com.oakclub.android.model.ListPhotoReturnDataItemObject;
import com.oakclub.android.model.PostMethodReturnObject;
import com.oakclub.android.model.ProfileInfoData;
import com.oakclub.android.model.SettingReturnObject;
import com.oakclub.android.model.UploadPhotoReturnObject;
import com.oakclub.android.model.UploadVideoObject;
import com.oakclub.android.util.BitmapScaler;
import com.oakclub.android.util.Constants;
import com.oakclub.android.util.OakClubUtil;
import com.oakclub.android.view.CheckBoxCustom;
import com.oakclub.android.view.CircleImageView;
import com.oakclub.android.view.RadioButtonCustom;

public class ProfileSettingFragment{
    public static ProfileInfoData profileInfoObj;
    CircleImageView avatar;
    CircleImageView video;
    ImageView imgAddVideo;
    ImageView imgPlayVideo;
    ImageView imgDelVideo;
    TextView tvEditVideo;
    TextView tvEditPhoto;
    TextView userNameTv;
    TextView userBriefInfoTv;
    TextView profileTvName;
    TextView profileTvBirthdate;
    TextView profileTvEmail;
    TextView profileTvGender;
    TextView profileTvRelationStatus;
    TextView profileTvHeight;
    TextView profileTvWeight;
    TextView profileTvInterestedIn;
    TextView profileTvLocation;
    TextView profileTvEthnicity;
    TextView profileTvSchool;
    TextView profileTvLanguage;
    TextView profileTvWork;
    TextView profileTvAboutMe;

    RelativeLayout profileLayoutName;
    RelativeLayout profileLayoutBirthdate;
    RelativeLayout profileLayoutEmail;
    RelativeLayout profileLayoutGender;
    RelativeLayout profileLayoutRelationStatus;
    RelativeLayout profileLayoutHeight;
    RelativeLayout profileLayoutWeight;
    RelativeLayout profileLayoutInterestedIn;
    RelativeLayout profileLayoutLocation;
    RelativeLayout profileLayoutEthnicity;
    RelativeLayout profileLayoutSchool;
    RelativeLayout profileLayoutLanguage;
    RelativeLayout profileLayoutWork;
    RelativeLayout profileLayoutAboutMe;

    FrameLayout fltListViewPhoto;
    FrameLayout fltAvatar;
    FrameLayout fltVideo;
    HorizontalScrollView listViewPhoto;
    ArrayList<ListPhotoReturnDataItemObject> arrListPhoto;
    private static final int PADDING_DIALOG = 12;

    Button btnDone;
    boolean hasChange = false;
    public Uri mImageCaptureUri;

    Bitmap photo;
    SlidingActivity activity;
    public ProfileSettingFragment(SlidingActivity activity){
        this.activity = activity;
    }
    
    public void initProfile() {        
        activity.setTheme(R.style.PickerSampleTheme_Light);
        activity.init(R.layout.activity_profile_settings);

        int widthImageParam = (int)OakClubUtil.getWidthScreen(activity)/3;
        LayoutParams params = new LayoutParams(widthImageParam, widthImageParam);
        params.gravity = Gravity.CENTER;
        fltAvatar = (FrameLayout)findViewById(R.id.activity_profile_settings_flt_photo);
        fltAvatar.setLayoutParams(params);
        fltVideo = (FrameLayout)findViewById(R.id.activity_profile_settings_flt_video);
        fltVideo.setLayoutParams(params);
        
        arrListPhoto = profileInfoObj.getPhotos();
        avatar = (CircleImageView) findViewById(R.id.user_avatar);
        video = (CircleImageView) findViewById(R.id.activity_profile_settings_flt_video_civAvatar);
        imgAddVideo = (ImageView) findViewById(R.id.activity_profile_setting_flt_video_ImgAddvideo);
        imgPlayVideo = (ImageView) findViewById(R.id.activity_profile_setting_flt_video_ImgPlayvideo);
        params = new LayoutParams(widthImageParam/4, widthImageParam/4);
        params.gravity = Gravity.RIGHT|Gravity.TOP;
        imgDelVideo = (ImageView) findViewById(R.id.activity_profile_setting_flt_video_ImgDelvideo);
        imgDelVideo.setLayoutParams(params);
        
        params = new LayoutParams(widthImageParam/3, widthImageParam/3);
        params.gravity = Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL;
        tvEditVideo = (TextView) findViewById(R.id.activity_profile_settings_flt_video_tvEditvideo);
        tvEditPhoto = (TextView) findViewById(R.id.activity_profile_settings_flt_video_tvEditPhoto);
        tvEditVideo.setLayoutParams(params);
        tvEditPhoto.setLayoutParams(params);
        userNameTv = (TextView) findViewById(R.id.activity_profile_setting_rlt_twname);
        userBriefInfoTv = (TextView) findViewById(R.id.activity_profile_setting_rlt_twinfo);
        profileTvName = (TextView) findViewById(R.id.profileTvName);
        profileTvBirthdate = (TextView) findViewById(R.id.profileTvBirthdate);
        profileTvEmail = (TextView) findViewById(R.id.profileTvEmail);
        profileTvGender = (TextView) findViewById(R.id.profileTvGender);
        profileTvRelationStatus = (TextView) findViewById(R.id.profileTvRelationStatus);
        profileTvHeight = (TextView) findViewById(R.id.profileTvHeight);
        profileTvWeight = (TextView) findViewById(R.id.profileTvWeight);
        profileTvInterestedIn = (TextView) findViewById(R.id.profileTvInterestedIn);
        profileTvLocation = (TextView) findViewById(R.id.profileTvLocation);
        profileTvEthnicity = (TextView) findViewById(R.id.profileTvEthnicity);
        profileTvSchool = (TextView) findViewById(R.id.profileTvSchool);
        profileTvLanguage = (TextView) findViewById(R.id.profileTvLanguage);
        profileTvWork = (TextView) findViewById(R.id.profileTvWork);
        profileTvAboutMe = (TextView) findViewById(R.id.profileTvAboutMe);

        profileLayoutName = (RelativeLayout) findViewById(R.id.profileLayoutName);
        profileLayoutBirthdate = (RelativeLayout) findViewById(R.id.profileLayoutBirthdate);
        profileLayoutEmail = (RelativeLayout) findViewById(R.id.profileLayoutEmail);
        profileLayoutGender = (RelativeLayout) findViewById(R.id.profileLayoutGender);
        profileLayoutRelationStatus = (RelativeLayout) findViewById(R.id.profileLayoutRelationStatus);
        profileLayoutHeight = (RelativeLayout) findViewById(R.id.profileLayoutHeight);
        profileLayoutWeight = (RelativeLayout) findViewById(R.id.profileLayoutWeight);
        profileLayoutInterestedIn = (RelativeLayout) findViewById(R.id.profileLayoutInterestedIn);
        profileLayoutLocation = (RelativeLayout) findViewById(R.id.profileLayoutLocation);
        profileLayoutEthnicity = (RelativeLayout) findViewById(R.id.profileLayoutEthnicity);
        profileLayoutSchool = (RelativeLayout) findViewById(R.id.profileLayoutSchool);
        profileLayoutLanguage = (RelativeLayout) findViewById(R.id.profileLayoutLanguage);
        profileLayoutWork = (RelativeLayout) findViewById(R.id.profileLayoutWork);
        profileLayoutAboutMe = (RelativeLayout) findViewById(R.id.profileLayoutAboutMe);

        btnDone = (Button) findViewById(R.id.buttonDone);
        fltListViewPhoto = (FrameLayout) findViewById(R.id.flt_list_view_photo);
        
        loadProfileInfo();

        //profileLayoutName.setOnClickListener(listener);
        profileLayoutBirthdate.setOnClickListener(listener);
        profileLayoutEmail.setOnClickListener(listener);
        profileLayoutGender.setOnClickListener(listener);
        profileLayoutRelationStatus.setOnClickListener(listener);
        profileLayoutHeight.setOnClickListener(listener);
        profileLayoutWeight.setOnClickListener(listener);
        profileLayoutInterestedIn.setOnClickListener(listener);
        profileLayoutLocation.setOnClickListener(listener);
        profileLayoutEthnicity.setOnClickListener(listener);
        //profileLayoutSchool.setOnClickListener(listener);
        profileLayoutLanguage.setOnClickListener(listener);
        //profileLayoutWork.setOnClickListener(listener);
        profileLayoutAboutMe.setOnClickListener(listener);
        btnDone.setOnClickListener(listener);
        avatar.setOnClickListener(listener);
        imgPlayVideo.setOnClickListener(listener);
        imgAddVideo.setOnClickListener(listener);
        imgDelVideo.setOnClickListener(listener);
        tvEditVideo.setOnClickListener(listener);
    }
    
    public void release()
    {
    	
    }
    
    private View findViewById(int id){
        return activity.view.findViewById(id);
    }
    
    /* HieuPham */
    private void getDataListPhoto() {

        String url;
        
        listViewPhoto = new HorizontalScrollView(activity);
        listViewPhoto.setHorizontalScrollBarEnabled(false);
        LinearLayout linear = new LinearLayout(activity);
        LayoutParams layoutListView = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        linear.setLayoutParams(layoutListView);
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        int widthPhotoItem = (int) OakClubUtil.getWidthScreen(activity)/5;
        LinearLayout.LayoutParams layoutView = new LinearLayout.LayoutParams(
                widthPhotoItem, widthPhotoItem);
//                (int) OakClubUtil.convertDpToPixel(80, activity),
//                (int) OakClubUtil.convertDpToPixel(80, activity));
        layoutView.gravity = Gravity.CENTER;
        for (int i = 0; i < arrListPhoto.size(); i++) {
            FrameLayout view = (FrameLayout) inflater.inflate(
                    R.layout.item_photo_profile, null);
            view.setLayoutParams(layoutView);
            view.setId(0);
            ImageView imgPhoto = (CircleImageView) view
                    .findViewById(R.id.item_photo_profile_ciwPhoto);
            url = arrListPhoto.get(i).getTweet_image_link();
            url = OakClubUtil.getFullLink(activity, url, 100, 100, 1);
            OakClubUtil.loadImageFromUrl(activity, imgPhoto, url);
            
            TextView tvId = new TextView(activity);
            tvId.setId(2);
            tvId.setText(arrListPhoto.get(i).getId());
            tvId.setVisibility(View.GONE);
            view.addView(tvId);
            view.setOnClickListener(listener);
            linear.addView(view);
        }
        FrameLayout view = (FrameLayout) inflater.inflate(
                R.layout.item_photo_profile, null);
        view.setLayoutParams(layoutView);
        view.setId(1);
        ImageView imgPhoto = (CircleImageView) view
                .findViewById(R.id.item_photo_profile_ciwPhoto);
        imgPhoto.setVisibility(View.GONE);
        ImageView imgFrame = (ImageView) view
                .findViewById(R.id.item_photo_profile_ivFrame);
        imgFrame.setImageResource(R.drawable.addphto_btn);
        view.setOnClickListener(listener);
        linear.addView(view);

        listViewPhoto.invalidate();
        listViewPhoto.addView(linear);
        fltListViewPhoto.addView(listViewPhoto);
    }

    Intent intent;
    public static final int SELECT_VIDEO = 2;
    public static final int PICK_FROM_CAMERA = 0;
    public static final int PICK_AVATAR_FROM_CAMERA = 1;
    private android.view.View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (profileInfoObj == null ) {
                return;
            }
            switch (v.getId()) {
            case R.id.user_avatar:
//                imagePath = null;
                intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                activity.startActivityForResult(Intent.createChooser(intent,
                        activity.getResources().getString(R.string.txt_complete_action_photo)),
                        PICK_AVATAR_FROM_CAMERA);
                break;
            case R.id.activity_profile_setting_flt_video_ImgAddvideo:
//              imagePath = null;
              intent = new Intent();

              intent.setType("video/*");
              intent.setAction(Intent.ACTION_GET_CONTENT);
              activity.startActivityForResult(Intent.createChooser(intent,
                      activity.getResources().getString(R.string.txt_complete_action_photo)),
                      SELECT_VIDEO); 
              break;
            case R.id.activity_profile_settings_flt_video_tvEditvideo:
//              imagePath = null;
              intent = new Intent();
              intent.setType("video/*");
              intent.setAction(Intent.ACTION_GET_CONTENT);
              activity.startActivityForResult(Intent.createChooser(intent,
                      activity.getResources().getString(R.string.txt_complete_action_photo)),
                      SELECT_VIDEO); 
              break;
            case R.id.activity_profile_setting_flt_video_ImgPlayvideo:
//              imagePath = null;
              if(!profileInfoObj.getVideo_link().equals("")){
                  String urlVideo = OakClubUtil.getFullLinkVideo(activity,
                                  profileInfoObj.getVideo_link(), Constants.VIDEO_EXTENSION);
                  intent = new Intent(activity, VideoViewActivity.class);
                  intent.putExtra("url_video", urlVideo);
                  activity.startActivity(intent);
              }
              break;
            case R.id.activity_profile_setting_flt_video_ImgDelvideo:
                solveDeleteVideo();
              break;
            case R.id.profileLayoutName:
                TextView profileName = (TextView) v
                        .findViewById(R.id.profileName);
                solveChangeField(Constants.PROFILE_SETTING_FIELD_NAME,
                        profileInfoObj.getName(), profileName.getText()
                                .toString());
                break;
            case R.id.profileLayoutEmail:
                TextView profileEmail = (TextView) v
                        .findViewById(R.id.profileEmail);
                solveChangeField(Constants.PROFILE_SETTING_FIELD_EMAIL,
                        profileInfoObj.getEmail(), profileEmail.getText()
                                .toString());
                break;
            case R.id.profileLayoutHeight:
                TextView profileHeight = (TextView) v
                        .findViewById(R.id.profileHeight);
                solveChangeFieldWithNumberPicker(
                        Constants.PROFILE_SETTING_FIELD_HEIGHT,
                        profileInfoObj.getHeight(), profileHeight.getText()
                                .toString());
                break;
            case R.id.profileLayoutWeight:
                TextView profileWeight = (TextView) v
                        .findViewById(R.id.profileWeight);
                solveChangeFieldWithNumberPicker(
                        Constants.PROFILE_SETTING_FIELD_WEIGHT,
                        profileInfoObj.getWeight(), profileWeight.getText()
                                .toString());
                break;
            case R.id.profileLayoutSchool:
                TextView profileSchool = (TextView) v
                    .findViewById(R.id.profileSchool);
                solveChangeField(Constants.PROFILE_SETTING_FIELD_SCHOOL,
                profileInfoObj.getSchool(), profileSchool.getText().toString());
                break;
            case R.id.profileLayoutAboutMe:
                TextView profileAboutMe = (TextView) v
                        .findViewById(R.id.profileAboutMe);
                solveChangeField(Constants.PROFILE_SETTING_FIELD_ABOUT_ME,
                        profileInfoObj.getAbout_me(), profileAboutMe.getText()
                                .toString());
                break;
            case R.id.profileLayoutWork:
                TextView profileWork = (TextView) v
                        .findViewById(R.id.profileWork);
                solveChangeBySingleChoice2(
                        Constants.PROFILE_SETTING_FIELD_WORK,
                        getWorkArrayListString(), getWorkArrayListId(),
                        getWorkId(profileInfoObj.getWork()), profileWork
                                .getText().toString());
                break;
            case R.id.profileLayoutGender:
                TextView profileGender = (TextView) v
                        .findViewById(R.id.profileGender);
                solveChangeBySingleChoice(
                        Constants.PROFILE_SETTING_FIELD_GENDER,
                        R.array.gender_list, profileInfoObj.getGender(),
                        profileGender.getText().toString());
                break;
            case R.id.profileLayoutInterestedIn:
                TextView profileInterestedIn = (TextView) v
                        .findViewById(R.id.profileInterestedIn);
                solveChangeBySingleChoice(
                        Constants.PROFILE_SETTING_FIELD_INTERESTED,
                        R.array.interested_in_list,
                        profileInfoObj.getInterested(), profileInterestedIn
                                .getText().toString());
                break;
            case R.id.profileLayoutEthnicity:
                TextView profileEthnicity = (TextView) v
                        .findViewById(R.id.profileEthnicity);
                solveChangeBySingleChoice2(
                        Constants.PROFILE_SETTING_FIELD_ETHNICITY,
                        getEthnicityArrayListString(), getEthnicityArrayListId(),
                        getEthnicityId(profileInfoObj.getEthnicity()),
                        profileEthnicity.getText().toString());
                break;
            case R.id.profileLayoutRelationStatus:
                TextView profileRelationStatus = (TextView) v
                        .findViewById(R.id.profileRelationStatus);
                solveChangeBySingleChoice2(
                        Constants.PROFILE_SETTING_FIELD_RELATION_STATUS,
                        getRelationshipArrayListString(), getRelationshipArrayListId(),
                        getRelationshipId(profileInfoObj
                                .getRelationship_status()),
                        profileRelationStatus.getText().toString());
                break;
            case R.id.profileLayoutLanguage:
                TextView profileLanguage = (TextView) v
                        .findViewById(R.id.profileLanguage);
                solveChangeLanguage(Constants.PROFILE_SETTING_FIELD_LANGUAGE,
                        getLanguageArrayListString(),
                        profileInfoObj.getLanguage(), profileLanguage.getText()
                                .toString());
                break;
            case R.id.profileLayoutBirthdate:
                TextView profileBirthdate = (TextView) v
                        .findViewById(R.id.profileBirthdate);
                solveChangeBirthdate(Constants.PROFILE_SETTING_FIELD_BIRTHDATE,
                        profileInfoObj.getBirthday_date(), profileBirthdate
                                .getText().toString());
                break;
            case R.id.buttonDone:
                //activity.finish();
                activity.setMenu(MenuOakclub.SNAPSHOT);
                SnapshotFragment snapshot = new SnapshotFragment(activity);
                snapshot.initSnapshot();
                break;
            case 0:
                TextView tvId = (TextView) v.findViewById(2);
                solveDeletePhoto(tvId.getText().toString());
                break;
            case 1:
//                imagePath = null;
                intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                activity.startActivityForResult(Intent.createChooser(intent,
                        "Complete action using"), PICK_FROM_CAMERA);
                break;
            default:
            }
        }
    };

    private void solveChangeField(final String fieldName,
            final String currentValue, final String title) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(activity);
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = LayoutInflater.from(activity.getApplicationContext());
        View mainRelativeLayout = inflater.inflate(
                R.layout.dialog_edit_text_input, null);
        dialog.setView(mainRelativeLayout, 0, 0, 0, 0);

        final TextView tvField = (TextView) mainRelativeLayout
                .findViewById(R.id.dialog_title);
        tvField.setText(title.toUpperCase());

        final EditText input = (EditText) mainRelativeLayout
                .findViewById(R.id.edt_content);
        input.setText(currentValue);
        Button okBtn;
        Button cancelBtn;
        okBtn = (Button) mainRelativeLayout.findViewById(R.id.ok_btn);
        cancelBtn = (Button) mainRelativeLayout.findViewById(R.id.cancel_btn);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String editable = input.getText().toString();
                if (editable != null && editable.length() != 0
                        && !editable.equals(currentValue)) {
                    hasChange = true;
                    SetProfileByFieldLoader loader = new SetProfileByFieldLoader(
                            "setField", activity, fieldName,
                            editable);
                    activity.getRequestQueue().addRequest(loader);

                    if (profileInfoObj != null) {
                        if (fieldName
                                .equals(Constants.PROFILE_SETTING_FIELD_NAME)) {
                            profileInfoObj.setName(editable);
                            profileTvName.setText(editable);
                        } else if (fieldName
                                .equals(Constants.PROFILE_SETTING_FIELD_SCHOOL)) {
                            profileInfoObj.setSchool(editable);
                            profileTvSchool.setText(editable);
                        } else if (fieldName
                                .equals(Constants.PROFILE_SETTING_FIELD_EMAIL)) {
                            profileInfoObj.setEmail(editable);
                            profileTvEmail.setText(editable);
                        } else if (fieldName
                                .equals(Constants.PROFILE_SETTING_FIELD_ABOUT_ME)) {
                            profileInfoObj.setAbout_me(editable);
                            profileTvAboutMe.setText(editable);
                        } else if (fieldName
                                .equals(Constants.PROFILE_SETTING_FIELD_HEIGHT)) {
                            profileInfoObj.setHeight(editable);
                            profileTvHeight.setText(editable);
                        } else if (fieldName
                                .equals(Constants.PROFILE_SETTING_FIELD_WEIGHT)) {
                            profileInfoObj.setWeight(editable);
                            profileTvWeight.setText(editable);
                        } else if (fieldName
                                .equals(Constants.PROFILE_SETTING_FIELD_INTERESTED)) {
                            profileInfoObj.setAbout_me(editable);
                            profileTvInterestedIn.setText(editable);
                        }
                    }
                }
                dialog.dismiss();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void solveChangeFieldWithNumberPicker(final String fieldName,
            final String currentValue, final String title) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(activity);
        final AlertDialog dialog = builder.create();// new
                                                    // Dialog(ProfileSettingActivity.this);
        LayoutInflater inflater = LayoutInflater.from(activity);
        View mainRelativeLayout = inflater.inflate(
                R.layout.dialog_number_picker_input, null);
        dialog.setView(mainRelativeLayout, 0, 0, 0, 0);

        final TextView tvField = (TextView) mainRelativeLayout
                .findViewById(R.id.dialog_title);
        tvField.setText(title.toUpperCase());

        final NumberPicker picker = (NumberPicker) mainRelativeLayout
                .findViewById(R.id.numberPicker2);
        picker.setFocusable(true);
        picker.setFocusableInTouchMode(true);
        int currentValueInt = 0;
        int defaultValuemax = 0;
        int defaultValuemin = 0;
        int defaultValue = 0;
        try {
            if (fieldName.equals(Constants.PROFILE_SETTING_FIELD_HEIGHT)) {
                defaultValuemax = Constants.DEFAULT_HEIGHT_MAX;
                defaultValue = Constants.DEFAULT_HEIGHT;
                defaultValuemin = Constants.DEFAULT_HEIGHT_MIN;
            } else if (fieldName.equals(Constants.PROFILE_SETTING_FIELD_WEIGHT)) {
                defaultValuemax = Constants.DEFAULT_WEIGHT_MAX;
                defaultValue = Constants.DEFAULT_WEIGHT;
                defaultValuemin = Constants.DEFAULT_WEIGHT_MIN;
            }
            if(Integer.parseInt(currentValue) <=0)
                currentValueInt = defaultValue;
            else if (Integer.parseInt(currentValue) < defaultValuemin)
                currentValueInt = defaultValuemin;
            else if (Integer.parseInt(currentValue) > defaultValuemax)
                currentValueInt = defaultValuemax;
            else
                currentValueInt = Integer.parseInt(currentValue);
        } catch (Exception e) {
            e.printStackTrace();
        }

        picker.setMaxValue(defaultValuemax);
        picker.setMinValue(defaultValuemin);
        picker.setValue(currentValueInt);
        Button okBtn;
        Button cancelBtn;
        okBtn = (Button) mainRelativeLayout.findViewById(R.id.ok_btn);
        cancelBtn = (Button) mainRelativeLayout.findViewById(R.id.cancel_btn);

        okBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String editable = "" + picker.getValue();
                if (editable != null && editable.length() != 0
                        && !editable.equals(currentValue)) {
                    hasChange = true;
                    SetProfileByFieldLoader loader = new SetProfileByFieldLoader(
                            "setField", activity, fieldName,
                            editable);
                    activity.getRequestQueue().addRequest(loader);

                    if (profileInfoObj != null) {
                        if (fieldName
                                .equals(Constants.PROFILE_SETTING_FIELD_HEIGHT)) {
                            profileInfoObj.setHeight(editable);
                            profileTvHeight.setText(editable);
                        } else if (fieldName
                                .equals(Constants.PROFILE_SETTING_FIELD_WEIGHT)) {
                            profileInfoObj.setWeight(editable);
                            profileTvWeight.setText(editable);
                        }
                    }
                }
                dialog.dismiss();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void solveChangeBySingleChoice(final String fieldName,
            int stringArrayId, final int currentValue, final String title) {
        final String[] stringList = activity.getResources()
                .getStringArray(stringArrayId);
        final ArrayList<RadioButton> radioButtons;
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(activity);
        final AlertDialog dialog = builder.create();// new
                                                    // Dialog(ProfileSettingActivity.this);
        LayoutInflater inflater = LayoutInflater.from(activity);
        View mainRelativeLayout = inflater.inflate(
                R.layout.dialog_radio_group_input, null);
        dialog.setView(mainRelativeLayout, 0, 0, 0, 0);

        final TextView tvField = (TextView) mainRelativeLayout
                .findViewById(R.id.dialog_title);
        tvField.setText(title.toUpperCase());

        final RadioGroup radioGroup = (RadioGroup) mainRelativeLayout
                .findViewById(R.id.radioGroup1);
        radioGroup.setOrientation(RadioGroup.VERTICAL);
        radioButtons = new ArrayList<RadioButton>();
        for (int i = 0; i < stringList.length; i++) {
            RadioButtonCustom radio = new RadioButtonCustom(
                    activity, activity.getResources().getDrawable(
                            R.drawable.radiogroup_selector2));
            radio.setText(stringList[i]);
            radio.setTextColor(Color.BLACK);

            // radio.setButtonDrawable(R.drawable.radiogroup_selector2);
            radio.setLayoutParams(new RadioGroup.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            int padding = (int) OakClubUtil.convertDpToPixel(PADDING_DIALOG,
                    activity);
            radio.setPadding(padding, padding, padding, padding);

            radioButtons.add(radio);
            radioGroup.addView(radio);
            ImageView separator = new ImageButton(activity);
            separator.setBackgroundResource(R.drawable.separators);
            separator.setLayoutParams(new RadioGroup.LayoutParams(
                    LayoutParams.MATCH_PARENT, 1));
            radioGroup.addView(separator);

        }
        if (currentValue >= 0) {
            radioGroup.check(radioButtons.get(currentValue).getId());
        }

        Button okBtn;
        Button cancelBtn;
        okBtn = (Button) mainRelativeLayout.findViewById(R.id.ok_btn);
        cancelBtn = (Button) mainRelativeLayout.findViewById(R.id.cancel_btn);
        okBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                int selectedButtonId = radioGroup.getCheckedRadioButtonId();
                int selectedId = -1;
                for (int i = 0; i < radioButtons.size(); i++) {
                    if (radioButtons.get(i).getId() == selectedButtonId) {
                        selectedId = i;
                        break;
                    }
                }
                if (selectedId != -1 && selectedId != currentValue) {
                    hasChange = true;
                    SetProfileByFieldLoader loader = new SetProfileByFieldLoader(
                            "setField", activity, fieldName,
                            "" + selectedId);
                    activity.getRequestQueue().addRequest(loader);

                    if (profileInfoObj != null) {
                        if (fieldName
                                .equals(Constants.PROFILE_SETTING_FIELD_GENDER)) {
                            profileInfoObj.setGender(selectedId);
                            profileTvGender
                                    .setText("" + stringList[selectedId]);
                        } else if (fieldName
                                .equals(Constants.PROFILE_SETTING_FIELD_INTERESTED)) {
                            profileInfoObj.setInterested(selectedId);
                            profileTvInterestedIn
                                    .setText(stringList[selectedId]);
                        } else if (fieldName
                                .equals(Constants.PROFILE_SETTING_FIELD_ETHNICITY)) {
                            profileInfoObj.setEthnicity(selectedId);
                            profileTvEthnicity.setText(stringList[selectedId]);
                        }

                    }
                }
                dialog.dismiss();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void solveChangeBySingleChoice2(final String fieldName,
            final ArrayList<String> stringList, final ArrayList<Integer> idList, final int currentId,
            final String title) {
        final ArrayList<RadioButton> radioButtons;
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(activity);
        final AlertDialog dialog = builder.create();// new
                                                    // Dialog(ProfileSettingActivity.this);
        LayoutInflater inflater = LayoutInflater.from(activity);
        View mainRelativeLayout = inflater.inflate(
                R.layout.dialog_radio_group_input, null);
        dialog.setView(mainRelativeLayout, 0, 0, 0, 0);

        final TextView tvField = (TextView) mainRelativeLayout
                .findViewById(R.id.dialog_title);
        tvField.setText(title.toUpperCase());

        final RadioGroup radioGroup = (RadioGroup) mainRelativeLayout
                .findViewById(R.id.radioGroup1);
        radioGroup.setOrientation(RadioGroup.VERTICAL);
        radioButtons = new ArrayList<RadioButton>();
        Log.v("size", stringList.size() + "");
        for (int i = 0; i < stringList.size(); i++) {
            RadioButtonCustom radio = new RadioButtonCustom(
                    activity, activity.getResources().getDrawable(
                            R.drawable.radiogroup_selector2));
            radio.setText(stringList.get(i));
            radio.setTextColor(Color.BLACK);
            // radio.setButtonDrawable(R.drawable.radiogroup_selector2);
            int padding = (int) OakClubUtil.convertDpToPixel(PADDING_DIALOG,
                    activity);
            radio.setPadding(padding, padding, padding, padding);
            radio.setId(idList.get(i));
            radio.setLayoutParams(new RadioGroup.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            radioButtons.add(radio);
            radioGroup.addView(radio);
            ImageView separator = new ImageButton(activity);
            separator.setBackgroundResource(R.drawable.separators);
            separator.setLayoutParams(new RadioGroup.LayoutParams(
                    LayoutParams.MATCH_PARENT, 1));
            radioGroup.addView(separator);
        }
        radioGroup.check(currentId);

        Button okBtn;
        Button cancelBtn;
        okBtn = (Button) mainRelativeLayout.findViewById(R.id.ok_btn);
        cancelBtn = (Button) mainRelativeLayout.findViewById(R.id.cancel_btn);
        okBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                int selectedButtonId = radioGroup.getCheckedRadioButtonId();
                int selectedId = -1;
                for (int i = 0; i < radioButtons.size(); i++) {
                    if (radioButtons.get(i).getId() == selectedButtonId) {
                        selectedId = radioButtons.get(i).getId();
                        break;
                    }
                }
                if (selectedId != -1 && selectedId != currentId) {
                    hasChange = true;
                    SetProfileByFieldLoader loader = new SetProfileByFieldLoader(
                            "setField", activity, fieldName,
                            "" + selectedId);
                    activity.getRequestQueue().addRequest(loader);

                    if (profileInfoObj != null) {
                        if (fieldName
                                .equals(Constants.PROFILE_SETTING_FIELD_GENDER)) {
                            profileInfoObj.setGender(selectedId);
                            profileTvGender.setText(""
                                    + stringList.get(selectedId));
                        } else if (fieldName
                                .equals(Constants.PROFILE_SETTING_FIELD_INTERESTED)) {
                            profileInfoObj.setInterested(selectedId);
                            profileTvInterestedIn.setText(stringList
                                    .get(selectedId));
                        } else if (fieldName
                                .equals(Constants.PROFILE_SETTING_FIELD_ETHNICITY)) {
                            profileInfoObj.setEthnicity(selectedId);
                            profileTvEthnicity.setText(stringList
                                    .get(selectedId));
                        } else if (fieldName
                                .equals(Constants.PROFILE_SETTING_FIELD_WORK)) {
                            profileInfoObj.setWork(selectedId);
                            profileTvWork.setText(getWorkString(selectedId));
                        } else if (fieldName
                                .equals(Constants.PROFILE_SETTING_FIELD_RELATION_STATUS)) {
                            profileInfoObj.setRelationship_status(selectedId);
                            profileTvRelationStatus.setText(stringList
                                    .get(selectedId));
                        }

                    }
                }
                dialog.dismiss();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void solveChangeLanguage(final String fieldName,
            final ArrayList<String> stringList,
            final ArrayList<Integer> currentValue, final String title) {
        LinearLayout linearLayout;
        final ArrayList<CheckBox> checkboxList;
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(activity);
        final AlertDialog dialog = builder.create();// new
                                                    // Dialog(ProfileSettingActivity.this);
        LayoutInflater inflater = LayoutInflater.from(activity);
        View mainRelativeLayout = inflater.inflate(
                R.layout.dialog_radio_group_input, null);
        dialog.setView(mainRelativeLayout, 0, 0, 0, 0);

        final TextView tvField = (TextView) mainRelativeLayout
                .findViewById(R.id.dialog_title);
        tvField.setText(title.toUpperCase());

        final RadioGroup radioGroup = (RadioGroup) mainRelativeLayout
                .findViewById(R.id.radioGroup1);
        radioGroup.setVisibility(View.GONE);
        linearLayout = (LinearLayout) mainRelativeLayout
                .findViewById(R.id.linearLayoutGroup);
        radioGroup.setOrientation(RadioGroup.VERTICAL);
        checkboxList = new ArrayList<CheckBox>();
        for (int i = 0; i < stringList.size(); i++) {
            CheckBoxCustom cb = new CheckBoxCustom(activity,
                    activity.getResources().getDrawable(R.drawable.radiogroup_selector));
            cb.setText(stringList.get(i));
            cb.setTextColor(Color.BLACK);
            int padding = (int) OakClubUtil.convertDpToPixel(PADDING_DIALOG,
                    activity);
            cb.setPadding(padding, padding, padding, padding);
            // cb.setButtonDrawable(R.drawable.radiogroup_selector);
            cb.setChecked(false);
            checkboxList.add(cb);
            linearLayout.addView(cb);
        }
        for (int i = 0; i < currentValue.size(); i++) {
            for (int j = 0; j < SlidingMenuActivity.mDataLanguageObj
                    .getLanguage().size(); j++) {
                if (SlidingMenuActivity.mDataLanguageObj.getLanguage().get(j)
                        .getId() == currentValue.get(i)) {
                    checkboxList.get(j).setChecked(true);
                }
            }
        }

        Button okBtn;
        Button cancelBtn;
        okBtn = (Button) mainRelativeLayout.findViewById(R.id.ok_btn);
        cancelBtn = (Button) mainRelativeLayout.findViewById(R.id.cancel_btn);
        okBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ArrayList<Integer> selectedId = new ArrayList<Integer>();
                String str = "";
                for (int i = 0; i < checkboxList.size(); i++) {
                    if (checkboxList.get(i).isChecked()) {
                        selectedId.add(i);
                        if (str.length() != 0) {
                            str = str
                                    + ","
                                    + activity.mDataLanguageObj.getLanguage().get(i)
                                            .getId();
                        } else {
                            str = str
                                    + ""
                                    + activity.mDataLanguageObj.getLanguage().get(i)
                                            .getId();
                        }
                    }
                }
                if (true) {
                    hasChange = true;
                    SetProfileByFieldLoader loader = new SetProfileByFieldLoader(
                            "setField", activity, fieldName,
                            str);
                    activity.getRequestQueue().addRequest(loader);

                    if (profileInfoObj != null) {
                        profileInfoObj.setLanguage(selectedId);
                        profileTvLanguage
                                .setText(getLanguageString(selectedId));
                    }
                }
                dialog.dismiss();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void solveChangeBirthdate(final String fieldName,
            final String currentValue, final String title) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = dateFormat.parse(currentValue);
            System.out.println(date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        DatePickerDialog.OnDateSetListener pDateSetListener = new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int month, int day) {
            }
        };
        if (date != null) {
            int year = date.getYear();
            int month = date.getMonth();
            int day = date.getDate();
            if (year < 100) {
                year += 1900;
            }
            final DatePickerDialog dialog = new DatePickerDialog(activity,
                    R.style.dialog_light, pDateSetListener, year, month, day);
            dialog.setTitle(title.toUpperCase());
            final DatePicker datePicker = dialog.getDatePicker();
            datePicker.init(year, month, day, null);
            OnClickListener listener = new OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog2, int which) {
                    int year;
                    int month;
                    int day;
                    datePicker.clearFocus();
                    year = datePicker.getYear();
                    month = datePicker.getMonth();
                    day = datePicker.getDayOfMonth();
                    Date date2 = new Date(year - 1900, month, day);
                    String newDay = dateFormat.format(date2);
                    if (newDay != null && !newDay.equals(currentValue)) {
                        hasChange = true;
                        SetProfileByFieldLoader loader = new SetProfileByFieldLoader(
                                "setField",activity,
                                fieldName, "" + newDay);
                        activity.getRequestQueue().addRequest(loader);

                        if (profileInfoObj != null) {
                            profileInfoObj.setBirthday_date(newDay);
                            profileTvBirthdate.setText("" + newDay);
                        }
                    }
                }
            };
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Done", listener);
            dialog.show();
        }
    }

    private void loadProfileInfo(){
        if (profileInfoObj != null) {
            
            String url = OakClubUtil.getFullLink(activity,
                    profileInfoObj.getAvatar());
            OakClubUtil.loadImageFromUrl(activity,
                    url, avatar);
            
            if(!profileInfoObj.getVideo_link().equals("")){
                url =OakClubUtil.getFullLinkVideo(activity,
                        profileInfoObj.getVideo_link(), Constants.PHOTO_EXTENSION);
                OakClubUtil.loadImageFromUrl(activity,
                        url, video);
                imgAddVideo.setVisibility(View.GONE);
            }
            else{
                imgDelVideo.setVisibility(View.GONE);
                video.setVisibility(View.GONE);
                imgPlayVideo.setVisibility(View.GONE);
                imgAddVideo.setVisibility(View.VISIBLE);
            }
            getDataListPhoto();
            
            Calendar cal = Calendar.getInstance();
            final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String yearNow = sdf.format(cal.getTime());
            Log.v("Year:", yearNow);
            String age = Integer.parseInt(yearNow.split("/")[2])
                    - Integer.parseInt(profileInfoObj.getBirthday_date()
                            .split("/")[2]) + "";
            Log.v("Age:", age);
            userNameTv.setText(profileInfoObj.getName() + ", "+ age);
            userBriefInfoTv.setText(getWorkString(getWorkId(profileInfoObj.getWork())) + ", " + 
                                    profileInfoObj.getLocation().getName());
            profileTvName.setText("" + profileInfoObj.getName());
            profileTvBirthdate.setText(""
                    + profileInfoObj.getBirthday_date());
            profileTvEmail.setText("" + profileInfoObj.getEmail());
            if (profileInfoObj.getGender() == 1) {
                profileTvGender.setText(""
                        + activity.getString(R.string.profile_gender_male));
            } else {
                profileTvGender.setText(""
                        + activity.getString(R.string.profile_gender_female));
            }
            profileTvLocation.setText(""
                    + profileInfoObj.getLocation().getName());
            profileTvHeight.setText("" + profileInfoObj.getHeight());
            profileTvWeight.setText("" + profileInfoObj.getWeight());
            profileTvWork.setText(""
                    + getWorkString(getWorkId(profileInfoObj.getWork())));
            profileTvAboutMe.setText("" + profileInfoObj.getAbout_me());
            profileTvSchool.setText("" + profileInfoObj.getSchool());
            profileTvEthnicity.setText(""
                    + getEthnicityString(profileInfoObj.getEthnicity()));
            profileTvLanguage.setText(""
                    + getLanguageString(profileInfoObj.getLanguage()));
            profileTvRelationStatus.setText(""
                    + getRelationshipString(profileInfoObj
                            .getRelationship_status()));
            profileTvInterestedIn.setText(""
                    + activity.getResources().getStringArray(
                            R.array.interested_in_list)[profileInfoObj
                            .getInterested()]);
        }
    }
    
    class SetProfileByFieldLoader extends RequestUI {
        String field;
        String value;
        SettingReturnObject obj;

        public SetProfileByFieldLoader(Object key, Activity activity,
                String field, String value) {
            super(key, activity);
            this.field = field;
            this.value = value;
        }

        @Override
        public void execute() throws Exception {
            obj = activity.oakClubApi.setProfileByField(field, value);
        }

        @Override
        public void executeUI(Exception ex) {
            if (obj != null && obj.isStatus()) {

            } else {
//              Toast.makeText(getApplicationContext(),
//                      getString(R.string.abnormal_error_message),
//                      Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void solveDeleteVideo() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(activity);
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = LayoutInflater.from(activity);
        View layout = inflater.inflate(R.layout.dialog_warning, null);
        dialog.setView(layout, 0, 0, 0, 0);

        TextView tvTitle = (TextView)layout.findViewById(R.id.dialog_warning_lltheader_tvTitle);
        //tvTitle.setText(getResources().getString(R.string.txt_title_delete_photo_warning));
        TextView tvMessage = (TextView)layout.findViewById(R.id.dialog_warning_tvQuestion);
        tvMessage.setText(activity.getResources().getString(R.string.txt_delete_video_question));
        Button btOk = (Button) layout.findViewById(R.id.dialog_warning_lltfooter_btOK);
        Button btCancel = (Button) layout
                .findViewById(R.id.dialog_warning_lltfooter_btCancel);
        
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                DeleteUserVideoLoader loader = new DeleteUserVideoLoader(
                        "deleteVideo", activity);
                activity.getRequestQueue().addRequest(loader);
                dialog.dismiss();
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    
    private void solveDeletePhoto(final String id) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(activity);
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = LayoutInflater.from(activity);
        View layout = inflater.inflate(R.layout.dialog_warning, null);
        dialog.setView(layout, 0, 0, 0, 0);
        
        TextView tvTitle = (TextView)layout.findViewById(R.id.dialog_warning_lltheader_tvTitle);
        //tvTitle.setText(getResources().getString(R.string.txt_title_delete_photo_warning));
        Button btOk = (Button) layout.findViewById(R.id.dialog_warning_lltfooter_btOK);
        Button btCancel = (Button) layout
                .findViewById(R.id.dialog_warning_lltfooter_btCancel);
        
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                DeleteUserPhotoLoader loader = new DeleteUserPhotoLoader(
                        "deletePhoto", activity, id);
                activity.getRequestQueue().addRequest(loader);
                dialog.dismiss();
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private static final int PHOTO_UPLOAD_MAX_SIZE = 3;
    public void doUploadPhoto(boolean isAvatar) {
        File file = null;
        String filePath = "";
        filePath = getPath(activity, mImageCaptureUri);
        if(filePath!= null && !filePath.equals("")){
            file = new File(filePath);
            if (checkFileMax(file, PHOTO_UPLOAD_MAX_SIZE)) {
                showDialogWarning(activity.getString(R.string.warning_profile_max_size_photo));
            }
            else{
                UploadProfilePhotoLoader loader = new UploadProfilePhotoLoader(
                        "uploadPhoto", activity, isAvatar, file);
                activity.getRequestQueue().addRequest(loader);
            }
        }
    }
    

    private static final int VIDEO_UPLOAD_MAX_SIZE = 3;
    public void doUploadVideo() {
        String filePath = "";
        filePath = getPath(activity, mImageCaptureUri);
        if(filePath!= null && !filePath.equals("")){
            if (checkIfFileExistAndNotEmpty(filePath)) {
                new TranscdingBackground(activity, filePath, OakClubUtil.getFileStore(activity).getAbsolutePath()).execute();
            }
            else {
                showDialogWarning(activity.getString(R.string.warning_not_found_video));
            }
        }
    }
    
    private boolean checkFileMax(File file, int maxSize){
        long fileSizeInMB = file.length()/1024/1024;
        if (fileSizeInMB > maxSize) 
            return true;
        return false;
    }
    private void showDialogWarning(String twWarning){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(activity);
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = LayoutInflater.from(activity);
        View layout = inflater.inflate(R.layout.dialog_warning_ok, null);
        dialog.setView(layout, 0, 0, 0, 0);
        Button btOk = (Button) layout.findViewById(R.id.dialog_internet_access_lltfooter_btOK);
        TextView tvWarning = (TextView)layout.findViewById(R.id.dialog_warning_tvQuestion);
        tvWarning.setText(twWarning);
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private String getRealPathFromURI(Context context, Uri contentUri) {
      Cursor cursor = null;
      try { 
        String[] proj = { MediaStore.Images.Media.DATA };
        cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
      } finally {
        if (cursor != null) {
          cursor.close();
        }
      }
    }



    @SuppressLint("NewApi")
    private String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
    
    private String getDataColumn(Context context, Uri uri, String selection,
            String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    
    class UploadProfilePhotoLoader extends RequestUI {
        boolean is_Avatar;
        UploadPhotoReturnObject obj;
        File file;
        ProgressDialog pdLoading;

        public UploadProfilePhotoLoader(Object key, Activity activity,
                boolean is_Avatar, File file) {
            super(key, activity);
            this.is_Avatar = is_Avatar;
            this.file = file;

            pdLoading = new ProgressDialog(activity);
            pdLoading.setMessage(activity.getString(
                    R.string.txt_upload_photo));
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        public void execute() throws Exception {
            obj = activity.oakClubApi.UploadProfilePhoto(is_Avatar, file);
        }

        @Override
        public void executeUI(Exception ex) {
            if (pdLoading != null && pdLoading.isShowing()) {
                pdLoading.dismiss();
            }
            if (obj != null && obj.isStatus()) {
                ListPhotoReturnDataItemObject photoObj = new ListPhotoReturnDataItemObject();
                photoObj.setId(obj.getData().getId());
                photoObj.setTweet_image_link(obj.getData()
                        .getTweet_image_link());
                photoObj.setIs_profile_picture(obj.getData().isIs_avatar());
                if (is_Avatar) {
                    String imageUrl = OakClubUtil.getFullLink(
                            activity, photoObj
                                    .getTweet_image_link());
                    OakClubUtil.loadImageFromUrl(activity, imageUrl,
                            avatar);
                    profileInfoObj.setAvatar(photoObj
                            .getTweet_image_link());
                    activity.isChangedAvatar = true;
                    arrListPhoto.add(0, photoObj);
                }
                else arrListPhoto.add(1, photoObj);
                profileInfoObj.setPhotos(arrListPhoto);
                if (fltListViewPhoto.getChildCount() > 0) {
                    fltListViewPhoto.removeAllViews();
                    getDataListPhoto();
                }
            } else {
                OakClubUtil.enableDialogWarning(activity,
                        activity.getResources().getString(R.string.txt_warning),
                        activity.getResources().getString(R.string.value_upload_failed));
            }
        }

    }
    
    class UploadProfileVideoLoader extends RequestUI {
        UploadVideoObject obj;
        File file;
        ProgressDialog pdLoading;

        public UploadProfileVideoLoader(Object key, Activity activity, File file) {
            super(key, activity);
            this.file = file;

            pdLoading = new ProgressDialog(activity);
            pdLoading.setMessage(activity.getString(
                    R.string.txt_upload_video));
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        public void execute() throws Exception {
            obj = activity.oakClubApi.UploadProfileVideo(file);
        }

        @Override
        public void executeUI(Exception ex) {
            if (pdLoading != null && pdLoading.isShowing()) {
                pdLoading.dismiss();
            }
            if (obj != null && obj.isStatus()) {
            	profileInfoObj.setVideo_link(obj.getData());
                String urlVideo = OakClubUtil.getFullLinkVideo(activity, obj.getData(), ".jpg");
                OakClubUtil.loadImageFromUrl(activity, urlVideo, video);
                imgPlayVideo.setVisibility(View.VISIBLE);
                imgDelVideo.setVisibility(View.VISIBLE);
                video.setVisibility(View.VISIBLE);
                imgAddVideo.setVisibility(View.GONE);
            } else {
                OakClubUtil.enableDialogWarning(activity,
                        activity.getResources().getString(R.string.txt_warning),
                        activity.getResources().getString(R.string.value_upload_failed));
            }
        }

    }

    class DeleteUserPhotoLoader extends RequestUI {
        String id;
        PostMethodReturnObject obj;

        ProgressDialog pdLoading;

        public DeleteUserPhotoLoader(Object key, Activity activity, String id) {
            super(key, activity);
            this.id = id;

            pdLoading = new ProgressDialog(activity);
            pdLoading.setMessage(activity.getString(
                    R.string.txt_loading));
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        public void execute() throws Exception {
            obj = activity.oakClubApi.DeleteUserPhoto(id);
        }

        @Override
        public void executeUI(Exception ex) {
            if (pdLoading != null && pdLoading.isShowing()) {
                pdLoading.dismiss();
            }
            if ( obj == null || !obj.isStatus() ) {
//              Toast.makeText(
//                      ProfileSettingActivity.this,
//                      ProfileSettingActivity.this
//                              .getString(R.string.value_upload_failed),
//                      Toast.LENGTH_SHORT).show();
                if(obj.getError_code()==2){
                    OakClubUtil.enableDialogWarning(activity, activity.getResources().getString(R.string.txt_warning), 
                            activity.getResources().getString(R.string.txt_delete_avatar));
                }
            } else {
                int idx = -1;
                for (int i = 0; i < arrListPhoto.size(); i++) {
                    if (arrListPhoto.get(i).getId().equals(id)) {
                        idx = i;
                        break;
                    }
                }
                if (idx != -1) {
                    arrListPhoto.remove(idx);
                    profileInfoObj.setPhotos(arrListPhoto);
                    if (fltListViewPhoto.getChildCount() > 0) {
                        fltListViewPhoto.removeAllViews();
                        getDataListPhoto();
                    }
//                  Toast.makeText(
//                          ProfileSettingActivity.this,
//                          ProfileSettingActivity.this
//                                  .getString(R.string.value_delete_success),
//                          Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
    
    class DeleteUserVideoLoader extends RequestUI {
        String id;
        PostMethodReturnObject obj;

        ProgressDialog pdLoading;

        public DeleteUserVideoLoader(Object key, Activity activity) {
            super(key, activity);

            pdLoading = new ProgressDialog(activity);
            pdLoading.setMessage(activity.getString(
                    R.string.txt_loading));
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        public void execute() throws Exception {
            obj = activity.oakClubApi.DeleteUserVideo();
        }

        @Override
        public void executeUI(Exception ex) {
            if (pdLoading != null && pdLoading.isShowing()) {
                pdLoading.dismiss();
            }
            if (obj == null || !obj.isStatus()) {
                OakClubUtil.enableDialogWarning(activity, activity.getResources().getString(R.string.txt_warning), 
                        activity.getResources().getString(R.string.value_delete_failed));
            } else {
                profileInfoObj.setVideo_link("");
                imgPlayVideo.setVisibility(View.GONE);
                imgDelVideo.setVisibility(View.GONE);
                video.setVisibility(View.GONE);
                imgAddVideo.setVisibility(View.VISIBLE);
            }
        }

    }

    private ArrayList<String> getLanguageArrayListString() {

        ArrayList<String> listString = new ArrayList<String>();
        if (SlidingMenuActivity.mDataLanguageObj != null)
            for (int i = 0; i < SlidingMenuActivity.mDataLanguageObj
                    .getLanguage().size(); i++) {
                listString.add(SlidingMenuActivity.mDataLanguageObj
                        .getLanguage().get(i).getName());
            }
        return listString;
    }

    private String getLanguageString(ArrayList<Integer> ids) {
        String str = "";
        if (SlidingMenuActivity.mDataLanguageObj != null && ids != null)
            for (int i = 0; i < SlidingMenuActivity.mDataLanguageObj
                    .getLanguage().size(); i++) {
                if (ids.contains(SlidingMenuActivity.mDataLanguageObj
                        .getLanguage().get(i).getId())) {
                    if (str.length() != 0) {
                        str += ",";
                    }
                    str += SlidingMenuActivity.mDataLanguageObj.getLanguage()
                            .get(i).getName();
                }
            }
        if (str.length() == 0) {
            str = "-";
        }
        return str;
    }

    private ArrayList<String> getWorkArrayListString() {

        ArrayList<String> listString = new ArrayList<String>();
        if (SlidingMenuActivity.mDataLanguageObj != null)
            for (int i = 0; i < SlidingMenuActivity.mDataLanguageObj
                    .getWork_cate().size(); i++) {
                Log.v("work-id", SlidingMenuActivity.mDataLanguageObj
                        .getWork_cate().get(i).getCate_name() + " " + SlidingMenuActivity.mDataLanguageObj
                        .getWork_cate().get(i).getCate_id());
                listString.add(SlidingMenuActivity.mDataLanguageObj
                        .getWork_cate().get(i).getCate_name());
            }
        return listString;
    }
    
    private ArrayList<Integer> getWorkArrayListId() {

        ArrayList<Integer> listId = new ArrayList<Integer>();
        if (SlidingMenuActivity.mDataLanguageObj != null)
            for (int i = 0; i < SlidingMenuActivity.mDataLanguageObj
                    .getWork_cate().size(); i++) {
                listId.add(SlidingMenuActivity.mDataLanguageObj
                        .getWork_cate().get(i).getCate_id());
            }
        return listId;
    }

    private ArrayList<String> getEthnicityArrayListString() {

        ArrayList<String> listString = new ArrayList<String>();
        if (SlidingMenuActivity.mDataLanguageObj != null)
            for (int i = 0; i < SlidingMenuActivity.mDataLanguageObj
                    .getEthnicity().size(); i++) {
                listString.add(SlidingMenuActivity.mDataLanguageObj
                        .getEthnicity().get(i).getName());
            }
        return listString;
    }
    
    private ArrayList<Integer> getEthnicityArrayListId() {

        ArrayList<Integer> listId = new ArrayList<Integer>();
        if (SlidingMenuActivity.mDataLanguageObj != null)
            for (int i = 0; i < SlidingMenuActivity.mDataLanguageObj
                    .getEthnicity().size(); i++) {
                listId.add(SlidingMenuActivity.mDataLanguageObj
                        .getEthnicity().get(i).getId());
            }
        return listId;
    }

    private ArrayList<String> getRelationshipArrayListString() {

        ArrayList<String> listString = new ArrayList<String>();
        if (SlidingMenuActivity.mDataLanguageObj != null)
            for (int i = 0; i < SlidingMenuActivity.mDataLanguageObj
                    .getRelationship_status().size(); i++) {
                Log.v("relation-id", SlidingMenuActivity.mDataLanguageObj
                        .getRelationship_status().get(i).getRel_text() + " " + SlidingMenuActivity.mDataLanguageObj
                        .getRelationship_status().get(i).getRel_status_id());
                listString.add(SlidingMenuActivity.mDataLanguageObj
                        .getRelationship_status().get(i).getRel_text());
            }
        return listString;
    }
    
    private ArrayList<Integer> getRelationshipArrayListId() {

        ArrayList<Integer> listId = new ArrayList<Integer>();
        if (SlidingMenuActivity.mDataLanguageObj != null)
            for (int i = 0; i < SlidingMenuActivity.mDataLanguageObj
                    .getRelationship_status().size(); i++) {
                listId.add(SlidingMenuActivity.mDataLanguageObj
                        .getRelationship_status().get(i).getRel_status_id());
            }
        return listId;
    }

    private String getWorkString(int id) {
        String str = "-";
        if (SlidingMenuActivity.mDataLanguageObj != null)
            for (int i = 0; i < SlidingMenuActivity.mDataLanguageObj
                    .getWork_cate().size(); i++) {
                if (SlidingMenuActivity.mDataLanguageObj.getWork_cate()
                        .get(i).getCate_id() == id) {
                    return SlidingMenuActivity.mDataLanguageObj
                            .getWork_cate().get(i).getCate_name();
                }
            }
        return str;
    }

    private int getWorkId(int id) {
        if (SlidingMenuActivity.mDataLanguageObj != null)
            for (int i = 0; i < SlidingMenuActivity.mDataLanguageObj
                    .getWork_cate().size(); i++) {
                if (SlidingMenuActivity.mDataLanguageObj.getWork_cate()
                        .get(i).getCate_id() == id) {
                        return id;
                }
            }
        return 0;
    }

    private String getRelationshipString(int id) {
        String str = "-";
        if (SlidingMenuActivity.mDataLanguageObj != null)
            for (int i = 0; i < SlidingMenuActivity.mDataLanguageObj
                    .getRelationship_status().size(); i++) {
                if (SlidingMenuActivity.mDataLanguageObj
                        .getRelationship_status().get(i).getRel_status_id() == id) {
                    return SlidingMenuActivity.mDataLanguageObj
                            .getRelationship_status().get(i).getRel_text();
                }
            }
        return str;
    }

    private int getRelationshipId(int id) {
        if (SlidingMenuActivity.mDataLanguageObj != null)
            for (int i = 0; i < SlidingMenuActivity.mDataLanguageObj
                    .getRelationship_status().size(); i++) {
                if (SlidingMenuActivity.mDataLanguageObj
                        .getRelationship_status().get(i).getRel_status_id() == id) {
                    return i;
                }
            }
        return 0;
    }

    private int getLanguageId(int id) {
        if (SlidingMenuActivity.mDataLanguageObj != null)
            for (int i = 0; i < SlidingMenuActivity.mDataLanguageObj
                    .getLanguage().size(); i++) {
                if (SlidingMenuActivity.mDataLanguageObj.getLanguage().get(i)
                        .getId() == id) {
                    return i;
                }
            }
        return 0;
    }

    private String getEthnicityString(int id) {
        String str = "-";
        if (SlidingMenuActivity.mDataLanguageObj != null)
            for (int i = 0; i < SlidingMenuActivity.mDataLanguageObj
                    .getEthnicity().size(); i++) {
                if (SlidingMenuActivity.mDataLanguageObj.getEthnicity().get(i)
                        .getId() == id) {
                    return SlidingMenuActivity.mDataLanguageObj.getEthnicity()
                            .get(i).getName();
                }
            }
        return str;
    }

    private int getEthnicityId(int id) {
        if (SlidingMenuActivity.mDataLanguageObj != null)
            for (int i = 0; i < SlidingMenuActivity.mDataLanguageObj
                    .getEthnicity().size(); i++) {
                if (SlidingMenuActivity.mDataLanguageObj.getEthnicity().get(i)
                        .getId() == id) {
                    return i;
                }
            }
        return 0;
    }


    /*
     * Compression video
     */
    public class TranscdingBackground extends AsyncTask<String, Integer, Integer>
    {
       
       ProgressDialog pdLoading;
       Activity _act;
       String inputFile = "";
       String outputFile = "";
       
       public TranscdingBackground (Activity act, String inputFile, String outputFile) {
           _act = act;
           this.inputFile = inputFile;
           this.outputFile = outputFile;
       }
   
       @Override
       protected void onPreExecute() {
           pdLoading=new ProgressDialog(_act);
           pdLoading.setMessage(_act.getString(R.string.txt_compressing_video));
           pdLoading.setCancelable(false);
           pdLoading.show();
       }

       protected Integer doInBackground(String... paths) {
           Log.i(Constants.TAG, "doInBackground started...");
           
           String[] complexCommand = {"ffmpeg","-y" ,
                   "-i", inputFile,
                   "-strict","experimental",
                   "-crf", "30",
                   "-preset", "ultrafast", 
                   "-acodec", "aac", 
                   "-ar", "44100", 
                   "-ac", "2", 
                   "-b:a", "96k", 
                   "-vcodec", "libx264", 
                   "-r", "25", 
                   "-b:v", "500k", 
                   "-s", "800x480",
                   "-f", "flv", outputFile + "/video.flv"};
           
           LoadJNI vk = new LoadJNI();
           
           try {
               vk.run(complexCommand, outputFile, activity.getApplicationContext());
             
           } catch (Throwable e) {
               Log.i(Constants.TAG, "vk run exeption.");
           }
           Log.i(Constants.TAG, "doInBackground finished");
           return Integer.valueOf(0);
       }

       protected void onProgressUpdate(Integer... progress) {

       }

       @Override
       protected void onCancelled() {
           Log.i(Constants.TAG, "onCancelled");
           //progressDialog.dismiss();
           super.onCancelled();
       }


       @Override
       protected void onPostExecute(Integer result) {
           Log.i(Constants.TAG, "onPostExecute");
           pdLoading.dismiss();
           File file = new File(outputFile + "/video.flv");

           if (checkFileMax(file, VIDEO_UPLOAD_MAX_SIZE)) {
               showDialogWarning(activity.getString(R.string.warning_profile_max_size_video));
           }
           else{
               UploadProfileVideoLoader loader = new UploadProfileVideoLoader(
                       "uploadVideo", activity, file);
               activity.getRequestQueue().addRequest(loader);
           }
           super.onPostExecute(result);

       }
    }
    
    private String[] utilConvertToComplex(String str) {
        String[] complex = str.split(" ");
        return complex;
    }
    
    private static boolean checkIfFileExistAndNotEmpty(String fullFileName) {
       File f = new File(fullFileName);
       long lengthInBytes = f.length();
       Log.d(Constants.TAG, fullFileName + " length in bytes: " + lengthInBytes);
       if (lengthInBytes > 100)
           return true;
       else {
           return false;
       }
   
   }
}
