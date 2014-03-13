package com.oakclub.android;

import java.util.ArrayList;

import android.os.Bundle;

import com.oakclub.android.base.ChatBaseActivity;
import com.oakclub.android.model.ListChatData;
import com.oakclub.android.model.adaptercustom.AdapterListChat;
import com.oakclub.android.util.OakClubUtil;

public class VIPActivity extends ChatBaseActivity {

	public static ArrayList<ListChatData> vipList;
	public static AdapterListChat adapterVip;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!OakClubUtil.isInternetAccess(VIPActivity.this))
            return;
        else{
        	init(CHAT_VIP);
        }
    }

}
