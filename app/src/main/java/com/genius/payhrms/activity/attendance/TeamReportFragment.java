package com.genius.payhrms.activity.attendance;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.genius.payhrms.R;
import com.genius.payhrms.activity.adapter.TeamReportAdapter;
import com.genius.payhrms.activity.adapter.TeampEmpAdapter;
import com.genius.payhrms.activity.model.TeamEmpModel;
import com.genius.payhrms.activity.model.TeamReportModel;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeamReportFragment extends Fragment {
    View v;

    RecyclerView rvItem;
    ArrayList<TeamReportModel> itemList=new ArrayList<>();
    LinearLayout llStrtDate,llEndDate;
    TextView tvStrtDate,tvEndDate;
    String startDate="",endDate="";
    Button btnShow;
    LinearLayout llNoData,llLoader,llMain;
    Pref pref;
    String securityCode;
    EditText etSearch;
    TeamReportAdapter detailsAdpater;
    TeampEmpAdapter teamAdapter;
    ArrayList<TeamEmpModel>teamList=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_team_report, container, false);
        initView();
        onClick();
        return v;
    }

    private void initView(){
        pref=new Pref(getContext());
        etSearch=(EditText)v.findViewById(R.id.etSearch);
        securityCode=pref.getSecurityCode();
        rvItem=(RecyclerView)v.findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);
        llEndDate=(LinearLayout)v.findViewById(R.id.llEndDate);
        llStrtDate=(LinearLayout)v.findViewById(R.id.llStrtDate);
        llNoData=(LinearLayout)v.findViewById(R.id.llNoData);
        llLoader=(LinearLayout)v.findViewById(R.id.llLoader);
        llMain=(LinearLayout)v.findViewById(R.id.llMain);

        tvStrtDate=(TextView)v.findViewById(R.id.tvStrtDate);
        tvEndDate=(TextView)v.findViewById(R.id.tvEndDate);
        btnShow=(Button)v.findViewById(R.id.btnShow);

        JSONObject object=new JSONObject();
        try {

            object.put("AEMEmployeeID",pref.getEmpId());
            object.put("CompanyID",pref.getEmpClintId());
            object.put("SecurityCode",pref.getSecurityCode());
            teamlist(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void onClick(){


        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // filter your list from your input
                filter(s.toString());
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });

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
                        startDate = year + "-" + month + "-" + dayOfMonth;
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
                        endDate =  year + "-" + month + "-" + dayOfMonth;
                        tvEndDate.setText(endDate);

                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.show();

    }

    void filter(String text){
        ArrayList<TeamEmpModel> temp = new ArrayList();
        for(TeamEmpModel d: teamList){

            if(d.getEmpName().toLowerCase().contains(text) || d.getEmpName().toUpperCase().contains(text)){
                temp.add(d);
            }
        }

        teamAdapter.updateList(temp);
    }

    private void teamlist(JSONObject jsonObject) {
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        AndroidNetworking.post(Api.sapprovercheckapi)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                            String responseData = job1.optString("Response_Data");
                            try {
                                JSONArray jsonArray=new JSONArray(responseData);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    final String Name = obj.optString("Name");
                                    String ApplicantID = obj.optString("ApplicantID");
                                    TeamEmpModel teamEmpModel=new TeamEmpModel();
                                    teamEmpModel.setEmpName(Name);
                                    teamEmpModel.setEmpID(ApplicantID);
                                    teamList.add(teamEmpModel);
                                }
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNoData.setVisibility(View.GONE);

                                teamAdapter=new TeampEmpAdapter(teamList,getContext());
                                rvItem.setAdapter(teamAdapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            // boolean _status = job1.getBoolean("status");
                            // do anything with response
                        }else {
                            llLoader.setVisibility(View.GONE);
                            llMain.setVisibility(View.GONE);
                            llNoData.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        llLoader.setVisibility(View.GONE);
                        llMain.setVisibility(View.GONE);
                        llNoData.setVisibility(View.VISIBLE);
                    }
                });
    }

}
