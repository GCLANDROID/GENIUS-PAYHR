package com.genius.hrms.activity.payroll;

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
import com.genius.hrms.R;
import com.genius.hrms.activity.activity.EmployeeDashBoardActivity;
import com.genius.hrms.activity.activity.UserDashBoardActivity;
import com.genius.hrms.activity.activity.WebViewActivity;
import com.genius.hrms.activity.adapter.SalaryAdapter;
import com.genius.hrms.activity.model.SalaryModule;
import com.genius.hrms.activity.model.SpinnerModel;
import com.genius.hrms.activity.utility.NetworkConnectionCheck;
import com.genius.hrms.activity.utility.Pref;
import com.genius.hrms.activity.utility.RecyclerItemClickListener;


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

    private void getSalaryList() {
        surl = pref.getIpAddress()+"GHRMSApi/api/Salary_New?SlNo=1&AEMClientID="+pref.getEmpClintId()+"&AEMEmployeeID="+pref.getEmpId()+"&FinancialYear="+yearid+"&Month=0&SecurityCode="+pref.getSecurityCode();
        Log.d("salaryinput",surl);
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);
        llAgain.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        salaryList.clear();


                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
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
                            } else {
                                //Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNodata.setVisibility(View.VISIBLE);
                                llAgain.setVisibility(View.GONE);

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
                llLoader.setVisibility(View.GONE);
                llMain.setVisibility(View.GONE);
                llNodata.setVisibility(View.GONE);
                llAgain.setVisibility(View.VISIBLE);

               // Toast.makeText(SalaryActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(SalaryActivity.this);
        requestQueue.add(stringRequest);
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

    private void setYearItem() {
        String surl = pref.getIpAddress()+"GHRMSApi/api/Commonddl?ddltype=1&id1=0&id2=0&id3=0&SecurityCode="+pref.getSecurityCode();
        Log.d("compurl", surl);
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);

                        progressBar.dismiss();
                        yearList.clear();
                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                //Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = responseData.length() - 1; i >= 0; i--) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String YearName = obj.optString("value");
                                    String YearID = obj.optString("id");
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

                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SalaryActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(SalaryActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



    }


    private void showSearchDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SalaryActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_search, null);
        dialogBuilder.setView(dialogView);
        spYear = (Spinner) dialogView.findViewById(R.id.spYear);
        setYearItem();
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


                    getSalaryList();
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
