package com.genius.payhrms.activity.dailylog;

import static com.genius.payhrms.activity.dailylog.DailyLogCalenderDashboardActivity.isAppMinimizeDailyLog;
import static com.genius.payhrms.activity.utility.Util.SECRET_KEY;
import static com.genius.payhrms.activity.utility.Util.encrypt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.genius.payhrms.activity.adapter.VisitingLocationAdapter;
import com.genius.payhrms.activity.dailylog.Dayco.DailyLogAttendaneDayco;
import com.genius.payhrms.activity.dailylog.Dayco.DailyLogAttendaneMathhew;
import com.genius.payhrms.activity.dailylog.archisman.DailyLogAttendaneArchisman;
import com.genius.payhrms.activity.geofence.GeoFenceDailyLogManageActivity;
import com.genius.payhrms.activity.model.VisitingLocationModel;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.Pref;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class VisitLocationActivity extends AppCompatActivity {
    private static final String TAG = "VisitLocationActivity";
    RecyclerView rvItem;
    ArrayList<VisitingLocationModel> itemList = new ArrayList<>();
    Button btnAdd;
    ImageView imgAdd;
    LinearLayout llLoader, llMain, llNoData;
    ProgressDialog pd, pd1;
    Pref pref;
    String formattedDate;
    TextView tvDate;
    FloatingActionButton fbAdd;
    ImageView imgBack, imgHome;
    TextView tvToolBar;
    String surl;
    String attCode;
    String frstPunch;
    double SLongitude, SLatitude, s;
    String punchFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_location);
        initView();

        onClick();
    }

    private void initView() {
        pref = new Pref(getApplicationContext());
        rvItem = (RecyclerView) findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(VisitLocationActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        llMain = (LinearLayout) findViewById(R.id.llMain);
        llLoader = (LinearLayout) findViewById(R.id.llLoader);
        llNoData = (LinearLayout) findViewById(R.id.llNoData);
        pd = new ProgressDialog(this);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c);
        Log.d("formattedDate", formattedDate);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvDate.setText(formattedDate);
        pd1 = new ProgressDialog(this);
        fbAdd = (FloatingActionButton) findViewById(R.id.fbAdd);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        tvToolBar = (TextView) findViewById(R.id.tvToolBar);
        if (pref.getLanguage().equals("hi")) {
            tvToolBar.setText("स्थान का दौरा किया");
            btnAdd.setText("अपनी गतिविधि शुरू करें");
        } else {
            tvToolBar.setText("Visited place");
            btnAdd.setText("Start your activity");
        }
    }



    private void getItem2(JSONObject object) {
        Log.e(TAG, "getItem2: called");
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
       /* if (pref.getSecurityCode().equals("123")) {
            surl = "http://111.93.182.174/GeniusiOSApi/api//get_OfflineDailyLogActivity?AEMEmployeeID=" + pref.getEmpId() + "&Year=0&Month=0&SecurityCode=" + pref.getSecurityCode() + "&AttendanceDate=" + formattedDate + "&Operation=1";
        } else {
            surl = pref.getIpAddress() + "GHRMSApi/api/get_OfflineDailyLogActivity?AEMEmployeeID=" + pref.getEmpId() + "&Year=0&Month=0&SecurityCode=" + pref.getSecurityCode() + "&AttendanceDate=" + formattedDate + "&Operation=1";

        }
        Log.d("inputactivity", surl);*/


        AndroidNetworking.post(Api.sGetOfflineDailyLogActivity)
                .addJSONObjectBody(object)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "VISIT_LOCATION_LIST: "+response.toString());
                        JSONObject job1 = response;
                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            itemList.clear();
                            String responseData = job1.optString("Response_Data");
                            JSONArray jsonArray = null;
                            try {
                                jsonArray = new JSONArray(responseData);
                                if (jsonArray.length() > 0) {
                                    for (int i = 0; i <jsonArray.length(); i++) {
                                        JSONObject obj = jsonArray.getJSONObject(i);

                                        String PunchInTime = obj.optString("PunchInTime");
                                        String AddressIN = obj.optString("AddressIN");
                                        String LongitudeIN=obj.optString("LongitudeIN");
                                        String LatitudeIN=obj.optString("LatitudeIN");

                                        VisitingLocationModel obj2 = new VisitingLocationModel(AddressIN,PunchInTime,LatitudeIN,LongitudeIN);
                                        itemList.add(obj2);
                                    }
                                    setAdapter();
                                    llLoader.setVisibility(View.GONE);
                                    llMain.setVisibility(View.VISIBLE);
                                    llNoData.setVisibility(View.GONE);
                                } else {
                                    llLoader.setVisibility(View.GONE);
                                    llMain.setVisibility(View.GONE);
                                    llNoData.setVisibility(View.VISIBLE);
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            llLoader.setVisibility(View.GONE);
                            llMain.setVisibility(View.GONE);
                            llNoData.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "VISIT_LOCATION_ERROR: "+anError);
                        if (anError.getErrorCode()==401){
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
                        } else {
                            llLoader.setVisibility(View.GONE);
                            llMain.setVisibility(View.GONE);
                            llNoData.setVisibility(View.VISIBLE);
                        }
                    }
                });

    }

    private void login(JSONObject jsonObject) {
        final ProgressDialog pd = new ProgressDialog(VisitLocationActivity.this);
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
                                String Genius_Access_Token=obj.optString("Genius_Access_Token");
                                pref.saveAccessToken(Genius_Access_Token);
                                // boolean _status = job1.getBoolean("status");
                                // do anything with response
                                JSONObject object=new JSONObject();
                                try {
                                    object.put("AEMEmployeeID",pref.getEmpId());
                                    object.put("Year",0);
                                    object.put("Month",0);
                                    object.put("SecurityCode",pref.getSecurityCode());
                                    object.put("AttendanceDate",formattedDate);
                                    object.put("Operation",1);
                                    getItem2(object);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                    }
                });
    }

    private void setAdapter() {
        VisitingLocationAdapter vAdapter = new VisitingLocationAdapter(itemList, getApplicationContext());
        rvItem.setAdapter(vAdapter);
    }

    private void onClick() {
        fbAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pref.getSecurityCode().equals("1153")) {
                   /* JSONObject object = new JSONObject();
                    try {
                        object.put("EmployeeID", pref.getEmpId());
                        object.put("AttendanceDate", formattedDate);
                        object.put("SecurityCode", pref.getSecurityCode());
                        getAttendanceInformationForSmart2(object);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/
                    //getAttendanceInformationForSmart();
                    isAppMinimizeDailyLog = true;
                    Intent intent = new Intent(VisitLocationActivity.this, OfflineDailyLogManageActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if (pref.getSecurityCode().equals("1157")) {
                    isAppMinimizeDailyLog = true;
                    Intent intent = new Intent(VisitLocationActivity.this, DailyLogMarkActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if (pref.getSecurityCode().equals("1168")) {
                    isAppMinimizeDailyLog = true;
                    Intent intent = new Intent(VisitLocationActivity.this, DailyLogAttendaneArchisman.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else if (pref.getSecurityCode().equals("1000")) {
                    isAppMinimizeDailyLog = true;
                    //TODO: Mathhew
                    Intent intent = new Intent(VisitLocationActivity.this, DailyLogAttendaneMathhew.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if (pref.getSecurityCode().equals("1167")) {
                    isAppMinimizeDailyLog = true;
                    Intent intent = new Intent(VisitLocationActivity.this, DailyLogAttendaneMathhew.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if (pref.getSecurityCode().equals("1169")) {
                    isAppMinimizeDailyLog = true;
                    Intent intent = new Intent(VisitLocationActivity.this, DailyLogAttendaneDayco.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if (pref.getSecurityCode().equals("1000")) {
                    isAppMinimizeDailyLog = true;
                    Intent intent = new Intent(VisitLocationActivity.this, DailyLogAttendaneArchisman.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if (pref.getSecurityCode().equals("5000")) {
                    isAppMinimizeDailyLog = true;
                    Intent intent = new Intent(VisitLocationActivity.this, DailyLogAttendaneArchisman.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    isAppMinimizeDailyLog = true;
                    Intent intent = new Intent(VisitLocationActivity.this, DailyLogMarkActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pref.getSecurityCode().equals("1153")) {
                    //getAttendanceInformationForSmart();
                    JSONObject object=new JSONObject();
                    try {
                        object.put("EmployeeID",pref.getEmpId());
                        object.put("AttendanceDate",formattedDate);
                        object.put("SecurityCode",pref.getSecurityCode());
                        getAttendanceInformationForSmart2(object);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                   /* Intent intent = new Intent(VisitLocationActivity.this, OfflineDailyLogManageActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);*/
                } else if (pref.getSecurityCode().equals("1157")) {
                    isAppMinimizeDailyLog = false;
                    Intent intent = new Intent(VisitLocationActivity.this, DailyLogMarkActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if (pref.getSecurityCode().equals("1168")) {
                    isAppMinimizeDailyLog = false;
                    Intent intent = new Intent(VisitLocationActivity.this, DailyLogAttendaneArchisman.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else if (pref.getSecurityCode().equals("1167")) {
                    isAppMinimizeDailyLog = false;
                    Intent intent = new Intent(VisitLocationActivity.this, DailyLogAttendaneMathhew.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if (pref.getSecurityCode().equals("1169")) {
                    isAppMinimizeDailyLog = false;
                    Intent intent = new Intent(VisitLocationActivity.this, DailyLogAttendaneDayco.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if (pref.getSecurityCode().equals("1000")) {
                    isAppMinimizeDailyLog = false;
                    Intent intent = new Intent(VisitLocationActivity.this, DailyLogAttendaneArchisman.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else if (pref.getSecurityCode().equals("5000")) {
                    Log.e(TAG, "onClick: called =======");
                    isAppMinimizeDailyLog = false;
                    Intent intent = new Intent(VisitLocationActivity.this, DailyLogAttendaneArchisman.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    isAppMinimizeDailyLog = false;
                    Intent intent = new Intent(VisitLocationActivity.this, DailyLogMarkActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
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
                Intent intent = new Intent(VisitLocationActivity.this, UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        pd.dismiss();
        pd1.dismiss();
    }


    private void getAttendanceInformationForSmart2(JSONObject object) {
        Log.e(TAG, "getAttendanceInformationForSmart2: called");
        final ProgressDialog progressDialog = new ProgressDialog(VisitLocationActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        /*String surl = pref.getIpAddress() + "GHRMSApi/api/attendance/SingleAttendanceExistanceStatus?EmployeeID=" + pref.getEmpId() + "&AttendanceDate=" + formattedDate + "&SecurityCode=" + pref.getSecurityCode();
        Log.d("input", surl);*/

        //http://171.16.2.67/GHRMSApi_V2/api/"Attendance/SingleAttendanceExistanceStatus
        //

        AndroidNetworking.post(Api.sSingleAttendanceExistanceStatus)
                .addJSONObjectBody(object)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.e(TAG, "InformationForSmart: "+response.toString());
                        JSONObject job1 = response;
                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            attCode = "1";
                        } else {
                            attCode = "0";
                        }
                        Intent intent = new Intent(VisitLocationActivity.this, SmartJuleDailyLogActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("attCode", attCode);
                        intent.putExtra("frstPunch", frstPunch);
                        intent.putExtra("punchFrom", punchFrom);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Log.e(TAG, "InformationForSmart_onError: "+anError);
                        if (anError.getErrorCode()==401){
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



    @Override
    protected void onResume() {
        super.onResume();
        //getItem();
        JSONObject object=new JSONObject();
        try {
            object.put("AEMEmployeeID",pref.getEmpId());
            object.put("Year",0);
            object.put("Month",0);
            object.put("SecurityCode",pref.getSecurityCode());
            object.put("AttendanceDate",formattedDate);
            object.put("Operation",1);
            getItem2(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        isAppMinimizeDailyLog = false;
        super.onBackPressed();
    }
}
