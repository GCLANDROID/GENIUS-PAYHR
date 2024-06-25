package com.genius.payhrms.activity.geofence;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
import com.androidnetworking.interfaces.UploadProgressListener;
import com.genius.payhrms.R;
import com.genius.payhrms.activity.adapter.GeoFenceApprovalAdapter;
import com.genius.payhrms.activity.model.GeoFenceApprovalModel;
import com.genius.payhrms.activity.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GeoFenceApprovalActivity extends AppCompatActivity implements View.OnClickListener {


    RecyclerView rvItem;
    ArrayList<GeoFenceApprovalModel> itemList = new ArrayList<>();

    LinearLayout llNoData, llLoader, llMain;
    Pref pref;
    GeoFenceApprovalAdapter apprvalAdapter;
    ArrayList<Integer> aIDList = new ArrayList<>();
    String aid = "";

    Button btnReject, btnApprove;

    AlertDialog alerDialog1;
    LinearLayout llARLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_fence_approval);
        initView();
    }


    private void initView() {
        pref = new Pref(GeoFenceApprovalActivity.this);

        rvItem = (RecyclerView)findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(GeoFenceApprovalActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);
        llNoData = (LinearLayout)findViewById(R.id.llNoData);
        llLoader = (LinearLayout)findViewById(R.id.llLoader);
        llMain = (LinearLayout)findViewById(R.id.llMain);

        btnReject = (Button) findViewById(R.id.btnReject);
        btnApprove = (Button) findViewById(R.id.btnApprove);
        llARLayout=(LinearLayout)findViewById(R.id.llARLayout);

        btnReject.setOnClickListener(this);
        btnApprove.setOnClickListener(this);

        //getItem();


    }




    private void setAdapter() {
        apprvalAdapter = new GeoFenceApprovalAdapter(itemList,  this);
        rvItem.setAdapter(apprvalAdapter);
    }


    public void updateAttendanceStatus(int position, boolean status) {
        itemList.get(position).setSelected(status);
        if (itemList.get(position).isSelected() == true) {
            aIDList.add(itemList.get(position).getgID() );

        } else {
            aIDList.remove(position);
        }

        aid = aIDList.toString().replace("[", "").replace("]", "");
        Log.d("aid", aid);
        if (aIDList.size() > 0) {
            llARLayout.setVisibility(View.VISIBLE);
        } else {
            llARLayout.setVisibility(View.GONE);
        }


        apprvalAdapter.notifyDataSetChanged();
    }






    private void approveAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GeoFenceApprovalActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);

        tvInvalidDate.setText("Attendance approved successfully");


        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                //getItem();
                aIDList.clear();
                aid="";

            }
        });

        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }


    private void rejectAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GeoFenceApprovalActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);

        tvInvalidDate.setText("Attendance rejected successfully");


        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                //getItem();
                aIDList.clear();
                aid="";

            }
        });

        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }


    @Override
    public void onClick(View view) {
        if (view==btnApprove){
            if (pref.getSecurityCode().equals("1157")) {
                //approveFunction("100");
            }else if (pref.getSecurityCode().equals("1163")){
                //approveFunction("100");
            }
        }else if (view==btnReject){
            if (pref.getSecurityCode().equals("1157")) {
                //rejectFunction("100");
            }else if (pref.getSecurityCode().equals("1163")){
                //rejectFunction("50");
            }
        }

    }
}