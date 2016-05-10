package com.example.yena.losspreventionsystem;

import android.content.Context;

import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.telemetry.EstimoteTelemetry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CAU on 2016-05-10.
 */
public class BeaconManagement {
    public List<BeaconInfo> beaconInfoList = new ArrayList<>();
    private EstimoteTelemetry estimoteTelemetry;
    public BeaconInfo beaconInfo;
    private BeaconManager beaconManager;
    private Context context;
    public int rssi=0,  txPower=0;
    public BeaconManagement(){

    }
    //내 비콘들 저장하는 list



    //거리가 벗어났는지
    public boolean isOverDistance(BeaconInfo beaconInfo, int maxDistace){
        if(beaconInfo.getDistance(rssi,txPower) < maxDistace){
            return true;
        }else{
            return false;
        }
    }

    //범위 탈출 알람


    //범위 탈출 시간

    //

}
