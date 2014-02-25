package com.oakclub.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.oakclub.android.SlidingActivity.MenuOakclub;
import com.oakclub.android.base.OakClubBaseActivity;
import com.oakclub.android.fragment.SnapshotFragment;
import com.oakclub.android.util.Constants;

public class VerifiedSecceedActivity extends OakClubBaseActivity{
	private Button btnOk;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verified_secceed);
		btnOk = (Button) findViewById(R.id.activity_secceed_verify_btn_ok);
		btnOk.setOnClickListener(listener);
	}
	private void startSnapshot() {	
		if(this.getIntent() != null && this.getIntent().getBooleanExtra(Constants.START_LOGIN, true) ){
			Intent intent = new Intent(VerifiedSecceedActivity.this,
					SlidingActivity.class);
			startActivity(intent);
	    }		
	}
	protected OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.activity_secceed_verify_btn_ok:				
         		   startSnapshot();
				finish();
				break;

			default:
				break;
			}
			
		}
	};
}
