package com.example.yena.losspreventionsystem;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yena on 2016-04-16.
 */
public class LPSDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "LossPreventionSystem.db";


    private static final String CREATE_TABLE_ITEM_INFO = "create table "
            + DBTable.ItemInfoTable.TABLE_NAME + "("
            + DBTable.ItemInfoTable.BEACON_ID + " text primary key, "
            + DBTable.ItemInfoTable.ITEM_NAME + " text not null , "
            + DBTable.ItemInfoTable.ITEM_DISTANCE + " double , "
            + DBTable.ItemInfoTable.ITEM_ALARM_STATUS + " int ); ";

    private static final String CREATE_TABLE_GROUP_INFO = "create table "
            + DBTable.GroupInfoTable.TABLE_NAME + "("
            + DBTable.GroupInfoTable.GROUP_ID + " integer primary key autoincrement, "
            + DBTable.GroupInfoTable.GROUP_NAME + " text not null ); ";

    private static final String CREATE_TABLE_ITEM_GROUP = "create table "
            + DBTable.ItemGroupTable.TABLE_NAME + "("
            + DBTable.ItemGroupTable.ITEM_GROUP_ID + " integer primary key autoincrement, "
            + DBTable.ItemGroupTable.BEACON_ID + " text, "
            + DBTable.ItemGroupTable.GROUP_ID + " int, " +
            "foreign key("+ DBTable.ItemGroupTable.BEACON_ID + ") references " + DBTable.ItemInfoTable.TABLE_NAME + "(" + DBTable.ItemInfoTable.BEACON_ID + ")," +
            "foreign key("+ DBTable.ItemGroupTable.GROUP_ID + ") references " + DBTable.GroupInfoTable.TABLE_NAME + "(" + DBTable.GroupInfoTable.GROUP_ID + ") " +
            ");";

    public LPSDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ITEM_INFO);
        db.execSQL(CREATE_TABLE_GROUP_INFO);
        db.execSQL(CREATE_TABLE_ITEM_GROUP);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + DBTable.ItemInfoTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS" + DBTable.GroupInfoTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS" + DBTable.ItemGroupTable.TABLE_NAME);
        onCreate(db);
    }
}
