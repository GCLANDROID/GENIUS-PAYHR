package com.genius.payhrms.activity.leaveapplication;

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
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.genius.payhrms.R;
import com.genius.payhrms.activity.activity.UserDashBoardActivity;
import com.genius.payhrms.activity.adapter.LeaveBalanceAdapter;
import com.genius.payhrms.activity.model.HoliDayModel;
import com.genius.payhrms.activity.model.LeaveBalanceModel;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.NetworkConnectionCheck;
import com.genius.payhrms.activity.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class LeaveBalanceActivity extends AppCompatActivity {
    RecyclerView rvHoliday;
    ArrayList<HoliDayModel> holidayList=new ArrayList<>();
    ImageView imgBack,imgHome;
    LinearLayout llLoder;
    String year;
    int y;
    Pref pref;
    NetworkConnectionCheck connectionCheck;
    LinearLayout llAgain,llMain,llNodata;
    ImageView imgAgain;
    TextView tvToolBar;
    ArrayList<String> typeAvaild = new ArrayList<>();
    String applicantId;
    ArrayList<LeaveBalanceModel> itemList = new ArrayList<>();
    String typeAvailable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_balance);
        initialize();
        onClick();
        //getApproverOrNot();

        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("CompanyID",pref.getEmpClintId());
            jsonObject.put("EmployeeID",pref.getEmpId());
            jsonObject.put("ApproverID",pref.getEmpId());
            jsonObject.put("SecurityCode",pref.getSecurityCode());
            getLeaveAllDetails(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void initialize(){
        pref=new Pref(LeaveBalanceActivity.this);
        connectionCheck=new NetworkConnectionCheck(LeaveBalanceActivity.this);
        rvHoliday=(RecyclerView)findViewById(R.id.rvHoliday);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(LeaveBalanceActivity.this, LinearLayoutManager.VERTICAL, false);
        rvHoliday.setLayoutManager(layoutManager);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        llLoder=(LinearLayout)findViewById(R.id.llWLLoader) ;
        llMain=(LinearLayout)findViewById(R.id.llMain);
        llNodata=(LinearLayout)findViewById(R.id.llNodata);
        llAgain=(LinearLayout)findViewById(R.id.llAgain);
        imgAgain=(ImageView)findViewById(R.id.imgAgain);
        y= Calendar.getInstance().get(Calendar.YEAR);
        year=String.valueOf(y);
        tvToolBar=(TextView)findViewById(R.id.tvToolBar);
        if (pref.getLanguage().equals("hi")){
            tvToolBar.setText("बकाया छुट्टियां");
        }else {
            tvToolBar.setText("Leave Balance");
        }


    }

    private void onClick(){
        imgAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getLeaveAllDetails();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LeaveBalanceActivity.this, UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private  void setAdapter(){
        LeaveBalanceAdapter lAdaapter = new LeaveBalanceAdapter(itemList, getApplicationContext());
        rvHoliday.setAdapter(lAdaapter);
    }

    public void getLeaveAllDetails(JSONObject jsonObject) {
        llLoder.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);

        AndroidNetworking.post(Api.sLeaveDetails)
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
                        String Response_Message=job1.optString("Response_Message");
                        if (Response_Code == 101) {


                            String responseData = job1.optString("Response_Data");
                            try {
                                JSONObject jsonArray=new JSONObject(responseData);
                                String Table1=jsonArray.optString("Table1");
                                JSONArray leaveBalanceArray = new JSONArray(Table1);
                                for (int i = 0; i < leaveBalanceArray.length(); i++) {
                                    JSONObject balanceObject = leaveBalanceArray.optJSONObject(i);
                                    final String Code = balanceObject.optString("LeaveTypeName");
                                    final String Opening = balanceObject.optString("Opening");
                                    final String LeaveAvailed = balanceObject.optString("LeaveAvailed");
                                    final String Avaliable = balanceObject.optString("Avaliable");
                                    String LeaveTypeID = balanceObject.optString("LeaveTypeID");
                                    // typeAvaild.add(LeaveTypeID + "_" + Avaliable);

                                    LeaveBalanceModel model = new LeaveBalanceModel(Code, Opening, LeaveAvailed,Avaliable);
                                    itemList.add(model);
                                }
                                //typeAvailable = typeAvaild.toString().replace("]", "").replace("[", "").replaceAll("\\s+", "");
                                ;
                                //Log.d("availd", typeAvaild.toString());
                                llMain.setVisibility(View.VISIBLE);
                                llNodata.setVisibility(View.GONE);
                                llLoder.setVisibility(View.GONE);
                                setAdapter();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            // boolean _status = job1.getBoolean("status");
                            // do anything with response
                        }else {
                            llLoder.setVisibility(View.GONE);
                            llMain.setVisibility(View.GONE);
                            llNodata.setVisibility(View.VISIBLE);

                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        llLoder.setVisibility(View.GONE);
                        llMain.setVisibility(View.GONE);
                        llNodata.setVisibility(View.VISIBLE);
                    }
                });
    }

}