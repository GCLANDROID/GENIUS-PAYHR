package com.genius.hrms.activity.geofence;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.genius.hrms.activity.adapter.MultipleConfigAdapter;
import com.genius.hrms.activity.model.MulFenceConfigModel;
import com.genius.hrms.activity.utility.Pref;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MultipleConfigReportActivity extends AppCompatActivity {


    LinearLayout llLoader, llMain, llNoData;
    RecyclerView rvItem;
    Pref pref;
    ArrayList<MulFenceConfigModel> itemList = new ArrayList<>();
    ImageView imgBack;
    ImageView imgHome;
    TextView tvToolBar;
    String tranLoc,transCretaed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_config_report);
        initView();
        fenceget();
        onClick();
    }


    private void initView() {
        pref = new Pref(getApplicationContext());
        llLoader = (LinearLayout) findViewById(R.id.llLoader);
        llMain = (LinearLayout) findViewById(R.id.llMain);
        llNoData = (LinearLayout) findViewById(R.id.llNoData);
        rvItem = (RecyclerView) findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(MultipleConfigReportActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        tvToolBar=(TextView)findViewById(R.id.tvToolBar);
        if (pref.getLanguage().equals("hi")){
            tvToolBar.setText("कॉन्फ़िगरेशन रिपोर्ट");
        }else {
            tvToolBar.setText("configuration report");
        }
    }

    private void fenceget() {
        String surl = pref.getIpAddress()+"GHRMSApi/api/get_GeofenceMultiEndPointConfiguration?LocationId=0&Operation=1&SecurityCode="+pref.getSecurityCode();
        Log.d("configurl", surl);
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseconfig", response);

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("responseconfig", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {

                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);



                                    String CreatedOn = obj.optString("CreatedOn");
                                    String LocationName = obj.optString("LocationName");
                                    String Address = obj.optString("Address").trim();
                                    String GeoFenceId=obj.optString("GeoFenceId");
                                    String GeofencePoints=obj.optString("GeofencePoints");
                                    MulFenceConfigModel mModel = new MulFenceConfigModel(CreatedOn,LocationName,Address,GeoFenceId,GeofencePoints);
                                    itemList.add(mModel);
                                    MultipleConfigAdapter cAdapter = new MultipleConfigAdapter(itemList,MultipleConfigReportActivity.this);
                                    rvItem.setAdapter(cAdapter);

                                }

                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNoData.setVisibility(View.GONE);


                            } else {
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNoData.setVisibility(View.VISIBLE);
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
                showAlert();
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(MultipleConfigReportActivity.this);
        requestQueue.add(stringRequest);


    }

    private void showAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Slow or No Internet connection");
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();

                    }
                });
        alertDialogBuilder.show();


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
                onBackPressed();
            }
        });
    }

    public String translateLocation(final int i){
        final Handler textViewHandler3 = new Handler();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                TranslateOptions options = TranslateOptions.newBuilder()
                        .setApiKey("AIzaSyDL1itt-7WRkrelJeuvOfiC-_SGc3JZ4vY")
                        .build();
                Translate translate = options.getService();
                final Translation translation =
                        translate.translate(itemList.get(i).getAddress(),
                                Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                textViewHandler3.post(new Runnable() {
                    @Override
                    public void run() {

                        Log.d("sssh", translation.getTranslatedText());
                        tranLoc = translation.getTranslatedText();




                    }
                });
                return null;
            }
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);

            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);

            }


        }.execute();



        return tranLoc;
    }

    public String tranlateCretaedOn(final int i){
        final Handler textViewHandler1 = new Handler();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                TranslateOptions options = TranslateOptions.newBuilder()
                        .setApiKey("AIzaSyDL1itt-7WRkrelJeuvOfiC-_SGc3JZ4vY")
                        .build();
                Translate translate = options.getService();
                final Translation translation =
                        translate.translate(itemList.get(i).getCraetdOn(),
                                Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                textViewHandler1.post(new Runnable() {
                    @Override
                    public void run() {

                        Log.d("sssh", translation.getTranslatedText());
                        transCretaed = translation.getTranslatedText();




                    }
                });
                return null;
            }
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);

            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                llLoader.setVisibility(View.GONE);
                llMain.setVisibility(View.VISIBLE);

            }


        }.execute();
        return transCretaed;
    }




}
