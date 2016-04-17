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
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;

public class InformationFragment extends Fragment {

    ArrayList<ItemInfo> itemList = new ArrayList<>();
    InformationAdapter adapter;
    boolean check = false;

    public InformationFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ArrayList<ItemInfo> arrayList = new ArrayList<ItemInfo>();
//        ItemInfo item1 = new ItemInfo("10203","fff",20,ItemInfo.ALARM_OFF);
//        ItemInfo item2 = new ItemInfo("ad2034","asdf",30,ItemInfo.ALARM_SILENT);
//        ItemInfo item3 = new ItemInfo("afds32","cvz",40,ItemInfo.ALARM_SOUND);
//        itemList.add(item1);
//        itemList.add(item2);
//        itemList.add(item3);
//            for (int i = 0; i < itemList.size(); i++) {
//                LPSDAO.insertItemInfo(getContext(), itemList.get(i));
//            }
        arrayList = LPSDAO.getItemInfo(getContext());
        for(int i=0;i<arrayList.size();i++){
            Log.d("arrayList.get(0)", arrayList.get(i).name);
        }

        adapter = new InformationAdapter(arrayList);
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
