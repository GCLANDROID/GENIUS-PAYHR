package com.genius.payhrms.activity.profile;

import static com.genius.payhrms.activity.utility.Util.SECRET_KEY;
import static com.genius.payhrms.activity.utility.Util.encrypt;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import android.util.Base64;
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
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.genius.payhrms.R;

import com.genius.payhrms.activity.activity.UserDashBoardActivity;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.NetworkConnectionCheck;
import com.genius.payhrms.activity.utility.Pref;
import com.genius.payhrms.activity.utility.ValidUtils;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;


public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    LinearLayout llOffical, llOffDetail, llCon, llConDetail, llPer, llPerDetail, llMis, llMisDetail,llDocuments;
    ImageView imgOffiPlus, imgOffiMinus, imgPerPlus, imgPerMinus, imgConPlus, imgConMinus, imgMisPlus, imgMisMinus;
    ImageView imgHome, imgBack;
    TextView tvEmplId, tvEmpCode, tvEmpName, tvDOJ, tvDepartment, tvDesignation, tvLocation, tvGender, tvEmpDOB, tvGurdianName, tvRealtionShip, tvQualification, tvMarital, tvBloodGroup;
    TextView tvParAddr, tvPreAddr, tvPhnNumber, tvEmail, tvPfNumber, tvEsiNumber, tvBankName, tvAcNumber,tvEName, tvAddharNumber,tvPanNumber,tvPan,tvBranch,tvBranchName, tvUanNumber;
    Pref pref;
    String empConsId, empClinId, empClintOffId, empId;
    NetworkConnectionCheck connectionCheck;
    ImageView imgOffFor, imgOffFor1, imgPerFor, imgPerFor1, imgConFor, imgConFor1, imgMisFor, imgMisFor1;
    TextView tvOff, tvPer, tvCon, tvMis,tvDocuments;
    LinearLayout llEmpId;
    TextView tvToolbar, tvPersonal, tvContact, tvToolBar;
    TextView tvUAN, tvAadhar, tvAc, tvBank, tvEsi, tvPf, tvEmailID, tvPhone, tvPreAdd, tvPerAdd, tvBlood, tvStatus, tvQuali, tvRelation, tvGName,tvEmergencyName, tvDateoOB, tvGen, tvLoc, tvDes, tvDept, tvDateoJ, tvName, tvCode, tvId;
    String surl;
    ImageView imgUser;
    TextView tvPersonalEmail,tvReportingManager,tvGMob,tvGurdianMob,tvReportingManagerTitle;
    LinearLayout llRegion,llAsset,lnEmergency,lnLocation,lnGurdianContact,lnRelationship,lnQualification,lnBloodGrp,lnPF,lnBankName,lnAcNumber,lnAadhar,lnPAN;
    TextView tvRegion,tvGrade;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initialize();
        onclick();
    }

    private void initialize() {
        pref = new Pref(ProfileActivity.this);
        tvPersonalEmail=(TextView)findViewById(R.id.tvPersonalEmail);
        tvRegion=(TextView)findViewById(R.id.tvRegion);
        tvGMob=(TextView)findViewById(R.id.tvGMob);
        tvGurdianMob=(TextView)findViewById(R.id.tvGurdianMob);
        tvReportingManagerTitle=(TextView)findViewById(R.id.tvReportingManagerTitle);
        tvReportingManager=(TextView)findViewById(R.id.tvReportingManager);
        imgUser=(ImageView)findViewById(R.id.imgUser) ;
        connectionCheck = new NetworkConnectionCheck(this);
        llRegion=(LinearLayout)findViewById(R.id.llRegion);

        llDocuments = (LinearLayout) findViewById(R.id.llDocuments);

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
        tvEName=(TextView)findViewById(R.id.tvEName);
        tvEmpCode = (TextView) findViewById(R.id.tvEmpCode);
        tvEmpName = (TextView) findViewById(R.id.tvEmpName);
        tvDOJ = (TextView) findViewById(R.id.tvDOJ);
        tvDepartment = (TextView) findViewById(R.id.tvDepartment);
        tvBranch=(TextView)findViewById(R.id.tvBranch);
        tvBranchName=(TextView)findViewById(R.id.tvBranchName);
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
        tvPanNumber=(TextView) findViewById(R.id.tvPanNumber);
        tvPan=(TextView)findViewById(R.id.tvPan);
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
        tvEmergencyName = (TextView) findViewById(R.id.tvEmergencyName);
        tvDateoOB = (TextView) findViewById(R.id.tvDateoOB);
        tvGen = (TextView) findViewById(R.id.tvGen);
        tvLoc = (TextView) findViewById(R.id.tvLoc);
        tvDes = (TextView) findViewById(R.id.tvDes);
        tvDept = (TextView) findViewById(R.id.tvDept);
        tvDateoJ = (TextView) findViewById(R.id.tvDateoJ);
        tvName = (TextView) findViewById(R.id.tvName);
        tvCode = (TextView) findViewById(R.id.tvCode);
        tvId = (TextView) findViewById(R.id.tvId);
        tvGrade=(TextView)findViewById(R.id.tvGrade);

        llAsset=(LinearLayout)findViewById(R.id.llAsset);
        lnEmergency=(LinearLayout)findViewById(R.id.lnEmergency);
        lnLocation=(LinearLayout) findViewById(R.id.lnLocation);
        lnGurdianContact=(LinearLayout) findViewById(R.id.lnGurdianContact);
        lnRelationship=(LinearLayout) findViewById(R.id.lnRelationship);
        lnQualification=(LinearLayout) findViewById(R.id.lnQualification);
        lnBloodGrp=(LinearLayout) findViewById(R.id.lnBloodGrp);
        lnPF=(LinearLayout) findViewById(R.id.lnPF);
        lnBankName=(LinearLayout) findViewById(R.id.lnBankName);
        lnAcNumber=(LinearLayout) findViewById(R.id.lnAcNumber);
        lnAadhar=(LinearLayout) findViewById(R.id.lnAadhar);
        lnPAN=(LinearLayout) findViewById(R.id.lnPAN);
        if (pref.getSecurityCode().equals("1156") || pref.getSecurityCode().equals("1000")){
            llAsset.setVisibility(View.VISIBLE);
        }else {
            llAsset.setVisibility(View.GONE);
        }

        if (pref.getSecurityCode().equals("1186")){
            lnLocation.setVisibility(View.GONE);
            lnGurdianContact.setVisibility(View.GONE);
            lnRelationship.setVisibility(View.GONE);
            lnQualification.setVisibility(View.GONE);
            lnBloodGrp.setVisibility(View.GONE);
            lnBankName.setVisibility(View.GONE);
            lnPF.setVisibility(View.GONE);
            lnAcNumber.setVisibility(View.GONE);
            lnAadhar.setVisibility(View.GONE);
            lnPAN.setVisibility(View.GONE);
            tvReportingManagerTitle.setText("Leave Approver");
        }else {

        }
        tvDocuments = (TextView) findViewById(R.id.tvDocuments);



        if (pref.getLanguage().equals("hi")) {
            tvOff.setText("आधिकारिक विवरण");
            tvPer.setText("व्यक्तिगत जानकारी");
            tvCon.setText("संपर्क विवरण");
            tvMis.setText("विविध");
            tvDocuments.setText("दस्तावेजों");
            tvToolBar.setText("प्रोफ़ाइल");

            tvId.setText("आयडी");
            tvCode.setText("कोड");
            tvName.setText("नाम");
            tvDateoJ.setText("जुड़ने की तारीख");
            tvDept.setText("विभाग");
            tvBranch.setText("डाली");
            tvDes.setText("पद");
            tvLoc.setText("स्थान");
            tvGen.setText("लिंग");
            tvDateoOB.setText("जन्म की तारीख");
            tvGName.setText("अभिभावक का नाम");
            tvRelation.setText("संबंध");
            tvQuali.setText("योग्यता");
            tvStatus.setText("वैवाहिक स्थिति");
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
            tvPan.setText("पैन नंबर");
            tvUAN.setText("यूएएन नंबर");
        } else {
            tvOff.setText("Official");
            tvPer.setText("Personal");
            tvCon.setText("Contact");
            tvMis.setText("Miscellaneous");
            tvDocuments.setText("Documents");
            tvToolBar.setText("Profile");

            tvId.setText("Id");
            tvCode.setText("Code");
            tvName.setText("Name");
            tvDateoJ.setText("Date Of Joining");
            tvDept.setText("Department");
            tvBranch.setText("Branch");
            tvDes.setText("Designation");
            tvLoc.setText("Location");
            tvGen.setText("Gender");
            tvDateoOB.setText("Date Of Birth");


            tvRelation.setText("Relationship");
            tvQuali.setText("Qualification");
            tvStatus.setText("Marital Status");
            tvBlood.setText("Blood Group");
            tvPerAdd.setText("Permanent Address");
            tvPreAdd.setText("Present Address");
            tvPhone.setText("Phone Number");
            tvEmailID.setText("Email Id");
            tvPf.setText("PF Number");
            tvEsi.setText("ESI Number");
            tvBank.setText("Bank Name");
            tvAc.setText("A/C Number");
            tvAadhar.setText("Aadhar Number");
            tvPan.setText("Pan Number");
            tvUAN.setText("UAN number");
        }

        if (pref.getSecurityCode().equals("1155")|| pref.getSecurityCode().equals("1156")){
            lnEmergency.setVisibility(View.VISIBLE);
        }else {
            lnEmergency.setVisibility(View.GONE);
        }

        if (pref.getSecurityCode().equals("1156")){
            llRegion.setVisibility(View.VISIBLE);
            //profileFunctionForWestern();
            JSONObject object=new JSONObject();
            try {
                object.put("AEMConsultantID",pref.getEmpConId());
                object.put("AEMClientID",pref.getEmpClintId());
                object.put("AEMClientOfficeID",pref.getEmpClintOffId());
                object.put("AEMEmployeeID",pref.getEmpId());
                object.put("WorkingStatus","1");
                object.put("CurrentPage","1");
                object.put("SecurityCode",pref.getSecurityCode());
                westernprofile(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            llRegion.setVisibility(View.GONE);
            //profileFunction();
            JSONObject object=new JSONObject();
            try {
                object.put("AEMConsultantID",pref.getEmpConId());
                object.put("AEMClientID",pref.getEmpClintId());
                object.put("AEMClientOfficeID",pref.getEmpClintOffId());
                object.put("AEMEmployeeID",pref.getEmpId());
                object.put("WorkingStatus","1");
                object.put("CurrentPage","1");
                object.put("SecurityCode",pref.getSecurityCode());
                profile(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    private void onclick() {
        llOffical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (llOffDetail.getVisibility() == view.GONE) {

                    llOffDetail.setVisibility(View.VISIBLE);

                } else {

                    llOffDetail.setVisibility(View.GONE);


                }
            }
        });

        llPer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (llPerDetail.getVisibility() == view.GONE) {

                    llPerDetail.setVisibility(View.VISIBLE);

                } else {

                    llPerDetail.setVisibility(View.GONE);

                }
            }
        });

        llCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (llConDetail.getVisibility() == view.GONE) {

                    llConDetail.setVisibility(View.VISIBLE);

                } else {

                    llConDetail.setVisibility(View.GONE);

                }
            }
        });

        llMis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (llMisDetail.getVisibility() == View.GONE) {

                    llMisDetail.setVisibility(View.VISIBLE);

                } else {

                    llMisDetail.setVisibility(View.GONE);

                }
            }
        });

        llDocuments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, DocumentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        llAsset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, CompanyAssetActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

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



    private void profile(JSONObject jsonObject) {
        final ProgressDialog pd = new ProgressDialog(ProfileActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(Api.sProfileapi)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {


                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        pd.dismiss();

                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                            //JSONArray responseData = job1.optJSONArray("Response_Data");
                            String responseData = job1.optString("Response_Data");
                            JSONArray jsonArray = null;
                            try {
                                jsonArray = new JSONArray(responseData);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.optJSONObject(i);
                                final String AEMEmployeeID = obj.optString("AEMEmployeeID");
                                // tvEmplId.setText(AEMEmployeeID);
                                final String ID = AEMEmployeeID;
                                tvEmplId.setText(ID);


                                //code feild
                                final String Code = obj.optString("Code");
                                pref.saveempCode(Code);
                                if (pref.getLanguage().equals("hi")) {
                                    final Handler textViewHandler1 = new Handler();
                                    new AsyncTask<Void, Void, Void>() {
                                        @Override
                                        protected Void doInBackground(Void... params) {
                                            @SuppressLint("StaticFieldLeak") TranslateOptions options = TranslateOptions.newBuilder()
                                                    .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
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
                                            pd.show();
                                        }

                                        @Override
                                        protected void onPostExecute(Void aVoid) {
                                            super.onPostExecute(aVoid);
                                            pd.show();
                                        }


                                    }.execute();
                                } else {
                                    tvEmpCode.setText(Code);
                                }


                                //Name field
                                final String Name = obj.optString("Name").toUpperCase();
                                pref.saveempName(Name);
                                if (pref.getLanguage().equals("hi")) {
                                    final Handler textViewHandler1 = new Handler();
                                    new AsyncTask<Void, Void, Void>() {
                                        @Override
                                        protected Void doInBackground(Void... params) {
                                            TranslateOptions options = TranslateOptions.newBuilder()
                                                    .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
                                                    .build();
                                            Translate translate = options.getService();
                                            final Translation translation =
                                                    translate.translate(Name,
                                                            Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                                            textViewHandler1.post(new Runnable() {
                                                @Override
                                                public void run() {

                                                    Log.d("sssh", translation.getTranslatedText());
                                                    String hName = translation.getTranslatedText();
                                                    tvEName.setText(hName);
                                                    tvEmpName.setText(hName);

                                                }
                                            });
                                            return null;
                                        }

                                        @Override
                                        protected void onPreExecute() {
                                            super.onPreExecute();
                                            pd.show();
                                        }

                                        @Override
                                        protected void onPostExecute(Void aVoid) {
                                            super.onPostExecute(aVoid);
                                            pd.show();
                                        }


                                    }.execute();
                                } else {
                                    tvEName.setText(Name);
                                    tvEmpName.setText(Name);
                                }


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
                                                    .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
                                                    .build();
                                            Translate translate = options.getService();
                                            final Translation translation =
                                                    translate.translate(Department,
                                                            Translate.TranslateOption.sourceLanguage("en"), Translate.TranslateOption.targetLanguage(pref.getLanguage()));
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
                                            pd.show();
                                        }

                                        @Override
                                        protected void onPostExecute(Void aVoid) {
                                            super.onPostExecute(aVoid);
                                            pd.show();
                                        }


                                    }.execute();
                                } else {
                                    tvDepartment.setText(Department);
                                }
                                final String Branch = ValidUtils.getFreshValue(obj.optString("Branch"), "-");
                                if (pref.getLanguage().equals("hi")) {
                                    final Handler textViewHandler4 = new Handler();
                                    new AsyncTask<Void, Void, Void>() {
                                        @Override
                                        protected Void doInBackground(Void... params) {
                                            TranslateOptions options = TranslateOptions.newBuilder()
                                                    .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
                                                    .build();
                                            Translate translate = options.getService();
                                            final Translation translation =
                                                    translate.translate(Branch,
                                                            Translate.TranslateOption.sourceLanguage("en"), Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                                            textViewHandler4.post(new Runnable() {
                                                @Override
                                                public void run() {

                                                    Log.d("sssh", translation.getTranslatedText());
                                                    String h = translation.getTranslatedText();
                                                    tvBranchName.setText(h);

                                                }
                                            });
                                            return null;
                                        }

                                        @Override
                                        protected void onPreExecute() {
                                            super.onPreExecute();
                                            pd.show();
                                        }

                                        @Override
                                        protected void onPostExecute(Void aVoid) {
                                            super.onPostExecute(aVoid);
                                            pd.show();
                                        }


                                    }.execute();
                                } else {
                                    tvBranchName.setText(Branch);
                                }


                                final String Designation = obj.optString("Designation");
                                if (pref.getLanguage().equals("hi")) {
                                    final Handler textViewHandler5 = new Handler();
                                    new AsyncTask<Void, Void, Void>() {
                                        @Override
                                        protected Void doInBackground(Void... params) {
                                            TranslateOptions options = TranslateOptions.newBuilder()
                                                    .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
                                                    .build();
                                            Translate translate = options.getService();
                                            final Translation translation =
                                                    translate.translate(Designation,
                                                            Translate.TranslateOption.sourceLanguage("en"), Translate.TranslateOption.targetLanguage(pref.getLanguage()));
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
                                            pd.show();
                                        }

                                        @Override
                                        protected void onPostExecute(Void aVoid) {
                                            super.onPostExecute(aVoid);
                                            pd.show();
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
                                                    .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
                                                    .build();
                                            Translate translate = options.getService();
                                            final Translation translation =
                                                    translate.translate(Location,
                                                            Translate.TranslateOption.sourceLanguage("en"), Translate.TranslateOption.targetLanguage(pref.getLanguage()));
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
                                            pd.show();
                                        }

                                        @Override
                                        protected void onPostExecute(Void aVoid) {
                                            super.onPostExecute(aVoid);
                                            pd.show();
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
                                                    .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
                                                    .build();
                                            Translate translate = options.getService();
                                            final Translation translation =
                                                    translate.translate(Sex,
                                                            Translate.TranslateOption.sourceLanguage("en"), Translate.TranslateOption.targetLanguage(pref.getLanguage()));
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
                                            pd.show();
                                        }

                                        @Override
                                        protected void onPostExecute(Void aVoid) {
                                            super.onPostExecute(aVoid);
                                            pd.show();
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
                                                    .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
                                                    .build();
                                            Translate translate = options.getService();
                                            final Translation translation =
                                                    translate.translate(GuardianName,
                                                            Translate.TranslateOption.sourceLanguage("en"), Translate.TranslateOption.targetLanguage(pref.getLanguage()));
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
                                            pd.show();
                                        }

                                        @Override
                                        protected void onPostExecute(Void aVoid) {
                                            super.onPostExecute(aVoid);
                                            pd.show();
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
                                                    .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
                                                    .build();
                                            Translate translate = options.getService();
                                            final Translation translation =
                                                    translate.translate(RelationShip,
                                                            Translate.TranslateOption.sourceLanguage("en"), Translate.TranslateOption.targetLanguage(pref.getLanguage()));
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
                                            pd.show();
                                        }

                                        @Override
                                        protected void onPostExecute(Void aVoid) {
                                            super.onPostExecute(aVoid);
                                            pd.show();
                                        }


                                    }.execute();
                                } else {
                                    tvRealtionShip.setText(RelationShip);
                                }

                                final String GuardName = obj.optString("GuardName");
                                tvEmergencyName.setText(GuardName);
                                final String Qualification = obj.optString("Qualification");
                                if (pref.getLanguage().equals("hi")) {
                                    final Handler textViewHandler11 = new Handler();
                                    new AsyncTask<Void, Void, Void>() {
                                        @Override
                                        protected Void doInBackground(Void... params) {
                                            TranslateOptions options = TranslateOptions.newBuilder()
                                                    .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
                                                    .build();
                                            Translate translate = options.getService();
                                            final Translation translation =
                                                    translate.translate(Qualification,
                                                            Translate.TranslateOption.sourceLanguage("en"), Translate.TranslateOption.targetLanguage(pref.getLanguage()));
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
                                            pd.show();
                                        }

                                        @Override
                                        protected void onPostExecute(Void aVoid) {
                                            super.onPostExecute(aVoid);
                                            pd.show();
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
                                                    .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
                                                    .build();
                                            Translate translate = options.getService();
                                            final Translation translation =
                                                    translate.translate(MaritalStatus,
                                                            Translate.TranslateOption.sourceLanguage("en"), Translate.TranslateOption.targetLanguage(pref.getLanguage()));
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
                                            pd.show();
                                        }

                                        @Override
                                        protected void onPostExecute(Void aVoid) {
                                            super.onPostExecute(aVoid);
                                            pd.show();
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
                                                    .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
                                                    .build();
                                            Translate translate = options.getService();
                                            final Translation translation =
                                                    translate.translate(BloodGroup,
                                                            Translate.TranslateOption.sourceLanguage("en"), Translate.TranslateOption.targetLanguage(pref.getLanguage()));
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
                                            pd.show();
                                        }

                                        @Override
                                        protected void onPostExecute(Void aVoid) {
                                            super.onPostExecute(aVoid);
                                            pd.show();
                                        }


                                    }.execute();
                                } else {
                                    tvBloodGroup.setText(BloodGroup);
                                }

                                final String permanentpincode = obj.optString("PermanentPinCode");

                                final String PermanentAddress = obj.optString("PermanentAddress");
                                if (pref.getLanguage().equals("hi")) {
                                    final Handler textViewHandler14 = new Handler();
                                    new AsyncTask<Void, Void, Void>() {
                                        @Override
                                        protected Void doInBackground(Void... params) {
                                            TranslateOptions options = TranslateOptions.newBuilder()
                                                    .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
                                                    .build();
                                            Translate translate = options.getService();
                                            final Translation translation =
                                                    translate.translate(PermanentAddress,
                                                            Translate.TranslateOption.sourceLanguage("en"), Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                                            textViewHandler14.post(new Runnable() {
                                                @Override
                                                public void run() {

                                                    Log.d("sssh", translation.getTranslatedText());
                                                    String h = translation.getTranslatedText();
                                                    if (permanentpincode.equals("null")) {
                                                        Log.d("null", permanentpincode);
                                                        tvParAddr.setText(h);
                                                    } else {
                                                        Log.d("value", permanentpincode);
                                                        tvParAddr.setText(h + "," + permanentpincode);
                                                    }

                                                }
                                            });
                                            return null;
                                        }

                                        @Override
                                        protected void onPreExecute() {
                                            super.onPreExecute();
                                            pd.show();
                                        }

                                        @Override
                                        protected void onPostExecute(Void aVoid) {
                                            super.onPostExecute(aVoid);
                                            pd.show();
                                        }


                                    }.execute();
                                } else {
                                    if (permanentpincode.equals("null")) {
                                        Log.d("null", permanentpincode);
                                        tvParAddr.setText(PermanentAddress);
                                    } else {
                                        Log.d("value", permanentpincode);
                                        tvParAddr.setText(PermanentAddress + "," + permanentpincode);
                                    }
                                    // tvParAddr.setText(PermanentAddress+","+permanentpincode);
                                }


                                final String presentpincode = obj.optString("PresentPincode");


                                final String PresentAddress = obj.optString("PresentAddress");
                                if (pref.getLanguage().equals("hi")) {
                                    final Handler textViewHandler15 = new Handler();
                                    new AsyncTask<Void, Void, Void>() {
                                        @Override
                                        protected Void doInBackground(Void... params) {
                                            TranslateOptions options = TranslateOptions.newBuilder()
                                                    .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
                                                    .build();
                                            Translate translate = options.getService();
                                            final Translation translation =
                                                    translate.translate(PresentAddress,
                                                            Translate.TranslateOption.sourceLanguage("en"), Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                                            textViewHandler15.post(new Runnable() {
                                                @SuppressLint("StaticFieldLeak")
                                                @Override
                                                public void run() {

                                                    Log.d("sssh", translation.getTranslatedText());
                                                    String h = translation.getTranslatedText();
                                                    if (presentpincode.equals("null")) {
                                                        Log.d("null", presentpincode);
                                                        tvPreAddr.setText(h);
                                                    } else {
                                                        Log.d("value", presentpincode);
                                                        tvPreAddr.setText(h + "," + presentpincode);
                                                    }


                                                }
                                            });
                                            return null;
                                        }

                                        @Override
                                        protected void onPreExecute() {
                                            super.onPreExecute();
                                            pd.show();
                                        }

                                        @Override
                                        protected void onPostExecute(Void aVoid) {
                                            super.onPostExecute(aVoid);
                                            pd.show();
                                        }


                                    }.execute();
                                } else {
                                    if (presentpincode.equals("null")) {
                                        Log.d("null", presentpincode);
                                        tvPreAddr.setText(PresentAddress);
                                    } else {
                                        Log.d("value", presentpincode);
                                        tvPreAddr.setText(PresentAddress + "," + presentpincode);
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
                                tvPfNumber.setText(PFNumber);
                                /*if (!PFNumber.equals("")) {
                                    tvPfNumber.setText(PFNumber);
                                } else {
                                    tvPfNumber.setText("N/A");
                                }*/

                                String ESINumber = obj.optString("ESINumber");
                                tvEsiNumber.setText(ESINumber);
                                /*if (!ESINumber.equals("")) {
                                    tvEsiNumber.setText(ESINumber);
                                }*/

                                final String BankName = obj.optString("BanKName");
                                if (pref.getLanguage().equals("hi")) {

                                    final Handler textViewHandler17 = new Handler();
                                    new AsyncTask<Void, Void, Void>() {
                                        @Override
                                        protected Void doInBackground(Void... params) {
                                            TranslateOptions options = TranslateOptions.newBuilder()
                                                    .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
                                                    .build();
                                            Translate translate = options.getService();
                                            final Translation translation =
                                                    translate.translate(BankName,
                                                            Translate.TranslateOption.sourceLanguage("en"), Translate.TranslateOption.targetLanguage(pref.getLanguage()));
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
                                            pd.show();
                                        }

                                        @Override
                                        protected void onPostExecute(Void aVoid) {
                                            super.onPostExecute(aVoid);
                                            pd.dismiss();
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
                                tvUanNumber.setText(UanNo);

                                /*if (!UanNo.equals("")) {
                                    tvUanNumber.setText(UanNo);
                                } else {
                                    tvUanNumber.setText("N/A");
                                }*/
                                String panNo = obj.optString("PanNo");
                                if (!panNo.equals("")) {
                                    tvPanNumber.setText(panNo);
                                } else {
                                    tvPanNumber.setText("N/A");
                                }

                                String ReportingManager = obj.optString("ReportingManager");
                                tvReportingManager.setText(ReportingManager);
                                String PersonalEmail = obj.optString("PersonalEmail");
                                tvPersonalEmail.setText(PersonalEmail);
                                String GuardContMobile = obj.optString("GuardContMobile");
                                tvGurdianMob.setText(GuardContMobile);

                                final String Level = obj.optString("Level");
                                tvGrade.setText(Level);
                            }

                            if (pref.getSecurityCode().equals("1155")) {
                                //profileImage();
                                JSONObject object = new JSONObject();
                                try {
                                    object.put("EmployeeID", pref.getEmpId());
                                    object.put("SecurityCode", pref.getSecurityCode());
                                    profileImage2(object);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {

                            }
                            // boolean _status = job1.getBoolean("status");
                            // do anything with response
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                        if (error.getErrorCode()==401){
                            JSONObject obj=new JSONObject();
                            try {
                                obj.put("MasterID",encrypt(pref.getMasterId(),SECRET_KEY));
                                obj.put("Password",encrypt(pref.getPassword(),SECRET_KEY));
                                obj.put("IMEI","0");
                                obj.put("DeviceID","0");
                                obj.put("DeviceType","A");
                                obj.put("SecurityCode",pref.getSecurityCode());
                                login(obj);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }
    private void westernprofile(JSONObject jsonObject) {
        Log.e(TAG, "westernprofile: "+jsonObject);
        final ProgressDialog progressBar = new ProgressDialog(ProfileActivity.this);
        progressBar.setMessage("Loading..");
        progressBar.setCancelable(false);
        progressBar.show();
        AndroidNetworking.post(Api.sWesternProfileapi)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {


                        JSONObject job1 = response;
                        Log.e("response12", "WESTERN_PROFILE: " + job1);
                        progressBar.dismiss();

                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                            String responseData = job1.optString("Response_Data");
                            JSONArray jsonArray = null;
                            try {
                                jsonArray = new JSONArray(responseData);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.optJSONObject(i);
                                final String AEMEmployeeID = obj.optString("AEMEmployeeID");
                                // tvEmplId.setText(AEMEmployeeID);
                                final String ID = AEMEmployeeID;
                                tvEmplId.setText(ID);


                                //code feild
                                final String Code = obj.optString("Code");
                                pref.saveempCode(Code);
                                if (pref.getLanguage().equals("hi")) {
                                    final Handler textViewHandler1 = new Handler();
                                    new AsyncTask<Void, Void, Void>() {
                                        @Override
                                        protected Void doInBackground(Void... params) {
                                            @SuppressLint("StaticFieldLeak") TranslateOptions options = TranslateOptions.newBuilder()
                                                    .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
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
                                final String Name = obj.optString("Name");
                                pref.saveempName(Name);
                                if (pref.getLanguage().equals("hi")) {
                                    final Handler textViewHandler1 = new Handler();
                                    new AsyncTask<Void, Void, Void>() {
                                        @Override
                                        protected Void doInBackground(Void... params) {
                                            TranslateOptions options = TranslateOptions.newBuilder()
                                                    .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
                                                    .build();
                                            Translate translate = options.getService();
                                            final Translation translation =
                                                    translate.translate(Name,
                                                            Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                                            textViewHandler1.post(new Runnable() {
                                                @Override
                                                public void run() {

                                                    Log.d("sssh", translation.getTranslatedText());
                                                    String hName = translation.getTranslatedText();
                                                    tvEName.setText(hName);
                                                    tvEmpName.setText(hName);

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
                                }else{
                                    tvEName.setText(Name);
                                    tvEmpName.setText(Name);
                                }



                                //DOJ

                                final String DateOfJoining = obj.optString("DateOfJoining");
                                tvDOJ.setText(DateOfJoining);
                                final String Level = obj.optString("Level");
                                tvGrade.setText(Level);


                                final String Department = obj.optString("Department");
                                if (pref.getLanguage().equals("hi")) {
                                    final Handler textViewHandler4 = new Handler();
                                    new AsyncTask<Void, Void, Void>() {
                                        @Override
                                        protected Void doInBackground(Void... params) {
                                            TranslateOptions options = TranslateOptions.newBuilder()
                                                    .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
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
                                final String Branch = ValidUtils.getFreshValue(obj.optString("Branch"),"-");
                                if (pref.getLanguage().equals("hi")) {
                                    final Handler textViewHandler4 = new Handler();
                                    new AsyncTask<Void, Void, Void>() {
                                        @Override
                                        protected Void doInBackground(Void... params) {
                                            TranslateOptions options = TranslateOptions.newBuilder()
                                                    .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
                                                    .build();
                                            Translate translate = options.getService();
                                            final Translation translation =
                                                    translate.translate(Branch,
                                                            Translate.TranslateOption.sourceLanguage("en"),   Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                                            textViewHandler4.post(new Runnable() {
                                                @Override
                                                public void run() {

                                                    Log.d("sssh", translation.getTranslatedText());
                                                    String h = translation.getTranslatedText();
                                                    tvBranchName.setText(h);

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
                                    tvBranchName.setText(Branch);
                                }


                                final String Designation = obj.optString("Designation");
                                if (pref.getLanguage().equals("hi")) {
                                    final Handler textViewHandler5 = new Handler();
                                    new AsyncTask<Void, Void, Void>() {
                                        @Override
                                        protected Void doInBackground(Void... params) {
                                            TranslateOptions options = TranslateOptions.newBuilder()
                                                    .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
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
                                                    .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
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
                                                    .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
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
                                                    .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
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
                                lnRelationship.setVisibility(View.GONE);
                                final String RelationShip = obj.optString("RelationShip");
                                if (pref.getLanguage().equals("hi")) {
                                    final Handler textViewHandler10 = new Handler();
                                    new AsyncTask<Void, Void, Void>() {
                                        @Override
                                        protected Void doInBackground(Void... params) {
                                            TranslateOptions options = TranslateOptions.newBuilder()
                                                    .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
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
                                                    .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
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
                                                    .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
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
                                                    .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
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
                                                    .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
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
                                final String GuardName = obj.optString("GuardName");
                                tvEmergencyName.setText(GuardName);

                                final String PresentAddress = obj.optString("PresentAddress");
                                if (pref.getLanguage().equals("hi")) {
                                    final Handler textViewHandler15 = new Handler();
                                    new AsyncTask<Void, Void, Void>() {
                                        @Override
                                        protected Void doInBackground(Void... params) {
                                            TranslateOptions options = TranslateOptions.newBuilder()
                                                    .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
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

                                String Region=obj.optString("Region");
                                tvRegion.setText(Region);


                                String PFNumber = obj.optString("PFNumber");
                                tvPfNumber.setText(PFNumber);
                               /* if (!PFNumber.equals("")) {
                                    tvPfNumber.setText(PFNumber);
                                } else {
                                    tvPfNumber.setText("N/A");
                                }*/

                                String ESINumber = obj.optString("ESINumber");
                                tvEsiNumber.setText(ESINumber);
                                /*if (!ESINumber.equals("")) {
                                    tvEsiNumber.setText(ESINumber);
                                }*/

                                final String BankName = obj.optString("BanKName");
                                if (pref.getLanguage().equals("hi")) {

                                    final Handler textViewHandler17 = new Handler();
                                    new AsyncTask<Void, Void, Void>() {
                                        @Override
                                        protected Void doInBackground(Void... params) {
                                            TranslateOptions options = TranslateOptions.newBuilder()
                                                    .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
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
                                tvUanNumber.setText(UanNo);
                                /*if (!UanNo.equals("")) {
                                    tvUanNumber.setText(UanNo);
                                } else {
                                    tvUanNumber.setText("N/A");
                                }*/
                                String panNo=obj.optString("PanNo");
                                if (!panNo.equals("")){
                                    tvPanNumber.setText(panNo);
                                }else{
                                    tvPanNumber.setText("N/A");
                                }

                                String ReportingManager=obj.optString("ReportingManager");
                                tvReportingManager.setText(ReportingManager);
                                String PersonalEmail=obj.optString("PersonalEmail");
                                tvPersonalEmail.setText(PersonalEmail);
                                String GuardContMobile=obj.optString("GuardContMobile");
                                tvGurdianMob.setText(GuardContMobile);
                            }


                            if (pref.getSecurityCode().equals("1155")) {
                                //profileImage();
                                JSONObject object = new JSONObject();
                                try {
                                    object.put("EmployeeID", pref.getEmpId());
                                    object.put("SecurityCode", pref.getSecurityCode());
                                    profileImage2(object);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {

                            }


                            // boolean _status = job1.getBoolean("status");


                            // do anything with response
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        progressBar.dismiss();
                        if (error.getErrorCode()==401){
                            JSONObject obj=new JSONObject();
                            try {
                                obj.put("MasterID",encrypt(pref.getMasterId(),SECRET_KEY));
                                obj.put("Password",encrypt(pref.getPassword(),SECRET_KEY));
                                obj.put("IMEI","0");
                                obj.put("DeviceID","0");
                                obj.put("DeviceType","A");
                                obj.put("SecurityCode",pref.getSecurityCode());
                                login(obj);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }
                });
    }




    public void profileImage2(JSONObject object) {
        Log.e(TAG, "profileImage2: "+object.toString());
        //String surl =  "https://cloud.geniusconsultant.com/GHRMSApi/api/GCLKYC_New/GetProfilePic?EmployeeID="+pref.getEmpId()+"&SecurityCode=1155";
        //Log.d("kyc", surl);
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();

        AndroidNetworking.post(Api.sProfilePic)
                .addJSONObjectBody(object)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "GET_PROFILE_PIC: "+response.toString());
                        progressBar.dismiss();
                        JSONObject job1 = response;
                        int Response_Code = job1.optInt("Response_Code");
                        String Response_Message = job1.optString("Response_Message");
                        if (Response_Code == 101) {
                            String responseData = job1.optString("Response_Data");
                            byte[] decodedString = Base64.decode(responseData, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            imgUser.setImageBitmap(decodedByte);
                        } else {

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressBar.dismiss();
                    }
                });
    }




    private void login(JSONObject jsonObject) {
        final ProgressDialog pd = new ProgressDialog(ProfileActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(Api.sLoginapi)
                .addJSONObjectBody(jsonObject)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        pd.dismiss();

                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                            JSONArray responseData = job1.optJSONArray("Response_Data");
                            for (int i = 0; i < responseData.length(); i++) {
                                JSONObject obj = responseData.optJSONObject(i);
                                String Genius_Access_Token=obj.optString("Genius_Access_Token");
                                pref.saveAccessToken(Genius_Access_Token);

                                // boolean _status = job1.getBoolean("status");

                                JSONObject object=new JSONObject();
                                try {
                                    object.put("AEMConsultantID",pref.getEmpConId());
                                    object.put("AEMClientID",pref.getEmpClintId());
                                    object.put("AEMClientOfficeID",pref.getEmpClintOffId());
                                    object.put("AEMEmployeeID",pref.getEmpId());
                                    object.put("WorkingStatus","1");
                                    object.put("CurrentPage","1");
                                    object.put("SecurityCode",pref.getSecurityCode());
                                    if (pref.getSecurityCode().equals("1156")){
                                        westernprofile(object);
                                    }else {
                                        profile(object);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                // do anything with response
                            }
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                    }
                });
    }
}
