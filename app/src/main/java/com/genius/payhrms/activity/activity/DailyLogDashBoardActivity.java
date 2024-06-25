package com.genius.payhrms.activity.activity;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.genius.payhrms.R;

public class DailyLogDashBoardActivity extends AppCompatActivity {
    LinearLayout llDailyLog;
    ImageView imgBack,imgHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_log_dash_board);
        intialize();
        onClick();
    }

           /* CompanyID:1090000029,
            EmployeeID:2070001720,
            StartDate:"4\/24\/2024,
            EndDate:4\/25\/2024,
            LeaveTypeID:"3890000046
            SecurityCode:1000*/

    private void intialize(){
        llDailyLog=(LinearLayout)findViewById(R.id.llDailyLog);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
    }

    private void onClick(){
        llDailyLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        Intent intent=new Intent(DailyLogDashBoardActivity.this,DailyLogActivity.class);
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
            public void onClick(View v) {
                Intent intent=new Intent(DailyLogDashBoardActivity.this,EmployeeDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
