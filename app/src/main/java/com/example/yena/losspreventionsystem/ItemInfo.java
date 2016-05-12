package com.example.yena.losspreventionsystem;

import android.database.Cursor;

/**
 * Created by yena on 2016-04-10.
 */
public class ItemInfo {
///// DISABLE : 아예 찾지도 않는거, ALARM_OFF : 알람기능만 없는거(팝업 메시지는 있다)
    public static final int  DISABLE = 0, ALARM_OFF = 1, ALARM_SILENT = 2,
            ALARM_VIBRATION = 3, ALARM_SOUND = 4, ALARM_SOUND_VIBRATION = 5;


    String beaconID;
    String name;
    double distance;
    int alarmStatus;
    boolean checked = false;

    public ItemInfo(){

    }

    //TODO 더 해줘야함!!!
    public ItemInfo(String beaconID, String name, double distance, int alarmStatus){
        this.beaconID = beaconID;
        this.name = name;
        this.distance = distance;
        this.alarmStatus = alarmStatus;
    }
    public ItemInfo(String beaconID, String name, double distance, int alarmStatus,boolean checked){
       this(beaconID,name,distance, alarmStatus);
        this.checked = checked;
    }

    public ItemInfo(Cursor cursor){
        beaconID = cursor.getString(0);
        name = cursor.getString(1);
        distance = cursor.getDouble(2);
        alarmStatus = cursor.getInt(3);
    }
}
