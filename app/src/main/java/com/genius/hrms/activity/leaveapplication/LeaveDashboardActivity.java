package com.genius.hrms.activity.leaveapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.genius.hrms.R;
import com.genius.hrms.activity.activity.UserDashBoardActivity;
import com.genius.hrms.activity.payroll.PayrollActivity;
import com.genius.hrms.activity.utility.Pref;

public class LeaveDashboardActivity extends AppCompatActivity {
    LinearLayout llLeaveApplication,llLeaveBalance;
    ImageView imgBack,imgHome;
    TextView tvLeave,tvLeaveBalance,tvToolBar;
    Pref pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_dashboard);
        initview();
        onClick();
    }

    private void onClick() {
        llLeaveApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LeaveDashboardActivity.this, LeaveApplicationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        llLeaveBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LeaveDashboardActivity.this, LeaveBalanceActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent=new Intent(LeaveDashboardActivity.this, UserDashBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //finish();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void initview() {
        llLeaveApplication=findViewById(R.id.llLeaveApplication);
        llLeaveBalance=findViewById(R.id.llLeaveBalance);
        imgBack=findViewById(R.id.imgBack);
        imgHome=findViewById(R.id.imgHome);
        tvLeave=findViewById(R.id.tvLeave);
        tvLeaveBalance=findViewById(R.id.tvLeaveBalance);
        tvToolBar=findViewById(R.id.tvToolBar);
        pref = new Pref(getApplicationContext());
        if (pref.getLanguage().equals("hi")) {
            tvLeaveBalance.setText("बकाया छुट्टियां");
            tvLeave.setText("छुट्टी की अर्जी");
            tvToolBar.setText("डैशबोर्ड छोड़ें");
        }
    }
}