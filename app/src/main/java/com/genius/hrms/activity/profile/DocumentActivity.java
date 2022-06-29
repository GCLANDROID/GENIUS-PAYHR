package com.genius.hrms.activity.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.genius.hrms.R;
import com.genius.hrms.activity.activity.UserDashBoardActivity;
import com.genius.hrms.activity.profile.ProfileActivity;
import com.genius.hrms.activity.utility.NetworkConnectionCheck;
import com.genius.hrms.activity.utility.Pref;

public class DocumentActivity extends AppCompatActivity {

    ImageView imgBack,imgHome;
    TextView tvToolBar;
    Pref pref;
    NetworkConnectionCheck connectionCheck;
    String surl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        initialize();

        if (connectionCheck.isNetworkAvailable()) {

        } else {
            connectionCheck.getNetworkActiveAlert().show();
        }

        onclick();
    }

    private void initialize()
    {
        pref = new Pref(DocumentActivity.this);
        connectionCheck = new NetworkConnectionCheck(this);

        imgBack =(ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        tvToolBar = (TextView) findViewById(R.id.tvToolBar);


        if (pref.getLanguage().equals("hi")) {

            tvToolBar.setText("दस्तावेजों");
        }
        else
        {
            tvToolBar.setText("DOCUMENTS");
        }
    }
    private  void onclick()
    {

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DocumentActivity.this, UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
}