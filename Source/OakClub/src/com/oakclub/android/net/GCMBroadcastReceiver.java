package com.oakclub.android.net;

import android.content.Context;

public class GCMBroadcastReceiver extends com.google.android.gcm.GCMBroadcastReceiver {
    
    @Override
    protected String getGCMIntentServiceClassName(Context context) {

        return "com.oakclub.android.net.gcm.GCMIntentService";
    }
}
