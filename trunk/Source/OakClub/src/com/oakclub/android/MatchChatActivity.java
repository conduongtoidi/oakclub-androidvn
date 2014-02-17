package com.oakclub.android;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.oakclub.android.base.ChatBaseActivity;
import com.oakclub.android.base.SlidingMenuActivity;
import com.oakclub.android.core.RequestUI;
import com.oakclub.android.fragment.ListChatFragment;
import com.oakclub.android.model.ListChatData;
import com.oakclub.android.model.ListChatReturnObject;
import com.oakclub.android.model.adaptercustom.AdapterListChat;
import com.oakclub.android.util.Constants;

public class MatchChatActivity extends ChatBaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!isInternetAccess())
            return;
        else
            init(adapterMatchListChatData, matchedList, this, CHAT_MATCHES);
		
//		if (matchedList==null || matchedList.size()==0){
//			matchedList = new ArrayList<ListChatData>();
//			vipList = new ArrayList<ListChatData>();
//			allList = new ArrayList<ListChatData>();
//			baseAllList = new ArrayList<ListChatData>();
//			final ListChatRequest request = new ListChatRequest("getListChat",
//					MatchChatActivity.this);
//			getRequestQueue().addRequest(request);
//		}
//		adapterMatchListChatData = new AdapterListChat(this, matchedList);
//		lvListChat.setAdapter(adapterMatchListChatData);
//		lvListChat.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> adapter, View view,
//					int position, long id) {
//				ListChatFragment.listener.onRightMenuClickListener();
//				Intent chatHistoryActivity = new Intent(MatchChatActivity.this
//						.getApplicationContext(), ChatActivity.class);
//				Bundle bundle = new Bundle();
//				bundle.putString(Constants.BUNDLE_PROFILE_ID,
//						matchedList.get(position).getProfile_id());
//				bundle.putString(Constants.BUNDLE_AVATAR,
//						matchedList.get(position).getAvatar());
//				bundle.putString(Constants.BUNDLE_NAME,
//						matchedList.get(position).getName());
//				chatHistoryActivity.putExtras(bundle);
//				chatHistoryActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//						| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//				startActivity(chatHistoryActivity);
//				// finish();
//			}
//		});
	}

	public class ListChatRequest extends RequestUI {

		public ListChatReturnObject obj;

		public ListChatRequest(Object key, Activity activity) {
			super(key, activity);

		}

		@Override
		public void execute() throws Exception {
			obj = oakClubApi.getListChat();
		}

		@Override
		public void executeUI(Exception ex) {
            pbLoading.setVisibility(View.GONE);
			if (obj == null || !obj.isStatus()) {
//				Toast.makeText(MatchChatActivity.this,
//						getString(R.string.abnormal_error_message),
//						Toast.LENGTH_SHORT).show();
			} else {
				allList.clear();
				matchedList.clear();
				vipList.clear();
				baseAllList.clear();
				for (int i = 0; i < obj.getData().size(); i++) {
					allList.add(obj.getData().get(i));
					baseAllList.add(obj.getData().get(i));
					if (baseAllList.get(i).isMatches()) {
						matchedList.add(baseAllList.get(i));
					} 
					if(baseAllList.get(i).isIs_vip()) {
						vipList.add(baseAllList.get(i));
					}
				}
				// arrayList.addAll(obj.getData());
				adapterMatchListChatData.notifyDataSetChanged();
				int count = 0 ; 
				for (int i = 0 ; i < baseAllList.size();i++){
					count = count+baseAllList.get(i).getUnread_count();
				}
				SlidingMenuActivity.totalUnreadMessage = count;
				if (SlidingMenuActivity.mNotificationTv!=null){
					SlidingMenuActivity.mNotificationTv.setText(""+SlidingMenuActivity.totalUnreadMessage);
					if (SlidingMenuActivity.totalUnreadMessage>0){
						SlidingMenuActivity.mNotificationTv.setVisibility(View.VISIBLE);	
					} else {
						SlidingMenuActivity.mNotificationTv.setVisibility(View.GONE);
					}
				}
			}
		}

	}
}
