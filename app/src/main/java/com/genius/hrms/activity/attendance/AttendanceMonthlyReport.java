package com.genius.hrms.activity.attendance;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genius.hrms.R;
import com.genius.hrms.activity.activity.EmployeeDashBoardActivity;
import com.genius.hrms.activity.adapter.AttendanceMonthlyAdapter;
import com.genius.hrms.activity.adapter.MonthAdapter;
import com.genius.hrms.activity.adapter.YearAdapter;
import com.genius.hrms.activity.model.MonthModel;
import com.genius.hrms.activity.model.YearModel;
import com.genius.hrms.activity.utility.Pref;
import com.genius.hrms.databinding.ActivityAttendanceMonthlyReportBinding;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class AttendanceMonthlyReport extends AppCompatActivity {
    ActivityAttendanceMonthlyReportBinding binding;
    ArrayList<MonthModel> monthList=new ArrayList<>();
    ArrayList<YearModel> yearList=new ArrayList<>();
    String monthID="";
    String monthName="";
    String yearValue="";
    String yearNumber="";
    Pref pref;
    JSONArray attendabceInfiList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_attendance_monthly_report);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_attendance_monthly_report);
        initview();
        onClick();

    }

    private void initview() {
        pref=new Pref(AttendanceMonthlyReport.this);

        monthList.add(new MonthModel("Please Select Month","0"));
        monthList.add(new MonthModel("January","1"));
        monthList.add(new MonthModel("February","2"));
        monthList.add(new MonthModel("March","3"));
        monthList.add(new MonthModel("April","4"));
        monthList.add(new MonthModel("May","5"));
        monthList.add(new MonthModel("June","6"));
        monthList.add(new MonthModel("July","7"));
        monthList.add(new MonthModel("August","8"));
        monthList.add(new MonthModel("September","9"));
        monthList.add(new MonthModel("October","10"));
        monthList.add(new MonthModel("November","11"));
        monthList.add(new MonthModel("December","12"));
        MonthAdapter customAdapter=new MonthAdapter(AttendanceMonthlyReport.this,monthList);
        binding.spMonth.setAdapter(customAdapter);

        //year Spinner
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        String currentYear=String.valueOf(year);
        int year1=year-1;
        String previousYear=String.valueOf(year1);
        int year2=year+1;
        String futureYear=String.valueOf(year2);
        String label="Please Select year";


        yearList.add(new YearModel(label,"0"));
        yearList.add(new YearModel(currentYear,"1"));
        yearList.add(new YearModel(previousYear,"2"));
        yearList.add(new YearModel(futureYear,"3"));
        YearAdapter yearAdapter=new YearAdapter(AttendanceMonthlyReport.this,yearList);
        binding.spYear.setAdapter(yearAdapter);

//        LinearLayoutManager layoutManager
//                = new LinearLayoutManager(AttendanceMonthlyReport.this, LinearLayoutManager.VERTICAL, false);
//        binding.rvAttendanceReport.setLayoutManager(layoutManager);

    }
    private void onClick() {
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(AttendanceMonthlyReport.this, EmployeeDashBoardActivity.class);
                startActivity(i);
                finish();
            }
        });
        binding.spMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>0){
                    monthID=monthList.get(position).getMonthId();
                    monthName=monthList.get(position).getMonthName();
                   // pref.saveSecurityCode(compID);
                     //Toast.makeText(AttendanceMonthlyReport.this, monthID, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.spYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>0){
                    yearValue=yearList.get(position).getYear();
                    yearNumber=yearList.get(position).getNumber();
                    // pref.saveSecurityCode(compID);
                    //Toast.makeText(AttendanceMonthlyReport.this, yearValue, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.llShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (yearValue.equals("")){
                        Toast.makeText(AttendanceMonthlyReport.this, "please select year", Toast.LENGTH_SHORT).show();
                    }
                    else if (monthID.equals("")){
                        Toast.makeText(AttendanceMonthlyReport.this, "please select month", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(AttendanceMonthlyReport.this,3); // you can change grid columns to 3 or more
                        binding.rvAttendanceReport.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView

                        getAttendanceList();
                    }






            }
        });


    }
    private void getAttendanceList() {
        Log.d("Arpan", "arpan");
        binding.llWLLoader.setVisibility(View.VISIBLE);
        binding.llMain.setVisibility(View.GONE);
        binding.llNodata.setVisibility(View.GONE);
        binding.llAgain.setVisibility(View.GONE);
        String surl="https://cloud.geniusconsultant.com/GHRMSApi/api/Attendance/DailyReport?CompanyID=1090000029&EmployeeID="+pref.getEmpId()+"&Calyear="+yearValue+"&MonthID="+monthID+"&SecurityCode="+pref.getSecurityCode();
        //String surl = API.url+ "get_EmployeeAttendanceReport?AEMConsultantID=" + pref.getEmpConId() + "&AEMClientID=" + pref.getEmpClintId() + "&AEMClientOfficeID=" + pref.getEmpClintOffId() + "&AEMEmployeeID=" + pref.getEmpId() + "&CurrentPage=0&AID=1&ApproverStatus=4&YearVal=" + year + "&MonthName=" + month + "&WorkingStatus=1&DbOperation=1&SecurityCode="+pref.getSecurityCode();
        Log.d("input", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);


                        // attendabceInfiList.clear();
                        attendabceInfiList=new JSONArray();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                attendabceInfiList=responseData;
                                for (int i=0;i<attendabceInfiList.length();i++){
                                    JSONObject innerObj = attendabceInfiList.getJSONObject(i);
                                    String AttendanceStatus=innerObj.optString("AttendanceStatus");
                                    if (AttendanceStatus.equals("null")){
                                        attendabceInfiList.remove(i);
                                    }

                                }
                                setAdapter();
                                binding.llWLLoader.setVisibility(View.GONE);
                                binding.llMain.setVisibility(View.VISIBLE);
                                binding.llNodata.setVisibility(View.GONE);
                                binding.llAgain.setVisibility(View.GONE);

                            } else {

                                binding.llWLLoader.setVisibility(View.GONE);
                                binding.llMain.setVisibility(View.GONE);
                                binding.llNodata.setVisibility(View.VISIBLE);
                                binding.llAgain.setVisibility(View.GONE);
                                //Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                binding.llWLLoader.setVisibility(View.GONE);
                binding.llMain.setVisibility(View.GONE);
                binding.llNodata.setVisibility(View.GONE);
                binding.llAgain.setVisibility(View.VISIBLE);

                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(AttendanceMonthlyReport.this);
        requestQueue.add(stringRequest);
    }
    private void setAdapter() {
        AttendanceMonthlyAdapter attendanceAdapter = new AttendanceMonthlyAdapter(AttendanceMonthlyReport.this,attendabceInfiList);
        binding.rvAttendanceReport.setAdapter(attendanceAdapter);
    }


}
