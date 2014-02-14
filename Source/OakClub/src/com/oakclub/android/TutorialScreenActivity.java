package com.oakclub.android;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
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
import android.widget.RelativeLayout;

import com.oakclub.android.util.Constants;
import com.oakclub.android.util.OakClubUtil;
import com.oakclub.android.view.TextViewWithFont;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.PageIndicator;

public class TutorialScreenActivity extends Activity {
	ViewPager mPager;
	PageIndicator mIndicator;
	TutorialScreenPagerAdapter adapter;
	ImageView cancelBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.PREFERENCE_NAME, 0);
		Editor editor = pref.edit();
		editor.putBoolean(Constants.PREFERENCE_SHOW_TUTORIAL, false);
		editor.commit();

		setContentView(R.layout.activity_tutorial_screen);
		mPager = (ViewPager) findViewById(R.id.tutorial_pager);
		mIndicator = (PageIndicator) findViewById(R.id.tutorial_indicator);
		adapter = new TutorialScreenPagerAdapter(getApplicationContext());
		mPager.setAdapter(adapter);
		mIndicator.setViewPager(mPager);
		cancelBtn = (ImageView) findViewById(R.id.cancel_btn);
		cancelBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	class TutorialScreenPagerAdapter extends PagerAdapter implements IconPagerAdapter {
		int[] resourceId = {R.drawable.tuts_options,R.drawable.tuts_chat,R.drawable.tuts_snapshots};
		int[] strId = {R.string.txt_tutorial_snapshots_swipe_photo_right, R.string.txt_tutorial_snapshots_swipe_photo_left,
				R.string.txt_tutorial_snapshots_or_just_use_button, R.string.txt_tutorial_chat_if_you_match,
				R.string.txt_tutorial_opt_you_can_edit, R.string.txt_tutorial_opt_you_can_choose};
		
		Context context;
		LayoutInflater inflater;

		public TutorialScreenPagerAdapter(Context context) {
			this.context = context;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Resources rs = getApplicationContext().getResources();
			int rsNaviHeight = getApplicationContext().getResources().getIdentifier("navigation_bar_height", "dimen", "android");
			int rsStatusHeight = getApplicationContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
			int NaviHeight = 0;
			int StatusHeight = 0;
			if (rsNaviHeight > 0)
				NaviHeight = rs.getDimensionPixelSize(rsNaviHeight);
			
			if (rsStatusHeight > 0)
				StatusHeight = rs.getDimensionPixelSize(rsStatusHeight);
			
			int heightScreen = (int) OakClubUtil.getHeightScreen(context) - NaviHeight - StatusHeight;
			int widthScreen = (int) OakClubUtil.getWidthScreen(context); //getWindowManager().getDefaultDisplay().getWidth();
			
			RelativeLayout rltPages = new RelativeLayout(context);
			ImageView img = new ImageView(context);
			img.setBackgroundResource(resourceId[position]);
			rltPages.addView(img, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			
			switch (position) {
			case 0:
				TextViewWithFont txt1 = new TextViewWithFont(context);
				TextViewWithFont txt2 = new TextViewWithFont(context);
				
				txt1.setText(context.getString(strId[4]));
				txt2.setText(context.getString(strId[5]));
				
				int paddingLeft = 1 * widthScreen / 3 + 60;
				int paddingRight = 0;
				int paddingTop = heightScreen / 10;
				int paddingBottom = 0;
				
				txt1.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
				float fontSize = (int) OakClubUtil.convertPixelsToDp(OakClubUtil.getWidthScreen(getApplicationContext())/20, getApplicationContext());
				txt1.setTextSize(fontSize);
				
				paddingTop = 5 * heightScreen / 8;
				txt2.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
				txt2.setTextSize(fontSize);

				rltPages.addView(txt1, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				rltPages.addView(txt2, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				break;
			case 1:
				txt1 = new TextViewWithFont(context);
				
				txt1.setText(context.getString(strId[3]));
				
				paddingLeft = 0;
				paddingRight = 0;
				paddingTop = 3 * heightScreen / 5;
				paddingBottom = 0;
				
				txt1.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
				fontSize = (int) OakClubUtil.convertPixelsToDp(OakClubUtil.getWidthScreen(getApplicationContext())/20, getApplicationContext());
				txt1.setTextSize(fontSize);
				txt1.setGravity(Gravity.CENTER);

				rltPages.addView(txt1, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				break;
			case 2:
				TextViewWithFont txt3 = new TextViewWithFont(context);
				
				txt1 = new TextViewWithFont(context);
				txt2 = new TextViewWithFont(context);
				txt3 = new TextViewWithFont(context);
				
				txt1.setText(context.getString(strId[0]));
				txt2.setText(context.getString(strId[1]));
				txt3.setText(context.getString(strId[2]));
				
				paddingLeft = 10;
				paddingRight = 1 * widthScreen / 3;
				paddingTop = heightScreen / 9;
				paddingBottom = 0;
				txt1.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
				fontSize = (int) OakClubUtil.convertPixelsToDp(OakClubUtil.getWidthScreen(getApplicationContext())/20, getApplicationContext());
				txt1.setTextSize(fontSize);
				txt1.setGravity(Gravity.CENTER);
				
				paddingLeft = 1 * widthScreen / 3;
				paddingRight = 0;
				paddingTop = 2 * heightScreen / 7;
				paddingBottom = 0;
				txt2.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
				txt2.setTextSize(fontSize);
				txt2.setGravity(Gravity.CENTER);
				
				paddingLeft = 0;
				paddingRight = 0;
				paddingTop = 7 * heightScreen / 11;
				paddingBottom = 0;
				txt3.setGravity(Gravity.CENTER);
				txt3.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
				txt3.setTextSize(fontSize);
				
				rltPages.addView(txt1, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				rltPages.addView(txt2, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				rltPages.addView(txt3, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			default:
				break;
			}			
			
			container.addView(rltPages, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
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
	}
}
