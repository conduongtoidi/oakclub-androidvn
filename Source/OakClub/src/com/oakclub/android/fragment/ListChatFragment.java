package com.oakclub.android.fragment;


import java.util.ArrayList;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.oakclub.android.AllChatActivity;
import com.oakclub.android.MatchChatActivity;
import com.oakclub.android.VIPActivity;
import com.oakclub.android.R;
import com.oakclub.android.base.ChatBaseActivity;
import com.oakclub.android.base.OakClubBaseActivity;
import com.oakclub.android.base.SlidingMenuActivity;
import com.oakclub.android.core.IRightMenuOnClickListener;
import com.oakclub.android.model.ListChatData;
import com.oakclub.android.model.adaptercustom.AdapterListChat;

@SuppressWarnings("deprecation")
public class ListChatFragment extends Fragment {

	TabHost tabHost;
	public static EditText searchEdt;
    LocalActivityManager  mLocalActivityManager;
    public static IRightMenuOnClickListener listener;
    
    private TextWatcher tw = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	View rootView = inflater.inflate(R.layout.activity_list_chat, container,false);
        tabHost =  (TabHost)rootView.findViewById(android.R.id.tabhost);
        searchEdt = (EditText)rootView.findViewById(R.id.search_editext);
        listener = new IRightMenuOnClickListener() {
			@Override
			public void onRightMenuClickListener() {
				SlidingMenuActivity activity = (SlidingMenuActivity)getActivity();
				if (activity!=null && activity.getSlidingMenu()!=null){
					activity.getSlidingMenu().toggle();
				}
			}
		};
        return rootView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
//       tabHost =  (TabHost)findViewById(android.R.id.tabhost);
        mLocalActivityManager = new LocalActivityManager(getActivity(), false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);
        tabHost.setup(mLocalActivityManager);
        tabHost.getTabWidget().setDividerDrawable(null);
        // Tab for Matched
        TabSpec matchedspec = tabHost.newTabSpec("Matched");
        View tabView1 = createTabView(getActivity(), getString(R.string.txt_matched));
        matchedspec.setIndicator(tabView1);
        Intent matchedIntent = new Intent(getActivity(), MatchChatActivity.class);
        matchedspec.setContent(matchedIntent);
        tabHost.addTab(matchedspec); 
        
        // Tab for Non-matched
        TabSpec nonMatchedspec = tabHost.newTabSpec("VIP");
        View tabView2 = createTabView(getActivity(),getString(R.string.txt_vips));
        nonMatchedspec.setIndicator(tabView2);
        Intent nonMatchedIntent = new Intent(getActivity(), VIPActivity.class);
        nonMatchedspec.setContent(nonMatchedIntent);
        tabHost.addTab(nonMatchedspec); 
        
        // Tab for All
        TabSpec allspec = tabHost.newTabSpec("All");
        View tabView3 = createTabView(getActivity(), getString(R.string.txt_all));
        allspec.setIndicator(tabView3);
        Intent allIntent = new Intent(getActivity(), AllChatActivity.class);
        allspec.setContent(allIntent);
        tabHost.addTab(allspec); 

        TextView tvLeft = (TextView) tabHost.getTabWidget().getChildAt(0).findViewById(R.id.tabTitleText);
        tvLeft.setGravity(Gravity.CENTER);
        tvLeft.setBackgroundResource(R.drawable.tab_right_selector);
        TextView tvMiddle = (TextView) tabHost.getTabWidget().getChildAt(1).findViewById(R.id.tabTitleText);
        tvMiddle.setGravity(Gravity.CENTER);
        tvMiddle.setBackgroundResource(R.drawable.tab_middle_selector);
        TextView tvRight = (TextView) tabHost.getTabWidget().getChildAt(2).findViewById(R.id.tabTitleText);
        tvRight.setGravity(Gravity.CENTER);
        tvRight.setBackgroundResource(R.drawable.tab_left_selector);

        tabHost.setOnTabChangedListener(new OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                TextView tvLeft = (TextView) tabHost.getTabWidget().getChildAt(0).findViewById(R.id.tabTitleText);
                tvLeft.setBackgroundResource(R.drawable.tab_right_selector);
                TextView tvMiddle = (TextView) tabHost.getTabWidget().getChildAt(1).findViewById(R.id.tabTitleText);
                tvMiddle.setBackgroundResource(R.drawable.tab_middle_selector);
                TextView tvRight = (TextView) tabHost.getTabWidget().getChildAt(2).findViewById(R.id.tabTitleText);
                tvRight.setBackgroundResource(R.drawable.tab_left_selector);
            }
        });
        searchChat();
        
    }
    
    
    private void searchChat(){
        tw = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                changeText();
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                    int arg2, int arg3) {
                
            }

            @Override
            public void onTextChanged(CharSequence str, int start, int before,
                    int count) {
                searchEdt.removeTextChangedListener(tw);
                changeText();
                searchEdt.addTextChangedListener(tw);
            }
        };
        searchEdt.addTextChangedListener(tw);
    }
    
    private void changeText(){
    	new Handler(Looper.getMainLooper()).post(new Runnable() {
			
			@Override
			public void run() {
				ArrayList<ListChatData> baseListChat =new ArrayList<ListChatData>();
				ChatBaseActivity currentActivity = (ChatBaseActivity) mLocalActivityManager.getActivity( tabHost.getCurrentTabTag());
				for(int i= 0;i<currentActivity.getLvListChat().getChildCount();i++){
					ListChatData data = (ListChatData) currentActivity.getLvListChat().getItemAtPosition(i);
					baseListChat.add(data);
				}
				
				String str = searchEdt.getText().toString().toLowerCase();		
		        if (OakClubBaseActivity.baseAllList!=null){
		            MatchChatActivity.matchedList.clear();
		            VIPActivity.vipList.clear();
		            AllChatActivity.allList.clear();
		            for (int i = 0 ; i<OakClubBaseActivity.baseAllList.size();i++){
		                String str1=OakClubBaseActivity.baseAllList.get(i).getName().toLowerCase();
		                if (str==null || str.length()==0 || str1.contains(str)){
		                	AllChatActivity.allList.add(OakClubBaseActivity.baseAllList.get(i));
		                    if (OakClubBaseActivity.baseAllList.get(i).isMatches()){
		                    	MatchChatActivity.matchedList.add(OakClubBaseActivity.baseAllList.get(i));
		                    }
		                    if (OakClubBaseActivity.baseAllList.get(i).isIs_vip()){
		                    	VIPActivity.vipList.add(OakClubBaseActivity.baseAllList.get(i));
		                    }
		                }
		            }
		            
		            if (MatchChatActivity.adapterMatch!=null){
		            	MatchChatActivity.adapterMatch.notifyDataSetChanged();
		            }
		            if (VIPActivity.adapterVip!=null){
		                VIPActivity.adapterVip.notifyDataSetChanged();
		            }

		            if (AllChatActivity.adapterAll!=null){
		            	AllChatActivity.adapterAll.notifyDataSetChanged();
		            }
		        }
				
			}
		});
        
    }
    
    private static View createTabView(Context context, String tabText) {
        View view = LayoutInflater.from(context).inflate(R.layout.tabhost_list_chat, null, false);
        TextView tv = (TextView) view.findViewById(R.id.tabTitleText);
        tv.setText(tabText);
        return view;
    }
}
