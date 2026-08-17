package com.genius.payhrms.activity.leaveapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.genius.payhrms.R;
import com.genius.payhrms.activity.activity.LoginActivity;
import com.genius.payhrms.activity.adapter.AdjustmentDetailsAdapter;
import com.genius.payhrms.activity.model.AdjustmentDetailsModel;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.Pref;
import com.genius.payhrms.activity.utility.SecurityCode;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class OtherApplicationDetailsFragment extends Fragment {
    private static final String TAG = "OtherDetailsFragment";


    View v;
    Pref pref;
    RecyclerView rvItem;
    LinearLayout llNoData,llLoader,llMain;

    AdjustmentDetailsAdapter adjustmentDetailsAdapter;
    ArrayList<AdjustmentDetailsModel> adjustmentList;
    AlertDialog alerDialog1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_other_application_details, container, false);

        initView();
        onClick();
        if(pref.getSecurityCode().equals(SecurityCode.IFB_Travel_System)){
            JSONObject object=new JSONObject();
            try {
                object.put("CompanyId",pref.getEmpClintId());
                object.put("EmployeeId",pref.getEmpId());
                object.put("SecurityCode",pref.getSecurityCode());
                TS_GetReport(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            JSONObject object=new JSONObject();
            try {
                object.put("CompanyID",pref.getEmpClintId());
                object.put("EmployeeID",pref.getEmpId());
                object.put("SecurityCode",pref.getSecurityCode());
                getReport(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return v;
    }

    private void TS_GetReport(JSONObject object) {
        Log.e(TAG, "getReport: "+object.toString());
        llMain.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        llLoader.setVisibility(View.VISIBLE);
        AndroidNetworking.post(Api.TS_DisplayApplication)
                .addJSONObjectBody(object)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "REPORT: "+response.toString());
                        JSONObject job1 = response;
                        int Response_Code = job1.optInt("Response_Code");
                        String Response_Message=job1.optString("Response_Message");
                        adjustmentList = new ArrayList<>();
                        if (Response_Code == 101) {
                            String responseData = job1.optString("Response_Data");
                            try {
                                JSONObject jsonArray = new JSONObject(responseData);
                                String Table=jsonArray.optString("Table");
                                JSONArray adjustmentArray = new JSONArray(Table);
                                if (adjustmentArray.length() > 0){
                                    for (int i = 0; i < adjustmentArray.length(); i++) {
                                        Log.e(TAG, "adjustmentArray: "+i);
                                        JSONObject obj = adjustmentArray.getJSONObject(i);
                                        adjustmentList.add(new AdjustmentDetailsModel(
                                                obj.optString("AID"),
                                                obj.optString("AdjustmentType"),
                                                obj.optString("AppliedDate"),
                                                obj.optString("StartDate"),
                                                obj.optString("EndDate"),
                                                obj.optString("InTime"),
                                                obj.optString("OutTime"),
                                                obj.optString("NoOfDays"),
                                                obj.optString("Reason"),
                                                obj.optString("Clientname"),
                                                obj.optString("Destination"),
                                                obj.optString("TravelDetails"),
                                                obj.optString("AdvanceAmount"),
                                                obj.optString("ApprovedBY"),
                                                obj.optString("ApprovedOn"),
                                                obj.optString("ApprovalStatus"),
                                                obj.optInt("Isdelete"),
                                                obj.optInt("OD"),
                                                obj.optString("offdate")
                                        ));
                                    }
                                    setAdapter();
                                    llMain.setVisibility(View.VISIBLE);
                                    llNoData.setVisibility(View.GONE);
                                    llLoader.setVisibility(View.GONE);
                                } else {
                                    llMain.setVisibility(View.GONE);
                                    llNoData.setVisibility(View.VISIBLE);
                                    llLoader.setVisibility(View.GONE);
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "Error: "+anError.toString());
                        Intent intent=new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                });
    }


    private void initView() {
        pref=new Pref(getContext());
        rvItem=(RecyclerView)v.findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);
        llNoData=(LinearLayout)v.findViewById(R.id.llNoData);
        llLoader=(LinearLayout)v.findViewById(R.id.llLoader);
        llMain=(LinearLayout)v.findViewById(R.id.llMain);
    }

    private void onClick() {

    }

    private void getReport(JSONObject object) {
        Log.e(TAG, "getReport: "+object.toString());
        llMain.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        llLoader.setVisibility(View.VISIBLE);
        AndroidNetworking.post(Api.sGetAdjustmentDetailsForGrid)
                .addJSONObjectBody(object)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "REPORT: "+response.toString());
                        JSONObject job1 = response;
                        int Response_Code = job1.optInt("Response_Code");
                        String Response_Message=job1.optString("Response_Message");
                        adjustmentList = new ArrayList<>();
                        if (Response_Code == 101) {
                            String responseData = job1.optString("Response_Data");
                            try {
                                JSONObject jsonArray = new JSONObject(responseData);
                                String Table=jsonArray.optString("Table");
                                JSONArray adjustmentArray = new JSONArray(Table);
                                if (adjustmentArray.length() > 0){
                                    for (int i = 0; i < adjustmentArray.length(); i++) {
                                        Log.e(TAG, "adjustmentArray: "+i);
                                        JSONObject obj = adjustmentArray.getJSONObject(i);
                                        adjustmentList.add(new AdjustmentDetailsModel(
                                                obj.optString("AID"),
                                                obj.optString("AdjustmentType"),
                                                obj.optString("AppliedDate"),
                                                obj.optString("StartDate"),
                                                obj.optString("EndDate"),
                                                obj.optString("InTime"),
                                                obj.optString("OutTime"),
                                                obj.optString("NoOfDays"),
                                                obj.optString("Reason"),
                                                obj.optString("Clientname"),
                                                obj.optString("Destination"),
                                                obj.optString("TravelDetails"),
                                                obj.optString("AdvanceAmount"),
                                                obj.optString("ApprovedBY"),
                                                obj.optString("ApprovedOn"),
                                                obj.optString("ApprovalStatus"),
                                                obj.optInt("Isdelete"),
                                                obj.optInt("OD"),
                                                obj.optString("offdate")
                                        ));
                                    }
                                    setAdapter();
                                    llMain.setVisibility(View.VISIBLE);
                                    llNoData.setVisibility(View.GONE);
                                    llLoader.setVisibility(View.GONE);
                                } else {
                                    llMain.setVisibility(View.GONE);
                                    llNoData.setVisibility(View.VISIBLE);
                                    llLoader.setVisibility(View.GONE);
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "Error: "+anError.toString());
                        Intent intent=new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                });

    }

    private void setAdapter() {
        adjustmentDetailsAdapter = new AdjustmentDetailsAdapter(getActivity(),adjustmentList,OtherApplicationDetailsFragment.this);
        rvItem.setAdapter(adjustmentDetailsAdapter);
    }

    public void deleteAdjustments(String AID){
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        String API_URL ="";
        JSONObject object=new JSONObject();
        if (pref.getSecurityCode().equals(SecurityCode.IFB_Travel_System)){
            API_URL = Api.TS_DeleteOtherApplication;
            try {
                object.put("CompanyId",pref.getEmpClintId());
                object.put("ApplicationId",AID);
                object.put("SecurityCode",pref.getSecurityCode());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            API_URL = Api.sDeleteAdjutmentApplication;
            try {
                object.put("CompanyID",pref.getEmpClintId());
                object.put("EmployeeID",AID);
                object.put("SecurityCode",pref.getSecurityCode());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.e(TAG, "deleteAdjustments: "+object);

        AndroidNetworking.post(API_URL)
                .addJSONObjectBody(object)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "Delete_Adjustments: "+response.toString());

                        JSONObject job1 = response;
                        pd.dismiss();
                        //Log.e("response12", "@@@@@@" + job1);


                        int Response_Code = job1.optInt("Response_Code");
                        String Response_Message=job1.optString("Response_Message");
                        if (Response_Code == 101) {
                            deleteAlert();
                        } 
                        /*JSONObject object=new JSONObject();
                        try {
                            object.put("CompanyID",pref.getEmpClintId());
                            object.put("EmployeeID",pref.getEmpId());
                            object.put("SecurityCode",pref.getSecurityCode());
                            getReport(object);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "Delete_Adjustments_onError: "+anError);
                    }
                });
    }

    private void deleteAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);

        tvInvalidDate.setText("Leave has been deleted successfully");



        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                JSONObject object=new JSONObject();
                try {
                    object.put("CompanyID",pref.getEmpClintId());
                    object.put("EmployeeID",pref.getEmpId());
                    object.put("SecurityCode",pref.getSecurityCode());
                    getReport(object);
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