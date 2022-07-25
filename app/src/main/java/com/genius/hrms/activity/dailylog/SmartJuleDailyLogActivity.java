package com.genius.hrms.activity.dailylog;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.developers.imagezipper.ImageZipper;
import com.genius.hrms.R;
import com.genius.hrms.activity.activity.UserDashBoardActivity;
import com.genius.hrms.activity.attendance.AttendanceManageActivity;
import com.genius.hrms.activity.attendance.AttendanceReportActivity;
import com.genius.hrms.activity.utility.NetworkConnectionCheck;
import com.genius.hrms.activity.utility.Pref;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
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
import com.wajahatkarim3.longimagecamera.LongImageCameraActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SmartJuleDailyLogActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static final String TAG = AttendanceManageActivity.class.getSimpleName();
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    protected GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    double lat = 0, lng = 0;
    String laat, lit, adrstr, imgString;
    TextView ad_text;
    String base64StringOfCameraPic;
    FusedLocationProviderClient fusedLocationProviderClient;
    ImageView img_capture, img_take;
    Button subbtn;
    Bitmap bitmap;
    Bitmap photo;
    String loat, ling;
    Uri tempUri;
    AlertDialog alerDialog1, alertDialog;
    Pref pref;
    ImageView imgBack, imgHome;
    File file, compressedImageFile;
    int flag = 0;
    NetworkConnectionCheck connectionCheck;
    LatLng latLng;
    String address;
    ArrayList<String> attendanceOptionList = new ArrayList<>();
    String attendnaceOption;
    String attenID;
    String securityCode;
    String attCode;
    private GoogleMap mMap;
    //  private MapView mapView;
    private LocationRequest mLocationRequest;
    String address1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_em3attendnace);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        pref = new Pref(SmartJuleDailyLogActivity.this);
        securityCode = "1153";

        connectionCheck = new NetworkConnectionCheck(SmartJuleDailyLogActivity.this);
        mLocationRequest = new LocationRequest();
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000);

        ad_text = findViewById(R.id.ad_text);
        img_capture = findViewById(R.id.img_capture);
        img_take = findViewById(R.id.img_take);
        subbtn = findViewById(R.id.subbtn);


        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        attCode = getIntent().getStringExtra("attCode");
        setUpMapIfNeeded();
        onClick();


    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(SmartJuleDailyLogActivity.this,
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
    public void onConnectionSuspended(int i) {
        if (ContextCompat.checkSelfPermission(SmartJuleDailyLogActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (location == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            } else {
                handleNewLocation(location);
            }
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(SmartJuleDailyLogActivity.this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {

            // other 'case' lines to check for other
            // permissions this app might request
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(SmartJuleDailyLogActivity.this,
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
                    Toast.makeText(SmartJuleDailyLogActivity.this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        //mMap.setMinZoomPreference(25);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.setMinZoomPreference(15);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(SmartJuleDailyLogActivity.this,
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
       /* mMap = googleMap;

        mMap = googleMap;
        mMap.setMapType( MAP_TYPES[MAP_TYPE_SATELLITE] );

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMinZoomPreference(15);

        showCurrentLocationOnMap();*/


    }

    @Override
    public void onPause() {
        super.onPause();

        // mapView.onPause();
        /*if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }*/
    }

    private void setUpMapIfNeeded() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //  mapView.getMapAsync(this);

    }

    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(SmartJuleDailyLogActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(SmartJuleDailyLogActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(SmartJuleDailyLogActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(SmartJuleDailyLogActivity.this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(SmartJuleDailyLogActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(SmartJuleDailyLogActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }


    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());

        lat = location.getLatitude();
        laat = String.valueOf(lat);
        lng = location.getLongitude();
        ling = String.valueOf(lng);

        latLng = new LatLng(lat, lng);
        address = getCompleteAddressString(lat, lng);
        address1= address.replaceAll("#","abc").replaceAll("\\s+", "_");
        Log.d("attenaddrsees", address);


        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(address)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mapmarker));
        ad_text.setText("You are at: " + address);


        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)      // Sets the center of the map to location user
                .zoom(16)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        mMap.addMarker(options);

    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder();

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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case LongImageCameraActivity.LONG_IMAGE_RESULT_CODE:


                if (resultCode == RESULT_OK && requestCode == LongImageCameraActivity.LONG_IMAGE_RESULT_CODE) {
                    file = (File) data.getExtras().get("picture");
                    try {
                        compressedImageFile = new ImageZipper(SmartJuleDailyLogActivity.this)
                                .setQuality(80)
                                .setMaxWidth(250)
                                .setMaxHeight(250)
                                .compressToFile(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String imageFileName = data.getStringExtra(LongImageCameraActivity.IMAGE_PATH_KEY);
                    Log.d("imageFileName", imageFileName);
                    Bitmap d = BitmapFactory.decodeFile(imageFileName);
                    int newHeight = (int) (d.getHeight() * (512.0 / d.getWidth()));
                    Bitmap putImage = Bitmap.createScaledBitmap(d, 512, newHeight, true);
                    img_capture.setImageBitmap(putImage);
                    flag = 1;

                }
                break;


        }
    }

    private void onClick() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SmartJuleDailyLogActivity.this, UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        subbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  if (flag==1) {
                    attenDancePunch("ok", "0");
                }else {
                    attendancefunction();
                }*/

                if (attCode.equals("1"))
                {
                    if (flag==1) {
                        attenDancePunchTest("OK", "0");
                    }else {

                        Toast.makeText(SmartJuleDailyLogActivity.this, "Please Click Your Selfie Image", Toast.LENGTH_LONG).show();

                    }
                }else {
                    attendanceAlert();
                }


            }
        });

        img_take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LongImageCameraActivity.launch(SmartJuleDailyLogActivity.this);
            }
        });
    }

    private void successAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SmartJuleDailyLogActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);

        tvInvalidDate.setText("Attendance saved successfully");


        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                finish();
                Intent intent = new Intent(SmartJuleDailyLogActivity.this, AttendanceReportActivity.class);
                startActivity(intent);
            }
        });

        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }

    private void attendanceAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SmartJuleDailyLogActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_attendnaceoption, null);
        dialogBuilder.setView(dialogView);
        Spinner spOption = (Spinner) dialogView.findViewById(R.id.spOption);
        attendanceOptionList.add("Home");
        attendanceOptionList.add("Office");
        final LinearLayout llReason = (LinearLayout) dialogView.findViewById(R.id.llReason);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (SmartJuleDailyLogActivity.this, android.R.layout.simple_spinner_item,
                        attendanceOptionList); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spOption.setAdapter(spinnerArrayAdapter);

        final EditText etReason = (EditText) dialogView.findViewById(R.id.etReason);
        spOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                attendnaceOption = attendanceOptionList.get(i);
                if (attendnaceOption.equals("Home")) {
                    attenID = "1";
                    llReason.setVisibility(View.VISIBLE);

                } else {
                    attenID = "0";
                    llReason.setVisibility(View.GONE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        LinearLayout llSubmit = (LinearLayout) dialogView.findViewById(R.id.llSubmit);
        llSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (attenID.equals("1")) {
                    if (etReason.getText().toString().length() > 0) {
                        if (flag==1) {
                            attenDancePunchTest(etReason.getText().toString(), "1");
                        }else {

                            Toast.makeText(SmartJuleDailyLogActivity.this, "Please Click Your Selfie Image", Toast.LENGTH_LONG).show();

                        }
                    } else {
                        Toast.makeText(SmartJuleDailyLogActivity.this, "Please Enter Your Reason", Toast.LENGTH_LONG).show();
                    }

                } else {
                    if (flag==1) {
                        attenDancePunchTest("OK", "0");
                    }else {

                        Toast.makeText(SmartJuleDailyLogActivity.this, "Please Click Your Selfie Image", Toast.LENGTH_LONG).show();

                    }
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

    private void attenDancePunch(String reason, String attenID) {
        final ProgressDialog progressDialog = new ProgressDialog(SmartJuleDailyLogActivity.this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        AndroidNetworking.upload("https://cloud.geniusconsultant.com/GHRMSApi/api/post_SelfAttendanceWithImage")
                .addMultipartFile("ImageFile", compressedImageFile)
                .addMultipartParameter("AEMEmployeeID", pref.getEmpId())
                .addMultipartParameter("Address", address)
                .addMultipartParameter("Longitude", ling)
                .addMultipartParameter("Latitude", laat)
                .addMultipartParameter("SecurityCode", "1135")
                .setTag("Uploadfirst")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {

                    }
                }).getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("response", String.valueOf(response));
                progressDialog.dismiss();
                JSONObject ob = response;
                String s1 = ob.optString("responseCode");
                if (s1.equals("1")) {
                    successAlert();

                }

            }

            @Override
            public void onError(ANError anError) {
                progressDialog.dismiss();
                Log.i("onError", String.valueOf(anError));
            }
        });
    }




    private void attenDancePunchTest(String reason, String attenID) {
        final ProgressDialog progressDialog = new ProgressDialog(SmartJuleDailyLogActivity.this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        AndroidNetworking.upload(pref.getIpAddress() + "GHRMSApi/api/post_DailyLogSmartJoules")
                .addMultipartFile("ImageFile", compressedImageFile)
                .addMultipartParameter("AEMEmployeeID", pref.getEmpId())
                .addMultipartParameter("ProjectAID","0")
                .addMultipartParameter("SubProjectAID","0")
                .addMultipartParameter("ApprovalStatus","0")
                .addMultipartParameter("Remarks",reason)
                .addMultipartParameter("Longitude", ling)
                .addMultipartParameter("Latitude", laat)
                .addMultipartParameter("Address", address)
                .addMultipartParameter("Year", "0")
                .addMultipartParameter("Month", "0")
                .addMultipartParameter("ApprovalStatus", "0")
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())
                .addMultipartParameter("PunchFrom",attenID)
                .setTag("Uploadfirst")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {

                    }
                }).getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("response", String.valueOf(response));
                progressDialog.dismiss();
                JSONObject ob = response;
                String s1 = ob.optString("responseCode");
                if (s1.equals("1")) {
                    successAlert();

                }
                // /data/user/0/com.genius.hrms/cache/images/1658470402880.png

            }

            @Override
            public void onError(ANError anError) {
                progressDialog.dismiss();
                Log.i("onError", String.valueOf(anError));
            }
        });
    }



    private void attenDancePunchTestWithoutImage(String reason, String attenID) {
        String surl = pref.getIpAddress() + "GHRMSApi/api/post_SelfAttendanceEM3?AEMEmployeeID=" + pref.getEmpId() + "&Address=" + address1 + "&Longitude=" + ling + "&Latitude=" + laat + "&SecurityCode=" + pref.getSecurityCode()+"&PunchFrom="+attenID+"&PunchFromReason="+reason.replaceAll("\\s+", "%20");
        Log.d("attendenceinput", surl);
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLeave", response);
                        progressBar.dismiss();
                        try {
                            JSONObject job1 = new JSONObject(response);

                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                successAlert();


                            }


                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SmartJuleDailyLogActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
             //   Toast.makeText(SmartJuleDailyLogActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(SmartJuleDailyLogActivity.this);
        requestQueue.add(stringRequest);

    }

    private void check() {

    }
}
