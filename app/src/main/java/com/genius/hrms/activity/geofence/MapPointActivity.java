package com.genius.hrms.activity.geofence;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.genius.hrms.R;

import com.genius.hrms.activity.activity.EmployeeDashBoardActivity;
import com.genius.hrms.activity.activity.UserDashBoardActivity;
import com.genius.hrms.activity.model.VisitingLocationModel;
import com.genius.hrms.activity.utility.DirectionsJSONParser;
import com.genius.hrms.activity.utility.NetworkConnectionCheck;
import com.genius.hrms.activity.utility.Pref;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import okhttp3.logging.HttpLoggingInterceptor;

public class MapPointActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public static final String TAG = MapPointActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    //  private MapView mapView;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    NetworkConnectionCheck connectionCheck;
    LatLng latLng;
    TextView tvAddress;

    String address = "N/A";
    double currentLatitude, currentLongitude;
    String address1;
    String empId;
    String lat = "0";
    String longt = "0";
    Pref pref;
    LinearLayout llSubmit;
    int flag = 0;

    ArrayList<VisitingLocationModel> addressList = new ArrayList<>();

    ArrayList<VisitingLocationModel> itemlist = new ArrayList<>();

    private static final int PATTERN_DASH_LENGTH_PX = 20;
    private static final int PATTERN_GAP_LENGTH_PX = 10;
    private static final PatternItem DOT = new Dot();
    private static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);


    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);
    List<LatLng> path = new ArrayList();
    LatLng d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, strt, dstn;
    TextView tvDistance;
    int timing = 100;
    ImageView imgBack,imgHome;
    String latValue,longiValue;
    TextView tvLat,tvLong;
    ArrayList<String>latList=new ArrayList<>();
    ArrayList<String>longList=new ArrayList<>();
    ArrayList<String>locationNameList=new ArrayList<>();

   String locationList;
    LinearLayout llConfigure;
    String locationPoint;
    ArrayList<String>fenceList=new ArrayList<>();
    String fenceid;
    TextView tvToolBar,tvConfig;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_point);
        initialize();
        setUpMapIfNeeded();
        onClick();


    }


    private void initialize() {
        pref = new Pref(getApplicationContext());
        connectionCheck = new NetworkConnectionCheck(getApplicationContext());
        itemlist.add(new VisitingLocationModel("sodepur", "00.00", "22.6982", "88.3895"));
        //itemlist.add(new VisitingLocationModel("agarparar", "00.00","22.45567", "88.367778895"));
        itemlist.add(new VisitingLocationModel("khardah", "00.00", "22.56789", "88.55444"));


        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds


        connectionCheck = new NetworkConnectionCheck(this);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        latValue=getIntent().getStringExtra("latValue");
        longiValue=getIntent().getStringExtra("longtiValue");
        tvLat=(TextView)findViewById(R.id.tvLat);
        tvLong=(TextView)findViewById(R.id.tvLong);
        tvLat.setText("Fence Latitiude Value:"+latValue);
        tvLong.setText("Fence Longtitude Value:"+longiValue);
        latList=(ArrayList<String>) getIntent().getSerializableExtra("arrayLatValue");
        Log.d("latList", String.valueOf(latList));
        longList=(ArrayList<String>) getIntent().getSerializableExtra("arrayLongValue");
        locationNameList=(ArrayList<String>) getIntent().getSerializableExtra("locationName");
        Log.d("locationNameList", String.valueOf(locationNameList));
        locationList= getIntent().getStringExtra("locationList");

        llConfigure=(LinearLayout)findViewById(R.id.llConfigure);
        fenceid=getfenceList();


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        pref.saveUpdateFlag(getIntent().getStringExtra("updateflag"));

        tvToolBar=(TextView)findViewById(R.id.tvToolBar);
        tvConfig=(TextView)findViewById(R.id.tvConfig);
        if (pref.getLanguage().equals("hi")){
            tvToolBar.setText("नक्शा बिंदु");
            tvConfig.setText("जियो फेंस को कॉन्फ़िगर करें");
        }else {
            tvToolBar.setText("Map point");
            tvConfig.setText("Configure Geo Fence");
        }

        // OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();





    }

