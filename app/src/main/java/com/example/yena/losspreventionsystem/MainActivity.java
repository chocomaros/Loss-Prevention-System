package com.example.yena.losspreventionsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private static final int REQUEST_PUT_ITEM_TO_GROUP = 0;
    private ImageButton btPutGroup, btAlarm;
    private FloatingActionButton fab;
    private int alarmControl = AlarmManagement.ALARM_ALL_ON;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final BluetoothChecker bluetoothChecker = new BluetoothChecker(this);
        bluetoothChecker.enableBluetooth();


        pref = this.getSharedPreferences(LPSSharedPreferences.NAME, 0);
        alarmControl = pref.getInt(LPSSharedPreferences.ALARM_CONTROL,AlarmManagement.ALARM_ALL_ON);

        btPutGroup = (ImageButton) findViewById(R.id.ib_put_in_group);
        btAlarm = (ImageButton) findViewById(R.id.ib_alarm);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        if(alarmControl == AlarmManagement.ALARM_ALL_OFF) {
            btAlarm.setImageResource(R.drawable.ic_alarm_off);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tl_main);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_information));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_group));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_location));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_setting));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);



        final ViewPager viewPager = (ViewPager) findViewById(R.id.vp_main);
        final TabAdapter adapter = new TabAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() != 0) {
                    btPutGroup.setVisibility(View.INVISIBLE);
                    fab.setVisibility(View.INVISIBLE);
                } else{
                    btPutGroup.setVisibility(View.VISIBLE);
                    fab.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        btPutGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,PutItemToGroupActivity.class);
                startActivityForResult(intent, REQUEST_PUT_ITEM_TO_GROUP);
            }
        });


        btAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = pref.edit();
                alarmControl = pref.getInt(LPSSharedPreferences.ALARM_CONTROL,AlarmManagement.ALARM_ALL_ON);
                if(alarmControl == AlarmManagement.ALARM_ALL_ON){
                    btAlarm.setImageResource(R.drawable.ic_alarm_off);
                    alarmControl = AlarmManagement.ALARM_ALL_OFF;
                    editor.putInt(LPSSharedPreferences.ALARM_CONTROL,alarmControl);
                    Toast.makeText(getApplicationContext(),"알람 꺼짐",Toast.LENGTH_SHORT).show();
                } else{
                    btAlarm.setImageResource(R.drawable.ic_alarm_on);
                    alarmControl = AlarmManagement.ALARM_ALL_ON;
                    editor.putInt(LPSSharedPreferences.ALARM_CONTROL,alarmControl);
                    Toast.makeText(getApplicationContext(),"알람 켜짐",Toast.LENGTH_SHORT).show();
                }
                editor.commit();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_PUT_ITEM_TO_GROUP){
            if(resultCode == RESULT_OK){
//                ArrayList<ItemInfo> itemsInGroup = new ArrayList<ItemInfo>();
//                itemsInGroup = (ArrayList<ItemInfo>)getIntent().getSerializableExtra(PutItemToGroupActivity.ITEMS_IN_GROUP);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
