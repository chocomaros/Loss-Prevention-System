package com.example.yena.losspreventionsystem;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/**
 * Created by yena on 2016-05-08.
 */
public class GroupSaveDialog extends Dialog {

    Button btNewGroup, btCancel, btSave;


    public GroupSaveDialog(Context context){
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_group_save);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_dialog_group);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        btNewGroup = (Button)findViewById(R.id.bt_new_group);
        btCancel = (Button)findViewById(R.id.bt_dialog_group_cancel);
        btSave = (Button)findViewById(R.id.bt_dialog_group_save);

        btNewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