private void onClick(){
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        llConfigure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfigureBottomSheet bottomSheetDialogFragment = new ConfigureBottomSheet();
                Bundle bundle = new Bundle();
                bundle. putString("locationPoint", locationList);
                bundle.putString("fenceid",fenceid);
                bottomSheetDialogFragment.setArguments(bundle);
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });
}

    @Override
    public void onPause() {
        super.onPause();

        // mapView.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    private String  getfenceList(){
        String fenceid;
        for (int i=0;i<latList.size();i++){
            fenceList.add(latList.get(i)+"-"+longList.get(i)+"#"+locationNameList.get(i));


            Log.d("locationList",fenceList.toString());

        }
        fenceid=fenceList.toString().trim();
        return fenceid;
    }


    private void setUpMapIfNeeded() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //  mapView.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(MapPointActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }


        for (int i = 0; i < latList.size(); i++) {

            strt = new LatLng(Double.parseDouble(latList.get(0)), Double.parseDouble(longList.get(0)));
            dstn = new LatLng(Double.parseDouble(latList.get(latList.size() - 1)), Double.parseDouble(longList.get(longList.size() - 1)));
            if (latList.size() == 2) {
                d = new LatLng(Double.parseDouble(latList.get(0)), Double.parseDouble(longList.get(0)));
                e = new LatLng(Double.parseDouble(latList.get(latList.size() - 1)), Double.parseDouble(longList.get(longList.size() - 1)));

                String url2 = getDirectionsUrl(d, e);
                Log.d("sturl", url2);
                DownloadTask downloadTask2 = new DownloadTask();
                downloadTask2.execute(url2);


            } else if (latList.size() == 3) {

                d = new LatLng(Double.parseDouble(latList.get(0)), Double.parseDouble(longList.get(0)));
                e = new LatLng(Double.parseDouble(latList.get(1)), Double.parseDouble(longList.get(1)));
                f = new LatLng(Double.parseDouble(latList.get(longList.size() - 1)), Double.parseDouble(longList.get(longList.size() - 1)));
                String url2 = getDirectionsUrl(d, e);
                Log.d("sturl", url2);
                DownloadTask downloadTask2 = new DownloadTask();
                downloadTask2.execute(url2);


                final Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(e, f);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);


                final Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(f, d);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);
            } else if (latList.size() == 4) {

                d = new LatLng(Double.parseDouble(latList.get(0)), Double.parseDouble(longList.get(0)));
                e = new LatLng(Double.parseDouble(latList.get(1)), Double.parseDouble(longList.get(1)));
                f = new LatLng(Double.parseDouble(latList.get(2)), Double.parseDouble(longList.get(2)));
                g = new LatLng(Double.parseDouble(latList.get(latList.size() - 1)), Double.parseDouble(longList.get(longList.size() - 1)));

                String url2 = getDirectionsUrl(d, e);
                Log.d("sturl", url2);
                DownloadTask downloadTask2 = new DownloadTask();
                downloadTask2.execute(url2);


                final Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(e, f);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);

                final Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(f, g);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);


                final Handler handler3 = new Handler();
                handler3.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(g, d);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);
            } else if (latList.size() == 5) {

                d = new LatLng(Double.parseDouble(latList.get(0)), Double.parseDouble(longList.get(0)));
                e = new LatLng(Double.parseDouble(latList.get(1)), Double.parseDouble(longList.get(1)));
                f = new LatLng(Double.parseDouble(latList.get(2)), Double.parseDouble(longList.get(2)));
                g = new LatLng(Double.parseDouble(latList.get(3)), Double.parseDouble(longList.get(3)));
                h = new LatLng(Double.parseDouble(latList.get(latList.size() - 1)), Double.parseDouble(longList.get(longList.size() - 1)));

                String url2 = getDirectionsUrl(d, e);
                Log.d("sturl", url2);
                DownloadTask downloadTask2 = new DownloadTask();
                downloadTask2.execute(url2);


                final Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(e, f);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);

                final Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(f, g);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);

                final Handler handler3 = new Handler();
                handler3.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(g, h);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);


                final Handler handler4 = new Handler();
                handler4.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(h, d);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);

            } else if (latList.size() == 6) {

                d = new LatLng(Double.parseDouble(latList.get(0)), Double.parseDouble(longList.get(0)));
                e = new LatLng(Double.parseDouble(latList.get(1)), Double.parseDouble(longList.get(1)));
                f = new LatLng(Double.parseDouble(latList.get(2)), Double.parseDouble(longList.get(2)));
                g = new LatLng(Double.parseDouble(latList.get(3)), Double.parseDouble(longList.get(3)));
                h = new LatLng(Double.parseDouble(latList.get(4)), Double.parseDouble(longList.get(4)));
                j = new LatLng(Double.parseDouble(latList.get(latList.size() - 1)), Double.parseDouble(longList.get(longList.size() - 1)));

                String url2 = getDirectionsUrl(d, e);
                Log.d("sturl", url2);
                DownloadTask downloadTask2 = new DownloadTask();
                downloadTask2.execute(url2);


                final Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(e, f);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);

                final Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(f, g);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);

                final Handler handler3 = new Handler();
                handler3.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(g, h);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);

                final Handler handler4 = new Handler();
                handler4.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(h, j);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);



                final Handler handler5 = new Handler();
                handler5.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(j, d);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);


            } else if (latList.size() == 7) {

                d = new LatLng(Double.parseDouble(latList.get(0)), Double.parseDouble(longList.get(0)));
                e = new LatLng(Double.parseDouble(latList.get(1)), Double.parseDouble(longList.get(1)));
                f = new LatLng(Double.parseDouble(latList.get(2)), Double.parseDouble(longList.get(2)));
                g = new LatLng(Double.parseDouble(latList.get(3)), Double.parseDouble(longList.get(3)));
                h = new LatLng(Double.parseDouble(latList.get(4)), Double.parseDouble(longList.get(4)));
                j = new LatLng(Double.parseDouble(latList.get(5)), Double.parseDouble(longList.get(5)));
                k = new LatLng(Double.parseDouble(latList.get(latList.size() - 1)), Double.parseDouble(longList.get(longList.size() - 1)));

                String url2 = getDirectionsUrl(d, e);
                Log.d("sturl", url2);
                DownloadTask downloadTask2 = new DownloadTask();
                downloadTask2.execute(url2);


                final Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(e, f);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);

                final Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(f, g);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);

                final Handler handler3 = new Handler();
                handler3.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(g, h);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);

                final Handler handler4 = new Handler();
                handler4.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(h, j);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);
                final Handler handler5 = new Handler();
                handler5.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(j, k);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);


                final Handler handler6 = new Handler();
                handler6.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(k, d);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);





            } else if (latList.size() == 8) {

                d = new LatLng(Double.parseDouble(latList.get(0)), Double.parseDouble(longList.get(0)));
                e = new LatLng(Double.parseDouble(latList.get(1)), Double.parseDouble(longList.get(1)));
                f = new LatLng(Double.parseDouble(latList.get(2)), Double.parseDouble(longList.get(2)));
                g = new LatLng(Double.parseDouble(latList.get(3)), Double.parseDouble(longList.get(3)));
                h = new LatLng(Double.parseDouble(latList.get(4)), Double.parseDouble(longList.get(4)));
                j = new LatLng(Double.parseDouble(latList.get(5)), Double.parseDouble(longList.get(5)));
                k = new LatLng(Double.parseDouble(latList.get(6)), Double.parseDouble(longList.get(6)));
                l = new LatLng(Double.parseDouble(latList.get(latList.size() - 1)), Double.parseDouble(longList.get(longList.size() - 1)));

                String url2 = getDirectionsUrl(d, e);
                Log.d("sturl", url2);
                DownloadTask downloadTask2 = new DownloadTask();
                downloadTask2.execute(url2);


                final Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(e, f);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);

                final Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(f, g);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);

                final Handler handler3 = new Handler();
                handler3.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(g, h);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);

                final Handler handler4 = new Handler();
                handler4.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(h, j);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);

                final Handler handler5 = new Handler();
                handler5.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(j, k);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);


                final Handler handler6 = new Handler();
                handler6.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(k, l);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);


                final Handler handler7 = new Handler();
                handler7.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(l, d);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);


            } else if (latList.size() == 8) {

                d = new LatLng(Double.parseDouble(latList.get(0)), Double.parseDouble(longList.get(0)));
                e = new LatLng(Double.parseDouble(latList.get(1)), Double.parseDouble(longList.get(1)));
                f = new LatLng(Double.parseDouble(latList.get(2)), Double.parseDouble(longList.get(2)));
                g = new LatLng(Double.parseDouble(latList.get(3)), Double.parseDouble(longList.get(3)));
                h = new LatLng(Double.parseDouble(latList.get(4)), Double.parseDouble(longList.get(4)));
                j = new LatLng(Double.parseDouble(latList.get(5)), Double.parseDouble(longList.get(5)));
                k = new LatLng(Double.parseDouble(latList.get(6)), Double.parseDouble(longList.get(6)));
                l = new LatLng(Double.parseDouble(latList.get(7)), Double.parseDouble(longList.get(7)));
                m = new LatLng(Double.parseDouble(latList.get(latList.size() - 1)), Double.parseDouble(longList.get(longList.size() - 1)));

                String url2 = getDirectionsUrl(d, e);
                Log.d("sturl", url2);
                DownloadTask downloadTask2 = new DownloadTask();
                downloadTask2.execute(url2);


                final Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(e, f);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);

                final Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(f, g);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);

                final Handler handler3 = new Handler();
                handler3.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(g, h);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);

                final Handler handler4 = new Handler();
                handler4.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(h, j);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);

                final Handler handler5 = new Handler();
                handler5.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(j, k);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);


                final Handler handler6 = new Handler();
                handler6.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(k, l);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);


                final Handler handler7 = new Handler();
                handler7.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(l, m);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);



                final Handler handler8 = new Handler();
                handler8.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(m, d);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);


            } else if (latList.size() == 9) {

                d = new LatLng(Double.parseDouble(latList.get(0)), Double.parseDouble(longList.get(0)));
                e = new LatLng(Double.parseDouble(latList.get(1)), Double.parseDouble(longList.get(1)));
                f = new LatLng(Double.parseDouble(latList.get(2)), Double.parseDouble(longList.get(2)));
                g = new LatLng(Double.parseDouble(latList.get(3)), Double.parseDouble(longList.get(3)));
                h = new LatLng(Double.parseDouble(latList.get(4)), Double.parseDouble(longList.get(4)));
                j = new LatLng(Double.parseDouble(latList.get(5)), Double.parseDouble(longList.get(5)));
                k = new LatLng(Double.parseDouble(latList.get(6)), Double.parseDouble(longList.get(6)));
                l = new LatLng(Double.parseDouble(latList.get(7)), Double.parseDouble(longList.get(7)));
                m = new LatLng(Double.parseDouble(latList.get(8)), Double.parseDouble(longList.get(8)));
                n = new LatLng(Double.parseDouble(latList.get(latList.size() - 1)), Double.parseDouble(longList.get(longList.size() - 1)));

                String url2 = getDirectionsUrl(d, e);
                Log.d("sturl", url2);
                DownloadTask downloadTask2 = new DownloadTask();
                downloadTask2.execute(url2);


                final Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(e, f);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);

                final Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(f, g);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);

                final Handler handler3 = new Handler();
                handler3.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(g, h);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);

                final Handler handler4 = new Handler();
                handler4.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(h, j);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);

                final Handler handler5 = new Handler();
                handler5.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(j, k);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);


                final Handler handler6 = new Handler();
                handler6.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(k, l);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);


                final Handler handler7 = new Handler();
                handler7.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(l, m);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);


                final Handler handler8 = new Handler();
                handler8.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(m, n);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);


                final Handler handler9 = new Handler();
                handler9.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(n, d);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);
            } else if (latList.size() == 10) {

                d = new LatLng(Double.parseDouble(latList.get(0)), Double.parseDouble(longList.get(0)));
                e = new LatLng(Double.parseDouble(latList.get(1)), Double.parseDouble(longList.get(1)));
                f = new LatLng(Double.parseDouble(latList.get(2)), Double.parseDouble(longList.get(2)));
                g = new LatLng(Double.parseDouble(latList.get(3)), Double.parseDouble(longList.get(3)));
                h = new LatLng(Double.parseDouble(latList.get(4)), Double.parseDouble(longList.get(4)));
                j = new LatLng(Double.parseDouble(latList.get(5)), Double.parseDouble(longList.get(5)));
                k = new LatLng(Double.parseDouble(latList.get(6)), Double.parseDouble(longList.get(6)));
                l = new LatLng(Double.parseDouble(latList.get(7)), Double.parseDouble(longList.get(7)));
                m = new LatLng(Double.parseDouble(latList.get(8)), Double.parseDouble(longList.get(8)));
                n = new LatLng(Double.parseDouble(latList.get(9)), Double.parseDouble(longList.get(9)));
                o = new LatLng(Double.parseDouble(latList.get(latList.size() - 1)), Double.parseDouble(longList.get(longList.size() - 1)));

                String url2 = getDirectionsUrl(d, e);
                Log.d("sturl", url2);
                DownloadTask downloadTask2 = new DownloadTask();
                downloadTask2.execute(url2);


                final Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(e, f);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);

                final Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(f, g);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);

                final Handler handler3 = new Handler();
                handler3.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(g, h);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);

                final Handler handler4 = new Handler();
                handler4.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(h, j);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);

                final Handler handler5 = new Handler();
                handler5.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(j, k);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);


                final Handler handler6 = new Handler();
                handler6.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(k, l);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);


                final Handler handler7 = new Handler();
                handler7.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(l, m);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);


                final Handler handler8 = new Handler();
                handler8.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(m, n);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);

                final Handler handler9 = new Handler();
                handler9.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(n, o);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);


                final Handler handler10 = new Handler();
                handler10.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(o, d);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);
            } else if (latList.size() == 11) {

                d = new LatLng(Double.parseDouble(latList.get(0)), Double.parseDouble(longList.get(0)));
                e = new LatLng(Double.parseDouble(latList.get(1)), Double.parseDouble(longList.get(1)));
                f = new LatLng(Double.parseDouble(latList.get(2)), Double.parseDouble(longList.get(2)));
                g = new LatLng(Double.parseDouble(latList.get(3)), Double.parseDouble(longList.get(3)));
                h = new LatLng(Double.parseDouble(latList.get(4)), Double.parseDouble(longList.get(4)));
                j = new LatLng(Double.parseDouble(latList.get(5)), Double.parseDouble(longList.get(5)));
                k = new LatLng(Double.parseDouble(latList.get(6)), Double.parseDouble(longList.get(6)));
                l = new LatLng(Double.parseDouble(latList.get(7)), Double.parseDouble(longList.get(7)));
                m = new LatLng(Double.parseDouble(latList.get(8)), Double.parseDouble(longList.get(8)));
                n = new LatLng(Double.parseDouble(latList.get(9)), Double.parseDouble(longList.get(9)));
                o = new LatLng(Double.parseDouble(latList.get(10)), Double.parseDouble(longList.get(10)));
                p = new LatLng(Double.parseDouble(latList.get(latList.size() - 1)), Double.parseDouble(longList.get(longList.size() - 1)));

                String url2 = getDirectionsUrl(d, e);
                Log.d("sturl", url2);
                DownloadTask downloadTask2 = new DownloadTask();
                downloadTask2.execute(url2);


                final Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(e, f);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);

                final Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(f, g);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);

                final Handler handler3 = new Handler();
                handler3.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(g, h);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);

                final Handler handler4 = new Handler();
                handler4.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(h, j);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);

                final Handler handler5 = new Handler();
                handler5.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(j, k);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);


                final Handler handler6 = new Handler();
                handler6.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(k, l);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);


                final Handler handler7 = new Handler();
                handler7.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(l, m);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);


                final Handler handler8 = new Handler();
                handler8.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(m, n);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);

                final Handler handler9 = new Handler();
                handler9.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(n, o);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);


                final Handler handler10 = new Handler();
                handler10.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(o, p);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);


                final Handler handler11 = new Handler();
                handler11.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(p, d);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);
            } else if (latList.size() == 12) {

                d = new LatLng(Double.parseDouble(latList.get(0)), Double.parseDouble(longList.get(0)));
                e = new LatLng(Double.parseDouble(latList.get(1)), Double.parseDouble(longList.get(1)));
                f = new LatLng(Double.parseDouble(latList.get(2)), Double.parseDouble(longList.get(2)));
                g = new LatLng(Double.parseDouble(latList.get(3)), Double.parseDouble(longList.get(3)));
                h = new LatLng(Double.parseDouble(latList.get(4)), Double.parseDouble(longList.get(4)));
                j = new LatLng(Double.parseDouble(latList.get(5)), Double.parseDouble(longList.get(5)));
                k = new LatLng(Double.parseDouble(latList.get(6)), Double.parseDouble(longList.get(6)));
                l = new LatLng(Double.parseDouble(latList.get(7)), Double.parseDouble(longList.get(7)));
                m = new LatLng(Double.parseDouble(latList.get(8)), Double.parseDouble(longList.get(8)));
                n = new LatLng(Double.parseDouble(latList.get(9)), Double.parseDouble(longList.get(9)));
                o = new LatLng(Double.parseDouble(latList.get(10)), Double.parseDouble(longList.get(10)));
                p = new LatLng(Double.parseDouble(latList.get(11)), Double.parseDouble(longList.get(11)));
                q = new LatLng(Double.parseDouble(latList.get(latList.size() - 1)), Double.parseDouble(longList.get(longList.size() - 1)));

                String url2 = getDirectionsUrl(d, e);
                Log.d("sturl", url2);
                DownloadTask downloadTask2 = new DownloadTask();
                downloadTask2.execute(url2);


                final Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(e, f);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);

                final Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(f, g);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);

                final Handler handler3 = new Handler();
                handler3.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(g, h);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);

                final Handler handler4 = new Handler();
                handler4.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(h, j);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);

                final Handler handler5 = new Handler();
                handler5.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(j, k);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);


                final Handler handler6 = new Handler();
                handler6.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(k, l);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);


                final Handler handler7 = new Handler();
                handler7.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(l, m);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);


                final Handler handler8 = new Handler();
                handler8.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(m, n);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);

                final Handler handler9 = new Handler();
                handler9.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(n, o);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);


                final Handler handler10 = new Handler();
                handler10.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(o, p);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);


                final Handler handler11 = new Handler();
                handler11.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(p, q);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);


                final Handler handler12 = new Handler();
                handler12.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(q, d);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);

            } else if (latList.size() == 13) {

                d = new LatLng(Double.parseDouble(latList.get(0)), Double.parseDouble(longList.get(0)));
                e = new LatLng(Double.parseDouble(latList.get(1)), Double.parseDouble(longList.get(1)));
                f = new LatLng(Double.parseDouble(latList.get(2)), Double.parseDouble(longList.get(2)));
                g = new LatLng(Double.parseDouble(latList.get(3)), Double.parseDouble(longList.get(3)));
                h = new LatLng(Double.parseDouble(latList.get(4)), Double.parseDouble(longList.get(4)));
                j = new LatLng(Double.parseDouble(latList.get(5)), Double.parseDouble(longList.get(5)));
                k = new LatLng(Double.parseDouble(latList.get(6)), Double.parseDouble(longList.get(6)));
                l = new LatLng(Double.parseDouble(latList.get(7)), Double.parseDouble(longList.get(7)));
                m = new LatLng(Double.parseDouble(latList.get(8)), Double.parseDouble(longList.get(8)));
                n = new LatLng(Double.parseDouble(latList.get(9)), Double.parseDouble(longList.get(9)));
                o = new LatLng(Double.parseDouble(latList.get(10)), Double.parseDouble(longList.get(10)));
                p = new LatLng(Double.parseDouble(latList.get(11)), Double.parseDouble(longList.get(11)));
                q = new LatLng(Double.parseDouble(latList.get(12)), Double.parseDouble(longList.get(12)));
                r = new LatLng(Double.parseDouble(latList.get(latList.size() - 1)), Double.parseDouble(longList.get(longList.size() - 1)));

                String url2 = getDirectionsUrl(d, e);
                Log.d("sturl", url2);
                DownloadTask downloadTask2 = new DownloadTask();
                downloadTask2.execute(url2);


                final Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(e, f);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);

                final Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(f, g);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);

                final Handler handler3 = new Handler();
                handler3.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(g, h);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);

                final Handler handler4 = new Handler();
                handler4.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(h, j);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);

                final Handler handler5 = new Handler();
                handler5.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(j, k);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);


                final Handler handler6 = new Handler();
                handler6.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(k, l);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);


                final Handler handler7 = new Handler();
                handler7.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(l, m);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);


                final Handler handler8 = new Handler();
                handler8.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(m, n);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);

                final Handler handler9 = new Handler();
                handler9.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(n, o);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);


                final Handler handler10 = new Handler();
                handler10.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(o, p);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);


                final Handler handler11 = new Handler();
                handler11.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(p, q);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);


                final Handler handler12 = new Handler();
                handler12.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(q, r);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);


                final Handler handler13 = new Handler();
                handler13.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url2 = getDirectionsUrl(r, d);
                        DownloadTask downloadTask2 = new DownloadTask();
                        Log.d("sturl", url2);

                        // Start downloading json data from Google Directions API
                        downloadTask2.execute(url2);

                    }
                }, timing);
            }

            double distance = CalculationByDistance(strt, dstn);

            String distance12 = String.format("%.3f", distance);

            createMarker(Double.parseDouble(latList.get(i)), Double.parseDouble(longList.get(i)));


            //  String dis = String.valueOf(CalculationByDistance(d, e))+"KM";


        }


        // Creates a CameraPosition from the builder


    }

    private void handleNewLocation(Location location) {

    }


    @Override
    public void onConnected(Bundle bundle) {

        if (ContextCompat.checkSelfPermission(MapPointActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (location == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            } else {
                handleNewLocation(location);
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(MapPointActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (ContextCompat.checkSelfPermission(MapPointActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (location == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            } else {
                handleNewLocation(location);
            }
        }


    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(MapPointActivity.this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //handleNewLocation(location);
    }


    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(MapPointActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MapPointActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(MapPointActivity.this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapPointActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(MapPointActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            // other 'case' lines to check for other
            // permissions this app might request
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(MapPointActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MapPointActivity.this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    protected Marker createMarker(double latitude, double longitude) {

        CameraPosition cameraPosition3 = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))      // Sets the center of the map to location user
                .zoom(12)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition3));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12));
        Log.d("markerset", "okk");

        return mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("here").icon(BitmapDescriptorFactory.fromResource(R.drawable.mapmarker)));

    }

    protected Marker sourcreateMarker(double latitude, double longitude, String title) {

        CameraPosition cameraPosition3 = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))      // Sets the center of the map to location user
                .zoom(12)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition3));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12));
        Log.d("markerset", "okk");

        return mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(title).icon(BitmapDescriptorFactory.fromResource(R.drawable.mapmarker)));

    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {


        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";
        String key = "key=AIzaSyCB0j4JDAEYdWakIncYaOrIKsN6Ql7Nmbc";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + key + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        Log.d("urlprint", url);

        return url;
    }


    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            //Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  setUpProgressDialog();

        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {


            try {

                ArrayList<LatLng> points = null;
                PolylineOptions lineOptions = new PolylineOptions();


                // Traversing through all the routes
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<LatLng>();
                    //lineOptions = new PolylineOptions();

                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);

                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);


                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    List<PatternItem> pattern = Arrays.asList(DOT, GAP, DOT, GAP);
                    lineOptions.pattern(pattern);
                    lineOptions.width(10);

                    lineOptions.color(Color.parseColor("#033301"));


                }
                //  textView.setText("Distance:"+distance + ", Duration:"+duration);
                // Drawing polyline in the Google Map for the i-th route
                mMap.addPolyline(lineOptions);
                //  progressDialog.dismiss();


            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
    }


    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        //  Toast.makeText(getApplicationContext(),"Radious Value:  "+valueResult+"  KM: "+kmInDec+" Meter: "+meterInDec,Toast.LENGTH_LONG).show();
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);
        return Radius * c;
    }





}
