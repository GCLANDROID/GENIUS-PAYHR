package com.genius.payhrms.activity.activity;

import static com.genius.payhrms.activity.utility.Util.SECRET_KEY;
import static com.genius.payhrms.activity.utility.Util.encrypt;

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

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
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
import com.genius.payhrms.activity.model.HoliDayModel;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.Pref;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText etUserId, etSecuritycode;
    TextView tvForgot,tvToolBar;
    //Button btnSubmit;
    ImageView btnSubmit,imgHome,imgBack;
    Pref pref;
    AlertDialog alerDialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initView();
        onClick();
    }

    private void initView() {
        pref = new Pref(getApplicationContext());

        etUserId = (EditText) findViewById(R.id.etUserId);
        etSecuritycode = (EditText) findViewById(R.id.etSecuritycode);

        tvForgot = (TextView) findViewById(R.id.tvForgot);
        //btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit=(ImageView) findViewById(R.id.btnsubmit);
        tvToolBar=findViewById(R.id.tvToolBar);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);

        if (pref.getLanguage().equals("hi")) {
            tvToolBar.setText("पासवर्ड भूल गया");
            etUserId.setHint("अपनी उपयोगकर्ता आईडी दर्ज करें");
            etSecuritycode.setHint("अपना गुप्त कोड डालो");
            //tvForgot.setText("पासवर्ड भूल गए");
            //btnSubmit.setText("प्रस्तुत");
        } else {
            tvToolBar.setText("FORGOT PASSWORD");
            etUserId.setHint("Enter your userid");
            etSecuritycode.setHint("Enter your security code");
            //tvForgot.setText("Forgot Password");
           // btnSubmit.setText("SUBMIT");
        }
    }

    private void onClick() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.put("MasterID",etUserId.getText().toString());
                    jsonObject.put("SecurityCode",etSecuritycode.getText().toString());
                    forgotpassword(jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
              //  forgotpassword();
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

    private void forgotpassword(JSONObject jsonObject) {
        final ProgressDialog progressDialog=new ProgressDialog(ForgotPasswordActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        AndroidNetworking.post(Api.sForgotPasswordapi)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        progressDialog.dismiss();
                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);

                        String responseText = job1.optString("Response_Message");
                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                            successAlert(responseText);
                            // boolean _status = job1.getBoolean("status");
                            // do anything with response
                        }else {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        progressDialog.dismiss();
                    }
                });
    }

    private void successAlert(String text) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ForgotPasswordActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);

        tvInvalidDate.setText(text);


        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
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
