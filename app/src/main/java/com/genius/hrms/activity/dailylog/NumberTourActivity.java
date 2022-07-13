package com.genius.hrms.activity.dailylog;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;

import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genius.hrms.R;

import com.genius.hrms.activity.activity.EmployeeDashBoardActivity;
import com.genius.hrms.activity.activity.UserDashBoardActivity;
import com.genius.hrms.activity.adapter.NumberTourAdapter;
import com.genius.hrms.activity.model.NumberTourModel;
import com.genius.hrms.activity.utility.Pref;
import com.google.android.gms.maps.model.LatLng;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class NumberTourActivity extends AppCompatActivity {
    RecyclerView rvItem;
    LinearLayout llLoader,llMain,llNodata;
    ArrayList<NumberTourModel>itemList=new ArrayList<>();
    Pref pref;
    ImageView imgBack,imgHome;
    String year,pastYear,futYear;
    int y;
    String month;
    AlertDialog alertDialog;
    ArrayList<String>yearList=new ArrayList<>();
    ArrayList<String>monthList=new ArrayList<>();
    ImageView imgSearch;
    String selectedYear;
    TextView tvLogBook;
    String hPinchin;
    ProgressDialog pd;
    String surl;
    double minLat,minLong,maxlat,maxlong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numer_tour);
        initView();
        getItem();
        onClick();
    }
    private void initView(){
        pref=new Pref(getApplicationContext());
        rvItem=(RecyclerView)findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(NumberTourActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);
        llNodata=(LinearLayout)findViewById(R.id.llNodata);
        llMain=(LinearLayout)findViewById(R.id.llMain);
        llLoader=(LinearLayout)findViewById(R.id.llLoader);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        y = Calendar.getInstance().get(Calendar.YEAR);
        year = String.valueOf(y);
        Log.d("year", year);

        int pyear=y-1;
        pastYear= String.valueOf(pyear);

        int fYear=y+1;
        futYear= String.valueOf(fYear);



        int m = Calendar.getInstance().get(Calendar.MONTH) + 1;
        Log.d("month", String.valueOf(m));
        if (m == 1) {
            month = "January";
        } else if (m == 2) {
            month = "February";
        } else if (m == 3) {
            month = "March";
        } else if (m == 4) {
            month = "April";
        } else if (m == 5) {
            month = "May";
        } else if (m == 6) {
            month = "June";
        } else if (m == 7) {
            month = "July";
        } else if (m == 8) {
            month = "August";
        } else if (m == 9) {
            month = "September";
        } else if (m == 10) {
            month = "October";
        } else if (m == 11) {
            month = "November";
        } else if (m == 12) {
            month = "December";
        }
        selectedYear=year;
        imgSearch=(ImageView)findViewById(R.id.imgSearch);
        tvLogBook=(TextView)findViewById(R.id.tvLogBook);
        if (pref.getLanguage().equals("hi")){

            tvLogBook.setText("कार्यपंजी");
        }else {
            tvLogBook.setText("Logbook");
        }
        pd=new ProgressDialog(NumberTourActivity.this);
        pd.setMessage("Loading.........");
    }

   private void onClick(){
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthList.clear();
                yearList.clear();
                searchDialog();
            }
        });
   }

    private void getItem(){
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);
        if(pref.getSecurityCode().equals("123")){
            surl="http://111.93.182.174/GeniusiOSApi/api/get_OfflineDailyLogActivity?AEMEmployeeID=" + pref.getEmpId() + "&Year=0&Month=0&SecurityCode=" + pref.getSecurityCode()+"&AttendanceDate=0&Operation=2";
        }else {
            surl = pref.getIpAddress() + "GHRMSApi/api/get_OfflineDailyLogActivity?AEMEmployeeID=" + pref.getEmpId() + "&Year=" + selectedYear + "&Month=" + month + "&SecurityCode=" + pref.getSecurityCode() + "&AttendanceDate=0&Operation=2";
        }
        Log.d("inputactivity", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);

                        // attendabceInfiList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i <responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);

                                    final String PunchIn = obj.optString("PunchIn");

                                    String TotalActivity = obj.optString("TotalActivity");
                                    String Min_LatitudeIN=obj.optString("Min_LatitudeIN");
                                    String Min_LongitudeIN=obj.optString("Min_LongitudeIN");
                                    String Max_LatitudeOUT=obj.optString("Max_LatitudeOUT");
                                    String Max_LongitudeOUT=obj.optString("Max_LongitudeOUT");

                                    NumberTourModel obj2 = new NumberTourModel(PunchIn,TotalActivity,Min_LatitudeIN,Min_LongitudeIN,Max_LatitudeOUT,Max_LongitudeOUT);
                                    itemList.add(obj2);


                                }
                                setAdapter();
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNodata.setVisibility(View.GONE);

                            } else {

                                llLoader.setVisibility(View.GONE);
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
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);
                llNodata.setVisibility(View.GONE);


                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(NumberTourActivity.this);
        requestQueue.add(stringRequest);
    }
    private void setAdapter(){
        NumberTourAdapter vAdapter=new NumberTourAdapter(itemList,NumberTourActivity.this);
        rvItem.setAdapter(vAdapter);
    }

    public  double distance(int i){
        if (!itemList.get(i).getMinlat().equalsIgnoreCase("")){
            minLat=Double.parseDouble(itemList.get(i).getMinlat());
        }else {
            minLat=0.0;
        }

        if (!itemList.get(i).getMinlong().equalsIgnoreCase("")){
            minLong=Double.parseDouble(itemList.get(i).getMinlong());
        }else {
            minLong=0.0;
        }

        if (!itemList.get(i).getMaxlat().equalsIgnoreCase("")){
            maxlat=Double.parseDouble(itemList.get(i).getMaxlat());
        }else {
            maxlat=0.0;
        }

        if (!itemList.get(i).getMaxlong().equalsIgnoreCase("")){
            maxlong=Double.parseDouble(itemList.get(i).getMaxlong());
        }else {
            maxlong=0.0;
        }
        LatLng src=new LatLng(minLat,minLong);
        LatLng des=new LatLng(maxlat,maxlong);
        double distance=CalculationByDistance(src,des);


        return  distance;

    }

    public  static double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        //  Toast.makeText(getApplicationContext(),"Radious Value:  "+valueResult+"  KM: "+kmInDec+" Meter: "+meterInDec,Toast.LENGTH_LONG).show();
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);
        return Radius * c;
    }

    private void searchDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(NumberTourActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.search_dialog, null);
        dialogBuilder.setView(dialogView);
        ImageView imgCancel=(ImageView)dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        Spinner spYear=(Spinner)dialogView.findViewById(R.id.spYear);

        yearList.add(pastYear);
        yearList.add(year);
        yearList.add(futYear);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,yearList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        int pos=yearList.indexOf(year);
        spYear.setSelection(pos);
        //Setting the ArrayAdapter data on the Spinner
        spYear.setAdapter(aa);


        Spinner spMonth=(Spinner)dialogView.findViewById(R.id.spMonth);
        monthList.add("January");
        monthList.add("February");
        monthList.add("March");
        monthList.add("April");
        monthList.add("May");
        monthList.add("June");
        monthList.add("July");
        monthList.add("August");
        monthList.add("September");
        monthList.add("October");
        monthList.add("November");
        monthList.add("December");

        ArrayAdapter bb = new ArrayAdapter(this,android.R.layout.simple_spinner_item,monthList);
        bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        int pos1=monthList.indexOf(month);
        spMonth.setSelection(pos1);
        //Setting the ArrayAdapter data on the Spinner
        spMonth.setAdapter(bb);

        spYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedYear=yearList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                month=monthList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button btnSearch=(Button)dialogView.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemList.clear();
                getItem();
                alertDialog.dismiss();
            }
        });

        TextView tvyear=(TextView)dialogView.findViewById(R.id.tvYear);
        TextView tvMonth=(TextView)dialogView.findViewById(R.id.tvMonth);
        if (pref.getLanguage().equals("hi")){
            tvyear.setText("साल");
            tvMonth.setText("महीना");
            btnSearch.setText("खोज");
        }else {
            tvyear.setText("Year");
            tvMonth.setText("Month");
            btnSearch.setText("Search");
        }




        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(true);
        Window window = alertDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog.show();

    }









}
