package com.oakclub.android.base;

import java.util.ArrayList;

import org.jivesoftware.smack.XMPPConnection;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.oakclub.android.MainActivity;
import com.oakclub.android.R;
import com.oakclub.android.R.drawable;
import com.oakclub.android.R.id;
import com.oakclub.android.R.layout;
import com.oakclub.android.R.string;
import com.oakclub.android.core.IRequestQueue;
import com.oakclub.android.core.RequestQueue;
import com.oakclub.android.core.RequestUI;
import com.oakclub.android.model.ChatHistoryData;
import com.oakclub.android.model.GetSnapShot;
import com.oakclub.android.model.ListChatData;
import com.oakclub.android.model.adaptercustom.AdapterListChat;
import com.oakclub.android.net.IOakClubApi;
import com.oakclub.android.net.OakClubApi;
import com.oakclub.android.util.Constants;
import com.oakclub.android.util.OakClubUtil;
import com.oakclub.android.view.GPSTracker;

public class OakClubBaseActivity extends FragmentActivity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	public IOakClubApi oakClubApi;
	public IOakClubApi oakClubApiTemp;
	public static String facebook_user_id;
	public static String access_token;
	public static long access_expires;
	public static String appVer;
	public static String nameDevice;
	public static XMPPConnection xmpp;
	public static String user_id;
	public static ArrayList<ListChatData> baseAllList;

	protected Intent intent;

	public static int[] mChatStatusIcon = { R.drawable.matchup_on,
			R.drawable.matchup_off, R.drawable.chat_up_on,
			R.drawable.chat_up_off, R.drawable.matchup_off };

	private LocationClient mLocationClient = null;
	protected GPSTracker mGPS = null;
	protected Location location = null;
	protected String android_token;

	protected View view;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(android.R.style.Theme_Holo_NoActionBar);
		super.onCreate(savedInstanceState);
		if (OakClubUtil.isInternetAccess(OakClubBaseActivity.this)) {
			if (Constants.imageLoader == null) {
				Constants.imageLoader = ImageLoader.getInstance();
				Constants.imageLoader.init(ImageLoaderConfiguration
						.createDefault(this.getApplicationContext()));
				setConfigImage();
			}

			if (Constants.options == null) {
				Constants.options = new DisplayImageOptions.Builder()
						.showStubImage(R.drawable.logo_splashscreen)
						.showImageForEmptyUri(R.drawable.logo_splashscreen)
						// .cacheInMemory().cacheOnDisc()
						.build();
			}

			oakClubApi = OakClubApi.createInstance(
					this.getApplicationContext(),
					getString(R.string.default_server_address));
			oakClubApiTemp = OakClubApi.createInstance(
					this.getApplicationContext(),
					getString(R.string.default_server_address_temp));

			mGPS = new GPSTracker(this);
			mLocationClient = new LocationClient(this, this, this);
			// if(mGPS.canGetLocation() )
			// location = mGPS.getLocation();

		} else {
			AlertDialog.Builder builder;
			builder = new AlertDialog.Builder(this);
			final AlertDialog dialog = builder.create();
			LayoutInflater inflater = LayoutInflater.from(this);
			View layout = inflater.inflate(R.layout.dialog_warning_ok, null);
			dialog.setView(layout, 0, 0, 0, 0);
			Button btnOK = (Button) layout
					.findViewById(R.id.dialog_internet_access_lltfooter_btOK);
			btnOK.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
					System.exit(0);
				}
			});
			dialog.setCancelable(false);
			dialog.show();
		}

	}


	class PingActivitiesLoader extends RequestUI {
		public PingActivitiesLoader(Object key, Activity activity) {
			super(key, activity);
		}

		@Override
		public void execute() throws Exception {
			oakClubApi.pingActivities();
		}

		@Override
		public void executeUI(Exception ex) {
			
		}
	}

	
	public RequestQueue getRequestQueue() {
		return ((IRequestQueue) getApplication()).getRequestQueue();
	}

	private void setConfigImage() {
		if (Constants.widthImage == 0 && Constants.heightImage == 0) {
			Display d = this.getWindowManager().getDefaultDisplay();
			int t = d.getWidth();
			if (t < 320) {
				Constants.heightImage = 100;
				Constants.widthImage = 100;
			} else if (t >= 320 && t < 480) {
				Constants.heightImage = 250;
				Constants.widthImage = 250;
			} else if (t >= 480 && t < 720) {
				Constants.heightImage = 250;
				Constants.widthImage = 250;
			}
			if (t >= 720 && t < 1024) {
				Constants.heightImage = 250;
				Constants.widthImage = 250;
			} else {
				Constants.heightImage = 320;
				Constants.widthImage = 320;
			}
		}
	}

	@Override
	protected void onPause() {
		System.gc();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		try {
			GCMRegistrar.onDestroy(this);

		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		System.gc();
		super.onDestroy();
	}

	protected void showOpenGPSSettingsDialog(final Activity activity) {
		new AlertDialog.Builder(activity)
				.setTitle(activity.getString(R.string.txt_gps_settings_title))
				.setMessage(
						activity.getString(R.string.txt_gps_settings_message))
				.setPositiveButton(activity.getString(R.string.txt_settings),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								activity.startActivity(new Intent(
										android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
								dialog.dismiss();
							}
						})
				.setNegativeButton(activity.getString(R.string.txt_cancel),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).show();
	}

	// Asyntask
	private AsyncTask<Void, Void, Void> mRegisterTask;

	protected void registerGCM() {
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		android_token = GCMRegistrar.getRegistrationId(this);
		Log.v("android_token", android_token);
		if (android_token.equals("")) {

			GCMRegistrar.register(this, Constants.SENDER_ID);
		} else {
			// Device is already registered on GCM Server
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				// Skips registration.
				Log.v("Already registered with GCM Server", "1");
			} else {
				final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... params) {
						// Register on our server
						oakClubApi.sendRegister(
								OakClubBaseActivity.facebook_user_id,
								OakClubBaseActivity.access_token, "3",
								LoginBaseActivity.appVer,
								LoginBaseActivity.nameDevice, android_token);
						GCMRegistrar.setRegisteredOnServer(context, true);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mRegisterTask = null;
					}
				};
				mRegisterTask.execute(null, null, null);
			}
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		// Connect the client.
		if (OakClubUtil.isInternetAccess(OakClubBaseActivity.this)) {
			mLocationClient.connect();
		}
	}

	/*
	 * Called when the Activity is no longer visible.
	 */
	@Override
	protected void onStop() {
		// Disconnecting the client invalidates it.
		super.onStop();
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle arg0) {
		// Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
		LocationManager manager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
				new LocationListener() {
					@Override
					public void onStatusChanged(String provider, int status,
							Bundle extras) {

					}

					@Override
					public void onProviderEnabled(String provider) {

					}

					@Override
					public void onProviderDisabled(String provider) {

					}

					@Override
					public void onLocationChanged(Location loc) {
						location = loc;
					}
				});
		// location = mLocationClient.getLastLocation();
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResume() {
		if (!OakClubUtil.isInternetAccess(OakClubBaseActivity.this)) {
			OakClubUtil.enableDialogWarning(this,
					this.getString(R.string.txt_warning),
					this.getString(R.string.txt_internet_message));
		}
		com.facebook.AppEventsLogger.activateApp(this,
				this.getString(R.string.app_id));
		super.onResume();
	}
}

// static {
// matchedList = new ArrayList<ListChatData>();
// nonMatchedList = new ArrayList<ListChatData>();
// allList = new ArrayList<ListChatData>();
// }
