package com.example.yena.losspreventionsystem;

import android.database.Cursor;

import java.util.ArrayList;

/**
 * Created by yena on 2016-04-11.
 */
public class GroupInfo {

    int id;
    String name;

    public GroupInfo(){

    }
    public GroupInfo(int id, String name){
        this.id = id;
        this.name = name;
    }

    public GroupInfo(Cursor cursor){
        id = cursor.getInt(0);
        name = cursor.getString(1);
    }
}
