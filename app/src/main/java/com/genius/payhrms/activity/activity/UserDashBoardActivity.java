package com.genius.payhrms.activity.activity;

import static com.genius.payhrms.activity.utility.Util.SECRET_KEY;
import static com.genius.payhrms.activity.utility.Util.encrypt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.genius.payhrms.activity.adapter.MenuItemAdapter;
import com.genius.payhrms.activity.leaveapplication.LeaveApplicationDashboardActivity;
import com.genius.payhrms.activity.model.HoliDayModel;
import com.genius.payhrms.activity.model.MenuItemModel;
import com.genius.payhrms.activity.reciver.DailylogSyncReciever;
import com.genius.payhrms.activity.reciver.NetworkStateChecker;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.Pref;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import im.delight.android.webview.AdvancedWebView;

public class UserDashBoardActivity extends AppCompatActivity {
    private static final String TAG = "UserDashBoardActivity";
    LinearLayout llLoader, llMain, llNoConnection;
    RecyclerView rvItem;
    String ipAddress;
    Pref pref;
    ArrayList<MenuItemModel> itemList = new ArrayList<>();
    SwipeRefreshLayout swipeToRefresh;
    EditText etSearch;
    MenuItemAdapter attendanceAdapter;
    AlertDialog alert2;
    TextView tvGreeting, tvLoginDateTime, tvEmployeeName;
    AlertDialog alerDialog1, alert1;
    LinearLayout llUser;
    ImageView imgLogout;
    String formattedDate, deviceName, menuName;
    ImageView imgUser;
    NetworkStateChecker airplaneModeChangeReceiver = new NetworkStateChecker();
    DailylogSyncReciever dailyLogReciever = new DailylogSyncReciever();
    String appVersionName;
    public static boolean isAppMinimizeDashboard = false;
    PDFView pdfView;
    LinearLayout llPdfLoading;
    TextView tvPdfPageNo;
    Dialog dialog;




    private DownloadManager dm;
    private long downloadId = -1L;

    // === dialog bits ===
    private AlertDialog progressDialog;
    private ProgressBar progressBarDialog;
    private TextView tvPercentDialog;

