package com.oakclub.android.base;

import java.util.ArrayList;
import java.util.HashMap;

import com.oakclub.android.AllChatActivity;
import com.oakclub.android.ChatActivity;
import com.oakclub.android.MatchChatActivity;
import com.oakclub.android.R;
import com.oakclub.android.VIPActivity;
import com.oakclub.android.R.id;
import com.oakclub.android.R.layout;
import com.oakclub.android.core.RequestUI;
import com.oakclub.android.fragment.ListChatFragment;
import com.oakclub.android.helper.operations.ListChatOperation;
import com.oakclub.android.model.ListChatData;
import com.oakclub.android.model.ListChatReturnObject;
import com.oakclub.android.model.adaptercustom.AdapterListChat;
import com.oakclub.android.model.parse.ParseDataChatList;
import com.oakclub.android.net.OakClubApi;
import com.oakclub.android.util.Constants;
import com.oakclub.android.util.OakClubUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.AdapterView.OnItemClickListener;

public class ChatBaseActivity extends OakClubBaseActivity {

	@Override
	protected void onResume() {
		updateListChat(ChatBaseActivity.this);
		super.onResume();
	}

	ListView lvListChat;
	protected ProgressBar pbLoading;
	protected boolean isLoading= false;
	protected static final int CHAT_ALL = 0;
	protected static final int CHAT_MATCHES = 1;
	protected static final int CHAT_VIP = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (!OakClubUtil.isInternetAccess(ChatBaseActivity.this))
			return;
		setContentView(R.layout.activity_listview_listchat);
		lvListChat = (ListView) findViewById(R.id.activity_listview_listchat_lvchat);
		pbLoading = (ProgressBar) findViewById(R.id.activity_listview_listchat_pbLoading);

		final ListChatRequest request = new ListChatRequest("getListChat", this);
		getRequestQueue().addRequest(request);
		
