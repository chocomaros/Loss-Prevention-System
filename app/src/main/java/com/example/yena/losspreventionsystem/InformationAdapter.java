package com.example.yena.losspreventionsystem;

import android.content.ClipData;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by yena on 2016-04-10.
 */
public class InformationAdapter extends RecyclerView.Adapter<InformationAdapter.ViewHolder> {

    public static final int MAIN = 0, PUT_GROUP = 1;

    ArrayList<ItemInfo> itemList;
    private ImageButton ibSilent, ibVibration, ibSound, ibSoundVibration;
    private Button btDisable;

    private boolean checkBoxVisibility, distanceVisibility;
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
                distanceVisibility = true;
                break;
            case PUT_GROUP :
                activityState = PUT_GROUP;
                checkBoxVisibility = true;
                distanceVisibility = false;
        }
        this.itemList = itemList;
    }

    @Override
    public void onBindViewHolder(InformationAdapter.ViewHolder holder, final int position) {
        final ItemInfo itemInfo = new ItemInfo(itemList.get(position).beaconID,itemList.get(position).name,
                itemList.get(position).distance,itemList.get(position).alarmStatus,false) ;
        holder.tvName.setText(itemInfo.name);
        holder. tvDistance.setText("거리 : "+itemInfo.distance);
        ArrayList<GroupInfo> groupList;
        groupList = LPSDAO.getGroupListOfItem(context,itemList.get(position));
        String groupName = null;
        for(int i=0;i<groupList.size();i++) {
            groupName.concat(" " +groupList.get(i).name);
            holder.tvGroup.setText("그룹 :" + groupName);
        }
//        cbPutGroup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(itemInfo.checked){
//                    itemInfo.checked = false;
//                } else{
//                    itemInfo.checked = true;
//                }
//            }
//        });'
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
                    //TODO Main에서 item click했을때
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

        TextView tvName, tvDistance, tvGroup;
        CheckBox cbPutGroup;
        public ViewHolder(View v){
            super(v);
            view = v;

            tvName = (TextView)view.findViewById(R.id.tv_item_name);
            tvDistance = (TextView)view.findViewById(R.id.tv_item_distance);
            tvGroup = (TextView)view.findViewById(R.id.tv_item_group);
            cbPutGroup = (CheckBox)view.findViewById(R.id.cb_put_group);

            if(checkBoxVisibility){
                cbPutGroup.setVisibility(View.VISIBLE);
            } else{
                cbPutGroup.setVisibility(View.GONE);
            }
            if(distanceVisibility){
                tvDistance.setVisibility(View.VISIBLE);
            } else{
                tvDistance.setVisibility(View.GONE);
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