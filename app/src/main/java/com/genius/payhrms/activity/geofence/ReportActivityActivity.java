package com.genius.payhrms.activity.geofence;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
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
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genius.payhrms.R;
import com.genius.payhrms.activity.activity.UserDashBoardActivity;
import com.genius.payhrms.activity.adapter.FenceReportAdapter;
import com.genius.payhrms.activity.model.ReportModel;
import com.genius.payhrms.activity.utility.Pref;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReportActivityActivity extends AppCompatActivity {
    RecyclerView rvReport;
    LinearLayout llLoader, llMain;
    ArrayList<ReportModel> itemList = new ArrayList<>();
    ImageView imgBack;
    String fentype;
    Pref pref;
    ImageView imgHome;
    TextView tvToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_activity);
        initialize();
        //getItem();
    }

    private void initialize() {
        rvReport = (RecyclerView) findViewById(R.id.rvReport);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(ReportActivityActivity.this, LinearLayoutManager.VERTICAL, false);
        rvReport.setLayoutManager(layoutManager);
        llMain = (LinearLayout) findViewById(R.id.llMain);
        llLoader = (LinearLayout) findViewById(R.id.llLoader);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        pref=new Pref(ReportActivityActivity.this);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
        tvToolBar=(TextView)findViewById(R.id.tvToolBar);
        if (pref.getLanguage().equals("hi")){
            tvToolBar.setText("रिपोर्ट");
        }else {
            tvToolBar.setText("Report");
        }
    }




    private void setAdapter() {
        FenceReportAdapter aAdapter = new FenceReportAdapter(itemList,ReportActivityActivity.this);
        rvReport.setAdapter(aAdapter);

    }


}
