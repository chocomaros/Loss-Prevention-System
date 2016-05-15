package com.example.yena.losspreventionsystem;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName;
    private TextView tvBeaconID;
    private Button btCheck, btRegister;
    private RadioGroup rgAlarmSelect;

    private int alarmStatusSelect;

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

        btCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

//        ArrayList<ItemInfo> itemList;
//        ItemInfo itemInfo1=new ItemInfo("gg","진동",3,3);
//        ItemInfo itemInfo2=new ItemInfo("asdf","진동+소리",4,5);
//        LPSDAO.insertItemInfo(this, itemInfo1);
//        LPSDAO.insertItemInfo(this, itemInfo2);
//        itemList = LPSDAO.getItemInfo(this);
//        AlarmManagement alarmManagement;
//        alarmManagement = new AlarmManagement(this);
//        alarmManagement.PopupMsg(itemList.get(1));
//        alarmManagement.generateNotification(itemList.get(1));
    }

}
