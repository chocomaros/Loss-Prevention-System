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

        itemList = LPSDAO.getItemInfo(getContext());
        for(int i=0;i<itemList.size();i++){
            Log.d("arrayList.get(0)", itemList.get(i).name);
        }

        adapter = new InformationAdapter(itemList,InformationAdapter.MAIN);
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
    public void onResume() {
        super.onResume();
        adapter.itemList = LPSDAO.getItemInfo(getContext());
        adapter.notifyDataSetChanged();
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
