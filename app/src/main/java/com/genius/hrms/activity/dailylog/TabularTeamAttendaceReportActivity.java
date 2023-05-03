package com.genius.hrms.activity.dailylog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genius.hrms.R;
import com.genius.hrms.activity.adapter.TabularTeamAttendanceReportAdapter;
import com.genius.hrms.activity.attendance.AttendanceCalenderDashboardActivity;
import com.genius.hrms.activity.attendance.AttendanceReportActivity;
import com.genius.hrms.activity.attendance.TeamAttendanceReportActivity;
import com.genius.hrms.activity.model.AttendanceCalenderModel;
import com.genius.hrms.activity.model.TabularTeamAttendaceReportModel;
import com.genius.hrms.activity.utility.Pref;
import com.genius.hrms.activity.utility.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class TabularTeamAttendaceReportActivity extends AppCompatActivity {
    RecyclerView rvItem;
    TextView tvEmpName;
    String empID,empName;
    Pref pref;
    ArrayList<TabularTeamAttendaceReportModel>itemList=new ArrayList<>();
    int y,m;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabular_team_attendace_report);
        initView();
    }

    private void initView(){
        pref = new Pref(TabularTeamAttendaceReportActivity.this);
        y = Calendar.getInstance().get(Calendar.YEAR);
        m = Calendar.getInstance().get(Calendar.MONTH) + 1;
        empID=getIntent().getStringExtra("empID");
        empName=getIntent().getStringExtra("empName");
        rvItem=(RecyclerView) findViewById(R.id.rvItem);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(TabularTeamAttendaceReportActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);

        tvEmpName=(TextView) findViewById(R.id.tvEmpName);
        tvEmpName.setText(empName);

        getAttendanceList(y,m);
    }


    private void getAttendanceList(int year, int month) {


        final ProgressDialog pd = new ProgressDialog(TabularTeamAttendaceReportActivity.this);
        pd.setMessage("Loading...");
        pd.show();
        pd.setCancelable(false);
        String surl = "https://cloud.geniusconsultant.com/GHRMSApi/api/get_EmployeeAttendanceReportAPP?&AEMEmployeeId="+empID+"&Year="+year+"&Month="+month+"&SecurityCode="+pref.getSecurityCode();
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
                                    String sDate = obj.optString("Date");
                                    String Date = obj.optString("Date");
                                    String PunchTiming = obj.optString("Punchtime");
                                    String Day = obj.optString("Day");
                                    String Status = obj.optString("Status");


                                    TabularTeamAttendaceReportModel model = new TabularTeamAttendaceReportModel();
                                    if (!Status.equals("")  ) {
                                        model.setDate(Date);
                                        model.setPunchTime(PunchTiming);
                                        model.setStatus(Status);
                                        itemList.add(model);
                                    }


                                }

                                TabularTeamAttendanceReportAdapter reportAdapter=new TabularTeamAttendanceReportAdapter(itemList,TabularTeamAttendaceReportActivity.this);
                                rvItem.setAdapter(reportAdapter);





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
        RequestQueue requestQueue = Volley.newRequestQueue(TabularTeamAttendaceReportActivity.this);
        requestQueue.add(stringRequest);
    }
}