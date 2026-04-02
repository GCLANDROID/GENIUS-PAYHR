package com.genius.payhrms.activity.leaveapplication.SortLeaveApplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.genius.payhrms.R;
import com.genius.payhrms.activity.activity.UserDashBoardActivity;
import com.genius.payhrms.activity.leaveapplication.ApplicationFragment;
import com.genius.payhrms.activity.leaveapplication.ApproverFragment;
import com.genius.payhrms.activity.leaveapplication.DetailsFragment;

import org.w3c.dom.Text;

public class SortLeaveActivity extends AppCompatActivity {
    TextView tvToolBar;
    LinearLayout llApplication,llDetails,llApproval;
    ImageView imgBack,imgHome;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_leave);
        initView();
        btnClick();
    }

    private void initView() {
        tvToolBar = findViewById(R.id.tvToolBar);
        llApplication = findViewById(R.id.llApplication);
        llDetails = findViewById(R.id.llDetails);
        llApproval = findViewById(R.id.llApproval);
        imgBack = findViewById(R.id.imgBack);
        imgHome = findViewById(R.id.imgHome);
        loadApplicationFragment();
    }

    private void btnClick() {
        llApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadApplicationFragment();
            }
        });
        llDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDetailsFragment();
            }
        });
        llApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadApproverFragment();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), UserDashBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    public void loadApplicationFragment() {
        tvToolBar.setText("Sort Leave Application");
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SortLeaveApplicationFragment pfragment = new SortLeaveApplicationFragment();
        transaction.replace(R.id.frameLayout, pfragment);
        transaction.commit();
    }

    public void loadDetailsFragment() {
        tvToolBar.setText("Sort Leave Details");
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SortLeaveDetailsFragment htfragment=new SortLeaveDetailsFragment();
        transaction.replace(R.id.frameLayout, htfragment);
        transaction.commit();
    }

    public void loadApproverFragment() {
        tvToolBar.setText("Sort Leave Approval");
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SortLeaveApproverFragment sortLeaveApproverFragment = new SortLeaveApproverFragment();
        transaction.replace(R.id.frameLayout, sortLeaveApproverFragment);
        transaction.commit();
    }


    public void approverHidden(){
        llApproval.setVisibility(View.GONE);
    }

    public void  approverVisibility(){
       llApproval.setVisibility(View.VISIBLE);
    }
}

