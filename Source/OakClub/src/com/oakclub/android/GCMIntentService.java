package com.oakclub.android;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts.Data;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;
import com.oakclub.android.MainActivity;
import com.oakclub.android.R;
import com.oakclub.android.R.drawable;
import com.oakclub.android.R.id;
import com.oakclub.android.R.layout;
import com.oakclub.android.R.string;
import com.oakclub.android.base.LoginBaseActivity;
import com.oakclub.android.base.OakClubBaseActivity;
import com.oakclub.android.model.DataChatNotification;
import com.oakclub.android.net.IOakClubApi;
import com.oakclub.android.net.OakClubApi;
import com.oakclub.android.net.OakClubJsonParser;
import com.oakclub.android.util.Constants;
import com.oakclub.android.util.OakClubUtil;

public class GCMIntentService extends GCMBaseIntentService {

	private static DataChatNotification data;

	public GCMIntentService() {
		super(Constants.SENDER_ID);
	}

	@Override
	protected void onError(Context context, String errorId) {
		Log.v("Registration", "Got an error!");
		Log.v("Registration", context.toString() + errorId.toString());
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.v("Registration", "Got a message!");
		Log.v("Registration", context.toString() + " " + intent.toString());

		if (intent.getExtras().getString("type").equals("chat")) {
			String notification = intent.getExtras().getString("alert");

			data = OakClubJsonParser.getJsonObjectByMapper(intent.getExtras()
					.getString("data"), DataChatNotification.class);

			// notifies user
			boolean isLoad = OakClubUtil.isForeground("com.oakclub.android",
					this);
			SharedPreferences pref = getSharedPreferences("oakclub_pref",
					MODE_PRIVATE);
			boolean isLogin = pref.getBoolean(Constants.PREFERENCE_LOGGINED,
					false);
			if (isLoad && isLogin) {
				generateNotification(context, notification);// , message);
			}
		}
	}

	@Override
	protected void onDeletedMessages(Context context, int total) {
		Log.i(TAG, "Received deleted messages notification");
		// notifies user
	}

	public IOakClubApi oakClubApi;

	@Override
	protected void onRegistered(Context context, String registerId) {
		Log.v("Registration", "Just registered!");
		Log.v("Registration", context.toString() + registerId.toString());
		// This is where you need to call your server to record the device token
		// and registration id.
		oakClubApi = OakClubApi.createInstance(this.getApplicationContext(),
				getString(R.string.default_server_address));
		oakClubApi.sendRegister(OakClubBaseActivity.facebook_user_id,
				OakClubBaseActivity.access_token, "3",
				LoginBaseActivity.appVer, LoginBaseActivity.nameDevice,
				registerId);
		GCMRegistrar.setRegisteredOnServer(context, true);
	}

	@Override
	protected void onUnregistered(Context context, String registerId) {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		// log message
		Log.i(TAG, "Received recoverable error: " + errorId);
		return super.onRecoverableError(context, errorId);
	}

	@SuppressWarnings("deprecation")
	public static void generateNotification(Context context, String chat) {

		Intent intent = new Intent(context, ChatActivity.class);
		
		Bundle bundle = new Bundle();
		bundle.putString(Constants.BUNDLE_PROFILE_ID,
                data.getProfile_id());
        bundle.putString(Constants.BUNDLE_AVATAR, data
                .getAvatar());
        bundle.putString(Constants.BUNDLE_NAME, data
                .getName());
        bundle.putInt(Constants.BUNDLE_STATUS, 1);
        bundle.putString(Constants.BUNDLE_MATCH_TIME, "");
        bundle.putBoolean(Constants.BUNDLE_NOTI, true);
		
        intent.putExtras(bundle);
		
        intent.setAction(Intent.ACTION_VIEW);
		intent.setAction(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TASK);

		PendingIntent content = PendingIntent.getActivity(context, 0, intent,
				PendingIntent.FLAG_ONE_SHOT);

		NotificationManager notiMgr = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification noti = new Notification(R.drawable.logo_oakclub,
				context.getString(R.string.app_name),
				System.currentTimeMillis());
		// layout
		RemoteViews view = new RemoteViews(context.getPackageName(),
				R.layout.layout_notification);
		view.setTextViewText(R.id.titleNotify, chat);

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