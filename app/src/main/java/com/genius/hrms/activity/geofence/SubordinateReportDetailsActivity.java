package com.genius.hrms.activity.geofence;

import android.content.Intent;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.genius.hrms.activity.adapter.SubordinateReportAdapter;
import com.genius.hrms.activity.model.SubordinateReportDetailsModel;
import com.genius.hrms.activity.utility.Pref;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SubordinateReportDetailsActivity extends AppCompatActivity {
     String year,month,date;
    String branchId,actionFlag;
    TextView tvToolBar;
    LinearLayout llLoader,llMain,llNoData;
    ArrayList<SubordinateReportDetailsModel>itemList=new ArrayList<>();
    RecyclerView rvItem;
    Pref pref;
    ImageView imgBack,imgHome;
    TextView tvDate,tvBranchName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subordinate_report_details);
        initView();
        onClick();
    }

    private void initView(){
        pref=new Pref(getApplicationContext());
       date=getIntent().getStringExtra("date").replaceAll("\\s+", "%20");;
       String showdate=getIntent().getStringExtra("date");
        int y = Calendar.getInstance().get(Calendar.YEAR);
        year = String.valueOf(y);
        Log.d("year", year);

        int m = Calendar.getInstance().get(Calendar.MONTH) + 1;
        Log.d("month", String.valueOf(m));
        if (m == 1) {
            month = "January";
        } else if (m == 2) {
            month = "February";
        } else if (m == 3) {
            month = "March";
        } else if (m == 4) {
            month = "April";
        } else if (m == 5) {
            month = "May";
        } else if (m == 6) {
            month = "June";
        } else if (m == 7) {
            month = "July";
        } else if (m == 8) {
            month = "August";
        } else if (m == 9) {
            month = "September";
        } else if (m == 10) {
            month = "October";
        } else if (m == 11) {
            month = "November";
        } else if (m == 12) {
            month = "December";
        }

        branchId=getIntent().getStringExtra("branchId");
        actionFlag=getIntent().getStringExtra("actionflag");
        tvToolBar=(TextView)findViewById(R.id.tvToolBar);
        if (actionFlag.equals("1")){
            if (pref.getLanguage().equals("hi")){
                 tvToolBar.setText("अंदर कर्मचारी ");
            }else {
                tvToolBar.setText("In Employee");
            }
        }else if (actionFlag.equals("2")){
            if (pref.getLanguage().equals("hi")){
                tvToolBar.setText("कर्मचारी बाहर");
            }else {
                tvToolBar.setText("Out Employee");
            }

        }else {
            if (pref.getLanguage().equals("hi")){
                tvToolBar.setText("चिअभी तक चिह्नित नहीं किया गया है");
            }else {
                tvToolBar.setText("Not Marked Employee");
            }
        }

        llNoData=(LinearLayout)findViewById(R.id.llNoData);
        llMain=(LinearLayout)findViewById(R.id.llMain);
        llLoader=(LinearLayout)findViewById(R.id.llLoader);
        rvItem=(RecyclerView)findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(SubordinateReportDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);
        getItemList(year,month,date,actionFlag);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        tvDate=(TextView)findViewById(R.id.tvDate);
        tvBranchName=(TextView)findViewById(R.id.tvBranchName);
        tvDate.setText(showdate);


    }


    private void getItemList(String y,String m,String date,String actionflag) {
        Log.d("Arpan", "arpan");
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        String surl = pref.getIpAddress()+"GHRMSApi/api/get_SupervisorEmployeeFenceReport?SDate="+date+"&EDate="+date+"&ApproverID=" + pref.getEmpId() + "&BranchId="+branchId+ "&ActionFlag="+actionflag+"&Operation=2&SecurityCode=" + pref.getSecurityCode();
        Log.d("inputdetails", surl);
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
                                    String BranchName = obj.optString("BranchName");
                                    String EmpName=obj.optString("EmpName");
                                    String Address=obj.optString("Address");
                                    String FenceTime=obj.optString("FenceTime");
                                    String FenceType=obj.optString("FenceType");
                                    tvBranchName.setText(BranchName);


                                    SubordinateReportDetailsModel obj2 = new SubordinateReportDetailsModel(EmpName,Address,FenceTime,FenceType);
                                    itemList.add(obj2);


                                }

                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNoData.setVisibility(View.GONE);
                                setAdapter();
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
        RequestQueue requestQueue = Volley.newRequestQueue(SubordinateReportDetailsActivity.this);
        requestQueue.add(stringRequest);
    }

    private void setAdapter() {
        SubordinateReportAdapter nAdapter = new SubordinateReportAdapter(itemList,SubordinateReportDetailsActivity.this);
        rvItem.setAdapter(nAdapter);

    }

    private void onClick(){
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
