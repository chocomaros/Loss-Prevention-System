package com.example.yena.losspreventionsystem;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BluetoothReceiver extends BroadcastReceiver {

    public BluetoothReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(intent.getAction())) {
            if(intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_OFF){
                Log.d("bluetoothReceiver", "블루투스 꺼졌당");
                context.startService(new Intent(context, LPSService.class));
            } else if(intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_ON){
                Log.d("bluetoothReceiver", "블루투스 켜졌당");
                context.stopService(new Intent(context, LPSService.class));
            }
        }
    }
}
