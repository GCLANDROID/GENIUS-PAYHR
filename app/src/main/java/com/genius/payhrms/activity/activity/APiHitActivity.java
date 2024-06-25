package com.genius.payhrms.activity.activity;

import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

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
import com.genius.payhrms.activity.dailylog.MapReportActivity;
import com.genius.payhrms.activity.model.VisitingLocationModel;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.Pref;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class APiHitActivity extends AppCompatActivity {
    private static final String TAG = "APiHitActivity";
    Pref pref;
    ArrayList<VisitingLocationModel>addressList=new ArrayList<>();
    String surl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_hit);
        pref=new Pref(getApplicationContext());
        String formattedDate = getIntent().getStringExtra("attdate").replaceAll("\\s+", "-");
        //loadNames(formattedDate);
        JSONObject object=new JSONObject();
        try {
            object.put("AEMEmployeeID",pref.getEmpId());
            object.put("Year",0);
            object.put("Month",0);
            object.put("SecurityCode",pref.getSecurityCode());
            object.put("AttendanceDate",formattedDate);
            object.put("Operation",1);
            loadNames2(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void loadNames2(JSONObject object) {
        //names.clear();
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);

        AndroidNetworking.post(Api.sGetOfflineDailyLogActivity)
                .addJSONObjectBody(object)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "MAP_VIEW: "+response);
                        pd.dismiss();
                        try {
                            JSONObject job1 = response;
                            int Response_Code = job1.optInt("Response_Code");
                            if (Response_Code == 101) {
                                String responseData = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(responseData);
                                if (jsonArray.length() > 0) {
                                    for (int i = 0; i <jsonArray.length(); i++) {
                                        JSONObject obj = jsonArray.getJSONObject(i);

                                        String PunchInTime = obj.optString("PunchInTime");
                                        String AddressIN = obj.optString("AddressIN");
                                        String LongitudeIN=obj.optString("LongitudeIN");
                                        String LatitudeIN=obj.optString("LatitudeIN");

                                        VisitingLocationModel obj2 = new VisitingLocationModel(AddressIN,PunchInTime,LatitudeIN,LongitudeIN);
                                        addressList.add(obj2);
                                    }
                                    ArrayList<String>myList=new ArrayList<>();
                                    Intent intent=new Intent(APiHitActivity.this, MapReportActivity.class);
                                    intent.putExtra("myList",addressList);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    pd.show();
                                }

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "onError: "+anError );
                        pd.show();
                    }
                });
    }
}
