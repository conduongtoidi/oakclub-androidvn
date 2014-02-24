package com.oakclub.android.base;

import java.util.ArrayList;

import com.oakclub.android.ChatActivity;
import com.oakclub.android.R;
import com.oakclub.android.AllChatActivity.ListChatRequest;
import com.oakclub.android.R.id;
import com.oakclub.android.R.layout;
import com.oakclub.android.core.RequestUI;
import com.oakclub.android.fragment.ListChatFragment;
import com.oakclub.android.model.ListChatData;
import com.oakclub.android.model.ListChatReturnObject;
import com.oakclub.android.model.adaptercustom.AdapterListChat;
import com.oakclub.android.util.Constants;
import com.oakclub.android.util.OakClubUtil;

import android.app.Activity;
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

	ListView lvListChat;
	protected ProgressBar pbLoading;

	private static ArrayList<ListChatData> listChat;
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
		if (matchedList == null)
			matchedList = new ArrayList<ListChatData>();
		if (vipList == null)
			vipList = new ArrayList<ListChatData>();
		if (allList == null)
			allList = new ArrayList<ListChatData>();
		if (baseAllList == null)
			baseAllList = new ArrayList<ListChatData>();
	}

	protected void init(AdapterListChat adapterListChatData,
			final ArrayList<ListChatData> listChat, final Activity activity,
			int chatCase) {
		listChat.clear();
		adapterListChatData = new AdapterListChat(this, listChat);
		switch (chatCase) {
		case CHAT_ALL:
			OakClubBaseActivity.adapterAllListChatData = adapterListChatData;
			break;
		case CHAT_MATCHES:
			OakClubBaseActivity.adapterMatchListChatData = adapterListChatData;
			break;
		case CHAT_VIP:
			OakClubBaseActivity.adapterVIPListChatData = adapterListChatData;
			break;

		default:
			break;
		}
		lvListChat.setAdapter(adapterListChatData);
		if ((ListChatFragment.searchEdt != null && ListChatFragment.searchEdt
				.getText().toString().length() == 0)
				&& (listChat == null || listChat.size() == 0)) {
			final ListChatRequest request = new ListChatRequest("getListChat",
					adapterListChatData, listChat, activity, chatCase);
			getRequestQueue().addRequest(request);
		} else
			pbLoading.setVisibility(View.GONE);
		lvListChat.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long id) {
				// ListChatFragment.listener.onRightMenuClickListener();
				Intent chatHistoryActivity = new Intent(activity
						.getApplicationContext(), ChatActivity.class);
				Bundle bundle = new Bundle();
				ImageView img = (ImageView) view
						.findViewById(R.id.item_listview_listchat_imgright);
//				Integer integer = (Integer) img.getTag();
//				integer = integer == null ? 0 : integer;
//				switch (integer) {
//				case R.drawable.matchup_on:
//					img.setImageResource(R.drawable.matchup_off);
//					img.setTag(R.drawable.matchup_off);
//					break;
//				case R.drawable.chat_up_on:
//					img.setImageResource(R.drawable.chat_up_off);
//					img.setTag(R.drawable.chat_up_off);
//					break;
//				}
				//
				bundle.putString(Constants.BUNDLE_PROFILE_ID,
						listChat.get(position).getProfile_id());
				bundle.putString(Constants.BUNDLE_AVATAR, listChat
						.get(position).getAvatar());
				bundle.putString(Constants.BUNDLE_NAME, listChat.get(position)
						.getName());
				bundle.putInt(Constants.BUNDLE_STATUS, listChat.get(position)
						.getStatus());
				bundle.putString(Constants.BUNDLE_MATCH_TIME,
						listChat.get(position).getMatch_time());
				bundle.putBoolean(Constants.BUNDLE_NOTI, false);
				
				switch (listChat.get(position).getStatus()) {
				case 0:
					listChat.get(position).setStatus(1);
					break;
				case 2:
					listChat.get(position).setStatus(3);
				default:
					break;
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
				
				
				chatHistoryActivity.putExtras(bundle);
				chatHistoryActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				startActivity(chatHistoryActivity);
				// finish();
			}
		});

	}

	protected class ListChatRequest extends RequestUI {
		private ListChatReturnObject obj;
		private AdapterListChat adapterListChatData;
		private ArrayList<ListChatData> listChat;
		private int chat;

		public ListChatRequest(Object key, AdapterListChat adapterListChatData,
				final ArrayList<ListChatData> listChat, Activity activity,
				int chatCase) {
			super(key, activity);
			this.adapterListChatData = adapterListChatData;
			this.listChat = listChat;
			this.chat = chatCase;
		}

		@Override
		public void execute() throws Exception {
			obj = oakClubApi.getListChat();
		}

		@Override
		public void executeUI(Exception ex) {
			pbLoading.setVisibility(View.GONE);
			if (obj == null || !obj.isStatus()) {
				// Toast.makeText(MatchChatActivity.this,
				// getString(R.string.abnormal_error_message),
				// Toast.LENGTH_SHORT).show();
			} else {
				allList.clear();
				matchedList.clear();
				vipList.clear();
				baseAllList.clear();
				for (int i = 0; i < obj.getData().size(); i++) {
					allList.add(obj.getData().get(i));
					baseAllList.add(obj.getData().get(i));
					SharedPreferences.Editor editor = getSharedPreferences(
							Constants.PREFERENCE_NAME, MODE_PRIVATE).edit();
					editor.putString(baseAllList.get(i).getProfile_id(),
							baseAllList.get(i).getName());
					editor.putInt(baseAllList.get(i).getName(), i);
					editor.commit();
					if (baseAllList.get(i).isMatches()) {
						matchedList.add(baseAllList.get(i));
					}
					if (baseAllList.get(i).isIs_vip()) {
						vipList.add(baseAllList.get(i));
					}
				}
				// arrayList.addAll(obj.getData());
				adapterListChatData.notifyDataSetChanged();
				// switch (chat) {
				// case CHAT_ALL :
				// adapterListChatData.notifyDataSetChanged();
				// break;
				// case CHAT_MATCHES :
				// adapterMatchListChatData.notifyDataSetChanged();
				// break;
				// case CHAT_VIP:
				// adapterVIPListChatData.notifyDataSetChanged();
				// break;
				// }
				int count = 0;
				for (int i = 0; i < baseAllList.size(); i++) {
					count = count + baseAllList.get(i).getUnread_count();
				}
				SlidingMenuActivity.totalUnreadMessage = count;
				if (SlidingMenuActivity.mNotificationTv != null) {
					SlidingMenuActivity.mNotificationTv.setText(""
							+ SlidingMenuActivity.totalUnreadMessage);
					if (SlidingMenuActivity.totalUnreadMessage > 0) {
						SlidingMenuActivity.mNotificationTv
								.setVisibility(View.VISIBLE);
					} else {
						SlidingMenuActivity.mNotificationTv
								.setVisibility(View.GONE);
					}
				}
			}
		}

	}
}
