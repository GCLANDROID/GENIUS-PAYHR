package com.genius.payhrms.activity.dailylog;

import static com.genius.payhrms.activity.utility.Util.SECRET_KEY;
import static com.genius.payhrms.activity.utility.Util.encrypt;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
import com.genius.payhrms.R;
import com.genius.payhrms.activity.activity.UserDashBoardActivity;
import com.genius.payhrms.activity.attendance.AttendanceMonthlyReport;
import com.genius.payhrms.activity.attendance.AttendanceRegulizationActivity;
import com.genius.payhrms.activity.attendance.AttendanceReportActivity;
import com.genius.payhrms.activity.attendance.AttendanceReportForPPSActivity;
import com.genius.payhrms.activity.attendance.BacklogActivity;
import com.genius.payhrms.activity.attendance.SuperVisiorActivity;
import com.genius.payhrms.activity.reciver.DailylogSyncReciever;
import com.genius.payhrms.activity.reciver.NetworkStateChecker;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.NetworkConnectionCheck;
import com.genius.payhrms.activity.utility.Pref;

import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.TimeZone;


public class OfflineDailyDashBoardActivity extends AppCompatActivity {
    private static final String TAG = "OfflineDailyDashBoardAc";
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

        JSONObject object=new JSONObject();
        try {
            object.put("CompanyID",pref.getEmpClintId());
            object.put("EmployeeID",pref.getEmpId());
            object.put("SecurityCode",pref.getSecurityCode());
            getApproverOrNot2(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
                    if (pref.getLoginID().equals("FSS0047") ||pref.getLoginID().equals("FSS0131") ||pref.getLoginID().equals("FSS0070")||pref.getLoginID().equals("FSS0462")||pref.getLoginID().equals("FSS0101")||pref.getLoginID().equals("FSS0107") ){
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
                    //holidayCheckForSmartJoules();
                    /*JSONObject object=new JSONObject();
                    try {
                        object.put("EmployeeID",pref.getEmpId());
                        object.put("HolidayDate",currentDate);
                        object.put("SecurityCode","1153");
                        holidayCheckForSmartJoules2(object);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/
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



    private void getApproverOrNot2(JSONObject object) {
        final ProgressDialog pd = new ProgressDialog(OfflineDailyDashBoardActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(true);
        pd.show();

        AndroidNetworking.post(Api.sapprovercheckapi)
                .addJSONObjectBody(object)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "getApproverOrNot2: "+response.toString());
                        pd.dismiss();

                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);

                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            approver = true;
                        }else {
                            approver = false;
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "onError: "+anError);
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
                    }
                });

    }

    private void login(JSONObject jsonObject) {
        final ProgressDialog pd = new ProgressDialog((Context) OfflineDailyDashBoardActivity.this);
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
                                JSONObject object=new JSONObject();
                                try {
                                    object.put("CompanyID",pref.getEmpClintId());
                                    object.put("EmployeeID",pref.getEmpId());
                                    object.put("SecurityCode",pref.getSecurityCode());
                                    getApproverOrNot2(object);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
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


