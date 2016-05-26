package com.example.yena.losspreventionsystem;


import android.app.AlarmManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

public class LPSService extends Service implements BeaconConsumer {

//    Vibrator vide; // 진동
//    MediaPlayer mediaPlayer;

    //비콘
    private static final String TAG = "ServiceActivity";
    private static final int INTERVAL = 1000; /// 1초
    private static String isConnect = "";
    org.altbeacon.beacon.BeaconManager beaconManager;
    ArrayList<Beacon> beaconList = new ArrayList<>();

    BluetoothChecker bluetoothChecker;

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
        //진동
//        initMediaPlayer();
        beaconInit();
        bluetoothChecker = new BluetoothChecker();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        beaconHandler.sendEmptyMessage(0);

        // TODO Auto-generated method stub
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("service onDestroy", "서비스 종료");
        beaconManager.unbind(this);
        // TODO Auto-generated method stub
        super.onDestroy();
//        Cancel_Vib();
//        Cancel_Mus();


    }
    //=================================================================
    //  Thread로 작업할 부분
    Runnable task = new Runnable(){
        public void run(){
//                Vibrator_pattern();
//                mediaPlayer.start();
        }
    };
    // =================소리 경로지정 및 초기화 ======================
//    private void initMediaPlayer() {
//
//        mediaPlayer = new MediaPlayer();
//        mediaPlayer = MediaPlayer.create(
//                LPSService.this, R.raw.konan);
//    }

    // ==================진동 패턴=====================================
//    public void Vibrator_pattern(){
//        vide =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//        long[] pattern = {0,500,200,400,100};//시작시간, 진동울리는 시간, 진동이 쉴시간, 진동이 울리는 시간, 진동이 쉴시간
//        vide.vibrate(pattern, -1);// -1(1번 반복), 0 or 양수(그 index부터 시작하여 무한반복 하겟다)
//    }

    /*
    ================= 취소===========================================
     */
    //진동 취소
//    public void Cancel_Vib(){
//        vide.cancel();
//    }
//    //소리 취소
//    public void Cancel_Mus(){
//        mediaPlayer.stop();
//    }

    //===============비콘 거리 비교 ==================================

    public void compareDistance(Beacon beacon) {
        SharedPreferences pref;
        pref = getSharedPreferences(LPSSharedPreferences.NAME,0);
        int maxDistance = pref.getInt(LPSSharedPreferences.DISTANCE_SETTING,50);
        ArrayList<ItemInfo> itemList;
        itemList = LPSDAO.getNotDisableItemInfo(getApplicationContext());
        if(beacon.getDistance() <= maxDistance){
            for(int i=0; i<itemList.size(); i++){
                if(itemList.get(i).beaconID.equals(beacon.getServiceUuid())){
                    itemList.get(i).lossCheck = false;
                    LPSDAO.updateItemInfoLossCheck(getApplicationContext(),itemList.get(i));
                }
            }
        } else{
            for(int i=0; i<itemList.size(); i++){
                if(itemList.get(i).beaconID.equals(beacon.getServiceUuid())){
                    if(itemList.get(i).lossCheck){
                        // 알람했는데 아직도 거리 안으로 못들어온거
                    } else{
                        AlarmManagement alarmManagement = new AlarmManagement(); ///컨텍스트 안해줘서 오류나려나?
                        itemList.get(i).lossTime = Calendar.getInstance();
                        itemList.get(i).lossCheck = true;
                        if(pref.getInt(LPSSharedPreferences.ALARM_CONTROL,AlarmManagement.ALARM_ALL_ON) == AlarmManagement.ALARM_ALL_OFF){
                            /// 전체 알람 껐을때
                            alarmManagement.generateNotification(itemList.get(i));
                        } else{
                            alarmManagement.PopupMsg(itemList.get(i));
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
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            // 비콘이 감지되면 해당 함수가 호출된다. Collection<Beacon> beacons에는 감지된 비콘의 리스트가,
            // region에는 비콘들에 대응하는 Region 객체가 들어온다.
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() == 0) {
                    Toast toast5 = Toast.makeText(getApplicationContext(), "탈주", Toast.LENGTH_LONG);
                    toast5.show();
                } else if (beacons.size() > 0) {
                    Toast toast5 = Toast.makeText(getApplicationContext(), "있음.", Toast.LENGTH_LONG);
                    toast5.show();
                    beaconList.clear();
                    for (Beacon beacon : beacons) {
                        beaconList.add(beacon);
                    }
                }

            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));

        } catch (RemoteException e) {
        }
    }


    //============================================================================================================================================
    public void beaconInit(){
        beaconManager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);

    }

    Handler beaconHandler = new Handler() {
        public void handleMessage(Message msg) {
            // 비콘의 아이디와 거리를 측정하여 textView에 넣는다.
            for (Beacon beacon : beaconList) {
                if(beaconList.contains(beacon)){
                    Log.i(TAG, "connect");
                    isConnect = "connect";
                }else isConnect = "disconnect";

                Log.i(TAG, "UUID : " + beacon.getServiceUuid() + " / " + "Major : " + beacon.getId2() + " / " + "Minor : " + beacon.getId3() + " / " + "Battery : " + beacon.getDataFields().get(0) + " / " + "Distance : " + Double.parseDouble(String.format("%.3f", beacon.getDistance())) +
                        "신호세기 : " + beacon.getTxPower() + " / " + "통신간격 : " + INTERVAL + "sec " + " / " + "연결상태 : " + isConnect + " / " + "m\n");

                compareDistance(beacon);
            }

            // 자기 자신을 INTERVAL 마다 호출
            beaconHandler.sendEmptyMessageDelayed(0, INTERVAL);
        }
    };
}
