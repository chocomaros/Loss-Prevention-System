package com.example.yena.losspreventionsystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements BeaconConsumer {

    private static final double REGISTER_DISTANCE = 0.5;

    private EditText etName;
    private TextView tvBeaconID;
    private Button btCheck, btRegister;
    private RadioGroup rgAlarmSelect;

    private int alarmStatusSelect;
    private boolean isExisted;

    List<Beacon> beaconList = new ArrayList<>();
    ArrayList<ItemInfo> itemList = new ArrayList<>();
    BeaconManager beaconManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etName = (EditText) findViewById(R.id.et_register_name);
        tvBeaconID = (TextView) findViewById(R.id.tv_register_beacon_id);
        rgAlarmSelect = (RadioGroup) findViewById(R.id.rg_select_alarm);
        btCheck = (Button) findViewById(R.id.bt_check_id);
        btRegister = (Button) findViewById(R.id.bt_register);

        alarmStatusSelect = AlarmManagement.ALARM_VIBRATION;
        isExisted = false;

        itemList = LPSDAO.getItemInfo(this);

        beaconManager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);


        btCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvBeaconID.setText("");

                // 비콘의 아이디와 거리를 측정하여 textView에 넣는다.
                if(beaconList.size() == 0){
                    printAlertDialog("비콘 확인", "주위에 비콘이 없습니다.");
                } else{
                    ArrayList<Beacon> tempList = new ArrayList<Beacon>();
                    for (Beacon beacon : beaconList) {
                        if (REGISTER_DISTANCE > beacon.getDistance()) {
                            tempList.add(beacon);
                            Log.d("들어옴,거리 : ", beacon.getDistance() + "");
                        }
                    }
                    if(tempList.size() == 0){
                        printAlertDialog("비콘 확인", "등록할 물건을 "+REGISTER_DISTANCE+"m 안에 대주십시오.");
                    } else if(tempList.size() == 1){
                        tvBeaconID.setText(tempList.get(0).getId1() + "");
                    } else{
                        printAlertDialog("비콘 확인", REGISTER_DISTANCE +"m 안에 비콘이 많습니다.");
                    }
                }
            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemInfo itemInfo = new ItemInfo(tvBeaconID.getText().toString(), etName.getText().toString(), 0, alarmStatusSelect);
                ArrayList<ItemInfo> itemList;
                Log.d("register item", itemInfo.beaconID + "," + itemInfo.name);
                itemList = LPSDAO.getItemInfo(getApplicationContext());
                for (int i = 0; i < itemList.size(); i++) {
                    if (itemInfo.beaconID.equals(itemList.get(i).beaconID)) {
                        isExisted = true;
                        break;
                    }
                    if(i == itemList.size() - 1){
                        isExisted = false;
                    }
                }
                if (isExisted) {
                    printAlertDialog("등록 오류", "Beacon ID가 이미 존재하는 ID입니다.\n새로운 beacon으로 등록해주세요.");
                } else {
                    if (itemInfo.name.isEmpty()) {
                        printAlertDialog("등록 오류", "이름을 입력해주세요.");
                    } else {
                        if(itemInfo.beaconID.isEmpty()){
                            printAlertDialog("등록 오류", "비콘 아이디를 확인해주세요.");
                        } else{
                            LPSDAO.insertItemInfo(getApplicationContext(), itemInfo);
                            if (alarmStatusSelect != AlarmManagement.DISABLE) {
                                BluetoothChecker bluetoothChecker = new BluetoothChecker();
                                if(bluetoothChecker.btAdapter.isEnabled()){
                                    startService(new Intent(RegisterActivity.this, LPSService.class));
                                }
                            }
                            printAlertDialog("등록 완료", "등록이 완료되었습니다.");
                        }
                    }
                }
            }
        });

        rgAlarmSelect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()

        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_alarm_disable:
                        alarmStatusSelect = AlarmManagement.DISABLE;
                        break;
                    case R.id.rb_alarm_off:
                        alarmStatusSelect = AlarmManagement.ALARM_OFF;
                        break;
                    case R.id.rb_alarm_silent:
                        alarmStatusSelect = AlarmManagement.ALARM_SILENT;
                        break;
                    case R.id.rb_alarm_vibration:
                        alarmStatusSelect = AlarmManagement.ALARM_VIBRATION;
                        break;
                    case R.id.rb_alarm_sound:
                        alarmStatusSelect = AlarmManagement.ALARM_SOUND;
                        break;
                    case R.id.rb_alarm_sound_vibration:
                        alarmStatusSelect = AlarmManagement.ALARM_SOUND_VIBRATION;
                        break;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            // 비콘이 감지되면 해당 함수가 호출된다. Collection<Beacon> beacons에는 감지된 비콘의 리스트가,
            // region에는 비콘들에 대응하는 Region 객체가 들어온다.
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                Log.d("비콘 갯수", "" + beacons.size());
                if (beacons.size() > 0) {
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

    void printAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final String titleName = title;
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                        if (titleName.equals("등록 완료")) {
                            finish();
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
