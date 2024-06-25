package com.genius.payhrms.activity.activity;

import static com.genius.payhrms.activity.utility.Util.SECRET_KEY;
import static com.genius.payhrms.activity.utility.Util.encrypt;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.genius.payhrms.activity.adapter.HolidayAdapter;
import com.genius.payhrms.activity.adapter.HolidayAdapterForSamrtJoules;
import com.genius.payhrms.activity.model.HoliDayModel;
import com.genius.payhrms.activity.profile.ProfileActivity;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.NetworkConnectionCheck;
import com.genius.payhrms.activity.utility.Pref;
import com.genius.payhrms.activity.utility.ValidUtils;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class HolidayActivity extends AppCompatActivity {
    RecyclerView rvHoliday;
    ArrayList<HoliDayModel> holidayList=new ArrayList<>();
    ImageView imgBack,imgHome;
    LinearLayout llLoder;
    String year;
    int y;
    Pref pref;
    NetworkConnectionCheck connectionCheck;
    LinearLayout llAgain,llMain,llNodata;
    ImageView imgAgain;
    TextView tvToolBar;
    LinearLayout lnHoliday;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday);
        initialize();
        onClick();
    }

    private void initialize(){
        pref=new Pref(HolidayActivity.this);
        lnHoliday=(LinearLayout)findViewById(R.id.lnHoliday);
        connectionCheck=new NetworkConnectionCheck(HolidayActivity.this);
        rvHoliday=(RecyclerView)findViewById(R.id.rvHoliday);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(HolidayActivity.this, LinearLayoutManager.VERTICAL, false);
        rvHoliday.setLayoutManager(layoutManager);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        llLoder=(LinearLayout)findViewById(R.id.llWLLoader) ;
        llMain=(LinearLayout)findViewById(R.id.llMain);
        llNodata=(LinearLayout)findViewById(R.id.llNodata);
        llAgain=(LinearLayout)findViewById(R.id.llAgain);
        imgAgain=(ImageView)findViewById(R.id.imgAgain);
        y= Calendar.getInstance().get(Calendar.YEAR);
        year=String.valueOf(y);
        tvToolBar=(TextView)findViewById(R.id.tvToolBar);
        if (pref.getLanguage().equals("hi")){
            tvToolBar.setText("छुट्टी की सूची");
        }else {
            tvToolBar.setText("Holiday List");
        }

        if (pref.getSecurityCode().equals("1153")){
            //getHolidayListForSamrtJoule();
            lnHoliday.setVisibility(View.VISIBLE);
        }else {
           // getHolidayList();
            lnHoliday.setVisibility(View.GONE);

            JSONObject object=new JSONObject();
            try {

                object.put("AEMEmployeeID",pref.getEmpId());
                object.put("Year",year);
                object.put("SecurityCode",pref.getSecurityCode());
                holiday(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    private void onClick(){
        imgAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  getHolidayList();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HolidayActivity.this,UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void holiday(JSONObject jsonObject) {
        llLoder.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);
        llAgain.setVisibility(View.GONE);
        AndroidNetworking.post(Api.sHolidayapi)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);

                        try {
                            int Response_Code = job1.optInt("Response_Code");
                            if (Response_Code == 101) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                                String responseData = job1.optString("Response_Data");

                                JSONArray jsonArray = new JSONArray(responseData);

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String HolidayName = obj.optString("HolidayName");
                                    String HolidayDate = obj.optString("HolidayDate");
                                    String HDay = obj.optString("HDay");

                                    HoliDayModel obj2 = new HoliDayModel(HolidayName, HolidayDate, HDay);
                                    holidayList.add(obj2);

                                }

                                llLoder.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNodata.setVisibility(View.GONE);
                                llAgain.setVisibility(View.GONE);
                                setAdapter();
                                // boolean _status = job1.getBoolean("status");
                                // do anything with response
                            } else {
                                llLoder.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNodata.setVisibility(View.VISIBLE);
                                llAgain.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        if (error.getErrorCode()==401){
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
                        }
                    }
                });
    }

    private void login(JSONObject jsonObject) {
        final ProgressDialog pd = new ProgressDialog(HolidayActivity.this);
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

                                JSONObject object=new JSONObject();
                                try {

                                    object.put("AEMEmployeeID",pref.getEmpId());
                                    object.put("Year",year);
                                    object.put("SecurityCode",pref.getSecurityCode());
                                    holiday(object);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                // do anything with response
                            }
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                    }
                });
    }



    private  void setAdapter(){
        HolidayAdapter hAdapter =new HolidayAdapter(holidayList,HolidayActivity.this);
        rvHoliday.setAdapter(hAdapter);
    }
}
