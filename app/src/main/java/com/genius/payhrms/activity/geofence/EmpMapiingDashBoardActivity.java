package com.genius.payhrms.activity.geofence;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.genius.payhrms.R;
import com.genius.payhrms.activity.activity.UserDashBoardActivity;
import com.genius.payhrms.activity.utility.Pref;


public class EmpMapiingDashBoardActivity extends AppCompatActivity {
    LinearLayout llManage, llReport;
    ImageView imgBack, imgHome;
    String point;
    TextView tvToolBar, tvManage, tvReport;
    Pref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_mapiing_dash_board);
        initView();
        onClick();
    }

    private void initView() {
        pref = new Pref(getApplicationContext());
        llManage = (LinearLayout) findViewById(R.id.llManage);
        llReport = (LinearLayout) findViewById(R.id.llReport);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        tvToolBar=(TextView)findViewById(R.id.tvToolBar);
        tvManage=(TextView)findViewById(R.id.tvManage);
        tvReport=(TextView)findViewById(R.id.tvReport);

        point = getIntent().getStringExtra("point");
        Log.d("point",point);
        if (pref.getLanguage().equals("hi")){
            tvToolBar.setText("एम्प्लॉई मैपिंग");
            tvManage.setText("प्रबंधन");
            tvReport.setText("रिपोर्ट");
        }else {
            tvToolBar.setText("Employee mapping");
            tvManage.setText("Manage");
            tvReport.setText("Report");
        }

    }

    private void onClick() {
        llManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(EmpMapiingDashBoardActivity.this, EmployeeMappingNumberActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("point", point);
                    startActivity(intent);

            }
        });

        llReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmpMapiingDashBoardActivity.this, EmpMapingReportActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("point", point);
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
                Intent intent = new Intent(EmpMapiingDashBoardActivity.this, UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
