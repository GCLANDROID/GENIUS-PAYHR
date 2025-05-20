package com.genius.payhrms.activity.attendance;


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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.androidnetworking.interfaces.UploadProgressListener;
import com.genius.payhrms.R;
import com.genius.payhrms.activity.activity.UserDashBoardActivity;
import com.genius.payhrms.activity.adapter.AttendanceRegulizationAdapter;
import com.genius.payhrms.activity.adapter.BackLogAdapter;
import com.genius.payhrms.activity.model.BackLogModel;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.Pref;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BacklogActivity extends AppCompatActivity {
    private static final String TAG = "BacklogActivity";
    TextView tvToolBar;
    RecyclerView rvItem;
    public static ArrayList<BackLogModel> newBacklogArray = new ArrayList<BackLogModel>();
    LinearLayout llHeading, llLoader, llMain;
    Pref pref;
    ArrayList<BackLogModel> blockLogList = new ArrayList<>();
    ArrayList<String> backLogItem = new ArrayList<>();
    BackLogAdapter backLogAdapter;
    ImageView imgHome,imgBack;
    ArrayList<String>backLogData=new ArrayList<>();
    LinearLayout btnSubmit;
    String backlogDetails;
    LinearLayout llNodata;
    EditText etFocus;
    AlertDialog alerDialog1;
    TextView tvRemark,tvDate,tvInTime,tvOutTime;
    String securityCode;
    FloatingActionButton fbUp,fbDown;
    ScrollView scMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backlog);
        initView();
        onClick();
    }

    private void initView() {
        pref = new Pref(getApplicationContext());
        fbUp=(FloatingActionButton)findViewById(R.id.fbUp);
        fbDown=(FloatingActionButton)findViewById(R.id.fbDown);
        securityCode=pref.getSecurityCode();
        tvToolBar = findViewById(R.id.tvToolBar);
        rvItem = findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(BacklogActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);
        llHeading = findViewById(R.id.llHeading);
        llLoader = findViewById(R.id.llLoader);
        llMain = findViewById(R.id.llMain);
        llNodata = findViewById(R.id.llNodata);
        etFocus=(EditText)findViewById(R.id.etFocus);

        JSONObject object=new JSONObject();
        try {
            object.put("AEMEmployeeID",pref.getEmpId());
            object.put("CompanyID",pref.getEmpClintId());
            object.put("SecurityCode",pref.getSecurityCode());
            attendanceregularization(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        imgHome=(ImageView)findViewById(R.id.imgHome);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        btnSubmit=(LinearLayout) findViewById(R.id.btnSubmit);

        tvRemark=(TextView)findViewById(R.id.tvRemark);
        tvDate=(TextView)findViewById(R.id.tvDate);
        tvInTime=(TextView)findViewById(R.id.tvInTime);
        tvOutTime=(TextView)findViewById(R.id.tvOutTime);
        if (pref.getLanguage().equals("hi")){
            tvRemark.setText("टिप्पणी");
            tvDate.setText("दिनांक");
            tvInTime.setText("अंदर आने का समय");
            tvOutTime.setText("बाहर जाने का समय");
            tvToolBar.setText("बैकलॉग उपस्थिति");

        }else {
            tvRemark.setText("Remarks");
            tvDate.setText("Date");
            tvInTime.setText("In Time");
            tvOutTime.setText("Out Time");
            tvToolBar.setText("Backlog Attendance");
        }
        scMain=(ScrollView)findViewById(R.id.scMain);

        fbUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rvItem.smoothScrollToPosition(0);
            }
        });
        fbDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rvItem.smoothScrollToPosition(blockLogList.size());

            }
        });

    }

    private void onClick(){
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etFocus.requestFocus();
            }
        });

        etFocus.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    if (backLogItem.size()>0) {
                        JSONObject jsonObject=new JSONObject();
                        try {
                            jsonObject.put("CompanyID",pref.getEmpClintId());
                            jsonObject.put("AttendanceData",backlogDetails);
                            jsonObject.put("EmployeeID",pref.getEmpId());
                            jsonObject.put("SecurityCode",pref.getSecurityCode());
                            //Log.e(TAG, "onFocusChange: "+jsonObject.toString());
                            regulizationSave(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(),"Please enter remarks",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BacklogActivity.this, UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    public void updateItemStatus(int position) {
        if (!blockLogList.get(position).getRemarks().equals("")){
            backLogItem.add(blockLogList.get(position).getDate() + "_" + blockLogList.get(position).getInTime() + "_" + blockLogList.get(position).getOutTime() + "_" + blockLogList.get(position).getRemarks());
        }
        String itemcomp = backLogItem.toString();
        backlogDetails = itemcomp.replace("[","").replace("]","");
        Log.d("aripitem", backlogDetails);
    }


    private void successAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(BacklogActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);
        if (pref.getLanguage().equals("hi")) {
            tvInvalidDate.setText("बैकलॉग उपस्थिति सफलतापूर्वक सहेजी गई");
        } else {
            tvInvalidDate.setText("Backlog attendance has been saved successfully");
        }


        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                backLogItem.clear();
                backlogDetails="";
                JSONObject object=new JSONObject();
                try {
                    object.put("AEMEmployeeID",pref.getEmpId());
                    object.put("CompanyID",pref.getEmpClintId());
                    object.put("SecurityCode",pref.getSecurityCode());
                    attendanceregularization(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });

        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }


    private void attendanceregularization(JSONObject jsonObject) {
        Log.e(TAG, "attendanceregularization: called");
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);
        blockLogList.clear();
        AndroidNetworking.post(Api.sAttendanceRegularizationapi)
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
                        blockLogList.clear();

                        int Response_Code = job1.optInt("Response_Code");
                        String Response_Message=job1.optString("Response_Message");
                        if (Response_Code == 101) {
                            // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                            String responseData = job1.optString("Response_Data");
                            try {
                                JSONArray jsonArray=new JSONArray(responseData);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.optJSONObject(i);
                                    String AttDate = obj.optString("AttDate");
                                    String InTime = obj.optString("InTime");
                                    String OutTime = obj.optString("OutTime");
                                    BackLogModel blockModule = new BackLogModel(AttDate, InTime, OutTime);
                                    blockLogList.add(blockModule);
                                }

                                if (blockLogList.size()>5){
                                    fbUp.setVisibility(View.VISIBLE);
                                    fbDown.setVisibility(View.VISIBLE);
                                }else {
                                    fbUp.setVisibility(View.GONE);
                                    fbDown.setVisibility(View.GONE);
                                }
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNodata.setVisibility(View.GONE);
                                backLogAdapter = new BackLogAdapter(blockLogList, BacklogActivity.this);
                                rvItem.setAdapter(backLogAdapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            // boolean _status = job1.getBoolean("status");
                            // do anything with response
                        }else {

                            llLoader.setVisibility(View.GONE);
                            llMain.setVisibility(View.GONE);
                            llNodata.setVisibility(View.VISIBLE);

                        }
                    }

                    @Override
                    public void onError(ANError error) {
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


    private void login(JSONObject jsonObject) {

        final ProgressDialog pd = new ProgressDialog(BacklogActivity.this);
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


                                JSONObject object=new JSONObject();
                                try {

                                    object.put("AEMEmployeeID",pref.getEmpId());
                                    object.put("CompanyID",pref.getEmpClintId());
                                    object.put("SecurityCode",pref.getSecurityCode());
                                    attendanceregularization(object);
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

    public void regulizationSave(JSONObject jsonObject) {
        etFocus.clearFocus();
        final ProgressDialog pg=new ProgressDialog(BacklogActivity.this);
        pg.setMessage("Loading..");
        pg.setCancelable(false);
        AndroidNetworking.post( Api.sAttendanceRegularizationsaveapi)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        pg.show();
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "regulizationSave: "+response.toString());
                        pg.dismiss();
                        JSONObject job = response;
                        String responseText=job.optString("Response_Message");
                        int Response_Code = job.optInt("Response_Code");
                        if (Response_Code == 101) {
                            successAlert();
                        } else {
                            Toast.makeText(BacklogActivity.this,responseText,Toast.LENGTH_LONG).show();
                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e("error",error.toString());
                        pg.dismiss();
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();

                    }
                });
    }

}
