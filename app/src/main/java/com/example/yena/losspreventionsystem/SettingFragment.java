package com.example.yena.losspreventionsystem;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


public class SettingFragment extends Fragment {

    public static final int DISTANCE1 = 10, DISTANCE2 = 30, DISTANCE3 = 50, DISTANCE4 = 70;

    private SharedPreferences pref;
    private View view;
    private int originalValue;
    private TextView tvAlarmDistanceSetting, tvAlarmDistanceValue;

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, null);
        pref = getActivity().getSharedPreferences(LPSSharedPreferences.NAME, 0);
        originalValue = pref.getInt(LPSSharedPreferences.DISTANCE_SETTING, DISTANCE1);

        tvAlarmDistanceSetting = (TextView)view.findViewById(R.id.tv_distance_setting);
        tvAlarmDistanceValue = (TextView)view.findViewById(R.id.tv_distance_setting_value);

        tvAlarmDistanceValue.setText(originalValue + "m");

        tvAlarmDistanceSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAlarmDistanceSettingDialog();
            }
        });

        TextView tvLogout = (TextView)view.findViewById(R.id.tv_logout);
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(LPSSharedPreferences.USER_ID, "");
                editor.putString(LPSSharedPreferences.USER_PW, "");
                editor.putBoolean(LPSSharedPreferences.AUTO_LOGIN, false);
                editor.commit();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        TextView tvUnregister = (TextView)view.findViewById(R.id.tv_unregister);
        tvUnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 회원탈퇴
            }
        });

        return view;
    }

    void displayAlarmDistanceSettingDialog(){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_alarm_distance_setting, null);

        final RadioGroup radioGroup = (RadioGroup)dialogLayout.findViewById(R.id.rg_distance);

        int radioId;
        originalValue = pref.getInt(LPSSharedPreferences.DISTANCE_SETTING,DISTANCE1);
        switch (originalValue){
            case DISTANCE1 :
                radioId = R.id.rb_distance1;
                break;
            case DISTANCE2 :
                radioId = R.id.rb_distance2;
                break;
            case DISTANCE3 :
                radioId = R.id.rb_distance3;
                break;
            case DISTANCE4 :
                radioId = R.id.rb_distance4;
                break;
            default:
                radioId = R.id.rb_distance1;
                break;
        }
        radioGroup.check(radioId);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                SharedPreferences.Editor editor = pref.edit();
                switch (checkedId) {
                    case R.id.rb_distance1:
                        editor.putInt(LPSSharedPreferences.DISTANCE_SETTING, DISTANCE1);
                        break;
                    case R.id.rb_distance2:
                        editor.putInt(LPSSharedPreferences.DISTANCE_SETTING, DISTANCE2);
                        break;
                    case R.id.rb_distance3:
                        editor.putInt(LPSSharedPreferences.DISTANCE_SETTING, DISTANCE3);
                        break;
                    case R.id.rb_distance4:
                        editor.putInt(LPSSharedPreferences.DISTANCE_SETTING, DISTANCE4);
                        break;
                }
                editor.commit();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(),R.style.AlarmDistanceDialogTheme));
        builder.setTitle("알람 거리 설정")
                .setView(dialogLayout)
                .setCancelable(false)
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putInt(LPSSharedPreferences.DISTANCE_SETTING,originalValue);
                        editor.commit();
                        dialog.cancel();
                    }
                })
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        setDistanceText(tvAlarmDistanceValue, pref.getInt(LPSSharedPreferences.DISTANCE_SETTING,originalValue));
                                dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void setDistanceText(TextView tvDistance, int distance){
        tvDistance.setText(distance + "m");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
