package com.genius.payhrms.activity.geofence;

import static com.genius.payhrms.activity.utility.Util.SECRET_KEY;
import static com.genius.payhrms.activity.utility.Util.encrypt;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.genius.payhrms.activity.activity.UserDashBoardActivity;
import com.genius.payhrms.activity.adapter.SubordinateReportAdapter;
import com.genius.payhrms.activity.dailylog.NumberTourActivity;
import com.genius.payhrms.activity.model.SubordinateReportDetailsModel;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.Pref;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class SubordinateReportDetailsActivity extends AppCompatActivity {
    private static final String TAG = "SubordinateReportDetail";
     String year,month,date;
    String branchId,actionFlag;
    TextView tvToolBar;
    LinearLayout llLoader,llMain,llNoData;
    ArrayList<SubordinateReportDetailsModel>itemList=new ArrayList<>();
    RecyclerView rvItem;
    Pref pref;
    ImageView imgBack,imgHome;
    TextView tvDate,tvBranchName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subordinate_report_details);
        initView();
        onClick();
    }

    private void initView(){
        pref=new Pref(getApplicationContext());
       date=getIntent().getStringExtra("date").replaceAll("\\s+", "%20");;
       String showdate=getIntent().getStringExtra("date");
        int y = Calendar.getInstance().get(Calendar.YEAR);
        year = String.valueOf(y);
        Log.d("year", year);

        int m = Calendar.getInstance().get(Calendar.MONTH) + 1;
        Log.d("month", String.valueOf(m));
        if (m == 1) {
            month = "January";
        } else if (m == 2) {
            month = "February";
        } else if (m == 3) {
            month = "March";
        } else if (m == 4) {
            month = "April";
        } else if (m == 5) {
            month = "May";
        } else if (m == 6) {
            month = "June";
        } else if (m == 7) {
            month = "July";
        } else if (m == 8) {
            month = "August";
        } else if (m == 9) {
            month = "September";
        } else if (m == 10) {
            month = "October";
        } else if (m == 11) {
            month = "November";
        } else if (m == 12) {
            month = "December";
        }

        branchId=getIntent().getStringExtra("branchId");
        actionFlag=getIntent().getStringExtra("actionflag");
        tvToolBar=(TextView)findViewById(R.id.tvToolBar);
        if (actionFlag.equals("1")){
            if (pref.getLanguage().equals("hi")){
                 tvToolBar.setText("अंदर कर्मचारी ");
            }else {
                tvToolBar.setText("In Employee");
            }
        }else if (actionFlag.equals("2")){
            if (pref.getLanguage().equals("hi")){
                tvToolBar.setText("कर्मचारी बाहर");
            }else {
                tvToolBar.setText("Out Employee");
            }

        }else {
            if (pref.getLanguage().equals("hi")){
                tvToolBar.setText("चिअभी तक चिह्नित नहीं किया गया है");
            }else {
                tvToolBar.setText("Not Marked Employee");
            }
        }

        llNoData=(LinearLayout)findViewById(R.id.llNoData);
        llMain=(LinearLayout)findViewById(R.id.llMain);
        llLoader=(LinearLayout)findViewById(R.id.llLoader);
        rvItem=(RecyclerView)findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(SubordinateReportDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);
        //getItemList(year,month,date,actionFlag);
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("SDate",date);
            jsonObject.put("EDate",date);
            jsonObject.put("ApproverID",pref.getEmpId());
            jsonObject.put("BranchId",branchId);
            jsonObject.put("ActionFlag",actionFlag);
            jsonObject.put("Operation","2");
            jsonObject.put("SecurityCode",pref.getSecurityCode());
            getItemList2(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        tvDate=(TextView)findViewById(R.id.tvDate);
        tvBranchName=(TextView)findViewById(R.id.tvBranchName);
        tvDate.setText(showdate);

    }

    private void getItemList2(final JSONObject jsonObject) {
        Log.e(TAG, "getItemList2: "+jsonObject);

        AndroidNetworking.post(Api.sSelfAttendanceApprovalPending)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject job1 = response;
                        try {
                            int Response_Code = job1.optInt("Response_Code");
                            if (Response_Code == 101) {
                                String responseData = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(responseData);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String BranchName = obj.optString("BranchName");
                                    String EmpName=obj.optString("EmpName");
                                    String Address=obj.optString("Address");
                                    String FenceTime=obj.optString("FenceTime");
                                    String FenceType=obj.optString("FenceType");
                                    tvBranchName.setText(BranchName);

                                    SubordinateReportDetailsModel obj2 = new SubordinateReportDetailsModel(EmpName,Address,FenceTime,FenceType);
                                    itemList.add(obj2);
                                }
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNoData.setVisibility(View.GONE);
                                setAdapter();
                            } else {
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNoData.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorCode()==401){
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
                        } else {
                            llLoader.setVisibility(View.VISIBLE);
                            llMain.setVisibility(View.GONE);
                            llNoData.setVisibility(View.GONE);
                        }
                    }
                });
    }


    private void login(JSONObject jsonObject) {
        final ProgressDialog pd = new ProgressDialog((Context) SubordinateReportDetailsActivity.this);
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
                                    jsonObject.put("SDate",date);
                                    jsonObject.put("EDate",date);
                                    jsonObject.put("ApproverID",pref.getEmpId());
                                    jsonObject.put("BranchId",branchId);
                                    jsonObject.put("ActionFlag",actionFlag);
                                    jsonObject.put("Operation","2");
                                    jsonObject.put("SecurityCode",pref.getSecurityCode());
                                    getItemList2(jsonObject);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                    }
                });
    }






    private void setAdapter() {
        SubordinateReportAdapter nAdapter = new SubordinateReportAdapter(itemList,SubordinateReportDetailsActivity.this);
        rvItem.setAdapter(nAdapter);

    }

    private void onClick(){
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
