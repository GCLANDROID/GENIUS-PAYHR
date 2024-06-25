package com.genius.payhrms.activity.utility;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;

import java.util.TimerTask;

public class CustomTimerTask extends TimerTask {
    private Context context;
    private Handler mHandler = new Handler();

    public CustomTimerTask(Context con) {
        this.context = con;
    }



    @Override
    public void run() {
        new Thread(new Runnable() {

            public void run() {

                mHandler.post(new Runnable() {
                    public void run() {
                        Intent startIntent = new Intent(context, LocationUpdatingService.class);

                        //startIntent.setAction(Constants.ACTION.START_ACTION);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            context.startForegroundService(startIntent);
                        } else {
                            context.startService(startIntent);
                        }

                    }
                });
            }
        }).start();

    }

}
