package com.afa.fizicianu.database;


import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;


public class DBHelper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;
    private static DBHelper sInstantce;

    public DBHelper(Context c){
        super(c,DATABASE_NAME,null,DATABASE_VERSION);
    }

    public static synchronized DBHelper getInstantce(Context c){
        if(sInstantce == null){
            sInstantce = new DBHelper(c.getApplicationContext());
        }
        return sInstantce;
    }


}
