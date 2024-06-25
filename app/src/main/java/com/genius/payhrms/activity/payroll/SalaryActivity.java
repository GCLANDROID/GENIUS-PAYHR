package com.genius.payhrms.activity.payroll;

import static com.genius.payhrms.activity.utility.Util.SECRET_KEY;
import static com.genius.payhrms.activity.utility.Util.encrypt;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
import com.genius.payhrms.R;
import com.genius.payhrms.activity.activity.HolidayActivity;
import com.genius.payhrms.activity.activity.UserDashBoardActivity;
import com.genius.payhrms.activity.adapter.SalaryAdapter;
import com.genius.payhrms.activity.model.HoliDayModel;
import com.genius.payhrms.activity.model.SalaryModule;
import com.genius.payhrms.activity.model.SpinnerModel;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.NetworkConnectionCheck;
import com.genius.payhrms.activity.utility.Pref;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
public class SalaryActivity extends AppCompatActivity  {
    RecyclerView rvSalary;
    ArrayList<SalaryModule> salaryList = new ArrayList<>();
    SalaryAdapter salaryAdapter;
    Spinner spYear;
    ImageView imgBack, imgHome;
    String year;
    int y;
    AlertDialog alertDialog;
    TextView tvYear;
    LinearLayout llSearch;
    LinearLayout llMain, llLoader;
    Pref pref;
    String surl;
    NetworkConnectionCheck connectionCheck;
    LinearLayout llNodata;
    LinearLayout llAgain;
    ImageView imgAgain;
    String yearId;
    String cuurentFinancialYear;
    ArrayList<SpinnerModel> modelYearList = new ArrayList<>();
    ArrayList<String> yearList = new ArrayList<>();
    String yearid = "";
    String yearName;
    TextView tvSalary,tvNoData;
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary);
        initialize();
        showSearchDialog();

        onClick();
    }

    private void initialize() {
        pref = new Pref(getApplicationContext());
        connectionCheck=new NetworkConnectionCheck(SalaryActivity.this);
        rvSalary = (RecyclerView) findViewById(R.id.rvSalary);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(SalaryActivity.this, LinearLayoutManager.VERTICAL, false);
        rvSalary.setLayoutManager(layoutManager);

        spYear = (Spinner) findViewById(R.id.spYear);
        //SpinnerAdapter spinnerAdapter = new SpinnerAdapter(SalaryActivity.this, spYearList);
        //  spYear.setAdapter(spinnerAdapter);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        llSearch = (LinearLayout) findViewById(R.id.llSearch);
        y = Calendar.getInstance().get(Calendar.YEAR);
        year = String.valueOf(y);
        tvYear = (TextView) findViewById(R.id.tvYear);
        int futureYear = y + 1;
        cuurentFinancialYear = year + "-" + futureYear;
        tvYear.setText(cuurentFinancialYear);

        llLoader = (LinearLayout) findViewById(R.id.llLoader);
        llMain = (LinearLayout) findViewById(R.id.llMain);
        llNodata=(LinearLayout)findViewById(R.id.llNodata);
        llAgain=(LinearLayout)findViewById(R.id.llAgain);

        imgAgain=(ImageView)findViewById(R.id.imgAgain);
        tvSalary=(TextView)findViewById(R.id.tvSalary);
        tvNoData=(TextView)findViewById(R.id.tvNoData);
        if (pref.getLanguage().equals("hi")){
            tvSalary.setText("वेतन");
            tvNoData.setText("कोई डेटा नहीं मिला");
        }else {
            tvSalary.setText("Salary");
            tvNoData.setText("No data found");
        }
   }

    private void getSalaryList(JSONObject jsonObject) {
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);
        llAgain.setVisibility(View.GONE);
        AndroidNetworking.post(Api.sPayslipapi)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        salaryList.clear();
                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);


                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                            String responseData = job1.optString("Response_Data");
                            try {
                                JSONArray jsonArray=new JSONArray(responseData);
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject obj = jsonArray.optJSONObject(i);
                                    String SalMonth = obj.optString("MonthName");
                                    String SalYear = obj.optString("FinancialYear");
                                    String MonthlyNet = obj.optString("MonthlyNet");
                                    String url = obj.optString("PayslipPage");
                                    SalaryModule salaryModule = new SalaryModule(SalYear, SalMonth, MonthlyNet, url);
                                    salaryList.add(salaryModule);


                                }

                                if (salaryList.size() > 0) {
                                    llLoader.setVisibility(View.GONE);
                                    llMain.setVisibility(View.VISIBLE);
                                    llNodata.setVisibility(View.GONE);
                                    llAgain.setVisibility(View.GONE);
                                    setAdapter();
                                } else {
                                    llLoader.setVisibility(View.GONE);
                                    llMain.setVisibility(View.GONE);
                                    llNodata.setVisibility(View.GONE);
                                    llAgain.setVisibility(View.GONE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            // boolean _status = job1.getBoolean("status");
                            // do anything with response
                        }else {
                            llLoader.setVisibility(View.GONE);
                            llMain.setVisibility(View.GONE);
                            llNodata.setVisibility(View.VISIBLE);
                            llAgain.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        if (error.getErrorCode()==401){
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
                        }
                    }
                });
    }

    private void login(JSONObject jsonObject) {
        final ProgressDialog pd = new ProgressDialog(SalaryActivity.this);
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


                             /*   JSONObject object=new JSONObject();
                                try {

                                    object.put("AEMEmployeeID",pref.getEmpId());
                                    object.put("Year",year);
                                    object.put("SecurityCode",pref.getSecurityCode());
                                    holiday(object);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }*/


                                // do anything with response
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
        salaryAdapter = new SalaryAdapter(salaryList,getApplicationContext());
        rvSalary.setAdapter(salaryAdapter);
    }

    private void onClick() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SalaryActivity.this, UserDashBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
              //  finish();
            }
        });

        llSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    showSearchDialog();
            }
        });
    }

    private void operBrowser() {
        Uri uri = Uri.parse(surl); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void setYearItem(JSONObject jsonObject) {
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();
        AndroidNetworking.post(Api.sCommomddlapi)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        progressBar.dismiss();
                        yearList.clear();
                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);


                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                            String responseData = job1.optString("Response_Data");
                            try {
                                JSONArray jsonArray=new JSONArray(responseData);
                                for (int i = jsonArray.length() - 1; i >= 0; i--) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String YearName = obj.optString("Value");
                                    String YearID = obj.optString("ID");
                                    yearList.add(YearName);
                                    SpinnerModel mainDocModule = new SpinnerModel(YearName, YearID);
                                    modelYearList.add(mainDocModule);

                                }
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (SalaryActivity.this, android.R.layout.simple_spinner_item,
                                                yearList); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spYear.setAdapter(spinnerArrayAdapter);
                                int index = yearList.indexOf(yearName);
                                Log.d("indexr", String.valueOf(index));
                                spYear.setSelection(index);
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

                        if (error.getErrorCode()==401){
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
                        }


                    }
                });
    }


    private void showSearchDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SalaryActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_search, null);
        dialogBuilder.setView(dialogView);
        spYear = (Spinner) dialogView.findViewById(R.id.spYear);

        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("ddltype",1);
            jsonObject.put("id1",0);
            jsonObject.put("id2",0);
            jsonObject.put("id3",0);
            jsonObject.put("SecurityCode",pref.getSecurityCode());
            setYearItem(jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        spYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                yearid = modelYearList.get(position).getItemId();
                yearName=modelYearList.get(position).getItem();
                tvYear.setText(yearName);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        LinearLayout llShow = (LinearLayout) dialogView.findViewById(R.id.llShow);
        llShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("SlNo", 1);
                    jsonObject.put("AEMClientID", pref.getEmpClintId());
                    jsonObject.put("AEMEmployeeID", pref.getEmpId());
                    jsonObject.put("FinancialYear", yearid);
                    jsonObject.put("Month", 0);
                    jsonObject.put("SecurityCode", pref.getSecurityCode());
                    getSalaryList(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                alertDialog.dismiss();
            }
        });
        TextView tvShow=(TextView)dialogView.findViewById(R.id.tvShow);
        if (pref.getLanguage().equals("hi")){
            tvShow.setText("प्रदर्शन");
        }else {
            tvShow.setText("SHOW");
        }



        ImageView imgCancel = (ImageView) dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        Window window = alertDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog.show();
    }
}
