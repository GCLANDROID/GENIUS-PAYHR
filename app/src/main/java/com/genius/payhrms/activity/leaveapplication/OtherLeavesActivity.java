package com.genius.payhrms.activity.leaveapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.genius.payhrms.R;
import com.genius.payhrms.activity.activity.EmployeeDashBoardActivity;
import com.genius.payhrms.activity.activity.UserDashBoardActivity;
import com.genius.payhrms.databinding.ActivityOtherLeavesBinding;


public class OtherLeavesActivity extends AppCompatActivity {
    ActivityOtherLeavesBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_other_leaves);
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/
        binding = ActivityOtherLeavesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadOtherApplicationFragment();

        btnClick();



    }

    private void btnClick() {
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OtherLeavesActivity.this, UserDashBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        binding.llApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadOtherApplicationFragment();
            }
        });

        binding.llDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadOtherDetailsFragment();
            }
        });

        binding.llApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadOtherLeaveApproverFragment();
            }
        });

    }


    public void loadOtherApplicationFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        OtherApplicationFragment pfragment=new OtherApplicationFragment();

        binding.llApplication.setBackgroundColor(Color.parseColor("#075994"));
        binding.tvApplication.setTextColor(Color.parseColor("#FFFFFF"));

        binding.llDetails.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.tvDetails.setTextColor(Color.parseColor("#075994"));

        binding.llApproval.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.tvApproval.setTextColor(Color.parseColor("#075994"));

        transaction.replace(R.id.frameLayout, pfragment);
        transaction.commit();
    }

    public void loadOtherDetailsFragment() {

        binding.llApplication.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.tvApplication.setTextColor(Color.parseColor("#075994"));

        binding.llDetails.setBackgroundColor(Color.parseColor("#075994"));
        binding.tvDetails.setTextColor(Color.parseColor("#FFFFFF"));

        binding.llApproval.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.tvApproval.setTextColor(Color.parseColor("#075994"));

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        OtherApplicationDetailsFragment pfragment=new OtherApplicationDetailsFragment();
        transaction.replace(R.id.frameLayout, pfragment);
        transaction.commit();
    }
    //OtherLeaveApproverFragment

    public void loadOtherLeaveApproverFragment() {

        binding.llApplication.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.tvApplication.setTextColor(Color.parseColor("#075994"));

        binding.llDetails.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.tvDetails.setTextColor(Color.parseColor("#075994"));

        binding.llApproval.setBackgroundColor(Color.parseColor("#075994"));
        binding.tvApproval.setTextColor(Color.parseColor("#FFFFFF"));

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        OtherLeaveApproverFragment pfragment=new OtherLeaveApproverFragment();
        transaction.replace(R.id.frameLayout, pfragment);
        transaction.commit();
    }

    public void  approverHidden(){
        binding.llApproval.setVisibility(View.GONE);
    }

    public void  approverVisibility(){
        binding.llApproval.setVisibility(View.VISIBLE);
    }
}