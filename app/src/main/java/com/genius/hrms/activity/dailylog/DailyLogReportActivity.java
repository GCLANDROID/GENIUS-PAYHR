package com.genius.hrms.activity.dailylog;

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
import com.genius.hrms.R;
import com.genius.hrms.activity.activity.EmployeeDashBoardActivity;
import com.genius.hrms.activity.activity.UserDashBoardActivity;
import com.genius.hrms.activity.adapter.DailyLogAdapter;
import com.genius.hrms.activity.model.DailyLogModel;
import com.genius.hrms.activity.utility.Pref;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class DailyLogReportActivity extends AppCompatActivity {
    RecyclerView rvReport;
    ArrayList<DailyLogModel> activityList = new ArrayList<>();
    ImageView imgBack, imgHome;
    LinearLayout llSearch;
    private AlertDialog alertDialog, alertDialog1, alertDialog2;
    LinearLayoutManager layoutManager;
    LinearLayout llLoder;
    String year;
    int y;
    TextView tvYear;
    LinearLayout llMain, llNodata;
    String month;
    TextView tvMonth;
    LinearLayout llAgain;
    ImageView imgAgain;
    Pref pref;
    String imgInUrl = "";
    String imgOutUrl = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_log_report);
        initialize();
        getItemList();
        onClick();
    }

    private void initialize() {
        pref = new Pref(getApplicationContext());
        rvReport = (RecyclerView) findViewById(R.id.rvReport);
        layoutManager
                = new LinearLayoutManager(DailyLogReportActivity.this, LinearLayoutManager.VERTICAL, false);
        rvReport.setLayoutManager(layoutManager);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        llSearch = (LinearLayout) findViewById(R.id.llSearch);
        llLoder = (LinearLayout) findViewById(R.id.llWLLoader);
        llMain = (LinearLayout) findViewById(R.id.llMain);
        llNodata = (LinearLayout) findViewById(R.id.llNodata);
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
    }

    private void getItemList() {
        Log.d("Arpan", "arpan");
        llLoder.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);
        llAgain.setVisibility(View.GONE);
        String surl = pref.getIpAddress()+"GHRMSApi/api/GCLDailyLogTatGY?AEMEmployeeID=" + pref.getEmpId() + "&Year=" + year + "&Month=" + month + "&SecurityCode=" + pref.getSecurityCode();
        Log.d("inputactivity", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);

                        // attendabceInfiList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = responseData.length() - 1; i >= 0; i--) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String PunchIn = obj.optString("PunchIn");
                                    String PunchInTime = obj.optString("PunchInTime");
                                    String PunchOutTime = obj.optString("PunchOutTime");
                                    String AddressIN = obj.optString("AddressIN");
                                    String AddressOUT = obj.optString("AddressOUT");
                                    imgInUrl = obj.optString("imgUrlIN");
                                    imgOutUrl = obj.optString("imgUrlOUT");
                                    Log.d("imgUrlOUT", imgOutUrl);
                                    String RemarksIN = obj.optString("RemarksIN");
                                    String RemarksOUT = obj.optString("RemarksOUT");

                                    DailyLogModel obj2 = new DailyLogModel(PunchIn, PunchInTime, PunchOutTime, AddressIN, AddressOUT, imgInUrl, imgOutUrl, RemarksIN, RemarksOUT);
                                    activityList.add(obj2);


                                }
                                setAdapter();
                                llLoder.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNodata.setVisibility(View.GONE);
                                llAgain.setVisibility(View.GONE);

                            } else {

                                llLoder.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNodata.setVisibility(View.VISIBLE);
                                llAgain.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();

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
        RequestQueue requestQueue = Volley.newRequestQueue(DailyLogReportActivity.this);
        requestQueue.add(stringRequest);
    }

    private void setAdapter() {
        DailyLogAdapter dAdapter = new DailyLogAdapter(activityList, DailyLogReportActivity.this);
        rvReport.setAdapter(dAdapter);
    }


    private void showYearDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DailyLogReportActivity.this, R.style.CustomDialogNew);
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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DailyLogReportActivity.this, R.style.CustomDialogNew);
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
                Intent intent = new Intent(DailyLogReportActivity.this, UserDashBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //  finish();
            }
        });

        llSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DailyLogReportActivity.this, R.style.CustomDialogNew);
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

                        activityList.clear();
                        getItemList();
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


    }

    public void openInBrowser(int postion) {

        String inImage="";
        inImage=activityList.get(postion).getInUrl();
        Uri uri = Uri.parse(inImage); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (!inImage.equals("")) {
            startActivity(intent);
        } else {

        }
    }

    public void openOutBrowser(int postion) {
        String outimage="";
        outimage=activityList.get(postion).getOutUrl();
        Uri uri = Uri.parse(outimage); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (!outimage.equals("")) {
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {

        }
    }
}
