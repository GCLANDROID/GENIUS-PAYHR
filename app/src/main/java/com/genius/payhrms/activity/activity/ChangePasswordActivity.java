package com.genius.payhrms.activity.activity;

import static com.genius.payhrms.activity.utility.Util.SECRET_KEY;
import static com.genius.payhrms.activity.utility.Util.encrypt;

import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;
import android.os.RecoverySystem;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.genius.payhrms.R;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.AttendanceService;
import com.genius.payhrms.activity.utility.Pref;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChangePasswordActivity extends AppCompatActivity {
    private static final String TAG = "ChangePasswordActivity";
    TextView tvNewPassword, tvConfirmPassword,tvOLDPassword;
    EditText etNewPassword, etConfirmPassword,etOLDPassword;
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
        tvOLDPassword = (TextView) findViewById(R.id.tvOLDPassword);
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
        etOLDPassword = (EditText) findViewById(R.id.etOLDPassword);

        tvOLDPassword.setText(Html.fromHtml("Old Password" + color));
        empId =pref.getEmpId() ;
        securityCode=pref.getSecurityCode();
        btnCancel=(Button)findViewById(R.id.btnCancel);
        ipAddress="https://cloud.geniusconsultant.com/";
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
                            if (etOLDPassword.getText().toString().length()>0) {
                                JSONObject jsonObject=new JSONObject();
                                try {
                                    Log.e(TAG, "onClick: OLD: "+etOLDPassword.getText().toString().trim());
                                    Log.e(TAG, "onClick: NEW: "+etNewPassword.getText().toString().trim());
                                    jsonObject.put("EmployeeId",pref.getEmpId());
                                    jsonObject.put("NewPassword",encrypt(etNewPassword.getText().toString().trim(), SECRET_KEY));
                                    jsonObject.put("ExistingPassword",encrypt(etOLDPassword.getText().toString().trim(), SECRET_KEY));
                                    jsonObject.put("SecurityCode",pref.getSecurityCode());
                                    changepassword(jsonObject);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                etOLDPassword.setError("Please Enter Old Password");
                                etOLDPassword.requestFocus();
                            }
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

    private void changepassword(JSONObject jsonObject) {
        Log.e(TAG, "changepassword: "+jsonObject.toString());
        final ProgressDialog pd = new ProgressDialog(ChangePasswordActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();

        AndroidNetworking.post(Api.sChangePasswordapi)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        pd.show();
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "CHANGE_PASSWORD: "+ response.toString());
                        pd.dismiss();
                        JSONObject job = response;
                        int  Response_Code = job.optInt("Response_Code");
                        String responseText = job.optString("Response_Message");
                        if (Response_Code==101) {
                            Intent intent=new Intent(ChangePasswordActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();
                        }
                        // boolean _status = job1.getBoolean("status");
                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        pd.dismiss();
                        Toast.makeText(ChangePasswordActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
