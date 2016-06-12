package com.example.yena.losspreventionsystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.security.acl.Group;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by yena on 2016-04-16.
 */
public class LPSDAO {

    public static ArrayList<ItemInfo> getItemInfo(Context context){
        ArrayList<ItemInfo> itemList = new ArrayList<ItemInfo>();

        LPSDBHelper dbHelper = new LPSDBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //itemList.clear();
        //TODO orderby 나중에 해야징
        Cursor cursor = db.rawQuery("select * from " + DBTable.ItemInfoTable.TABLE_NAME + ";", null);
        while(cursor.moveToNext()){
            ItemInfo item = new ItemInfo(cursor);
            itemList.add(item);
        }
        cursor.close();
        dbHelper.close();
        return itemList;
    }

    public static ArrayList<ItemGroup> getAllItemGroup(Context context){
        ArrayList<ItemGroup> itemGroupList = new ArrayList<ItemGroup>();

        LPSDBHelper dbHelper = new LPSDBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //groupList.clear();
        Cursor cursor = db.rawQuery("select * from " + DBTable.ItemGroupTable.TABLE_NAME + ";", null);
        while(cursor.moveToNext()){
            ItemGroup itemGroup = new ItemGroup(cursor);
            itemGroupList.add(itemGroup);
        }
        cursor.close();
        dbHelper.close();
        return itemGroupList;
    }

    public static ArrayList<ItemInfo> getNotDisableItemInfo(Context context){
        ArrayList<ItemInfo> itemList = new ArrayList<ItemInfo>();

        LPSDBHelper dbHelper = new LPSDBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //itemList.clear();
        //TODO orderby 나중에 해야징
        Cursor cursor = db.rawQuery("select * from " + DBTable.ItemInfoTable.TABLE_NAME +
                " where " + DBTable.ItemInfoTable.ITEM_ALARM_STATUS + " != " + AlarmManagement.DISABLE + ";", null);
        while(cursor.moveToNext()){
            ItemInfo item = new ItemInfo(cursor);
            itemList.add(item);
        }
        cursor.close();
        dbHelper.close();
        return itemList;
    }

    public static ArrayList<GroupInfo> getGroupInfo(Context context){
        ArrayList<GroupInfo> groupList = new ArrayList<GroupInfo>();

        LPSDBHelper dbHelper = new LPSDBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //groupList.clear();
        Cursor cursor = db.rawQuery("select * from " + DBTable.GroupInfoTable.TABLE_NAME + ";", null);
        while(cursor.moveToNext()){
            GroupInfo group = new GroupInfo(cursor);
            groupList.add(group);
        }
        cursor.close();
        dbHelper.close();
        return groupList;
    }

    public static ArrayList<GroupInfo> getGroupListOfItem(Context context, ItemInfo itemInfo){
        ArrayList<GroupInfo> groupList = new ArrayList<GroupInfo>();

        LPSDBHelper dbHelper = new LPSDBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //groupList.clear();
        Cursor cursor = db.rawQuery("select " + DBTable.GroupInfoTable.GROUP_ID + ", " + DBTable.GroupInfoTable.GROUP_NAME +
                " from " + DBTable.ItemGroupTable.TABLE_NAME + " NATURAL JOIN " + DBTable.GroupInfoTable.TABLE_NAME +
                " where " + DBTable.ItemGroupTable.BEACON_ID + " = '" + itemInfo.beaconID + "';", null);
        while(cursor.moveToNext()){
            GroupInfo group = new GroupInfo(cursor);
            groupList.add(group);
        }
        cursor.close();
        dbHelper.close();
        return groupList;
    }

    public static ArrayList<ItemInfo> getItemListOfGroup(Context context, GroupInfo groupInfo){
        ArrayList<ItemInfo> itemList = new ArrayList<>();

        LPSDBHelper dbHelper = new LPSDBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //groupList.clear();
        Cursor cursor = db.rawQuery("select " + DBTable.ItemInfoTable.BEACON_ID + ", " + DBTable.ItemInfoTable.ITEM_NAME + ", " + DBTable.ItemInfoTable.ITEM_DISTANCE +
                ", " + DBTable.ItemInfoTable.ITEM_ALARM_STATUS + ", " + DBTable.ItemInfoTable.ITEM_LOSS_TIME + ", " + DBTable.ItemInfoTable.ITEM_LOSS_CHECK + ", " + DBTable.ItemInfoTable.ITEM_LOCK +
                " from " + DBTable.ItemGroupTable.TABLE_NAME + " NATURAL JOIN " + DBTable.ItemInfoTable.TABLE_NAME +
                " where " + DBTable.ItemGroupTable.GROUP_ID + " = " + groupInfo.id + ";", null);
        while(cursor.moveToNext()){
            ItemInfo item = new ItemInfo(cursor);
            itemList.add(item);
        }
        cursor.close();
        dbHelper.close();
        return itemList;
    }

