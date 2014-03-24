package com.oakclub.android.fragment;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oakclub.android.AllChatActivity;
import com.oakclub.android.ChatActivity;
import com.oakclub.android.InfoProfileOtherActivity;
import com.oakclub.android.MainActivity;
import com.oakclub.android.MatchChatActivity;
import com.oakclub.android.R;
import com.oakclub.android.SlidingActivity;
import com.oakclub.android.TutorialScreenActivity;
import com.oakclub.android.VIPActivity;
import com.oakclub.android.base.SlidingMenuActivity;
import com.oakclub.android.core.RequestUI;
import com.oakclub.android.helper.operations.ListChatOperation;
import com.oakclub.android.model.ChatHistoryData;
import com.oakclub.android.model.DataConfig;
import com.oakclub.android.model.GetConfigData;
import com.oakclub.android.model.GetSnapShot;
import com.oakclub.android.model.HangoutProfileOtherReturnObject;
import com.oakclub.android.model.ListChatData;
import com.oakclub.android.model.SetLikeMessageReturnObject;
import com.oakclub.android.model.SnapshotData;
import com.oakclub.android.model.adaptercustom.AdapterSnapShot;
import com.oakclub.android.model.adaptercustom.StickerScreenAdapter;
import com.oakclub.android.net.OakClubApi;
import com.oakclub.android.net.OnBootReceiver;
import com.oakclub.android.util.Constants;
import com.oakclub.android.util.OakClubUtil;
import com.oakclub.android.view.ProgressCircle;
import com.oakclub.android.view.SnapshotMain;

public class SnapshotFragment{

    private ImageButton btnNope;
    private ImageButton btnLike;
    private ImageButton btnInfo;
    private ImageButton btnChat;

    public FrameLayout fltBody;
    private LinearLayout lltSnapshotMain;
    private RelativeLayout progressFinder;
    private FrameLayout btnInvite;
    public String urlAvatar;

    private boolean isAction = false;

    protected static final int START_RECORD = 0;
    protected static final int LIMIT_RECORD = 35;
    protected static final int SEG_RECORD = 0;
    protected static final int ORIGIN_ROTATE = 25;
    protected String TAG_SNAPSHOT_ID = "snapshot_id";
    protected AdapterSnapShot arrayAdapter;
    protected GetSnapShot objSnapshot;
    protected int lengthSnap = 0;
    protected View touchCurrent = null;

    public ArrayList<SnapshotData> arrSnapshotData;
    public JSONArray jsonArray = null;
    private RelativeLayout progressCircle;

    private Handler handler;
    private Runnable runnable;

    private DataConfig dataConfig;
    private int counter = 0;
    private int RATEAPP = 70;

    
    SlidingActivity activity;
    public SnapshotFragment(SlidingActivity activity){
        this.activity = activity;
    }
    
    public void initSnapshot() {
    	
        activity.init(R.layout.activity_snapshot);
        activity.sendBroadcast(new Intent(activity, OnBootReceiver.class));
        
        if (ProfileSettingFragment.profileInfoObj != null) {
        	urlAvatar = OakClubUtil.getFullLink(activity, ProfileSettingFragment.profileInfoObj.getAvatar());
        } else {
        	Intent intent = new Intent(activity, MainActivity.class);
        	activity.startActivity(intent);
        	activity.finish();
        }
        init(START_RECORD);
    }
    
    private View findViewById(int id){
        return activity.view.findViewById(id);
    }
    
