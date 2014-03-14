
package com.oakclub.android;

import java.util.ArrayList;
import java.util.HashMap;


import android.R.animator;
import android.R.bool;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oakclub.android.helper.operations.ListChatOperation;
import com.oakclub.android.image.SmartImageView;
import com.oakclub.android.base.OakClubBaseActivity;
import com.oakclub.android.base.SlidingMenuActivity;
import com.oakclub.android.core.RequestUI;
import com.oakclub.android.model.ListPhotoReturnDataItemObject;
import com.oakclub.android.model.SnapshotData;
import com.oakclub.android.util.Constants;
import com.oakclub.android.util.OakClubUtil;
import com.oakclub.android.util.ParseDataProfileInfo;
import com.oakclub.android.view.CircleImageView;
import com.oakclub.android.view.RadioButtonCustom;
import com.oakclub.android.view.ViewPagerCustom;

public class InfoProfileOtherActivity extends OakClubBaseActivity {
	private TextView tvAbout;
	private TextView tvShareInterest;
	private TextView tvMutualMatch;
    private ImageButton imgPlayvideo;
    private ImageView imgAboutme;
	private ImageView imgShareInterests;
	private ImageView imgMutualFriends;
	private ImageView imgLocation;
	private TextView tvLocation;
	private ImageView imgHome;
	private TextView tvHome;
	private ImageView imgBirthday;
	private TextView tvBirthday;
	private ImageView imgSchool;
	private TextView tvSchool;
	private ImageView imgWork;
	private TextView tvWork;
	private ImageView imgInterested_In;
	private TextView tvInterested_In;
	

    private  TextView tvwNumCapture;
    private  TextView tvwNumVideo;
	private TextView tvwName;
	private TextView tvwAge;
	private TextView tvwDistance;
	private TextView tvwActive;
	private TextView tvwView;
	private TextView tvwLike;
	private TextView tvwAbout;
	private Button btDone;
	private ImageButton ibnLike;
	private ImageButton ibnReject;
	
	private FrameLayout rltMain;
	
	private String profileId = "-1";
	private String content = "";
	private boolean isOtherReport = false;
	private SnapshotData snapShotData;
	
	@Override
    protected void onPause() {
        System.gc();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        System.gc();
        super.onDestroy();
    }
    


    private ArrayList<ListPhotoReturnDataItemObject> imageCache;
    private String urlVideo = "";
	
	private boolean isZoom = false;
	private ViewPager pager;

    private ViewPagerCustom vpShareInterests;
    private ViewPagerCustom vpMutualFriends;
    
    private RelativeLayout header;
    private RelativeLayout footer;
    
    private ImageButton btnReport;
    private RelativeLayout rltNavigation;
    
