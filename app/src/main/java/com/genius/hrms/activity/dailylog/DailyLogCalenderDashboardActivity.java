package com.genius.hrms.activity.dailylog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genius.hrms.R;
import com.genius.hrms.activity.activity.LoginActivity;
import com.genius.hrms.activity.activity.UserDashBoardActivity;
import com.genius.hrms.activity.adapter.AttendanceCalenderAdapter;
import com.genius.hrms.activity.attendance.AttendanceReportActivity;
import com.genius.hrms.activity.attendance.BacklogActivity;
import com.genius.hrms.activity.attendance.SuperVisiorActivity;
import com.genius.hrms.activity.model.AttendanceCalenderModel;
import com.genius.hrms.activity.model.SpinnerModel;
import com.genius.hrms.activity.payroll.SalaryActivity;
import com.genius.hrms.activity.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DailyLogCalenderDashboardActivity extends AppCompatActivity implements View.OnClickListener {
    Spinner spMonth;
    ArrayList<String>monthList=new ArrayList<>();
    ArrayList<SpinnerModel>mmonthList=new ArrayList<>();
    DrawerLayout dlMain;
    boolean mslideState;
    ImageView imgMenu;
    String Month;
    int y;
    Pref pref;
    ArrayList<AttendanceCalenderModel> itemList = new ArrayList<>();
    RecyclerView rvItem;
    LinearLayout llManage, llReport, llLog, llSubordinate, llBackLog;
    ImageView imgHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_log_calender_dashboard);
        initView();
        ONCLICK();

    }

    private void initView(){
        pref=new Pref(DailyLogCalenderDashboardActivity.this);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        imgHome.setOnClickListener(this);
        llManage = (LinearLayout) findViewById(R.id.llAttandanceManage);
        llReport = (LinearLayout) findViewById(R.id.llReport);
        llSubordinate = (LinearLayout) findViewById(R.id.llSubordinate);

        llLog = (LinearLayout) findViewById(R.id.llLog);
        llBackLog = (LinearLayout) findViewById(R.id.llBackLog);
        rvItem = (RecyclerView) findViewById(R.id.rvItem);
        rvItem.setLayoutManager(new GridLayoutManager(this, 3));
        y = Calendar.getInstance().get(Calendar.YEAR);
        dlMain = (DrawerLayout) findViewById(R.id.dlMain);
        imgMenu = (ImageView) findViewById(R.id.imgMenu);
        imgMenu.setOnClickListener(this);
        dlMain.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {

            }

            @Override
            public void onDrawerOpened(@NonNull View view) {
                mslideState = true;

            }

            @Override
            public void onDrawerClosed(@NonNull View view) {
                mslideState = false;

            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });
        Calendar calendar = Calendar.getInstance();
        int month=calendar.get(Calendar.MONTH)+1;
        if (month==1){
            Month="January";
        }else if (month==2){
            Month="February";
        }else if (month==3){
            Month="March";
        }else if (month==4){
            Month="April";
        }else if (month==5){
            Month="May";
        }else if (month==6){
            Month="June";
        }else if (month==7){
            Month="July";
        }else if (month==8){
            Month="August";
        }else if (month==9){
            Month="September";
        }else if (month==10){
            Month="October";
        }else if (month==11){
            Month="November";
        }else if (month==12){
            Month="December";
        }

        monthList.add("January");
        monthList.add("February");
        monthList.add("March");
        monthList.add("April");
        monthList.add("May");
        monthList.add("June");
        monthList.add("July");
        monthList.add("August");
        monthList.add("September");
        monthList.add("October");
        monthList.add("November");
        monthList.add("December");

        mmonthList.add(new SpinnerModel("January","1"));
        mmonthList.add(new SpinnerModel("January","2"));
        mmonthList.add(new SpinnerModel("January","3"));
        mmonthList.add(new SpinnerModel("January","4"));
        mmonthList.add(new SpinnerModel("January","5"));
        mmonthList.add(new SpinnerModel("January","6"));
        mmonthList.add(new SpinnerModel("January","7"));
        mmonthList.add(new SpinnerModel("January","8"));
        mmonthList.add(new SpinnerModel("January","9"));
        mmonthList.add(new SpinnerModel("January","10"));
        mmonthList.add(new SpinnerModel("January","11"));
        mmonthList.add(new SpinnerModel("January","12"));

        spMonth=(Spinner) findViewById(R.id.spMonth);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (DailyLogCalenderDashboardActivity.this, android.R.layout.simple_spinner_item,
                        monthList); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMonth.setAdapter(spinnerArrayAdapter);
        int index = monthList.indexOf(Month);
        Log.d("indexr", String.valueOf(index));
        spMonth.setSelection(index);

        llManage.setOnClickListener(this);
        llLog.setOnClickListener(this);
        llBackLog.setOnClickListener(this);
        llReport.setOnClickListener(this);
        llSubordinate.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == imgMenu) {
            dlMain.openDrawer(Gravity.LEFT);
        }else if (view==llManage){
            Intent intent = new Intent(DailyLogCalenderDashboardActivity.this, VisitLocationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if (view==llLog){
            Intent intent = new Intent(DailyLogCalenderDashboardActivity.this, NumberTourActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if (view==llSubordinate){
            Intent intent = new Intent(DailyLogCalenderDashboardActivity.this, SuperVisiorActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if (view==llBackLog){
            Intent intent = new Intent(DailyLogCalenderDashboardActivity.this, BacklogActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if (view==llReport){
            Intent intent = new Intent(DailyLogCalenderDashboardActivity.this, AttendanceReportActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if (view==imgHome){
            Intent intent = new Intent(DailyLogCalenderDashboardActivity.this, UserDashBoardActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void ONCLICK(){
        spMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String monthID=mmonthList.get(i).getItemId();
                getAttendanceList(monthID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getAttendanceList(String month) {
        final ProgressDialog pd = new ProgressDialog(DailyLogCalenderDashboardActivity.this);
        pd.setMessage("Loading...");
        pd.show();
        pd.setCancelable(false);
        String surl =   "https://cloud.geniusconsultant.com/GHRMSApi/api/get_EmployeeAttendanceReportAPP?&AEMEmployeeId="+pref.getEmpId()+"&Year="+y+"&Month="+month+"&SecurityCode="+pref.getSecurityCode();
        Log.d("input", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        pd.dismiss();
                        itemList.clear();
                        // attendabceInfiList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String Date = obj.optString("Date");
                                    String PunchTiming = obj.optString("Punchtime");
                                    String Day = obj.optString("Day");
                                    String Status = obj.optString("Status");

                                    AttendanceCalenderModel obj2 = new AttendanceCalenderModel();
                                    obj2.setDate(Date);
                                    obj2.setStatus(Status);
                                    obj2.setTime(PunchTiming);
                                    obj2.setDay(Day);
                                    itemList.add(obj2);


                                }

                                setAdapter();


                            } else {

                                //   Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();

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
        RequestQueue requestQueue = Volley.newRequestQueue(DailyLogCalenderDashboardActivity.this);
        requestQueue.add(stringRequest);
    }

    private void setAdapter() {
        AttendanceCalenderAdapter attendanceAdapter = new AttendanceCalenderAdapter(itemList);
        rvItem.setAdapter(attendanceAdapter);
    }
}