package com.genius.hrms.activity.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.genius.hrms.R;
import com.genius.hrms.activity.activity.UserDashBoardActivity;
import com.genius.hrms.activity.utility.Pref;

public class ProfileDashboardActivity extends AppCompatActivity {
    LinearLayout llView,llUpdate;
    TextView tvView,tvUpdate,tvToolBar;
    Pref pref;
    ImageView imgBack,imgHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_dashboard);
        initView();
        onClick();
    }

    private void initView(){
        pref=new Pref(ProfileDashboardActivity.this);
        llView=(LinearLayout)findViewById(R.id.llView);
        llUpdate=(LinearLayout)findViewById(R.id.llUpdate);

        tvUpdate=(TextView)findViewById(R.id.tvUpdate);
        tvView=(TextView)findViewById(R.id.tvView);
        tvToolBar=(TextView)findViewById(R.id.tvToolBar);
        if (pref.getLanguage().equals("hi")){
            tvView.setText("देखना");
            tvUpdate.setText("आवश्यक विवरण अपडेट करें");
            tvToolBar.setText("प्रोफाइल डैशबोर्ड");
        }else {
            tvView.setText("View");
            tvUpdate.setText("Update essential details");
            tvToolBar.setText("Profile Dashboard");
        }
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
    }

    private void onClick(){
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileDashboardActivity.this, UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
        llView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileDashboardActivity.this,ProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        llUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileDashboardActivity.this,ProfileUpdateActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }
}
