package com.oakclub.android.helper.operations;

public class ProfileUerOperation {

	//Table Setting Name
    public static final String TABLE_SETTING = "setting";
    
    //Attribute Table Setting
    private static final String KEY_ID = "id";
    private static final String KEY_GENDER_SEARCH = "gender_of_search";
    private static final String KEY_FRIEND = "include_friend"; 
    private static final String KEY_AGE_FROM = "age_from"; 
    private static final String KEY_AGE_TO = "age_to"; 
    private static final String KEY_RANGE = "range"; 
    
    
    //Query Create Table Setting
    public static final String CREATE_TABLE_SETTING = "CREATE TABLE "
            + TABLE_SETTING + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_GENDER_SEARCH
            + " TEXT," + KEY_FRIEND + " BOOLEAN," + KEY_AGE_FROM + "INTEGER," + KEY_AGE_TO+"INTEGER,"
            + KEY_RANGE+ " INTEGER" + ")";
}
