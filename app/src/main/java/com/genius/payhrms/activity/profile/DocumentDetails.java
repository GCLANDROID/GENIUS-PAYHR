package com.genius.payhrms.activity.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genius.payhrms.R;
import com.genius.payhrms.activity.activity.UserDashBoardActivity;
import com.genius.payhrms.activity.model.DocumentDetailsAdapter;
import com.genius.payhrms.activity.model.DocumentDetailsModel;
import com.genius.payhrms.activity.utility.NetworkConnectionCheck;
import com.genius.payhrms.activity.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DocumentDetails extends AppCompatActivity {
    ImageView imgBack, imgHome;
    TextView tvToolBar;
    int documentID;
    RecyclerView rvDocumentDetails;

    Pref pref;
    NetworkConnectionCheck connectionCheck;
    ArrayList<DocumentDetailsModel> itemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_details);
        pref = new Pref(getApplicationContext());

        rvDocumentDetails = (RecyclerView) findViewById(R.id.rvDocumentDetails);
        rvDocumentDetails.setLayoutManager(new LinearLayoutManager(this));

        initView();

        if (connectionCheck.isNetworkAvailable()) {

        } else {
            connectionCheck.getNetworkActiveAlert().show();
        }

        onclick();

    }


    private void initView() {
        pref = new Pref(DocumentDetails.this);

        connectionCheck = new NetworkConnectionCheck(this);

        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        tvToolBar = (TextView) findViewById(R.id.tvToolBar);
        rvDocumentDetails = (RecyclerView) findViewById(R.id.rvDocumentDetails);


        if (pref.getLanguage().equals("hi")) {

            tvToolBar.setText("दस्तावेज विवरणं");
        } else {
            tvToolBar.setText("DOCUMENT DETAILS");
        }

        documentID = getIntent().getIntExtra("documentID", 0);

        //documentDetails();
    }

    private void onclick() {

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DocumentDetails.this, UserDashBoardActivity.class);
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