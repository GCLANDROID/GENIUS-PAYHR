package com.genius.hrms.activity.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genius.hrms.R;
import com.genius.hrms.activity.adapter.HolidayAdapter;
import com.genius.hrms.activity.adapter.LeaveBalanceAdapter;
import com.genius.hrms.activity.adapter.SalaryAdapter;
import com.genius.hrms.activity.model.HoliDayModel;
import com.genius.hrms.activity.model.LeaveBalanceModel;
import com.genius.hrms.activity.model.SalaryModule;
import com.genius.hrms.activity.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class VoiceAssistantActivity extends AppCompatActivity {
    ImageView imgMic;
    EditText etText;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;
    LinearLayout llVoice,llMain,llNodata;
    RecyclerView rvSalary;
    ArrayList<SalaryModule> salaryList = new ArrayList<>();
    ImageView imgMicSearch;
    TextToSpeech t1;
    String mId,yID;
    Pref pref;
    ArrayList<LeaveBalanceModel> itemList = new ArrayList<>();
    TextView tvSearch;
    ArrayList<HoliDayModel> holidayList=new ArrayList<>();
    String cuyear;
    int y;
    String year;
    ImageView imgBack,imgHome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_assistant);
        initView();
        onCLick();
    }

    private void initView(){
        pref=new Pref(VoiceAssistantActivity.this);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        tvSearch=(TextView)findViewById(R.id.tvSearch);
        llMain=(LinearLayout)findViewById(R.id.llMain);
        llVoice=(LinearLayout)findViewById(R.id.llVoice);
        llNodata=(LinearLayout)findViewById(R.id.llNodata);
        rvSalary = (RecyclerView) findViewById(R.id.rvSalary);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(VoiceAssistantActivity.this, LinearLayoutManager.VERTICAL, false);
        rvSalary.setLayoutManager(layoutManager);
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });

        imgMic=(ImageView)findViewById(R.id.imgMic);
        imgMicSearch=(ImageView)findViewById(R.id.imgMicSearch);
        etText=(EditText)findViewById(R.id.etText);
        y= Calendar.getInstance().get(Calendar.YEAR);
        cuyear=String.valueOf(y);

        imgMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent
                        = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                        Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

                try {
                    startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
                }
                catch (Exception e) {
                    Toast
                            .makeText(VoiceAssistantActivity.this, " " + e.getMessage(),
                                    Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
                etText.setText(
                        Objects.requireNonNull(result).get(0));
                String text=etText.getText().toString().toLowerCase();

                if (text.contains("salary") ||text.contains("payslip") ||text.contains("pay slip")){
                    tvSearch.setText("Your Salary List is Here:");

                    if (text.contains("january")){
                        mId="1";

                    }else if (text.contains("february")){

                        mId="2";
                    }else if (text.contains("march")){

                        mId="3";
                    }else if (text.contains("april")){

                        mId="4";
                    }else if (text.contains("may")){

                        mId="5";
                    }else if (text.contains("june")){

                        mId="6";
                    }else if (text.contains("july")){

                        mId="7";
                    }else if (text.contains("august")){

                        mId="8";
                    }else if (text.contains("september")){

                        mId="9";
                    }else if (text.contains("october")){

                        mId="10";
                    }else if (text.contains("november")){

                        mId="11";
                    }else if (text.contains("december")){

                        mId="12";
                    }else {

                        mId="0";
                    }

                    if (text.contains("january") && text.contains("2022")){
                        yID="17";
                    }else if (text.contains("february") && text.contains("2022")){
                        yID="17";
                    }else if (text.contains("march") && text.contains("2022")){
                        yID="17";
                    }else if (text.contains("2022")){
                        yID="18";
                    }else if (text.contains("january") && text.contains("2021")){
                        yID="16";
                    }else if (text.contains("february") && text.contains("2021")){
                        yID="16";
                    }else if (text.contains("march") && text.contains("2021")){
                        yID="16";
                    }else if (text.contains("2021")){
                        yID="17";
                    }else if (text.contains("january") && text.contains("2023")){
                        yID="18";
                    }else if (text.contains("february") && text.contains("2023")){
                        yID="18";
                    }else if (text.contains("march") && text.contains("2023")){
                        yID="18";
                    }else if ( text.contains("2023")){
                        yID="19";
                    }else if (text.contains("january") && text.contains("2024")){
                        yID="19";
                    }else if (text.contains("february") && text.contains("2024")){
                        yID="19";
                    }else if (text.contains("march") && text.contains("2024")){
                        yID="19";
                    }else if ( text.contains("2024")){
                        yID="20";
                    }else if (text.contains("january") && text.contains("2025")){
                        yID="20";
                    }else if (text.contains("february") && text.contains("2025")){
                        yID="20";
                    }else if (text.contains("march") && text.contains("2025")){
                        yID="20";
                    }else if ( text.contains("2025")){
                        yID="21";
                    }else {
                        yID="17";
                    }
                    getSalaryList(mId,yID);
                }else if (text.contains("name") || text.contains("hi")||text.contains("hello")){

                    t1.speak("Hi "+pref.getempname()+"I am Your Genius Voice Assistant.You may ask me about your salary,CTC,Leave Balance and Yearly Holiday also", TextToSpeech.QUEUE_FLUSH, null);
                }else if (text.contains("leave balance") || text.contains("leavebalance")|| text.contains("balance")){
                    tvSearch.setText("Your Leave Balance is Here:");
                    getLeaveAllDetails();
                }else if (text.contains("ctc")){
                    t1.speak("Here is your current CTC", TextToSpeech.QUEUE_FLUSH, null);
                    openBrowser();
                }else if (text.contains("holiday")){
                    tvSearch.setText("Holiday list is Here:");
                    if (text.contains("2021")){
                        year="2021";
                    }else if (text.contains("2022")){
                        year="2022";
                    }else if (text.contains("2023")){
                        year="2023";
                    }else if (text.contains("2024")){
                        year="2024";
                    }else if (text.contains("2025")){
                        year="2025";
                    }else if (text.contains("2026")){
                        year="2026";
                    }else if (text.contains("2027")){
                        year="2027";
                    }else {
                        year=cuyear;
                    }

                    t1.speak("Here is your Holidaylist", TextToSpeech.QUEUE_FLUSH, null);
                    getHolidayList(year);

                }
                else {
                    t1.speak("Sorry! I don't have any training regarding this", TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        }
    }
    private void getSalaryList(String monthid,String yearid) {
        llVoice.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);
        String surl = "https://cloud.geniusconsultant.com/GHRMSApi/api/Salary_New?SlNo=1&AEMClientID="+pref.getEmpClintId()+"&AEMEmployeeID="+pref.getEmpId()+"&FinancialYear="+yearid+"&Month="+monthid+"&SecurityCode="+pref.getSecurityCode();
        Log.d("salaryinput",surl);
        final ProgressDialog progressDialog=new ProgressDialog(VoiceAssistantActivity.this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        progressDialog.dismiss();
                        salaryList.clear();
                        itemList.clear();
                        holidayList.clear();



                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                t1.speak("Here is your salary or payslip", TextToSpeech.QUEUE_FLUSH, null);
                                //      Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String SalMonth = obj.optString("SalMonth");
                                    String SalYear = obj.optString("SalYear");
                                    String MonthlyNet = obj.optString("MonthlyNet");
                                    String url = obj.optString("PayslipPage");
                                    SalaryModule salaryModule = new SalaryModule(SalYear, SalMonth, MonthlyNet, url);
                                    salaryList.add(salaryModule);

                                }

                                SalaryAdapter salaryAdapter = new SalaryAdapter(salaryList,getApplicationContext());
                                rvSalary.setAdapter(salaryAdapter);

                                llVoice.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNodata.setVisibility(View.GONE);


                            } else {
                                llVoice.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNodata.setVisibility(View.VISIBLE);
                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();

                            //Toast.makeText(SalaryActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llVoice.setVisibility(View.GONE);
                llMain.setVisibility(View.GONE);
                llNodata.setVisibility(View.VISIBLE);
                // Toast.makeText(SalaryActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(VoiceAssistantActivity.this);
        requestQueue.add(stringRequest);
    }

    private void onCLick(){
        imgMicSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llVoice.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);
                llNodata.setVisibility(View.GONE);
                etText.setText("");
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(VoiceAssistantActivity.this,UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void getLeaveAllDetails() {
        llVoice.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);
        //names.clear();
       final ProgressDialog progressDialog=new ProgressDialog(VoiceAssistantActivity.this);
       progressDialog.setMessage("Loading.");
       progressDialog.setCancelable(false);
       progressDialog.show();

        String surl = pref.getIpAddress() + "ghrmsapi/api/Leave/LeaveApplicationDetails?CompanyID=" + pref.getEmpClintId() + "&EmployeeID=" + pref.getEmpId() + "&ApproverID=" + pref.getEmpId() + "&SecurityCode=" + pref.getSecurityCode();
        Log.d("printurlrequest", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        progressDialog.dismiss();
                        itemList.clear();
                        salaryList.clear();
                        holidayList.clear();
                        t1.speak("Here is your leave balance", TextToSpeech.QUEUE_FLUSH, null);

                        // attendabceInfiList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");

                                JSONArray leaveBalanceArray = responseData.optJSONArray(1);
                                for (int i = 0; i < leaveBalanceArray.length(); i++) {
                                    JSONObject balanceObject = leaveBalanceArray.optJSONObject(i);
                                    final String Code = balanceObject.optString("LeaveTypeName");
                                    final String Opening = balanceObject.optString("Opening");
                                    final String LeaveAvailed = balanceObject.optString("LeaveAvailed");
                                    final String Avaliable = balanceObject.optString("Avaliable");
                                    String LeaveTypeID = balanceObject.optString("LeaveTypeID");
                                    // typeAvaild.add(LeaveTypeID + "_" + Avaliable);

                                    LeaveBalanceModel model = new LeaveBalanceModel(Code, Opening, LeaveAvailed,Avaliable);
                                    itemList.add(model);
                                }
                                llVoice.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNodata.setVisibility(View.GONE);

                                LeaveBalanceAdapter lAdaapter = new LeaveBalanceAdapter(itemList, getApplicationContext());
                                rvSalary.setAdapter(lAdaapter);






                            } else {
                                llVoice.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNodata.setVisibility(View.VISIBLE);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              progressDialog.dismiss();
                llVoice.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);
                llNodata.setVisibility(View.GONE);


                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }

    private void openBrowser(){
        if (!pref.getCTCURL().equals("")) {
            Uri uri = Uri.parse(pref.getCTCURL()); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    private void getHolidayList(String year){

        llVoice.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);

       final ProgressDialog progressDialog=new ProgressDialog(VoiceAssistantActivity.this);
       progressDialog.setMessage("Loading..");
       progressDialog.show();
       progressDialog.setCancelable(false);
        String surl =pref.getIpAddress()+"GHRMSApi/api/GCLHolidayList_New?AEMEmployeeID="+pref.getEmpId()+"&Year="+year+"&SecurityCode="+pref.getSecurityCode();
        Log.d("inputholiday",surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);

                        // attendabceInfiList.clear();
                        progressDialog.dismiss();
                        salaryList.clear();
                        itemList.clear();
                        holidayList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText=job1.optString("responseText");

                            boolean responseStatus=job1.optBoolean("responseStatus");
                            if (responseStatus){
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData=job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++){
                                    JSONObject obj=responseData.getJSONObject(i);
                                    String HolidayName=obj.optString("HolidayName");
                                    String HolidayDate=obj.optString("HolidayDate");
                                    String HDay=obj.optString("HDay");

                                    HoliDayModel obj2 = new HoliDayModel(HolidayName,HolidayDate,HDay);
                                    holidayList.add(obj2);


                                }
                                llVoice.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNodata.setVisibility(View.GONE);
                                HolidayAdapter hAdapter =new HolidayAdapter(holidayList,VoiceAssistantActivity.this);
                                rvSalary.setAdapter(hAdapter);

                            }

                            else {
                                llVoice.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNodata.setVisibility(View.VISIBLE);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              progressDialog.dismiss();
                llVoice.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);
                llNodata.setVisibility(View.GONE);
                Toast.makeText(VoiceAssistantActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert",error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(VoiceAssistantActivity.this);
        requestQueue.add(stringRequest);
    }
}