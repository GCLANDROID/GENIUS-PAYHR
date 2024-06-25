package com.genius.payhrms.activity.leaveapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.genius.payhrms.R;
import com.genius.payhrms.activity.activity.UserDashBoardActivity;
import com.genius.payhrms.activity.utility.Pref;

public class LeaveApplicationDashboardActivity extends AppCompatActivity {
    TextView tvToolBar,tvLeaveApplication,tvSpecialHoliday;
    Pref pref;
    LinearLayout llLeaveApplication,llSpecialHoliday;
    ImageView imgBack,imgHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_application_dashboard);
        initView();
        onClick();
    }

    private void initView(){
        pref=new Pref(LeaveApplicationDashboardActivity.this);
        tvToolBar=(TextView)findViewById(R.id.tvToolBar);
        tvLeaveApplication=(TextView)findViewById(R.id.tvLeaveApplication);
        tvSpecialHoliday=(TextView)findViewById(R.id.tvSpecialHoliday);
        if (pref.getLanguage().equals("hi")){
            tvToolBar.setText("छुट्टी का डैशबोर्ड");
            tvLeaveApplication.setText("छुट्टी की अर्जी");
            tvSpecialHoliday.setText("विशेष अवकाश");
        } else {
            tvToolBar.setText("Leave Dashboard");
            tvLeaveApplication.setText("Leave Application");
            tvSpecialHoliday.setText("Special Holiday");
        }

        llLeaveApplication=(LinearLayout)findViewById(R.id.llLeaveApplication);
        llSpecialHoliday=(LinearLayout)findViewById(R.id.llSpecialHoliday);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        imgBack=(ImageView)findViewById(R.id.imgBack);
    }

    private void onClick(){
        llLeaveApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LeaveApplicationDashboardActivity.this,LeaveApplicationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LeaveApplicationDashboardActivity.this, UserDashBoardActivity.class);
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
        llSpecialHoliday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LeaveApplicationDashboardActivity.this,SpecialholidayActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }
}
