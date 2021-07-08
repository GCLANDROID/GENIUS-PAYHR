package com.genius.hrms.activity.activity;

import android.content.Intent;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
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
import com.genius.hrms.activity.adapter.HolidayAdapter;
import com.genius.hrms.activity.model.HoliDayModel;
import com.genius.hrms.activity.utility.NetworkConnectionCheck;
import com.genius.hrms.activity.utility.Pref;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class HolidayActivity extends AppCompatActivity {
    RecyclerView rvHoliday;
    ArrayList<HoliDayModel> holidayList=new ArrayList<>();
    ImageView imgBack,imgHome;
    LinearLayout llLoder;
    String year;
    int y;
    Pref pref;
    NetworkConnectionCheck connectionCheck;
    LinearLayout llAgain,llMain,llNodata;
    ImageView imgAgain;
    TextView tvToolBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday);
        initialize();
        getHolidayList();
        onClick();
    }

    private void initialize(){
        pref=new Pref(HolidayActivity.this);
        connectionCheck=new NetworkConnectionCheck(HolidayActivity.this);
        rvHoliday=(RecyclerView)findViewById(R.id.rvHoliday);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(HolidayActivity.this, LinearLayoutManager.VERTICAL, false);
        rvHoliday.setLayoutManager(layoutManager);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        llLoder=(LinearLayout)findViewById(R.id.llWLLoader) ;
        llMain=(LinearLayout)findViewById(R.id.llMain);
        llNodata=(LinearLayout)findViewById(R.id.llNodata);
        llAgain=(LinearLayout)findViewById(R.id.llAgain);
        imgAgain=(ImageView)findViewById(R.id.imgAgain);
        y= Calendar.getInstance().get(Calendar.YEAR);
        year=String.valueOf(y);
        tvToolBar=(TextView)findViewById(R.id.tvToolBar);
        if (pref.getLanguage().equals("hi")){
            tvToolBar.setText("छुट्टी की सूची");
        }else {
            tvToolBar.setText("Holiday List");
        }


    }

    private void onClick(){
        imgAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHolidayList();
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
                Intent intent=new Intent(HolidayActivity.this,UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getHolidayList(){
        Log.d("Arpan","arpan");
        llLoder.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);
        llAgain.setVisibility(View.GONE);
        String surl =pref.getIpAddress()+"GHRMSApi/api/GCLHolidayList_New?AEMEmployeeID="+pref.getEmpId()+"&Year="+year+"&SecurityCode="+pref.getSecurityCode();
        Log.d("inputholiday",surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);

                        // attendabceInfiList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText=job1.optString("responseText");

                            boolean responseStatus=job1.optBoolean("responseStatus");
                            if (responseStatus){
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData=job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++){
                                    JSONObject obj=responseData.getJSONObject(i);
                                    String HolidayName=obj.optString("HolidayName");
                                    String HolidayDate=obj.optString("HolidayDate");
                                    String HDay=obj.optString("HDay");

                                    HoliDayModel obj2 = new HoliDayModel(HolidayName,HolidayDate,HDay);
                                    holidayList.add(obj2);


                                }
                                llLoder.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNodata.setVisibility(View.GONE);
                                llAgain.setVisibility(View.GONE);
                                setAdapter();

                            }

                            else {

                                llLoder.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNodata.setVisibility(View.VISIBLE);
                                llAgain.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoder.setVisibility(View.GONE);
                llMain.setVisibility(View.GONE);
                llNodata.setVisibility(View.GONE);
                llAgain.setVisibility(View.VISIBLE);

                 Toast.makeText(HolidayActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert",error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(HolidayActivity.this);
        requestQueue.add(stringRequest);
    }

    private  void setAdapter(){
        HolidayAdapter hAdapter =new HolidayAdapter(holidayList,HolidayActivity.this);
        rvHoliday.setAdapter(hAdapter);
    }
}
