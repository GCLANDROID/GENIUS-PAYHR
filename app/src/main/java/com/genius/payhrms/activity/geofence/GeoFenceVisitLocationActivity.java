package com.genius.payhrms.activity.geofence;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.genius.payhrms.R;
import com.genius.payhrms.activity.activity.UserDashBoardActivity;
import com.genius.payhrms.activity.adapter.VisitingLocationAdapter;
import com.genius.payhrms.activity.dailylog.OfflineDailyLogManageActivity;
import com.genius.payhrms.activity.dailylog.SmartJuleDailyLogActivity;
import com.genius.payhrms.activity.model.VisitingLocationModel;
import com.genius.payhrms.activity.utility.Pref;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class GeoFenceVisitLocationActivity extends AppCompatActivity {
    RecyclerView rvItem;
    ArrayList<VisitingLocationModel>itemList=new ArrayList<>();
    Button btnAdd;
    ImageView imgAdd;
    LinearLayout llLoader,llMain,llNoData;
    ProgressDialog pd,pd1;
    Pref pref;
    String formattedDate;
    TextView tvDate;
    FloatingActionButton fbAdd;
    ImageView imgBack,imgHome;
    TextView tvToolBar;
    String surl;
    String attCode;
    String frstPunch;
    double SLongitude,SLatitude,s;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_location);
        initView();
        //getItem();
        onClick();
    }

    private void initView(){
        pref=new Pref(getApplicationContext());
        rvItem=(RecyclerView)findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(GeoFenceVisitLocationActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);

        btnAdd=(Button)findViewById(R.id.btnAdd);
        llMain=(LinearLayout)findViewById(R.id.llMain);
        llLoader=(LinearLayout)findViewById(R.id.llLoader);
        llNoData=(LinearLayout)findViewById(R.id.llNoData);
        pd=new ProgressDialog(this);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
         formattedDate = df.format(c);
        Log.d("formattedDate",formattedDate);
        tvDate=(TextView)findViewById(R.id.tvDate);
        tvDate.setText(formattedDate);
        pd1=new ProgressDialog(this);
        fbAdd=(FloatingActionButton)findViewById(R.id.fbAdd);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        tvToolBar=(TextView)findViewById(R.id.tvToolBar);
        if (pref.getLanguage().equals("hi")){
            tvToolBar.setText("स्थान का दौरा किया");
            btnAdd.setText("अपनी गतिविधि शुरू करें");
        }else {
            tvToolBar.setText("Visited place");
            btnAdd.setText("Start your activity");
        }
    }


    private void setAdapter(){
        VisitingLocationAdapter vAdapter=new VisitingLocationAdapter(itemList,getApplicationContext());
        rvItem.setAdapter(vAdapter);
    }
    private void onClick(){
        fbAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (pref.getSecurityCode().equals("1153")){

                       //getAttendanceInformationForSmart();
                        Intent intent = new Intent(GeoFenceVisitLocationActivity.this, OfflineDailyLogManageActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else if (pref.getSecurityCode().equals("1157")||pref.getSecurityCode().equals("1163")){

                                //getValueForGeoFenceForIntas();





                    }
                    else {
                        pd.setMessage("Loading....");
                        pd.setCancelable(false);
                        pd.show();
                        Intent intent = new Intent(GeoFenceVisitLocationActivity.this, OfflineDailyLogManageActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                }

        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pref.getSecurityCode().equals("1153")){

                    //getAttendanceInformationForSmart();
                    Intent intent = new Intent(GeoFenceVisitLocationActivity.this, OfflineDailyLogManageActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else if (pref.getSecurityCode().equals("1157")||pref.getSecurityCode().equals("1163")){


                            //getValueForGeoFenceForIntas();



                }
                else {
                    pd.setMessage("Loading....");
                    pd.setCancelable(false);
                    pd.show();
                    Intent intent = new Intent(GeoFenceVisitLocationActivity.this, OfflineDailyLogManageActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }


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
                Intent intent=new Intent(GeoFenceVisitLocationActivity.this, UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        pd.dismiss();
        pd1.dismiss();
    }
}
