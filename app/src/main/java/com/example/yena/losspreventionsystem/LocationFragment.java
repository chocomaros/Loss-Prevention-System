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

    private static final int LARGE_CIRCLE_RANGE = 70, MIDDLE_CIRCLE_RANGE = 40, SMALL_CIRCLE_RANGE = 20;

    private Spinner spinnerItemSelection;
    private ItemInfo itemInfo;
    private ArrayList<ItemInfo> itemList;
    private LocationFindView locationFindView;

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

        locationFindView = (LocationFindView)view.findViewById(R.id.view_location_find);
        locationFindView.setScActivatedTrue(); //TODO 거리에 따라 어느 곳에 알파값 줄건지

        return view;
    }

    AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            itemInfo = itemList.get(position);
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
