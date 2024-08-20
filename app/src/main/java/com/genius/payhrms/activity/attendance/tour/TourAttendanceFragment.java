package com.genius.payhrms.activity.attendance.tour;

import static com.genius.payhrms.activity.utility.Util.SECRET_KEY;
import static com.genius.payhrms.activity.utility.Util.encrypt;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.genius.payhrms.activity.activity.UserDashBoardActivity;
import com.genius.payhrms.activity.attendance.AttendanceCalenderDashboardActivity;
import com.genius.payhrms.activity.attendance.AttendanceMarkActivity;
import com.genius.payhrms.activity.helper.DatabaseHelperForDailyLog;
import com.genius.payhrms.activity.model.SpinnerModel;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.GPSTracker;
import com.genius.payhrms.activity.utility.NetworkConnectionCheck;
import com.genius.payhrms.activity.utility.Pref;
import com.genius.payhrms.databinding.FragmentTourApprovalBinding;
import com.genius.payhrms.databinding.FragmentTourAttendanceBinding;
import com.google.android.gms.location.LocationRequest;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;


public class TourAttendanceFragment extends Fragment {
    FragmentTourAttendanceBinding binding;

    private NetworkConnectionCheck connectionCheck;
    Pref pref;
    GPSTracker gps;
    String currentDateTimeString;
    String currentlat, currentlong;
    ArrayList<SpinnerModel> punchTypeList = new ArrayList<>();
    ArrayList<String> punchtypeList = new ArrayList<>();
    String Punchtype;
    int flag;
    private String encodedImage;
    private Uri imageUri, uri;
    private static final int CAMERA_REQUEST = 1;
    File compressedImageFile, file;
    AlertDialog alerDialog1;
    double latitude = 0.0, longitude = 0.0;
    String address;
    AlertDialog locationpopup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTourAttendanceBinding.inflate(getLayoutInflater());
        initview();
        onClick();
        return binding.getRoot();
    }


    @SuppressLint("RestrictedApi")
    private void initview() {

        connectionCheck = new NetworkConnectionCheck(getContext());

        if (connectionCheck.isGPSEnabled()) {

        } else {
            locationAlert();
        }

        pref = new Pref(getContext());


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.lnLoader.setVisibility(View.GONE);
                binding.lnMain.setVisibility(View.VISIBLE);


            }
        }, 3000);


        // Change base URL to your upload server URL.
        /*uploadService = (AttendanceService) new Retrofit.Builder()
                .baseUrl(SERVER_PATH)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AttendanceService.class);*/


        gps = new GPSTracker(getContext());
        if (pref.getShiftFlag().equals("1")) {
            binding.llShift.setVisibility(View.VISIBLE);
        } else {
            binding.llShift.setVisibility(View.GONE);
        }

        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            Log.d("saikatdas", String.valueOf(latitude));
            longitude = gps.getLongitude();
        } else {
// can't get location
// GPS or Network is not enabled
// Ask user to enable GPS/network in settings

        }

        binding.tvName.setText(pref.getEmpName());


        // tvAddress.setText("YOU ARE AT: " + address);
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        currentDateTimeString = sdf.format(d);
        binding.tvTime.setText("Current time is : " + currentDateTimeString);
        currentlat = String.valueOf(latitude);
        currentlong = String.valueOf(longitude);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        // Change base URL to your upload server URL.


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("BranchID", pref.getEmpClintOffId());
            jsonObject.put("SecurityCode", pref.getSecurityCode());
            getpunchType(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        getAPIKey();
    }


    private void onClick() {


        binding.imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cameraIntent();


            }
        });


        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.tvAddress.getText().toString().equals("YOU ARE AT: null") || binding.tvAddress.getText().toString().equals("YOU ARE AT: ")) {


                            shiftFlagFilter();



                } else {
                    Toast.makeText(getContext(), "Sorry! Your address not found.Please click on Refresh button", Toast.LENGTH_LONG).show();
                }
            }


        });


    }

    private void cameraIntent() {
        flag = 1;
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getActivity().getContentResolver().insert(
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
                            compressedImageFile = new ImageZipper(getContext())
                                    .setQuality(80)
                                    .setMaxWidth(250)
                                    .setMaxHeight(250)
                                    .compressToFile(file);
                            // Log.d("imageSixw", String.valueOf(getReadableFileSize(compressedImageFile.length())));


                            BitmapFactory.Options o = new BitmapFactory.Options();
                            o.inSampleSize = 6;
                            Bitmap bo = cropToSquare(BitmapFactory.decodeFile(imageurl, o));
                            Bitmap bm = new ImageZipper(getContext()).compressToBitmap(file);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bo.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
                            byte[] b = baos.toByteArray();
                            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                            Log.d("encodedimage", encodedImage);
                            binding.imgImage.setImageBitmap(bm);
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


        }
    }

    private String getRealPathFromURIPath(Uri contentURI) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(contentURI, proj, null, null, null);
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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.succes_alert, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llOk = (LinearLayout) dialogView.findViewById(R.id.llOk);
        llOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerDialog1.dismiss();
                Intent intent = new Intent(getContext(), AttendanceCalenderDashboardActivity.class);
                startActivity(intent);

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


    private void getAPIKey() {
        String surl = "https://cloud.geniusconsultant.com/GeniusESS/API/Utility/GetLocationKey";
        Log.d("residancelist", surl);
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
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
                binding.tvAddress.setText(address);

                // Toast.makeText(SalaryActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());

            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);


    }

    private void getaddressFromAPI(String apikey) {
        String testUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=28.5530871,77.201581&key=" + apikey;
        String surl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitude + "&key=" + apikey;
        Log.d("residancelist", surl);
        final ProgressDialog pd = new ProgressDialog(getContext());
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
                                JSONObject plus_code = job1.optJSONObject("plus_code");
                                //address =plus_code.optString("compound_code");
                                JSONObject object = results.optJSONObject(0);
                                address = object.optString("formatted_address");
                                binding.tvAddress.setText("You are at:- " + address);


                            } else {
                                address = getCompleteAddressString(latitude, longitude);
                                binding.tvAddress.setText(address);
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
                binding.tvAddress.setText(address);

                // Toast.makeText(SalaryActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());

            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);


    }


    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
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


    private void login(JSONObject jsonObject) {

        final ProgressDialog pd = new ProgressDialog(getContext());
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
                                String Genius_Access_Token = obj.optString("Genius_Access_Token");
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


    private void getpunchType(JSONObject jsonObject) {


        punchtypeList.add("Tour");

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_spinner_item,
                        punchtypeList); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spshift.setAdapter(spinnerArrayAdapter);

    }


    public void selfAttendanceWithPunch() {
        final ProgressDialog pg = new ProgressDialog(getContext());
        pg.setMessage("Loading..");
        pg.setCancelable(false);
        pg.show();
        //Log.e(TAG, "selfAttendance: ", );

        AndroidNetworking.upload(Api.sselfattendanceimageapi)
                .addMultipartParameter("AEMEmployeeID", pref.getEmpId())
                .addMultipartParameter("Address", address)
                .addMultipartParameter("Longitude", String.valueOf(longitude))
                .addMultipartParameter("Latitude", String.valueOf(latitude))
                .addMultipartParameter("Punchtype", "2")
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())
                .addMultipartFile("Image", compressedImageFile)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
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
                        int Response_Code = job.optInt("Response_Code");
                        String Response_Message = job.optString("Response_Message");
                        if (Response_Code == 101) {
                            // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                            successAlert();
                            // boolean _status = job1.getBoolean("status");
                            // do anything with response
                        } else {
                            Toast.makeText(getContext(), Response_Message, Toast.LENGTH_LONG).show();
                        }

                        // boolean _status = job1.getBoolean("status");
                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e("error", error.toString());
                        pg.dismiss();
                        if (error.getErrorCode() == 401) {
                            JSONObject obj = new JSONObject();
                            try {
                                obj.put("MasterID", encrypt(pref.getMasterId(), SECRET_KEY));
                                obj.put("Password", encrypt(pref.getPassword(), SECRET_KEY));
                                obj.put("IMEI", "0");
                                obj.put("DeviceID", "0");
                                obj.put("DeviceType", "A");
                                obj.put("SecurityCode", pref.getSecurityCode());
                                login(obj);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });
    }

    private void selfAttendanceWithPunch(JSONObject jsonObject) {

        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(Api.sPostSelfAttendanceShalimarapi)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        pd.dismiss();
                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);

                        int Response_Code = job1.optInt("Response_Code");
                        String Response_Message = job1.optString("Response_Message");
                        if (Response_Code == 101) {
                            // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                            successAlert();
                            // boolean _status = job1.getBoolean("status");
                            // do anything with response
                        } else {

                            Toast.makeText(getContext(), Response_Message, Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onError(ANError error) {

                        if (error.getErrorCode() == 401) {
                            JSONObject obj = new JSONObject();
                            try {
                                obj.put("MasterID", encrypt(pref.getMasterId(), SECRET_KEY));
                                obj.put("Password", encrypt(pref.getPassword(), SECRET_KEY));
                                obj.put("IMEI", "0");
                                obj.put("DeviceID", "0");
                                obj.put("DeviceType", "A");
                                obj.put("SecurityCode", pref.getSecurityCode());
                                login(obj);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }
                });
    }

    private void shiftFlagFilter() {

        if (flag == 1) {
            // attendance();
            selfAttendanceWithPunch();
        } else {
            //attendancefunction();
            JSONObject object = new JSONObject();
            try {
                object.put("AEMEmployeeID", pref.getEmpId());
                object.put("Address", address);
                object.put("Longitude", longitude);
                object.put("Latitude", latitude);
                object.put("Punchtype", "2");
                object.put("SecurityCode", pref.getSecurityCode());
                selfAttendanceWithPunch(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }


    private void locationAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.location_error, null);
        dialogBuilder.setView(dialogView);
        TextView tvError = (TextView) dialogView.findViewById(R.id.tvError);
        tvError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationpopup.dismiss();
                Intent intent = new Intent(getContext(), AttendanceCalenderDashboardActivity.class);
                startActivity(intent);

            }
        });


        locationpopup = dialogBuilder.create();
        locationpopup.setCancelable(false);
        Window window = locationpopup.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        locationpopup.show();
    }
}