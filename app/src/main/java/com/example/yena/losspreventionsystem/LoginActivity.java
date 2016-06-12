package com.example.yena.losspreventionsystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private static final int LOGIN_TERM = 1000;

    private EditText etInputID, etInputPW;
    //private CheckBox cbAutoLogin;
    private String id, password;
    private Button btLogin, btSignUp;

    private SharedPreferences pref;
    private Boolean autoLoginCheck = false;
    List<ItemInfo> items=new ArrayList<>();
    List<GroupInfo> groups=new ArrayList<>();
    List<ItemGroup> itemgroups = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);

        etInputID = (EditText)findViewById(R.id.et_id);
        etInputPW = (EditText)findViewById(R.id.et_password);
       // cbAutoLogin = (CheckBox)findViewById(R.id.cb_auto_login);
        btLogin = (Button)findViewById(R.id.bt_login);
        btSignUp = (Button)findViewById(R.id.bt_sign_up);

        pref = getSharedPreferences(LPSSharedPreferences.NAME,0);

        if(pref.getBoolean(LPSSharedPreferences.AUTO_LOGIN, false)){ /// 이미 자동로그인 되어있을 경우
            id = pref.getString(LPSSharedPreferences.USER_ID, "");
            password = pref.getString(LPSSharedPreferences.USER_PW, "");
            etInputID.setText(id);
            etInputPW.setText(password);
           // cbAutoLogin.setChecked(true);
            //TODO 서버에 로그인 확인해야하나?

            new AsyncTask<String, String, Integer>() {
                @Override
                protected Integer doInBackground(String... params) {

                    return NetworkManager.Login(getApplicationContext(),id,password);
                }

                //메인쓰레드로
                @Override
                protected void onPostExecute(Integer aBoolean) {
                    if (aBoolean==1){

                        SharedPreferences.Editor editor = pref.edit();

                        Log.d("login", "id : " + id);

                        editor.putString(LPSSharedPreferences.USER_ID, id);
                        editor.putString(LPSSharedPreferences.USER_PW, password);
                        editor.putBoolean(LPSSharedPreferences.AUTO_LOGIN, true);
                        editor.commit();

                        Log.d("login", " pref id : " + pref.getString(LPSSharedPreferences.USER_ID, ""));

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    }else{

                        printAlertDialog("로그인 실패", "아이디나 비밀번호가 올바르지 않습니다.");
                    }
                }
            }.execute("");


            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

//        cbAutoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    autoLoginCheck = true;
//                }
//                else{
//                    autoLoginCheck = false;
//                }
//            }
//        });

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                id = etInputID.getText().toString();
                password = etInputPW.getText().toString();

                new AsyncTask<String, String, Integer>() {
                    @Override
                    protected Integer doInBackground(String... params) {

                        return NetworkManager.Login(getApplicationContext(),id,password);
                    }

                    //메인쓰레드로
                    @Override
                    protected void onPostExecute(Integer aBoolean) {
                        if (aBoolean==1){

                            SharedPreferences.Editor editor = pref.edit();

                            Log.d("login", "id : " + id);

                            editor.putString(LPSSharedPreferences.USER_ID, id);
                            editor.putString(LPSSharedPreferences.USER_PW, password);
                            editor.putBoolean(LPSSharedPreferences.AUTO_LOGIN, true);
                            editor.commit();

                            Log.d("login", " pref id : " + pref.getString(LPSSharedPreferences.USER_ID, ""));

                            ///////////////////////////////////////////////////////////////////////////// TODO: DB내용 서버에서가져오기


                            new AsyncTask<String, String, ArrayList<ItemInfo>>() {
                                @Override
                                protected ArrayList<ItemInfo> doInBackground(String... params) {

                                    return NetworkManager.getItem(getApplicationContext(), pref.getString(LPSSharedPreferences.USER_ID, ""));
                                }

                                //메인쓰레드로
                                @Override
                                protected void onPostExecute(ArrayList<ItemInfo> aBoolean ) {

                                    items.addAll(aBoolean);
                                    for(int i = 0; i<items.size(); i++) {
                                        LPSDAO.insertItemInfo(getApplicationContext(), items.get(i));
                                    }
                                    // final InfoDBManager manager = new InfoDBManager(getApplicationContext(), "Info.db", null, 1);
                                    //final StepDBManager manager = new StepDBManager(getApplicationContext(), "step.db", null, 1);
                                    //SQLiteDatabase db = manager.getReadableDatabase();
                                    //Cursor cursor = db.rawQuery("select * from stepTABLE where date ='" + cal.currentTime() + "'", null);
                                    //cursor.moveToFirst()

                                }
                            }.execute("");

                            new AsyncTask<String, String, ArrayList<GroupInfo>>() {
                                @Override
                                protected ArrayList<GroupInfo> doInBackground(String... params) {

                                    return NetworkManager.getGroup(getApplicationContext(), pref.getString(LPSSharedPreferences.USER_ID, ""));
                                }

                                //메인쓰레드로
                                @Override
                                protected void onPostExecute(ArrayList<GroupInfo> aBoolean ) {

                                    groups.addAll(aBoolean);
                                    for(int i = 0; i<groups.size(); i++) {
                                        LPSDAO.insertGroupInfo(getApplicationContext(), groups.get(i));
                                    }

                                }
                            }.execute("");

                            new AsyncTask<String, String, ArrayList<ItemGroup>>() {
                                @Override
                                protected ArrayList<ItemGroup> doInBackground(String... params) {

                                    return NetworkManager.getItemGroup(getApplicationContext(), pref.getString(LPSSharedPreferences.USER_ID, ""));
                                }

                                //메인쓰레드로
                                @Override
                                protected void onPostExecute(ArrayList<ItemGroup> aBoolean ) {

                                    itemgroups.addAll(aBoolean);
                                    for(int i = 0; i<itemgroups.size(); i++) {
                                        LPSDAO.insertItemGroupInfo(getApplicationContext(), itemgroups.get(i));
                                    }

                                }
                            }.execute("");




                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.zoom_enter, R.anim.cycle_7);
                                    finish();
                                }
                            }, LOGIN_TERM);

                        }else{

                                printAlertDialog("로그인 실패", "아이디나 비밀번호가 올바르지 않습니다.");
                            }
                        }
                    }.execute("");
                /*
                if (correctLogin()) {
                    SharedPreferences.Editor editor = pref.edit();



                    editor.putString(LPSSharedPreferences.USER_ID, id);
                    editor.putString(LPSSharedPreferences.USER_PW, password);
                    editor.putBoolean(LPSSharedPreferences.AUTO_LOGIN, true);
                    editor.commit();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }*/
            }
        });

        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
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

    Boolean correctLogin(){
        //TODO 서버에서 정보 확인하는거?
        return false;
    }
}
