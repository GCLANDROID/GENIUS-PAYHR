package com.genius.payhrms.activity.leaveapplication;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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


import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import com.developers.imagezipper.ImageZipper;
import com.genius.payhrms.R;

import com.genius.payhrms.activity.activity.LoginActivity;
import com.genius.payhrms.activity.adapter.CompOffAdapter;
import com.genius.payhrms.activity.adapter.DayBreakUpAdapter;
import com.genius.payhrms.activity.adapter.LeaveBalanceDetailsAdapter;
import com.genius.payhrms.activity.adapter.MultipleImageAdapter;
import com.genius.payhrms.activity.adapter.PreviewAdapter;
import com.genius.payhrms.activity.model.CompOffDetailsModel;
import com.genius.payhrms.activity.model.DayBreakUpModel;
import com.genius.payhrms.activity.model.LeaveBalanceDetailsModel;
import com.genius.payhrms.activity.model.MultipleDocModel;
import com.genius.payhrms.activity.model.PrevieModel;
import com.genius.payhrms.activity.model.SpinnerModel;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.FileToBase64Converter;
import com.genius.payhrms.activity.utility.FileUtils;
import com.genius.payhrms.activity.utility.Pref;
import com.genius.payhrms.activity.utility.RealPathUtil;
import com.genius.payhrms.activity.utility.SecurityCode;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import static android.os.Build.VERSION.SDK_INT;

/**
 * A simple {@link Fragment} subclass.
 */
