package com.genius.payhrms.activity.reciver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;


import com.genius.payhrms.activity.dailylog.QRCodeScannerActivity;
import com.genius.payhrms.activity.helper.DatabaseHelper;
import com.genius.payhrms.activity.utility.ApiClient;
import com.genius.payhrms.activity.utility.Pref;
import com.genius.payhrms.activity.utility.UploadObject;

import retrofit2.Call;
import retrofit2.Callback;


public class NetworkStateChecker extends BroadcastReceiver {
    private Context context;
    private DatabaseHelper db;
    Pref pref;


    @SuppressLint("Range")
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        db = new DatabaseHelper(context);
        pref=new Pref(context);
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        //if there is a network
        if (activeNetwork != null) {
            //if connected to wifi or mobile data plan
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                Log.d("networkcome","1");
                //getting all the unsynced names
                final Cursor cursor = db.getUnsyncedNames();
                if (cursor.moveToFirst()) {
                    do {
                        //calling the method to save the unsynced name to MySQL




                                saveName(
                                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)),
                                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE))




                                );



                    } while (cursor.moveToNext());
                }
            }
        }
    }




    private void saveName(final int id, String date) {


    }

    }

