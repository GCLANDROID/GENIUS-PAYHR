package com.genius.hrms.activity.attendance;

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

import com.android.volley.DefaultRetryPolicy;
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
import com.genius.hrms.R;
import com.genius.hrms.activity.activity.UserDashBoardActivity;
import com.genius.hrms.activity.adapter.AttendanceRegulizationAdapter;
import com.genius.hrms.activity.adapter.BackLogAdapter;
import com.genius.hrms.activity.model.AttendanceRegulizationModel;
import com.genius.hrms.activity.model.BackLogModel;
import com.genius.hrms.activity.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AttendanceRegulizationActivity extends AppCompatActivity {
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_regulization);
        initView();
        getRegulizationData();
        onClick();
    }

    private void initView(){
        pref=new Pref(AttendanceRegulizationActivity.this);
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
                    regulizationSave();
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


    public void updateItemStatus(int position) {
        if (!itemList.get(position).getRemarks().equals("")){
            regulizationItem.add(itemList.get(position).getDate() + "_" + itemList.get(position).getInTime() + "_" + itemList.get(position).getOutTime() + "_" + itemList.get(position).getRemarks());
        }
        String itemcomp = regulizationItem.toString();
        regulizationDetails=itemcomp.replace("[","").replace("]","");

        Log.d("aripitem", regulizationDetails);


    }

    public void regulizationSave() {
        etFocus.clearFocus();
        Log.d("regulizationDetails",regulizationDetails);

        final ProgressDialog pg=new ProgressDialog(AttendanceRegulizationActivity.this);
        pg.setMessage("Loading..");
        pg.setCancelable(false);
        AndroidNetworking.upload( pref.getIpAddress() + "GHRMSApi/api/Attendance/AttendanceBakLogSave")
                .addMultipartParameter("CompanyID", pref.getEmpClintId())
                .addMultipartParameter("StrAttData", regulizationDetails)
                .addMultipartParameter("EmployeeID", pref.getEmpId())
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())
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
                        String responseText=job.optString("responseText");
                        boolean responseStatus = job.optBoolean("responseStatus");
                        if (responseStatus) {

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
            tvInvalidDate.setText(" Attendance saved successfully");
        }


        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
               getRegulizationData();
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
