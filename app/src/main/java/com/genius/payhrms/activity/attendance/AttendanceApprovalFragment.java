package com.genius.payhrms.activity.attendance;

import static com.genius.payhrms.activity.utility.Util.SECRET_KEY;
import static com.genius.payhrms.activity.utility.Util.encrypt;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.genius.payhrms.R;
import com.genius.payhrms.activity.adapter.AttendanceApprovalAdapter;
import com.genius.payhrms.activity.model.AttendanceApprovalModel;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;


public class AttendanceApprovalFragment extends Fragment {
    private static final String TAG = "AttendanceApprovalFragm";
    View v;

    RecyclerView rvItem;
    ArrayList<AttendanceApprovalModel> itemList = new ArrayList<>();
    LinearLayout llStrtDate, llEndDate;
    TextView tvStrtDate, tvEndDate;
    String startDate = "", endDate = "";
    Button btnShow;
    LinearLayout llNoData, llLoader, llMain;
    Pref pref;
    AttendanceApprovalAdapter detailsAdpater;
    ArrayList<String> aIDList = new ArrayList<>();
    String aid = "";
    LinearLayout llARLayout;
    Button btnReject, btnApprove;
    AlertDialog.Builder builder;
    AlertDialog alerDialog1;
    String intime;
    String securityCode;
    String url;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_attendance_approval, container, false);
        initView();
        onClick();
        return v;
    }


    private void initView() {
        pref = new Pref(getContext());
        securityCode=pref.getSecurityCode();

        url=pref.getIpAddress()+"/GHRMSApi";

        rvItem = (RecyclerView) v.findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);
        llEndDate = (LinearLayout) v.findViewById(R.id.llEndDate);
        llStrtDate = (LinearLayout) v.findViewById(R.id.llStrtDate);
        llNoData = (LinearLayout) v.findViewById(R.id.llNoData);
        llLoader = (LinearLayout) v.findViewById(R.id.llLoader);
        llMain = (LinearLayout) v.findViewById(R.id.llMain);

        tvStrtDate = (TextView) v.findViewById(R.id.tvStrtDate);
        tvEndDate = (TextView) v.findViewById(R.id.tvEndDate);
        btnShow = (Button) v.findViewById(R.id.btnShow);

        llARLayout = (LinearLayout) v.findViewById(R.id.llARLayout);

        btnReject = (Button) v.findViewById(R.id.btnReject);
        btnApprove = (Button) v.findViewById(R.id.btnApprove);
        builder = new AlertDialog.Builder(getContext());

    }

    private void onClick() {
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
                        JSONObject jsonObject=new JSONObject();
                        try {
                            jsonObject.put("CompanyID",pref.getEmpClintId());
                            jsonObject.put("ApproverID",pref.getEmpId());
                            jsonObject.put("StartDate",startDate);
                            jsonObject.put("EndDate",endDate);
                            jsonObject.put("SecurityCode",pref.getSecurityCode());
                            getItem2(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getContext(), "Please select End Date", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "please select Start Date", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!aid.equals("")) {
                    //approveFunction();
                    JSONObject jsonObject=new JSONObject();
                    try {
                        jsonObject.put("CompanyID",pref.getEmpClintId());
                        jsonObject.put("AttendanceData",aid);
                        jsonObject.put("Approvedby",pref.getEmpId());
                        jsonObject.put("ApprovedStatus","1");
                        jsonObject.put("SecurityCode",securityCode);
                        approveFunction2(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(getContext(),"Please select item",Toast.LENGTH_LONG).show();
                }
            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (!aid.equals("")) {
                   //Setting message manually and performing action on button click
                   builder.setMessage("Do you want to reject ?")
                           .setCancelable(false)
                           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int id) {
                                   JSONObject jsonObject=new JSONObject();
                                   try {
                                       jsonObject.put("CompanyID",pref.getEmpClintId());
                                       jsonObject.put("AttendanceData",aid);
                                       jsonObject.put("Approvedby",pref.getEmpId());
                                       jsonObject.put("ApprovedStatus","-1");
                                       jsonObject.put("SecurityCode",securityCode);
                                       rejectFunction2(jsonObject);
                                   } catch (JSONException e) {
                                       e.printStackTrace();
                                   }
                                   dialog.cancel();

                               }
                           })
                           .setNegativeButton("No", new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int id) {
                                   //  Action for 'NO' Button
                                   dialog.cancel();

                               }
                           });
                   //Creating dialog box
                   AlertDialog alert = builder.create();
                   //Setting the title manually
                   alert.setTitle("Rejection Alert");
                   alert.show();
               }else {
                   Toast.makeText(getContext(),"Please select item",Toast.LENGTH_LONG).show();
               }
            }
        });

    }



    private void getItem2(JSONObject jsonObject) {
        Log.e(TAG, "getItem2: called: "+jsonObject);
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        /*String surl =  url+"/api/Attendance/SelfAttendanceApprovalPending?CompanyID=" + pref.getEmpConId() + "&ApproverID=" + pref.getEmpId() + "&StartDate=" + startDate + "&EndDate=" + endDate + "&SecurityCode=" + securityCode;
        Log.d("teamUrl", surl);*/

        AndroidNetworking.post(Api.sSelfAttendanceApprovalPending)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "SelfAttendanceApprovalPending: "+response);
                        itemList.clear();
                        try {
                            JSONObject job1 = response;
                            int Response_Code = job1.optInt("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code == 101) {
                                String responseData = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(responseData);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String EmpName = obj.optString("EmpName");
                                    String AttendanceDate = obj.optString("AttendanceDate");
                                    String InTime = obj.optString("InTime");
                                    String OutTime = obj.optString("OutTime");
                                    String WorkSource = obj.optString("WorkSource");
                                    String Code=obj.optString("Code");
                                    String AID = obj.optString("AID");
                                    String AttendanceNature = obj.optString("Remarks");
                                    AttendanceApprovalModel obj2 = new AttendanceApprovalModel(AID, EmpName, AttendanceDate, InTime, OutTime,WorkSource,AttendanceNature);
                                    obj2.setCode(Code);
                                    itemList.add(obj2);
                                }
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNoData.setVisibility(View.GONE);
                                setAdapter();
                            } else {
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNoData.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorCode()==401){
                            JSONObject obj=new JSONObject();
                            try {
                                obj.put("MasterID",encrypt(pref.getMasterId(),SECRET_KEY));
                                obj.put("Password",encrypt(pref.getPassword(),SECRET_KEY));
                                obj.put("IMEI","0");
                                obj.put("DeviceID","0");
                                obj.put("DeviceType","A");
                                obj.put("SecurityCode",pref.getSecurityCode());
                                login(obj);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            llLoader.setVisibility(View.VISIBLE);
                            llMain.setVisibility(View.GONE);
                            llNoData.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void login(JSONObject jsonObject) {
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(Api.sLoginapi)
                .addJSONObjectBody(jsonObject)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        pd.dismiss();

                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                            JSONArray responseData = job1.optJSONArray("Response_Data");
                            for (int i = 0; i < responseData.length(); i++) {
                                JSONObject obj = responseData.optJSONObject(i);
                                String Genius_Access_Token=obj.optString("Genius_Access_Token");
                                pref.saveAccessToken(Genius_Access_Token);

                                // boolean _status = job1.getBoolean("status");

                                JSONObject jsonObject=new JSONObject();
                                try {
                                    jsonObject.put("CompanyID",pref.getEmpClintId());
                                    jsonObject.put("ApproverID",pref.getEmpId());
                                    jsonObject.put("StartDate",startDate);
                                    jsonObject.put("EndDate",endDate);
                                    jsonObject.put("SecurityCode",pref.getSecurityCode());
                                    getItem2(jsonObject);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                    }
                });
    }

    private void setAdapter() {
        detailsAdpater = new AttendanceApprovalAdapter(itemList, getContext(), this);
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
                        startDate = year + "-" + month + "-" + dayOfMonth;
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
                        endDate = year + "-" + month + "-" + dayOfMonth;
                        tvEndDate.setText(endDate);

                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.show();

    }


    public void updateAttendanceStatus(int position, boolean status) {
        itemList.get(position).setSelected(status);
        if (itemList.get(position).isSelected() == true) {
            aIDList.add(itemList.get(position).getaId() + "_" + itemList.get(position).getInTime() + "_" + itemList.get(position).getOutTime());
        } else {
            aIDList.remove(itemList.get(position).getaId() + "_" + itemList.get(position).getInTime() + "_" + itemList.get(position).getOutTime());
        }

        aid = aIDList.toString().replace("[", "").replace("]", "");
        Log.d("aid", aid);
        if (aIDList.size() > 0) {
            llARLayout.setVisibility(View.VISIBLE);
        } else {
            llARLayout.setVisibility(View.GONE);
        }


        detailsAdpater.notifyDataSetChanged();
    }

    private void approveFunction2(JSONObject jsonObject) {
        Log.e(TAG, "approveFunction2: "+jsonObject);
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(Api.sSelfAttendanceApproval)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.e(TAG, "Self_Attendance_Approval: "+response);
                                pd.dismiss();
                                JSONObject job1 = response;
                                int Response_Code = job1.optInt("Response_Code");
                                String Response_Message = job1.optString("Response_Message");
                                if (Response_Code == 101) {
                                    approveAlert();
                                } else {
                                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                pd.dismiss();
                                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                            }
                        });

    }



    private void rejectFunction2(JSONObject jsonObject) {
        Log.e(TAG, "rejectFunction2: "+jsonObject);
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();


        AndroidNetworking.post(Api.sSelfAttendanceApproval)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "Reject_Function: "+response);
                        pd.dismiss();
                        JSONObject job1 = response;
                        int Response_Code = job1.optInt("Response_Code");
                        String Response_Message = job1.optString("Response_Message");
                        if (Response_Code == 101) {
                            rejectAlert();
                        } else {
                            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        pd.dismiss();
                        Log.e(TAG, "Reject_Function_onError: "+anError);
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

        tvInvalidDate.setText("Attendance has been approved successfully");


        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.put("CompanyID",pref.getEmpClintId());
                    jsonObject.put("ApproverID",pref.getEmpId());
                    jsonObject.put("StartDate",startDate);
                    jsonObject.put("EndDate",endDate);
                    jsonObject.put("SecurityCode",pref.getSecurityCode());
                    getItem2(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                aIDList.clear();
                aid="";

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

        tvInvalidDate.setText("Attendance has been rejected successfully");


        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                //getItem();
                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.put("CompanyID",pref.getEmpClintId());
                    jsonObject.put("ApproverID",pref.getEmpId());
                    jsonObject.put("StartDate",startDate);
                    jsonObject.put("EndDate",endDate);
                    jsonObject.put("SecurityCode",pref.getSecurityCode());
                    getItem2(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                aIDList.clear();
                aid="";

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
