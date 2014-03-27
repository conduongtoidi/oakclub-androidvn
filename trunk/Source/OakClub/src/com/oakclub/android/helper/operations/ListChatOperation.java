package com.oakclub.android.helper.operations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.oakclub.android.helper.DatabaseHelper;
import com.oakclub.android.model.ListChatData;

public class ListChatOperation {
	// Database fields
	private DatabaseHelper dbHelper;
	private SQLiteDatabase database;

	//Table Setting Name
    public static final String TABLE_LIST_CHAT = "list_chat";
    
    //Attribute Table Setting
//    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_AVATAR = "avatar"; 
    private static final String KEY_AGE = "age"; 
    private static final String KEY_CITY = "city"; 
    private static final String KEY_PROFILE_ID = "profile_id";
    private static final String KEY_XMPP_USER = "xmpp_username";
    private static final String KEY_JID = "jid";
    private static final String KEY_STATUS = "status";
    private static final String KEY_VIP = "is_vip";
    private static final String KEY_LAST_MESSAGE = "last_message";
    private static final String KEY_LAST_MESSAGE_TIME = "last_message_time";
    private static final String KEY_IS_MATCH = "matches";
    private static final String KEY_MATCH_TIME = "match_time";
    private static final String KEY_ACTIVE_TIME = "last_active_time";
    private static final String KEY_FID = "fid"; 
    private static final String KEY_UNREAD_COUNT = "unread_count"; 
    
    
    //Query Create Table Setting
    public static final String CREATE_TABLE_LIST_CHAT = "" +
    		"CREATE TABLE "+ TABLE_LIST_CHAT + "(" + 
			KEY_PROFILE_ID + " TEXT PRIMARY KEY, "+
//			KEY_ID + " INTEGER PRIMARY KEY," + 
			KEY_NAME + " TEXT, " +
			KEY_AVATAR + " TEXT, "+
//			KEY_AGE + "INTEGER, "+
//			KEY_CITY + "TEXT, "+
//			KEY_XMPP_USER + "TEXT, "+
//			KEY_JID + "TEXT, "+
			KEY_STATUS + " INTEGER, "+
			KEY_VIP + " BOOLEAN, "+
			KEY_LAST_MESSAGE + " DATETIME, "+
			KEY_LAST_MESSAGE_TIME + " DATETIME, "+
			KEY_IS_MATCH + " BOOLEAN, "+
			KEY_MATCH_TIME + " DATETIME, "+
			KEY_FID + " TEXT, "+
			KEY_UNREAD_COUNT + " INTEGER, " +
			KEY_ACTIVE_TIME + " DATETIME "+ ")";

    public ListChatOperation(Context context) {
        dbHelper = new DatabaseHelper(context);
    }
    	
