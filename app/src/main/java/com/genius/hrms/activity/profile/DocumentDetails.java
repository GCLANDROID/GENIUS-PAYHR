package com.genius.hrms.activity.profile;

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
import com.genius.hrms.R;
import com.genius.hrms.activity.activity.UserDashBoardActivity;
import com.genius.hrms.activity.adapter.DocumentListAdapter;
import com.genius.hrms.activity.model.DocumentDetailsAdapter;
import com.genius.hrms.activity.model.DocumentDetailsModel;
import com.genius.hrms.activity.model.DocumentListModel;
import com.genius.hrms.activity.utility.NetworkConnectionCheck;
import com.genius.hrms.activity.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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

        documentDetails();
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

    private void documentDetails() {
        final ProgressDialog pd = new ProgressDialog(DocumentDetails.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();

       String surl = "https://cloud.geniusconsultant.com/PayHR_Demo_V2/WebApiForMobileApp/api/Documents/GetDocuments?DocumentIndexId="+documentID+"&AEMClientID=" + pref.getEmpClintId()+"&AEMEmployeeID=" + pref.getEmpId() +"&FinancialYear=0&SecurityCode=" + pref.getSecurityCode();
        Log.d("documentdetails", surl);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        pd.dismiss();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {

                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.optJSONObject(i);

                                    String ManualCategory = obj.optString("ManualCategory");
                                    String ManualSubCategory = obj.optString("ManualSubCategory");
                                    String ManualDescription = obj.optString("ManualDescription");
                                    String FilePath=obj.optString("FilePath");

                                    DocumentDetailsModel myModel = new DocumentDetailsModel(ManualCategory,ManualSubCategory, ManualDescription);
                                    myModel.setDocumentPath(FilePath);
                                    itemList.add(myModel);

                                }
                                DocumentDetailsAdapter reportAdapter = new DocumentDetailsAdapter(itemList,DocumentDetails.this);
                                rvDocumentDetails.setAdapter(reportAdapter);

                            } else {

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DocumentDetails.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(DocumentDetails.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(DocumentDetails.this);
        requestQueue.add(stringRequest);
    }
}