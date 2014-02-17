package com.oakclub.android.base;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;
import com.google.android.gcm.GCMRegistrar;
import com.oakclub.android.ChatActivity;
import com.oakclub.android.R;
import com.oakclub.android.SlidingActivity;
import com.oakclub.android.core.RequestUI;
import com.oakclub.android.fragment.ProfileSettingFragment;
import com.oakclub.android.model.ChatHistoryData;
import com.oakclub.android.model.GetDataLanguageReturnObject;
import com.oakclub.android.model.ListChatData;
import com.oakclub.android.model.SendRegisterReturnObject;
import com.oakclub.android.model.SetLocationReturnObject;
import com.oakclub.android.net.AppService;
import com.oakclub.android.util.Constants;
import com.oakclub.android.util.OakClubUtil;
import com.viewpagerindicator.PageIndicator;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class LoginBaseActivity extends OakClubBaseActivity {

    protected LinearLayout mLoginButton;
    //protected boolean onLoginSuccess = false;
    SendRegisterReturnObject sendRegObj;
    ProgressDialog pd;
    public static String appVer;
    public static String nameDevice;
    public static Facebook facebook;
    public static AsyncFacebookRunner mAsyncRunner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isInternetAccess())
        {
            return;
        }
        
        if (savedInstanceState == null) {
            appVer = android.os.Build.VERSION.RELEASE;
            nameDevice = android.os.Build.MODEL;
            facebook = new Facebook(getString(R.string.app_id));
            mAsyncRunner = new AsyncFacebookRunner(facebook);
            logInFacebook();
        }
        
    }

    protected void printHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.oakclub.android", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

    @SuppressWarnings("deprecation")
    protected void loginWithFB() {

    	SharedPreferences pref = getSharedPreferences(Constants.PREFERENCE_NAME, 0);
        boolean loggedIn = pref.getBoolean(Constants.PREFERENCE_LOGGINED, false);
        
        if (loggedIn)
        {
        	logInFacebook();
        } else
        // if (!facebook.isSessionValid())
        {
        	
            facebook.authorize(LoginBaseActivity.this, new String[]{
                    "friends_photos",
                    "friends_relationship_details",
                    "friends_relationships",
                    "friends_birthday",
                    "friends_location",
                    "friends_education_history",
                    "friends_work_history",
                    "friends_likes",
                    "friends_religion_politics",
                    "friends_status",
                    "friends_about_me",
                    "friends_interests",
                    "user_relationship_details",
                    "user_relationships",
                    "user_birthday",
                    "user_location",
                    "user_education_history",
                    "user_work_history",
                    "user_photos",
                    "user_likes",
                    "user_religion_politics",
                    "user_status",
                    "user_about_me",
                    "user_interests",
                    "email",
                    "user_hometown",
                    "friends_hometown",
                    "friends_events",
                    "friends_checkins",
                    "user_checkins",
                    "user_events"
                    }, Facebook.FORCE_DIALOG_AUTH, new DialogListener() {

                        @Override
                        public void onComplete(Bundle values) {
                        	Log.v("onCompleteFacebook", "1");
                        	//Log.v("onLoginSuccess", onLoginSuccess + "");
                            //if (onLoginSuccess) 
                        	{
                                (new Handler()).postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        //Session.openActiveSession(LoginBaseActivity.this, false, null);

                                        Editor editor = getSharedPreferences(Constants.PREFERENCE_NAME, 0).edit();
                                        editor.putBoolean(Constants.PREFERENCE_LOGGINED, true);
                                        editor.commit();
                                        mLoginButton.setEnabled(false);
                                        pd = new ProgressDialog(LoginBaseActivity.this);
                                        pd.setMessage(getString(R.string.txt_loading));
                                        pd.setCancelable(false);
                                        pd.show();
                                        getFacebookUserId();
                                    }
                                }, 1000);
                            }
                        }

                        @Override
                        public void onFacebookError(FacebookError error) {
                            Log.e("FB:", "Facebook Error");
                            OakClubUtil.enableDialogWarning(LoginBaseActivity.this, getResources().getString(R.string.txt_warning),
                                    getResources().getString(R.string.txt_signin_failed));
                            if (isInternetAccess()) {
                            	oakClubApi.sendRegister("", "", "3", appVer, nameDevice, android_token);
                            }
                        }

                        @Override
                        public void onError(DialogError e) {
                            Log.e("FB:", "Error");
                            OakClubUtil.enableDialogWarning(LoginBaseActivity.this, getResources().getString(R.string.txt_warning),
                                    getResources().getString(R.string.txt_signin_failed));
                            if (isInternetAccess()) {
                            	oakClubApi.sendRegister("", "", "3", appVer, nameDevice, android_token);
                            }
                        }

                        @Override
                        public void onCancel() {
                        	Log.v("onCancelFacebook", "1");
                            //onLoginSuccess = false;
                        }
                    });
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //onLoginSuccess = true;
        Log.v("onActivityResult", 1 + "");
        facebook.authorizeCallback(requestCode, resultCode, data);
    }
    class SendRegisterRequest extends RequestUI {
        SendRegisterReturnObject obj;
        String user_id;
        String access_token;

        public SendRegisterRequest(Object key, Activity activity, String user_id, String access_token) {
            super(key, activity);
            this.user_id = user_id;
            this.access_token = access_token;
            SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.PREFERENCE_NAME, 0);
            Editor editor = pref.edit();
            editor.putString(Constants.PREFERENCE_USER_ID, this.user_id);
            editor.putString(Constants.HEADER_ACCESS_TOKEN, access_token);
            editor.commit();
        }

        @Override
        public void execute() throws Exception {
            obj = oakClubApi.sendRegister(user_id, access_token, "3", appVer, nameDevice, android_token);
            sendRegObj = obj;
        }

        @Override
        public void executeUI(Exception ex) {
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
                mLoginButton.setEnabled(true);
            }
            mLoginButton.setEnabled(true);
            if (obj == null || (!obj.isStatus() && obj.getError_status().equals("1"))) {
                OakClubUtil.enableDialogWarning(LoginBaseActivity.this, getResources().getString(R.string.txt_warning),
                        getResources().getString(R.string.txt_signin_failed));
            } else {
                ProfileSettingFragment.profileInfoObj = obj.getData();
                // Register custom Broadcast receiver to show messages on activity
         		registerGCM();
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        location = mGPS.getLocation();
                        if (location != null) {
                            double longitude = location.getLongitude();
                            double latitude = location.getLatitude();
                            SendUserLocation loader = new SendUserLocation("sendLocation", LoginBaseActivity.this, ""
                                    + latitude, "" + longitude);
                            getRequestQueue().addRequest(loader);
                            Log.v("snapshot", "snapshot");
                            Intent intent = new Intent(LoginBaseActivity.this, SlidingActivity.class);
                            startActivity(intent);
                            // }
                            finish();
                        } else if (mGPS.isGPSEnabled) {
                            // showOpenGPSSettingsDialog(MainActivity.this);
                            OakClubUtil.enableDialogWarning(LoginBaseActivity.this,
                                    getResources().getString(R.string.txt_warning),
                                    getResources().getString(R.string.txt_gps_settings_title));
                        }
                    }
                });
                user_id = obj.getData().getProfile_id();
                LoginXMPPLoader loader = new LoginXMPPLoader("loginXMPP", LoginBaseActivity.this);
                getRequestQueue().addRequest(loader);
                GetDataLanguageLoader loader2 = new GetDataLanguageLoader("getDataLanguage", LoginBaseActivity.this);
                loader2.setPriority(RequestUI.PRIORITY_LOW);
                getRequestQueue().addRequest(loader2);
            }
        }
    }

    private void loginXMPP() {
        if (sendRegObj == null || sendRegObj.getData() == null) {
            return;
        }
        String username = sendRegObj.getData().getXmpp_username();
        String password = sendRegObj.getData().getXmpp_password();
        AppService.username = username;
        AppService.password = password;
        SharedPreferences.Editor editor = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.commit();
        ConnectionConfiguration config = new ConnectionConfiguration(getString(R.string.xmpp_server_address),
                Constants.XMPP_SERVER_PORT, Constants.XMPP_SERVICE_NAME);
        config.setSASLAuthenticationEnabled(true);
        if (xmpp == null) {
            xmpp = new XMPPConnection(config);
        } else {
            PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
            //xmpp.addPacketListener(null, filter);
            xmpp.disconnect();
        }
        try {
            SASLAuthentication.supportSASLMechanism("DIGEST-MD5", 0);
            // SASLAuthentication.supportSASLMechanism("PLAIN", 0);
            xmpp.connect();
            xmpp.login(username, password);
            Log.v("xmpp login", username);
            PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
            xmpp.addPacketListener(new org.jivesoftware.smack.PacketListener() {

                @Override
                public void processPacket(Packet packet) {
                    SharedPreferences pref = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE);
                    boolean isLogin = pref.getBoolean(Constants.PREFERENCE_LOGGINED, false);
                    if (isLogin) {
                        Message message = (Message) packet;
                        if (message.getBody() != null) {
                        	Log.v("Message login", message.getBody());
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
            Toast.makeText(getApplicationContext(), "XMPP Error 2", 1).show();
            e.printStackTrace();
        }
    }

    private void solveReceiveNewMessage(Message msg) {
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

    private void getFacebookUserId() {
        Request.Callback callback2 = new Request.Callback() {
            public void onCompleted(Response response) {
                if (response == null)
                    Log.v("response", "null");
                if (response.getGraphObject() == null)
                    Log.v("response.getGraphObject()", "null");
                if (response == null || response.getGraphObject() == null) {
                    Toast.makeText(getApplicationContext(), getString(R.string.abnormal_error_message),
                            Toast.LENGTH_SHORT).show();
                    mLoginButton.setEnabled(true);
                    pd.dismiss();
                    return;
                }
                JSONObject graphResponse = response.getGraphObject().getInnerJSONObject();
                String id = null;
                try {
                    id = graphResponse.getString("id");
                } catch (JSONException e) {
                }
                if (id == null) {
                    id = "";
                }
                facebook_user_id = id;
                FacebookRequestError error = response.getError();
                if (error != null) {
                    mLoginButton.setEnabled(true);
                    if (pd.isShowing()) {
                        pd.dismiss();
                    }
                } else {
                    access_token = Session.getActiveSession().getAccessToken();
                    SendRegisterRequest request = new SendRegisterRequest("sendRegister", LoginBaseActivity.this,
                            facebook_user_id, access_token);
                    getRequestQueue().addRequest(request);
                }
            }
        };
        Session.setActiveSession(facebook.getSession());
        Request request2 = new Request(Session.getActiveSession(), "me", null, HttpMethod.GET, callback2);
        RequestAsyncTask task2 = new RequestAsyncTask(request2);
        task2.execute();
    }

    @SuppressWarnings("static-access")
    protected void logInFacebook() {
        SharedPreferences pref = getSharedPreferences(Constants.PREFERENCE_NAME, 0);
        boolean loggedIn = pref.getBoolean(Constants.PREFERENCE_LOGGINED, false);
        Log.v("LoginFacebook: ", loggedIn + "");

        // registerGCM();

        if (loggedIn) {
            pd = new ProgressDialog(LoginBaseActivity.this);
            pd.setMessage(getString(R.string.txt_loading));
            pd.setCancelable(false);
            pd.show();
            facebook_user_id = pref.getString(Constants.PREFERENCE_USER_ID, null);
            access_token = pref.getString(Constants.HEADER_ACCESS_TOKEN, null);
            SendRegisterRequest request = new SendRegisterRequest("sendRegister", LoginBaseActivity.this,
                    facebook_user_id, access_token);
            getRequestQueue().addRequest(request);
        }
    }

    class SendUserLocation extends RequestUI {
        SetLocationReturnObject result;
        String latitude;
        String longitude;

        public SendUserLocation(Object key, Activity activity, String latitude, String longitude) {
            super(key, activity);
            this.latitude = latitude;
            this.longitude = longitude;
        }

        @Override
        public void execute() throws Exception {
            result = oakClubApi.SetUserLocation(latitude, longitude);
        }

        @Override
        public void executeUI(Exception ex) {

        }
    }
    class LoginXMPPLoader extends RequestUI {

        public LoginXMPPLoader(Object key, Activity activity) {
            super(key, activity);
        }

        @Override
        public void execute() throws Exception {
            loginXMPP();
        }

        @Override
        public void executeUI(Exception ex) {

        }

    }

    class GetDataLanguageLoader extends RequestUI {
        GetDataLanguageReturnObject obj;

        public GetDataLanguageLoader(Object key, Activity activity) {
            super(key, activity);
        }

        @Override
        public void execute() throws Exception {
            obj = oakClubApi.GetDataLanguage();
        }

        @Override
        public void executeUI(Exception ex) {
            if (obj != null && obj.getData() != null) {
                SlidingMenuActivity.mDataLanguageObj = obj.getData();
            }
        }

    }

    private void updateNewMessage(final ChatHistoryData message) {
        boolean needToShowNotification = false;
        if (ChatActivity.profile_id != null && ChatActivity.profile_id.equals(message.getFrom())) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                	ChatActivity.chatLv.setVisibility(View.VISIBLE);
                	ChatActivity.lltMatch.setVisibility(View.GONE);
                    ChatActivity.messageArrayList.add(message);
                    ChatActivity.adapter.notifyDataSetChanged();
                    ChatActivity.chatLv.setSelection(ChatActivity.adapter.getCount() - 1);
                }
            });
            if (!ChatActivity.isActive) {
                needToShowNotification = true;
            }
        } else {
            needToShowNotification = true;
        }
        int id = -1;
        if (baseAllList != null)
            for (int i = 0; i < baseAllList.size(); i++) {
                if (baseAllList.get(i).getProfile_id().equals(message.getFrom())) {
                    id = i;
                    break;
                }
            }
        if (id == -1) {
            ListChatData newMessage = new ListChatData();
            newMessage.setProfile_id(message.getFrom());
            newMessage.setName("NO NAME");
            newMessage.setLast_message(message.getBody());
            newMessage.setTime(message.getTime_string());
            newMessage.setStatus(2);
            newMessage.setMatches(false);
            newMessage.setUnread_count(1);
            baseAllList.add(0, newMessage);
            allList.add(0, newMessage);
            vipList.add(0, newMessage);
        } else {
            baseAllList.get(id).setLast_message(message.getBody());
            baseAllList.get(id).setTime(message.getTime_string());
            matchedList.get(id).setUnread_count(matchedList.get(id).getUnread_count() + 1);
            for (int i = 0; i < matchedList.size(); i++) {
                if (matchedList.get(i).getProfile_id().equals(message.getFrom())) {
                    matchedList.get(i).setLast_message(message.getBody());
                    matchedList.get(i).setTime(message.getTime_string());
                    matchedList.get(i).setUnread_count(matchedList.get(i).getUnread_count() + 1);
                }
            }
            for (int i = 0; i < allList.size(); i++) {
                if (allList.get(i).getProfile_id().equals(message.getFrom())) {
                    allList.get(i).setLast_message(message.getBody());
                    allList.get(i).setTime(message.getTime_string());
                    allList.get(i).setUnread_count(allList.get(i).getUnread_count() + 1);
                }
            }
            for (int i = 0; i < vipList.size(); i++) {
                if (vipList.get(i).getProfile_id().equals(message.getFrom())) {
                    vipList.get(i).setLast_message(message.getBody());
                    vipList.get(i).setTime(message.getTime_string());
                    vipList.get(i).setUnread_count(vipList.get(i).getUnread_count() + 1);
                }
            }
        }
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                SlidingMenuActivity.totalUnreadMessage += 1;
                if (SlidingMenuActivity.mNotificationTv != null) {
                    SlidingMenuActivity.mNotificationTv.setText("" + SlidingMenuActivity.totalUnreadMessage);
                    if (SlidingMenuActivity.totalUnreadMessage > 0) {
                        SlidingMenuActivity.mNotificationTv.setVisibility(View.VISIBLE);
                    } else {
                        SlidingMenuActivity.mNotificationTv.setVisibility(View.GONE);
                    }
                }
                if (adapterAllListChatData != null) {
                    adapterAllListChatData.notifyDataSetChanged();
                }
                if (adapterVIPListChatData != null) {
                    adapterVIPListChatData.notifyDataSetChanged();
                }
                if (adapterMatchListChatData != null) {
                    adapterMatchListChatData.notifyDataSetChanged();
                }
            }
        });
    }
    

    @Override
    protected void onPause(){
        System.gc();
        super.onPause();
    }

    @Override
    protected void onDestroy(){
        System.gc();
        super.onDestroy();
    }

}
