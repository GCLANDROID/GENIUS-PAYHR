package com.genius.payhrms.activity.activity;

import static com.genius.payhrms.activity.utility.Util.SECRET_KEY;
import static com.genius.payhrms.activity.utility.Util.encrypt;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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
import android.view.ViewGroup;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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

import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.GPSTracker;
import com.genius.payhrms.activity.utility.NetworkConnectionCheck;
import com.genius.payhrms.activity.utility.Pref;
import com.genius.payhrms.activity.utility.Util;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    TextView tvSignIn;
    EditText etUserId, etPassword;
    String userId, password;
    LinearLayout llSignIn;
    NetworkConnectionCheck connectionCheck;
    AlertDialog alertDialog,alerDialog1;
    private Dialog forgotPasswordAlertDialog;
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
    private static String INIT_VECTOR="6832054171691981";


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


        pgBar = (ProgressBar) findViewById(R.id.pgBar);
        refreshedToken = "12233";
        pref.saveRefreshToken(refreshedToken);
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

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
           // tvLogin.setText("लॉग इन");
            tvWelcome.setText("स्वागत हे");
            ckRemember.setText("कृपया याद रखें");
            tvShow.setText("प्रदर्शन");
            tvHide.setText("छिपाना");
            etUserId.setHint("अपनी उपयोगकर्ता आईडी दर्ज करें");
            etPassword.setHint("अपना पासवर्ड डालें");
            etSecurityCode.setHint("अपना गुप्त कोड डालो");
            tvForgot.setText("पासवर्ड भूल गए?");
        } else {

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
                                    if (etSecurityCode.getText().toString().equals("11")||etSecurityCode.getText().toString().equals("123")) {
                                        //loginFunctionForPPS();
                                    }else {
                                        //loginFunction();
                                        //throw new RuntimeException("testing");
                                        JSONObject obj=new JSONObject();
                                        try {
                                            obj.put("MasterID",encrypt(etUserId.getText().toString(),SECRET_KEY));
                                            obj.put("Password",encrypt(etPassword.getText().toString(),SECRET_KEY));
                                            obj.put("IMEI",android_id);
                                            obj.put("DeviceID",android_id);
                                            obj.put("DeviceType","A");
                                            obj.put("SecurityCode",etSecurityCode.getText().toString());
                                            login(obj);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
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
                /*Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);*/
                openForgotPasswordDialog();
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

    private void openForgotPasswordDialog() {
        forgotPasswordAlertDialog = new Dialog(LoginActivity.this, R.style.CustomDialogNew2);
        forgotPasswordAlertDialog.setContentView(R.layout.dialog_forgot_password);
        forgotPasswordAlertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        forgotPasswordAlertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        EditText etUserId = (EditText) forgotPasswordAlertDialog.findViewById(R.id.etUserId);
        EditText etSecurityCode = (EditText) forgotPasswordAlertDialog.findViewById(R.id.etSecurityCode);
        ImageView btnSubmit = (ImageView) forgotPasswordAlertDialog.findViewById(R.id.btnSubmit);
        ImageView imgCancel = (ImageView) forgotPasswordAlertDialog.findViewById(R.id.imgCancel);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etUserId.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please Enter User Id", Toast.LENGTH_SHORT).show();
                } else if(etSecurityCode.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please Enter Security Code", Toast.LENGTH_SHORT).show();
                } else {
                    JSONObject jsonObject=new JSONObject();
                    try {
                        jsonObject.put("MasterID",etUserId.getText().toString());
                        jsonObject.put("SecurityCode",etSecurityCode.getText().toString());
                        forgotpassword(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPasswordAlertDialog.cancel();;
            }
        });
        forgotPasswordAlertDialog.show();
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




    private void upDateAlert() {
        Intent intent = new Intent(LoginActivity.this, UpdateActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }




    private void login(JSONObject jsonObject) {
        Log.e("LOGIN", "login: "+jsonObject.toString());
        final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
        pd.setMessage("Loading..");
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
                        Log.e("LOGIN", "@@@@@@" + job1);
                        pd.dismiss();
                        responseText = job1.optString("Response_Message");
                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code==101) {
                            // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                            JSONArray responseData = job1.optJSONArray("Response_Data");
                            for (int i = 0; i < responseData.length(); i++) {
                                JSONObject obj = responseData.optJSONObject(i);
                                AEMEmployeeID = obj.optString("AEMEmployeeID");
                                pref.saveEmpId(AEMEmployeeID);
                                String Genius_Access_Token=obj.optString("Genius_Access_Token");
                                pref.saveAccessToken(Genius_Access_Token);
                                String Access_Token_Expires_On= Util.changeAnyDateFormat(obj.optString("Access_Token_Expires_On"),"dd-MM-yyyy hh:mm:ss","hh:mm");
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
                                String ShiftFlag=obj.optString("ShiftFlag");
                                pref.saveShiftFlag(ShiftFlag);



                            }
                            if (etSecurityCode.getText().toString().equals("1138")){
                                Intent intent = new Intent(LoginActivity.this, UserDashBoardActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }else {
                                /*Intent intent = new Intent(LoginActivity.this, UserDashBoardActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);*/
                                //checkBersion();
                                JSONObject object=new JSONObject();
                                try {
                                    object.put("AndroidVersion", version);
                                    object.put("SecurityCode",pref.getSecurityCode());
                                    versionCheck(object);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                /*Intent intent = new Intent(LoginActivity.this, UserDashBoardActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("ismodiFied", IsModified);
                                intent.putExtra("empId", AEMEmployeeID);
                                intent.putExtra("securityCode", SecurityCode);
                                startActivity(intent);
                                finish();*/
                            }
                        } else {
                            shoeDialog();
                            tvLogin.setVisibility(View.VISIBLE);
                            pgBar.setVisibility(View.GONE);

                        }
                        // boolean _status = job1.getBoolean("status");
                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.e("LOGIN", "onError: "+error );
                        pd.dismiss();
                    }
                });
    }

    private void versionCheck(JSONObject jsonObject) {
        Log.e(TAG, "versionCheck: INPUT: "+jsonObject);
        final ProgressDialog pd=new ProgressDialog(LoginActivity.this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(Api.sversioncheckapi)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pd.dismiss();
                        JSONObject job1 = response;
                        try {
                            Log.e(TAG, "VERSION_CHECK: " + job1.toString(4));
                            String Response_Code = job1.optString("Response_Code");
                            if (Response_Code.equals("100")) {
                                JSONObject obj2 = job1.optJSONObject("Response_Data");
                                if (obj2.optBoolean("UpdateStatus")) {
                                    Intent intent = new Intent(LoginActivity.this, UpdateActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    if (etSecurityCode.getText().toString().equals("6715")||etSecurityCode.getText().toString().equals("6716")){
                                        Intent intent = new Intent(LoginActivity.this, EmplyoeeCalendarDashboarActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.putExtra("ismodiFied", IsModified);
                                        intent.putExtra("empId", AEMEmployeeID);
                                        intent.putExtra("securityCode", SecurityCode);
                                        startActivity(intent);
                                        finish();
                                    }else {
                                        Intent intent = new Intent(LoginActivity.this, UserDashBoardActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.putExtra("ismodiFied", IsModified);
                                        intent.putExtra("empId", AEMEmployeeID);
                                        intent.putExtra("securityCode", SecurityCode);
                                        startActivity(intent);
                                        finish();
                                    }

                                }
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }


                        /*int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                            String responseData = job1.optString("Response_Data");
                            try {
                                JSONArray jsonArray=new JSONArray(responseData);
                                JSONObject obj = jsonArray.optJSONObject(0);
                                boolean bAndriodAutoUpdateStatus=obj.optBoolean("bAndriodAutoUpdateStatus");
                                String AndriodVersion=obj.optString("AndriodVersion");
                                boolean AppRenameFlag=obj.optBoolean("AppRenameFlag");
                                String AppRenameText=obj.optString("AppRenameText");
                                pref.saveMsgStatus(AppRenameFlag);
                                pref.saveMsg(AppRenameText);

                                if (version.equals(AndriodVersion)) {
                                    if (IsModified.equals("1")) {
                                        Intent intent = new Intent(LoginActivity.this, UserDashBoardActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Intent intent = new Intent(LoginActivity.this, UserDashBoardActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.putExtra("ismodiFied", IsModified);
                                        intent.putExtra("empId", AEMEmployeeID );
                                        intent.putExtra("securityCode", SecurityCode );
                                        intent.putExtra("goingstatus","1");
                                        startActivity(intent);
                                        finish();
                                    }
                                } else {
                                    if (bAndriodAutoUpdateStatus){
                                        Intent intent = new Intent(LoginActivity.this, UpdateActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }else {
                                        if (IsModified.equals("1")) {
                                            Intent intent = new Intent(LoginActivity.this, UserDashBoardActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Intent intent = new Intent(LoginActivity.this, UserDashBoardActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.putExtra("ismodiFied", IsModified);
                                            intent.putExtra("empId", AEMEmployeeID );
                                            intent.putExtra("securityCode", SecurityCode );
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            // boolean _status = job1.getBoolean("status");


                            // do anything with response
                        }else {

                        }*/
                    }

                    @Override
                    public void onError(ANError error) {


                            pd.dismiss();



                    }
                });
    }


    private void forgotpassword(JSONObject jsonObject) {
        final ProgressDialog progressDialog=new ProgressDialog(LoginActivity.this);
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
                            forgotPasswordAlertDialog.dismiss();
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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this, R.style.CustomDialogNew);
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
