package com.example.yena.losspreventionsystem;

/**
 * Created by yena on 2016-04-16.
 */
public class DBTable {
    public static class ItemInfoTable{
        public static final String TABLE_NAME = "ItemInfo",
                BEACON_ID = "BEACON_ID",
                ITEM_NAME = "ITEM_NAME",
                ITEM_DISTANCE = "ITEM_DISTANCE",
                ITEM_ALARM_STATUS = "ITEM_ALARM_STATUS";
    }
    public static class GroupInfoTable{
        public static final String TABLE_NAME = "GroupInfo",
                GROUP_ID = "GROUP_ID",
                GROUP_NAME = "GROUP_NAME";
    }
    public static class ItemGroupTable{
        public static final String TABLE_NAME = "ItemGroup",
                ITEM_GROUP_ID = "ITEM_GROUP_ID",
                BEACON_ID = "BEACON_ID",
                GROUP_ID = "GROUP_ID";
    }
}
