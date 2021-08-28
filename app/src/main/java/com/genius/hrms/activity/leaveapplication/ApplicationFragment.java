package com.genius.hrms.activity.leaveapplication;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.developers.imagezipper.ImageZipper;
import com.genius.hrms.R;

import com.genius.hrms.activity.activity.LoginActivity;
import com.genius.hrms.activity.adapter.CompOffAdapter;
import com.genius.hrms.activity.adapter.DayBreakUpAdapter;
import com.genius.hrms.activity.adapter.LeaveBalanceDetailsAdapter;
import com.genius.hrms.activity.adapter.PreviewAdapter;
import com.genius.hrms.activity.model.CompOffDetailsModel;
import com.genius.hrms.activity.model.DayBreakUpModel;
import com.genius.hrms.activity.model.LeaveBalanceDetailsModel;
import com.genius.hrms.activity.model.PrevieModel;
import com.genius.hrms.activity.model.SpinnerModel;
import com.genius.hrms.activity.utility.Pref;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ApplicationFragment extends Fragment {


    View v;
    RecyclerView rvItem;
    ArrayList<LeaveBalanceDetailsModel> itemList = new ArrayList<>();
    TextView tvRequested, tvApporved, tvRejected, tvPending;
    LinearLayout llLoader, llPending, llRejected, llApproved, llRequested;
    Pref pref;
    TextView tvEmpName, tvApproverName;
    Spinner spRefName, spLeaveType, spLeaveMode;
    ArrayList<String> refList = new ArrayList<>();
    ArrayList<SpinnerModel> mRefList = new ArrayList<>();
    ArrayList<String> leaveTypeList = new ArrayList<>();
    ArrayList<SpinnerModel> mLeaveTypeList = new ArrayList<>();
    ArrayList<String> leaveMode = new ArrayList<>();
    ArrayList<SpinnerModel> mLeaveMode = new ArrayList<>();
    ProgressDialog pd;
    LinearLayout llStrtDate;
    TextView tvStrtDate;
    LinearLayout llEndDate;
    TextView tvEndDate;
    AlertDialog al1, alert1, alert2, alert3, alert4;
    int strtDate;
    ArrayList<String> applicantList = new ArrayList<>();
    ArrayList<SpinnerModel> mApplicantList = new ArrayList<>();
    String applicantId;
    String appid = "", applicantName;
    String typeId = "";
    ImageView imgEndDay, imgStrtDay;
    String startDate, endDate, showEndDate;
    String leaveModeId = "";
    RecyclerView rvBrkupItem;
    ArrayList<DayBreakUpModel> dayBreakupList = new ArrayList<>();
    ArrayList<String> typeIdList = new ArrayList<>();
    ArrayList<String> availdList = new ArrayList<>();
    ArrayList<String> typeAvaild = new ArrayList<>();
    String typeAvailable, preViewResponse;
    DayBreakUpAdapter dayAdapter;
    ArrayList<String> dayBreakupListDetails = new ArrayList<>();
    ArrayList<String> compOffListDetails = new ArrayList<>();
    String dayBreakUpDetails;
    String compOffDetails="";
    LinearLayout llPreview;
    EditText etReason;
    LinearLayout llChoose;
    ImageView imgPic;
    Uri imageUri;
    String encodedImage;
    File file;
    private static final int CAMERA_REQUEST = 1;
    private static final int PDF_REQUEST = 2;
    int attachmentFlag = 0;
    RecyclerView rvPreviewItem;
    ArrayList<PrevieModel> previewItem = new ArrayList<>();
    String leaveType;
    LinearLayout llShow;
    TextView tvAllApplication, tvCancel, tvApproval;
    File pdffile;
    String LeaveValue;
    ProgressDialog pg;
    String category;
    AlertDialog alerDialog1;
    String stringFile = "";
    TextView tvRequestedName, tvApprovedName, tvRejectedName, tvPendingName, tvLeaveTypeName, tvLeaveModeName, tvContactName, tvStartDateName, tvEndDateName, tvReasonName, tvDocName, tvPreviewName;
    String color;
    TextView tvBalance, tvDetail;
    String hCode;
    AlertDialog alert5;
    ArrayList<CompOffDetailsModel> compOffList = new ArrayList<>();
    RecyclerView rvCompOffItem;
    CompOffAdapter compOffAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_application, container, false);
        initView();
        onClick();
        return v;
    }

    @SuppressLint("ResourceType")
    private void initView() {
        rvItem = (RecyclerView) v.findViewById(R.id.rvItem);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rvItem.setLayoutManager(gridLayoutManager);
        tvRequested = (TextView) v.findViewById(R.id.tvRequested);
        tvApporved = (TextView) v.findViewById(R.id.tvApporved);
        tvRejected = (TextView) v.findViewById(R.id.tvRejected);
        tvPending = (TextView) v.findViewById(R.id.tvPending);
        llLoader = (LinearLayout) v.findViewById(R.id.llLoader);
        llPending = (LinearLayout) v.findViewById(R.id.llPending);
        llRejected = (LinearLayout) v.findViewById(R.id.llRejected);
        llApproved = (LinearLayout) v.findViewById(R.id.llApproved);
        llRequested = (LinearLayout) v.findViewById(R.id.llRequested);
        pref = new Pref(getContext());
        getApproverOrNot();
        tvEmpName = (TextView) v.findViewById(R.id.tvEmpName);

        tvApproverName = (TextView) v.findViewById(R.id.tvApproverName);
        spRefName = (Spinner) v.findViewById(R.id.spRefName);
        spLeaveType = (Spinner) v.findViewById(R.id.spLeaveType);
        spLeaveMode = (Spinner) v.findViewById(R.id.spLeaveMode);
        pd = new ProgressDialog(getActivity());
        llStrtDate = (LinearLayout) v.findViewById(R.id.llStrtDate);
        tvStrtDate = (TextView) v.findViewById(R.id.tvStrtDate);
        llEndDate = (LinearLayout) v.findViewById(R.id.llEndDate);
        tvEndDate = (TextView) v.findViewById(R.id.tvEndDate);
        imgEndDay = (ImageView) v.findViewById(R.id.imgEndDay);
        imgStrtDay = (ImageView) v.findViewById(R.id.imgStrtDay);
        spRefName = (Spinner) v.findViewById(R.id.spRefName);
        llPreview = (LinearLayout) v.findViewById(R.id.llPreview);
        etReason = (EditText) v.findViewById(R.id.etReason);
        llChoose = (LinearLayout) v.findViewById(R.id.llChoose);
        imgPic = (ImageView) v.findViewById(R.id.imgPic);
        llShow = (LinearLayout) v.findViewById(R.id.llShow);

        tvAllApplication = (TextView) v.findViewById(R.id.tvAllApplication);
        tvCancel = (TextView) v.findViewById(R.id.tvCancel);
        tvApproval = (TextView) v.findViewById(R.id.tvApproval);
        pg = new ProgressDialog(getContext());
        pg.setMessage("Loading..");
        pg.setCancelable(false);


        color = "<font color='#EE0000'>*</font>";
      /*  String gender = "Gender";
        tvGenderTitle.setText(Html.fromHtml(gender + color));
*/

        tvRequestedName = (TextView) v.findViewById(R.id.tvRequestedName);
        tvApprovedName = (TextView) v.findViewById(R.id.tvApprovedName);
        tvRejectedName = (TextView) v.findViewById(R.id.tvRejectedName);
        tvPendingName = (TextView) v.findViewById(R.id.tvPendingName);
        tvLeaveTypeName = (TextView) v.findViewById(R.id.tvLeaveTypeName);
        tvLeaveModeName = (TextView) v.findViewById(R.id.tvLeaveModeName);
        tvContactName = (TextView) v.findViewById(R.id.tvContactName);
        tvStartDateName = (TextView) v.findViewById(R.id.tvStartDateName);
        tvEndDateName = (TextView) v.findViewById(R.id.tvEndDateName);
        tvReasonName = (TextView) v.findViewById(R.id.tvReasonName);
        tvDocName = (TextView) v.findViewById(R.id.tvDocName);
        tvPreviewName = (TextView) v.findViewById(R.id.tvPreviewName);
        tvBalance = (TextView) v.findViewById(R.id.tvBalance);
        tvDetail = (TextView) v.findViewById(R.id.tvDetail);
        if (pref.getLanguage().equals("hi")) {
            tvRequestedName.setText("अनुरोध किया");
            tvApprovedName.setText("मंजूर की");
            tvRejectedName.setText("अस्वीकृत");
            tvLeaveModeName.setText("अपूर्ण");
            tvContactName.setText("आपातकालीन संपर्क");
            tvStartDateName.setText(Html.fromHtml("आरंभ करने की तिथि" + color));
            tvEndDateName.setText(Html.fromHtml("अंतिम तिथि" + color));
            tvReasonName.setText(Html.fromHtml("कारण" + color));
            tvDocName.setText("दस्तावेज़ अपलोड करें");
            tvLeaveTypeName.setText(Html.fromHtml("प्रकार" + color));
            tvLeaveModeName.setText(Html.fromHtml("मोड" + color));
            tvBalance.setText("बकाया छुट्टियां");
            tvDetail.setText("छुट्टी का अनुरोध विवरण");
            tvPreviewName.setText("पूर्वावलोकन");


        } else {
            tvRequestedName.setText("Requested");
            tvApprovedName.setText("Approved");
            tvRejectedName.setText("Rejected");
            tvLeaveModeName.setText("Pending");
            tvContactName.setText("Emergency Contact");
            tvStartDateName.setText(Html.fromHtml("Start date" + color));
            tvEndDateName.setText(Html.fromHtml("End date" + color));
            tvReasonName.setText(Html.fromHtml("Reason" + color));
            tvDocName.setText("Upload document");
            tvLeaveTypeName.setText(Html.fromHtml("Type" + color));
            tvLeaveModeName.setText(Html.fromHtml("Mode" + color));
            tvBalance.setText("Leave balance details");
            tvDetail.setText("Leave request details");
            tvPreviewName.setText("PREVIEW");


        }


    }


    private void onClick() {


        spLeaveType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {

                    String lId = mLeaveTypeList.get(position).getItemId();
                    leaveType = mLeaveTypeList.get(position).getItem().replaceAll("\\s+", "%20");
                    String[] sep = lId.split("_");
                    typeId = sep[0];
                    category = sep[1];
                    if (pref.getLanguage().equals("hi")) {
                        getHindiMode();
                    } else {
                        getLeaveMode();
                    }
                    Log.d("typeId", typeId);
                    Log.d("category", category);


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spLeaveMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                leaveModeId = mLeaveMode.get(i).getItemId();
                Log.d("leaveModeId",leaveModeId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        imgStrtDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!typeId.equals("")) {
                    showStrtDatePicker();
                } else {
                    Toast.makeText(getContext(), "Please select Leave type", Toast.LENGTH_LONG).show();
                }
            }
        });

        imgEndDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tvStrtDate.getText().toString().equals("")) {
                    showendDatePicker();
                } else {
                    showErrorDialog("Please select Start Date");
                }
            }
        });
        etReason.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etReason.getText().toString().length() > 3) {
                    llPreview.setVisibility(View.VISIBLE);
                } else {
                    llPreview.setVisibility(View.GONE);
                }

            }
        });

        llPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dayBreakupListDetails != null) {
                    if (!tvEndDate.getText().toString().equals("")) {
                        preView();
                    }else {
                        Toast.makeText(getContext(),"End date not selected",Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "please select daily break up details", Toast.LENGTH_LONG).show();
                }
            }
        });

        llChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooseFileDialog();
            }
        });


    }


    private void setAdapter() {
        LeaveBalanceDetailsAdapter lAdaapter = new LeaveBalanceDetailsAdapter(itemList, getContext());
        rvItem.setAdapter(lAdaapter);
    }


    private void getLeaveAllDetails() {
        //names.clear();
        llLoader.setVisibility(View.VISIBLE);
        llRejected.setEnabled(false);
        llPending.setEnabled(false);
        llApproved.setEnabled(false);
        llRequested.setEnabled(false);
        String surl = pref.getIpAddress() + "ghrmsapi/api/Leave/LeaveApplicationDetails?CompanyID=" + pref.getEmpClintId() + "&EmployeeID=" + applicantId + "&ApproverID=" + pref.getEmpId() + "&SecurityCode=" + pref.getSecurityCode();
        Log.d("printurlrequest", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        llLoader.setVisibility(View.GONE);

                        // attendabceInfiList.clear();

                        leaveTypeList.add("Please select");
                        mLeaveTypeList.add(new SpinnerModel("0", "0"));
                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                //for request,reject,etc details
                                JSONArray leaveRequestDetails = responseData.optJSONArray(0);
                                for (int i = 0; i < leaveRequestDetails.length(); i++) {
                                    JSONObject requestObject = leaveRequestDetails.optJSONObject(i);
                                    String Request = requestObject.optString("Request");
                                    tvRequested.setText(Request);
                                    String Approve = requestObject.optString("Approve");
                                    String Reject = requestObject.optString("Reject");
                                    String Pending = requestObject.optString("Pending");
                                    tvApporved.setText(Approve);
                                    tvRejected.setText(Reject);
                                    tvPending.setText(Pending);
                                }
                                //for  balance details
                                JSONArray leaveBalanceArray = responseData.optJSONArray(1);
                                for (int i = 0; i < leaveBalanceArray.length(); i++) {
                                    JSONObject balanceObject = leaveBalanceArray.optJSONObject(i);
                                    final String Code = balanceObject.optString("Code");
                                    final String Opening = balanceObject.optString("Opening");
                                    final String LeaveAvailed = balanceObject.optString("LeaveAvailed");
                                    final String Avaliable = balanceObject.optString("Avaliable");
                                    String LeaveTypeID = balanceObject.optString("LeaveTypeID");
                                    typeAvaild.add(LeaveTypeID + "_" + Avaliable);

                                    LeaveBalanceDetailsModel model = new LeaveBalanceDetailsModel(Code, Opening, LeaveAvailed);
                                    itemList.add(model);
                                }
                                typeAvailable = typeAvaild.toString().replace("]", "").replace("[", "").replaceAll("\\s+", "");
                                ;
                                Log.d("availd", typeAvaild.toString());
                                setAdapter();


                                //leave type

                                JSONArray leaveTypeArray = responseData.optJSONArray(7);
                                if (leaveTypeArray!=null) {
                                    for (int i = 0; i < leaveTypeArray.length(); i++) {
                                        JSONObject typeObject = leaveTypeArray.optJSONObject(i);
                                        String LeaveTypeID = typeObject.optString("LeaveTypeID");
                                        final String Name = typeObject.optString("Name");
                                        if (pref.getLanguage().equals("hi")) {
                                            final Handler textViewHandler2 = new Handler();
                                            new AsyncTask<Void, Void, Void>() {
                                                @Override
                                                protected Void doInBackground(Void... params) {
                                                    TranslateOptions options = TranslateOptions.newBuilder()
                                                            .setApiKey("AIzaSyDL1itt-7WRkrelJeuvOfiC-_SGc3JZ4vY")
                                                            .build();
                                                    Translate translate = options.getService();
                                                    final Translation translation =
                                                            translate.translate(Name,
                                                                    Translate.TranslateOption.sourceLanguage("en"), Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                                                    textViewHandler2.post(new Runnable() {
                                                        @Override
                                                        public void run() {

                                                            Log.d("sssh", translation.getTranslatedText());
                                                            String hLoc = translation.getTranslatedText();
                                                            leaveTypeList.add(hLoc);


                                                        }
                                                    });
                                                    return null;
                                                }

                                                @Override
                                                protected void onPreExecute() {
                                                    super.onPreExecute();


                                                }

                                                @Override
                                                protected void onPostExecute(Void aVoid) {
                                                    super.onPostExecute(aVoid);


                                                }


                                            }.execute();
                                        } else {
                                            leaveTypeList.add(Name);
                                        }


                                        SpinnerModel spModel = new SpinnerModel(Name, LeaveTypeID);
                                        mLeaveTypeList.add(spModel);
                                    }

                                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                            (getContext(), android.R.layout.simple_spinner_item,
                                                    leaveTypeList); //selected item will look like a spinner set from XML
                                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spLeaveType.setAdapter(spinnerArrayAdapter);
                                }


                                //approver name
                                JSONArray approverNameArray = responseData.optJSONArray(2);
                                for (int i = 0; i < approverNameArray.length(); i++) {
                                    JSONObject approverObject = approverNameArray.optJSONObject(i);
                                    final String ApproverName = approverObject.optString("ApproverName");

                                    if (pref.getLanguage().equals("hi")) {
                                        final Handler textViewHandler2 = new Handler();
                                        new AsyncTask<Void, Void, Void>() {
                                            @Override
                                            protected Void doInBackground(Void... params) {
                                                TranslateOptions options = TranslateOptions.newBuilder()
                                                        .setApiKey("AIzaSyDL1itt-7WRkrelJeuvOfiC-_SGc3JZ4vY")
                                                        .build();
                                                Translate translate = options.getService();
                                                final Translation translation =
                                                        translate.translate(ApproverName,
                                                                Translate.TranslateOption.sourceLanguage("en"), Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                                                textViewHandler2.post(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        Log.d("sssh", translation.getTranslatedText());
                                                        String hLoc = translation.getTranslatedText();
                                                        tvApproverName.setText("स्वीकृति देने वाला नाम:" + hLoc);


                                                    }
                                                });
                                                return null;
                                            }

                                            @Override
                                            protected void onPreExecute() {
                                                super.onPreExecute();


                                            }

                                            @Override
                                            protected void onPostExecute(Void aVoid) {
                                                super.onPostExecute(aVoid);


                                            }


                                        }.execute();
                                    } else {
                                        tvApproverName.setText("Approver Name:" + ApproverName);
                                    }


                                }


                                //refrence name
                                refList.clear();
                                mRefList.clear();
                                refList.add("Please select");
                                mRefList.add(new SpinnerModel("0", "0"));
                                JSONArray leaveRefArray = responseData.optJSONArray(3);
                                for (int i = 0; i < leaveRefArray.length(); i++) {
                                    JSONObject refObject = leaveRefArray.optJSONObject(i);
                                    String ApplicantName = refObject.optString("ApplicantName");
                                    String ID = refObject.optString("ID");
                                    refList.add(ApplicantName);

                                    SpinnerModel spModel = new SpinnerModel(ApplicantName, ID);
                                    mRefList.add(spModel);
                                }

                                ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>
                                        (getContext(), android.R.layout.simple_spinner_item,
                                                refList); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spRefName.setAdapter(spinnerArrayAdapter1);


                                llRejected.setEnabled(true);
                                llPending.setEnabled(true);
                                llApproved.setEnabled(true);
                                llRequested.setEnabled(true);


                            } else {


                                llRejected.setEnabled(false);
                                llPending.setEnabled(false);
                                llApproved.setEnabled(false);
                                llRequested.setEnabled(false);


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.GONE);
                llRejected.setEnabled(false);
                llPending.setEnabled(false);
                llApproved.setEnabled(false);
                llRequested.setEnabled(false);


                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);


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
                        mApplicantList.clear();
                        applicantList.clear();
                        applicantList.add("Please select");
                        mApplicantList.add(new SpinnerModel("0", "0"));


                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                showApproverDialog();
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    final String Name = obj.optString("Name");
                                    String ApplicantID = obj.optString("ApplicantID");

                                    if (pref.getLanguage().equals("hi")) {
                                        final Handler textViewHandler2 = new Handler();
                                        new AsyncTask<Void, Void, Void>() {
                                            @Override
                                            protected Void doInBackground(Void... params) {
                                                TranslateOptions options = TranslateOptions.newBuilder()
                                                        .setApiKey("AIzaSyDL1itt-7WRkrelJeuvOfiC-_SGc3JZ4vY")
                                                        .build();
                                                Translate translate = options.getService();
                                                final Translation translation =
                                                        translate.translate(Name,
                                                                Translate.TranslateOption.sourceLanguage("en"), Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                                                textViewHandler2.post(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        Log.d("sssh", translation.getTranslatedText());
                                                        String hLoc = translation.getTranslatedText();
                                                        applicantList.add(hLoc);


                                                    }
                                                });
                                                return null;
                                            }

                                            @Override
                                            protected void onPreExecute() {
                                                super.onPreExecute();
                                                pd.show();


                                            }

                                            @Override
                                            protected void onPostExecute(Void aVoid) {
                                                super.onPostExecute(aVoid);
                                                pd.dismiss();


                                            }


                                        }.execute();
                                    } else {
                                        applicantList.add(Name);
                                    }

                                    SpinnerModel spModel = new SpinnerModel(Name, ApplicantID);
                                    mApplicantList.add(spModel);


                                }

                                ((LeaveApplicationActivity) getContext()).approverVisibility();
                                //llShow.setVisibility(View.VISIBLE);


                            } else {
                                applicantId = pref.getEmpId();
                                getLeaveAllDetails();
                                ((LeaveApplicationActivity) getContext()).approverHidden();
                                // llShow.setVisibility(View.GONE);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.GONE);


                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }


    private void getLeaveMode() {


        //names.clear();
//        llLoader.setVisibility(View.VISIBLE);

        pd.setMessage("Loading...");
        pd.setCancelable(true);
        pd.show();


        String surl = pref.getIpAddress() + "ghrmsapi/api/Leave/LeaveMode?CompanyID=" + pref.getEmpClintId() + "&EmployeeID=" + applicantId + "&&LeaveTypeID=" + typeId + "&SecurityCode=" + pref.getSecurityCode();
        Log.d("printurlbalance", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
//                        llLoader.setVisibility(View.GONE);

                        pd.dismiss();
                        // attendabceInfiList.clear();

                        leaveMode.clear();
                        mLeaveMode.clear();


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
                                    final String VALUE = obj.optString("VALUE");
                                    String ID = obj.optString("ID");

                                    SpinnerModel spModel = new SpinnerModel(VALUE, ID);
                                    mLeaveMode.add(spModel);
                                    leaveMode.add(VALUE);


                                }

                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (getContext(), android.R.layout.simple_spinner_item,
                                                leaveMode); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spLeaveMode.setAdapter(spinnerArrayAdapter);


                            } else {


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.GONE);


                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    private void getHindiMode() {


        //names.clear();
//        llLoader.setVisibility(View.VISIBLE);
        leaveMode.add("पूरा दिन");
        leaveMode.add("पूरा और आधा दिन");
        leaveMode.add("पूरा दिन");
        mLeaveMode.add(new SpinnerModel("पूरा दिन", "1"));
        mLeaveMode.add(new SpinnerModel("पूरा और आधा दिन", "2"));
        mLeaveMode.add(new SpinnerModel("आधा दिन", "0"));

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_spinner_item,
                        leaveMode); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLeaveMode.setAdapter(spinnerArrayAdapter);


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


                        strtDate = dayOfMonth + monthOfYear + year;
                        int month = (monthOfYear + 1);
                        startDate = month + "/" + dayOfMonth + "/" + year;
                        tvStrtDate.setText(startDate);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker();
        datePickerDialog.show();

    }


    private void showendDatePicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        int enddate = dayOfMonth + monthOfYear + year;
                        int month = (monthOfYear + 1);
                        endDate = month + "/" + dayOfMonth + "/" + year;
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                        Date strDate = null;
                        try {
                            strDate = sdf.parse(startDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                        Date striDate = null;
                        try {
                            striDate = df.parse(endDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (striDate.getTime() > strDate.getTime() ||striDate.getTime() == strDate.getTime()) {
                            validationChecking();
                        }else {
                            showErrorDialog("End date should not before than Start date");
                        }



                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker();
        datePickerDialog.show();

    }


    private void showErrorDialog(String text) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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


    private void showApproverDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.approver_dialog, null);
        dialogBuilder.setView(dialogView);
        TextView tvDialog = (TextView) dialogView.findViewById(R.id.tvDialog);
        if (pref.getLanguage().equals("hi")) {
            tvDialog.setText("स्वयं के लिए आवेदन करने या सूची से चयन करने के लिए स्व पर क्लिक करें");
        } else {
            tvDialog.setText("Click self to apply for own or select from the list");
        }
        Spinner spSub = (Spinner) dialogView.findViewById(R.id.spSub);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_spinner_item,
                        applicantList); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSub.setAdapter(spinnerArrayAdapter);
        spSub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    appid = mApplicantList.get(i).getItemId();
                    applicantName = mApplicantList.get(i).getItem();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Button btnSubmit = (Button) dialogView.findViewById(R.id.btnSubmit);
        if (pref.getLanguage().equals("hi")) {
            btnSubmit.setText("प्रस्तुत");
        } else {
            btnSubmit.setText("Submit");
        }
        Button btnSelf = (Button) dialogView.findViewById(R.id.btnSelf);
        if (pref.getLanguage().equals("hi")) {
            btnSelf.setText("स्वयं");
        } else {
            btnSelf.setText("Self");
        }
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!appid.equals("")) {

                    applicantId = appid;
                    alert1.dismiss();
                    getLeaveAllDetails();
                    if (pref.getLanguage().equals("hi")) {
                        final Handler textViewHandler2 = new Handler();
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... params) {
                                TranslateOptions options = TranslateOptions.newBuilder()
                                        .setApiKey("AIzaSyDL1itt-7WRkrelJeuvOfiC-_SGc3JZ4vY")
                                        .build();
                                Translate translate = options.getService();
                                final Translation translation =
                                        translate.translate(applicantName,
                                                Translate.TranslateOption.sourceLanguage("en"), Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                                textViewHandler2.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        Log.d("sssh", translation.getTranslatedText());
                                        String hLoc = translation.getTranslatedText();
                                        tvEmpName.setText(hLoc + " का छुट्टी का आवेदन");


                                    }
                                });
                                return null;
                            }

                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();


                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);


                            }


                        }.execute();
                    } else {
                        tvEmpName.setText("Leave application of " + applicantName);
                    }

                } else {
                    Toast.makeText(getContext(), "Please select leave applicant", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnSelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applicantId = pref.getEmpId();
                alert1.dismiss();
                getLeaveAllDetails();
                if (pref.getLanguage().equals("hi")) {
                    final Handler textViewHandler2 = new Handler();
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            TranslateOptions options = TranslateOptions.newBuilder()
                                    .setApiKey("AIzaSyDL1itt-7WRkrelJeuvOfiC-_SGc3JZ4vY")
                                    .build();
                            Translate translate = options.getService();
                            final Translation translation =
                                    translate.translate(pref.getEmpName(),
                                            Translate.TranslateOption.sourceLanguage("en"), Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                            textViewHandler2.post(new Runnable() {
                                @Override
                                public void run() {

                                    Log.d("sssh", translation.getTranslatedText());
                                    String hLoc = translation.getTranslatedText();
                                    tvEmpName.setText(hLoc + " का छुट्टी का आवेदन");


                                }
                            });
                            return null;
                        }

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();


                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);


                        }


                    }.execute();
                } else {
                    tvEmpName.setText("Leave application of " + pref.getEmpName());
                }
            }
        });

        alert1 = dialogBuilder.create();
        alert1.setCancelable(true);
        Window window = alert1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.TOP);
        alert1.show();


    }

    private void validationChecking() {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading...");
        pd.setCancelable(true);
        pd.show();

        String surl = pref.getIpAddress() + "ghrmsapi/api/Leave/CheckLeaveStartDayStatus?CompanyID=" + pref.getEmpClintId() + "&EmployeeID=" + applicantId + "&StartDate=" + startDate + "&EndDate=" + endDate + "&LeaveTypeID=" + typeId + "&LeaveMode=" + leaveModeId + "&SecurityCode=" + pref.getSecurityCode();
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
                                tvEndDate.setText(endDate);
                                if (leaveModeId.equals("0") || leaveModeId.equals("2")) {
                                    dayBreakupListDetails.clear();
                                    showDailyBrkUpDialog();

                                } else if (leaveModeId.equals("1")){


                                }else {
                                    showCompOffDialog();
                                }


                            } else {
                                endDate = "";
                                tvEndDate.setText("");
                                showErrorDialog(responseText);

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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    private void showDailyBrkUpDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.day_breakup_dialog, null);
        dialogBuilder.setView(dialogView);
        rvBrkupItem = (RecyclerView) dialogView.findViewById(R.id.rvBrkupItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvBrkupItem.setLayoutManager(layoutManager);
        getDayBreakUp();
        Button btnSubmit = (Button) dialogView.findViewById(R.id.btnSubmit);
        if (pref.getLanguage().equals("hi")) {
            btnSubmit.setText("प्रस्तुत");
        } else {
            btnSubmit.setText("Submit");
        }
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert2.dismiss();
                if (category.equals("1") || category.equals("3")) {
                    showCompOffDialog();
                } else {

                }
            }
        });
        Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);
        if (pref.getLanguage().equals("hi")) {
            btnCancel.setText("रद्द करना");
        } else {
            btnCancel.setText("Cancel");
        }
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayBreakupListDetails.clear();
                alert2.dismiss();
                endDate="";
                tvEndDate.setText("");
            }
        });


        alert2 = dialogBuilder.create();
        alert2.setCancelable(false);
        Window window = alert2.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.TOP);
        alert2.show();


    }


    private void showCompOffDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.compoff_dialog, null);
        dialogBuilder.setView(dialogView);
        rvCompOffItem = (RecyclerView) dialogView.findViewById(R.id.rvCompOffItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvCompOffItem.setLayoutManager(layoutManager);
        getCompOffDetails();
        Button btnSubmit = (Button) dialogView.findViewById(R.id.btnSubmit);
        if (pref.getLanguage().equals("hi")) {
            btnSubmit.setText("प्रस्तुत");
        } else {
            btnSubmit.setText("Submit");
        }
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (compOffListDetails.size()>0) {
                    alert5.dismiss();
                }else {
                    Toast.makeText(getContext(),"Please select item",Toast.LENGTH_LONG).show();
                }

            }
        });
        Button btnCancel = (Button) dialogView.findViewById(R.id.btnDiscard);
        if (pref.getLanguage().equals("hi")) {
            btnCancel.setText("रद्द करना");
        } else {
            btnCancel.setText("Cancel");
        }
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  dayBreakupListDetails.clear();
                alert5.dismiss();
                endDate="";
                tvEndDate.setText("");
            }
        });


        alert5 = dialogBuilder.create();
        alert5.setCancelable(false);
        Window window = alert5.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.TOP);
        alert5.show();


    }


    private void showChooseFileDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.choose_file_dialog, null);
        dialogBuilder.setView(dialogView);
        Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert4.dismiss();
            }
        });
        ImageView imgCamera = (ImageView) dialogView.findViewById(R.id.imgCamera);
        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraIntent();
            }
        });
        ImageView imgPdf = (ImageView) dialogView.findViewById(R.id.imgPDF);
        imgPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPDFChooser();
            }
        });


        alert4 = dialogBuilder.create();
        alert4.setCancelable(true);
        Window window = alert4.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alert4.show();


    }

    private void getDayBreakUp() {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading...");
        pd.setCancelable(true);
        pd.show();

        String surl = pref.getIpAddress() + "ghrmsapi/api/Leave/DayDetails?CompanyID=" + pref.getEmpClintId() + "&EmployeeID=" + applicantId + "&StartDate=" + startDate + "&EndDate=" + endDate + "&LeaveTypeID=" + typeId + "&SecurityCode=" + pref.getSecurityCode();
        Log.d("printurlbalance", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
//                        llLoader.setVisibility(View.GONE);

                        pd.dismiss();

                        dayBreakupList.clear();


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
                                    String BreakDate = obj.optString("BreakDate");
                                    String DateName = obj.optString("DateName");
                                    String DayAccess = obj.optString("DayAccess");
                                    String DayAccessDesc = obj.optString("DayAccessDesc");
                                    if (DayAccess.equals("-1")) {
                                        dayBreakupListDetails.add(BreakDate + "_" + "0" + "_" + "0");
                                    } else {

                                    }

                                    DayBreakUpModel spModel = new DayBreakUpModel(BreakDate, DateName, DayAccess, DayAccessDesc);
                                    dayBreakupList.add(spModel);


                                }

                                dayAdapter = new DayBreakUpAdapter(dayBreakupList, ApplicationFragment.this, getContext());
                                rvBrkupItem.setAdapter(dayAdapter);


                            } else {


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.GONE);


                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    public void updateStatus(int position, boolean status) {
        dayBreakupList.get(position).setSelected(status);
        if (dayBreakupList.get(position).isSelected() == true) {
            dayBreakupListDetails.add(dayBreakupList.get(position).getBrkupDate() + "_" + dayBreakupList.get(position).getDayModeValue() + "_" + dayBreakupList.get(position).getBalance());


        } else {
            dayBreakupListDetails.remove(position);
        }


        dayBreakUpDetails = dayBreakupListDetails.toString().replace("[", "").replace("]", "").replaceAll("\\s+", "");
        Log.d("detailslist", dayBreakUpDetails);

        /*Log.d("arpan", itemList.toString());
        String i = itemList.toString();
        String d = i.replace("[", "").replace("]", "");
        empId = d.replaceAll("\\s+", "");
        String emp=empName.toString();
        String replace=emp.replace("[", "").replace("]", "");
        tvEmpName.setText(replace);
*/

        dayAdapter.notifyDataSetChanged();
    }

    public void updateStatusForComPff(int position, boolean status) {
        compOffList.get(position).setSelected(status);
        if (compOffList.get(position).isSelected() == true) {
            compOffListDetails.add(compOffList.get(position).getBrkUpDate() + "_" + compOffList.get(position).getDayValue() );


        } else {
            compOffListDetails.remove(position);
        }


        compOffDetails = compOffListDetails.toString().replace("[", "").replace("]", "").replaceAll("\\s+", "");
        Log.d("compffdetails", compOffDetails);

        /*Log.d("arpan", itemList.toString());
        String i = itemList.toString();
        String d = i.replace("[", "").replace("]", "");
        empId = d.replaceAll("\\s+", "");
        String emp=empName.toString();
        String replace=emp.replace("[", "").replace("]", "");
        tvEmpName.setText(replace);
*/

        compOffAdapter.notifyDataSetChanged();
    }


    private void preView() {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading...");
        pd.setCancelable(true);
        pd.show();

        String surl = pref.getIpAddress() + "ghrmsapi/api/Leave/CheckLeaveViewSummary?CompanyID=" + pref.getEmpClintId() + "&EmployeeID=" + applicantId + "&StartDate=" + startDate + "&EndDate=" + endDate + "&LeaveTypeID=" + typeId + "&LeaveMode=" + leaveModeId + "&StrAvailableBalance=" + typeAvailable + "&StrDayBreakUp=" + dayBreakUpDetails + "&IsAttachment=" + attachmentFlag + "&SecurityCode=" + pref.getSecurityCode();
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
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                if (leaveModeId.equals("1")) {
                                    dayBreakUpDetails = job1.optString("responseData");
                                } else {

                                }
                                showPreviewDialog();


                            } else {

                                showErrorDialog(responseText);


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.GONE);


                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }


    private void cameraIntent() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getActivity().getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_REQUEST:

                if (resultCode == RESULT_OK) {
                    try {
                        try {

                            //messageAlert();
                            String imageurl = /*"file://" +*/ getRealPathFromURIPath(imageUri);
                            file = new File(imageurl);

                            // Log.d("imageSixw", String.valueOf(getReadableFileSize(compressedImageFile.length())));


                            BitmapFactory.Options o = new BitmapFactory.Options();
                            o.inSampleSize = 6;
                            //Bitmap bm = cropToSquare(BitmapFactory.decodeFile(imageurl, o));
                            Bitmap bm = new ImageZipper(getContext()).compressToBitmap(file);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                            byte[] b = baos.toByteArray();
                            encodedImage = encodeFileToBase64Binary(file);
                            Log.d("encoded", encodedImage);
                            imgPic.setImageBitmap(bm);
                            attachmentFlag = 1;
                            alert4.dismiss();
                            String contentType = "image/jpg";
                            String[] brkDown = imageurl.split("/");
                            String name = brkDown[5];
                            stringFile = name + "_" + encodedImage + "_" + contentType;
                            Log.d("stringFile", stringFile);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }

                }
                break;
            case PDF_REQUEST:
                if (requestCode == PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

                    Uri selectedFileURI = data.getData();
                    pdffile = new File(getRealPDFPathFromURI(selectedFileURI));
                    alert4.dismiss();
                    encodedImage = encodeFileToBase64Binary(pdffile);
                    String filePath = getRealPDFPathFromURI(selectedFileURI);
                    String[] brkDown = filePath.split("/");
                    String name = brkDown[5];
                    String contentType = "application/pdf";
                    stringFile = name + "_" + encodedImage + "_" + contentType;


                }


                break;


        }


    }

    private String getRealPathFromURIPath(Uri contentURI) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(contentURI, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    private void showPreviewDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.preview_dialog, null);
        dialogBuilder.setView(dialogView);
        rvPreviewItem = (RecyclerView) dialogView.findViewById(R.id.rvPreviewItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvPreviewItem.setLayoutManager(layoutManager);
        getPreviewItem();
        TextView tvReason = (TextView) dialogView.findViewById(R.id.tvReason);
        TextView tvValue = (TextView) dialogView.findViewById(R.id.tvValue);
        TextView tvEndDate = (TextView) dialogView.findViewById(R.id.tvEndDate);
        TextView tvStrtDate = (TextView) dialogView.findViewById(R.id.tvStrtDate);
        TextView tvType = (TextView) dialogView.findViewById(R.id.tvType);

        Button btnSubmit = (Button) dialogView.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leaveSave();
            }
        });
        Button btnDiscard = (Button) dialogView.findViewById(R.id.btnDiscard);
        btnDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert3.dismiss();
            }
        });

        if (pref.getLanguage().equals("hi")) {
            tvReason.setText("कारण");
            tvValue.setText("मूल्य");
            tvEndDate.setText("अंतिम तिथि");
            tvStrtDate.setText("आरंभ तिथि");
            tvType.setText("प्रकार");
            btnSubmit.setText("प्रस्तुत");
            btnDiscard.setText("रद्द करें");
        } else {
            tvReason.setText("Reason");
            tvValue.setText("Value");
            tvEndDate.setText("End date");
            tvStrtDate.setText("Start date");
            tvType.setText("Type");
            btnDiscard.setText("Discard");
            btnSubmit.setText("Submit");
        }


        alert3 = dialogBuilder.create();
        alert3.setCancelable(false);
        Window window = alert3.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.TOP);
        alert3.show();


    }

    private void getPreviewItem() {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading...");
        pd.setCancelable(true);
        pd.show();
        String surl = pref.getIpAddress() + "ghrmsapi/api/Leave/BindViewSummary?CompanyID=" + pref.getEmpClintId() + "&EmployeeID=" + applicantId + "&StartDate=" + startDate + "&EndDate=" + endDate + "&StrDayBreakUp=" + dayBreakUpDetails + "&LeaveType=" + leaveType + "&Reason=" + etReason.getText().toString().replaceAll("\\s+", "%20") + "&IsAttachment=" + attachmentFlag + "&SecurityCode=" + pref.getSecurityCode();
        Log.d("printurlbalance", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
//                        llLoader.setVisibility(View.GONE);

                        pd.dismiss();

                        previewItem.clear();


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
                                    String LeaveType = obj.optString("LeaveType");
                                    String StartDate = obj.optString("StartDate");
                                    String EndDate = obj.optString("EndDate");
                                    LeaveValue = obj.optString("LeaveValue");
                                    String Reason = obj.optString("Reason");


                                    PrevieModel spModel = new PrevieModel(LeaveType, StartDate, EndDate, LeaveValue, Reason);
                                    previewItem.add(spModel);


                                }

                                PreviewAdapter preAdapter = new PreviewAdapter(previewItem, getContext());
                                rvPreviewItem.setAdapter(preAdapter);


                            } else {


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    private String encodeFileToBase64Binary(File yourFile) {
        int size = (int) yourFile.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(yourFile));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String encoded = Base64.encodeToString(bytes, Base64.NO_WRAP);
        return encoded;
    }


    private void showPDFChooser() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PDF_REQUEST);


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String getRealPDFPathFromURI(Uri contentURI) {
        final String id = DocumentsContract.getDocumentId(contentURI);
        final Uri contentUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);

    }


    private void leaveSave() {
        AndroidNetworking.upload(pref.getIpAddress() + "ghrmsapi/api/Leave/LeaveAdd")
                .addMultipartParameter("CompanyID", pref.getEmpClintId())
                .addMultipartParameter("EmployeeId", applicantId)
                .addMultipartParameter("StartDate", startDate)
                .addMultipartParameter("EndDate", endDate)
                .addMultipartParameter("LeaveTypeID", typeId)
                .addMultipartParameter("LeaveMode", leaveModeId)
                .addMultipartParameter("AppliedLeave", LeaveValue)
                .addMultipartParameter("Reasons", etReason.getText().toString())
                .addMultipartParameter("LeaveCategory", category)
                .addMultipartParameter("StrDayBreakUp", dayBreakUpDetails)
                .addMultipartParameter("StrCompOff",compOffDetails)
                .addMultipartParameter("StrFile", stringFile)
                .addMultipartParameter("createdby", pref.getEmpId())
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
                alert3.dismiss();
                ((LeaveApplicationActivity) getContext()).loadDetailsFragment();
            }
        });

        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }

    private void getCompOffDetails() {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading...");
        pd.setCancelable(true);
        pd.show();
        String surl = pref.getIpAddress()+"ghrmsapi/api/Leave/CompBreakUp?CompanyID=" + pref.getEmpClintId() + "&EmployeeID=" + pref.getEmpId() + "&StartDate=" + startDate + "&EndDate=" + endDate + "&LeaveTypeID=" + typeId + "&Iscompoff=" + category + "&SecurityCode=" + pref.getSecurityCode();
        Log.d("printcompff", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
//                        llLoader.setVisibility(View.GONE);
                        compOffList.clear();
                        pd.dismiss();


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
                                    String CoffDate = obj.optString("CoffDate");
                                    String LeaveValue = obj.optString("LeaveValue");


                                    CompOffDetailsModel spModel = new CompOffDetailsModel(CoffDate, LeaveValue);
                                    compOffList.add(spModel);


                                }

                                compOffAdapter = new CompOffAdapter(compOffList, ApplicationFragment.this, getContext());
                                rvCompOffItem.setAdapter(compOffAdapter);


                            } else {


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


}
