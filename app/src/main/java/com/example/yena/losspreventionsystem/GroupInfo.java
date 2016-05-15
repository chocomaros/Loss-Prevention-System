package com.example.yena.losspreventionsystem;

import android.database.Cursor;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by yena on 2016-04-11.
 */
public class GroupInfo implements Serializable {

    int id;
    String name;
    boolean radiobuttonChecked = false;

    public GroupInfo(){

    }
    public GroupInfo(int id, String name){
        this.id = id;
        this.name = name;
    }

    public GroupInfo(int id, String name, boolean radiobuttonChecked){
        this.id = id;
        this.name = name;
        this.radiobuttonChecked = radiobuttonChecked;
    }

    public GroupInfo(Cursor cursor){
        id = cursor.getInt(0);
        name = cursor.getString(1);
    }
}
