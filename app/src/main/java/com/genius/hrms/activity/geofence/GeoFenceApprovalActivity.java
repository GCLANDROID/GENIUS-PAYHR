package com.genius.hrms.activity.geofence;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
import com.genius.hrms.R;
import com.genius.hrms.activity.adapter.AttendanceApprovalAdapter;
import com.genius.hrms.activity.adapter.GeoFenceApprovalAdapter;
import com.genius.hrms.activity.model.AttendanceApprovalModel;
import com.genius.hrms.activity.model.GeoFenceApprovalModel;
import com.genius.hrms.activity.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GeoFenceApprovalActivity extends AppCompatActivity implements View.OnClickListener {


    RecyclerView rvItem;
    ArrayList<GeoFenceApprovalModel> itemList = new ArrayList<>();

    LinearLayout llNoData, llLoader, llMain;
    Pref pref;
    GeoFenceApprovalAdapter apprvalAdapter;
    ArrayList<Integer> aIDList = new ArrayList<>();
    String aid = "";

    Button btnReject, btnApprove;

    AlertDialog alerDialog1;
    LinearLayout llARLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_fence_approval);
        initView();
    }


    private void initView() {
        pref = new Pref(GeoFenceApprovalActivity.this);

        rvItem = (RecyclerView)findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(GeoFenceApprovalActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);
        llNoData = (LinearLayout)findViewById(R.id.llNoData);
        llLoader = (LinearLayout)findViewById(R.id.llLoader);
        llMain = (LinearLayout)findViewById(R.id.llMain);

        btnReject = (Button) findViewById(R.id.btnReject);
        btnApprove = (Button) findViewById(R.id.btnApprove);
        llARLayout=(LinearLayout)findViewById(R.id.llARLayout);

        btnReject.setOnClickListener(this);
        btnApprove.setOnClickListener(this);

        getItem();


    }


    private void getItem() {
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        String surl =  "https://cloud.geniusconsultant.com/GHRMSApi/api/EmpGeoFence/Get_GeoFenceApprovalPending?ApproverID="+pref.getEmpId()+"&SecurityCode="+pref.getSecurityCode();
        Log.d("teamUrl", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        itemList.clear();



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
                                    String EmpName = obj.optString("EmpName");
                                    String FName = obj.optString("FName");
                                    String Address = obj.optString("Address");
                                    String Latitude = obj.optString("Latitude");
                                    String Longitude = obj.optString("Longitude");
                                    int GID = obj.optInt("GID");

                                    GeoFenceApprovalModel approvalModel = new GeoFenceApprovalModel();
                                    approvalModel.setEmpName(EmpName);
                                    approvalModel.setAddress(Address);
                                    approvalModel.setLaat(Latitude);
                                    approvalModel.setLoong(Longitude);
                                    approvalModel.setgID(GID);
                                    approvalModel.setImageFile("https://cloud.geniusconsultant.com/GHRMSApi/MobileAttImage/"+FName);

                                    itemList.add(approvalModel);


                                }
                                setAdapter();
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNoData.setVisibility(View.GONE);

                            } else {
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNoData.setVisibility(View.VISIBLE);
                                //Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);
                llNoData.setVisibility(View.GONE);

                Toast.makeText(GeoFenceApprovalActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(GeoFenceApprovalActivity.this);
        requestQueue.add(stringRequest);

    }

    private void setAdapter() {
        apprvalAdapter = new GeoFenceApprovalAdapter(itemList,  this);
        rvItem.setAdapter(apprvalAdapter);
    }


    public void updateAttendanceStatus(int position, boolean status) {
        itemList.get(position).setSelected(status);
        if (itemList.get(position).isSelected() == true) {
            aIDList.add(itemList.get(position).getgID() );

        } else {
            aIDList.remove(position);
        }

        aid = aIDList.toString().replace("[", "").replace("]", "");
        Log.d("aid", aid);
        if (aIDList.size() > 0) {
            llARLayout.setVisibility(View.VISIBLE);
        } else {
            llARLayout.setVisibility(View.GONE);
        }


        apprvalAdapter.notifyDataSetChanged();
    }

    private void approveFunction() {
        final ProgressDialog pd = new ProgressDialog(GeoFenceApprovalActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.upload(pref.getIpAddress() + "GHRMSApi/API/EmpGeoFence/GeoFenceApproval")
                .addMultipartParameter("Radius", "100")
                .addMultipartParameter("StrAttData", aid)
                .addMultipartParameter("Approvedby", pref.getEmpId())
                .addMultipartParameter("ApproverStatus", "1")
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())

                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        pd.show();

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        pd.dismiss();
                        JSONObject job = response;
                        boolean responseStatus = job.optBoolean("responseStatus");
                        if (responseStatus) {
                            approveAlert();
                            llARLayout.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(GeoFenceApprovalActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        pd.dismiss();
                        Toast.makeText(GeoFenceApprovalActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();

                    }
                });

    }

    private void rejectFunction() {
        final ProgressDialog pd = new ProgressDialog(GeoFenceApprovalActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();

        AndroidNetworking.upload(pref.getIpAddress() + "GHRMSApi/API/EmpGeoFence/GeoFenceApproval")
                .addMultipartParameter("Radius", "100")
                .addMultipartParameter("StrAttData", aid)
                .addMultipartParameter("Approvedby", pref.getEmpId())
                .addMultipartParameter("ApproverStatus", "0")
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())

                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        pd.show();

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        pd.dismiss();
                        JSONObject job = response;
                        boolean responseStatus = job.optBoolean("responseStatus");
                        if (responseStatus) {
                            llARLayout.setVisibility(View.GONE);
                            rejectAlert();
                        } else {
                            Toast.makeText(GeoFenceApprovalActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        pd.dismiss();
                        Toast.makeText(GeoFenceApprovalActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();

                    }
                });

    }


    private void approveAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GeoFenceApprovalActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);

        tvInvalidDate.setText("Attendance approved successfully");


        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                getItem();
                aIDList.clear();
                aid="";

            }
        });

        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }


    private void rejectAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GeoFenceApprovalActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);

        tvInvalidDate.setText("Attendance rejected successfully");


        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                getItem();
                aIDList.clear();
                aid="";

            }
        });

        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }


    @Override
    public void onClick(View view) {
        if (view==btnApprove){
            approveFunction();
        }else if (view==btnReject){
            rejectFunction();
        }

    }
}