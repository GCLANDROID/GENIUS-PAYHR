package com.genius.hrms.activity.geofence;

import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.genius.hrms.activity.attendance.AttendanceManageActivity;
import com.genius.hrms.activity.dailylog.NumberTourActivity;
import com.genius.hrms.activity.dailylog.OfflineDailyLogReportActivity;
import com.genius.hrms.activity.utility.LocationUpdaterService;
import com.genius.hrms.activity.utility.Pref;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GeoFenceManageDashBoardActivity extends AppCompatActivity {
    ImageView imgBack, imgHome;
    LinearLayout llManage, llReport;
    Pref pref;
    String surl,surl1;
    String point;
    TextView tvTollBar,tvManage,tvReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_fence_manage_dash_board);
        initView();
        onClick();
    }

    private void initView() {
        pref = new Pref(getApplicationContext());
        Intent startIntent = new Intent(GeoFenceManageDashBoardActivity.this, LocationUpdaterService.class);
        startService(startIntent);
        llManage = (LinearLayout) findViewById(R.id.llManage);
        llReport = (LinearLayout) findViewById(R.id.llReport);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        point=getIntent().getStringExtra("point");
        tvTollBar=(TextView)findViewById(R.id.tvToolBar);
        tvManage=(TextView)findViewById(R.id.tvManage);
        tvReport=(TextView)findViewById(R.id.tvReport);
        if (pref.getLanguage().equals("hi")){
            tvTollBar.setText("गतिविधि");
            tvManage.setText("प्रबंधन");
            tvReport.setText("रिपोर्ट");
        }else {
            tvTollBar.setText("Activity");
            tvManage.setText("Manage");
            tvReport.setText("Report");
        }
    }

    private void onClick() {
        llManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GeoFenceManageDashBoardActivity.this, GeoFenceDailyLogManageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        llReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NumberTourActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
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
                Intent intent = new Intent(getApplicationContext(), UserDashBoardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }


    private void getValue() {

        surl = pref.getIpAddress()+"GHRMSApi/api/get_EmployeeGeofenceConfigure?EmployeeId=" + pref.getEmpId() + "&GeoFenceId=000&Operation=1&SecurityCode=" + pref.getSecurityCode();
        Log.d("valuefetechurl", surl);
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseconfig", response);


                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("responseconfig", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {

                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String SLongitude = obj.optString("SLongitude");
                                    String SLatitude = obj.optString("SLatitude");
                                    String EndPoint = obj.optString("EndPoint");
                                    pref.saveEndPoint(EndPoint);
                                    pref.saveOwnLat(String.valueOf(SLatitude));
                                    pref.saveOwnLong(String.valueOf(SLongitude));


                                }

                                pd.dismiss();
                                Intent intent = new Intent(getApplicationContext(), GeoFenceActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);


                            } else {
                                pd.dismiss();
                                Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();

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

            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(GeoFenceManageDashBoardActivity.this);
        requestQueue.add(stringRequest);


    }

    private void getMulValue() {
        surl1 = pref.getIpAddress()+"GHRMSApi/api/get_EmployeeGeofenceMultipointConfigure?EmployeeId="+pref.getEmpId()+"&GeoFenceId=000&Operation=1&SecurityCode="+pref.getSecurityCode();

        Log.d("valuefetechurl", surl1);
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseconfig", response);


                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("responseconfig", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {

                                pd.dismiss();
                                Intent intent = new Intent(getApplicationContext(), MulFenceAttendanceActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);


                            } else {
                                pd.dismiss();
                                Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();

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

            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(GeoFenceManageDashBoardActivity.this);
        requestQueue.add(stringRequest);


    }
}
