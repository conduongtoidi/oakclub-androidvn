package com.oakclub.android.net;

import java.util.concurrent.atomic.AtomicInteger;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.android.gcm.GCMBaseIntentService;
import com.oakclub.android.ChatActivity;
import com.oakclub.android.R;
import com.oakclub.android.util.Constants;

public class GCMIntentService extends GCMBaseIntentService {

    public GCMIntentService() {
        super(Constants.SENDER_ID);
    }
    
    @Override
    protected void onError(Context context, String errorId) {
        Log.e("Registration", "Got an error!"); 
        Log.e("Registration", context.toString() + errorId.toString());
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i("Registration", "Got a message!");
        Log.i("Registration", context.toString() + " " + intent.toString());
        //String name = intent.getExtras().getString("username");
        String profileId = intent.getExtras().getString("targetId");
        String message = intent.getExtras().getString("content");
        // notifies user
        generateNotification(context, profileId, message);     
    }

    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        // notifies user 
    }
    
    @Override
    protected void onRegistered(Context context, String registerId) {
        Log.i("Registration", "Just registered!");
        Log.i("Registration", context.toString() + registerId.toString());  
        // This is where you need to call your server to record the device toekn and registration id.
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
    public static void generateNotification(Context context, String profileId, String message) {

        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(Constants.BUNDLE_AVATAR, "");
        intent.putExtra(Constants.BUNDLE_NAME, "No Name");
        intent.putExtra(Constants.BUNDLE_PROFILE_ID, profileId);

        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        
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
        view.setTextViewText(R.id.titleNotify, message);
        
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
