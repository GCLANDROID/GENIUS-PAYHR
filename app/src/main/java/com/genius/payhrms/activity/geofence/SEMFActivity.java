package com.genius.payhrms.activity.geofence;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;

import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.androidbuts.multispinnerfilter.SpinnerListener;
import com.genius.payhrms.R;

import com.genius.payhrms.activity.activity.UserDashBoardActivity;
import com.genius.payhrms.activity.adapter.SEMFEmployeeMappinglAdapter;
import com.genius.payhrms.activity.model.EmployeeMapiingModule;
import com.genius.payhrms.activity.model.SpinnerModel;
import com.genius.payhrms.activity.utility.Pref;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SEMFActivity extends AppCompatActivity {
    ArrayList<EmployeeMapiingModule> empList = new ArrayList<>();
    RecyclerView rvItem;
    LinearLayout llLoader, llMain, llNoData;
    Pref pref;
    ImageView imgBack, imgHome;
    ArrayList<String> itemList = new ArrayList<>();
    SEMFEmployeeMappinglAdapter cAdapter;
    ArrayList<SpinnerModel> mLocationList = new ArrayList<>();
    ArrayList<String> locationList = new ArrayList<>();
    Button btnMap;
    Spinner spLocation;
    AlertDialog alerDialog1;
    String empId;
    String loactionId;
    AlertDialog alerDialog4;
    String point;
    String surl, surl2, surl1;
    EditText etSearch;
    TextView tvToolBar;
    String translated = "";
    MultiSpinnerSearch spMulLocation;
    ArrayList<String> locationIdList = new ArrayList<>();
    ArrayList<KeyPairBoolData> loactionList = new ArrayList<>();
    EditText etEmp;
    LinearLayout llEmp;
    ArrayList<String> slocationIdList = new ArrayList<>();
    String sLocationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semf);
        initView();

        onClick();
    }


    private void initView() {
        pref = new Pref(getApplicationContext());
        llLoader = (LinearLayout) findViewById(R.id.llLoader);
        llMain = (LinearLayout) findViewById(R.id.llMain);
        llNoData = (LinearLayout) findViewById(R.id.llNoData);
        rvItem = (RecyclerView) findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(SEMFActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        btnMap = (Button) findViewById(R.id.btnMap);
        point = getIntent().getStringExtra("point");
        etSearch = (EditText) findViewById(R.id.etSearch);
        tvToolBar = (TextView) findViewById(R.id.tvToolBar);
        if (pref.getLanguage().equals("hi")) {
            tvToolBar.setText("कर्मचारी मानचित्रण प्रबंधन");
            btnMap.setText("स्थान के साथ मानचित्रण");
        } else {
            tvToolBar.setText("Employee mapping");
            btnMap.setText("Map with location");

        }

        etEmp=(EditText)findViewById(R.id.etEmp);
        llEmp=(LinearLayout)findViewById(R.id.llEmp);
        if (point.equals("mul")){
            llEmp.setVisibility(View.GONE);
        }else {
            llEmp.setVisibility(View.VISIBLE);
        }
        if (point.equals("mul")){
            //getEmpList();
        }else {

        }
    }

    private void onClick() {
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationAlert();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });


        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //filter(s.toString());
            }
        });

        etEmp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etEmp.getText().toString().length()>1){
                    //getEmpList();
                }

            }
        });

    }



    public void updateAttendanceStatus(int position, boolean status) {
        empId= empList.get(position).getEmpId();
        Log.d("arpan", itemList.toString());

    }


    private void locationAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SEMFActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_spinner, null);
        dialogBuilder.setView(dialogView);
        //locationGet();
        spLocation = (Spinner) dialogView.findViewById(R.id.spLocation);
        spMulLocation = (MultiSpinnerSearch) dialogView.findViewById(R.id.spMulLocation);

            spMulLocation.setVisibility(View.VISIBLE);
            spLocation.setVisibility(View.GONE);


        spLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loactionId = mLocationList.get(position).getItemId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spMulLocation.setItems(loactionList, -1, new SpinnerListener() {

            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {

                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {
                        String locationId = items.get(i).getId();
                        locationIdList.add(empId+"_"+locationId);
                        String loaction = locationIdList.toString();
                        loactionId = loaction.replace("[", "").replace("]", "").replaceAll("\\s+", "");
                        slocationIdList.add(locationId);
                        String l=slocationIdList.toString();
                        sLocationId=l.replace("[", "").replace("]", "").replaceAll("\\s+", "");;

                    }
                }
            }


        });
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //empMapping();
                alerDialog1.dismiss();
            }
        });
        TextView tvLocation = (TextView) dialogView.findViewById(R.id.tvLocation);
        if (pref.getLanguage().equals("hi")) {
            tvLocation.setText("स्थान");
            btnOk.setText("जोड़");
        } else {
            tvLocation.setText("Location");
            btnOk.setText("Add");
        }


        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }





    private void successAlert(String responseText) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SEMFActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);

        tvInvalidDate.setText(responseText);

        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog4.dismiss();
                Intent intent = new Intent(SEMFActivity.this, FencingDashBoardActivity.class);
                intent.putExtra("point",pref.getPoint());
                startActivity(intent);
                finish();

            }
        });

        alerDialog4 = dialogBuilder.create();
        alerDialog4.setCancelable(true);
        Window window = alerDialog4.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog4.show();
    }

}
