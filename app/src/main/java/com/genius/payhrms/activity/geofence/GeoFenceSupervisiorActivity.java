package com.genius.payhrms.activity.geofence;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.genius.payhrms.R;
import com.genius.payhrms.activity.activity.UserDashBoardActivity;
import com.genius.payhrms.activity.attendance.SuperVisiorActivity;

public class GeoFenceSupervisiorActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout llTeam,llApproval;
    ImageView imgBack,imgHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_fence_supervisior);
        initView();
    }

    private void initView(){
        llTeam=(LinearLayout) findViewById(R.id.llTeam);
        llApproval=(LinearLayout) findViewById(R.id.llApproval);

        imgHome=(ImageView)findViewById(R.id.imgHome);
        imgBack=(ImageView)findViewById(R.id.imgBack);

        llTeam.setOnClickListener(this);
        llApproval.setOnClickListener(this);

        imgBack.setOnClickListener(this);
        imgHome.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view==llApproval){
            Intent intent=new Intent(GeoFenceSupervisiorActivity.this,GeoFenceApprovalActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if (view==llTeam){
            Intent intent = new Intent(GeoFenceSupervisiorActivity.this, SuperVisiorActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if (view==imgBack){
            onBackPressed();
        }else if (view==imgHome){
            Intent intent=new Intent(GeoFenceSupervisiorActivity.this, UserDashBoardActivity.class);
            startActivity(intent);
            finish();
        }
    }
}