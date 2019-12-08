package com.example.myapplication.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.myapplication.lockscreen.service.Undead_Service;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent in = new Intent(context, Undead_Service.class);
            context.startForegroundService(in);
        } else {
            Intent in = new Intent(context, Undead_Service.class);
            context.startService(in);
        }
    }
}
