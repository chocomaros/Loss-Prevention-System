package com.example.yena.losspreventionsystem;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class RegisterActivity extends AppCompatActivity implements BeaconConsumer{

    private EditText etName;
    private TextView tvBeaconID;
    private Button btCheck, btRegister;
    private RadioGroup rgAlarmSelect;

    private int alarmStatusSelect;

    List<Beacon> beaconList = new ArrayList<>();
    BeaconManager beaconManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etName = (EditText)findViewById(R.id.et_register_name);
        tvBeaconID = (TextView)findViewById(R.id.tv_register_beacon_id);
        rgAlarmSelect = (RadioGroup)findViewById(R.id.rg_select_alarm);
        btCheck = (Button)findViewById(R.id.bt_check_id);
        btRegister = (Button)findViewById(R.id.bt_register);

        alarmStatusSelect = AlarmManagement.ALARM_VIBRATION;


        beaconManager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);


        btCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sendEmptyMessage(0);
            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        rgAlarmSelect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_alarm_disable :
                        alarmStatusSelect = AlarmManagement.DISABLE;
                        break;
                    case R.id.rb_alarm_off :
                        alarmStatusSelect = AlarmManagement.ALARM_OFF;
                        break;
                    case R.id.rb_alarm_silent :
                        alarmStatusSelect = AlarmManagement.ALARM_SILENT;
                        break;
                    case R.id.rb_alarm_vibration :
                        alarmStatusSelect = AlarmManagement.ALARM_VIBRATION;
                        break;
                    case R.id.rb_alarm_sound :
                        alarmStatusSelect = AlarmManagement.ALARM_SOUND;
                        break;
                    case R.id.rb_alarm_sound_vibration :
                        alarmStatusSelect = AlarmManagement.ALARM_SOUND_VIBRATION;
                        break;
                }
            }
        });
    }
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            tvBeaconID.setText("");

            // 비콘의 아이디와 거리를 측정하여 textView에 넣는다.
            for(Beacon beacon : beaconList){
                tvBeaconID.append("\n"+beacon.getId1());
            }

            // 자기 자신을 1초마다 호출
            handler.sendEmptyMessageDelayed(0, 100);
        }
    };


    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            // 비콘이 감지되면 해당 함수가 호출된다. Collection<Beacon> beacons에는 감지된 비콘의 리스트가,
            // region에는 비콘들에 대응하는 Region 객체가 들어온다.
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
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
}
