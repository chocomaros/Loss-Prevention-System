package com.example.yena.losspreventionsystem;

import java.util.ArrayList;

/**
 * Created by yena on 2016-04-11.
 */
public class GroupInfo {

    int id;
    String name;
    ArrayList<Integer> itemList;

    public GroupInfo(){

    }
    public GroupInfo(int id, String name, ArrayList<Integer> itemList){
        this.id = id;
        this.name = name;
        this.itemList = itemList;
    }
}
