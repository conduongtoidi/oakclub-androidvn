package com.oakclub.android.fragment;

import java.util.ArrayList;
import java.util.Locale;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.google.android.gcm.GCMRegistrar;
import com.oakclub.android.MainActivity;
import com.oakclub.android.R;
import com.oakclub.android.SlidingActivity;
import com.oakclub.android.base.SlidingMenuActivity;
import com.oakclub.android.core.RequestUI;
import com.oakclub.android.model.GetDataLanguageReturnObject;
import com.oakclub.android.model.GetSnapshotSettingsReturnObject;
import com.oakclub.android.model.SettingObject;
import com.oakclub.android.util.Constants;
import com.oakclub.android.util.OakClubUtil;
import com.oakclub.android.view.RadioButtonCustom;
import com.oakclub.android.view.RangeSeekBar;
import com.oakclub.android.view.RangeSeekBar.OnRangeSeekBarChangeListener;

public class SettingFragment{
    GetSnapshotSettingsReturnObject snapshotObj;
    LinearLayout linearContact;
    LinearLayout linearLogout;
    FrameLayout fltMaleCheckbox;
    CheckBox cbMale;
    FrameLayout fltFemaleCheckbox;
    CheckBox cbFemale;
    FrameLayout fltIncludeFriendsCheckbox;
    CheckBox cbIncludeFriends;
    SeekBar distanceSeekbar;
    boolean maleChecked;
    boolean femaleChecked;
    boolean includeFriendsChecked;
    int maxDistance;
    TextView tvAgeLimit;
    TextView tvDistance;
    ViewGroup rangeSeekBarLayout;
    RangeSeekBar<Integer> seekBar;
    LinearLayout languageLayout;

    String[] stringList;
    TextView tvLanguage;
    SettingObject settingObject;
    ProgressDialog pdLoading;

    SlidingActivity activity;
    public SettingFragment(SlidingActivity activity){
        this.activity = activity;
    }
    
    public void initSetting() {
        activity.init(R.layout.activity_snapshot_setting);
        
        linearContact = (LinearLayout)findViewById(R.id.linear_contact_us);
        linearLogout = (LinearLayout)findViewById(R.id.linear_logout);
        fltMaleCheckbox = (FrameLayout)findViewById(R.id.flt_male_checkbox);
        cbMale = (CheckBox) findViewById(R.id.male_checkbox);
        fltFemaleCheckbox = (FrameLayout)findViewById(R.id.flt_female_checkbox);
        cbFemale = (CheckBox) findViewById(R.id.female_checkbox);
        fltIncludeFriendsCheckbox = (FrameLayout)findViewById(R.id.flt_include_friends_checkbox);
        cbIncludeFriends = (CheckBox)findViewById(R.id.include_friends_checkbox);
        distanceSeekbar = (SeekBar) findViewById(R.id.seekBarDistance);
        tvAgeLimit = (TextView)findViewById(R.id.tvAgeLimit);
        tvDistance = (TextView)findViewById(R.id.tvDistance);
        rangeSeekBarLayout = (ViewGroup)findViewById(R.id.range_seekbar_layout);
        languageLayout = (LinearLayout)findViewById(R.id.language_layout);
        seekBar = new RangeSeekBar<Integer>(Constants.MIN_SEARCH_AGE, Constants.MAX_SEARCH_AGE, activity);
        rangeSeekBarLayout.addView(seekBar);
        settingObject = new SettingObject();
        tvLanguage = (TextView) findViewById(R.id.activity_snapshot_setting_tvlanguage);
        stringList = activity.getResources().getStringArray(R.array.language_list);
        init();
    }

    private View findViewById(int id){
        return activity.view.findViewById(id);
    }
    
