package com.example.yena.losspreventionsystem;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class LocationFragment extends Fragment {

    private Spinner spinnerItemSelection;
    private ItemInfo itemInfo;
    private ArrayList<ItemInfo> itemList;
    private FrameLayout flLocation;

    public LocationFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemList = new ArrayList<>();
        itemList.addAll(LPSDAO.getItemInfo(getContext()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location,null);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),R.layout.spinner_item);
        for(int i=0; i<itemList.size(); i++){
            adapter.add(itemList.get(i).name);
        }
        spinnerItemSelection = (Spinner)view.findViewById(R.id.spinner_location_item);
        spinnerItemSelection.setAdapter(adapter);
        spinnerItemSelection.setOnItemSelectedListener(itemSelectedListener);
        return view;
    }

    AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            itemInfo = itemList.get(position);
            Toast.makeText(getContext(),itemInfo.name,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    @Override
    public void onResume() {
        super.onResume();
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
