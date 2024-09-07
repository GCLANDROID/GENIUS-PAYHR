package com.genius.payhrms.activity.dailylog.archisman;

import static com.genius.payhrms.activity.dailylog.DailyLogCalenderDashboardActivity.isAppMinimizeDailyLog;
import static com.genius.payhrms.activity.utility.Util.SECRET_KEY;
import static com.genius.payhrms.activity.utility.Util.encrypt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import com.genius.payhrms.R;
import com.genius.payhrms.activity.activity.LoginActivity;
import com.genius.payhrms.activity.activity.UserDashBoardActivity;
import com.genius.payhrms.activity.dailylog.DailyLogMarkActivity;
import com.genius.payhrms.activity.helper.DatabaseHelperForDailyLog;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.AttendanceService;
import com.genius.payhrms.activity.utility.GPSTracker;
import com.genius.payhrms.activity.utility.Pref;
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
import com.wajahatkarim3.longimagecamera.LongBackImageCameraActivity;
import com.wajahatkarim3.longimagecamera.LongImageCameraActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DailyLogAttendaneArchisman extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = "DailyLogAttendaneArchis";
    TextView tvAddress, tvTime;
    LinearLayout llRefresh;
    ImageView imgCamera, imgImage;
    GPSTracker gps;
    ;
    double latitude = 0.0, longitude = 0.0;
    String address = "--";
    Pref pref;
    String currentDateTimeString;
    private String encodedImage;
    private Uri imageUri, uri;
    private static final int CAMERA_REQUEST = 1;
    int flag;
    File compressedImageFile, file;
    Button btnSubmit,btnSubmitOut;
    String currentlat, currentlong;

    ;

    TextView tvName;
    EditText etRemarks;
    AlertDialog alerDialog1;
    ImageView imgBack, imgHome;
    TextView tvClick, tvClickHere;

    private static final int REQUEST_GALLERY_CODE = 200;
    Bitmap bitmap;
    LinearLayout llImage, llNote;
    TextView tvCapture;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    //  private MapView mapView;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    LatLng latLng;
    AlertDialog al2;
    TextView tvCustom;
    ImageView imgUser;
    private static String SERVER_PATH = "";
    private AttendanceService uploadService;
    LinearLayout lnMain,lnLoader;

    private DatabaseHelperForDailyLog db;
    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;
    //public static String DATA_SAVED_BROADCAST = "https://cloud.geniusconsultant.com/GHRMSApi/api/post_OfflineDailyLogActivity";
    public static String DATA_SAVED_BROADCAST = Api.sPostOfflineDailyLogActivity;
    private BroadcastReceiver broadcastReceiver;

    private Spinner spinner;
    double geoFencingValue=0.0;
    String workMode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_log_attendane_archisman);
        Log.e(TAG, "onCreate: DailyLogAttendaneArchis");
        initview();
        spinnerChoose();
        setUpMapIfNeeded();
        onClick();
    }

    @SuppressLint("RestrictedApi")
    private void initview() {
        db = new DatabaseHelperForDailyLog(this);
        pref = new Pref(DailyLogAttendaneArchisman.this);
        lnMain=(LinearLayout) findViewById(R.id.lnMain);
        lnLoader=(LinearLayout) findViewById(R.id.lnLoader);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                lnLoader.setVisibility(View.GONE);
                lnMain.setVisibility(View.VISIBLE);
            }
        }, 3000);
        imgUser = (ImageView) findViewById(R.id.imgUser);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvTime = (TextView) findViewById(R.id.tvTime);
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000);

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(360, TimeUnit.SECONDS)
                .connectTimeout(360, TimeUnit.SECONDS)
                .build();

        // Change base URL to your upload server URL.
        /*uploadService = (AttendanceService) new Retrofit.Builder()
                .baseUrl(SERVER_PATH)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AttendanceService.class);*/


        llRefresh = (LinearLayout) findViewById(R.id.llRefresh);

        imgCamera = (ImageView) findViewById(R.id.imgCamera);
        imgImage = (ImageView) findViewById(R.id.imgImage);

        gps = new GPSTracker(DailyLogAttendaneArchisman.this);

        spinner = findViewById(R.id.autoCompleteTextView);

        // Sample data for autocomplete suggestions
        spinner = findViewById(R.id.autoCompleteTextView);

        // Sample data for autocomplete suggestions
        String[] operation = {"Office","Field"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, operation);

        spinner.setAdapter(adapter);

        // tvAddress.setText("YOU ARE AT: " + address);
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        currentDateTimeString = sdf.format(d);
        tvTime.setText("Current time is : " + currentDateTimeString);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmitOut = (Button) findViewById(R.id.btnSubmitOut);
        btnSubmitOut.setVisibility(View.GONE);
        btnSubmit.setText("Submit");
        currentlat = String.valueOf(latitude);
        currentlong = String.valueOf(longitude);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        // Change base URL to your upload server URL.

        tvName = (TextView) findViewById(R.id.tvName);
        tvName.setText("Hi! " + pref.getEmpName());
        etRemarks = (EditText) findViewById(R.id.etRemarks);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        tvClick = (TextView) findViewById(R.id.tvClick);
        tvClickHere = (TextView) findViewById(R.id.tvClickHere);
        tvCapture = (TextView) findViewById(R.id.tvCapture);
        llImage = (LinearLayout) findViewById(R.id.llImage);
        tvCustom = (TextView) findViewById(R.id.tvCustom);
    }


    private void spinnerChoose(){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = (String) parent.getItemAtPosition(position);
                workMode=selectedOption;
                // Perform actions based on the selected option
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case where nothing is selected

            }
        });


    }
    public static float distFrom(float lat1, float lng1, float lat2, float lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        float dist = (float) (earthRadius * c);

        return dist;
    }




    private void onClick() {
        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraIntent();
            }
        });

        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pref.getEmpId().equalsIgnoreCase("2070002087")) {
                    galleryIntent();
                }
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == 1) {
                    if (!tvAddress.getText().toString().equals("YOU ARE AT: null") || tvAddress.getText().toString().equals("YOU ARE AT: ")) {
                        if (etRemarks.getText().toString().length() > 0) {
                            if (workMode.equalsIgnoreCase("Office")) {
                                //dailyActivityWithGeoFence();
                                dailyActivityWithGeoFence2();
                            } else {
                                //dailyActivity();
                                dailyActivity2();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Please enter remarks", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Sorry! Your address not found.Please click on Refresh button", Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (pref.getSecurityCode().equals("1156")) {
                        //dailyLogWithoutImage();
                        JSONObject obj = new JSONObject();
                        try {
                            obj.put("AEMEmployeeID", pref.getEmpId());
                            obj.put("Remarks", etRemarks.getText().toString());
                            obj.put("Longitude", currentlong);
                            obj.put("Latitude", currentlat);
                            obj.put("Address", address);
                            obj.put("ApprovalStatus", "1");
                            obj.put("Year", "0");
                            obj.put("Month", "0");
                            obj.put("SecurityCode", pref.getSecurityCode());
                            dailyLogWithoutImage2(obj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "please attach Image", Toast.LENGTH_LONG).show();
                    }
                }
            }

        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tvCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LongImageCameraActivity.launch(DailyLogAttendaneArchisman.this);
            }
        });


        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DailyLogAttendaneArchisman.this, UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tvClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);

            }
        });


    }

    private void cameraIntent() {
        flag = 1;
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_REQUEST:

                if (resultCode == Activity.RESULT_OK) {
                    try {
                        try {

                            //messageAlert();
                            String imageurl = /*"file://" +*/ getRealPathFromURIPath(imageUri);
                            file = new File(imageurl);
                            compressedImageFile = new ImageZipper(DailyLogAttendaneArchisman.this)
                                    .setQuality(80)
                                    .setMaxWidth(250)
                                    .setMaxHeight(250)
                                    .compressToFile(file);
                            // Log.d("imageSixw", String.valueOf(getReadableFileSize(compressedImageFile.length())));


                            BitmapFactory.Options o = new BitmapFactory.Options();
                            o.inSampleSize = 6;
                            Bitmap bo = cropToSquare(BitmapFactory.decodeFile(imageurl, o));
                            Bitmap bm = new ImageZipper(getApplicationContext()).compressToBitmap(file);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bo.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
                            byte[] b = baos.toByteArray();
                            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                            Log.d("encodedimage", encodedImage);
                            imgImage.setImageBitmap(bm);
                            flag = 1;
                            // al2.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }

                }
                break;
            case REQUEST_GALLERY_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    InputStream imageStream = null;
                    try {
                        try {
                            uri = data.getData();
                            String filePath = getRealPathFromURIPath(uri, DailyLogAttendaneArchisman.this);
                            file = new File(filePath);
                            compressedImageFile = new ImageZipper(DailyLogAttendaneArchisman.this)
                                    .setQuality(80)
                                    .setMaxWidth(250)
                                    .setMaxHeight(250)
                                    .compressToFile(file);
                            imageStream = getContentResolver().openInputStream(uri);
                            Bitmap bm = cropToSquare(BitmapFactory.decodeStream(imageStream));
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.JPEG, 10, baos); //bm is the bitmap object
                            byte[] b = baos.toByteArray();
                            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                            imgImage.setImageBitmap(bm);
                            flag = 1;
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
                    file = (File) data.getExtras().get("picture");
                    try {
                        compressedImageFile = new ImageZipper(DailyLogAttendaneArchisman.this)
                                .setQuality(80)
                                .setMaxWidth(250)
                                .setMaxHeight(250)
                                .compressToFile(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String imageFileName = data.getStringExtra(LongBackImageCameraActivity.IMAGE_PATH_KEY);
                    Log.d("imageFileName", imageFileName);
                    Bitmap d = BitmapFactory.decodeFile(imageFileName);
                    int newHeight = (int) (d.getHeight() * (512.0 / d.getWidth()));
                    Bitmap putImage = Bitmap.createScaledBitmap(d, 512, newHeight, true);
                    imgImage.setImageBitmap(putImage);
                    flag = 1;
                    // al2.dismiss();

                }
                break;


        }
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


    private void successAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DailyLogAttendaneArchisman.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.succes_alert, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llOk = (LinearLayout) dialogView.findViewById(R.id.llOk);
        llOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerDialog1.dismiss();
                onBackPressed();

            }
        });
        TextView tvSuccess = (TextView) dialogView.findViewById(R.id.tvSuccess);
        tvSuccess.setText("Your Attendance saved successfully");


        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(false);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }



    private void galleryIntent() {
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
        openGalleryIntent.setType("image/*");
        startActivityForResult(openGalleryIntent, REQUEST_GALLERY_CODE);
    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }


    private void getAPIKey() {
        String surl = "https://cloud.geniusconsultant.com/GeniusESS/API/Utility/GetLocationKey";
        Log.d("residancelist", surl);
        final ProgressDialog progressDialog=new ProgressDialog(DailyLogAttendaneArchisman.this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.d("clint", "1");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        progressDialog.dismiss();


                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            getaddressFromAPI(responseText);





                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Toast.makeText(SalaryActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                address = getCompleteAddressString(latitude, longitude);
                tvAddress.setText(address);

                // Toast.makeText(SalaryActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());

            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(DailyLogAttendaneArchisman.this);
        requestQueue.add(stringRequest);


    }

    private void getaddressFromAPI(String apikey) {
        String testUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=28.5530871,77.201581&key=" + apikey;
        String surl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitude + "&key=" + apikey;
        Log.d("residancelist", surl);
        final ProgressDialog pd = new ProgressDialog(DailyLogAttendaneArchisman.this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        Log.d("clint", "1");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        pd.dismiss();


                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String status = job1.optString("status");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (status.equalsIgnoreCase("OK")) {
                                JSONArray results = job1.optJSONArray("results");
                                //for (int i = 0; i < results.length(); i++) {
                                JSONObject object = results.optJSONObject(0);
                                String formatted_address = object.optString("formatted_address");
                                JSONArray address_components=object.optJSONArray("address_components");
                                for (int i=0;i<address_components.length();i++){
                                    JSONObject compOBJ=address_components.optJSONObject(i);
                                    JSONArray types = compOBJ.optJSONArray("types");
                                    for (int j = 0; j < types.length(); j++) {
                                        if (types.get(j).equals("sublocality") || types.get(j).equals("route")|| types.get(j).equals("establishment")) {
                                            address = formatted_address.replaceAll("Unnamed Road,","");
                                            tvAddress.setText("You are at:- " + address);
                                        }else {
                                            address = getCompleteAddressString(latitude, longitude);
                                            tvAddress.setText(address);
                                        }
                                    }
                                }


                                //}


                            } else {

                                address = getCompleteAddressString(latitude, longitude);
                                tvAddress.setText(address);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Toast.makeText(SalaryActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                address = getCompleteAddressString(latitude, longitude);
                tvAddress.setText(address);
                // Toast.makeText(SalaryActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());

            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(DailyLogAttendaneArchisman.this);
        requestQueue.add(stringRequest);


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



    @Override
    public void onPause() {
        super.onPause();
        // mapView.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }

        if (isAppMinimizeDailyLog){
            Intent intent = new Intent(DailyLogAttendaneArchisman.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }


    private void setUpMapIfNeeded() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //mapView.getMapAsync(this);

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
            if (ContextCompat.checkSelfPermission(DailyLogAttendaneArchisman.this,
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


    @Override
    public void onConnected(Bundle bundle) {
        if (ContextCompat.checkSelfPermission(DailyLogAttendaneArchisman.this,
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

        mGoogleApiClient = new GoogleApiClient.Builder(DailyLogAttendaneArchisman.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(DailyLogAttendaneArchisman.this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
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
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }


    private void handleNewLocation(Location location) {
        latitude = location.getLatitude();
        currentlat = String.valueOf(latitude);
        longitude = location.getLongitude();
        currentlong = String.valueOf(longitude);

        Log.i("LALAL ","LONGITUDDE "+ latitude +" OOO "+longitude);



        // tvAddress.setText(address);
        // latLng = new LatLng(latitude, longitude);

        latLng = new LatLng(latitude, longitude);
        String addressP = getCompleteAddressString(latitude, longitude);


        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(addressP)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mapmarker));




        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)      // Sets the center of the map to location user
                .zoom(16)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        mMap.addMarker(options);
        getAPIKey();

    }




    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(DailyLogAttendaneArchisman.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(DailyLogAttendaneArchisman.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(DailyLogAttendaneArchisman.this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(DailyLogAttendaneArchisman.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(DailyLogAttendaneArchisman.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {


            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(DailyLogAttendaneArchisman.this,
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
                    Toast.makeText(DailyLogAttendaneArchisman.this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private void dailyLogWithoutImage() {
        final ProgressDialog pd=new ProgressDialog(DailyLogAttendaneArchisman.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.upload(pref.getIpAddress() + "ghrmsapi/api/post_Dailylog_Withoutimage")
                .addMultipartParameter("AEMEmployeeID", pref.getEmpId())
                .addMultipartParameter("Remarks", etRemarks.getText().toString())
                .addMultipartParameter("Longitude", currentlong)
                .addMultipartParameter("Latitude", currentlat)
                .addMultipartParameter("Address", address)
                .addMultipartParameter("ApprovalStatus", "1")
                .addMultipartParameter("Year", "0")
                .addMultipartParameter("Month", "0")
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {


                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        pd.dismiss();
                        JSONObject job = response;
                        boolean responseStatus = job.optBoolean("responseStatus");
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
                        pd.dismiss();

                    }
                });
    }

    private void dailyLogWithoutImage2(JSONObject object) {
        Log.e(TAG, "dailyLogWithoutImage2: called");
        final ProgressDialog pd=new ProgressDialog(DailyLogAttendaneArchisman.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(Api.sPostDailyLogWithoutImage)
                .addJSONObjectBody(object)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "WITH_OUT_IMAGE: "+response.toString());
                        pd.dismiss();
                        /*JSONObject job = response;
                        boolean responseStatus = job.optBoolean("responseStatus");
                        if (responseStatus) {
                            successAlert();
                        } else {

                        }*/

                        JSONObject job1 = response;
                        int Response_Code = job1.optInt("Response_Code");
                        String Response_Message = job1.optString("Response_Message");
                        if (Response_Code == 101) {
                            successAlert();
                        } else {
                            Toast.makeText(DailyLogAttendaneArchisman.this,Response_Message,Toast.LENGTH_LONG).show();
                        }
                        // boolean _status = job1.getBoolean("status");
                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        pd.dismiss();
                        if (error.getErrorCode()==401){
                            JSONObject obj=new JSONObject();
                            try {
                                obj.put("MasterID",encrypt(pref.getMasterId(),SECRET_KEY));
                                obj.put("Password",encrypt(pref.getPassword(),SECRET_KEY));
                                obj.put("IMEI","0");
                                obj.put("DeviceID","0");
                                obj.put("DeviceType","A");
                                obj.put("SecurityCode",pref.getSecurityCode());
                                login(obj);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(DailyLogAttendaneArchisman.this, "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void saveNameToLocalStorage( String currentlat,String currentlong,String address,String remarks, String date ,int status) {
        db.addName( currentlat,currentlong,address,remarks,date,  status);
        showAlert();


    }

    private void showAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Unable to Upload Image Due to Network Issue.Your Attendace has been saved successfully without Image.");
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                        Intent intent=new Intent(DailyLogAttendaneArchisman.this,UserDashBoardActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        alertDialogBuilder.show();


    }

    private void dailyActivity() {
        final ProgressDialog progressDialog=new ProgressDialog(DailyLogAttendaneArchisman.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        progressDialog.setCancelable(false);
        Log.e(TAG, "dailyActivity: \nAEMEmployeeID: "+ pref.getEmpId()
                +"\nApprovalStatus: 1"
                +"\nRemarks: "+etRemarks.getText().toString()
                +"\nLongitude: "+currentlong
                +"\nLatitude: "+currentlat
                +"\nAddress: "+address
                +"\nYear: 0"
                +"\nMonth: 0"
                +"\nSecurityCode: "+pref.getSecurityCode()
                +"\nWorkMode: "+workMode
                +"\nFName: 0"
                +"\nImage:");


        AndroidNetworking.upload(pref.getIpAddress() + "ghrmsapi/api/Post_Dailylog_Archisman/Post_ATTENDANCE_ARCHSIMAN")
                .addMultipartParameter("AEMEmployeeID", pref.getEmpId())
                .addMultipartParameter("ApprovalStatus", "1")
                .addMultipartParameter("Remarks", etRemarks.getText().toString())
                .addMultipartParameter("Longitude", currentlong)
                .addMultipartParameter("Latitude", currentlat)
                .addMultipartParameter("Address", address)
                .addMultipartParameter("Year", "0")
                .addMultipartParameter("Month", "0")
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())
                .addMultipartParameter("WorkMode", workMode)
                .addMultipartParameter("FName", "0")
                .addMultipartFile("Image",compressedImageFile)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {


                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        progressDialog.dismiss();
                        JSONObject job = response;
                        boolean responseStatus = job.optBoolean("responseStatus");
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
                        progressDialog.dismiss();
                        Toast.makeText(DailyLogAttendaneArchisman.this, "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void dailyActivity2() {
        Log.e(TAG, "dailyActivity2: called");
        final ProgressDialog progressDialog=new ProgressDialog(DailyLogAttendaneArchisman.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        progressDialog.setCancelable(false);
        //"http://171.16.2.67/GHRMSApi_V2/api/FileUpload/PostGeoFenceAndAttendanceArchisman"
        //Api.sPostGeoFenceAndAttendanceArchisman
        Log.e(TAG, "dailyActivity2: workMode: "+workMode);
        AndroidNetworking.upload(Api.sPostGeoFenceAndAttendanceArchisman)
                .addMultipartParameter("EmployeeID", pref.getEmpId())
                .addMultipartParameter("RemarksIN", etRemarks.getText().toString())
                .addMultipartParameter("RemarksOUT", etRemarks.getText().toString())
                .addMultipartParameter("LongitudeIN", currentlong)
                .addMultipartParameter("LongitudeOUT", currentlong)
                .addMultipartParameter("LatitudeIN", currentlat)
                .addMultipartParameter("LatitudeOUT", currentlat)
                .addMultipartParameter("AddressIN", address)
                .addMultipartParameter("AddressOUT", address)
                .addMultipartParameter("Year", "0")
                .addMultipartParameter("Month", "0")
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())
                .addMultipartParameter("WorkMode", workMode)
                .addMultipartParameter("ApprovalStatus", "1")
                //.addMultipartParameter("FNameIN", "0")
                .addMultipartParameter("FNameOUT", "0")
                .addMultipartParameter("Operation", "3")
                .addMultipartFile("Image",compressedImageFile)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {


                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "Archisman: Field: "+response.toString());
                        progressDialog.dismiss();
                       /* JSONObject job = response;
                        boolean responseStatus = job.optBoolean("responseStatus");
                        if (responseStatus) {
                            successAlert();
                        } else {

                        }*/

                        JSONObject job1 = response;
                        int Response_Code = job1.optInt("Response_Code");
                        String Response_Message = job1.optString("Response_Message");
                        if (Response_Code == 101) {
                            successAlert();
                        } else {
                            //Toast.makeText(DailyLogAttendaneArchisman.this,"Sorry! Your are out of range from your geo fence location",Toast.LENGTH_LONG).show();
                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        progressDialog.dismiss();
                        if (error.getErrorCode()==401){
                            JSONObject obj=new JSONObject();
                            try {
                                obj.put("MasterID",encrypt(pref.getMasterId(),SECRET_KEY));
                                obj.put("Password",encrypt(pref.getPassword(),SECRET_KEY));
                                obj.put("IMEI","0");
                                obj.put("DeviceID","0");
                                obj.put("DeviceType","A");
                                obj.put("SecurityCode",pref.getSecurityCode());
                                login(obj);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(DailyLogAttendaneArchisman.this, "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private void dailyActivityWithGeoFence() {
        final ProgressDialog progressDialog=new ProgressDialog(DailyLogAttendaneArchisman.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        progressDialog.setCancelable(false);
        Log.e(TAG, "dailyActivityWithGeoFence: \nAEMEmployeeID: "+ pref.getEmpId()
                +"\nApprovalStatus: 1"
                +"\nRemarks: "+etRemarks.getText().toString()
                +"\nLongitude: "+currentlong
                +"\nLatitude: "+currentlat
                +"\nAddress: "+address
                +"\nYear: 0"
                +"\nMonth: 0"
                +"\nSecurityCode: "+pref.getSecurityCode()
                +"\nWorkMode: "+workMode
                +"\nFName: 0"
                +"\nImage:");

        AndroidNetworking.upload(pref.getIpAddress() + "ghrmsapi/api/Post_Dailylog_Archisman/Post_GEOFENCE_ATTENDANCE_ARCHISMAN")
                .addMultipartParameter("AEMEmployeeID", pref.getEmpId())
                .addMultipartParameter("ApprovalStatus", "1")
                .addMultipartParameter("Remarks", etRemarks.getText().toString())
                .addMultipartParameter("Longitude", currentlong)
                .addMultipartParameter("Latitude", currentlat)
                .addMultipartParameter("Address", address)
                .addMultipartParameter("Year", "0")
                .addMultipartParameter("Month", "0")
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())
                .addMultipartParameter("WorkMode", workMode)
                .addMultipartParameter("FName", "0")
                .addMultipartFile("Image",compressedImageFile)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {


                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        progressDialog.dismiss();
                        JSONObject job = response;
                        boolean responseStatus = job.optBoolean("responseStatus");
                        if (responseStatus) {
                            successAlert();
                        } else {
                            Toast.makeText(DailyLogAttendaneArchisman.this,"Sorry! Your are out of range from your geo fence location",Toast.LENGTH_LONG).show();

                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        progressDialog.dismiss();
                        Toast.makeText(DailyLogAttendaneArchisman.this, "Something went wrong", Toast.LENGTH_LONG).show();

                    }
                });

    }


    private void dailyActivityWithGeoFence2() {
        Log.e(TAG, "dailyActivityWithGeoFence2: called");
        final ProgressDialog progressDialog=new ProgressDialog(DailyLogAttendaneArchisman.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        progressDialog.setCancelable(false);
        // http://171.16.2.67/GHRMSApi_V2/api/FileUpload/PostGeoFenceAndAttendanceArchisman
        // Api.sPostGeoFenceAndAttendanceArchisman
        AndroidNetworking.upload(Api.sPostGeoFenceAndAttendanceArchisman)
                .addMultipartParameter("EmployeeID", pref.getEmpId())
                .addMultipartParameter("RemarksIN", etRemarks.getText().toString())
                .addMultipartParameter("RemarksOUT", etRemarks.getText().toString())
                .addMultipartParameter("LongitudeIN", currentlong)
                .addMultipartParameter("LongitudeOUT", currentlong)
                .addMultipartParameter("LatitudeIN", currentlat)
                .addMultipartParameter("LatitudeOUT", currentlat)
                .addMultipartParameter("AddressIN", address)
                .addMultipartParameter("AddressOUT", address)
                .addMultipartParameter("Year", "0")
                .addMultipartParameter("Month", "0")
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())
                .addMultipartParameter("WorkMode", workMode)
                .addMultipartParameter("ApprovalStatus", "1")
                //.addMultipartParameter("FNameIN", "0")
                .addMultipartParameter("FNameOUT", "0")
                .addMultipartParameter("Operation", "3")
                .addMultipartFile("Image",compressedImageFile)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {


                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "GeoFence: "+response.toString());
                        progressDialog.dismiss();
                        JSONObject job = response;

                        JSONObject job1 = response;
                        int Response_Code = job1.optInt("Response_Code");
                        String Response_Message = job1.optString("Response_Message");
                        if (Response_Code == 101) {
                            successAlert();
                        } else {
                            Toast.makeText(DailyLogAttendaneArchisman.this,Response_Message,Toast.LENGTH_LONG).show();
                        }

                       /* boolean responseStatus = job.optBoolean("responseStatus");
                        if (responseStatus) {
                            successAlert();
                        } else {
                            Toast.makeText(DailyLogAttendaneArchisman.this,"Sorry! Your are out of range from your geo fence location",Toast.LENGTH_LONG).show();

                        }*/


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        progressDialog.dismiss();
                        if (error.getErrorCode()==401){
                            JSONObject obj=new JSONObject();
                            try {
                                obj.put("MasterID",encrypt(pref.getMasterId(),SECRET_KEY));
                                obj.put("Password",encrypt(pref.getPassword(),SECRET_KEY));
                                obj.put("IMEI","0");
                                obj.put("DeviceID","0");
                                obj.put("DeviceType","A");
                                obj.put("SecurityCode",pref.getSecurityCode());
                                login(obj);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(DailyLogAttendaneArchisman.this, "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }


    private void login(JSONObject jsonObject) {
        final ProgressDialog pd = new ProgressDialog(DailyLogAttendaneArchisman.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(Api.sLoginapi)
                .addJSONObjectBody(jsonObject)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        pd.dismiss();

                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                            JSONArray responseData = job1.optJSONArray("Response_Data");
                            for (int i = 0; i < responseData.length(); i++) {
                                JSONObject obj = responseData.optJSONObject(i);
                                String Genius_Access_Token=obj.optString("Genius_Access_Token");
                                pref.saveAccessToken(Genius_Access_Token);

                                // boolean _status = job1.getBoolean("status");


                                // do anything with response
                            }
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                    }
                });
    }


    @Override
    public void onBackPressed() {
        isAppMinimizeDailyLog = false;
        super.onBackPressed();
    }
}