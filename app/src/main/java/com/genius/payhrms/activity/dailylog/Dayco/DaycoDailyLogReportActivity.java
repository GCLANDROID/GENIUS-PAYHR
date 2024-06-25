package com.genius.payhrms.activity.dailylog.Dayco;

import static com.genius.payhrms.activity.utility.Util.SECRET_KEY;
import static com.genius.payhrms.activity.utility.Util.encrypt;

import android.app.AlertDialog;
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
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.genius.payhrms.R;
import com.genius.payhrms.activity.activity.UserDashBoardActivity;
import com.genius.payhrms.activity.adapter.DaycoDailyLogAdapter;
import com.genius.payhrms.activity.model.OfflineDailyModel;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class DaycoDailyLogReportActivity extends AppCompatActivity {
    private static final String TAG = "DaycoDailyLogReportActi";
    RecyclerView rvReport;
    ArrayList<OfflineDailyModel> activityList = new ArrayList<>();
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
    ImageView imgSearch;
    TextView tvToolBar;
    String surl;

    String attDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_daily_log_report);
        Log.e(TAG, "onCreate: called");
        initialize();

        onClick();
    }


    private void initialize() {
        pref = new Pref(getApplicationContext());
        rvReport = (RecyclerView) findViewById(R.id.rvReport);
        layoutManager
                = new LinearLayoutManager(DaycoDailyLogReportActivity.this, LinearLayoutManager.VERTICAL, false);
        rvReport.setLayoutManager(layoutManager);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        llSearch = (LinearLayout) findViewById(R.id.llSearch);
        llSearch.setVisibility(View.GONE);
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
        attDate=getIntent().getStringExtra("attdate").replaceAll("\\s+", "-");

        JSONObject object=new JSONObject();
        try {
            object.put("AEMEmployeeID",pref.getEmpId());
            object.put("Year","0");
            object.put("Month","0");
            object.put("SecurityCode",pref.getSecurityCode());
            object.put("AttendanceDate",attDate);
            object.put("Operation","1");
            getItemList2(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        imgSearch=(ImageView)findViewById(R.id.imgSearch);
        tvToolBar=(TextView)findViewById(R.id.tvToolBar);
        if (pref.getLanguage().equals("hi")){
            tvToolBar.setText("लॉग दृश्य");
        }else {
            tvToolBar.setText("Log View");
        }
    }


    private void getItemList2(JSONObject object) {
        Log.e(TAG, "getItemList2: called");
        llLoder.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);
        llAgain.setVisibility(View.GONE);

        AndroidNetworking.post(Api.sGetOfflineDailyLogActivityDayco)
                .addJSONObjectBody(object)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "Dayco_Daily_Log: "+response.toString());
                        try {
                            JSONObject job1 = response;
                            int Response_Code = job1.optInt("Response_Code");
                            if (Response_Code == 101) {
                                //String responseData = job1.optString("Response_Data");
                                String responseData = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(responseData);
                                Log.e(TAG, "LENGTH: "+jsonArray.length());
                                if (jsonArray.length() > 0) {
                                    for (int i = 0; i <jsonArray.length(); i++) {
                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        String PunchIn = obj.optString("PunchIn");
                                        String PunchInTime = obj.optString("PunchInTime");
                                        String PunchOutTime = obj.optString("PunchOutTime");
                                        String AddressIN = obj.optString("AddressIN");
                                        String AddressOUT = obj.optString("AddressOUT");
                                        String RemarksIN = obj.optString("RemarksIN");
                                        String RemarksOUT = obj.optString("RemarksOUT");
                                        String imgUrlIN=obj.optString("imgUrlIN");
                                        String imgUrlOUT=obj.optString("imgUrlOUT");
                                        String CostCenterName=obj.optString("CostCenterName");
                                        String DocmentName=obj.optString("DocmentName");
                                        String WorkMode=obj.optString("WorkMode");
                                        String Client=obj.optString("Client");

                                        OfflineDailyModel obj2 = new OfflineDailyModel(PunchIn, PunchInTime, PunchOutTime, AddressIN, AddressOUT, RemarksIN, RemarksOUT,imgUrlIN,imgUrlOUT);
                                        obj2.setCostCenter(CostCenterName);
                                        obj2.setDocUrl(DocmentName);
                                        obj2.setWorkMode(WorkMode);
                                        obj2.setClientName(Client);
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
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "Dayco_Daily_Log_onError: "+anError);
                        llLoder.setVisibility(View.GONE);
                        llMain.setVisibility(View.GONE);
                        llNodata.setVisibility(View.GONE);
                        llAgain.setVisibility(View.VISIBLE);
                        if (anError.getErrorCode()==401){
                            JSONObject obj=new JSONObject();
                            try {
                                obj.put("MasterID",encrypt(pref.getMasterId(),SECRET_KEY));
                                obj.put("Password",encrypt(pref.getPassword(),SECRET_KEY));
                                obj.put("IMEI","0");
                                obj.put("DeviceID","0");
                                obj.put("DeviceType","A");
                                obj.put("SecurityCode",pref.getSecurityCode());
                                login(obj);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(DaycoDailyLogReportActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void login(JSONObject jsonObject) {
        final ProgressDialog pd = new ProgressDialog(DaycoDailyLogReportActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(Api.sLoginapi)
                .addJSONObjectBody(jsonObject)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        pd.dismiss();

                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                            JSONArray responseData = job1.optJSONArray("Response_Data");
                            for (int i = 0; i < responseData.length(); i++) {
                                JSONObject obj = responseData.optJSONObject(i);
                                String Genius_Access_Token=obj.optString("Genius_Access_Token");
                                pref.saveAccessToken(Genius_Access_Token);

                                // boolean _status = job1.getBoolean("status");
                                // do anything with response
                                JSONObject object=new JSONObject();
                                try {
                                    object.put("AEMEmployeeID",pref.getEmpId());
                                    object.put("Year","0");
                                    object.put("Month","0");
                                    object.put("SecurityCode",pref.getSecurityCode());
                                    object.put("AttendanceDate",attDate);
                                    object.put("Operation","1");
                                    getItemList2(object);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                    }
                });
    }

    private void setAdapter() {
        DaycoDailyLogAdapter dAdapter = new DaycoDailyLogAdapter(activityList, DaycoDailyLogReportActivity.this);
        rvReport.setAdapter(dAdapter);
    }


    private void showYearDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DaycoDailyLogReportActivity.this, R.style.CustomDialogNew);
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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DaycoDailyLogReportActivity.this, R.style.CustomDialogNew);
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
                Intent intent = new Intent(DaycoDailyLogReportActivity.this, UserDashBoardActivity.class);
                startActivity(intent);
                  finish();
            }
        });

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DaycoDailyLogReportActivity.this, R.style.CustomDialogNew);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.attendancereportsearch, null);
                dialogBuilder.setView(dialogView);
                LinearLayout llYear = (LinearLayout) dialogView.findViewById(R.id.llYear);
                tvYear = (TextView) dialogView.findViewById(R.id.tvYear);
                tvMonth = (TextView) dialogView.findViewById(R.id.tvMonth);
                ImageView imgCancel = (ImageView) dialogView.findViewById(R.id.imgCancel);
                TextView tvSelectYear=(TextView)dialogView.findViewById(R.id.tvSelectYear);
                TextView tvSelectMonth=(TextView)dialogView.findViewById(R.id.tvSelectMonth);
                Button btnSubmit = (Button) dialogView.findViewById(R.id.btnSubmit);
                if (pref.getLanguage().equals("hi")){
                    tvSelectYear.setText("वर्ष का चयन करें");
                    tvSelectMonth.setText("महीना चुनिए");
                    btnSubmit.setText("खोज");

                }else {
                    tvSelectYear.setText("Select year");
                    tvSelectMonth.setText("Select month");
                    btnSubmit.setText("Search");
                }

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


                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        activityList.clear();

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
}
