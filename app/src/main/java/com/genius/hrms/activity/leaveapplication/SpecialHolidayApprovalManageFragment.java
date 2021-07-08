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
import com.genius.hrms.activity.adapter.SpecialHolidayAppovalItemAdapter;
import com.genius.hrms.activity.model.SpecialHolidayApprovalItemModel;
import com.genius.hrms.activity.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpecialHolidayApprovalManageFragment extends Fragment {
    View view;
    Pref pref;
    LinearLayout llStrtDate,llEndDate;
    String startDate,endDate;
    TextView tvStrtDate,tvEndDate;
    Button btnShow;
    ArrayList<SpecialHolidayApprovalItemModel> itemList=new ArrayList<>();
    LinearLayout llNoData,llLoader,llMain;
    AlertDialog alerDialog1;
    RecyclerView rvItem;
    SpecialHolidayAppovalItemAdapter spAdapter;
    Button btnSubmit;
    AlertDialog al1;
    TextView tvDate,tvRemarks,tvName;
    Button btnApprove,btnReject;
    ArrayList<String>mIdList=new ArrayList<>();
    String mId="";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_approval_manage, container, false);
        initView();
        onClick();
        return view;
    }

    private void initView(){
        pref=new Pref(getContext());
        llStrtDate=(LinearLayout)view.findViewById(R.id.llStrtDate);
        llEndDate=(LinearLayout)view.findViewById(R.id.llEndDate);

        tvStrtDate=(TextView) view.findViewById(R.id.tvStrtDate);
        tvEndDate=(TextView) view.findViewById(R.id.tvEndDate);
        tvDate=(TextView) view.findViewById(R.id.tvDate);
        tvRemarks=(TextView) view.findViewById(R.id.tvRemarks);
        tvName=(TextView)view.findViewById(R.id.tvName);

        btnShow=(Button) view.findViewById(R.id.btnShow);
        rvItem=(RecyclerView)view.findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);
        llNoData=(LinearLayout)view.findViewById(R.id.llNoData);
        llLoader=(LinearLayout)view.findViewById(R.id.llLoader);
        llMain=(LinearLayout)view.findViewById(R.id.llMain);
        btnSubmit=(Button)view.findViewById(R.id.btnSubmit);
        btnApprove=(Button)view.findViewById(R.id.btnApprove);
        btnReject=(Button)view.findViewById(R.id.btnReject);

        if (pref.getLanguage().equals("hi")){
            tvStrtDate.setText("कृपया प्रारंभ दिन चुनें");
            tvEndDate.setText("कृपया अंतिम दिन चुनें");
            tvDate.setText("दिनांक");
            tvRemarks.setText("टिप्पणियों");
            tvName.setText("स्थिति");
            btnShow.setText("नाम");
        }else {
            tvStrtDate.setText("Please select start day");
            tvEndDate.setText("Please select end day");
            tvDate.setText("Date");
            tvRemarks.setText("Remarks");
            tvName.setText("Name");
            btnShow.setText("Show");
        }
    }

    private void getItem(){
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        String surl = pref.getIpAddress()+"ghrmsapi/api/HolidayMap/ExceptionHolidayApprovalView?CompanyID="+pref.getEmpClintId()+"&StartDate="+startDate+"&EndDate="+endDate+"&ApproverID="+pref.getEmpId()+"&SecurityCode="+pref.getSecurityCode();
        Log.d("inputReportforSp", surl);
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
                                    String ExDate = obj.optString("ExDate");
                                    String REMARKS = obj.optString("REMARKS");
                                    String AID = obj.optString("AID");
                                    String EmpName=obj.optString("EmpName");


                                    SpecialHolidayApprovalItemModel obj2 = new SpecialHolidayApprovalItemModel(AID,EmpName,ExDate,REMARKS);
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                90000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }
    private void setAdapter(){
        spAdapter=new SpecialHolidayAppovalItemAdapter(itemList, SpecialHolidayApprovalManageFragment.this,getContext());
        rvItem.setAdapter(spAdapter);
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
        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIdList.size()>0){
                    approveFunction();
                }else {
                    Toast.makeText(getContext(),"please select item",Toast.LENGTH_LONG).show();
                }
            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIdList.size()>0){
                    rejectFunction();
                }else {
                    Toast.makeText(getContext(),"please select item",Toast.LENGTH_LONG).show();
                }
            }
        });



    }
    public void updateStatus(int position, boolean status) {
        itemList.get(position).setSelected(status);
        if (itemList.get(position).isSelected() == true) {
            mIdList.add(itemList.get(position).getaId() );


        } else {
            mIdList.remove(position);
        }


        mId = mIdList.toString().replace("[", "").replace("]", "").replaceAll("\\s+", "");
        Log.d("detailslist", mId);
        /*Log.d("arpan", itemList.toString());
        String i = itemList.toString();
        String d = i.replace("[", "").replace("]", "");
        empId = d.replaceAll("\\s+", "");
        String emp=empName.toString();
        String replace=emp.replace("[", "").replace("]", "");
        tvEmpName.setText(replace);
*/

        spAdapter.notifyDataSetChanged();
    }

    private void approveFunction() {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading..");
        pd.setCancelable(false);


        AndroidNetworking.upload(pref.getIpAddress()+"ghrmsapi/api/HolidayMap/ExceptionHolidayApprovalSave")
                .addMultipartParameter("CompanyID", pref.getEmpClintId())
                .addMultipartParameter("StrAID", mId)
                .addMultipartParameter("ApproverID", pref.getEmpId())
                .addMultipartParameter("ApprovalStatus", "1")
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
                        } else {
                            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e("errot",error.toString());
                        pd.dismiss();
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();

                    }
                });

    }

    private void rejectFunction() {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();

        AndroidNetworking.upload(pref.getIpAddress()+"ghrmsapi/api/HolidayMap/ExceptionHolidayApprovalSave")
                .addMultipartParameter("CompanyID", pref.getEmpClintId())
                .addMultipartParameter("StrAID", mId)
                .addMultipartParameter("ApproverID", pref.getEmpId())
                .addMultipartParameter("ApprovalStatus", "0")
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
                            rejectAlert();
                        } else {
                            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        pd.dismiss();
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();

                    }
                });

    }

    private void approveAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);

        tvInvalidDate.setText("Leave approved successfully");



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

    private void rejectAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);

        tvInvalidDate.setText("Leave rejected successfully");



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
