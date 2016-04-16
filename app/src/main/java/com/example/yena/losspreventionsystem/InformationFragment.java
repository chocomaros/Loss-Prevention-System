package com.example.yena.losspreventionsystem;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class InformationFragment extends Fragment {

    ArrayList<ItemInfo> itemList = new ArrayList<>();
    InformationAdapter adapter;

    public InformationFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(1);
        arrayList.add(2);
        ItemInfo item1 = new ItemInfo("a",20,arrayList,ItemInfo.ALARM_OFF);
        ItemInfo item2 = new ItemInfo("b",30,arrayList,ItemInfo.ALARM_SILENT);
        ItemInfo item3 = new ItemInfo("c",40,arrayList,ItemInfo.ALARM_SOUND);
        itemList.add(item1);
        itemList.add(item2);
        itemList.add(item3);
        Log.d("item1", " name : " + item1.name + " distance : " + item1.distance + " group : " + item1.groupList.get(0) + " alarmStatus : " + item1.alarmStatus);
        adapter = new InformationAdapter(itemList);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_information,container,false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_information);
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
