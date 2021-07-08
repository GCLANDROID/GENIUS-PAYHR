package com.genius.hrms.activity.dailyactivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.genius.hrms.R;
import com.genius.hrms.activity.activity.EmployeeDashBoardActivity;
import com.genius.hrms.activity.activity.UserDashBoardActivity;
import com.genius.hrms.activity.attendance.AttendanceReportActivity;
import com.genius.hrms.activity.utility.Pref;


public class DailyTaskDashBoardActivity extends AppCompatActivity {
    LinearLayout llManage,llReport,llLog;

    ImageView imgBack,imgHome;
    Pref pref;
    AlertDialog alertDialog;
    TextView tvToolBar,tvLogBook,tvReport,tvManage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_task_dash_board);
        initialize();
        onClick();
    }

    private void initialize(){

        pref=new Pref(getApplicationContext());
        llManage=(LinearLayout)findViewById(R.id.llManage);
        llReport=(LinearLayout)findViewById(R.id.llReport);


        llLog=(LinearLayout)findViewById(R.id.llLog);


        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);

        tvToolBar=(TextView)findViewById(R.id.tvToolBar);
        tvLogBook=(TextView)findViewById(R.id.tvLogBook);
        tvReport=(TextView)findViewById(R.id.tvReport);
        tvManage=(TextView)findViewById(R.id.tvManage);
        if (pref.getLanguage().equals("hi")){
            tvToolBar.setText("दैनिक गतिविधि");
            tvLogBook.setText("कार्यपंजी");
            tvReport.setText("रिपोर्ट");
            tvManage.setText("प्रबंधन");
        }else {
            tvToolBar.setText("Daily Activity");
            tvLogBook.setText("Logbook");
            tvReport.setText("Report");
            tvManage.setText("Manage");
        }
    }
    private void onClick(){
        llManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pref.getAttenFlag().equals("0")) {


                    Intent intent = new Intent(DailyTaskDashBoardActivity.this, DailyActivityManageActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    blockshowing();
               }
            }
        });

        llReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent=new Intent(DailyTaskDashBoardActivity.this, AttendanceReportActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        llLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent=new Intent(DailyTaskDashBoardActivity.this, DailyTaskReportActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DailyTaskDashBoardActivity.this, UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void blockshowing() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DailyTaskDashBoardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_blocked, null);
        dialogBuilder.setView(dialogView);
        TextView tvSuccess=(TextView)dialogView.findViewById(R.id.tvSuccess);
        tvSuccess.setText("Your daily activity has been blocked");


        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
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
}