    private int profile_info_other_image_height;
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_info_other);
		view = getWindow().getDecorView().findViewById(android.R.id.content);

		profile_info_other_image_height = (int) OakClubUtil.getHeightScreen(getApplicationContext()) * 2 / 3;
		init();
		SlideToDown(header, 600);
		SlideToUp(footer, 600);
		assignInfo();
		
	}

	private void SlideToDown(RelativeLayout rl, int duration) {
	    Animation slide = null;
	    rl.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
	    slide = new TranslateAnimation(rl.getX(), rl.getX(), rl.getY() - rl.getMeasuredHeight(), rl.getY());

	    slide.setDuration(duration);
	    slide.setFillAfter(true);
	    slide.setFillEnabled(true);
	    rl.startAnimation(slide);

	    slide.setAnimationListener(new AnimationListener() {

	        @Override
	        public void onAnimationStart(Animation animation) {

	        }

	        @Override
	        public void onAnimationRepeat(Animation animation) {
	        }

	        @Override
	        public void onAnimationEnd(Animation animation) {

	        }

	    });

	}

	private void SlideToUp(RelativeLayout rl, int duration) {
	    Animation slide = null;
	    rl.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
	    slide = new TranslateAnimation(rl.getX(), rl.getX(), rl.getY() + rl.getMeasuredHeight(), rl.getY());

	    slide.setDuration(duration);
	    slide.setFillAfter(true);
	    slide.setFillEnabled(true);
	    rl.startAnimation(slide);

	    slide.setAnimationListener(new AnimationListener() {

	        @Override
	        public void onAnimationStart(Animation animation) {

	        }

	        @Override
	        public void onAnimationRepeat(Animation animation) {
	        }

	        @Override
	        public void onAnimationEnd(Animation animation) {

	        }

	    });

	}

	private void init(){
	    Bundle b = this.getIntent().getExtras();
		snapShotData = (SnapshotData)b.getSerializable("SnapshotDataBundle");
		if(snapShotData==null){
			Log.e("ERROR", "ERROR:InfoProfileOther:SnapshotData:NULL");
			return;
		}
		this.profileId = snapShotData.getProfile_id();
		imageCache = (ArrayList<ListPhotoReturnDataItemObject>)snapShotData.getPhotos();
		urlVideo = snapShotData.getVideo_link();
		pager = (ViewPager) findViewById(R.id.activity_profile_info_other_header_fltSnapshot_vpr_list_images);
		pager.getLayoutParams().height = profile_info_other_image_height;
		pager.getLayoutParams().width = LayoutParams.MATCH_PARENT;
		pager.requestLayout();
		
		pager.setAdapter(new ImagePagerAdapter());
		pager.setCurrentItem(0);
		pager.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				pager.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
                if(arg0+1<=imageCache.size())
                    tvwNumCapture.setText((arg0+1)+"/"+imageCache.size());
                else tvwNumCapture.setText(arg0+"/"+imageCache.size());
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		
		header = (RelativeLayout) findViewById(R.id.activity_profile_info_other_header);	
		footer = (RelativeLayout) findViewById(R.id.activity_profile_info_other_rltContent);
		tvAbout = (TextView) findViewById(R.id.activity_profile_info_other_body_tvAboutMe);
		tvShareInterest = (TextView) findViewById(R.id.activity_profile_info_other_body_tvShareInterests);
		tvMutualMatch = (TextView) findViewById(R.id.activity_profile_info_other_body_tvMutualFriends);
		imgAboutme = (ImageView) findViewById(R.id.activity_profile_info_other_body_imgAboutme);
		imgShareInterests = (ImageView) findViewById(R.id.activity_profile_info_other_body_imgShareInterests);
		imgMutualFriends = (ImageView) findViewById(R.id.activity_profile_info_other_body_imgMutualFriends);
		imgLocation = (ImageView) findViewById(R.id.activity_profile_info_other_body_imgLocation);
		tvLocation = (TextView) findViewById(R.id.activity_profile_info_other_body_location);
		imgHome = (ImageView) findViewById(R.id.activity_profile_info_other_body_imgHome);
		tvHome = (TextView) findViewById(R.id.activity_profile_info_other_body_home);
		imgBirthday = (ImageView) findViewById(R.id.activity_profile_info_other_body_imgBirthday);
		tvBirthday = (TextView) findViewById(R.id.activity_profile_info_other_body_birthday);
		imgSchool = (ImageView) findViewById(R.id.activity_profile_info_other_body_imgSchool);
		tvSchool = (TextView) findViewById(R.id.activity_profile_info_other_body_school);
		imgWork = (ImageView) findViewById(R.id.activity_profile_info_other_body_imgWork);
		tvWork = (TextView) findViewById(R.id.activity_profile_info_other_body_work);
		imgInterested_In = (ImageView) findViewById(R.id.activity_profile_info_other_body_imgInterested);
		tvInterested_In = (TextView) findViewById(R.id.activity_profile_info_other_body_interested);

        btDone = (Button)findViewById(R.id.activity_profile_info_other_header_fltSnapshot_btDone);
        btDone.setOnClickListener(eventClickButton);
        imgPlayvideo = (ImageButton)findViewById(R.id.activity_profile_info_other_header_fltSnapshot_imgPlayvideo);
        imgPlayvideo.setOnClickListener(eventClickButton);
		
		ibnLike = (ImageButton)findViewById(R.id.activity_profile_info_other_header_fltSnapshot_ibLike);
		ibnLike.setOnClickListener(eventClickButton);
		ibnReject = (ImageButton)findViewById(R.id.activity_profile_info_other_header_fltSnapshot_ibNope);
		ibnReject.setOnClickListener(eventClickButton);		
		if(snapShotData.isFromChat()){
            ibnLike.setVisibility(View.GONE);
            ibnReject.setVisibility(View.GONE);
		}
        tvwNumCapture = (TextView)findViewById(R.id.activity_profile_info_other_header_fltSnapshot_tvwNumCapture);
        tvwNumCapture.setText((pager.getCurrentItem()+1)+"/"+imageCache.size());
        tvwNumVideo = (TextView)findViewById(R.id.activity_profile_info_other_header_fltSnapshot_tvwNumVideo);
        if(!urlVideo.equals("")){    
            imgPlayvideo.setVisibility(View.VISIBLE);
            tvwNumVideo.setText("1");
        }
        else {    
            imgPlayvideo.setVisibility(View.GONE);
            tvwNumVideo.setText("0");
        }
	
		tvwName = (TextView)findViewById(R.id.activity_profile_info_other_body_fltInfo_tvName);		
		tvwAge = (TextView)findViewById(R.id.activity_profile_info_other_body_fltInfo_tvAge);		
		tvwDistance = (TextView)findViewById(R.id.activity_profile_info_other_body_rltInfoDistance_tvDistance);		
		tvwActive = (TextView)findViewById(R.id.activity_profile_info_other_body_rltInfoDistance_tvActive);	
		tvwView = (TextView)findViewById(R.id.activity_profile_info_other_body_rltInfoDistance_tvViewed);	
		tvwLike = (TextView)findViewById(R.id.activity_profile_info_other_body_rltInfoDistance_tvLiked);		
		tvwAbout = (TextView)findViewById(R.id.activity_profile_info_other_body_tvInfoAboutMe);
		rltMain = (FrameLayout)findViewById(R.id.activity_profile_info_other_header_fltSnapshot);
		rltMain.getLayoutParams().height = profile_info_other_image_height;
		rltMain.requestLayout();
		
        vpShareInterests =(ViewPagerCustom)findViewById(R.id.activity_profile_info_other_body_vpShareInterests);
        vpMutualFriends =(ViewPagerCustom)findViewById(R.id.activity_profile_info_other_body_vpMutualFriends);
        btnReport = (ImageButton) findViewById(R.id.activity_profile_info_other_body_fltInfo_imgReport);
        btnReport.setOnClickListener(eventClickButton);
        rltNavigation = (RelativeLayout) findViewById(R.id.activity_profile_info_other_bottom_navigation);
		
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		intent = InfoProfileOtherActivity.this.getIntent();
		intent.putExtra("ACTION", -1);
		InfoProfileOtherActivity.this.setResult(RESULT_OK,intent);
		finishAct();
	}
	
	private void finishAct(){
	    //OakClubUtil.releaseImagePager(pager);
	    finish();
	}
	
	private OnClickListener zoomEvent = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Log.v("Zoom: ", isZoom + "");
			if(!isZoom){
				
				ResizeAnimation animation = null;
				Resources rs = getApplicationContext().getResources();
				int rsNaviHeight = getApplicationContext().getResources().getIdentifier("navigation_bar_height", "dimen", "android");
				int rsStatusHeight = getApplicationContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
				int NaviHeight = 0;
				int StatusHeight = 0;
				if (rsNaviHeight > 0)
					NaviHeight = rs.getDimensionPixelSize(rsNaviHeight);
				
				if (rsStatusHeight > 0)
					StatusHeight = rs.getDimensionPixelSize(rsStatusHeight);
				
				isZoom = true;
				
				rltMain.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
				Log.v("rltHeight", rltMain.getHeight() + "");
				Log.v("Height Srceen", OakClubUtil.getHeightScreen(getApplicationContext()) + "");
				Log.v("NavHeight", NaviHeight + "");
				Log.v("StatusHeight", StatusHeight + "");
				animation = new ResizeAnimation(rltMain, rltMain.getHeight(), (int) OakClubUtil.getHeightScreen(getApplicationContext()) - StatusHeight);
				//1 second is enougth for animation duration run smoothly
				animation.setDuration(500);
				animation.setAnimationListener(new Animation.AnimationListener() {
				    
				    @Override
				    public void onAnimationStart(Animation animation) {
				    	rltNavigation.setVisibility(View.GONE);
				    }
				    
				    @Override
				    public void onAnimationRepeat(Animation animation) {

				    }
				    
				    @Override
				    public void onAnimationEnd(Animation animation) {
				    	Log.v("rltHeightEndABC", rltMain.getHeight() + "");
						RelativeLayout.LayoutParams layout = new android.widget.RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
						rltMain.setLayoutParams(layout);
						
						FrameLayout.LayoutParams layoutPager = new android.widget.FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
						pager.setLayoutParams(layoutPager);
				    	Log.v("rltHeightEnd", rltMain.getHeight() + "");
						rltNavigation.setVisibility(View.VISIBLE);
				    }
				});
				rltMain.startAnimation(animation);
				
				Animation fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_fadeout_content_info);
				
				fadeOut.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
						
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
						
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						footer.setVisibility(View.GONE);
					}
				});
				footer.startAnimation(fadeOut);
			}
			else {
//				isZoom = false;
//				ResizeAnimation animation = null;
//				Resources rs = getApplicationContext().getResources();
//				int rsNaviHeight = getApplicationContext().getResources().getIdentifier("navigation_bar_height", "dimen", "android");
//				int rsStatusHeight = getApplicationContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
//				int NaviHeight = 0;
//				int StatusHeight = 0;
//				
//				if (rsNaviHeight > 0)
//					NaviHeight = rs.getDimensionPixelSize(rsNaviHeight);
//				
//				if (rsStatusHeight > 0)
//					StatusHeight = rs.getDimensionPixelSize(rsStatusHeight);
//				animation = new ResizeAnimation(rltMain, getWindowManager().getDefaultDisplay().getHeight() - NaviHeight - StatusHeight, InfoProfileOtherActivity.this.getResources().getDimensionPixelSize(R.dimen.profile_info_other_image));
//				//1 second is enougth for animation duration run smoothly
//				animation.setDuration(500);
//				animation.setAnimationListener(new Animation.AnimationListener() {
//				    
//				    @Override
//				    public void onAnimationStart(Animation animation) {
//				    	FrameLayout.LayoutParams layoutPager = new android.widget.FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,InfoProfileOtherActivity.this.getResources().getDimensionPixelSize(R.dimen.profile_info_other_image));
//						layoutPager.gravity = Gravity.CENTER;
//						pager.setLayoutParams(layoutPager);
//						rltNavigation.setVisibility(View.GONE);
//				    }
//				    
//				    @Override
//				    public void onAnimationRepeat(Animation animation) {
//
//				    }
//				    
//				    @Override
//				    public void onAnimationEnd(Animation animation) {
//				    	RelativeLayout.LayoutParams layout = new android.widget.RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//						rltMain.setLayoutParams(layout);
//						rltContent.setVisibility(View.VISIBLE);
//						rltNavigation.setVisibility(View.VISIBLE);
//				    }
//				});
//				rltMain.startAnimation(animation);
			}
													
		}
	};
	
	private String getWorkString(int id){
		String str = "-";
		if (SlidingMenuActivity.mDataLanguageObj!=null)
		for (int i = 0; i<SlidingMenuActivity.mDataLanguageObj.getWork_cate().size();i++){
			if (SlidingMenuActivity.mDataLanguageObj.getWork_cate().get(i).getCate_id()==id){
				return SlidingMenuActivity.mDataLanguageObj.getWork_cate().get(i).getCate_name();
			}
		}
		return str;
	}
	
	private OnClickListener eventClickButton = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			String action = "-1";
			switch (v.getId()) {
	            case R.id.activity_profile_info_other_header_fltSnapshot_btDone:
	                if(isZoom){
	                    isZoom = false;
	                    ResizeAnimation animation = null;
	                    Resources rs = getApplicationContext().getResources();
	                    int rsNaviHeight = getApplicationContext().getResources().getIdentifier("navigation_bar_height", "dimen", "android");
	                    int rsStatusHeight = getApplicationContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
	                    int NaviHeight = 0;
	                    int StatusHeight = 0;
	                    
	                    if (rsNaviHeight > 0)
	                        NaviHeight = rs.getDimensionPixelSize(rsNaviHeight);
	                    
	                    if (rsStatusHeight > 0)
	                        StatusHeight = rs.getDimensionPixelSize(rsStatusHeight);
	                    animation = new ResizeAnimation(rltMain, (int) OakClubUtil.getHeightScreen(getApplicationContext()) - NaviHeight - StatusHeight, profile_info_other_image_height);
	                    animation.setDuration(500);
	                    animation.setAnimationListener(new Animation.AnimationListener() {
	                        
	                        @Override
	                        public void onAnimationStart(Animation animation) {
	                            FrameLayout.LayoutParams layoutPager = new android.widget.FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, profile_info_other_image_height);
	                            layoutPager.gravity = Gravity.CENTER;
	                            pager.setLayoutParams(layoutPager);
	                            rltNavigation.setVisibility(View.GONE);
	                        }
	                        
	                        @Override
	                        public void onAnimationRepeat(Animation animation) {
	    
	                        }
	                        
	                        @Override
	                        public void onAnimationEnd(Animation animation) {
	                            RelativeLayout.LayoutParams layout = new android.widget.RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
	                            rltMain.setLayoutParams(layout);
	                            footer.setVisibility(View.VISIBLE);
	                            rltNavigation.setVisibility(View.VISIBLE);
	                        }
	                    });
	                    rltMain.startAnimation(animation);
	                    
	                    return;
	                }
	                finishAct();
                break;
            case R.id.activity_profile_info_other_header_fltSnapshot_imgPlayvideo:
                String videoUrl = OakClubUtil.getFullLinkVideo(InfoProfileOtherActivity.this,
                        urlVideo, Constants.VIDEO_EXTENSION);
                intent = new Intent(InfoProfileOtherActivity.this, VideoViewActivity.class);
                intent.putExtra("url_video", videoUrl);
                startActivity(intent);
                break;
			case R.id.activity_profile_info_other_header_fltSnapshot_ibLike:
				action = Constants.ACTION_LIKE;
				Constants.ACTION = action;
				intent = InfoProfileOtherActivity.this.getIntent();
                intent.putExtra("ACTION", action);
                InfoProfileOtherActivity.this.setResult(RESULT_OK,intent);
                finishAct();
				break;
			case R.id.activity_profile_info_other_header_fltSnapshot_ibNope:
				action = Constants.ACTION_NOPE;
				Constants.ACTION = action;
				intent = InfoProfileOtherActivity.this.getIntent();
                intent.putExtra("ACTION", action);
                InfoProfileOtherActivity.this.setResult(RESULT_OK,intent);
                finishAct();
				break;
			case R.id.activity_profile_info_other_body_fltInfo_imgReport:
				solveEditBtn();
				break;
			default:
				break;
			}
		}
	};
	
	
	private void solveEditBtn(){
		final String[] stringList = getResources()
				.getStringArray(R.array.report_user_reasons);
		final ArrayList<RadioButton> radioButtons;
		AlertDialog.Builder builder;
		builder = new AlertDialog.Builder(InfoProfileOtherActivity.this);
		final AlertDialog dialog = builder.create();//new Dialog(ProfileSettingActivity.this);
		final LayoutInflater inflater = LayoutInflater.from(this);
		View mainRelativeLayout = inflater.inflate(
				R.layout.dialog_report_spam_input, null);
		dialog.setView(mainRelativeLayout,0,0,0,0);
		
		final RadioGroup radioGroup = (RadioGroup)mainRelativeLayout.findViewById(R.id.radioGroup1);
		radioGroup.setOrientation(RadioGroup.VERTICAL);
		radioButtons = new ArrayList<RadioButton>();
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		
		final EditText editText = (EditText) mainRelativeLayout.findViewById(R.id.editText);
//		lp.setMargins(0, 5, 0, 5);
		for (int i = 0; i < stringList.length; i++) {
			RadioButtonCustom radio = new RadioButtonCustom(this, getResources().getDrawable(R.drawable.radiogroup_selector2));
			radio.setText(stringList[i]);
			radio.setTextColor(Color.BLACK);
			radio.setLayoutParams(new RadioGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			int padding = (int) OakClubUtil.convertDpToPixel(12,this);
			radio.setPadding(padding, padding, padding, padding);
//			radio.setBackgroundResource(R.drawable.white_purple_btn_);
			
			if (radio.getText().equals(getString(R.string.txt_report_user_other))) {
			    
				radio.setOnClickListener(new OnClickListener() {		
					@Override
					public void onClick(View v) {
						editText.setVisibility(View.VISIBLE);
						editText.requestFocus();
						InputMethodManager imm = (InputMethodManager) getSystemService(InfoProfileOtherActivity.this.INPUT_METHOD_SERVICE);

						imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
						isOtherReport = true;
						Log.v("other", "1");
					}
				});
			}
			else {
				radio.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						editText.setVisibility(View.GONE);
						InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
						isOtherReport = false;
						Log.v("other", "0");
					}
				});
			}
			
			radioButtons.add(radio);
			radioGroup.addView(radio);
			ImageView separator = new ImageButton(this);
			separator.setBackgroundResource(R.drawable.separators);
			separator.setLayoutParams(new RadioGroup.LayoutParams(LayoutParams.MATCH_PARENT, 1));
			radioGroup.addView(separator);

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
						if (isOtherReport){
							content = editText.getText().toString();
						}
						else {
							content = radioButtons.get(i).getText().toString();
						}
						break;
					}
				}
				if (selectedId != -1 ) {
				    showBlockConfirmDialog(getResources().getString(R.string.txt_report_message), 
                        getResources().getString(R.string.txt_report_anyway), true);
				}
				dialog.dismiss();
			}
		});
		Button btnBlock = (Button)mainRelativeLayout.findViewById(R.id.dialog_report_spam_input_block_user);
        btnBlock.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showBlockConfirmDialog(getResources().getString(R.string.txt_block_message), 
                        getResources().getString(R.string.txt_block_anyway), false);
            }
        });

        Button btnCancel = (Button) mainRelativeLayout
                .findViewById(R.id.dialog_report_spam_input_cancel);
        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        
        if(!snapShotData.isFromChat()){
            btnBlock.setVisibility(View.GONE);
        }
		dialog.setCancelable(true);
		dialog.show();
	}
	
	private void showBlockConfirmDialog(String tvQuestion, String btName, final boolean isReport){
		AlertDialog.Builder builder;
		builder = new AlertDialog.Builder(InfoProfileOtherActivity.this);
		final AlertDialog dialog = builder.create();//new Dialog(ProfileSettingActivity.this);
		final LayoutInflater inflater = LayoutInflater.from(this);
		View mainRelativeLayout = inflater.inflate(
				R.layout.dialog_report_spam_confirm, null);
		dialog.setView(mainRelativeLayout,0,0,0,0);
		TextView tvQues = (TextView)mainRelativeLayout.findViewById(R.id.dialog_report_spam_confirm_tvquestion);
        tvQues.setText(tvQuestion);
		Button okBtn;
		Log.v("Content:", content);
		okBtn = (Button)mainRelativeLayout.findViewById(R.id.ok_btn);
        okBtn.setText(btName);
		okBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
			    if(isReport){
                    ReportUserRequest loader = new ReportUserRequest("reportUser", InfoProfileOtherActivity.this, profileId, content);
                    getRequestQueue().addRequest(loader);
                }
                else{

                	ListChatOperation listChatDb = new ListChatOperation(InfoProfileOtherActivity.this);
                	listChatDb.deleteListChat(profileId);
                	
                    SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.PREFERENCE_NAME, 0);
                    Editor editor = pref.edit();
                    editor.putBoolean(Constants.IS_LOAD_CHAT_AGAIN, true);
                    editor.commit();
                    
                    BlockUserRequest loader = new BlockUserRequest("blockUser", InfoProfileOtherActivity.this, profileId);
                    getRequestQueue().addRequest(loader);
                }
				dialog.dismiss();
			}
		});	
		
		Button btnCancel;
		btnCancel = (Button)mainRelativeLayout.findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		dialog.setCancelable(true);
		dialog.show();
	}
	
	protected void assignInfo() {
		if (snapShotData != null) {
			Log.v("profile_id", snapShotData.getProfile_id());
			HashMap<String, String> t = ParseDataProfileInfo.getDataOtherProfile(getApplicationContext(),snapShotData);
            int age = snapShotData.getAge();
			tvwName.setText(OakClubUtil.getFirstName(snapShotData.getName()) +", "+ age);
			if(age<=1) Log.e("Error", "Error:InfoProfileOtherActivity:age is wrong");
			tvwAge.setText(age+"");
			tvwDistance.setText(t.get("distance") +"");
			tvwActive.setText(t.get("active") + "");
			tvwView.setText(snapShotData.getViewed()+"");
			tvwLike.setText(snapShotData.getLike()+"");
			
			if (snapShotData.getAbout_me() != null && !snapShotData.getAbout_me().isEmpty()){
				Log.v("visible About Me", "1");
				tvwAbout.setText(snapShotData.getAbout_me()+"");
			}
			else {
				tvAbout.setVisibility(View.GONE);
				tvwAbout.setVisibility(View.GONE);
				imgAboutme.setVisibility(View.GONE);
			}
			
			if (snapShotData.getShare_interests() != null && !snapShotData.getShare_interests().isEmpty()){
				
				vpShareInterests.setListForView(snapShotData.getShare_interests());
				vpShareInterests.init();
				vpShareInterests.assignInfo();
			}
			else {
				tvShareInterest.setVisibility(View.GONE);
				vpShareInterests.setVisibility(View.GONE);
				imgShareInterests.setVisibility(View.GONE);
			}
			
            if (snapShotData.getMutual_friends() != null && !snapShotData.getMutual_friends().isEmpty()){
				vpMutualFriends.setListForView(snapShotData.getMutual_friends());
				vpMutualFriends.init();
				vpMutualFriends.assignInfo();
            }
            else {
            	tvMutualMatch.setVisibility(View.GONE);
            	vpMutualFriends.setVisibility(View.GONE);
            	imgMutualFriends.setVisibility(View.GONE);
            }
            
            if (snapShotData.getLocation_name() != null && snapShotData.getLocation_name() != null) {
            	tvLocation.setText(snapShotData.getLocation_name());
            }
            else {
            	tvLocation.setVisibility(View.GONE);
            	imgLocation.setVisibility(View.GONE);
            }
            if (snapShotData.getHometown_name() != null && snapShotData.getHometown_name() != null) {
            	tvHome.setText(snapShotData.getHometown_name());
            }
            else {
            	tvHome.setVisibility(View.GONE);
            	imgHome.setVisibility(View.GONE);
            }
            if (snapShotData.getBirthday_date() != null && !snapShotData.getBirthday_date().isEmpty()) {
            	tvBirthday.setText(snapShotData.getBirthday_date());
            }
            else {
            	tvBirthday.setVisibility(View.GONE);
            	imgBirthday.setVisibility(View.GONE);
            }
            if (snapShotData.getSchool() != null && !snapShotData.getSchool().isEmpty()) {
            	tvSchool.setText(snapShotData.getSchool());
            }
            else {
            	tvSchool.setVisibility(View.GONE);
            	imgSchool.setVisibility(View.GONE);
            }
            if (snapShotData.getWork()>0) {
            	tvWork.setText(getWorkString(snapShotData.getWork()));
            }
            else {
            	tvWork.setVisibility(View.GONE);
            	imgWork.setVisibility(View.GONE);
            }
            if (snapShotData.getInterested()>0) {
            	tvInterested_In.setText(ParseDataProfileInfo.getInterested(getApplicationContext(), snapShotData.getInterested()));
            }
            else {
            	tvInterested_In.setVisibility(View.GONE);
            	imgInterested_In.setVisibility(View.GONE);
            }
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		//outState.putInt(STATE_POSITION, pager.getCurrentItem());
	}

	private class ImagePagerAdapter extends PagerAdapter {

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {		    
		    View view = (View)object;
            ((ViewPager) container).removeView(view);
            view = null;
		}

		@Override
		public void finishUpdate(View container) {
		    return;
		}

		@Override
		public int getCount() {
		    int size =imageCache.size();
		    if(!urlVideo.equals(""))
		        size++;
			return size;
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
		    FrameLayout fltImage = new FrameLayout(InfoProfileOtherActivity.this);

            android.widget.FrameLayout.LayoutParams params;
		    if(position< imageCache.size()){
		        SmartImageView imageView = new SmartImageView(InfoProfileOtherActivity.this);
                imageView.setBackgroundColor(Color.BLACK);
                imageView.setBackgroundResource(R.drawable.logo_splashscreen);
                imageView.setOnClickListener(zoomEvent);
                params = new  android.widget.FrameLayout.LayoutParams(
                                android.widget.FrameLayout.LayoutParams.MATCH_PARENT, 
                                profile_info_other_image_height);
                params.gravity = Gravity.CENTER;
                imageView.setLayoutParams(params);
            
    			String imageUrl = OakClubUtil.getFullLink(InfoProfileOtherActivity.this, imageCache.get(position).getTweet_image_link(), Constants.widthImage,Constants.heightImage,1);
    			OakClubUtil.loadImageFromUrl(InfoProfileOtherActivity.this, imageUrl, imageView);
    			fltImage.addView(imageView);
		    }
		    else {
                if(!urlVideo.equals("")){
                    CircleImageView imageView = new CircleImageView(InfoProfileOtherActivity.this);
                    imageView.setBackgroundColor(Color.BLACK);
                    params = new  android.widget.FrameLayout.LayoutParams(
                            android.widget.FrameLayout.LayoutParams.MATCH_PARENT, 
                            profile_info_other_image_height);
                    params.gravity = Gravity.CENTER;
                    imageView.setLayoutParams(params);
                    String imageUrl = OakClubUtil.getFullLinkVideo(InfoProfileOtherActivity.this, urlVideo, Constants.PHOTO_EXTENSION);
                    OakClubUtil.loadImageFromUrl(InfoProfileOtherActivity.this, imageUrl, imageView);
                    fltImage.addView(imageView);
                    
                    ImageView imageViewVideo = new ImageView(InfoProfileOtherActivity.this);
                    params = new  android.widget.FrameLayout.LayoutParams(
                                    android.widget.FrameLayout.LayoutParams.WRAP_CONTENT,
                                    android.widget.FrameLayout.LayoutParams.WRAP_CONTENT);
                    params.gravity = Gravity.CENTER;
                    imageViewVideo.setLayoutParams(params);
                    imageViewVideo.setBackgroundResource(R.drawable.large_video_btn);
                    //imageViewVideo.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, profile_info_other_image_height));
                    imageView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                String videoUrl = OakClubUtil.getFullLinkVideo(InfoProfileOtherActivity.this,
                                        urlVideo, Constants.VIDEO_EXTENSION);
                                intent = new Intent(InfoProfileOtherActivity.this, VideoViewActivity.class);
                                intent.putExtra("url_video", videoUrl);
                                startActivity(intent);
                            }
                    });
                    fltImage.addView(imageViewVideo);
                }
		    }
		    params = new  android.widget.FrameLayout.LayoutParams(
		                    android.widget.FrameLayout.LayoutParams.WRAP_CONTENT,
		                    android.widget.FrameLayout.LayoutParams.WRAP_CONTENT);
		    params.gravity = Gravity.CENTER;
		    fltImage.setLayoutParams(params);
		    fltImage.setBackgroundColor(Color.BLACK);
			((ViewPager) view).addView(fltImage, 0);
			return fltImage;
			
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View container) {
		}
	}

	private class ReportUserRequest extends RequestUI{
		String profile_id;
		String content;

		public ReportUserRequest(Object key, Activity activity,String profile_id, String content) {
			super(key, activity);
			this.profile_id = profile_id;
			this.content = content;
		}

		@Override
		public void execute() throws Exception {
			oakClubApi.ReportUserById(profile_id, content);
		}

		@Override
		public void executeUI(Exception ex) {
			
		}
		
	}
	
	private class BlockUserRequest extends RequestUI{
        String profile_id;

        public BlockUserRequest(Object key, Activity activity,String profile_id) {
            super(key, activity);
            this.profile_id = profile_id;
        }

        @Override
        public void execute() throws Exception {
            oakClubApi.BlockUserById(profile_id);
        }

        @Override
        public void executeUI(Exception ex) {
//            SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.PREFERENCE_NAME, 0);
//            Editor editor = pref.edit();
//            editor.putBoolean(Constants.IS_LOAD_CHAT_AGAIN, true);
//            editor.commit();
        }
        
    }   
    
	
	public class ResizeAnimation extends Animation 
	{
	    View view;
	    int startH;
	    int endH;
	    int diffH;

	    public ResizeAnimation(View v, int oldh, int newh)
	    {
	    	view = v;
	        startH = oldh;
	        endH = newh;
	        diffH = endH - startH;
	    }
	        
	    @Override
	    protected void applyTransformation(float interpolatedTime, Transformation t) 
	    {
	    	view.setAlpha(1);
	        view.getLayoutParams().height = (int) (startH + (diffH * interpolatedTime));
	        view.requestLayout();
	    }

	    @Override
	    public void initialize(int width, int height, int parentWidth, int parentHeight) 
	    {
	        super.initialize(width, height, parentWidth, parentHeight);
	    }

	    @Override
	    public boolean willChangeBounds() 
	    {
	        return true;
	    }
	}
}