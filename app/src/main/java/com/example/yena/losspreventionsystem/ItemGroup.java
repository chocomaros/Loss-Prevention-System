package com.example.yena.losspreventionsystem;

import android.database.Cursor;

/**
 * Created by yena on 2016-06-04.
 */
public class ItemGroup {

    int id;
    String beaconID;
    int groupID;


    public ItemGroup(int id, String beaconID, int groupID){
        this.id = id;
        this.beaconID = beaconID;
        this. groupID = groupID;
    }

    public ItemGroup(Cursor cursor){
        id = cursor.getInt(0);
        beaconID = cursor.getString(1);
        groupID = cursor.getInt(2);
    }
}