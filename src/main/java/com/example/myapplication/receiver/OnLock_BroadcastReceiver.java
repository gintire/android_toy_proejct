package com.example.myapplication.receiver;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.myapplication.lockscreen.LockScreenActivity;

public class OnLock_BroadcastReceiver extends BroadcastReceiver {
    private KeyguardManager km = null;

    private KeyguardManager.KeyguardLock keyLock = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            Log.e("onReceive","SCREEN_ON");
            /*PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);
            try {
                pendingIntent.send();
            } catch(PendingIntent.CanceledException e) {
                e.printStackTrace();
            }*/
        } else if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Log.e("onReceive","SCREEN_OFF");
            if (km == null) {
                km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            }
            if (keyLock == null) {
                keyLock = km.newKeyguardLock(Context.KEYGUARD_SERVICE);
            }
            disableKeyguard();

            Intent i = new Intent(context, LockScreenActivity.class);
            i.setClass(context, LockScreenActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } else if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.e("onReceive","SCREEN_BOOT_COMPLETED");
        }
    }
    public void reenableKeyguard() {
        keyLock.reenableKeyguard();
    }
    public void disableKeyguard() {
        keyLock.disableKeyguard();
    }
}
