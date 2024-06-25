package com.genius.payhrms.activity.dailylog;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.genius.payhrms.R;
import com.genius.payhrms.activity.activity.UserDashBoardActivity;


public class OfflineDailyLogActivity extends AppCompatActivity {
    LinearLayout llDailyLog;
    ImageView imgBack,imgHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_daily_log);
        intiView();
        onClick();
    }
    private void intiView(){
        llDailyLog=(LinearLayout)findViewById(R.id.llDailyLog);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
    }
    private void onClick(){
        llDailyLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OfflineDailyLogActivity.this, OfflineDailyDashBoardActivity.class);
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
                Intent intent=new Intent(OfflineDailyLogActivity.this, UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
