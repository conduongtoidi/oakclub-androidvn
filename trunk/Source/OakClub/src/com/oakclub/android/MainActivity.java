package com.oakclub.android;

import java.util.concurrent.atomic.AtomicInteger;


import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;

import com.oakclub.android.base.LoginBaseActivity;
import com.oakclub.android.core.RequestUI;
import com.oakclub.android.util.Constants;
import com.oakclub.android.util.OakClubUtil;
import com.oakclub.android.view.TextViewWithFont;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.PageIndicator;

public class MainActivity extends LoginBaseActivity {
	ViewPager mPager;
	PageIndicator mIndicator;
	WelcomeScreenPagerAdapter adapter;
	LinearLayout bottomLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!OakClubUtil.isInternetAccess(MainActivity.this))
		{
			Log.v("inter", "1");
			return;
		}

		setContentView(R.layout.activity_main);
		
        //view = getWindow().getDecorView().findViewById(android.R.id.content);
		Bundle bundleListChatData = getIntent().getExtras();
		if (bundleListChatData != null) {
			isLoadListMutualMatch = bundleListChatData.getBoolean(Constants.isLoadListMutualMatch);
			profileIdMultualMatch = bundleListChatData.getString(Constants.BUNDLE_PROFILE_ID);
			
		}
        
		mPager = (ViewPager) findViewById(R.id.welcome_pager);
		mIndicator = (PageIndicator) findViewById(R.id.welcome_indicator);
		adapter = new WelcomeScreenPagerAdapter(getApplicationContext());
		mPager.setAdapter(adapter);
		mIndicator.setViewPager(mPager);
		
		mLoginButton = (LinearLayout) findViewById(R.id.activity_main_lltlogin);
		mLoginButton.setOnClickListener(listener);
		bottomLayout = (LinearLayout) findViewById(R.id.welcome_bottom_layout);
		bottomLayout.setOnClickListener(listener);
		
		Constants.country = getResources().getConfiguration().locale.getLanguage();
		UpdateLanguage loader = new UpdateLanguage("updateLanguageApp", MainActivity.this, Constants.country);
        getRequestQueue().addRequest(loader);
		
	}

	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.activity_main_lltlogin:
				loginWithFB();
				break;
			case R.id.welcome_bottom_layout:
				Intent intent = new Intent(MainActivity.this,
						InformationScreenActivity.class);
				startActivity(intent);
				break;
			default:
				break;
			}

		}
	};

	class WelcomeScreenPagerAdapter extends PagerAdapter implements
			IconPagerAdapter {

		int[] resourceId = { R.drawable.s1_bg, R.drawable.s2_bg,
				R.drawable.s3_bg };
		int[] strId = { R.string.txt_s1_bg, R.string.txt_s2_bg,
				R.string.txt_s3_bg };
		int index = 0;

		Context context;
		LayoutInflater inflater;

		public WelcomeScreenPagerAdapter(Context context) {
			this.context = context;
			inflater = LayoutInflater.from(context);
		}

		@SuppressWarnings("deprecation")
        @Override
		public Object instantiateItem(ViewGroup container, int position) {

			RelativeLayout rltPages = new RelativeLayout(context);
			ImageView img = new ImageView(context);
			img.setBackgroundResource(resourceId[position]);
			rltPages.addView(img, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);

			TextViewWithFont txt = new TextViewWithFont(context);
			txt.setText(this.context.getString(strId[position]));
			float fontSize = (int) OakClubUtil.convertPixelsToDp(
					OakClubUtil.getWidthScreen(getApplicationContext()) / 20,
					getApplicationContext());
			txt.setTextSize(fontSize);
			int paddingTop = (int) OakClubUtil.convertPixelsToDp(20, context);
			txt.setPadding(0, paddingTop, 0, 0);
			txt.setGravity(Gravity.CENTER);
			rltPages.addView(txt, LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);

			container.addView(rltPages, LayoutParams.MATCH_PARENT,
					LayoutParams.FILL_PARENT);
			return rltPages;
		}

		@Override
		public int getCount() {
			return resourceId.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public int getIconResId(int index) {
			return R.drawable.indicator_welcome;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public void setPrimaryItem(View container, int position, Object object) {
			// txtView.setText(this.context.getString(strId[position]));
			// float fontSize =
			// getWindowManager().getDefaultDisplay().getHeight() * 2 / 100;
			// txtView.setTextSize(fontSize);
			// super.setPrimaryItem(container, position, object);
		}
	}

	static class ContentNotification {
		static AtomicInteger atomicInteger = new AtomicInteger(1);

		@SuppressWarnings("deprecation")
		public static void setNotification(String profileId, Context context,
				String str) {
			Intent intent = new Intent(context, MainActivity.class);
			intent.setAction(Intent.ACTION_MAIN);

			PendingIntent content = PendingIntent.getActivity(context, 0,
					intent, PendingIntent.FLAG_UPDATE_CURRENT);

			NotificationManager notiMgr = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			Notification noti = new Notification(R.drawable.logo_oakclub,
					context.getString(R.string.app_name),
					System.currentTimeMillis());
			// layout
			RemoteViews view = new RemoteViews(context.getPackageName(),
					R.layout.layout_notification);
			// view.setTextViewText(R.id.titleNotify, title);
			view.setTextViewText(R.id.titleNotify,
					context.getString(R.string.txt_notification_content));
			noti.contentView = view;
			noti.contentIntent = content;
			noti.flags |= Notification.FLAG_AUTO_CANCEL;

			// Play default notification sound
			noti.defaults |= Notification.DEFAULT_SOUND;

			// Vibrate if vibrate is enabled
			noti.defaults |= Notification.DEFAULT_VIBRATE;

			notiMgr.notify(1, noti);
		}
	}

    @Override
    protected void onPause(){
        System.gc();
        super.onPause();
    }

    @Override
    protected void onDestroy(){
        //OakClubUtil.releaseImagePager(mPager);
        System.gc();
        super.onDestroy();
    }
    
    class UpdateLanguage extends RequestUI {

    	String key_language;
		public UpdateLanguage(Object key, Activity activity, String key_language) {
			super(key, activity);
			this.key_language = key_language;
		}

		@Override
		public void execute() throws Exception {
			oakClubApi.UpdateLanguage(key_language);
			
		}

		@Override
		public void executeUI(Exception ex) {
			
		}
    	
    }
	
}
