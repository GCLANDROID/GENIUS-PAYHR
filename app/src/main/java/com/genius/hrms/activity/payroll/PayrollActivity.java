package com.genius.hrms.activity.payroll;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genius.hrms.R;
import com.genius.hrms.activity.activity.EmployeeDashBoardActivity;
import com.genius.hrms.activity.activity.UserDashBoardActivity;
import com.genius.hrms.activity.activity.WebViewActivity;
import com.genius.hrms.activity.leaveapplication.LeaveDashboardActivity;
import com.genius.hrms.activity.utility.Pref;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;


public class PayrollActivity extends AppCompatActivity {
    LinearLayout llSalary,llCTC,llRem;
    ImageView imgBack,imgHome;
    Pref pref;

    String menu;
    String s1, s2, s3, s4, s5, s6;
    TextView tvRem;
    TextView tvToolBar,tvSalary,tvCtc,tvReim;
    LinearLayout llIT;
    int y;
    String year;
    String cuurentFinancialYear;

    LinearLayout lnNonAccess,lnAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payroll);
        initialize();
        accessChecking();
        onClick();

    }

    private  void initialize(){
        pref=new Pref(this);
        lnNonAccess=(LinearLayout)findViewById(R.id.lnNonAccess);
        lnAccess=(LinearLayout)findViewById(R.id.lnAccess);
        y = Calendar.getInstance().get(Calendar.YEAR);
        year = String.valueOf(y);
        int futureYear = y + 1;
        cuurentFinancialYear = year + "-" + futureYear;
        llSalary=(LinearLayout)findViewById(R.id.llSalary);
        llRem=(LinearLayout)findViewById(R.id.llRem);
        llCTC=(LinearLayout)findViewById(R.id.llCTC);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);

        llIT=(LinearLayout)findViewById(R.id.llIT);
        if (pref.getPayrollFlag().equals("")){
            llCTC.setVisibility(View.VISIBLE);
            llSalary.setVisibility(View.VISIBLE);
            llIT.setVisibility(View.VISIBLE);
        }else {
            menu=pref.getPayrollFlag();
            String d = menu.replace("{", "").replace("}", "");
            Log.d("split", d);
            Log.d("menuu", menu);
            String[] separated = menu.split(",");
            if (separated.length == 1) {

                s1 = separated[0];
                Log.d("arpan", "riku");
                if (s1.equals("1")) {
                    llCTC.setVisibility(View.GONE);
                } else if (s1.equals("2")) {
                    llSalary.setVisibility(View.GONE);
                } else if (s1.equals("3")) {
                    llRem.setVisibility(View.GONE);
                }else if (s1.equals("4")) {
                    llIT.setVisibility(View.GONE);
                }

            }else if (separated.length==2){
                s1 = separated[0];
                s2=separated[1];
                Log.d("arpan", "riku");
                if (s1.equals("1")) {
                    llCTC.setVisibility(View.GONE);
                } else if (s1.equals("2")) {
                    llSalary.setVisibility(View.GONE);
                } else if (s1.equals("3")) {
                    llRem.setVisibility(View.GONE);
                }else if (s1.equals("4")) {
                    llIT.setVisibility(View.GONE);
                }

                if (s2.equals("1")) {
                    llCTC.setVisibility(View.GONE);
                } else if (s2.equals("2")) {
                    llSalary.setVisibility(View.GONE);
                } else if (s2.equals("3")) {
                    llRem.setVisibility(View.GONE);
                }else if (s2.equals("4")) {
                    llIT.setVisibility(View.GONE);
                }



            }else if (separated.length==3){
                s1 = separated[0];
                s2=separated[1];
                s3=separated[2];
                Log.d("arpan", "riku");
                if (s1.equals("1")) {
                    llCTC.setVisibility(View.GONE);
                } else if (s1.equals("2")) {
                    llSalary.setVisibility(View.GONE);
                } else if (s1.equals("3")) {
                    llRem.setVisibility(View.GONE);
                }else if (s1.equals("4")) {
                    llIT.setVisibility(View.GONE);
                }

                if (s2.equals("1")) {
                    llCTC.setVisibility(View.GONE);
                } else if (s2.equals("2")) {
                    llSalary.setVisibility(View.GONE);
                } else if (s2.equals("3")) {
                    llRem.setVisibility(View.GONE);
                }else if (s2.equals("4")) {
                    llIT.setVisibility(View.GONE);
                }

                if (s3.equals("1")) {
                    llCTC.setVisibility(View.GONE);
                } else if (s3.equals("2")) {
                    llSalary.setVisibility(View.GONE);
                } else if (s3.equals("3")) {
                    llRem.setVisibility(View.GONE);
                }else if (s3.equals("4")) {
                    llIT.setVisibility(View.GONE);
                }



            }
        }

        tvReim=(TextView)findViewById(R.id.tvReim);
        if (pref.getSecurityCode().equals("1020")){
            tvReim.setText("Interim Payment");
        }else {

        }
        tvToolBar=(TextView)findViewById(R.id.tvToolBar);
        tvSalary=(TextView)findViewById(R.id.tvSalary);
        tvCtc=(TextView)findViewById(R.id.tvCtc);

         if (pref.getLanguage().equals("hi")){
            tvToolBar.setText("पेरोल");
            tvSalary.setText("वेतन");
            tvCtc.setText("सीटीसी");
            tvReim.setText("अदायगी");
        }else {
            tvToolBar.setText("Payroll");
            tvSalary.setText("Salary");
            tvCtc.setText("CTC");
            tvReim.setText("Reimbursement");
        }

    }

    private  void onClick(){
        llSalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               accessCheckingForSalary();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent=new Intent(PayrollActivity.this, UserDashBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //finish();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        llCTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              accessCheckingForCTC();
            }
        });
        llRem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(PayrollActivity.this, ReimActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        llRem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(PayrollActivity.this, ReimActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        llIT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   openITBrowser();
            }
        });

    }


    private void openBrowser(){
        if (!pref.getCTCURL().equals("")) {
            Uri uri = Uri.parse(pref.getCTCURL()); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }


    private void openITBrowser(){
        if (!pref.getITView().equals("")) {
            Uri uri = Uri.parse(pref.getITView()); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    private void accessChecking() {
        String surl = pref.getIpAddress() + "ghrmsapi/api/MenuOnOff/Get?CompanyID="+pref.getEmpClintId()+"&MenuItemName=Payroll&SecurityCode="+pref.getSecurityCode();
        Log.d("salaryinput",surl);
        final ProgressDialog pd=new ProgressDialog(PayrollActivity.this);
        pd.setMessage("Loading");
        pd.show();
        pd.setCancelable(false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        pd.dismiss();


                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                lnAccess.setVisibility(View.GONE);
                                lnNonAccess.setVisibility(View.VISIBLE);

                            } else {

                                lnAccess.setVisibility(View.VISIBLE);
                                lnNonAccess.setVisibility(View.GONE);

                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();

                            //Toast.makeText(SalaryActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                lnAccess.setVisibility(View.VISIBLE);
                lnNonAccess.setVisibility(View.GONE);

                // Toast.makeText(SalaryActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(PayrollActivity.this);
        requestQueue.add(stringRequest);
    }

    private void accessCheckingForCTC() {
        String surl = pref.getIpAddress() + "ghrmsapi/api/MenuOnOff/Get?CompanyID="+pref.getEmpClintId()+"&MenuItemName=CurrentCTC&SecurityCode="+pref.getSecurityCode();
        Log.d("salaryinput",surl);
        final ProgressDialog pd=new ProgressDialog(PayrollActivity.this);
        pd.setMessage("Loading");
        pd.show();
        pd.setCancelable(false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        pd.dismiss();


                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                showAlert();
                            } else {

                                Intent intent=new Intent(PayrollActivity.this, WebViewActivity.class);
                                intent.putExtra("imageurl",pref.getCTCURL());
                                intent.putExtra("month",".");
                                intent.putExtra("flag","CTC");
                                intent.putExtra("year",cuurentFinancialYear);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();

                            //Toast.makeText(SalaryActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Intent intent=new Intent(PayrollActivity.this, WebViewActivity.class);
                intent.putExtra("imageurl",pref.getCTCURL());
                intent.putExtra("month",".");
                intent.putExtra("flag","CTC");
                intent.putExtra("year",cuurentFinancialYear);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                // Toast.makeText(SalaryActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(PayrollActivity.this);
        requestQueue.add(stringRequest);
    }

    private void accessCheckingForSalary() {
        String surl = pref.getIpAddress() + "ghrmsapi/api/MenuOnOff/Get?CompanyID="+pref.getEmpClintId()+"&MenuItemName=Payslip&SecurityCode="+pref.getSecurityCode();
        Log.d("salaryinput",surl);
        final ProgressDialog pd=new ProgressDialog(PayrollActivity.this);
        pd.setMessage("Loading");
        pd.show();
        pd.setCancelable(false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        pd.dismiss();


                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                showAlert();
                            } else {

                                Intent intent=new Intent(PayrollActivity.this, SalaryActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();

                            //Toast.makeText(SalaryActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Intent intent=new Intent(PayrollActivity.this, SalaryActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                // Toast.makeText(SalaryActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(PayrollActivity.this);
        requestQueue.add(stringRequest);
    }


    private void showAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Sorry ! Currently this menu is not accessible");
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                    }
                });
        alertDialogBuilder.show();


    }
}
