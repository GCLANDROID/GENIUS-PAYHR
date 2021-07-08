package com.genius.hrms.activity.dailylog;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.genius.hrms.R;
import com.genius.hrms.activity.activity.EmployeeDashBoardActivity;
import com.genius.hrms.activity.activity.UserDashBoardActivity;
import com.genius.hrms.activity.adapter.OfflineAttenAdapter;
import com.genius.hrms.activity.helper.DatabaseHelper;
import com.genius.hrms.activity.model.OfflineDailyLogModel;


import java.util.ArrayList;
import java.util.List;



public class OfflineAttenReportActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private List<OfflineDailyLogModel> saleList=new ArrayList<>();
    private ListView listViewNames;
    OfflineAttenAdapter offlineSaleAdapter;
    ImageView imgHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_sale_report);
        db = new DatabaseHelper(this);
        listViewNames = (ListView) findViewById(R.id.listViewNames);
        loadNames();
        ImageView imgBack=(ImageView)findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        imgHome=(ImageView) findViewById(R.id.imgHome);
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), UserDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void loadNames() {
        //names.clear();
        Cursor cursor = db.getNames();
        if (cursor.moveToFirst()) {
            do {
                OfflineDailyLogModel name = new OfflineDailyLogModel(
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ADDRESS)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_REMARKS))
                );
                saleList.add(name);
                Log.d("names",saleList.toString());
            } while (cursor.moveToNext());
        }

        offlineSaleAdapter = new OfflineAttenAdapter(this, R.layout.raw, saleList);
        listViewNames.setAdapter(offlineSaleAdapter);
    }


}
