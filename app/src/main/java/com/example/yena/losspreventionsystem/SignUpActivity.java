package com.example.yena.losspreventionsystem;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    private EditText etInputName, etInputID, etInputPW, etInputPW2;
    private String name, id, password, password2;
    private Button btCheckID, btSignUp;

    private Boolean isCheckedID = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etInputName = (EditText)findViewById(R.id.et_signup_name);
        etInputID = (EditText)findViewById(R.id.et_signup_id);
        etInputPW = (EditText)findViewById(R.id.et_signup_password);
        etInputPW2 = (EditText)findViewById(R.id.et_signup_password2);

        btCheckID = (Button)findViewById(R.id.bt_check_id);
        btSignUp = (Button)findViewById(R.id.bt_complete_sign_up);

        btCheckID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 서버에 중복확인
                isCheckedID = true;
            }
        });

        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkNotBlank(etInputName) && checkNotBlank(etInputID) && checkNotBlank(etInputPW) && checkNotBlank(etInputPW2)){
                    if (isCheckedID) {
                        password = etInputPW.getText().toString();
                        password2 = etInputPW2.getText().toString();
                        name = etInputName.getText().toString();
                        id = etInputID.getText().toString();
                        if (password.equals(password2)) {
                            //TODO 디비에 이름, ID, PW 넣기
                            printAlertDialog("가입 완료", "가입이 완료되었습니다.");
                        } else {
                            printAlertDialog("비밀번호 확인", "비밀번호가 일치하지 않습니다.");
                        }
                    } else {
                        printAlertDialog("아이디 확인", "아이디 중복 확인을 해주세요.");
                    }
               } else{
                    printAlertDialog("공백란 확인","쓰여지지 않은 곳이 있습니다. 다시 해주세요.");
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

    private boolean checkNotBlank(EditText text){ /// 공백이면 false
        if(text.getText().toString().isEmpty()){
            return false;
        }
        else{
            return true;
        }
    }

}