    Intent intent;
    public void init() {
        
        GetDataLanguageLoader loader2 = new GetDataLanguageLoader("getDataLanguage", activity);
        loader2.setPriority(RequestUI.PRIORITY_LOW);
        activity.getRequestQueue().addRequest(loader2);
        
        tvAgeLimit.setGravity(Gravity.CENTER);
        distanceSeekbar.setMax(7);
        cbMale.setOnClickListener(evenClick);
        cbFemale.setOnClickListener(evenClick);
        fltMaleCheckbox.setOnClickListener(evenClick);
        fltFemaleCheckbox.setOnClickListener(evenClick);
        fltIncludeFriendsCheckbox.setOnClickListener(evenClick);
        languageLayout.setOnClickListener(evenClick);
        linearContact.setOnClickListener(evenClick);
        linearLogout.setOnClickListener(evenClick);
        seekBar.setNotifyWhileDragging(true);
        seekBar.setOnRangeSeekBarChangeListener(evenRangeSeekBar);
        distanceSeekbar.setOnSeekBarChangeListener(evenSeekBar);

        SharedPreferences pref = activity.getSharedPreferences(Constants.PREFERENCE_NAME, 0);
        int currentId = pref.getInt(Constants.PREFERENCE_LANGUAGE, 0);
        tvLanguage.setText(stringList[currentId]);
        
        if(Constants.settingObject==null){
            pdLoading=new ProgressDialog(activity);
            pdLoading.setMessage(activity.getString(R.string.txt_loading));
            pdLoading.setCancelable(false);
            pdLoading.show();
        }
        else {
            settingObject =Constants.settingObject;
            if(settingObject.getFilter_male().equals("")){
                cbMale.setChecked(true);
                cbFemale.setChecked(false);
            }
            else if(settingObject.getFilter_female().equals("")) {
                cbMale.setChecked(false);
                cbFemale.setChecked(true);
            }
            else {
                cbMale.setChecked(true);
                cbFemale.setChecked(false);
            }
            cbIncludeFriends.setChecked(settingObject.isInclude_friends());
            seekBar.setNormalizedMinValue(((double)settingObject.getAge_from()-Constants.MIN_SEARCH_AGE)/seekBar.getAbsoluteMaxValue());
            seekBar.setNormalizedMaxValue(((double)settingObject.getAge_to()-Constants.MIN_SEARCH_AGE)/seekBar.getAbsoluteMaxValue());
            tvAgeLimit.setText(""+settingObject.getAge_from()+" "+activity.getString(R.string.txt_age_from)
                    + " " + settingObject.getAge_to()+" "+activity.getString(R.string.txt_age_to));
            int distance = (int)((settingObject.getRange() - 50)/50);
            distanceSeekbar.setProgress(distance);

            maxDistance = settingObject.getRange();
            if(maxDistance<50)
                tvDistance.setText(activity.getString(R.string.msg_people_around_sub_1)+ " " +"50km");
            else if(maxDistance >= 400)
                tvDistance.setText(activity.getString(R.string.msg_people_around_sub_1)+ " " + activity.getString(R.string.txt_distance_world));
            else if(maxDistance >= 350)
                tvDistance.setText(activity.getString(R.string.msg_people_around_sub_1)+ " " + activity.getString(R.string.txt_distance_country));
            else tvDistance.setText(activity.getString(R.string.msg_people_around_sub_1)+ " " +maxDistance+"km");
        }
        if(!Constants.isChangedSetting){
            LoadAccountSettingsLoader settingLoader = new LoadAccountSettingsLoader("getAccountSetting", activity);
            activity.getRequestQueue().addRequest(settingLoader);
        }
            
    }

    class GetDataLanguageLoader extends RequestUI {
        GetDataLanguageReturnObject obj;
        public GetDataLanguageLoader(Object key, Activity activity) {
            super(key, activity);
        }

        @Override
        public void execute() throws Exception {
            obj = activity.oakClubApi.GetDataLanguage();
        }

        @Override
        public void executeUI(Exception ex) {
            if (obj!=null && obj.getData()!=null){
                SlidingMenuActivity.mDataLanguageObj = obj.getData();
            }
        }

    }
    
    class LoadAccountSettingsLoader extends RequestUI {
        public LoadAccountSettingsLoader(Object key, Activity activity) {
            super(key, activity);
        }

        @Override
        public void execute() throws Exception {
            snapshotObj = activity.oakClubApi.getSnapshotSetting();
        }

