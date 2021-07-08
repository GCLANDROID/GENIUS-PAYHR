package com.genius.hrms.activity.reciver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;


import com.genius.hrms.activity.utility.MulLocationUpdatingService;

import java.util.Timer;
import java.util.TimerTask;

public class MulAlarm extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent)
    {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, ":okk");
        wl.acquire();

        Timer timer = new Timer ();
        TimerTask hourlyTask = new TimerTask () {
            @Override
            public void run () {
                Intent startIntent = new Intent(context, MulLocationUpdatingService.class);
                //startIntent.setAction(Constants.ACTION.START_ACTION);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(startIntent);
                } else {
                    context.startService(startIntent);
                }
            }
        };

// schedule the task to run starting now and then every 1 min
        timer.schedule (hourlyTask, 0l, 1000*1*60);






        // Put here YOUR code.

       // Toast.makeText(context, "Alarm !!!!!!!!!!", Toast.LENGTH_LONG).show();



        wl.release();
    }

    public void setAlarm(Context context)
    {
        AlarmManager am =(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, MulAlarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000*10*60, pi); // Millisec * Second * Minute
    }

    public void cancelAlarm(Context context)
    {
        Intent intent = new Intent(context, MulAlarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }


}
