package com.example.yena.losspreventionsystem;


import android.database.Cursor;

import com.estimote.sdk.Beacon;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by yena on 2016-04-10.
 */
public class ItemInfo implements Serializable {
/// DISABLE : 아예 찾지도 않는거, ALARM_OFF : 알람기능만 없는거(팝업 메시지는 있다)

    public static final int  DISABLE = 0, ALARM_OFF = 1, ALARM_SILENT = 2,
            ALARM_VIBRATION = 3, ALARM_SOUND = 4, ALARM_SOUND_VIBRATION = 5;


    String beaconID;
    String name;
    double distance;
    int alarmStatus;
    Calendar lossTime;
    boolean checked = false;

    public ItemInfo(){}

    public double getDistance(Beacon beacon) {
        return Math.pow(10d, ((double)beacon.getMeasuredPower() - beacon.getRssi()) / (10 * 2));
    }

    //TODO 더 해줘야함!!!
    public ItemInfo(Beacon beacon, String name, double distance, int alarmStatus){
        this.beaconID = beacon.getProximityUUID()+":"+ beacon.getMajor()+":"+beacon.getMinor();
        this.name = name;
        this.distance = getDistance(beacon);
        this.alarmStatus = alarmStatus;
        this.lossTime = null;
    }
    public ItemInfo(Beacon beacon, String name, double distance, int alarmStatus, Calendar lossTime){
        this.beaconID = beacon.getProximityUUID()+":"+ beacon.getMajor()+":"+beacon.getMinor();
        this.name = name;
        this.distance = getDistance(beacon);
        this.alarmStatus = alarmStatus;
        this.lossTime = lossTime;
    }

    public ItemInfo(Beacon beacon, String name, double distance, int alarmStatus,boolean checked){
       this(beacon,name,distance, alarmStatus);
        this.checked = checked;
    }

    public ItemInfo(Cursor cursor){
        beaconID = cursor.getString(0);
        name = cursor.getString(1);
        distance = cursor.getDouble(2);
        alarmStatus = cursor.getInt(3);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA);
        try {
            lossTime.setTime(sdf.parse(cursor.getString(4)));
        }
        catch(Exception e){
            System.out.println("Item 정보를 읽어오는 도중, 시간이 잘못되었습니다.");
        }
    }
}
