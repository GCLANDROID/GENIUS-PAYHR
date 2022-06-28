package com.genius.hrms.activity.geofence;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.genius.hrms.R;
import com.genius.hrms.activity.activity.EmployeeDashBoardActivity;
import com.genius.hrms.activity.activity.UserDashBoardActivity;
import com.genius.hrms.activity.utility.Pref;


public class FencingDashBoardActivity extends AppCompatActivity {
    LinearLayout llFenceConfig, llFence, llConfig, llReport, llEMap;
    ImageView imgBack, imgHome;
    Pref pref;
    ProgressDialog pd, pd1;
    LinearLayout llMapping;
    LinearLayout llGeoConfig;
    String point;
    TextView tvToolBar,tvConfig,tvEmpMapping,tvActivity,tvSubReport;
    LinearLayout llSubReport;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fencing_dash_board);
        initView();
        onClick();


    }

    @Override
    protected void onPause() {
        super.onPause();
        pd.dismiss();
        pd1.dismiss();
    }

    private void onClick(){
        llFence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pd1.setMessage("Loading..");
                pd1.setCancelable(false);
                pd1.show();
                Intent intent = new Intent(FencingDashBoardActivity.this, GeoFenceManageDashBoardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("point",point);
                startActivity(intent);


            }
        });


        llFenceConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setMessage("Loading...");
                pd.show();
                pd.setCancelable(false);
                Intent intent = new Intent(FencingDashBoardActivity.this, FenceNumberActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("point",point);
                startActivity(intent);
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
                Intent intent = new Intent(getApplicationContext(), UserDashBoardActivity.class);
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

        llMapping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FencingDashBoardActivity.this, EmpMapiingDashBoardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("point",point);
                startActivity(intent);
            }
        });


    }
    private void initView(){
        pref = new Pref(getApplicationContext());
        pd = new ProgressDialog(FencingDashBoardActivity.this);
        pd1 = new ProgressDialog(FencingDashBoardActivity.this);
        llFenceConfig = (LinearLayout) findViewById(R.id.llFenceConfig);
        llFence = (LinearLayout) findViewById(R.id.llFence);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        llReport = (LinearLayout) findViewById(R.id.llReport);
        llMapping = (LinearLayout) findViewById(R.id.llMapping);
        imgBack = (ImageView) findViewById(R.id.imgBack);



        point=getIntent().getStringExtra("point");
        Log.d("point",point);
        tvToolBar=(TextView)findViewById(R.id.tvToolBar);
        tvConfig=(TextView)findViewById(R.id.tvConfig);
        tvEmpMapping=(TextView)findViewById(R.id.tvEmpMapping);
        tvActivity=(TextView)findViewById(R.id.tvActivity);
        tvSubReport=(TextView)findViewById(R.id.tvSubReport);
       if (pref.getLanguage().equals("hi")){
           tvToolBar.setText("भू बाड़");
           tvConfig.setText("विन्यास");
           tvEmpMapping.setText("कर्मचारी मैपिंग");
           tvActivity.setText("गतिविधि");

       }else {
           tvToolBar.setText("Geo Fence");
           tvConfig.setText("Configuration");
           tvEmpMapping.setText("Employee Mapping");
           tvActivity.setText("Activity");

       }




    }
}
