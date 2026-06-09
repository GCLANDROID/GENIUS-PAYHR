package com.genius.payhrms.activity.attendance.cflo.tour;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.genius.payhrms.activity.attendance.tour.TourActivity;
import com.genius.payhrms.activity.leaveapplication.OtherLeavesActivity;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.Pref;
import com.genius.payhrms.activity.utility.Util;
import com.genius.payhrms.databinding.ActivityTourApplicationBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TourApplicationActivity extends AppCompatActivity {
    ActivityTourApplicationBinding binding;
    Pref pref;
    int adjustmentID;
    ArrayList<String>travelType=new ArrayList<>();
    String startDate="",endDate="";
    String selectedtravelType="";
    AlertDialog alerDialog1,al1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_tour_application);
        initView();
    }

    private void initView(){
        pref=new Pref(TourApplicationActivity.this);
        JSONObject object=new JSONObject();
        try {
            object.put("EmployeeID",pref.getEmpId());
            object.put("CompanyID",pref.getEmpClintId());
            object.put("SecurityCode",pref.getSecurityCode());
            getTourApplicationCount(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        travelType.add("Domestic");
        travelType.add("International");
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (TourApplicationActivity.this, android.R.layout.simple_spinner_item,
                        travelType); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spType.setAdapter(spinnerArrayAdapter);

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
        binding.spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedtravelType=travelType.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startDate.equals("")){
                    Toast.makeText(TourApplicationActivity.this, "Please select start date", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (endDate.equals("")){
                    Toast.makeText(TourApplicationActivity.this, "Please select end date", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (binding.etDestination.getText().toString().length()<=0){
                    Toast.makeText(TourApplicationActivity.this, "Please enter destination", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (selectedtravelType.equals("")){
                    Toast.makeText(TourApplicationActivity.this, "Please select travel type", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (binding.etPurpouse.getText().toString().length()<=0){
                    Toast.makeText(TourApplicationActivity.this, "Please enter purpose", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isEndDateLessThanStartDate(startDate, endDate)) {
                    Toast.makeText(TourApplicationActivity.this, "End Date cannot be less than Start Date", Toast.LENGTH_SHORT).show();
                    return;
                }
                JSONObject object1=new JSONObject();
                try {
                    object1.put("CompanyID",pref.getEmpClintId());
                    object1.put("EmployeeID",pref.getEmpId());
                    object1.put("TourStartDate",startDate);
                    object1.put("TourEndDate",endDate);
                    object1.put("Destination",binding.etDestination.getText().toString());
                    object1.put("AccomodationType","");
                    object1.put("TravelType",selectedtravelType);
                    object1.put("AdvanceAmount","");
                    object1.put("Purpose",binding.etPurpouse.getText().toString());
                    object1.put("TourDetails","");
                    object1.put("CreatedBy",pref.getEmpId());
                    object1.put("AID","");
                    object1.put("SecurityCode",pref.getSecurityCode());
                    postTourApplication(object1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        binding.imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TourApplicationActivity.this, EmployeeDashBoardActivity.class);
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

        final ProgressDialog pd=new ProgressDialog(TourApplicationActivity.this);
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
                                    String AdjustmentType=jsonObject1.optString("AdjustmentType");
                                    if (AdjustmentType.equals("TOUR")) {
                                        int TotalApplication = jsonObject1.optInt("TotalApplication");
                                        binding.tvTourApplicationCount.setText(""+TotalApplication);
                                        int ApproveApplication = jsonObject1.optInt("ApproveApplication");
                                        binding.tvTourApproverCount.setText(""+ApproveApplication);
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


        DatePickerDialog datePickerDialog = new DatePickerDialog(TourApplicationActivity.this,
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


        DatePickerDialog datePickerDialog = new DatePickerDialog(TourApplicationActivity.this,
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
    private void postTourApplication(JSONObject object) {

        final ProgressDialog pd=new ProgressDialog(TourApplicationActivity.this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(Api.CFLOSaveTour)
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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TourApplicationActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);


        tvInvalidDate.setText("Tour application has been successfully applied");



        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                Intent intent=new Intent(TourApplicationActivity.this, OtherApplicationDetailsActivity.class);
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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TourApplicationActivity.this, R.style.CustomDialogNew);
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