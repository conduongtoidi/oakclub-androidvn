package com.oakclub.android;

import com.oakclub.android.base.OakClubBaseActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class VerifiedFailedActivity extends OakClubBaseActivity {
	private Button btnOk;
	private TextView tvbottom;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verified_failed);
		btnOk = (Button) findViewById(R.id.activity_failed_verify_btn_ok);
		tvbottom = (TextView) findViewById(R.id.activity_failed_verify_bottom);
		tvbottom.setVisibility(View.INVISIBLE);
		btnOk.setOnClickListener(listener);
	}
	protected OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.activity_failed_verify_btn_ok:
				finish();
				break;

			default:
				break;
			}
			
		}
	};
}