public class ApplicationFragment extends Fragment {
    private static final String TAG = "ApplicationFragment";

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
    ArrayList<String> halfdetails = new ArrayList<>();
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
    private static final int GALLERY_IMAGE_SELECTION = 3;
    int attachmentFlag = 0,multipleImageSelected = 0;
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
    TextView tvRequestedName, tvApprovedName, tvRejectedName, tvPendingName, tvLeaveTypeName, tvLeaveModeName, tvContactName, tvStartDateName, tvEndDateName, tvReasonName, tvDocName, tvPreviewName,txtLengthCount;
    String color;
    TextView tvBalance, tvDetail;
    String hCode;
    AlertDialog alert5;
    ArrayList<CompOffDetailsModel> compOffList = new ArrayList<>();
    RecyclerView rvCompOffItem;
    CompOffAdapter compOffAdapter;
    LinearLayout lnBalance,lnDocument;
    RecyclerView rvMulImages;
    ArrayList<Uri> imageURI;
    ArrayList<MultipleDocModel> multipleImageUriList = new ArrayList<>();
    MultipleImageAdapter multipleImageAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_application, container, false);
        Log.e(TAG, "onCreateView: Dayco Matthews");
        initView();
        onClick();
        return v;
    }

    @SuppressLint("ResourceType")
    private void initView() {
        lnBalance=(LinearLayout)v.findViewById(R.id.lnBalance);
        lnDocument=(LinearLayout)v.findViewById(R.id.lnDocument);
        rvItem = (RecyclerView) v.findViewById(R.id.rvItem);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rvItem.setLayoutManager(gridLayoutManager);
        rvMulImages = (RecyclerView) v.findViewById(R.id.rvMulImages);
        GridLayoutManager gridLayoutManagerMulImages = new GridLayoutManager(getContext(), 4);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvMulImages.setLayoutManager(linearLayoutManager);
        tvRequested = (TextView) v.findViewById(R.id.tvRequested);
        tvApporved = (TextView) v.findViewById(R.id.tvApporved);
        tvRejected = (TextView) v.findViewById(R.id.tvRejected);
        tvPending = (TextView) v.findViewById(R.id.tvPending);
        txtLengthCount = (TextView) v.findViewById(R.id.txtLengthCount);
        llLoader = (LinearLayout) v.findViewById(R.id.llLoader);
        llPending = (LinearLayout) v.findViewById(R.id.llPending);
        llRejected = (LinearLayout) v.findViewById(R.id.llRejected);
        llApproved = (LinearLayout) v.findViewById(R.id.llApproved);
        llRequested = (LinearLayout) v.findViewById(R.id.llRequested);
        pref = new Pref(getContext());
        if (pref.getSecurityCode().equals("1167")){
            lnBalance.setVisibility(View.GONE);
            lnDocument.setVisibility(View.GONE);
        }else {
            lnBalance.setVisibility(View.VISIBLE);
            lnDocument.setVisibility(View.VISIBLE);
        }
        JSONObject object=new JSONObject();
        try {
            object.put("AEMEmployeeID",pref.getEmpId());
            object.put("CompanyID",pref.getEmpClintId());
            object.put("SecurityCode",pref.getSecurityCode());
            getApproverOrNot(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                        JSONObject jsonObject=new JSONObject();
                        try {
                            jsonObject.put("CompanyID",pref.getEmpClintId());
                            jsonObject.put("EmployeeID",applicantId);
                            jsonObject.put("LeaveTypeID",typeId);
                            jsonObject.put("SecurityCode",pref.getSecurityCode());
                            getLeaveMode(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.e("typeId", typeId);
                    Log.e("category", category);
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
                tvEndDate.setText("");
                endDate="";
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
                txtLengthCount.setText(etReason.getText().toString().length()+"/"+150);
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
                        //preView();
                        JSONObject object = new JSONObject();
                        try {
                            object.put("CompanyID", pref.getEmpClintId());
                            object.put("EmployeeID", applicantId);
                            object.put("StartDate", startDate);
                            object.put("endDate", endDate);
                            object.put("LeaveTypeID", typeId);
                            object.put("LeaveMode", leaveModeId);
                            object.put("StrAvailableBalance", typeAvailable);
                            object.put("StrDayBreakUp", dayBreakUpDetails);
                            object.put("IsAttachment", attachmentFlag);
                            object.put("SecurityCode", pref.getSecurityCode());
                            preView(object);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
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



    public void getLeaveAllDetails(JSONObject jsonObject) {
        Log.e(TAG, "getLeaveAllDetails: called: "+jsonObject);
        llLoader.setVisibility(View.VISIBLE);
        llRejected.setEnabled(false);
        llPending.setEnabled(false);
        llApproved.setEnabled(false);
        llRequested.setEnabled(false);

        AndroidNetworking.post(Api.sLeaveDetails)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        llLoader.setVisibility(View.GONE);
                        JSONObject job1 = response;
                        Log.e(TAG, "getLeaveAllDetails: " + job1);

                        leaveTypeList.add("Please select");
                        mLeaveTypeList.add(new SpinnerModel("0", "0"));
                        int Response_Code = job1.optInt("Response_Code");
                        String Response_Message=job1.optString("Response_Message");
                        if (Response_Code == 101) {
                            String responseData = job1.optString("Response_Data");
                            try {
                                JSONObject jsonArray=new JSONObject(responseData);
                                String Table1=jsonArray.optString("Table1");
                                Log.e(TAG, "onResponse: Table1: "+Table1);
                                JSONArray leaveBalanceArray = new JSONArray(Table1);
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

                                String Table=jsonArray.optString("Table");
                                JSONArray leaveReqArray = new JSONArray(Table);
                                for (int i = 0; i < leaveReqArray.length(); i++) {
                                    JSONObject requestObject = leaveReqArray.optJSONObject(i);
                                    String Request = requestObject.optString("Request");
                                    tvRequested.setText(Request);
                                    String Approve = requestObject.optString("Approve");
                                    String Reject = requestObject.optString("Reject");
                                    String Pending = requestObject.optString("Pending");
                                    tvApporved.setText(Approve);
                                    tvRejected.setText(Reject);
                                    tvPending.setText(Pending);
                                }

                                String Table7=jsonArray.optString("Table7");
                                Log.e(TAG, "onResponse: Table7: "+Table7);
                                JSONArray leaveTypeArray = new JSONArray(Table7);
                                for (int i = 0; i < leaveTypeArray.length(); i++) {
                                    JSONObject typeObject = leaveTypeArray.optJSONObject(i);
                                    String LeaveTypeID = typeObject.optString("LeaveTypeID");
                                    final String Name;
                                    if (typeObject.has("Name")){
                                        Name = typeObject.optString("Name");
                                    } else {
                                        Name = typeObject.optString("LeaveTypeName");
                                    }

                                    if (pref.getLanguage().equals("hi")) {
                                        /*final Handler textViewHandler2 = new Handler();
                                        new AsyncTask<Void, Void, Void>() .execute();*/
                                    } else {
                                        leaveTypeList.add(Name);
                                    }

                                    Log.e(TAG, "LEAVE NAME: name: "+Name+" LeaveTypeID: "+LeaveTypeID);
                                    SpinnerModel spModel = new SpinnerModel(Name, LeaveTypeID);
                                    mLeaveTypeList.add(spModel);
                                }

                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (getContext(), android.R.layout.simple_spinner_item,
                                                leaveTypeList); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spLeaveType.setAdapter(spinnerArrayAdapter);

                                String Table2=jsonArray.optString("Table2");
                                JSONArray leaveApproverArray = new JSONArray(Table2);
                                for (int i = 0; i < leaveApproverArray.length(); i++) {
                                    JSONObject approverObject = leaveApproverArray.optJSONObject(i);
                                    final String ApproverName = approverObject.optString("ApproverName");

                                    if (pref.getLanguage().equals("hi")) {
                                        final Handler textViewHandler2 = new Handler();
                                        new AsyncTask<Void, Void, Void>() {
                                            @Override
                                            protected Void doInBackground(Void... params) {
                                                TranslateOptions options = TranslateOptions.newBuilder()
                                                        .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
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
                            } catch (Exception e) {
                                e.printStackTrace();
                            }





                            // boolean _status = job1.getBoolean("status");


                            // do anything with response
                        }else {

                        }
                    }

                    @Override
                    public void onError(ANError error) {

                    }
                });
    }


    private void getApproverOrNot(JSONObject jsonObject) {
        Log.e(TAG, "getApproverOrNot: called: "+jsonObject.toString());
        final ProgressDialog pd=new ProgressDialog(getContext());
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(Api.sapprovercheckapi)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mApplicantList.clear();
                        applicantList.clear();
                        applicantList.add("Please select");
                        mApplicantList.add(new SpinnerModel("0", "0"));

                        pd.dismiss();
                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);


                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                            String responseData = job1.optString("Response_Data");
                            try {
                                JSONArray jsonArray=new JSONArray(responseData);
                                if (jsonArray.length()>0) {

                                    showApproverDialog();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        final String Name = obj.optString("Name");
                                        String ApplicantID = obj.optString("ApplicantID");

                                        if (pref.getLanguage().equals("hi")) {
                                            final Handler textViewHandler2 = new Handler();
                                            new AsyncTask<Void, Void, Void>() {
                                                @Override
                                                protected Void doInBackground(Void... params) {
                                                    TranslateOptions options = TranslateOptions.newBuilder()
                                                            .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
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
                                    //getLeaveAllDetails();
                                    ((LeaveApplicationActivity) getContext()).approverHidden();
                                    // llShow.setVisibility(View.GONE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }





                            // boolean _status = job1.getBoolean("status");


                            // do anything with response
                        }else {
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
                    }

                    @Override
                    public void onError(ANError error) {

                        pd.dismiss();


                    }
                });
    }




    public void getLeaveMode(JSONObject jsonObject) {
        Log.e(TAG, "getLeaveMode: called");
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();

        AndroidNetworking.post(Api.sLeaveModeapi)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        leaveMode.clear();
                        mLeaveMode.clear();

                        JSONObject job1 = response;
                        pd.dismiss();
                        Log.e("response12", "@@@@@@" + job1);


                        int Response_Code = job1.optInt("Response_Code");
                        String Response_Message=job1.optString("Response_Message");
                        if (Response_Code == 101) {
                            String responseData = job1.optString("Response_Data");
                            try {
                                JSONArray jsonArray=new JSONArray(responseData);
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject obj=jsonArray.optJSONObject(i);
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

                            } catch (Exception e) {
                                e.printStackTrace();
                            }



                            // boolean _status = job1.getBoolean("status");


                            // do anything with response
                        }else {
                            Toast.makeText(getContext(),Response_Message,Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                        llLoader.setVisibility(View.GONE);
                    }
                });
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
                            JSONObject jsonObject=new JSONObject();
                            try {
                                jsonObject.put("CompanyID",pref.getEmpClintId());
                                jsonObject.put("EmployeeID",applicantId);
                                jsonObject.put("StartDate",startDate);
                                jsonObject.put("EndDate",endDate);
                                jsonObject.put("LeaveTypeID",typeId);
                                jsonObject.put("LeaveMode",leaveModeId);
                                jsonObject.put("SecurityCode",pref.getSecurityCode());
                                validationChecking(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
                tvEndDate.setText("");
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
                    if (pref.getLanguage().equals("hi")) {
                        final Handler textViewHandler2 = new Handler();
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... params) {
                                TranslateOptions options = TranslateOptions.newBuilder()
                                        .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
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
                if (pref.getLanguage().equals("hi")) {
                    final Handler textViewHandler2 = new Handler();
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            TranslateOptions options = TranslateOptions.newBuilder()
                                    .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
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
        alert1.setCancelable(false);
        Window window = alert1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.TOP);
        alert1.show();


    }


    private void validationChecking(JSONObject jsonObject) {
        Log.e(TAG, "validationChecking: called: "+jsonObject);
        final ProgressDialog pd=new ProgressDialog(getContext());
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(Api.sLeaveStartCheckapi)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        pd.dismiss();
                        JSONObject job1 = response;
                        Log.e(TAG, "Validation_Checking" + job1);
                        String Response_Message=job1.optString("Response_Message");

                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                            tvEndDate.setText(endDate);
                            if (leaveModeId.equals("0") || leaveModeId.equals("2")) {
                                dayBreakupListDetails.clear();
                                showDailyBrkUpDialog();

                            } else if (leaveModeId.equals("1")){


                            }else {
                                showCompOffDialog();
                            }






                            // boolean _status = job1.getBoolean("status");


                            // do anything with response
                        } else {
                            endDate = "";
                            tvEndDate.setText("");
                            showErrorDialog(Response_Message);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                    }
                });
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

        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("CompanyID",pref.getEmpClintId());
            jsonObject.put("EmployeeID",applicantId);
            jsonObject.put("StartDate",startDate);
            jsonObject.put("EndDate",endDate);
            jsonObject.put("LeaveTypeID",typeId);
            jsonObject.put("SecurityCode",pref.getSecurityCode());
            getDayBreakUp(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Button btnSubmit = (Button) dialogView.findViewById(R.id.btnSubmit);
        if (pref.getLanguage().equals("hi")) {
            btnSubmit.setText("प्रस्तुत");
        } else {
            btnSubmit.setText("Submit");
        }
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (leaveModeId.equals("2")){
                    if (halfdetails.contains("0.5") && halfdetails.contains("1")){
                        alert2.dismiss();
                    }else {
                        Toast.makeText(getContext(),"You have to select one half day and one full day",Toast.LENGTH_LONG).show();
                    }
                }else {
                    if (halfdetails.contains("0.5") || halfdetails.contains("1")){
                        alert2.dismiss();
                    }else {
                        Toast.makeText(getContext(),"Please select first half or second half",Toast.LENGTH_LONG).show();
                    }
                }
               /* if (category.equals("1") || category.equals("3")) {
                    showCompOffDialog();
                } else {

                }*/
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
        //getCompOffDetails();
        JSONObject object = new JSONObject();
        try {
            object.put("CompanyID", pref.getEmpClintId());
            object.put("EmployeeID", pref.getEmpId());
            object.put("StartDate", startDate);
            object.put("EndDate", endDate);
            object.put("LeaveTypeID", typeId);
            object.put("Iscompoff", category);
            object.put("SecurityCode", pref.getSecurityCode());
            getCompOffDetails2(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

        ImageView imgGallery = dialogView.findViewById(R.id.imgGallery);
        imgGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galleryImageSelection();
            }
        });

        alert4 = dialogBuilder.create();
        alert4.setCancelable(true);
        Window window = alert4.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alert4.show();
    }

    private void galleryImageSelection() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        //intent.setType("*/*");  // or set specific MIME type if you need specific document types
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(Intent.createChooser(intent, "Select Media"), GALLERY_IMAGE_SELECTION);
    }

    private void showPDFChooser() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PDF_REQUEST);
    }



    private void getDayBreakUp(JSONObject jsonObject) {
        Log.e(TAG, "getDayBreakUp: called");
        final ProgressDialog pd=new ProgressDialog(getContext());
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(Api.sLeaveDayDetailsapi)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pd.dismiss();
                        JSONObject job1 = response;
                        Log.e(TAG, "getDayBreakUp: " + job1);
                        String Response_Message=job1.optString("Response_Message");
                        try {
                            int Response_Code = job1.optInt("Response_Code");
                            if (Response_Code == 101) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                String responseData = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(responseData);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
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

                                dayAdapter = new DayBreakUpAdapter(dayBreakupList, ApplicationFragment.this, getContext(),leaveModeId);
                                rvBrkupItem.setAdapter(dayAdapter);

                            } else {

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                    }
                });
    }


    public void updateStatus(int position, boolean status) {
        dayBreakupList.get(position).setSelected(status);
        if (dayBreakupList.get(position).isSelected() == true) {
            halfdetails.add(dayBreakupList.get(position).getBalance());
            dayBreakupListDetails.add(dayBreakupList.get(position).getBrkupDate() + "_" + dayBreakupList.get(position).getDayModeValue() + "_" + dayBreakupList.get(position).getBalance());


        } else {
            dayBreakupListDetails.remove(position);
            halfdetails.remove(position);
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




    private void preView(JSONObject object) {
        Log.e(TAG, "preView2: "+object);
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading...");
        pd.setCancelable(true);
        pd.show();
        AndroidNetworking.post(Api.sCheckLeaveViewSummary)
                .addJSONObjectBody(object)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "PRE_VIEW: "+response.toString());
                        pd.dismiss();
                        JSONObject job1 = response;
                        int Response_Code = job1.optInt("Response_Code");
                        String Response_Message = job1.optString("Response_Message");
                        if (Response_Code == 101) {
                            if (leaveModeId.equals("1")) {
                                dayBreakUpDetails = job1.optString("Response_Data");
                            } else {
                                //dayBreakUpDetails = null;
                            }
                            showPreviewDialog();
                        } else {
                            showErrorDialog(Response_Message);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "PRE_VIEW_onError: "+anError);
                        if (anError.getErrorCode()==401) {
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            startActivity(intent);
                        }

                        pd.dismiss();
                    }
                });

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
                            Log.e(TAG, "File Size: "+FileUtils.checkFileSize(file.getAbsolutePath()));
                            file = new ImageZipper(getContext())
                                    .setQuality(60)
                                    .setMaxWidth(640)
                                    .setMaxHeight(480)
                                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                                    .compressToFile(file);
                            Log.e(TAG, "File Size: "+FileUtils.checkFileSize(file.getAbsolutePath()));
                            // Log.d("imageSixw", String.valueOf(getReadableFileSize(compressedImageFile.length())));
                            BitmapFactory.Options o = new BitmapFactory.Options();
                            o.inSampleSize = 6;
                            //Bitmap bm = cropToSquare(BitmapFactory.decodeFile(imageurl, o));
                            Bitmap bm = new ImageZipper(getContext()).compressToBitmap(file);
                            //int memorySize = bm.getByteCount();
                            //Log.e(TAG, "memorySize: "+memorySize);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                            byte[] b = baos.toByteArray();
                            encodedImage = encodeFileToBase64Binary(file);
                            Log.d("encoded", encodedImage);
                            //imgPic.setImageBitmap(bm);
                            attachmentFlag = 1;
                            multipleImageSelected = 0;
                            alert4.dismiss();
                            String contentType = "image/jpg";
                            String[] brkDown = imageurl.split("/");
                            String name = brkDown[brkDown.length-1];
                            //stringFile = name + "_" + encodedImage + "_" + contentType;
                            String completeString = name + "_" + encodedImage + "_" + contentType;
                            Log.d("stringFile", completeString);
                            //rvMulImages.setVisibility(View.GONE);
                            MultipleDocModel multipleDocModel = new MultipleDocModel(name,contentType,imageUri,completeString);
                            if (pref.getSecurityCode().equals(SecurityCode.Arun_Nursery) || pref.getSecurityCode().equals(SecurityCode.Future_Foundation)){
                                multipleImageUriList.add(multipleDocModel);
                            } else {
                                multipleImageUriList.clear();
                                multipleImageUriList.add(multipleDocModel);
                            }

                            if (multipleImageAdapter == null){
                                multipleImageAdapter = new MultipleImageAdapter(getContext(),multipleImageUriList);
                                rvMulImages.setAdapter(multipleImageAdapter);
                            }  else {
                                multipleImageAdapter.notifyDataSetChanged();
                            }

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
                    String realPath = getRealPath(getContext(),selectedFileURI);
                    pdffile = new File(realPath);
                    alert4.dismiss();
                    //encodedImage = encodeFileToBase64Binary(pdffile);

                    //String filePath = getRealPDFPathFromURI(selectedFileURI);
                    String[] brkDown = realPath.split("/");
                    String name = brkDown[brkDown.length-1];
                    Log.e(TAG, "onActivityResult: "+name);
                    try {
                        encodedImage = FileToBase64Converter.convertToBase64(FileToBase64Converter.convertInputStreamToFile(getContext(),selectedFileURI,name)).replaceAll("\n","");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    String contentType = "application/pdf";
                    //stringFile = name + "_" + encodedImage + "_" + contentType;

                    String completeString = name + "_" + encodedImage + "_" + contentType;
                    Log.e(TAG, "onActivityResult: "+completeString);
                    //imgPic.setImageResource(R.drawable.pdficon);
                    attachmentFlag = 1;
                    multipleImageSelected = 0;
                    //rvMulImages.setVisibility(View.GONE);
                    MultipleDocModel multipleDocModel = new MultipleDocModel(name,contentType,imageUri,completeString);
                    if (pref.getSecurityCode().equals(SecurityCode.Arun_Nursery) || pref.getSecurityCode().equals(SecurityCode.Future_Foundation)){
                        multipleImageUriList.add(multipleDocModel);
                    } else {
                        multipleImageUriList.clear();
                        multipleImageUriList.add(multipleDocModel);
                    }
                    if (multipleImageAdapter == null){
                        multipleImageAdapter = new MultipleImageAdapter(getContext(),multipleImageUriList);
                        rvMulImages.setAdapter(multipleImageAdapter);
                    }  else {
                        multipleImageAdapter.notifyDataSetChanged();
                    }
                }
                break;
            case GALLERY_IMAGE_SELECTION:
                if (requestCode == GALLERY_IMAGE_SELECTION && resultCode == RESULT_OK) {
                    if (data.getData() != null) {
                        Uri selectedFileURI = data.getData();
                        String realPath = getRealPath(getContext(),selectedFileURI);
                        Log.e(TAG, "onActivityResult: "+realPath);
                        file = new File(realPath);
                        try {
                            file = new ImageZipper(getContext())
                                    .setQuality(60)
                                    .setMaxWidth(640)
                                    .setMaxHeight(480)
                                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                                    .compressToFile(file);
                            Log.e(TAG, "File Size: "+FileUtils.checkFileSize(file.getAbsolutePath()));
                            // Log.d("imageSixw", String.valueOf(getReadableFileSize(compressedImageFile.length())));
                            BitmapFactory.Options o = new BitmapFactory.Options();
                            o.inSampleSize = 6;
                            //Bitmap bm = cropToSquare(BitmapFactory.decodeFile(imageurl, o));
                            Bitmap bm = new ImageZipper(getContext()).compressToBitmap(file);
                            //int memorySize = bm.getByteCount();
                            //Log.e(TAG, "memorySize: "+memorySize);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                            byte[] b = baos.toByteArray();
                            encodedImage = encodeFileToBase64Binary(file);
                            Log.d("encoded", encodedImage);
                            //imgPic.setImageBitmap(bm);
                            attachmentFlag = 1;
                            multipleImageSelected = 0;
                            alert4.dismiss();
                            String contentType = "image/jpg";
                            String[] brkDown = realPath.split("/");
                            String name = brkDown[brkDown.length-1];
                            //stringFile = name + "_" + encodedImage + "_" + contentType;
                            String completeString = name + "_" + encodedImage + "_" + contentType;
                            Log.d("stringFile", completeString);
                            //rvMulImages.setVisibility(View.GONE);
                            MultipleDocModel multipleDocModel = new MultipleDocModel(name,contentType,selectedFileURI,completeString);
                            if (pref.getSecurityCode().equals(SecurityCode.Arun_Nursery) || pref.getSecurityCode().equals(SecurityCode.Future_Foundation)){
                                multipleImageUriList.add(multipleDocModel);
                            } else {
                                multipleImageUriList.clear();
                                multipleImageUriList.add(multipleDocModel);
                            }
                            if (multipleImageAdapter == null){
                                multipleImageAdapter = new MultipleImageAdapter(getContext(),multipleImageUriList);
                                rvMulImages.setAdapter(multipleImageAdapter);
                            }  else {
                                multipleImageAdapter.notifyDataSetChanged();
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
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
        //getPreviewItem();
        JSONObject object = new JSONObject();
        try {
            object.put("CompanyID", pref.getEmpClintId());
            object.put("EmployeeID", applicantId);
            object.put("StartDate", startDate);
            object.put("EndDate", endDate);
            object.put("StrDayBreakUp", dayBreakUpDetails);
            object.put("LeaveType", leaveType);
            object.put("Reason", etReason.getText().toString().replaceAll("\\s+", "%20"));
            object.put("IsAttachment", attachmentFlag);
            object.put("SecurityCode", pref.getSecurityCode());
            getPreviewItem2(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TextView tvReason = (TextView) dialogView.findViewById(R.id.tvReason);
        TextView tvValue = (TextView) dialogView.findViewById(R.id.tvValue);
        TextView tvEndDate = (TextView) dialogView.findViewById(R.id.tvEndDate);
        TextView tvStrtDate = (TextView) dialogView.findViewById(R.id.tvStrtDate);
        TextView tvType = (TextView) dialogView.findViewById(R.id.tvType);

        Button btnSubmit = (Button) dialogView.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (multipleImageSelected == 1){

                } else {
                    final ProgressDialog progressDialog=new ProgressDialog(getContext());
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                    try {
                        object.put("CompanyID", pref.getEmpClintId());
                        object.put("EmployeeId", applicantId);
                        object.put("StartDate", startDate);
                        object.put("EndDate", endDate);
                        object.put("LeaveTypeID", typeId);
                        object.put("LeaveMode", leaveModeId);
                        object.put("AppliedLeave", LeaveValue);
                        object.put("Reasons", etReason.getText().toString());
                        object.put("LeaveCategory", category);
                        object.put("StrDayBreakUp", (dayBreakUpDetails.isEmpty())?JSONObject.NULL:dayBreakUpDetails);
                        object.put("StrCompOff", compOffDetails);
                        object.put("StrFile", stringFile);
                        object.put("createdby", pref.getEmpId());
                        object.put("SecurityCode", pref.getSecurityCode());
                        object.put("Operation","0");
                        LeaveSave(object,progressDialog);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }*/
                LeaveSubmitOperation();
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

    private void LeaveSubmitOperation() {
        final ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        JSONObject object = new JSONObject();
        new Thread(new Runnable() {
            @Override
            public void run() {
               if(multipleImageUriList.size() > 0){
                   for (MultipleDocModel model:multipleImageUriList){
                       if (stringFile.isEmpty()){
                           stringFile = model.getBase64String();
                       } else {
                           stringFile += ","+model.getBase64String();
                       }
                   }
               }

                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            object.put("CompanyID", pref.getEmpClintId());
                            object.put("EmployeeId", applicantId);
                            object.put("StartDate", startDate);
                            object.put("EndDate", endDate);
                            object.put("LeaveTypeID", typeId);
                            object.put("LeaveMode", leaveModeId);
                            object.put("AppliedLeave", LeaveValue);
                            object.put("Reasons", etReason.getText().toString());
                            object.put("LeaveCategory", category);
                            object.put("StrDayBreakUp", (dayBreakUpDetails.isEmpty())?JSONObject.NULL:dayBreakUpDetails);
                            object.put("StrCompOff", compOffDetails);
                            object.put("StrFile", stringFile);
                            object.put("createdby", pref.getEmpId());
                            object.put("SecurityCode", pref.getSecurityCode());
                            object.put("Operation","0");
                            LeaveSave(object,progressDialog);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();
    }


    private void getPreviewItem2(JSONObject object) {
        Log.e(TAG, "BIND_VIEW_SUMMERY_input: Called "+object);
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading...");
        pd.setCancelable(true);
        pd.show();

        //CompanyID=" + pref.getEmpClintId() + "&EmployeeID=" + applicantId + "&StartDate=" + startDate + "&EndDate=" + endDate + "&StrDayBreakUp=" + dayBreakUpDetails + "&LeaveType=" + leaveType + "&Reason=" + etReason.getText().toString().replaceAll("\\s+", "%20") + "&IsAttachment=" + attachmentFlag + "&SecurityCode=" + pref.getSecurityCode();

        AndroidNetworking.post(Api.sBindViewSummary)
                .addJSONObjectBody(object)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "BIND_VIEW_SUMMERY: "+response.toString());
                        pd.dismiss();
                        try {
                            previewItem.clear();
                            JSONObject job1 = response;
                            int Response_Code = job1.optInt("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code == 101) {
                                String responseData = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(responseData);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);

                                    String LeaveType = obj.optString("LeaveType");
                                    String StartDate = obj.optString("StartDate");
                                    String EndDate = obj.optString("EndDate");
                                    LeaveValue = obj.optString("LeaveValue");
                                    String Reason = obj.optString("Reason");

                                    Log.e(TAG, "LeaveValue: "+LeaveValue);

                                    PrevieModel spModel = new PrevieModel(LeaveType, StartDate, EndDate, LeaveValue, Reason);
                                    previewItem.add(spModel);
                                }

                                PreviewAdapter preAdapter = new PreviewAdapter(previewItem, getContext());
                                rvPreviewItem.setAdapter(preAdapter);
                            } else {

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        pd.dismiss();
                        Toast.makeText(getContext(), anError.toString(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, "BIND_VIEW_SUMMERY_error"+anError);
                    }
                });
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


    private void LeaveSave(JSONObject object, ProgressDialog progressDialog)  {
        Log.e(TAG, "LeaveSave: object: "+object);
        AndroidNetworking.post(Api.sLeaveAdd)
                .addJSONObjectBody(object)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.e(TAG, "LEAVE_SAVE: "+response.toString());
                                progressDialog.dismiss();
                                JSONObject job1 = response;
                                int Response_Code = job1.optInt("Response_Code");
                                String Response_Message = job1.optString("Response_Message");
                                if (Response_Code == 101) {
                                    successAlert();
                                } else {
                                    Toast.makeText(getContext(),Response_Message,Toast.LENGTH_LONG).show();
                                    alert3.dismiss();
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                progressDialog.dismiss();
                                Log.e(TAG, "LEAVE_SAVE_onError: "+anError);
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

            tvInvalidDate.setText("Leave has been successfully applied");
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




    private void getCompOffDetails2(JSONObject object) {
        Log.e(TAG, "getCompOffDetails2: called");
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading...");
        pd.setCancelable(true);
        pd.show();
        /*String surl = pref.getIpAddress()+"ghrmsapi/api/Leave/CompBreakUp?CompanyID=" + pref.getEmpClintId() + "&EmployeeID=" + pref.getEmpId() + "&StartDate=" + startDate + "&EndDate=" + endDate + "&LeaveTypeID=" + typeId + "&Iscompoff=" + category + "&SecurityCode=" + pref.getSecurityCode();
        Log.d("printcompff", surl);*/

        AndroidNetworking.post(Api.sGetCompOffBreakUp)
                .addJSONObjectBody(object)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject job1 = response;
                        try {
                            int Response_Code = job1.optInt("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code == 101) {
                                String responseData = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(responseData);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
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
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    public static String getRealPath(Context context, Uri fileUri) {
        String realPath;
        Log.e("SDK_INT", "= "+ SDK_INT);
        // SDK < API11
        if (SDK_INT < 11) {
            realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(context, fileUri);
        }
        // SDK >= 11 && SDK < 19
        else if (SDK_INT < 19) {
            realPath = RealPathUtil.getRealPathFromURI_API11to18(context, fileUri);
        }
        // SDK > 19 (Android 4.4) and up
        else {
            realPath = RealPathUtil.getRealPathFromURI_API19(context, fileUri);
        }
        return realPath;
    }
    //String imageToString(Uri uri) throws IOException {
    String imageToString() throws IOException {
        Log.e(TAG, "imageToString: called");
        String images ="";
        for (int i = 0; i < imageURI.size(); i++){
            Uri uri = imageURI.get(i);
            Log.e(TAG, "imageToString: "+uri);
            String imageurl =  getRealPath(getContext(),uri);
            file = new File(imageurl);
            Log.e(TAG, "File Size: "+FileUtils.checkFileSize(file.getAbsolutePath()));
            file = new ImageZipper(getContext())
                    .setQuality(60)
                    .setMaxWidth(640)
                    .setMaxHeight(480)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .compressToFile(file);
            //Log.e(TAG, "imageToString: "+file.getPath());
            encodedImage = encodeFileToBase64Binary(file);
            //Log.d("encoded", encodedImage);
            String contentType = "image/jpg";
            String[] brkDown = imageurl.split("/");
            String name = brkDown[5];
            if(images.isEmpty()){
                images = name + "_" + encodedImage + "_" + contentType;
            } else {
                images += ","+name + "_" + encodedImage + "_" + contentType;
            }
            Log.e(TAG, "stringFile: "+images);
        }
        return images;
    }


}
