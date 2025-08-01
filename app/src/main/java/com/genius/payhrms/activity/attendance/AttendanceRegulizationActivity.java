package com.genius.payhrms.activity.attendance;

import static com.genius.payhrms.activity.utility.Util.SECRET_KEY;
import static com.genius.payhrms.activity.utility.Util.encrypt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.androidnetworking.interfaces.UploadProgressListener;
import com.genius.payhrms.R;
import com.genius.payhrms.activity.activity.UserDashBoardActivity;
import com.genius.payhrms.activity.adapter.AttendanceRegulizationAdapter;
import com.genius.payhrms.activity.model.BackLogModel;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.Pref;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AttendanceRegulizationActivity extends AppCompatActivity {
    private static final String TAG = "AttendanceRegulizationA";
    ImageView imgBack,imgHome;
    TextView tvToolBar;
    LinearLayout llLoader,llMain,llNodata;
    Pref pref;
    ArrayList<BackLogModel> itemList = new ArrayList<>();
    ArrayList<String> regulizationItem = new ArrayList<>();
    AttendanceRegulizationAdapter regulizationAdapter;
    LinearLayout btnSubmit;
    String regulizationDetails;
    RecyclerView rvItem;
    AlertDialog alerDialog1;
    EditText etFocus;
    TextView tvNote;
    FloatingActionButton fbUp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_regulization);
        initView();
        Log.e(TAG, "onCreate: ================ called");
        //getRegulizationData();

        JSONObject object=new JSONObject();
        try {

            object.put("AEMEmployeeID",pref.getEmpId());
            object.put("CompanyID",pref.getEmpClintId());
            object.put("SecurityCode",pref.getSecurityCode());
            //object.put("SecurityCode","3000");
            attendanceregularization(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        onClick();
    }

    private void initView(){
        pref=new Pref(AttendanceRegulizationActivity.this);
        fbUp=(FloatingActionButton)findViewById(R.id.fbUp);
        tvNote=(TextView)findViewById(R.id.tvNote);
        if (pref.getSecurityCode().equals("1153")){
            tvNote.setVisibility(View.GONE);
        }else {
            tvNote.setVisibility(View.VISIBLE);
        }
        rvItem=(RecyclerView)findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(AttendanceRegulizationActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);

        llMain=(LinearLayout)findViewById(R.id.llMain);
        llLoader=(LinearLayout)findViewById(R.id.llLoader);
        llNodata=(LinearLayout)findViewById(R.id.llNodata);

        btnSubmit=(LinearLayout)findViewById(R.id.btnSubmit);
        etFocus=(EditText)findViewById(R.id.etFocus);

        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        fbUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rvItem.smoothScrollToPosition(0);
            }
        });
    }

    private void onClick(){
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etFocus.requestFocus();
            }
        });

        etFocus.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    if (regulizationItem.size()>0){
                    //regulizationSave();
                        JSONObject jsonObject=new JSONObject();
                        try {
                            jsonObject.put("CompanyID",pref.getEmpClintId());
                            jsonObject.put("StrAttData",regulizationDetails);
                            jsonObject.put("EmployeeID",pref.getEmpId());
                            //jsonObject.put("SecurityCode","3000");
                            jsonObject.put("SecurityCode",pref.getSecurityCode());
                            regulizationSave(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(),"please select item",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AttendanceRegulizationActivity.this, UserDashBoardActivity.class);
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


    private void getRegulizationData() {
        String surl =  pref.getIpAddress()+"GHRMSApi/api/Attendance/AttendanceBakLog?CompanyID=" + pref.getEmpClintId() + "&EmployeeID=" + pref.getEmpId() + "&SecurityCode=" + pref.getSecurityCode();
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);
        itemList.clear();
        Log.d("attendanceregulURL",surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("blockActivityData", response);

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String AttDate = obj.optString("AttDate");
                                    String InTime = obj.optString("InTime");
                                    String OutTime = obj.optString("OutTime");


                                    BackLogModel blockModule = new BackLogModel(AttDate, InTime, OutTime);
                                    itemList.add(blockModule);

                                }
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNodata.setVisibility(View.GONE);
                                regulizationAdapter = new AttendanceRegulizationAdapter(itemList, AttendanceRegulizationActivity.this);
                                rvItem.setAdapter(regulizationAdapter);
                            } else {

                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNodata.setVisibility(View.VISIBLE);
                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            //  Toast.makeText(AboutUsActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);
                llNodata.setVisibility(View.GONE);
                Toast.makeText(AttendanceRegulizationActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(AttendanceRegulizationActivity.this);
        requestQueue.add(stringRequest);


    }

    private void attendanceregularization(JSONObject jsonObject) {
        Log.e(TAG, "attendanceregularization: called");
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);
        itemList.clear();
        AndroidNetworking.post(Api.sAttendanceRegularizationapi)
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
                            // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                            JSONArray responseData = job1.optJSONArray("Response_Data");
                            for (int i = 0; i < responseData.length(); i++) {
                                JSONObject obj = responseData.optJSONObject(i);
                                String AttDate = obj.optString("AttDate");
                                String InTime = obj.optString("InTime");
                                String OutTime = obj.optString("OutTime");


                                BackLogModel blockModule = new BackLogModel(AttDate, InTime, OutTime);
                                itemList.add(blockModule);


                            }
                            llLoader.setVisibility(View.GONE);
                            llMain.setVisibility(View.VISIBLE);
                            llNodata.setVisibility(View.GONE);
                            regulizationAdapter = new AttendanceRegulizationAdapter(itemList, AttendanceRegulizationActivity.this);
                            rvItem.setAdapter(regulizationAdapter);


                            // boolean _status = job1.getBoolean("status");


                            // do anything with response
                        }else {

                            llLoader.setVisibility(View.GONE);
                            llMain.setVisibility(View.GONE);
                            llNodata.setVisibility(View.VISIBLE);

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
        final ProgressDialog pd = new ProgressDialog(AttendanceRegulizationActivity.this);
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

                                    object.put("AEMEmployeeID",pref.getEmpId());
                                    object.put("CompanyID",pref.getEmpClintId());
                                    object.put("SecurityCode",pref.getSecurityCode());
                                    attendanceregularization(object);
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


    public void updateItemStatus(int position) {
        if (!itemList.get(position).getRemarks().equals("")){
            regulizationItem.add(itemList.get(position).getDate() + "_" + itemList.get(position).getInTime() + "_" + itemList.get(position).getOutTime() + "_" + itemList.get(position).getRemarks());
        }
        String itemcomp = regulizationItem.toString();
        regulizationDetails=itemcomp.replace("[","").replace("]","");

        Log.d("aripitem", regulizationDetails);


    }



    public void regulizationSave(JSONObject jsonObject) {
        etFocus.clearFocus();
        Log.d("regulizationDetails",regulizationDetails);

        final ProgressDialog pg=new ProgressDialog(AttendanceRegulizationActivity.this);
        pg.setMessage("Loading..");
        pg.setCancelable(false);
        AndroidNetworking.post( Api.sAttendanceRegularizationsaveapi)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        pg.show();

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        pg.dismiss();
                        JSONObject job = response;
                        String responseText=job.optString("Response_Message");
                        int Response_Code = job.optInt("Response_Code");
                        if (Response_Code == 101) {

                            successAlert();
                        } else {
                            Toast.makeText(AttendanceRegulizationActivity.this,responseText,Toast.LENGTH_LONG).show();
                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e("error",error.toString());
                        pg.dismiss();
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();

                    }
                });
    }


    private void successAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AttendanceRegulizationActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);
        if (pref.getLanguage().equals("hi")) {
            tvInvalidDate.setText(" उपस्थिति सफलतापूर्वक सहेजी गई");
        } else {
            tvInvalidDate.setText("Attendance has been saved successfully");
        }


        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
              // getRegulizationData();

                JSONObject object=new JSONObject();

                try {

                    object.put("AEMEmployeeID",pref.getEmpId());
                    object.put("CompanyID",pref.getEmpClintId());
                    object.put("SecurityCode",pref.getSecurityCode());
                    attendanceregularization(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                regulizationItem.clear();
                regulizationDetails="";
            }
        });

        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }
}
