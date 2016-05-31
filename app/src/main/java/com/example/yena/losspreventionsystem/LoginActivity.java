package com.example.yena.losspreventionsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private EditText etInputID, etInputPW;
    //private CheckBox cbAutoLogin;
    private String id, password;
    private Button btLogin, btSignUp;

    private SharedPreferences pref;
    private Boolean autoLoginCheck = false;

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
                if (correctLogin()) {
                    SharedPreferences.Editor editor = pref.edit();

                    id = etInputID.getText().toString();
                    password = etInputPW.getText().toString();

                    editor.putString(LPSSharedPreferences.USER_ID, id);
                    editor.putString(LPSSharedPreferences.USER_PW, password);
                    editor.putBoolean(LPSSharedPreferences.AUTO_LOGIN, true);
                    editor.commit();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
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

    Boolean correctLogin(){
        //TODO 서버에서 정보 확인하는거?
        return true;
    }
}
