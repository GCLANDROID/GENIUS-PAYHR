package com.genius.hrms.activity.geofence;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genius.hrms.R;

import com.genius.hrms.activity.activity.EmployeeDashBoardActivity;
import com.genius.hrms.activity.activity.UserDashBoardActivity;
import com.genius.hrms.activity.model.AddedLocationModel;
import com.genius.hrms.activity.model.MulFenceConfigModel;
import com.genius.hrms.activity.utility.LocationAlertIntentService;
import com.genius.hrms.activity.utility.Pref;
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
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UpdateMulFenceConfigActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public static final String TAG = UpdateMulFenceConfigActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    //  private MapView mapView;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    double currentLatitude, currentLongitude;
    String address1;
    String empId;
    String lat = "0";
    String longt = "0";
    String addr;
    GoogleApiClient googleApiClient;
    LatLng latLng;
    AlertDialog alertDialog, alertDialog1;
    LinearLayout llStart, llEnd;
    private static final int LOC_PERM_REQ_CODE = 1;
    //meters
    private static final int GEOFENCE_RADIUS = 10;
    //in milli seconds
    private static final int GEOFENCE_EXPIRATION = 6000;
    private GeofencingClient geofencingClient;
    Pref pref;
    ArrayList<String> readiusValue = new ArrayList<>();
    EditText etLocation;
    String configValue;
    String responseText;
    ImageView imgBack, imgHome,imgLatSearch;
    EditText etRadius;
    ImageView imgSearch;
    private View view;
    LinearLayout llNext, llSearch;
    public ArrayList<String> latList = new ArrayList<>();
    public ArrayList<String> longList = new ArrayList<>();
    public ArrayList<String> locationList = new ArrayList<>();
    ArrayList<AddedLocationModel>loactionName=new ArrayList<>();
    TextView tvAdded;
    EditText locationSearch;
    EditText etLat,etLong;
    ArrayList<MulFenceConfigModel>itemList=new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mul_fence_config);


        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000);


        setUpMapIfNeeded();
        llStart = (LinearLayout) findViewById(R.id.llStart);
        llStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = latList.toString();
                String latValue = a.replace("[", "").replace("]", "");

                String b = longList.toString();
                String longValue = b.replace("[", "").replace("]", "");

                String sLocation=locationList.toString();
                String locationPoint=sLocation.replace("[","").replace("]","");
                Log.d("locationPoint",locationPoint);


                Intent intent = new Intent(UpdateMulFenceConfigActivity.this, MapPointActivity.class);
                intent.putExtra("latValue", latValue);
                intent.putExtra("longtiValue", longValue);
                intent.putExtra("arrayLatValue", latList);
                intent.putExtra("arrayLongValue", longList);
                intent.putExtra("locationName",locationList);
                intent.putExtra("locationList",locationPoint);
                intent.putExtra("updateflag","1");
                startActivity(intent);
            }
        });
        llEnd = (LinearLayout) findViewById(R.id.llEnd);
        llEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoeDialog1();
            }
        });

         locationSearch = (EditText) findViewById(R.id.etLocation);

        geofencingClient = LocationServices.getGeofencingClient(this);
        pref = new Pref(getApplicationContext());
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        fenceget(getIntent().getStringExtra("fenceid"));
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateMulFenceConfigActivity.this, UserDashBoardActivity.class);
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
        imgSearch = (ImageView) findViewById(R.id.imgSearch);
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationSearch.getText().toString().length()>0) {
                    searchLocation();
                }else {
                    Toast.makeText(getApplicationContext(),"Please enter location name to search",Toast.LENGTH_LONG).show();
                }

            }
        });
        llNext = (LinearLayout) findViewById(R.id.llNext);

        llSearch = (LinearLayout) findViewById(R.id.llSearch);
        llNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latList.add(lat);
                longList.add(longt);
                locationList.add(address1);
                AddedLocationModel admodel=new AddedLocationModel(address1);
                loactionName.add(admodel);
                Log.d("loactionName",loactionName.toString());
                llSearch.setVisibility(View.VISIBLE);
                llNext.setVisibility(View.VISIBLE);
                Log.d("sizeoflat", String.valueOf(latList.size()));

                if (latList.size()>7){
                    llNext.setVisibility(View.VISIBLE);
                }else {
                    llNext.setVisibility(View.VISIBLE);
                }

                locationSearch.setText("");
                etLong.setText("");
                etLat.setText("");
                Toast.makeText(getApplicationContext(),"Location Added Successfully",Toast.LENGTH_LONG).show();

            }
        });
        tvAdded = (TextView) findViewById(R.id.tvAdded);

        tvAdded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("sixzz", String.valueOf(latList.size()));
                BottomSheetDialogFragment bottomSheetDialogFragment = new BootomSheet();
                Bundle bundle = new Bundle();
                bundle. putParcelableArrayList("arraylist", loactionName);
                bottomSheetDialogFragment.setArguments(bundle);
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });
        etLat=(EditText)findViewById(R.id.etLat);
        etLong=(EditText)findViewById(R.id.etLong);
        imgLatSearch=(ImageView)findViewById(R.id.imgLatSearch);
        imgLatSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etLat.getText().toString().length()>0) {
                    if (etLong.getText().toString().length()>0) {
                        searchLocationByLatLong(Double.parseDouble(etLat.getText().toString()), Double.parseDouble(etLong.getText().toString()));
                    }else {
                        Toast.makeText(getApplicationContext(),"Please enter longitude to search",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"Please enter lattitude to search",Toast.LENGTH_LONG).show();
                }
            }
        });
        pref.saveFenceId(getIntent().getStringExtra("fenceid"));


    }

    private void fenceget(String geofenceid) {
        String surl = pref.getIpAddress()+"GHRMSApi/api/get_GeofenceMultiEndPointConfiguration?LocationId="+geofenceid+"&Operation=1&SecurityCode="+pref.getSecurityCode();
        Log.d("configurl", surl);
        final ProgressDialog progressDialog=new ProgressDialog(UpdateMulFenceConfigActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseconfig", response);
                        progressDialog.dismiss();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("responseconfig", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {

                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);



                                    String CreatedOn = obj.optString("CreatedOn");
                                    String LocationName = obj.optString("LocationName");
                                    String Address = obj.optString("Address").trim();
                                    String GeoFenceId=obj.optString("GeoFenceId");
                                    AddedLocationModel mModel = new AddedLocationModel(Address);
                                    loactionName.add(mModel);



                                }




                            } else {

                            }


                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            //  Toast.makeText(EmployeeDashBoardActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Toast.makeText(EmployeeDashBoardActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
                showAlert();
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(UpdateMulFenceConfigActivity.this);
        requestQueue.add(stringRequest);


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


    private void setUpMapIfNeeded() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.g_map);
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
            if (ContextCompat.checkSelfPermission(UpdateMulFenceConfigActivity.this,
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

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());

        currentLatitude = location.getLatitude();
        lat = String.valueOf(currentLatitude);
        currentLongitude = location.getLongitude();
        longt = String.valueOf(currentLongitude);

        latLng = new LatLng(currentLatitude, currentLongitude);
        String address = getAddress(currentLatitude, currentLongitude);
        address1 = address.replaceAll(",", "=");

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(address)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mapmarker));


        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)      // Sets the center of the map to location user
                .zoom(25)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        mMap.addMarker(options);
        Circle circle = mMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(200)
                .strokeWidth(2)
                .strokeColor(Color.parseColor("#707070"))
                .fillColor(Color.parseColor("#751492E6")));
        addLocationAlert(currentLatitude, currentLongitude);
        Toast.makeText(getApplicationContext(), currentLatitude + "lat", Toast.LENGTH_LONG).show();

    }


    @Override
    public void onConnected(Bundle bundle) {

        if (ContextCompat.checkSelfPermission(UpdateMulFenceConfigActivity.this,
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

        mGoogleApiClient = new GoogleApiClient.Builder(UpdateMulFenceConfigActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (ContextCompat.checkSelfPermission(UpdateMulFenceConfigActivity.this,
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
                connectionResult.startResolutionForResult(UpdateMulFenceConfigActivity.this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
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
        handleNewLocation(location);
    }


    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(UpdateMulFenceConfigActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(UpdateMulFenceConfigActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(UpdateMulFenceConfigActivity.this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(UpdateMulFenceConfigActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(UpdateMulFenceConfigActivity.this,
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
                    if (ContextCompat.checkSelfPermission(UpdateMulFenceConfigActivity.this,
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
                    Toast.makeText(UpdateMulFenceConfigActivity.this, "permission denied", Toast.LENGTH_LONG).show();
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


    private void shoeDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(UpdateMulFenceConfigActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_latlong, null);
        dialogBuilder.setView(dialogView);
        TextView tvLat = (TextView) dialogView.findViewById(R.id.tvLat);
        String a = latList.toString();
        String latValue = a.replace("[", "").replace("]", "");
        tvLat.setText(latValue);

        String b = longList.toString();
        String longValue = b.replace("[", "").replace("]", "");
        Log.d("longValue", longValue);

        TextView tvLong = (TextView) dialogView.findViewById(R.id.tvLong);
        tvLong.setText(longValue);
        LinearLayout llSubmit = (LinearLayout) dialogView.findViewById(R.id.llSubmit);
        Spinner spRadius = (Spinner) dialogView.findViewById(R.id.spRadius);
        readiusValue.add("20");
        readiusValue.add("30");
        readiusValue.add("40");
        readiusValue.add("50");
        readiusValue.add("60");
        readiusValue.add("70");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, readiusValue);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRadius.setAdapter(dataAdapter);
        spRadius.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                configValue = readiusValue.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        etLocation = (EditText) dialogView.findViewById(R.id.etLocation);
        etRadius = (EditText) dialogView.findViewById(R.id.etRadius);


        llSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etLocation.getText().toString().length() > 0) {
                    if (etRadius.getText().toString().length() > 0) {
                        fenceConfig();
                        alertDialog.dismiss();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please set your fencing radius", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please set your fencing location", Toast.LENGTH_LONG).show();
                }
            }
        });

        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(true);
        Window window = alertDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog.show();


    }

    private void shoeDialog1() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(UpdateMulFenceConfigActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_latlong, null);
        dialogBuilder.setView(dialogView);
        TextView tvLat = (TextView) dialogView.findViewById(R.id.tvLat);
        tvLat.setText(String.valueOf(currentLatitude));
        TextView tvLong = (TextView) dialogView.findViewById(R.id.tvLong);
        tvLong.setText(String.valueOf(currentLongitude));
        LinearLayout llSubmit = (LinearLayout) dialogView.findViewById(R.id.llSubmit);
        llSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                alertDialog1.dismiss();
            }
        });


        alertDialog1 = dialogBuilder.create();
        alertDialog1.setCancelable(true);
        Window window = alertDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog1.show();

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


    private void requestLocationAccessPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOC_PERM_REQ_CODE);
    }


    private PendingIntent getGeofencePendingIntent() {
        Intent intent = new Intent(this, LocationAlertIntentService.class);
        return PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
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

    public void searchLocation() {
        Log.d("cpuhit", "1");


        address1 = locationSearch.getText().toString();
        List<Address> addressList = null;

        if (address1 != null || !address1.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(address1, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addressList.size()>0) {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title(address1));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                currentLatitude = address.getLatitude();
                lat = String.valueOf(currentLatitude);
                currentLongitude = address.getLongitude();
                longt = String.valueOf(currentLongitude);
                llNext.setVisibility(View.VISIBLE);
                llSearch.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), address.getLatitude() + " " + address.getLongitude(), Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getApplicationContext(),"Sorry!Can not find this location",Toast.LENGTH_LONG).show();
            }
        }
    }


    public Marker searchLocationByLatLong(double latitude, double longitude) {

        address1 = getCompleteAddressString(latitude, longitude).replaceAll(",","=");

        address1= getAddress(latitude,longitude).replaceAll(",","=");
        CameraPosition cameraPosition3 = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))      // Sets the center of the map to location user
                .zoom(12)                   // Sets the zoom
                .bearing(90)
                // Sets the orientation of the camera to east
                .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition3));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12));
        Log.d("markerset", "okk");
        lat=etLat.getText().toString();
        longt=etLong.getText().toString();



        return mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(address1).icon(BitmapDescriptorFactory.fromResource(R.drawable.mapmarker)));


        }



    private void fenceConfig() {
        String surl = pref.getIpAddress()+"GHRMSApi/api/get_GeofenceConfiguration?SLongitude=" + currentLongitude + "&SLatitude=" + currentLatitude + "&SAddress=" + address1 + "&ELongitude=" + currentLongitude + "&ELatitude=" + currentLatitude + "&EAddress=" + address1 + "&EndPoint=" + etRadius.getText().toString() + "&LocationName=" + etLocation.getText().toString().replaceAll("\\s+", "") + "&Operation=3&SecurityCode=" + pref.getSecurityCode();
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();
        Log.d("configurl", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseconfig", response);
                        progressBar.dismiss();
                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("responseconfig", "@@@@@@" + job1);
                            responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {

                                showAlert();


                            }


                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(UpdateMulFenceConfigActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
                Toast.makeText(UpdateMulFenceConfigActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(UpdateMulFenceConfigActivity.this);
        requestQueue.add(stringRequest);


    }


    private void showAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(responseText);
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                        Intent intent = new Intent(UpdateMulFenceConfigActivity.this, MultipleConfigReportActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        alertDialogBuilder.show();


    }


    public String getAddress(double lat, double lng) {
        String address = null;
        Geocoder geocoder = new Geocoder(UpdateMulFenceConfigActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            address=add;;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return address;
    }







}
