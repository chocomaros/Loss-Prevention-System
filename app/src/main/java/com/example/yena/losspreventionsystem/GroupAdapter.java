package com.example.yena.losspreventionsystem;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by yena on 2016-04-11.
 */
public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

    private ArrayList<GroupInfo> groupList;

    public GroupAdapter(){

    }

    public GroupAdapter(ArrayList<GroupInfo> groupList){
        this.groupList = groupList;
    }

    @Override
    public void onBindViewHolder(GroupAdapter.ViewHolder holder, int position) {
        GroupInfo groupInfo = groupList.get(position);
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

        public ViewHolder(View v){
            super(v);
            view = v;
            tvName = (TextView)view.findViewById(R.id.tv_group_name);
        }
    }
}
