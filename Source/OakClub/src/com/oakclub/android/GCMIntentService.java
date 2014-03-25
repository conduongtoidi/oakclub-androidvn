package com.oakclub.android;

import java.util.Random;

import org.jivesoftware.smack.XMPPException;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts.Data;
import android.support.v4.app.NotificationCompat;
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

	private static DataChatNotification dataChat;

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
		if (OakClubBaseActivity.xmpp != null && !OakClubBaseActivity.xmpp.isConnected()) {
			try {
				OakClubBaseActivity.xmpp.connect();
			} catch (XMPPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String notificationContent = intent.getExtras().getString("alert");
		boolean isLoad = OakClubUtil.isForeground("com.oakclub.android",
				this);
		SharedPreferences pref = getSharedPreferences("oakclub_pref",
				MODE_PRIVATE);
		boolean isLogin = pref.getBoolean(Constants.PREFERENCE_LOGGINED,
				false);
		if (intent.getExtras().getString("type").equals("chat")) {
			
			dataChat = OakClubJsonParser.getJsonObjectByMapper(intent.getExtras()
					.getString("info"), DataChatNotification.class);
			if (isLoad && isLogin) {
				ChatNotification(context, notificationContent);
			}
			
			
		} else if (intent.getExtras().getString("type").equals("mutual_match")) {
			if (isLoad && isLogin) {
				dataChat = OakClubJsonParser.getJsonObjectByMapper(intent.getExtras()
						.getString("info"), DataChatNotification.class);
				MutualMatchNotification(context, notificationContent);
			}
		} else if (intent.getExtras().getString("type").equals("total_like")) {			
			if (isLoad && isLogin) {
				TotalLikeNotification(context, notificationContent);
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
	public static void ChatNotification(Context context, String content) {

		Intent intent = new Intent(context, ChatActivity.class);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setAction(Intent.ACTION_MAIN);
		
		Bundle bundle = new Bundle();
		bundle.putString(Constants.BUNDLE_PROFILE_ID,
                dataChat.getProfile_id());
        bundle.putString(Constants.BUNDLE_AVATAR, dataChat
                .getAvatar());
        bundle.putString(Constants.BUNDLE_NAME, dataChat
                .getName());
        bundle.putInt(Constants.BUNDLE_STATUS, 1);
        bundle.putString(Constants.BUNDLE_MATCH_TIME, "");
        bundle.putBoolean(Constants.BUNDLE_NOTI, true);
		
        intent.putExtras(bundle);
		
        
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TASK);

		PendingIntent pI = PendingIntent.getActivity(context, Math.abs(dataChat.getProfile_id().hashCode()), intent,
				PendingIntent.FLAG_CANCEL_CURRENT);

		NotificationManager notiMgr = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		
		Notification noti = new Notification(R.drawable.logo_oakclub,
				context.getString(R.string.app_name),
				System.currentTimeMillis());
		// layout
		RemoteViews view = new RemoteViews(context.getPackageName(),
				R.layout.layout_notification);
		view.setTextViewText(R.id.titleNotify, content);

		noti.contentView = view;
		noti.contentIntent = pI;
		noti.flags |= Notification.FLAG_AUTO_CANCEL;

		// Play default notification sound
		noti.defaults |= Notification.DEFAULT_SOUND;

		// Vibrate if vibrate is enabled
		noti.defaults |= Notification.DEFAULT_VIBRATE;
		Log.v("profile_id_hashcode", Math.abs(dataChat.getProfile_id().hashCode()) + "");
		noti.flags |= Notification.FLAG_SHOW_LIGHTS;
		noti.ledARGB = 0xff00ff00;
		noti.ledOnMS = 300;
		noti.ledOffMS = 1000;
		
		notiMgr.notify(Math.abs(dataChat.getProfile_id().hashCode()), noti);
		
//		String appName = context.getString(R.string.app_name);
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(context)
//                        .setSmallIcon(R.drawable.logo_oakclub)
//                        .setTicker(context.getString(R.string.app_name));
//
//        //Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/raw/" + audioToneName);
//        //mBuilder.setSound(NotificationCompat.d);
//        mBuilder.setAutoCancel(true);
//        //mBuilder.setVibrate(Utility.vibrationPattern);
//        // Creates an explicit intent for an Activity in your app
//        Intent resultIntent = new Intent(context, ChatActivity.class);
//		Bundle bundle = new Bundle();
//		bundle.putString(Constants.BUNDLE_PROFILE_ID,
//                dataChat.getProfile_id());
//        bundle.putString(Constants.BUNDLE_AVATAR, dataChat
//                .getAvatar());
//        bundle.putString(Constants.BUNDLE_NAME, dataChat
//                .getName());
//        bundle.putInt(Constants.BUNDLE_STATUS, 1);
//        bundle.putString(Constants.BUNDLE_MATCH_TIME, "");
//        bundle.putBoolean(Constants.BUNDLE_NOTI, true);
//		
//        resultIntent.putExtras(bundle);
//        // The stack builder object will contain an artificial back stack for
//        // the
//        // started Activity.
//        // This ensures that navigating backward from the Activity leads out of
//        // your application to the Home screen.
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//        // Adds the back stack for the Intent (but not the Intent itself)
//        stackBuilder.addParentStack(ChatActivity.class);
//        // Adds the Intent that starts the Activity to the top of the stack
//        stackBuilder.addNextIntent(resultIntent);
//        PendingIntent resultPendingIntent =
//                stackBuilder.getPendingIntent(
//                		dataChat.getProfile_id().hashCode(),
//                        PendingIntent.FLAG_UPDATE_CURRENT
//                        );
//        mBuilder.setContentIntent(resultPendingIntent);
//        mBuilder.setContent(view);
//
//        NotificationManager mNotificationManager =
//                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        // mId allows you to update the notification later on.
//        mNotificationManager.notify(dataChat.getProfile_id().hashCode(), mBuilder.build());
	}

	@SuppressWarnings("deprecation")
	public static void MutualMatchNotification(Context context, String content) {

		Intent intent = new Intent(context, MainActivity.class);
		
		Bundle bundle = new Bundle();
		bundle.putBoolean(Constants.isLoadListMutualMatch, true);
		bundle.putString(Constants.BUNDLE_PROFILE_ID, dataChat.getProfile_id());
		intent.putExtras(bundle);
		
        intent.setAction(Intent.ACTION_VIEW);
		intent.setAction(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TASK);

		Random rd = new Random();
		PendingIntent pI = PendingIntent.getActivity(context, rd.nextInt(10), intent,
				PendingIntent.FLAG_ONE_SHOT);

		NotificationManager notiMgr = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification noti = new Notification(R.drawable.logo_oakclub,
				context.getString(R.string.app_name),
				System.currentTimeMillis());
		// layout
		RemoteViews view = new RemoteViews(context.getPackageName(),
				R.layout.layout_notification);
		view.setTextViewText(R.id.titleNotify, content);

		noti.contentView = view;
		noti.contentIntent = pI;
		noti.flags |= Notification.FLAG_AUTO_CANCEL;

		// Play default notification sound
		noti.defaults |= Notification.DEFAULT_SOUND;

		// Vibrate if vibrate is enabled
		noti.defaults |= Notification.DEFAULT_VIBRATE;
		Log.v("profile_id_hashcode", dataChat.getProfile_id().hashCode() + "");
		noti.flags |= Notification.FLAG_SHOW_LIGHTS;
		noti.ledARGB = 0xff00ff00;
		noti.ledOnMS = 300;
		noti.ledOffMS = 1000;
		
		notiMgr.notify(dataChat.getProfile_id().hashCode(), noti);
	}
	
	@SuppressWarnings("deprecation")
	public static void TotalLikeNotification(Context context, String content) {

		Intent intent = new Intent(context, MainActivity.class);
		
        intent.setAction(Intent.ACTION_VIEW);
		intent.setAction(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TASK);

		Random rd = new Random();
		PendingIntent pI = PendingIntent.getActivity(context, rd.nextInt(10), intent,
				PendingIntent.FLAG_ONE_SHOT);

		NotificationManager notiMgr = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification noti = new Notification(R.drawable.logo_oakclub,
				context.getString(R.string.app_name),
				System.currentTimeMillis());
		// layout
		RemoteViews view = new RemoteViews(context.getPackageName(),
				R.layout.layout_notification);
		view.setTextViewText(R.id.titleNotify, content);

		noti.contentView = view;
		noti.contentIntent = pI;
		noti.flags |= Notification.FLAG_AUTO_CANCEL;

		// Play default notification sound
		noti.defaults |= Notification.DEFAULT_SOUND;

		// Vibrate if vibrate is enabled
		noti.defaults |= Notification.DEFAULT_VIBRATE;
		
		noti.flags |= Notification.FLAG_SHOW_LIGHTS;
		noti.ledARGB = 0xff00ff00;
		noti.ledOnMS = 300;
		noti.ledOffMS = 1000;
		
		notiMgr.notify(1, noti);
	}
}
