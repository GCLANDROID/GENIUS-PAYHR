package com.genius.payhrms.activity.attendance.tour;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.genius.payhrms.R;
import com.genius.payhrms.activity.leaveapplication.ApplicationFragment;

public class TourActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour);
        loadApplicationFragment();
    }


    public void loadApplicationFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        TourApplicationFragment pfragment = new TourApplicationFragment();
        transaction.replace(R.id.frameLayout, pfragment);
        transaction.commit();
    }
}