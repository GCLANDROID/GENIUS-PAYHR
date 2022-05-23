package com.genius.hrms.activity.geofence;

import android.app.ProgressDialog;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import com.genius.hrms.R;
import com.genius.hrms.activity.activity.EmployeeDashBoardActivity;
import com.genius.hrms.activity.activity.UserDashBoardActivity;
import com.genius.hrms.activity.leaveapplication.LeaveApplicationActivity;
import com.genius.hrms.activity.model.SpinnerModel;
import com.genius.hrms.activity.utility.Pref;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ConfigNumberActivity extends AppCompatActivity {
    LinearLayout llMultiple,llSingle;
    Pref pref;
    String menu;
    String s1,s2;
    ImageView imgBack,imgHome;
    TextView tvToolbar,tvMultiple,tvSingle;
    boolean accessFlag=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_number);
        getApproverOrNot();
        initView();
        onClick();
    }

    private void initView() {
        pref = new Pref(getApplicationContext());
        llMultiple = (LinearLayout) findViewById(R.id.llMultiple);
        llSingle = (LinearLayout) findViewById(R.id.llSingle);
        menu = pref.getFenceSubMenu();
        if (pref.getFenceSubMenu().equals("")) {
            llMultiple.setVisibility(View.VISIBLE);
            llSingle.setVisibility(View.VISIBLE);

        } else {
            String d = menu.replace("{", "").replace("}", "");
            Log.d("split", d);
            Log.d("menuu", menu);
            String[] separated = menu.split(",");
            if (separated.length == 2) {
                Log.d("arpan", "riku");
                s1 = separated[0];
                s2 = separated[1];
                if (s1.equals("1")) {
                    llSingle.setVisibility(View.GONE);
                } else if (s1.equals("2")) {
                    llMultiple.setVisibility(View.GONE);

                }


                if (s2.equals("1")) {
                    llSingle.setVisibility(View.GONE);
                } else if (s2.equals("2")) {
                    llMultiple.setVisibility(View.GONE);

                }
            }
        }
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        tvToolbar=(TextView)findViewById(R.id.tvToolBar);
        tvMultiple=(TextView)findViewById(R.id.tvMultiple);
        tvSingle=(TextView)findViewById(R.id.tvSingle);
        if (pref.getLanguage().equals("hi")){
            tvToolbar.setText("भू बाड़");
            tvMultiple.setText("विभिन्न");
            tvSingle.setText("एक");

        }else {
            tvToolbar.setText("Geo Fence");
            tvMultiple.setText("Multiple Geo Fence");
            tvSingle.setText("Single Geo Fence");
        }
    }

    private void onClick(){
        llMultiple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accessFlag) {
                    Intent intent = new Intent(ConfigNumberActivity.this, FencingDashBoardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("point", "mul");
                    pref.savePoint("mul");
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(ConfigNumberActivity.this, GeoFenceManageDashBoardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("point", "sin");
                    startActivity(intent);
                }
            }
        });

        llSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accessFlag){
                    Intent intent = new Intent(ConfigNumberActivity.this, FencingDashBoardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("point", "sin");
                    pref.savePoint("sin");
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(ConfigNumberActivity.this, GeoFenceManageDashBoardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("point", "sin");
                    pref.savePoint("sin");
                    startActivity(intent);
                }
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), UserDashBoardActivity.class);
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
    }

    private void getApproverOrNot() {
        final ProgressDialog pd = new ProgressDialog(getApplicationContext());
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



                                accessFlag=true;


                                //llShow.setVisibility(View.VISIBLE);


                            } else {
                              accessFlag=false;
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
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }


}
