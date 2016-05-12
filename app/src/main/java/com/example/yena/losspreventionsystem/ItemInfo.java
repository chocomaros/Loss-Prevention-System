package com.example.yena.losspreventionsystem;

import android.database.Cursor;

<<<<<<< HEAD
/**
 * Created by yena on 2016-04-10.
 */
public class ItemInfo {
///// DISABLE : 아예 찾지도 않는거, ALARM_OFF : 알람기능만 없는거(팝업 메시지는 있다)
=======
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by yena on 2016-04-10.
 */
public class ItemInfo implements Serializable {
/// DISABLE : 아예 찾지도 않는거, ALARM_OFF : 알람기능만 없는거(팝업 메시지는 있다)
>>>>>>> origin/master
    public static final int  DISABLE = 0, ALARM_OFF = 1, ALARM_SILENT = 2,
            ALARM_VIBRATION = 3, ALARM_SOUND = 4, ALARM_SOUND_VIBRATION = 5;


    String beaconID;
    String name;
    double distance;
    int alarmStatus;
    Calendar lossTime;
    boolean checked = false;

    public ItemInfo(){

    }

    //TODO 더 해줘야함!!!
    public ItemInfo(String beaconID, String name, double distance, int alarmStatus){
        this.beaconID = beaconID;
        this.name = name;
        this.distance = distance;
        this.alarmStatus = alarmStatus;
        this.lossTime = null;
    }
    public ItemInfo(String beaconID, String name, double distance, int alarmStatus, Calendar lossTime){
        this.beaconID = beaconID;
        this.name = name;
        this.distance = distance;
        this.alarmStatus = alarmStatus;
        this.lossTime = lossTime;
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA);
        try {
            lossTime.setTime(sdf.parse(cursor.getString(4)));
        }
        catch(Exception e){
            System.out.println("Item 정보를 읽어오는 도중, 시간이 잘못되었습니다.");
        }
    }
}
