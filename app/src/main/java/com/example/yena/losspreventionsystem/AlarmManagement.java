package com.example.yena.losspreventionsystem;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;

/**
 * Created by CAU on 2016-05-10.
 */
public class AlarmManagement {

    /// DISABLE : 아예 찾지도 않는거, ALARM_OFF : 알람기능만 없는거(팝업 메시지는 있다)
    public static final int  DISABLE = 0, ALARM_OFF = 1, ALARM_SILENT = 2,
            ALARM_VIBRATION = 3, ALARM_SOUND = 4, ALARM_SOUND_VIBRATION = 5;
    //소리
    MediaPlayer mAudio = null;
    AudioManager am = null;
    int StreamType = 0;
    //진동
    Vibrator vide;

    public Context context;
    //푸시메시지
    private NotificationManager notificationManager;


    private static final int NOTIFICATION_ID = 1;

    public AlarmManagement(){

    }
    public AlarmManagement(Context context){
        this.context = context;
    }

    // 팝업 메시지
    public void PopupMsg(ItemInfo item){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Loss Prevention System");
        builder.setMessage(item.name+"이(가) 사라졌습니다.");
        builder.setCancelable(false);
        final int alarmStatus = item.alarmStatus;
        switch (alarmStatus){
            case ALARM_SOUND :
                MediaPlay();
                break;
            case ALARM_VIBRATION :
                Vibrator_pattern();
                break;
            case ALARM_SOUND_VIBRATION :
                MediaPlay();
                Vibrator_pattern();
                break;
            case ALARM_SILENT :
                break;
        }
        builder.setNeutralButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (alarmStatus) {
                    case ALARM_SOUND:
                        Cancel_Ringtone();
                        break;
                    case ALARM_VIBRATION:
                        Cancel_Vib();
                        break;
                    case ALARM_SOUND_VIBRATION:
                        Cancel_Vib();
                        Cancel_Ringtone();
                        break;
                    case ALARM_SILENT:
                        break;
                }
            }
        });
        builder.show();
    }

    //푸시 메시지
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
     void generateNotification(ItemInfo item) {

        notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notify = new Notification.Builder(context)
                .setTicker("아주 중요한 메시지")
                .setContentTitle("중요한 메지지 이니 무조건 쳐보기")
                .setContentText("몇시 경 "+item.name+"을(를) 잃어버렸습니다.")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .build();

        notificationManager.notify(NOTIFICATION_ID, notify);
    }

    //진동
    public void Vibrator_pattern(){
        vide = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0,500,200,400,100};//시작시간, 진동울리는 시간, 진동이 쉴시간, 진동이 울리는 시간, 진동이 쉴시간
        vide.vibrate(pattern, 0);// -1(1번 반복), 0 or 양수(그 index부터 시작하여 무한반복 하겟다)
    }

    //소리
    public  void MediaPlay(){
        try{
            mAudio = new MediaPlayer();
            Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            mAudio.setDataSource(alert.toString());

            am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
            am.setStreamVolume(StreamType,am.getStreamMaxVolume(StreamType), AudioManager.FLAG_PLAY_SOUND);
            //String path = "/system/Ringtone/sound.ogg";
            //mAudio.setDataSource(path);

            mAudio.setAudioStreamType(AudioManager.STREAM_RING);
            mAudio.setAudioStreamType(StreamType);
            mAudio.setLooping(true);
            mAudio.prepare();

        }catch(Exception e) {
        }

        mAudio.start();

    }


    //무한반복일때 이것으로 종료
    public void Cancel_Vib(){
        vide.cancel();
    }

    public void Cancel_Ringtone(){
        mAudio.stop();
    }


}

