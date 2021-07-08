package com.genius.hrms.activity.leaveapplication;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
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
import com.genius.hrms.activity.activity.LoginActivity;
import com.genius.hrms.activity.adapter.LeaveDetailsAdapter;
import com.genius.hrms.activity.adapter.SpecialHolidayAppItemAdapter;
import com.genius.hrms.activity.model.LeaveDetailsModel;
import com.genius.hrms.activity.model.SpecialHolidayItemModel;
import com.genius.hrms.activity.model.SpinnerModel;
import com.genius.hrms.activity.utility.Pref;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpecialHolidayApplicationFragment extends Fragment {


    View view;
    Pref pref;
    LinearLayout llStrtDate,llEndDate;
    String startDate,endDate;
    TextView tvStrtDate,tvEndDate;
    Button btnShow;
    ArrayList<SpecialHolidayItemModel>itemList=new ArrayList<>();
    LinearLayout llNoData,llLoader,llMain;
    AlertDialog alerDialog1;
    RecyclerView rvItem;
    SpecialHolidayAppItemAdapter spAdapter;
    Button btnSubmit;
    ArrayList<String>spHolidayList=new ArrayList<>();
    String dateDetails="";
    AlertDialog al1;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_special_holiday_application, container, false);
        initView();
        onClick();
        return view;
    }
    private void initView(){
        pref=new Pref(getContext());
        getApproverOrNot();
        llStrtDate=(LinearLayout)view.findViewById(R.id.llStrtDate);
        llEndDate=(LinearLayout)view.findViewById(R.id.llEndDate);
        tvStrtDate=(TextView) view.findViewById(R.id.tvStrtDate);
        tvEndDate=(TextView) view.findViewById(R.id.tvEndDate);
        btnShow=(Button) view.findViewById(R.id.btnShow);
        rvItem=(RecyclerView)view.findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);
        llNoData=(LinearLayout)view.findViewById(R.id.llNoData);
        llLoader=(LinearLayout)view.findViewById(R.id.llLoader);
        llMain=(LinearLayout)view.findViewById(R.id.llMain);
        btnSubmit=(Button)view.findViewById(R.id.btnSubmit);

    }


    private void getApproverOrNot() {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading...");
        pd.setCancelable(true);
        pd.show();

        String surl = pref.getIpAddress() + "ghrmsapi/api/Leave/LeaveApplicationApprover?CompanyID=" + pref.getEmpClintId() + "&EmployeeID=" + pref.getEmpId() + "&SecurityCode=" + pref.getSecurityCode();
        Log.d("printurlbalance", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
//                        llLoader.setVisibility(View.GONE);

                        pd.dismiss();


                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {

                                ((SpecialholidayActivity) getContext()).approverVisibility();


                            } else {

                                ((SpecialholidayActivity) getContext()).approverHidden();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();


                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    private void getItem(){
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        String surl = pref.getIpAddress()+"ghrmsapi/api/HolidayMap/ExceptionHolidayADD?CompanyID="+pref.getEmpClintId()+"&EmployeeID="+pref.getEmpId()+"&StartDate="+startDate+"&EndDate="+endDate+"&SecurityCode="+pref.getSecurityCode();
        Log.d("inputLeaveBlanace", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        itemList.clear();

                        // attendabceInfiList.clear();

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
                                    String DayName = obj.optString("DayName");
                                    String Date = obj.optString("Date");


                                    SpecialHolidayItemModel obj2 = new SpecialHolidayItemModel(Date,DayName);
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                90000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }
    private void setAdapter(){
        spAdapter=new SpecialHolidayAppItemAdapter(itemList,SpecialHolidayApplicationFragment.this,getContext());
        rvItem.setAdapter(spAdapter);
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
                        startDate = month + "/" + dayOfMonth + "/" + year;
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
                        endDate = month + "/" + dayOfMonth + "/" + year;
                        tvEndDate.setText(endDate);

                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.show();

    }

    private void onClick(){
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
                    }else {
                        Toast.makeText(getContext(),"Please select End Date",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getContext(),"please select Start Date",Toast.LENGTH_LONG).show();
                }
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spHolidayList.size()>0) {
                    shoeRemarksDialog();
                }else {
                    Toast.makeText(getContext(),"please select date",Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    public void updateStatus(int position, boolean status) {
        itemList.get(position).setSelected(status);
        if (itemList.get(position).isSelected() == true) {
            spHolidayList.add(itemList.get(position).getDate() );


        } else {
            spHolidayList.remove(position);
        }


        dateDetails = spHolidayList.toString().replace("[", "").replace("]", "").replaceAll("\\s+", "");
        Log.d("detailslist", dateDetails);

        /*Log.d("arpan", itemList.toString());
        String i = itemList.toString();
        String d = i.replace("[", "").replace("]", "");
        empId = d.replaceAll("\\s+", "");
        String emp=empName.toString();
        String replace=emp.replace("[", "").replace("]", "");
        tvEmpName.setText(replace);
*/

        spAdapter.notifyDataSetChanged();
    }

    public void specialHolidaySave(String remarks) {
        final ProgressDialog pg=new ProgressDialog(getContext());
        pg.setMessage("Loading..");
        pg.setCancelable(false);
        AndroidNetworking.upload(pref.getIpAddress()+"ghrmsapi/api/HolidayMap/ExceptionHolidaySave")
                .addMultipartParameter("CompanyID", pref.getEmpClintId())
                .addMultipartParameter("EmployeeID", pref.getEmpId())
                .addMultipartParameter("StrDate", dateDetails)
                .addMultipartParameter("Remarks", remarks)
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        pg.show();

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        pg.dismiss();
                        JSONObject job = response;
                        boolean responseStatus = job.optBoolean("responseStatus");
                        if (responseStatus) {
                            successAlert();
                        } else {

                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        pg.dismiss();
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();

                    }
                });
    }


    private void successAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);
        if (pref.getLanguage().equals("hi")) {
            tvInvalidDate.setText("सफलतापूर्वक लागू किया गया");
        } else {

            tvInvalidDate.setText("Successfully applied");
        }


        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                getItem();


            }
        });

        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }

    private void shoeRemarksDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_remarks, null);
        dialogBuilder.setView(dialogView);
        final EditText etRemarks=(EditText)dialogView.findViewById(R.id.etRemarks);
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etRemarks.getText().toString().length()>0) {
                    al1.dismiss();
                    specialHolidaySave(etRemarks.getText().toString());
                }else {
                    Toast.makeText(getContext(),"please enter remarks",Toast.LENGTH_LONG).show();
                }
            }
        });
        al1 = dialogBuilder.create();
        al1.setCancelable(true);
        Window window = al1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        al1.show();


    }


}
