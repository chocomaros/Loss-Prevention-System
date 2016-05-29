package com.example.yena.losspreventionsystem;


import android.app.AlarmManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LPSService extends Service implements BeaconConsumer {

//    Vibrator vide; // 진동
//    MediaPlayer mediaPlayer;

    //비콘
    private static final String TAG = "ServiceActivity";
    private static final int INTERVAL = 1000; /// 1초
    private static String isConnect = "";
    org.altbeacon.beacon.BeaconManager beaconManager;
    ArrayList<Beacon> beaconList = new ArrayList<>();
//    myhandler beaconHandler = new myhandler();
//    BluetoothChecker bluetoothChecker;

    public LPSService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //bluetoothChecker = new BluetoothChecker();
        beaconInit();

//        for (Beacon beacon : beaconList) {
//            if (beaconList.contains(beacon)) {
//                Log.i(TAG, "connect");
//                isConnect = "connect";
//            } else isConnect = "disconnect";
//
//            Log.i(TAG, "UUID : " + beacon.getServiceUuid() + " / " + "Major : " + beacon.getId2() + " / " + "Minor : " + beacon.getId3() + " / " + "Battery : " + beacon.getDataFields().get(0) + " / " + "Distance : " + Double.parseDouble(String.format("%.3f", beacon.getDistance())) +
//                    "신호세기 : " + beacon.getTxPower() + " / " + "통신간격 : " + INTERVAL + "sec " + " / " + "연결상태 : " + isConnect + " / " + "m\n");
//
//            compareDistance(beacon);
//        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        beaconHandler.started=true;
//        beaconHandler.sendEmptyMessage(0);

        // TODO Auto-generated method stub
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("service onDestroy", "서비스 종료");
        beaconManager.unbind(this);
        // TODO Auto-generated method stub
    }

    //===============비콘 거리 비교 ==================================

    public void compareDistance(Beacon beacon) {
        SharedPreferences pref;
        pref = getSharedPreferences(LPSSharedPreferences.NAME,0);
        int maxDistance = pref.getInt(LPSSharedPreferences.DISTANCE_SETTING,SettingFragment.DISTANCE1);
        ArrayList<ItemInfo> itemList;
        itemList = LPSDAO.getNotDisableItemInfo(getApplicationContext());
        if(beacon.getDistance() <= maxDistance){
            for(int i=0; i<itemList.size(); i++){
                if(itemList.get(i).beaconID.equals(beacon.getId1().toString())){
                    itemList.get(i).lossCheck = false;
                    LPSDAO.updateItemInfoLossCheck(getApplicationContext(),itemList.get(i));
                }
            }
        } else{
            for(int i=0; i<itemList.size(); i++){
                if(itemList.get(i).beaconID.equals(beacon.getId1().toString())){
                    if(itemList.get(i).lossCheck){
                        Log.d("service","알람했는데 아직 거리 안으로 못들어옴");
                        // 알람했는데 아직도 거리 안으로 못들어온거
                    } else{
                        AlarmManagement alarmManagement = new AlarmManagement(getApplicationContext()); ///컨텍스트 안해줘서 오류나려나?
                        itemList.get(i).lossTime = Calendar.getInstance();
                        itemList.get(i).lossCheck = true;
                        if(pref.getInt(LPSSharedPreferences.ALARM_CONTROL,AlarmManagement.ALARM_ALL_ON) == AlarmManagement.ALARM_ALL_OFF){
                            /// 전체 알람 껐을때
                            alarmManagement.generateNotification(itemList.get(i));
                        } else{
                            Intent popupIntent = new Intent(LPSService.this, AlarmActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            popupIntent.putExtra(AlarmActivity.ITEM_INFO,itemList.get(i));
                            // 그리고 호출한다.
                            startActivity(popupIntent);
                            alarmManagement.generateNotification(itemList.get(i));
                        }
                        LPSDAO.updateItemInfoLossCheck(getApplicationContext(),itemList.get(i));
                        LPSDAO.updateItemInfoLossTime(getApplicationContext(), itemList.get(i));
                    }
                }
            }
            //new Thread(task).start();
        }

    }

    //==================================================================


    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {

            }

            @Override
            public void didExitRegion(Region region) {

            }

            @Override
            public void didDetermineStateForRegion(int i, Region region) {
                if(i == MonitorNotifier.INSIDE){
                    Log.d("Region","안에 있다.");
                } else if(i == MonitorNotifier.OUTSIDE){
                    Log.d("Region","밖에 있다.");
                }
            }
        });

        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            // 비콘이 감지되면 해당 함수가 호출된다. Collection<Beacon> beacons에는 감지된 비콘의 리스트가,
            // region에는 비콘들에 대응하는 Region 객체가 들어온다.
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() == 0) {
                    Log.d("서비스","비콘 0개!");
                } else if (beacons.size() > 0) {
                    for (Beacon beacon : beacons) {
                        //compareDistance(beacon);
                        Log.d("beacon distance", beacon.getDistance()+"");
                    }
                }

            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
            beaconManager.startMonitoringBeaconsInRegion(new Region("abcd", null, null, null));
        } catch (RemoteException e) {
        }
    }


    //============================================================================================================================================
    public void beaconInit(){
        if (Build.VERSION.SDK_INT >= 19 && getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            // BLE
            BeaconManager.setAndroidLScanningDisabled(true);
        }
        beaconManager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        if(beaconManager.isAndroidLScanningDisabled()){
            Log.d("androidL","scanning disabled");
        } else{
            Log.d("androidL","scanning");
        }
        beaconManager.setAndroidLScanningDisabled(true);
        if(beaconManager.isAndroidLScanningDisabled()){
            Log.d("androidL","scanning disabled");
        } else{
            Log.d("androidL","scanning");
        }
        beaconManager.bind(this);

    }

    class myhandler extends Handler {
        boolean started = false;
        public void handleMessage(Message msg) {
            // 비콘의 아이디와 거리를 측정하여 textView에 넣는다.
            for (Beacon beacon : beaconList) {
                if (beaconList.contains(beacon)) {
                    Log.i(TAG, "connect");
                    isConnect = "connect";
                } else isConnect = "disconnect";

                Log.i(TAG, "UUID : " + beacon.getServiceUuid() + " / " + "Major : " + beacon.getId2() + " / " + "Minor : " + beacon.getId3() + " / " + "Battery : " + beacon.getDataFields().get(0) + " / " + "Distance : " + Double.parseDouble(String.format("%.3f", beacon.getDistance())) +
                        "신호세기 : " + beacon.getTxPower() + " / " + "통신간격 : " + INTERVAL + "sec " + " / " + "연결상태 : " + isConnect + " / " + "m\n");

                compareDistance(beacon);
            }

//             자기 자신을 INTERVAL 마다 호출
            if(started){
//                beaconHandler.sendEmptyMessageDelayed(0, INTERVAL);
            }
        }
    }
}
