package com.oakclub.android.net;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.oakclub.android.ChatActivity;
import com.oakclub.android.MainActivity;
import com.oakclub.android.R;
import com.oakclub.android.base.OakClubBaseActivity;
import com.oakclub.android.base.SlidingMenuActivity;
import com.oakclub.android.model.ChatHistoryData;
import com.oakclub.android.model.ListChatData;
import com.oakclub.android.util.Constants;
import com.oakclub.android.util.OakClubUtil;

public class AppService extends WakefulIntentService {

	public static String username;
	public static String password;
	XMPPConnection xmpp = null;
	String fromName;
	public AppService() {
		super("AppService");
	}

	@Override
	protected void doWakefulWork(Intent intent) {
		SharedPreferences pref = getSharedPreferences("oakclub_pref",
				MODE_PRIVATE);
		boolean isLoad = OakClubUtil.isForeground("com.oakclub.android", this);// pref.getBoolean(Constants.isLoadChatFromNotificationWhenKillApp,
																// false);
		username = pref.getString("username", null);
		password = pref.getString("password", null);

		final boolean isLogin = pref.getBoolean(Constants.PREFERENCE_LOGGINED,
				false);
		Log.v("turn on service", "1");
		Log.v("isLogin", isLogin + "");
		if (isLoad && isLogin) {
			Log.v("user-pass:", username + " " + password);
			ConnectionConfiguration config = new ConnectionConfiguration(
					getString(R.string.xmpp_server_address),
					Constants.XMPP_SERVER_PORT, Constants.XMPP_SERVICE_NAME);
			config.setSASLAuthenticationEnabled(true);

			if (xmpp == null) {
				xmpp = new XMPPConnection(config);
			} else {
				try {
					PacketFilter filter = new MessageTypeFilter(
							Message.Type.chat);
					xmpp.addPacketListener(null, filter);
					xmpp.disconnect();
				} catch (Exception ex) {

				}
			}
			try {
				SASLAuthentication.supportSASLMechanism("DIGEST-MD5", 0);
				// SASLAuthentication.supportSASLMechanism("PLAIN", 0);
				xmpp = new XMPPConnection(config);
				xmpp.connect();
				xmpp.login(username, password);
				PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
				xmpp.addPacketListener(
						new org.jivesoftware.smack.PacketListener() {

							@Override
							public void processPacket(Packet packet) {
								if (isLogin) {
									Message message = (Message) packet;
									Log.v("app service", "1");
									if (message.getBody() != null) {
										Log.i("To Huy", "" + message.getBody());
										solveReceiveNewMessage(message);
									}
								} else {
									xmpp.disconnect();
								}
							}
						}, filter);
			} catch (XMPPException e) {
				Toast.makeText(getApplicationContext(), "XMPP Error", 1).show();
				e.printStackTrace();
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "XMPP Error 2", 1)
						.show();
				e.printStackTrace();
			}
		} else {
			try {
				PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
				xmpp.addPacketListener(null, filter);
				xmpp.disconnect();
			} catch (Exception ex) {

			}
		}
	}

	public void solveReceiveNewMessage(Message msg) {
		xmpp.disconnect();
		ChatHistoryData message = new ChatHistoryData();
		message.setBody(msg.getBody());
		String str;
		str = msg.getTo();
		str = str.substring(0, str.indexOf('@'));

		message.setTo(str);
		str = msg.getFrom();
		str = str.substring(0, str.indexOf('@'));

		message.setFrom(str);
		SimpleDateFormat df = new SimpleDateFormat(Constants.CHAT_TIME_FORMAT);
		String formattedDate = df.format(new Date());
		message.setTime_string(formattedDate);
		updateNewMessage(message);
	}

	public void updateNewMessage(final ChatHistoryData message) {
		boolean needToShowNotification = false;
		if (ChatActivity.profile_id != null
				&& ChatActivity.profile_id.equals(message.getFrom())) {
			new Handler(Looper.getMainLooper()).post(new Runnable() {

				@Override
				public void run() {
					ChatActivity.messageArrayList.add(message);
					ChatActivity.adapter.notifyDataSetChanged();
					ChatActivity.chatLv.setSelection(ChatActivity.adapter
							.getCount() - 1);

				}
			});
			
		} 
		if (OakClubUtil.isForeground("com.oakclub.android", this)) {
            needToShowNotification = true;
        }else {
			needToShowNotification = false;
		}
		if (needToShowNotification) {
			ContentNotification noti = new ContentNotification();
			noti.setNotification(message.getFrom(), getApplicationContext(),
					message.getBody());
		}
		int id = -1;
		if (OakClubBaseActivity.baseAllList != null)
			for (int i = 0; i < OakClubBaseActivity.baseAllList.size(); i++) {
				if (OakClubBaseActivity.baseAllList.get(i).getProfile_id()
						.equals(message.getFrom())) {
					id = i;
					break;
				}
			}
		Log.v("id style", id + "");
		if (id == -1) {
			ListChatData newMessage = new ListChatData();
			newMessage.setProfile_id(message.getFrom());
			newMessage.setName("NO NAME");
			newMessage.setLast_message(message.getBody());
			newMessage.setTime(message.getTime_string());
			newMessage.setStatus(2);
			newMessage.setMatches(false);
			newMessage.setUnread_count(1);
			OakClubBaseActivity.baseAllList.add(0, newMessage);
			OakClubBaseActivity.allList.add(0, newMessage);
			OakClubBaseActivity.vipList.add(0, newMessage);
		} else {
			OakClubBaseActivity.baseAllList.get(id).setLast_message(
					message.getBody());
			OakClubBaseActivity.baseAllList.get(id).setTime(
					message.getTime_string());
			OakClubBaseActivity.matchedList.get(id)
					.setUnread_count(
							OakClubBaseActivity.matchedList.get(id)
									.getUnread_count() + 1);
			for (int i = 0; i < OakClubBaseActivity.matchedList.size(); i++) {
				if (OakClubBaseActivity.matchedList.get(i).getProfile_id()
						.equals(message.getFrom())) {
					OakClubBaseActivity.matchedList.get(i).setLast_message(
							message.getBody());
					OakClubBaseActivity.matchedList.get(i).setTime(
							message.getTime_string());
					OakClubBaseActivity.matchedList.get(i).setUnread_count(
							OakClubBaseActivity.matchedList.get(i)
									.getUnread_count() + 1);
				}
			}
			for (int i = 0; i < OakClubBaseActivity.allList.size(); i++) {
				if (OakClubBaseActivity.allList.get(i).getProfile_id()
						.equals(message.getFrom())) {
					OakClubBaseActivity.allList.get(i).setLast_message(
							message.getBody());
					OakClubBaseActivity.allList.get(i).setTime(
							message.getTime_string());
					OakClubBaseActivity.allList.get(i).setUnread_count(
							OakClubBaseActivity.allList.get(i)
									.getUnread_count() + 1);
				}
			}
			for (int i = 0; i < OakClubBaseActivity.vipList.size(); i++) {
				if (OakClubBaseActivity.vipList.get(i).getProfile_id()
						.equals(message.getFrom())) {
					OakClubBaseActivity.vipList.get(i).setLast_message(
							message.getBody());
					OakClubBaseActivity.vipList.get(i).setTime(
							message.getTime_string());
					OakClubBaseActivity.vipList.get(i).setUnread_count(
							OakClubBaseActivity.vipList.get(i)
									.getUnread_count() + 1);
				}
			}
		}
		new Handler(Looper.getMainLooper()).post(new Runnable() {

			@Override
			public void run() {
				SlidingMenuActivity.totalUnreadMessage += 1;
				if (SlidingMenuActivity.mNotificationTv != null) {
					SlidingMenuActivity.mNotificationTv.setText(""
							+ SlidingMenuActivity.totalUnreadMessage);
					if (SlidingMenuActivity.totalUnreadMessage > 0) {
						SlidingMenuActivity.mNotificationTv
								.setVisibility(View.VISIBLE);
						SharedPreferences.Editor editor = getSharedPreferences(
								Constants.PREFERENCE_NAME, MODE_PRIVATE).edit();
						editor.putBoolean(Constants.isLoadChat, true);
						editor.commit();
					} else {
						SlidingMenuActivity.mNotificationTv
								.setVisibility(View.GONE);
					}
				}
				if (OakClubBaseActivity.adapterAllListChatData != null) {
					OakClubBaseActivity.adapterAllListChatData
							.notifyDataSetChanged();
				}
				if (OakClubBaseActivity.adapterVIPListChatData != null) {
					OakClubBaseActivity.adapterVIPListChatData
							.notifyDataSetChanged();
				}
				if (OakClubBaseActivity.adapterMatchListChatData != null) {
					OakClubBaseActivity.adapterMatchListChatData
							.notifyDataSetChanged();
				}
			}
		});
	}

	class ContentNotification {
		AtomicInteger atomicInteger = new AtomicInteger(1);

		@SuppressWarnings("deprecation")
		public void setNotification(String profileId, Context context,
				String str) {
			int id = -1;
	
			Intent intent = new Intent(context, MainActivity.class);
			intent.setAction(Intent.ACTION_MAIN);
			
			SharedPreferences pref = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE);
			fromName = pref.getString(profileId, null);
			if (fromName != null) {
				id = pref.getInt(fromName, -1);
			}
			
			PendingIntent content = PendingIntent.getActivity(context, 0,
					intent, PendingIntent.FLAG_CANCEL_CURRENT);

			NotificationManager notiMgr = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			Notification noti = new Notification(R.drawable.logo_oakclub,
					context.getString(R.string.app_name),
					System.currentTimeMillis());
			// layout
			RemoteViews view = new RemoteViews(context.getPackageName(),
					R.layout.layout_notification);
			// view.setTextViewText(R.id.titleNotify, title);
//			view.setTextViewText(R.id.titleNotify,
//					OakClubBaseActivity.baseAllList.get(id).getName() + ": " + str);
//	         view.setTextViewText(R.id.titleNotify,
//	                    context.getString(R.string.txt_notification_content));

			view.setTextViewText(R.id.titleNotify,
					fromName + ": " + str);

			noti.contentView = view;
			noti.contentIntent = content;
			noti.flags |= Notification.FLAG_AUTO_CANCEL + Notification.FLAG_SHOW_LIGHTS;
			
			// Play default notification sound
			noti.defaults |= Notification.DEFAULT_SOUND;

			// Vibrate if vibrate is enabled
			noti.defaults |= Notification.DEFAULT_VIBRATE;

			notiMgr.notify(id, noti);

		}
	}
}