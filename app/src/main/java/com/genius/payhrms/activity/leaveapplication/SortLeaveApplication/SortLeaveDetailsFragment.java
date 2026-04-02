package com.genius.payhrms.activity.leaveapplication.SortLeaveApplication;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.genius.payhrms.R;
import com.genius.payhrms.activity.adapter.SortLeaveEmployeeAdapter;
import com.genius.payhrms.activity.model.SortLeaveDetailsModel;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.Pref;
import com.genius.payhrms.activity.utility.ShowDialog;
import com.genius.payhrms.activity.utility.Util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SortLeaveDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SortLeaveDetailsFragment extends Fragment {
    private static final String TAG = "SortLeaveDetailsFragmen";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final Log log = LogFactory.getLog(SortLeaveDetailsFragment.class);

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SortLeaveDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SortLeaveDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SortLeaveDetailsFragment newInstance(String param1, String param2) {
        SortLeaveDetailsFragment fragment = new SortLeaveDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    LinearLayout llStrtDate,llEndDate,llLoader,llNoData,llMain;
    String startDate="",endDate="";
    TextView tvStrtDate,tvEndDate;
    Button btnShow;
    Pref pref;
    RecyclerView rvItem;
    ArrayList<SortLeaveDetailsModel> reportList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sort_leave_details, container, false);
        initView(v);
        btnClick();
        return v;
    }

    private void initView(View v) {
        pref = new Pref(getContext());
        llStrtDate = v.findViewById(R.id.llStrtDate);
        llEndDate = v.findViewById(R.id.llEndDate);
        llLoader = v.findViewById(R.id.llLoader);
        llNoData = v.findViewById(R.id.llNoData);
        llMain = v.findViewById(R.id.llMain);
        rvItem = v.findViewById(R.id.rvItem);
        tvStrtDate = v.findViewById(R.id.tvStrtDate);
        tvEndDate = v.findViewById(R.id.tvEndDate);
        btnShow = v.findViewById(R.id.btnShow);
        rvItem.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void btnClick() {
        llStrtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStrtDatePicker();
            }
        });

        llEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEndDatePicker();
            }
        });
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!startDate.equals("")) {
                    if (!endDate.equals("")) {
                        JSONObject object=new JSONObject();
                        try {
                            object.put("EmployeeID",pref.getEmpId());
                            object.put("FromDate", Util.changeAnyDateFormat(startDate,"M/d/yyyy","YYYY-M-d"));
                            object.put("Todate",Util.changeAnyDateFormat(endDate,"M/d/yyyy","YYYY-M-d"));
                            object.put("SecurityCode",pref.getSecurityCode());
                            leaveReport(object);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else {
                        Toast.makeText(getContext(),"Please select End Date",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getContext(),"please select Start Date",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void leaveReport(JSONObject object) {
        android.util.Log.e(TAG, "leaveReport: "+object);
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        AndroidNetworking.post(Api.GetSortLeaveApplications)
                .addJSONObjectBody(object)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            android.util.Log.e(TAG, "SORT_LEAVE_DETAILS: " + response.toString(4));
                            JSONObject job1 = response;
                            int Response_Code = job1.optInt("Response_Code");
                            String Response_Message=job1.optString("Response_Message");
                            if (Response_Code == 101) {
                                JSONObject responseData=job1.optJSONObject("Response_Data");
                                JSONArray Table = responseData.optJSONArray("Table");
                                reportList.clear();
                                if (Table.length() > 0){
                                    for (int i = 0; i < Table.length(); i++) {
                                        JSONObject object = Table.optJSONObject(i);
                                        String EmployeeName = object.optString("EmployeeName");
                                        int Year = object.optInt("Year");
                                        String Month = object.optString("Month");
                                        String AID = object.optString("AID");
                                        String ApplicationDate = object.optString("Application Date");
                                        double ShortLeaveMins = object.optDouble("Short Leave Mins");
                                        String Status = object.optString("Status");
                                        String Reason = object.optString("Reason");
                                        String ApprovedBy = object.optString("Approved By");
                                        String ApprovedOn = object.optString("Approved On");
                                        reportList.add(new SortLeaveDetailsModel(EmployeeName,String.valueOf(Year),Month,ApplicationDate,
                                                String.valueOf(ShortLeaveMins),Status,Reason,ApprovedBy,ApprovedOn,AID));
                                    }
                                    SortLeaveEmployeeAdapter sortLeaveEmployeeAdapter = new SortLeaveEmployeeAdapter(getActivity(),SortLeaveDetailsFragment.this,reportList);
                                    rvItem.setAdapter(sortLeaveEmployeeAdapter);
                                    llLoader.setVisibility(View.GONE);
                                    llMain.setVisibility(View.VISIBLE);
                                    llNoData.setVisibility(View.GONE);
                                } else {
                                    llLoader.setVisibility(View.GONE);
                                    llMain.setVisibility(View.GONE);
                                    llNoData.setVisibility(View.VISIBLE);
                                }
                            } else {
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNoData.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        android.util.Log.e(TAG, "SORT_LEAVE_DETAILS_error: " + anError.getErrorBody());
                        llLoader.setVisibility(View.GONE);
                        llMain.setVisibility(View.GONE);
                        llNoData.setVisibility(View.VISIBLE);
                    }
                });
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

    public void deleteApplication(String AID){
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();

        JSONObject object=new JSONObject();
        try {
            object.put("AID",AID);
            object.put("EmployeeID", pref.getEmpId());
            object.put("SecurityCode",pref.getSecurityCode());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(Api.DeleteShortLeave)
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
                            android.util.Log.e(TAG, "DELETE_SHORT_LEAVE: "+response.toString(0));
                            JSONObject job1 = response;
                            int Response_Code = job1.optInt("Response_Code");
                            String Response_Message=job1.optString("Response_Message");
                            if (Response_Code == 101) {
                                ShowDialog.showSuccessDialog(getActivity(), Response_Message, new ShowDialog.ResultListener() {
                                    @Override
                                    public void onSuccess() {
                                        ShowDialog.onDismiss();
                                        JSONObject object=new JSONObject();
                                        try {
                                            object.put("EmployeeID",pref.getEmpId());
                                            object.put("FromDate", Util.changeAnyDateFormat(startDate,"M/d/yyyy","YYYY-M-d"));
                                            object.put("Todate",Util.changeAnyDateFormat(endDate,"M/d/yyyy","YYYY-M-d"));
                                            object.put("SecurityCode",pref.getSecurityCode());
                                            leaveReport(object);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            } else {
                                ShowDialog.showErrorDialog(getActivity(),Response_Message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        android.util.Log.e(TAG, "DELETE_SHORT_LEAVE_error: "+anError.getErrorBody());
                        pd.dismiss();
                    }
                });
    }


}