package com.example.yena.losspreventionsystem;


import android.database.Cursor;
import android.util.Log;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by yena on 2016-04-10.
 */
public class ItemInfo implements Serializable {

    String beaconID;
    String name;
    double distance;
    int alarmStatus;
    Calendar lossTime;
    boolean lossCheck;
    boolean checked = false;

    public ItemInfo(){}

//    public double getDistance(Beacon beacon) {
//        return Math.pow(10d, ((double)beacon.getMeasuredPower() - beacon.getRssi()) / (10 * 2));
//    }
//
//    //TODO 더 해줘야함!!!
//    public ItemInfo(Beacon beacon, String name, double distance, int alarmStatus){
//        this.beaconID = beacon.getProximityUUID()+":"+ beacon.getMajor()+":"+beacon.getMinor();
//        this.name = name;
//        this.distance = getDistance(beacon);
//        this.alarmStatus = alarmStatus;
//        this.lossTime = null;
//    }
//    public ItemInfo(Beacon beacon, String name, double distance, int alarmStatus, Calendar lossTime){
//        this.beaconID = beacon.getProximityUUID()+":"+ beacon.getMajor()+":"+beacon.getMinor();
//        this.name = name;
//        this.distance = getDistance(beacon);
//        this.alarmStatus = alarmStatus;
//        this.lossTime = lossTime;
//    }
//
//    public ItemInfo(Beacon beacon, String name, double distance, int alarmStatus,boolean checked){
//       this(beacon,name,distance, alarmStatus);
//        this.checked = checked;
//    }

    public ItemInfo(String beaconID, String name, double distance, int alarmStatus,boolean checked){
        this(beaconID,name,distance, alarmStatus);
        this.checked = checked;
    }

    public ItemInfo(String beaconID, String name, double distance, int alarmStatus){
        this.beaconID = beaconID;
        this.name = name;
        this.distance = distance;
        this.alarmStatus = alarmStatus;
        this.lossTime = Calendar.getInstance();
        this.lossTime.setTimeInMillis(0);
        this.lossCheck = false;
    }
    public ItemInfo(String beaconID, String name, int alarmStatus, Calendar lossTime){
        this.beaconID = beaconID;
        this.name = name;
        this.distance = 0;
        this.alarmStatus = alarmStatus;
        this.lossTime = lossTime;
        this.lossCheck = false;
    }

    public ItemInfo(Cursor cursor){
        beaconID = cursor.getString(0);
        name = cursor.getString(1);
        distance = cursor.getDouble(2);
        alarmStatus = cursor.getInt(3);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA);
        try {
            lossTime = Calendar.getInstance();
            lossTime.setTime(sdf.parse(cursor.getString(4)));
        }
        catch(Exception e){
            Log.d("ItemInfo","Item 정보를 읽어오는 도중, 시간이 잘못되었습니다.");
        }
        if(cursor.getInt(5) == 0){
            lossCheck = false;
        } else if(cursor.getInt(5) == 1){
            lossCheck = true;
        }
    }

    int lossCheckToInt(){
        if(lossCheck){
            return 1;
        } else{
            return 0;
        }
    }
}
