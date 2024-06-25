package com.genius.payhrms.activity.geofence;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.developers.imagezipper.ImageZipper;
import com.genius.payhrms.R;
import com.genius.payhrms.activity.activity.UserDashBoardActivity;
import com.genius.payhrms.activity.attendance.AttendanceManageActivity;
import com.genius.payhrms.activity.dailylog.NumberTourActivity;
import com.genius.payhrms.activity.dailylog.OfflineDailyDashBoardActivity;
import com.genius.payhrms.activity.helper.DatabaseHelper;
import com.genius.payhrms.activity.utility.ApiClient;
import com.genius.payhrms.activity.utility.AttendanceService;
import com.genius.payhrms.activity.utility.GPSTracker;
import com.genius.payhrms.activity.utility.NetworkConnectionCheck;
import com.genius.payhrms.activity.utility.Pref;
import com.genius.payhrms.activity.utility.UploadObject;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.inforoeste.mocklocationdetector.MockLocationDetector;
import com.wajahatkarim3.longimagecamera.LongImageCameraActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GeoFenceDailyLogManageActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public static final String TAG = AttendanceManageActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    //  private MapView mapView;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    BottomSheetDialog dialog;
    TextView tvtextAddress;
    TextView tvcapturest;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    NetworkConnectionCheck connectionCheck;
    LatLng latLng;
    TextView tvAddress;
    ImageView imgCamera, imgPic;
    String userChoosenTask = "";
    private String encodedImage;
    private Uri imageUri;
    private static final int CAMERA_REQUEST = 1;
    private static final int GALLERY_REQUEST = 2;
    ImageView imgBack, imgHome;
    String address = "N/A";
    double currentLatitude, currentLongitude;
    String address1;
    String lat = "0";
    String longt = "0";
    int flag;
    AlertDialog alerDialog1, alertDialog, alertDialog2;
    GoogleApiClient googleApiClient;
    int addflag = 0;
    AlertDialog alert1;
    File file, seccondcompress, thirdcompress;
    private static String SERVER_PATH = "";
    private AttendanceService uploadService;
    ProgressDialog progressDialog;
    Pref pref;
    EditText etRemarks;
    Button btnSubmit;
    int cameraflag;
    private CoordinatorLayout coordinatorLayout;
    GPSTracker gps;
    double latitude, longitude;
    String cuuaddress = "";
    String currlat, currlong;
    File imageZipperFile;
    String imageSize;
    TextView tvSize;

    private DatabaseHelper db;
    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;
    public static String DATA_SAVED_BROADCAST = "";
    private BroadcastReceiver broadcastReceiver;
    ;
    AlertDialog alerDialog4;
    LinearLayout llImage;
    private Location mCurrentLocation;
    LinearLayout llLoader;
    TextView tvToolbar,tvRemark;
    String tranAddr;
    ProgressDialog pd;
    TextView tvName;
    AlertDialog al1;
    File compressedImageFile;
    Button btnMarkDailyLogSubmit;
    boolean flagt=false;
    LatLng p;
    double SLongitude,SLatitude,radius;
    String jsonData;
    JSONArray sourceArray;
    ArrayList<Boolean>flagList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_log_manage);


        initialize();

        setUpMapIfNeeded();
        onClick();
    }

    private void initialize() {
        btnMarkDailyLogSubmit=findViewById(R.id.btnMarkDailyLogSubmit);
        pref = new Pref(getApplicationContext());
        radius=getIntent().getDoubleExtra("radius",0.00);
        jsonData=getIntent().getStringExtra("jsonData");
        try {
            sourceArray=new JSONArray(jsonData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //SERVER_PATH = pref.getIpAddress()+"GHRMSApi/api/";
        //DATA_SAVED_BROADCAST = pref.getIpAddress()+"GHRMSApi/api/post_OfflineDailyLogActivity";
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds


        connectionCheck = new NetworkConnectionCheck(this);

        //  mapView = findViewById(R.id.map);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(360, TimeUnit.SECONDS)
                .connectTimeout(360, TimeUnit.SECONDS)
                .build();

        // Change base URL to your upload server URL.
        uploadService = (AttendanceService) new Retrofit.Builder()
                .baseUrl(SERVER_PATH)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AttendanceService.class);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
       // etRemarks = (EditText) findViewById(R.id.etRemarks);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);


        db = new DatabaseHelper(this);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //loading the names again

            }
        };

        registerReceiver(broadcastReceiver, new IntentFilter(DATA_SAVED_BROADCAST));
        llImage = (LinearLayout) findViewById(R.id.llImage);
        //imgCamera = (ImageView) findViewById(R.id.imgCamera);
        //imgPic = (ImageView) findViewById(R.id.imgEmp);
        llLoader = (LinearLayout) findViewById(R.id.llLoader);
        tvToolbar=(TextView)findViewById(R.id.tvToolBar);
        //tvRemark=(TextView)findViewById(R.id.tvRemark);
        if (pref.getLanguage().equals("hi")){
            tvToolbar.setText("दैनिक लॉग प्रबंधन");
//            tvRemark.setText("टिप्पणियों");
//            btnSubmit.setText("गतिविधि सबमिट करें");
            btnMarkDailyLogSubmit.setText("अपनी उपस्थिति को चिह्नित करें");
        }else {
            tvToolbar.setText("Daily log manage");
//            tvRemark.setText("Remarks");
//            btnSubmit.setText("Submit");
            btnMarkDailyLogSubmit.setText("Mark Your Attendance");
        }
        pd=new ProgressDialog(this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);

        tvName=(TextView)findViewById(R.id.tvName);
        tvName.setText("Hi! "+pref.getEmpName());




        //tvAddress.setText("YOU ARE AT: "+cuuaddress);



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
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //  mapView.getMapAsync(this);

    }

    @SuppressLint("MissingPermission")
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
            if (ContextCompat.checkSelfPermission(GeoFenceDailyLogManageActivity.this,
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
        Log.d("laat",lat);
        currentLongitude = location.getLongitude();
        longt = String.valueOf(currentLongitude);
        latLng = new LatLng(currentLatitude, currentLongitude);
        try {

            for (int i = 0; i < sourceArray.length(); i++) {
                JSONObject obj = sourceArray.getJSONObject(i);
                SLatitude = Double.parseDouble(obj.optString("Latitude"));
                SLongitude = Double.parseDouble(obj.optString("Longitude"));

                p = new LatLng(SLatitude, SLongitude);
                LatLng q=new LatLng(currentLatitude,currentLongitude);
                Double distance=CalculationByDistance(p,q);
                Log.d("distancecal", String.valueOf(distance));
                if (distance<radius || distance==radius){
                    flagt=true;
                    Log.d("desus","1");
                }else {
                    Log.d("desus","0");
                    flagt=false;
                }

                flagList.add(flagt);



            }
            Log.d("flagList",flagList.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }



        address = getCompleteAddressString(currentLatitude, currentLongitude);
        address1 = address.replaceAll("\\s+", "%20");
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(address)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mapmarker));
        if (!cuuaddress.equals("")) {
            tvAddress.setText(address);
        } else {
            tvAddress.setText(pref.getAddress());
        }


        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)      // Sets the center of the map to location user
                .zoom(16)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        mMap.addMarker(options);
        boolean isMock = MockLocationDetector.isLocationFromMockProvider(this, location);
        if (isMock) {
            btnSubmit.setVisibility(View.GONE);
            showMocAlert();
        } else {
            btnSubmit.setVisibility(View.VISIBLE);
        }

    }


    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(Bundle bundle) {

        if (ContextCompat.checkSelfPermission(GeoFenceDailyLogManageActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            @SuppressLint("MissingPermission") Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (location == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            } else {
                handleNewLocation(location);
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(GeoFenceDailyLogManageActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnectionSuspended(int i) {
        if (ContextCompat.checkSelfPermission(GeoFenceDailyLogManageActivity.this,
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
                connectionResult.startResolutionForResult(GeoFenceDailyLogManageActivity.this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
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
        if (ContextCompat.checkSelfPermission(GeoFenceDailyLogManageActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(GeoFenceDailyLogManageActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(GeoFenceDailyLogManageActivity.this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(GeoFenceDailyLogManageActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(GeoFenceDailyLogManageActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @SuppressLint("MissingPermission")
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
                    if (ContextCompat.checkSelfPermission(GeoFenceDailyLogManageActivity.this,
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
                    Toast.makeText(GeoFenceDailyLogManageActivity.this, "permission denied", Toast.LENGTH_LONG).show();
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

    private void locationalerts() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GeoFenceDailyLogManageActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_locationalert, null);
        dialogBuilder.setView(dialogView);
        TextView tvSuccess=(TextView)dialogView.findViewById(R.id.tvSuccess);
        if (pref.getLanguage().equals("hi")){
            tvSuccess.setText("कृपया प्रतीक्षा करें,स्थान प्राप्त कर रहा है");
        }else {
            tvSuccess.setText("Please wait,location is fetching");
        }
        alert1 = dialogBuilder.create();
        alert1.setCancelable(false);
        Window window = alert1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alert1.show();
    }

    private void onClick() {
        btnMarkDailyLogSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address=getCompleteAddressString(currentLatitude,currentLongitude);
                v= getLayoutInflater().inflate(R.layout.fragment_daily_log_bottom_sheet, null);

                dialog = new BottomSheetDialog(GeoFenceDailyLogManageActivity.this);
                dialog.setContentView(v);
                LinearLayout llCamera=(LinearLayout) dialog.findViewById(R.id.llCamera);

                if (pref.getSecurityCode().equals("1163")){
                    llCamera.setVisibility(View.GONE);
                }else {
                    llCamera.setVisibility(View.VISIBLE);
                }
                dialog.findViewById(R.id.imgCamera).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //cameraIntent();
                        dialogCamera();
                    }
                });
                imgPic=dialog.findViewById(R.id.imgEmp);
                tvAddress=dialog.findViewById(R.id.tvAddress);
                tvAddress.setText(address);
                tvRemark=dialog.findViewById(R.id.tvRemark);
                etRemarks=dialog.findViewById(R.id.etRemarks);
                btnSubmit=dialog.findViewById(R.id.btnSubmit);
                tvcapturest=dialog.findViewById(R.id.tvcapturest);
                tvtextAddress=dialog.findViewById(R.id.tvtextAddress);
                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cameraflag == 1) {
                            Date d = new Date();
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                            String currentDateTimeString = sdf.format(d);

                            Date dof = Calendar.getInstance().getTime();


                            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                            String formattedDate = df.format(dof);

                            String date = formattedDate + "  " + currentDateTimeString;
                            if (pref.getSecurityCode().equals("1163")){
                                if ( pref.getMasterId().equals("ALPL007") || pref.getMasterId().equals("ALPL412")){
                                    if (connectionCheck.isNetworkAvailable()) {
                                        //dailyActivity(date);
                                    } else {
                                        //attendanceGivenfunction(date);
                                    }
                                    Log.d("riku","1");
                                }else {
                                    Log.d("riku","2");
                                    if (flagList.contains(true)){
                                        if (connectionCheck.isNetworkAvailable()) {
                                           // dailyActivity(date);
                                        } else {
                                            //attendanceGivenfunction(date);
                                        }
                                    }else {
                                        Toast.makeText(GeoFenceDailyLogManageActivity.this,"The system detects that you are not inside your fencing zone.",Toast.LENGTH_LONG).show();

                                    }
                                }
                            }else {
                                Log.d("riku","3");
                                if (flagList.contains(true)){
                                    if (connectionCheck.isNetworkAvailable()) {
                                        //dailyActivity(date);
                                    } else {
                                        //attendanceGivenfunction(date);
                                    }
                                }else {
                                    Toast.makeText(GeoFenceDailyLogManageActivity.this,"The system detects that you are not inside your fencing zone.",Toast.LENGTH_LONG).show();

                                }
                            }




                        } else {
                            if (pref.getSecurityCode().equals("1163")){
                                Date d = new Date();
                                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                                String currentDateTimeString = sdf.format(d);

                                Date dof = Calendar.getInstance().getTime();


                                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                                String formattedDate = df.format(dof);

                                String date = formattedDate + "  " + currentDateTimeString;
                                if ( pref.getMasterId().equals("ALPL007") || pref.getMasterId().equals("ALPL412")) {
                                    //attendanceGivenfunction(date);
                                }else {
                                    if (flagList.contains(true)){

                                        //attendanceGivenfunction(date);

                                    }else {
                                        Toast.makeText(GeoFenceDailyLogManageActivity.this,"The system detects that you are not inside your fencing zone.",Toast.LENGTH_LONG).show();

                                    }
                                }
                            }else {
                                showAlert();
                            }

                        }
                    }
                });
                dialog.show();
                if (pref.getLanguage().equals("hi")){
                   // tvToolbar.setText("दैनिक लॉग प्रबंधन");
            tvRemark.setText("टिप्पणियों");
            btnSubmit.setText("गतिविधि सबमिट करें");
            tvcapturest.setText("छवि कैप्चर करें");
            tvtextAddress.setText("पता");
                }else {
                  //  tvToolbar.setText("Daily log manage");
            tvRemark.setText("Remarks");
            btnSubmit.setText("Submit");
             tvcapturest.setText("Capture Image");
                    tvtextAddress.setText("Address");
                }


            }
        });


        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GeoFenceDailyLogManageActivity.this, UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cameraflag == 1) {
                    Date d = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    String currentDateTimeString = sdf.format(d);

                    Date dof = Calendar.getInstance().getTime();


                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                    String formattedDate = df.format(dof);

                    String date = formattedDate + "  " + currentDateTimeString;

                    if (connectionCheck.isNetworkAvailable()) {
                        //dailyActivity(date);
                    } else {
                        //attendanceGivenfunction(date);
                    }


                } else {
                    showAlert();

                }
            }
        });


//        imgCamera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cameraIntent();
//            }
//        });


    }



    private void saveNameToLocalStorage(String address, String date, String lat, String longt, String remarks, int status) {
       // db.addName(address, date, lat, longt, remarks, status);
        successAlert();


    }

    private void successAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GeoFenceDailyLogManageActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);
        if (pref.getLanguage().equals("hi")){
            tvInvalidDate.setText("सफलतापूर्वक चिह्नित किया गया ");
        }else {
            tvInvalidDate.setText("Successfully marked ");
        }

        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog4.dismiss();
                if (connectionCheck.isNetworkAvailable()) {
                    Intent intent = new Intent(GeoFenceDailyLogManageActivity.this, NumberTourActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Intent intent = new Intent(GeoFenceDailyLogManageActivity.this, OfflineDailyDashBoardActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        alerDialog4 = dialogBuilder.create();
        alerDialog4.setCancelable(true);
        Window window = alerDialog4.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog4.show();
    }
    private void uploadAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GeoFenceDailyLogManageActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_upload, null);
        dialogBuilder.setView(dialogView);


        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                al1.dismiss();
                LongImageCameraActivity.launch(GeoFenceDailyLogManageActivity.this);

            }
        });

        al1 = dialogBuilder.create();
        al1.setCancelable(true);
        Window window = al1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        al1.show();
    }


    private void cameraIntent() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Profile Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_REQUEST:

                if (resultCode == Activity.RESULT_OK) {
                    try {
                        try {
                            String imageurl = /*"file://" +*/ getRealPathFromURI(imageUri);
                            file = new File(imageurl);
                            imageZipperFile = new ImageZipper(GeoFenceDailyLogManageActivity.this)
                                    .setQuality(100)
                                    .setMaxWidth(300)
                                    .setMaxHeight(300)
                                    .compressToFile(file);
                            //Log.d("imageSixw", String.valueOf(getReadableFileSize(compressedImageFile.length())));
                            BitmapFactory.Options o = new BitmapFactory.Options();
                            o.inSampleSize = 2;
                            Bitmap bm = cropToSquare(BitmapFactory.decodeFile(imageurl, o));
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.PNG, 10, baos); //bm is the bitmap object
                           /* byte[] b = baos.toByteArray();
                            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);*/

                            imgPic.setImageBitmap(bm);
                            addflag=1;
                            cameraflag=1;



                            // _pref.saveImage(encodedImage);
                            //saveImage(encodedImage);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }

                }
                break;
            case LongImageCameraActivity.LONG_IMAGE_RESULT_CODE:


                if (resultCode == RESULT_OK && requestCode == LongImageCameraActivity.LONG_IMAGE_RESULT_CODE) {
                    String imageFileName = data.getStringExtra(LongImageCameraActivity.IMAGE_PATH_KEY);
                    Log.d("imageFileName", imageFileName);
                    Bitmap d = BitmapFactory.decodeFile(imageFileName);
                    int newHeight = (int) (d.getHeight() * (512.0 / d.getWidth()));
                    Bitmap putImage = Bitmap.createScaledBitmap(d, 512, newHeight, true);
                    imgPic.setImageBitmap(putImage);
                    file = (File) data.getExtras().get("picture");
                    Log.d("fjjgk", file.toString());
                    cameraflag=1;

                    try {
                        imageZipperFile = new ImageZipper(GeoFenceDailyLogManageActivity.this)
                                .setQuality(100)
                                .setMaxWidth(300)
                                .setMaxHeight(300)
                                .compressToFile(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }





                }
                break;
        }




    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    private String getRealPathFromURIPath(Uri contentURI) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentURI, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public static Bitmap cropToSquare(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = (height > width) ? width : height;
        int newHeight = (height > width) ? height - (height - width) : height;
        int cropW = (width - height) / 2;
        cropW = (cropW < 0) ? 0 : cropW;
        int cropH = (height - width) / 2;
        cropH = (cropH < 0) ? 0 : cropH;
        Bitmap cropImg = Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight);
        return cropImg;
    }



    private void showAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        if (pref.getLanguage().equals("hi")){
            alertDialogBuilder.setMessage("कृपया अपनी सेल्फी तस्वीर संलग्न करें");
        }else {
            alertDialogBuilder.setMessage("Please attach your selfie picture");
        }
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                    }
                });
        alertDialogBuilder.show();


    }

    private void showMocAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        if (pref.getLanguage().equals("hi")){
            alertDialogBuilder.setMessage("आप मॉक लोकेशन का उपयोग कर रहे हैं");
        }else {
            alertDialogBuilder.setMessage("You are using Mock Location");
        }
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();


                    }
                });
        alertDialogBuilder.show();


    }
    public void dialogCamera(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GeoFenceDailyLogManageActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_camera, null);
        dialogBuilder.setView(dialogView);
        //TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);
        Button btnCamera1=(Button) dialogView.findViewById(R.id.btnCamera1);
        Button btnCamera2=(Button) dialogView.findViewById(R.id.btnCamera2);

        btnCamera1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog2.dismiss();
//                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent,CAMERA_REQUEST);
                cameraIntent();
            }
        });
        btnCamera2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog2.dismiss();
                LongImageCameraActivity.launch(GeoFenceDailyLogManageActivity.this);
            }
        });

        alertDialog2 = dialogBuilder.create();
        alertDialog2.setCancelable(true);
        Window window = alertDialog2.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog2.show();
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

        // Toast.makeText(getApplicationContext(),distance+"KM",Toast.LENGTH_LONG).show();
        return ddis;
    }






}