    public static int getItemGroupID(Context context, GroupInfo groupInfo, ItemInfo itemInfo){

        LPSDBHelper dbHelper = new LPSDBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //groupList.clear();
        Cursor cursor = db.rawQuery("select " + DBTable.ItemGroupTable.ITEM_GROUP_ID +
                " from " + DBTable.ItemGroupTable.TABLE_NAME +
                " where " + DBTable.ItemGroupTable.GROUP_ID + " = " + groupInfo.id + " and " + DBTable.ItemGroupTable.BEACON_ID + " = '" + itemInfo.beaconID + "';", null);
        int id = 0;
        while(cursor.moveToNext()){
         id = cursor.getInt(0);
        }
        cursor.close();
        dbHelper.close();
        return id;
    }

    public static void insertItemInfo(Context context, ItemInfo itemInfo){
        LPSDBHelper dbHelper = new LPSDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA);
        db.execSQL("insert into " + DBTable.ItemInfoTable.TABLE_NAME +
                " values ('" + itemInfo.beaconID + "', '" + itemInfo.name + "', " + itemInfo.distance + ", " + itemInfo.alarmStatus +
                ", '" + sdf.format(new Date(itemInfo.lossTime.getTimeInMillis())) + "', " + itemInfo.lossCheckToInt() + ", " + itemInfo.lockToInt() + ");");
        dbHelper.close();
    }

    public static int insertItemGroupInfo(Context context, ItemGroup itemGroup){
        ArrayList<ItemGroup> itemGroupList = new ArrayList<ItemGroup>();

        LPSDBHelper dbHelper = new LPSDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        db.execSQL("insert into " + DBTable.ItemGroupTable.TABLE_NAME+
//                " values (null, '" + itemInfo.beaconID + "', " + groupInfo.id +");");
        ContentValues values = new ContentValues();
        values.put(DBTable.ItemGroupTable.BEACON_ID, itemGroup.beaconID);
        values.put(DBTable.ItemGroupTable.GROUP_ID, itemGroup.id);

        int id = (int) db.insert(DBTable.ItemGroupTable.TABLE_NAME, null, values);
        dbHelper.close();
        return id;
    }



    public static void deleteItemInfo(Context context, ItemInfo itemInfo){
        LPSDBHelper dbHelper = new LPSDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from " + DBTable.ItemInfoTable.TABLE_NAME +
                " where  " + DBTable.ItemInfoTable.BEACON_ID +" = '" + itemInfo.beaconID + "';");
        dbHelper.close();
    }

    public static int insertGroupInfo(Context context, GroupInfo groupInfo){
        LPSDBHelper dbHelper = new LPSDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBTable.GroupInfoTable.GROUP_NAME, groupInfo.name);


