package com.genius.hrms.activity.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genius.hrms.R;

import com.genius.hrms.activity.utility.GPSTracker;
import com.genius.hrms.activity.utility.NetworkConnectionCheck;
import com.genius.hrms.activity.utility.Pref;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;


public class LoginActivity extends AppCompatActivity {
    TextView tvSignIn;
    EditText etUserId, etPassword;
    String userId, password;
    LinearLayout llSignIn;
    NetworkConnectionCheck connectionCheck;
    AlertDialog alertDialog;
    androidx.appcompat.app.AlertDialog alertDialog1;
    AlertDialog al1;
    Pref pref;
    String UserType;
    String refreshedToken;
    EditText etSecurityCode;
    String AEMEmployeeID;
    String version;
    CheckBox ckRemember;
    TextView tvShow, tvHide;
    ImageView imgshow,imghide;
    LinearLayout llLoader;
    int MY_SOCKET_TIMEOUT_MS = 60000;
    GPSTracker gps;
    double latitude, longitude;
    String TutorialFlag;
    ImageView imgForward;
    ProgressBar pgBar;
    String android_id;
    TextView tvWelcome, tvLogin;
    TextView tvForgot;
    String LoginFlag = "";
    String IsModified;
    String SecurityCode;
    String flaggoing;
    String cpassword;
    String ipAddress;
    String playversion;
    boolean responseStatus;
    String responseText;
    String address;
    TextView tvQuery;
    String loginFlag="1";
    private FirebaseDatabase mFirebaseInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();

        onClick();
    }

    @SuppressLint("MissingPermission")
    private void initialize() {
        gps = new GPSTracker(LoginActivity.this);
        pref = new Pref(LoginActivity.this);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            Log.d("saikatdas", String.valueOf(latitude));
            longitude = gps.getLongitude();
        } else {
// can't get location
// GPS or Network is not enabled
// Ask user to enable GPS/network in settings

        }
        String address = getCompleteAddressString(latitude, longitude);
        pref.saveAddress(address);
        llSignIn = (LinearLayout) findViewById(R.id.llSignIn);


        etUserId = (EditText) findViewById(R.id.etUserId);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPassword.setText(cpassword);
        connectionCheck = new NetworkConnectionCheck(this);
        pref = new Pref(LoginActivity.this);

