package com.oakclub.android;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.oakclub.android.core.RequestUI;
import com.oakclub.android.fragment.ProfileSettingFragment;
import com.oakclub.android.fragment.SnapshotFragment;
import com.oakclub.android.model.VerifiedReturnObject;
import com.oakclub.android.util.Constants;
import com.oakclub.android.util.OakClubUtil;
import com.oakclub.android.util.RichTextHelper;

public class VerifiedActivity extends SlidingActivity {
	private Button btnSkip;
	private Button btnContinueVerified;

	private TextView line;
	public static boolean firstOpenSessonCall;
	public static boolean firstRequestCall;
	public static boolean start_login = false;
	private TextView tvVerifiedWay2;
	private WebDialog feedDialog;
	private TextView tvSamplePost;
	private LinearLayout layoutHeader;
	SlidingActivity activity;

	public VerifiedActivity(SlidingActivity activity, boolean startLogin) {
		this.activity = activity;
		start_login = startLogin;
	}

	public VerifiedActivity() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		activity.setMenu(MenuOakclub.VERIFIED);
		initVerified();
	}

	public void initVerified() {
		if (!OakClubUtil.isInternetAccess(activity)) {
			return;
		}
		Intent intent = activity.getIntent();
		start_login = intent
				.getBooleanExtra(Constants.START_LOGIN, start_login);
		if (!start_login) {
			activity.init(R.layout.activity_verified);
		} else {
			activity.setContentView(R.layout.activity_verified);
		}
		if (ProfileSettingFragment.profileInfoObj == null) {
			Intent intent2 = new Intent(activity, MainActivity.class);
			activity.startActivity(intent2);
			activity.finish();
		}

		btnSkip = (Button) activity.findViewById(R.id.btn_skip_verified);
		btnContinueVerified = (Button) activity
				.findViewById(R.id.btn_continue_verified);
		layoutHeader = (LinearLayout) activity
				.findViewById(R.id.activity_verified_header);

		line = (TextView) activity.findViewById(R.id.underline_back);
		tvVerifiedWay2 = (TextView) activity
				.findViewById(R.id.txt_verified_way_2);
		SpannableStringBuilder styledText = RichTextHelper.getRichText(activity
				.getString(R.string.txt_verified_way_2));
		tvVerifiedWay2.setText(styledText);

		tvSamplePost = (TextView) activity.findViewById(R.id.txt_sample_post);
		tvSamplePost.setText(activity.getString(R.string.txt_got_verified)
				+ "\n" + activity.getString(R.string.txt_oakclub_page) + "\n"
				+ activity.getString(R.string.txt_sample_post));
		if (start_login) {
			layoutHeader.setVisibility(View.GONE);
			line.setVisibility(View.GONE);
			btnSkip.setVisibility(View.VISIBLE);
		}
		btnSkip.setOnClickListener(buttonClick);
		btnContinueVerified.setOnClickListener(buttonClick);

	}

	public OnClickListener buttonClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (OakClubUtil.isInternetAccess(activity)) {

				switch (v.getId()) {
				case R.id.btn_continue_verified:
					((Button) activity.findViewById(R.id.btn_continue_verified))
							.setEnabled(false);
					publishStory();
					break;
				case R.id.btn_skip_verified:
					btnSkip.setEnabled(false);
					// SendSkipVerifiedRequest loader = new
					// SendSkipVerifiedRequest(
					// Constants.SKIP_VERIFIED, activity);
					// activity.getRequestQueue().addRequest(loader);
					if (start_login) {
						activity.setResult(RESULT_OK);
						finish();
					} else {
						comebackSnapshot();
					}
					break;
				case R.id.btn_back:
					// activity.finish();
					break;
				default:
					break;
				}
			} else {
				AlertDialog.Builder builder;
				builder = new AlertDialog.Builder(activity);
				final AlertDialog dialog = builder.create();
				LayoutInflater inflater = LayoutInflater.from(activity);
				View layout = inflater
						.inflate(R.layout.dialog_warning_ok, null);
				dialog.setView(layout, 0, 0, 0, 0);
				Button btnOK = (Button) layout
						.findViewById(R.id.dialog_internet_access_lltfooter_btOK);
				btnOK.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
						activity.finish();
					}
				});
				dialog.setCancelable(false);
				dialog.show();
			}
		}
	};

	private void comebackSnapshot() {
		if (activity.getMenu() != MenuOakclub.SNAPSHOT) {
			activity.setMenu(MenuOakclub.SNAPSHOT);
			SnapshotFragment snapshot = new SnapshotFragment(activity);
			snapshot.initSnapshot();
			activity.snapshot = snapshot;
		}
	}

	private boolean isSubsetOf(String string, Collection<String> superset) {
		if (!superset.contains(string)) {
			return false;
		}
		return true;
	}

	private void publishStory() {

		firstOpenSessonCall = true;
		firstRequestCall = true;

		SharedPreferences pref = activity.getSharedPreferences(
				Constants.PREFERENCE_NAME, 0);
		Long expirationTime = pref.getLong(Constants.HEADER_ACCESS_EXPIRES, 0);

		Date accessTokenExpires = new Date(expirationTime);

		List<String> permission = Arrays
				.<String> asList(Constants.FACEBOOK_PERMISSION);

		AccessToken accessToken = AccessToken.createFromExistingAccessToken(
				MainActivity.access_token, accessTokenExpires, null, null,
				permission);
		try {
			Session.openActiveSessionWithAccessToken(
					activity.getApplicationContext(), accessToken,
					new StatusCallback() {

						@Override
						public void call(Session session, SessionState state,
								Exception exception) {

							if (session.isOpened()
									&& VerifiedActivity.firstOpenSessonCall) {
								VerifiedActivity.firstOpenSessonCall = false;
								List<String> permissions = session
										.getPermissions();
								if (!isSubsetOf(Constants.VERIFIED_PERMISSIONS,
										permissions)) {
									Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
											activity,
											Constants.VERIFIED_PERMISSIONS);
									session.requestNewPublishPermissions(newPermissionsRequest);
									session.addCallback(new StatusCallback() {

										@Override
										public void call(Session session,
												SessionState state,
												Exception exception) {
											if (session.isOpened()
													&& VerifiedActivity.firstRequestCall) {
												VerifiedActivity.firstRequestCall = false;
												VerifiedActivity.this
														.publishFeedDialog();
												return;
											}
										}
									});

								} else {
									VerifiedActivity.this.publishFeedDialog();
								}
							}
						}
					});
		} catch (Exception ex) {

		}
	}

	private void startSnapshot() {
		Intent intent = new Intent(activity, SlidingActivity.class);
		activity.startActivity(intent);
	}

	private void publishFeedDialog() {
		if (!activity.isFinishing()) {
			Bundle params = new Bundle();
			params.putString("name",
					activity.getString(R.string.txt_got_verified));
			params.putString("caption",
					activity.getString(R.string.txt_oakclub_page));
			params.putString("description",
					activity.getString(R.string.txt_sample_post));

			params.putString("link", activity.getString(R.string.txt_share_url));
			try {
				feedDialog = (new WebDialog.FeedDialogBuilder(activity,
						Session.getActiveSession(), params))
						.setOnCompleteListener(new OnCompleteListener() {

							@Override
							public void onComplete(Bundle values,
									FacebookException error) {

								if (error == null) {

									final String postId = values
											.getString("post_id");
									if (postId != null) {
										SendVerifiedRequest loader = new SendVerifiedRequest(
												Constants.VERIFY_USER, activity);
										activity.getRequestQueue().addRequest(
												loader);

									} else {
										((Button) activity
												.findViewById(R.id.btn_continue_verified))
												.setEnabled(true);
										Intent intent2 = new Intent(activity,
												VerifiedFinishedActivity.class);
										intent2.putExtra(Constants.START_LOGIN,
												VerifiedActivity.start_login);
										intent2.putExtra(
												Constants.VERIFIED_FAILED, 1);
										activity.startActivityForResult(
												intent2, 0);
										// if(!start_login)
										// {
										// comebackSnapshot();
										// }
									}
								} else if (error instanceof FacebookOperationCanceledException) {
									((Button) activity
											.findViewById(R.id.btn_continue_verified))
											.setEnabled(true);
									Intent intent2 = new Intent(activity,
											VerifiedFinishedActivity.class);
									intent2.putExtra(Constants.START_LOGIN,
											VerifiedActivity.start_login);
									intent2.putExtra(Constants.VERIFIED_FAILED,
											1);
									activity.startActivityForResult(intent2, 0);
									// if (!start_login)
									// {
									// comebackSnapshot();
									// }
								} else {
									((Button) activity
											.findViewById(R.id.btn_continue_verified))
											.setEnabled(true);
									Toast.makeText(
											activity.getApplicationContext(),
											"Error posting story",
											Toast.LENGTH_LONG).show();
								}
							}
						}).build();
				feedDialog.show();

			} catch (Exception e) {

			}
		}
	}

	@Override
	protected void onResume() {
		if (!OakClubUtil.isInternetAccess(this)) {
			VerifiedActivity.firstOpenSessonCall = false;
			VerifiedActivity.firstRequestCall = false;
			if (!VerifiedActivity.start_login)
				finish();
		}
		super.onResume();
		if (((Button) findViewById(R.id.btn_continue_verified)) != null)
			((Button) findViewById(R.id.btn_continue_verified))
					.setEnabled(true);
		if (((Button) findViewById(R.id.btn_skip_verified)) != null)
			((Button) findViewById(R.id.btn_skip_verified)).setEnabled(true);
	}

	class SendVerifiedRequest extends RequestUI {
		VerifiedReturnObject obj;

		public SendVerifiedRequest(Object key, Activity activity) {
			super(key, activity);
		}

		@Override
		public void execute() throws Exception {
			obj = activity.oakClubApi.VerifiedUser();
		}

		@Override
		public void executeUI(Exception ex) {
			if (obj != null && obj.isStatus()
					&& ProfileSettingFragment.profileInfoObj != null) {
				ProfileSettingFragment.profileInfoObj.setIs_verify(true);
				Intent intent2 = new Intent(activity,
						VerifiedFinishedActivity.class);
				intent2.putExtra(Constants.START_LOGIN,
						VerifiedActivity.start_login);
				activity.startActivityForResult(intent2, 0);
				if (start_login) {
					Intent intent = getIntent();
					intent.putExtra(Constants.VERIFIED_SUCCESS, true);
					setResult(Activity.RESULT_OK, intent);
					finish();
				}
			} else {
				Toast.makeText(activity, activity.getString(R.string.abnormal_error_message), Toast.LENGTH_LONG).show();
				Intent intent = new Intent(activity, MainActivity.class);
				activity.startActivity(intent);
				activity.finish();
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
			obj = activity.oakClubApi.SkipVerified();
		}

		@Override
		public void executeUI(Exception ex) {
			if (ProfileSettingFragment.profileInfoObj != null) {
				ProfileSettingFragment.profileInfoObj.setSkip_verify(true);
				btnSkip.setEnabled(true);
				if (start_login) {
					startSnapshot();
				}
				activity.finish();
			} else {
				Toast.makeText(activity, activity.getString(R.string.abnormal_error_message), Toast.LENGTH_LONG).show();
				Intent intent = new Intent(activity, MainActivity.class);
				activity.startActivity(intent);
				activity.finish();
			}

		}

	}
}
