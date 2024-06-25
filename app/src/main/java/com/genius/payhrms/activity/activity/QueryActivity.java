package com.genius.payhrms.activity.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.genius.payhrms.R;

public class QueryActivity extends AppCompatActivity {
    LinearLayout llUnAuthorised,llAuthorised;
    String goingFlag;
    ImageView imgHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        initView();
        onClick();
    }

    private void initView(){
        llAuthorised=(LinearLayout)findViewById(R.id.llAuthorised);
        llUnAuthorised=(LinearLayout)findViewById(R.id.llUnAuthorised);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        goingFlag=getIntent().getStringExtra("goingFlag");
        if (goingFlag.equals("1")){
            llUnAuthorised.setVisibility(View.VISIBLE);
            imgHome.setVisibility(View.GONE);
        }else {
            llUnAuthorised.setVisibility(View.GONE);
            imgHome.setVisibility(View.VISIBLE);
        }
    }

    private void onClick(){

    }
}
