package com.oakclub.android;

import java.util.List;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oakclub.android.base.OakClubBaseActivity;
import com.oakclub.android.util.Constants;
import com.oakclub.android.util.RichTextHelper;

public class PurchaseConfirmedActivity extends OakClubBaseActivity {
	private Button btnBack;
	private TextView price;
	private TextView transactionID;
	private LinearLayout btnShareOnFacebook;
	private LinearLayout btnEmailToFriend;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.popup_purchase_seccess);
		btnBack = (Button) findViewById(R.id.btn_back);
		price = (TextView) findViewById(R.id.purchase_confirmed_price);
		transactionID = (TextView) findViewById(R.id.purchase_confirmed_transactionID);
		Intent intent = getIntent();
		String priceString = "";
		String transactionId = "";
		if (intent != null) {
			priceString = intent.getStringExtra(Constants.PURCHASE_PRICE);
			transactionId = intent.getStringExtra(Constants.TRANSACTION_ID);
		}
		price.setText(priceString);
		transactionID.setText(RichTextHelper.getRichText(String.format(
				getString(R.string.txt_purchase_confirmed_transaction_id), transactionId)));
		btnShareOnFacebook = (LinearLayout) findViewById(R.id.purchase_confirmed_btn_share_on_facebook);
		btnEmailToFriend = (LinearLayout) findViewById(R.id.purchase_confirmed_btn_email_to_friend);
		btnBack.setOnClickListener(buttonClick);
		btnShareOnFacebook.setOnClickListener(buttonClick);
		btnEmailToFriend.setOnClickListener(buttonClick);
	}

	private OnClickListener buttonClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_back:
				setResult(RESULT_OK);
				finish();
				break;
			case R.id.purchase_confirmed_btn_share_on_facebook:
				btnShareOnFacebook.setEnabled(false);
				Intent shareIntent = new Intent(
						android.content.Intent.ACTION_SEND);
				shareIntent.setType("text/plain");
//				shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
//						getString(R.string.app_name));
//				shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
//						(String) v.getTag(R.drawable.ic_launcher));
				PackageManager pm = getPackageManager();
				List<ResolveInfo> activityList = pm.queryIntentActivities(
						shareIntent, 0);
				for (final ResolveInfo app : activityList) {
					if ((app.activityInfo.name).contains("facebook")) {
						final ActivityInfo activity = app.activityInfo;
						final ComponentName name = new ComponentName(
								activity.applicationInfo.packageName,
								activity.name);
//						shareIntent.putExtra(Intent.EXTRA_TEXT,
//								getString(R.string.txt_share_title) + "\n"
//										+ getString(R.string.txt_share_url));
						shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
						shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
						shareIntent.setComponent(name);
						startActivityForResult(shareIntent, 0);
						break;
					}
				}
				break;
			case R.id.purchase_confirmed_btn_email_to_friend:
				btnEmailToFriend.setEnabled(false);
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("message/rfc822");
				intent.putExtra(Intent.EXTRA_EMAIL, "");
				intent.putExtra(Intent.EXTRA_SUBJECT, "");
				intent.putExtra(Intent.EXTRA_TEXT, "");
				try {
					startActivityForResult(Intent.createChooser(intent, "Send mail..."),0);
				} catch (android.content.ActivityNotFoundException ex) {
				}
				break;
			default:
				break;
			}
		}
	};
	@Override
	public void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		btnShareOnFacebook.setEnabled(true);
		btnEmailToFriend.setEnabled(true);
	};
}
