package com.genius.payhrms.activity.leaveapplication;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.genius.payhrms.R;
import com.genius.payhrms.activity.activity.EmplyoeeCalendarDashboarActivity;
import com.genius.payhrms.activity.activity.UserDashBoardActivity;
import com.genius.payhrms.activity.utility.Pref;


public class LeaveApplicationActivity extends AppCompatActivity {
    LinearLayout llApplication,llApproval,llDetails;
    ImageView imgBack,imgHome;
    TextView tvApproval,tvDetails,tvApllication,tvToolBar;
    Pref pref;
    LinearLayout lnNonAccess,lnAccess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_application);
        initView();
        loadApplicationFragment();
        onClick();
    }


    private void initView(){
        pref=new Pref(getApplicationContext());
        lnNonAccess=(LinearLayout)findViewById(R.id.lnNonAccess);
        lnAccess=(LinearLayout)findViewById(R.id.lnAccess);
        llApplication=(LinearLayout)findViewById(R.id.llApplication);
        llApproval=(LinearLayout)findViewById(R.id.llApproval);
        llDetails=(LinearLayout)findViewById(R.id.llDetails);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);

        tvApllication=(TextView)findViewById(R.id.tvApllication);
        tvDetails=(TextView)findViewById(R.id.tvDetails);
        tvApproval=(TextView)findViewById(R.id.tvApproval);
        tvToolBar=(TextView)findViewById(R.id.tvToolBar);
        if (pref.getLanguage().equals("hi")){
            tvApllication.setText("छुट्टी की अर्जी");
            tvDetails.setText("रिपोर्ट");
            tvApproval.setText("टीम रिपोर्ट");
            tvToolBar.setText("छुट्टी की अर्जी");
        }else {
            tvApllication.setText("Leave Application");
            tvDetails.setText("Report");
            tvApproval.setText("Leave Approval");
            tvToolBar.setText("Leave Application");
        }
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
                loadDetailsFragment();
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
                if (pref.getSecurityCode().equals("6715")|| pref.getSecurityCode().toString().equals("6716")){
                    Intent intent=new Intent(getApplicationContext(), EmplyoeeCalendarDashboarActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    //finish();
                } else {
                    Intent intent=new Intent(getApplicationContext(), UserDashBoardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    //finish();
                }
            }
        });
    }


    public void loadApplicationFragment() {
        tvToolBar.setText("Leave Application");
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        ApplicationFragment pfragment = new ApplicationFragment();
        transaction.replace(R.id.frameLayout, pfragment);
        transaction.commit();
   }

    public void loadApproverFragment() {
        tvToolBar.setText("Leave Details");
        tvToolBar.setText("Leave Approval");
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        ApproverFragment efr=new ApproverFragment();
        transaction.replace(R.id.frameLayout, efr);
        transaction.commit();
    }


    public void loadDetailsFragment() {
        tvToolBar.setText("Leave Details");
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        DetailsFragment htfragment=new DetailsFragment();
        transaction.replace(R.id.frameLayout, htfragment);
        transaction.commit();
    }

    public void  approverVisibility(){
        llApproval.setVisibility(View.VISIBLE);
    }

    public void  approverHidden(){
        llApproval.setVisibility(View.GONE);
    }
}
