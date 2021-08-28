package com.genius.hrms.activity.attendance;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
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
import android.widget.TimePicker;
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
import com.genius.hrms.R;
import com.genius.hrms.activity.adapter.AttendanceApprovalAdapter;
import com.genius.hrms.activity.adapter.TeamReportAdapter;
import com.genius.hrms.activity.model.AttendanceApprovalModel;
import com.genius.hrms.activity.model.TeamReportModel;
import com.genius.hrms.activity.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;


public class AttendanceApprovalFragment extends Fragment {

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
                        getItem();
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
                    approveFunction();
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
                                   rejectFunction();
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

    private void getItem() {
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        String surl =  url+"/api/Attendance/SelfAttendanceApprovalPending?CompanyID=" + pref.getEmpConId() + "&ApproverID=" + pref.getEmpId() + "&StartDate=" + startDate + "&EndDate=" + endDate + "&SecurityCode=" + securityCode;
        Log.d("teamUrl", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        itemList.clear();



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
                                    String EmpName = obj.optString("EmpName");
                                    String AttendanceDate = obj.optString("AttendanceDate");
                                    String InTime = obj.optString("InTime");
                                    String OutTime = obj.optString("OutTime");
                                    String WorkSource = obj.optString("WorkSource");
                                    String AID = obj.optString("AID");
                                    String AttendanceNature = obj.optString("AttendanceNature");
                                    AttendanceApprovalModel obj2 = new AttendanceApprovalModel(AID, EmpName, AttendanceDate, InTime, OutTime,WorkSource,AttendanceNature);
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
            aIDList.remove(position);
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


    private void approveFunction() {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        AndroidNetworking.upload(pref.getIpAddress() + "GHRMSApi/API/Attendance/SelfAttendanceApproval")
                .addMultipartParameter("CompanyID", pref.getEmpClintId())
                .addMultipartParameter("StrAttData", aid)
                .addMultipartParameter("Approvedby", pref.getEmpId())
                .addMultipartParameter("ApproverStatus", "1")
                .addMultipartParameter("SecurityCode", securityCode)

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

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        pd.dismiss();
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();

                    }
                });

    }

    private void rejectFunction() {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();

        AndroidNetworking.upload(pref.getIpAddress() + "GHRMSApi/API/Attendance/SelfAttendanceApproval")
                .addMultipartParameter("CompanyID", pref.getEmpClintId())
                .addMultipartParameter("StrAttData", aid)
                .addMultipartParameter("Approvedby", pref.getEmpId())
                .addMultipartParameter("ApproverStatus", "-1")
                .addMultipartParameter("SecurityCode", securityCode)

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

        tvInvalidDate.setText("Attendance approved successfully");


        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                getItem();
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

        tvInvalidDate.setText("Attendance rejected successfully");


        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                getItem();
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
