package com.genius.payhrms.activity.leaveapplication.SortLeaveApplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.genius.payhrms.R;
import com.genius.payhrms.activity.leaveapplication.LeaveApplicationActivity;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.Pref;
import com.genius.payhrms.activity.utility.ShowDialog;
import com.genius.payhrms.activity.utility.TimeDateConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SortLeaveApplicationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SortLeaveApplicationFragment extends Fragment {
    private static final String TAG = "SortLeaveApplicationFra";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SortLeaveApplicationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SortLeaveApplicationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SortLeaveApplicationFragment newInstance(String param1, String param2) {
        SortLeaveApplicationFragment fragment = new SortLeaveApplicationFragment();
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
    Pref pref;
    ImageView imgStrtDay,imgEndDay,imgTimePick;
    EditText etReason;
    LinearLayout llSave;
    String applicantId;
    TextView tvApproverName,tvEmpName,tvStrtDate;
    EditText edtMinute;
    String startDate="";
    AlertDialog alerDialog1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sort_leave_application, container, false);
        initView(v);
        btnClick();
        return v;
    }



    private void initView(View v) {
        pref = new Pref(getContext());
        imgStrtDay = v.findViewById(R.id.imgStrtDay);
        imgEndDay = v.findViewById(R.id.imgEndDay);
        etReason = v.findViewById(R.id.etReason);
        llSave = v.findViewById(R.id.llSave);
        tvApproverName = v.findViewById(R.id.tvApproverName);
        tvEmpName = v.findViewById(R.id.tvEmpName);
        tvStrtDate = v.findViewById(R.id.tvStrtDate);
        imgTimePick = v.findViewById(R.id.imgTimePick);
        edtMinute = v.findViewById(R.id.edtMinute);
        tvEmpName.setText("Sort leave application of "+pref.getEmpName());
        JSONObject object=new JSONObject();
        try {
            object.put("AEMEmployeeID",pref.getEmpId());
            object.put("CompanyID",pref.getEmpClintId());
            object.put("SecurityCode",pref.getSecurityCode());
            getApproverOrNot(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void btnClick() {
        imgStrtDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showApplicationDatePicker();
            }
        });

        llSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(startDate.isEmpty()){
                    Toast.makeText(getContext(), "Please select Application Date", Toast.LENGTH_SHORT).show();
                } else if(edtMinute.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Please Enter Sort Leave Time", Toast.LENGTH_SHORT).show();
                }else if(Integer.parseInt(edtMinute.getText().toString()) < 5){
                    Toast.makeText(getContext(), "The minimum short leave duration must be 5 minutes", Toast.LENGTH_SHORT).show();
                } else if(Integer.parseInt(edtMinute.getText().toString()) > 60){
                    Toast.makeText(getContext(), "The maximum short leave duration must be 60 minutes", Toast.LENGTH_SHORT).show();
                } else if (etReason.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Please Enter Reason", Toast.LENGTH_SHORT).show();
                }else {
                    JSONObject object=new JSONObject();
                    try {
                        object.put("EmployeeID",pref.getEmpId());
                        object.put("ApplicationDate",startDate);
                        object.put("SLeaveTime",edtMinute.getText().toString());
                        object.put("Reason",etReason.getText().toString().trim());
                        object.put("CreatedBy",pref.getEmpId());
                        object.put("SecurityCode",pref.getSecurityCode());
                        saveLeaveApplication(object);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void saveLeaveApplication(JSONObject object) {
        final ProgressDialog pd=new ProgressDialog(getContext());
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(Api.SaveSortLeaveApplication)
                .addJSONObjectBody(object)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pd.dismiss();
                        try {
                            Log.e(TAG, "SAVE_SORT_LEAVE_APPLICATION: "+response.toString(4));
                            JSONObject job1 = response;
                            int Response_Code = job1.optInt("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code == 101) {
                                ShowDialog.showSuccessDialog(getContext(), Response_Message, new ShowDialog.ResultListener() {
                                    @Override
                                    public void onSuccess() {
                                        ShowDialog.onDismiss();
                                        ((SortLeaveActivity)getContext()).loadDetailsFragment();
                                    }
                                });
                            } else {
                                ShowDialog.showErrorDialog(getContext(),Response_Message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        pd.dismiss();
                        Log.e(TAG, "SAVE_SORT_LEAVE_APPLICATION_error: "+anError.getErrorBody());
                    }
                });
    }



    private void showApplicationDatePicker() {
        final Calendar c = Calendar.getInstance();

        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                int month = (monthOfYear + 1);
                startDate = month + "/" + dayOfMonth + "/" + year;
                tvStrtDate.setText(TimeDateConverter.convert_Date_MM_DD_YYYY_To_dd_MMM_yyyy(startDate));
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker();
        datePickerDialog.show();
    }


    private void getApproverOrNot(JSONObject object) {
        final ProgressDialog pd=new ProgressDialog(getContext());
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(Api.sapprovercheckapi)
                .addJSONObjectBody(object)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "APPROVE_CHECK: "+response.toString(0));
                            pd.dismiss();
                            JSONObject job1 = response;
                            int Response_Code = job1.optInt("Response_Code");
                            if (Response_Code == 101) {
                                String responseData = job1.optString("Response_Data");
                                JSONArray jsonArray=new JSONArray(responseData);
                                if (jsonArray.length()>0) {
                                    Log.e(TAG, "onResponse: "+jsonArray.length());
                                    if (jsonArray.length()>0) {
                                        ((SortLeaveActivity) getContext()).approverVisibility();
                                    } else {
                                        ((SortLeaveActivity) getContext()).approverHidden();
                                    }
                                    JSONObject jsonObject=new JSONObject();
                                    try {
                                        jsonObject.put("CompanyID",pref.getEmpClintId());
                                        jsonObject.put("EmployeeID",pref.getEmpId());
                                        jsonObject.put("ApproverID",pref.getEmpId());
                                        jsonObject.put("SecurityCode",pref.getSecurityCode());
                                        getLeaveAllDetails(jsonObject);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    applicantId = pref.getEmpId();
                                    JSONObject jsonObject=new JSONObject();
                                    try {
                                        jsonObject.put("CompanyID",pref.getEmpClintId());
                                        jsonObject.put("EmployeeID",pref.getEmpId());
                                        jsonObject.put("ApproverID",pref.getEmpId());
                                        jsonObject.put("SecurityCode",pref.getSecurityCode());
                                        getLeaveAllDetails(jsonObject);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                ((SortLeaveActivity) getContext()).approverHidden();
                                applicantId = pref.getEmpId();
                                JSONObject jsonObject=new JSONObject();
                                try {
                                    jsonObject.put("CompanyID",pref.getEmpClintId());
                                    jsonObject.put("EmployeeID",pref.getEmpId());
                                    jsonObject.put("ApproverID",pref.getEmpId());
                                    jsonObject.put("SecurityCode",pref.getSecurityCode());
                                    getLeaveAllDetails(jsonObject);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        pd.dismiss();
                    }
                });
    }

    private void getLeaveAllDetails(JSONObject jsonObject) {
        Log.e(TAG, "getLeaveAllDetails: called: "+jsonObject);
        final ProgressDialog pd=new ProgressDialog(getContext());
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        AndroidNetworking.post(Api.sLeaveDetails)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            pd.dismiss();
                            JSONObject job1 = response;
                            Log.e(TAG, "getLeaveAllDetails: " + job1);
                            int Response_Code = job1.optInt("Response_Code");
                            String Response_Message=job1.optString("Response_Message");
                            if (Response_Code == 101) {
                                String responseData = job1.optString("Response_Data");
                                JSONObject jsonArray=new JSONObject(responseData);
                                String Table2=jsonArray.optString("Table2");
                                JSONArray leaveApproverArray = new JSONArray(Table2);
                                for (int i = 0; i < leaveApproverArray.length(); i++) {
                                    JSONObject approverObject = leaveApproverArray.optJSONObject(i);
                                    final String ApproverName = approverObject.optString("ApproverName");
                                    tvApproverName.setText("Approver Name: " + ApproverName);
                                }
                            } else {

                            }
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        pd.dismiss();
                    }
                });
    }

    private void successAlert(String message) {
        android.app.AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);
        if (pref.getLanguage().equals("hi")) {
            tvInvalidDate.setText("सफलतापूर्वक लागू किया गया");
        } else {
            tvInvalidDate.setText(message);
            /*if (pref.getSecurityCode().equals(SecurityCode.Western_Enterprises)){
                tvInvalidDate.setText("Leave application save successfully");
            } else {
                tvInvalidDate.setText("Leave Application Save successfully and Mail sent to your Approver!");
            }*/
        }


        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                ((SortLeaveActivity)getContext()).loadDetailsFragment();
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