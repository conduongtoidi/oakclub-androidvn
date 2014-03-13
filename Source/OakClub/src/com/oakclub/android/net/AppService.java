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
import com.oakclub.android.AllChatActivity;
import com.oakclub.android.ChatActivity;
import com.oakclub.android.MainActivity;
import com.oakclub.android.MatchChatActivity;
import com.oakclub.android.R;
import com.oakclub.android.VIPActivity;
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
	
	}

}