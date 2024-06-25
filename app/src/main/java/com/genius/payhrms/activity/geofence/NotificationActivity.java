package com.genius.payhrms.activity.geofence;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;


import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.genius.payhrms.R;
import com.genius.payhrms.activity.activity.UserDashBoardActivity;
import com.genius.payhrms.activity.adapter.NotificationAdapter;
import com.genius.payhrms.activity.model.Notificationmodel;
import com.genius.payhrms.activity.utility.Pref;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NotificationActivity extends AppCompatActivity {
    RecyclerView rvItem;
    ArrayList<Notificationmodel> itemList = new ArrayList<>();
    LinearLayout llLoader, llMain, llNoData;
    Pref pref;
    ImageView imgBack, imgHome;
    TextView tvToolBar;
    String year, month;
    String date;
    ImageView imgSearch;
    AlertDialog alertDialog;
    ArrayList<String>yearList=new ArrayList<>();
    ArrayList<String>monthList=new ArrayList<>();
    String pastYear,futYear,selectedYear;
    TextView tvDate,tvNotMark,tvIn,tvOut,tvLocation;
    String strtDate,endDate;
    Calendar myCalendar;
    TextView tvStrtDate,tvendDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        initialize();

        onClick();
    }

    private void initialize() {
        pref = new Pref(getApplicationContext());
        rvItem = (RecyclerView) findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(NotificationActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);
        llLoader = (LinearLayout) findViewById(R.id.llLoader);
        llMain = (LinearLayout) findViewById(R.id.llMain);
        llNoData = (LinearLayout) findViewById(R.id.llNoData);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        tvToolBar = (TextView) findViewById(R.id.tvToolBar);
        if (pref.getLanguage().equals("hi")) {
            tvToolBar.setText("टीम की रिपोर्ट");
        } else {
            tvToolBar.setText("Team report");
        }

        int y = Calendar.getInstance().get(Calendar.YEAR);
        year = String.valueOf(y);
        Log.d("year", year);

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

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("MMM/dd/yyyy");//MMM/dd/yyyy
        String formattedDate = df.format(c);
        Log.d("formattedDate", formattedDate);
        String[] separated = formattedDate.split("/");
        String s1 = separated[0];
        String s2 = separated[1];
        Log.d("s1", s2);
        strtDate = m + "/" + s2 + "/" + y;
        endDate=m + "/" + s2 + "/" + y;
        imgSearch = (ImageView) findViewById(R.id.imgSearch);
        int pyear=y-1;
        pastYear= String.valueOf(pyear);

        int fYear=y+1;
        futYear= String.valueOf(fYear);

        //getItemList();
        tvDate=(TextView)findViewById(R.id.tvDate);
        tvIn=(TextView)findViewById(R.id.tvIn);
        tvOut=(TextView)findViewById(R.id.tvOut);
        tvLocation=(TextView)findViewById(R.id.tvLocation);
        tvNotMark=(TextView)findViewById(R.id.tvNotMark);

        if (pref.getLanguage().equals("hi")){
            tvDate.setText("दिनांक");
            tvIn.setText("अंदर");
            tvOut.setText("बाहर");
            tvLocation.setText("स्थान/शाखाओं");
            tvNotMark.setText("चिह्नित नहीं किया गया");


        }else {
            tvDate.setText("Date");
            tvIn.setText("In");
            tvOut.setText("Out");
            tvLocation.setText("Location/Branches");
            tvNotMark.setText("Not marked");
        }

        myCalendar = Calendar.getInstance();




    }

    private void onClick() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), UserDashBoardActivity.class);
                startActivity(i);
                finish();
            }
        });
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yearList.clear();
                monthList.clear();
                searchDialog();

            }
        });
    }



    private void setAdapter() {
        NotificationAdapter nAdapter = new NotificationAdapter(itemList,NotificationActivity.this);
        rvItem.setAdapter(nAdapter);

    }

    private void searchDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(NotificationActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.date_search_dialog, null);
        dialogBuilder.setView(dialogView);
        ImageView imgCancel=(ImageView)dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        tvStrtDate=(TextView)dialogView.findViewById(R.id.tvStrtDate);
        tvendDate=(TextView)dialogView.findViewById(R.id.tvendDate);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelEnd();
            }

        };
        LinearLayout llStrt=(LinearLayout)dialogView.findViewById(R.id.llStrt);
        llStrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(NotificationActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        LinearLayout llEnd=(LinearLayout)dialogView.findViewById(R.id.llEnd);
        llEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(NotificationActivity.this, date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        Button btnSearch=(Button)dialogView.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //getItemList();
                alertDialog.dismiss();
            }
        });

        TextView tvyear=(TextView)dialogView.findViewById(R.id.tvYear);
        TextView tvMonth=(TextView)dialogView.findViewById(R.id.tvMonth);
        if (pref.getLanguage().equals("hi")){

            btnSearch.setText("खोज");
        }else {

            btnSearch.setText("Search");
        }




        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(true);
        Window window = alertDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog.show();

    }
    private void updateLabel(){
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        tvStrtDate.setText(sdf.format(myCalendar.getTime()));
        strtDate=sdf.format(myCalendar.getTime());
    }

    private void updateLabelEnd(){
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        tvendDate.setText(sdf.format(myCalendar.getTime()));
        endDate=sdf.format(myCalendar.getTime());
    }


}
