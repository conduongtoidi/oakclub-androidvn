package com.oakclub.android;

import com.oakclub.android.base.LoginBaseActivity;
import com.oakclub.android.util.OakClubUtil;
import com.oakclub.android.view.TextViewWithFont;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class InformationScreenActivity extends LoginBaseActivity {
	private TextViewWithFont txtTitle;
	private TextViewWithFont txtContent1;
	private TextViewWithFont txtContent2;
	private TextViewWithFont txtContent3;
	private TextViewWithFont txtContent4;
	private TextViewWithFont txtContent5;
	private ImageButton btnCancel;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_information_screen);

		txtTitle = (TextViewWithFont) findViewById(R.id.activity_infomation_txtTitle);
		txtContent1 = (TextViewWithFont) findViewById(R.id.activity_infomation_txtContent1);
		txtContent2 = (TextViewWithFont) findViewById(R.id.activity_infomation_txtContent2);
		txtContent3 = (TextViewWithFont) findViewById(R.id.activity_infomation_txtContent3);
		txtContent4 = (TextViewWithFont) findViewById(R.id.activity_infomation_txtContent4);
		txtContent5 = (TextViewWithFont) findViewById(R.id.activity_infomation_txtContent5);
		btnCancel = (ImageButton) findViewById(R.id.activity_infomation_screen_btnCancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
		int fontSize = (int) OakClubUtil.convertPixelsToDp(OakClubUtil.getWidthScreen(this)/15, this);
        txtTitle.setTextSize(fontSize);
        txtTitle.setFont("helveticaneuelight.ttf");
        fontSize = (int) OakClubUtil.convertPixelsToDp(OakClubUtil.getWidthScreen(this)/25, this);
        
        int paddingLeft = (int) OakClubUtil.convertDpToPixel(50, this);
        int paddingRight = (int) OakClubUtil.convertDpToPixel(15, this);
        txtContent1.setTextSize(fontSize);
        txtContent1.setPadding(paddingLeft, 0, paddingRight, 0);
        txtContent2.setTextSize(fontSize);
        txtContent2.setPadding(paddingLeft, 0, paddingRight, 0);
        txtContent3.setTextSize(fontSize);
        txtContent3.setPadding(paddingLeft, 0, paddingRight, 0);
        txtContent4.setTextSize(fontSize);
        txtContent4.setPadding(paddingLeft, 0, paddingRight, 0);
        txtContent5.setTextSize(fontSize);
        txtContent5.setPadding(paddingLeft, 0, paddingRight, 0);
        
		mLoginButton = (LinearLayout) findViewById(R.id.activity_information_screen_lltlogin);
		mLoginButton.setOnClickListener(listener);
		
	}
	
	private OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.activity_information_screen_lltlogin :
                    loginWithFB();
                    break;
                default :
                    break;
            }
            
        }
    };
	
	
}
