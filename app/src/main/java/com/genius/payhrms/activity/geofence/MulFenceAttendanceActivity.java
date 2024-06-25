package com.genius.payhrms.activity.geofence;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genius.payhrms.R;

import com.genius.payhrms.activity.model.MulFenceModel;
import com.genius.payhrms.activity.utility.LocationAlertIntentService;
import com.genius.payhrms.activity.utility.MulLocationUpdatingService;
import com.genius.payhrms.activity.utility.Pref;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;

public class MulFenceAttendanceActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final int LOC_PERM_REQ_CODE = 1;
    //meters
    private static final int GEOFENCE_RADIUS = 10;
    //in milli seconds
    private static final int GEOFENCE_EXPIRATION = 6000;

    private GoogleMap mMap;

    private GeofencingClient geofencingClient;
    private final int[] MAP_TYPES = {GoogleMap.MAP_TYPE_SATELLITE,
            MAP_TYPE_NORMAL,
            GoogleMap.MAP_TYPE_HYBRID,
            GoogleMap.MAP_TYPE_TERRAIN,
            GoogleMap.MAP_TYPE_NONE};
    private LatLng destPosition;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    double lat, longi;
    Pref pref;
    String longitude, latitude;

    AlertDialog alertDialog, alertDialog1;


    String android_id;
    String address1;
    int flag;
    double ddis;
    //public static final String DATA_SAVED_BROADCAST = "http://111.93.182.174/GeniusiOSApi/api/Geofence?";
    private BroadcastReceiver broadcastReceiver;;
    String date;
    float radiusValue;
    ArrayList<MulFenceModel>itemList=new ArrayList<>();
    String flat,seclat,thrdlat,frthlat,fivlat,sixlat,sevenlat,eightlat,ninlat,tenlat;
    TreeSet<String> frstValue=new TreeSet<>();
    double maxfrstlat,maxseclat,maxthrdlat,maxfrthlat,maxfivlat,maxsixlat,maxsevlat,maxeightlat,maxninlat,maxtenlat,minfrslat,minseclat,minthrdlat,minfrthlat,minfivlat,minsixlat,minsevlat,mineightlat,minninlat,mintenlat;
    TreeSet<String> scndValue=new TreeSet<>();
    TreeSet<String> thrdValue=new TreeSet<>();
    TreeSet<String> fourValue=new TreeSet<>();
    TreeSet<String> fiveValue=new TreeSet<>();
    TreeSet<String> sixValue=new TreeSet<>();
    TreeSet<String> sevenValue=new TreeSet<>();
    String caddress;
    String latt,longtt;
    double currentLatitude;
    boolean flagt=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mul_fence_attendance);
        initView();


    }

    private void initView(){

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.g_map);
        mapFragment.getMapAsync(this);
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000);

        geofencingClient = LocationServices.getGeofencingClient(this);
        pref = new Pref(getApplicationContext());

        latitude = "22.7898989";

        Log.d("slat", latitude);

        longitude = "88.79779";


        Log.d("slong", longitude);



        lat = Double.parseDouble(latitude);
        Log.d("double", String.valueOf(lat));
        longi = Double.parseDouble(longitude);


        destPosition = new LatLng(lat, longi);
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.d("android_id", android_id);
        if (ddis < 0.020 || ddis == 0.020) {
            flag = 1;
            Log.d("ddis", String.valueOf(flag));

        } else {
            flag = 2;
            Log.d("ddis", String.valueOf(flag));
        }
        final Handler handler = new Handler();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //loading the names again

            }
        };




        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String currentDateTimeString = sdf.format(d);

        Date dof = Calendar.getInstance().getTime();


        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(dof);

        date = formattedDate+"="+currentDateTimeString;

    }



    private void getfenceValue(){
        flat=itemList.get(0).getLattitude();
        seclat=itemList.get(1).getLattitude();
        Log.d("flat",flat);
        Log.d("seclat",seclat);

        frstValue.add(flat);
        frstValue.add(seclat);
        maxfrstlat= Double.parseDouble(frstValue.first());
        minfrslat= Double.parseDouble(frstValue.last());


        thrdlat=itemList.get(2).getLattitude();

        scndValue.add(seclat);
        scndValue.add(thrdlat);

        minseclat=Double.parseDouble(scndValue.last());
        maxseclat=Double.parseDouble(scndValue.first());



        if (itemList.size()==4){
            frthlat=itemList.get(3).getLattitude();
            thrdValue.add(frthlat);
            thrdValue.add(thrdlat);

            maxthrdlat= Double.parseDouble(thrdValue.first());
            minthrdlat=Double.parseDouble(thrdValue.last());
            Log.d("maxvalue",thrdValue.first());
            Log.d("minvalue",thrdValue.last());



        }

        if (itemList.size()==5){
            frthlat=itemList.get(3).getLattitude();
            fivlat=itemList.get(4).getLattitude();
            fourValue.add(frthlat);
            fourValue.add(fivlat);

            maxfrthlat=Double.parseDouble(fourValue.first());
            minfrthlat=Double.parseDouble(fourValue.last());

            Log.d("maxfourvalue",fourValue.first());
            Log.d("minfourvalue",fourValue.last());



        }


        if (itemList.size()==6){
            fivlat=itemList.get(4).getLattitude();
            sixlat=itemList.get(5).getLattitude();
            fiveValue.add(sixlat);
            fiveValue.add(fivlat);

            maxfivlat=Double.parseDouble(fiveValue.first());
            minfivlat=Double.parseDouble(fiveValue.last());

            Log.d("maxfivevalue",fiveValue.first());
            Log.d("minfivevalue",fiveValue.last());



        }


        if (itemList.size()==7){

            sixlat=itemList.get(5).getLattitude();
            sevenlat=itemList.get(6).getLattitude();
            sixValue.add(sixlat);
            sixValue.add(sevenlat);

            maxsixlat=Double.parseDouble(sixValue.first());
            minsixlat=Double.parseDouble(sixValue.last());

            Log.d("maxsixvalue",sixValue.first());
            Log.d("minsixvalue",sixValue.last());



        }



        if (itemList.size()==8){


            sevenlat=itemList.get(6).getLattitude();
            eightlat=itemList.get(7).getLattitude();
            sevenValue.add(eightlat);
            sevenValue.add(sevenlat);

            maxsevlat=Double.parseDouble(sevenValue.first());
            minsevlat=Double.parseDouble(sevenValue.last());

            Log.d("maxssevenvalue",sevenValue.first());
            Log.d("minssevenalue",sevenValue.last());



        }








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
            if (ContextCompat.checkSelfPermission(this,
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




        // Creates a CameraPosition from the builder

    }

    @SuppressLint("MissingPermission")
    private void showCurrentLocationOnMap() {
        if (isLocationAccessPermitted()) {
            requestLocationAccessPermission();
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
    }

    private boolean isLocationAccessPermitted() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestLocationAccessPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOC_PERM_REQ_CODE);
    }

    @SuppressLint("MissingPermission")
    private void addLocationAlert(double lat, double lng) {
        if (isLocationAccessPermitted()) {
            requestLocationAccessPermission();
        } else {
            String key = "" + lat + "-" + lng;
            Log.d("geofence", key);
            Geofence geofence = getGeofence(lat, lng, key);
            geofencingClient.addGeofences(getGeofencingRequest(geofence),
                    getGeofencePendingIntent())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                            } else {
                            }
                        }
                    });
        }
    }



    private PendingIntent getGeofencePendingIntent() {
        Intent intent = new Intent(this, LocationAlertIntentService.class);
        return PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private GeofencingRequest getGeofencingRequest(Geofence geofence) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL);
        builder.addGeofence(geofence);
        return builder.build();
    }

    private Geofence getGeofence(double lat, double lang, String key) {
        return new Geofence.Builder()
                .setRequestId(key)
                .setCircularRegion(lat, lang, GEOFENCE_RADIUS)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_DWELL)
                .setLoiteringDelay(10000)
                .build();
    }

    /*    @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu, menu);
            return true;
        }*/


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            final Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (location == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            } else {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handleNewLocation(location);
                    }
                }, 2000);
            }
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
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

        }

    }

    @Override
    public void onLocationChanged(final Location location) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handleNewLocation(location);
            }
        }, 2000);


    }


    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    private void handleNewLocation(Location location) {


         currentLatitude = location.getLatitude();
        Log.d("currentLatitude", String.valueOf(currentLatitude));
        double currentLongitude = location.getLongitude();
        Log.d("currentLongitude", String.valueOf(currentLongitude));
        latt=String.valueOf(currentLatitude);
        String longtt=String.valueOf(currentLongitude);
        caddress=getCompleteAddressString(currentLatitude,currentLongitude).replaceAll("\\s+", "");



        LatLng latLng = new LatLng(currentLatitude, currentLongitude);


        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("I am here!")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mapmarker));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)      // Sets the center of the map to location user
                .zoom(35)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(0)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
        mMap.addMarker(new MarkerOptions().position(latLng));

        mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));


        if (itemList.size()==3) {

            if (currentLatitude > maxfrstlat && currentLatitude < minfrslat) {

                   // fencein(caddress);
                  //  Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();



            } else if (currentLatitude > maxseclat && currentLatitude < minseclat) {

                  //  fencein(caddress);
                   // Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            } else {

                  //  fenceout();


            }
        }

        if (itemList.size()==4){
            if (currentLatitude>maxfrstlat && currentLatitude<minfrslat){

                   // fencein(caddress);
                  //  Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }else if (currentLatitude>maxseclat && currentLatitude<minseclat){

                   // fencein(caddress);
                   // Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }else if (currentLatitude>maxthrdlat && currentLatitude<minthrdlat){

                   // fencein(caddress);
                   // Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }else {

                   // fenceout();

            }
        }


        if (itemList.size()==5){
            if (currentLatitude>maxfrstlat && currentLatitude<minfrslat){

                   // fencein(caddress);
                   // Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }else if (currentLatitude>maxseclat && currentLatitude<minseclat){

                   // fencein(caddress);
                    //Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }else if (currentLatitude>maxthrdlat && currentLatitude<minthrdlat){

                   // fencein(caddress);
                  //  Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }else if (currentLatitude>maxfrthlat && currentLatitude<minfrthlat){

                   // fencein(caddress);
                  //  Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }
            else {

                   // fenceout();

            }
        }


        if (itemList.size()==6){
            if (currentLatitude>maxfrstlat && currentLatitude<minfrslat){

                   // fencein(caddress);
                   // Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();


            }else if (currentLatitude>maxseclat && currentLatitude<minseclat){

                   // fencein(caddress);
                   // Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }else if (currentLatitude>maxthrdlat && currentLatitude<minthrdlat){

                   // fencein(caddress);
                   // Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }else if (currentLatitude>maxfrthlat && currentLatitude<minfrthlat){

                   // fencein(caddress);
                  //  Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }
            else if (currentLatitude>maxfivlat && currentLatitude<minfivlat){

                 //   fencein(caddress);
                   // Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }
            else {

                  //  fenceout();

            }
        }



        if (itemList.size()==7){
            if (currentLatitude>maxfrstlat && currentLatitude<minfrslat){

                    //fencein(caddress);
                  //  Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }else if (currentLatitude>maxseclat && currentLatitude<minseclat){

                   // fencein(caddress);
                  //  Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }else if (currentLatitude>maxthrdlat && currentLatitude<minthrdlat){

                   // fencein(caddress);
                  //  Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();


            }else if (currentLatitude>maxfrthlat && currentLatitude<minfrthlat){

                   // fencein(caddress);
                   // Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }
            else if (currentLatitude>maxfivlat && currentLatitude<minfivlat){

                    //fencein(caddress);
                  //  Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }
            else if (currentLatitude>maxsixlat && currentLatitude<minsixlat){

                  //  fencein(caddress);
                  //  Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }
            else {

                    //fenceout();

            }
        }




        if (itemList.size()==8){
            if (currentLatitude>maxfrstlat && currentLatitude<minfrslat){

                   // fencein(caddress);
                   // Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();


            }else if (currentLatitude>maxseclat && currentLatitude<minseclat){

                   // fencein(caddress);
                   // Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }else if (currentLatitude>maxthrdlat && currentLatitude<minthrdlat){

                   // fencein(caddress);
                  //  Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }else if (currentLatitude>maxfrthlat && currentLatitude<minfrthlat){

                   // fencein(caddress);
                   // Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }
            else if (currentLatitude>maxfivlat && currentLatitude<minfivlat){

                   // fencein(caddress);
                   // Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }
            else if (currentLatitude>maxsixlat && currentLatitude<minsixlat){

                    //fencein(caddress);
                 //   Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }
            else if (currentLatitude>maxsevlat && currentLatitude<minsevlat){

                   // fencein(caddress);
                 //   Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }
            else {

                    //fenceout();

            }
        }


        addLocationAlert(currentLatitude, currentLongitude);
        //getMulValue();






        // CalculationByDistance(latLng, destPosition);

    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MulFenceAttendanceActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
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
                    if (ContextCompat.checkSelfPermission(this,
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
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current ", strReturnedAddress.toString());
            } else {
                Log.w("My Current", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current", "Canont get Address!");
        }
        return strAdd;
    }



    private void showfencedialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MulFenceAttendanceActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_fence, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llSubmit = (LinearLayout) dialogView.findViewById(R.id.llSubmit);
        llSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });
        TextView tvText=(TextView)dialogView.findViewById(R.id.tvText);
        if (pref.getLanguage().equals("hi")){
            tvText.setText("गतिविधि सफलतापूर्वक सबमिट की गई");
        }else {
            tvText.setText("Activity submitted successfully");
        }

        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(true);
        Window window = alertDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog.show();


    }

    private void shownotfencedialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MulFenceAttendanceActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_not_fence, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llSubmit = (LinearLayout) dialogView.findViewById(R.id.llSubmit);
        llSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog1.dismiss();
            }
        });
        TextView tvText=(TextView)dialogView.findViewById(R.id.tvText);
        if (pref.getLanguage().equals("hi")){
            tvText.setText("आप अपने स्थान से सीमा से बाहर हैं");
        }else {
            tvText.setText("You are out of range from your location");
        }

        alertDialog1 = dialogBuilder.create();
        alertDialog1.setCancelable(true);
        Window window = alertDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog1.show();


    }





    private void startTracking() {
        Intent startIntent = new Intent(this, MulLocationUpdatingService.class);

        //startIntent.setAction(Constants.ACTION.START_ACTION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(startIntent);
        } else {
            startService(startIntent);
        }
    }




}
