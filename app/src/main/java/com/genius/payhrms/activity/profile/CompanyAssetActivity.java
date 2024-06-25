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
import android.widget.LinearLayout;
import android.widget.Toast;

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
import com.genius.payhrms.activity.activity.UserDashBoardActivity;
import com.genius.payhrms.activity.adapter.CompanyAssetAdapter;
import com.genius.payhrms.activity.model.CompanyAssetModel;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CompanyAssetActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView rvItem;
    ArrayList<CompanyAssetModel>itemList=new ArrayList<>();
    Pref pref;
    LinearLayout lnMain,lnNodata;
    ImageView imgBack,imgHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_asset);
        initView();
    }

    private void initView(){
        pref=new Pref(CompanyAssetActivity.this);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        lnNodata=(LinearLayout)findViewById(R.id.lnNodata);
        lnMain=(LinearLayout)findViewById(R.id.lnMain);
        rvItem=(RecyclerView) findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(CompanyAssetActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("AEMEmployeeId",pref.getEmpId());
            jsonObject.put("SecurityCode",pref.getSecurityCode());
            getAssetList(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        imgHome.setOnClickListener(this);
        imgBack.setOnClickListener(this);
    }



    public void getAssetList(JSONObject jsonObject) {
        final ProgressDialog pd=new ProgressDialog(CompanyAssetActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        lnMain.setVisibility(View.VISIBLE);
        lnNodata.setVisibility(View.GONE);

        AndroidNetworking.post(Api.sCompanyAssetapi)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        itemList.clear();
                        pd.dismiss();

                        JSONObject job1 = response;
                        pd.dismiss();
                        Log.e("response12", "@@@@@@" + job1);


                        int Response_Code = job1.optInt("Response_Code");
                        String Response_Message=job1.optString("Response_Message");
                        if (Response_Code == 101) {
                            String responseData = job1.optString("Response_Data");
                            try {
                                JSONArray jsonArray=new JSONArray(responseData);
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject assetOBJ=jsonArray.optJSONObject(i);
                                    String assetName=assetOBJ.optString("Asset Name");
                                    String assignedDate=assetOBJ.optString("Assigned Date");
                                    String realesedDate=assetOBJ.optString("Released Date");
                                    CompanyAssetModel assetModel=new CompanyAssetModel();
                                    assetModel.setAssetName(assetName);
                                    assetModel.setAssignedDate(assignedDate);
                                    assetModel.setRelaseDate(realesedDate);
                                    itemList.add(assetModel);
                                }
                                if (responseData.length()>0) {
                                    lnMain.setVisibility(View.VISIBLE);
                                    lnNodata.setVisibility(View.GONE);
                                    setAdapter();
                                }else {
                                    lnMain.setVisibility(View.GONE);
                                    lnNodata.setVisibility(View.VISIBLE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }




                            // boolean _status = job1.getBoolean("status");


                            // do anything with response
                        }else {

                            lnMain.setVisibility(View.GONE);
                            lnNodata.setVisibility(View.VISIBLE);

                        }
                    }

                    @Override
                    public void onError(ANError error) {

                        pd.dismiss();
                        lnMain.setVisibility(View.GONE);
                        lnNodata.setVisibility(View.VISIBLE);


                    }
                });
    }

    private void setAdapter() {
        CompanyAssetAdapter assetAdapter = new CompanyAssetAdapter(itemList);
        rvItem.setAdapter(assetAdapter);
    }

    @Override
    public void onClick(View view) {
        if (view==imgBack){
            onBackPressed();
        }else if (view==imgHome){
            Intent intent=new Intent(CompanyAssetActivity.this, UserDashBoardActivity.class);
            startActivity(intent);
            finish();
        }

    }
}