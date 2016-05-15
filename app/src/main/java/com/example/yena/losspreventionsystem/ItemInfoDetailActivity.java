package com.example.yena.losspreventionsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ItemInfoDetailActivity extends AppCompatActivity {

    private ItemInfo itemInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info_detail);

        Intent intent = getIntent();
        itemInfo = (ItemInfo)intent.getSerializableExtra("ItemInfo");
    }
}
