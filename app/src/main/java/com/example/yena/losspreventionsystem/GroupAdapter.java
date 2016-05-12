package com.example.yena.losspreventionsystem;

import android.support.v7.widget.RecyclerView;
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

    private ArrayList<GroupInfo> groupList;
    private int activityState;
    private boolean radioButtonVisibility;

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
    public void onBindViewHolder(GroupAdapter.ViewHolder holder, int position) {
        GroupInfo groupInfo = groupList.get(position);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activityState == MAIN){

                } else if(activityState == GROUP_LIST_DIALOG){

                }
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
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View view;
        TextView tvName;
        RadioButton rbGroup;

        public ViewHolder(View v){
            super(v);
            view = v;
            tvName = (TextView)view.findViewById(R.id.tv_group_name);
            rbGroup = (RadioButton)view.findViewById(R.id.rb_group);

            if(radioButtonVisibility){
                rbGroup.setVisibility(View.VISIBLE);
            } else{
                rbGroup.setVisibility(View.GONE);
            }
        }
    }
}
