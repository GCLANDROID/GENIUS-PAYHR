package com.genius.payhrms.activity.leaveapplication;

import static com.genius.payhrms.activity.utility.Util.SECRET_KEY;
import static com.genius.payhrms.activity.utility.Util.encrypt;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.genius.payhrms.activity.dailylog.DailyLogReportActivity;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LeaveDashboardActivity extends AppCompatActivity {
    private static final String TAG = "LeaveDashboardActivity";
    LinearLayout llLeaveApplication,llLeaveBalance;
    ImageView imgBack,imgHome;
    TextView tvLeave,tvLeaveBalance,tvToolBar;
    Pref pref;
    LinearLayout lnNonAccess,lnAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_dashboard);
        initview();
        onClick();
    }

    private void onClick() {
        llLeaveApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LeaveDashboardActivity.this, LeaveApplicationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        llLeaveBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LeaveDashboardActivity.this, LeaveBalanceActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent=new Intent(LeaveDashboardActivity.this, UserDashBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //finish();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void initview() {
        lnNonAccess=(LinearLayout)findViewById(R.id.lnNonAccess);
        lnAccess=(LinearLayout)findViewById(R.id.lnAccess);
        llLeaveApplication=findViewById(R.id.llLeaveApplication);
        llLeaveBalance=findViewById(R.id.llLeaveBalance);
        imgBack=findViewById(R.id.imgBack);
        imgHome=findViewById(R.id.imgHome);
        tvLeave=findViewById(R.id.tvLeave);
        tvLeaveBalance=findViewById(R.id.tvLeaveBalance);
        tvToolBar=findViewById(R.id.tvToolBar);
        pref = new Pref(getApplicationContext());
        if (pref.getLanguage().equals("hi")) {
            tvLeaveBalance.setText("बकाया छुट्टियां");
            tvLeave.setText("छुट्टी की अर्जी");
            tvToolBar.setText("डैशबोर्ड छोड़ें");
        }
        //accessChecking();
        JSONObject object=new JSONObject();
        try {
            object.put("CompanyID",pref.getEmpClintId());
            //object.put("MenuItemName","Leave Application");
            object.put("MenuItemName","Leave");
            object.put("SecurityCode",pref.getSecurityCode());
            accessChecking2(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //CompanyID="+pref.getEmpClintId()+"&MenuItemName=Leave&SecurityCode="+pref.getSecurityCode();
    }

    private void accessChecking2(JSONObject object) {
        Log.e(TAG, "accessChecking2: "+object);
       /* String surl = pref.getIpAddress() + "ghrmsapi/api/MenuOnOff/Get?CompanyID="+pref.getEmpClintId()+"&MenuItemName=Leave&SecurityCode="+pref.getSecurityCode();
        Log.d("salaryinput",surl);*/
        final ProgressDialog pd=new ProgressDialog(LeaveDashboardActivity.this);
        pd.setMessage("Loading");
        pd.show();
        pd.setCancelable(false);
        //http://171.16.2.67/GHRMSApi_V2_DevMode/api/MenuAccess/GetMenuOnOff
        //Api.sGetMenuOnOff
        AndroidNetworking.post(Api.sGetMenuOnOff)
                .addJSONObjectBody(object)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "GET_MENU_ON_OFF: "+response.toString());
                        pd.dismiss();
                        JSONObject job1 = response;
                        int Response_Code = job1.optInt("Response_Code");
                        String Response_Message = job1.optString("Response_Message");
                        if (Response_Code == 101) {
                            JSONObject menuObj = job1.optJSONObject("Response_Data");
                            if (menuObj.optBoolean("MenuStatus")){
                                lnAccess.setVisibility(View.GONE);
                                lnNonAccess.setVisibility(View.VISIBLE);
                            } else {
                                lnAccess.setVisibility(View.VISIBLE);
                                lnNonAccess.setVisibility(View.GONE);
                            }
                        } else {
                            lnAccess.setVisibility(View.GONE);
                            lnNonAccess.setVisibility(View.VISIBLE);
                            Toast.makeText(LeaveDashboardActivity.this, Response_Message, Toast.LENGTH_SHORT).show();
                            //Log.e(TAG, "ERROR: "+job1.optJSONObject("Response_Data"));
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "GET_MENU_ON_OFF_onError: "+anError);
                        pd.dismiss();
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
                            lnAccess.setVisibility(View.VISIBLE);
                            lnNonAccess.setVisibility(View.GONE);
                        }
                    }
                });
    }


    private void login(JSONObject jsonObject) {
        final ProgressDialog pd = new ProgressDialog((Context) LeaveDashboardActivity.this);
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

                                JSONObject object=new JSONObject();
                                try {
                                    object.put("CompanyID",pref.getEmpClintId());
                                    object.put("MenuItemName","Leave Application");
                                    object.put("SecurityCode",pref.getSecurityCode());
                                    accessChecking2(object);
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
}