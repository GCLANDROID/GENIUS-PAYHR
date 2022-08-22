package com.genius.hrms.activity.attendance;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genius.hrms.R;

import com.genius.hrms.activity.activity.EmployeeDashBoardActivity;
import com.genius.hrms.activity.activity.LoginActivity;
import com.genius.hrms.activity.activity.UserDashBoardActivity;
import com.genius.hrms.activity.dailylog.SmartJuleDailyLogActivity;
import com.genius.hrms.activity.model.AttendanceModule;
import com.genius.hrms.activity.utility.CreativePermission;
import com.genius.hrms.activity.utility.GPSTracker;
import com.genius.hrms.activity.utility.NetworkConnectionCheck;
import com.genius.hrms.activity.utility.Pref;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class AttendanceActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final int PERMISSION_ALL = 100;
    LinearLayout llAttandanceManage, llAttendanceReport;
    ImageView imgBack, imgHome;
    AlertDialog alerDialog1;
    String month, year;
    int y;
    Pref pref;
    NetworkConnectionCheck connectionCheck;
    int flag;
    String formattedDate;
    String AttendanceType;
    String ApproverStatus;
    GoogleApiClient googleApiClient;
    AlertDialog alertDialog;
    TextView tvManage, tvReport, tvToolBar;
    LinearLayout llWeeklyOff, llHolidayMap;
    TextView tvWeeklyOff, tvHoliDayMap;
    LinearLayout llSupervisior;
    TextView tvSupervisior;
    TextView tvBackLog, tvRegulization;
    LinearLayout llBackLog, llRegulization;
    GPSTracker gps;
    double latitude;
    LinearLayout llFace;
    String attCode;
    private CreativePermission myPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        initialize();
        onClick();

    }

    private void initialize() {
        pref = new Pref(getApplicationContext());
        llFace=findViewById(R.id.llFace);

        tvSupervisior = findViewById(R.id.tvSupervisior);
        connectionCheck = new NetworkConnectionCheck(this);
        llAttandanceManage = findViewById(R.id.llAttandanceManage);
        llAttendanceReport = findViewById(R.id.llAttendanceReport);
        llWeeklyOff = findViewById(R.id.llWeeklyOff);
        llHolidayMap = findViewById(R.id.llHolidayMap);
        llBackLog = findViewById(R.id.llBackLog);
        if (pref.getWeeklyOffFlag().equals("1")) {
            llWeeklyOff.setVisibility(View.VISIBLE);
        } else {
            llWeeklyOff.setVisibility(View.GONE);
        }

        if (pref.getHolidayMapFlag().equals("1")) {
            llHolidayMap.setVisibility(View.VISIBLE);
        } else {
            llHolidayMap.setVisibility(View.GONE);
        }


        imgBack = findViewById(R.id.imgBack);
        imgHome = findViewById(R.id.imgHome);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        formattedDate = df.format(c);


        y = Calendar.getInstance().get(Calendar.YEAR);
        year = String.valueOf(y);
        Log.d("year", year);

        int m = Calendar.getInstance().get(Calendar.MONTH) + 1;
        Log.d("month", String.valueOf(m));
        if (m == 1) {
            month = "January";
        } else if (m == 2) {
            month = "February";
        } else if (m == 3) {
            month = "March";
        } else if (m == 4) {
            month = "April";
        } else if (m == 5) {
            month = "May";
        } else if (m == 6) {
            month = "June";
        } else if (m == 7) {
            month = "July";
        } else if (m == 8) {
            month = "August";
        } else if (m == 9) {
            month = "September";
        } else if (m == 10) {
            month = "October";
        } else if (m == 11) {
            month = "November";
        } else if (m == 12) {
            month = "December";
        }


//

        tvToolBar = findViewById(R.id.tvToolBar);
        tvManage = findViewById(R.id.tvManage);
        tvReport = findViewById(R.id.tvReport);
        tvWeeklyOff = findViewById(R.id.tvWeeklyOff);
        tvHoliDayMap = findViewById(R.id.tvHoliDayMap);
        tvBackLog = findViewById(R.id.tvBackLog);
        tvRegulization = findViewById(R.id.tvRegulization);
        if (pref.getLanguage().equals("hi")) {
            tvReport.setText("रिपोर्ट");
            tvManage.setText("प्रबंधन");
            tvToolBar.setText("उपस्थिति");
            tvWeeklyOff.setText("साप्ताहिक बंद");
            tvHoliDayMap.setText("छुट्टी की मैपिंग");
            tvBackLog.setText("बैकलॉग उपस्थिति");
            tvRegulization.setText("अटेन्डेस रेगुलाइजेशन");
            tvSupervisior.setText("पर्यवेक्षक");
        } else {
            tvReport.setText("Report");
            tvManage.setText("Manage");
            tvToolBar.setText("Attendance");
            tvWeeklyOff.setText("Weekly Off");
            tvHoliDayMap.setText("Holiday Mapping");
            tvBackLog.setText("Backlog Attendance");
            tvRegulization.setText("Attendance Regulization");
            tvSupervisior.setText("Supervisior");
        }

        myPermission = new CreativePermission(this, PERMISSION_ALL);
        llSupervisior = findViewById(R.id.llSupervisior);
        llRegulization = findViewById(R.id.llRegulization);
        gps = new GPSTracker(AttendanceActivity.this);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            Log.d("saikatdas", String.valueOf(latitude));
            double longitude = gps.getLongitude();
        } else {
// can't get location
// GPS or Network is not enabled
// Ask user to enable GPS/network in settings

        }
        if (pref.getSecurityCode().equals("123")) {
            llBackLog.setVisibility(View.GONE);
            llRegulization.setVisibility(View.GONE);
        } else {
            llBackLog.setVisibility(View.VISIBLE);
            llRegulization.setVisibility(View.GONE);
        }


    }

    private void onClick() {
        llFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AttendanceActivity.this, FRDashboard.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });
        llBackLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AttendanceActivity.this, BacklogActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        llRegulization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AttendanceActivity.this, AttendanceRegulizationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        llAttandanceManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionCheck.isNetworkAvailable()) {
                    if (connectionCheck.isGPSEnabled()) {


                        attenDanceIntent();
                    }else {
                        connectionCheck.getSettingsAlert().show();
                    }


                } else {
                    connectionCheck.getNetworkActiveAlert().show();
                }
            }
        });

        llAttendanceReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectionCheck.isNetworkAvailable()) {

                    attenDanceReportIntent();
                } else {
                    connectionCheck.getNetworkActiveAlert().show();
                }
            }
        });

        llWeeklyOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AttendanceActivity.this, WeeklyoffActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        llHolidayMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AttendanceActivity.this, HoliDayMapActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AttendanceActivity.this, UserDashBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //  finish();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        llSupervisior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attenSuperVisiorIntent();

            }
        });


    }


    private void turnGPSOn() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(AttendanceActivity.this).build();
            googleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            // **************************
            builder.setAlwaysShow(true); // this is the key ingredient
            // **************************

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                    .checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result
                            .getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            Intent intent = new Intent(AttendanceActivity.this, AttendanceReportActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                try {
                                    status.startResolutionForResult(AttendanceActivity.this, 1000);
                                } catch (IntentSender.SendIntentException e) {
                                    // Ignore the error.
                                }
                            } catch (Exception e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

                            break;
                    }
                }
            });
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void blockshowing() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AttendanceActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_blocked, null);
        dialogBuilder.setView(dialogView);
        TextView tvSuccess = dialogView.findViewById(R.id.tvSuccess);
        tvSuccess.setText("Your attendance has been blocked");


        Button btnOk = dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
            }
        });

        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(true);
        Window window = alertDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog.show();
    }

    private void attenDanceIntent() {
        if (pref.getSecurityCode().equals("11") || pref.getSecurityCode().equals("123")) {
            Intent intent = new Intent(AttendanceActivity.this, AttendanceManageForPPSActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (pref.getSecurityCode().equals("1135")) {
            getAttendanceInformation();
        }else if (pref.getSecurityCode().equals("1153")) {
            getAttendanceInformationForSmart();
        } else {
            Intent intent = new Intent(AttendanceActivity.this, AttendanceManageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }


    }

    private void attenDanceReportIntent() {
        if (pref.getSecurityCode().equals("11") || pref.getSecurityCode().equals("123")) {
            Intent intent = new Intent(AttendanceActivity.this, AttendanceReportForPPSActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Intent intent = new Intent(AttendanceActivity.this, AttendanceReportActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }


    }


    private void attenSuperVisiorIntent() {
        if (pref.getSecurityCode().equals("11") || pref.getSecurityCode().equals("123")) {
            Intent intent = new Intent(AttendanceActivity.this, AttenApprovalActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Intent intent = new Intent(AttendanceActivity.this, SuperVisiorActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }


    }

    private void getAttendanceInformation() {
        Log.d("Arpan", "arpan");
        final ProgressDialog progressDialog = new ProgressDialog(AttendanceActivity.this);
        progressDialog.setMessage("Loadingg..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String surl = pref.getIpAddress() + "GHRMSApi/api/attendance/SingleAttendanceExistanceStatus?EmployeeID=" + pref.getEmpId() + "&AttendanceDate=" + formattedDate + "&SecurityCode=" + pref.getSecurityCode();
        Log.d("input", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        progressDialog.dismiss();

                        // attendabceInfiList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");


                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                                attCode = "1";


                            } else {
                                attCode = "0";
                            }

                            Intent intent = new Intent(AttendanceActivity.this, Em3AttendnaceActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("attCode", attCode);
                            startActivity(intent);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(AttendanceActivity.this);
        requestQueue.add(stringRequest);
    }

    private void getAttendanceInformationForSmart() {
        Log.d("Arpan", "arpan");
        final ProgressDialog progressDialog = new ProgressDialog(AttendanceActivity.this);
        progressDialog.setMessage("Loadingg..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String surl = pref.getIpAddress() + "GHRMSApi/api/attendance/SingleAttendanceExistanceStatus?EmployeeID=" + pref.getEmpId() + "&AttendanceDate=" + formattedDate + "&SecurityCode=" + pref.getSecurityCode();
        Log.d("input", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        progressDialog.dismiss();

                        // attendabceInfiList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");


                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                                attCode = "1";


                            } else {
                                attCode = "0";
                            }

                           /* Intent intent = new Intent(AttendanceActivity.this, SmartJuleDailyLogActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("attCode", attCode);
                            startActivity(intent);
*/
                            Intent intent = new Intent(AttendanceActivity.this, AttendanceManageActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(AttendanceActivity.this);
        requestQueue.add(stringRequest);
    }



}
