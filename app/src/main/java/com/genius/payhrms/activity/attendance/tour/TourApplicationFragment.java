package com.genius.payhrms.activity.attendance.tour;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.genius.payhrms.R;
import com.genius.payhrms.activity.leaveapplication.LeaveApplicationActivity;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.Pref;
import com.genius.payhrms.activity.utility.Util;
import com.genius.payhrms.databinding.FragmentTourApplicationBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class TourApplicationFragment extends Fragment {
    FragmentTourApplicationBinding binding;
    Pref pref;
    AlertDialog alerDialog1;
    String startDate="";
    String endDate="";
    int strtDate,enddate;
    AlertDialog al1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTourApplicationBinding.inflate(getLayoutInflater());
        initView();
        return binding.getRoot();
    }

    private void initView(){
        pref=new Pref(getContext());
        binding.tvStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStrtDatePicker();
            }
        });
        binding.tvEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEndDatePicker();
            }
        });
        binding.tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!startDate.equals("")){
                    if (!endDate.equals("")){
                        if (binding.etRemarks.getText().toString().length()>0){
                            JSONObject jsonObject=new JSONObject();
                            try {
                                jsonObject.put("SecurityCode",pref.getSecurityCode());
                                jsonObject.put("Companyid",pref.getEmpClintId());
                                jsonObject.put("Employeeid",pref.getEmpId());
                                jsonObject.put("StartDate",startDate);
                                jsonObject.put("EndDate",endDate);
                                jsonObject.put("Destination","");
                                jsonObject.put("AccomodationType","");
                                jsonObject.put("TravelType","");
                                jsonObject.put("AdvanceAmount","0.00");
                                jsonObject.put("Purpose",binding.etRemarks.getText().toString());
                                jsonObject.put("CreatedBy",pref.getEmpId());
                                jsonObject.put("AID","");
                                TourSave(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            Toast.makeText(getContext(),"Please enter Purpouse of Tour",Toast.LENGTH_LONG).show();
                        }

                    }else {
                        Toast.makeText(getContext(),"Please select End Date of Tour",Toast.LENGTH_LONG).show();
                    }

                }else {
                    Toast.makeText(getContext(),"Please select Start Date of Tour",Toast.LENGTH_LONG).show();
                }



            }
        });



    }


    private void TourSave(JSONObject object) {
        final ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        AndroidNetworking.post(Api.sSaveTourApplication)
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
                        if (Response_Code.equals("101") ) {
                            successAlert();
                        } else {
                            Toast.makeText(getContext(),Response_Message,Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();

                    }
                });
    }


    private void successAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);


            tvInvalidDate.setText("Tour application has been successfully applied");



            Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alerDialog1.dismiss();
                    ((TourActivity) getContext()).loadTourApplicationViewFragment();


                }
            });

            alerDialog1 = dialogBuilder.create();
            alerDialog1.setCancelable(true);
            Window window = alerDialog1.getWindow();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);
            alerDialog1.show();
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


                        strtDate = dayOfMonth + monthOfYear + year;
                        int month = (monthOfYear + 1);
                        startDate = month + "/" + dayOfMonth + "/" + year;
                        binding.tvStartDate.setText(Util.changeAnyDateFormat(startDate,"MM/dd/yyyy","dd MMM yyyy"));

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker();
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


                        enddate = dayOfMonth + monthOfYear + year;
                        int month = (monthOfYear + 1);
                        endDate = month + "/" + dayOfMonth + "/" + year;
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                        Date strDate = null;
                        try {
                            strDate = sdf.parse(startDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                        Date striDate = null;
                        try {
                            striDate = df.parse(endDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (striDate.getTime() > strDate.getTime() ||striDate.getTime() == strDate.getTime()) {
                            binding.tvEndDate.setText(Util.changeAnyDateFormat(endDate,"MM/dd/yyyy","dd MMM yyyy"));
                        }else {
                            showErrorDialog("End date should not before than Start date");
                        }


                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker();
        datePickerDialog.show();

    }


    private void showErrorDialog(String text) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.error_ayput, null);
        dialogBuilder.setView(dialogView);
        TextView tvError = (TextView) dialogView.findViewById(R.id.tvError);
        tvError.setText(text);
        ImageView imgCancel = (ImageView) dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                al1.dismiss();
                binding.tvEndDate.setText("");
                endDate="";
            }
        });

        al1 = dialogBuilder.create();
        al1.setCancelable(false);
        Window window = al1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        al1.show();
    }



}