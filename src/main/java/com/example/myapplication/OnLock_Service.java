package com.example.myapplication;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class OnLock_Service extends Service {
    private OnLock_BroadcastReceiver mReceiver;
    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new OnLock_BroadcastReceiver();
        registerReceiver(mReceiver, filter);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if(intent!=null) {
            if(intent.getAction() == null) {
                if(mReceiver==null) {
                    IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
                    filter.addAction(Intent.ACTION_SCREEN_OFF);
                    mReceiver = new OnLock_BroadcastReceiver();
                    registerReceiver(mReceiver, filter);
                }
            }
        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mReceiver!=null) {
            unregisterReceiver(mReceiver);
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