//        db.execSQL("insert into " + DBTable.GroupInfoTable.TABLE_NAME +
//                " values (null, '" + groupInfo.name + "');");
        int id = (int)db.insert(DBTable.GroupInfoTable.TABLE_NAME, null, values);
        dbHelper.close();
        return id;
    }

    public static void deleteGroupInfo(Context context, GroupInfo groupInfo){
        LPSDBHelper dbHelper = new LPSDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from " + DBTable.GroupInfoTable.TABLE_NAME +
                " where " + DBTable.GroupInfoTable.GROUP_ID +" = " + groupInfo.id + ";");
        dbHelper.close();
    }

    public static int insertItemGroup(Context context, ItemInfo itemInfo, GroupInfo groupInfo){
        LPSDBHelper dbHelper = new LPSDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        db.execSQL("insert into " + DBTable.ItemGroupTable.TABLE_NAME+
//                " values (null, '" + itemInfo.beaconID + "', " + groupInfo.id +");");
        ContentValues values = new ContentValues();
        values.put(DBTable.ItemGroupTable.BEACON_ID, itemInfo.beaconID);
        values.put(DBTable.ItemGroupTable.GROUP_ID, groupInfo.id);

        int id = (int) db.insert(DBTable.ItemGroupTable.TABLE_NAME, null, values);
        dbHelper.close();
        return id;
    }

    public static void deleteItemGroup(Context context, ItemInfo itemInfo, GroupInfo groupInfo){
        LPSDBHelper dbHelper = new LPSDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from " + DBTable.ItemGroupTable.TABLE_NAME +
                " where " + DBTable.ItemInfoTable.BEACON_ID + " = '" + itemInfo.beaconID + "' and " + DBTable.GroupInfoTable.GROUP_ID + " = " + groupInfo.id + ";");
        dbHelper.close();
    }

    public static void deleteItemGroup(Context context, ItemInfo itemInfo){
        LPSDBHelper dbHelper = new LPSDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from " + DBTable.ItemGroupTable.TABLE_NAME +
                " where " + DBTable.ItemInfoTable.BEACON_ID + " = '" + itemInfo.beaconID + "';");
        dbHelper.close();
    }

    public static void deleteItemGroup(Context context, GroupInfo groupInfo){
        LPSDBHelper dbHelper = new LPSDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from " + DBTable.ItemGroupTable.TABLE_NAME +
                " where " + DBTable.GroupInfoTable.GROUP_ID + " = " + groupInfo.id + ";");
        dbHelper.close();
    }

    public static void updateItemInfoAlarmStatus(Context context, ItemInfo itemInfo){
        LPSDBHelper dbHelper = new LPSDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("update " + DBTable.ItemInfoTable.TABLE_NAME +
                " set " + DBTable.ItemInfoTable.ITEM_ALARM_STATUS + " = " + itemInfo.alarmStatus +
                " where " + DBTable.ItemInfoTable.BEACON_ID + " = '" + itemInfo.beaconID + "';");
        dbHelper.close();
    }

    public static void updateGroupInfoName(Context context, GroupInfo groupInfo){
        LPSDBHelper dbHelper = new LPSDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("update " + DBTable.GroupInfoTable.TABLE_NAME +
                " set " + DBTable.GroupInfoTable.GROUP_NAME + " = '" + groupInfo.name + "'"+
                " where " + DBTable.GroupInfoTable.GROUP_ID + " = " + groupInfo.id + ";");
        dbHelper.close();
    }

    public static void updateItemInfoName(Context context, ItemInfo itemInfo){
        LPSDBHelper dbHelper = new LPSDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("update " + DBTable.ItemInfoTable.TABLE_NAME +
                " set " + DBTable.ItemInfoTable.ITEM_NAME + " = '" + itemInfo.name + "'"+
                " where " + DBTable.ItemInfoTable.BEACON_ID + " = '" + itemInfo.beaconID + "';");
        dbHelper.close();
    }

    public static void updateItemInfoLossCheck(Context context, ItemInfo itemInfo){
        LPSDBHelper dbHelper = new LPSDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("update " + DBTable.ItemInfoTable.TABLE_NAME +
                " set " + DBTable.ItemInfoTable.ITEM_LOSS_CHECK + " = " + itemInfo.lossCheckToInt() +
                " where " + DBTable.ItemInfoTable.BEACON_ID + " = '" + itemInfo.beaconID + "';");
        dbHelper.close();
    }

    public static void updateItemInfoLossTime(Context context, ItemInfo itemInfo){
        LPSDBHelper dbHelper = new LPSDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA);
        db.execSQL("update " + DBTable.ItemInfoTable.TABLE_NAME +
                " set " + DBTable.ItemInfoTable.ITEM_LOSS_TIME + " = '" + sdf.format(new Date(itemInfo.lossTime.getTimeInMillis()))  + "'"+
                " where " + DBTable.ItemInfoTable.BEACON_ID + " = '" + itemInfo.beaconID + "';");
        dbHelper.close();
    }

    public static void updateItemInfoLock(Context context, ItemInfo itemInfo){
        LPSDBHelper dbHelper = new LPSDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("update " + DBTable.ItemInfoTable.TABLE_NAME +
                " set " + DBTable.ItemInfoTable.ITEM_LOCK + " = " + itemInfo.lockToInt() +
                " where " + DBTable.ItemInfoTable.BEACON_ID + " = '" + itemInfo.beaconID + "';");
        dbHelper.close();
    }

    public static void resetTableAll(Context context){
        LPSDBHelper dbHelper = new LPSDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DBTable.ItemInfoTable.TABLE_NAME, null, null);
        db.delete(DBTable.GroupInfoTable.TABLE_NAME, null, null);
        db.delete(DBTable.ItemGroupTable.TABLE_NAME, null, null);
        dbHelper.close();
    }
}