package com.example.yena.losspreventionsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GroupInfoDetailActivity extends AppCompatActivity {

    private GroupInfo groupInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info_detail);

        Intent intent = getIntent();
        groupInfo = (GroupInfo)intent.getSerializableExtra("GroupInfo");
    }
}
