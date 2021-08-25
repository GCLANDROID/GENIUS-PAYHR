package com.genius.hrms.activity.profile;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genius.hrms.R;

import com.genius.hrms.activity.activity.EmployeeDashBoardActivity;
import com.genius.hrms.activity.activity.UserDashBoardActivity;
import com.genius.hrms.activity.utility.NetworkConnectionCheck;
import com.genius.hrms.activity.utility.Pref;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;


public class ProfileActivity extends AppCompatActivity {
    LinearLayout llOffical, llOffDetail, llCon, llConDetail, llPer, llPerDetail, llMis, llMisDetail;
    ImageView imgOffiPlus, imgOffiMinus, imgPerPlus, imgPerMinus, imgConPlus, imgConMinus, imgMisPlus, imgMisMinus;
    ImageView imgHome, imgBack;
    TextView tvEmplId, tvEmpCode, tvEmpName, tvDOJ, tvDepartment, tvDesignation, tvLocation, tvGender, tvEmpDOB, tvGurdianName, tvRealtionShip, tvQualification, tvMarital, tvBloodGroup;
    TextView tvParAddr, tvPreAddr, tvPhnNumber, tvEmail, tvPfNumber, tvEsiNumber, tvBankName, tvAcNumber, tvAddharNumber, tvUanNumber;
    Pref pref;
    String empConsId, empClinId, empClintOffId, empId;
    NetworkConnectionCheck connectionCheck;
    ImageView imgOffFor, imgOffFor1, imgPerFor, imgPerFor1, imgConFor, imgConFor1, imgMisFor, imgMisFor1;
    TextView tvOff, tvPer, tvCon, tvMis;
    LinearLayout llEmpId;
    TextView tvToolbar, tvPersonal, tvContact, tvToolBar;
    TextView tvUAN, tvAadhar, tvAc, tvBank, tvEsi, tvPf, tvEmailID, tvPhone, tvPreAdd, tvPerAdd, tvBlood, tvStatus, tvQuali, tvRelation, tvGName, tvDateoOB, tvGen, tvLoc, tvDes, tvDept, tvDateoJ, tvName, tvCode, tvId;
    String surl;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initialize();

        if (connectionCheck.isNetworkAvailable()) {
            profileFunction();
        } else {
            connectionCheck.getNetworkActiveAlert().show();
        }