    private final Handler handler = new Handler(Looper.getMainLooper());
    String partAURL, partBURL;
    String incrementLetter,promotionLetter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dash_board);
        initView();
        onClick();
    }

    private void initView() {
        pref = new Pref(UserDashBoardActivity.this);
        // Referencing the button
        pref.setFirstTimeLaunch(true);

        dialog = new Dialog(UserDashBoardActivity.this, R.style.CustomDialogNew2);
        //LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View dialogView = inflater.inflate(R.layout.training_popup_layout, null);
        dialog.setContentView(R.layout.formsixteen_pdf_popup);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        imgUser = (ImageView) findViewById(R.id.imgUser);
        llLoader = (LinearLayout) findViewById(R.id.llLoader);
        llMain = (LinearLayout) findViewById(R.id.llMain);
        llNoConnection = (LinearLayout) findViewById(R.id.llNoConnection);
        imgLogout = findViewById(R.id.imgLogout);

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            int versionCode = pInfo.versionCode;
            // Now you can use version and versionCode as needed
            Log.e(TAG, "Version Name: " + version + " Version Code: " + versionCode);
            appVersionName = version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        rvItem = (RecyclerView) findViewById(R.id.rvItem);
        rvItem.setLayoutManager(new GridLayoutManager(this, 3));

        if (pref.getSecurityCode().equals("1080")) {
            ipAddress = "https://adityabirla.geniusconsultant.com/";
        } else {
            ipAddress = "https://cloud.geniusconsultant.com/";
        }
        pref.saveIpAddress(ipAddress);
        swipeToRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        etSearch = (EditText) findViewById(R.id.etSearch);
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


        tvEmployeeName = (TextView) findViewById(R.id.tvEmployeeName);
        tvEmployeeName.setText(pref.getEmpName());
        if (pref.getMsgStatus()) {
            msgAlert();
        } else {

        }


        Date cd = Calendar.getInstance().getTime();
        System.out.println("Current time => " + cd);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        formattedDate = df.format(cd);
        deviceName = android.os.Build.MODEL;
        //activeUsers();

        SharedPreferences prefs = getSharedPreferences("com.genius.hrms", MODE_PRIVATE);

        int launch_count = prefs.getInt("launch_count", 0);

        if (launch_count >= 3) {
            // third time launch
            // Toast.makeText(DashBoardActivity.this,"3 time",Toast.LENGTH_LONG).show();
            RateApp(UserDashBoardActivity.this);

        } else {
            prefs.edit()
                    .putInt("launch_count", launch_count + 1)
                    .apply();
        }

        JSONObject obUserDeviceDetails = new JSONObject();
        try {
            obUserDeviceDetails.put("EmployeeID", pref.getEmpId());
            obUserDeviceDetails.put("DeviceVersion", appVersionName);
            obUserDeviceDetails.put("IMEI", "0");
            obUserDeviceDetails.put("DeviceID", "0");
            obUserDeviceDetails.put("DeviceType", "A");
            obUserDeviceDetails.put("SecurityCode", pref.getSecurityCode());
            SaveUserDeviceDetails(obUserDeviceDetails);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
    }

    private void SaveUserDeviceDetails(JSONObject obUserDeviceDetails) {
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoConnection.setVisibility(View.GONE);
        AndroidNetworking.post(Api.UserDeviceDetails)
                .addJSONObjectBody(obUserDeviceDetails)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "USER_DEVICE_DETAILS: " + response.toString(4));
                            //pd.dismiss();
                            //profileFunction();
                            if (pref.getSecurityCode().equals("1134")) {
                                msgAlertD();
                            } else {
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("SecurityCode", pref.getSecurityCode());
                                    menu(jsonObject);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        //pd.dismiss();
                        Log.e(TAG, "USER_DEVICE_DETAILS_error: " + anError);
                    }
                });
    }


    private void menu(JSONObject jsonObject) {
        Log.e(TAG, "menu_object: " + jsonObject);
        AndroidNetworking.post(Api.sMenuapi)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        itemList.clear();
                        JSONObject job1 = response;
                        Log.e(TAG, "MENU: " + job1);

                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                            JSONArray responseData = job1.optJSONArray("Response_Data");
                            for (int i = 0; i < responseData.length(); i++) {
                                JSONObject obj = responseData.optJSONObject(i);
                                String MenuItemName = obj.optString("MenuItemName");
                                int MenuItemId = obj.optInt("MenuItemId");
                                if (MenuItemName.equalsIgnoreCase("Dailylog")) {
                                    menuName = "Dailylog Attendance";
                                } else {
                                    menuName = MenuItemName;
                                }
                                MenuItemModel obj2 = new MenuItemModel(menuName, MenuItemId);
                                itemList.add(obj2);
                            }
                            if (pref.getSecurityCode().equals("1186")) {
                                itemList.add(new MenuItemModel("Form-16", 105));
                                itemList.add(new MenuItemModel("Increment Letter", 107));
                            }
                            llLoader.setVisibility(View.GONE);
                            llMain.setVisibility(View.VISIBLE);
                            llNoConnection.setVisibility(View.GONE);
                            setAdapter();
                            // boolean _status = job1.getBoolean("status");
                            // do anything with response
                        } else {
                            llLoader.setVisibility(View.VISIBLE);
                            llMain.setVisibility(View.GONE);
                            llNoConnection.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.e(TAG, "MENU_onError: " + error);
                        if (error.getErrorCode() == 401) {
                            JSONObject obj = new JSONObject();
                            try {
                                obj.put("MasterID", encrypt(pref.getMasterId(), SECRET_KEY));
                                obj.put("Password", encrypt(pref.getPassword(), SECRET_KEY));
                                obj.put("IMEI", "0");
                                obj.put("DeviceID", "0");
                                obj.put("DeviceType", "A");
                                obj.put("SecurityCode", pref.getSecurityCode());
                                login(obj);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    private void login(JSONObject jsonObject) {
        Log.e(TAG, "login: " + jsonObject.toString());
        final ProgressDialog pd = new ProgressDialog(UserDashBoardActivity.this);
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

                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                            JSONArray responseData = job1.optJSONArray("Response_Data");
                            for (int i = 0; i < responseData.length(); i++) {
                                JSONObject obj = responseData.optJSONObject(i);
                                String Genius_Access_Token = obj.optString("Genius_Access_Token");
                                pref.saveAccessToken(Genius_Access_Token);
                                String CompanyName=obj.optString("CompanyName");
                                pref.saveCompanyName(CompanyName);

                                // boolean _status = job1.getBoolean("status");
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("SecurityCode", pref.getSecurityCode());
                                    menu(jsonObject);
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


    public void getFormSixten() {

        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("EmployeeID",pref.getEmpId());
            jsonObject.put("SecurityCode",pref.getSecurityCode());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "login: " + jsonObject.toString());
        final ProgressDialog pd = new ProgressDialog(UserDashBoardActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(Api.sGetEnForm16api)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
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

                            String Response_Data = job1.optString("Response_Data");
                            byte[] rawJsonBytes = Base64.decode(Response_Data, Base64.DEFAULT);
                            String jsonText = new String(rawJsonBytes, StandardCharsets.UTF_8);
                            try {
                                JSONObject root = new JSONObject(jsonText);
                                String PartA = root.getString("PartA");
                                String PartB = root.getString("PartB");

                                byte[] rawpartA = Base64.decode(PartA, Base64.DEFAULT);
                                partAURL = new String(rawpartA, StandardCharsets.UTF_8);


                                byte[] rawpartB = Base64.decode(PartB, Base64.DEFAULT);
                                partBURL = new String(rawpartB, StandardCharsets.UTF_8);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            showformsixteenDialog();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                    }
                });
    }

    public void getIncerementLetter() {

        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("EmployeeID",pref.getEmpId());
            jsonObject.put("SecurityCode",pref.getSecurityCode());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "login: " + jsonObject.toString());
        final ProgressDialog pd = new ProgressDialog(UserDashBoardActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(Api.sIncrementLetterapi)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
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

                            String Response_Data = job1.optString("Response_Data");
                            byte[] rawJsonBytes = Base64.decode(Response_Data, Base64.DEFAULT);
                            String jsonText = new String(rawJsonBytes, StandardCharsets.UTF_8);
                            try {
                                JSONObject root = new JSONObject(jsonText);
                                String PartA = root.getString("IncUrl");
                                String PartB = root.getString("PromUrl");

                                byte[] rawpartA = Base64.decode(PartA, Base64.DEFAULT);
                                incrementLetter = new String(rawpartA, StandardCharsets.UTF_8);


                                byte[] rawpartB = Base64.decode(PartB, Base64.DEFAULT);
                                promotionLetter = new String(rawpartB, StandardCharsets.UTF_8);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            showIncementLetterDialog();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                    }
                });
    }


    private void setAdapter() {
        attendanceAdapter = new MenuItemAdapter(itemList, UserDashBoardActivity.this);
        rvItem.setAdapter(attendanceAdapter);
    }

    private void onClick() {
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //getMenuList();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("SecurityCode", pref.getSecurityCode());
                    menu(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                swipeToRefresh.setRefreshing(false);
            }
        });

        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.saveLoginFlag("0");
                Intent intent = new Intent(UserDashBoardActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    public void showformsixteenDialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(UserDashBoardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_form_sixteen, null);
        dialogBuilder.setView(dialogView);
        ImageView imgCancel = (ImageView) dialogView.findViewById(R.id.imgCancel);

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert2.dismiss();
            }
        });

        LinearLayout llPartA = (LinearLayout) dialogView.findViewById(R.id.llPartA);
        LinearLayout llPartB = (LinearLayout) dialogView.findViewById(R.id.llPartB);
        llPartA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openForsixteenPopup("Part-A", partAURL);
            }
        });

        llPartB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openForsixteenPopup("Part-B", partBURL);
            }
        });


        alert2 = dialogBuilder.create();
        alert2.setCancelable(false);
        Window window = alert2.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alert2.show();
    }


    public void showIncementLetterDialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(UserDashBoardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_incement_letter, null);
        dialogBuilder.setView(dialogView);
        ImageView imgCancel = (ImageView) dialogView.findViewById(R.id.imgCancel);

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert2.dismiss();
            }
        });

        LinearLayout llPartA = (LinearLayout) dialogView.findViewById(R.id.llPartA);
        LinearLayout llPartB = (LinearLayout) dialogView.findViewById(R.id.llPartB);
        llPartA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openForsixteenPopup("Increment Letter", incrementLetter);
            }
        });

        llPartB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openForsixteenPopup("Promotion Letter", promotionLetter);
            }
        });


        alert2 = dialogBuilder.create();
        alert2.setCancelable(false);
        Window window = alert2.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alert2.show();
    }

    public void shoeDialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(UserDashBoardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.language_dialoge, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llEnglish = (LinearLayout) dialogView.findViewById(R.id.llEnglish);
        LinearLayout llHindi = (LinearLayout) dialogView.findViewById(R.id.llHindi);
        LinearLayout llTamil = (LinearLayout) dialogView.findViewById(R.id.llTamil);
        ImageView imgCancelDialog = (ImageView) dialogView.findViewById(R.id.imgCancelDialog);
        final ImageView imgGreyBridge = (ImageView) dialogView.findViewById(R.id.imgGreyBridge);
        final ImageView imgBlueBridge = (ImageView) dialogView.findViewById(R.id.imgBlueBridge);
        final ImageView imgGreyTajMahal = (ImageView) dialogView.findViewById(R.id.imgGreyTajMahal);
        final ImageView imgBlueTajMahal = (ImageView) dialogView.findViewById(R.id.imgBlueTajMahal);
        final ImageView imgGreyTamil = (ImageView) dialogView.findViewById(R.id.imgGreyTamil);
        final ImageView imgBlueTamil = (ImageView) dialogView.findViewById(R.id.imgBlueTamil);
        final TextView tvEnglish = (TextView) dialogView.findViewById(R.id.tvEnglish);
        final TextView tvHindi = (TextView) dialogView.findViewById(R.id.tvHindi);
        final TextView tvTamil = (TextView) dialogView.findViewById(R.id.tvTamil);
        imgCancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert2.dismiss();
            }
        });
        llHindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.saveLanguage("hi");
                alert2.dismiss();
                // getMenuList();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("SecurityCode", pref.getSecurityCode());
                    menu(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Calendar c = Calendar.getInstance();
                int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

                if (timeOfDay >= 0 && timeOfDay < 12) {
                    tvGreeting.setText("शुभ प्रभात");
                } else if (timeOfDay >= 12 && timeOfDay < 16) {
                    tvGreeting.setText("नमस्कार");
                } else if (timeOfDay >= 16 && timeOfDay < 21) {
                    tvGreeting.setText("सुसंध्या");
                } else if (timeOfDay >= 21 && timeOfDay < 24) {
                    tvGreeting.setText("सुसंध्या");
                }
                imgGreyBridge.setVisibility(View.VISIBLE);
                imgBlueBridge.setVisibility(View.GONE);
                imgBlueTajMahal.setVisibility(View.VISIBLE);
                imgGreyTajMahal.setVisibility(View.GONE);
                imgGreyTamil.setVisibility(View.VISIBLE);
                imgBlueTamil.setVisibility(View.GONE);
                //72128E
                tvEnglish.setTextColor(Color.parseColor("#B1ACAC"));
                tvHindi.setTextColor(Color.parseColor("#72128E"));
                tvTamil.setTextColor(Color.parseColor("#B1ACAC"));
            }
        });

        llEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.saveLanguage("en");
                alert2.dismiss();
                //getMenuList();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("SecurityCode", pref.getSecurityCode());
                    menu(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Calendar c = Calendar.getInstance();
                int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

                if (timeOfDay >= 0 && timeOfDay < 12) {
                    tvGreeting.setText("Good Morning");
                } else if (timeOfDay >= 12 && timeOfDay < 16) {
                    tvGreeting.setText("Good Afternoon");
                } else if (timeOfDay >= 16 && timeOfDay < 21) {
                    tvGreeting.setText("Good Evening");
                } else if (timeOfDay >= 21 && timeOfDay < 24) {
                    tvGreeting.setText("Good Evening");
                }

                imgGreyBridge.setVisibility(View.GONE);
                imgBlueBridge.setVisibility(View.VISIBLE);
                imgBlueTajMahal.setVisibility(View.GONE);
                imgGreyTajMahal.setVisibility(View.VISIBLE);
                imgGreyTamil.setVisibility(View.VISIBLE);
                imgBlueTamil.setVisibility(View.GONE);
                tvEnglish.setTextColor(Color.parseColor("#72128E"));
                tvHindi.setTextColor(Color.parseColor("#B1ACAC"));
                tvTamil.setTextColor(Color.parseColor("#B1ACAC"));
            }
        });

        llTamil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.saveLanguage("ta");
                alert2.dismiss();
                // getMenuList();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("SecurityCode", pref.getSecurityCode());
                    menu(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Calendar c = Calendar.getInstance();
                int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

                if (timeOfDay >= 0 && timeOfDay < 12) {
                    tvGreeting.setText("Good Morning");
                } else if (timeOfDay >= 12 && timeOfDay < 16) {
                    tvGreeting.setText("Good Afternoon");
                } else if (timeOfDay >= 16 && timeOfDay < 21) {
                    tvGreeting.setText("Good Evening");
                } else if (timeOfDay >= 21 && timeOfDay < 24) {
                    tvGreeting.setText("Good Evening");
                }

                imgGreyBridge.setVisibility(View.VISIBLE);
                imgBlueBridge.setVisibility(View.GONE);
                imgBlueTajMahal.setVisibility(View.GONE);
                imgGreyTajMahal.setVisibility(View.VISIBLE);
                imgGreyTamil.setVisibility(View.GONE);
                imgBlueTamil.setVisibility(View.VISIBLE);
                tvEnglish.setTextColor(Color.parseColor("#B1ACAC"));
                tvHindi.setTextColor(Color.parseColor("#B1ACAC"));
                tvTamil.setTextColor(Color.parseColor("#72128E"));
            }
        });
        if (pref.getLanguage().equals("hi")) {
            imgGreyBridge.setVisibility(View.VISIBLE);
            imgBlueBridge.setVisibility(View.GONE);
            imgBlueTajMahal.setVisibility(View.VISIBLE);
            imgGreyTajMahal.setVisibility(View.GONE);
            tvEnglish.setTextColor(Color.parseColor("#B1ACAC"));
            tvHindi.setTextColor(Color.parseColor("#72128E"));
            tvTamil.setTextColor(Color.parseColor("#B1ACAC"));

        } else if (pref.getLanguage().equals("ta")) {
            imgGreyBridge.setVisibility(View.VISIBLE);
            imgBlueBridge.setVisibility(View.GONE);
            imgBlueTajMahal.setVisibility(View.GONE);
            imgGreyTajMahal.setVisibility(View.VISIBLE);
            imgGreyTamil.setVisibility(View.GONE);
            imgBlueTamil.setVisibility(View.VISIBLE);
            tvEnglish.setTextColor(Color.parseColor("#B1ACAC"));
            tvHindi.setTextColor(Color.parseColor("#B1ACAC"));
            tvTamil.setTextColor(Color.parseColor("#72128E"));
        } else {
            imgGreyBridge.setVisibility(View.GONE);
            imgBlueBridge.setVisibility(View.VISIBLE);
            imgBlueTajMahal.setVisibility(View.GONE);
            imgGreyTajMahal.setVisibility(View.VISIBLE);
            imgGreyTamil.setVisibility(View.VISIBLE);
            imgBlueTamil.setVisibility(View.GONE);
            tvEnglish.setTextColor(Color.parseColor("#72128E"));
            tvHindi.setTextColor(Color.parseColor("#B1ACAC"));
            tvHindi.setTextColor(Color.parseColor("#B1ACAC"));
        }
        alert2 = dialogBuilder.create();
        alert2.setCancelable(false);
        Window window = alert2.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alert2.show();
    }

    private void msgAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(UserDashBoardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_message, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);
        tvInvalidDate.setText(pref.getMsg());
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

    private void msgAlertD() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(UserDashBoardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.msg_dialog, null);
        dialogBuilder.setView(dialogView);

        Button btn_next = (Button) dialogView.findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert1.dismiss();
                //  getMenuList();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("SecurityCode", pref.getSecurityCode());
                    menu(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        alert1 = dialogBuilder.create();
        alert1.setCancelable(true);
        Window window = alert1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alert1.show();
    }


    public void activeUsers() {

    }


    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(airplaneModeChangeReceiver, filter);
        registerReceiver(dailyLogReciever, filter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(airplaneModeChangeReceiver);
        unregisterReceiver(dailyLogReciever);
    }

    public void RateApp(final Context mContext) {
        try {
            final ReviewManager manager = ReviewManagerFactory.create(mContext);
            manager.requestReviewFlow().addOnCompleteListener(new OnCompleteListener<ReviewInfo>() {
                @Override
                public void onComplete(@NonNull Task<ReviewInfo> task) {
                    if (task.isSuccessful()) {
                        ReviewInfo reviewInfo = task.getResult();
                        manager.launchReviewFlow((Activity) mContext, reviewInfo).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                //Toast.makeText(mContext, "Rating Failed", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // Toast.makeText(mContext, "Review Completed, Thank You!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    // Toast.makeText(mContext, "In-App Request Failed", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void openForsixteenPopup(String docname, String fileURL) {


        TextView textView = dialog.findViewById(R.id.textView);
        textView.setText(docname);
        final ImageView imgCancel = dialog.findViewById(R.id.imgCancel);


        llPdfLoading = dialog.findViewById(R.id.llPdfLoading);
        tvPdfPageNo = dialog.findViewById(R.id.tvPdfPageNo);
        pdfView = dialog.findViewById(R.id.pdfView);

        llPdfLoading.setVisibility(View.VISIBLE);
        pdfView.setVisibility(View.VISIBLE);
        new RetrievePdfFromUrl().execute(fileURL);


        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.cancel();


            }
        });
        ImageView imgDownload = dialog.findViewById(R.id.imgDownload);
        imgDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Build.VERSION.SDK_INT < 30 &&
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
                    } else {
                        createAndShowProgressDialog();   // ⬅️ new
                        startDownload(fileURL,docname+"_Form16.pdf");
                    }
                }
            }
        });


        dialog.setCancelable(false);
        dialog.show();
    }


    class RetrievePdfFromUrl extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            // we are using inputstream
            // for getting out PDF.
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                // below is the step where we are
                // creating our connection.
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    // response is success.
                    // we are getting input stream from url
                    // and storing it in our variable.
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                // this is the method
                // to handle errors.
                e.printStackTrace();

                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            // after the execution of our async
            // task we are loading our pdf in our pdf view.
            //openTrainingPopup(doc_name,doc_type,url,inputStream);
            if (inputStream == null) {
                Toast.makeText(UserDashBoardActivity.this,
                        "No doument found",
                        Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                return;                 // stop here
            }

            pdfView.fromStream(inputStream)
                    .swipeHorizontal(true)
                    .onPageChange(new OnPageChangeListener() {
                        @Override
                        public void onPageChanged(int page, int pageCount) {
                            Log.e(TAG, "onPageChanged: Current Page: " + page + " Total number of page: " + pageCount);
                            tvPdfPageNo.setText(page + 1 + " / " + pageCount);
                        }
                    })
                    .onRender(new OnRenderListener() {
                        @Override
                        public void onInitiallyRendered(int nbPages) {
                            Log.e(TAG, "onInitiallyRendered: nbPages: " + nbPages);
                            llPdfLoading.setVisibility(View.GONE);
                            //DocumentLoadingProgress.showDialog(ViewPdfActivity.this,false);
                            //binding.pageNumber.setVisibility(View.VISIBLE);
                        }
                    })
                    .onTap(new OnTapListener() {
                        @Override
                        public boolean onTap(MotionEvent e) {
                            Log.e(TAG, "onTap: called.");
                            if (tvPdfPageNo.getVisibility() == View.VISIBLE) {
                                tvPdfPageNo.setVisibility(View.GONE);
                            } else {
                                tvPdfPageNo.setVisibility(View.VISIBLE);
                            }
                            return false;
                        }
                    })
                    .spacing(15)
                    .pageSnap(true)
                    .autoSpacing(true)
                    .pageFling(true)
                    .load();
        }
    }


    private void createAndShowProgressDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_progress, null, false);
        progressBarDialog = view.findViewById(R.id.progressBarDialog);
        tvPercentDialog = view.findViewById(R.id.tvPercentDialog);

        progressDialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setView(view)
                .create();
        progressDialog.show();
    }

    private void startDownload(String fileuRL,String fileName) {
        Uri uri = Uri.parse(fileuRL);
        DownloadManager.Request req = new DownloadManager.Request(uri)
                .setTitle(fileName)
                .setDescription("Downloading…")
                .setNotificationVisibility(
                        DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS, fileName);

        downloadId = dm.enqueue(req);
        tvPercentDialog.setText("0 %");
        progressBarDialog.setProgress(0);
        trackProgress();
    }

    /**
     * Poll DownloadManager every 500 ms and update dialog UI
     */
    private void trackProgress() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DownloadManager.Query q = new DownloadManager.Query().setFilterById(downloadId);
                try (Cursor c = dm.query(q)) {
                    if (c != null && c.moveToFirst()) {
                        int bytes = c.getInt(c.getColumnIndexOrThrow(
                                DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                        int total = c.getInt(c.getColumnIndexOrThrow(
                                DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                        int status = c.getInt(c.getColumnIndexOrThrow(
                                DownloadManager.COLUMN_STATUS));

                        if (total > 0) {
                            int pct = (int) ((bytes * 100L) / total);
                            progressBarDialog.setProgress(pct);
                            tvPercentDialog.setText(pct + " %");
                        }

                        if (status == DownloadManager.STATUS_SUCCESSFUL ||
                                status == DownloadManager.STATUS_FAILED) {
                            if (progressDialog != null && progressDialog.isShowing())
                                progressDialog.dismiss();
                            return;                 // stop polling
                        }
                    }
                }
                handler.postDelayed(this, 500);
            }
        }, 0);
    }

    @Override
    public void onRequestPermissionsResult(int reqCode, @NonNull String[] perms,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(reqCode, perms, grantResults);
        if (reqCode == 101 && grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        } else {
            Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show();
        }
    }
}
