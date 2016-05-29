package com.example.yena.losspreventionsystem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

public class AlarmActivity extends AppCompatActivity {

    public static final String ITEM_INFO = "ITEM_INFO";

    AlarmManagement alarmManagement;
    ItemInfo itemInfo;
    //소리
    MediaPlayer mediaPlayer = null;
    AudioManager am = null;

    int StreamType = 0;
    //진동
    Vibrator vide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                // 키잠금 해제하기
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                // 화면 켜기
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        Intent intent = getIntent();
        itemInfo = (ItemInfo)intent.getSerializableExtra(ITEM_INFO);
        initMediaPlayer();
        PopupMsg(itemInfo);
    }

    // 팝업 메시지
    void PopupMsg(ItemInfo item){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Loss Prevention System");
        builder.setMessage(item.name + "이(가) 사라졌습니다.");
        builder.setCancelable(false);
        final int alarmStatus = item.alarmStatus;
        switch (alarmStatus){
            case AlarmManagement.ALARM_SOUND :
                mediaPlayer.start();
                break;
            case AlarmManagement.ALARM_VIBRATION :
                Vibrator_pattern();
                break;
            case AlarmManagement.ALARM_SOUND_VIBRATION :
                mediaPlayer.start();
                Vibrator_pattern();
                break;
            case AlarmManagement.ALARM_SILENT :
                break;
        }
        builder.setNeutralButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (alarmStatus) {
                    case AlarmManagement.ALARM_SOUND:
                        Cancel_Mus();
                        break;
                    case AlarmManagement.ALARM_VIBRATION:
                        Cancel_Vib();
                        break;
                    case AlarmManagement.ALARM_SOUND_VIBRATION:
                        Cancel_Vib();
                        Cancel_Mus();
                        break;
                    case AlarmManagement.ALARM_SILENT:
                        break;
                }
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

//     =================소리 경로지정 및 초기화 ======================
    private void initMediaPlayer() {

        mediaPlayer = new MediaPlayer();
        mediaPlayer = MediaPlayer.create(this, R.raw.konan);
    }

    // ==================진동 패턴=====================================
    public void Vibrator_pattern(){
        vide =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0,500,200,400,100};//시작시간, 진동울리는 시간, 진동이 쉴시간, 진동이 울리는 시간, 진동이 쉴시간
        vide.vibrate(pattern, 0);// -1(1번 반복), 0 or 양수(그 index부터 시작하여 무한반복 하겟다)
    }

    /*
    ================= 취소===========================================
     */
    //진동 취소
    public void Cancel_Vib(){
        vide.cancel();
    }
    //소리 취소
    public void Cancel_Mus(){
        mediaPlayer.stop();
    }
}