        onclick();
    }

    private void initialize() {
        pref = new Pref(ProfileActivity.this);
        connectionCheck = new NetworkConnectionCheck(this);

        llOffical = (LinearLayout) findViewById(R.id.llOffical);
        llOffDetail = (LinearLayout) findViewById(R.id.llOffiDetail);

        llCon = (LinearLayout) findViewById(R.id.llContact);
        llConDetail = (LinearLayout) findViewById(R.id.llContactDetail);

        llPer = (LinearLayout) findViewById(R.id.llPersonal);
        llPerDetail = (LinearLayout) findViewById(R.id.llPersDetail);

        llMis = (LinearLayout) findViewById(R.id.llMis);
        llMisDetail = (LinearLayout) findViewById(R.id.llMisDetail);

        imgOffiPlus = (ImageView) findViewById(R.id.imgOffiPlus);
        imgOffiMinus = (ImageView) findViewById(R.id.imgOffiMius);

        imgPerPlus = (ImageView) findViewById(R.id.imgpersPlus);
        imgPerMinus = (ImageView) findViewById(R.id.imgPersMinus);

        imgConPlus = (ImageView) findViewById(R.id.imgConPlus);
        imgConMinus = (ImageView) findViewById(R.id.imgConMinus);

        imgMisPlus = (ImageView) findViewById(R.id.imgMisPlus);
        imgMisMinus = (ImageView) findViewById(R.id.imgMisMinus);

        imgHome = (ImageView) findViewById(R.id.imgHome);
        imgBack = (ImageView) findViewById(R.id.imgBack);

        tvEmplId = (TextView) findViewById(R.id.tvEmplId);
        tvEmpCode = (TextView) findViewById(R.id.tvEmpCode);
        tvEmpName = (TextView) findViewById(R.id.tvEmpName);
        tvDOJ = (TextView) findViewById(R.id.tvDOJ);
        tvDepartment = (TextView) findViewById(R.id.tvDepartment);
        tvDesignation = (TextView) findViewById(R.id.tvDesignation);
        tvLocation = (TextView) findViewById(R.id.tvLocation);


        tvGender = (TextView) findViewById(R.id.tvGender);
        tvEmpDOB = (TextView) findViewById(R.id.tvEmpCodeDOB);
        tvGurdianName = (TextView) findViewById(R.id.tvGurdianName);
        tvRealtionShip = (TextView) findViewById(R.id.tvRealtionShip);
        tvQualification = (TextView) findViewById(R.id.tvQualification);
        tvMarital = (TextView) findViewById(R.id.tvMarital);
        tvBloodGroup = (TextView) findViewById(R.id.tvBloodGroup);

        tvParAddr = (TextView) findViewById(R.id.tvParAddr);
        tvPreAddr = (TextView) findViewById(R.id.tvPreAddr);
        tvPhnNumber = (TextView) findViewById(R.id.tvPhnNumber);
        tvEmail = (TextView) findViewById(R.id.tvEmail);

        tvPfNumber = (TextView) findViewById(R.id.tvPfNumber);
        tvEsiNumber = (TextView) findViewById(R.id.tvEsiNumber);
        tvAcNumber = (TextView) findViewById(R.id.tvAcNumber);
        tvBankName = (TextView) findViewById(R.id.tvBankName);
        tvAddharNumber = (TextView) findViewById(R.id.tvAddharNumber);
        tvUanNumber = (TextView) findViewById(R.id.tvUanNumber);
        empConsId = pref.getEmpConId();
        Log.d("empConsId", empConsId);
        empClinId = pref.getEmpClintId();
        Log.d("empClinId", empClinId);
        empClintOffId = pref.getEmpClintOffId();
        Log.d("empClintOffId", empClintOffId);
        empId = pref.getEmpId();
        Log.d("empId", empId);

        int month = Calendar.getInstance().get(Calendar.MONTH);
        Log.d("month", String.valueOf(month));

        imgPerFor = (ImageView) findViewById(R.id.imgPerFor);
        imgPerFor1 = (ImageView) findViewById(R.id.imgPerFor1);
        imgOffFor = (ImageView) findViewById(R.id.imgOffFor);
        imgOffFor1 = (ImageView) findViewById(R.id.imgOffFor1);
        imgConFor = (ImageView) findViewById(R.id.imgConFor);
        imgConFor1 = (ImageView) findViewById(R.id.imgConFor1);
        imgMisFor = (ImageView) findViewById(R.id.imgMisFor);
        imgMisFor1 = (ImageView) findViewById(R.id.imgMisFor1);

        tvOff = (TextView) findViewById(R.id.tvOfficial);
        tvPer = (TextView) findViewById(R.id.tvPersonal);
        tvCon = (TextView) findViewById(R.id.tvContact);
        tvMis = (TextView) findViewById(R.id.tvMis);
        llEmpId = (LinearLayout) findViewById(R.id.llEmpId);
        if (pref.getSecurityCode().equals("1070")) {
            llEmpId.setVisibility(View.GONE);
        } else {
            llEmpId.setVisibility(View.GONE);
        }
        tvToolBar = (TextView) findViewById(R.id.tvToolBar);
        tvUAN = (TextView) findViewById(R.id.tvUAN);
        tvAadhar = (TextView) findViewById(R.id.tvAadhar);
        tvAc = (TextView) findViewById(R.id.tvAc);
        tvBank = (TextView) findViewById(R.id.tvBank);
        tvEsi = (TextView) findViewById(R.id.tvEsi);
        tvPf = (TextView) findViewById(R.id.tvPf);
        tvEmailID = (TextView) findViewById(R.id.tvEmailID);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvPreAdd = (TextView) findViewById(R.id.tvPreAdd);
        tvPerAdd = (TextView) findViewById(R.id.tvPerAdd);
        tvBlood = (TextView) findViewById(R.id.tvBlood);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        tvQuali = (TextView) findViewById(R.id.tvQuali);
        tvRelation = (TextView) findViewById(R.id.tvRelation);
        tvGName = (TextView) findViewById(R.id.tvGName);
        tvDateoOB = (TextView) findViewById(R.id.tvDateoOB);
        tvGen = (TextView) findViewById(R.id.tvGen);
        tvLoc = (TextView) findViewById(R.id.tvLoc);
        tvDes = (TextView) findViewById(R.id.tvDes);
        tvDept = (TextView) findViewById(R.id.tvDept);
        tvDateoJ = (TextView) findViewById(R.id.tvDateoJ);
        tvName = (TextView) findViewById(R.id.tvName);
        tvCode = (TextView) findViewById(R.id.tvCode);
        tvId = (TextView) findViewById(R.id.tvId);


        if (pref.getLanguage().equals("hi")) {
            tvOff.setText("आधिकारिक विवरण");
            tvPer.setText("व्यक्तिगत जानकारी");
            tvCon.setText("संपर्क विवरण");
            tvMis.setText("विविध");
            tvToolBar.setText("प्रोफ़ाइल");

            tvId.setText("आयडी");
            tvCode.setText("कोड");
            tvName.setText("नाम");
            tvDateoJ.setText("जुड़ने की तारीख");
            tvDept.setText("विभाग");
            tvDes.setText("पद");
            tvLoc.setText("स्थान");
            tvGen.setText("लिंग");
            tvDateoOB.setText("जन्म की तारीख");
            tvGName.setText("अभिभावक का नाम");
            tvRelation.setText("संबंध");
            tvQuali.setText("योग्यता");
            tvStatus.setText("वैवाहिक स्थिति");
            tvBlood.setText("रक्त समूह");
            tvPerAdd.setText("स्थाई पता");
            tvPreAdd.setText("वर्तमान पता");
            tvPhone.setText("फ़ोन नंबर");
            tvEmailID.setText("ईमेल आईडी");
            tvPf.setText("पीएफ नंबर");
            tvEsi.setText("ईएसआई नंबर");
            tvBank.setText("बैंक का नाम");
            tvAc.setText("खाता संख्या");
            tvAadhar.setText("आधार संख्या");
            tvUAN.setText("यूएएन नंबर");
        } else {
            tvOff.setText("Official");
            tvPer.setText("Personal");
            tvCon.setText("Contact");
            tvMis.setText("Miscellaneous");
            tvToolBar.setText("Profile");

            tvId.setText("Id");
            tvCode.setText("Code");
            tvName.setText("Name");
            tvDateoJ.setText("Date of joining");
            tvDept.setText("Department");
            tvDes.setText("Designation");
            tvLoc.setText("Location");
            tvGen.setText("Gender");
            tvDateoOB.setText("Date of birth");
            tvGName.setText("Gurdian name");
            tvRelation.setText("Relationship");
            tvQuali.setText("Qualification");
            tvStatus.setText("Martial status");
            tvBlood.setText("Blood group");
            tvPerAdd.setText("Permanent address");
            tvPreAdd.setText("present address");
            tvPhone.setText("Phone number");
            tvEmailID.setText("Email Id");
            tvPf.setText("PF number");
            tvEsi.setText("ESI number");
            tvBank.setText("Bank name");
            tvAc.setText("A/C number");
            tvAadhar.setText("Aadhar number");
            tvUAN.setText("UAN number");
        }


    }

    private void onclick() {
        llOffical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgOffiPlus.getVisibility() == view.VISIBLE) {
                    imgOffiPlus.setVisibility(View.GONE);
                    imgOffiMinus.setVisibility(View.VISIBLE);
                    llOffDetail.setVisibility(View.VISIBLE);
                    llPerDetail.setVisibility(View.GONE);
                    imgPerPlus.setVisibility(View.VISIBLE);
                    imgConPlus.setVisibility(View.VISIBLE);
                    llConDetail.setVisibility(View.GONE);
                    llMisDetail.setVisibility(View.GONE);
                    imgMisPlus.setVisibility(View.VISIBLE);
                    imgOffFor.setVisibility(View.GONE);
                    imgOffFor1.setVisibility(View.VISIBLE);
                    imgPerFor.setVisibility(View.VISIBLE);
                    imgPerFor1.setVisibility(View.GONE);
                    imgConFor.setVisibility(View.VISIBLE);
                    imgConFor1.setVisibility(View.GONE);
                    imgMisFor.setVisibility(View.VISIBLE);
                    imgMisFor1.setVisibility(View.GONE);
                    llOffical.setBackgroundColor(Color.parseColor("#075994"));
                    llPer.setBackgroundColor(Color.parseColor("#c8c9ca"));
                    llCon.setBackgroundColor(Color.parseColor("#c8c9ca"));
                    llMis.setBackgroundColor(Color.parseColor("#c8c9ca"));
                    tvOff.setTextColor(Color.parseColor("#ffffff"));
                    tvPer.setTextColor(Color.parseColor("#000000"));
                    tvCon.setTextColor(Color.parseColor("#000000"));
                    tvMis.setTextColor(Color.parseColor("#000000"));

                } else {
                    imgOffiPlus.setVisibility(View.VISIBLE);
                    imgOffiMinus.setVisibility(View.GONE);
                    llOffDetail.setVisibility(View.GONE);
                    llPerDetail.setVisibility(View.GONE);
                    imgPerPlus.setVisibility(View.VISIBLE);
                    imgConPlus.setVisibility(View.VISIBLE);
                    llConDetail.setVisibility(View.GONE);
                    llMisDetail.setVisibility(View.GONE);
                    imgMisPlus.setVisibility(View.VISIBLE);
                    imgOffFor.setVisibility(View.VISIBLE);
                    imgOffFor1.setVisibility(View.GONE);
                    imgPerFor.setVisibility(View.VISIBLE);
                    imgPerFor1.setVisibility(View.GONE);
                    imgConFor.setVisibility(View.VISIBLE);
                    imgConFor1.setVisibility(View.GONE);
                    imgMisFor.setVisibility(View.VISIBLE);
                    imgMisFor1.setVisibility(View.GONE);
                    llOffical.setBackgroundColor(Color.parseColor("#c8c9ca"));
                    llPer.setBackgroundColor(Color.parseColor("#c8c9ca"));
                    llCon.setBackgroundColor(Color.parseColor("#c8c9ca"));
                    llMis.setBackgroundColor(Color.parseColor("#c8c9ca"));
                    tvOff.setTextColor(Color.parseColor("#000000"));
                    tvPer.setTextColor(Color.parseColor("#000000"));
                    tvCon.setTextColor(Color.parseColor("#000000"));
                    tvMis.setTextColor(Color.parseColor("#000000"));


                }
            }
        });

        llPer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgPerPlus.getVisibility() == view.VISIBLE) {
                    imgPerPlus.setVisibility(View.GONE);
                    imgPerMinus.setVisibility(View.VISIBLE);
                    llPerDetail.setVisibility(View.VISIBLE);
                    llOffDetail.setVisibility(View.GONE);
                    imgOffiPlus.setVisibility(View.VISIBLE);
                    imgConPlus.setVisibility(View.VISIBLE);
                    llConDetail.setVisibility(View.GONE);
                    llMisDetail.setVisibility(View.GONE);
                    imgMisPlus.setVisibility(View.VISIBLE);
                    imgOffFor.setVisibility(View.VISIBLE);
                    imgOffFor1.setVisibility(View.GONE);
                    imgPerFor.setVisibility(View.GONE);
                    imgPerFor1.setVisibility(View.VISIBLE);
                    imgConFor.setVisibility(View.VISIBLE);
                    imgConFor1.setVisibility(View.GONE);
                    imgMisFor.setVisibility(View.VISIBLE);
                    imgMisFor1.setVisibility(View.GONE);
                    llOffical.setBackgroundColor(Color.parseColor("#c8c9ca"));
                    llPer.setBackgroundColor(Color.parseColor("#075994"));
                    llCon.setBackgroundColor(Color.parseColor("#c8c9ca"));
                    llMis.setBackgroundColor(Color.parseColor("#c8c9ca"));
                    tvOff.setTextColor(Color.parseColor("#000000"));
                    tvPer.setTextColor(Color.parseColor("#ffffff"));
                    tvCon.setTextColor(Color.parseColor("#000000"));
                    tvMis.setTextColor(Color.parseColor("#000000"));


                } else {
                    imgPerPlus.setVisibility(View.VISIBLE);
                    imgPerMinus.setVisibility(View.GONE);
                    llPerDetail.setVisibility(View.GONE);
                    llOffDetail.setVisibility(View.GONE);
                    imgOffiPlus.setVisibility(View.VISIBLE);
                    imgConPlus.setVisibility(View.VISIBLE);
                    llConDetail.setVisibility(View.GONE);
                    llMisDetail.setVisibility(View.GONE);
                    imgMisPlus.setVisibility(View.VISIBLE);
                    imgOffFor.setVisibility(View.VISIBLE);
                    imgOffFor1.setVisibility(View.GONE);
                    imgPerFor.setVisibility(View.VISIBLE);
                    imgPerFor1.setVisibility(View.GONE);
                    imgConFor.setVisibility(View.VISIBLE);
                    imgConFor1.setVisibility(View.GONE);
                    imgMisFor.setVisibility(View.VISIBLE);
                    imgMisFor1.setVisibility(View.GONE);
                    llOffical.setBackgroundColor(Color.parseColor("#c8c9ca"));
                    llPer.setBackgroundColor(Color.parseColor("#c8c9ca"));
                    llCon.setBackgroundColor(Color.parseColor("#c8c9ca"));
                    llMis.setBackgroundColor(Color.parseColor("#c8c9ca"));
                    tvOff.setTextColor(Color.parseColor("#000000"));
                    tvPer.setTextColor(Color.parseColor("#000000"));
                    tvCon.setTextColor(Color.parseColor("#000000"));
                    tvMis.setTextColor(Color.parseColor("#000000"));


                }
            }
        });

        llCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgConPlus.getVisibility() == view.VISIBLE) {
                    imgConPlus.setVisibility(View.GONE);
                    imgConMinus.setVisibility(View.VISIBLE);
                    llConDetail.setVisibility(View.VISIBLE);
                    imgOffiPlus.setVisibility(View.VISIBLE);
                    llOffDetail.setVisibility(View.GONE);
                    llPerDetail.setVisibility(View.GONE);
                    imgPerPlus.setVisibility(View.VISIBLE);
                    llMisDetail.setVisibility(View.GONE);
                    imgMisPlus.setVisibility(View.VISIBLE);
                    imgOffFor.setVisibility(View.VISIBLE);
                    imgOffFor1.setVisibility(View.GONE);
                    imgPerFor.setVisibility(View.VISIBLE);
                    imgPerFor1.setVisibility(View.GONE);
                    imgConFor.setVisibility(View.GONE);
                    imgConFor1.setVisibility(View.VISIBLE);
                    imgMisFor.setVisibility(View.VISIBLE);
                    imgMisFor1.setVisibility(View.GONE);
                    llOffical.setBackgroundColor(Color.parseColor("#c8c9ca"));
                    llPer.setBackgroundColor(Color.parseColor("#c8c9ca"));
                    llCon.setBackgroundColor(Color.parseColor("#075994"));
                    llMis.setBackgroundColor(Color.parseColor("#c8c9ca"));
                    tvOff.setTextColor(Color.parseColor("#000000"));
                    tvPer.setTextColor(Color.parseColor("#000000"));
                    tvCon.setTextColor(Color.parseColor("#ffffff"));
                    tvMis.setTextColor(Color.parseColor("#000000"));

                } else {
                    imgConPlus.setVisibility(View.VISIBLE);
                    imgConMinus.setVisibility(View.GONE);
                    llConDetail.setVisibility(View.GONE);
                    imgOffiPlus.setVisibility(View.VISIBLE);
                    llOffDetail.setVisibility(View.GONE);
                    llPerDetail.setVisibility(View.GONE);
                    imgPerPlus.setVisibility(View.VISIBLE);
                    llMisDetail.setVisibility(View.GONE);
                    imgMisPlus.setVisibility(View.VISIBLE);
                    imgOffFor.setVisibility(View.VISIBLE);
                    imgOffFor1.setVisibility(View.GONE);
                    imgPerFor.setVisibility(View.VISIBLE);
                    imgPerFor1.setVisibility(View.GONE);
                    imgConFor.setVisibility(View.VISIBLE);
                    imgConFor1.setVisibility(View.GONE);
                    imgMisFor.setVisibility(View.VISIBLE);
                    imgMisFor1.setVisibility(View.GONE);
                    llOffical.setBackgroundColor(Color.parseColor("#c8c9ca"));
                    llPer.setBackgroundColor(Color.parseColor("#c8c9ca"));
                    llCon.setBackgroundColor(Color.parseColor("#c8c9ca"));
                    llMis.setBackgroundColor(Color.parseColor("#c8c9ca"));
                    tvOff.setTextColor(Color.parseColor("#000000"));
                    tvPer.setTextColor(Color.parseColor("#000000"));
                    tvCon.setTextColor(Color.parseColor("#000000"));
                    tvMis.setTextColor(Color.parseColor("#000000"));

                }
            }
        });

        llMis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgMisPlus.getVisibility() == View.VISIBLE) {
                    imgMisPlus.setVisibility(View.GONE);
                    llMisDetail.setVisibility(View.VISIBLE);
                    imgMisMinus.setVisibility(View.VISIBLE);
                    imgOffiPlus.setVisibility(View.VISIBLE);
                    imgConPlus.setVisibility(View.VISIBLE);
                    imgPerPlus.setVisibility(View.VISIBLE);
                    llOffDetail.setVisibility(View.GONE);
                    llPerDetail.setVisibility(View.GONE);
                    llConDetail.setVisibility(View.GONE);
                    imgOffFor.setVisibility(View.VISIBLE);
                    imgOffFor1.setVisibility(View.GONE);
                    imgPerFor.setVisibility(View.VISIBLE);
                    imgPerFor1.setVisibility(View.GONE);
                    imgConFor.setVisibility(View.VISIBLE);
                    imgConFor1.setVisibility(View.GONE);
                    imgMisFor.setVisibility(View.GONE);
                    imgMisFor1.setVisibility(View.VISIBLE);
                    llOffical.setBackgroundColor(Color.parseColor("#c8c9ca"));
                    llPer.setBackgroundColor(Color.parseColor("#c8c9ca"));
                    llCon.setBackgroundColor(Color.parseColor("#c8c9ca"));
                    llMis.setBackgroundColor(Color.parseColor("#075994"));
                    tvOff.setTextColor(Color.parseColor("#000000"));
                    tvPer.setTextColor(Color.parseColor("#000000"));
                    tvCon.setTextColor(Color.parseColor("#000000"));
                    tvMis.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    imgMisPlus.setVisibility(View.VISIBLE);
                    llMisDetail.setVisibility(View.GONE);
                    imgMisMinus.setVisibility(View.GONE);
                    imgOffiPlus.setVisibility(View.VISIBLE);
                    imgConPlus.setVisibility(View.VISIBLE);
                    imgPerPlus.setVisibility(View.VISIBLE);
                    llOffDetail.setVisibility(View.GONE);
                    llPerDetail.setVisibility(View.GONE);
                    llConDetail.setVisibility(View.GONE);
                    imgOffFor.setVisibility(View.VISIBLE);
                    imgOffFor1.setVisibility(View.GONE);
                    imgPerFor.setVisibility(View.VISIBLE);
                    imgPerFor1.setVisibility(View.GONE);
                    imgConFor.setVisibility(View.VISIBLE);
                    imgConFor1.setVisibility(View.GONE);
                    imgMisFor.setVisibility(View.VISIBLE);
                    imgMisFor1.setVisibility(View.GONE);
                    llOffical.setBackgroundColor(Color.parseColor("#c8c9ca"));
                    llPer.setBackgroundColor(Color.parseColor("#c8c9ca"));
                    llCon.setBackgroundColor(Color.parseColor("#c8c9ca"));
                    llMis.setBackgroundColor(Color.parseColor("#c8c9ca"));
                    tvOff.setTextColor(Color.parseColor("#000000"));
                    tvPer.setTextColor(Color.parseColor("#000000"));
                    tvCon.setTextColor(Color.parseColor("#000000"));
                    tvMis.setTextColor(Color.parseColor("#000000"));
                }
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    public void profileFunction() {
        if (pref.getSecurityCode().equals("11")||pref.getSecurityCode().equals("123")){
            surl ="http://111.93.182.174/GeniusiOSApi/api/gcl_KYC?AEMConsultantID="+pref.getEmpConId()+"&AEMClientID="+pref.getEmpClintId()+"&AEMClientOfficeID="+pref.getEmpClintOffId()+"&AEMEmployeeID="+pref.getEmpId() +"&SecurityCode="+pref.getSecurityCode()+"&WorkingStatus=1&CurrentPage=0";

        }else {

            surl = pref.getIpAddress() + "GHRMSApi/api/GCLKYC_New?AEMConsultantID=" + pref.getEmpConId() + "&AEMClientID=" + pref.getEmpClintId() + "&AEMClientOfficeID=" + pref.getEmpClintOffId() + "&AEMEmployeeID=" + pref.getEmpId() + "&WorkingStatus=1&CurrentPage=1&SecurityCode=" + pref.getSecurityCode();
        }
        Log.d("kyc", surl);
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        progressBar.dismiss();
                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                //   Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    //AEEMPID
                                    final String AEMEmployeeID = obj.optString("AEMEmployeeID");
                                    // tvEmplId.setText(AEMEmployeeID);
                                    final String ID = AEMEmployeeID;
                                    tvEmplId.setText(ID);


                                    //code feild
                                    final String Code = obj.optString("Code");

                                    if (pref.getLanguage().equals("hi")) {
                                        final Handler textViewHandler1 = new Handler();
                                        new AsyncTask<Void, Void, Void>() {
                                            @Override
                                            protected Void doInBackground(Void... params) {
                                                TranslateOptions options = TranslateOptions.newBuilder()
                                                        .setApiKey("AIzaSyDL1itt-7WRkrelJeuvOfiC-_SGc3JZ4vY")
                                                        .build();
                                                Translate translate = options.getService();
                                                final Translation translation =
                                                        translate.translate(Code,
                                                                Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                                                textViewHandler1.post(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        Log.d("sssh", translation.getTranslatedText());
                                                        String hCode = translation.getTranslatedText();
                                                        tvEmpCode.setText(hCode);

                                                    }
                                                });
                                                return null;
                                            }

                                            @Override
                                            protected void onPreExecute() {
                                                super.onPreExecute();
                                                progressBar.show();
                                            }

                                            @Override
                                            protected void onPostExecute(Void aVoid) {
                                                super.onPostExecute(aVoid);
                                                progressBar.show();
                                            }


                                        }.execute();
                                    } else {
                                        tvEmpCode.setText(Code);
                                    }


                                    //Name field
                                    String Name = obj.optString("Name");
                                    tvEmpName.setText(Name);


                                    //DOJ

                                    final String DateOfJoining = obj.optString("DateOfJoining");
                                    tvDOJ.setText(DateOfJoining);


                                    final String Department = obj.optString("Department");
                                    if (pref.getLanguage().equals("hi")) {
                                        final Handler textViewHandler4 = new Handler();
                                        new AsyncTask<Void, Void, Void>() {
                                            @Override
                                            protected Void doInBackground(Void... params) {
                                                TranslateOptions options = TranslateOptions.newBuilder()
                                                        .setApiKey("AIzaSyDL1itt-7WRkrelJeuvOfiC-_SGc3JZ4vY")
                                                        .build();
                                                Translate translate = options.getService();
                                                final Translation translation =
                                                        translate.translate(Department,
                                                              Translate.TranslateOption.sourceLanguage("en"),   Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                                                textViewHandler4.post(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        Log.d("sssh", translation.getTranslatedText());
                                                        String h = translation.getTranslatedText();
                                                        tvDepartment.setText(h);

                                                    }
                                                });
                                                return null;
                                            }

                                            @Override
                                            protected void onPreExecute() {
                                                super.onPreExecute();
                                                progressBar.show();
                                            }

                                            @Override
                                            protected void onPostExecute(Void aVoid) {
                                                super.onPostExecute(aVoid);
                                                progressBar.show();
                                            }


                                        }.execute();
                                    } else {
                                        tvDepartment.setText(Department);
                                    }

                                    final String Designation = obj.optString("Designation");
                                    if (pref.getLanguage().equals("hi")) {
                                        final Handler textViewHandler5 = new Handler();
                                        new AsyncTask<Void, Void, Void>() {
                                            @Override
                                            protected Void doInBackground(Void... params) {
                                                TranslateOptions options = TranslateOptions.newBuilder()
                                                        .setApiKey("AIzaSyDL1itt-7WRkrelJeuvOfiC-_SGc3JZ4vY")
                                                        .build();
                                                Translate translate = options.getService();
                                                final Translation translation =
                                                        translate.translate(Designation,
                                                                Translate.TranslateOption.sourceLanguage("en"),  Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                                                textViewHandler5.post(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        Log.d("sssh", translation.getTranslatedText());
                                                        String h = translation.getTranslatedText();
                                                        tvDesignation.setText(h);

                                                    }
                                                });
                                                return null;
                                            }

                                            @Override
                                            protected void onPreExecute() {
                                                super.onPreExecute();
                                                progressBar.show();
                                            }

                                            @Override
                                            protected void onPostExecute(Void aVoid) {
                                                super.onPostExecute(aVoid);
                                                progressBar.show();
                                            }


                                        }.execute();

                                    } else {
                                        tvDesignation.setText(Designation);
                                    }

                                    final String Location = obj.optString("Location");

                                    if (pref.getLanguage().equals("hi")) {
                                        final Handler textViewHandler6 = new Handler();
                                        new AsyncTask<Void, Void, Void>() {
                                            @Override
                                            protected Void doInBackground(Void... params) {
                                                TranslateOptions options = TranslateOptions.newBuilder()
                                                        .setApiKey("AIzaSyDL1itt-7WRkrelJeuvOfiC-_SGc3JZ4vY")
                                                        .build();
                                                Translate translate = options.getService();
                                                final Translation translation =
                                                        translate.translate(Location,
                                                                Translate.TranslateOption.sourceLanguage("en"),   Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                                                textViewHandler6.post(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        Log.d("sssh", translation.getTranslatedText());
                                                        String h = translation.getTranslatedText();
                                                        tvLocation.setText(h);

                                                    }
                                                });
                                                return null;
                                            }

                                            @Override
                                            protected void onPreExecute() {
                                                super.onPreExecute();
                                                progressBar.show();
                                            }

                                            @Override
                                            protected void onPostExecute(Void aVoid) {
                                                super.onPostExecute(aVoid);
                                                progressBar.show();
                                            }


                                        }.execute();
                                    } else {
                                        tvLocation.setText(Location);
                                    }

                                    final String Sex = obj.optString("Sex");
                                    if (pref.getLanguage().equals("hi")) {
                                        final Handler textViewHandler7 = new Handler();
                                        new AsyncTask<Void, Void, Void>() {
                                            @Override
                                            protected Void doInBackground(Void... params) {
                                                TranslateOptions options = TranslateOptions.newBuilder()
                                                        .setApiKey("AIzaSyDL1itt-7WRkrelJeuvOfiC-_SGc3JZ4vY")
                                                        .build();
                                                Translate translate = options.getService();
                                                final Translation translation =
                                                        translate.translate(Sex,
                                                                Translate.TranslateOption.sourceLanguage("en"),   Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                                                textViewHandler7.post(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        Log.d("sssh", translation.getTranslatedText());
                                                        String h = translation.getTranslatedText();
                                                        tvGender.setText(h);

                                                    }
                                                });
                                                return null;
                                            }

                                            @Override
                                            protected void onPreExecute() {
                                                super.onPreExecute();
                                                progressBar.show();
                                            }

                                            @Override
                                            protected void onPostExecute(Void aVoid) {
                                                super.onPostExecute(aVoid);
                                                progressBar.show();
                                            }


                                        }.execute();
                                    } else {
                                        tvGender.setText(Sex);
                                    }

                                    final String DateOfBirth = obj.optString("DateOfBirth");
                                    tvEmpDOB.setText(DateOfBirth);


                                    final String GuardianName = obj.optString("GuardianName");
                                    if (pref.getLanguage().equals("hi")) {
                                        final Handler textViewHandler9 = new Handler();
                                        new AsyncTask<Void, Void, Void>() {
                                            @Override
                                            protected Void doInBackground(Void... params) {
                                                TranslateOptions options = TranslateOptions.newBuilder()
                                                        .setApiKey("AIzaSyDL1itt-7WRkrelJeuvOfiC-_SGc3JZ4vY")
                                                        .build();
                                                Translate translate = options.getService();
                                                final Translation translation =
                                                        translate.translate(GuardianName,
                                                                Translate.TranslateOption.sourceLanguage("en"),   Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                                                textViewHandler9.post(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        Log.d("sssh", translation.getTranslatedText());
                                                        String h = translation.getTranslatedText();
                                                        tvGurdianName.setText(h);

                                                    }
                                                });
                                                return null;
                                            }

                                            @Override
                                            protected void onPreExecute() {
                                                super.onPreExecute();
                                                progressBar.show();
                                            }

                                            @Override
                                            protected void onPostExecute(Void aVoid) {
                                                super.onPostExecute(aVoid);
                                                progressBar.show();
                                            }


                                        }.execute();
                                    } else {
                                        tvGurdianName.setText(GuardianName);
                                    }

                                    final String RelationShip = obj.optString("RelationShip");
                                    if (pref.getLanguage().equals("hi")) {
                                        final Handler textViewHandler10 = new Handler();
                                        new AsyncTask<Void, Void, Void>() {
                                            @Override
                                            protected Void doInBackground(Void... params) {
                                                TranslateOptions options = TranslateOptions.newBuilder()
                                                        .setApiKey("AIzaSyDL1itt-7WRkrelJeuvOfiC-_SGc3JZ4vY")
                                                        .build();
                                                Translate translate = options.getService();
                                                final Translation translation =
                                                        translate.translate(RelationShip,
                                                                Translate.TranslateOption.sourceLanguage("en"),  Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                                                textViewHandler10.post(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        Log.d("sssh", translation.getTranslatedText());
                                                        String h = translation.getTranslatedText();
                                                        tvRealtionShip.setText(h);

                                                    }
                                                });
                                                return null;
                                            }

                                            @Override
                                            protected void onPreExecute() {
                                                super.onPreExecute();
                                                progressBar.show();
                                            }

                                            @Override
                                            protected void onPostExecute(Void aVoid) {
                                                super.onPostExecute(aVoid);
                                                progressBar.show();
                                            }


                                        }.execute();
                                    } else {
                                        tvRealtionShip.setText(RelationShip);
                                    }


                                    final String Qualification = obj.optString("Qualification");
                                    if (pref.getLanguage().equals("hi")) {
                                        final Handler textViewHandler11 = new Handler();
                                        new AsyncTask<Void, Void, Void>() {
                                            @Override
                                            protected Void doInBackground(Void... params) {
                                                TranslateOptions options = TranslateOptions.newBuilder()
                                                        .setApiKey("AIzaSyDL1itt-7WRkrelJeuvOfiC-_SGc3JZ4vY")
                                                        .build();
                                                Translate translate = options.getService();
                                                final Translation translation =
                                                        translate.translate(Qualification,
                                                                Translate.TranslateOption.sourceLanguage("en"),   Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                                                textViewHandler11.post(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        Log.d("sssh", translation.getTranslatedText());
                                                        String h = translation.getTranslatedText();
                                                        tvQualification.setText(h);

                                                    }
                                                });
                                                return null;
                                            }

                                            @Override
                                            protected void onPreExecute() {
                                                super.onPreExecute();
                                                progressBar.show();
                                            }

                                            @Override
                                            protected void onPostExecute(Void aVoid) {
                                                super.onPostExecute(aVoid);
                                                progressBar.show();
                                            }
                                        }.execute();
                                    } else {
                                        tvQualification.setText(Qualification);
                                    }


                                    final String MaritalStatus = obj.optString("MaritalStatus");
                                    if (pref.getLanguage().equals("hi")) {
                                        final Handler textViewHandler12 = new Handler();
                                        new AsyncTask<Void, Void, Void>() {
                                            @Override
                                            protected Void doInBackground(Void... params) {
                                                TranslateOptions options = TranslateOptions.newBuilder()
                                                        .setApiKey("AIzaSyDL1itt-7WRkrelJeuvOfiC-_SGc3JZ4vY")
                                                        .build();
                                                Translate translate = options.getService();
                                                final Translation translation =
                                                        translate.translate(MaritalStatus,
                                                                Translate.TranslateOption.sourceLanguage("en"),  Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                                                textViewHandler12.post(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        Log.d("sssh", translation.getTranslatedText());
                                                        String h = translation.getTranslatedText();
                                                        tvMarital.setText(h);

                                                    }
                                                });
                                                return null;
                                            }

                                            @Override
                                            protected void onPreExecute() {
                                                super.onPreExecute();
                                                progressBar.show();
                                            }

                                            @Override
                                            protected void onPostExecute(Void aVoid) {
                                                super.onPostExecute(aVoid);
                                                progressBar.show();
                                            }


                                        }.execute();
                                    } else {
                                        tvMarital.setText(MaritalStatus);
                                    }

                                    final String BloodGroup = obj.optString("BloodGroup");
                                    if (pref.getLanguage().equals("hi")) {
                                        final Handler textViewHandler13 = new Handler();
                                        new AsyncTask<Void, Void, Void>() {
                                            @Override
                                            protected Void doInBackground(Void... params) {
                                                TranslateOptions options = TranslateOptions.newBuilder()
                                                        .setApiKey("AIzaSyDL1itt-7WRkrelJeuvOfiC-_SGc3JZ4vY")
                                                        .build();
                                                Translate translate = options.getService();
                                                final Translation translation =
                                                        translate.translate(BloodGroup,
                                                                Translate.TranslateOption.sourceLanguage("en"),   Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                                                textViewHandler13.post(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        Log.d("sssh", translation.getTranslatedText());
                                                        String h = translation.getTranslatedText();
                                                        tvBloodGroup.setText(h);

                                                    }
                                                });
                                                return null;
                                            }

                                            @Override
                                            protected void onPreExecute() {
                                                super.onPreExecute();
                                                progressBar.show();
                                            }

                                            @Override
                                            protected void onPostExecute(Void aVoid) {
                                                super.onPostExecute(aVoid);
                                                progressBar.show();
                                            }


                                        }.execute();
                                    } else {
                                        tvBloodGroup.setText(BloodGroup);
                                    }

                                    final String permanentpincode=obj.optString("PermanentPinCode");

                                    final String PermanentAddress = obj.optString("PermanentAddress");
                                    if (pref.getLanguage().equals("hi")) {
                                        final Handler textViewHandler14 = new Handler();
                                        new AsyncTask<Void, Void, Void>() {
                                            @Override
                                            protected Void doInBackground(Void... params) {
                                                TranslateOptions options = TranslateOptions.newBuilder()
                                                        .setApiKey("AIzaSyDL1itt-7WRkrelJeuvOfiC-_SGc3JZ4vY")
                                                        .build();
                                                Translate translate = options.getService();
                                                final Translation translation =
                                                        translate.translate(PermanentAddress,
                                                                Translate.TranslateOption.sourceLanguage("en"),  Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                                                textViewHandler14.post(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        Log.d("sssh", translation.getTranslatedText());
                                                        String h = translation.getTranslatedText();
                                                        if (permanentpincode.equals("null")){
                                                            Log.d("null",permanentpincode);
                                                            tvParAddr.setText(h);
                                                        }else {
                                                            Log.d("value",permanentpincode);
                                                            tvParAddr.setText(h + "," + permanentpincode);
                                                        }

                                                    }
                                                });
                                                return null;
                                            }

                                            @Override
                                            protected void onPreExecute() {
                                                super.onPreExecute();
                                                progressBar.show();
                                            }

                                            @Override
                                            protected void onPostExecute(Void aVoid) {
                                                super.onPostExecute(aVoid);
                                                progressBar.show();
                                            }


                                        }.execute();
                                    } else {
                                        if (permanentpincode.equals("null")){
                                            Log.d("null",permanentpincode);
                                            tvParAddr.setText(PermanentAddress);
                                        }else {
                                            Log.d("value",permanentpincode);
                                            tvParAddr.setText(PermanentAddress + "," + permanentpincode);
                                        }
                                       // tvParAddr.setText(PermanentAddress+","+permanentpincode);
                                    }


                                    final  String presentpincode=obj.optString("PresentPincode");


                                    final String PresentAddress = obj.optString("PresentAddress");
                                    if (pref.getLanguage().equals("hi")) {
                                        final Handler textViewHandler15 = new Handler();
                                        new AsyncTask<Void, Void, Void>() {
                                            @Override
                                            protected Void doInBackground(Void... params) {
                                                TranslateOptions options = TranslateOptions.newBuilder()
                                                        .setApiKey("AIzaSyDL1itt-7WRkrelJeuvOfiC-_SGc3JZ4vY")
                                                        .build();
                                                Translate translate = options.getService();
                                                final Translation translation =
                                                        translate.translate(PresentAddress,
                                                                Translate.TranslateOption.sourceLanguage("en"),   Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                                                textViewHandler15.post(new Runnable() {
                                                    @SuppressLint("StaticFieldLeak")
                                                    @Override
                                                    public void run() {

                                                        Log.d("sssh", translation.getTranslatedText());
                                                        String h = translation.getTranslatedText();
                                                        if (presentpincode.equals("null")){
                                                            Log.d("null",presentpincode);
                                                            tvPreAddr.setText(h);
                                                        }else{
                                                            Log.d("value",presentpincode);
                                                            tvPreAddr.setText(h+","+presentpincode);
                                                        }


                                                    }
                                                });
                                                return null;
                                            }

                                            @Override
                                            protected void onPreExecute() {
                                                super.onPreExecute();
                                                progressBar.show();
                                            }

                                            @Override
                                            protected void onPostExecute(Void aVoid) {
                                                super.onPostExecute(aVoid);
                                                progressBar.show();
                                            }


                                        }.execute();
                                    } else {
                                        if (presentpincode.equals("null")){
                                            Log.d("null",presentpincode);
                                            tvPreAddr.setText(PresentAddress);
                                        }else{
                                            Log.d("value",presentpincode);
                                            tvPreAddr.setText(PresentAddress+","+presentpincode);
                                        }
                                        //tvPreAddr.setText(PresentAddress+","+presentpincode);
                                    }

                                    String Mobile = obj.optString("Mobile");
                                    if (!Mobile.equals("")) {
                                        tvPhnNumber.setText(Mobile);
                                    } else {
                                        tvPhnNumber.setText("N/A");
                                    }

                                    final String EmailID = obj.optString("EmailID");

                                    tvEmail.setText(EmailID);


                                    String PFNumber = obj.optString("PFNumber");
                                    if (!PFNumber.equals("")) {
                                        tvPfNumber.setText(PFNumber);
                                    } else {
                                        tvPfNumber.setText("N/A");
                                    }

                                    String ESINumber = obj.optString("ESINumber");
                                    if (!ESINumber.equals("")) {
                                        tvEsiNumber.setText(ESINumber);
                                    }

                                    final String BankName = obj.optString("BanKName");
                                    if (pref.getLanguage().equals("hi")) {

                                        final Handler textViewHandler17 = new Handler();
                                        new AsyncTask<Void, Void, Void>() {
                                            @Override
                                            protected Void doInBackground(Void... params) {
                                                TranslateOptions options = TranslateOptions.newBuilder()
                                                        .setApiKey("AIzaSyDL1itt-7WRkrelJeuvOfiC-_SGc3JZ4vY")
                                                        .build();
                                                Translate translate = options.getService();
                                                final Translation translation =
                                                        translate.translate(BankName,
                                                                Translate.TranslateOption.sourceLanguage("en"),  Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                                                textViewHandler17.post(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        Log.d("sssh", translation.getTranslatedText());
                                                        String h = translation.getTranslatedText();
                                                        tvBankName.setText(h);

                                                    }
                                                });
                                                return null;
                                            }

                                            @Override
                                            protected void onPreExecute() {
                                                super.onPreExecute();
                                                progressBar.show();
                                            }

                                            @Override
                                            protected void onPostExecute(Void aVoid) {
                                                super.onPostExecute(aVoid);
                                                progressBar.dismiss();
                                            }


                                        }.execute();
                                    } else {
                                        tvBankName.setText(BankName);
                                    }


                                    String AccountNumber = obj.optString("AccountNumber");
                                    if (!AccountNumber.equals("")) {
                                        tvAcNumber.setText(AccountNumber);
                                    } else {
                                        tvAcNumber.setText("N/A");
                                    }

                                    String AadharCard = obj.optString("AadharCard");
                                    if (!AadharCard.equals("")) {
                                        tvAddharNumber.setText(AadharCard);
                                    } else {
                                        tvAddharNumber.setText("N/A");
                                    }

                                    String UanNo = obj.optString("UanNo");
                                    if (!UanNo.equals("")) {
                                        tvUanNumber.setText(UanNo);
                                    } else {
                                        tvUanNumber.setText("N/A");
                                    }
                                }
                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ProfileActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
                Toast.makeText(ProfileActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(ProfileActivity.this);
        requestQueue.add(stringRequest);

    }


}