    private void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }
 
    private void close() {
        dbHelper.close();
    }
    
    public void insertListChat(ListChatData data) {
		this.open();
		ContentValues values = new ContentValues();
		values.put(KEY_PROFILE_ID, data.getProfile_id());
		values.put(KEY_NAME, data.getName());
		values.put(KEY_AVATAR, data.getAvatar());
//		values.put(KEY_XMPP_USER, data.getProfile_id());
		values.put(KEY_STATUS, data.getStatus());
		values.put(KEY_VIP, data.isIs_vip());
		values.put(KEY_LAST_MESSAGE, data.getLast_message());
		values.put(KEY_LAST_MESSAGE_TIME, data.getLast_message_time());
		values.put(KEY_IS_MATCH, data.isMatches());
		values.put(KEY_MATCH_TIME, data.getMatch_time());
		values.put(KEY_FID, data.getFid());
		values.put(KEY_UNREAD_COUNT, data.getUnread_count());
		values.put(KEY_ACTIVE_TIME, data.getLast_active_time());
		database.insert(TABLE_LIST_CHAT, null, values);
		this.close();
	}    
    
    public int updateListChat(ListChatData data) {
		this.open();
		ContentValues values = new ContentValues();
		values.put(KEY_PROFILE_ID, data.getProfile_id());
		values.put(KEY_NAME, data.getName());
		values.put(KEY_AVATAR, data.getAvatar());
//		values.put(KEY_XMPP_USER, data.getProfile_id());
		values.put(KEY_STATUS, data.getStatus());
		values.put(KEY_VIP, data.isIs_vip());
		values.put(KEY_LAST_MESSAGE, data.getLast_message());
		values.put(KEY_LAST_MESSAGE_TIME, data.getLast_message_time());
		values.put(KEY_IS_MATCH, data.isMatches());
		values.put(KEY_MATCH_TIME, data.getMatch_time());
		values.put(KEY_FID, data.getFid());
		values.put(KEY_UNREAD_COUNT, data.getUnread_count());
		values.put(KEY_ACTIVE_TIME, data.getLast_active_time());
	    int result = database.update(TABLE_LIST_CHAT, values, KEY_PROFILE_ID + " = ?", new String[] { data.getProfile_id()});
	    this.close();
		return result;
	}

	public void deleteListChat(String id) {
		this.open();
		String deleteQuery = "DELETE FROM "+TABLE_LIST_CHAT+" where "+KEY_PROFILE_ID+ "='"+id+"'";
		database.execSQL(deleteQuery);
		this.close();
	}
	
	public void deleteAllListChat() {
		this.open();
		String deleteQuery = "DELETE FROM "+TABLE_LIST_CHAT;
		database.execSQL(deleteQuery);
		this.close();
	}

	public ArrayList<ListChatData> getListChat() {
		ArrayList<ListChatData> listChat = new ArrayList<ListChatData>();
		String selectQuery = "SELECT * FROM "+ TABLE_LIST_CHAT +" ORDER BY "+KEY_VIP+" DESC , DATE("+KEY_ACTIVE_TIME+") DESC ";
		this.open();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	        do {
	        	ListChatData data = new ListChatData();
	        	data.setProfile_id(cursor.getString(0));
	        	data.setName(cursor.getString(1));
	        	data.setAvatar(cursor.getString(2));
	        	data.setStatus(cursor.getInt(3));
	        	data.setIs_vip(cursor.getInt(4)==0?false:true);
	        	data.setLast_message(cursor.getString(5));
	        	data.setLast_message_time(cursor.getString(6));
	        	data.setMatches(cursor.getInt(7)==0?false:true);
	        	data.setMatch_time(cursor.getString(8));
	        	data.setFid(cursor.getString(9));
	        	data.setUnread_count(cursor.getInt(10));
	        	data.setLast_active_time(cursor.getString(11));
	        	listChat.add(data);
	        } while (cursor.moveToNext());
	    }
		this.close();
	    return listChat;
	}
	
	public ListChatData getChatData(String profileId){
		ListChatData data = new ListChatData();
		String selectQuery = "SELECT * FROM "+ TABLE_LIST_CHAT + " WHERE "+KEY_PROFILE_ID+" ='"+profileId+"'";
		this.open();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	    	data.setProfile_id(cursor.getString(0));
        	data.setName(cursor.getString(1));
        	data.setAvatar(cursor.getString(2));
        	data.setStatus(cursor.getInt(3));
        	data.setIs_vip(cursor.getInt(4)==0?false:true);
        	data.setLast_message(cursor.getString(5));
        	data.setLast_message_time(cursor.getString(6));
        	data.setMatches(cursor.getInt(7)==0?false:true);
        	data.setMatch_time(cursor.getString(8));
        	data.setFid(cursor.getString(9));
        	data.setUnread_count(cursor.getInt(10));
        	data.setLast_active_time(cursor.getString(11));
	    }
		this.close();
		return data;
	}

	public ArrayList<ListChatData> getListMatch() {
		ArrayList<ListChatData> listChat = new ArrayList<ListChatData>();
		String selectQuery = "SELECT * FROM "+ TABLE_LIST_CHAT + " WHERE "+KEY_IS_MATCH+ " = '1'"  +"ORDER BY DATE("+KEY_ACTIVE_TIME+") DESC";
		this.open();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	        do {
	        	ListChatData data = new ListChatData();
	        	data.setProfile_id(cursor.getString(0));
	        	data.setName(cursor.getString(1));
	        	data.setAvatar(cursor.getString(2));
	        	data.setStatus(cursor.getInt(3));
	        	data.setIs_vip(cursor.getInt(4)==0?false:true);
	        	data.setLast_message(cursor.getString(5));
	        	data.setLast_message_time(cursor.getString(6));
	        	data.setMatches(cursor.getInt(7)==0?false:true);
	        	data.setMatch_time(cursor.getString(8));
	        	data.setFid(cursor.getString(9));
	        	data.setUnread_count(cursor.getInt(10));
	        	data.setLast_active_time(cursor.getString(11));
	        	listChat.add(data);
	        } while (cursor.moveToNext());
	    }
		this.close();
	    return listChat;
	}

	public ArrayList<ListChatData> getListVip() {
		ArrayList<ListChatData> listChat = new ArrayList<ListChatData>();
		String selectQuery = "SELECT * FROM "+ TABLE_LIST_CHAT + " WHERE "+KEY_VIP+ " = '1'" +"ORDER BY DATE("+KEY_ACTIVE_TIME+") DESC";
		this.open();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	        do {
	        	ListChatData data = new ListChatData();
	        	data.setProfile_id(cursor.getString(0));
	        	data.setName(cursor.getString(1));
	        	data.setAvatar(cursor.getString(2));
	        	data.setStatus(cursor.getInt(3));
	        	data.setIs_vip(cursor.getInt(4)==0?false:true);
	        	data.setLast_message(cursor.getString(5));
	        	data.setLast_message_time(cursor.getString(6));
	        	data.setMatches(cursor.getInt(7)==0?false:true);
	        	data.setMatch_time(cursor.getString(8));
	        	data.setFid(cursor.getString(9));
	        	data.setUnread_count(cursor.getInt(10));
	        	data.setLast_active_time(cursor.getString(11));
	        	listChat.add(data);
	        } while (cursor.moveToNext());
	    }
		this.close();
	    return listChat;
	}
    
	public int getTotalNotification(){
		this.open();
		String selectQuery = "SELECT Count(*) FROM "+ TABLE_LIST_CHAT + " WHERE "+KEY_STATUS+ " = '0' OR "+KEY_STATUS+ " = '2'";
		Cursor cursor = database.rawQuery(selectQuery, null);
		int count =0;
		if (cursor.moveToFirst()) {
			count = cursor.getInt(0);
		}
	    cursor.close();
		this.close();
		return count;
	}
	
	public int updateNewMessage(ListChatData data) {
		this.open();
		ContentValues values = new ContentValues();
		values.put(KEY_STATUS, data.getStatus());
		values.put(KEY_LAST_MESSAGE, data.getLast_message());
		values.put(KEY_LAST_MESSAGE_TIME, data.getLast_message_time());
		values.put(KEY_UNREAD_COUNT, data.getUnread_count());
		values.put(KEY_ACTIVE_TIME, data.getLast_active_time());
	    int result = database.update(TABLE_LIST_CHAT, values, KEY_PROFILE_ID + " = ?", new String[] { data.getProfile_id()});
	    this.close();
		return result;
	}
	
	public int updateReadMessage(ListChatData data) {
		this.open();
		ContentValues values = new ContentValues();
		values.put(KEY_STATUS, data.getStatus());
		values.put(KEY_UNREAD_COUNT, data.getUnread_count());
	    int result = database.update(TABLE_LIST_CHAT, values, KEY_PROFILE_ID + " = ?", new String[] { data.getProfile_id()});
	    this.close();
		return result;
	}
	
	public int updateReadMessage(String profileId) {
		this.open();
		ContentValues values = new ContentValues();
		values.put(KEY_STATUS, 3);
		values.put(KEY_UNREAD_COUNT, 0);
	    int result = database.update(TABLE_LIST_CHAT, values, KEY_PROFILE_ID + " = ?", new String[] { profileId});
	    this.close();
		return result;
	}
	

	public int updateReadMutualMatch(String profileId) {
		this.open();
		ContentValues values = new ContentValues();
		values.put(KEY_STATUS, 1);
	    int result = database.update(TABLE_LIST_CHAT, values, KEY_PROFILE_ID + " = ?", new String[] { profileId});
	    this.close();
		return result;
	}
	
	public boolean checkProfileExist(String profileId){
		this.open();
		String selectQuery = "SELECT Count(*) FROM "+ TABLE_LIST_CHAT + " WHERE "+KEY_PROFILE_ID+ " = '"+profileId+"'";
		Cursor cursor = database.rawQuery(selectQuery, null);
		int count =0;
		if (cursor.moveToFirst()) {
			count = cursor.getInt(0);
		}
	    cursor.close();
		this.close();
		if(count>0)
			return true;
		return false;
	}
	
}
