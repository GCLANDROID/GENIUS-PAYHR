package com.genius.payhrms.activity.dailylog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.genius.payhrms.R;
import com.genius.payhrms.activity.activity.UserDashBoardActivity;

public class QRAttendanceDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrattendance_dashboard);
        LinearLayout llQRGenerator=(LinearLayout) findViewById(R.id.llQRGenerator);
        LinearLayout llQRCode=(LinearLayout) findViewById(R.id.llQRCode);
        llQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(QRAttendanceDashboardActivity.this,QRCodeScannerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        llQRGenerator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(QRAttendanceDashboardActivity.this,QRGeneratorActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        ImageView imgBack=(ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ImageView imgHome=(ImageView) findViewById(R.id.imgHome);
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(QRAttendanceDashboardActivity.this, UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}