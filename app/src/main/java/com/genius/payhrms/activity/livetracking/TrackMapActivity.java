package com.genius.payhrms.activity.livetracking;

import android.os.Bundle;


import androidx.fragment.app.FragmentActivity;

import com.genius.payhrms.R;

import com.genius.payhrms.activity.utility.Pref;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;



import java.util.HashMap;

public class TrackMapActivity extends FragmentActivity implements OnMapReadyCallback {
    private HashMap<String, Marker> mMarkers = new HashMap<>();
    private GoogleMap mMap;
    String trackid;
    Pref pref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_map);
        initialize();
    }

    private void initialize(){
        pref=new Pref(getApplicationContext());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        trackid=getIntent().getStringExtra("trackId")+"-"+pref.getSecurityCode();


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Authenticate with Firebase when the Google map is loaded
        mMap = googleMap;
        mMap.setMaxZoomPreference(15);

    }





}
