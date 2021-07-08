package com.genius.hrms.activity.activity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.genius.hrms.R;
import com.genius.hrms.activity.geofence.MulFenceConfigActivity;


public class SettingDashBoardActivity extends AppCompatActivity {
    LinearLayout llConfig,llFence;
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_dash_board);


        llFence=(LinearLayout)findViewById(R.id.llFence);


        llFence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingDashBoardActivity.this, MulFenceConfigActivity.class);
                startActivity(intent);
            }
        });

        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}
