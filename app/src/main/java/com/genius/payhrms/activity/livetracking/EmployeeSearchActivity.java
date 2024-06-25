package com.genius.payhrms.activity.livetracking;

import android.app.ProgressDialog;

import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

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
import com.genius.payhrms.activity.adapter.TrackingAdapter;
import com.genius.payhrms.activity.model.TrackingModel;
import com.genius.payhrms.activity.utility.Pref;


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
                    //getEmpList();
                }

            }
        });

    }




}
