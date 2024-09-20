package com.genius.payhrms.activity.attendance.tour;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.genius.payhrms.R;
import com.genius.payhrms.activity.activity.ELearningActivity;
import com.genius.payhrms.activity.activity.LoginActivity;
import com.genius.payhrms.activity.adapter.TourViewAdapter;
import com.genius.payhrms.activity.attendance.AttendanceCalenderDashboardActivity;
import com.genius.payhrms.activity.model.SpinnerModel;
import com.genius.payhrms.activity.model.TourViewModel;
import com.genius.payhrms.activity.payroll.SalaryActivity;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.App;
import com.genius.payhrms.activity.utility.Pref;
import com.genius.payhrms.databinding.FragmentTourApplicationBinding;
import com.genius.payhrms.databinding.FragmentTourViewBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;


public class TourViewFragment extends Fragment {

    FragmentTourViewBinding binding;
    Pref pref;
    ArrayList<TourViewModel>itemList=new ArrayList<>();
    ArrayList<String>yearList=new ArrayList<>();
    ArrayList<String>monthList=new ArrayList<>();
    ArrayList<SpinnerModel>mmonthList=new ArrayList<>();
    String Month;
    String yearid,monthid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTourViewBinding.inflate(getLayoutInflater());
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
                    jsonObject.put("BranchId","0");
                    jsonObject.put("DepartmentId","0");
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
        AndroidNetworking.post(Api.sGetTourApplicationView)
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
                                        String AppliedOn = tourOBJ.optString("AppliedOn");
                                        String ApprovalStatus = tourOBJ.optString("ApprovalStatus");
                                        TourViewModel tourViewModel = new TourViewModel();
                                        tourViewModel.setApprovalStatus(ApprovalStatus);
                                        tourViewModel.setAppliedOn(AppliedOn);
                                        tourViewModel.setReason(Reason);
                                        tourViewModel.setEndDate(EndDate);
                                        tourViewModel.setStartDate(StartDate);
                                        itemList.add(tourViewModel);

                                    }
                                    TourViewAdapter tourViewAdapter = new TourViewAdapter(getContext(), itemList);
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
}