package com.genius.payhrms.activity.dailylog;

import static com.genius.payhrms.activity.utility.Util.SECRET_KEY;
import static com.genius.payhrms.activity.utility.Util.encrypt;

import android.app.ProgressDialog;
import android.content.Intent;

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
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.genius.payhrms.R;

import com.genius.payhrms.activity.activity.UserDashBoardActivity;
import com.genius.payhrms.activity.adapter.TourReportAdapter;
import com.genius.payhrms.activity.model.VisitingLocationModel;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.Pref;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class TourViewReportActivity extends AppCompatActivity {
    private static final String TAG = "TourViewReportActivity";
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

        JSONObject object=new JSONObject();
        try {
            object.put("AEMEmployeeID",pref.getEmpId());
            object.put("Year",0);
            object.put("Month",0);
            object.put("SecurityCode",pref.getSecurityCode());
            object.put("AttendanceDate",formattedDate);
            object.put("Operation",1);
            getItem2(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

    private void getItem2(JSONObject object){
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);


        AndroidNetworking.post(Api.sGetOfflineDailyLogActivity)
                .addJSONObjectBody(object)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "TOUR_VIEW_DATA: "+response);
                        JSONObject job1 = response;
                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            String responseData = job1.optString("Response_Data");
                            try {
                                JSONArray jsonArray = new JSONArray(responseData);
                                if (jsonArray.length() > 0) {
                                    for (int i = 0; i <jsonArray.length(); i++) {
                                        JSONObject obj = jsonArray.getJSONObject(i);

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
                                } else {
                                    llLoader.setVisibility(View.GONE);
                                    llMain.setVisibility(View.GONE);
                                    llNoData.setVisibility(View.VISIBLE);
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            llLoader.setVisibility(View.GONE);
                            llMain.setVisibility(View.GONE);
                            llNoData.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "TOUR_VIEW_DATA_error: "+anError);
                        if (anError.getErrorCode()==401){
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
                        } else {
                            llLoader.setVisibility(View.GONE);
                            llMain.setVisibility(View.GONE);
                            llNoData.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    private void login(JSONObject jsonObject) {
        final ProgressDialog pd = new ProgressDialog(TourViewReportActivity.this);
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
                                // do anything with response
                                JSONObject object=new JSONObject();
                                try {
                                    object.put("AEMEmployeeID",pref.getEmpId());
                                    object.put("Year",0);
                                    object.put("Month",0);
                                    object.put("SecurityCode",pref.getSecurityCode());
                                    object.put("AttendanceDate",formattedDate);
                                    object.put("Operation",1);
                                    getItem2(object);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                    }
                });
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
