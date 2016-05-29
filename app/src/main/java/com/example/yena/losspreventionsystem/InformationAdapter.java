package com.example.yena.losspreventionsystem;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by yena on 2016-04-10.
 */
public class InformationAdapter extends RecyclerView.Adapter<InformationAdapter.ViewHolder> {

    public static final int MAIN = 0, PUT_GROUP = 1;

    ArrayList<ItemInfo> itemList;
    private ImageButton ibSilent, ibVibration, ibSound, ibSoundVibration;

    private boolean checkBoxVisibility;
    private Context context;
    private int activityState;

    public InformationAdapter(){

    }

    public InformationAdapter(ArrayList<ItemInfo> itemList){
        this.itemList = itemList;
    }
    public InformationAdapter(ArrayList<ItemInfo> itemList, int whichActivity){
        switch (whichActivity){
            case MAIN :
                activityState = MAIN;
                checkBoxVisibility = false;
                break;
            case PUT_GROUP :
                activityState = PUT_GROUP;
                checkBoxVisibility = true;
                break;
        }
        this.itemList = itemList;
    }

    @Override
    public void onBindViewHolder(InformationAdapter.ViewHolder holder, final int position) {
        final ItemInfo itemInfo = new ItemInfo(itemList.get(position).beaconID,itemList.get(position).name,
                itemList.get(position).alarmStatus, itemList.get(position).lossTime) ;

        holder.tvName.setText(itemInfo.name);

        if(itemInfo.lossTime.getTimeInMillis() == 0){
            holder.tvLossTime.setText("x");
        } else{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA);
            holder.tvLossTime.setText(sdf.format(new Date(itemInfo.lossTime.getTimeInMillis())));
        }
        switch (itemInfo.alarmStatus){
            case AlarmManagement.DISABLE :
                holder.ivAlarmStatus.setImageResource(R.drawable.ic_disable);
                break;
            case AlarmManagement.ALARM_OFF :
                holder.ivAlarmStatus.setImageResource(R.drawable.ic_alarm_off2);
                break;
            case AlarmManagement.ALARM_SILENT :
                holder.ivAlarmStatus.setImageResource(R.drawable.ic_silent);
                break;
            case AlarmManagement.ALARM_VIBRATION :
                holder.ivAlarmStatus.setImageResource(R.drawable.ic_vibration);
                break;
            case AlarmManagement.ALARM_SOUND :
                holder.ivAlarmStatus.setImageResource(R.drawable.ic_sound);
                break;
            case AlarmManagement.ALARM_SOUND_VIBRATION :
                holder.ivAlarmStatus.setImageResource(R.drawable.ic_sound_vibration);
                break;
        }

        final CheckBox checkBox = holder.cbPutGroup;
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityState == PUT_GROUP) {
                    if (checkBox.isChecked()) {
                        checkBox.setChecked(false);
                        itemList.get(position).checked=false;
                    } else {
                        checkBox.setChecked(true);
                        itemList.get(position).checked=true;
                    }
                } else if (activityState == MAIN) {
                    Intent intent = new Intent(v.getContext(),ItemInfoDetailActivity.class);
                    intent.putExtra("ItemInfo",itemInfo);
                    v.getContext().startActivity(intent);
                }
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    itemList.get(position).checked=true;
                } else {
                    itemList.get(position).checked=false;
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.card_item_info,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;

        TextView tvName, tvLossTime;
        CheckBox cbPutGroup;
        ImageView ivAlarmStatus;

        public ViewHolder(View v){
            super(v);
            view = v;

            tvName = (TextView)view.findViewById(R.id.tv_item_name);
            tvLossTime = (TextView)view.findViewById(R.id.tv_item_loss_time);
            cbPutGroup = (CheckBox)view.findViewById(R.id.cb_put_group);
            ivAlarmStatus = (ImageView)view.findViewById(R.id.iv_alarm_status);

            if(checkBoxVisibility){
                cbPutGroup.setVisibility(View.VISIBLE);
            } else{
                cbPutGroup.setVisibility(View.GONE);
            }


//            ibSilent = (ImageButton)view.findViewById(R.id.ib_alarm_silent);
//            ibVibration = (ImageButton)view.findViewById(R.id.ib_alarm_vibration);
//            ibSound = (ImageButton)view.findViewById(R.id.ib_alarm_sound);
//            ibSoundVibration = (ImageButton)view.findViewById(R.id.ib_alarm_sound_vibration);
//
//            btDisable = (Button)view.findViewById(R.id.bt_disable);
        }
    }
}