package com.genius.payhrms.activity.activity;

import static com.genius.payhrms.activity.utility.Util.SECRET_KEY;
import static com.genius.payhrms.activity.utility.Util.encrypt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
import com.genius.payhrms.activity.model.HoliDayModel;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.CreativePermission;
import com.genius.payhrms.activity.utility.NetworkConnectionCheck;
import com.genius.payhrms.activity.utility.Pref;
import com.genius.payhrms.activity.utility.Util;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class SplashScreenActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "SplashScreenActivity";
    private static final int PERMISSION_ALL = 100;
    private CreativePermission myPermission;
    GoogleApiClient googleApiClient;
    private NetworkConnectionCheck connectionCheck;
    Pref pref;
    String android_id, refreshedToken;
    String version;
    boolean responseStatus;
    AlertDialog alertDialog;
    String IsModified;
    String AEMEmployeeID;
    String SecurityCode;

    private static boolean s_persistenceInitialized = false;
    String loginFlag = "1";
    private ReviewManager reviewManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initialize();
        CheckPermission();
    }

    private void showSplash() {
        if (pref.getLoginFlag().equals("1")) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (loginFlag.equals("1")) {
                        // loginFunction();
                        JSONObject obj = new JSONObject();
                        try {
                            obj.put("MasterID", encrypt(pref.getMasterId(), SECRET_KEY));
                            obj.put("Password", encrypt(pref.getPassword(), SECRET_KEY));
                            obj.put("IMEI", android_id);
                            obj.put("DeviceID", android_id);
                            obj.put("DeviceType", "A");
                            obj.put("SecurityCode", pref.getSecurityCode());
                            login(obj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(SplashScreenActivity.this, "Block by administrator", Toast.LENGTH_LONG).show();
                    }


                }
            }, 3000);
        } else {
            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
            finish();
        }
    }


    private void CheckPermission() {
        if (!myPermission.hasPermissions()) {
            myPermission.reqPermisions();
        } else {
            setup();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ALL) {
            setup();

        } else {
            finish();
        }
    }


    private void initialize() {
        pref = new Pref(getApplicationContext());
        reviewManager = ReviewManagerFactory.create(this);
        connectionCheck = new NetworkConnectionCheck(this);
        myPermission = new CreativePermission(this, PERMISSION_ALL);
        refreshedToken = "12344";
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

        try {
            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
            int verCode = pInfo.versionCode;
            Log.d("sddk", version);
            Log.d("sdkl", String.valueOf(verCode));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        //  RateApp(SplashScreenActivity.this);
    }

    private void setup() {
        if (connectionCheck.isGPSEnabled()) {
            if (connectionCheck.isNetworkAvailable()) {
                showSplash();
            } else {
                startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                finish();
            }
        } else {
            turnGPSOn();
        }
    }

    private void turnGPSOn() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(SplashScreenActivity.this).build();
            googleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            // **************************
            builder.setAlwaysShow(true); // this is the key ingredient
            // **************************

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                    .checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result
                            .getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            showSplash();
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                try {
                                    status.startResolutionForResult(SplashScreenActivity.this, 1000);
                                } catch (IntentSender.SendIntentException e) {
                                    // Ignore the error.
                                }
                            } catch (Exception e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

                            break;
                    }
                }
            });
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                showSplash();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                finish();
                //Write your code if there's no result
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    private void login(JSONObject jsonObject) {

        final ProgressDialog pd = new ProgressDialog(SplashScreenActivity.this);
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
                        Log.e("response12", "@@@@@@" + job1);
                        pd.dismiss();
                        String responseText = job1.optString("Response_Message");
                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                            JSONArray responseData = job1.optJSONArray("Response_Data");
                            for (int i = 0; i < responseData.length(); i++) {
                                JSONObject obj = responseData.optJSONObject(i);
                                AEMEmployeeID = obj.optString("AEMEmployeeID");
                                pref.saveEmpId(AEMEmployeeID);
                                String Genius_Access_Token = obj.optString("Genius_Access_Token");
                                pref.saveAccessToken(Genius_Access_Token);
                                String Access_Token_Expires_On = Util.changeAnyDateFormat(obj.optString("Access_Token_Expires_On"), "dd-MM-yyyy hh:mm:ss", "hh:mm");
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
                                Log.d("Master", MasterID);
                                String UserType = obj.optString("UserType");
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
                                String TutorialFlag = obj.optString("TutorialFlag");
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
                                String LoginFlag = "1";//hardcode on sp by dk and it willbe remove .sp name:GHRMSUserAuthenticationWithnDevice_New
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
                            if (pref.getSecurityCode().equals("1138")) {
                                Intent intent = new Intent(SplashScreenActivity.this, UserDashBoardActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {

                                //checkBersion();

                                JSONObject object = new JSONObject();
                                try {
                                    object.put("AndroidVersion", version);
                                    object.put("SecurityCode", pref.getSecurityCode());
                                    versionCheck(object);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                /*Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();*/
                            }
                        } else {
                            Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                        // boolean _status = job1.getBoolean("status");
                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                        Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    private void versionCheck(JSONObject jsonObject) {
        Log.e(TAG, "versionCheck: " + jsonObject.toString());
        final ProgressDialog pd = new ProgressDialog(SplashScreenActivity.this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(Api.sversioncheckapi)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
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
                                    Intent intent = new Intent(SplashScreenActivity.this, UpdateActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    if (pref.getSecurityCode().equals("6715")){
                                        Intent intent = new Intent(SplashScreenActivity.this, EmplyoeeCalendarDashboarActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.putExtra("ismodiFied", IsModified);
                                        intent.putExtra("empId", AEMEmployeeID);
                                        intent.putExtra("securityCode", SecurityCode);
                                        startActivity(intent);
                                        finish();
                                    }else {
                                        Intent intent = new Intent(SplashScreenActivity.this, UserDashBoardActivity.class);
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
                    }

                    @Override
                    public void onError(ANError error) {


                        pd.dismiss();
                        Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();


                    }
                });
    }

    private void upDateAlert(String upDateVersion, String currentVersion) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SplashScreenActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_update_alert, null);
        dialogBuilder.setView(dialogView);
        TextView tvAttenDate = (TextView) dialogView.findViewById(R.id.tvAttenDate);
        tvAttenDate.setText("New version " + upDateVersion + " is available");
        TextView tvCurrent = (TextView) dialogView.findViewById(R.id.tvCurrent);
        tvCurrent.setText("Current Version is :" + currentVersion);
        Button btnSkip = (Button) dialogView.findViewById(R.id.btnSkip);

        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
                }
                alertDialog.dismiss();


            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashScreenActivity.this, UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        Window window = alertDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog.show();
    }


}