    Intent intent;
    ProgressCircle progCir;
    ProgressCircle progFin;
    private void init(int start) {
        arrSnapshotData = new ArrayList<SnapshotData>();
        arrSnapshotMain = new ArrayList<SnapshotMain>();
        
        progressCircle = (RelativeLayout) findViewById(R.id.activity_snapshot_flt_process);
        progCir = (ProgressCircle)progressCircle.findViewById(R.id.progress_snapshot_layout_flt_process);
        
        progressFinder = (RelativeLayout) findViewById(R.id.activity_snapshot_flt_process_finder);
        progFin = (ProgressCircle)progressFinder.findViewById(R.id.progress_snapshot_layout_flt_process_finder);
        
        fltBody = (FrameLayout) findViewById(R.id.activity_snapshot_flt_body);
        
        lltSnapshotMain = (LinearLayout) findViewById(R.id.activity_snapshot_llt_main);
        btnInvite = (FrameLayout) progressFinder
                .findViewById(R.id.progress_snapshot_layout_flt_footer_action_find_flt_invite);
        btnInvite.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT,
                        activity.getString(R.string.txt_share_title) + "\n"
                                + activity.getString(R.string.txt_share_url));
                intent.setType("text/plain");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
            }
        });

        btnNope = (ImageButton) findViewById(R.id.activity_snapshot_flt_footer_ibn_nope);
        btnNope.setOnClickListener(snapEvent);

        btnLike = (ImageButton) findViewById(R.id.activity_snapshot_flt_footer_ibn_like);
        btnLike.setOnClickListener(snapEvent);

        btnInfo = (ImageButton) findViewById(R.id.activity_snapshot_flt_footer_ibn_info);
        btnInfo.setOnClickListener(snapEvent);
        
        btnChat = (ImageButton) findViewById(R.id.activity_snapshot_flt_footer_ibn_chat);
        btnChat.setOnClickListener(snapEvent);
        
        if(ProfileSettingFragment.profileInfoObj != null && !ProfileSettingFragment.profileInfoObj.isIs_vip()){
        	btnChat.setBackgroundResource(R.drawable.vipchat_inactive);
        }
        getListSnapshotData(start);
        
        GetConfig loader = new GetConfig(Constants.GETCONFIG, activity);
        activity.getRequestQueue().addRequest(loader);
    }
    
    public void showTutorialActivity() {
        intent = new Intent(activity,
                TutorialScreenActivity.class);
        activity.startActivity(intent);
    }
    

    private static final int TIMER = 100;
    public void activityResult(int requestCode, int resultCode, Intent data){
        if (data != null && data.getExtras() != null
                && data.getExtras().containsKey("ACTION")) {
            final String action = data.getStringExtra("ACTION");
            handler = new Handler();
            if (Constants.ACTION_LIKE.equals(action)) {
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        getContentMain().setPressButtonAction(action);
                    }
                };
                handler.postDelayed(runnable, TIMER);

            } else if (Constants.ACTION_NOPE.equals(action)) {
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        getContentMain().setPressButtonAction(action);
                    }
                };
                handler.postDelayed(runnable, TIMER);
            }
        }
    }
    
    private OnClickListener snapEvent = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.activity_snapshot_flt_footer_ibn_like
                    && !isAction && getContentMain()!=null) {
                getContentMain().setPressButtonAction(Constants.ACTION_LIKE);
            } else if (v.getId() == R.id.activity_snapshot_flt_footer_ibn_info) {
                intent = new Intent(activity,
                        InfoProfileOtherActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                Bundle b = new Bundle();
                b.putSerializable("SnapshotDataBundle",
                        getContentMain().getData());
                b.putString("Snapshot", "SnapshotFragment");
                intent.putExtras(b);
                activity.startActivityForResult(intent, 1);
                System.gc();
            } else if (v.getId() == R.id.activity_snapshot_flt_footer_ibn_nope
                    && !isAction && getContentMain()!=null) {
                getContentMain().setPressButtonAction(Constants.ACTION_NOPE);
            }else if(v.getId() == R.id.activity_snapshot_flt_footer_ibn_chat){
            	SnapshotData chatAccount = getContentMain().getData();
                if(chatAccount != null){
                    if (ProfileSettingFragment.profileInfoObj != null) {
                        if(!ProfileSettingFragment.profileInfoObj.isIs_vip()){
                            Toast.makeText(activity, activity.getString(R.string.txt_non_VIP_message), Toast.LENGTH_SHORT).show();
                        }else{

//                            AddRosterEvent loader = new AddRosterEvent("addRoster", activity, chatAccount.getProfile_id());
//                            activity.getRequestQueue().addRequest(loader);
                            
                            Intent chatHistoryActivity = new Intent(activity.getApplicationContext(), ChatActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.BUNDLE_PROFILE_ID,
                                    chatAccount.getProfile_id());
                            bundle.putString(Constants.BUNDLE_AVATAR, chatAccount
                                    .getAvatar());
                            bundle.putString(Constants.BUNDLE_NAME, chatAccount
                                    .getName());
                            bundle.putBoolean(Constants.BUNDLE_NOTI, false);
                            bundle.putInt(Constants.BUNDLE_STATUS, 1);
                            bundle.putString(Constants.BUNDLE_MATCH_TIME, chatAccount.getLike_time());
                            
                            
                            chatHistoryActivity.putExtras(bundle);
                            chatHistoryActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                            activity.startActivity(chatHistoryActivity);
                        }
                    } else {
                        Intent intent = new Intent(activity, MainActivity.class);
                        activity.startActivity(intent);
                        activity.finish();
                    }
                }
            }
        }
    };
    

    private void showProgress() {
        progressCircle.setVisibility(View.VISIBLE);
        lltSnapshotMain.setVisibility(View.GONE);
    }

    private void hideProgress() {
        progressCircle.setVisibility(View.GONE);
        lltSnapshotMain.setVisibility(View.VISIBLE);
    }

    public void getListSnapshotData(int start) {
        if (activity.oakClubApi == null) {
            activity.oakClubApi = OakClubApi.createInstance(
                    activity.getApplicationContext(),
                    activity.getString(R.string.default_server_address));
        }
        showProgress();
        GetSnapShotLoader loader = new GetSnapShotLoader(
                Constants.GET_SNAPSHOT, activity,
                MainActivity.facebook_user_id, start, LIMIT_RECORD);
        activity.getRequestQueue().addRequest(loader);
    }

    public void setFavoriteSnapshot(String profileId, String isLike, boolean isLikedMe){
        if (activity.oakClubApi == null) {
            activity.oakClubApi = OakClubApi.createInstance(
                    activity.getApplicationContext(),
                    activity.getString(R.string.default_server_address));
        }
        
        SnapshotEvent snapEvent = new SnapshotEvent(
                Constants.SET_FAVORITE, activity,
                profileId, isLike, isLikedMe);
        activity.getRequestQueue().addRequest(snapEvent);
    }
    
    class GetConfig extends RequestUI {
        GetConfigData obj;

        public GetConfig(Object key, Activity activity) {
            super(key, activity);
        }

        @Override
        public void execute() throws Exception {
            obj = activity.oakClubApi.GetConfig();
        }

        @Override
        public void executeUI(Exception ex) {
            if (obj != null && obj.getData() != null) {
                dataConfig = obj.getData();
                HashMap<String, String> stickers = new HashMap<String, String>();
                for (int i = 0; i < obj.getData().getStickers().size(); i++) {
                    stickers.put(obj.getData().getStickers().get(i).getSymbol_name(), obj.getData().getStickers().get(i).getImage());
                }
                StickerScreenAdapter.stickers.add(stickers);
            }
            if (activity.isLoadListMutualMatch) {
            	activity.isLoadListMutualMatch = false;
            	activity.getSlidingMenu().showSecondaryMenu();
            }
        }
    }
    
    class SnapshotEvent extends RequestUI {
        private String proId = "";
        private String numberSet = "";
        private SetLikeMessageReturnObject resultEvent;
        private boolean isLike;
        protected ProgressDialog pd;

        public SnapshotEvent(Object key, Activity activity, String profileId,
                String numberSet, boolean isLikedMe) {
            super(key, activity);
            this.proId = profileId;
            this.numberSet = numberSet;
            this.isLike = isLikedMe;
            
            if (Constants.ACTION_LIKE.equals(numberSet) && isLikedMe) {
                pd = new ProgressDialog(activity);
                pd.setMessage(activity.getString(R.string.txt_loading));
                pd.setCancelable(false);
                pd.show();  
            }
        }

        @Override
        public void execute() throws Exception {
            resultEvent = activity.oakClubApi.SetFavoriteInSnapshot(proId, numberSet);
        }

        @Override
        public void executeUI(Exception ex) {
        	if (resultEvent != null && resultEvent.isStatus()) {
        		if (pd != null && pd.isShowing()) {
    				pd.dismiss();
    			}
        		counter++;
        		showDialogSnapshotCounter(activity);
                launchMarket();
        	} else {
        		AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(activity);
                final AlertDialog dialog = builder.create();
                LayoutInflater inflater = LayoutInflater
                        .from(activity);
                View layout = inflater.inflate(R.layout.dialog_warning_ok,
                        null);
                dialog.setView(layout, 0, 0, 0, 0);
                TextView tvTitle = (TextView)layout.findViewById(R.id.dialog_warning_lltheader_tvTitle);
                tvTitle.setText(activity.getString(R.string.txt_warning));
                TextView tvQuestion = (TextView)layout.findViewById(R.id.dialog_warning_tvQuestion);
                tvQuestion.setText(activity.getString(R.string.txt_internet_message));
                Button btnOK = (Button) layout
                        .findViewById(R.id.dialog_internet_access_lltfooter_btOK);
                btnOK.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        System.exit(0);
                        //getListSnapshotData(START_RECORD);
                    }
                });
                dialog.setCancelable(false);
                dialog.show();
            }
        }

    }