		if(baseAllList == null){
			isLoading=true;
		}
		if(AllChatActivity.allList ==null && AllChatActivity.adapterAll ==null){
			AllChatActivity.allList = new ArrayList<ListChatData>();
			AllChatActivity.adapterAll = new AdapterListChat(this, AllChatActivity.allList);
		}
		if(MatchChatActivity.matchedList ==null && MatchChatActivity.adapterMatch ==null){
			MatchChatActivity.matchedList = new ArrayList<ListChatData>();
			MatchChatActivity.adapterMatch = new AdapterListChat(this, MatchChatActivity.matchedList);
		}
		if(VIPActivity.vipList ==null && VIPActivity.adapterVip==null){
			VIPActivity.vipList = new ArrayList<ListChatData>();
			VIPActivity.adapterVip = new AdapterListChat(this, VIPActivity.vipList);
		}
	}
	
	private ListChatOperation listChatDb;
	protected void init(int chatCase)
	{
		ArrayList<ListChatData> list = new ArrayList<ListChatData>();
		AdapterListChat adapterListChatData = new AdapterListChat(this, list);
		listChatDb = new ListChatOperation(ChatBaseActivity.this);

		AllChatActivity.allList.clear();
		AllChatActivity.allList.addAll(listChatDb.getListChat());
		AllChatActivity.adapterAll.notifyDataSetChanged();

		MatchChatActivity.matchedList.clear();
		MatchChatActivity.matchedList.addAll(listChatDb.getListMatch());
		MatchChatActivity.adapterMatch.notifyDataSetChanged();

		VIPActivity.vipList.clear();
		VIPActivity.vipList.addAll(listChatDb.getListVip());
		VIPActivity.adapterVip.notifyDataSetChanged();
		
		switch (chatCase) {
		case CHAT_ALL:
			list = AllChatActivity.allList;
			adapterListChatData = AllChatActivity.adapterAll;
			break;
		case CHAT_MATCHES:
			list = MatchChatActivity.matchedList;
			adapterListChatData = MatchChatActivity.adapterMatch;
			break;
		case CHAT_VIP:
			list = VIPActivity.vipList;
			adapterListChatData = VIPActivity.adapterVip;
			break;
		default:
			break;
		}
		final ArrayList<ListChatData> listAdapter = list;
		lvListChat.setAdapter(adapterListChatData);
		lvListChat.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long id) {
				Intent chatHistoryActivity = new Intent(ChatBaseActivity.this
						, ChatActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString(Constants.BUNDLE_PROFILE_ID,
						listAdapter.get(position).getProfile_id());
				bundle.putString(Constants.BUNDLE_AVATAR, listAdapter
						.get(position).getAvatar());
				bundle.putString(Constants.BUNDLE_NAME, listAdapter.get(position)
						.getName());
				bundle.putInt(Constants.BUNDLE_STATUS, listAdapter.get(position)
						.getStatus());
				bundle.putString(Constants.BUNDLE_MATCH_TIME,
						listAdapter.get(position).getMatch_time());
				bundle.putBoolean(Constants.BUNDLE_NOTI, false);
				
				chatHistoryActivity.putExtras(bundle);
				chatHistoryActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				startActivity(chatHistoryActivity);
			}
		});

		if(!isLoading){
			adapterListChatData.ignoreDisabled=true;
			pbLoading.setVisibility(View.GONE);
		}
	}
	
	protected class ListChatRequest extends RequestUI {
//		private ListChatReturnObject obj;

		public ListChatRequest(Object key, Activity activity) {
			super(key, activity);
		}

		@Override
		public void execute() throws Exception {
			setMap(oakClubApiTemp.getChatList());
//			obj = oakClubApi.getListChat();
		}

		@Override
		public void executeUI(Exception ex) {
//			if (obj == null || !obj.isStatus()) {
//			} else {
//				isLoading = false;
//				pbLoading.setVisibility(View.GONE);
//				listChatDb = new ListChatOperation(ChatBaseActivity.this);
//				listChatDb.deleteAllListChat();
//				for (int i = 0; i < obj.getData().size(); i++) {
//					listChatDb.insertListChat(obj.getData().get(i));
//				}
//				updateListChat(ChatBaseActivity.this);
//			}
			
			if(getMap()==null|| !getMap().get("errorCode").equals(0)){
				
			}
			else{
				HashMap<String, Object> object = (HashMap<String, Object>) getMap().get("data");
				ParseDataChatList parse = new ParseDataChatList(object);
				ArrayList<ListChatData> listData = new ArrayList<ListChatData>();
				listData = parse.getList();
				isLoading = false;
				pbLoading.setVisibility(View.GONE);
				listChatDb = new ListChatOperation(ChatBaseActivity.this);
				listChatDb.deleteAllListChat();
				for (ListChatData data : listData) {
					listChatDb.insertListChat(data);
				}
				updateListChat(ChatBaseActivity.this);
			}
		}

	}
	
	public static void updateListChat(Context context){
		if(baseAllList ==null){
			baseAllList = new ArrayList<ListChatData>();
		}
		
		if(AllChatActivity.allList ==null && AllChatActivity.adapterAll ==null){
			AllChatActivity.allList = new ArrayList<ListChatData>();
			AllChatActivity.adapterAll = new AdapterListChat(context, AllChatActivity.allList);
		}
		if(MatchChatActivity.matchedList ==null && MatchChatActivity.adapterMatch ==null){
			MatchChatActivity.matchedList = new ArrayList<ListChatData>();
			MatchChatActivity.adapterMatch = new AdapterListChat(context, MatchChatActivity.matchedList);
		}
		if(VIPActivity.vipList ==null && VIPActivity.adapterVip==null){
			VIPActivity.vipList = new ArrayList<ListChatData>();
			VIPActivity.adapterVip = new AdapterListChat(context, VIPActivity.vipList);
		}
		ListChatOperation listChatDb = new ListChatOperation(context);
		baseAllList = listChatDb.getListChat();
		
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
			
		try{
			SlidingMenuActivity.getTotalNotification(listChatDb.getTotalNotification());
		}
		catch(Exception ex)
		{
			
		}
	}
}
