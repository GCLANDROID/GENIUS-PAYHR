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


public class EmployeeMappingNumberActivity extends AppCompatActivity {
    LinearLayout llSingle,llMultiple;
    ImageView imgBack,imgHome;
    TextView tvSingle,tvMultiple;
    Pref pref;
    String point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_mapping_number);
        initView();
        onClick();
    }
    private void initView(){
        pref=new Pref(getApplicationContext());
        llSingle=(LinearLayout)findViewById(R.id.llSingle);
        llMultiple=(LinearLayout)findViewById(R.id.llMultiple);
        tvSingle=(TextView)findViewById(R.id.tvSingle);
        tvMultiple=(TextView)findViewById(R.id.tvMultiple);
        if (pref.getLanguage().equals("hi")){
            tvSingle.setText("एकल कर्मचारी कई स्थान");
            tvMultiple.setText("कई कर्मचारी एकल स्थान");
        }else {
            tvSingle.setText("Single employee multiple location");
            tvMultiple.setText("Multiple employee single fence");
        }
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        point=getIntent().getStringExtra("point");
        Log.d("point",point);
    }

    private void onClick(){
        llSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EmployeeMappingNumberActivity.this,SEMFActivity.class);
                intent.putExtra("point",point);
                startActivity(intent);
            }
        });
        llMultiple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EmployeeMappingNumberActivity.this,EmpMappingActivity.class);
                intent.putExtra("point",point);
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
                Intent intent=new Intent(getApplicationContext(), UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
