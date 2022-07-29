package com.genius.hrms.activity.dailylog;

import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.genius.hrms.R;
import com.genius.hrms.activity.activity.EmployeeDashBoardActivity;
import com.genius.hrms.activity.activity.UserDashBoardActivity;
import com.genius.hrms.activity.attendance.AttendanceActivity;
import com.genius.hrms.activity.attendance.AttendanceReportActivity;
import com.genius.hrms.activity.attendance.AttendanceReportForPPSActivity;
import com.genius.hrms.activity.attendance.BacklogActivity;
import com.genius.hrms.activity.attendance.SuperVisiorActivity;
import com.genius.hrms.activity.utility.NetworkConnectionCheck;
import com.genius.hrms.activity.utility.Pref;


public class OfflineDailyDashBoardActivity extends AppCompatActivity {
    LinearLayout llManage, llReport, llLog,llSubordinate,llBackLog;

    ImageView imgBack, imgHome;
    NetworkConnectionCheck connectionCheck;
    ProgressDialog progressDialog;
    TextView tvManage,tvLogBook,tvReport,tvToolBar,tvsubordinate,tvBackLog;
    Pref pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_daily_dash_board);
        initialize();
        onClick();
    }

    private void initialize() {
        pref=new Pref(getApplicationContext());
        connectionCheck = new NetworkConnectionCheck(OfflineDailyDashBoardActivity.this);
        llManage = (LinearLayout) findViewById(R.id.llManage);
        llReport = (LinearLayout) findViewById(R.id.llReport);
        llSubordinate = (LinearLayout) findViewById(R.id.llSubordinate);


        llLog = (LinearLayout) findViewById(R.id.llLog);
        llBackLog=(LinearLayout)findViewById(R.id.llBackLog);

        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        tvLogBook=(TextView)findViewById(R.id.tvLogBook);
        tvManage=(TextView)findViewById(R.id.tvManage);
        tvReport=(TextView)findViewById(R.id.tvReport);
        tvToolBar=(TextView)findViewById(R.id.tvToolBar);
        tvsubordinate=(TextView)findViewById(R.id.tvsubordinate);
        tvBackLog=findViewById(R.id.tvBackLog);
        if (pref.getLanguage().equals("hi")){
            tvManage.setText("प्रबंधन");
            tvLogBook.setText("कार्यपंजी");
            tvReport.setText("रिपोर्ट");
            tvToolBar.setText("दैनिक लॉग");
            tvsubordinate.setText("टीम रिपोर्ट");
            tvBackLog.setText("बैकलॉग उपस्थिति");
        }else {
            tvManage.setText("Manage");
            tvLogBook.setText("Log Book");
            tvReport.setText("Attendance Report");
            tvToolBar.setText("Daily Log");
            tvsubordinate.setText("Team Report");
            tvBackLog.setText("Attendance Regularization");
        }
    }

    private void onClick() {
        llBackLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OfflineDailyDashBoardActivity.this, BacklogActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        llManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();

                    Intent intent = new Intent(OfflineDailyDashBoardActivity.this, VisitLocationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

            }
        });


        llLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (connectionCheck.isNetworkAvailable()) {

                    Intent intent = new Intent(OfflineDailyDashBoardActivity.this, NumberTourActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(OfflineDailyDashBoardActivity.this, OfflineAttenReportActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        llSubordinate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent intent = new Intent(OfflineDailyDashBoardActivity.this, SuperVisiorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });



        llReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (pref.getSecurityCode().equals("123")){
                    Intent intent = new Intent(OfflineDailyDashBoardActivity.this, AttendanceReportForPPSActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(OfflineDailyDashBoardActivity.this, AttendanceReportActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

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
                Intent intent = new Intent(OfflineDailyDashBoardActivity.this, UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        progressDialog.dismiss();
    }
}