//        Log.d("token",refreshedToken);
        etSecurityCode = (EditText) findViewById(R.id.etSecuritycode);
        try {
            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
            int verCode = pInfo.versionCode;
            Log.d("sddk", version);
            Log.d("sdkl", String.valueOf(verCode));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        ckRemember = (CheckBox) findViewById(R.id.ckRemember);

        if (pref.getCheckFlag().equals("1")) {
            ckRemember.setChecked(true);
            etUserId.setText(pref.getLoginID());
            etPassword.setText(pref.getPassword());
            etSecurityCode.setText(pref.getSecurityCode());
        }

        if (pref.getCheckFlag().equals("2")) {
            ckRemember.setChecked(false);
            etUserId.setText("");
            etPassword.setText("");
            etSecurityCode.setText("");
        }

        tvShow = (TextView) findViewById(R.id.tvShow);
        tvHide = (TextView) findViewById(R.id.tvHide);
//        imgshow=(ImageView) findViewById(R.id.imgShow);
//        imghide=(ImageView) findViewById(R.id.imghide);

        imgForward = (ImageView) findViewById(R.id.imgForward);
        pgBar = (ProgressBar) findViewById(R.id.pgBar);
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        pref.saveRefreshToken(refreshedToken);
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        if (android_id.equals("")) {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            android_id = telephonyManager.getDeviceId();
        } else {
            android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }

        Log.d("androidid", android_id);

        tvLogin = (TextView) findViewById(R.id.tvLogin);
        tvWelcome = (TextView) findViewById(R.id.tvWelcome);
        tvForgot = (TextView) findViewById(R.id.tvForgot);
        Log.d("getlan", pref.getLanguage());
        if (pref.getLanguage().equals("hi")) {
            tvLogin.setText("लॉग इन");
            tvWelcome.setText("स्वागत हे");
            ckRemember.setText("कृपया याद रखें");
            tvShow.setText("प्रदर्शन");
            tvHide.setText("छिपाना");
            etUserId.setHint("अपनी उपयोगकर्ता आईडी दर्ज करें");
            etPassword.setHint("अपना पासवर्ड डालें");
            etSecurityCode.setHint("अपना गुप्त कोड डालो");
            tvForgot.setText("पासवर्ड भूल गए?");
        } else {
            tvLogin.setText("LogIn");
            tvWelcome.setText("Welcome");
            ckRemember.setText("Remember Me");
            tvShow.setText("Show");
            tvHide.setText("Hide");
            etUserId.setHint("Enter your user id");
            etPassword.setHint("Enter your password");
            etSecurityCode.setHint("Enter your security code");
            tvForgot.setText("Forgot Password");
        }
        tvQuery=(TextView)findViewById(R.id.tvQuery);



    }

    private void onClick() {

        etUserId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etUserId.getText().toString().contains("TEMP") || etUserId.getText().toString().contains("temp")) {
                    etPassword.setText("password");
                } else {
                    etPassword.setText("");
                }

            }
        });

        llSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etUserId.getText().toString().length() > 0) {
                    if (etPassword.getText().toString().length() > 0) {
                        if (connectionCheck.isNetworkAvailable()) {
                            if (etSecurityCode.getText().toString().length() > 0) {

                                    if (etSecurityCode.getText().toString().equals("11")||etSecurityCode.getText().toString().equals("123"))
                                    {
                                        loginFunctionForPPS();
                                    }else {
                                        loginFunction();
                                    }

                            } else {
                                etSecurityCode.setError("please enter Security Code");
                                etSecurityCode.requestFocus();
                            }

                        } else {
                            connectionCheck.getNetworkActiveAlert().show();
                        }


                    } else {
                        etPassword.setError("please enter your password");
                        etPassword.requestFocus();
                    }

                } else {
                    etUserId.setError("please enter your user id");
                    etUserId.requestFocus();
                }


            }
        });

        ckRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    pref.saveCheckFlag("1");
                } else {
                    pref.saveCheckFlag("2");
                }
            }
        });

        tvShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvHide.setVisibility(View.VISIBLE);
                tvShow.setVisibility(View.GONE);
                etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

            }
        });

        tvHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvShow.setVisibility(View.VISIBLE);
                tvHide.setVisibility(View.GONE);
                etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        });
