package com.oakclub.android.helper.operations;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.oakclub.android.helper.DatabaseHelper;
import com.oakclub.android.model.ChatHistoryData;

public class HistoryChatOperation {

	private DatabaseHelper dbHelper;
	private SQLiteDatabase database;
	
	//Table Setting Name
    public static final String TABLE_HISTORY_CHAT = "history_chat";
    
    //Attribute Table Setting
    private static final String KEY_ID = "id";
    private static final String KEY_PROFILE_ID = "profile_id"; 
    private static final String KEY_PROFILE_FROM = "profile_from"; 
    private static final String KEY_PROFILE_TO = "profile_to"; 
    private static final String KEY_TIME = "time_message"; 
    private static final String KEY_BODY = "body"; 
    private static final String KEY_STATUS = "status";
    
    
    
    //Query Create Table Setting
    public static final String CREATE_TABLE_HISTORY_CHAT = "CREATE TABLE "
            + TABLE_HISTORY_CHAT 
            + "{"
    		+ KEY_ID + " INTEGER AUTOINCREMENT, " 
            + KEY_PROFILE_ID + " TEXT PRIMARY KEY, "
            + KEY_PROFILE_FROM + " TEXT, "
            + KEY_PROFILE_TO + " TEXT, "
            + KEY_TIME + " DATETIME, "
            + KEY_BODY+ " TEXT"
            + KEY_STATUS + " INTEGER, "
    		+ "FOREIGN KEY("+KEY_PROFILE_ID+") REFERENCES "+ListChatOperation.TABLE_LIST_CHAT+"("+KEY_PROFILE_ID+")" 
            + "}";

    public HistoryChatOperation(Context context) {
        dbHelper = new DatabaseHelper(context);
    }
    	
    private void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }
 
    private void close() {
        dbHelper.close();
    }
    
    public void insertHistoryChat(ChatHistoryData data) {
		this.open();
		ContentValues values = new ContentValues();
		values.put(KEY_PROFILE_ID, data.getFrom());
		values.put(KEY_STATUS, data.getTo());
		values.put(KEY_TIME, data.getTime_string());
		values.put(KEY_BODY, data.getBody());
		database.insert(TABLE_HISTORY_CHAT, null, values);
		this.close();
	}  
    
    public void deleteChat(int id) {
		this.open();
		String deleteQuery = "DELETE FROM "+TABLE_HISTORY_CHAT+" where "+KEY_ID+ "='"+id+"'";
		database.execSQL(deleteQuery);
		this.close();
	}    
    

	public void deleteAllHistoryChat() {
		this.open();
		String deleteQuery = "DELETE FROM "+TABLE_HISTORY_CHAT;
		database.execSQL(deleteQuery);
		this.close();
	}
	
	public ChatHistoryData getHistoryChat(String profileId){
		ChatHistoryData data = new ChatHistoryData();
		String selectQuery = "SELECT * FROM "+ TABLE_HISTORY_CHAT + " WHERE "+KEY_PROFILE_ID+" ='"+profileId+"'";
		this.open();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	    	data.setProfile_id(cursor.getString(1));
	    	data.setFrom(cursor.getString(2));
	    	data.setTo(cursor.getString(3));
	    	data.setTime_string(cursor.getString(4));
	    	data.setBody(cursor.getString(5));
	    	data.setStatus(cursor.getInt(6));
	    }
		this.close();
		return data;
	}
}

