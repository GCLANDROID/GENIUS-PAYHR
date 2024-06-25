package com.genius.payhrms.activity.activity;

import static com.genius.payhrms.activity.utility.Util.SECRET_KEY;
import static com.genius.payhrms.activity.utility.Util.encrypt;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.genius.payhrms.R;
import com.genius.payhrms.activity.adapter.ELearningAdapter;
import com.genius.payhrms.activity.model.ELearningModel;
import com.genius.payhrms.activity.model.HoliDayModel;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.Pref;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ELearningActivity extends AppCompatActivity {
    TextView tvToolBar;
    Pref pref;
    LinearLayout llLoader,llNoData,llMain;
    RecyclerView rvItem;
    ArrayList<ELearningModel>itemList=new ArrayList<>();
    ImageView imgBack,imgHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elearning);
        initialize();
       // elearningItemget();
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("ManualCategoryID",0);
            jsonObject.put("ManualSubCategoryID",0);
            jsonObject.put("FunctionID",0);
            jsonObject.put("FinancialYear",0);
            jsonObject.put("SecurityCode",pref.getSecurityCode());
            elearning(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        onClick();
    }
    private void initialize(){
        pref=new Pref(getApplicationContext());
        tvToolBar=(TextView)findViewById(R.id.tvToolBar);
        tvToolBar.setText("E-LEARNING / PRIVACY POLICY");
        llLoader=(LinearLayout)findViewById(R.id.llLoader);
        llMain=(LinearLayout)findViewById(R.id.llMain);
        llNoData=(LinearLayout)findViewById(R.id.llNoData);
        rvItem = (RecyclerView) findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(ELearningActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);

    }



    private void elearning(JSONObject jsonObject) {
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        AndroidNetworking.post(Api.sElearningapi)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {


                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);


                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                            String responseData= job1.optString("Response_Data");
                            try {
                                JSONArray jsonArray = new JSONArray(responseData);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.optJSONObject(i);
                                    String FileLabel=obj.optString("FileLabel");
                                    String ImageUrl=obj.optString("ImageUrl");

                                    ELearningModel mModel = new ELearningModel(FileLabel,ImageUrl);
                                    itemList.add(mModel);
                                    ELearningAdapter cAdapter = new ELearningAdapter(itemList,ELearningActivity.this);
                                    rvItem.setAdapter(cAdapter);


                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            llLoader.setVisibility(View.GONE);
                            llMain.setVisibility(View.VISIBLE);
                            llNoData.setVisibility(View.GONE);

                            // boolean _status = job1.getBoolean("status");
                            // do anything with response
                        }else {
                            llLoader.setVisibility(View.GONE);
                            llMain.setVisibility(View.GONE);
                            llNoData.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        if (error.getErrorCode()==401){
                            JSONObject obj=new JSONObject();
                            try {
                                obj.put("MasterID",encrypt(pref.getMasterId(),SECRET_KEY));
                                obj.put("Password",encrypt(pref.getPassword(),SECRET_KEY));
                                obj.put("IMEI","0");
                                obj.put("DeviceID","0");
                                obj.put("DeviceType","A");
                                obj.put("SecurityCode",pref.getSecurityCode());
                                login(obj);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    private void login(JSONObject jsonObject) {
        final ProgressDialog pd = new ProgressDialog(ELearningActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(Api.sLoginapi)
                .addJSONObjectBody(jsonObject)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        pd.dismiss();

                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                            JSONArray responseData = job1.optJSONArray("Response_Data");
                            for (int i = 0; i < responseData.length(); i++) {
                                JSONObject obj = responseData.optJSONObject(i);
                                String Genius_Access_Token=obj.optString("Genius_Access_Token");
                                pref.saveAccessToken(Genius_Access_Token);

                                // boolean _status = job1.getBoolean("status");

                                JSONObject jsonObject=new JSONObject();
                                try {
                                    jsonObject.put("ManualCategoryID",0);
                                    jsonObject.put("ManualSubCategoryID",0);
                                    jsonObject.put("FunctionID",0);
                                    jsonObject.put("FinancialYear",0);
                                    jsonObject.put("SecurityCode",pref.getSecurityCode());
                                    elearning(jsonObject);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                // do anything with response
                            }
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                    }
                });
    }


    private void showAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Slow or No Internet connection");
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();

                    }
                });
        alertDialogBuilder.show();


    }

    private void onClick(){
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
