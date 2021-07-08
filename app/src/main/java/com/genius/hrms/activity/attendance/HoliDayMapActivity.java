package com.genius.hrms.activity.attendance;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.bumptech.glide.load.engine.prefill.PreFillType;
import com.genius.hrms.R;
import com.genius.hrms.activity.activity.EmployeeDashBoardActivity;
import com.genius.hrms.activity.activity.UserDashBoardActivity;
import com.genius.hrms.activity.adapter.HolidayMapAdapter;
import com.genius.hrms.activity.model.HolidayMapModel;
import com.genius.hrms.activity.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HoliDayMapActivity extends AppCompatActivity {
    LinearLayout llMain,llLoader,llVisibility,llNoData;
    RecyclerView rvItem;
    ArrayList<HolidayMapModel>itemList=new ArrayList<>();
    Pref pref;
    String year;
    String holidayid,holidayDate,holidayName;
    String date;
    AlertDialog.Builder builder;;
    AlertDialog alerDialog1;
    TextView tvToolBar;
    ImageView imgBack,imgHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holi_day_map);
        initView();
        getItemList();
        onClick();
    }

    private void initView(){
        pref=new Pref(HoliDayMapActivity.this);
        llMain=(LinearLayout)findViewById(R.id.llMain);
        llLoader=(LinearLayout)findViewById(R.id.llLoader);
        llNoData=(LinearLayout)findViewById(R.id.llNoData);

        rvItem=(RecyclerView)findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(HoliDayMapActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);

        int y = Calendar.getInstance().get(Calendar.YEAR);
        year = String.valueOf(y);
        Log.d("year", year);
        builder = new AlertDialog.Builder(this);
        tvToolBar=(TextView)findViewById(R.id.tvToolBar);
        if (pref.getLanguage().equals("hi")){
            tvToolBar.setText("हॉलिडे मैपिंग");
        }else {
            tvToolBar.setText("Holiday Mapping");
        }
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);

    }


    private void getItemList() {
        Log.d("Arpan", "arpan");
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);

        String surl = pref.getIpAddress()+"GHRMSApi/api/HolidayMap/GetHoliday?CompanyID="+pref.getEmpClintId()+"&EmployeeID="+pref.getEmpId()+"&Year="+year+"&SecurityCode="+pref.getSecurityCode();
        Log.d("input", surl);
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
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String HolidayID = obj.optString("HolidayID");
                                    String HolidayName = obj.optString("HolidayName");
                                    String HolidayDate = obj.optString("HolidayDate");
                                    HolidayMapModel obj2 = new HolidayMapModel(HolidayID,HolidayName,HolidayDate);
                                    itemList.add(obj2);


                                }
                                setAdapter();

                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNoData.setVisibility(View.GONE);

                            } else {
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNoData.setVisibility(View.VISIBLE);
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
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);
                llNoData.setVisibility(View.GONE);
                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(HoliDayMapActivity.this);
        requestQueue.add(stringRequest);
    }

    private void setAdapter() {
       HolidayMapAdapter attendanceAdapter = new HolidayMapAdapter(itemList, HoliDayMapActivity.this);
        rvItem.setAdapter(attendanceAdapter);
    }


    public void updateStatus(int pos) {
       String  getDate=itemList.get(pos).getHolidayDate();
        String[] separate=getDate.split("/");
        String dDate=separate[0];
        String dMonth=separate[1];
        String dYear=separate[2];
        holidayDate=dMonth+"/"+dDate+"/"+dYear;
        holidayid=itemList.get(pos).getHolidayId();
        holidayName=itemList.get(pos).getHolidayName();

    }

    public void showStrtDatePicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(HoliDayMapActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {



                        int month = (monthOfYear + 1);
                        date = month + "/" + dayOfMonth + "/" + year;
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                        Date strDate = null;
                        try {
                            strDate = sdf.parse(holidayDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                        Date striDate = null;
                        try {
                            striDate = df.parse(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (striDate.getTime() > strDate.getTime()) {
                            alert();
                        }else {
                            Toast.makeText(HoliDayMapActivity.this, "You can not take holiday before original holiday date", Toast.LENGTH_SHORT).show();

                        }


                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();

    }

    private void alert(){
        builder.setMessage("Are you sure to map "+date+" against "+holidayName+" ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                       holidayMap();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();

                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("AlertDialog");
        alert.show();
    }

    private void holidayMap() {
        final ProgressDialog pd = new ProgressDialog(HoliDayMapActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(true);
        pd.show();

        String surl = pref.getIpAddress()+"ghrmsapi/api/HolidayMap/MapHoliday?CompanyID=" + pref.getEmpClintId() + "&EmployeeID=" + pref.getEmpId() + "&HolidayID=" + holidayid + "&HolidayDate=" + holidayDate + "&ReferralDate=" + date  + "&SecurityCode=" + pref.getSecurityCode();
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
                                   successAlert();



                            } else {

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
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


    private void successAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(HoliDayMapActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);

        tvInvalidDate.setText("You has been mapped "+date+ "against "+holidayDate);



        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                getItemList();
            }
        });

        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
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
                Intent intent=new Intent(HoliDayMapActivity.this, UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
