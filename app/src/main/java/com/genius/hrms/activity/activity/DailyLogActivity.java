package com.genius.hrms.activity.activity;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.genius.hrms.R;
import com.genius.hrms.activity.attendance.AttendanceReportActivity;
import com.genius.hrms.activity.dailylog.DailyLogManageActivity;
import com.genius.hrms.activity.dailylog.DailyLogReportActivity;


public class DailyLogActivity extends AppCompatActivity {
    LinearLayout llManage, llReport, llLog;
    LinearLayout llManageD, llReportD, llLogD;
    LinearLayout llManageD1, llReportD1, llLogD1;
    ImageView imgBack, imgHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_log);
        initialize();
        onClick();
    }

    private void initialize() {


        llManage = (LinearLayout) findViewById(R.id.llManage);
        llReport = (LinearLayout) findViewById(R.id.llReport);

        llManageD = (LinearLayout) findViewById(R.id.llManageD);
        llReportD = (LinearLayout) findViewById(R.id.llReportD);

        llManageD1 = (LinearLayout) findViewById(R.id.llManageD1);
        llReportD1 = (LinearLayout) findViewById(R.id.llReportD1);

        llLog = (LinearLayout) findViewById(R.id.llLog);
        llLogD = (LinearLayout) findViewById(R.id.llLogD);
        llLogD1 = (LinearLayout) findViewById(R.id.llLogD1);

        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
    }

    private void onClick() {
        llManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llManageD.setVisibility(View.GONE);
                llManageD1.setVisibility(View.VISIBLE);

                llReportD.setVisibility(View.VISIBLE);
                llReportD1.setVisibility(View.GONE);

                llLogD.setVisibility(View.VISIBLE);
                llLogD1.setVisibility(View.GONE);


                Intent intent = new Intent(DailyLogActivity.this, DailyLogManageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        llReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llReportD.setVisibility(View.GONE);
                llReportD1.setVisibility(View.VISIBLE);

                llManageD.setVisibility(View.VISIBLE);
                llManageD1.setVisibility(View.GONE);

                llLogD.setVisibility(View.VISIBLE);
                llLogD1.setVisibility(View.GONE);

                Intent intent = new Intent(DailyLogActivity.this, AttendanceReportActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        llLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llReportD.setVisibility(View.VISIBLE);
                llReportD1.setVisibility(View.GONE);

                llManageD.setVisibility(View.VISIBLE);
                llManageD1.setVisibility(View.GONE);

                llLogD.setVisibility(View.GONE);
                llLogD1.setVisibility(View.VISIBLE);

                Intent intent = new Intent(DailyLogActivity.this, DailyLogReportActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
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
                Intent intent = new Intent(DailyLogActivity.this, EmployeeDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

}
