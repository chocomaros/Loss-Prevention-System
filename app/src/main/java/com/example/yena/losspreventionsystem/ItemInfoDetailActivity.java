package com.example.yena.losspreventionsystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ItemInfoDetailActivity extends AppCompatActivity {

    private ItemInfo itemInfo;
    private TextView tvName, tvBeaconID, tvLossTime;
    private RadioGroup rgAlarm;
    private ImageButton ibEdit, ibDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        itemInfo = (ItemInfo)intent.getSerializableExtra("ItemInfo");

        tvName = (TextView)findViewById(R.id.tv_item_detail_name);
        tvBeaconID = (TextView)findViewById(R.id.tv_item_detail_beacon_id);
        tvLossTime = (TextView)findViewById(R.id.tv_item_detail_loss_time);
        rgAlarm = (RadioGroup)findViewById(R.id.rg_item_detail_alarm);
        ibEdit = (ImageButton)findViewById(R.id.ib_edit_item);
        ibDelete = (ImageButton)findViewById(R.id.ib_delete_item);

        tvName.setText(itemInfo.name);
        tvBeaconID.setText(itemInfo.beaconID);
        rgAlarm.check(firstAlarmStatus());

        if(itemInfo.lossTime.getTimeInMillis() == 0){
            tvLossTime.setText("x");
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA);
            tvLossTime.setText(sdf.format(new Date(itemInfo.lossTime.getTimeInMillis())));
        }

        ibEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ibDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printDeleteAlertDialog();
            }
        });

        rgAlarm.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_alarm_disable_detail_item:
                        itemInfo.alarmStatus = AlarmManagement.DISABLE;
                        break;
                    case R.id.rb_alarm_off_detail_item:
                        itemInfo.alarmStatus = AlarmManagement.ALARM_OFF;
                        break;
                    case R.id.rb_alarm_silent_detail_item:
                        itemInfo.alarmStatus = AlarmManagement.ALARM_SILENT;
                        break;
                    case R.id.rb_alarm_vibration_detail_item:
                        itemInfo.alarmStatus = AlarmManagement.ALARM_VIBRATION;
                        break;
                    case R.id.rb_alarm_sound_detail_item:
                        itemInfo.alarmStatus = AlarmManagement.ALARM_SOUND;
                        break;
                    case R.id.rb_alarm_sound_vibration_detail_item:
                        itemInfo.alarmStatus = AlarmManagement.ALARM_SOUND_VIBRATION;
                        break;
                }
                LPSDAO.updateItemInfoAlarmStatus(getApplicationContext(),itemInfo);
            }
        });
    }

    void printDeleteAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("삭제 확인")
                .setMessage("정말로 이 물건을 삭제하시겠습니까?")
                .setCancelable(true)
                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        LPSDAO.deleteItemGroup(getApplicationContext(), itemInfo);
                        LPSDAO.deleteItemInfo(getApplicationContext(), itemInfo);
                        dialog.cancel();
                        finish();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    int firstAlarmStatus(){
        switch (itemInfo.alarmStatus) {
            case AlarmManagement.DISABLE :
                return R.id.rb_alarm_disable_detail_item;
            case AlarmManagement.ALARM_OFF :
                return R.id.rb_alarm_off_detail_item;
            case AlarmManagement.ALARM_SILENT :
                return R.id.rb_alarm_silent_detail_item;
            case AlarmManagement.ALARM_VIBRATION :
                return R.id.rb_alarm_vibration_detail_item;
            case AlarmManagement.ALARM_SOUND :
                return R.id.rb_alarm_sound_detail_item;
            case AlarmManagement.ALARM_SOUND_VIBRATION :
                return R.id.rb_alarm_sound_vibration_detail_item;
        }
        return 0;
    }

}