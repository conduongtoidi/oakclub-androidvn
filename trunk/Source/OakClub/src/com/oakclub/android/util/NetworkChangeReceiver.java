package com.oakclub.android.util;

import org.jivesoftware.smack.XMPPException;

import com.oakclub.android.base.OakClubBaseActivity;
import com.oakclub.android.net.Connectivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.widget.Toast;

public class NetworkChangeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		 String status = Connectivity.getConnectivityStatusString(context);
		 
        Toast.makeText(context, status, Toast.LENGTH_LONG).show();
        
        if (OakClubUtil.isInternetAccess(context)) {
        	if (OakClubBaseActivity.xmpp != null && !OakClubBaseActivity.xmpp.isConnected()) {
				try {
					StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
							.permitAll().build();
					StrictMode.setThreadPolicy(policy);
					OakClubBaseActivity.xmpp.connect();
				} catch (XMPPException e) {
					e.printStackTrace();
				}
			}
        }
	}
	
	
}
