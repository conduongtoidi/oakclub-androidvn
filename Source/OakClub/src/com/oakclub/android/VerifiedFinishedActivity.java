package com.oakclub.android;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.oakclub.android.base.OakClubBaseActivity;
import com.oakclub.android.util.Constants;
import com.oakclub.android.view.TextViewWithFont;

public class VerifiedFinishedActivity extends OakClubBaseActivity{
	private Button btnOk;
	private boolean startLogin;
	private int type;
	private TextViewWithFont tvbottom;
	private TextViewWithFont tvTitle;
	private TextViewWithFont tvVerifiedMember;
	private LinearLayout layoutVerifiedContent1;
	private LinearLayout layoutVerifiedContent2;
	private ImageView imvIcon;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.popup_verified_finished);
		btnOk = (Button) findViewById(R.id.activity_secceed_verify_btn_ok);
		tvTitle = (TextViewWithFont) findViewById(R.id.tv_finish_post_title);
		tvVerifiedMember = (TextViewWithFont) findViewById(R.id.tv_finish_post_verified_member);
		layoutVerifiedContent1 = (LinearLayout) findViewById(R.id.tv_verified_content_1);
		layoutVerifiedContent2 = (LinearLayout) findViewById(R.id.tv_verified_content_2);
		imvIcon = (ImageView) findViewById(R.id.img_verified_icon_popup);
		btnOk.setOnClickListener(listener);
		startLogin = getIntent().getBooleanExtra(Constants.START_LOGIN, false);
		type = getIntent().getIntExtra(Constants.VERIFIED_FAILED, 0);
		if(type == 1){
			tvTitle.setText(getString(R.string.txt_op));
			tvTitle.setTextColor(Color.BLACK);
			tvVerifiedMember.setText(getString(R.string.txt_not_complete_verified) +"\n"+ getString(R.string.txt_try_again));
			tvVerifiedMember.setTextColor(Color.RED);
			tvVerifiedMember.setTextSize(14);
			tvbottom = (TextViewWithFont) findViewById(R.id.activity_failed_verify_bottom);
			imvIcon.setImageResource(R.drawable.unclock_icon_popup);
			if(startLogin)
				tvbottom.setVisibility(View.INVISIBLE);
			layoutVerifiedContent1.setVisibility(View.GONE);
			layoutVerifiedContent2.setVisibility(View.GONE);
			btnOk.setBackgroundResource(R.drawable.red_boder_button);
			btnOk.setTextColor(Color.RED);
		}
	}
	protected OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.activity_secceed_verify_btn_ok:				
				Intent intent = getIntent();
				if(!startLogin && intent != null){
					intent.putExtra(Constants.COMEBACK_SNAPSHOT, true);
					setResult(RESULT_OK, intent);
				}
				finish();
				break;

			default:
				break;
			}
			
		}
	};
}
