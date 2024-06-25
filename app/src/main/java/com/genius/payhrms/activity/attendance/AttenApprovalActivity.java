package com.genius.payhrms.activity.attendance;

import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
import com.genius.payhrms.activity.adapter.AttendanceApprovalForPPSAdapter;
import com.genius.payhrms.activity.model.AttendanceApprovalModule;
import com.genius.payhrms.activity.utility.NetworkConnectionCheck;
import com.genius.payhrms.activity.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;



public class AttenApprovalActivity extends AppCompatActivity {

    RecyclerView rvAttenApproval;
    ArrayList<AttendanceApprovalModule> attendabceInfiList = new ArrayList<>();

    ImageView imgBack, imgHome;
    LinearLayout llSearch;
    private AlertDialog alertDialog, alertDialog1, alertDialog2;

    ProgressBar progressBar;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    public static int mPageCount = 0;
    boolean mIsEndReached = false;
    private boolean loading = false;
    LinearLayoutManager layoutManager;
    LinearLayout llLoder;
    AttendanceApprovalForPPSAdapter attendanceAdapter;
    String year;
    int y;
    TextView tvYear;
    LinearLayout llMain;
    String month;
    TextView tvMonth;
    String AttendanceID;
    Pref pref;
    ArrayList<String> item = new ArrayList<>();
    Button btnApprove,btnReject;
    String attId;
    NetworkConnectionCheck connectionCheck;
    LinearLayout llReply;
    LinearLayout llNodata;
    LinearLayout llAgain;
    ImageView imgAgain;
    int flag=0;
    ImageView imgSearch;
    android.app.AlertDialog alerDialog1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atten_approval);
        mPageCount = 1;
        initialize();
        if (connectionCheck.isNetworkAvailable()) {
            //getAttendanceList();
        }else {
            connectionCheck.getNetworkActiveAlert().show();
        }
        onClick();
    }

    private void initialize() {
        pref = new Pref(AttenApprovalActivity.this);
        connectionCheck=new NetworkConnectionCheck(AttenApprovalActivity.this);
        rvAttenApproval = (RecyclerView) findViewById(R.id.rvAttendanceApprove);
        layoutManager
                = new LinearLayoutManager(AttenApprovalActivity.this, LinearLayoutManager.VERTICAL, false);
        rvAttenApproval.setLayoutManager(layoutManager);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        llSearch = (LinearLayout) findViewById(R.id.llSearch);
        llLoder = (LinearLayout) findViewById(R.id.llWLLoader);
        llMain = (LinearLayout) findViewById(R.id.llMain);
        llNodata=(LinearLayout)findViewById(R.id.llNodata);

        progressBar = (ProgressBar) findViewById(R.id.WLpagination_loader);
        rvAttenApproval.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                    if (!loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = true;


                           progressBar.setVisibility(View.VISIBLE);
                            if (!mIsEndReached) {
                                mPageCount = mPageCount + 1;
                                if (connectionCheck.isNetworkAvailable()) {

                                    //getAttendanceList();
                                }else {
                                    connectionCheck.getNetworkActiveAlert().show();
                                }
                                onClick();

                            }

                        }
                    }
                }
            }
        });
        setAdapter();
        y = Calendar.getInstance().get(Calendar.YEAR);
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

        btnApprove=(Button)findViewById(R.id.btnApprove);
        btnReject=(Button)findViewById(R.id.btnReject);
        llReply=(LinearLayout)findViewById(R.id.llReply);
        llAgain=(LinearLayout)findViewById(R.id.llAgain);
        imgAgain=(ImageView)findViewById(R.id.imgAgain);
        imgAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getAttendanceList();
            }
        });
        imgSearch=(ImageView)findViewById(R.id.imgSearch);

    }



    private void setAdapter() {
        attendanceAdapter = new AttendanceApprovalForPPSAdapter(attendabceInfiList, AttenApprovalActivity.this);
        rvAttenApproval.setAdapter(attendanceAdapter);
    }

    public void updateAttendanceStatus(int position, boolean status) {
        attendabceInfiList.get(position).setSelected(status);
        if (attendabceInfiList.get(position).isSelected()==true) {
            item.add(attendabceInfiList.get(position).getAttId());
        }else {
            item.clear();
        }


        Log.d("arpan", item.toString());
        String i = item.toString();
        String d = i.replace("[", "").replace("]", "");
        attId = d.replaceAll("\\s+", "");
        Log.d("commas", attId);
        if(item.size()>0){
            llReply.setVisibility(View.VISIBLE);
        }else {
            llReply.setVisibility(View.GONE);
        }


        attendanceAdapter.notifyDataSetChanged();
    }


    private void onClick(){
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AttenApprovalActivity.this, UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AttenApprovalActivity.this, R.style.CustomDialogNew);
                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.attendancereportsearch, null);
                dialogBuilder.setView(dialogView);
                LinearLayout llYear=(LinearLayout)dialogView.findViewById(R.id.llYear);
                tvYear=(TextView) dialogView.findViewById(R.id.tvYear);
                tvMonth=(TextView)dialogView.findViewById(R.id.tvMonth);
                ImageView imgCancel=(ImageView)dialogView.findViewById(R.id.imgCancel);

                llYear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showYearDialog();
                    }
                });

                tvYear.setText(year);
                LinearLayout llMonth=(LinearLayout)dialogView.findViewById(R.id.llMonth);
                llMonth.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showMonthDialog();

                    }
                });
                tvMonth.setText(month);

                Button btnSubmit=(Button)dialogView.findViewById(R.id.btnSubmit);
                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPageCount=1;
                        attendabceInfiList.clear();
                        flag=0;
                        llLoder.setVisibility(View.VISIBLE);
                        llMain.setVisibility(View.GONE);
                        //getAttendanceList();
                        alertDialog.dismiss();
                    }
                });
                imgCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog = dialogBuilder.create();
                alertDialog.setCancelable(true);
                Window window = alertDialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.CENTER);
                alertDialog.show();

            }
        });

        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectionCheck.isNetworkAvailable()){
                    if (item.size()>0) {
                        acceptAttendance();
                        mPageCount=1;



                    }else {
                        Toast.makeText(AttenApprovalActivity.this,"Please select atleast one value",Toast.LENGTH_LONG).show();
                    }
                }else {
                    connectionCheck.getNetworkActiveAlert().show();
                }


            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.size()>0) {
                    rejectAttendance();
                    mPageCount=1;



                }else {
                    Toast.makeText(AttenApprovalActivity.this,"Please select atleast one value",Toast.LENGTH_LONG).show();

                }


            }
        });


    }


    private void showYearDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AttenApprovalActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_year, null);
        dialogBuilder.setView(dialogView);
        final TextView tvYear1=(TextView)dialogView.findViewById(R.id.tvYear1);
        final TextView tvYear2=(TextView)dialogView.findViewById(R.id.tvYear2);
        final TextView tvYear3=(TextView)dialogView.findViewById(R.id.tvYear3);
        LinearLayout llY1=(LinearLayout)dialogView.findViewById(R.id.llY1);
        LinearLayout llY2=(LinearLayout)dialogView.findViewById(R.id.llY2);
        LinearLayout llY3=(LinearLayout)dialogView.findViewById(R.id.llY3);

        int pastx1=y-2;
        String pasty1=String.valueOf(pastx1);
        tvYear1.setText(pasty1);

        int pastx2=y-1;
        String pasty2=String.valueOf(pastx2);
        tvYear2.setText(pasty2);

        String pastx3=String.valueOf(y);
        tvYear3.setText(pastx3);

        ImageView imgCancel=(ImageView)dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog1.dismiss();


            }
        });


        llY3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year=tvYear3.getText().toString();
                Log.d("yrtrr",year);
                tvYear.setText(year);
                alertDialog1.dismiss();

            }
        });

        llY2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year=tvYear2.getText().toString();
                alertDialog1.dismiss();
                tvYear.setText(year);
                Log.d("ttt",year);
            }
        });

        llY1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year=tvYear1.getText().toString();
                alertDialog1.dismiss();
                tvYear.setText(year);
                Log.d("ttt",year);
            }
        });

        alertDialog1= dialogBuilder.create();
        alertDialog1.setCancelable(true);
        Window window = alertDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog1.show();
    }

    private void showMonthDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AttenApprovalActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_month, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llM1=(LinearLayout)dialogView.findViewById(R.id.llM1);
        LinearLayout llM2=(LinearLayout)dialogView.findViewById(R.id.llM2);
        LinearLayout llM3=(LinearLayout)dialogView.findViewById(R.id.llM3);
        LinearLayout llM4=(LinearLayout)dialogView.findViewById(R.id.llM4);
        LinearLayout llM5=(LinearLayout)dialogView.findViewById(R.id.llM5);
        LinearLayout llM6=(LinearLayout)dialogView.findViewById(R.id.llM6);
        LinearLayout llM7=(LinearLayout)dialogView.findViewById(R.id.llM7);
        LinearLayout llM8=(LinearLayout)dialogView.findViewById(R.id.llM8);
        LinearLayout llM9=(LinearLayout)dialogView.findViewById(R.id.llM9);
        LinearLayout llM10=(LinearLayout)dialogView.findViewById(R.id.llM10);
        LinearLayout llM11=(LinearLayout)dialogView.findViewById(R.id.llM111);
        LinearLayout llM112=(LinearLayout)dialogView.findViewById(R.id.llM12);

        final TextView tvJan=(TextView)dialogView.findViewById(R.id.tvJan);
        tvJan.setText("January");
        final TextView tvFeb=(TextView)dialogView.findViewById(R.id.tvFeb);
        final TextView tvMarch=(TextView)dialogView.findViewById(R.id.tvMarch);
        final TextView tvApril=(TextView)dialogView.findViewById(R.id.tvApril);
        final TextView tvMay=(TextView)dialogView.findViewById(R.id.tvMay);
        final TextView tvJune=(TextView)dialogView.findViewById(R.id.tvJune);
        final TextView tvJuly=(TextView)dialogView.findViewById(R.id.tvJuly);
        final TextView tvAugust=(TextView)dialogView.findViewById(R.id.tvAugust);
        final TextView tvSept=(TextView)dialogView.findViewById(R.id.tvSeptember);
        final TextView tvOct=(TextView)dialogView.findViewById(R.id.tvOct);
        final TextView tvNov=(TextView)dialogView.findViewById(R.id.tvNovember);
        final TextView tvDec=(TextView)dialogView.findViewById(R.id.tvDecember);

        llM1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month=tvJan.getText().toString();
                Log.d("monnn",month);
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month=tvFeb.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });

        llM3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month=tvMarch.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month=tvApril.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month=tvMay.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });

        llM6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month=tvJune.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month=tvJuly.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month=tvAugust.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month=tvSept.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month=tvOct.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month=tvNov.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM112.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month=tvDec.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        ImageView imgCancel=(ImageView)dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog2.dismiss();
            }
        });


        alertDialog2 = dialogBuilder.create();
        alertDialog2.setCancelable(true);
        Window window = alertDialog2.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog2.show();

    }

    private void  acceptAttendance(){
        String surl ="http://111.93.182.174/GeniusiOSApi/api/get_GCLSelfAttendanceWoLeave?AEMConsultantID="+pref.getEmpConId()+"&AEMClientID="+pref.getEmpClintId()+"&AEMClientOfficeID=0&AEMEmployeeID="+pref.getEmpId()+"&CurrentPage=1&AID=0&ApproverStatus=1&YearVal="+year+"&MonthName="+month+"&WorkingStatus=1&SecurityCode=123&DbOperation=4&AttIds="+attId;
        Log.d("accept",surl);
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLeave", response);
                        progressBar.dismiss();
                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText=job1.optString("responseText");
                            boolean responseStatus=job1.optBoolean("responseStatus");
                            if (responseStatus){


                                successAlert("Successfully Approved....");

                            }


                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AttenApprovalActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
                Toast.makeText(AttenApprovalActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert",error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(AttenApprovalActivity.this);
        requestQueue.add(stringRequest);

    }


    private void rejectAttendance(){
        String surl ="http://111.93.182.174/GeniusiOSApi/api/get_GCLSelfAttendanceWoLeave?AEMConsultantID="+pref.getEmpConId()+"&AEMClientID="+pref.getEmpClintId()+"&AEMClientOfficeID=0&AEMEmployeeID="+pref.getEmpId()+"&CurrentPage=1&AID=0&ApproverStatus=2&YearVal="+year+"&MonthName="+month+"&WorkingStatus=1&SecurityCode=123&DbOperation=4&AttIds="+attId;
        Log.d("rejecurl",surl);
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLeave", response);
                        progressBar.dismiss();
                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText=job1.optString("responseText");
                            boolean responseStatus=job1.optBoolean("responseStatus");
                            if (responseStatus){
                               // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                                 successAlert("Successfully Rejected....");

                            }


                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AttenApprovalActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
                Toast.makeText(AttenApprovalActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert",error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(AttenApprovalActivity.this);
        requestQueue.add(stringRequest);

    }

    private void successAlert(String tetxt) {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(AttenApprovalActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);

            tvInvalidDate.setText(tetxt);



        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                item.clear();
                attendabceInfiList.clear();
                //getAttendanceList();
            }
        });

        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }


}
