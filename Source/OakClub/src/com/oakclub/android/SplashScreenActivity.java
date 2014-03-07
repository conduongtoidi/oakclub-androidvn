package com.oakclub.android;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.oakclub.android.util.Constants;
import com.oakclub.android.util.OakClubUtil;
import com.oakclub.android.view.ImageLoader;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View.MeasureSpec;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SplashScreenActivity extends Activity {
	private ImageView img;
	private int fromX;
	private int fromY;
	private int toX;
	private int toY;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		img = (ImageView) findViewById(R.id.activity_splash_screen_img);
		
		ImageLoader imgLoader = new ImageLoader(this);
        imgLoader.clearCache();

		try {
			saveLogcatToFile(getApplicationContext());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//OakClubApplication.getInstance().clearApplicationData();
		SlideToDown(img, 500);
	}
	
	public void SlideToDown(final ImageView img, int duration) {
	    Animation slide = null;
	    img.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
	    fromX = (int) img.getX();
	    fromY = (int) img.getY();
	    toX = (int) img.getX();
	    toY = (int) ((int) img.getY() + OakClubUtil.convertPixelsToDp(100, getApplicationContext()));
	    Log.v("FX - FY - TX - TY", fromX + " " + fromY + " " + toX + " " + toY);
	    slide = new TranslateAnimation(fromX, toX, fromY, toY);

	    slide.setDuration(duration);
	    slide.setFillAfter(true);
	    slide.setFillEnabled(true);
	    img.startAnimation(slide);

	    slide.setAnimationListener(new AnimationListener() {

	        @Override
	        public void onAnimationStart(Animation animation) {

	        }

	        @Override
	        public void onAnimationRepeat(Animation animation) {
	        }

	        @Override
	        public void onAnimationEnd(Animation animation) {
	        	fromY = toY;
	        	SlideToUp(img, 500);
	        }

	    });

	}

	public void SlideToUp(ImageView img, int duration) {
	    Animation slide = null;
	    img.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
	    Log.v("FX - FY - TX - TY", fromX + " " + fromY + " " + toX + " " + OakClubUtil.getWidthScreen(getApplicationContext()));
	    slide = new TranslateAnimation(fromX, toX, fromY, - OakClubUtil.getHeightScreen(getApplicationContext()));

	    slide.setDuration(duration);
	    slide.setFillAfter(true);
	    slide.setFillEnabled(true);
	    img.startAnimation(slide);

	    slide.setAnimationListener(new AnimationListener() {
	    	
	        @Override
	        public void onAnimationStart(Animation animation) {

	        }

	        @Override
	        public void onAnimationRepeat(Animation animation) {
	        }

	        @Override
	        public void onAnimationEnd(Animation animation) {
	        	SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.PREFERENCE_NAME, 0);
				boolean isFirstJoin = pref.getBoolean(Constants.PREFERENCE_FIRST_JOINED, true);
				if (isFirstJoin){
					Editor editor = getSharedPreferences(
							Constants.PREFERENCE_NAME, 0)
							.edit();
					editor.putBoolean(
							Constants.PREFERENCE_FIRST_JOINED, false);
					editor.commit();
					Intent intent = new Intent(SplashScreenActivity.this,ChooseLanguageActivity.class);
					startActivity(intent);

				} else {
					int lang = pref.getInt(Constants.PREFERENCE_LANGUAGE, 0);
					Log.v("LangId: ", lang + "");
					if(lang<Constants.LANGUAGE_LOCALE.length-1)
						setLocale(Constants.LANGUAGE_LOCALE[lang]);
					else setLocale(Constants.LANGUAGE_LOCALE[0]);
					Constants.country = getResources().getConfiguration().locale.getLanguage();
					
					Intent intent = new Intent(SplashScreenActivity.this,MainActivity.class);
					startActivity(intent);
				}
				finish();
	        }

	    });

	}
	
	private void setLocale(String lang) {
        Locale myLocale;
        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }
	
	public static void saveLogcatToFile(Context context) throws IOException {
		Calendar cal = Calendar.getInstance();
		
		final SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
		String yearNow = sdf.format(cal.getTime());
		Log.v("Year:", yearNow);
		
		
	    String fileName = "logcat_"+yearNow+".txt";
	    String filePath = OakClubUtil.getFileStore(context).getAbsolutePath();
	    File outputFile = new File(filePath,fileName);
	    @SuppressWarnings("unused")
	    Process process = Runtime.getRuntime().exec("logcat -f "+outputFile.getAbsolutePath());
	}
}
