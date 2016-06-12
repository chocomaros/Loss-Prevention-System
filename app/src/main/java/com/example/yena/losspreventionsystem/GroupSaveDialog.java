package com.example.yena.losspreventionsystem;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * Created by yena on 2016-05-08.
 */
public class GroupSaveDialog extends Dialog {

    private ArrayList<GroupInfo> groupList;
    private Button btNewGroup, btCancel, btSave;
    private GroupInfo savedGroup;
    private boolean saveButtonClicked = false;
    private RecyclerView recyclerView;
    private GroupAdapter adapter;
    private SharedPreferences pref;


    public GroupSaveDialog(Context context){
        super(context);
        pref = context.getSharedPreferences(LPSSharedPreferences.NAME, 0);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_group_save);

        recyclerView = (RecyclerView) findViewById(R.id.rv_dialog_group);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        groupList = LPSDAO.getGroupInfo(context);
        adapter = new GroupAdapter(groupList,InformationAdapter.PUT_GROUP);
        recyclerView.setAdapter(adapter);

        btNewGroup = (Button)findViewById(R.id.bt_new_group);
        btCancel = (Button)findViewById(R.id.bt_dialog_group_cancel);
        btSave = (Button)findViewById(R.id.bt_dialog_group_save);

        btNewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGroupDialog();
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButtonClicked = false;
                cancel();
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter.groupList.size() == 0){
                    printAlertDialog("그룹 체크 확인", "새 그룹을 만들고 저장해주세요.");
                } else{
                    for(int i=0; i<adapter.groupList.size(); i++){
                        if(adapter.groupList.get(i).radiobuttonChecked){
                            savedGroup = adapter.groupList.get(i);
                            saveButtonClicked = true;
                            dismiss();
                            break;
                        } else{
                            if(i == adapter.groupList.size() - 1){
                                //라디오버튼 체크된게 없을 때
                                printAlertDialog("그룹 체크 확인", "체크된 그룹이 있는지 확인해주세요.");
                            }
                        }
                    }
                }
            }
        });
    }

    void printAlertDialog(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

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

    void newGroupDialog(){
        final EditText input = new EditText(getContext());
        input.setSingleLine();
        input.setTextColor(Color.WHITE);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(input);

        builder.setTitle("새 그룹 만들기")
                .setMessage("그룹 이름을 입력해주세요.")
                .setCancelable(true)
                .setNegativeButton("취소", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if(input.getText().toString().isEmpty()){
                            printAlertDialog("이름 확인","그룹 이름을 입력하지않았습니다.");
                        } else{
                            final GroupInfo newGroup = new GroupInfo();
                            newGroup.name = input.getText().toString();
                            //LPSDAO.insertGroupInfo(getContext(),newGroup);


                            new AsyncTask<String, String, Integer>() {
                                @Override
                                protected Integer doInBackground(String... params) {

                                    return NetworkManager.addGroup(getContext(), pref.getString(LPSSharedPreferences.USER_ID, ""), newGroup.name, LPSDAO.insertGroupInfo(getContext(), newGroup));

                                }
                                //메인쓰레드로
                                @Override
                                protected void onPostExecute(Integer aBoolean) {

                                    adapter.groupList = LPSDAO.getGroupInfo(getContext());
                                    adapter.notifyDataSetChanged();


                                }
                            }.execute("");

                            adapter.groupList = LPSDAO.getGroupInfo(getContext());
                            adapter.notifyDataSetChanged();


                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public GroupInfo getSavedGroup(){
        return savedGroup;
    }

    public boolean getSaveButtonClicked(){
        return saveButtonClicked;
    }

}
