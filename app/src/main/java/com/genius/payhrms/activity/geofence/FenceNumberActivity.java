package com.genius.payhrms.activity.geofence;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genius.payhrms.R;
import com.genius.payhrms.activity.activity.UserDashBoardActivity;
import com.genius.payhrms.activity.utility.Pref;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FenceNumberActivity extends AppCompatActivity {
    TextView tvNumber;
    ImageView imgBack,imgHome;
    Pref pref;
    FloatingActionButton btnAdd;
    String point;
    String surl;
    TextView tvToolBar,tvTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fence_number);
        initView();
        onClick();
        //fenceget();
    }

    private void initView(){
        pref=new Pref(getApplicationContext());
        tvNumber=(TextView)findViewById(R.id.tvNumber);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        btnAdd=(FloatingActionButton)findViewById(R.id.btnAdd);
        point=getIntent().getStringExtra("point");
        tvToolBar=(TextView)findViewById(R.id.tvToolBar);
        tvTotal=(TextView)findViewById(R.id.tvTotal);
        if (pref.getLanguage().equals("hi")){
            tvToolBar.setText("विन्यास");
            tvTotal.setText("कुल जियो बाड़ कॉन्फ़िगर किया गया");
        }else {
            tvToolBar.setText("Configuration");
            tvTotal.setText("Total Geo Fence Configured");
        }
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
                Intent intent=new Intent(FenceNumberActivity.this, UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
        tvNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (point.equals("mul")) {
                    Intent intent = new Intent(FenceNumberActivity.this, MultipleConfigReportActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(FenceNumberActivity.this, ConfigReportActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (point.equals("mul")) {
                    Intent intent = new Intent(getApplicationContext(), MulFenceConfigActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getApplicationContext(), SingleFenceConfigActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }
        });
    }



    private void showAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Slow or No Internet connection");
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();

                    }
                });
        alertDialogBuilder.show();


    }
}
