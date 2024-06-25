package com.genius.payhrms.activity.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.genius.payhrms.R;

public class FRDashboard extends AppCompatActivity {
    LinearLayout llFaceRecog,llAddFace;
    ImageView imgBack,imgHome;
    String intt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f_r_dashboard);
        initialize();
        onClick();
    }

    private void initialize(){

        llFaceRecog=(LinearLayout)findViewById(R.id.llFaceRecog);
        llAddFace=(LinearLayout)findViewById(R.id.llAddFace);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);



    }



    private void onClick() {
        llFaceRecog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), MarkInAttendanceDashboardActivity.class);
//
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
                Intent intent = new Intent(FRDashboard.this, MarkInAttendance.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


        llAddFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddPerson.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
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
            public void onClick(View view) {
                Intent intent=new Intent(FRDashboard.this,AttendanceActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }






}