package com.genius.hrms.activity.geofence;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.genius.hrms.activity.adapter.EmpMapingReportAdapter;
import com.genius.hrms.activity.model.EmpMapiingReportModel;
import com.genius.hrms.activity.model.SpinnerModel;
import com.genius.hrms.activity.utility.Pref;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EmpMapingReportActivity extends AppCompatActivity {
    Spinner spLocation;
    LinearLayout llMain, llRV, llLoader, llNoData;
    RecyclerView rvItem;
    ImageView imgBack, imgHome;
    ArrayList<SpinnerModel> mLocationList = new ArrayList<>();
    ArrayList<String> locationList = new ArrayList<>();
    Pref pref;
    ArrayList<EmpMapiingReportModel> empList = new ArrayList<>();
    LinearLayout llNoData1;
    String locationId = "0";
    String point;
    String surl,surl1;
    TextView tvToolbar,tvNodata;
    String surl2,translated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_maping_report);

        initView();
        locationGet();
        onClick();
    }

    private void initView() {
        pref = new Pref(getApplicationContext());
        spLocation = (Spinner) findViewById(R.id.spLocation);
        llMain = (LinearLayout) findViewById(R.id.llMain);
        llRV = (LinearLayout) findViewById(R.id.llRV);
        llNoData = (LinearLayout) findViewById(R.id.llNoData);
        llLoader = (LinearLayout) findViewById(R.id.llLoader);
        rvItem = (RecyclerView) findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(EmpMapingReportActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);

        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        llNoData1 = (LinearLayout) findViewById(R.id.llNoData1);
        point=getIntent().getStringExtra("point");
        tvToolbar=(TextView)findViewById(R.id.tvToolBar);
        tvNodata=(TextView)findViewById(R.id.tvNoData);
        if (pref.getLanguage().equals("hi")){
            tvToolbar.setText("कर्मचारी मानचित्रण रिपोर्ट");
            tvNodata.setText("कोई डेटा नहीं मिला");
        }else {
            tvToolbar.setText("Employee mapping report");
            tvNodata.setText("No vdata found");
        }
    }

    private void locationGet() {
        if (point.equals("mul")){
            surl2 = pref.getIpAddress()+"GHRMSApi/api/get_GeofenceMultiEndPointConfiguration?LocationId=0&Operation=1&SecurityCode=" + pref.getSecurityCode();

        }else {
            surl2 =  pref.getIpAddress()+"GHRMSApi/api/get_GeofenceConfiguration?SLongitude=0&SLatitude=0&SAddress=0&ELongitude=0&ELatitude=0&EAddress=0&EndPoint=0&LocationName=0&Operation=1&SecurityCode=" + pref.getSecurityCode();
        }
        Log.d("configurl", surl2);
        final ProgressDialog progressDialog = new ProgressDialog(EmpMapingReportActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseconfig", response);
                        progressDialog.dismiss();
                        locationList.clear();
                        mLocationList.clear();
                        locationList.add("Please select");
                        mLocationList.add(new SpinnerModel("0","0"));

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("responseconfig", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {

                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    final String LocationName = obj.optString("LocationName");
                                    final String GeoFenceId = obj.optString("GeoFenceId");
                                    if (pref.getLanguageFlag().equals("1")) {
                                        final Handler textViewHandler1 = new Handler();
                                        new AsyncTask<Void, Void, Void>() {
                                            @Override
                                            protected Void doInBackground(Void... params) {
                                                TranslateOptions options = TranslateOptions.newBuilder()
                                                        .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
                                                        .build();
                                                Translate translate = options.getService();
                                                final Translation translation =
                                                        translate.translate(LocationName,
                                                                Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                                                textViewHandler1.post(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        Log.d("sssh", translation.getTranslatedText());
                                                        translated = translation.getTranslatedText();
                                                        locationList.add(translated);
                                                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                                                (EmpMapingReportActivity.this, android.R.layout.simple_spinner_item,
                                                                        locationList); //selected item will look like a spinner set from XML
                                                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                        spLocation.setAdapter(spinnerArrayAdapter);

                                                        SpinnerModel spModel = new SpinnerModel(translated, GeoFenceId);

                                                        mLocationList.add(spModel);


                                                    }
                                                });
                                                return null;
                                            }

                                            @Override
                                            protected void onPreExecute() {
                                                super.onPreExecute();
                                                progressDialog.show();

                                            }

                                            @Override
                                            protected void onPostExecute(Void aVoid) {
                                                super.onPostExecute(aVoid);
                                                progressDialog.dismiss();

                                            }


                                        }.execute();
                                    }else {
                                        locationList.add(LocationName);
                                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                                (EmpMapingReportActivity.this, android.R.layout.simple_spinner_item,
                                                        locationList); //selected item will look like a spinner set from XML
                                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spLocation.setAdapter(spinnerArrayAdapter);
                                        SpinnerModel spModel = new SpinnerModel(LocationName, GeoFenceId);

                                        mLocationList.add(spModel);

                                    }


                                }



                            } else {
                                progressDialog.show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(EmpMapingReportActivity.this);
        requestQueue.add(stringRequest);


    }

    private void onClick() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
        spLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    locationId = mLocationList.get(position).getItemId();
                    getEmpList();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getEmpList() {
        if (point.equals("mul")){
            surl1 =  pref.getIpAddress()+"GHRMSApi/api/get_EmployeeGeofenceMultipointConfigure?EmployeeId=0&GeoFenceId="+locationId+"&Operation=1&SecurityCode="+pref.getSecurityCode();

        }else {
            surl1 =  pref.getIpAddress()+"GHRMSApi/api/get_EmployeeGeofenceConfigure?EmployeeId=0&GeoFenceId=" + locationId + "&Operation=1&SecurityCode=" + pref.getSecurityCode();

        }
        Log.d("configurl", surl1);
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading..");
        llRV.setVisibility(View.VISIBLE);
        llNoData1.setVisibility(View.GONE);
        pd.setCancelable(false);
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseconfig", response);
                        empList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("responseconfig", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {

                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String EmpName = obj.optString("EmpName");
                                    String LocationName=obj.optString("LocationName");

                                    EmpMapiingReportModel mModel = new EmpMapiingReportModel(EmpName,LocationName);
                                    empList.add(mModel);

                                }

                                pd.dismiss();
                                EmpMapingReportAdapter cAdapter = new EmpMapingReportAdapter(empList,EmpMapingReportActivity.this);
                                rvItem.setAdapter(cAdapter);


                            } else {
                                pd.dismiss();
                                llRV.setVisibility(View.GONE);
                                llNoData1.setVisibility(View.VISIBLE);
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
        RequestQueue requestQueue = Volley.newRequestQueue(EmpMapingReportActivity.this);
        requestQueue.add(stringRequest);


    }
}
