package com.genius.payhrms.activity.dailylog;

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
import com.genius.payhrms.R;
import com.genius.payhrms.activity.adapter.TabularTeamAttendanceReportAdapter;
import com.genius.payhrms.activity.model.TabularTeamAttendaceReportModel;
import com.genius.payhrms.activity.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

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

        //getAttendanceList(y,m);
    }



}