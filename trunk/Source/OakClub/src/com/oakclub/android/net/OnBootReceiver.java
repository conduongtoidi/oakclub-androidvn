package com.oakclub.android.net;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.util.Log;

public class OnBootReceiver extends BroadcastReceiver {
	private static final int PERIOD = 300000; // 5 minutes

	@Override
	public void onReceive(Context context, Intent intent) {
		
		AlarmManager mgr = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(context, OnAlarmReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
		
//		mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//				PERIOD, 10000, pi);
		mgr.cancel(pi);
//		mgr.set
		
//		ComponentName receiver = new ComponentName(context, OnBootReceiver.class);
//        PackageManager pm = context.getPackageManager();
//
//        pm.setComponentEnabledSetting(receiver,
//                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//                PackageManager.DONT_KILL_APP);
	}
	
}
