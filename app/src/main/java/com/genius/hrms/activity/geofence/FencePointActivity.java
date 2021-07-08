package com.genius.hrms.activity.geofence;

import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genius.hrms.R;
import com.genius.hrms.activity.activity.EmployeeDashBoardActivity;
import com.genius.hrms.activity.activity.UserDashBoardActivity;
import com.genius.hrms.activity.adapter.FencePointAdapter;
import com.genius.hrms.activity.model.FencePointModel;
import com.genius.hrms.activity.utility.Pref;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FencePointActivity extends AppCompatActivity {
    RecyclerView rvitem;
    ArrayList<FencePointModel>itemList=new ArrayList<>();
    Pref pref;
    ImageView imgBack,imgHome;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fence_point);
        initView();
        onClick();

    }

    private void initView(){
        pref=new Pref(getApplicationContext());
        rvitem=(RecyclerView)findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(FencePointActivity.this, LinearLayoutManager.VERTICAL, false);
        rvitem.setLayoutManager(layoutManager);
        fenceget(getIntent().getStringExtra("fenceid"));
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);

    }
    private void onClick(){
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void fenceget(String geofenceid) {
        String surl =  pref.getIpAddress()+"GHRMSApi/api/get_GeofenceMultiEndPointConfiguration?LocationId="+geofenceid+"&Operation=1&SecurityCode="+pref.getSecurityCode();
        Log.d("configurl", surl);
        final ProgressDialog progressDialog=new ProgressDialog(FencePointActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseconfig", response);
                        progressDialog.dismiss();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("responseconfig", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {

                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);




                                    String Address = obj.optString("Address").trim();

                                    FencePointModel mModel = new FencePointModel(Address);
                                    itemList.add(mModel);



                                }
                                FencePointAdapter fenceAdapter=new FencePointAdapter(itemList,FencePointActivity.this);
                                rvitem.setAdapter(fenceAdapter);




                            } else {

                            }


                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            //  Toast.makeText(EmployeeDashBoardActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Toast.makeText(EmployeeDashBoardActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());

            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(FencePointActivity.this);
        requestQueue.add(stringRequest);


    }
}
