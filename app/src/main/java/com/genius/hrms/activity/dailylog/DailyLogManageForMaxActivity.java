package com.genius.hrms.activity.dailylog;

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
import android.provider.MediaStore;
import android.util.Base64;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
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
import com.genius.hrms.activity.dailyactivity.DailyActivityManageActivity;
import com.genius.hrms.activity.helper.DatabaseHelper;
import com.genius.hrms.activity.model.SpinnerModel;
import com.genius.hrms.activity.utility.ApiClient;
import com.genius.hrms.activity.utility.AttendanceService;
import com.genius.hrms.activity.utility.GPSTracker;
import com.genius.hrms.activity.utility.NetworkConnectionCheck;
import com.genius.hrms.activity.utility.Pref;
import com.genius.hrms.activity.utility.UploadObject;
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
import com.inforoeste.mocklocationdetector.MockLocationDetector;
import com.wajahatkarim3.longimagecamera.LongImageCameraActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
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

public class DailyLogManageForMaxActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public static final String TAG = AttendanceManageActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    //  private MapView mapView;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

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
    private static String SERVER_PATH = "https://www.cloud.geniusconsultant.com/GHRMSApi/api/";
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
    public static String DATA_SAVED_BROADCAST = "https://www.cloud.geniusconsultant.com/GHRMSApi/api/post_OfflineDailyLogActivity";
    private BroadcastReceiver broadcastReceiver;
    ;
    AlertDialog alerDialog4, alertDialog1;
    LinearLayout llImage;
    private Location mCurrentLocation;
    LinearLayout llLoader;
    TextView tvToolbar, tvRemark;
    String tranAddr;
    ProgressDialog pd;
    TextView tvName;
    AlertDialog al1;
    File compressedImageFile;
    LinearLayout llStore;
    Spinner spStore;
    ArrayList<String> storeNameList = new ArrayList<>();
    ArrayList<SpinnerModel> modelStoreNameList = new ArrayList<>();
    String costCenterId = "";
    EditText etStore;
    String aId;
    boolean responseStatus;
    String CostCenterName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_log_manage_max);

        initialize();

        setUpMapIfNeeded();
        onClick();
    }

    private void initialize() {
        pref = new Pref(getApplicationContext());

        SERVER_PATH = pref.getIpAddress() + "GHRMSApi/api/";
        DATA_SAVED_BROADCAST = pref.getIpAddress() + "GHRMSApi/api/post_OfflineDailyLogActivity";
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
        etRemarks = (EditText) findViewById(R.id.etRemarks);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        gps = new GPSTracker(DailyLogManageForMaxActivity.this);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            currlat = String.valueOf(latitude);
            Log.d("saikatdas", String.valueOf(latitude));
            longitude = gps.getLongitude();
            currlong = String.valueOf(longitude);
        } else {
// can't get location
// GPS or Network is not enabled
// Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

        cuuaddress = getCompleteAddressString(latitude, longitude);
        Log.d("cuuaddress", cuuaddress);
        db = new DatabaseHelper(this);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //loading the names again

            }
        };

        registerReceiver(broadcastReceiver, new IntentFilter(DATA_SAVED_BROADCAST));
        llImage = (LinearLayout) findViewById(R.id.llImage);
        imgCamera = (ImageView) findViewById(R.id.imgCamera);
        imgPic = (ImageView) findViewById(R.id.imgEmp);
        llLoader = (LinearLayout) findViewById(R.id.llLoader);
        tvToolbar = (TextView) findViewById(R.id.tvToolBar);
        tvRemark = (TextView) findViewById(R.id.tvRemark);
        if (pref.getLanguage().equals("hi")) {
            tvToolbar.setText("दैनिक लॉग प्रबंधन");
            tvRemark.setText("टिप्पणियों");
            btnSubmit.setText("गतिविधि सबमिट करें");
        } else {
            tvToolbar.setText("Daily log manage");
            tvRemark.setText("Remarks");
            btnSubmit.setText("Submit");
        }
        pd = new ProgressDialog(this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);

        tvName = (TextView) findViewById(R.id.tvName);
        tvName.setText("Hi! " + pref.getEmpName());

        tvAddress.setText("YOU ARE AT: " + cuuaddress);
        llStore = (LinearLayout) findViewById(R.id.llStore);
        spStore = (Spinner) findViewById(R.id.spStore);
        etStore = (EditText) findViewById(R.id.etStore);
        attendanceChecking();


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
            if (ContextCompat.checkSelfPermission(DailyLogManageForMaxActivity.this,
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

        if (ContextCompat.checkSelfPermission(DailyLogManageForMaxActivity.this,
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

        mGoogleApiClient = new GoogleApiClient.Builder(DailyLogManageForMaxActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnectionSuspended(int i) {
        if (ContextCompat.checkSelfPermission(DailyLogManageForMaxActivity.this,
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
                connectionResult.startResolutionForResult(DailyLogManageForMaxActivity.this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
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
        if (ContextCompat.checkSelfPermission(DailyLogManageForMaxActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(DailyLogManageForMaxActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(DailyLogManageForMaxActivity.this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(DailyLogManageForMaxActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(DailyLogManageForMaxActivity.this,
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
                    if (ContextCompat.checkSelfPermission(DailyLogManageForMaxActivity.this,
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
                    Toast.makeText(DailyLogManageForMaxActivity.this, "permission denied", Toast.LENGTH_LONG).show();
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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DailyLogManageForMaxActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_locationalert, null);
        dialogBuilder.setView(dialogView);
        TextView tvSuccess = (TextView) dialogView.findViewById(R.id.tvSuccess);
        if (pref.getLanguage().equals("hi")) {
            tvSuccess.setText("कृपया प्रतीक्षा करें,स्थान प्राप्त कर रहा है");
        } else {
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


        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DailyLogManageForMaxActivity.this, UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        spStore.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    costCenterId = modelStoreNameList.get(i).getItemId();
                    if (costCenterId.equals("1")) {
                        llStore.setVisibility(View.VISIBLE);
                    } else {
                        llStore.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
                if (!costCenterId.equals("")) {
                    if (cameraflag == 1) {
                        storeNameChecking();

                    } else {
                        showAlert();
                    }

                } else {
                    Toast.makeText(DailyLogManageForMaxActivity.this, "Please Select Store Name", Toast.LENGTH_LONG).show();
                }

            }
        });




        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraIntent();
            }
        });


    }

    private void storeNameChecking() {
        if (costCenterId.equals("1")) {
            if (etStore.getText().toString().length() > 1) {
                Date d = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                String currentDateTimeString = sdf.format(d);

                Date dof = Calendar.getInstance().getTime();


                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(dof);

                String date = formattedDate + "  " + currentDateTimeString;

                if (connectionCheck.isNetworkAvailable()) {
                    dailyActivity(date);
                } else {
                    attendanceGivenfunction(date);
                }

            } else {
                Toast.makeText(DailyLogManageForMaxActivity.this, "Please Enter Others Merchant Name", Toast.LENGTH_LONG).show();
            }

        } else {
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String currentDateTimeString = sdf.format(d);

            Date dof = Calendar.getInstance().getTime();


            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String formattedDate = df.format(dof);

            String date = formattedDate + "  " + currentDateTimeString;

            if (connectionCheck.isNetworkAvailable()) {
                dailyActivity(date);
            } else {
                attendanceGivenfunction(date);
            }
        }
    }

    private void attendanceGivenfunction(final String attendate) {
        Log.d("hitt", "1");
        final String serAddress;
        progressDialog.show();
        if (!cuuaddress.equals("")) {
            serAddress = cuuaddress;
        } else {
            serAddress = pref.getAddress();
        }
        btnSubmit.setVisibility(View.GONE);
        llLoader.setVisibility(View.VISIBLE);

        Call<UploadObject> fileUpload = ApiClient.getService().offlineDailyLof(pref.getEmpId(), "0", etRemarks.getText().toString(), currlong, currlat, serAddress, "0", "0", pref.getSecurityCode(), "0", attendate);
        fileUpload.enqueue(new Callback<UploadObject>() {
            @Override
            public void onResponse(Call<UploadObject> call, Response<UploadObject> response) {
                progressDialog.dismiss();
                UploadObject extraWorkingDayModel = response.body();
                if (extraWorkingDayModel.isResponseStatus()) {
                    // Toast.makeText(getApplicationContext(), extraWorkingDayModel.getResponseText(), Toast.LENGTH_SHORT).show();
                    saveNameToLocalStorage(serAddress, attendate, lat, longt, etRemarks.getText().toString(), NAME_SYNCED_WITH_SERVER);
                    successAlert();


                } else {


                    saveNameToLocalStorage(serAddress, attendate, lat, longt, etRemarks.getText().toString(), NAME_NOT_SYNCED_WITH_SERVER);
                    //      Toast.makeText(getApplicationContext(), extraWorkingDayModel.getResponseText(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UploadObject> call, Throwable t) {
                progressDialog.dismiss();

                Log.e("error", "Error " + t.getMessage());
                saveNameToLocalStorage(serAddress, attendate, lat, longt, etRemarks.getText().toString(), NAME_NOT_SYNCED_WITH_SERVER);
                //  Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_LONG).show();


                //   Toast.makeText(AttendanceManageActivity.this,"attendance saved without image",Toast.LENGTH_LONG).show();
            }

        });
    }


    private void saveNameToLocalStorage(String address, String date, String lat, String longt, String remarks, int status) {
        db.addName(address, date, lat, longt, remarks, status);
        successAlert();


    }

    private void successAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DailyLogManageForMaxActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);
        if (pref.getLanguage().equals("hi")) {
            tvInvalidDate.setText("सफलतापूर्वक चिह्नित किया गया ");
        } else {
            tvInvalidDate.setText("Successfully marked ");
        }

        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog4.dismiss();
                if (connectionCheck.isNetworkAvailable()) {
                    if (responseStatus){
                        uploadAlert();
                    }else {
                        Intent intent = new Intent(DailyLogManageForMaxActivity.this, OfflineDailyDashBoardActivity.class);
                        startActivity(intent);
                        finish();
                    }



                } else {
                    Intent intent = new Intent(DailyLogManageForMaxActivity.this, OfflineDailyDashBoardActivity.class);
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

    private void successAlertForFileSave() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DailyLogManageForMaxActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);
        if (pref.getLanguage().equals("hi")) {
            tvInvalidDate.setText("सफलतापूर्वक चिह्नित किया गया ");
        } else {
            tvInvalidDate.setText("File has been saved successfully ");
        }

        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog1.dismiss();
                if (connectionCheck.isNetworkAvailable()) {

                    Intent intent = new Intent(DailyLogManageForMaxActivity.this, OfflineDailyDashBoardActivity.class);
                    startActivity(intent);
                    finish();

                } else {

                }
            }
        });

        alertDialog1 = dialogBuilder.create();
        alertDialog1.setCancelable(true);
        Window window = alertDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog1.show();
    }

    private void uploadAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DailyLogManageForMaxActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_upload, null);
        dialogBuilder.setView(dialogView);


        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                al1.dismiss();
                LongImageCameraActivity.launch(DailyLogManageForMaxActivity.this);

            }
        });

        Button btnCancel=(Button)dialogView.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DailyLogManageForMaxActivity.this,OfflineDailyDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
        ImageView imgCancel = (ImageView) dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                al1.dismiss();
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
                            imageZipperFile = new ImageZipper(DailyLogManageForMaxActivity.this)
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
                            byte[] b = baos.toByteArray();
                            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                            Log.d("images", encodedImage);
                            imgPic.setImageBitmap(bm);
                            addflag = 1;
                            cameraflag = 1;


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

                    file = (File) data.getExtras().get("picture");
                    Log.d("fjjgk", file.toString());

                    try {
                        compressedImageFile = new ImageZipper(DailyLogManageForMaxActivity.this)
                                .setQuality(100)
                                .setMaxWidth(300)
                                .setMaxHeight(300)
                                .compressToFile(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    fileSave();


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

    private void dailyActivity(final String attendate) {
        final ProgressDialog pg = new ProgressDialog(DailyLogManageForMaxActivity.this);
        pg.setMessage("Loading..");
        pg.setCancelable(false);
        pg.show();
        AndroidNetworking.upload(pref.getIpAddress() + "GHRMSApi/api/Attendance/AttendanceDailyActivity")
                .addMultipartParameter("EmployeeID", pref.getEmpId())
                .addMultipartParameter("Remarks", etRemarks.getText().toString())
                .addMultipartParameter("Longitude", longt)
                .addMultipartParameter("Latitude", lat)
                .addMultipartParameter("Address", cuuaddress)
                .addMultipartParameter("CostCentreID", costCenterId)
                .addMultipartParameter("CostCentreName", etStore.getText().toString())
                .addMultipartParameter("Year", "0")
                .addMultipartParameter("Month", "0")
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())
                .addMultipartFile("Image", imageZipperFile)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        pg.show();

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        pg.dismiss();
                        JSONObject job = response;
                        boolean responseStatus = job.optBoolean("responseStatus");
                        String responseData = job.optString("responseData");
                        aId = responseData;
                        if (responseStatus) {
                            successAlert();
                        } else {

                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        pg.dismiss();
                        Toast.makeText(DailyLogManageForMaxActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                        //saveNameToLocalStorage(cuuaddress, attendate, lat, longt, etRemarks.getText().toString(), NAME_NOT_SYNCED_WITH_SERVER);

                    }
                });

    }

    //                saveNameToLocalStorage(serAddress, attendate, lat, longt, etRemarks.getText().toString(), NAME_NOT_SYNCED_WITH_SERVER);
    private void showAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        if (pref.getLanguage().equals("hi")) {
            alertDialogBuilder.setMessage("कृपया अपनी सेल्फी तस्वीर संलग्न करें");
        } else {
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
        if (pref.getLanguage().equals("hi")) {
            alertDialogBuilder.setMessage("आप मॉक लोकेशन का उपयोग कर रहे हैं");
        } else {
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


    private void setStoreName() {

        String surl = pref.getIpAddress() + "GHRMSApi/api/Attendance/CostCentre?CompanyID=" + pref.getEmpClintId() + "&EmployeeID=" + pref.getEmpId() + "&SecurityCode=" + pref.getSecurityCode();
        Log.d("projectcodeurl", surl);
        final ProgressDialog pd = new ProgressDialog(DailyLogManageForMaxActivity.this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseIFBCategory", response);
                        pd.dismiss();
                        modelStoreNameList.clear();
                        storeNameList.clear();
                        storeNameList.add("Please select");
                        modelStoreNameList.add(new SpinnerModel("0", "0"));

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            // String responseText = job1.optString("responseText");
                            boolean ResponseStatus = job1.optBoolean("responseStatus");
                            if (ResponseStatus) {
                                //Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String value = obj.optString("CostCenterName");
                                    String id = String.valueOf(obj.optInt("CostCenterID"));
                                    storeNameList.add(value);
                                    SpinnerModel spModel = new SpinnerModel(value, id);
                                    modelStoreNameList.add(spModel);

                                }

                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (DailyLogManageForMaxActivity.this, android.R.layout.simple_spinner_item,
                                                storeNameList); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spStore.setAdapter(spinnerArrayAdapter);

                                int index = storeNameList.indexOf(CostCenterName);
                                Log.d("indexr", String.valueOf(index));
                                spStore.setSelection(index);
                                if (responseStatus) {
                                    spStore.setEnabled(false);
                                } else {
                                    spStore.setEnabled(true);
                                }


                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DailyLogManageForMaxActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();

                Toast.makeText(DailyLogManageForMaxActivity.this, "No Internet connection", Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(DailyLogManageForMaxActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    private void attendanceChecking() {

        String surl = pref.getIpAddress() + "GHRMSApi/api/Attendance/FirstAttendanceView?CompanyID=" + pref.getEmpClintId() + "&EmployeeID=" + pref.getEmpId() + "&CostCentreID=1&SecurityCode=" + pref.getSecurityCode();
        Log.d("projectcodeurl", surl);
        final ProgressDialog pd = new ProgressDialog(DailyLogManageForMaxActivity.this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseIFBCategory", response);
                        pd.dismiss();
                        setStoreName();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            // String responseText = job1.optString("responseText");
                            responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                //Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");

                                JSONObject obj = responseData.getJSONObject(0);
                                CostCenterName = obj.optString("CostCenterName");
                                btnSubmit.setText("Mark Out");


                            } else {
                                btnSubmit.setText("Mark In");

                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DailyLogManageForMaxActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();

                Toast.makeText(DailyLogManageForMaxActivity.this, "No Internet connection", Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(DailyLogManageForMaxActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    private void fileSave() {
        final ProgressDialog pg = new ProgressDialog(DailyLogManageForMaxActivity.this);
        pg.setMessage("Loading..");
        pg.setCancelable(false);
        pg.show();
        AndroidNetworking.upload(pref.getIpAddress() + "GHRMSApi/api/Attendance/AttendanceDocUpload")
                .addMultipartParameter("EmployeeID", pref.getEmpId())
                .addMultipartParameter("AttAID", aId)
                .addMultipartParameter("Year", "0")
                .addMultipartParameter("Month", "0")
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())
                .addMultipartFile("Image", compressedImageFile)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        pg.show();

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        pg.dismiss();
                        JSONObject job = response;
                        boolean responseStatus = job.optBoolean("responseStatus");
                        if (responseStatus) {
                            successAlertForFileSave();
                        } else {

                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e("geterror", String.valueOf(error));
                        pg.dismiss();
                        Toast.makeText(DailyLogManageForMaxActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                        //saveNameToLocalStorage(cuuaddress, attendate, lat, longt, etRemarks.getText().toString(), NAME_NOT_SYNCED_WITH_SERVER);

                    }
                });

    }


}
