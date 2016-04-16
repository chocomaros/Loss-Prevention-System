package com.example.yena.losspreventionsystem;

import java.util.ArrayList;

/**
 * Created by yena on 2016-04-10.
 */
public class ItemInfo {
/// DISABLE : 아예 찾지도 않는거, ALARM_OFF : 알람기능만 없는거(팝업 메시지는 있다)
    public static final int  DISABLE = 0, ALARM_OFF = 1, ALARM_SILENT = 2,
            ALARM_VIBRATION = 3, ALARM_SOUND = 4, ALARM_SOUND_VIBRATION = 5;

    int beaconID;
    String name;
    double distance;
    ArrayList<Integer> groupList = new ArrayList<>();
    int alarmStatus;

    public ItemInfo(){

    }
    //TODO 더 해줘야함!!!
    public ItemInfo(String name, double distance, ArrayList<Integer> groupList, int alarmStatus){
        this.name = name;
        this.distance = distance;
        this.groupList.addAll(groupList);
        this.alarmStatus = alarmStatus;
    }

//    ArrayList<String> groupListToString(){
//    }
}