        @Override
        public void executeUI(Exception ex) {
            if (pdLoading!=null && pdLoading.isShowing()){
                pdLoading.dismiss();
            }
            if (snapshotObj != null && snapshotObj.getData() != null) {
                if(Constants.settingObject == null){
                    if (snapshotObj.getData().getGender_of_search()
                            .equalsIgnoreCase("Male")) {
                        cbMale.setChecked(false);
                        cbFemale.setChecked(true);
                    } else if (snapshotObj.getData().getGender_of_search()
                            .equalsIgnoreCase("Female")) {
                        cbMale.setChecked(true);
                        cbFemale.setChecked(false);
                    } else {
                        cbMale.setChecked(true);
                        cbFemale.setChecked(false);
                    }
                    cbIncludeFriends.setChecked(snapshotObj.getData().isInclude_friend());
                    seekBar.setNormalizedMinValue(((double)snapshotObj.getData().getAge_from()-Constants.MIN_SEARCH_AGE)/seekBar.getAbsoluteMaxValue());
                    seekBar.setNormalizedMaxValue(((double)snapshotObj.getData().getAge_to()-Constants.MIN_SEARCH_AGE)/seekBar.getAbsoluteMaxValue());
                    tvAgeLimit.setText(""+snapshotObj.getData().getAge_from()+" "+activity.getString(R.string.txt_age_from)
                            + " " + snapshotObj.getData().getAge_to()+" "+activity.getString(R.string.txt_age_to));
                    
                    int distance = (int)((snapshotObj.getData().getRange() - 50)/50);
                    distanceSeekbar.setProgress(distance);
                    maxDistance = snapshotObj.getData().getRange();
                    if(maxDistance < 50)
                        tvDistance.setText(activity.getString(R.string.msg_people_around_sub_1)+ " " +"50km");
                    else if(maxDistance >= 400)
                        tvDistance.setText(activity.getString(R.string.msg_people_around_sub_1)+ " " + activity.getString(R.string.txt_distance_world));
                    else if(maxDistance >= 350)
                        tvDistance.setText(activity.getString(R.string.msg_people_around_sub_1)+ " " + activity.getString(R.string.txt_distance_country));
                    else tvDistance.setText(activity.getString(R.string.msg_people_around_sub_1)+ " " +maxDistance+"km");
                }   
                /* HieuPham */
                settingObject.setAge_from(snapshotObj.getData().getAge_from());
                settingObject.setAge_to(snapshotObj.getData().getAge_to());
                if(cbFemale.isChecked()){
                    settingObject.setFilter_female("");
                    settingObject.setFilter_male("on");
                }else{
                    settingObject.setFilter_male("");
                    settingObject.setFilter_female("on");
                }
                settingObject.setInclude_friends(snapshotObj.getData().isInclude_friend());
                settingObject.setRange(snapshotObj.getData().getRange());
                Constants.isChangedSetting = false;
                Constants.settingObject = settingObject;
            
            }
        }
    }
    
