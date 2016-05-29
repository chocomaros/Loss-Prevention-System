package com.example.yena.losspreventionsystem;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;

public class LocationFragment extends Fragment implements BeaconConsumer{

    private static final int LARGE_CIRCLE_RANGE = 70, MIDDLE_CIRCLE_RANGE = 40, SMALL_CIRCLE_RANGE = 20;

    private Spinner spinnerItemSelection;
    private ItemInfo itemInfo;
    private ArrayList<ItemInfo> itemList;
    private LocationFindView locationFindView;
    private BeaconManager beaconManager;
    private ArrayList<Beacon> beaconList;
    private boolean findBeacon;


    public LocationFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemList = new ArrayList<>();
        itemList.addAll(LPSDAO.getItemInfo(getContext()));
        beaconInit();
        beaconList = new ArrayList<>();
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

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            // 비콘이 감지되면 해당 함수가 호출된다. Collection<Beacon> beacons에는 감지된 비콘의 리스트가,
            // region에는 비콘들에 대응하는 Region 객체가 들어온다.
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                Log.d("비콘 갯수", "" + beacons.size());
                    findBeacon = false;
                    for (Beacon beacon : beacons) {
                        Log.d("거리",beacon.getDistance()+"m");
                        if (beacon.getId1().toString().equals(itemInfo.beaconID)) {
                            findBeacon = true;
                            locationFindView.setLabel(String.format("%.2f",beacon.getDistance()) + "m");
                            if (beacon.getDistance() <= SMALL_CIRCLE_RANGE) {
                               locationFindView.setScActivatedTrue();
                                break;
                            } else if(beacon.getDistance() > SMALL_CIRCLE_RANGE && beacon.getDistance() <= MIDDLE_CIRCLE_RANGE){
                                locationFindView.setMcActivatedTrue();
                                break;
                            } else if(beacon.getDistance() > MIDDLE_CIRCLE_RANGE && beacon.getDistance() <= LARGE_CIRCLE_RANGE){
                                locationFindView.setLcActivatedTrue();
                                break;
                            } else{
                                locationFindView.setCircleActivatedFalse();
                                break;
                            }
                        }
                    }
                if(!findBeacon){
                    locationFindView.setLabel("x");
                    locationFindView.setCircleActivatedFalse();
                }
            }
        });
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));

        } catch (RemoteException e) {
        }
    }

    public void beaconInit(){
        beaconManager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(getApplicationContext());
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);

    }

    @Override
    public Context getApplicationContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return getActivity().bindService(intent, serviceConnection, i);
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {
        getActivity().unbindService(serviceConnection);
    }
}
