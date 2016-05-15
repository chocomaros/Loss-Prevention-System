package com.example.yena.losspreventionsystem;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by yena on 2016-04-11.
 */
public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

    public static final int MAIN = 0, GROUP_LIST_DIALOG = 1;

    ArrayList<GroupInfo> groupList;
    private int activityState;
    private boolean radioButtonVisibility;
    private Context context;

    public GroupAdapter(){

    }

    public GroupAdapter(ArrayList<GroupInfo> groupList){
        this.groupList = groupList;
    }

    public GroupAdapter(ArrayList<GroupInfo> groupList, int whichActivity){
        switch(whichActivity){
            case MAIN :
                activityState = MAIN;
                radioButtonVisibility = false;
                break;
            case GROUP_LIST_DIALOG :
                activityState = GROUP_LIST_DIALOG;
                radioButtonVisibility = true;
                break;
        }
        this.groupList = groupList;
    }

    @Override
    public void onBindViewHolder(final GroupAdapter.ViewHolder holder, final int position) {
        GroupInfo groupInfo = groupList.get(position);
        ArrayList<ItemInfo> itemsInGroup;
        itemsInGroup = LPSDAO.getItemListOfGroup(context,groupInfo);

        String itemsName = new String();
        for(int i=0;i<itemsInGroup.size();i++){
            if(i<itemsInGroup.size() - 1){
                itemsName = itemsName.concat(itemsInGroup.get(i).name + ", ");
            } else{
                itemsName = itemsName.concat(itemsInGroup.get(i).name);
            }
        }

        holder.tvName.setText(groupInfo.name);
        holder.tvItemsName.setText(itemsName);

        if(!groupInfo.radiobuttonChecked){
            holder.rbGroup.setChecked(false);
        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activityState == MAIN){
                    //TODO MAIN에서 group 클릭했을때
                } else if(activityState == GROUP_LIST_DIALOG){
                    if(holder.rbGroup.isChecked()){
                        holder.rbGroup.setChecked(false);
                        groupList.get(position).radiobuttonChecked = false;
                    } else{
                        for(int i=0;i<groupList.size();i++){
                            if(i == position){
                                groupList.get(i).radiobuttonChecked = true;
                            } else{
                                groupList.get(i).radiobuttonChecked = false;
                            }
                        }
                        holder.rbGroup.setChecked(true);
                    }
                }
                GroupAdapter.this.notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_group_info,parent,false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View view;
        TextView tvName, tvItemsName;
        RadioButton rbGroup;

        public ViewHolder(View v){
            super(v);
            view = v;
            tvName = (TextView)view.findViewById(R.id.tv_group_name);
            rbGroup = (RadioButton)view.findViewById(R.id.rb_group);
            tvItemsName = (TextView)view.findViewById(R.id.tv_items_name_in_group);

            if(radioButtonVisibility){
                rbGroup.setVisibility(View.VISIBLE);
            } else{
                rbGroup.setVisibility(View.GONE);
            }
        }
    }
}
