package com.genius.payhrms.activity.payroll;

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
import com.genius.payhrms.R;
import com.genius.payhrms.activity.activity.UserDashBoardActivity;
import com.genius.payhrms.activity.adapter.SalaryAdapter;
import com.genius.payhrms.activity.model.SalaryModule;
import com.genius.payhrms.activity.model.SpinnerModel;
import com.genius.payhrms.activity.utility.NetworkConnectionCheck;
import com.genius.payhrms.activity.utility.Pref;
import com.genius.payhrms.activity.utility.RecyclerItemClickListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;


public class ReimActivity extends AppCompatActivity implements RecyclerItemClickListener.OnItemClickListener {
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
    TextView tvToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reim);
        initialize();
        showSearchDialog();
        onClick();
    }

    private void initialize() {
        pref = new Pref(getApplicationContext());
        connectionCheck = new NetworkConnectionCheck(ReimActivity.this);
        rvSalary = (RecyclerView) findViewById(R.id.rvSalary);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(ReimActivity.this, LinearLayoutManager.VERTICAL, false);
        rvSalary.setLayoutManager(layoutManager);
   //     rvSalary.addOnItemTouchListener((RecyclerView) new RecyclerItemClickListener(ReimActivity.this, ReimActivity.this));


        rvSalary.addOnItemTouchListener(new RecyclerItemClickListener(ReimActivity.this, ReimActivity.this));


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
        llNodata = (LinearLayout) findViewById(R.id.llNodata);
        llAgain = (LinearLayout) findViewById(R.id.llAgain);
        yearId = "14";
        imgAgain = (ImageView) findViewById(R.id.imgAgain);
        imgAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tvToolBar=(TextView)findViewById(R.id.tvToolBar);
        if (pref.getSecurityCode().equals("1020")){
            tvToolBar.setText("Interim Payment");
        }
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
                Intent intent = new Intent(ReimActivity.this, UserDashBoardActivity.class);
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


    private void showSearchDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ReimActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_search, null);
        dialogBuilder.setView(dialogView);
        spYear = (Spinner) dialogView.findViewById(R.id.spYear);
        //setYearItem();
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
                if (!yearid.equals("")) {

                        //getSalaryList(yearid);
                        alertDialog.dismiss();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please select month", Toast.LENGTH_LONG).show();
                    }

            }
        });



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


    @Override
    public void onItemClick(View childView, int position) {
        surl = salaryList.get(position).getSurl();
        operBrowser();

    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }

    private void operBrowser() {
        Uri uri = Uri.parse(surl); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }


}
