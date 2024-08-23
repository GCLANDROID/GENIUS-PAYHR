package com.genius.payhrms.activity.attendance.tour;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.genius.payhrms.R;
import com.genius.payhrms.activity.activity.LoginActivity;
import com.genius.payhrms.activity.adapter.TourApprovalAdapter;
import com.genius.payhrms.activity.adapter.TourViewAdapter;
import com.genius.payhrms.activity.model.SpinnerModel;
import com.genius.payhrms.activity.model.TourApprovalModel;
import com.genius.payhrms.activity.model.TourViewModel;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.Pref;
import com.genius.payhrms.databinding.FragmentTourApprovalBinding;
import com.genius.payhrms.databinding.FragmentTourViewBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;


public class TourApprovalFragment extends Fragment {
    FragmentTourApprovalBinding binding;
    Pref pref;
    ArrayList<TourApprovalModel> itemList=new ArrayList<>();
    ArrayList<String>yearList=new ArrayList<>();
    ArrayList<String>monthList=new ArrayList<>();
    ArrayList<SpinnerModel>mmonthList=new ArrayList<>();
    String Month;
    String yearid,monthid;
    AlertDialog alerDialog1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTourApprovalBinding.inflate(getLayoutInflater());
        initView();
        return binding.getRoot();
    }


    private void initView(){
        pref=new Pref(getContext());
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.rvItem.setLayoutManager(layoutManager);
        yearList.add("2024-2025");
        yearList.add("2025-2026");
        yearList.add("2026-2027");

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_spinner_item,
                        yearList); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spYear.setAdapter(spinnerArrayAdapter);

        Calendar calendar = Calendar.getInstance();
        int month=calendar.get(Calendar.MONTH)+1;
        if (month==1){
            Month="January";
        }else if (month==2){
            Month="February";
        }else if (month==3){
            Month="March";
        }else if (month==4){
            Month="April";
        }else if (month==5){
            Month="May";
        }else if (month==6){
            Month="June";
        }else if (month==7){
            Month="July";
        }else if (month==8){
            Month="August";
        }else if (month==9){
            Month="September";
        }else if (month==10){
            Month="October";
        }else if (month==11){
            Month="November";
        }else if (month==12){
            Month="December";
        }
        monthList.add("All");
        monthList.add("January");
        monthList.add("February");
        monthList.add("March");
        monthList.add("April");
        monthList.add("May");
        monthList.add("June");
        monthList.add("July");
        monthList.add("August");
        monthList.add("September");
        monthList.add("October");
        monthList.add("November");
        monthList.add("December");
        mmonthList.add(new SpinnerModel("January",""));
        mmonthList.add(new SpinnerModel("January","1"));
        mmonthList.add(new SpinnerModel("January","2"));
        mmonthList.add(new SpinnerModel("January","3"));
        mmonthList.add(new SpinnerModel("January","4"));
        mmonthList.add(new SpinnerModel("January","5"));
        mmonthList.add(new SpinnerModel("January","6"));
        mmonthList.add(new SpinnerModel("January","7"));
        mmonthList.add(new SpinnerModel("January","8"));
        mmonthList.add(new SpinnerModel("January","9"));
        mmonthList.add(new SpinnerModel("January","10"));
        mmonthList.add(new SpinnerModel("January","11"));
        mmonthList.add(new SpinnerModel("January","12"));


        ArrayAdapter<String> spinnerMonthArrayAdapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_spinner_item,
                        monthList); //selected item will look like a spinner set from XML
        spinnerMonthArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spMonth.setAdapter(spinnerMonthArrayAdapter);
        int index = monthList.indexOf(Month);
        binding.spMonth.setSelection(index);

        binding.spYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                yearid=yearList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.spMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                monthid=mmonthList.get(i).getItemId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.put("SecurityCode",pref.getSecurityCode());
                    jsonObject.put("Companyid",pref.getEmpClintId());
                    jsonObject.put("Employeeid",pref.getEmpId());
                    jsonObject.put("MonthId",monthid);
                    jsonObject.put("Fyear",yearid);
                    jsonObject.put("YearID",20);
                    jsonObject.put("ViewType","2");

                    TourView(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });




    }


    private void TourView(JSONObject object) {
        final ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        binding.llMain.setVisibility(View.VISIBLE);
        binding.llNoData.setVisibility(View.GONE);
        AndroidNetworking.post(Api.sGetApprovalView)
                .addJSONObjectBody(object)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        itemList=new ArrayList<>();
                        JSONObject job1 = response;
                        String Response_Code = job1.optString("Response_Code");
                        String Response_Message = job1.optString("Response_Message");
                        if (Response_Code.equals("101")){
                            String Response_Data=job1.optString("Response_Data");
                            try {
                                JSONObject ResponseData=new JSONObject(Response_Data);
                                String Table=ResponseData.optString("Table");
                                JSONArray TableArray=new JSONArray(Table);
                                if (TableArray.length()>0) {
                                    binding.llNoData.setVisibility(View.GONE);
                                    binding.llMain.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < TableArray.length(); i++) {
                                        JSONObject tourOBJ = TableArray.optJSONObject(i);
                                        String StartDate = tourOBJ.optString("StartDate");
                                        String EndDate = tourOBJ.optString("EndDate");
                                        String Reason = tourOBJ.optString("Reason");
                                        String EmpCode = tourOBJ.optString("EmpCode");
                                        String EmpName = tourOBJ.optString("EmpName");
                                        int AID=tourOBJ.optInt("AID");
                                        int TourMasterAID=tourOBJ.optInt("TourMasterAID");
                                        String ApprovalStatus = tourOBJ.optString("ApprovalStatus");
                                        TourApprovalModel tourViewModel = new TourApprovalModel();
                                        tourViewModel.setApprovalStatus(ApprovalStatus);
                                        tourViewModel.setEmpCode(EmpCode);
                                        tourViewModel.setReason(Reason);
                                        tourViewModel.setEndDate(EndDate);
                                        tourViewModel.setStartDate(StartDate);
                                        tourViewModel.setAID(AID);
                                        tourViewModel.setEmpName(EmpName);
                                        tourViewModel.setTourMasterAID(TourMasterAID);
                                        itemList.add(tourViewModel);

                                    }
                                    TourApprovalAdapter tourViewAdapter = new TourApprovalAdapter(getContext(), itemList,TourApprovalFragment.this);
                                    binding.rvItem.setAdapter(tourViewAdapter);
                                }else {
                                    binding.llNoData.setVisibility(View.VISIBLE);
                                    binding.llMain.setVisibility(View.GONE);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Intent intent=new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                });
    }


    public void TourApproval(int AID,int TourID,String approvaltype) {


        JSONObject object=new JSONObject();
        try {

            object.put("SecurityCode",pref.getSecurityCode());
            object.put("CompanyID",pref.getEmpClintId());
            object.put("AID",AID);
            object.put("TourId",TourID);
            object.put("ApprovalType",approvaltype);
            object.put("CreatedBy",pref.getEmpId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        AndroidNetworking.post(Api.sSaveTourApprove)
                .addJSONObjectBody(object)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();

                        JSONObject job1 = response;
                        String Response_Code = job1.optString("Response_Code");
                        String Response_Message = job1.optString("Response_Message");
                        if (Response_Code.equals("101")){
                            if (approvaltype.equals("1")){
                                successalert("Tour application has been approved successfully");
                            }else {
                                successalert("Tour application has been rejected successfully");
                            }


                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Intent intent=new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                });
    }


    private void successalert(String m) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);

        tvInvalidDate.setText(m);



        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.put("SecurityCode",pref.getSecurityCode());
                    jsonObject.put("Companyid",pref.getEmpClintId());
                    jsonObject.put("Employeeid",pref.getEmpId());
                    jsonObject.put("MonthId",monthid);
                    jsonObject.put("Fyear",yearid);
                    jsonObject.put("YearID",20);
                    jsonObject.put("ViewType","2");

                    TourView(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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