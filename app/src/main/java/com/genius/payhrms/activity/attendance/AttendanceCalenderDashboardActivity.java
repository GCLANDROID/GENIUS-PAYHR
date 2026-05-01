package com.genius.payhrms.activity.attendance;

import static com.genius.payhrms.activity.activity.UserDashBoardActivity.isAppMinimizeDashboard;
import static com.genius.payhrms.activity.utility.Util.SECRET_KEY;
import static com.genius.payhrms.activity.utility.Util.encrypt;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import com.genius.payhrms.R;
import com.genius.payhrms.activity.activity.DistanceCalculationActivity;
import com.genius.payhrms.activity.activity.UserDashBoardActivity;
import com.genius.payhrms.activity.adapter.AttendanceCalenderAdapter;
import com.genius.payhrms.activity.attendance.tour.TourActivity;
import com.genius.payhrms.activity.customcalender.CustomCalendar;
import com.genius.payhrms.activity.customcalender.OnDateSelectedListener;
import com.genius.payhrms.activity.customcalender.OnNavigationButtonClickedListener;
import com.genius.payhrms.activity.customcalender.Property;
import com.genius.payhrms.activity.dailylog.NumberTourActivity;
import com.genius.payhrms.activity.dailylog.QRAttendanceDashboardActivity;
import com.genius.payhrms.activity.dailylog.QRCodeScannerActivity;
import com.genius.payhrms.activity.leaveapplication.OtherLeavesActivity;
import com.genius.payhrms.activity.model.AttendanceCalenderModel;
import com.genius.payhrms.activity.model.SpinnerModel;
import com.genius.payhrms.activity.reciver.DailylogSyncReciever;
import com.genius.payhrms.activity.reciver.NetworkStateChecker;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.Pref;
import com.genius.payhrms.activity.utility.SecurityCode;
import com.genius.payhrms.activity.utility.Util;


