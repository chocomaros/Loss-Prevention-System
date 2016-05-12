package com.example.yena.losspreventionsystem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CAU on 2016-05-10.
 */
public class BeaconManagement {
    public List<ItemInfo> beaconInfoList = new ArrayList<>();

    public BeaconManagement(){

    }


    //거리가 벗어났는지
    public boolean isOverDistance(ItemInfo itemInfo, int maxDistance){
        if(itemInfo.distance < maxDistance){
            return true;
        }else{
            return false;
        }
    }

    //범위 탈출 알람


    //범위 탈출 시간

    //

}
