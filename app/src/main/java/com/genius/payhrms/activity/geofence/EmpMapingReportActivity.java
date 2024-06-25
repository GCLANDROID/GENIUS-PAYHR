package com.genius.payhrms.activity.geofence;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.genius.payhrms.R;

import com.genius.payhrms.activity.activity.UserDashBoardActivity;
import com.genius.payhrms.activity.adapter.EmpMapingReportAdapter;
import com.genius.payhrms.activity.model.EmpMapiingReportModel;
import com.genius.payhrms.activity.model.SpinnerModel;
import com.genius.payhrms.activity.utility.Pref;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EmpMapingReportActivity extends AppCompatActivity {
    Spinner spLocation;
    LinearLayout llMain, llRV, llLoader, llNoData;
    RecyclerView rvItem;
    ImageView imgBack, imgHome;
    ArrayList<SpinnerModel> mLocationList = new ArrayList<>();
    ArrayList<String> locationList = new ArrayList<>();
    Pref pref;
    ArrayList<EmpMapiingReportModel> empList = new ArrayList<>();
    LinearLayout llNoData1;
    String locationId = "0";
    String point;
    String surl,surl1;
    TextView tvToolbar,tvNodata;
    String surl2,translated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_maping_report);

        initView();
        //locationGet();
        onClick();
    }

    private void initView() {
        pref = new Pref(getApplicationContext());
        spLocation = (Spinner) findViewById(R.id.spLocation);
        llMain = (LinearLayout) findViewById(R.id.llMain);
        llRV = (LinearLayout) findViewById(R.id.llRV);
        llNoData = (LinearLayout) findViewById(R.id.llNoData);
        llLoader = (LinearLayout) findViewById(R.id.llLoader);
        rvItem = (RecyclerView) findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(EmpMapingReportActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);

        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        llNoData1 = (LinearLayout) findViewById(R.id.llNoData1);
        point=getIntent().getStringExtra("point");
        tvToolbar=(TextView)findViewById(R.id.tvToolBar);
        tvNodata=(TextView)findViewById(R.id.tvNoData);
        if (pref.getLanguage().equals("hi")){
            tvToolbar.setText("कर्मचारी मानचित्रण रिपोर्ट");
            tvNodata.setText("कोई डेटा नहीं मिला");
        }else {
            tvToolbar.setText("Employee mapping report");
            tvNodata.setText("No vdata found");
        }
    }


    private void onClick() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
        spLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    locationId = mLocationList.get(position).getItemId();
                    //getEmpList();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


}
