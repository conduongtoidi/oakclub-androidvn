package com.oakclub.android;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oakclub.android.base.OakClubBaseActivity;
import com.oakclub.android.base.SlidingMenuActivity;
import com.oakclub.android.core.RequestUI;
import com.oakclub.android.model.ProfileInfoData;
import com.oakclub.android.model.ProfileUpdateFirstTimeObject;
import com.oakclub.android.model.SettingReturnObject;
import com.oakclub.android.util.BitmapScaler;
import com.oakclub.android.util.Constants;
import com.oakclub.android.util.OakClubUtil;
import com.oakclub.android.util.ParseDataProfileInfo;
import com.oakclub.android.view.CircleImageView;
import com.oakclub.android.view.CircleImageWithRing;

public class ProfileUpdateActivity extends OakClubBaseActivity {
	ProfileInfoData profileInfoObj;
	CircleImageView avatar;
	TextView userNameTv;
	TextView userBriefInfoTv;
	TextView profileTvName;
	TextView profileTvBirthdate;
	TextView profileTvEmail;
	TextView profileTvGender;
	TextView profileTvInterestedIn;
	TextView profileTvLocation;
	double longitude;
	double latitude;
	
	RelativeLayout profileLayoutName;
	RelativeLayout profileLayoutBirthdate;
	RelativeLayout profileLayoutEmail;
	RelativeLayout profileLayoutGender;
	
	RelativeLayout profileLayoutInterestedIn;
	RelativeLayout profileLayoutLocation;
	
