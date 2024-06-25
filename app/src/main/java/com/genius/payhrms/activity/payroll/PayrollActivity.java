package com.genius.payhrms.activity.payroll;

import static com.genius.payhrms.activity.utility.Util.SECRET_KEY;
import static com.genius.payhrms.activity.utility.Util.encrypt;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
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
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.genius.payhrms.R;
import com.genius.payhrms.activity.activity.UserDashBoardActivity;
import com.genius.payhrms.activity.activity.WebViewActivity;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;


public class PayrollActivity extends AppCompatActivity {
    private static final String TAG = "PayrollActivity";
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
        //accessChecking();
        JSONObject object=new JSONObject();
        try {
            object.put("CompanyID",pref.getEmpClintId());
            object.put("MenuItemName","Payroll");
            object.put("SecurityCode",pref.getSecurityCode());
            accessChecking2(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

            } else if (separated.length==3){
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

         if (pref.getLanguage().equals("hi")) {
            tvToolBar.setText("पेरोल");
            tvSalary.setText("वेतन");
            tvCtc.setText("सीटीसी");
            tvReim.setText("अदायगी");
        } else {
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
                //accessCheckingForSalary();
                JSONObject object = new JSONObject();
                try {
                    object.put("CompanyID", pref.getEmpClintId());
                    object.put("MenuItemName", "Payslip");
                    object.put("SecurityCode", pref.getSecurityCode());
                    accessCheckingForSalary2(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                //accessCheckingForCTC();
                JSONObject object = new JSONObject();
                try {
                    object.put("CompanyID", pref.getEmpClintId());
                    object.put("MenuItemName", "CurrentCTC");
                    object.put("SecurityCode", pref.getSecurityCode());
                    accessCheckingForCTC2(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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



    private void accessChecking2(JSONObject object) {
        Log.e(TAG, "accessChecking2: "+object.toString());
        /*String surl = pref.getIpAddress() + "ghrmsapi/api/MenuOnOff/Get?CompanyID="+pref.getEmpClintId()+"&MenuItemName=Payroll&SecurityCode="+pref.getSecurityCode();
        Log.d("salaryinput",surl);*/
        final ProgressDialog pd=new ProgressDialog(PayrollActivity.this);
        pd.setMessage("Loading");
        pd.show();
        pd.setCancelable(false);

        AndroidNetworking.post(Api.sGetMenuOnOff)
                .addJSONObjectBody(object)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "GET_MENU_ON_OFF_PAYROLL: "+response.toString());
                        pd.dismiss();
                        JSONObject job1 = response;
                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            JSONObject menuObj = job1.optJSONObject("Response_Data");
                            if (menuObj.optBoolean("MenuStatus")){
                                lnAccess.setVisibility(View.GONE);
                                lnNonAccess.setVisibility(View.VISIBLE);
                            } else {
                                lnAccess.setVisibility(View.VISIBLE);
                                lnNonAccess.setVisibility(View.GONE);
                            }
                        } else {
                            lnAccess.setVisibility(View.GONE);
                            lnNonAccess.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "GET_MENU_ON_OFF_onError: "+anError);
                        pd.dismiss();
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
                            lnAccess.setVisibility(View.VISIBLE);
                            lnNonAccess.setVisibility(View.GONE);
                        }
                    }
                });
    }


    private void login(JSONObject jsonObject) {
        final ProgressDialog pd = new ProgressDialog((Context) PayrollActivity.this);
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

                                JSONObject object=new JSONObject();
                                try {
                                    object.put("CompanyID",pref.getEmpClintId());
                                    object.put("MenuItemName","Payroll");
                                    object.put("SecurityCode",pref.getSecurityCode());
                                    accessChecking2(object);
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



    private void accessCheckingForCTC2(JSONObject object) {
        /*String surl = pref.getIpAddress() + "ghrmsapi/api/MenuOnOff/Get?CompanyID="+pref.getEmpClintId()+"&MenuItemName=CurrentCTC&SecurityCode="+pref.getSecurityCode();
        Log.d("salaryinput",surl);*/
        final ProgressDialog pd=new ProgressDialog(PayrollActivity.this);
        pd.setMessage("Loading");
        pd.show();
        pd.setCancelable(false);

        AndroidNetworking.post(Api.sGetMenuOnOff)
                .addJSONObjectBody(object)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "GET_MENU_ON_OFF_CTC: "+response.toString());
                        pd.dismiss();
                        JSONObject job1 = response;
                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            JSONObject menuObj = job1.optJSONObject("Response_Data");
                            if (menuObj.optBoolean("MenuStatus")){
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
                        } else {
                           //ERROR
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "GET_MENU_ON_OFF_onError: "+anError);
                        pd.dismiss();
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
                            Intent intent=new Intent(PayrollActivity.this, WebViewActivity.class);
                            intent.putExtra("imageurl",pref.getCTCURL());
                            intent.putExtra("month",".");
                            intent.putExtra("flag","CTC");
                            intent.putExtra("year",cuurentFinancialYear);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
    }



    private void accessCheckingForSalary2(JSONObject object) {
       /* String surl = pref.getIpAddress() + "ghrmsapi/api/MenuOnOff/Get?CompanyID="+pref.getEmpClintId()+"&MenuItemName=Payslip&SecurityCode="+pref.getSecurityCode();
        Log.d("salaryinput",surl);*/
        final ProgressDialog pd=new ProgressDialog(PayrollActivity.this);
        pd.setMessage("Loading");
        pd.show();
        pd.setCancelable(false);

        AndroidNetworking.post(Api.sGetMenuOnOff)
                .addJSONObjectBody(object)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "GET_MENU_ON_OFF_SALARY: "+response.toString());
                        pd.dismiss();
                        JSONObject job1 = response;
                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            JSONObject menuObj = job1.optJSONObject("Response_Data");
                            if (menuObj.optBoolean("MenuStatus")){
                                showAlert();
                            } else {
                                Intent intent=new Intent(PayrollActivity.this, SalaryActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        } else {
                            // ERROR
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "GET_MENU_ON_OFF_SALARY: "+anError);
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
                            Intent intent=new Intent(PayrollActivity.this, SalaryActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
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
