package com.genius.hrms.activity.dailylog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.genius.hrms.activity.adapter.TourReportAdapter;
import com.genius.hrms.activity.model.VisitingLocationModel;
import com.genius.hrms.activity.utility.Pref;
import com.google.android.gms.maps.model.LatLng;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class TourViewReportActivity extends AppCompatActivity {
    RecyclerView rvItem;
    ArrayList<VisitingLocationModel> itemList=new ArrayList<>();
    LinearLayout llLoader,llMain,llNoData;
    Pref pref;
    String formattedDate;
    TextView tvDate;
    ImageView imgBack,imgHome;
    TextView tvDistance,tvToolBar;
     String hLoc ;
     ProgressDialog progressDialog;
    String surl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_view_report);
        initView();
        onClick();

    }

    private void initView(){
        pref=new Pref(getApplicationContext());
        rvItem=(RecyclerView)findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(TourViewReportActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);
        llMain=(LinearLayout)findViewById(R.id.llMain);
        llLoader=(LinearLayout)findViewById(R.id.llLoader);
        llNoData=(LinearLayout)findViewById(R.id.llNoData);

        formattedDate = getIntent().getStringExtra("attdate").replaceAll("\\s+", "-");

        tvDate=(TextView)findViewById(R.id.tvDate);
        tvDate.setText(formattedDate);
        getItem(formattedDate);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        tvDistance=(TextView)findViewById(R.id.tvDistance);
        tvToolBar=(TextView)findViewById(R.id.tvToolBar);
        if (pref.getLanguage().equals("hi")){
            tvToolBar.setText("भ्रमण दृश्य");
        }else {
            tvToolBar.setText("Tour View");
        }
        progressDialog=new ProgressDialog(TourViewReportActivity.this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);

    }

    private void getItem(String date){
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        if (pref.getSecurityCode().equals("123")){
            surl= "http://111.93.182.174/GeniusiOSApi/api/get_OfflineDailyLogActivity?AEMEmployeeID=" + pref.getEmpId() + "&Year=0&Month=0&SecurityCode=" + pref.getSecurityCode()+"&AttendanceDate=0&Operation=2";

        }else {
            surl = pref.getIpAddress()+"GHRMSApi/api/get_OfflineDailyLogActivity?AEMEmployeeID=" + pref.getEmpId() + "&Year=0&Month=0&SecurityCode=" + pref.getSecurityCode()+"&AttendanceDate="+date+"&Operation=1";

        }
        Log.d("inputactivity", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);

                        // attendabceInfiList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i <responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);

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
                               // double distance=distance();
                              //  String sDisttance=String.format("%.3f", distance);
                               // tvDistance.setText("Total Distance:"+sDisttance+"KM");

                               // Log.d("sDisttance",sDisttance);

                            } else {

                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNoData.setVisibility(View.VISIBLE);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);
                llNoData.setVisibility(View.GONE);


                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(TourViewReportActivity.this);
        requestQueue.add(stringRequest);
    }
    private void setAdapter(){
        TourReportAdapter vAdapter=new TourReportAdapter(itemList,getApplicationContext());
        rvItem.setAdapter(vAdapter);
    }
    private void onClick(){
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), UserDashBoardActivity.class);
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

    public  double distance(){
        LatLng src=new LatLng(Double.parseDouble(itemList.get(0).getLattitude()),Double.parseDouble(itemList.get(0).getLongitude()));
        LatLng des=new LatLng(Double.parseDouble(itemList.get(itemList.size()-1).getLattitude()),Double.parseDouble(itemList.get(itemList.size()-1).getLongitude()));
        double distance=CalculationByDistance(src,des);
        return  distance;

    }

    public  static double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        //  Toast.makeText(getApplicationContext(),"Radious Value:  "+valueResult+"  KM: "+kmInDec+" Meter: "+meterInDec,Toast.LENGTH_LONG).show();
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);
        return Radius * c;
    }


    public void pdShow(){
        progressDialog.show();
    }
    public void pdDismiss(){
        progressDialog.dismiss();
    }




}
