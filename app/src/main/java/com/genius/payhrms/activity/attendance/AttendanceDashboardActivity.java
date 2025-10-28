package com.genius.payhrms.activity.attendance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.genius.payhrms.R;
import com.genius.payhrms.activity.activity.EmplyoeeCalendarDashboarActivity;
import com.genius.payhrms.activity.activity.LoginActivity;

public class AttendanceDashboardActivity extends AppCompatActivity {
    LinearLayout llManage,llLog,llReport,llSubordinate,llBackLog,llQRCode;
    TextView tvToolBar;
    ImageView imgBack,imgHome;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_daily_dash_board);
        initView();
        btnClick();
    }

    private void initView() {
        tvToolBar = findViewById(R.id.tvToolBar);
        tvToolBar.setText("Attendance Manage");
        imgBack = findViewById(R.id.imgBack);
        imgHome = findViewById(R.id.imgHome);
        llManage = findViewById(R.id.llManage);
        llLog = findViewById(R.id.llLog);
        llReport = findViewById(R.id.llReport);
        llSubordinate = findViewById(R.id.llSubordinate);
        llBackLog = findViewById(R.id.llBackLog);
        llQRCode = findViewById(R.id.llQRCode);
        llLog.setVisibility(View.GONE);
        llSubordinate.setVisibility(View.GONE);
        llBackLog.setVisibility(View.GONE);
        llQRCode.setVisibility(View.GONE);
    }

    private void btnClick() {
        llManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AttendanceDashboardActivity.this, AttendanceMarkActivity.class);
                intent.putExtra("address","");
                startActivity(intent);
            }
        });

        llReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AttendanceDashboardActivity.this, AttendanceReportActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AttendanceDashboardActivity.this, EmplyoeeCalendarDashboarActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}
