package com.oakclub.android;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.oakclub.android.base.OakClubBaseActivity;
import com.oakclub.android.core.RequestUI;
import com.oakclub.android.fragment.ProfileSettingFragment;
import com.oakclub.android.model.VerifiedReturnObject;
import com.oakclub.android.util.Constants;
import com.oakclub.android.util.OakClubUtil;
import com.oakclub.android.util.RichTextHelper;

public class VerifiedActivity extends OakClubBaseActivity {
	private Button btn_skip;
	private Button btn_back;
	private TextView line;
	private Session session;
	private  boolean first;
	private boolean start_login;
	private TextView tvVerifiedWay2;
	private boolean force_verified;
	private WebDialog feedDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!OakClubUtil.isInternetAccess(VerifiedActivity.this)) {
			return;
		}
		if(ProfileSettingFragment.profileInfoObj == null) return;
		setContentView(R.layout.activity_verified);
		btn_skip = (Button) findViewById(R.id.btn_skip_verified);
		btn_back = (Button) findViewById(R.id.btn_back);
		line = (TextView) findViewById(R.id.underline_back);
		tvVerifiedWay2 = (TextView) findViewById(R.id.txt_verified_way_2);
		SpannableStringBuilder styledText = RichTextHelper.getRichText(getString(R.string.txt_verified_way_2));
		tvVerifiedWay2.setText(styledText);
		Intent intent = getIntent();
		start_login = intent.getBooleanExtra(Constants.START_LOGIN, false);
		force_verified = intent.getBooleanExtra(Constants.FORCE_VERIFIED, false);
		if(start_login){
			btn_back.setVisibility(View.INVISIBLE);
			line.setVisibility(View.GONE);
			btn_skip.setVisibility(View.VISIBLE);
		}
	}

	public void VerifiedClick(View v) {
		if (OakClubUtil.isInternetAccess(VerifiedActivity.this)) {
 
			switch (v.getId()) {
			case R.id.btn_continue_verified:
				publishStory();
				break;
			case R.id.btn_skip_verified:
//				SharedPreferences pref = getApplicationContext()
//				.getSharedPreferences(Constants.PREFERENCE_NAME, 0);
//				Editor editor = pref.edit();
//				editor.putBoolean(Constants.KEY_IS_SKIP, true);
//				editor.commit();
				SendSkipVerifiedRequest loader = new SendSkipVerifiedRequest(Constants.SKIP_VERIFIED, VerifiedActivity.this);
				getRequestQueue().addRequest(loader);
				
				break;
			case R.id.btn_back:
				finish();
				break;
			default:
				break;
			}
		} else {
			AlertDialog.Builder builder;
			builder = new AlertDialog.Builder(this);
			final AlertDialog dialog = builder.create();
			LayoutInflater inflater = LayoutInflater.from(this);
			View layout = inflater.inflate(R.layout.dialog_warning_ok, null);
			dialog.setView(layout, 0, 0, 0, 0);
			Button btnOK = (Button) layout
					.findViewById(R.id.dialog_internet_access_lltfooter_btOK);
			btnOK.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});
			dialog.setCancelable(false);
			dialog.show();
		}
	}

	private boolean isSubsetOf(String string, Collection<String> superset) {
		if (!superset.contains(string)) {
			return false;
		}
		return true;
	}

	private void publishStory() {

		first = true;
		session = Session.getActiveSession();
		if (session == null) {
			SharedPreferences pref = getSharedPreferences(
					Constants.PREFERENCE_NAME, 0);
			Long expirationTime = pref.getLong(Constants.HEADER_ACCESS_EXPIRES,
					0);

			Date accessTokenExpires = new Date(expirationTime);
			
			List<String> permission = Arrays.<String>asList(Constants.FACEBOOK_PERMISSION);
			
			AccessToken accessToken = AccessToken
					.createFromExistingAccessToken(MainActivity.access_token,
							accessTokenExpires, null, null,permission);
			
			Session.openActiveSessionWithAccessToken(getApplicationContext(),
					accessToken, new StatusCallback() {

						@Override
						public void call(Session session, SessionState state,
								Exception exception) {
							
							if(session.isOpened()){
									VerifiedActivity.this.session = session;
									if ( session != null && VerifiedActivity.this.first) {																
										List<String> permissions = session
												.getPermissions();
										if (!isSubsetOf(Constants.VERIFIED_PERMISSIONS,
												permissions)) {
											Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
													VerifiedActivity.this,
													Constants.VERIFIED_PERMISSIONS);
											session.addCallback(new StatusCallback() {
												
												@Override
												public void call(Session session, SessionState state, Exception exception) {										
													if(session.isOpened())
													{
														VerifiedActivity.this.publishFeedDialog();
														return;
													}	
												}
											});
											session.requestNewPublishPermissions(newPermissionsRequest);
										}else
										{
											VerifiedActivity.this.publishFeedDialog();
										}
								}								
							} 
						}
					});
		} else {

			List<String> permissions = session.getPermissions();
			
				if (!isSubsetOf(Constants.VERIFIED_PERMISSIONS,
						permissions)) {
					Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
							VerifiedActivity.this,
							Constants.VERIFIED_PERMISSIONS);
					session.requestNewPublishPermissions(newPermissionsRequest);
					session.addCallback(new StatusCallback() {
						
						@Override
						public void call(Session session, SessionState state, Exception exception) {
							if(session.isOpened())
							{
								VerifiedActivity.this.publishFeedDialog();
								return;
							}							
						}
					});
				}else
					{
						VerifiedActivity.this.publishFeedDialog();
					}
		}
	}
	private void startSnapshot() {
		Intent intent = new Intent(VerifiedActivity.this,
				SlidingActivity.class);
		startActivity(intent);
	}
	private void publishFeedDialog() {
		if(!first) return;
		VerifiedActivity.this.first = false;
		Bundle params = new Bundle();
		params.putString("name", getString(R.string.app_name));
		params.putString("description", getString(R.string.txt_sample_post));
		params.putString("link", getString(R.string.txt_share_url));		

		feedDialog = (new WebDialog.FeedDialogBuilder(this,
				Session.getActiveSession(), params)).setOnCompleteListener(
				new OnCompleteListener() {

					@Override
					public void onComplete(Bundle values,
							FacebookException error) {
							
						if (error == null) {				
							// When the story is posted, echo the success
							// and the post Id.
							final String postId = values.getString("post_id");
							if (postId != null) {
								SendVerifiedRequest loader = new SendVerifiedRequest(
										Constants.VERIFY_USER, VerifiedActivity.this);
								getRequestQueue().addRequest(loader);						        	   
					        	  							        	   		
							} else {
								
								Intent intent2 = new Intent(VerifiedActivity.this, VerifiedFailedActivity.class);
					        	   startActivity(intent2);
							}
						} else if (error instanceof FacebookOperationCanceledException) {
							Intent intent2 = new Intent(VerifiedActivity.this, VerifiedFailedActivity.class);
				        	   startActivity(intent2);
						} else {
							// Generic, ex: network error
							Toast.makeText(getApplicationContext(),
									"Error posting story", Toast.LENGTH_SHORT)
									.show();
						}
					}

				}).build();
		  feedDialog.show();
	}
