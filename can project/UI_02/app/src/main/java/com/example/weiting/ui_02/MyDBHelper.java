package com.example.weiting.ui_02;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Weiting on 2017/10/2.
 */
 public  class MyDBHelper extends SQLiteOpenHelper {
        private static final String database = "mydata.db";
        private static final int version = 2;
    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
    }
    public MyDBHelper(Context context){
        this(context,database,null,version);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE myTable(_id integer primary key autoincrement,userID text no null,icanID text no null,usedTime integer no null,battery integer no null,temperature integer no null,latitude real no null,longitude real no null,state text no null,lastUsedTime text no null)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS myTable");
        onCreate(db);
    }
 }


