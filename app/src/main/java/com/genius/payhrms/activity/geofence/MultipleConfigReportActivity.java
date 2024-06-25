package com.genius.payhrms.activity.geofence;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.genius.payhrms.R;

import com.genius.payhrms.activity.adapter.MultipleConfigAdapter;
import com.genius.payhrms.activity.model.MulFenceConfigModel;
import com.genius.payhrms.activity.utility.Pref;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MultipleConfigReportActivity extends AppCompatActivity {


    LinearLayout llLoader, llMain, llNoData;
    RecyclerView rvItem;
    Pref pref;
    ArrayList<MulFenceConfigModel> itemList = new ArrayList<>();
    ImageView imgBack;
    ImageView imgHome;
    TextView tvToolBar;
    String tranLoc,transCretaed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_config_report);
        initView();
        //fenceget();
        onClick();
    }


    private void initView() {
        pref = new Pref(getApplicationContext());
        llLoader = (LinearLayout) findViewById(R.id.llLoader);
        llMain = (LinearLayout) findViewById(R.id.llMain);
        llNoData = (LinearLayout) findViewById(R.id.llNoData);
        rvItem = (RecyclerView) findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(MultipleConfigReportActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        tvToolBar=(TextView)findViewById(R.id.tvToolBar);
        if (pref.getLanguage().equals("hi")){
            tvToolBar.setText("कॉन्फ़िगरेशन रिपोर्ट");
        }else {
            tvToolBar.setText("configuration report");
        }
    }



    private void showAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Slow or No Internet connection");
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();

                    }
                });
        alertDialogBuilder.show();


    }

    private void onClick(){
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public String translateLocation(final int i){
        final Handler textViewHandler3 = new Handler();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                TranslateOptions options = TranslateOptions.newBuilder()
                        .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
                        .build();
                Translate translate = options.getService();
                final Translation translation =
                        translate.translate(itemList.get(i).getAddress(),
                                Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                textViewHandler3.post(new Runnable() {
                    @Override
                    public void run() {

                        Log.d("sssh", translation.getTranslatedText());
                        tranLoc = translation.getTranslatedText();




                    }
                });
                return null;
            }
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);

            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);

            }


        }.execute();



        return tranLoc;
    }

    public String tranlateCretaedOn(final int i){
        final Handler textViewHandler1 = new Handler();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                TranslateOptions options = TranslateOptions.newBuilder()
                        .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
                        .build();
                Translate translate = options.getService();
                final Translation translation =
                        translate.translate(itemList.get(i).getCraetdOn(),
                                Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                textViewHandler1.post(new Runnable() {
                    @Override
                    public void run() {

                        Log.d("sssh", translation.getTranslatedText());
                        transCretaed = translation.getTranslatedText();




                    }
                });
                return null;
            }
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);

            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                llLoader.setVisibility(View.GONE);
                llMain.setVisibility(View.VISIBLE);

            }


        }.execute();
        return transCretaed;
    }




}
