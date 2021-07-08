package com.genius.hrms.activity.leaveapplication;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.genius.hrms.activity.adapter.LeaveDetailsAdapter;
import com.genius.hrms.activity.model.LeaveDetailsModel;
import com.genius.hrms.activity.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {


    View v;

    RecyclerView rvItem;
    ArrayList<LeaveDetailsModel> itemList=new ArrayList<>();
    LinearLayout llStrtDate,llEndDate;
    TextView tvStrtDate,tvEndDate;
    String startDate="",endDate="";
    Button btnShow;
    LinearLayout llNoData,llLoader,llMain;
    Pref pref;
    AlertDialog alerDialog1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_details, container, false);
        initView();
        onClick();

        return v;
    }

    private void initView(){
        pref=new Pref(getContext());
        rvItem=(RecyclerView)v.findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);
        llEndDate=(LinearLayout)v.findViewById(R.id.llEndDate);
        llStrtDate=(LinearLayout)v.findViewById(R.id.llStrtDate);
        llNoData=(LinearLayout)v.findViewById(R.id.llNoData);
        llLoader=(LinearLayout)v.findViewById(R.id.llLoader);
        llMain=(LinearLayout)v.findViewById(R.id.llMain);

        tvStrtDate=(TextView)v.findViewById(R.id.tvStrtDate);
        tvEndDate=(TextView)v.findViewById(R.id.tvEndDate);
        btnShow=(Button)v.findViewById(R.id.btnShow);
        if (pref.getLanguage().equals("hi")){
            tvStrtDate.setText("अपनी छुट्टी की आरंभ तिथि चुनें");
            tvEndDate.setText("अपनी छुट्टी की समाप्ति तिथि चुनें");
            btnShow.setText("प्रदर्शन");
        }else {
            tvStrtDate.setText("Select your leave start date");
            tvEndDate.setText("Select your leave end date");
            btnShow.setText("Show");
        }
    }

    private void onClick(){
        llStrtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStrtDatePicker();
            }
        });
        llEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEndDatePicker();
            }
        });
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!startDate.equals("")) {
                    if (!endDate.equals("")) {
                        getItem();
                    }else {
                        Toast.makeText(getContext(),"Please select End Date",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getContext(),"please select Start Date",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    private void getItem(){
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        String surl = pref.getIpAddress()+"ghrmsapi/api/Leave/LeaveApplicationDeatilsForApplicant?CompanyID="+pref.getEmpClintId()+"&EmployeeID="+pref.getEmpId()+"&StartDate="+startDate+"&EndDate="+endDate+"&SecurityCode="+pref.getSecurityCode();
        Log.d("inputLeaveBlanace", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        itemList.clear();

                        // attendabceInfiList.clear();

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
                                    String ApplicationMID = obj.optString("ApplicationMID");
                                    String LeaveName = obj.optString("LeaveName");
                                    String LeaveSDate = obj.optString("LeaveSDate");
                                    String LeaveEDate = obj.optString("LeaveEDate");
                                    String LeaveValue = obj.optString("LeaveValue");
                                    String Reason = obj.optString("Reason");
                                    String ApprovalRemarks = obj.optString("ApprovalRemarks");
                                    String ApprovalStatus = obj.optString("ApprovalStatus");
                                    String ApprovedDate = obj.optString("ApprovedDate");
                                    String ApprovedBY = obj.optString("ApprovedBY");


                                    LeaveDetailsModel obj2 = new LeaveDetailsModel(ApplicationMID,LeaveName,LeaveSDate,LeaveEDate,LeaveValue,Reason,ApprovalStatus,ApprovedDate,ApprovedBY,ApprovalRemarks);
                                    itemList.add(obj2);


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

                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }
    private void setAdapter(){
        LeaveDetailsAdapter detailsAdpater=new LeaveDetailsAdapter(itemList,getContext(),DetailsFragment.this);
        rvItem.setAdapter(detailsAdpater);
    }


    private void showStrtDatePicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {



                        int month = (monthOfYear + 1);
                        startDate = month + "/" + dayOfMonth + "/" + year;
                        tvStrtDate.setText(startDate);

                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.show();

    }


    private void showEndDatePicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {



                        int month = (monthOfYear + 1);
                        endDate = month + "/" + dayOfMonth + "/" + year;
                        tvEndDate.setText(endDate);

                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.show();

    }

    public void leaveDelete(String mid) {
       final ProgressDialog pg=new ProgressDialog(getContext());
        pg.setMessage("Loading..");
        pg.setCancelable(false);
        AndroidNetworking.upload(pref.getIpAddress()+"ghrmsapi/api/Leave/LeaveAppDelete")
                .addMultipartParameter("CompanyID", pref.getEmpClintId())
                .addMultipartParameter("ApplicationMID", mid)
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
                        boolean responseStatus = job.optBoolean("responseStatus");
                        if (responseStatus) {
                            successAlert();
                        } else {

                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        pg.dismiss();
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();

                    }
                });
    }


    private void successAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);
        if (pref.getLanguage().equals("hi")) {
            tvInvalidDate.setText("सफलतापूर्वक हटा दिया गया");
        } else {

            tvInvalidDate.setText("Leave delete successfully");
        }


        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                getItem();


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
