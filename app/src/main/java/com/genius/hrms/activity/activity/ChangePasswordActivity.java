package com.genius.hrms.activity.activity;

import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.genius.hrms.R;
import com.genius.hrms.activity.utility.AttendanceService;
import com.genius.hrms.activity.utility.Pref;
import com.genius.hrms.activity.utility.UploadObject;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChangePasswordActivity extends AppCompatActivity {

    TextView tvNewPassword, tvConfirmPassword;
    EditText etNewPassword, etConfirmPassword;
    Button btnUpdate,btnCancel;
    String isModiFied, empId, securityCode;
    private static String SERVER_PATH = "";
    private AttendanceService uploadService;
    ProgressDialog progressDialog;
    Pref pref;
    String newPassword,confirmPassowrd;
    String ipAddress;
    ImageView imgHome,imgBack;
    String status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initView();
        onClick();
    }

    private void initView() {
        pref=new Pref(getApplicationContext());
        tvNewPassword = (TextView) findViewById(R.id.tvNewPassword);
        tvConfirmPassword = (TextView) findViewById(R.id.tvConfirmPassword);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);



        String color = "<font color='#EE0000'>*</font>";
        if (pref.getLanguage().equals("hi")) {
            newPassword = "नया पासवर्ड:";
        }else {
            newPassword="New Password:";
        }
        tvNewPassword.setText(Html.fromHtml(newPassword + color));

         if (pref.getLanguage().equals("hi")){
             confirmPassowrd="पासवर्ड की पुष्टि कीजिये";
         }else {
             confirmPassowrd = "Confirm Password";
         }

        tvConfirmPassword.setText(Html.fromHtml(confirmPassowrd + color));

        etNewPassword = (EditText) findViewById(R.id.etNewPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);


        empId =pref.getEmpId() ;
        securityCode=pref.getSecurityCode();
        btnCancel=(Button)findViewById(R.id.btnCancel);
        if (pref.getSecurityCode().equals("1080")){
            ipAddress="https://adityabirla.geniusconsultant.com/";
        }else {
            ipAddress="https://cloud.geniusconsultant.com/";
        }
         SERVER_PATH = ipAddress+"GHRMSApi/api/Authentication/";



        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .build();

        // Change base URL to your upload server URL.
        uploadService = (AttendanceService) new Retrofit.Builder()
                .baseUrl(SERVER_PATH)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AttendanceService.class);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(true);

        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);



    }

    private void onClick() {

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etNewPassword.getText().toString().length() > 0) {
                    if (etConfirmPassword.getText().toString().length() > 0) {
                        if (etNewPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {


                            changePassword();

                        } else {
                            etConfirmPassword.setError("Confirm password should be same with new password");
                            etConfirmPassword.requestFocus();
                        }

                    } else {
                        etConfirmPassword.setError("Please enter confirm Password");
                        etConfirmPassword.requestFocus();
                    }

                } else {
                    etNewPassword.setError("Please enter new Password");
                    etNewPassword.requestFocus();

                }

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etConfirmPassword.setText("");
                etNewPassword.setText("");
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
                Intent intent=new Intent(getApplicationContext(),UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


    private void changePassword() {
        progressDialog.show();

        Call<UploadObject> fileUpload = uploadService.changePassword(empId, etNewPassword.getText().toString(), securityCode);
        fileUpload.enqueue(new Callback<UploadObject>() {
            @Override
            public void onResponse(Call<UploadObject> call, Response<UploadObject> response) {
                progressDialog.dismiss();
                UploadObject extraWorkingDayModel = response.body();
                if (extraWorkingDayModel.isResponseStatus()) {
                    Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                    intent.putExtra("flaggoing","1");
                    startActivity(intent);
                    finish();
                    pref.savePassword(etNewPassword.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), extraWorkingDayModel.getResponseText(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UploadObject> call, Throwable t) {
                progressDialog.dismiss();

                Log.e("error", "Error " + t.getMessage());
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();


                //   Toast.makeText(AttendanceManageActivity.this,"attendance saved without image",Toast.LENGTH_LONG).show();
            }

        });

    }


}
