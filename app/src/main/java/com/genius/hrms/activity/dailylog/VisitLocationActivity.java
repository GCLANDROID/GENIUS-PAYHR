package com.genius.hrms.activity.dailylog;

import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genius.hrms.R;
import com.genius.hrms.activity.activity.EmployeeDashBoardActivity;
import com.genius.hrms.activity.activity.UserDashBoardActivity;
import com.genius.hrms.activity.adapter.VisitingLocationAdapter;
import com.genius.hrms.activity.attendance.AttendanceActivity;
import com.genius.hrms.activity.geofence.GeoFenceDailyLogManageActivity;
import com.genius.hrms.activity.model.VisitingLocationModel;
import com.genius.hrms.activity.utility.Pref;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class VisitLocationActivity extends AppCompatActivity {
    RecyclerView rvItem;
    ArrayList<VisitingLocationModel>itemList=new ArrayList<>();
    Button btnAdd;
    ImageView imgAdd;
    LinearLayout llLoader,llMain,llNoData;
    ProgressDialog pd,pd1;
    Pref pref;
    String formattedDate;
    TextView tvDate;
    FloatingActionButton fbAdd;
    ImageView imgBack,imgHome;
    TextView tvToolBar;
    String surl;
    String attCode;
    String frstPunch;
    double SLongitude,SLatitude,s;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_location);
        initView();

        onClick();
    }

    private void initView(){
        pref=new Pref(getApplicationContext());
        rvItem=(RecyclerView)findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(VisitLocationActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);

        btnAdd=(Button)findViewById(R.id.btnAdd);
        llMain=(LinearLayout)findViewById(R.id.llMain);
        llLoader=(LinearLayout)findViewById(R.id.llLoader);
        llNoData=(LinearLayout)findViewById(R.id.llNoData);
        pd=new ProgressDialog(this);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
         formattedDate = df.format(c);
        Log.d("formattedDate",formattedDate);
        tvDate=(TextView)findViewById(R.id.tvDate);
        tvDate.setText(formattedDate);
        pd1=new ProgressDialog(this);
        fbAdd=(FloatingActionButton)findViewById(R.id.fbAdd);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        tvToolBar=(TextView)findViewById(R.id.tvToolBar);
        if (pref.getLanguage().equals("hi")){
            tvToolBar.setText("स्थान का दौरा किया");
            btnAdd.setText("अपनी गतिविधि शुरू करें");
        }else {
            tvToolBar.setText("Visited place");
            btnAdd.setText("Start your activity");
        }
    }

    private void getItem(){
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        if (pref.getSecurityCode().equals("123")){
           surl= "http://111.93.182.174/GeniusiOSApi/api//get_OfflineDailyLogActivity?AEMEmployeeID=" + pref.getEmpId() + "&Year=0&Month=0&SecurityCode=" + pref.getSecurityCode()+"&AttendanceDate="+formattedDate+"&Operation=1";
        }else {
            surl = pref.getIpAddress()+"GHRMSApi/api/get_OfflineDailyLogActivity?AEMEmployeeID=" + pref.getEmpId() + "&Year=0&Month=0&SecurityCode=" + pref.getSecurityCode()+"&AttendanceDate="+formattedDate+"&Operation=1";

        }
        Log.d("inputactivity", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);

                        // attendabceInfiList.clear();
                        itemList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i <responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);

                                    String PunchInTime = obj.optString("PunchInTime");
                                    String AddressIN = obj.optString("AddressIN");
                                    String LongitudeIN=obj.optString("LongitudeIN");
                                    String LatitudeIN=obj.optString("LatitudeIN");

                                    VisitingLocationModel obj2 = new VisitingLocationModel(AddressIN,PunchInTime,LatitudeIN,LongitudeIN);
                                    itemList.add(obj2);


                                }

                                JSONObject frstPunchObj=responseData.optJSONObject(0);
                                frstPunch=frstPunchObj.optString("PunchInTime");
                                setAdapter();
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNoData.setVisibility(View.GONE);

                            } else {

                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNoData.setVisibility(View.VISIBLE);
                                frstPunch="0";

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);
                llNoData.setVisibility(View.GONE);


                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(VisitLocationActivity.this);
        requestQueue.add(stringRequest);
    }
    private void setAdapter(){
        VisitingLocationAdapter vAdapter=new VisitingLocationAdapter(itemList,getApplicationContext());
        rvItem.setAdapter(vAdapter);
    }
    private void onClick(){
        fbAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (pref.getSecurityCode().equals("1153")){

                       getAttendanceInformationForSmart();
                        /*Intent intent = new Intent(VisitLocationActivity.this, OfflineDailyLogManageActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);*/
                    }else if (pref.getSecurityCode().equals("1157")){

                        Intent intent = new Intent(VisitLocationActivity.this, DailyLogMarkActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(VisitLocationActivity.this, DailyLogMarkActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                }

        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pref.getSecurityCode().equals("1153")){

                    getAttendanceInformationForSmart();
                   /* Intent intent = new Intent(VisitLocationActivity.this, OfflineDailyLogManageActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);*/
                }else if (pref.getSecurityCode().equals("1157")){

                    Intent intent = new Intent(VisitLocationActivity.this, DailyLogMarkActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(VisitLocationActivity.this, DailyLogMarkActivity.class);
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
                Intent intent=new Intent(VisitLocationActivity.this, UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        pd.dismiss();
        pd1.dismiss();
    }


    private void getAttendanceInformationForSmart() {
        Log.d("Arpan", "arpan");
        final ProgressDialog progressDialog = new ProgressDialog(VisitLocationActivity.this);
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

                            Intent intent = new Intent(VisitLocationActivity.this, SmartJuleDailyLogActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("attCode", attCode);
                            intent.putExtra("frstPunch",frstPunch);
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
        RequestQueue requestQueue = Volley.newRequestQueue(VisitLocationActivity.this);
        requestQueue.add(stringRequest);
    }

    private void getValueForGeoFenceForIntas() {

        String surl =  pref.getIpAddress()+"GHRMSApi/api/get_EmployeeGeofenceConfigure?EmployeeId=" + pref.getEmpId() + "&GeoFenceId=0&Operation=5&SecurityCode=" + pref.getSecurityCode();
        Log.d("valuefetechurl", surl);
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new com.android.volley.Response.Listener<String>() {
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
                                String jsonData=responseData.toString();
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                     SLatitude = Double.parseDouble(obj.optString("Latitude"));
                                     SLongitude = Double.parseDouble(obj.optString("Longitude"));
                                     double EndPoint = Double.parseDouble(obj.optString("Radius"));
                                     s=EndPoint/100;



                                }

                                pd.dismiss();

                                Intent intent=new Intent(VisitLocationActivity.this,GeoFenceDailyLogManageActivity.class);
                                intent.putExtra("radius",s);
                                intent.putExtra("jsonData",jsonData);
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
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Toast.makeText(EmployeeDashBoardActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());

            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(VisitLocationActivity.this);
        requestQueue.add(stringRequest);


    }

    @Override
    protected void onResume() {
        super.onResume();
        getItem();
    }
}
