package com.genius.hrms.activity.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;

import android.os.Bundle;

import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genius.hrms.R;

import com.genius.hrms.activity.attendance.AttendanceActivity;
import com.genius.hrms.activity.dailyactivity.DailyTaskDashBoardActivity;
import com.genius.hrms.activity.dailylog.OfflineDailyDashBoardActivity;
import com.genius.hrms.activity.geofence.ConfigNumberActivity;
import com.genius.hrms.activity.geofence.NotificationActivity;
import com.genius.hrms.activity.leaveapplication.LeaveApplicationActivity;
import com.genius.hrms.activity.leaveapplication.LeaveWebViewActivity;
import com.genius.hrms.activity.payroll.PayrollActivity;
import com.genius.hrms.activity.profile.ProfileActivity;
import com.genius.hrms.activity.utility.Pref;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

public class EmployeeDashBoardActivity extends AppCompatActivity {
    TextView tvGreeting, tvLoginDateTime, tvEmployeeName;
    Pref pref;
    LinearLayout llProfile, llAttenDance, llPayroll, llHoliday, llLeave, llDailyActivity, llDailyLog;
    String menu;
    String s1, s2, s3, s4, s5, s6;
    String dailyLog;
    LinearLayout llGeoFence;
    androidx.appcompat.widget.Toolbar toolbar;
    DrawerLayout dlMain;
    ImageView imgageView;
    boolean mslideState;
    LinearLayout llLogOut, llLearning;
    TextView tvLearning;
    String tutorialFlag;
    LinearLayout llBlank;
    TextView tvtoolbar, tvProfile, tvDailyLog, tvPayroll, tvAttendance, tvLeave, tvGeoFence, tvDailyActivity, tvLogOut,tvSubReport,tvChangePassword;
    private BroadcastReceiver receiver;
    String android_id;
    LinearLayout llTracking, llLanguage;
    AlertDialog alert2;
    String language;
    ProgressDialog progressBar;
    TextView tvTracking, tvLanguage;
    String lebelId;
    TextView tvHoliday;
    LinearLayout llSupport;
    AlertDialog al1;
    File file, compressedImageFile;
    private static final int REQUEST_GALLERY_CODE = 200;
    private static final int READ_REQUEST_CODE = 300;
    private Uri uri;
    String encodedImage;
    ImageView imgPic;
    EditText etRemarks;
    LinearLayout llNotification,llChangePassword,llChat;
    String ipAddress;
    AlertDialog alert1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_dash_board);
        intialize();



        // autoLaunchVivo(this);
        onClick();
    }

    private void intialize() {
        pref = new Pref(getApplicationContext());
        pref.setFirstTimeLaunch(true);
        if (pref.getAccessFlag().equals("1")){
            Intent intent=new Intent(EmployeeDashBoardActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }else {

        }
        tvGreeting = (TextView) findViewById(R.id.tvGreeting);
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) {
            if (pref.getLanguage().equals("hi")) {
                tvGreeting.setText("शुभ प्रभात");
            } else {
                tvGreeting.setText("Good Morning");
            }
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            if (pref.getLanguage().equals("hi")) {
                tvGreeting.setText("नमस्कार");
            } else {
                tvGreeting.setText("Good Afternoon");
            }
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            if (pref.getLanguage().equals("hi")) {
                tvGreeting.setText("सुसंध्या");
            } else {
                tvGreeting.setText("Good Evening");
            }

        } else if (timeOfDay >= 21 && timeOfDay < 24) {

            if (pref.getLanguage().equals("hi")) {
                tvGreeting.setText("सुसंध्या");

            } else {
                tvGreeting.setText("Good Evening");

            }
        }
        tvLoginDateTime = (TextView) findViewById(R.id.tvLoginDateTime);
        tvLoginDateTime.setText(pref.getloginTime());
        tvEmployeeName = (TextView) findViewById(R.id.tvEmployeeName);
        tvEmployeeName.setText(pref.getEmpName());
        llProfile = (LinearLayout) findViewById(R.id.llProfile);
        llAttenDance = (LinearLayout) findViewById(R.id.llAttenDance);
        llPayroll = (LinearLayout) findViewById(R.id.llPayroll);
        llHoliday = (LinearLayout) findViewById(R.id.llHoliday);
        llLeave = (LinearLayout) findViewById(R.id.llLeave);
        llBlank = (LinearLayout) findViewById(R.id.llBlank);
        llNotification=(LinearLayout)findViewById(R.id.llNotification) ;
        llChangePassword=(LinearLayout)findViewById(R.id.llChangePassword);
        llChat=(LinearLayout)findViewById(R.id.llChat);


        llDailyActivity = (LinearLayout) findViewById(R.id.llDailyActivity);
        if (pref.getDailyActivityFlag().equals("1")) {
            llDailyActivity.setVisibility(View.VISIBLE);
        } else {
            llDailyActivity.setVisibility(View.GONE);
        }
        menu = pref.getMenu();
        if (pref.getMenu().equals("")) {
            llProfile.setVisibility(View.VISIBLE);
            llAttenDance.setVisibility(View.VISIBLE);
            llPayroll.setVisibility(View.VISIBLE);
            llHoliday.setVisibility(View.VISIBLE);
            llLeave.setVisibility(View.VISIBLE);
        } else {
            String d = menu.replace("{", "").replace("}", "");
            Log.d("split", d);
            Log.d("menuu", menu);
            String[] separated = menu.split(",");
            if (separated.length == 2) {
                Log.d("arpan", "riku");
                s1 = separated[0];
                s2 = separated[1];
                if (s1.equals("1")) {
                    llProfile.setVisibility(View.GONE);
                } else if (s1.equals("2")) {
                    llAttenDance.setVisibility(View.GONE);
                } else if (s1.equals("3")) {
                    llPayroll.setVisibility(View.GONE);
                } else if (s1.equals("4")) {
                    llLeave.setVisibility(View.GONE);

                } else if (s1.equals("5")) {
                    llHoliday.setVisibility(View.GONE);


                }


                if (s2.equals("1")) {
                    llProfile.setVisibility(View.GONE);
                } else if (s2.equals("2")) {
                    llAttenDance.setVisibility(View.GONE);
                } else if (s2.equals("3")) {
                    llPayroll.setVisibility(View.GONE);
                } else if (s2.equals("4")) {

                    llLeave.setVisibility(View.GONE);

                } else if (s2.equals("5")) {
                    llHoliday.setVisibility(View.GONE);

                }

            } else if (separated.length == 3) {
                Log.d("arpan", "riku1");
                s1 = separated[0];
                s2 = separated[1];
                s3 = separated[2];
                Log.d("co", s2);

                if (s1.equals("1")) {
                    llProfile.setVisibility(View.GONE);
                } else if (s1.equals("2")) {
                    llAttenDance.setVisibility(View.GONE);
                } else if (s1.equals("3")) {
                    llPayroll.setVisibility(View.GONE);
                } else if (s1.equals("4")) {
                    llLeave.setVisibility(View.GONE);

                } else if (s1.equals("5")) {
                    llHoliday.setVisibility(View.GONE);


                }


                if (s2.equals("1")) {
                    llProfile.setVisibility(View.GONE);
                } else if (s2.equals("2")) {
                    llAttenDance.setVisibility(View.GONE);
                } else if (s2.equals("3")) {
                    llPayroll.setVisibility(View.GONE);
                } else if (s2.equals("4")) {

                    llLeave.setVisibility(View.GONE);

                } else if (s2.equals("5")) {
                    llHoliday.setVisibility(View.GONE);

                }

                if (s2.equals("1")) {
                    llProfile.setVisibility(View.GONE);
                } else if (s2.equals("2")) {
                    llAttenDance.setVisibility(View.GONE);
                } else if (s2.equals("3")) {
                    llPayroll.setVisibility(View.GONE);
                } else if (s2.equals("4")) {

                    llLeave.setVisibility(View.GONE);

                } else if (s2.equals("5")) {
                    llHoliday.setVisibility(View.GONE);

                }

                if (s3.equals("1")) {
                    llProfile.setVisibility(View.GONE);
                } else if (s3.equals("2")) {
                    llAttenDance.setVisibility(View.GONE);
                } else if (s3.equals("3")) {
                    llPayroll.setVisibility(View.GONE);
                } else if (s3.equals("4")) {

                    llLeave.setVisibility(View.GONE);

                } else if (s3.equals("5")) {
                    llHoliday.setVisibility(View.GONE);

                }
            } else if (separated.length == 4) {
                Log.d("arpan", "riku1");
                s1 = separated[0];
                s2 = separated[1];
                s3 = separated[2];
                s4 = separated[3];
                Log.d("co", s3);


                if (s1.equals("1")) {
                    llProfile.setVisibility(View.GONE);
                } else if (s1.equals("2")) {
                    llAttenDance.setVisibility(View.GONE);
                } else if (s1.equals("3")) {
                    llPayroll.setVisibility(View.GONE);
                } else if (s1.equals("4")) {
                    llLeave.setVisibility(View.GONE);

                } else if (s1.equals("5")) {
                    llHoliday.setVisibility(View.GONE);


                }


                if (s2.equals("1")) {
                    llProfile.setVisibility(View.GONE);
                } else if (s2.equals("2")) {
                    llAttenDance.setVisibility(View.GONE);
                } else if (s2.equals("3")) {
                    llPayroll.setVisibility(View.GONE);
                } else if (s2.equals("4")) {

                    llLeave.setVisibility(View.GONE);

                } else if (s2.equals("5")) {
                    llHoliday.setVisibility(View.GONE);

                }

                if (s2.equals("1")) {
                    llProfile.setVisibility(View.GONE);
                } else if (s2.equals("2")) {
                    llAttenDance.setVisibility(View.GONE);
                } else if (s2.equals("3")) {
                    llPayroll.setVisibility(View.GONE);
                } else if (s2.equals("4")) {

                    llLeave.setVisibility(View.GONE);

                } else if (s2.equals("5")) {
                    llHoliday.setVisibility(View.GONE);

                }

                if (s3.equals("1")) {
                    llProfile.setVisibility(View.GONE);
                } else if (s3.equals("2")) {
                    llAttenDance.setVisibility(View.GONE);
                } else if (s3.equals("3")) {
                    llPayroll.setVisibility(View.GONE);
                } else if (s3.equals("4")) {

                    llLeave.setVisibility(View.GONE);

                } else if (s3.equals("5")) {
                    llHoliday.setVisibility(View.GONE);

                }

                if (s4.equals("1")) {
                    llProfile.setVisibility(View.GONE);
                } else if (s4.equals("2")) {
                    llAttenDance.setVisibility(View.GONE);
                } else if (s4.equals("3")) {
                    llPayroll.setVisibility(View.GONE);
                } else if (s4.equals("4")) {

                    llLeave.setVisibility(View.GONE);

                } else if (s4.equals("5")) {
                    llHoliday.setVisibility(View.GONE);

                }
            } else if (separated.length == 1) {
                Log.d("arpan", "riku1");
                s1 = separated[0];
                Log.d("arpan", "riku");
                if (s1.equals("1")) {
                    llProfile.setVisibility(View.GONE);
                } else if (s1.equals("2")) {
                    llAttenDance.setVisibility(View.GONE);
                } else if (s1.equals("3")) {
                    llPayroll.setVisibility(View.GONE);
                } else if (s1.equals("4")) {
                    llLeave.setVisibility(View.GONE);

                } else if (s1.equals("5")) {
                    llHoliday.setVisibility(View.GONE);


                }


            }
            final ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
            progressDialog.setMessage("Loding");
            language = pref.getLanguage();

            progressBar = new ProgressDialog(EmployeeDashBoardActivity.this);
            progressBar.setMessage("Loading..");
            progressBar.setCancelable(false);

            tvTracking = (TextView) findViewById(R.id.tvTracking);
            tvLanguage = (TextView) findViewById(R.id.tvLanguage);


        }

        llDailyLog = (LinearLayout) findViewById(R.id.llDailyLog);
        llSupport = (LinearLayout) findViewById(R.id.llSupport);

        if (pref.getDailyLogFlag().equals("1")) {
            llDailyLog.setVisibility(View.VISIBLE);
        } else if (pref.getDailyLogFlag().equals("0")) {
            llDailyLog.setVisibility(View.GONE);
        }

        llGeoFence = (LinearLayout) findViewById(R.id.llGeoFence);
        if (pref.getGeoFenceMenuFlag().equals("1")) {
            llGeoFence.setVisibility(View.VISIBLE);
        } else {
            llGeoFence.setVisibility(View.GONE);
        }

        imgageView = (ImageView) findViewById(R.id.imageview);
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        dlMain = (DrawerLayout) findViewById(R.id.dlMain);
        llLogOut = (LinearLayout) findViewById(R.id.llLogOut);
        llLearning = (LinearLayout) findViewById(R.id.llLearning);
        tvLearning = (TextView) findViewById(R.id.tvLearning);
        tvSubReport=(TextView)findViewById(R.id.tvSubReport);
        tutorialFlag = pref.getTutorialFlag();
        if (tutorialFlag.equals("1")) {
            llLearning.setVisibility(View.VISIBLE);
        } else {
            llLearning.setVisibility(View.GONE);
        }

        //tvLearning.setText(pref.getTutorialText());
        tvtoolbar = (TextView) findViewById(R.id.tvtoolbar);
        tvProfile = (TextView) findViewById(R.id.tvProfile);
        tvDailyLog = (TextView) findViewById(R.id.tvDailyLog);
        tvPayroll = (TextView) findViewById(R.id.tvPayroll);
        tvAttendance = (TextView) findViewById(R.id.tvAttendance);
        tvLeave = (TextView) findViewById(R.id.tvLeave);
        tvGeoFence = (TextView) findViewById(R.id.tvGeoFence);
        tvDailyActivity = (TextView) findViewById(R.id.tvDailyActivity);
        tvLogOut = (TextView) findViewById(R.id.tvLogOut);
        tvHoliday = (TextView) findViewById(R.id.tvHoliday);
        tvChangePassword=(TextView)findViewById(R.id.tvChangePassword);

        if (language.equals("hi")) {
            tvtoolbar.setText("डैशबोर्ड");
            tvProfile.setText("प्रोफ़ाइल");
            tvDailyLog.setText("दैनिक लॉग");
            tvPayroll.setText("पेरोल");
            tvAttendance.setText("उपस्थिति");
            tvLeave.setText("छुट्टी की अर्जी");
            tvGeoFence.setText("भू बाड़");
            tvDailyActivity.setText("दैनिक गतिविधि");
            tvLogOut.setText("लॉग आउट");
            tvTracking.setText("लाइव ट्रैकिंग");
            tvLanguage.setText("भाषा सेटिंग");
            tvHoliday.setText("छुट्टी की सूची");
            tvSubReport.setText("अधीनस्थ रिपोर्ट");
            tvChangePassword.setText("पासवर्ड बदलें");
            final Handler textViewHandler13 = new Handler();
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    TranslateOptions options = TranslateOptions.newBuilder()
                            .setApiKey("AIzaSyDL1itt-7WRkrelJeuvOfiC-_SGc3JZ4vY")
                            .build();
                    Translate translate = options.getService();
                    final Translation translation =
                            translate.translate(pref.getEmpName(),
                                    Translate.TranslateOption.sourceLanguage("en"),    Translate.TranslateOption.targetLanguage("hi"));
                    textViewHandler13.post(new Runnable() {
                        @Override
                        public void run() {

                            Log.d("sssh", translation.getTranslatedText());
                            String h = translation.getTranslatedText();
                            tvEmployeeName.setText(h);

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

            final Handler textViewHandler1 = new Handler();
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    TranslateOptions options = TranslateOptions.newBuilder()
                            .setApiKey("AIzaSyDL1itt-7WRkrelJeuvOfiC-_SGc3JZ4vY")
                            .build();
                    Translate translate = options.getService();
                    final Translation translation =
                            translate.translate(pref.getTutorialText(),
                                    Translate.TranslateOption.sourceLanguage("en"),  Translate.TranslateOption.targetLanguage("hi"));
                    textViewHandler1.post(new Runnable() {
                        @Override
                        public void run() {

                            Log.d("sssh", translation.getTranslatedText());
                            String h = translation.getTranslatedText();
                            tvLearning.setText(h);

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
            tvtoolbar.setText("Dashboard");
            tvProfile.setText("Profile");
            tvDailyLog.setText("Daily Log");
            tvPayroll.setText("Payroll");
            tvAttendance.setText("Attendance");
            tvLeave.setText("Leave Application");
            tvGeoFence.setText("Geo Fence");
            tvDailyActivity.setText("Daily Activity");
            tvLogOut.setText("Log Out");
            tvTracking.setText("Live tracking");
            tvLanguage.setText("Language setting");
            tvHoliday.setText("Holiday List");
            tvSubReport.setText("Subordinate Report");
            tvChangePassword.setText("Change Password");
            tvEmployeeName.setText(pref.getEmpName());
            tvLearning.setText(pref.getTutorialText());



        }

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
        llTracking = (LinearLayout) findViewById(R.id.llTracking);
        if (pref.getLivetrackingFlag().equals("1")) {
            llTracking.setVisibility(View.GONE);
        } else {
            llTracking.setVisibility(View.GONE);
        }
        llLanguage = (LinearLayout) findViewById(R.id.llLanguage);
        if (pref.getLanguageFlag().equals("1")) {
            llLanguage.setVisibility(View.VISIBLE);
        } else {
            llLanguage.setVisibility(View.GONE);
        }
        if (pref.getLivetrackingFlag().equals("1")) {
            //  startTracking();
        } else {

        }
        if (pref.getTeamReportFlag().equals("1")){
            llNotification.setVisibility(View.VISIBLE);
        }else {
            llNotification.setVisibility(View.GONE);
        }

        if (pref.getSecurityCode().equals("1080")){
            ipAddress="https://adityabirla.geniusconsultant.com/";

        }else {
            ipAddress="https://cloud.geniusconsultant.com/";
        }
        pref.saveIpAddress(ipAddress);

    }


    private void onClick() {
        llProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployeeDashBoardActivity.this, ProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        llAttenDance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployeeDashBoardActivity.this, AttendanceActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        llPayroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployeeDashBoardActivity.this, PayrollActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        llHoliday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployeeDashBoardActivity.this, HolidayActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        llLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pref.getSecurityCode().equals("5555")){
                    Intent intent = new Intent(EmployeeDashBoardActivity.this, LeaveWebViewActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(EmployeeDashBoardActivity.this, LeaveApplicationActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        llDailyActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployeeDashBoardActivity.this, DailyTaskDashBoardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        llDailyLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(EmployeeDashBoardActivity.this, OfflineDailyDashBoardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }
        });
        llLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutFunction();

            }
        });
        llGeoFence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployeeDashBoardActivity.this, ConfigNumberActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        llTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(EmployeeDashBoardActivity.this, EmployeeSearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);*/

            }
        });

        llChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EmployeeDashBoardActivity.this,ChangePasswordActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        llLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoeDialog();
            }
        });

        imgageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlMain.openDrawer(Gravity.LEFT);
            }
        });

        dlMain.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {

            }

            @Override
            public void onDrawerOpened(@NonNull View view) {
                mslideState = true;

            }

            @Override
            public void onDrawerClosed(@NonNull View view) {
                mslideState = false;

            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });

        llLearning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployeeDashBoardActivity.this, ELearningActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }
        });
        llBlank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        llSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTechnicalSupport();
                dlMain.closeDrawer(Gravity.LEFT);
            }
        });
        llNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlMain.closeDrawer(Gravity.LEFT);
                Intent intent=new Intent(getApplicationContext(), NotificationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        llChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlMain.closeDrawer(Gravity.LEFT);
                Intent intent=new Intent(getApplicationContext(), ChatActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void operBrowser() {
        Uri uri = Uri.parse(pref.getLeaveUrl()); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (!pref.getLeaveUrl().equals("")) {
            startActivity(intent);
        } else {

        }
    }

    private static void autoLaunchVivo(Context context) {
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.iqoo.secure",
                    "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity"));
            context.startActivity(intent);
        } catch (Exception e) {
            try {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.vivo.permissionmanager",
                        "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
                context.startActivity(intent);
            } catch (Exception ex) {
                try {
                    Intent intent = new Intent();
                    intent.setClassName("com.iqoo.secure",
                            "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager");
                    context.startActivity(intent);
                } catch (Exception exx) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void logoutFunction() {
        final ProgressDialog progressDialog = new ProgressDialog(EmployeeDashBoardActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("LOADING...");
        progressDialog.show();
        byte[] data = new byte[0];
        try {
            data = pref.getPassword().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String base64 = Base64.encodeToString(data, Base64.DEFAULT).replaceAll("\\s+", "");


        String surl = ipAddress+"GHRMSApi/api/get_UserLogout?MasterID=" + pref.getLoginID() + "&Password=" + base64 + "&IMEI=" + android_id + "&Version=v1&SecurityCode=" + pref.getSecurityCode() + "&DeviceID=" + pref.getRefreshToken() + "&DeviceType=A";
        Log.d("inputLogout", surl);


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


                                progressDialog.dismiss();

                                Intent intent = new Intent(EmployeeDashBoardActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                                pref.setFirstTimeLaunch(false);
                                pref.saveLoginFlag("2");

                            } else {
                                progressDialog.dismiss();

                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EmployeeDashBoardActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(EmployeeDashBoardActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }





    private void shoeDialog() {


    }

    @Override
    protected void onResume() {
        super.onResume();
        language = pref.getLanguage();
    }

    private void openWhatsApp() {
        String text="support:-"+"\n"+etRemarks.getText().toString();
        String toNumber = "+91 9804043285"; // contains spaces.
        toNumber = toNumber.replace("+", "").replace(" ", "");
        Uri imgUri = Uri.parse(file.getAbsolutePath());
        Intent sendIntent = new Intent("android.intent.action.MAIN");
        sendIntent.putExtra("jid", toNumber + "@s.whatsapp.net");
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
        sendIntent.setType("image/jpeg");
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setPackage("com.whatsapp");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }


    private void showTechnicalSupport() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EmployeeDashBoardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.technical_layput, null);
        dialogBuilder.setView(dialogView);
        ImageView imgCancel = (ImageView) dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                al1.dismiss();
            }
        });
        ImageView imgAttach = (ImageView) dialogView.findViewById(R.id.imgAttach);
        imgAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryIntent();
            }
        });
        imgPic = (ImageView) dialogView.findViewById(R.id.imgPic);
        Button btnSubmit=(Button)dialogView.findViewById(R.id.btnSubmit);
         etRemarks=(EditText)dialogView.findViewById(R.id.etRemarks);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhatsApp();
            }
        });
        al1 = dialogBuilder.create();
        al1.setCancelable(true);
        Window window = al1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        al1.show();


    }


    private void galleryIntent() {
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
        openGalleryIntent.setType("image/*");
        startActivityForResult(openGalleryIntent, REQUEST_GALLERY_CODE);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case REQUEST_GALLERY_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    InputStream imageStream = null;
                    try {
                        try {
                            uri = data.getData();
                            String filePath = getRealPathFromURIPath(uri, EmployeeDashBoardActivity.this);
                            file = new File(filePath);
                            //  Log.d(TAG, "filePath=" + filePath);
                            imageStream = getContentResolver().openInputStream(uri);
                            Bitmap bm = cropToSquare(BitmapFactory.decodeStream(imageStream));
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.PNG, 10, baos); //bm is the bitmap object
                            byte[] b = baos.toByteArray();
                            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                            imgPic.setImageBitmap(bm);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }

                }
                break;


        }


    }





    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    public static Bitmap cropToSquare(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = (height > width) ? width : height;
        int newHeight = (height > width) ? height - (height - width) : height;
        int cropW = (width - height) / 2;
        cropW = (cropW < 0) ? 0 : cropW;
        int cropH = (height - width) / 2;
        cropH = (cropH < 0) ? 0 : cropH;
        Bitmap cropImg = Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight);

        return cropImg;
    }

    private void msgAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EmployeeDashBoardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.msg_dialog, null);
        dialogBuilder.setView(dialogView);

        ImageView imgCancel=(ImageView)dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert1.dismiss();
            }
        });


        alert1 = dialogBuilder.create();
        alert1.setCancelable(true);
        Window window = alert1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.TOP );
        alert1.show();
    }


}
