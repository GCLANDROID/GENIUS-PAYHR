package com.genius.payhrms.activity.attendance;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genius.payhrms.R;
import com.genius.payhrms.activity.activity.UserDashBoardActivity;
import com.genius.payhrms.activity.utility.Pref;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class WeeklyoffActivity extends AppCompatActivity {
    ImageView imgDate;
    TextView tvDate;
    Button btnSubmit;
    String startDate="";
    ImageView imgBack,imgHome;
    Pref pref;
    AlertDialog alerDialog1;
    TextView tvToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weeklyoff);
        initView();
        onClick();
    }

    private void initView(){
        pref=new Pref(WeeklyoffActivity.this);
        imgDate=(ImageView)findViewById(R.id.imgDate);
        tvDate=(TextView)findViewById(R.id.tvDate);
        btnSubmit=(Button)findViewById(R.id.btnSubmit);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        tvToolBar=(TextView)findViewById(R.id.tvToolBar);
        if (pref.getLanguage().equals("hi")){
            tvToolBar.setText("साप्ताहिक बंद");
            tvDate.setText("कृपया तारीख का चयन करें");
            btnSubmit.setText("प्रस्तुत");
        }else {
            tvToolBar.setText("Weekly Off");
            tvDate.setText("Please select date");
            btnSubmit.setText("Submit");
        }
    }

    private void onClick(){
        imgDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              showDatePicker();
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
                Intent intent=new Intent(WeeklyoffActivity.this, UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!startDate.equals("")){
                    weeklyOff();
                }else {
                    Toast.makeText(WeeklyoffActivity.this,"Please select date",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {



                        int month = (monthOfYear + 1);
                        startDate = month + "/" + dayOfMonth + "/" + year;
                        tvDate.setText(startDate);

                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.show();

    }



    private void weeklyOff(){

        String surl = pref.getIpAddress()+"GHRMSApi/api/get_WO?CompanyID="+pref.getEmpClintId()+"&EmployeeID="+pref.getEmpId()+"&WeeklyOffDate="+startDate+"&SecurityCode="+pref.getSecurityCode();

        Log.d("weeklyoff", surl);
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLeave", response);
                        progressBar.dismiss();
                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                //
                                successAlert();


                            }else {

                            }


                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(WeeklyoffActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
                Toast.makeText(WeeklyoffActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(WeeklyoffActivity.this);
        requestQueue.add(stringRequest);

    }


    private void successAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(WeeklyoffActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);
        if (pref.getLanguage().equals("hi")){
            tvInvalidDate.setText(startDate+"एक साप्ताहिक बंद के रूप में सहेजा गया");
        }else {
            tvInvalidDate.setText(startDate+"saved as a weekly off");
        }


        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                Intent intent = new Intent(WeeklyoffActivity.this, AttendanceActivity.class);
                startActivity(intent);
                finish();
            }
        });

        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }
}
