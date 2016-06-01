package com.example.yena.losspreventionsystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ItemInfoDetailActivity extends AppCompatActivity {

    private ItemInfo itemInfo;
    private TextView tvName, tvBeaconID, tvLossTime;
    private RadioGroup rgAlarm;
    private ImageButton ibDelete, ibLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        ItemInfo getItemInfo = (ItemInfo)intent.getSerializableExtra("ItemInfo");
        ArrayList<ItemInfo> itemList = LPSDAO.getItemInfo(this);
        for(int i=0; i< itemList.size(); i++){
            if(itemList.get(i).beaconID.equals(getItemInfo.beaconID)){
                itemInfo = itemList.get(i);
            }
        }

        tvName = (TextView)findViewById(R.id.tv_item_detail_name);
        tvBeaconID = (TextView)findViewById(R.id.tv_item_detail_beacon_id);
        tvLossTime = (TextView)findViewById(R.id.tv_item_detail_loss_time);
        rgAlarm = (RadioGroup)findViewById(R.id.rg_item_detail_alarm);
        ibDelete = (ImageButton)findViewById(R.id.ib_delete_item);
        ibLock = (ImageButton)findViewById(R.id.ib_lock_item);

        setLockImage();

        tvName.setText(itemInfo.name);
        tvBeaconID.setText(itemInfo.beaconID);
        rgAlarm.check(firstAlarmStatus());
        if(itemInfo.lossTime.getTimeInMillis() == 0){
            tvLossTime.setText("x");
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA);

            tvLossTime.setText(sdf.format(new Date(itemInfo.lossTime.getTimeInMillis())));
        }

        ibLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemInfo.lock){
                    itemInfo.lock = false;
                } else{
                    itemInfo.lock = true;
                }
                LPSDAO.updateItemInfoLock(getApplicationContext(), itemInfo);
                setLockImage();
            }
        });

        ibDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printDeleteAlertDialog();
            }
        });

        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printNameChangeAlertDialog();
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
                .setMessage("정말로 이 물건("+itemInfo.name+")을 삭제하시겠습니까?")
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

    void printNameChangeAlertDialog(){
        final EditText input = new EditText(getApplicationContext());
        input.setText(itemInfo.name);
        input.setSingleLine();
        input.setTextColor(Color.WHITE);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(input);

        builder.setTitle("이름 수정")
                .setMessage("물건 이름을 수정해주세요.")
                .setCancelable(true)
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        itemInfo.name = input.getText().toString();
                        LPSDAO.updateItemInfoName(getApplicationContext(), itemInfo);
                        tvName.setText(itemInfo.name);
                        dialog.cancel();
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

    void setLockImage(){
        if(itemInfo.lock){
            ibLock.setBackgroundResource(R.drawable.ic_lock);
        } else{
            ibLock.setBackgroundResource(R.drawable.ic_unlock);
        }
    }

}
