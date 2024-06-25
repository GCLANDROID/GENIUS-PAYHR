package com.genius.payhrms.activity.leaveapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.genius.payhrms.R;
import com.genius.payhrms.activity.activity.UserDashBoardActivity;
import com.genius.payhrms.activity.utility.Pref;

public class SpecialholidayActivity extends AppCompatActivity {
    ImageView imgBack,imgHome;
    LinearLayout llApplication,llApproval;
    TextView tvApllication,tvApproval;
    Pref pref;
    TextView tvToolBar,tvDetails;
    LinearLayout llDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialholiday);
        initView();
        loadApplicationFragment();
        onClick();
    }
    private void initView(){
        pref=new Pref(SpecialholidayActivity.this);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);

        llApplication=(LinearLayout)findViewById(R.id.llApplication);
        llApproval=(LinearLayout)findViewById(R.id.llApproval);
        llDetails=(LinearLayout)findViewById(R.id.llDetails);

        tvApllication=(TextView)findViewById(R.id.tvApllication);
        tvApproval=(TextView)findViewById(R.id.tvApproval);
        tvToolBar=(TextView)findViewById(R.id.tvToolBar);
        tvDetails=(TextView)findViewById(R.id.tvDetails);
        if (pref.getLanguage().equals("hi")){
            tvApllication.setText("आवेदन");
            tvApproval.setText("अनुमोदन");
            tvToolBar.setText("विशेष अवकाश");
            tvDetails.setText("वविवरण");


        }else
        {
            tvApllication.setText("Application");
            tvApproval.setText("Approval");
            tvToolBar.setText("Special Holiday");
            tvDetails.setText("Report");
        }
    }

    public void loadApplicationFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SpecialHolidayApplicationFragment pfragment=new SpecialHolidayApplicationFragment();
        transaction.replace(R.id.frameLayout, pfragment);
        transaction.commit();
        tvApllication.setTextColor(Color.parseColor("#075994"));
        tvApproval.setTextColor(Color.parseColor("#ffffff"));
        tvDetails.setTextColor(Color.parseColor("#ffffff"));
    }

    public void loadApproverFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SpecialHolidayApproverFragment efr=new SpecialHolidayApproverFragment();
        transaction.replace(R.id.frameLayout, efr);
        transaction.commit();

        tvApllication.setTextColor(Color.parseColor("#ffffff"));
        tvApproval.setTextColor(Color.parseColor("#075994"));
        tvDetails.setTextColor(Color.parseColor("#ffffff"));
    }


    public void loadReportFragment() {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SpecialHolidayReportFragment efr=new SpecialHolidayReportFragment();
        transaction.replace(R.id.frameLayout, efr);
        transaction.commit();

        tvApllication.setTextColor(Color.parseColor("#ffffff"));
        tvApproval.setTextColor(Color.parseColor("#ffffff"));
        tvDetails.setTextColor(Color.parseColor("#075994"));




    }


    private void onClick(){
        llApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadApplicationFragment();
            }
        });
        llApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadApproverFragment();
            }
        });

        llDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadReportFragment();
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
                Intent intent=new Intent(SpecialholidayActivity.this, UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void  approverVisibility(){
        llApproval.setVisibility(View.VISIBLE);
    }

    public void  approverHidden(){
        llApproval.setVisibility(View.GONE);
    }



}
