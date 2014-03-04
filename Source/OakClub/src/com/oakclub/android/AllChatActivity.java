package com.oakclub.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.oakclub.android.base.ChatBaseActivity;
import com.oakclub.android.base.SlidingMenuActivity;
import com.oakclub.android.core.RequestUI;
import com.oakclub.android.fragment.ListChatFragment;
import com.oakclub.android.model.ListChatReturnObject;
import com.oakclub.android.model.adaptercustom.AdapterListChat;
import com.oakclub.android.util.Constants;
import com.oakclub.android.util.OakClubUtil;

public class AllChatActivity extends ChatBaseActivity {

	// ArrayList<ListChatData> arrayList;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (!OakClubUtil.isInternetAccess(AllChatActivity.this))
            return;
		else
		    init(adapterAllListChatData, allList, this, CHAT_ALL);
		
//		adapterAllListChatData = new AdapterListChat(this, allList);
//		lvListChat.setAdapter(adapterAllListChatData);
//		if ((ListChatFragment.searchEdt != null && ListChatFragment.searchEdt
//				.getText().toString().length() == 0)
//				&& (allList == null || allList.size() == 0)) {
//			final ListChatRequest request = new ListChatRequest("getListChat",
//					AllChatActivity.this);
//			getRequestQueue().addRequest(request);
//		}
//		lvListChat.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> adapter, View view,
//					int position, long id) {
//				ListChatFragment.listener.onRightMenuClickListener();
//				Intent chatHistoryActivity = new Intent(AllChatActivity.this
//						.getApplicationContext(), ChatActivity.class);
//				Bundle bundle = new Bundle();
//				bundle.putString(Constants.BUNDLE_PROFILE_ID,
//						allList.get(position).getProfile_id());
//				bundle.putString(Constants.BUNDLE_AVATAR, allList.get(position)
//						.getAvatar());
//				bundle.putString(Constants.BUNDLE_NAME, allList.get(position)
//						.getName());
//				chatHistoryActivity.putExtras(bundle);
//				chatHistoryActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//						| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//				startActivity(chatHistoryActivity);
//				// finish();
//			}
//		});
		// (new Handler()).postDelayed(new Runnable() {
		//
		// @Override
		// public void run() {
		// getRequestQueue().addRequest(request);
		// }
		// },2000);

	}

	
	
	public class ListChatRequest extends RequestUI {

		private ListChatReturnObject obj;
		public ListChatRequest(Object key, Activity activity) {
			super(key, activity);
		}

		@Override
		public void execute() throws Exception {
			// TODO Auto-generated method stub
			obj = oakClubApi.getListChat();
		}

		@Override
		public void executeUI(Exception ex) {
		    pbLoading.setVisibility(View.GONE);
			if (obj == null || !obj.isStatus()) {
				Toast.makeText(AllChatActivity.this,
						getString(R.string.abnormal_error_message),
						Toast.LENGTH_SHORT).show();
			} else {
				allList.clear();
				matchedList.clear();
				vipList.clear();
				allList.addAll(obj.getData());
				for (int i = 0; i < allList.size(); i++) {
					if (allList.get(i).isMatches()) {
						matchedList.add(allList.get(i));
					} 
					if (allList.get(i).isIs_vip()) {
						vipList.add(allList.get(i));
					}
				}
				// arrayList.addAll(obj.getData());
				adapterAllListChatData.notifyDataSetChanged();
				if (SlidingMenuActivity.listProfileSendMessage.isEmpty() || !SlidingMenuActivity.listProfileSendMessage.contains(ChatActivity.profile_id)) {
					SlidingMenuActivity.listProfileSendMessage.add(ChatActivity.profile_id);
				}
				
				if (SlidingMenuActivity.listProfileSendMessage.isEmpty()) {
					SlidingMenuActivity.mNotificationTv
					.setVisibility(View.GONE);
				} else {
					SlidingMenuActivity.mNotificationTv.setText(""
							+ SlidingMenuActivity.listProfileSendMessage.size());
						SlidingMenuActivity.mNotificationTv
								.setVisibility(View.VISIBLE);
				}
			}
		}

	}
}
