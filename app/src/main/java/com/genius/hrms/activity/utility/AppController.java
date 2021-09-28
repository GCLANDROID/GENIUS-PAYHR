package com.genius.hrms.activity.utility;

import android.app.Application;
import android.os.StrictMode;

import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.FirebaseApp;


public class AppController extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //MultiDex.install(this);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        FirebaseApp.initializeApp(getApplicationContext());
       /*
        registerActivityLifecycleCallbacks(new MyLifecycleHandler());
*/
    }

}
