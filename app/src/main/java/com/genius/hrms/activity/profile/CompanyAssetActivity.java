package com.genius.hrms.activity.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genius.hrms.R;
import com.genius.hrms.activity.activity.UserDashBoardActivity;
import com.genius.hrms.activity.adapter.AttendanceAdapter;
import com.genius.hrms.activity.adapter.CompanyAssetAdapter;
import com.genius.hrms.activity.attendance.AttendanceReportActivity;
import com.genius.hrms.activity.model.CompanyAssetModel;
import com.genius.hrms.activity.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CompanyAssetActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView rvItem;
    ArrayList<CompanyAssetModel>itemList=new ArrayList<>();
    Pref pref;
    LinearLayout lnMain,lnNodata;
    ImageView imgBack,imgHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_asset);
        initView();
    }

    private void initView(){
        pref=new Pref(CompanyAssetActivity.this);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        lnNodata=(LinearLayout)findViewById(R.id.lnNodata);
        lnMain=(LinearLayout)findViewById(R.id.lnMain);
        rvItem=(RecyclerView) findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(CompanyAssetActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);
        getAssetList();
        imgHome.setOnClickListener(this);
        imgBack.setOnClickListener(this);
    }

    private void getAssetList() {
        final ProgressDialog pd=new ProgressDialog(CompanyAssetActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        lnMain.setVisibility(View.VISIBLE);
        lnNodata.setVisibility(View.GONE);
        String surl = pref.getIpAddress()+"GHRMSApi/API/get_EmployeeAsset?&AEMEmployeeId="+pref.getEmpId()+"&SecurityCode="+pref.getSecurityCode();
        Log.d("input", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        pd.dismiss();

                        // attendabceInfiList.clear();
                        itemList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i=0;i<responseData.length();i++){
                                    JSONObject assetOBJ=responseData.optJSONObject(i);
                                    String assetName=assetOBJ.optString("Asset Name");
                                    String assignedDate=assetOBJ.optString("Assigned Date");
                                    String realesedDate=assetOBJ.optString("Released Date");
                                    CompanyAssetModel assetModel=new CompanyAssetModel();
                                    assetModel.setAssetName(assetName);
                                    assetModel.setAssignedDate(assignedDate);
                                    assetModel.setRelaseDate(realesedDate);
                                    itemList.add(assetModel);
                                }
                                lnMain.setVisibility(View.VISIBLE);
                                lnNodata.setVisibility(View.GONE);
                                setAdapter();


                            } else {
                                lnMain.setVisibility(View.GONE);
                                lnNodata.setVisibility(View.VISIBLE);


                                //Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               pd.dismiss();
                lnMain.setVisibility(View.GONE);
                lnNodata.setVisibility(View.VISIBLE);
                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(CompanyAssetActivity.this);
        requestQueue.add(stringRequest);
    }

    private void setAdapter() {
        CompanyAssetAdapter assetAdapter = new CompanyAssetAdapter(itemList);
        rvItem.setAdapter(assetAdapter);
    }

    @Override
    public void onClick(View view) {
        if (view==imgBack){
            onBackPressed();
        }else if (view==imgHome){
            Intent intent=new Intent(CompanyAssetActivity.this, UserDashBoardActivity.class);
            startActivity(intent);
            finish();
        }

    }
}