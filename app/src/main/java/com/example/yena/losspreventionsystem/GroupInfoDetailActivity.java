package com.example.yena.losspreventionsystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GroupInfoDetailActivity extends AppCompatActivity {

    private GroupInfo groupInfo;
    private TextView tvName;
    private RadioGroup rgAlarm;
    private ImageButton ibEdit, ibDelete;
    private ListView lvItemsInGroup;
    private ArrayList<ItemInfo> itemsInGroup;

    private int groupAlarmStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        groupInfo = (GroupInfo)intent.getSerializableExtra("GroupInfo");

        tvName = (TextView)findViewById(R.id.tv_group_detail_name);
        rgAlarm = (RadioGroup)findViewById(R.id.rg_group_detail_alarm);
        ibEdit = (ImageButton)findViewById(R.id.ib_edit_group);
        ibDelete = (ImageButton)findViewById(R.id.ib_delete_group);
        lvItemsInGroup = (ListView)findViewById(R.id.lv_group_detail);

        tvName.setText(groupInfo.name);
        itemsInGroup = LPSDAO.getItemListOfGroup(this,groupInfo);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        for(int i=0; i<itemsInGroup.size(); i++){
            adapter.add(itemsInGroup.get(i).name);
        }
        lvItemsInGroup.setAdapter(adapter);

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
                    case R.id.rb_alarm_disable_detail_group:
                        groupAlarmStatus = AlarmManagement.DISABLE;
                        printAlarmChangeAlertDialog();
                        break;
                    case R.id.rb_alarm_off_detail_group:
                        groupAlarmStatus = AlarmManagement.ALARM_OFF;
                        printAlarmChangeAlertDialog();
                        break;
                    case R.id.rb_alarm_silent_detail_group:
                        groupAlarmStatus = AlarmManagement.ALARM_SILENT;
                        printAlarmChangeAlertDialog();
                        break;
                    case R.id.rb_alarm_vibration_detail_group:
                        groupAlarmStatus = AlarmManagement.ALARM_VIBRATION;
                        printAlarmChangeAlertDialog();
                        break;
                    case R.id.rb_alarm_sound_detail_group:
                        groupAlarmStatus = AlarmManagement.ALARM_SOUND;
                        printAlarmChangeAlertDialog();
                        break;
                    case R.id.rb_alarm_sound_vibration_detail_group:
                        groupAlarmStatus = AlarmManagement.ALARM_SOUND_VIBRATION;
                        printAlarmChangeAlertDialog();
                        break;
                }
            }
        });

    }

    void printDeleteAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("삭제 확인")
                .setMessage("정말로 이 그룹을 삭제하시겠습니까?")
                .setCancelable(true)
                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        LPSDAO.deleteItemGroup(getApplicationContext(), groupInfo);
                        LPSDAO.deleteGroupInfo(getApplicationContext(), groupInfo);
                        dialog.cancel();
                        finish();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void printAlarmChangeAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("알람 확인")
                .setMessage("정말로 그룹 내 모든 물건을 이 알람으로 변경하시겠습니까?")
                .setCancelable(true)
                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        for(int i=0; i<itemsInGroup.size(); i++){
                            itemsInGroup.get(i).alarmStatus = groupAlarmStatus;
                            LPSDAO.updateItemInfoAlarmStatus(getApplicationContext(),itemsInGroup.get(i));
                        }
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
