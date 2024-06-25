package com.genius.payhrms.activity.activity;

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
import com.genius.payhrms.R;
import com.genius.payhrms.activity.adapter.MenuAdapter;
import com.genius.payhrms.activity.model.MenuModule;
import com.genius.payhrms.activity.utility.NetworkConnectionCheck;



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
            //getAboutInformation();
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
                //getAboutInformation();
            }
        });
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
