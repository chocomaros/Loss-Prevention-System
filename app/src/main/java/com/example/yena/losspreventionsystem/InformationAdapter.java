package com.example.yena.losspreventionsystem;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by yena on 2016-04-10.
 */
public class InformationAdapter extends RecyclerView.Adapter<InformationAdapter.ViewHolder> {

    private ArrayList<ItemInfo> itemList;
    private TextView tvName, tvDistance, tvGroup;
    private ImageButton ibSilent, ibVibration, ibSound, ibSoundVibration;
    private Button btDisable;
    private String groupName = "";

    public InformationAdapter(){

    }

    public InformationAdapter(ArrayList<ItemInfo> itemList){
        this.itemList = itemList;
    }

    @Override
    public void onBindViewHolder(InformationAdapter.ViewHolder holder, int position) {
        ItemInfo itemInfo = itemList.get(position);
        tvName.setText(itemInfo.name);
        tvDistance.setText("거리 : "+itemInfo.distance);
        for(int i=0;i<itemInfo.groupList.size();i++) { //TODO 디비에서 그룹리스트 받아와서 그 id에 맞는 이름 찾아와야함
            groupName.concat(" "+itemInfo.groupList.get(i));
            tvGroup.setText("그룹 :"+groupName);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_info,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        View view;

        public ViewHolder(View v){
            super(v);
            view = v;

            tvName = (TextView)view.findViewById(R.id.tv_item_name);
            tvDistance = (TextView)view.findViewById(R.id.tv_item_distance);
            tvGroup = (TextView)view.findViewById(R.id.tv_item_group);

//            ibSilent = (ImageButton)view.findViewById(R.id.ib_alarm_silent);
//            ibVibration = (ImageButton)view.findViewById(R.id.ib_alarm_vibration);
//            ibSound = (ImageButton)view.findViewById(R.id.ib_alarm_sound);
//            ibSoundVibration = (ImageButton)view.findViewById(R.id.ib_alarm_sound_vibration);
//
//            btDisable = (Button)view.findViewById(R.id.bt_disable);
        }
    }
}