package com.example.yena.losspreventionsystem;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PutItemToGroupActivity extends AppCompatActivity {

    public static final String ITEMS_IN_GROUP = "ItemsInGroup";

    private ArrayList<ItemInfo> itemList;
    private Button btCancel, btSave;
    private GroupSaveDialog groupSaveDialog;
    private Context context;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put_item_to_group);
        context = getApplicationContext();
        pref = context.getSharedPreferences(LPSSharedPreferences.NAME, 0);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_information_put_group);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        itemList = LPSDAO.getItemInfo(this);
        final InformationAdapter adapter = new InformationAdapter(itemList,InformationAdapter.PUT_GROUP);
        recyclerView.setAdapter(adapter);

        btCancel = (Button)findViewById(R.id.bt_put_to_group_cancel);
        btSave = (Button)findViewById(R.id.bt_put_to_group_save);

        groupSaveDialog = new GroupSaveDialog(this);

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<ItemInfo> itemsInGroup = new ArrayList<>();
                for (int i = 0; i < itemList.size(); i++) {
                    if (itemList.get(i).checked) {
                        itemsInGroup.add(itemList.get(i));
                    }
                }
                Log.d("PutItemToGroupActivity ", "checked item # " + itemsInGroup.size());

                if (itemsInGroup.size() == 0) {
                    printAlertDialog("체크 확인", "체크된 물건이 없습니다.");
                } else {
                    groupSaveDialog.show();

                    // 다이얼로그 없어졌을때
                    groupSaveDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            if(groupSaveDialog.getSaveButtonClicked()){
                                ArrayList<ItemInfo> items;
                                items = LPSDAO.getItemListOfGroup(getApplicationContext(),groupSaveDialog.getSavedGroup());
                                if(items.size() == 0){
                                    int i;
                                    for(i=0; i<itemsInGroup.size(); i++){


                                        final String beaconID = itemsInGroup.get(i).beaconID;
                                        final ItemInfo tempItem = itemsInGroup.get(i);

                                        new AsyncTask<String, String, Integer>() {
                                            @Override
                                            protected Integer doInBackground(String... params) {
                                                return NetworkManager.addItemGroup(getApplicationContext(), pref.getString(LPSSharedPreferences.USER_ID, ""), LPSDAO.insertItemGroup(getApplicationContext(),tempItem ,groupSaveDialog.getSavedGroup()), groupSaveDialog.getSavedGroup().id, beaconID);
                                            }

                                            //메인쓰레드로
                                            @Override
                                            protected void onPostExecute(Integer aBoolean) {
                                                if (aBoolean==1){

                                                }
                                            }
                                        }.execute("");

                                    }
                                } else{
                                    for(int i=0;i<itemsInGroup.size();i++){
                                        for(int j=0; j<items.size(); j++){
                                            if(itemsInGroup.get(i).beaconID.equals(items.get(j).beaconID)){
                                                break;
                                            }
                                            if(j == items.size() -1){
                                                LPSDAO.insertItemGroup(context,itemsInGroup.get(i),groupSaveDialog.getSavedGroup());

                                                final String beaconID = itemsInGroup.get(i).beaconID;
                                                final ItemInfo tempItem = itemsInGroup.get(i);
                                                new AsyncTask<String, String, Integer>() {
                                                    @Override
                                                    protected Integer doInBackground(String... params) {
                                                        return NetworkManager.addItemGroup(getApplicationContext(), pref.getString(LPSSharedPreferences.USER_ID, ""),LPSDAO.insertItemGroup(getApplicationContext(),tempItem ,groupSaveDialog.getSavedGroup()), groupSaveDialog.getSavedGroup().id, beaconID);
                                                    }

                                                    //메인쓰레드로
                                                    @Override
                                                    protected void onPostExecute(Integer aBoolean) {
                                                        if (aBoolean==1){

                                                        }
                                                    }
                                                }.execute("");
                                            }
                                        }
                                    }
                                }
                                finish();
                            }
                        }
                    });

                    //다이얼로그 취소 누를때
                    groupSaveDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                        }
                    });

                    Intent intent = new Intent(PutItemToGroupActivity.this, GroupSaveDialog.class);
                    intent.putExtra(ITEMS_IN_GROUP, itemsInGroup);
                    //setResult(RESULT_OK,intent);
                }
            }
        });
    }

    void printAlertDialog(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
