package com.genius.payhrms.activity.attendance.cflo.otherapplicationdetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.genius.payhrms.R;
import com.genius.payhrms.activity.activity.EmployeeDashBoardActivity;
import com.genius.payhrms.activity.attendance.cflo.adapter.ApplicationApprovalAdapter;
import com.genius.payhrms.activity.attendance.cflo.adapter.ApplicationDetailsAdapter;
import com.genius.payhrms.activity.attendance.cflo.model.ApplicationDetailsModel;
import com.genius.payhrms.activity.attendance.cflo.model.ApproverDetailsModel;
import com.genius.payhrms.activity.attendance.cflo.tour.TourApplicationActivity;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.Pref;
import com.genius.payhrms.activity.utility.ValidUtils;
import com.genius.payhrms.databinding.ActivityOtherApplicationApprovalBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OtherApplicationApprovalActivity extends AppCompatActivity {
    ActivityOtherApplicationApprovalBinding binding;
    Pref pref;
    ArrayList<ApproverDetailsModel>itemList=new ArrayList<>();
    AlertDialog alerDialog1,al1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_other_application_approval);
        initView();
    }

    private void initView(){
        pref=new Pref(OtherApplicationApprovalActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.rvItem.setLayoutManager(linearLayoutManager);
        JSONObject object=new JSONObject();
        try {
            object.put("ApproverID",pref.getEmpId());
            object.put("CompanyID",pref.getEmpClintId());
            object.put("SecurityCode",pref.getSecurityCode());
            getAllApplicationDetails(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        binding.imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(OtherApplicationApprovalActivity.this, EmployeeDashBoardActivity.class);
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


    private void getAllApplicationDetails(JSONObject object) {

        binding.llLoader.setVisibility(View.VISIBLE);
        binding.llMain.setVisibility(View.GONE);
        binding.llNoData.setVisibility(View.GONE);
        AndroidNetworking.post(Api.CFLOApplicationApprovalDetails)
                .addJSONObjectBody(object)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        itemList.clear();



                        JSONObject job1 = response;
                        Log.d("AdjustmentDashboard", "LEAVE_APPROVER: " + job1);

                        String Response_Code = job1.optString("Response_Code");
                        if (Response_Code .equals("101") ) {
                            binding.llLoader.setVisibility(View.GONE);
                            binding.llMain.setVisibility(View.VISIBLE);
                            binding.llNoData.setVisibility(View.GONE);
                            String Response_Data=job1.optString("Response_Data");
                            try {
                                JSONObject responseData=new JSONObject(Response_Data);
                                JSONArray Table=responseData.optJSONArray("Table");
                                for (int i=0;i<Table.length();i++){
                                    JSONObject obj=Table.getJSONObject(i);
                                    String Name=obj.optString("Name");
                                    String EmpCode=obj.optString("EmpCode");
                                    String AdjustmentType=obj.optString("AdjustmentType");
                                    String AppliedDate=obj.optString("AppliedDate");
                                    String StartDate=obj.optString("StartDate");
                                    String EndDate=obj.optString("EndDate");
                                    String NoOfDays=obj.optString("NoOfDays");
                                    String Reason=obj.optString("Reason");
                                    String Destination=obj.optString("Destination");
                                    String ApprovalStatus=ValidUtils.getFreshValue(obj.optString("ApprovalStatus"),"-");
                                    String AID=obj.optString("AID");
                                    ApproverDetailsModel model=new ApproverDetailsModel();
                                    model.setApplicant(Name+" ("+EmpCode+")");
                                    model.setApplicationType(AdjustmentType);
                                    model.setApplicationDate(AppliedDate);
                                    model.setStartDate(StartDate);
                                    model.setEndDate(EndDate);
                                    model.setNoOfDays(NoOfDays);
                                    model.setReason(Reason);
                                    model.setDestination(Destination);
                                    model.setApporvalStatus(ApprovalStatus);
                                    model.setAID(AID);
                                    itemList.add(model);

                                }

                                ApplicationApprovalAdapter detailsAdapter=new ApplicationApprovalAdapter(itemList,OtherApplicationApprovalActivity.this);
                                binding.rvItem.setAdapter(detailsAdapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                binding.llLoader.setVisibility(View.VISIBLE);
                                binding.llMain.setVisibility(View.GONE);
                                binding.llNoData.setVisibility(View.GONE);
                            }

                        }else {
                            binding.llLoader.setVisibility(View.GONE);
                            binding.llMain.setVisibility(View.GONE);
                            binding.llNoData.setVisibility(View.VISIBLE);
                        }




                    }

                    @Override
                    public void onError(ANError error) {
                        binding.llLoader.setVisibility(View.GONE);
                        binding.llMain.setVisibility(View.GONE);
                        binding.llNoData.setVisibility(View.VISIBLE);

                    }
                });
    }


    public void approvalApplication(String aID,int approvalFlag,String approvalStatus) {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("CompanyID",pref.getEmpClintId());
            jsonObject.put("AppAID",aID);
            jsonObject.put("ApprovalStatus",approvalFlag);
            jsonObject.put("ApprovalStatusDetails",approvalStatus);
            jsonObject.put("ApprovedBY",pref.getEmpId());
            jsonObject.put("SecurityCode",pref.getSecurityCode());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        final ProgressDialog pd=new ProgressDialog(OtherApplicationApprovalActivity.this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(Api.CFLOApplicationApproval)
                .addJSONObjectBody(jsonObject)
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

    private void successAlert(String text) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(OtherApplicationApprovalActivity.this, R.style.CustomDialogNew);
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
                JSONObject object=new JSONObject();
                try {
                    object.put("ApproverID",pref.getEmpId());
                    object.put("CompanyID",pref.getEmpClintId());
                    object.put("SecurityCode",pref.getSecurityCode());
                    getAllApplicationDetails(object);
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
    private void showErrorDialog(String text) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(OtherApplicationApprovalActivity.this, R.style.CustomDialogNew);
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
                JSONObject object=new JSONObject();
                try {
                    object.put("ApproverID",pref.getEmpId());
                    object.put("CompanyID",pref.getEmpClintId());
                    object.put("SecurityCode",pref.getSecurityCode());
                    getAllApplicationDetails(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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