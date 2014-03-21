package com.oakclub.android.model.parse;

import java.util.ArrayList;
import java.util.HashMap;


import com.oakclub.android.model.ListChatData;

@SuppressWarnings("unchecked")
public class ParseDataChatList {
	private ArrayList<HashMap<String, Object>> rosters;
	private ArrayList<ListChatData> list;
	public ParseDataChatList(HashMap<String, Object> data){
		rosters = new ArrayList<HashMap<String,Object>>();
		if(data.containsKey("rosters")){
			rosters = (ArrayList<HashMap<String, Object>>) data.get("rosters");
			setList(getChatList(rosters));
		}
	}
	
	private ArrayList<ListChatData> getChatList(ArrayList<HashMap<String, Object>> rosters){
		ArrayList<ListChatData> list = new ArrayList<ListChatData>();
		if(rosters==null || rosters.size()<=0){
			return null;
		}
		for (HashMap<String, Object> roster : rosters) {
			ListChatData data = new ListChatData();
			data.setAvatar(roster.get("avatar").toString());
			data.setFid("");
			data.setIs_vip(Boolean.parseBoolean(roster.get("is_vip").toString()));
			data.setLast_active_time(roster.get("last_active_time").toString());
			data.setName(roster.get("name").toString());
			data.setProfile_id(roster.get("profile_id").toString());
			data.setUnread_count(Integer.parseInt(roster.get("unread_num").toString()));

			HashMap<String, Object> lastMessage = (HashMap<String, Object>)roster.get("last_message");
			data.setLast_message(lastMessage.get("message").toString());
			data.setLast_message_time(lastMessage.get("time").toString());

			HashMap<String, Object> mutualMatch = (HashMap<String, Object>)roster.get("mutual_match");
			data.setMatch_time(mutualMatch.get("time").toString());
			data.setMatches(Boolean.parseBoolean(mutualMatch.get("is_matched").toString()));
			data.setChatted(Boolean.parseBoolean(mutualMatch.get("chat_flag").toString()));
			if(!data.getLast_message_time().equals("")){
				data.setStatus(data.getUnread_count()>0?2:3);
			}else if(data.isMatches())
				data.setStatus(data.isChatted()?0:1);
			else data.setStatus(4);
			list.add(data);
		}
		return list;
	}

	public ArrayList<ListChatData> getList() {
		return list;
	}

	public void setList(ArrayList<ListChatData> list) {
		this.list = list;
	}
}
