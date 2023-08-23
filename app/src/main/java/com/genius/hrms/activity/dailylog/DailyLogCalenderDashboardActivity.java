package com.genius.hrms.activity.dailylog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.genius.hrms.activity.reciver.DailylogSyncReciever;
import com.genius.hrms.activity.reciver.NetworkStateChecker;
import com.genius.hrms.activity.utility.Pref;
import com.genius.hrms.activity.utility.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.naishadhparmar.zcustomcalendar.CustomCalendar;
import org.naishadhparmar.zcustomcalendar.OnDateSelectedListener;
import org.naishadhparmar.zcustomcalendar.OnNavigationButtonClickedListener;
import org.naishadhparmar.zcustomcalendar.Property;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DailyLogCalenderDashboardActivity extends AppCompatActivity implements View.OnClickListener , OnNavigationButtonClickedListener {
    Spinner spMonth;
    ArrayList<String>monthList=new ArrayList<>();
    ArrayList<SpinnerModel>mmonthList=new ArrayList<>();
    DrawerLayout dlMain;
    boolean mslideState;
    ImageView imgMenu;
    String Month;
    int y,m;
    Pref pref;
    ArrayList<AttendanceCalenderModel> itemList = new ArrayList<>();
    RecyclerView rvItem;
    LinearLayout llManage, llReport, llLog, llSubordinate, llBackLog,llQRCode;
    ImageView imgHome;
    boolean approver;
    NetworkStateChecker airplaneModeChangeReceiver = new NetworkStateChecker();
    DailylogSyncReciever dailyLogReciever = new DailylogSyncReciever();

    CustomCalendar customCalendar;
    JSONArray attendanceArray;
    Button btnLeave;
    ArrayList<String>presentDays=new ArrayList<>();
    ArrayList<String>dateList=new ArrayList<>();
    ArrayList<String>halfday=new ArrayList<>();
    ArrayList<String>halfdayleave=new ArrayList<>();
    TextView tvPresent;
    LinearLayout lnStatus;
    TextView tvDetails,tvOK;
    LinearLayout llFace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_log_calender_dashboard);
        initView();

        ONCLICK();

    }

    private void initView(){
        pref=new Pref(DailyLogCalenderDashboardActivity.this);
        llFace=(LinearLayout)findViewById(R.id.llFace);
        getApproverOrNot();
        imgHome=(ImageView)findViewById(R.id.imgHome);
        imgHome.setOnClickListener(this);
        llManage = (LinearLayout) findViewById(R.id.llAttandanceManage);
        llReport = (LinearLayout) findViewById(R.id.llReport);
        llSubordinate = (LinearLayout) findViewById(R.id.llSubordinate);
        llQRCode = (LinearLayout) findViewById(R.id.llQRCode);
        if (pref.getSecurityCode().equals("1000")||pref.getSecurityCode().equals("1160")){
            llQRCode.setVisibility(View.VISIBLE);
        }else {
            llQRCode.setVisibility(View.GONE);
        }


        llLog = (LinearLayout) findViewById(R.id.llLog);
        llBackLog = (LinearLayout) findViewById(R.id.llBackLog);
        rvItem = (RecyclerView) findViewById(R.id.rvItem);
        rvItem.setLayoutManager(new GridLayoutManager(this, 3));
        y = Calendar.getInstance().get(Calendar.YEAR);
        m = Calendar.getInstance().get(Calendar.MONTH) + 1;
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
        llQRCode.setOnClickListener(this);


        customCalendar = findViewById(R.id.custom_calendar);

        // Initialize description hashmap
        HashMap<Object, Property> descHashMap = new HashMap<>();

        // Initialize default property
        Property defaultProperty = new Property();

        // Initialize default resource
        defaultProperty.layoutResource = R.layout.default_view;

        // Initialize and assign variable
        defaultProperty.dateTextViewResource = R.id.text_view;

        // Put object and property
        descHashMap.put("default", defaultProperty);

        // for current date
        Property currentProperty = new Property();
        currentProperty.layoutResource = R.layout.wo_view;
        currentProperty.dateTextViewResource = R.id.text_view;
        descHashMap.put("WO", currentProperty);

        // for present date
        Property presentProperty = new Property();
        presentProperty.layoutResource = R.layout.present_view;
        presentProperty.dateTextViewResource = R.id.text_view;
        descHashMap.put("P", presentProperty);

        // For absent
        Property absentProperty = new Property();
        absentProperty.layoutResource = R.layout.absent_view;
        absentProperty.dateTextViewResource = R.id.text_view;
        descHashMap.put("A", absentProperty);

        //holiday
        Property holidayProperty = new Property();
        holidayProperty.layoutResource = R.layout.holiday_view;
        holidayProperty.dateTextViewResource = R.id.text_view;
        descHashMap.put("H", holidayProperty);

        //leave
        Property leaveProperty = new Property();
        leaveProperty.layoutResource = R.layout.leave_view;
        leaveProperty.dateTextViewResource = R.id.text_view;
        descHashMap.put("L", leaveProperty);
        tvPresent=(TextView)findViewById(R.id.tvPresent);

        Property hdlProperty = new Property();
        hdlProperty.layoutResource = R.layout.hdl_view;
        hdlProperty.dateTextViewResource = R.id.text_view;
        descHashMap.put("HDL", hdlProperty);


        Property wcProperty = new Property();
        wcProperty.layoutResource = R.layout.wc_view;
        wcProperty.dateTextViewResource = R.id.text_view;
        descHashMap.put("WC", wcProperty);


        Property unapprovedleaveProperty = new Property();
        unapprovedleaveProperty.layoutResource = R.layout.ul_view;
        unapprovedleaveProperty.dateTextViewResource = R.id.text_view;
        descHashMap.put("UL", unapprovedleaveProperty);


        Property hdproperty = new Property();
        hdproperty.layoutResource = R.layout.hd_view;
        hdproperty.dateTextViewResource = R.id.text_view;
        descHashMap.put("HD", hdproperty);

        Property missedproperty = new Property();
        missedproperty.layoutResource = R.layout.missed_punch_view;
        missedproperty.dateTextViewResource = R.id.text_view;
        descHashMap.put("M", missedproperty);

        Property current = new Property();
        current.layoutResource = R.layout.current_view;
        current.dateTextViewResource = R.id.text_view;
        descHashMap.put("C", current);





        customCalendar.setMapDescToProp(descHashMap);
        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.PREVIOUS, this);
        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.NEXT, this);
        lnStatus=(LinearLayout) findViewById(R.id.lnStatus);
        tvDetails=(TextView)findViewById(R.id.tvDetails);
        tvOK=(TextView)findViewById(R.id.tvOK);

        customCalendar.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(View view, Calendar selectedDate, Object desc) {
                String sDate=selectedDate.get(Calendar.DAY_OF_MONTH)
                        +"/" +(selectedDate.get(Calendar.MONTH)+1)
                        +"/" + selectedDate.get(Calendar.YEAR);


                String date=Util.changeAnyDateFormat(sDate,"dd/MM/yyyy","dd MMM yy");
                int pos =dateList.indexOf(date);
                Log.d("position", String.valueOf(pos));
                JSONObject object=attendanceArray.optJSONObject(pos);
                String PunchTiming = object.optString("Punchtime");
                String Status = object.optString("Status").toUpperCase();


                if (Status.equalsIgnoreCase("")) {
                    lnStatus.setVisibility(View.VISIBLE);
                    lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#B6B6B6")));
                    tvDetails.setText(date+" : Advance attendance is not available" );
                    tvDetails.setTextColor(Color.parseColor("#F2FFFFFF"));
                    tvOK.setTextColor(Color.parseColor("#F2FFFFFF"));
                }else if (Status.equalsIgnoreCase("P")){
                    lnStatus.setVisibility(View.VISIBLE);
                    lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F20BCA03")));
                    tvDetails.setText(date + " : " + PunchTiming );
                    tvDetails.setTextColor(Color.parseColor("#F2FFFFFF"));
                    tvOK.setTextColor(Color.parseColor("#F2FFFFFF"));


                }else if (Status.equalsIgnoreCase("A")){
                    lnStatus.setVisibility(View.VISIBLE);
                    lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F2FA0209")));
                    tvDetails.setText(date + " : "+ PunchTiming + "Absent" );
                    tvDetails.setTextColor(Color.parseColor("#F2FFFFFF"));
                    tvOK.setTextColor(Color.parseColor("#F2FFFFFF"));

                }else if (Status.equalsIgnoreCase("L")){
                    lnStatus.setVisibility(View.VISIBLE);
                    lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F2DD7C03")));
                    tvDetails.setText(date + " : " + PunchTiming  );
                    tvDetails.setTextColor(Color.parseColor("#F2FFFFFF"));
                    tvOK.setTextColor(Color.parseColor("#F2FFFFFF"));

                }else if (Status.equalsIgnoreCase("HDL")){
                    lnStatus.setVisibility(View.VISIBLE);
                    lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#C09A72")));
                    tvDetails.setText(date + " : " + PunchTiming );
                    tvDetails.setTextColor(Color.parseColor("#F2FFFFFF"));
                    tvOK.setTextColor(Color.parseColor("#F2FFFFFF"));

                }else if (Status.equalsIgnoreCase("H")){
                    lnStatus.setVisibility(View.VISIBLE);
                    lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFED45")));
                    tvDetails.setText(date + " : "+PunchTiming + " - Holiday" );
                    tvDetails.setTextColor(Color.parseColor("#340885"));
                    tvOK.setTextColor(Color.parseColor("#340885"));
                }else if (Status.equalsIgnoreCase("WO")){
                    lnStatus.setVisibility(View.VISIBLE);
                    lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1589FF")));
                    tvDetails.setText(date + " : "+PunchTiming + " Weekly Off" );
                    tvDetails.setTextColor(Color.parseColor("#F2FFFFFF"));
                    tvOK.setTextColor(Color.parseColor("#F2FFFFFF"));

                }else if (Status.equalsIgnoreCase("WC")){
                    lnStatus.setVisibility(View.VISIBLE);
                    lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F20C5EDA")));
                    tvDetails.setText(date + " : " + PunchTiming );
                    tvDetails.setTextColor(Color.parseColor("#F2FFFFFF"));
                    tvOK.setTextColor(Color.parseColor("#F2FFFFFF"));

                }else if (Status.equalsIgnoreCase("M")){
                    lnStatus.setVisibility(View.VISIBLE);
                    lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#AC4782")));
                    tvDetails.setText(date + " : "+ PunchTiming + " Missed Punch" );
                    tvDetails.setTextColor(Color.parseColor("#F2FFFFFF"));
                    tvOK.setTextColor(Color.parseColor("#F2FFFFFF"));

                }else if (Status.equalsIgnoreCase("HD")){
                    lnStatus.setVisibility(View.VISIBLE);
                    lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFCA61")));
                    tvDetails.setText(date + " : "+PunchTiming + " - Half Day" );
                    tvDetails.setTextColor(Color.parseColor("#340885"));
                    tvOK.setTextColor(Color.parseColor("#340885"));

                }else if (Status.equalsIgnoreCase("UL")){
                    lnStatus.setVisibility(View.VISIBLE);
                    lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#DE2852")));
                    tvDetails.setText(date + " : "+PunchTiming + "Un Approved Leave" );
                    tvDetails.setTextColor(Color.parseColor("#F2FFFFFF"));
                    tvOK.setTextColor(Color.parseColor("#F2FFFFFF"));

                }else if (Status.equalsIgnoreCase("C")){
                    lnStatus.setVisibility(View.VISIBLE);
                    lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#010360")));
                    tvDetails.setText(date + " : "+PunchTiming );
                    tvDetails.setTextColor(Color.parseColor("#F2FFFFFF"));
                    tvOK.setTextColor(Color.parseColor("#F2FFFFFF"));

                }else {
                    lnStatus.setVisibility(View.VISIBLE);
                    lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#AFAFAF")));
                    tvDetails.setText("Advance Attendance is not available" );
                    tvDetails.setTextColor(Color.parseColor("#F2FFFFFF"));
                    tvOK.setTextColor(Color.parseColor("#F2FFFFFF"));

                }
            }
        });

        tvOK.setOnClickListener(this);

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
        }else if (view==llQRCode){
            if (pref.getSecurityCode().equals("1160")){
                if (pref.getLoginID().equals("FSS0120") ||pref.getLoginID().equals("FSS0243") ||pref.getLoginID().equals("FSS0047")||pref.getLoginID().equals("FSS0163")||pref.getLoginID().equals("FSS0101") ){
                    Intent intent = new Intent(DailyLogCalenderDashboardActivity.this, QRAttendanceDashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(DailyLogCalenderDashboardActivity.this, QRCodeScannerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }else {
                if (approver) {
                    Intent intent = new Intent(DailyLogCalenderDashboardActivity.this, QRAttendanceDashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(DailyLogCalenderDashboardActivity.this, QRCodeScannerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

        }else if (view==tvOK){
            lnStatus.setVisibility(View.GONE);
        }
    }

    private void ONCLICK(){
        spMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String monthID=mmonthList.get(i).getItemId();
                //getAttendanceList(monthID);
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

    private void getApproverOrNot() {
        final ProgressDialog pd = new ProgressDialog(DailyLogCalenderDashboardActivity.this);
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

                                llSubordinate.setVisibility(View.VISIBLE);
                                approver=true;
                            } else {
                                llSubordinate.setVisibility(View.GONE);
                                approver=false;
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
        RequestQueue requestQueue = Volley.newRequestQueue(DailyLogCalenderDashboardActivity.this);
        requestQueue.add(stringRequest);

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

    @Override
    protected void onResume() {
        super.onResume();
        getAttendanceList(y, m);
    }

    private void getAttendanceList(int year, int month) {
        presentDays=new ArrayList<>();
        halfday=new ArrayList<>();
        halfdayleave=new ArrayList<>();
        dateList=new ArrayList<>();
        final HashMap<Integer, Object> dateHashmap = new HashMap<>();

        // initialize calendar
        final Calendar calendar = Calendar.getInstance();


        final ProgressDialog pd = new ProgressDialog(DailyLogCalenderDashboardActivity.this);
        pd.setMessage("Loading...");
        pd.show();
        pd.setCancelable(false);
        String surl = "https://cloud.geniusconsultant.com/GHRMSApi/api/get_EmployeeAttendanceReportAPP?&AEMEmployeeId="+pref.getEmpId()+"&Year="+year+"&Month="+month+"&SecurityCode="+pref.getSecurityCode();
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
                                attendanceArray=responseData;
                                for (int i = 0; i < responseData.length(); i++) {


                                    JSONObject obj = responseData.getJSONObject(i);
                                    String sDate = obj.optString("Date");
                                    String Date = Util.changeAnyDateFormat(obj.optString("Date"), "dd MMM yy", "dd");
                                    int date = Integer.parseInt(Date);
                                    String PunchTiming = obj.optString("PunchTiming");
                                    String Day = obj.optString("Day");
                                    String Status = obj.optString("Status");


                                    AttendanceCalenderModel obj2 = new AttendanceCalenderModel();
                                    obj2.setDate(Date);
                                    obj2.setStatus(Status);
                                    obj2.setTime(PunchTiming);
                                    obj2.setDay(Day);
                                    itemList.add(obj2);
                                    dateList.add(sDate);
                                    if (Status.equalsIgnoreCase("P")||Status.equalsIgnoreCase("WC")){
                                        presentDays.add(Day);
                                    }

                                    if (Status.equalsIgnoreCase("HD")){
                                        halfday.add(Day);
                                    }

                                    if (Status.equalsIgnoreCase("HDL")){
                                        halfdayleave.add(Day);
                                    }

                                    dateHashmap.put(date, Status);


                                }


                                customCalendar.setDate(calendar, dateHashmap);

                                float halfdaycount=halfday.size();
                                float hdlcount=halfdayleave.size();

                                float halfdayCount=halfdaycount/2;
                                float halfdayleavecount=hdlcount/2;

                                float totalcount=presentDays.size()+halfdayCount+halfdayleavecount;

                                tvPresent.setText(""+totalcount);
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

    private void getAttendanceListForNav(int year, int month, final Calendar calendar) {
        final HashMap<Integer, Object> dateHashmap = new HashMap<>();
        presentDays=new ArrayList<>();
        halfday=new ArrayList<>();
        halfdayleave=new ArrayList<>();
        dateList=new ArrayList<>();
        // initialize calendar



        final ProgressDialog pd = new ProgressDialog(DailyLogCalenderDashboardActivity.this);
        pd.setMessage("Loading...");
        pd.show();
        pd.setCancelable(false);
        String surl = "https://cloud.geniusconsultant.com/GHRMSApi/api/get_EmployeeAttendanceReportAPP?&AEMEmployeeId="+pref.getEmpId()+"&Year="+y+"&Month="+month+"&SecurityCode="+pref.getSecurityCode();
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
                                attendanceArray=responseData;
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String sDate = obj.optString("Date");
                                    String Date = Util.changeAnyDateFormat(obj.optString("Date"), "dd MMM yy", "dd");
                                    int date = Integer.parseInt(Date);
                                    String PunchTiming = obj.optString("PunchTiming");
                                    String Day = obj.optString("Day");
                                    String Status = obj.optString("Status");


                                    AttendanceCalenderModel obj2 = new AttendanceCalenderModel();
                                    obj2.setDate(Date);
                                    obj2.setStatus(Status);
                                    obj2.setTime(PunchTiming);
                                    obj2.setDay(Day);
                                    itemList.add(obj2);
                                    dateList.add(sDate);
                                    if (Status.equalsIgnoreCase("P")||Status.equalsIgnoreCase("WC")){
                                        presentDays.add(Day);
                                    }

                                    if (Status.equalsIgnoreCase("HD")){
                                        halfday.add(Day);
                                    }

                                    if (Status.equalsIgnoreCase("HDL")){
                                        halfdayleave.add(Day);
                                    }
                                    dateHashmap.put(date, Status);


                                }

                                customCalendar.setDate(calendar, dateHashmap);
                                float halfdaycount=halfday.size();
                                float hdlcount=halfdayleave.size();

                                float halfdayCount=halfdaycount/2;
                                float halfdayleavecount=hdlcount/2;

                                float totalcount=presentDays.size()+halfdayCount+halfdayleavecount;

                                tvPresent.setText(""+totalcount);

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

    @Override
    public Map<Integer, Object>[] onNavigationButtonClicked(int whichButton, Calendar newMonth) {
        Map<Integer, Object>[] arr = new Map[2];
        arr[0] = new HashMap<>();
        switch(newMonth.get(Calendar.MONTH)) {
            case Calendar.JANUARY:
                Calendar calendar=Calendar.getInstance();
                calendar.set(y,0,1);

                getAttendanceListForNav(y,1,calendar);



                break;
            case Calendar.FEBRUARY:
                Calendar calendar1=Calendar.getInstance();
                calendar1.set(y,1,1);

                getAttendanceListForNav(y,2,calendar1);

                break;
            case Calendar.MARCH:
                Calendar calendar2=Calendar.getInstance();
                calendar2.set(y,2,1);

                getAttendanceListForNav(y,3,calendar2);

                break;
            case  Calendar.APRIL:
                Calendar calendar3=Calendar.getInstance();
                calendar3.set(y,3,1);

                getAttendanceListForNav(y,4,calendar3);
                break;
            case Calendar.MAY:
                Calendar calendar4=Calendar.getInstance();
                calendar4.set(y,4,1);

                getAttendanceListForNav(y,5,calendar4);
                break;
            case Calendar.JUNE:
                Calendar calendar5=Calendar.getInstance();
                calendar5.set(y,5,1);

                getAttendanceListForNav(y,6,calendar5);
                break;
            case Calendar.JULY:
                Calendar calendar6=Calendar.getInstance();
                calendar6.set(y,6,1);

                getAttendanceListForNav(y,7,calendar6);
                break;
            case Calendar.AUGUST:
                Calendar calendar7=Calendar.getInstance();
                calendar7.set(y,7,1);

                getAttendanceListForNav(y,8,calendar7);
                break;
            case Calendar.SEPTEMBER:
                Calendar calendar8=Calendar.getInstance();
                calendar8.set(y,8,1);

                getAttendanceListForNav(y,9,calendar8);
                break;
            case Calendar.OCTOBER:
                Calendar calendar9=Calendar.getInstance();
                calendar9.set(y,9,1);

                getAttendanceListForNav(y,10,calendar9);
                break;
            case Calendar.NOVEMBER:
                Calendar calendar10=Calendar.getInstance();
                calendar10.set(y,10,1);

                getAttendanceListForNav(y,11,calendar10);
                break;
            case Calendar.DECEMBER:
                Calendar calendar11=Calendar.getInstance();
                calendar11.set(y,11,1);

                getAttendanceListForNav(y,12,calendar11);
                break;
        }


        return arr;
    }



}