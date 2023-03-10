package com.genius.hrms.activity.dailylog;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genius.hrms.R;
import com.genius.hrms.activity.activity.EmployeeDashBoardActivity;
import com.genius.hrms.activity.activity.UserDashBoardActivity;
import com.genius.hrms.activity.attendance.AttendanceActivity;
import com.genius.hrms.activity.attendance.AttendanceMonthlyReport;
import com.genius.hrms.activity.attendance.AttendanceRegulizationActivity;
import com.genius.hrms.activity.attendance.AttendanceReportActivity;
import com.genius.hrms.activity.attendance.AttendanceReportForPPSActivity;
import com.genius.hrms.activity.attendance.BacklogActivity;
import com.genius.hrms.activity.attendance.SuperVisiorActivity;
import com.genius.hrms.activity.leaveapplication.LeaveApplicationActivity;
import com.genius.hrms.activity.model.SpinnerModel;
import com.genius.hrms.activity.reciver.DailylogSyncReciever;
import com.genius.hrms.activity.reciver.NetworkStateChecker;
import com.genius.hrms.activity.utility.NetworkConnectionCheck;
import com.genius.hrms.activity.utility.Pref;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class OfflineDailyDashBoardActivity extends AppCompatActivity {
    LinearLayout llManage, llReport, llLog, llSubordinate, llBackLog, llMonthlyAttenReport, llQRCode;

    ImageView imgBack, imgHome;
    NetworkConnectionCheck connectionCheck;
    ProgressDialog progressDialog;
    TextView tvManage, tvLogBook, tvReport, tvToolBar, tvsubordinate, tvBackLog;
    Pref pref;
    String currentDate;
    boolean approver;
    NetworkStateChecker airplaneModeChangeReceiver = new NetworkStateChecker();
    DailylogSyncReciever dailyLogReciever = new DailylogSyncReciever();
    private ReviewManager reviewManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_daily_dash_board);
        initialize();
        onClick();
    }

    private void initialize() {
        pref = new Pref(getApplicationContext());
        reviewManager = ReviewManagerFactory.create(this);
        connectionCheck = new NetworkConnectionCheck(OfflineDailyDashBoardActivity.this);
        llManage = (LinearLayout) findViewById(R.id.llManage);
        llReport = (LinearLayout) findViewById(R.id.llReport);
        llSubordinate = (LinearLayout) findViewById(R.id.llSubordinate);
        llMonthlyAttenReport = (LinearLayout) findViewById(R.id.llMonthlyAttenReport);
        llQRCode = (LinearLayout) findViewById(R.id.llQRCode);

        if (pref.getSecurityCode().equals("1155")) {
            llMonthlyAttenReport.setVisibility(View.VISIBLE);
        } else {
            llMonthlyAttenReport.setVisibility(View.GONE);
        }


        llLog = (LinearLayout) findViewById(R.id.llLog);
        llBackLog = (LinearLayout) findViewById(R.id.llBackLog);

        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        tvLogBook = (TextView) findViewById(R.id.tvLogBook);
        tvManage = (TextView) findViewById(R.id.tvManage);
        tvReport = (TextView) findViewById(R.id.tvReport);
        tvToolBar = (TextView) findViewById(R.id.tvToolBar);
        tvsubordinate = (TextView) findViewById(R.id.tvsubordinate);
        tvBackLog = findViewById(R.id.tvBackLog);
        if (pref.getLanguage().equals("hi")) {
            tvManage.setText("प्रबंधन");
            tvLogBook.setText("कार्यपंजी");
            tvReport.setText("रिपोर्ट");
            tvToolBar.setText("दैनिक लॉग");
            tvsubordinate.setText("टीम रिपोर्ट");
            tvBackLog.setText("बैकलॉग उपस्थिति");
        } else {
            tvManage.setText("Manage");
            tvLogBook.setText("Log Book");
            tvReport.setText("Attendance Report");
            tvToolBar.setText("Daily Log");
            tvsubordinate.setText("Team Report");
            tvBackLog.setText("Attendance Regularization");
        }

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        currentDate = currentYear + "-" + currentMonth + "-" + currentDay;
        if (pref.getSecurityCode().equals("1158")) {
            llBackLog.setVisibility(View.GONE);
        } else {
            llBackLog.setVisibility(View.VISIBLE);
        }

        if (pref.getSecurityCode().equals("1000") || pref.getSecurityCode().equals("2000")|| pref.getSecurityCode().equals("1160")) {
            llQRCode.setVisibility(View.VISIBLE);

        } else {
            llQRCode.setVisibility(View.GONE);

        }

        getApproverOrNot();

        //currentDate="2022-08-15";
    }

    private void onClick() {
        llBackLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pref.getSecurityCode().equals("1153")) {
                    Intent intent = new Intent(OfflineDailyDashBoardActivity.this, AttendanceRegulizationActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(OfflineDailyDashBoardActivity.this, BacklogActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }
        });

        llMonthlyAttenReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OfflineDailyDashBoardActivity.this, AttendanceMonthlyReport.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        llQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pref.getSecurityCode().equals("1160")){
                    if (pref.getLoginID().equals("FSS0120") ||pref.getLoginID().equals("FSS0243") ||pref.getLoginID().equals("FSS0047")||pref.getLoginID().equals("FSS0163")||pref.getLoginID().equals("FSS0101") ){
                        Intent intent = new Intent(OfflineDailyDashBoardActivity.this, QRAttendanceDashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(OfflineDailyDashBoardActivity.this, QRCodeScannerActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                }else {
                    if (approver) {
                        Intent intent = new Intent(OfflineDailyDashBoardActivity.this, QRAttendanceDashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(OfflineDailyDashBoardActivity.this, QRCodeScannerActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }

            }
        });


        llManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pref.getSecurityCode().equals("1153")) {
                    holidayCheckForSmartJoules();
                } else {
                    Intent intent = new Intent(OfflineDailyDashBoardActivity.this, VisitLocationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }


            }
        });


        llLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (connectionCheck.isNetworkAvailable()) {

                    Intent intent = new Intent(OfflineDailyDashBoardActivity.this, NumberTourActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(OfflineDailyDashBoardActivity.this, OfflineAttenReportActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        llSubordinate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(OfflineDailyDashBoardActivity.this, SuperVisiorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });


        llReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (pref.getSecurityCode().equals("123")) {
                    Intent intent = new Intent(OfflineDailyDashBoardActivity.this, AttendanceReportForPPSActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(OfflineDailyDashBoardActivity.this, AttendanceReportActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }
        });


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OfflineDailyDashBoardActivity.this, UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        progressDialog.dismiss();
    }

    private void holidayCheckForSmartJoules() {
        Log.d("Arpan", "arpan");
        final ProgressDialog progressDialog = new ProgressDialog(OfflineDailyDashBoardActivity.this);
        progressDialog.setMessage("Loadingg..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String surl = pref.getIpAddress() + "GHRMSApi/api/getHolidayCheck/Get_HolidayListCheck?EmployeeID=" + pref.getEmpId() + "&HolidayDate=" + currentDate + "&SecurityCode=1153";
        Log.d("holidaycheck", surl);
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

                                showAlert();


                            } else {

                                Intent intent = new Intent(OfflineDailyDashBoardActivity.this, VisitLocationActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);


                            }


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
        RequestQueue requestQueue = Volley.newRequestQueue(OfflineDailyDashBoardActivity.this);
        requestQueue.add(stringRequest);
    }

    private void showAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("It's a public holiday. Do you still want to continue?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                        Intent intent = new Intent(OfflineDailyDashBoardActivity.this, VisitLocationActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();

                    }
                });
        alertDialogBuilder.show();


    }

    private void getApproverOrNot() {
        final ProgressDialog pd = new ProgressDialog(OfflineDailyDashBoardActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(true);
        pd.show();

        String surl = pref.getIpAddress() + "ghrmsapi/api/Leave/LeaveApplicationApprover?CompanyID=" + pref.getEmpClintId() + "&EmployeeID=" + pref.getEmpId() + "&SecurityCode=" + pref.getSecurityCode();
        Log.d("printurlbalance", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
//                        llLoader.setVisibility(View.GONE);

                        pd.dismiss();


                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {

                                approver = true;
                            } else {
                                approver = false;
                                // llShow.setVisibility(View.GONE);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                pd.dismiss();

                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(OfflineDailyDashBoardActivity.this);
        requestQueue.add(stringRequest);

    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(airplaneModeChangeReceiver, filter);
        registerReceiver(dailyLogReciever, filter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(airplaneModeChangeReceiver);
        unregisterReceiver(dailyLogReciever);
    }
}


