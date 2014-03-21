package com.oakclub.android.model.parse;

import java.util.ArrayList;
import java.util.HashMap;


import com.oakclub.android.model.ChatHistoryData;

@SuppressWarnings("unchecked")
public class ParseDataChatHistory {
	private ArrayList<HashMap<String, Object>> messages;
	private ArrayList<ChatHistoryData> list;
	public ParseDataChatHistory(HashMap<String, Object> data){
		messages = new ArrayList<HashMap<String,Object>>();
		if(data.containsKey("messages")){
			messages = (ArrayList<HashMap<String, Object>>) data.get("messages");
			setList(getChatList(messages));
		}
	}
	
	private ArrayList<ChatHistoryData> getChatList(ArrayList<HashMap<String, Object>> messages){
		ArrayList<ChatHistoryData> list = new ArrayList<ChatHistoryData>();
		if(messages==null || messages.size()<=0){
			return null;
		}
		for (HashMap<String, Object> message : messages) {
			ChatHistoryData data = new ChatHistoryData();
			data.setBody(message.get("content").toString());
			data.setTo(message.get("to").toString());
			data.setFrom(message.get("from").toString());
			data.setTime_string(message.get("time").toString());
			list.add(data);
		}
		return list;
	}

	public ArrayList<ChatHistoryData> getList() {
		return list;
	}

	public void setList(ArrayList<ChatHistoryData> list) {
		this.list = list;
	}
}
