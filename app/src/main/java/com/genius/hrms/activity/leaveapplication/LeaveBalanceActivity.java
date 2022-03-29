package com.genius.hrms.activity.leaveapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genius.hrms.R;
import com.genius.hrms.activity.activity.UserDashBoardActivity;
import com.genius.hrms.activity.adapter.LeaveBalanceAdapter;
import com.genius.hrms.activity.model.HoliDayModel;
import com.genius.hrms.activity.model.LeaveBalanceModel;
import com.genius.hrms.activity.model.SpinnerModel;
import com.genius.hrms.activity.utility.NetworkConnectionCheck;
import com.genius.hrms.activity.utility.Pref;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class LeaveBalanceActivity extends AppCompatActivity {
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
    ArrayList<String> typeAvaild = new ArrayList<>();
    String applicantId;
    ArrayList<LeaveBalanceModel> itemList = new ArrayList<>();
    String typeAvailable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_balance);
        initialize();
        onClick();
        //getApproverOrNot();
        getLeaveAllDetails();
    }
    private void initialize(){
        pref=new Pref(LeaveBalanceActivity.this);
        connectionCheck=new NetworkConnectionCheck(LeaveBalanceActivity.this);
        rvHoliday=(RecyclerView)findViewById(R.id.rvHoliday);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(LeaveBalanceActivity.this, LinearLayoutManager.VERTICAL, false);
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
            tvToolBar.setText("बकाया छुट्टियां");
        }else {
            tvToolBar.setText("Leave Balance");
        }


    }

    private void onClick(){
        imgAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLeaveAllDetails();
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
                Intent intent=new Intent(LeaveBalanceActivity.this, UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private  void setAdapter(){
        LeaveBalanceAdapter lAdaapter = new LeaveBalanceAdapter(itemList, getApplicationContext());
        rvHoliday.setAdapter(lAdaapter);
    }
    private void getLeaveAllDetails() {
        //names.clear();
        llLoder.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);

        String surl = pref.getIpAddress() + "ghrmsapi/api/Leave/LeaveApplicationDetails?CompanyID=" + pref.getEmpClintId() + "&EmployeeID=" + pref.getEmpId() + "&ApproverID=" + pref.getEmpId() + "&SecurityCode=" + pref.getSecurityCode();
        Log.d("printurlrequest", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        llLoder.setVisibility(View.GONE);

                        // attendabceInfiList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");

                                JSONArray leaveBalanceArray = responseData.optJSONArray(1);
                                for (int i = 0; i < leaveBalanceArray.length(); i++) {
                                    JSONObject balanceObject = leaveBalanceArray.optJSONObject(i);
                                    final String Code = balanceObject.optString("LeaveTypeName");
                                    final String Opening = balanceObject.optString("Opening");
                                    final String LeaveAvailed = balanceObject.optString("LeaveAvailed");
                                    final String Avaliable = balanceObject.optString("Avaliable");
                                    String LeaveTypeID = balanceObject.optString("LeaveTypeID");
                                   // typeAvaild.add(LeaveTypeID + "_" + Avaliable);

                                    LeaveBalanceModel model = new LeaveBalanceModel(Code, Opening, LeaveAvailed,Avaliable);
                                    itemList.add(model);
                                }
                                //typeAvailable = typeAvaild.toString().replace("]", "").replace("[", "").replaceAll("\\s+", "");
                                ;
                                //Log.d("availd", typeAvaild.toString());
                                llMain.setVisibility(View.VISIBLE);
                                llNodata.setVisibility(View.GONE);
                                setAdapter();







                            } else {
                                llMain.setVisibility(View.GONE);
                                llNodata.setVisibility(View.VISIBLE);

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
                llNodata.setVisibility(View.VISIBLE);


                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }

}