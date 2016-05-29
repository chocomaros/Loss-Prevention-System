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

import java.util.Calendar;

/**
 * Created by CAU on 2016-05-10.
 */
public class AlarmManagement {

    /// DISABLE : 아예 찾지도 않는거, ALARM_OFF : 알람기능만 없는거(팝업 메시지는 있다)
    public static final int  DISABLE = 0, ALARM_OFF = 1, ALARM_SILENT = 2,
            ALARM_VIBRATION = 3, ALARM_SOUND = 4, ALARM_SOUND_VIBRATION = 5;
    public static final int ALARM_ALL_OFF = 0, ALARM_ALL_ON = 1;

    public Context context;
    //푸시메시지
    private NotificationManager notificationManager;


    private static final int NOTIFICATION_ID = 1;

    public AlarmManagement(){

    }
    public AlarmManagement(Context context){
        this.context = context;
    }


    //푸시 메시지
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
     void generateNotification(ItemInfo item) {
        notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notify = new Notification.Builder(context)
                .setTicker("아주 중요한 메시지")
                .setContentTitle("중요한 메지지 이니 무조건 쳐보기")
                .setContentText(item.lossTime.get(Calendar.HOUR_OF_DAY) +":" + item.lossTime.get(Calendar.MINUTE) + " 에 " + item.name + "을(를) 잃어버렸습니다.")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setWhen(System.currentTimeMillis())
                        .build();

        notificationManager.notify(NOTIFICATION_ID, notify);
    }

}

