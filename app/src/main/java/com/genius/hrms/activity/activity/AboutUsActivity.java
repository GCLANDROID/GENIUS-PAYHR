package com.genius.hrms.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

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
import com.genius.hrms.activity.adapter.MenuAdapter;
import com.genius.hrms.activity.model.MenuModule;
import com.genius.hrms.activity.utility.NetworkConnectionCheck;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class AboutUsActivity extends AppCompatActivity {
    ImageView imgHome, imgBack;
    LinearLayout llLoader, llMain;
    RecyclerView rvAbout;
    ArrayList<MenuModule> menuList = new ArrayList<>();
    MenuAdapter menuAdapter;
    NetworkConnectionCheck connectionCheck;
    ImageView imgAgain;
    LinearLayout llAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        initialize();
        if (connectionCheck.isNetworkAvailable()) {
            getAboutInformation();
        } else {
            connectionCheck.getNetworkActiveAlert().show();
        }
        onClick();
    }

    private void initialize() {
        //TEST

        connectionCheck=new NetworkConnectionCheck(AboutUsActivity.this);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        llLoader = (LinearLayout) findViewById(R.id.llLoader);
        llMain = (LinearLayout) findViewById(R.id.llMain);
        rvAbout=(RecyclerView)findViewById(R.id.rvAbout);
        imgAgain = (ImageView) findViewById(R.id.imgAgain);
        llAgain = (LinearLayout) findViewById(R.id.llAgain);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(AboutUsActivity.this, LinearLayoutManager.VERTICAL, false);
        rvAbout.setLayoutManager(layoutManager);
        final Handler textViewHandler = new Handler();



    }

    private void onClick() {

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AboutUsActivity.this, DashBoardActivity.class);
                startActivity(intent);
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imgAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAboutInformation();
            }
        });
    }

    private void getAboutInformation() {
        String surl = "http://111.93.182.174/GeniusiOSApi/api/gcl_HomeScreen?MenuId=1&SecurityCode=0000";
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
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 1; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String MenuName = obj.optString("MenuName");


                                    String Description = obj.optString("Description").replaceAll("<br/>", "").replaceAll("<b>", "").replaceAll("</b>", "");
                                    MenuModule menuModule = new MenuModule(MenuName, Description, false);
                                    menuList.add(menuModule);


                                }
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llAgain.setVisibility(View.GONE);
                                menuAdapter = new MenuAdapter(menuList, AboutUsActivity.this);
                                rvAbout.setAdapter(menuAdapter);
                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            //  Toast.makeText(AboutUsActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                            llLoader.setVisibility(View.GONE);
                            llMain.setVisibility(View.GONE);
                            llAgain.setVisibility(View.VISIBLE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(AboutUsActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(AboutUsActivity.this);
        requestQueue.add(stringRequest);


    }


    public void updateStatus(int position, boolean status) {
        for (int i = 0; i < menuList.size(); i++) {
            if (i == position) {
                menuList.get(i).setExpanded(status);
            } else {
                menuList.get(i).setExpanded(false);
            }
        }
        menuAdapter.notifyDataSetChanged();
    }




}