    public void showLanguagueDialog(final int currentId){
        
        final ArrayList<RadioButton> radioButtons;
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(activity);
        final AlertDialog dialog = builder.create();//new Dialog(ProfileSettingActivity.activity);
        final LayoutInflater inflater = LayoutInflater.from(activity);
        View mainRelativeLayout = inflater.inflate(
                R.layout.dialog_choose_language, null);
        dialog.setView(mainRelativeLayout,0,0,0,0);
        
        final RadioGroup radioGroup = (RadioGroup)mainRelativeLayout.findViewById(R.id.radioGroup1);
        radioGroup.setOrientation(RadioGroup.VERTICAL);
        radioButtons = new ArrayList<RadioButton>();
        for (int i = 0; i < stringList.length; i++) {
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            RadioButton radio;
//            if(stringList[i] == activity.getString(R.string.txt_language_ar)){
//                radio = new RadioButton(activity);
//                radio.setBackgroundResource(R.drawable.radiogroup_selector2);
//                params.gravity = Gravity.LEFT|Gravity.CENTER_VERTICAL;
//            }
//            else
                radio = new RadioButtonCustom(activity, activity.getResources().getDrawable(R.drawable.radiogroup_selector2));
            
            //RadioButtonCustom radio = new RadioButtonCustom(activity,activity.getResources().getDrawable(R.drawable.radiogroup_selector2));
            radio.setText(stringList[i]);
            radio.setTextColor(Color.BLACK);
            radio.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
            radio.setLayoutParams(params);
            int padding = (int) OakClubUtil.convertDpToPixel(12,activity);
            radio.setPadding(padding, padding, padding, padding);
            radioButtons.add(radio);
            radioGroup.addView(radio);
            ImageView separator = new ImageButton(activity);
            separator.setBackgroundResource(R.drawable.separators);
            separator.setLayoutParams(new RadioGroup.LayoutParams(LayoutParams.MATCH_PARENT, 1));
            radioGroup.addView(separator);
            if (i==currentId){
                radio.setChecked(true);
            }
            
        }
        
        Button okBtn;
        okBtn = (Button)mainRelativeLayout.findViewById(R.id.ok_btn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                int selectedButtonId = radioGroup
                        .getCheckedRadioButtonId();
                int selectedId = -1;
                for (int i = 0; i < radioButtons.size(); i++) {
                    if (radioButtons.get(i).getId() == selectedButtonId) {
                        selectedId = i;
                        break;
                    }
                }
                if (currentId!=selectedId){
                	if(selectedId<Constants.LANGUAGE_LOCALE.length)
						setLocale(Constants.LANGUAGE_LOCALE[selectedId]);
					else setLocale(Constants.LANGUAGE_LOCALE[0]);                
                                        
                    SharedPreferences pref = activity.getSharedPreferences(
                            Constants.PREFERENCE_NAME, 0);
                    boolean loggedIn = pref
                            .getBoolean(Constants.PREFERENCE_LOGGINED, false);
                    
                    Constants.country = activity.getResources().getConfiguration().locale.getLanguage();
                    Editor editor = activity.getSharedPreferences(Constants.PREFERENCE_NAME, 0).edit();
                    editor.putInt(Constants.PREFERENCE_LANGUAGE, selectedId);
                    editor.commit();
                    activity.finish();
                    activity.startActivity(activity.getIntent());
                    UpdateLanguage loader = new UpdateLanguage("updateLanguageApp", activity, Constants.country);
                    activity.getRequestQueue().addRequest(loader);
                }
                dialog.dismiss();
            }
        }); 
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
    private void setLocale(String lang) {
        Locale myLocale;
        myLocale = new Locale(lang);
        Resources res = activity.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Log.v("locale", "" + res.getConfiguration().locale.getDisplayName());
    }
    
    private OnRangeSeekBarChangeListener<Integer> evenRangeSeekBar = new OnRangeSeekBarChangeListener<Integer>() {
        @Override
        public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
            
            tvAgeLimit.setText(""+minValue +" "+activity.getString(R.string.txt_age_from) +" " + maxValue+" "+activity.getString(R.string.txt_age_to));
            
            settingObject.setAge_from(minValue);
            settingObject.setAge_to(maxValue);
            Constants.settingObject = settingObject;
            Constants.isChangedSetting = true;
        }
    };
    
    private OnSeekBarChangeListener evenSeekBar = new OnSeekBarChangeListener() {
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            
        }
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            maxDistance = progress*50 + 50;
            settingObject.setRange(progress*50 + 50);
            if(maxDistance<50){
                tvDistance.setText(activity.getString(R.string.msg_people_around_sub_1)+ " " +"50km");
                settingObject.setRange(50);
            }
            else if(maxDistance >= 400)
                tvDistance.setText(activity.getString(R.string.msg_people_around_sub_1)+ " " + activity.getString(R.string.txt_distance_world));
            else if(maxDistance >= 350)
                tvDistance.setText(activity.getString(R.string.msg_people_around_sub_1)+ " " + activity.getString(R.string.txt_distance_country));
            else tvDistance.setText(activity.getString(R.string.msg_people_around_sub_1)+ " " +maxDistance+"km");
            
            Constants.settingObject = settingObject;
            Constants.isChangedSetting = true;
        }
    };
    
    
    private OnClickListener evenClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.language_layout:
                SharedPreferences pref = activity.getSharedPreferences(Constants.PREFERENCE_NAME, 0);
                int currentId = pref.getInt(Constants.PREFERENCE_LANGUAGE, 0);
                showLanguagueDialog(currentId);
                break;
            case R.id.linear_contact_us:
                intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{activity.getString(R.string.mail_server_address)});
                intent.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.txt_subject_mail_contact));
                intent.putExtra(Intent.EXTRA_TEXT   , "");
                try {
                    activity.startActivity(Intent.createChooser(intent, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    //Toast.makeText(activity, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.linear_logout:
//              if (Session.getActiveSession()!=null){
//                  Session.getActiveSession().closeAndClearTokenInformation();
//                  intent = new Intent(activity,MainActivity.class);
//                  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//                  finish();
//                  startActivity(intent);
//              }
                
                Editor editor = activity.getSharedPreferences(Constants.PREFERENCE_NAME, 0)
                .edit();
                editor.putBoolean(Constants.PREFERENCE_LOGGINED, false);
                editor.putString(Constants.PREFERENCE_USER_ID, null);
                editor.putString(Constants.HEADER_ACCESS_TOKEN, null);
                editor.putString("username", null);
                editor.putString("password", null);
                GCMRegistrar.setRegisteredOnServer(activity, false);
                editor.commit();
                
                
//                Session.getActiveSession().closeAndClearTokenInformation();
//                Session.setActiveSession(null);
                intent = new Intent(activity,
                        MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED); 
                activity.startActivity(intent);
                NotificationManager nMgr = (NotificationManager) activity.getSystemService(activity.NOTIFICATION_SERVICE);
                nMgr.cancelAll();
                activity.finish();
                break;

            case R.id.female_checkbox:
            case R.id.flt_male_checkbox: 
                Log.v("Male checkbox: ", cbMale.isChecked() + "");
                if(cbMale.isChecked()){
                    cbMale.setChecked(false);
                    cbFemale.setChecked(true);
                    Log.v("Male: ", cbMale.isChecked() + "");
                    Log.v("Female: ", cbFemale.isChecked() + "");
                    settingObject.setFilter_male("on");
                    settingObject.setFilter_female("");
                }
                else {
                    cbMale.setChecked(true);
                    cbFemale.setChecked(false);
                    Log.v("Male: ", cbMale.isChecked() + "");
                    Log.v("Female: ", cbFemale.isChecked() + "");
                    settingObject.setFilter_male("");
                    settingObject.setFilter_female("on");
                }
                Constants.settingObject = settingObject;
                Constants.isChangedSetting = true;  
                break;
            
            case R.id.male_checkbox:
            case R.id.flt_female_checkbox:
                Log.v("Female checkbox: ", cbFemale.isChecked() + "");
                if(cbFemale.isChecked()){
                    cbMale.setChecked(true);
                    cbFemale.setChecked(false);
                    Log.v("Male: ", cbMale.isChecked() + "");
                    Log.v("Female: ", cbFemale.isChecked() + "");
                    settingObject.setFilter_male("");
                    settingObject.setFilter_female("on");
                }
                else {
                    cbMale.setChecked(false);
                    cbFemale.setChecked(true);
                    Log.v("Male: ", cbMale.isChecked() + "");
                    Log.v("Female: ", cbFemale.isChecked() + "");
                    settingObject.setFilter_male("on");
                    settingObject.setFilter_female("");
                } 
                Constants.settingObject = settingObject;
                Constants.isChangedSetting = true;  
                break;
            case R.id.flt_include_friends_checkbox: 
                if(cbIncludeFriends.isChecked()){
                    cbIncludeFriends.setChecked(false);
                    settingObject.setInclude_friends(false);
                }
                else {
                    cbIncludeFriends.setChecked(true);
                    settingObject.setInclude_friends(true);
                } 
                Constants.settingObject = settingObject;
                Constants.isChangedSetting = true;  
                break;
            default: break;
            }   
        }
    };
    
    class UpdateLanguage extends RequestUI {

    	String key_language;
		public UpdateLanguage(Object key, Activity activity, String key_language) {
			super(key, activity);
			this.key_language = key_language;
		}

		@Override
		public void execute() throws Exception {
			activity.oakClubApi.UpdateLanguage(key_language);
			
		}

		@Override
		public void executeUI(Exception ex) {
			
		}
    	
    }
}
