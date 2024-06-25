package com.genius.payhrms.activity.activity;

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
import com.genius.payhrms.R;
import com.genius.payhrms.activity.adapter.MenuServiceAdapter;
import com.genius.payhrms.activity.model.MenuModule;
import com.genius.payhrms.activity.utility.NetworkConnectionCheck;


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