//	public void enableDialogWarning(final Context context, String title, final String message)
//	 {
//       AlertDialog.Builder builder;
//       builder = new AlertDialog.Builder(context);
//       final AlertDialog dialog = builder.create();
//       LayoutInflater inflater = LayoutInflater
//               .from(context);
//       View layout = inflater.inflate(R.layout.dialog_warning_ok,
//               null);
//       dialog.setView(layout, 0, 0, 0, 0);
//       TextView tvTitle = (TextView)layout.findViewById(R.id.dialog_warning_lltheader_tvTitle);
//       tvTitle.setText(title);
//       TextView tvQuestion = (TextView)layout.findViewById(R.id.dialog_warning_tvQuestion);
//       tvQuestion.setText(message);
//       Button btnOK = (Button) layout
//               .findViewById(R.id.dialog_internet_access_lltfooter_btOK);
//       btnOK.setOnClickListener(new OnClickListener() {
//
//           @Override
//           public void onClick(View v) {
//        	   first = true;
//        	   String success = context.getString(R.string.txt_you_have_verified);
//               if(success.equals(message)){
//            	   if(VerifiedActivity.this.force_verified || VerifiedActivity.this.start_login){
//            		   startSnapshot();
//            	   }
//            	   ((Activity) context).finish();
//               }
//               else{
//                   dialog.dismiss();
//               }
//           }
//       });
//       dialog.setCancelable(false);
//       dialog.show();
//	 }
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,resultCode, data);
	}
	@Override
	protected void onDestroy() {
		first = true;
		super.onDestroy();
	}
	class SendVerifiedRequest extends RequestUI {
		VerifiedReturnObject obj;

		public SendVerifiedRequest(Object key, Activity activity) {
			super(key, activity);
		}

		@Override
		public void execute() throws Exception {
			obj = oakClubApi.VerifiedUser();
		}

		@Override
		public void executeUI(Exception ex) {
			if (obj != null && obj.isStatus()){
				  ProfileSettingFragment.profileInfoObj.setIs_verified(true);
	        	   Intent intent = VerifiedActivity.this.getIntent();
	        	   intent.putExtra(Constants.VERIFIED_SUCCESS, true);
	        	   setResult(RESULT_OK, intent);
	        	   //Log.v("token new", Session.getActiveSession().getAccessToken());
	        	   Intent intent2 = new Intent(VerifiedActivity.this, VerifiedSecceedActivity.class);
	        	   intent2.putExtra(Constants.START_LOGIN, VerifiedActivity.this.start_login);
	        	   startActivity(intent2);
	        	   finish();
			}
		}

	}
	class SendSkipVerifiedRequest extends RequestUI {
		VerifiedReturnObject obj;

		public SendSkipVerifiedRequest(Object key, Activity activity) {
			super(key, activity);
		}

		@Override
		public void execute() throws Exception {
			obj = oakClubApi.SkipVerified();
		}

		@Override
		public void executeUI(Exception ex) {

				if(start_login){
					startSnapshot();
				}
				finish();

		}

	}
}