import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AttendanceCalenderDashboardActivity extends AppCompatActivity implements View.OnClickListener , OnNavigationButtonClickedListener {
    private static final String TAG = "ACD";
    private static final org.apache.commons.logging.Log log = LogFactory.getLog(AttendanceCalenderDashboardActivity.class);
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
    LinearLayout llManage, llReport, llLog, llSubordinate, llBackLog,llQRCode,llAdjustment,llTour,llDistanceCalculation;
    ImageView imgHome;
    boolean approver;
    NetworkStateChecker airplaneModeChangeReceiver = new NetworkStateChecker();
    DailylogSyncReciever dailyLogReciever = new DailylogSyncReciever();

    CustomCalendar customCalendar;
    JSONArray attendanceArray;
    Button btnLeave;
    ArrayList<String>presentDays=new ArrayList<>();
    ArrayList<String> wowDays =new ArrayList<>();
    ArrayList<String>dateList=new ArrayList<>();
    TextView tvPresent;
    LinearLayout lnStatus;
    TextView tvDetails,tvOK;
    String attCode,formattedDate;
    ArrayList<String>halfday=new ArrayList<>();
    ArrayList<String>halfdayleave=new ArrayList<>();
    LinearLayout llFace;
    AlertDialog alerDialog1;
    int date;

    int presentDayCount = 0,absentDayCount = 0, onLeaveCount = 0;

    String address = "";
    public static boolean isAppMinimizeAttendance = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_log_calender_dashboard);
        initView();
        ONCLICK();
    }

    private void initView(){
        llFace=(LinearLayout)findViewById(R.id.llFace);
        pref=new Pref(AttendanceCalenderDashboardActivity.this);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        formattedDate = df.format(c);

        JSONObject object=new JSONObject();
        try {
            object.put("AEMEmployeeID",pref.getEmpId());
            object.put("CompanyID",pref.getEmpClintId());
            object.put("SecurityCode",pref.getSecurityCode());
            approverCheck(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        imgHome=(ImageView)findViewById(R.id.imgHome);
        imgHome.setOnClickListener(this);
        llManage = (LinearLayout) findViewById(R.id.llAttandanceManage);
        llReport = (LinearLayout) findViewById(R.id.llReport);
        llSubordinate = (LinearLayout) findViewById(R.id.llSubordinate);
        llQRCode = (LinearLayout) findViewById(R.id.llQRCode);
        llDistanceCalculation = (LinearLayout) findViewById(R.id.llDistanceCalculation);
        if (pref.getSecurityCode().equals("1000")){
            llQRCode.setVisibility(View.VISIBLE);
        }else {
            llQRCode.setVisibility(View.GONE);
        }

        llAdjustment=(LinearLayout)findViewById(R.id.llAdjustment);
        llTour=(LinearLayout) findViewById(R.id.llTour);

        if (pref.getSecurityCode().equals("1186")){
            llAdjustment.setVisibility(View.VISIBLE);
            llTour.setVisibility(View.VISIBLE);

        }else {
            llAdjustment.setVisibility(View.GONE);
            llTour.setVisibility(View.GONE);
        }

        if (pref.getSecurityCode().equals(SecurityCode.CFLO_WORLD) || pref.getSecurityCode().equals(SecurityCode.DOCTOR_SAND )){
            llDistanceCalculation.setVisibility(View.VISIBLE);
        } else{
            llDistanceCalculation.setVisibility(View.GONE);
        }


        llLog = (LinearLayout) findViewById(R.id.llLog);
        llLog.setVisibility(View.GONE);
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
                (AttendanceCalenderDashboardActivity.this, android.R.layout.simple_spinner_item,
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
        llTour.setOnClickListener(this);
        llAdjustment.setOnClickListener(this);
        llDistanceCalculation.setOnClickListener(this);



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



        Property hcProperty = new Property();
        hcProperty.layoutResource = R.layout.wc_view;
        hcProperty.dateTextViewResource = R.id.text_view;
        descHashMap.put("HC", hcProperty);

        Property unapprovedleaveProperty = new Property();
        unapprovedleaveProperty.layoutResource = R.layout.ul_view;
        unapprovedleaveProperty.dateTextViewResource = R.id.text_view;
        descHashMap.put("UL", unapprovedleaveProperty);

        Property approperty = new Property();
        approperty.layoutResource = R.layout.approval_pending_view;
        approperty.dateTextViewResource = R.id.text_view;
        descHashMap.put("AP", approperty);

        Property hdproperty = new Property();
        hdproperty.layoutResource = R.layout.hd_view;
        hdproperty.dateTextViewResource = R.id.text_view;
        descHashMap.put("HD", hdproperty);

        Property missedproperty = new Property();
        missedproperty.layoutResource = R.layout.missed_punch_view;
        missedproperty.dateTextViewResource = R.id.text_view;
        descHashMap.put("M", missedproperty);


        Property wowproperty = new Property();
        wowproperty.layoutResource = R.layout.wow_view;
        wowproperty.dateTextViewResource = R.id.text_view;
        descHashMap.put("WOW", wowproperty);

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
                //Log.e("CAL", "onDateSelected: "+selectedDate.get() );
                String sDate=selectedDate.get(Calendar.DAY_OF_MONTH)
                        +"/" +(selectedDate.get(Calendar.MONTH)+1)
                        +"/" + selectedDate.get(Calendar.YEAR);

                String date=Util.changeAnyDateFormat(sDate,"dd/MM/yyyy","dd-MM-yy");
                int pos =dateList.indexOf(date);
                Log.d("position", String.valueOf(pos));
                JSONObject object=attendanceArray.optJSONObject(pos);
                String PunchTiming = object.optString("Punchtime");
                String Status = object.optString("Status").trim().toUpperCase();

                if (Status.equalsIgnoreCase("")) {
                    lnStatus.setVisibility(View.VISIBLE);
                    lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#B6B6B6")));
                    tvDetails.setText(date+" : Advance attendance is not available" );
                    tvDetails.setTextColor(Color.parseColor("#F2FFFFFF"));
                    tvOK.setTextColor(Color.parseColor("#F2FFFFFF"));
                } else if (Status.equalsIgnoreCase("P")) {
                    lnStatus.setVisibility(View.VISIBLE);
                    lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F20BCA03")));
                    tvDetails.setText(date + " : " + PunchTiming );
                    tvDetails.setTextColor(Color.parseColor("#F2FFFFFF"));
                    tvOK.setTextColor(Color.parseColor("#F2FFFFFF"));

                } else if (Status.equalsIgnoreCase("A")) {
                    lnStatus.setVisibility(View.VISIBLE);
                    lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F2FA0209")));
                    tvDetails.setText(date + " : "+ PunchTiming + "Absent" );
                    tvDetails.setTextColor(Color.parseColor("#F2FFFFFF"));
                    tvOK.setTextColor(Color.parseColor("#F2FFFFFF"));

                } else if (Status.equalsIgnoreCase("L")) {
                    lnStatus.setVisibility(View.VISIBLE);
                    lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F2DD7C03")));
                    tvDetails.setText(date + " : " + PunchTiming  );
                    tvDetails.setTextColor(Color.parseColor("#F2FFFFFF"));
                    tvOK.setTextColor(Color.parseColor("#F2FFFFFF"));

                } else if (Status.equalsIgnoreCase("HDL")) {
                    lnStatus.setVisibility(View.VISIBLE);
                    lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#C09A72")));
                    tvDetails.setText(date + " : " + PunchTiming );
                    tvDetails.setTextColor(Color.parseColor("#F2FFFFFF"));
                    tvOK.setTextColor(Color.parseColor("#F2FFFFFF"));

                } else if (Status.equalsIgnoreCase("H")) {
                    lnStatus.setVisibility(View.VISIBLE);
                    lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFED45")));
                    tvDetails.setText(date + " : "+PunchTiming + " - Holiday" );
                    tvDetails.setTextColor(Color.parseColor("#340885"));
                    tvOK.setTextColor(Color.parseColor("#340885"));

                } else if (Status.equalsIgnoreCase("WO")) {
                    lnStatus.setVisibility(View.VISIBLE);
                    lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1589FF")));
                    tvDetails.setText(date + " : "+PunchTiming + " Weekly Off" );
                    tvDetails.setTextColor(Color.parseColor("#F2FFFFFF"));
                    tvOK.setTextColor(Color.parseColor("#F2FFFFFF"));

                } else if (Status.equalsIgnoreCase("WC")) {
                    lnStatus.setVisibility(View.VISIBLE);
                    lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#59945C")));
                    tvDetails.setText(date + " : " + PunchTiming );
                    tvDetails.setTextColor(Color.parseColor("#F2FFFFFF"));
                    tvOK.setTextColor(Color.parseColor("#F2FFFFFF"));

                }else if (Status.equalsIgnoreCase("HC")) {
                    lnStatus.setVisibility(View.VISIBLE);
                    lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#59945C")));
                    tvDetails.setText(date + " : " + PunchTiming );
                    tvDetails.setTextColor(Color.parseColor("#F2FFFFFF"));
                    tvOK.setTextColor(Color.parseColor("#F2FFFFFF"));

                } else if (Status.equalsIgnoreCase("M")) {
                    lnStatus.setVisibility(View.VISIBLE);
                    lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#AC4782")));
                    tvDetails.setText(date + " : "+ PunchTiming + " Missed Punch" );
                    tvDetails.setTextColor(Color.parseColor("#F2FFFFFF"));
                    tvOK.setTextColor(Color.parseColor("#F2FFFFFF"));

                } else if (Status.equalsIgnoreCase("HD")) {
                    lnStatus.setVisibility(View.VISIBLE);
                    lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFCA61")));
                    tvDetails.setText(date + " : "+PunchTiming + " - Half Day" );
                    tvDetails.setTextColor(Color.parseColor("#340885"));
                    tvOK.setTextColor(Color.parseColor("#340885"));

                } else if (Status.equalsIgnoreCase("UL")) {
                    lnStatus.setVisibility(View.VISIBLE);
                    lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#DE2852")));
                    tvDetails.setText(date + " : "+PunchTiming + "Un Approved Leave" );
                    tvDetails.setTextColor(Color.parseColor("#F2FFFFFF"));
                    tvOK.setTextColor(Color.parseColor("#F2FFFFFF"));

                } else if (Status.equalsIgnoreCase("AP")){
                    lnStatus.setVisibility(View.VISIBLE);
                    lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F2FF89CC")));
                    tvDetails.setText(date + " : "+PunchTiming+" Attendance Approval Pending" );
                    tvDetails.setTextColor(Color.parseColor("#F2FFFFFF"));
                    tvOK.setTextColor(Color.parseColor("#F2FFFFFF"));
                } else if (Status.equalsIgnoreCase("C")) {
                    lnStatus.setVisibility(View.VISIBLE);
                    lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#010360")));
                    tvDetails.setText(date + " : "+PunchTiming );
                    tvDetails.setTextColor(Color.parseColor("#F2FFFFFF"));
                    tvOK.setTextColor(Color.parseColor("#F2FFFFFF"));
                }else if (Status.equalsIgnoreCase("WOW")) {
                    lnStatus.setVisibility(View.VISIBLE);
                    lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#05A761")));
                    tvDetails.setText(date + " : "+PunchTiming+" -Week off Work date" );
                    tvDetails.setTextColor(Color.parseColor("#F2FFFFFF"));
                    tvOK.setTextColor(Color.parseColor("#F2FFFFFF"));
                } else {
                    lnStatus.setVisibility(View.VISIBLE);
                    lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#AFAFAF")));
                    tvDetails.setText("Advance Attendance is not available" );
                    tvDetails.setTextColor(Color.parseColor("#F2FFFFFF"));
                    tvOK.setTextColor(Color.parseColor("#F2FFFFFF"));
                }
            }
        });
        tvOK.setOnClickListener(this);
        llFace.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == imgMenu) {
            dlMain.openDrawer(Gravity.LEFT);
        }else if (view==llManage) {
            isAppMinimizeAttendance = true;
            attenDanceIntent();
        }else if (view==llLog){
            isAppMinimizeAttendance = true;
            Intent intent = new Intent(AttendanceCalenderDashboardActivity.this, NumberTourActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if (view==llSubordinate){
            isAppMinimizeAttendance = true;
            Intent intent = new Intent(AttendanceCalenderDashboardActivity.this, SuperVisiorActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (view==llBackLog) {
            isAppMinimizeAttendance = true;
            Intent intent = new Intent(AttendanceCalenderDashboardActivity.this, BacklogActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (view==llReport) {
            isAppMinimizeAttendance = true;
            attenDanceReportIntent();
        } else if (view==imgHome) {
            isAppMinimizeDashboard = false;
            Intent intent = new Intent(AttendanceCalenderDashboardActivity.this, UserDashBoardActivity.class);
            startActivity(intent);
            finish();
        } else if (view==llQRCode){

            if (approver) {
                Intent intent = new Intent(AttendanceCalenderDashboardActivity.this, QRAttendanceDashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Intent intent = new Intent(AttendanceCalenderDashboardActivity.this, QRCodeScannerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        } else if (view==tvOK) {
            lnStatus.setVisibility(View.GONE);
        } else if (view==llFace) {
            faceAlert();
        }else if (view==llAdjustment){
            Intent intent = new Intent(AttendanceCalenderDashboardActivity.this, OtherLeavesActivity.class);
            startActivity(intent);
            finish();
        }else if (view==llTour){
            Intent intent = new Intent(AttendanceCalenderDashboardActivity.this, TourActivity.class);
            startActivity(intent);
            finish();
        }else if(view == llDistanceCalculation){
            Intent intent = new Intent(AttendanceCalenderDashboardActivity.this, DistanceCalculationActivity.class);
            startActivity(intent);
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

    private void setAdapter() {
        AttendanceCalenderAdapter attendanceAdapter = new AttendanceCalenderAdapter(itemList);
        rvItem.setAdapter(attendanceAdapter);
    }


    private void approverCheck(JSONObject jsonObject) {
        final ProgressDialog pd=new ProgressDialog(AttendanceCalenderDashboardActivity.this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(Api.sapprovercheckapi)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        pd.dismiss();
                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);


                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                            String responseData = job1.optString("Response_Data");
                            try {
                                JSONArray jsonArray = new JSONArray(responseData);
                                if (responseData.length()>0) {

                                    llSubordinate.setVisibility(View.VISIBLE);
                                } else {
                                    llSubordinate.setVisibility(View.GONE);
                                    // llShow.setVisibility(View.GONE);
                                }

                                JSONObject object=new JSONObject();
                                try {
                                    object.put("AEMConsultantID",pref.getEmpConId());
                                    object.put("AEMClientID",pref.getEmpClintId());
                                    object.put("AEMClientOfficeID",pref.getEmpClintOffId());
                                    object.put("AEMEmployeeID",pref.getEmpId());
                                    object.put("CurrentPage",0);
                                    object.put("AID",1);
                                    object.put("ApproverStatus",4);
                                    object.put("YearVal",y);
                                    object.put("MonthName",m);
                                    object.put("WorkingStatus",1);
                                    object.put("DbOperation",1);
                                    object.put("SecurityCode",pref.getSecurityCode());
                                    attendanceReport(object);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }





                            // boolean _status = job1.getBoolean("status");


                            // do anything with response
                        }else {

                        }
                    }

                    @Override
                    public void onError(ANError error) {

                        pd.dismiss();


                    }
                });
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
        isAppMinimizeAttendance = false;
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("AEMEmployeeId",pref.getEmpId());
            jsonObject.put("Year",y);
            jsonObject.put("Month",m);
            jsonObject.put("SecurityCode",pref.getSecurityCode());
            currentcalendar(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause: isAppMinimizeAttendance: "+isAppMinimizeAttendance+" isAppMinimizeDashboard: "+isAppMinimizeDashboard);

    }

    @Override
    public Map<Integer, Object>[] onNavigationButtonClicked(int whichButton, Calendar newMonth) {
        Map<Integer, Object>[] arr = new Map[2];
        arr[0] = new HashMap<>();
        Log.e(TAG, "MONTH: "+newMonth.get(Calendar.MONTH));
        Log.e(TAG, "YEAR: "+newMonth.get(Calendar.YEAR));
        switch(newMonth.get(Calendar.MONTH)) {
            case Calendar.JANUARY:
                Calendar calendar=Calendar.getInstance();
                calendar.set(newMonth.get(Calendar.YEAR),0,1);

                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.put("AEMEmployeeId",pref.getEmpId());
                    jsonObject.put("Year",newMonth.get(Calendar.YEAR));
                    jsonObject.put("Month",1);
                    jsonObject.put("SecurityCode",pref.getSecurityCode());
                    currentcalendarForNav(jsonObject,calendar);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Calendar.FEBRUARY:
                Calendar calendar1=Calendar.getInstance();
                calendar1.set(newMonth.get(Calendar.YEAR),1,1);


                JSONObject jsonObject1=new JSONObject();
                try {
                    jsonObject1.put("AEMEmployeeId",pref.getEmpId());
                    jsonObject1.put("Year",newMonth.get(Calendar.YEAR));
                    jsonObject1.put("Month",2);
                    jsonObject1.put("SecurityCode",pref.getSecurityCode());
                    currentcalendarForNav(jsonObject1,calendar1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case Calendar.MARCH:
                Calendar calendar2=Calendar.getInstance();
                calendar2.set(newMonth.get(Calendar.YEAR),2,1);



                JSONObject jsonObject2=new JSONObject();
                try {
                    jsonObject2.put("AEMEmployeeId",pref.getEmpId());
                    jsonObject2.put("Year",newMonth.get(Calendar.YEAR));
                    jsonObject2.put("Month",3);
                    jsonObject2.put("SecurityCode",pref.getSecurityCode());
                    currentcalendarForNav(jsonObject2,calendar2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case  Calendar.APRIL:
                Calendar calendar3=Calendar.getInstance();
                calendar3.set(newMonth.get(Calendar.YEAR),3,1);



                JSONObject jsonObject3=new JSONObject();
                try {
                    jsonObject3.put("AEMEmployeeId",pref.getEmpId());
                    jsonObject3.put("Year",newMonth.get(Calendar.YEAR));
                    jsonObject3.put("Month",4);
                    jsonObject3.put("SecurityCode",pref.getSecurityCode());
                    currentcalendarForNav(jsonObject3,calendar3);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Calendar.MAY:
                Calendar calendar4=Calendar.getInstance();
                calendar4.set(newMonth.get(Calendar.YEAR),4,1);


                JSONObject jsonObject4=new JSONObject();
                try {
                    jsonObject4.put("AEMEmployeeId",pref.getEmpId());
                    jsonObject4.put("Year",newMonth.get(Calendar.YEAR));
                    jsonObject4.put("Month",5);
                    jsonObject4.put("SecurityCode",pref.getSecurityCode());
                    currentcalendarForNav(jsonObject4,calendar4);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Calendar.JUNE:
                Calendar calendar5=Calendar.getInstance();
                calendar5.set(newMonth.get(Calendar.YEAR),5,1);


                JSONObject jsonObject5=new JSONObject();
                try {
                    jsonObject5.put("AEMEmployeeId",pref.getEmpId());
                    jsonObject5.put("Year",newMonth.get(Calendar.YEAR));
                    jsonObject5.put("Month",6);
                    jsonObject5.put("SecurityCode",pref.getSecurityCode());
                    currentcalendarForNav(jsonObject5,calendar5);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Calendar.JULY:
                Calendar calendar6=Calendar.getInstance();
                calendar6.set(newMonth.get(Calendar.YEAR),6,1);



                JSONObject jsonObject6=new JSONObject();
                try {
                    jsonObject6.put("AEMEmployeeId",pref.getEmpId());
                    jsonObject6.put("Year",newMonth.get(Calendar.YEAR));
                    jsonObject6.put("Month",7);
                    jsonObject6.put("SecurityCode",pref.getSecurityCode());
                    currentcalendarForNav(jsonObject6,calendar6);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Calendar.AUGUST:
                Calendar calendar7=Calendar.getInstance();
                calendar7.set(newMonth.get(Calendar.YEAR),7,1);



                JSONObject jsonObject7=new JSONObject();
                try {
                    jsonObject7.put("AEMEmployeeId",pref.getEmpId());
                    jsonObject7.put("Year",newMonth.get(Calendar.YEAR));
                    jsonObject7.put("Month",8);
                    jsonObject7.put("SecurityCode",pref.getSecurityCode());
                    currentcalendarForNav(jsonObject7,calendar7);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Calendar.SEPTEMBER:
                Calendar calendar8=Calendar.getInstance();
                calendar8.set(newMonth.get(Calendar.YEAR),8,1);



                JSONObject jsonObject8=new JSONObject();
                try {
                    jsonObject8.put("AEMEmployeeId",pref.getEmpId());
                    jsonObject8.put("Year",newMonth.get(Calendar.YEAR));
                    jsonObject8.put("Month",9);
                    jsonObject8.put("SecurityCode",pref.getSecurityCode());
                    currentcalendarForNav(jsonObject8,calendar8);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Calendar.OCTOBER:
                Calendar calendar9=Calendar.getInstance();
                calendar9.set(newMonth.get(Calendar.YEAR),9,1);

                JSONObject jsonObject9=new JSONObject();
                try {
                    jsonObject9.put("AEMEmployeeId",pref.getEmpId());
                    jsonObject9.put("Year",newMonth.get(Calendar.YEAR));
                    jsonObject9.put("Month",10);
                    jsonObject9.put("SecurityCode",pref.getSecurityCode());
                    currentcalendarForNav(jsonObject9,calendar9);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Calendar.NOVEMBER:
                Calendar calendar10=Calendar.getInstance();
                calendar10.set(newMonth.get(Calendar.YEAR),10,1);

                JSONObject jsonObject10=new JSONObject();
                try {
                    jsonObject10.put("AEMEmployeeId",pref.getEmpId());
                    jsonObject10.put("Year",newMonth.get(Calendar.YEAR));
                    jsonObject10.put("Month",11);
                    jsonObject10.put("SecurityCode",pref.getSecurityCode());
                    currentcalendarForNav(jsonObject10,calendar10);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Calendar.DECEMBER:
                Calendar calendar11=Calendar.getInstance();
                Log.e(TAG, "onNavigationButtonClicked: "+calendar11);
                calendar11.set(newMonth.get(Calendar.YEAR),11,1);


                JSONObject jsonObject11=new JSONObject();
                try {
                    jsonObject11.put("AEMEmployeeId",pref.getEmpId());
                    jsonObject11.put("Year",newMonth.get(Calendar.YEAR));
                    jsonObject11.put("Month",12);
                    jsonObject11.put("SecurityCode",pref.getSecurityCode());
                    currentcalendarForNav(jsonObject11,calendar11);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }


        return arr;
    }


    private void attenDanceIntent() {
        if (pref.getSecurityCode().equals("11") || pref.getSecurityCode().equals("123")) {
            Intent intent = new Intent(AttendanceCalenderDashboardActivity.this, AttendanceManageForPPSActivity.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (pref.getSecurityCode().equals("1135")) {

        }else if (pref.getSecurityCode().equals("1153")) {
           // getAttendanceInformationForSmart();
        } else {
            Log.e(TAG, "attenDanceIntent: AttendanceMarkActivity");
            Intent intent = new Intent(AttendanceCalenderDashboardActivity.this, AttendanceMarkActivity.class);
            intent.putExtra("address",address);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }



    /*private void getAttendanceInformationForSmart() {
        Log.d("Arpan", "arpan");
        final ProgressDialog progressDialog = new ProgressDialog(AttendanceCalenderDashboardActivity.this);
        progressDialog.setMessage("Loading..");
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

                           *//* Intent intent = new Intent(AttendanceActivity.this, SmartJuleDailyLogActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("attCode", attCode);
                            startActivity(intent);
*//*
                            Intent intent = new Intent(AttendanceCalenderDashboardActivity.this, AttendanceManageActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
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
        RequestQueue requestQueue = Volley.newRequestQueue(AttendanceCalenderDashboardActivity.this);
        requestQueue.add(stringRequest);
    }*/

    private void attenDanceReportIntent() {
        if (pref.getSecurityCode().equals("11") || pref.getSecurityCode().equals("123")) {
            Intent intent = new Intent(AttendanceCalenderDashboardActivity.this, AttendanceReportForPPSActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Intent intent = new Intent(AttendanceCalenderDashboardActivity.this, AttendanceReportActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }


    }

    private void faceAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AttendanceCalenderDashboardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_face, null);
        dialogBuilder.setView(dialogView);


        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();


            }
        });

        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }

    private void currentcalendar(JSONObject jsonObject) {
        Log.e(TAG, "currentcalendar: "+jsonObject.toString());
        presentDays=new ArrayList<>();
        halfday=new ArrayList<>();
        halfdayleave=new ArrayList<>();
        dateList=new ArrayList<>();
        wowDays = new ArrayList<>();
        final HashMap<Integer, Object> dateHashmap = new HashMap<>();

        // initialize calendar
        final Calendar calendar = Calendar.getInstance();

        final ProgressDialog pd = new ProgressDialog(AttendanceCalenderDashboardActivity.this);
        pd.setMessage("Loading...");
        pd.show();
        pd.setCancelable(false);
        AndroidNetworking.post(Api.sCalendarapi)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        pd.dismiss();
                        itemList.clear();
                        presentDayCount = 0;
                        absentDayCount = 0;
                        onLeaveCount = 0;
                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                            String responseData = job1.optString("Response_Data");
                            try {
                                JSONArray jsonArray=new JSONArray(responseData);
                                attendanceArray=jsonArray;
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.optJSONObject(i);
                                    String sDate = obj.optString("Date");
                                    String Date = Util.changeAnyDateFormat(obj.optString("Date"), "dd-MM-yy", "dd");
                                    try {
                                        date = Integer.parseInt(Date);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

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

                                    if (Status.equalsIgnoreCase("WOW")){
                                        wowDays.add(Day);
                                    }
                                    dateHashmap.put(date, Status);
                                }

                                customCalendar.setDate(calendar, dateHashmap);

                                float halfdaycount=halfday.size();
                                float hdlcount=halfdayleave.size();

                                float halfdayCount=halfdaycount/2;
                                float halfdayleavecount=hdlcount/2;

                                float totalcount=0;

                                if (pref.getSecurityCode().equals(SecurityCode.Shyamoly_Paribahan)){
                                    totalcount=presentDays.size()+halfdayCount+halfdayleavecount+ wowDays.size();
                                } else {
                                    totalcount=presentDays.size()+halfdayCount+halfdayleavecount;
                                }

                                tvPresent.setText(""+totalcount);
                                setAdapter();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            // boolean _status = job1.getBoolean("status");


                            // do anything with response
                        }else {

                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();

                        if (error.getErrorCode()==401){
                            JSONObject obj=new JSONObject();
                            try {
                                obj.put("MasterID",encrypt(pref.getMasterId(),SECRET_KEY));
                                obj.put("Password",encrypt(pref.getPassword(),SECRET_KEY));
                                obj.put("IMEI","0");
                                obj.put("DeviceID","0");
                                obj.put("DeviceType","A");
                                obj.put("SecurityCode",pref.getSecurityCode());
                                login(obj);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }
                });
    }
    private void currentcalendarForNav(JSONObject jsonObject,final Calendar calendar) {

        presentDays=new ArrayList<>();
        halfday=new ArrayList<>();
        halfdayleave=new ArrayList<>();
        dateList=new ArrayList<>();
        wowDays = new ArrayList<>();
        final HashMap<Integer, Object> dateHashmap = new HashMap<>();

        // initialize calendar



        final ProgressDialog pd = new ProgressDialog(AttendanceCalenderDashboardActivity.this);
        pd.setMessage("Loading...");
        pd.show();
        pd.setCancelable(false);
        AndroidNetworking.post(Api.sCalendarapi)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {


                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        pd.dismiss();
                        itemList.clear();

                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                            String responseData = job1.optString("Response_Data");
                            try {
                                JSONArray jsonArray=new JSONArray(responseData);
                                attendanceArray=jsonArray;
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.optJSONObject(i);
                                    String sDate = obj.optString("Date");
                                    String Date = Util.changeAnyDateFormat(obj.optString("Date"), "dd-MM-yy", "dd");
                                    try {
                                        date = Integer.parseInt(Date);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

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

                                    if (Status.equalsIgnoreCase("WOW")){
                                        wowDays.add(Day);
                                    }

                                   /*if (Status.equalsIgnoreCase("P")){
                                        presentDays.add(Day);
                                        presentDayCount++;
                                    }

                                    if (Status.equalsIgnoreCase("A")){
                                        presentDays.add(Day);
                                        absentDayCount++;
                                    }*/


                                    dateHashmap.put(date, Status);

                                }

                                customCalendar.setDate(calendar, dateHashmap);

                                float halfdaycount=halfday.size();
                                float hdlcount=halfdayleave.size();

                                float halfdayCount=halfdaycount/2;
                                float halfdayleavecount=hdlcount/2;

                                float totalcount = 0;

                                if (pref.getSecurityCode().equals(SecurityCode.Shyamoly_Paribahan)){
                                    totalcount=presentDays.size()+halfdayCount+halfdayleavecount+ wowDays.size();
                                } else {
                                    totalcount=presentDays.size()+halfdayCount+halfdayleavecount;
                                }

                                tvPresent.setText(""+totalcount);
                                setAdapter();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            // boolean _status = job1.getBoolean("status");


                            // do anything with response
                        }else {

                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                        if (error.getErrorCode()==401){
                            JSONObject obj=new JSONObject();
                            try {
                                obj.put("MasterID",encrypt(pref.getMasterId(),SECRET_KEY));
                                obj.put("Password",encrypt(pref.getPassword(),SECRET_KEY));
                                obj.put("IMEI","0");
                                obj.put("DeviceID","0");
                                obj.put("DeviceType","A");
                                obj.put("SecurityCode",pref.getSecurityCode());
                                login(obj);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }
                });
    }

    private void login(JSONObject jsonObject) {

        final ProgressDialog pd = new ProgressDialog(AttendanceCalenderDashboardActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(Api.sLoginapi)
                .addJSONObjectBody(jsonObject)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {


                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        pd.dismiss();

                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                            JSONArray responseData = job1.optJSONArray("Response_Data");
                            for (int i = 0; i < responseData.length(); i++) {
                                JSONObject obj = responseData.optJSONObject(i);
                                String Genius_Access_Token=obj.optString("Genius_Access_Token");
                                pref.saveAccessToken(Genius_Access_Token);

                                // boolean _status = job1.getBoolean("status");


                                JSONObject jsonObject=new JSONObject();
                                try {
                                    jsonObject.put("AEMEmployeeId",pref.getEmpId());
                                    jsonObject.put("Year",y);
                                    jsonObject.put("Month",m);
                                    jsonObject.put("SecurityCode",pref.getSecurityCode());
                                    currentcalendar(jsonObject);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                // do anything with response
                            }
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();


                    }
                });
    }


    private void attendanceReport(final JSONObject jsonObject) {
        Log.e(TAG, "attendanceReport: "+jsonObject.toString());
        /*llLoder.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);
        llAgain.setVisibility(View.GONE);*/
        final ProgressDialog pd=new ProgressDialog(AttendanceCalenderDashboardActivity.this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        AndroidNetworking.post(Api.sAttendanceReportapi)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        pd.dismiss();
                        JSONObject job1 = response;
                        //attendabceInfiList=new JSONArray();
                        Log.e(TAG, "AttendanceReportapi: " + job1);


                        int Response_Code = job1.optInt("Response_Code");
                        String Response_Message=job1.optString("Response_Message");
                        if (Response_Code == 101) {
                            // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                            String responseData = job1.optString("Response_Data");
                            try {
                                JSONArray jsonArray=new JSONArray(responseData);
                                /*attendabceInfiList=jsonArray;

                                attendanceAdapter = new AttendanceAdapter(AttendanceReportActivity.this,attendabceInfiList,1);
                                rvAttendanceReport.setAdapter(attendanceAdapter);
                                llLoder.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNodata.setVisibility(View.GONE);
                                llAgain.setVisibility(View.GONE);*/
                              /*  if (){

                                }*/

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    if (!jsonObject1.getString("EmpInAddress").equals("--")){
                                        address = jsonObject1.getString("EmpInAddress");
                                        break;
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            // boolean _status = job1.getBoolean("status");
                            // do anything with response
                        }else {
                            /*llLoder.setVisibility(View.GONE);
                            llMain.setVisibility(View.VISIBLE);
                            llNodata.setVisibility(View.VISIBLE);
                            llAgain.setVisibility(View.GONE);*/
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                    }
                });
    }


    @Override
    public void onBackPressed() {
        isAppMinimizeDashboard = false;
        super.onBackPressed();
    }
}