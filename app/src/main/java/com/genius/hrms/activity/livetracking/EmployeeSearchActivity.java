package com.genius.hrms.activity.livetracking;

import android.app.ProgressDialog;

import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

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
import com.genius.hrms.activity.adapter.TrackingAdapter;
import com.genius.hrms.activity.model.TrackingModel;
import com.genius.hrms.activity.utility.Pref;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EmployeeSearchActivity extends AppCompatActivity {
    EditText imgSearch;
    RecyclerView rvItem;
    EditText etSearch;
    ArrayList<TrackingModel> itemList = new ArrayList<>();
    ImageView imgBack, imgHome;
    Pref pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_search);
        initView();
        onClick();
    }

    private void initView() {
        pref=new Pref(getApplicationContext());
        rvItem = (RecyclerView) findViewById(R.id.rvItem);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(EmployeeSearchActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(linearLayoutManager);
        etSearch = (EditText) findViewById(R.id.etSearch);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgBack);
    }

    private void onClick() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etSearch.getText().toString().length()==3){
                    getEmpList();
                }

            }
        });

    }

    private void getEmpList() {
        final ProgressDialog pd=new ProgressDialog(EmployeeSearchActivity.this);
        pd.setMessage("Loding...");
        pd.setCancelable(false);
        pd.show();
        String surl = pref.getIpAddress()+"GHRMSApi/api/get_EmployeeSearchModule?Searchtext="+etSearch.getText().toString()+"&SecurityCode="+pref.getSecurityCode();
        Log.d("input", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);


                        // attendabceInfiList.clear();
                        pd.dismiss();

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
                                    String Name=obj.optString("Name");
                                    String EmployeeID=obj.optString("EmployeeID");
                                    TrackingModel tModel=new TrackingModel(Name,EmployeeID);
                                    itemList.add(tModel);



                                }
                                pd.dismiss();
                                TrackingAdapter tAdapter=new TrackingAdapter(itemList,getApplicationContext());
                                rvItem.setAdapter(tAdapter);


                            } else {

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(EmployeeSearchActivity.this);
        requestQueue.add(stringRequest);
    }


}
