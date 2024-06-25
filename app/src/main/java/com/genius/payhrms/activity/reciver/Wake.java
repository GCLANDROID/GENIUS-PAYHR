package com.genius.payhrms.activity.reciver;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;


import androidx.legacy.content.WakefulBroadcastReceiver;

import com.genius.payhrms.activity.utility.LocationUpdatingService;

public class Wake extends WakefulBroadcastReceiver {
    private PowerManager.WakeLock screenWakeLock;

    @SuppressLint("InvalidWakeLockTag")
    @Override
    public void onReceive(Context context, Intent intent) {

        {
            if (screenWakeLock == null)
            {
                PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                screenWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP,
                        "ScreenLock tag from AlarmListener");
                screenWakeLock.acquire();
            }
            Intent service = new Intent(context, LocationUpdatingService.class);
            startWakefulService(context, service);
            if (screenWakeLock != null)
                screenWakeLock.release();
        }

    }
}
