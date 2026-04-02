package com.genius.payhrms.activity.leaveapplication.SortLeaveApplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.genius.payhrms.R;
import com.genius.payhrms.activity.adapter.ShortLeaveApproverAdapter;
import com.genius.payhrms.activity.model.SortLeaveApproverModel;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.Pref;
import com.genius.payhrms.activity.utility.Util;

import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SortLeaveApproverFragment extends Fragment {
    private static final String TAG = "SortLeaveApproverFragme";
    private static final org.apache.commons.logging.Log log = LogFactory.getLog(SortLeaveApproverFragment.class);
    Pref pref;
    LinearLayout llLoader,llMain,llNoData,llShow;
    RecyclerView rvItem;
    ArrayList<SortLeaveApproverModel> approverList = new ArrayList<>();
    ArrayList<String> mIdList = new ArrayList<>();
    String mId;
    ShortLeaveApproverAdapter shortLeaveApproverAdapter;
    AppCompatButton btnApprove, btnReject;
    AlertDialog alerDialog1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sort_leave_approver, container, false);
        initView(v);
        btnClick();
        return v;
    }



    private void initView(View v) {
        pref = new Pref(getActivity());
        llLoader = v.findViewById(R.id.llLoader);
        llMain = v.findViewById(R.id.llMain);
        llNoData = v.findViewById(R.id.llNoData);
        llShow = v.findViewById(R.id.llShow);
        rvItem = v.findViewById(R.id.rvItem);
        rvItem.setLayoutManager(new LinearLayoutManager(getActivity()));
        btnApprove = v.findViewById(R.id.btnApprove);
        btnReject = v.findViewById(R.id.btnReject);
        JSONObject object=new JSONObject();
        try {
            object.put("EmployeeID",pref.getEmpId());
            object.put("SecurityCode",pref.getSecurityCode());
            ApprovalList(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void btnClick() {
        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject object=new JSONObject();
                try {
                    object.put("EmployeeID",0);
                    object.put("AID",mId);
                    object.put("UserID",pref.getEmpId());
                    object.put("Mode","Approve");
                    object.put("SecurityCode",pref.getSecurityCode());
                    approveApplication(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject object=new JSONObject();
                try {
                    object.put("EmployeeID",0);
                    object.put("AID",mId);
                    object.put("UserID",pref.getEmpId());
                    object.put("Mode","Reject");
                    object.put("SecurityCode",pref.getSecurityCode());
                    rejectApplication(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void rejectApplication(JSONObject object) {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(Api.ApproveRejectShortLeaveApplication)
                .addJSONObjectBody(object)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                . getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "REJECT_APPLICATION: "+response.toString(4));
                            pd.dismiss();
                            JSONObject job1 = response;
                            int Response_Code = job1.optInt("Response_Code");
                            String Response_Message=job1.optString("Response_Message");
                            if (Response_Code == 101) {
                                rejectAlert();
                            }else {
                                Toast.makeText(getContext(),Response_Message,Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private void approveApplication(JSONObject object) {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(Api.ApproveRejectShortLeaveApplication)
                .addJSONObjectBody(object)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            pd.dismiss();
                            Log.e(TAG, "APPROVE_APPLICATION: "+response.toString(4));
                            JSONObject job1 = response;
                            int Response_Code = job1.optInt("Response_Code");
                            String Response_Message=job1.optString("Response_Message");
                            if (Response_Code == 101) {
                                approveAlert();
                            }else {
                                Toast.makeText(getContext(),Response_Message,Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        pd.dismiss();
                        Log.e(TAG, "APPROVE_APPLICATION_error: "+anError.getErrorBody());
                    }
                });
    }

    private void ApprovalList(JSONObject object) {
        rvItem.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        llMain.setVisibility(View.GONE);
        llLoader.setVisibility(View.VISIBLE);
        llShow.setVisibility(View.GONE);
        AndroidNetworking.post(Api.GetShortLeaveForApproval)
                .addJSONObjectBody(object)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "Approval_List: "+response.toString(4));
                            JSONObject job1 = response;
                            int Response_Code = job1.optInt("Response_Code");
                            String Response_Message=job1.optString("Response_Message");
                            if (Response_Code == 101) {
                                approverList.clear();
                                JSONObject responseData=job1.optJSONObject("Response_Data");
                                if (responseData == null){
                                    rvItem.setVisibility(View.GONE);
                                    llNoData.setVisibility(View.VISIBLE);
                                    llLoader.setVisibility(View.GONE);
                                    llMain.setVisibility(View.GONE);
                                    return;
                                }
                                JSONArray Table = responseData.optJSONArray("Table");
                                for (int i = 0; i < Table.length(); i++) {
                                    JSONObject object = Table.optJSONObject(i);
                                    String EmployeeID = object.optString("EmployeeID");
                                    String EmployeeName = object.optString("EmployeeName");
                                    String AID = object.optString("AID");
                                    String YearID = object.optString("YearID");
                                    String Month = object.optString("Month");
                                    String ApplicationDate = object.optString("Application Date");
                                    Double ShortLeaveMins = object.optDouble("Short Leave Mins");
                                    String Status = object.optString("Status");
                                    String Reason = object.optString("Reason");
                                    String ApprovedBy = object.optString("Approved By");
                                    String ApprovedOn = object.optString("Approved On");
                                    approverList.add(new SortLeaveApproverModel(EmployeeID,EmployeeName,AID,YearID,Month,ApplicationDate,String.valueOf(ShortLeaveMins),
                                            Status,Reason,ApprovedBy,ApprovedOn));
                                }
                                shortLeaveApproverAdapter = new ShortLeaveApproverAdapter(getContext(),SortLeaveApproverFragment.this,approverList);
                                rvItem.setAdapter(shortLeaveApproverAdapter);
                                rvItem.setVisibility(View.VISIBLE);
                                llNoData.setVisibility(View.GONE);
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                            } else {
                                rvItem.setVisibility(View.GONE);
                                llNoData.setVisibility(View.VISIBLE);
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "Approval_List_error: "+anError.getErrorBody());
                        rvItem.setVisibility(View.GONE);
                        llNoData.setVisibility(View.VISIBLE);
                        llLoader.setVisibility(View.GONE);
                        llMain.setVisibility(View.GONE);
                    }
                });
    }

    public void updateAttendanceStatus(int position, boolean status) {
        approverList.get(position).setSelected(status);
        String idBind = "";
        if (approverList.get(position).isSelected() == true) {
            idBind = pref.getEmpId()+"_"+approverList.get(position).getEmployeeID()+"_"+approverList.get(position).getAID();
            mIdList.add(idBind);
        } else {
            idBind = pref.getEmpId()+"_"+approverList.get(position).getEmployeeID()+"_"+approverList.get(position).getAID();
            mIdList.remove(idBind);
        }
        mId = mIdList.toString().replace("[", "").replace("]", "").replaceAll("\\s+", "");
        Log.e(TAG, "updateAttendanceStatus: "+mId);
        if (mIdList.size() > 0) {
            llShow.setVisibility(View.VISIBLE);
        } else {
            llShow.setVisibility(View.GONE);
        }
        shortLeaveApproverAdapter.notifyDataSetChanged();
    }

    private void approveAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);

        tvInvalidDate.setText("Sort Leave has been approved successfully");

        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                JSONObject object=new JSONObject();
                try {
                    object.put("EmployeeID",pref.getEmpId());
                    object.put("SecurityCode",pref.getSecurityCode());
                    ApprovalList(object);
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

    private void rejectAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);
        if (pref.getLanguage().equals("hi")) {
            tvInvalidDate.setText("");
        } else {
            tvInvalidDate.setText("Sort Leave has been rejected successfully");
        }

        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                JSONObject object=new JSONObject();
                try {
                    object.put("EmployeeID",pref.getEmpId());
                    object.put("SecurityCode",pref.getSecurityCode());
                    ApprovalList(object);
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