package com.genius.payhrms.activity.activity;

import static com.genius.payhrms.activity.utility.Util.SECRET_KEY;
import static com.genius.payhrms.activity.utility.Util.encrypt;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
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
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.genius.payhrms.R;
import com.genius.payhrms.activity.adapter.HolidayAdapter;
import com.genius.payhrms.activity.adapter.LeaveBalanceAdapter;
import com.genius.payhrms.activity.adapter.SalaryAdapter;
import com.genius.payhrms.activity.dailylog.NumberTourActivity;
import com.genius.payhrms.activity.model.HoliDayModel;
import com.genius.payhrms.activity.model.LeaveBalanceModel;
import com.genius.payhrms.activity.model.SalaryModule;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class VoiceAssistantActivity extends AppCompatActivity {
    private static final String TAG = "VoiceAssistantActivity";
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
        etText.setEnabled(false);
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
                    //getSalaryList(mId,yID);

                    JSONObject object=new JSONObject();
                    try {
                        object.put("SlNo","1");
                        object.put("AEMClientID",pref.getEmpClintId());
                        object.put("AEMEmployeeID",pref.getEmpId());
                        object.put("FinancialYear",yID);
                        object.put("Month",mId);
                        object.put("SecurityCode",pref.getSecurityCode());
                        getSalaryList(object);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if (text.contains("name") || text.contains("hi")||text.contains("hello")){
                    t1.speak("Hi "+pref.getempname()+"I am Your Genius Voice Assistant.You may ask me about your salary,CTC,Leave Balance and Yearly Holiday also", TextToSpeech.QUEUE_FLUSH, null);
                }else if (text.contains("leave balance") || text.contains("leavebalance")|| text.contains("balance")){
                    tvSearch.setText("Your Leave Balance is Here:");
                    //getLeaveAllDetails();
                    JSONObject object=new JSONObject();
                    try {
                        object.put("CompanyID",pref.getEmpClintId());
                        object.put("EmployeeID",pref.getEmpId());
                        object.put("ApproverID",pref.getEmpId());
                        object.put("SecurityCode",pref.getSecurityCode());
                        getLeaveAllDetails(object);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
                    // getHolidayList(year);

                    JSONObject object=new JSONObject();
                    try {
                        object.put("AEMEmployeeID",pref.getEmpId());
                        object.put("Year",year);
                        object.put("SecurityCode",pref.getSecurityCode());
                        getHolidayList(object);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    t1.speak("Sorry! I don't have any training regarding this", TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        }
    }


    private void getSalaryList(JSONObject object) {
        Log.e(TAG, "getSalaryList: "+object.toString());
        llVoice.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);

        final ProgressDialog progressDialog=new ProgressDialog(VoiceAssistantActivity.this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        AndroidNetworking.post(Api.sPayslipapi)
                .addJSONObjectBody(object)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("VOICE_SALARY:", response.toString());
                        progressDialog.dismiss();
                        salaryList.clear();
                        itemList.clear();
                        holidayList.clear();
                        try {
                            JSONObject job1 = response;
                            int Response_Code = job1.optInt("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code == 101) {
                                String responseData = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(responseData);
                                if (jsonArray.length() > 0) {
                                    for (int i = 0; i <jsonArray.length(); i++) {
                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        String SalMonth = obj.optString("SalMonth");
                                        String SalYear = obj.optString("SalYear");
                                        String MonthlyNet = obj.optString("MonthlyNet");
                                        String url = obj.optString("PayslipPage");
                                        SalaryModule salaryModule = new SalaryModule(SalYear, SalMonth, MonthlyNet, url);
                                        salaryList.add(salaryModule);
                                    }
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
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "onError: "+anError);

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
                            llVoice.setVisibility(View.GONE);
                            llMain.setVisibility(View.GONE);
                            llNodata.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    private void login(JSONObject jsonObject) {
        final ProgressDialog pd = new ProgressDialog((Context) VoiceAssistantActivity.this);
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

                                /*JSONObject object=new JSONObject();
                                try {
                                    object.put("SlNo","1");
                                    object.put("AEMClientID",pref.getEmpClintId());
                                    object.put("AEMEmployeeID",pref.getEmpId());
                                    object.put("FinancialYear",yID);
                                    object.put("Month",mId);
                                    object.put("SecurityCode",pref.getSecurityCode());
                                    getSalaryList(object);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }*/
                            }
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                    }
                });
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



    private void getLeaveAllDetails(JSONObject object) {
        Log.e(TAG, "getLeaveAllDetails: "+object.toString());
        llVoice.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);
        //names.clear();
        final ProgressDialog progressDialog=new ProgressDialog(VoiceAssistantActivity.this);
        progressDialog.setMessage("Loading.");
        progressDialog.setCancelable(false);
        progressDialog.show();

        AndroidNetworking.post(Api.sLeaveDetails)
                .addJSONObjectBody(object)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "VOICE_LEAVE_ALL_DETAILS: "+response.toString());
                        progressDialog.dismiss();
                        itemList.clear();
                        salaryList.clear();
                        holidayList.clear();
                        JSONObject job1 = response;
                        try {
                            int Response_Code = job1.optInt("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code == 101) {
                                String responseData = job1.optString("Response_Data");
                                JSONObject jsonArray = new JSONObject(responseData);
                                String Table1 = jsonArray.optString("Table1");
                                JSONArray leaveBalanceArray = new JSONArray(Table1);

                                for (int i = 0; i < leaveBalanceArray.length(); i++) {
                                    JSONObject balanceObject = leaveBalanceArray.optJSONObject(i);
                                    final String Code = balanceObject.optString("LeaveTypeName");
                                    final String Opening = balanceObject.optString("Opening");
                                    final String LeaveAvailed = balanceObject.optString("LeaveAvailed");
                                    final String Avaliable = balanceObject.optString("Avaliable");
                                    String LeaveTypeID = balanceObject.optString("LeaveTypeID");
                                    // typeAvaild.add(LeaveTypeID + "_" + Avaliable);

                                    LeaveBalanceModel model = new LeaveBalanceModel(Code, Opening, LeaveAvailed, Avaliable);
                                    itemList.add(model);
                                }

                                llVoice.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNodata.setVisibility(View.GONE);

                                LeaveBalanceAdapter lAdaapter = new LeaveBalanceAdapter(itemList, getApplicationContext());
                                rvSalary.setAdapter(lAdaapter);
                                //}
                            } else {
                                llVoice.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNodata.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
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
                            llVoice.setVisibility(View.VISIBLE);
                            llMain.setVisibility(View.GONE);
                            llNodata.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void openBrowser(){
        if (!pref.getCTCURL().equals("")) {
            Uri uri = Uri.parse(pref.getCTCURL()); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }




    private void getHolidayList(JSONObject object){
        llVoice.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);

        final ProgressDialog progressDialog = new ProgressDialog(VoiceAssistantActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        AndroidNetworking.post(Api.sHolidayapi)
                .addJSONObjectBody(object)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "VOICE_HOLIDAY: "+response.toString());
                        progressDialog.dismiss();
                        salaryList.clear();
                        itemList.clear();

                        holidayList.clear();
                        try {
                            JSONObject job1 = response;
                            int Response_Code = job1.optInt("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code == 101) {
                                String responseData = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(responseData);
                                if (jsonArray.length() > 0) {
                                    for (int i = 0; i <jsonArray.length(); i++) {
                                        JSONObject obj = jsonArray.getJSONObject(i);
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
                            } else {
                                llVoice.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNodata.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "VOICE_HOLIDAY_error: "+anError);
                        progressDialog.dismiss();
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
                            llVoice.setVisibility(View.VISIBLE);
                            llMain.setVisibility(View.GONE);
                            llNodata.setVisibility(View.GONE);
                        }

                    }
                });
    }
}