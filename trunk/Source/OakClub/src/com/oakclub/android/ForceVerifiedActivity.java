package com.oakclub.android;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.TextView;

import com.oakclub.android.fragment.ProfileSettingFragment;
import com.oakclub.android.util.OakClubUtil;
import com.oakclub.android.util.RichTextHelper;

public class ForceVerifiedActivity extends VerifiedActivity{
	private TextView tvVerifiedWay2;
	private TextView tvDearName;
	private TextView tvrule; 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!OakClubUtil.isInternetAccess(ForceVerifiedActivity.this)) {
			return;
		}
		if(ProfileSettingFragment.profileInfoObj == null) return;
		setContentView(R.layout.activity_force_verified);
		tvVerifiedWay2 = (TextView) findViewById(R.id.txt_verified_way_2);
		tvDearName = (TextView) findViewById(R.id.txt_dear_name);
		tvrule = (TextView) findViewById(R.id.txt_force_verified);
		
		SpannableStringBuilder styledText = RichTextHelper.getRichText(getString(R.string.txt_force_verified));
		tvDearName.setText(ProfileSettingFragment.profileInfoObj.getName());
		tvrule.setText(styledText);			
		styledText = RichTextHelper.getRichText(getString(R.string.txt_verified_way_2));		
		tvVerifiedWay2.setText(styledText);
		
	}
	@Override
	public void VerifiedClick(View v) {
		super.VerifiedClick(v);
	}
}
