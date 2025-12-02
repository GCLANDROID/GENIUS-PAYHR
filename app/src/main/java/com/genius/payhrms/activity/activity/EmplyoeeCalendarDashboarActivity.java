package com.genius.payhrms.activity.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.genius.payhrms.R;
import com.genius.payhrms.activity.adapter.NewMenuItemAdapter;
import com.genius.payhrms.activity.customcalender.CustomCalendar;
import com.genius.payhrms.activity.customcalender.OnDateSelectedListener;
import com.genius.payhrms.activity.customcalender.OnNavigationButtonClickedListener;
import com.genius.payhrms.activity.customcalender.Property;
import com.genius.payhrms.activity.model.AttendanceCalenderModel;
import com.genius.payhrms.activity.model.MenuItemModel;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.Pref;
import com.genius.payhrms.activity.utility.SecurityCode;
import com.genius.payhrms.activity.utility.Util;
import com.genius.payhrms.databinding.ActivityEmplyoeeCalendarDashboarBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EmplyoeeCalendarDashboarActivity extends AppCompatActivity implements OnNavigationButtonClickedListener {
    private static final String TAG = "EmplyoeeCalendarDashboa";
    ActivityEmplyoeeCalendarDashboarBinding binding;
    Pref pref;
    String greeting;
    RecyclerView rvItem;
    boolean mslideState;
    ArrayList<String>presentDays=new ArrayList<>();
    ArrayList<String>halfday=new ArrayList<>();
    ArrayList<String>halfdayleave=new ArrayList<>();
    ArrayList<String>dateList=new ArrayList<>();
    ArrayList<AttendanceCalenderModel> itemList = new ArrayList<>();
    int presentDayCount = 0,absentDayCount = 0, onLeaveCount = 0;
    JSONArray attendanceArray;
    int date;
    int y,m;
    NewMenuItemAdapter itemAdapter;
    ArrayList<MenuItemModel>menuitemList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_emplyoee_calendar_dashboar);
        initView();
    }


    private void initView() {
        pref = new Pref(EmplyoeeCalendarDashboarActivity.this);

        y = Calendar.getInstance().get(Calendar.YEAR);
        m = Calendar.getInstance().get(Calendar.MONTH) + 1;

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) {

            greeting = "Good Morning";

        } else if (timeOfDay >= 12 && timeOfDay < 16) {

            greeting = "Good Afternoon";

        } else if (timeOfDay >= 16 && timeOfDay < 21) {

            greeting = "Good Evening";


        } else if (timeOfDay >= 21 && timeOfDay < 24) {


            greeting = "Good Evening";


        }


        binding.tvEmpName.setText(greeting + " " + pref.getEmpName());
        rvItem=(RecyclerView)findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(EmplyoeeCalendarDashboarActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);


        binding.dlMain.addDrawerListener(new DrawerLayout.DrawerListener() {
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

        binding.imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding. dlMain.openDrawer(Gravity.LEFT);
            }
        });

        binding.imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EmplyoeeCalendarDashboarActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


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

        binding.customCalendar.setMapDescToProp(descHashMap);
        binding.customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.PREVIOUS, this);
        binding.customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.NEXT, this);

        binding.customCalendar.setOnDateSelectedListener(new OnDateSelectedListener() {
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
                String Status = object.optString("Status").toUpperCase();

                if (Status.equalsIgnoreCase("")) {
                    binding.lnStatus.setVisibility(View.VISIBLE);
                    binding.lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#B6B6B6")));
                    binding.tvDetails.setText(date+" : Advance attendance is not available" );
                    binding.tvDetails.setTextColor(Color.parseColor("#F2FFFFFF"));
                    binding.tvOK.setTextColor(Color.parseColor("#F2FFFFFF"));
                } else if (Status.equalsIgnoreCase("P")) {
                    binding.lnStatus.setVisibility(View.VISIBLE);
                    binding.lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F20BCA03")));
                    binding.tvDetails.setText(date + " : " + PunchTiming );
                    binding.tvDetails.setTextColor(Color.parseColor("#F2FFFFFF"));
                    binding.tvOK.setTextColor(Color.parseColor("#F2FFFFFF"));

                } else if (Status.equalsIgnoreCase("A")) {
                    binding.lnStatus.setVisibility(View.VISIBLE);
                    binding.lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F2FA0209")));
                    binding.tvDetails.setText(date + " : "+ PunchTiming + "Absent" );
                    binding.tvDetails.setTextColor(Color.parseColor("#F2FFFFFF"));
                    binding.tvOK.setTextColor(Color.parseColor("#F2FFFFFF"));

                } else if (Status.equalsIgnoreCase("L")) {
                    binding.lnStatus.setVisibility(View.VISIBLE);
                    binding.lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F2DD7C03")));
                    binding.tvDetails.setText(date + " : " + PunchTiming  );
                    binding.tvDetails.setTextColor(Color.parseColor("#F2FFFFFF"));
                    binding.tvOK.setTextColor(Color.parseColor("#F2FFFFFF"));

                } else if (Status.equalsIgnoreCase("HDL")) {
                    binding.lnStatus.setVisibility(View.VISIBLE);
                    binding.lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#C09A72")));
                    binding.tvDetails.setText(date + " : " + PunchTiming );
                    binding.tvDetails.setTextColor(Color.parseColor("#F2FFFFFF"));
                    binding.tvOK.setTextColor(Color.parseColor("#F2FFFFFF"));

                } else if (Status.equalsIgnoreCase("H")) {
                    binding.lnStatus.setVisibility(View.VISIBLE);
                    binding.lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFED45")));
                    binding.tvDetails.setText(date + " : "+PunchTiming + " - Holiday" );
                    binding.tvDetails.setTextColor(Color.parseColor("#340885"));
                    binding.tvOK.setTextColor(Color.parseColor("#340885"));

                } else if (Status.equalsIgnoreCase("WO")) {
                    binding.lnStatus.setVisibility(View.VISIBLE);
                    binding.lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1589FF")));
                    binding.tvDetails.setText(date + " : "+PunchTiming + " Weekly Off" );
                    binding.tvDetails.setTextColor(Color.parseColor("#F2FFFFFF"));
                    binding.tvOK.setTextColor(Color.parseColor("#F2FFFFFF"));

                } else if (Status.equalsIgnoreCase("WC")) {
                    binding.lnStatus.setVisibility(View.VISIBLE);
                    binding.lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F20C5EDA")));
                    binding.tvDetails.setText(date + " : " + PunchTiming );
                    binding.tvDetails.setTextColor(Color.parseColor("#F2FFFFFF"));
                    binding.tvOK.setTextColor(Color.parseColor("#F2FFFFFF"));

                } else if (Status.equalsIgnoreCase("M")) {
                    binding.lnStatus.setVisibility(View.VISIBLE);
                    binding.lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#AC4782")));
                    binding.tvDetails.setText(date + " : "+ PunchTiming + " Missed Punch" );
                    binding.tvDetails.setTextColor(Color.parseColor("#F2FFFFFF"));
                    binding.tvOK.setTextColor(Color.parseColor("#F2FFFFFF"));

                } else if (Status.equalsIgnoreCase("HD")) {
                    binding.lnStatus.setVisibility(View.VISIBLE);
                    binding.lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFCA61")));
                    binding.tvDetails.setText(date + " : "+PunchTiming + " - Half Day" );
                    binding.tvDetails.setTextColor(Color.parseColor("#340885"));
                    binding.tvOK.setTextColor(Color.parseColor("#340885"));

                } else if (Status.equalsIgnoreCase("UL")) {
                    binding.lnStatus.setVisibility(View.VISIBLE);
                    binding.lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#DE2852")));
                    binding.tvDetails.setText(date + " : "+PunchTiming + "Un Approved Leave" );
                    binding.tvDetails.setTextColor(Color.parseColor("#F2FFFFFF"));
                    binding.tvOK.setTextColor(Color.parseColor("#F2FFFFFF"));

                } else if (Status.equalsIgnoreCase("C")) {
                    binding.lnStatus.setVisibility(View.VISIBLE);
                    binding.lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#010360")));
                    binding.tvDetails.setText(date + " : "+PunchTiming );
                    binding.tvDetails.setTextColor(Color.parseColor("#F2FFFFFF"));
                    binding.tvOK.setTextColor(Color.parseColor("#F2FFFFFF"));
                } else {
                    binding.lnStatus.setVisibility(View.VISIBLE);
                    binding.lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#AFAFAF")));
                    binding.tvDetails.setText("Advance Attendance is not available" );
                    binding.tvDetails.setTextColor(Color.parseColor("#F2FFFFFF"));
                    binding.tvOK.setTextColor(Color.parseColor("#F2FFFFFF"));
                }
            }
        });
    }


    private void currentcalendar(JSONObject jsonObject) {

        presentDays=new ArrayList<>();
        halfday=new ArrayList<>();
        halfdayleave=new ArrayList<>();
        dateList=new ArrayList<>();
        final HashMap<Integer, Object> dateHashmap = new HashMap<>();

        // initialize calendar
        final Calendar calendar = Calendar.getInstance();

        final ProgressDialog pd = new ProgressDialog(EmplyoeeCalendarDashboarActivity.this);
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
                                    dateHashmap.put(date, Status);
                                }

                                binding.customCalendar.setDate(calendar, dateHashmap);

                                float halfdaycount=halfday.size();
                                float hdlcount=halfdayleave.size();

                                float halfdayCount=halfdaycount/2;
                                float halfdayleavecount=hdlcount/2;

                                float totalcount=presentDays.size()+halfdayCount+halfdayleavecount;

                                binding.tvPresent.setText(""+totalcount);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            // boolean _status = job1.getBoolean("status");


                            // do anything with response
                        }else {

                        }

                        JSONObject jsonObject=new JSONObject();
                        try {
                            jsonObject.put("SecurityCode",pref.getSecurityCode());
                            menu(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();

                        if (error.getErrorCode()==401){
                            Intent intent=new Intent(EmplyoeeCalendarDashboarActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }


                    }
                });
    }
    private void currentcalendarForNav(JSONObject jsonObject,final Calendar calendar) {

        presentDays=new ArrayList<>();
        halfday=new ArrayList<>();
        halfdayleave=new ArrayList<>();
        dateList=new ArrayList<>();
        final HashMap<Integer, Object> dateHashmap = new HashMap<>();

        // initialize calendar



        final ProgressDialog pd = new ProgressDialog(EmplyoeeCalendarDashboarActivity.this);
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

                                binding.customCalendar.setDate(calendar, dateHashmap);

                                float halfdaycount=halfday.size();
                                float hdlcount=halfdayleave.size();

                                float halfdayCount=halfdaycount/2;
                                float halfdayleavecount=hdlcount/2;

                                float totalcount=presentDays.size()+halfdayCount+halfdayleavecount;

                                binding.tvPresent.setText(""+totalcount);


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
                            Intent intent=new Intent(EmplyoeeCalendarDashboarActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }


                    }
                });
    }

    @Override
    public Map<Integer, Object>[] onNavigationButtonClicked(int whichButton, Calendar newMonth) {
        Map<Integer, Object>[] arr = new Map[2];
        arr[0] = new HashMap<>();


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


    private void menu(JSONObject jsonObject) {
        final ProgressDialog progressDialog=new ProgressDialog(EmplyoeeCalendarDashboarActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading");
        progressDialog.show();


        AndroidNetworking.post(Api.sMenuapi)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        menuitemList.clear();
                        progressDialog.dismiss();
                        JSONObject job1 = response;


                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                            int attendanceRegularIndex = -1;
                            int changePwdIndex = -1;
                            int leaveAppIndex = -1;
                            JSONArray responseData = job1.optJSONArray("Response_Data");
                            for (int i = 0; i < responseData.length(); i++) {
                                JSONObject obj = responseData.optJSONObject(i);
                                String MenuItemName = obj.optString("MenuItemName");
                                int MenuItemId = obj.optInt("MenuItemId");
                                if (MenuItemName.equalsIgnoreCase("Attendance Regularization")) {
                                    attendanceRegularIndex = i;
                                } else if (MenuItemName.equalsIgnoreCase("Change Password")) {
                                    changePwdIndex = i;
                                } else if(MenuItemName.equalsIgnoreCase("Leave Application")){
                                    leaveAppIndex = i;
                                }
                                MenuItemModel obj2 = new MenuItemModel(MenuItemName,MenuItemId);
                                menuitemList.add(obj2);
                            }


                            try {
                                if (attendanceRegularIndex != -1 && leaveAppIndex !=-1){
                                    MenuItemModel menu = menuitemList.get(attendanceRegularIndex);
                                    menuitemList.remove(attendanceRegularIndex);
                                    menuitemList.add(leaveAppIndex,menu);
                                } else {
                                    if (attendanceRegularIndex != -1 && changePwdIndex != -1) {
                                        Collections.swap(menuitemList, attendanceRegularIndex, changePwdIndex);
                                        Log.d("MenuSwap", "Swapped Attendance Regularization and Change Password");
                                    } else {
                                        Log.d("MenuSwap", "One or both items not found — no swap performed");
                                    }
                                }
                                /*if (pref.getSecurityCode().equals(SecurityCode.IFB_Travel_System)){
                                    MenuItemModel menuTeamReport = new MenuItemModel("Team Report",600);
                                    menuitemList.add(changePwdIndex,menuTeamReport);
                                }*/
                            } catch (Exception e){
                                e.printStackTrace();
                            }

                            setAdapter();
                            // boolean _status = job1.getBoolean("status");
                            // do anything with response
                            if (pref.getSecurityCode().equals(SecurityCode.IFB_Travel_System)){
                                JSONObject object=new JSONObject();
                                try {
                                    object.put("AEMEmployeeID",pref.getEmpId());
                                    object.put("CompanyID",pref.getEmpClintId());
                                    object.put("SecurityCode",pref.getSecurityCode());
                                    getApproverOrNot(object,changePwdIndex);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }else {
                            Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        progressDialog.dismiss();

                    }
                });
    }


    private void getApproverOrNot(JSONObject object, int changePwdIndex) {
        final ProgressDialog pd = new ProgressDialog(EmplyoeeCalendarDashboarActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(true);
        pd.show();

        AndroidNetworking.post(Api.sapprovercheckapi)
                .addJSONObjectBody(object)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "getApproverOrNot2: "+response.toString());
                        pd.dismiss();

                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);

                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            if (pref.getSecurityCode().equals(SecurityCode.IFB_Travel_System)){
                                MenuItemModel menuTeamReport = new MenuItemModel("Team Report",600);
                                menuitemList.add(changePwdIndex,menuTeamReport);
                            }
                            itemAdapter.notifyDataSetChanged();
                        }else {

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "onError: "+anError);
                        pd.dismiss();
                    }
                });

    }

    private void setAdapter(){

        itemAdapter = new NewMenuItemAdapter(menuitemList, EmplyoeeCalendarDashboarActivity.this);
        rvItem.setAdapter(itemAdapter);

    }
}