//    
//    public void setMutualMatch(String profileId){
//
//        if (activity.oakClubApi == null) {
//            activity.oakClubApi = OakClubApi.createInstance(
//                    activity.getApplicationContext(),
//                    activity.getString(R.string.default_server_address));
//        }
//        
//        SetViewMuatualEvent loader = new SetViewMuatualEvent(
//                Constants.SET_VIEW_MUTUAL_MATCH, activity,
//                profileId);
//        activity.getRequestQueue().addRequest(loader);
//    }
//    
//    class SetViewMuatualEvent extends RequestUI {
//        private String proId = "";
//
//        public SetViewMuatualEvent(Object key, Activity activity, String proId) {
//            super(key, activity);
//            this.proId = proId;
//        }
//
//        @Override
//        public void execute() throws Exception {
//            activity.oakClubApi.SetViewedMutualMatch(proId);
//        }
//
//        @Override
//        public void executeUI(Exception ex) {
//
//        }
//
//    }

    class AddRosterEvent extends RequestUI {
        private String profileId = "";

        public AddRosterEvent(Object key, Activity activity, String profileId) {
            super(key, activity);
            this.profileId = profileId;
        }

        @Override
        public void execute() throws Exception {
            setMap(activity.oakClubApiTemp.addRoster(profileId));
        }

        @Override
        public void executeUI(Exception ex) {
			if(getMap()==null|| !getMap().get("errorCode").equals(0)){
				
			}
        }

    }

    class GetSnapShotLoader extends RequestUI {
    	GetSnapShot obj;
        String userId;
        int startIndex;
        int limitIndex;

        public GetSnapShotLoader(Object key, Activity activity, String userId,
                int startIndex, int limitIndex) {
            super(key, activity);
            this.userId = userId;
            this.startIndex = startIndex;
            this.limitIndex = limitIndex;
        }

        @Override
        public void execute() throws Exception {
            obj = activity.oakClubApi.getSnapShot(userId, startIndex, limitIndex);
        }

        @Override
        public void executeUI(Exception ex) {
            if (obj != null && obj.getData() != null
                    && obj.getData().size() > 0) {
                hideProgress();
                counter = 0;
                objSnapshot = obj;
                loadDataSnapshot(obj.getData());
            } 
            else {
                progressCircle.setVisibility(View.GONE);
                progressFinder.setVisibility(View.VISIBLE);
            }
        }
    }
    
    public void updateListChat(String profileId){
        	ListChatOperation listChatDb = new ListChatOperation(activity);
        	if(!listChatDb.checkProfileExist(profileId)){
    			GetOtherProfile loader2 = new GetOtherProfile(
    					Constants.GET_HANGOUT_PROFILE, activity,
    					profileId);
    			activity.getRequestQueue().addRequest(loader2);
    		}
    }

	class GetOtherProfile extends RequestUI {

		HangoutProfileOtherReturnObject data = new HangoutProfileOtherReturnObject();
		String profile_id;
		ChatHistoryData message;

		public GetOtherProfile(Object key, Activity activity,
				String profile_id) {
			super(key, activity);
			this.profile_id = profile_id;
		}

		@Override
		public void execute() throws Exception {
			data = activity.oakClubApi.getHangoutProfileOther(profile_id);
		}

		@Override
		public void executeUI(Exception ex) {
			if (data != null) {
				ListChatData newMessage = new ListChatData();
				newMessage.setProfile_id(profile_id);
				newMessage.setName(data.getData().getName());
				newMessage.setAvatar(data.getData().getAvatar());
				newMessage.setLast_message("");
//				newMessage.setLast_message_time(DateFormat.getDateTimeInstance().format(new Date()));
				newMessage.setMatch_time(DateFormat.getDateTimeInstance().format(new Date()));
				newMessage.setLast_active_time(DateFormat.getDateTimeInstance().format(new Date()));
				newMessage.setStatus(0);
				newMessage.setMatches(true);
				newMessage.setUnread_count(0);
				
				ListChatOperation listChatDb = new ListChatOperation(activity);
				listChatDb.insertListChat(newMessage);
								
				AllChatActivity.allList.clear();
				AllChatActivity.allList.addAll(listChatDb.getListChat());
				AllChatActivity.adapterAll.ignoreDisabled=true;
				AllChatActivity.adapterAll.notifyDataSetChanged();

				MatchChatActivity.matchedList.clear();
				MatchChatActivity.matchedList.addAll(listChatDb.getListMatch());
				MatchChatActivity.adapterMatch.ignoreDisabled=true;
				MatchChatActivity.adapterMatch.notifyDataSetChanged();

				VIPActivity.vipList.clear();
				VIPActivity.vipList.addAll(listChatDb.getListVip());
				VIPActivity.adapterVip.ignoreDisabled=true;
				VIPActivity.adapterVip.notifyDataSetChanged();
					
				SlidingMenuActivity.getTotalNotification(listChatDb.getTotalNotification());
				
	            
			} else {
				OakClubUtil.enableDialogWarning(activity,
						activity.getString(R.string.txt_warning),
						activity.getString(R.string.txt_internet_message));
			}
		}
	}

    private SnapshotMain getContentMain(){
        SnapshotMain snapshot =null;
        if(fltBody.getChildCount()==1)
            snapshot = (SnapshotMain) fltBody.getChildAt(0);
        else if(fltBody.getChildCount()>=1)
            snapshot = (SnapshotMain) fltBody.getChildAt(1);
        else snapshot=null;
        return snapshot;
    }
    
    private ArrayList<SnapshotMain> arrSnapshotMain =null;
    private void loadDataSnapshot(ArrayList<SnapshotData> data){
        if(arrSnapshotMain.size()==0){
            int size =  data.size();
            for(int i =0; i< size;i++){
                SnapshotMain snapshotLayout = new SnapshotMain(activity, this,data.get(0));
                arrSnapshotMain.add(snapshotLayout);
                data.remove(0);
            }

            addDataIntoSnapshotLayout();
        }
    }

    public void addDataIntoSnapshotLayout() {
        if(fltBody.getChildCount()<=0 && arrSnapshotMain.size()<=0){
            showProgress();
            GetSnapShotLoader loader = new GetSnapShotLoader(
                    Constants.GET_SNAPSHOT, activity,
                    MainActivity.facebook_user_id, 0, LIMIT_RECORD);
            activity.getRequestQueue().addRequest(loader);
        }
        else if(arrSnapshotMain.size()==1){
            fltBody.addView(arrSnapshotMain.get(0));
            arrSnapshotMain.remove(0);
            fltBody.getChildAt(0).bringToFront();
        }
        else if(arrSnapshotMain.size()>=2){
            for(int i =fltBody.getChildCount(); i<2;i++){
                fltBody.addView(arrSnapshotMain.get(0));
                arrSnapshotMain.remove(0);
                fltBody.getChildAt(0).bringToFront();
                if(i==1){
                	handler = new Handler();
                	runnable = new Runnable() {
						@Override
						public void run() {
		                	fltBody.getChildAt(0).setVisibility(View.VISIBLE);
						}
					};
					handler.postDelayed(runnable, TIMER);
                	fltBody.getChildAt(0).setVisibility(View.INVISIBLE);
                }
            }            
        }
    }
    
    private void launchMarket() {
//    	objSnapshot.getSnapshot_counter() + counter == RATEAPP
    	if (objSnapshot.getSnapshot_counter() + counter == RATEAPP) {
    		AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(activity);
            final AlertDialog dialog = builder.create();
            LayoutInflater inflater = LayoutInflater.from(activity);
            View layout = inflater.inflate(R.layout.dialog_rate_app, null);
            dialog.setView(layout, 0, 0, 0, 0);
            
//            TextView tvTitle = (TextView)layout.findViewById(R.id.dialog_warning_lltheader_tvTitle);
//            tvTitle.setText(activity.getString(R.string.txt_warning));
            
//            TextView tvContent = (TextView)layout.findViewById(R.id.dialog_warning_tvContent);
//            tvContent.setText(String.format("Please, rate our app"));
            Button btRate = (Button) layout.findViewById(R.id.dialog_warning_lltfooter_btRate);
            Button btLeaveUsMessage = (Button) layout
                    .findViewById(R.id.dialog_warning_lltfooter_btLeave_us_message);
            Button btAskMeLater = (Button) layout
                    .findViewById(R.id.dialog_warning_lltfooter_btAsk_me_later);
            
            btRate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    dialog.dismiss();
                    Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
                    Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    try {
                        activity.startActivity(myAppLinkToMarket);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(activity, " unable to find market app", Toast.LENGTH_LONG).show();
                    }
                }
            });
            
            btLeaveUsMessage.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					intent = new Intent(Intent.ACTION_SEND);
	                intent.setType("message/rfc822");
	                intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{activity.getString(R.string.mail_server_address)});
	                intent.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.txt_subject_mail_contact));
	                intent.putExtra(Intent.EXTRA_TEXT   , "");
	                try {
	                    activity.startActivity(Intent.createChooser(intent, "Send mail..."));
	                } catch (android.content.ActivityNotFoundException ex) {
	                    //Toast.makeText(activity, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
	                }
					
				}
			});
            
            btAskMeLater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    dialog.dismiss();
                }
            });
            dialog.setCancelable(false);
            dialog.show();
        }
    }

	private void showDialogSnapshotCounter(final Activity activity) {
		if (dataConfig != null){
	        for (int i = 0; i < dataConfig.getConfigs().getSnapshot_counter().getInvite_friend().size(); i++) {
	            if (objSnapshot.getSnapshot_counter() + counter == dataConfig.getConfigs().getSnapshot_counter().getInvite_friend().get(i)) {
	                AlertDialog.Builder builder;
	                builder = new AlertDialog.Builder(activity);
	                final AlertDialog dialog = builder.create();
	                LayoutInflater inflater = LayoutInflater.from(activity);
	                View layout = inflater.inflate(R.layout.dialog_warning, null);
	                dialog.setView(layout, 0, 0, 0, 0);
	                
	                TextView tvTitle = (TextView)layout.findViewById(R.id.dialog_warning_lltheader_tvTitle);
	                tvTitle.setText(activity.getString(R.string.txt_warning));
	                
	                TextView tvContent = (TextView)layout.findViewById(R.id.dialog_warning_tvQuestion);
	                tvContent.setText(String.format(activity.getString(R.string.txt_you_rated_snapshots), dataConfig.getConfigs().getSnapshot_counter().getInvite_friend().get(i)));
	                Button btOk = (Button) layout.findViewById(R.id.dialog_warning_lltfooter_btOK);
	                btOk.setText(activity.getString(R.string.txt_tell_your_friend));
	                Button btCancel = (Button) layout
	                        .findViewById(R.id.dialog_warning_lltfooter_btCancel);
	                
	                btOk.setOnClickListener(new View.OnClickListener() {
	                    @Override
	                    public void onClick(View arg0) {
	                        dialog.dismiss();
	                        intent = new Intent();
	                        intent.setAction(Intent.ACTION_SEND);
	                        intent.putExtra(Intent.EXTRA_TEXT,
	                                activity.getString(R.string.txt_share_title) + "\n"
	                                        + activity.getString(R.string.txt_share_url));
	                        intent.setType("text/plain");
	                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
	                                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
	                                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
	                        activity.startActivity(intent);
	                    }
	                });
	                btCancel.setOnClickListener(new View.OnClickListener() {
	                    @Override
	                    public void onClick(View arg0) {
	                        dialog.dismiss();
	                    }
	                });
	                dialog.setCancelable(false);
	                dialog.show();
	            }
	        }
		}
    }
}