//        imgshow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                imghide.setVisibility(View.VISIBLE);
//                imgshow.setVisibility(View.GONE);
//                etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//
//            }
//        });
//        imghide.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                imgshow.setVisibility(View.VISIBLE);
//                imghide.setVisibility(View.GONE);
//                etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
//            }
//        });

        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        tvQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QueryActivity.class);
                intent.putExtra("goingFlag","1");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    public void loginFunction() {
        byte[] data = new byte[0];
        try {
            data = etPassword.getText().toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String base64 = Base64.encodeToString(data, Base64.DEFAULT).replaceAll("\\s+", "");
        if (etSecurityCode.getText().toString().equals("1080")) {
            ipAddress = "https://adityabirla.geniusconsultant.com/";

        } else {
            ipAddress = "https://cloud.geniusconsultant.com/";
        }


        String surl = ipAddress+"GHRMSApi/api/AuthenticateWithEncryption?MasterID=" + etUserId.getText().toString() + "&Password=" + base64 + "&IMEI=" + android_id + "&Version=v1&SecurityCode=" + etSecurityCode.getText().toString() + "&DeviceID=" + refreshedToken + "&DeviceType=A";
        Log.d("inputLogin", surl);

        imgForward.setVisibility(View.GONE);
        pgBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    AEMEmployeeID = obj.optString("AEMEmployeeID");
                                    pref.saveEmpId(AEMEmployeeID);
                                    Log.d("aemp", pref.getEmpId());
                                    String Name = obj.optString("Name");
                                    pref.saveEmpName(Name);
                                    Log.d("empname", Name);
                                    String LoginDateTime = obj.optString("LoginDateTime");
                                    pref.saveloginTime(LoginDateTime);
                                    String FlagMenu = obj.optString("FlagMenu");
                                    pref.saveMenu(FlagMenu);
                                    Log.d("menud", pref.getMenu());
                                    String AEMConsultantID = obj.optString("AEMConsultantID");
                                    pref.saveEmpConId(AEMConsultantID);
                                    String AEMClientID = obj.optString("AEMClientID");
                                    pref.saveEmpClintId(AEMClientID);
                                    String AEMClientOfficeID = obj.optString("AEMClientOfficeID");
                                    pref.saveEmpClintOffId(AEMClientOfficeID);
                                    String MasterID = obj.optString("MasterID");
                                    pref.saveMasterId(etUserId.getText().toString());
                                    Log.d("Master", MasterID);
                                    UserType = obj.optString("UserType");
                                    pref.saveUserType(UserType);
                                    String CTCUrl = obj.optString("CtcPage");
                                    pref.saveCTCURL(CTCUrl);
                                    Log.d("ctcurl", CTCUrl);
                                    String FlagAddr = obj.optString("FlagAddr");
                                    pref.saveFlagLocation(FlagAddr);
                                    String Password = obj.optString("Password");
                                    pref.savePassword(Password);
                                    SecurityCode = obj.optString("SecurityCode");
                                    pref.saveSecurityCode(SecurityCode);
                                    String LeavePage = obj.optString("LeavePage");
                                    pref.saveLeaveUrl(LeavePage);
                                    String PayrollMenu = obj.optString("PayrollMenu");
                                    pref.savePayrollFlag(PayrollMenu);
                                    String ImgLocation = obj.optString("ImgLocation");
                                    pref.saveImgFlag(ImgLocation);
                                    String Loginid = obj.optString("Loginid");
                                    pref.saveLoginID(etUserId.getText().toString());
                                    String DailyActivityFlag = obj.optString("DailyActivityFlag");
                                    pref.saveDailyActivityFlag(DailyActivityFlag);
                                    String AttendanceEnableStatus = obj.optString("AttendanceEnableStatus");
                                    pref.saveAttenFlag(AttendanceEnableStatus);
                                    String DailyLogActivity = obj.optString("DailyLogActivity");
                                    pref.saveDailyLogFlag(DailyLogActivity);
                                    Log.d("DailyLogActivity", DailyLogActivity);
                                    String DemoFlag = obj.optString("DemoFlag");
                                    pref.saveDemoFlag(DemoFlag);
                                    String OfflineFlag = obj.optString("OfflineFlag");
                                    pref.saveOffAttnFlag(OfflineFlag);
                                    String GeoConfFlag = obj.optString("GeoConfFlag");
                                    TutorialFlag = obj.optString("TutorialFlag");
                                    pref.saveTutorialFlag(TutorialFlag);
                                    String TutorialMenuText = obj.optString("TutorialMenuText");
                                    pref.saveTutorialText(TutorialMenuText);
                                    pref.saveGeoFenceConfig(GeoConfFlag);
                                    String GeoFenceMenuFlag = obj.optString("GeoFenceMenuFlag");
                                    pref.saveGeoFenceMenuFlag(GeoFenceMenuFlag);
                                    String GeoFenceAttFlag = obj.optString("GeoFenceAttFlag");
                                    pref.saveGeoFenceFlag(GeoFenceAttFlag);
                                    String GeoMultiPointConfFlag = obj.optString("GeoMultiPointConfFlag");
                                    pref.saveFenceSubMenu(GeoMultiPointConfFlag);
                                    if (pref.getCheckFlag().equals("1")) {
                                        pref.saveLoginFlag("1");
                                    } else {
                                        pref.saveLoginFlag("2");
                                    }
                                    String LiveDriverTrackingFlag = obj.optString("LiveDriverTrackingFlag");
                                    pref.saveLivetrackingFlag(LiveDriverTrackingFlag);
                                    String LiveTrackingFlag = obj.optString("LiveTrackingFlag");
                                    pref.saveLivetrackingServiceFlag(LiveTrackingFlag);
                                    String MultiLangFlag = obj.optString("MultiLangFlag");
                                    pref.saveLanguageFlag(MultiLangFlag);
                                    pref.saveLanguage("en");
                                    LoginFlag = "1";//hardcode on sp by dk and it willbe remove .sp name:GHRMSUserAuthenticationWithnDevice_New
                                    String ITPage = obj.optString("ITPage");
                                    pref.saveITView(ITPage);
                                    String TeamRptFlag = obj.optString("TeamRptFlag");
                                    pref.saveTeamReportFlag(TeamRptFlag);
                                    IsModified = obj.optString("IsModified");

                                    String GeoFenceMapFlag = obj.optString("GeoFenceMapFlag");
                                    pref.saveEmpMapAccessFlag(GeoFenceMapFlag);
                                    String IsWeeklyOff = obj.optString("IsWeeklyOff");
                                    pref.saveWeeklyOffFlag(IsWeeklyOff);
                                    String IsHoliday = obj.optString("IsHoliday");
                                    pref.saveHolidayMapFlag(IsHoliday);


                                }
                                if (etSecurityCode.getText().toString().equals("1138")){
                                    Intent intent = new Intent(LoginActivity.this, UserDashBoardActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }else {

                                    checkBersion();
                                }
                            } else {
                                shoeDialog();
                                imgForward.setVisibility(View.VISIBLE);
                                pgBar.setVisibility(View.GONE);

                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                imgForward.setVisibility(View.VISIBLE);
                pgBar.setVisibility(View.GONE);
                //Toast.makeText(LoginActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                showAlert();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void shoeDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_invalidcredential, null);
        dialogBuilder.setView(dialogView);
        TextView tvText = (TextView) dialogView.findViewById(R.id.tvText);
        tvText.setText(responseText);
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(true);
        Window window = alertDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog.show();


    }

    private void shoeAccessDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_access, null);
        dialogBuilder.setView(dialogView);
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                al1.dismiss();
            }
        });
        al1 = dialogBuilder.create();
        al1.setCancelable(true);
        Window window = al1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        al1.show();


    }

    private void showAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("somthing went wrong");
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                    }
                });
        alertDialogBuilder.show();


    }


    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current ", strReturnedAddress.toString());
            } else {
                Log.w("My Current", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current", "Canont get Address!");
        }
        return strAdd;
    }


    private void checkBersion() {
        String surl = "https://cloud.geniusconsultant.com/GHRMSAPI/api/ApkVersionAndAutoUpdateStatus?SecurityCode=" + pref.getSecurityCode();
        Log.d("apkversion", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLeave", response);

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            JSONArray responseData = job1.optJSONArray("responseData");
                            JSONObject obj = responseData.optJSONObject(0);
                            boolean bAndriodAutoUpdateStatus = obj.optBoolean("bAndriodAutoUpdateStatus");
                            String AndriodVersion = obj.optString("AndriodVersion");
                            boolean AppRenameFlag=obj.optBoolean("AppRenameFlag");
                            String AppRenameText=obj.optString("AppRenameText");
                            pref.saveMsgStatus(AppRenameFlag);
                            pref.saveMsg(AppRenameText);
                            if (version.equals(AndriodVersion)) {
                                if (loginFlag.equals("1")) {
                                    if (IsModified.equals("1")) {
                                        Intent intent = new Intent(LoginActivity.this, UserDashBoardActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        imgForward.setVisibility(View.GONE);
                                        pgBar.setVisibility(View.VISIBLE);
                                    } else {
                                        Intent intent = new Intent(LoginActivity.this, UserDashBoardActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.putExtra("ismodiFied", IsModified);
                                        intent.putExtra("empId", AEMEmployeeID);
                                        intent.putExtra("securityCode", SecurityCode);
                                        intent.putExtra("goingstatus","1");
                                        startActivity(intent);
                                        imgForward.setVisibility(View.GONE);
                                        pgBar.setVisibility(View.VISIBLE);

                                    }
                                } else {
                                    shoeAccessDialog();
                                }

                            } else {
                                if (bAndriodAutoUpdateStatus) {
                                    upDateAlert();
                                } else {
                                    if (loginFlag.equals("1")) {
                                        if (IsModified.equals("1")) {
                                            Intent intent = new Intent(LoginActivity.this, UserDashBoardActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            imgForward.setVisibility(View.GONE);
                                            pgBar.setVisibility(View.VISIBLE);
                                        } else {
                                            Intent intent = new Intent(LoginActivity.this, UserDashBoardActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.putExtra("ismodiFied", IsModified);
                                            intent.putExtra("empId", AEMEmployeeID);
                                            intent.putExtra("securityCode", SecurityCode);
                                            startActivity(intent);
                                            imgForward.setVisibility(View.GONE);
                                            pgBar.setVisibility(View.VISIBLE);

                                        }
                                    } else {
                                        shoeAccessDialog();
                                    }
                                }
                            }


                            // boolean _status = job1.getBoolean("status")

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Toast.makeText(DashBoardActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();


                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                9000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    private void upDateAlert() {
        Intent intent = new Intent(LoginActivity.this, UpdateActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


    public void loginFunctionForPPS() {
        byte[] data = new byte[0];
        try {
            data = etPassword.getText().toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String base64 = Base64.encodeToString(data, Base64.DEFAULT).replaceAll("\\s+", "");
        ;


        String surl = "http://111.93.182.174/GeniusiOSApi/api/get_GCLAuthenticateWithEncryption?MasterID=" + etUserId.getText().toString() + "&Password=" + base64 + "&IMEI=0000&Version=" + version + "&SecurityCode=" + etSecurityCode.getText().toString() + "&DeviceID=" + refreshedToken + "&DeviceType=A";
        Log.d("inputLogin", surl);

        imgForward.setVisibility(View.GONE);
        pgBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    AEMEmployeeID = obj.optString("AEMEmployeeID");
                                    pref.saveEmpId(AEMEmployeeID);
                                    Log.d("aemp", pref.getEmpId());
                                    String Name = obj.optString("Name");
                                    pref.saveEmpName(Name);
                                    String LoginDateTime = obj.optString("LoginDateTime");
                                    pref.saveloginTime(LoginDateTime);
                                    String FlagMenu = obj.optString("FlagMenu");
                                    pref.saveMenu(FlagMenu);
                                    Log.d("menud", pref.getMenu());
                                    String AEMConsultantID = obj.optString("AEMConsultantID");
                                    pref.saveEmpConId(AEMConsultantID);
                                    String AEMClientID = obj.optString("AEMClientID");
                                    pref.saveEmpClintId(AEMClientID);
                                    String AEMClientOfficeID = obj.optString("AEMClientOfficeID");
                                    pref.saveEmpClintOffId(AEMClientOfficeID);
                                    String MasterID = obj.optString("MasterID");
                                    pref.saveMasterId(etUserId.getText().toString());
                                    Log.d("Master", MasterID);
                                    UserType = obj.optString("UserType");
                                    pref.saveUserType(UserType);
                                    String CTCUrl = obj.optString("CTCUrl");
                                    pref.saveCTCURL(CTCUrl);
                                    String WeeklyOff = obj.optString("WeeklyOff");
                                    pref.saveWeeklyoff(WeeklyOff);
                                    String Leave = obj.optString("LeaveApply");
                                    pref.saveOnLeave(Leave);
                                    String LeaveUrl = obj.optString("LeaveUrl");
                                    pref.saveLeaveUrl(LeaveUrl);
                                    String AttdImage = obj.optString("AttdImage");
                                    pref.saveAttdImg(AttdImage);
                                    String BackAttd = obj.optString("BackAttd");
                                    pref.saveBackAttd(BackAttd);
                                    String IsSupervisor = obj.optString("IsSupervisor");
                                    pref.saveSup(IsSupervisor);
                                    String CompanyName = obj.optString("CompanyName");
                                    pref.saveSecurityCode(CompanyName);
                                    String FlagAddr = obj.optString("FlagAddr");
                                    pref.saveFlagLocation(FlagAddr);
                                    String Password = obj.optString("Password");
                                    pref.savePassword(etPassword.getText().toString());
                                    String OffAttFlag = obj.optString("OffAttFlag");
                                    pref.saveOffAttnFlag(OffAttFlag);
                                    if (pref.getCheckFlag().equals("1")) {
                                        pref.saveLoginFlag("1");
                                    } else {
                                        pref.saveLoginFlag("2");
                                    }
                                    LoginFlag="1";
                                    IsModified="1";


                                }
                                checkBersion();


                                /*Intent intent = new Intent(LoginActivity.this, EmployeeDashBoardActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();*/
                                Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();


                                imgForward.setVisibility(View.VISIBLE);
                                pgBar.setVisibility(View.GONE);
                            } else {
                                shoeDialog();
                                imgForward.setVisibility(View.VISIBLE);
                                pgBar.setVisibility(View.GONE);

                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                imgForward.setVisibility(View.VISIBLE);
                pgBar.setVisibility(View.GONE);
                //Toast.makeText(LoginActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                showAlert();
                //  Toast.makeText(LoginActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();


            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                9000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



    }
    private void getBlockingStatus() {

        DatabaseReference active_users = mFirebaseInstance.getReference("Blocking");
        active_users.child(etSecurityCode.getText().toString()).child("Code").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    //for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    String code = (String) dataSnapshot.getValue();

                    Log.d("blobkcode",code);

                    if (!code.equals("") && code!=null){

                        loginFlag=code;


                    }else {
                        loginFlag="1";
                    }

                    // }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
