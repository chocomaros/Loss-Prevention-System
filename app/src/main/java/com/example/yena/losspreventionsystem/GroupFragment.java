package com.example.yena.losspreventionsystem;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class GroupFragment extends Fragment {

    ArrayList<GroupInfo> groupList = new ArrayList<>();
    GroupAdapter adapter;

    public GroupFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(1);
        arrayList.add(2);
        GroupInfo group1 = new GroupInfo(1,"첫번째",arrayList);
        GroupInfo group2 = new GroupInfo(2,"두번째",arrayList);
        GroupInfo group3 = new GroupInfo(3,"세번째",arrayList);
        groupList.add(group1);
        groupList.add(group2);
        groupList.add(group3);
        adapter = new GroupAdapter(groupList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group,container,false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_group);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return view;
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
