package com.oakclub.android.helper;


import com.oakclub.android.helper.operations.ListChatOperation;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	// Database Name
	private static final String DATABASE_NAME = "Oakclub";
	
	//Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Table Name
    private static final String TABLE_HISTORY_CHAT_OLD = "history_chat_old";
    private static final String TABLE_HISTORY_CHAT_NEW = "history_chat_new";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(ListChatOperation.CREATE_TABLE_LIST_CHAT);
    }

    // Method is called during an upgrade of the database,
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
            int newVersion) {
        Log.w(DatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        
        database.execSQL("DROP TABLE IF EXISTS " + ListChatOperation.TABLE_LIST_CHAT);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY_CHAT_OLD);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY_CHAT_NEW);
        onCreate(database);
    }
	
}
