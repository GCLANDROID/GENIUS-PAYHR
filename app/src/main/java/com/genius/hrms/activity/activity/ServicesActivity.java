package com.genius.hrms.activity.activity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.genius.hrms.R;
import com.genius.hrms.activity.adapter.MenuServiceAdapter;
import com.genius.hrms.activity.model.MenuModule;
import com.genius.hrms.activity.utility.NetworkConnectionCheck;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;



public class ServicesActivity extends AppCompatActivity {

    ImageView imgBack,imgHome;
    RecyclerView rvService;
    ArrayList<MenuModule>menuList=new ArrayList<>();
    LinearLayout llMain,llLoader;
    NetworkConnectionCheck connectionCheck;
    MenuServiceAdapter menuAdapter;
    LinearLayout llAgain;
    ImageView imgAgain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        initialize();
        if (connectionCheck.isNetworkAvailable()) {
            getServiceInformation();
        }else {
            connectionCheck.getNetworkActiveAlert().show();
        }
        onClick();
    }
    private void initialize(){
        connectionCheck=new NetworkConnectionCheck(ServicesActivity.this);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        rvService=(RecyclerView)findViewById(R.id.rvService);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(ServicesActivity.this, LinearLayoutManager.VERTICAL, false);
        rvService.setLayoutManager(layoutManager);
        llMain=(LinearLayout)findViewById(R.id.llMain);
        llLoader=(LinearLayout)findViewById(R.id.llLoader);
        llAgain=(LinearLayout)findViewById(R.id.llAgain);
        imgAgain=(ImageView)findViewById(R.id.imgAgain);
        imgAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getServiceInformation();
            }
        });



    }

    private void onClick(){

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ServicesActivity.this,DashBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //finish();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void getServiceInformation(){
        String surl ="http://111.93.182.174/GeniusiOSApi/api/gcl_HomeScreen?MenuId=9&SecurityCode=0000";
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llAgain.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText=job1.optString("responseText");
                            boolean responseStatus=job1.optBoolean("responseStatus");
                            if (responseStatus){
                              //  Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                                JSONArray responseData=job1.optJSONArray("responseData");
                                for (int i = 1; i < responseData.length(); i++){
                                    JSONObject obj=responseData.getJSONObject(i);
                                    String MenuName=obj.optString("MenuName");
                                    String Description=obj.optString("Description").replaceAll("<br/>","").replaceAll("<b>","").replaceAll("</b>","");
                                    MenuModule menuModule=new MenuModule(MenuName,Description,false);
                                    menuList.add(menuModule);



                                }
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llAgain.setVisibility(View.GONE);
                                 menuAdapter=new MenuServiceAdapter(menuList,ServicesActivity.this);
                                rvService.setAdapter(menuAdapter);
                            }
                            else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ServicesActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.GONE);
                llMain.setVisibility(View.GONE);
                llAgain.setVisibility(View.VISIBLE);

               // Toast.makeText(ServicesActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert",error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(ServicesActivity.this);
        requestQueue.add(stringRequest);


    }


    public void updateStatus(int position,boolean status)
    {
        for (int i =0 ;i<menuList.size();i++)
        {
            if (i==position)
            {
                menuList.get(i).setExpanded(status);
            }
            else
            {
                menuList.get(i).setExpanded(false);
            }
        }
        menuAdapter.notifyDataSetChanged();
    }
}
