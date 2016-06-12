package com.example.yena.losspreventionsystem;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.security.acl.Group;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class GroupInfoDetailActivity extends AppCompatActivity {

    private static final int GROUP_DELETE = 1, ITEM_DELETE = 2;

    private GroupInfo groupInfo;
    private TextView tvName;
    private RadioGroup rgAlarm;
    private ImageButton ibDelete;
    private ListView lvItemsInGroup;
    private ArrayList<ItemInfo> itemsInGroup;
    private ArrayAdapter<String> adapter;
    private ItemInfo deleteItemInfo;
    private SharedPreferences pref;

    private int groupAlarmStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pref = getSharedPreferences(LPSSharedPreferences.NAME, 0);

        Intent intent = getIntent();
        groupInfo = (GroupInfo)intent.getSerializableExtra("GroupInfo");

        tvName = (TextView)findViewById(R.id.tv_group_detail_name);
        rgAlarm = (RadioGroup)findViewById(R.id.rg_group_detail_alarm);
        ibDelete = (ImageButton)findViewById(R.id.ib_delete_group);
        lvItemsInGroup = (ListView)findViewById(R.id.lv_group_detail);

        tvName.setText(groupInfo.name);
        itemsInGroup = LPSDAO.getItemListOfGroup(this,groupInfo);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        for(int i=0; i<itemsInGroup.size(); i++){
            adapter.add(itemsInGroup.get(i).name);
        }
        lvItemsInGroup.setAdapter(adapter);
        lvItemsInGroup.setOnItemClickListener(itemClickListener);

        ibDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printDeleteAlertDialog("정말로 이 그룹("+groupInfo.name+")을 삭제하시겠습니까?",GROUP_DELETE);
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

    @Override
    protected void onResume() {
        super.onResume();


    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            deleteItemInfo = itemsInGroup.get(position);
            printDeleteAlertDialog("이 물건("+deleteItemInfo.name+")을 그룹에서 삭제하시겠습니까?",ITEM_DELETE);
        }
    };

    void printDeleteAlertDialog(String message, int deleteType){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final int type = deleteType;
        builder.setTitle("삭제 확인")
                .setMessage(message)
                .setCancelable(true)
                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if(type == GROUP_DELETE){

                            new AsyncTask<String, String, Integer>() {
                                @Override
                                protected Integer doInBackground(String... params) {

                                    return NetworkManager.allGroupDeleteItemGroup(getApplicationContext(), pref.getString(LPSSharedPreferences.USER_ID, ""), groupInfo.id);

                                }
                                //메인쓰레드로
                                @Override
                                protected void onPostExecute(Integer aBoolean) {

                                }
                            }.execute("");

                            new AsyncTask<String, String, Integer>() {
                                @Override
                                protected Integer doInBackground(String... params) {

                                    return NetworkManager.deleteGroup(getApplicationContext(), pref.getString(LPSSharedPreferences.USER_ID, ""), groupInfo.id);

                                }
                                //메인쓰레드로
                                @Override
                                protected void onPostExecute(Integer aBoolean) {
                                    LPSDAO.deleteItemGroup(getApplicationContext(), groupInfo);
                                }
                            }.execute("");

                            LPSDAO.deleteItemGroup(getApplicationContext(), groupInfo);
                            LPSDAO.deleteGroupInfo(getApplicationContext(), groupInfo);
                            dialog.cancel();
                            finish();
                        } else if(type == ITEM_DELETE){
                            new AsyncTask<String, String, Integer>() {
                                @Override
                                protected Integer doInBackground(String... params) {

                                    return NetworkManager.deleteItemGroup(getApplicationContext(), pref.getString(LPSSharedPreferences.USER_ID, ""), LPSDAO.getItemGroupID(getApplicationContext(),groupInfo,deleteItemInfo));

                                }
                                //메인쓰레드로
                                @Override
                                protected void onPostExecute(Integer aBoolean) {
                                }
                            }.execute("");
                            LPSDAO.deleteItemGroup(getApplicationContext(), deleteItemInfo, groupInfo);

                            itemsInGroup = LPSDAO.getItemListOfGroup(getApplicationContext(),groupInfo);
                            adapter.clear();
                            for(int i=0; i<itemsInGroup.size(); i++){
                                adapter.add(itemsInGroup.get(i).name);
                            }
                            adapter.notifyDataSetChanged();
                            dialog.cancel();
                        }
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
                        //// TODO: 2016-06-04  알람업데이트 
                        for(int i=0; i<itemsInGroup.size(); i++){
                            itemsInGroup.get(i).alarmStatus = groupAlarmStatus;
                            final String beaconID = itemsInGroup.get(i).beaconID;
                            final String itemName = itemsInGroup.get(i).name;

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA);
                            final String lossTime = sdf.format(new Date(itemsInGroup.get(i).lossTime.getTimeInMillis()));
                            final int lock = itemsInGroup.get(i).lockToInt();

                            new AsyncTask<String, String, Integer>() {
                                @Override
                                protected Integer doInBackground(String... params) {

                                    return NetworkManager.editItem(getApplicationContext(),pref.getString(LPSSharedPreferences.USER_ID, ""),beaconID, itemName, lossTime, groupAlarmStatus, lock);

                                }
                                //메인쓰레드로
                                @Override
                                protected void onPostExecute(Integer aBoolean) {
                                }
                            }.execute("");

                            LPSDAO.updateItemInfoAlarmStatus(getApplicationContext(),itemsInGroup.get(i));
                        }
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void printNameChangeAlertDialog(){
        final EditText input = new EditText(getApplicationContext());
        input.setText(groupInfo.name);
        input.setSingleLine();
        input.setTextColor(Color.WHITE);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(input);

        builder.setTitle("이름 수정")
                .setMessage("그룹 이름을 수정해주세요.")
                .setCancelable(true)
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        groupInfo.name = input.getText().toString();
                        
                        new AsyncTask<String, String, Integer>() {
                            @Override
                            protected Integer doInBackground(String... params) {

                                return NetworkManager.editGroup(getApplication(), pref.getString(LPSSharedPreferences.USER_ID, ""), groupInfo.name, groupInfo.id);

                            }
                            //메인쓰레드로
                            @Override
                            protected void onPostExecute(Integer aBoolean) {
                                LPSDAO.updateGroupInfoName(getApplicationContext(),groupInfo);
                            }
                        }.execute("");
                        tvName.setText(groupInfo.name);
                        
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
