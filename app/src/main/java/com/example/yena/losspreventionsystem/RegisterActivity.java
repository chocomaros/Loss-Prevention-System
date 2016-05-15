package com.example.yena.losspreventionsystem;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ArrayList<ItemInfo> itemList;
//        ItemInfo itemInfo1=new ItemInfo("gg","진동",3,3);
//        ItemInfo itemInfo2=new ItemInfo("asdf","진동+소리",4,5);
//        LPSDAO.insertItemInfo(this, itemInfo1);
//        LPSDAO.insertItemInfo(this, itemInfo2);
        itemList = LPSDAO.getItemInfo(this);
        AlarmManagement alarmManagement;
        alarmManagement = new AlarmManagement(this);
        alarmManagement.PopupMsg(itemList.get(1));
        alarmManagement.generateNotification(itemList.get(1));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
