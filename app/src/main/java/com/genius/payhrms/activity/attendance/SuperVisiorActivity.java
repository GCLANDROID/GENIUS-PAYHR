package com.genius.payhrms.activity.attendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.genius.payhrms.R;
import com.genius.payhrms.activity.activity.UserDashBoardActivity;

public class SuperVisiorActivity extends AppCompatActivity {
    ImageView imgBack,imgHome;
    LinearLayout llTeam,llApproval,llTBlock,llABlock;

    TextView tvToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_visior);
        initView();
        loadTeamFragment();
        onClick();
    }

    private void initView(){
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);

        llTeam=(LinearLayout)findViewById(R.id.llTeam);
        llApproval=(LinearLayout)findViewById(R.id.llApproval);

        tvToolBar=(TextView)findViewById(R.id.tvToolBar);

        llTBlock=(LinearLayout)findViewById(R.id.llTBlock);
        llABlock=(LinearLayout)findViewById(R.id.llABlock);

    }


    public void loadTeamFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        TeamReportFragment tpFragement=new TeamReportFragment();
        transaction.replace(R.id.frameLayout, tpFragement);
        transaction.commit();
        llTBlock.setVisibility(View.VISIBLE);
        llABlock.setVisibility(View.GONE);
        tvToolBar.setText("Team Report");
    }

    public void loadApproverFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        AttendanceApprovalFragment efr = new AttendanceApprovalFragment();
        transaction.replace(R.id.frameLayout, efr);
        transaction.commit();

        llTBlock.setVisibility(View.GONE);
        llABlock.setVisibility(View.VISIBLE);

        tvToolBar.setText("Attendance Approval");

        //tvHeader.setText("Personal");
    }

    private void onClick(){
        llTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTeamFragment();
            }
        });

        llApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadApproverFragment();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SuperVisiorActivity.this, UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

}
