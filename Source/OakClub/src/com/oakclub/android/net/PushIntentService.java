package com.oakclub.android.net;

import com.oakclub.android.R;
import com.oakclub.android.SnapshotActivity;
import com.oakclub.android.base.SlidingMenuActivity;
import com.oakclub.android.util.Constants;
import com.stepsdk.android.push.GCMIntentService;
import com.stepsdk.android.push.PushController;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

public class PushIntentService extends GCMIntentService {

    public int onStartCommand(Intent intent, int flags, int startId) {
        PushController.getInstance(getApplicationContext())
        .withSenderId("GCM_SENDER_ID")
        .withServerUrl("http://android-push-server:3000/push_api")
        .withBaseActivityClass(SlidingMenuActivity.class);     

        return super.onStartCommand(intent, flags, startId);
    }

    
    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    @Override
    protected void generateNotification(Context context, String title, String message, String url, String sound) {
    	Intent intent = new Intent(context, SnapshotActivity.class);
		intent.setAction(Intent.ACTION_MAIN);
		
		
		PendingIntent content = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationManager notiMgr = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification noti = new Notification(R.drawable.logo_oakclub,
				context.getString(R.string.app_name),
				System.currentTimeMillis());
		// layout
		RemoteViews view = new RemoteViews(context.getPackageName(),
				R.layout.layout_notification);
		// view.setTextViewText(R.id.titleNotify, title);
		view.setTextViewText(R.id.titleNotify, context.getString(R.string.txt_notification_content));
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