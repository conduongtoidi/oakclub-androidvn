package com.oakclub.android.net;

import android.content.Context;

import com.google.android.gcm.GCMBroadcastReceiver;

public class PushBroadcastReceiver extends GCMBroadcastReceiver {
	@Override
	protected String getGCMIntentServiceClassName(Context context) {
		// TODO Auto-generated method stub
		return super.getGCMIntentServiceClassName(context);
	}
}
