package com.genius.payhrms.activity.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.genius.payhrms.R;
import com.genius.payhrms.activity.adapter.DistanceDetailsAdapter;
import com.genius.payhrms.activity.dailylog.Dayco.DailyLogAttendaneDayco;
import com.genius.payhrms.activity.model.DistanceModel;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.Pref;
import com.genius.payhrms.activity.utility.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DistanceCalculationActivity extends AppCompatActivity {
    private static final String TAG = "DistanceCalculationActi";
    Pref pref;
    TextView tvTotalDistance;
    RecyclerView rvDistance;
    ArrayList<DistanceModel> distanceList;
    String inputDate;
    TextView tvSelectedDate;
    Button btnShow;
    ConstraintLayout clMain;
    LinearLayout llNoData,llLoading;
    ImageView imgBack,imgHome;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance_calculation);
        initView();
        btnClick();
    }



    private void initView() {
        pref = new Pref(this);
        tvTotalDistance = findViewById(R.id.tvTotalDistance);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        rvDistance = findViewById(R.id.rvDistance);
        rvDistance = findViewById(R.id.rvDistance);
        llNoData = findViewById(R.id.llNoData);
        llLoading = findViewById(R.id.llLoading);
        btnShow = findViewById(R.id.btnShow);
        imgBack = findViewById(R.id.imgBack);
        imgHome = findViewById(R.id.imgHome);
        clMain = findViewById(R.id.clMain);
        rvDistance.setLayoutManager(new LinearLayoutManager(this));

        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Month starts from 0
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        inputDate = year+"-"+month+"-"+day;
        tvSelectedDate.setText(Util.changeAnyDateFormat(inputDate,"yyyy-M-d","d-MMM-yyyy"));
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("AEMEmployeeID",pref.getEmpId());
            jsonObject.put("StartDate",inputDate);
            jsonObject.put("EndDate",inputDate);
            jsonObject.put("SecurityCode",pref.getSecurityCode());
            getEmployeeDistance(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void btnClick() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DistanceCalculationActivity.this,UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
        tvSelectedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker();
            }
        });
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.put("AEMEmployeeID",pref.getEmpId());
                    jsonObject.put("StartDate",inputDate);
                    jsonObject.put("EndDate",inputDate);
                    jsonObject.put("SecurityCode",pref.getSecurityCode());
                    getEmployeeDistance(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void getEmployeeDistance(JSONObject jsonObject) {
        llNoData.setVisibility(View.GONE);
        llLoading.setVisibility(View.VISIBLE);
        clMain.setVisibility(View.GONE);
        AndroidNetworking.post(Api.GetEmployeeDistance)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "Employee_Distance: "+response.toString(4) );
                            JSONObject job1 = response;
                            int Response_Code = job1.optInt("Response_Code");
                            if (Response_Code == 101) {
                                JSONObject responseData = job1.optJSONObject("Response_Data");
                                JSONArray DistanceDetails = responseData.optJSONArray("DistanceDetails");
                                JSONArray TotalDistance = responseData.optJSONArray("TotalDistance");
                                llNoData.setVisibility(View.GONE);
                                llLoading.setVisibility(View.GONE);
                                clMain.setVisibility(View.VISIBLE);
                                distanceList = new ArrayList<>();
                                if(TotalDistance != null){
                                    if (TotalDistance.length() > 0){
                                        for (int i = 0; i < TotalDistance.length(); i++) {
                                            JSONObject TotalDistanceOBJ = TotalDistance.getJSONObject(i);
                                            tvTotalDistance.setText(TotalDistanceOBJ.optString("TotalDistanceKM"));
                                        }
                                    }
                                }
                                if(DistanceDetails != null){
                                    if(DistanceDetails.length() > 0){
                                        for (int i = 0; i < DistanceDetails.length(); i++) {
                                            JSONObject DistanceDetailsOBJ = DistanceDetails.getJSONObject(i);
                                            String EmployeeID = DistanceDetailsOBJ.getString("EmployeeID");
                                            String EmployeeName = DistanceDetailsOBJ.getString("EmployeeName");
                                            String Attendnacedate = DistanceDetailsOBJ.getString("Attendnacedate");
                                            String Address = DistanceDetailsOBJ.getString("Address");
                                            String DistanceInKM = String.valueOf(DistanceDetailsOBJ.getDouble("DistanceInKM"));
                                            DistanceModel distanceModel = new DistanceModel(EmployeeID,EmployeeName,Attendnacedate,Address,DistanceInKM);
                                            distanceList.add(distanceModel);
                                        }
                                        DistanceDetailsAdapter distanceDetailsAdapter = new DistanceDetailsAdapter(DistanceCalculationActivity.this,distanceList);
                                        rvDistance.setAdapter(distanceDetailsAdapter);
                                    } else {
                                        llNoData.setVisibility(View.VISIBLE);
                                        llLoading.setVisibility(View.GONE);
                                        clMain.setVisibility(View.GONE);
                                    }
                                } else {
                                    llNoData.setVisibility(View.VISIBLE);
                                    llLoading.setVisibility(View.GONE);
                                    clMain.setVisibility(View.GONE);
                                }
                            } else {
                                llNoData.setVisibility(View.VISIBLE);
                                llLoading.setVisibility(View.GONE);
                                clMain.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "Employee_Distance_error: "+anError.getErrorBody());
                        llNoData.setVisibility(View.VISIBLE);
                        llLoading.setVisibility(View.GONE);
                        clMain.setVisibility(View.GONE);
                    }
                });
    }

    private void DatePicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(DistanceCalculationActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        inputDate = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                        Log.e(TAG, "onDateSet: "+inputDate);
                        tvSelectedDate.setText(Util.changeAnyDateFormat(inputDate,"yyyy-M-d","d-MMM-yyyy"));
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker();
        datePickerDialog.show();
    }
}