	Button btnDone;
	boolean hasChange = false;
	Uri mImageCaptureUri;
	String imagePath;
	Bitmap photo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_profile);
		init();
	}

	private void init(){
		
		profileInfoObj = (ProfileInfoData)this.getIntent().getSerializableExtra("Info");
		if(profileInfoObj==null) return;
		OnLayoutClickListener listener = new OnLayoutClickListener();
		avatar = (CircleImageView) findViewById(R.id.update_profile_top_rlt_header_civw_avatar);
		userNameTv = (TextView) findViewById(R.id.update_profile_top_rlt_header_llt_info_tvw_user_name);
		userBriefInfoTv = (TextView) findViewById(R.id.update_profile_top_rlt_header_llt_info_tvw_user_brief_info);
		profileTvName = (TextView) findViewById(R.id.update_profile_body_cvw_info_rlt_name_tvw_name);
		profileTvBirthdate = (TextView) findViewById(R.id.update_profile_body_cvw_info_rlt_birthdate_tvw_birthdate);
		profileTvEmail = (TextView) findViewById(R.id.update_profile_body_cvw_info_rlt_email_tvw_email);
		profileTvGender = (TextView) findViewById(R.id.update_profile_body_cvw_info_rlt_gender_tvw_gender);
		profileTvInterestedIn = (TextView) findViewById(R.id.update_profile_body_cvw_info_rlt_interested_in_tvw_interested_in);
		profileTvLocation = (TextView) findViewById(R.id.update_profile_body_cvw_info_rlt_location_tvw_location);
		
		profileLayoutName = (RelativeLayout) findViewById(R.id.update_profile_body_cvw_info_rlt_name);
		profileLayoutBirthdate = (RelativeLayout) findViewById(R.id.update_profile_body_cvw_info_rlt_birthdate);
		profileLayoutEmail = (RelativeLayout) findViewById(R.id.update_profile_body_cvw_info_rlt_email);
		profileLayoutGender = (RelativeLayout) findViewById(R.id.update_profile_body_cvw_info_rlt_gender);
		profileLayoutInterestedIn = (RelativeLayout) findViewById(R.id.update_profile_body_cvw_info_rlt_interested_in);
		profileLayoutLocation = (RelativeLayout) findViewById(R.id.update_profile_body_cvw_info_rlt_location);	
		
		btnDone = (Button) findViewById(R.id.update_profile_footer_btn_done);
		btnDone.setOnClickListener(listener);
		
		profileLayoutName.setOnClickListener(listener);
		profileLayoutBirthdate.setOnClickListener(listener);
		profileLayoutEmail.setOnClickListener(listener);
		profileLayoutGender.setOnClickListener(listener);
		profileLayoutInterestedIn.setOnClickListener(listener);
		profileLayoutLocation.setOnClickListener(listener);
		
		initInfo();
		
	}
	
	class OnLayoutClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if (profileInfoObj==null){
				return;
			}
			switch (v.getId()) {
			case R.id.update_profile_body_cvw_info_rlt_name:
				solveChangeField(Constants.PROFILE_SETTING_FIELD_NAME,profileInfoObj.getName());
				break;
			case R.id.update_profile_body_cvw_info_rlt_email:
				solveChangeField(Constants.PROFILE_SETTING_FIELD_EMAIL,profileInfoObj.getEmail());
				break;
			case R.id.update_profile_body_cvw_info_rlt_gender:
				solveChangeBySingleChoice(
						Constants.PROFILE_SETTING_FIELD_GENDER,
						R.array.gender_list, profileInfoObj.getGender());
				break;
			case R.id.update_profile_body_cvw_info_rlt_interested_in:
				solveChangeBySingleChoice(
						Constants.PROFILE_SETTING_FIELD_INTERESTED,
						R.array.interested_in_list, profileInfoObj.getInterested());
				break;
			case R.id.update_profile_body_cvw_info_rlt_birthdate:
				solveChangeBirthdate(Constants.PROFILE_SETTING_FIELD_BIRTHDATE,
						profileInfoObj.getBirthday_date());
				break;
			case R.id.update_profile_footer_btn_done:
			    runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Location location = null;//OakClubUtil.getLocation(getApplicationContext());
                        if (location != null) {
                        	Log.v("TEST", "btnDone:is nulll");
                            longitude = location.getLongitude();
                            latitude = location.getLatitude();
                            SetProfileByFieldLoader loader = new SetProfileByFieldLoader("updateProfileFirstTime",
                                    ProfileUpdateActivity.this,
                                    profileTvName.getText().toString(),
                                    profileTvGender.getText().toString(),
                                    profileTvBirthdate.getText().toString(),
                                    profileTvInterestedIn.getText().toString(),
                                    profileTvEmail.getText().toString(), longitude, latitude);
                            getRequestQueue().addRequest(loader);
                           
                        } else {
                            showOpenGPSSettingsDialog(ProfileUpdateActivity.this);
//                            Toast.makeText(getApplicationContext(),
//                                    getString(R.string.abnormal_error_message),
//                                    Toast.LENGTH_SHORT);
                        }
                    }
                });
				break;
			default:
			}
		}
	}
	
	private void initInfo(){
		if (profileInfoObj != null) {
			String url = OakClubUtil.getFullLink(getApplicationContext(), profileInfoObj.getAvatar(),100,100,1);
			OakClubUtil.loadImageFromUrl(getApplicationContext(), avatar,url);
			userNameTv.setText(profileInfoObj.getName());
			userBriefInfoTv.setText(profileInfoObj.getLocation().getName());
			profileTvName.setText("" + profileInfoObj.getName());
			profileTvBirthdate.setText(""+ profileInfoObj.getBirthday_date());
			profileTvGender.setText(profileInfoObj.getGender() == 1?""
					+ getString(R.string.profile_gender_male):""
					+ getString(R.string.profile_gender_female));
			
			profileTvLocation.setText(""+ profileInfoObj.getLocation().getName());
			profileTvInterestedIn.setText(ParseDataProfileInfo.getInterested(this, profileInfoObj.getInterested()));
			
			profileTvEmail.setText(""+ profileInfoObj.getEmail());
		}
	}

	public void solveChangeField(final String fieldName,
			final String currentValue) {
		final EditText input = new EditText(ProfileUpdateActivity.this);
		input.setText(currentValue);
		new AlertDialog.Builder(ProfileUpdateActivity.this)
				.setTitle("Update profile settings")
				.setMessage("New " + fieldName)
				.setView(input)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						String editable = input.getText().toString();
						if (editable != null && editable.length() != 0 && !editable.equals(currentValue)) {
							hasChange = true;
							if (profileInfoObj != null) {
								if (fieldName.equals(Constants.PROFILE_SETTING_FIELD_NAME)) {
									profileInfoObj.setName(editable);
									profileTvName.setText(editable);
								}else if(fieldName.equals(Constants.PROFILE_SETTING_FIELD_EMAIL)){
									profileInfoObj.setEmail(editable);
									profileTvEmail.setText(editable);
								}
							}
						}
					}
				})
				.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
						}
					}).show();
	}

	public void solveChangeBySingleChoice(final String fieldName,
			int stringArrayId, final int currentValue) {
		final String[] stringList = getResources().getStringArray(stringArrayId);
		final ArrayList<RadioButton> radioButtons;
		final RadioGroup radioGroup = new RadioGroup(getApplicationContext());
		radioGroup.setOrientation(RadioGroup.VERTICAL);
		radioButtons = new ArrayList<RadioButton>();
		for (int i = 0; i < stringList.length; i++) {
			RadioButton radio = new RadioButton(getApplicationContext());
			radio.setText(stringList[i]);
			radio.setTextColor(Color.BLACK);
			radioButtons.add(radio);
			radioGroup.addView(radio);
		}
		if (currentValue >= 0) {
			radioGroup.check(radioButtons.get(currentValue).getId());
		}
		new AlertDialog.Builder(ProfileUpdateActivity.this)
				.setTitle("Update profile settings")
				.setMessage("Set " + fieldName)
				.setView(radioGroup)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
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
//							SetProfileByFieldLoader loader = new SetProfileByFieldLoader(
//									"setField", ProfileUpdateActivity.this,
//									fieldName, "" + selectedId);
//							getRequestQueue().addRequest(loader);

							if (profileInfoObj != null) {
								if (fieldName.equals(Constants.PROFILE_SETTING_FIELD_GENDER)) {
									
									profileInfoObj.setGender(selectedId);
									profileTvGender.setText(""+ stringList[selectedId]);
								} else if (fieldName.equals(Constants.PROFILE_SETTING_FIELD_INTERESTED)) {
									
									profileInfoObj.setInterested(selectedId);
									profileTvInterestedIn.setText(stringList[selectedId]);
								}

							}
						}
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
							}
						}).show();
	}

	private void solveChangeBirthdate(final String fieldName,
			final String currentValue) {
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
			final DatePickerDialog dialog = new DatePickerDialog(this,
					pDateSetListener, year, month, day);
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
//						SetProfileByFieldLoader loader = new SetProfileByFieldLoader(
//								"setField", ProfileUpdateActivity.this,
//								fieldName, "" + newDay);
//						getRequestQueue().addRequest(loader);

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

	class SetProfileByFieldLoader extends RequestUI {
		String name;
		int gender;
		String birthday;
		String interested;
		String email;
		double longitude;
		double latitude;
		ProfileUpdateFirstTimeObject obj;

		public SetProfileByFieldLoader(Object key, Activity activity, String name, String gender, String birthday, String interested, String email, double longitude, double latitude) {
			super(key, activity);
			this.name=name;
			this.gender =  gender.equals("Male")? 1: 0;
			this.birthday=birthday;
			this.interested=interested;
			this.email = email;
			this.longitude = longitude;
			this.latitude = latitude;			        
		}

		@Override
		public void execute() throws Exception {
		    obj = oakClubApi.updateProfileFirstTime(name, gender, birthday, interested, email, longitude, latitude);
			//obj = oakClubApi.setProfileByField(field, value);
		}

		@Override
		public void executeUI(Exception ex) {
			if (obj != null && obj.isStatus()) {
			    intent = new Intent(ProfileUpdateActivity.this, SnapshotActivity.class );
			    startActivity(intent);
                finish();
			} else {
				Toast.makeText(getApplicationContext(),
						getString(R.string.abnormal_error_message),
						Toast.LENGTH_SHORT).show();
			}
		}

	}

}
