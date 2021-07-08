package com.genius.hrms.activity.geofence;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.genius.hrms.activity.utility.Pref;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FenceNumberActivity extends AppCompatActivity {
    TextView tvNumber;
    ImageView imgBack,imgHome;
    Pref pref;
    FloatingActionButton btnAdd;
    String point;
    String surl;
    TextView tvToolBar,tvTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fence_number);
        initView();
        onClick();
        fenceget();
    }

    private void initView(){
        pref=new Pref(getApplicationContext());
        tvNumber=(TextView)findViewById(R.id.tvNumber);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        btnAdd=(FloatingActionButton)findViewById(R.id.btnAdd);
        point=getIntent().getStringExtra("point");
        tvToolBar=(TextView)findViewById(R.id.tvToolBar);
        tvTotal=(TextView)findViewById(R.id.tvTotal);
        if (pref.getLanguage().equals("hi")){
            tvToolBar.setText("विन्यास");
            tvTotal.setText("कुल जियो बाड़ कॉन्फ़िगर किया गया");
        }else {
            tvToolBar.setText("Configuration");
            tvTotal.setText("Total Geo Fence Configured");
        }
    }

    private void onClick(){
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FenceNumberActivity.this, UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
        tvNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (point.equals("mul")) {
                    Intent intent = new Intent(FenceNumberActivity.this, MultipleConfigReportActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(FenceNumberActivity.this, ConfigReportActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (point.equals("mul")) {
                    Intent intent = new Intent(getApplicationContext(), MulFenceConfigActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getApplicationContext(), SingleFenceConfigActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }
        });
    }

    private void fenceget() {
        if (point.equals("mul")){
            surl =  pref.getIpAddress()+"GHRMSApi/api/get_GeofenceMultiEndPointConfiguration?LocationId=0&Operation=2&SecurityCode=" + pref.getSecurityCode();

        }else {
            surl =  pref.getIpAddress()+"GHRMSApi/api/get_GeofenceConfiguration?SLongitude=0&SLatitude=0&SAddress=0&ELongitude=0&ELatitude=0&EAddress=0&EndPoint=0&LocationName=0&Operation=2&SecurityCode=" + pref.getSecurityCode();
        }
        Log.d("configurl", surl);
        final ProgressDialog progressDialog=new ProgressDialog(FenceNumberActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseconfig", response);
                        progressDialog.dismiss();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("responseconfig", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {

                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String Total = obj.optString("Total");
                                    tvNumber.setText(Total);


                                }



                            } else {
                                progressDialog.dismiss();
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
                progressDialog.dismiss();
                Log.e("ert", error.toString());
                showAlert();
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(FenceNumberActivity.this);
        requestQueue.add(stringRequest);


    }

    private void showAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Slow or No Internet connection");
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();

                    }
                });
        alertDialogBuilder.show();


    }
}
