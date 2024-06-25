package com.genius.payhrms.activity.leaveapplication;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genius.payhrms.R;
import com.genius.payhrms.activity.adapter.SpecialHolidayApprovalReportAdapter;
import com.genius.payhrms.activity.model.SpecialHolidayApprovalReportModel;
import com.genius.payhrms.activity.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpecialHolidayApprovalReportFragment extends Fragment {
    View view;
    Pref pref;
    LinearLayout llStrtDate,llEndDate;
    String startDate,endDate;
    TextView tvStrtDate,tvEndDate;
    Button btnShow;
    ArrayList<SpecialHolidayApprovalReportModel> itemList=new ArrayList<>();
    LinearLayout llNoData,llLoader,llMain;
    AlertDialog alerDialog1;
    RecyclerView rvItem;
    SpecialHolidayApprovalReportAdapter spAdapter;
    Button btnSubmit;
    AlertDialog al1;
    TextView tvDate,tvRemarks,tvStatus,tvName;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_approval_report, container, false);
        initView();
        onClick();
        return view;
    }

    private void initView(){
        pref=new Pref(getContext());
        llStrtDate=(LinearLayout)view.findViewById(R.id.llStrtDate);
        llEndDate=(LinearLayout)view.findViewById(R.id.llEndDate);

        tvStrtDate=(TextView) view.findViewById(R.id.tvStrtDate);
        tvEndDate=(TextView) view.findViewById(R.id.tvEndDate);
        tvDate=(TextView) view.findViewById(R.id.tvDate);
        tvRemarks=(TextView) view.findViewById(R.id.tvRemarks);
        tvStatus=(TextView) view.findViewById(R.id.tvStatus);
        tvName=(TextView)view.findViewById(R.id.tvName);

        btnShow=(Button) view.findViewById(R.id.btnShow);
        rvItem=(RecyclerView)view.findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);
        llNoData=(LinearLayout)view.findViewById(R.id.llNoData);
        llLoader=(LinearLayout)view.findViewById(R.id.llLoader);
        llMain=(LinearLayout)view.findViewById(R.id.llMain);
        btnSubmit=(Button)view.findViewById(R.id.btnSubmit);

        if (pref.getLanguage().equals("hi")){
            tvStrtDate.setText("कृपया प्रारंभ दिन चुनें");
            tvEndDate.setText("कृपया अंतिम दिन चुनें");
            tvDate.setText("दिनांक");
            tvRemarks.setText("टिप्पणियों");
            tvStatus.setText("स्थिति");
            tvName.setText("स्थिति");
            btnShow.setText("नाम");
        }else {
            tvStrtDate.setText("Please select start day");
            tvEndDate.setText("Please select end day");
            tvDate.setText("Date");
            tvRemarks.setText("Remarks");
            tvStatus.setText("Status");
            tvName.setText("Name");
            btnShow.setText("Show");
        }
    }


    private void setAdapter(){
        spAdapter=new SpecialHolidayApprovalReportAdapter(itemList,getContext());
        rvItem.setAdapter(spAdapter);
    }

    private void showStrtDatePicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {



                        int month = (monthOfYear + 1);
                        startDate = month + "/" + dayOfMonth + "/" + year;
                        tvStrtDate.setText(startDate);

                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.show();

    }


    private void showEndDatePicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {



                        int month = (monthOfYear + 1);
                        endDate = month + "/" + dayOfMonth + "/" + year;
                        tvEndDate.setText(endDate);

                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.show();

    }

    private void onClick(){
        llStrtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStrtDatePicker();
            }
        });

        llEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEndDatePicker();
            }
        });
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!startDate.equals("")) {
                    if (!endDate.equals("")) {
                        //getItem();
                    }else {
                        Toast.makeText(getContext(),"Please select End Date",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getContext(),"please select Start Date",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}
