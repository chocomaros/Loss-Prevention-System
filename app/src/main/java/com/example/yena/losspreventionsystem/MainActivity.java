package com.example.yena.losspreventionsystem;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    public static final int ALARM_ALL_OFF = 0, ALARM_ALL_ON = 1;
    private static final int REQUEST_PUT_ITEM_TO_GROUP = 0;
    private ImageButton btEdit, btAlarm;
    private FloatingActionButton fab;
    private int alarmControl = ALARM_ALL_ON;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = this.getSharedPreferences(LPSSharedPreferences.NAME, 0);
        alarmControl = pref.getInt(LPSSharedPreferences.ALARM_CONTROL,ALARM_ALL_ON);

        btEdit = (ImageButton) findViewById(R.id.ib_edit);
        btAlarm = (ImageButton) findViewById(R.id.ib_alarm);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        if(alarmControl == ALARM_ALL_OFF) {
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
                    btEdit.setVisibility(View.INVISIBLE);
                    fab.setVisibility(View.INVISIBLE);
                } else{
                    btEdit.setVisibility(View.VISIBLE);
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

        btEdit.setOnClickListener(new View.OnClickListener() {
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
                alarmControl = pref.getInt(LPSSharedPreferences.ALARM_CONTROL,ALARM_ALL_ON);
                if(alarmControl == ALARM_ALL_ON){
                    btAlarm.setImageResource(R.drawable.ic_alarm_off);
                    alarmControl = ALARM_ALL_OFF;
                    editor.putInt(LPSSharedPreferences.ALARM_CONTROL,alarmControl);
                } else{
                    btAlarm.setImageResource(R.drawable.ic_alarm_on);
                    alarmControl = ALARM_ALL_ON;
                    editor.putInt(LPSSharedPreferences.ALARM_CONTROL,alarmControl);
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
