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
import com.genius.payhrms.activity.adapter.EmployeeMappinglAdapter;
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

public class EmpMappingActivity extends AppCompatActivity {
    ArrayList<EmployeeMapiingModule> empList = new ArrayList<>();
    RecyclerView rvItem;
    LinearLayout  llMain, llNoData;
    Pref pref;
    ImageView imgBack, imgHome;
    ArrayList<String> itemList = new ArrayList<>();
    EmployeeMappinglAdapter cAdapter;
    ArrayList<SpinnerModel> mLocationList = new ArrayList<>();
    ArrayList<String> locationList = new ArrayList<>();
    Button btnMap;
    Spinner spLocation;
    AlertDialog alerDialog1;
    String empId;
    String loactionId;
    AlertDialog alerDialog4;
    String point;
    String surl, surl2,surl1;
    EditText etSearch;
    TextView tvToolBar;
    String translated = "";
    MultiSpinnerSearch spMulLocation;
    ArrayList<String>locationIdList=new ArrayList<>();
    ArrayList<KeyPairBoolData>loactionList=new ArrayList<>();
    EditText etEmp;
    LinearLayout llEmp;
    RecyclerView rvEmp;
    TextView tvEmpName;
    ArrayList<String>empName=new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semf);
        initView();
        onClick();
    }

    private void initView() {
        pref = new Pref(getApplicationContext());

        llMain = (LinearLayout) findViewById(R.id.llMain);
        llNoData = (LinearLayout) findViewById(R.id.llNoData);
        rvItem = (RecyclerView) findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(EmpMappingActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        btnMap = (Button) findViewById(R.id.btnMap);
        point = getIntent().getStringExtra("point");
        Log.d("point",point);
        etSearch=(EditText)findViewById(R.id.etSearch);
        tvToolBar=(TextView)findViewById(R.id.tvToolBar);
        if (pref.getLanguage().equals("hi")){
            tvToolBar.setText("कर्मचारी मानचित्रण प्रबंधन");
            btnMap.setText("स्थान के साथ मानचित्रण");
        }else {
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
      //  tvEmpName=(TextView)findViewById(R.id.tvEmpName);
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
                filter(s.toString());
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
        empList.get(position).setSelected(status);
        if (empList.get(position).isSelected() == true) {
            itemList.add(empList.get(position).getEmpId());
            empName.add(empList.get(position).getEmpName());

        } else {
            itemList.clear();
        }



        Log.d("arpan", itemList.toString());
        String i = itemList.toString();
        String d = i.replace("[", "").replace("]", "");
        empId = d.replaceAll("\\s+", "");
        String emp=empName.toString();
        String replace=emp.replace("[", "").replace("]", "");
//        tvEmpName.setText(replace);


        cAdapter.notifyDataSetChanged();
    }


    private void locationAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EmpMappingActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_spinner, null);
        dialogBuilder.setView(dialogView);
        //locationGet();
        spLocation = (Spinner) dialogView.findViewById(R.id.spLocation);
        spMulLocation=(MultiSpinnerSearch)dialogView.findViewById(R.id.spMulLocation);

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
                        String locationId=items.get(i).getId();
                        locationIdList.add(locationId);
                        String loaction=locationIdList.toString();
                        loactionId=loaction.replace("[","").replace("]","").replaceAll("\\s+", "");





                    }
                }
            }


        });
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //empMapping(loactionId);
                alerDialog1.dismiss();
            }
        });
        TextView tvLocation=(TextView)dialogView.findViewById(R.id.tvLocation);
        if (pref.getLanguage().equals("hi")){
            tvLocation.setText("स्थान");
            btnOk.setText("जोड़");
        }else {
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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EmpMappingActivity.this, R.style.CustomDialogNew);
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

                Intent intent = new Intent(EmpMappingActivity.this, FencingDashBoardActivity.class);
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

    void filter(String text) {
        ArrayList<EmployeeMapiingModule> temp = new ArrayList();
        for (EmployeeMapiingModule d : empList) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.getEmpName().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        //update recyclerview
        cAdapter.updateList(temp);
    }

    public String translate(final int i){

        final ProgressDialog pd=new ProgressDialog(EmpMappingActivity.this);
        pd.setMessage("loading..");
        pd.setCancelable(false);
        final Handler textViewHandler4 = new Handler();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                TranslateOptions options = TranslateOptions.newBuilder()
                        .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
                        .build();
                final Translate translate = options.getService();
                final Translation translation =
                        translate.translate(mLocationList.get(i).getItem(),
                                Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                textViewHandler4.post(new Runnable() {
                    @Override
                    public void run() {

                        Log.d("sssh", translation.getTranslatedText());
                        translated = translation.getTranslatedText();




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
        return translated;

    }



}
