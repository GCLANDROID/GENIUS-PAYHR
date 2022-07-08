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
import android.widget.LinearLayout;
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
import com.genius.hrms.activity.model.DocumentListModel;
import com.genius.hrms.activity.utility.NetworkConnectionCheck;
import com.genius.hrms.activity.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DocumentActivity extends AppCompatActivity {

    ImageView imgBack,imgHome;
    TextView tvToolBar;
    Pref pref;
    NetworkConnectionCheck connectionCheck;
    String surl;

    RecyclerView rvDocumentList;
    ArrayList<DocumentListModel> itemList = new ArrayList<DocumentListModel>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        pref=new Pref(getApplicationContext());
        rvDocumentList = (RecyclerView) findViewById(R.id.rvDocumentList);
        rvDocumentList.setLayoutManager(new LinearLayoutManager(this));

        initialize();



        if (connectionCheck.isNetworkAvailable()) {

        } else {
            connectionCheck.getNetworkActiveAlert().show();
        }

        onclick();

        getDocumentList();

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

    private void getDocumentList() {
        final ProgressDialog pd = new ProgressDialog(DocumentActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
         surl = "https://cloud.geniusconsultant.com/PayHR_Demo_V2/WebApiForMobileApp/api/Documents/GetDocuments?DocumentIndexId=0&AEMClientID=" + pref.getEmpClintId()+"&AEMEmployeeID=" + pref.getEmpId() +"&FinancialYear="+pref.getDate()+"&SecurityCode=" + pref.getSecurityCode();
        Log.d("documentlist", surl);

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
                                    int HRManualID = obj.optInt("HRManualID");
                                    pref.saveHRManualID(HRManualID);
                                    String ManualDescription = obj.optString("ManualDescription");


                                    DocumentListModel myModel = new DocumentListModel(HRManualID,ManualDescription);
                                    itemList.add(myModel);

                                }
                                DocumentListAdapter reportAdapter = new DocumentListAdapter(itemList,DocumentActivity.this);
                                rvDocumentList.setAdapter(reportAdapter);

                            } else {

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DocumentActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(DocumentActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(DocumentActivity.this);
        requestQueue.add(stringRequest);
    }
}