package com.genius.payhrms.activity.attendance.tour;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.genius.payhrms.R;
import com.genius.payhrms.activity.attendance.AttendanceCalenderDashboardActivity;
import com.genius.payhrms.activity.leaveapplication.ApplicationFragment;
import com.genius.payhrms.activity.leaveapplication.LeaveApplicationActivity;
import com.genius.payhrms.activity.model.SpinnerModel;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.Pref;
import com.genius.payhrms.databinding.ActivityTourBinding;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TourActivity extends AppCompatActivity {
    ActivityTourBinding binding;
    Pref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tour);
        initView();
        loadApplicationFragment();
    }

    private void initView() {
        pref = new Pref(TourActivity.this);

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TourActivity.this, AttendanceCalenderDashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
        binding.llApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadApplicationFragment();
            }
        });
        binding.llDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadTourApplicationViewFragment();
            }
        });

        binding.llApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadTourApplicationApprovalViewFragment();
            }
        });

        binding.lAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadTourAttendanceFragment();
            }
        });
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("SecurityCode", pref.getSecurityCode());
            jsonObject.put("Employeeid", pref.getEmpId());
            TourApplicable(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JSONObject object=new JSONObject();
        try {
            object.put("AEMEmployeeID",pref.getEmpId());
            object.put("CompanyID",pref.getEmpClintId());
            object.put("SecurityCode",pref.getSecurityCode());
            getApproverOrNot(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void loadApplicationFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        TourApplicationFragment pfragment = new TourApplicationFragment();
        transaction.replace(R.id.frameLayout, pfragment);
        transaction.commit();
        binding.tvToolBar.setText("Tour Application");


        binding.llApplication.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1589FF")));
        binding.llDetails.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        binding.llApproval.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        binding.lAttendance.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));

        binding.tvApllication.setTextColor(Color.parseColor("#FFFFFF"));
        binding.tvDetails.setTextColor(Color.parseColor("#1589FF"));
        binding.tvApproval.setTextColor(Color.parseColor("#1589FF"));
        binding.tvAttendance.setTextColor(Color.parseColor("#1589FF"));
    }


    public void loadTourApplicationViewFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        TourViewFragment pfragment = new TourViewFragment();
        transaction.replace(R.id.frameLayout, pfragment);
        transaction.commit();
        binding.tvToolBar.setText("Tour Application Details");


        binding.llApplication.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        binding.llDetails.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1589FF")));
        binding.llApproval.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        binding.lAttendance.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));

        binding.tvApllication.setTextColor(Color.parseColor("#1589FF"));
        binding.tvDetails.setTextColor(Color.parseColor("#FFFFFF"));
        binding.tvApproval.setTextColor(Color.parseColor("#1589FF"));
        binding.tvAttendance.setTextColor(Color.parseColor("#1589FF"));
    }

    public void loadTourApplicationApprovalViewFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        TourApprovalFragment pfragment = new TourApprovalFragment();
        transaction.replace(R.id.frameLayout, pfragment);
        transaction.commit();
        binding.tvToolBar.setText("Tour Application Approval");


        binding.llApplication.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        binding.llDetails.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        binding.llApproval.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1589FF")));
        binding.lAttendance.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));

        binding.tvApllication.setTextColor(Color.parseColor("#1589FF"));
        binding.tvDetails.setTextColor(Color.parseColor("#1589FF"));
        binding.tvApproval.setTextColor(Color.parseColor("#FFFFFF"));
        binding.tvAttendance.setTextColor(Color.parseColor("#1589FF"));
    }


    public void loadTourAttendanceFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        TourAttendanceFragment pfragment = new TourAttendanceFragment();
        transaction.replace(R.id.frameLayout, pfragment);
        transaction.commit();
        binding.tvToolBar.setText("Tour Attendance");


        binding.llApplication.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        binding.llDetails.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        binding.llApproval.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        binding.lAttendance.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1589FF")));

        binding.tvApllication.setTextColor(Color.parseColor("#1589FF"));
        binding.tvDetails.setTextColor(Color.parseColor("#1589FF"));
        binding.tvApproval.setTextColor(Color.parseColor("#1589FF"));
        binding.tvAttendance.setTextColor(Color.parseColor("#FFFFFF"));
    }


    private void TourApplicable(JSONObject object) {
        final ProgressDialog progressDialog = new ProgressDialog(TourActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        AndroidNetworking.post(Api.sGetIStouraplicable)
                .addJSONObjectBody(object)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();

                        JSONObject job1 = response;

                        String Response_Code = job1.optString("Response_Code");
                        String Response_Message = job1.optString("Response_Message");
                        if (Response_Code.equals("101")) {
                            String Response_Data = job1.optString("Response_Data");
                            try {
                                JSONObject ResponseData = new JSONObject(Response_Data);
                                String Table = ResponseData.optString("Table");
                                JSONArray TableArray = new JSONArray(Table);
                                if (TableArray.length() > 0) {


                                    JSONObject tourOBJ = TableArray.optJSONObject(0);
                                    int MENULINK = tourOBJ.optInt("MENULINK");
                                    if (MENULINK==1){
                                        binding.lAttendance.setVisibility(View.VISIBLE);
                                    }else {
                                        binding.lAttendance.setVisibility(View.GONE);
                                    }


                                } else {

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();

                    }
                });
    }


    private void getApproverOrNot(JSONObject jsonObject) {

        final ProgressDialog pd=new ProgressDialog(TourActivity.this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(Api.sapprovercheckapi)
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
                        Log.e("response12", "@@@@@@" + job1);


                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                            String responseData = job1.optString("Response_Data");
                            try {
                                JSONArray jsonArray=new JSONArray(responseData);
                                if (jsonArray.length()>0) {

                                    binding.llApproval.setVisibility(View.VISIBLE);
                                } else {
                                    binding.llApproval.setVisibility(View.GONE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }





                            // boolean _status = job1.getBoolean("status");


                            // do anything with response
                        }else {
                            binding.llApproval.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(ANError error) {

                        pd.dismiss();


                    }
                });
    }
}