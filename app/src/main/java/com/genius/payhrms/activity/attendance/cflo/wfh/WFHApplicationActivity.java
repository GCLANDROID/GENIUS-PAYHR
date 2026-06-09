package com.genius.payhrms.activity.attendance.cflo.wfh;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.genius.payhrms.R;
import com.genius.payhrms.activity.activity.EmployeeDashBoardActivity;
import com.genius.payhrms.activity.attendance.AttendanceCalenderDashboardActivity;
import com.genius.payhrms.activity.attendance.cflo.otherapplicationdetails.OtherApplicationDetailsActivity;
import com.genius.payhrms.activity.attendance.cflo.tour.TourApplicationActivity;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.Pref;
import com.genius.payhrms.activity.utility.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WFHApplicationActivity extends AppCompatActivity {
    com.genius.payhrms.databinding.ActivityWfhapplicationBinding binding;
    Pref pref;
    int adjustmentID;
    String startDate="",endDate="";
    AlertDialog alerDialog1,al1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_wfhapplication);
        initView();
    }

    private void initView(){
        pref=new Pref(WFHApplicationActivity.this);
        JSONObject object=new JSONObject();
        try {
            object.put("EmployeeID",pref.getEmpId());
            object.put("CompanyID",pref.getEmpClintId());
            object.put("SecurityCode",pref.getSecurityCode());
            getTourApplicationCount(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        binding.llStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStrtDatePicker();
            }
        });
        binding.llEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEndDatePicker();
            }
        });

        binding.tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startDate.equals("")){
                    Toast.makeText(WFHApplicationActivity.this, "Please select start date", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (endDate.equals("")){
                    Toast.makeText(WFHApplicationActivity.this, "Please select end date", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (binding.etWorkPlace.getText().toString().length()<=0){
                    Toast.makeText(WFHApplicationActivity.this, "Please enter workplace", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (binding.etPurpouse.getText().toString().length()<=0){
                    Toast.makeText(WFHApplicationActivity.this, "Please enter purpose", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isEndDateLessThanStartDate(startDate, endDate)) {
                    Toast.makeText(WFHApplicationActivity.this, "End Date cannot be less than Start Date", Toast.LENGTH_SHORT).show();
                    return;
                }
                JSONObject object1=new JSONObject();
                try {
                    object1.put("CompanyID",pref.getEmpClintId());
                    object1.put("EmployeeID",pref.getEmpId());
                    object1.put("StartDate",startDate);
                    object1.put("EndDate",endDate);
                    object1.put("WorkPlace",binding.etWorkPlace.getText().toString());
                    object1.put("Purpose",binding.etPurpouse.getText().toString());
                    object1.put("Accessories","");
                    object1.put("CreatedBy",pref.getEmpId());
                    object1.put("AID","");
                    object1.put("SecurityCode",pref.getSecurityCode());
                    postApplication(object1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        binding.imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(WFHApplicationActivity.this, EmployeeDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void getTourApplicationCount(JSONObject object) {

        final ProgressDialog pd=new ProgressDialog(WFHApplicationActivity.this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(Api.CFLOApplicationCount)
                .addJSONObjectBody(object)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        /*mApplicantList.clear();
                        applicantList.clear();
                        applicantList.add("Please select");
                        mApplicantList.add(new SpinnerModel("0", "0"));*/

                        pd.dismiss();
                        JSONObject job1 = response;
                        Log.d("AdjustmentDashboard", "LEAVE_APPROVER: " + job1);

                        String Response_Code = job1.optString("Response_Code");
                        if (Response_Code .equals("101") ) {
                            // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                            String responseData = job1.optString("Response_Data");
                            try {
                                JSONObject jsonObject=new JSONObject(responseData);
                                JSONArray Table= jsonObject.optJSONArray("Table");
                                for (int i=0;i<Table.length();i++){
                                    JSONObject jsonObject1=Table.optJSONObject(i);
                                    String AdjustmentType=jsonObject1.optString("AdjustmentType").trim();
                                    if (AdjustmentType.equals("WORK FROM HOME")) {
                                        int TotalApplication = jsonObject1.optInt("TotalApplication");
                                        binding.tvApplicationCount.setText(""+TotalApplication);
                                        int ApproveApplication = jsonObject1.optInt("ApproveApplication");
                                        binding.tvApprovedCount.setText(""+ApproveApplication);
                                        adjustmentID= jsonObject1.optInt("AdjustmentTypeID");
                                    }

                                    JSONArray Table1= jsonObject.optJSONArray("Table1");
                                    JSONObject frstOBJ= Table1.optJSONObject(0);
                                    String ApproverName= frstOBJ.optString("ApproverName");
                                    binding.tvApprovername.setText("Approver: "+ApproverName);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();

                    }
                });
    }

    private void showStrtDatePicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(WFHApplicationActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {



                        int month = (monthOfYear + 1);
                        startDate = year + "-" + month + "-" + dayOfMonth;
                        binding.tvStartDate.setText(Util.changeAnyDateFormat(startDate,"yyyy-MM-dd","dd MMM, yyyy"));

                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.show();

    }
    private void showEndDatePicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(WFHApplicationActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {



                        int month = (monthOfYear + 1);
                        endDate =  year + "-" + month + "-" + dayOfMonth;
                        binding.tvEndDate.setText(Util.changeAnyDateFormat(endDate,"yyyy-MM-dd","dd MMM, yyyy"));

                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.show();

    }
    private void postApplication(JSONObject object) {

        final ProgressDialog pd=new ProgressDialog(WFHApplicationActivity.this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(Api.CFLOSaveWFH)
                .addJSONObjectBody(object)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        /*mApplicantList.clear();
                        applicantList.clear();
                        applicantList.add("Please select");
                        mApplicantList.add(new SpinnerModel("0", "0"));*/

                        pd.dismiss();
                        JSONObject job1 = response;
                        Log.d("AdjustmentDashboard", "LEAVE_APPROVER: " + job1);

                        String Response_Code = job1.optString("Response_Code");
                        String Response_Message=job1.optString("Response_Message");
                        if (Response_Code .equals("101") ) {
                            successAlert(Response_Message);


                        }else {
                            showErrorDialog(Response_Message);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();

                    }
                });
    }

    private boolean isEndDateLessThanStartDate(String startDateStr, String endDateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try {
            Date startDate = sdf.parse(startDateStr);
            Date endDate = sdf.parse(endDateStr);

            return endDate.before(startDate);

        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void successAlert(String text) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(WFHApplicationActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);


        tvInvalidDate.setText(text);



        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                Intent intent=new Intent(WFHApplicationActivity.this, OtherApplicationDetailsActivity.class);
                startActivity(intent);
                finish();



            }
        });

        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }
    private void showErrorDialog(String text) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(WFHApplicationActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.error_ayput, null);
        dialogBuilder.setView(dialogView);
        TextView tvError = (TextView) dialogView.findViewById(R.id.tvError);
        tvError.setText(text);
        ImageView imgCancel = (ImageView) dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                al1.dismiss();

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