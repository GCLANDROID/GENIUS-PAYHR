package com.genius.hrms.activity.attendance;

import android.app.AlertDialog;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.genius.hrms.activity.activity.UserDashBoardActivity;
import com.genius.hrms.activity.adapter.AttendanceAdapter;
import com.genius.hrms.activity.model.AttendanceModule;
import com.genius.hrms.activity.utility.NetworkConnectionCheck;
import com.genius.hrms.activity.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;


public class TeamAttendanceReportActivity extends AppCompatActivity {
    RecyclerView rvAttendanceReport;
    JSONArray attendabceInfiList;
    AttendanceModule attendanceModule;
    ImageView imgBack, imgHome;
    LinearLayout llSearch;
    private AlertDialog alertDialog, alertDialog1, alertDialog2;

    String[] spMonthList = {"-----select-----", "January", "February", "March", "April", "May", "June", "July", "March", "May", "June", "July", "August", "September", "October", "November", "December"};
    ProgressBar progressBar;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    public static int mPageCount = 0;
    boolean mIsEndReached = false;
    private boolean loading = false;
    LinearLayoutManager layoutManager;
    LinearLayout llLoder;
    AttendanceAdapter attendanceAdapter;
    String year;
    int y;
    TextView tvYear;
    LinearLayout llMain, llNodata;
    String month;
    TextView tvMonth;
    String AttendanceID;
    Pref pref;
    NetworkConnectionCheck connectionCheck;
    LinearLayout llAgain;
    ImageView imgAgain;
    String imgUrl = "";
    int flag;
    ImageView imgSearch;
    TextView tvToolBar,tvEmpName;
    String empID,empName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_report);
        mPageCount = 1;
        initialize();
        onClick();
        if (connectionCheck.isNetworkAvailable()) {
            if (pref.getSecurityCode().equals("1153")){
                getAttendanceListForSmartJL();
            }else {
                getAttendanceList();
            }

        } else {
            connectionCheck.getNetworkActiveAlert().show();
        }
    }

    private void initialize() {
        pref = new Pref(TeamAttendanceReportActivity.this);

        empID=getIntent().getStringExtra("empID");
        empName=getIntent().getStringExtra("empName");
        connectionCheck = new NetworkConnectionCheck(TeamAttendanceReportActivity.this);
        rvAttendanceReport = (RecyclerView) findViewById(R.id.rvAttendanceReport);
        layoutManager
                = new LinearLayoutManager(TeamAttendanceReportActivity.this, LinearLayoutManager.VERTICAL, false);
        rvAttendanceReport.setLayoutManager(layoutManager);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        llSearch = (LinearLayout) findViewById(R.id.llSearch);
        llLoder = (LinearLayout) findViewById(R.id.llWLLoader);
        llMain = (LinearLayout) findViewById(R.id.llMain);
        llNodata = (LinearLayout) findViewById(R.id.llNodata);
        progressBar = (ProgressBar) findViewById(R.id.WLpagination_loader);
        rvAttendanceReport.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

                            }

                        }
                    }
                }
            }
        });

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
        llAgain = (LinearLayout) findViewById(R.id.llAgain);
        imgAgain = (ImageView) findViewById(R.id.imgAgain);
        imgAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAttendanceList();
            }
        });
        imgSearch=(ImageView)findViewById(R.id.imgSearch);
        tvToolBar=(TextView)findViewById(R.id.tvToolBar);
        tvEmpName=(TextView) findViewById(R.id.tvEmpName);
        tvEmpName.setText(empName);
        if (pref.getLanguage().equals("hi")){
            tvToolBar.setText("उपस्थिति विवरण");
        }else {
            tvToolBar.setText("Team Attendance Report");
        }

    }

    private void getAttendanceList() {
        Log.d("Arpan", "arpan");
        llLoder.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);
        llAgain.setVisibility(View.GONE);
        String surl = pref.getIpAddress()+"GHRMSApi/api/get_EmployeeAttendanceReport?AEMConsultantID=" + pref.getEmpConId() + "&AEMClientID=" + pref.getEmpClintId() + "&AEMClientOfficeID=" + pref.getEmpClintOffId() + "&AEMEmployeeID=" + empID + "&CurrentPage=0&AID=1&ApproverStatus=4&YearVal=" + year + "&MonthName=" + month + "&WorkingStatus=1&DbOperation=1&SecurityCode="+pref.getSecurityCode();
        Log.d("input", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        loading = false;

                        // attendabceInfiList.clear();
                        attendabceInfiList=new JSONArray();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                attendabceInfiList=responseData;

                                attendanceAdapter = new AttendanceAdapter(TeamAttendanceReportActivity.this,attendabceInfiList,1);
                                rvAttendanceReport.setAdapter(attendanceAdapter);
                                llLoder.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNodata.setVisibility(View.GONE);
                                llAgain.setVisibility(View.GONE);

                            } else {

                                llLoder.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNodata.setVisibility(View.VISIBLE);
                                llAgain.setVisibility(View.GONE);
                                //Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoder.setVisibility(View.GONE);
                llMain.setVisibility(View.GONE);
                llNodata.setVisibility(View.GONE);
                llAgain.setVisibility(View.VISIBLE);

                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(TeamAttendanceReportActivity.this);
        requestQueue.add(stringRequest);
    }

    private void getAttendanceListForSmartJL() {
        Log.d("Arpan", "arpan");
        llLoder.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);
        llAgain.setVisibility(View.GONE);
        String surl = pref.getIpAddress()+"GHRMSApi/api/get_EmployeeAttendanceReportSmartJoules?AEMConsultantID=" + pref.getEmpConId() + "&AEMClientID=" + pref.getEmpClintId() + "&AEMClientOfficeID=" + pref.getEmpClintOffId() + "&AEMEmployeeID=" + empID+ "&CurrentPage=0&AID=1&ApproverStatus=4&YearVal=" + year + "&MonthName=" + month + "&WorkingStatus=1&DbOperation=1&SecurityCode="+pref.getSecurityCode();
        Log.d("input", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        loading = false;

                        // attendabceInfiList.clear();
                        attendabceInfiList=new JSONArray();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                attendabceInfiList=responseData;

                                attendanceAdapter = new AttendanceAdapter(TeamAttendanceReportActivity.this,attendabceInfiList,2);
                                rvAttendanceReport.setAdapter(attendanceAdapter);
                                llLoder.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNodata.setVisibility(View.GONE);
                                llAgain.setVisibility(View.GONE);

                            } else {

                                llLoder.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNodata.setVisibility(View.VISIBLE);
                                llAgain.setVisibility(View.GONE);
                                //Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoder.setVisibility(View.GONE);
                llMain.setVisibility(View.GONE);
                llNodata.setVisibility(View.GONE);
                llAgain.setVisibility(View.VISIBLE);

                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(TeamAttendanceReportActivity.this);
        requestQueue.add(stringRequest);
    }

    private void setAdapter() {

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
                Intent intent = new Intent(TeamAttendanceReportActivity.this, UserDashBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //  finish();
            }
        });

        llSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TeamAttendanceReportActivity.this, R.style.CustomDialogNew);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.attendancereportsearch, null);
                dialogBuilder.setView(dialogView);
                LinearLayout llYear = (LinearLayout) dialogView.findViewById(R.id.llYear);
                tvYear = (TextView) dialogView.findViewById(R.id.tvYear);
                tvMonth = (TextView) dialogView.findViewById(R.id.tvMonth);
                ImageView imgCancel = (ImageView) dialogView.findViewById(R.id.imgCancel);

                llYear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showYearDialog();
                    }
                });

                tvYear.setText(year);
                LinearLayout llMonth = (LinearLayout) dialogView.findViewById(R.id.llMonth);
                llMonth.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showMonthDialog();

                    }
                });
                tvMonth.setText(month);

                Button btnSubmit = (Button) dialogView.findViewById(R.id.btnSubmit);
                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPageCount = 1;

                        if (pref.getSecurityCode().equals("1153")){
                            getAttendanceListForSmartJL();
                        }else {
                            getAttendanceList();
                        }


                        alertDialog.dismiss();
                    }
                });
                imgCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                TextView tvSelectYear=(TextView)dialogView.findViewById(R.id.tvSelectYear);
                TextView tvSelectMonth=(TextView)dialogView.findViewById(R.id.tvSelectMonth);
                if (pref.getLanguage().equals("hi")){
                    tvSelectMonth.setText("महीना");
                    tvSelectYear.setText("साल");
                    btnSubmit.setText("खोज");
                }else {
                    tvSelectMonth.setText("Month");
                    tvSelectYear.setText("Year");
                    btnSubmit.setText("Search");
                }


                alertDialog = dialogBuilder.create();
                alertDialog.setCancelable(true);
                Window window = alertDialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.CENTER);
                alertDialog.show();
            }
        });


    }


    private void showYearDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TeamAttendanceReportActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_year, null);
        dialogBuilder.setView(dialogView);
        final TextView tvYear1 = (TextView) dialogView.findViewById(R.id.tvYear1);
        final TextView tvYear2 = (TextView) dialogView.findViewById(R.id.tvYear2);
        final TextView tvYear3 = (TextView) dialogView.findViewById(R.id.tvYear3);
        LinearLayout llY1 = (LinearLayout) dialogView.findViewById(R.id.llY1);
        LinearLayout llY2 = (LinearLayout) dialogView.findViewById(R.id.llY2);
        LinearLayout llY3 = (LinearLayout) dialogView.findViewById(R.id.llY3);

        int pastx1 = y - 2;
        String pasty1 = String.valueOf(pastx1);
        tvYear1.setText(pasty1);

        int pastx2 = y - 1;
        String pasty2 = String.valueOf(pastx2);
        tvYear2.setText(pasty2);

        String pastx3 = String.valueOf(y);
        tvYear3.setText(pastx3);

        ImageView imgCancel = (ImageView) dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog1.dismiss();


            }
        });


        llY3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year = tvYear3.getText().toString();
                Log.d("yrtrr", year);
                tvYear.setText(year);
                alertDialog1.dismiss();

            }
        });

        llY2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year = tvYear2.getText().toString();
                alertDialog1.dismiss();
                tvYear.setText(year);
                Log.d("ttt", year);
            }
        });

        llY1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year = tvYear1.getText().toString();
                alertDialog1.dismiss();
                tvYear.setText(year);
                Log.d("ttt", year);
            }
        });

        alertDialog1 = dialogBuilder.create();
        alertDialog1.setCancelable(true);
        Window window = alertDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog1.show();
    }

    private void showMonthDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TeamAttendanceReportActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_month, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llM1 = (LinearLayout) dialogView.findViewById(R.id.llM1);
        LinearLayout llM2 = (LinearLayout) dialogView.findViewById(R.id.llM2);
        LinearLayout llM3 = (LinearLayout) dialogView.findViewById(R.id.llM3);
        LinearLayout llM4 = (LinearLayout) dialogView.findViewById(R.id.llM4);
        LinearLayout llM5 = (LinearLayout) dialogView.findViewById(R.id.llM5);
        LinearLayout llM6 = (LinearLayout) dialogView.findViewById(R.id.llM6);
        LinearLayout llM7 = (LinearLayout) dialogView.findViewById(R.id.llM7);
        LinearLayout llM8 = (LinearLayout) dialogView.findViewById(R.id.llM8);
        LinearLayout llM9 = (LinearLayout) dialogView.findViewById(R.id.llM9);
        LinearLayout llM10 = (LinearLayout) dialogView.findViewById(R.id.llM10);
        LinearLayout llM11 = (LinearLayout) dialogView.findViewById(R.id.llM111);
        LinearLayout llM112 = (LinearLayout) dialogView.findViewById(R.id.llM12);

        final TextView tvJan = (TextView) dialogView.findViewById(R.id.tvJan);
        tvJan.setText("January");
        final TextView tvFeb = (TextView) dialogView.findViewById(R.id.tvFeb);
        final TextView tvMarch = (TextView) dialogView.findViewById(R.id.tvMarch);
        final TextView tvApril = (TextView) dialogView.findViewById(R.id.tvApril);
        final TextView tvMay = (TextView) dialogView.findViewById(R.id.tvMay);
        final TextView tvJune = (TextView) dialogView.findViewById(R.id.tvJune);
        final TextView tvJuly = (TextView) dialogView.findViewById(R.id.tvJuly);
        final TextView tvAugust = (TextView) dialogView.findViewById(R.id.tvAugust);
        final TextView tvSept = (TextView) dialogView.findViewById(R.id.tvSeptember);
        final TextView tvOct = (TextView) dialogView.findViewById(R.id.tvOct);
        final TextView tvNov = (TextView) dialogView.findViewById(R.id.tvNovember);
        final TextView tvDec = (TextView) dialogView.findViewById(R.id.tvDecember);

        llM1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvJan.getText().toString();
                Log.d("monnn", month);
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvFeb.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });

        llM3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvMarch.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvApril.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvMay.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });

        llM6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvJune.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvJuly.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvAugust.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvSept.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvOct.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvNov.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM112.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvDec.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        ImageView imgCancel = (ImageView) dialogView.findViewById(R.id.imgCancel);
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

    public void openBrowser() {
        Uri uri = Uri.parse(imgUrl); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (!imgUrl.equals("")) {
            startActivity(intent);
        } else {

        }
    }


}
