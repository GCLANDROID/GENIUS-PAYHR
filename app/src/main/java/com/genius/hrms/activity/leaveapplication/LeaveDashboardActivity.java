package com.genius.hrms.activity.leaveapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genius.hrms.R;
import com.genius.hrms.activity.activity.UserDashBoardActivity;
import com.genius.hrms.activity.payroll.PayrollActivity;
import com.genius.hrms.activity.utility.Pref;

import org.json.JSONException;
import org.json.JSONObject;

public class LeaveDashboardActivity extends AppCompatActivity {
    LinearLayout llLeaveApplication,llLeaveBalance;
    ImageView imgBack,imgHome;
    TextView tvLeave,tvLeaveBalance,tvToolBar;
    Pref pref;
    LinearLayout lnNonAccess,lnAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_dashboard);
        initview();
        onClick();
    }

    private void onClick() {
        llLeaveApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LeaveDashboardActivity.this, LeaveApplicationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        llLeaveBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LeaveDashboardActivity.this, LeaveBalanceActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent=new Intent(LeaveDashboardActivity.this, UserDashBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //finish();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void initview() {
        lnNonAccess=(LinearLayout)findViewById(R.id.lnNonAccess);
        lnAccess=(LinearLayout)findViewById(R.id.lnAccess);
        llLeaveApplication=findViewById(R.id.llLeaveApplication);
        llLeaveBalance=findViewById(R.id.llLeaveBalance);
        imgBack=findViewById(R.id.imgBack);
        imgHome=findViewById(R.id.imgHome);
        tvLeave=findViewById(R.id.tvLeave);
        tvLeaveBalance=findViewById(R.id.tvLeaveBalance);
        tvToolBar=findViewById(R.id.tvToolBar);
        pref = new Pref(getApplicationContext());
        if (pref.getLanguage().equals("hi")) {
            tvLeaveBalance.setText("बकाया छुट्टियां");
            tvLeave.setText("छुट्टी की अर्जी");
            tvToolBar.setText("डैशबोर्ड छोड़ें");
        }
        accessChecking();
    }


    private void accessChecking() {
        String surl = pref.getIpAddress() + "ghrmsapi/api/MenuOnOff/Get?CompanyID="+pref.getEmpClintId()+"&MenuItemName=Leave&SecurityCode="+pref.getSecurityCode();
        Log.d("salaryinput",surl);
        final ProgressDialog pd=new ProgressDialog(LeaveDashboardActivity.this);
        pd.setMessage("Loading");
        pd.show();
        pd.setCancelable(false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        pd.dismiss();


                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                lnAccess.setVisibility(View.GONE);
                                lnNonAccess.setVisibility(View.VISIBLE);

                            } else {

                                lnAccess.setVisibility(View.VISIBLE);
                                lnNonAccess.setVisibility(View.GONE);

                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();

                            //Toast.makeText(SalaryActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                lnAccess.setVisibility(View.VISIBLE);
                lnNonAccess.setVisibility(View.GONE);

                // Toast.makeText(SalaryActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(LeaveDashboardActivity.this);
        requestQueue.add(stringRequest);
    }
}