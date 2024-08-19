package com.genius.payhrms.activity.attendance.tour;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.genius.payhrms.R;
import com.genius.payhrms.activity.leaveapplication.ApplicationFragment;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.Pref;
import com.genius.payhrms.databinding.ActivityTourBinding;

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
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("SecurityCode", pref.getSecurityCode());
            jsonObject.put("Employeeid", pref.getEmpId());
            TourApplicable(jsonObject);
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
    }


    public void loadTourApplicationViewFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        TourViewFragment pfragment = new TourViewFragment();
        transaction.replace(R.id.frameLayout, pfragment);
        transaction.commit();
        binding.tvToolBar.setText("Tour Application Details");
    }

    public void loadTourApplicationApprovalViewFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        TourApprovalFragment pfragment = new TourApprovalFragment();
        transaction.replace(R.id.frameLayout, pfragment);
        transaction.commit();
        binding.tvToolBar.setText("Tour Application Approval");
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
}