package com.genius.hrms.activity.attendance;


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
import com.genius.hrms.R;
import com.genius.hrms.activity.activity.UserDashBoardActivity;
import com.genius.hrms.activity.adapter.BackLogAdapter;
import com.genius.hrms.activity.model.BackLogModel;
import com.genius.hrms.activity.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BacklogActivity extends AppCompatActivity {
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backlog);
        initView();
        onClick();
    }

    private void initView() {
        pref = new Pref(getApplicationContext());
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
        getBackLogData();

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
                        backlogSave();
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

    private void getBackLogData() {
        String surl = pref.getIpAddress() + "GHRMSApi/api/Attendance/AttendanceBakLog?CompanyID=" + pref.getEmpClintId() + "&EmployeeID=" + pref.getEmpId() + "&SecurityCode=" + securityCode;
        Log.d("backlogURL",surl);
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);
        blockLogList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("blockActivityData", response);

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
                                    String AttDate = obj.optString("AttDate");
                                    String InTime = obj.optString("InTime");
                                    String OutTime = obj.optString("OutTime");


                                    BackLogModel blockModule = new BackLogModel(AttDate, InTime, OutTime);
                                    blockLogList.add(blockModule);


                                }
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNodata.setVisibility(View.GONE);
                                backLogAdapter = new BackLogAdapter(blockLogList, BacklogActivity.this);
                                rvItem.setAdapter(backLogAdapter);
                            } else {

                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNodata.setVisibility(View.VISIBLE);
                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            //  Toast.makeText(AboutUsActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);
                llNodata.setVisibility(View.GONE);
                Toast.makeText(BacklogActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(BacklogActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    public void updateItemStatus(int position) {
        if (!blockLogList.get(position).getRemarks().equals("")){
            backLogItem.add(blockLogList.get(position).getDate() + "_" + blockLogList.get(position).getInTime() + "_" + blockLogList.get(position).getOutTime() + "_" + blockLogList.get(position).getRemarks());
        }
        String itemcomp = backLogItem.toString();
         backlogDetails=itemcomp.replace("[","").replace("]","");

        Log.d("aripitem", backlogDetails);


    }

    public void backlogSave() {
        etFocus.clearFocus();
      /*  backLogData.clear();
        for (int i=0;i<newBacklogArray.size();i++){
            if (newBacklogArray.get(i).isSelected()){

                backLogData.add(newBacklogArray.get(i).getDate()+"_"+newBacklogArray.get(i).getInTime()+"_"+newBacklogArray.get(i).getOutTime()+"_"+newBacklogArray.get(i).getRemarks());

            }

        }
        Log.d("ranhjana",backLogData.toString());*/
        final ProgressDialog pg=new ProgressDialog(BacklogActivity.this);
        pg.setMessage("Loading..");
        pg.setCancelable(false);
        pg.show();
        AndroidNetworking.upload( pref.getIpAddress() + "GHRMSApi/api/Attendance/AttendanceBakLogSave")
                .addMultipartParameter("CompanyID", pref.getEmpClintId())
                .addMultipartParameter("StrAttData", backlogDetails)
                .addMultipartParameter("EmployeeID", pref.getEmpId())
                .addMultipartParameter("SecurityCode", securityCode)
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

                        pg.dismiss();
                        JSONObject job = response;
                        String responseText=job.optString("responseText");
                        boolean responseStatus = job.optBoolean("responseStatus");
                        if (responseStatus) {

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


    private void successAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(BacklogActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);
        if (pref.getLanguage().equals("hi")) {
            tvInvalidDate.setText("बैकलॉग उपस्थिति सफलतापूर्वक सहेजी गई");
        } else {
            tvInvalidDate.setText("Backlog Attendance saved successfully");
        }


        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();

                backLogItem.clear();
                backlogDetails="";
               getBackLogData();

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
