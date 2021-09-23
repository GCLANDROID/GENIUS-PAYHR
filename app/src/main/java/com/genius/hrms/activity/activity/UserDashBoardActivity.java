package com.genius.hrms.activity.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genius.hrms.R;
import com.genius.hrms.activity.adapter.AttendanceAdapter;
import com.genius.hrms.activity.adapter.MenuItemAdapter;
import com.genius.hrms.activity.attendance.AttendanceManageActivity;
import com.genius.hrms.activity.attendance.AttendanceReportActivity;
import com.genius.hrms.activity.leaveapplication.LeaveApplicationDashboardActivity;
import com.genius.hrms.activity.model.AttendanceModule;
import com.genius.hrms.activity.model.MenuItemModel;
import com.genius.hrms.activity.utility.Pref;
import com.google.android.gms.common.data.DataHolder;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UserDashBoardActivity extends AppCompatActivity {
    LinearLayout llLoader,llMain,llNoConnection;
    RecyclerView rvItem;
    String ipAddress;
    Pref pref;
    ArrayList<MenuItemModel>itemList=new ArrayList<>();
    SwipeRefreshLayout swipeToRefresh;
    EditText etSearch;
    MenuItemAdapter attendanceAdapter;
    AlertDialog alert2;
    TextView tvGreeting, tvLoginDateTime, tvEmployeeName;
    AlertDialog alerDialog1,alert1;
    LinearLayout llUser;
    ImageView imgLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dash_board);
        initView();
        onClick();
    }

    private void initView(){
        pref=new Pref(UserDashBoardActivity.this);
        pref.setFirstTimeLaunch(true);

        llLoader=(LinearLayout)findViewById(R.id.llLoader);
        llMain=(LinearLayout)findViewById(R.id.llMain);
        llNoConnection=(LinearLayout)findViewById(R.id.llNoConnection);
        imgLogout=findViewById(R.id.imgLogout);

        rvItem=(RecyclerView)findViewById(R.id.rvItem);
        rvItem.setLayoutManager(new GridLayoutManager(this, 3));

        if (pref.getSecurityCode().equals("1080")){
            ipAddress="https://adityabirla.geniusconsultant.com/";

        }else {
            ipAddress="https://www.cloud.geniusconsultant.com/";
        }
        pref.saveIpAddress(ipAddress);
        swipeToRefresh=(SwipeRefreshLayout)findViewById(R.id.swipeToRefresh);
        etSearch=(EditText)findViewById(R.id.etSearch);
        tvGreeting = (TextView) findViewById(R.id.tvGreeting);
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) {
            if (pref.getLanguage().equals("hi")) {
                tvGreeting.setText("शुभ प्रभात");
            } else {
                tvGreeting.setText("Good Morning");
            }
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            if (pref.getLanguage().equals("hi")) {
                tvGreeting.setText("नमस्कार");
            } else {
                tvGreeting.setText("Good Afternoon");
            }
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            if (pref.getLanguage().equals("hi")) {
                tvGreeting.setText("सुसंध्या");
            } else {
                tvGreeting.setText("Good Evening");
            }

        } else if (timeOfDay >= 21 && timeOfDay < 24) {

            if (pref.getLanguage().equals("hi")) {
                tvGreeting.setText("सुसंध्या");
            } else {
                tvGreeting.setText("Good Evening");
            }
        }

        tvLoginDateTime = (TextView) findViewById(R.id.tvLoginDateTime);
        tvLoginDateTime.setText(pref.getloginTime());
        tvEmployeeName = (TextView) findViewById(R.id.tvEmployeeName);
        tvEmployeeName.setText(pref.getEmpName());
        if (pref.getMsgStatus()){
            msgAlert();
        }else {

        }
        if (pref.getSecurityCode().equals("1134")){
            msgAlertD();
        }else {
            getMenuList();
        }
        llUser=(LinearLayout)findViewById(R.id.llUser);
   }
    private void getMenuList() {
        Log.d("Arpan", "arpan");
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoConnection.setVisibility(View.GONE);
        String surl = "https://www.cloud.geniusconsultant.com/GHRMSApi/api/MenuItemList?SecurityCode="+pref.getSecurityCode();
        Log.d("inputMenu", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);

                          itemList.clear();
                        // attendabceInfiList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String MenuItemName = obj.optString("MenuItemName");
                                    int MenuItemId = obj.optInt("MenuItemId");

                                    MenuItemModel obj2 = new MenuItemModel(MenuItemName,MenuItemId);
                                    itemList.add(obj2);
                                }
                               // itemList.add(new MenuItemModel("Post Query",101));
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNoConnection.setVisibility(View.GONE);
                                setAdapter();

                            } else {
                                llLoader.setVisibility(View.VISIBLE);
                                llMain.setVisibility(View.GONE);
                                llNoConnection.setVisibility(View.GONE);
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
                llLoader.setVisibility(View.GONE);
                llMain.setVisibility(View.GONE);
                llNoConnection.setVisibility(View.VISIBLE);
                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(UserDashBoardActivity.this);
        requestQueue.add(stringRequest);
    }

    private void setAdapter() {
         attendanceAdapter = new MenuItemAdapter(itemList, UserDashBoardActivity.this);
        rvItem.setAdapter(attendanceAdapter);
    }

    private void onClick(){
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMenuList();
                swipeToRefresh.setRefreshing(false);
            }
        });
        llUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UserDashBoardActivity.this, LeaveApplicationDashboardActivity.class);
                startActivity(intent);
            }
        });
        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserDashBoardActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

    }
    public void shoeDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(UserDashBoardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.language_dialoge, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llEnglish = (LinearLayout) dialogView.findViewById(R.id.llEnglish);
        LinearLayout llHindi = (LinearLayout) dialogView.findViewById(R.id.llHindi);
        final ImageView imgGreyBridge=(ImageView)dialogView.findViewById(R.id.imgGreyBridge);
        final ImageView imgBlueBridge=(ImageView)dialogView.findViewById(R.id.imgBlueBridge);
        final ImageView imgGreyTajMahal=(ImageView)dialogView.findViewById(R.id.imgGreyTajMahal);
        final ImageView imgBlueTajMahal=(ImageView)dialogView.findViewById(R.id.imgBlueTajMahal);
        final TextView tvEnglish=(TextView)dialogView.findViewById(R.id.tvEnglish);
        final TextView tvHindi=(TextView)dialogView.findViewById(R.id.tvHindi);
        llHindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.saveLanguage("hi");
                alert2.dismiss();
                getMenuList();
                Calendar c = Calendar.getInstance();
                int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

                if (timeOfDay >= 0 && timeOfDay < 12) {
                        tvGreeting.setText("शुभ प्रभात");
                } else if (timeOfDay >= 12 && timeOfDay < 16) {
                        tvGreeting.setText("नमस्कार");
                } else if (timeOfDay >= 16 && timeOfDay < 21) {
                        tvGreeting.setText("सुसंध्या");
                } else if (timeOfDay >= 21 && timeOfDay < 24) {
                        tvGreeting.setText("सुसंध्या");
                }
                imgGreyBridge.setVisibility(View.VISIBLE);
                imgBlueBridge.setVisibility(View.GONE);
                imgBlueTajMahal.setVisibility(View.VISIBLE);
                imgGreyTajMahal.setVisibility(View.GONE);
                tvEnglish.setTextColor(Color.parseColor("#72128E"));
                tvHindi.setTextColor(Color.parseColor("#B1ACAC"));
            }
        });

        llEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pref.saveLanguage("en");
                alert2.dismiss();
                getMenuList();
                Calendar c = Calendar.getInstance();
                int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

                if (timeOfDay >= 0 && timeOfDay < 12) {
                        tvGreeting.setText("Good Morning");
                } else if (timeOfDay >= 12 && timeOfDay < 16) {
                        tvGreeting.setText("Good Afternoon");
                } else if (timeOfDay >= 16 && timeOfDay < 21) {
                        tvGreeting.setText("Good Evening");
                } else if (timeOfDay >= 21 && timeOfDay < 24) {
                        tvGreeting.setText("Good Evening");
                }

                imgGreyBridge.setVisibility(View.GONE);
                imgBlueBridge.setVisibility(View.VISIBLE);
                imgBlueTajMahal.setVisibility(View.GONE);
                imgGreyTajMahal.setVisibility(View.VISIBLE);

                tvEnglish.setTextColor(Color.parseColor("#B1ACAC"));
                tvHindi.setTextColor(Color.parseColor("#72128E"));
            }
        });
        if (pref.getLanguage().equals("hi")) {
            imgGreyBridge.setVisibility(View.VISIBLE);
            imgBlueBridge.setVisibility(View.GONE);
            imgBlueTajMahal.setVisibility(View.VISIBLE);
            imgGreyTajMahal.setVisibility(View.GONE);
            tvEnglish.setTextColor(Color.parseColor("#B1ACAC"));
            tvHindi.setTextColor(Color.parseColor("#72128E"));
        } else {
            imgGreyBridge.setVisibility(View.GONE);
            imgBlueBridge.setVisibility(View.VISIBLE);
            imgBlueTajMahal.setVisibility(View.GONE);
            imgGreyTajMahal.setVisibility(View.VISIBLE);
            tvEnglish.setTextColor(Color.parseColor("#72128E"));
            tvHindi.setTextColor(Color.parseColor("#B1ACAC"));
        }
        alert2 = dialogBuilder.create();
        alert2.setCancelable(false);
        Window window = alert2.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alert2.show();
    }
    private void msgAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(UserDashBoardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_message, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);
        tvInvalidDate.setText(pref.getMsg());
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
            }
        });
        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }

    private void msgAlertD() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(UserDashBoardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.msg_dialog, null);
        dialogBuilder.setView(dialogView);

        Button btn_next=(Button)dialogView.findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert1.dismiss();
                getMenuList();
            }
        });
        alert1 = dialogBuilder.create();
        alert1.setCancelable(true);
        Window window = alert1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER );
        alert1.show();
    }

}
