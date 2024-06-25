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
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

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

import com.genius.payhrms.activity.utility.Constants;
import com.genius.payhrms.activity.utility.LocationAlertIntentService;
import com.genius.payhrms.activity.utility.LocationUpdateService;
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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;


public class GeoFenceActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
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
    double distance;
    AlertDialog alertDialog, alertDialog1;

    double grtlat, lesslat;
    double ownlat, ownlong;
    double glat, llat, glong, llong;
    String android_id;
    String address1;
    String serviceaddress;
    int flag;
    double ddis;
    public static final String DATA_SAVED_BROADCAST = "";
    private BroadcastReceiver broadcastReceiver;
    ;
    String date;
    double radiusValue;

    private BroadcastReceiver receiver;
    List<Double> latList;
    List<Double> longList;
    double currentLatitude,currentLongitude;
    LatLng p;
    boolean flagt=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geofence);
        // Toolbar tb = findViewById(R.id.toolbar);
       /* setSupportActionBar(tb);
        tb.setSubtitle("Location Alert");*/

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.g_map);
        mapFragment.getMapAsync(this);
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000);

        geofencingClient = LocationServices.getGeofencingClient(this);
        pref = new Pref(getApplicationContext());

        latitude = pref.getOwnLat();

        Log.d("slat", latitude);

        longitude = pref.getOwnLong();


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

        registerReceiver(broadcastReceiver, new IntentFilter(DATA_SAVED_BROADCAST));

        double endPoint = Double.parseDouble(pref.getEndPoint());
        double c = endPoint / 100;
        radiusValue = c;
        Log.d("radiusValue", String.valueOf(c));


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
        currentLongitude = location.getLongitude();
        Log.d("currentLongitude", String.valueOf(currentLongitude));

        ownlat = Double.parseDouble(pref.getOwnLat());
        Log.d("ownlat", String.valueOf(ownlat));
        ownlong = Double.parseDouble(pref.getOwnLong());
        Log.d("ownlong", String.valueOf(ownlong));

        double clat = Double.parseDouble(latitude);
        Log.d("clat", String.valueOf(clat));

        double clong = Double.parseDouble(pref.getOwnLong());

        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        LatLng elatLng = new LatLng(ownlat, ownlong);

        double dlat = ownlat - 0.0001000;


        if (ownlat > dlat) {
            //CalculationByDistance(latLng, elatLng);
        } else {
            //  Toast.makeText(getApplicationContext(), "out of range 1", Toast.LENGTH_LONG).show();
            //fenceout();
        }
        // Toast.makeText(getApplicationContext(), ownlat + "lat", Toast.LENGTH_LONG).show();
        //  Toast.makeText(getApplicationContext(), ownlong + "long", Toast.LENGTH_LONG).show();


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


        addLocationAlert(currentLatitude, currentLongitude);
        String address = getCompleteAddressString(currentLatitude, currentLongitude);
        Log.d("attenaddrsees", address);
        address1 = address.replaceAll("\\s+", "%20");
        //getValue();


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
                                ActivityCompat.requestPermissions(GeoFenceActivity.this,
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

    private void showfencedialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GeoFenceActivity.this, R.style.CustomDialogNew);
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
        TextView tvText = (TextView) dialogView.findViewById(R.id.tvText);
        if (pref.getLanguage().equals("hi")) {
            tvText.setText("उपस्थिति सफलतापूर्वक सहेज ली गई है\n");
        } else {
            tvText.setText("Attendance has been saved successfully");
        }


        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(true);
        Window window = alertDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog.show();


    }

    private void shownotfencedialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GeoFenceActivity.this, R.style.CustomDialogNew);
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
        TextView tvText = (TextView) dialogView.findViewById(R.id.tvText);
        if (pref.getLanguage().equals("hi")) {
            tvText.setText("आप अपने स्थान से सीमा से बाहर हैं");
        } else {
            tvText.setText("You are out of range from your location");
        }

        alertDialog1 = dialogBuilder.create();
        alertDialog1.setCancelable(true);
        Window window = alertDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog1.show();


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
        Log.d("RadiusValue", " KM " + kmInDec
                + " Meter " + meterInDec);
        String distance = String.format("%.3f", valueResult);
        final double ddis = Double.parseDouble(distance);
        Log.d("distance", String.valueOf(ddis));
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ddis < radiusValue || ddis == radiusValue) {
                    // Toast.makeText(getApplicationContext(), "fencing zone", Toast.LENGTH_LONG).show();


                  //  fencein(address1);


                } else {


                   // fenceout();


                }
            }
        }, 1000);


        // Toast.makeText(getApplicationContext(),distance+"KM",Toast.LENGTH_LONG).show();
        return ddis;
    }

    /*public double CalculationByDistanceforservice(LatLng StartP, LatLng EndP) {
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
        Log.d("RadiusValue", valueResult + " KM " + kmInDec
                + " Meter " + meterInDec);
        String distance = String.format("%.3f", valueResult);
        ddis = Double.parseDouble(distance);
        Log.d("chota", String.valueOf(ddis));


                if (ddis < radiusValue || ddis == radiusValue) {
                     Toast.makeText(getApplicationContext(), "fencing zone", Toast.LENGTH_LONG).show();


                        fenceinforservice(serviceaddress);


                } else {
                    Toast.makeText(getApplicationContext(), "not fencing zone", Toast.LENGTH_LONG).show();

                        fenceoutforservice();



                }



        // Toast.makeText(getApplicationContext(),distance+"KM",Toast.LENGTH_LONG).show();
        return Radius * c;
    }*/

   /* private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
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
    }*/





    @Override
    protected void onResume() {
        super.onResume();

        registerLocationtReceiver();
    }


    private void registerLocationtReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.foody.LOCATION_UPDATE_INTENT");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String lat = String.valueOf(intent.getDoubleExtra(Constants.ACTION.LATITUDE, 0));
                String longitude = String.valueOf(intent.getDoubleExtra(Constants.ACTION.LONGITUDE, 0));
               /* pref.saveString(Constants.PreferenceKey.CURRENT_LATITUDE, lat);
                pref.saveString(Constants.PreferenceKey.CURRENT_LONGITUDE, longitude);*/
                Log.d("Soumya1111", lat + "&" + longitude);
                LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(longitude));
                LatLng elatLng = new LatLng(Double.parseDouble(pref.getOwnLat()), Double.parseDouble(pref.getOwnLong()));
                serviceaddress = getCompleteAddressString(Double.parseDouble(lat), Double.parseDouble(longitude)).replaceAll("\\s+", "%20");

                //CalculationByDistanceforservice(latLng,elatLng);
            }
        };
        registerReceiver(receiver, filter);

    }

    private void removeLocationReceiver() {
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        startTracking();
        registerLocationtReceiver();

    }

    private void startTracking() {
        Intent startIntent = new Intent(this, LocationUpdateService.class);
        //startIntent.setAction(Constants.ACTION.START_ACTION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(startIntent);
        } else {
            startService(startIntent);
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
                String country = returnedAddress.getCountryName();
                String postal_code = returnedAddress.getPostalCode();
                String state = returnedAddress.getAdminArea();
                String city = returnedAddress.getLocality();


              /* pref.saveString(Constants.PreferenceKey.CURRENT_ADDRESS, strAdd);
               pref.saveString(Constants.PreferenceKey.CURRENT_COUNTRY, country);
               pref.saveString(Constants.PreferenceKey.CURRENT_POSTAL_CODE, postal_code);
               pref.saveString(Constants.PreferenceKey.CURRENT_STATE, state);
               pref.saveString(Constants.PreferenceKey.CURRENT_CITY, city);*/

                //  pref.saveCurrentAddress(strAdd);
                Log.w("My Current address", strReturnedAddress.toString());
            } else {
                Log.w("My Current address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current address", "Canont get Address!");
        }
        return strAdd;
    }




}
