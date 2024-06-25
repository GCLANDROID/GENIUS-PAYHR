package com.genius.payhrms.activity.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.genius.payhrms.R;
import com.genius.payhrms.activity.activity.UserDashBoardActivity;
import com.genius.payhrms.activity.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileUpdateActivity extends AppCompatActivity {
    ImageView imgBack, imgHome;
    TextView tvToolBar, tvPhoneNumber, tvEmail;
    EditText etPhnNumber, etEmail;
    Button btnUpdate, btnCancel;
    Pref pref;
    String surl;
    ProgressDialog pd;
    AlertDialog al1,alerDialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);
        initView();
        //approValChecking();
        onClick();
    }

    private void initView() {
        pref = new Pref(ProfileUpdateActivity.this);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);

        tvToolBar = (TextView) findViewById(R.id.tvToolBar);
        tvPhoneNumber = (TextView) findViewById(R.id.tvPhoneNumber);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        if (pref.getLanguage().equals("hi")) {
            tvToolBar.setText("अपडेट ");
            tvPhoneNumber.setText("फ़ोन नंबर");
            tvEmail.setText("ईमेल");
            btnCancel.setText("रद्द करना");
            btnUpdate.setText("अपडेट");
        } else {
            tvToolBar.setText("Update ");
            tvPhoneNumber.setText("Phone Number");
            tvEmail.setText("Email Address");
            btnCancel.setText("Cancel");
            btnUpdate.setText("Update");
        }

        etPhnNumber = (EditText) findViewById(R.id.etPhnNumber);
        etEmail = (EditText) findViewById(R.id.etEmail);
        pd = new ProgressDialog(ProfileUpdateActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);


    }

    private void onClick() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPhnNumber.getText().toString().length()>9) {
                    if (etEmail.getText().toString().contains("@")) {
                        //updateEmailAndPhn();
                    }else {
                        Toast.makeText(getApplicationContext(),"Please enter valid email address",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"Please enter Phone number",Toast.LENGTH_LONG).show();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etEmail.setText("");
                etPhnNumber.setText("");
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
                Intent intent=new Intent(ProfileUpdateActivity.this, UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }







    private void shoeBlockDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ProfileUpdateActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_access, null);
        dialogBuilder.setView(dialogView);
        TextView tvMsg = (TextView) dialogView.findViewById(R.id.tvMsg);
        tvMsg.setText("Sorry!Your previous request is waiting for approval");
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                al1.dismiss();
                Intent intent = new Intent(ProfileUpdateActivity.this, ProfileDashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
        al1 = dialogBuilder.create();
        al1.setCancelable(true);
        Window window = al1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        al1.show();


    }

    private void successAlert(String msg) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ProfileUpdateActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);


        tvInvalidDate.setText(msg);



        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                Intent intent=new Intent(ProfileUpdateActivity.this,ProfileDashboardActivity.class);
                startActivity(intent);
                finish();


            }
        });

        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }

}
